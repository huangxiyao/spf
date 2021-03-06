package com.hp.it.spf.wsrp.log;

import java.lang.ref.WeakReference;
import java.util.Hashtable;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.MessageContext;
import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import org.apache.axis.message.RPCParam;
import org.xml.sax.SAXException;
import com.hp.it.spf.xa.log.portal.TimeRecorder;
import com.hp.it.spf.xa.log.portal.Operation;
import com.hp.it.spf.xa.misc.Consts;
import com.hp.it.spf.xa.misc.portal.RequestContext;
import com.hp.it.spf.xa.dc.portal.ErrorCode;
import com.hp.it.spf.wsrp.misc.Predicates;
import com.hp.it.spf.wsrp.misc.Utils;
import com.vignette.portal.log.LogWrapper;

import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPBody;
import javax.servlet.http.HttpServletRequest;

import oasis.names.tc.wsrp.v2.types.NamedString;
import oasis.names.tc.wsrp.v2.types.GetMarkup;
import oasis.names.tc.wsrp.v2.types.PerformBlockingInteraction;
import oasis.names.tc.wsrp.v2.types.HandleEvents;
import oasis.names.tc.wsrp.v2.types.GetResource;

import org.apache.axis.transport.http.HTTPConstants;
import org.apache.log4j.MDC;

/**
 * A handler capturing WSRP execution time. This class relies on {@link TimeRecorder} to
 * perform this logging.
 * <p> This handler will also set, as HTTP header in the outgoing WSRP request, the diagnostic ID
 * which should allow end-to-end tracing of WSRP calls starting at the portal web tier, through
 * portal application tier down to the portlet tier. 
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
@SuppressWarnings("serial")
public class WsrpTimeRecordingHandler extends BasicHandler {
	private static final LogWrapper LOG = new LogWrapper(WsrpTimeRecordingHandler.class);

	/**
	 * Name of HttpServletRequest attribute holding the number of WSRP requests performed for
	 * this portal request.
	 */
	private static final String SPF_DC_PORTLET_REQ_COUNT = "SPF_DC_PORTLET_REQ_COUNT";
	private static final String SPF_DC_SAVED = "SPF_DC_ID_SAVED";

	public void invoke(MessageContext messageContext) throws AxisFault {
		if (!Predicates.isWsrpBaseCall(messageContext)) {
			return;
		}

		try {
			RequestContext requestContext = getRequestContext(messageContext);
			// RequestContext may be null for console requests
			if (requestContext != null) {
				if (!messageContext.getPastPivot()) {
					setDiagnosticId(messageContext);
					requestContext.getTimeRecorder().recordStart(Operation.WSRP_CALL,
							getPortletFriendlyId(messageContext));
				}
				else {
					requestContext.getTimeRecorder().recordEnd(Operation.WSRP_CALL);
				}
			}
		}
		catch (Exception e) {
			// Don't rethrow this exception as are in the logging code so the error is due to
			// logging infrastructure and probably does not impact proper application operation
			LOG.error("Error occured while recording time", e);
		}
		finally {
			if (messageContext.getPastPivot()) {
				clearDiagnosticId();
			}
		}
	}

	/**
	 * This method sets a portlet diagnostic Id in the request header.
	 * Portlet diagnostic id contains {sessionpart}+{requestpart}+{portletpart}.
	 *
	 * @param messageContext WSRP call message context
	 * @throws Exception If an error occurs when retrieving HttpServletRequest reference
	 */
	private void setDiagnosticId(MessageContext messageContext) throws Exception
	{
		HttpServletRequest request = Utils.retrieveRequest(messageContext);
		if (request == null) {
			throw new IllegalStateException("Cannot find request in the content of the messageContext");
		}

		//several threads may be accessing the request so let's ensure only one of them get it here.
		Integer portletRequestCount;
		synchronized (request) {
			portletRequestCount = (Integer) request.getAttribute(SPF_DC_PORTLET_REQ_COUNT);
			if (portletRequestCount != null) {
				portletRequestCount = Integer.valueOf((portletRequestCount.intValue() + 1) % 100);
			}else {
				portletRequestCount = Integer.valueOf(1);
			}
			request.setAttribute(SPF_DC_PORTLET_REQ_COUNT, portletRequestCount);
		}
		// Get the diagnostic id from request attribute. It was set in RequestLogFilter.
		String diagnosticId = com.hp.it.spf.xa.misc.Utils.getDiagnosticId(request);		
		String portletDiagnosticId = String.format("%s+%02d", diagnosticId, portletRequestCount);

		String currentDiagnosticId = (String) MDC.get(Consts.DIAGNOSTIC_ID);
		if (currentDiagnosticId != null) {
			MDC.put(SPF_DC_SAVED, currentDiagnosticId);
		}
		MDC.put(Consts.DIAGNOSTIC_ID, portletDiagnosticId);
		setHttpRequestHeader(messageContext, Consts.DIAGNOSTIC_ID, portletDiagnosticId);
	}

	/**
	 * This method sets the diagnostic Id in the request header.
	 * We are getting the REQUEST HEADERS from messageContext property, which returns hashtable of headers.
	 * creating a new hashtable if the headers are null, and add the header name and value to the hashtable 
	 * and set the messageContex property.
	 *
	 * @param messageContext WSRP call message context
	 * @param headerName name of HTTP header set in WSRP request
	 * @param headerValue value of HTTP header set in WSRP request
	 */	
	@SuppressWarnings("unchecked")
	private void setHttpRequestHeader(MessageContext messageContext, String headerName, String headerValue)
	{
		Hashtable headers = (Hashtable) messageContext.getProperty(HTTPConstants.REQUEST_HEADERS);
		if (headers == null) {
			headers = new Hashtable();			
		}
		headers.put(headerName, headerValue);
		messageContext.setProperty(HTTPConstants.REQUEST_HEADERS, headers);
	}

	private void clearDiagnosticId() {
		String previousDiagnosticId = (String) MDC.get(SPF_DC_SAVED);
		if (previousDiagnosticId != null) {
			MDC.remove(SPF_DC_SAVED);
			MDC.put(Consts.DIAGNOSTIC_ID, previousDiagnosticId);
		}
		else {
			MDC.remove(Consts.DIAGNOSTIC_ID);
		}
	}

	private String getPortletFriendlyId(MessageContext messageContext) throws AxisFault, SAXException, SOAPException
	{
		// Portlet friendly Id is stored in ClientAttributes structure which is only present in
		// WSRP V2
		if (! Predicates.isWsrpV2(messageContext)) {
			return null;
		}

		RPCParam rpcParam = Utils.getWsrpCallRPCParam(messageContext.getMessage());
		if (rpcParam != null) {
			Object value = rpcParam.getObjectValue();
			if (value != null) {
				NamedString[] clientAttributes = null;

				if (value instanceof GetMarkup) {
					clientAttributes = ((GetMarkup) value).getMarkupParams()
							.getClientData().getClientAttributes();
				} else if (value instanceof PerformBlockingInteraction) {
					clientAttributes = ((PerformBlockingInteraction) value).getMarkupParams()
							.getClientData().getClientAttributes();
				} else if (value instanceof HandleEvents) {
					clientAttributes = ((HandleEvents) value).getMarkupParams()
							.getClientData().getClientAttributes();
				} else if (value instanceof GetResource) {
					clientAttributes = ((GetResource) value).getResourceParams()
							.getClientData().getClientAttributes();
				}

				if (clientAttributes != null) {
					for (NamedString ns : clientAttributes) {
						if ("com.vignette.portal.portlet.friendlyid".equals(ns.getName())) {
							return ns.getValue();
						}
					}
				}
			} else {
				LOG.error("Parameter value of WSRP call is null");
			}
		} else {
			LOG.error("Unable to find WSRP RPCParam parameter");
		}

		return null;
	}

	@Override
	public void onFault(MessageContext messageContext) {
		if (!Predicates.isWsrpBaseCall(messageContext)) {
			return;
		}

		try {
			RequestContext requestContext = getRequestContext(messageContext);
			// RequestContext may be null for console requests
			if (requestContext != null) {
				SOAPFault fault = getFault(messageContext);
				String faultString = (fault != null ? fault.getFaultCode() + ":" + fault.getFaultString() : null);
				if (TimeRecorder.isOperationRecordingEnabled(Operation.WSRP_CALL)) {
					requestContext.getTimeRecorder().recordError(Operation.WSRP_CALL, faultString);
				}
				// save also the error information in the DiagnosticContext
				requestContext.getDiagnosticContext().setError(ErrorCode.WSRP001, faultString);
			}
		}
		catch (Exception e) {
			// Don't rethrow this exception as we are already in an error handler. Just log it.
			LOG.error("Error occurred while logging WSRP error", e);
		}
		finally {
			clearDiagnosticId();
		}
	}

	private SOAPFault getFault(MessageContext messageContext) throws SOAPException {
		Message responseMessage = messageContext.getResponseMessage();
		if (responseMessage != null) {
			SOAPBody body = responseMessage.getSOAPPart().getEnvelope().getBody();
			if (body != null && body.hasFault()) {
				return body.getFault();
			}
		}
		return null;
	}


	private RequestContext getRequestContext(MessageContext messageContext) throws Exception {
		final String REQUEST_CONTEXT_MC_KEY = TimeRecorder.class.getName();

		// Let's check first if we already have it in the context. This would be the case
		// when this method is called during web service response because it that case
		// we cannot retrieve the request as the required data is not available.

		// Axis MessageContext finalize prevents GC for Http Request/Session,
		// so use WeakReference to improve the memory usage and GC behavior.
		WeakReference<RequestContext> reference = (WeakReference<RequestContext>) messageContext.getProperty(REQUEST_CONTEXT_MC_KEY);
		RequestContext requestContext = null;
		if (reference != null) {
			requestContext = reference.get();
		}
		if (requestContext != null) {
			return requestContext;
		}

		HttpServletRequest request = Utils.retrieveRequest(messageContext);
		if (request == null) {
			throw new IllegalStateException("Cannot find request in the content of the messageContext");
		}
		requestContext = (RequestContext) request.getAttribute(RequestContext.REQUEST_KEY);

		// We are probably in the web service request now. Let's save the timeRecorder we got
		// from the portal request in the messageContext so it can be used when this handler
		// gets called for web service response.
		if (requestContext != null) {
			messageContext.setProperty(REQUEST_CONTEXT_MC_KEY, new WeakReference<RequestContext>(requestContext));
		}
		return requestContext;
	}

}
