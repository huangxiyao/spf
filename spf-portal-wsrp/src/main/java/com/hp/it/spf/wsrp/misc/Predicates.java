package com.hp.it.spf.wsrp.misc;

import org.apache.axis.MessageContext;

/**
 * Helper class which can be used by {@link com.hp.it.spf.wsrp.rewriter.IRewriter} implementations in their
 * {@link com.hp.it.spf.wsrp.rewriter.IRewriter#shouldApply(org.apache.axis.MessageContext)}
 * method.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class Predicates {
	private Predicates() {
	}

	/**
	 * @param messageContext web service message context
	 * @return <tt>true</tt> if the web service call is WSRP (V1 or V2)
	 */
	public static boolean isWsrp(MessageContext messageContext) {
		String actionURI = messageContext.getSOAPActionURI();
		return actionURI != null && actionURI.startsWith("urn:oasis:names:tc:wsrp:");
	}

	/**
	 * @param messageContext web service message context
	 * @return <tt>true</tt> if the web service call is WSRP 2.0
	 */
	public static boolean isWsrpV2(MessageContext messageContext) {
		String actionURI = messageContext.getSOAPActionURI();
		return actionURI != null && actionURI.startsWith("urn:oasis:names:tc:wsrp:v2:");
	}

	/**
	 * @param messageContext web service message context
	 * @return <tt>true</tt> if the web service request is targeted to OpenPortal WSRP producer
	 */
	public static boolean isOpenPortalProducer(MessageContext messageContext) {
		String url = messageContext.getStrProp(MessageContext.TRANS_URL);
		return url != null && url.indexOf("/portletdriver/wsrp/router") != -1;
	}
	
	/**
	 * @param messageContext web service message context
	 * @return <tt>true</tt> if the web service request is targeted to WSRP4J producer
	 */
	public static boolean isWsrp4jProducer(MessageContext messageContext) {
		String url = messageContext.getStrProp(MessageContext.TRANS_URL);
		return url != null && url.indexOf("/WSRP4JProducer/") != -1;
	}

	/**
	 * @param messageContext web service message context
	 * @param methodNames	names of the WSRP methods this web service call is performed for
	 * @return <tt>true</tt> if the web service call is a call made to any of the specified methods
	 */
	public static boolean isAnyOfMethods(MessageContext messageContext, String... methodNames) {
		String actionURI = messageContext.getSOAPActionURI();
		if (actionURI == null) {
			return false;
		}
		for (String methodName : methodNames) {
			if (actionURI.endsWith(':' + methodName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param messageContext web service message context
	 * @return <tt>true</tt> if the web service call is a WSRP call (V1 or V2) to any of the
	 * following methods: getMarkup, performBlockingInteraction, handleEvents, getResource
	 */
	public static boolean isWsrpBaseCall(MessageContext messageContext) {
		return isWsrp(messageContext) &&
				isAnyOfMethods(messageContext,
						"getMarkup",
						"performBlockingInteraction",
						"handleEvents",
						"getResource");
	}
}
