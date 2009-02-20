package com.hp.it.spf.wsrp.log;

import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.MessageContext;
import org.apache.axis.AxisFault;
import org.apache.axis.Message;
import com.hp.it.spf.xa.log.portal.TimeRecorder;
import com.hp.it.spf.xa.log.portal.Operation;
import com.hp.it.spf.xa.misc.portal.RequestContext;
import com.hp.it.spf.xa.dc.portal.ErrorCode;
import com.hp.it.spf.wsrp.misc.Predicates;
import com.hp.it.spf.wsrp.misc.Utils;
import com.vignette.portal.log.LogWrapper;

import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPBody;
import javax.servlet.http.HttpServletRequest;

/**
 * A handler capturing WSRP execution time. This class relies on {@link TimeRecorder} to
 * peform this logging.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class WsrpTimeRecordingHandler extends BasicHandler {
	private static final LogWrapper LOG = new LogWrapper(WsrpTimeRecordingHandler.class);


	public void invoke(MessageContext messageContext) throws AxisFault {
		if (!Predicates.isWsrpBaseCall(messageContext)) {
			return;
		}

		try {
			RequestContext requestContext = getRequestContext(messageContext);
			// RequestContext may be null for console requests
			if (requestContext != null) {
				if (!messageContext.getPastPivot()) {
					requestContext.getTimeRecorder().recordStart(Operation.WSRP_CALL);
				}
				else {
					requestContext.getTimeRecorder().recordEnd(Operation.WSRP_CALL);
				}
			}
		}
		catch (Exception e) {
			// Don't retrhow this exception as are in the loging code so the error is due to
			// logging infrastructure and probably does not impact proper application operation
			LOG.error("Error occured while recording time", e);
		}
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
			LOG.error("Error occured while logging WSRP error", e);
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
		RequestContext requestContext = (RequestContext) messageContext.getProperty(REQUEST_CONTEXT_MC_KEY);
		if (requestContext != null) {
			return requestContext;
		}

		HttpServletRequest request = Utils.retrieveRequest(messageContext);
		if (request == null) {
			LOG.error("Cannot find request in the content of messageContext");
		}
		requestContext = (RequestContext) request.getAttribute(RequestContext.REQUEST_KEY);

		// We are probably in the web service request now. Let's save the timeRecorder we got
		// from the portal request in the messageContext so it can be used when this handler
		// gets called for web service response.
		if (requestContext != null) {
			messageContext.setProperty(REQUEST_CONTEXT_MC_KEY, requestContext);
		}
		return requestContext;
	}

}
