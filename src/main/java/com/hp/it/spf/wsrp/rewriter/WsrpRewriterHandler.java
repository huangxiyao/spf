package com.hp.it.spf.wsrp.rewriter;

import com.hp.it.spf.wsrp.rewriter.impl.ConfigModeUserIdRewriter;
import com.hp.it.spf.wsrp.rewriter.impl.UploadRewriter;
import com.hp.it.spf.wsrp.rewriter.impl.UrlTemplateProcessingRewriter;
import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.message.RPCElement;
import org.apache.axis.message.RPCParam;
import org.xml.sax.SAXException;

import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import java.util.Iterator;
import java.util.Vector;

/**
 * The handler performing changes to outgoing WSRP request or returned response. The goal of this
 * class is to bridge the gaps in interoperability between Vignette WSRP consumer and the WSRP
 * producers. The rewrite operations are defined by implmentations of {@link com.hp.it.spf.wsrp.rewriter.IRewriter}.
 * They are registered as either request or response rewriters.
 * <p/>
 * This class must be declated in Vignette's <tt>client.template</tt> file requestFlow and responseFlow
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class WsrpRewriterHandler extends BasicHandler {

	/**
	 * Contains the rewriters which will be invoked for WSRP requests.
	 */
	private static IRewriter[] REQUEST_REWRITERS = {
			new ConfigModeUserIdRewriter(),
			new UploadRewriter()
	};

	/**
	 * Contains rewrtiers which will be invoked for WSRP responses.
	 */
	private static IRewriter[] RESPONSE_REWRITERS = {
			new UrlTemplateProcessingRewriter()
	};


	/**
	 * Invoked by Axis for each web service call - once for request and once for response. Invokes
	 * registered {@link com.hp.it.spf.wsrp.rewriter.IRewriter} objects. This method returns
	 * immediately for web service calls which are not WSRP.
	 *
	 * @param messageContext web service message context
	 * @throws AxisFault If an error occurs when processing WSRP request or response.
	 */
	public void invoke(MessageContext messageContext) throws AxisFault {
		if (!Predicates.isWsrp(messageContext)) {
			return;
		}

		try {
			if (!messageContext.getPastPivot()) {
				invokeRewriters(REQUEST_REWRITERS, messageContext);
			}
			else {
				invokeRewriters(RESPONSE_REWRITERS, messageContext);
			}
		}
		catch (SAXException e) {
			throw new AxisFault("Error rewriting WSRP message", e);
		}
		catch (SOAPException e) {
			throw new AxisFault("Error rewriting WSRP message", e);
		}
	}

	/**
	 * Calls the given list of rewriters.
	 *
	 * @param rewriters	  rewiters to invoke
	 * @param messageContext this web service call message context
	 * @throws SAXException  If an error occurs when extracting request or reponse objects
	 * @throws SOAPException If an error occurs when processing SOAP message
	 * @throws AxisFault	 If an unexpected error occurs
	 */
	private void invokeRewriters(IRewriter[] rewriters, MessageContext messageContext) throws SAXException, SOAPException, AxisFault {
		RPCParam rpcParam = getWsrpCallRPCParam(messageContext);
		for (IRewriter rewriter : rewriters) {
			if (rewriter.shouldApply(messageContext)) {
				rewriter.rewrite(rpcParam.getObjectValue());
			}
		}
	}


	/**
	 * Extracts from the SOAP message WSRP request or response object
	 *
	 * @param messageContext this web service call message context
	 * @return RPCParam containing the object
	 * @throws SAXException  If an error occurs when extracting request or reponse objects
	 * @throws SOAPException If an error occurs when processing SOAP message
	 * @throws AxisFault	 If an unexpected error occurs
	 */
	private RPCParam getWsrpCallRPCParam(MessageContext messageContext) throws SOAPException, SAXException, AxisFault {
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

}
