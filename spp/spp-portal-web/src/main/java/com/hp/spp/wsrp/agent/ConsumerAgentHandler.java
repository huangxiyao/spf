package com.hp.spp.wsrp.agent;

import java.util.Iterator;
import java.util.Vector;

import oasis.names.tc.wsrp.v1.types.RegistrationData;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.message.RPCElement;
import org.apache.axis.message.RPCParam;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPMessage;

/**
 * Rewrites the registration data by trimming the consumer agent name to 50 characters.
 * VAP 7.4 consumer agent value contains not only the portal name but also the version and build
 * number which makes the WSRP4J producer failing as its database column storing this data is
 * too small. 
 * 
 * @author Pranav Vaniawala (pranav.v@hp.com)
 *
 */
public class ConsumerAgentHandler extends BasicHandler {

	private static final Logger mLog = Logger.getLogger(ConsumerAgentHandler.class);

	/**
	 * Max size of the user agent which will be sent.
	 */
	static final int CONSUMER_AGENT_MAX_SIZE = 50;

	/**
	 * Position at which the user agent name will be split to include its tail.
	 */
	private static final int CONSUMER_AGENT_SPLIT_POS = 20;

	/**
	 * Invoked by Axis for each web service call - once for request and once for response. Invokes
	 * the rewrite action in-case of "register" SOAPAction. 
	 *
	 * @param messageContext web service message context
	 * @throws AxisFault If an error occurs when processing WSRP request or response.
	 */
	public void invoke(MessageContext messageContext) throws AxisFault {

		if(isRewriteRequired(messageContext)){			
			try {
				RPCParam rpcParam = getRPCParameter(messageContext.getMessage());
				if (rpcParam != null) {
					reWrite(rpcParam.getObjectValue());
				}else{
					mLog.debug("RPCParameter is null");
				}
			} catch (SAXException e) {
				throw new AxisFault("Error rewriting consumerAgent element", e);
			}
			catch (SOAPException e) {
				throw new AxisFault("Error rewriting consumerAgent element", e);
			}
		}
	}

	/**
	 * Rewrites user agent value including in the result its head and tail.
	 * @param data registration data
	 */
	private void reWrite(Object data) {
		if (!(data instanceof RegistrationData)) {
			throw new IllegalArgumentException("RegistrationData expected but was: " + 
					(data == null ? "null" : data.getClass().getName()));
		}

		RegistrationData registrationData = (RegistrationData) data;
		String consumerAgent = registrationData.getConsumerAgent();
		if (consumerAgent == null || consumerAgent.length() <= CONSUMER_AGENT_MAX_SIZE) {
			return;
		}

		// Vignette sends in consumer agent value a version and build number in the tail.
		// Let's keep the tail.
		registrationData.setConsumerAgent(
				consumerAgent.substring(0, CONSUMER_AGENT_SPLIT_POS) + 
				"..." + 
				consumerAgent.substring(consumerAgent.length() - (CONSUMER_AGENT_MAX_SIZE - CONSUMER_AGENT_SPLIT_POS - 3)));		
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
	private RPCParam getRPCParameter(SOAPMessage message) throws AxisFault, SAXException, SOAPException {

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
	 * The method determines if reWrite of the consumerAgent property required or not depending upon 
	 * the SOAPAction.
	 * 
	 * @param messageContext
	 * @return boolean value "true" - for register SOAPAction 
	 * 						 "false" - for all other SOAPAction
	 * 		
	 */
	private boolean isRewriteRequired(MessageContext messageContext) {
		String actionURI = messageContext.getSOAPActionURI();
		return "urn:oasis:names:tc:wsrp:v1:register".equals(actionURI);		
	}
}
