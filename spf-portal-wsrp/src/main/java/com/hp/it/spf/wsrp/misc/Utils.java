package com.hp.it.spf.wsrp.misc;

import org.apache.axis.MessageContext;
import org.apache.axis.AxisFault;
import org.apache.axis.message.RPCElement;
import org.apache.axis.message.RPCParam;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;

import com.hp.it.spf.wsrp.injector.context.portal.filter.RequestBindingFilter;
import com.hp.it.spf.xa.wsrp.portal.RequestMap;
import com.vignette.portal.log.LogWrapper;

import java.util.Iterator;
import java.util.Vector;

/**
 * Utility class which provides methods to retrieve or manipulate information from Axis MessageContext.
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class Utils {
	private static final LogWrapper LOG = new LogWrapper(Utils.class);

	private Utils() {}

	/**
	 * Retrieves the portal request associated with this web service call.
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
		// For console access it seems that the user agent value can be retrieved only at request
		// handling time but not at response handling time. We know this method will be called 
		// during a request so let's store the request object in message context and have it
		// ready to use when this method is called at response time
		HttpServletRequest request = (HttpServletRequest) messageContext.getProperty(Utils.class.getName() + ".Request");
		if (request != null) {
			return request;
		}
		
		String userAgentValue = findUserAgentValue(messageContext.getRequestMessage());
		if (userAgentValue != null) {
			int pos = userAgentValue.lastIndexOf(RequestBindingFilter.KEY_PREFIX);
			if (pos != -1) {
				// this extraction should be done somehow by RequestWrapper
				// as it's the only class that know how this was encoded
				String requestKey = userAgentValue.substring(pos
						+ RequestBindingFilter.KEY_PREFIX.length());
				request = RequestMap.getInstance().get(requestKey);
				// store the request in message context so we can reuse it at response time
				messageContext.setProperty(Utils.class.getName() + ".Request", request);
				return request;
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
	 * Extracts from the SOAP message WSRP request or response object. 
 	 * When this method is called in a handler called within the request flow, the method returns
 	 * the request object. If this method is called in a handler within the response  flow, 
 	 * this method returns response object.
	 *
	 * @param messageContext this web service call message context
	 * @return RPCParam containing the object (request parameter or result)
	 * @throws org.xml.sax.SAXException  If an error occurs when extracting request or response objects
	 * @throws javax.xml.soap.SOAPException If an error occurs when processing SOAP message
	 * @throws org.apache.axis.AxisFault	 If an unexpected error occurs
	 */
	public static RPCParam getWsrpCallRPCParam(MessageContext messageContext) 
	throws SOAPException, SAXException, AxisFault 
	{
		return getWsrpCallRPCParam(messageContext.getMessage());
	}
	
	
	/**
	 * Extracts from the SOAP message WSRP object.
	 * If the object passed to the method is a request message, it will return the request 
	 * parameter object. If the object is the response method, it will return the service call
	 * result object.
	 * 
	 * @param message this web service call message 
	 * @return RPCParam containing the object (request parameter or result) or <tt>null</tt>
	 * if the response is a SOAPFault
	 * @throws org.xml.sax.SAXException  If an error occurs when extracting request or response objects
	 * @throws javax.xml.soap.SOAPException If an error occurs when processing SOAP message
	 * @throws org.apache.axis.AxisFault	 If an unexpected error occurs
	 */
	@SuppressWarnings("unchecked")
	public static RPCParam getWsrpCallRPCParam(SOAPMessage message) 
	throws SOAPException, SAXException, AxisFault 
	{
		SOAPBody soapBody = message.getSOAPPart().getEnvelope().getBody();
		Iterator it = soapBody.getChildElements();
		if (!it.hasNext()) {
			throw new AxisFault("SOAPBody has no children: " + soapBody);
		}

		Object operation = it.next();
		if (!(operation instanceof RPCElement)) {
			if (operation instanceof SOAPFault) {
				return null;
			}
			throw new AxisFault("Operation is not instance of RPCElement: " + 
					(operation == null ? "null" : operation.getClass().getName()));
		}

		RPCElement rpcElement = (RPCElement) operation;
		Vector params = rpcElement.getParams();
		if (params == null || params.size() != 1) {
			throw new AxisFault("Number of operation parameters is not 1: " + 
					(params == null ? "null" : params.size()));
		}

		return (RPCParam) params.get(0);
		
	}
	
	
	/**
	 * Checks whether the SOAP message contains a SOAP fault.
	 * 
	 * @param message SOAP message
	 * @return <tt>true</tt> if this SOAP message is a fault
	 * @throws SOAPException If an error occurs when processing SOAP message
	 */
	public static boolean isSOAPFault(SOAPMessage message) throws SOAPException {
		SOAPBody soapBody = message.getSOAPPart().getEnvelope().getBody();
		return soapBody.hasFault();
	}


	/**
	 * Finds <code>userAgent</code> value and extracts the request key from it.
	 *
	 * @return request key or <code>null</code> if none could be found
	 * @throws Exception
	 *             If an error occurs when accessing the envelope occurs
	 */
	private static String findUserAgentValue(SOAPMessage requestMessage) throws Exception {
		RPCParam rpcParam = getWsrpCallRPCParam(requestMessage);
		if (rpcParam != null) {
			Object value = rpcParam.getObjectValue();
			if (value != null) {
				if (value instanceof oasis.names.tc.wsrp.v2.types.GetMarkup) {
					return ((oasis.names.tc.wsrp.v2.types.GetMarkup) value).getMarkupParams()
							.getClientData().getUserAgent();
				} else if (value instanceof oasis.names.tc.wsrp.v2.types.PerformBlockingInteraction) {
					return ((oasis.names.tc.wsrp.v2.types.PerformBlockingInteraction) value)
							.getMarkupParams().getClientData()
							.getUserAgent();
				} else if (value instanceof oasis.names.tc.wsrp.v2.types.HandleEvents) {
					return ((oasis.names.tc.wsrp.v2.types.HandleEvents) value)
							.getMarkupParams().getClientData()
							.getUserAgent();
				} else if (value instanceof oasis.names.tc.wsrp.v2.types.GetResource) {
					return ((oasis.names.tc.wsrp.v2.types.GetResource) value)
							.getResourceParams().getClientData()
							.getUserAgent();
				} else if (value instanceof oasis.names.tc.wsrp.v1.types.GetMarkup) {
					return ((oasis.names.tc.wsrp.v1.types.GetMarkup) value)
							.getMarkupParams().getClientData()
							.getUserAgent();
				} else if (value instanceof oasis.names.tc.wsrp.v1.types.PerformBlockingInteraction) {
					return ((oasis.names.tc.wsrp.v1.types.PerformBlockingInteraction) value)
							.getMarkupParams().getClientData()
							.getUserAgent();
				}
			} else {
				LOG.error("Parameter value of WSRP call is null");
			}
		} else {
			LOG.error("Unable to find WSRP RPCParam parameter");
		}

		return null;
	}
}
