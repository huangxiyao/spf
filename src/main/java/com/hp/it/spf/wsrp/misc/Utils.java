package com.hp.it.spf.wsrp.misc;

import org.apache.axis.MessageContext;
import org.apache.axis.AxisFault;
import org.apache.axis.message.RPCElement;
import org.apache.axis.message.RPCParam;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;

import com.hp.it.spf.wsrp.injector.context.portal.filter.RequestBindingFilter;
import com.hp.it.spf.xa.wsrp.portal.RequestMap;
import com.vignette.portal.log.LogWrapper;

import java.util.Iterator;
import java.util.Vector;

import oasis.names.tc.wsrp.v2.types.GetMarkup;
import oasis.names.tc.wsrp.v2.types.PerformBlockingInteraction;
import oasis.names.tc.wsrp.v2.types.GetResource;
import oasis.names.tc.wsrp.v2.types.HandleEvents;

/**
 * Utility class which provides methods to retrieve or manipulate information from Axis MessageContext.
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class Utils {
	private static final LogWrapper LOG = new LogWrapper(Utils.class);

	private Utils() {}

	/**
	 * Retrieves the portal request associdated with this web service call.
	 * This method works only if {@link com.hp.it.spf.wsrp.injector.context.portal.filter.RequestBindingFilter}
	 * has been applied to the request as this filter makes the request available to this method.
	 * Note that this method assumes that this is a WSRP call so the appropriate check should be done
	 * before the method is called. Otherwise the results are not predictable.
	 *
	 * @param messageContext web service message context.
	 * @return corresponding portal request or <tt>null</tt> if the request could not be found
	 * @throws Exception If an unexpected error occurs while processing the message context data
	 */
	public static HttpServletRequest retrieveRequest(MessageContext messageContext) throws Exception {
		String userAgentValue = findUserAgentValue(messageContext);
		if (userAgentValue != null) {
			int pos = userAgentValue.lastIndexOf(RequestBindingFilter.KEY_PREFIX);
			if (pos != -1) {
				// this extraction should be done somehow by RequestWrapper
				// as it's the only class that know how this was encoded
				String requestKey = userAgentValue.substring(pos
						+ RequestBindingFilter.KEY_PREFIX.length());
				return RequestMap.getInstance().get(requestKey);
			} else {
				LOG.error("SPF request key not found!");
			}
		} else {
			LOG.error("User-agent value not found!");
		}

		// we didn't find the request :-(
		return null;
	}


	/**
	 * Extracts from the SOAP message WSRP request or response object
	 *
	 * @param messageContext this web service call message context
	 * @return RPCParam containing the object
	 * @throws org.xml.sax.SAXException  If an error occurs when extracting request or reponse objects
	 * @throws javax.xml.soap.SOAPException If an error occurs when processing SOAP message
	 * @throws org.apache.axis.AxisFault	 If an unexpected error occurs
	 */
	public static RPCParam getWsrpCallRPCParam(MessageContext messageContext) throws SOAPException, SAXException, AxisFault {
		SOAPBody soapBody = messageContext.getMessage().getSOAPPart().getEnvelope().getBody();
		Iterator it = soapBody.getChildElements();
		if (!it.hasNext()) {
			throw new AxisFault("SOAPBody has no children: " + soapBody);
		}

		Object operation = it.next();
		if (!(operation instanceof RPCElement)) {
			throw new AxisFault("Operation is not instance of RPCElement: " + (operation == null ? "null" : operation.getClass().getName()));
		}

		RPCElement rpcElement = (RPCElement) operation;
		Vector params = rpcElement.getParams();
		if (params == null || params.size() != 1) {
			throw new AxisFault("Number of operation parameters is not 1: " + (params == null ? "null" : params.size()));
		}

		return (RPCParam) params.get(0);
	}


	/**
	 * Finds <code>userAgent</code> value and extracts the request key from it.
	 *
	 * @return request key or <code>null</code> if none could be found
	 * @throws Exception
	 *             If an error occurs when accessing the envelope occurs
	 */
	private static String findUserAgentValue(MessageContext messageContext) throws Exception {
		RPCParam rpcParam = getWsrpCallRPCParam(messageContext);
		if (rpcParam != null) {
			Object value = rpcParam.getObjectValue();
			if (value != null) {
				if (value instanceof GetMarkup) {
					return ((GetMarkup) value).getMarkupParams()
							.getClientData().getUserAgent();
				} else if (value instanceof PerformBlockingInteraction) {
					return ((PerformBlockingInteraction) value)
							.getMarkupParams().getClientData()
							.getUserAgent();
				} else if (value instanceof HandleEvents) {
					return ((HandleEvents) value).
							getMarkupParams().getClientData()
							.getUserAgent();
				} else if (value instanceof GetResource) {
					return ((GetResource) value).
							getResourceParams().getClientData()
							.getUserAgent();
				}
			} else {
				LOG.error("Parameter value of WSRP call is null");
			}
		} else {
			LOG.error("Unable to find WSRP RPCParam parameter");
		}

		return null;

//		SOAPMessage message = messageContext.getMessage();
//		SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
//
//		SOAPBody body = envelope.getBody();
//		Iterator it = body.getChildElements();
//		if (it.hasNext()) {
//			SOAPElement operation = (SOAPElement) it.next();
//			boolean isGetMarkup = "getMarkup".equals(operation.getElementName()
//					.getLocalName());
//			if (isGetMarkup
//					|| "performBlockingInteraction".equals(operation
//							.getElementName().getLocalName())) {
//				if (operation instanceof RPCElement) {
//					String operationName = isGetMarkup ? "getMarkup"
//							: "performBlockingInteraction";
//					RPCParam rpcParam = ((RPCElement) operation)
//							.getParam(operationName);
//					if (rpcParam != null) {
//						Object value = rpcParam.getObjectValue();
//						if (value != null) {
//							if (isGetMarkup && value instanceof GetMarkup) {
//								return ((GetMarkup) value).getMarkupParams()
//										.getClientData().getUserAgent();
//							} else if (value instanceof PerformBlockingInteraction) {
//								return ((PerformBlockingInteraction) value)
//										.getMarkupParams().getClientData()
//										.getUserAgent();
//							}
//						} else {
//							LOG.error("Parameter value of '" + operationName
//									+ "' is null");
//						}
//					} else {
//						LOG.error("Unable to find '" + operationName
//								+ "' parameter");
//					}
//				} else {
//					LOG.error("Operation is not of type 'RPCElement': "
//							+ operation.getClass().getName());
//				}
//			}
//		}
//		return null;
	}
}
