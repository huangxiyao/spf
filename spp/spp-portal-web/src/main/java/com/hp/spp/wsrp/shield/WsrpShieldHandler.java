package com.hp.spp.wsrp.shield;

import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.MessageContext;
import org.apache.axis.AxisFault;
import org.apache.log4j.Logger;

/**
 * Axis handler which let's only the requests to the WSRP servers which are enabled based on
 * the information provided by {@link WsrpShield}
 */
public class WsrpShieldHandler extends BasicHandler {
	private static final Logger mLog = Logger.getLogger(WsrpShieldHandler.class);

	public void invoke(MessageContext messageContext) throws AxisFault {
		if (!isWsrpRequest(messageContext)) {
			return;
		}

		String url = getEndpointUrl(messageContext);
		if (mLog.isDebugEnabled()) {
			mLog.debug("WSRP endpoint URL: " + url);
		}
		if (!isRemoteServerEnabled(url)) {
			throw new RemoteServerDisabledException("WSRP server is disabled (" + url + "). You can enable it using wsrpAdmin.jsp page.");
		}
	}

	private boolean isRemoteServerEnabled(String url) {
		return WsrpShield.getInstance().isServerEnabled(url);
	}

	private String getEndpointUrl(MessageContext messageContext) {
		return (String) messageContext.getProperty(MessageContext.TRANS_URL);
	}

	private boolean isWsrpRequest(MessageContext messageContext) {
		// We don't want to handle the responses
		if (messageContext.getPastPivot()) {
			return false;
		}

		// We are only interested in WSRP requests
		String actionURI = messageContext.getSOAPActionURI();
		return actionURI != null && actionURI.startsWith("urn:oasis:names:tc:wsrp:v1");
	}
}
