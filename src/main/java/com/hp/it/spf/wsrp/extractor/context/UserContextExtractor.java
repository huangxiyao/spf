/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.wsrp.extractor.context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hp.it.spf.xa.misc.Consts;
import com.hp.it.spf.xa.wsrp.ProfileHelper;

/**
 * Extracts user context data from the appropriate SOAP header elements and make
 * them available to portlets as map in portlet request. <br/>
 * Vignette Portal does not allow sending different sets of profile attributes
 * for different sites, and the profile structure supported by Vignette Portal
 * must be predefined. <br/>
 * In SPF, this issue was addressed by injecting user profile information into
 * the WSRP SOAP request. The injection occurred in the consumer with another
 * project called wsrp-injector. On the producer side, this project can extract
 * the profile from WSRP SOAP request.
 * 
 * @author Oliver, Kaijian Ding, Ye Liu
 * @version 1.0
 * @see com.hp.it.spf.wsrp.injector.context.UserContextInjector
 */
public class UserContextExtractor implements SOAPHandler {
	/*
	 * UserContextKeys object's key value, use this key to retrieve the
	 * UserContextKeys object
	 */
	public static final String USER_CONTEXT_KEYS_KEY = Consts.PORTAL_CONTEXT_KEY;

	/*
	 * UserProfile object's key value, use this key to retrieve the UserProfile
	 * object
	 */
	public static final String USER_PROFILE_KEY = Consts.USER_PROFILE_KEY;

	/*
	 * the signature of the soap method that need to be handled
	 */
	public static final String[] SOAP_ACTION = {
			"\"urn:oasis:names:tc:wsrp:v1:getMarkup\"",
			"\"urn:oasis:names:tc:wsrp:v2:getMarkup\"",
			"\"urn:oasis:names:tc:wsrp:v1:performBlockingInteraction\"",
			"\"urn:oasis:names:tc:wsrp:v2:performBlockingInteraction\"",
			"\"urn:oasis:names:tc:wsrp:v2:handleEvents\"",
			"\"urn:oasis:names:tc:wsrp:v2:getResource\"" };

	private static final Logger mLog = Logger
			.getLogger(UserContextExtractor.class);
	private static final Logger mTLog = Logger.getLogger("TIME."
			+ UserContextExtractor.class.getName());

	/*
	 * @see com.hp.it.spf.wsrp.extractor.profile.ProfileHelper
	 */
	public static final ProfileHelper PROFILE_HELPER = new ProfileHelper();

	/*
	 * @see javax.xml.ws.handler.soap.SOAPHandler#getHeaders()
	 */
	public Set getHeaders() {
		return null;
	}

	/**
	 * Handle inbound message, if the soap action need to be handle, then
	 * extract user profile information from soap message's head
	 * 
	 * @param mc
	 *            message context
	 * @return true/false
	 */
	public boolean handleMessage(MessageContext mc) {
		if (mc instanceof SOAPMessageContext) {
			if (isRequest(mc) && isNeedHandledSOAPAction(mc)) {
				invoke((SOAPMessageContext) mc);
			}
		}
		return true;
	}

	/**
	 * Handle fault if any error occurs
	 * 
	 * @param mc
	 *            message context
	 * @return true/false
	 */
	public boolean handleFault(MessageContext mc) {
		return true;
	}

	/*
	 * @see
	 * javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
	 */
	public void close(MessageContext mc) {

	}

	/**
	 * Extracts the context data and make it available to portlets.
	 * 
	 * @param messageContext
	 *            current message context
	 */
	public void invoke(SOAPMessageContext messageContext) {

		long start = 0;
		if (mTLog.isDebugEnabled()) {
			start = System.currentTimeMillis();
		}
		Map userContextKeys = null;
		Map userProfile = null;
		try {
			Map userContext = extractUserContext(messageContext.getMessage()
					.getSOAPPart().getEnvelope());
			if (userContext != null) {
				userContextKeys = (Map) userContext.get(USER_CONTEXT_KEYS_KEY);

				userProfile = (Map) userContext.get(USER_PROFILE_KEY);
				// convert last change date and last login date to
				// java.util.Date type
				if (userProfile != null) {
					DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
					String lastChangeDateString = (String) userProfile
							.get(Consts.KEY_LAST_CHANGE_DATE);
					if ((lastChangeDateString != null)
							&& !("".equals(lastChangeDateString.trim()))) {
						try {
							userProfile.put(Consts.KEY_LAST_CHANGE_DATE, format
									.parse(lastChangeDateString));
						} catch (ParseException e) {
							userProfile.put(Consts.KEY_LAST_CHANGE_DATE, null);
						}
					} else {
						userProfile.put(Consts.KEY_LAST_CHANGE_DATE, null);
					}
					String lastLoginDateString = (String) userProfile
							.get(Consts.KEY_LAST_LOGIN_DATE);
					if ((lastLoginDateString != null)
							&& !("".equals(lastLoginDateString.trim()))) {
						try {
							userProfile.put(Consts.KEY_LAST_LOGIN_DATE, format
									.parse(lastLoginDateString));
						} catch (ParseException e) {
							userProfile.put(Consts.KEY_LAST_LOGIN_DATE, null);
						}
					} else {
						userProfile.put(Consts.KEY_LAST_LOGIN_DATE, null);
					}
				}
			}

			// retrieve friendlyid and set it to userContextKey map
			NodeList nodes = messageContext.getMessage().getSOAPBody()
					.getElementsByTagName("clientAttributes");
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				boolean isFriendlyID = node.getAttributes()
						.getNamedItem("name").getNodeValue().equals(
								Consts.KEY_PORTAL_PORTLET_ID);
				if (isFriendlyID) {
					String value = node.getFirstChild().getTextContent();
					if (userContextKeys == null) {
						userContextKeys = new HashMap();
					}
					userContextKeys.put(Consts.KEY_PORTAL_PORTLET_ID, value);
					break;
				}
			}

			if (mLog.isDebugEnabled()) {
				mLog.debug("User context keys: " + userContextKeys);
				mLog.debug("User profile: " + userProfile);
			}

			makeContextDataAvailableToPortlets(messageContext, userContextKeys,
					userProfile);
		} catch (SOAPException e) {
			mLog.error("Error while retrieving context keys", e);
		}
		if (mTLog.isDebugEnabled()) {
			mTLog.debug("context keys extracted | "
					+ (System.currentTimeMillis() - start));
		}
	}

	/**
	 * Extract user profile information from soap
	 * 
	 * @param envelope
	 *            soap envelope
	 * @return user profile map
	 * @throws SOAPException
	 */
	private Map extractUserContext(SOAPEnvelope envelope) throws SOAPException {
		Name headerName = envelope.createName("UserContext", "spf",
				"http://www.hp.com/spf");
		SOAPHeader header = envelope.getHeader();
		Iterator it = header.getChildElements(headerName);
		if (it.hasNext()) {
			SOAPElement headerElement = (SOAPElement) it.next();
			if (headerElement.hasChildNodes()) {
				String userContextString = headerElement.getFirstChild()
						.getNodeValue();
				try {
					// FIXME this is a special way to unescape special character
					userContextString = PROFILE_HELPER
							.spfSpecialUnescape(userContextString);
					return PROFILE_HELPER.profileFromString(userContextString);
				} catch (IllegalArgumentException e) {
					mLog.error("Error parsing context", e);
				}
			}
		}
		// if you get here this means that we didn't find the node
		if (mLog.isDebugEnabled()) {
			mLog.debug("UserContext node not found in the header");
		}
		return null;
	}

	/**
	 * Puts the context data (keys and profile) into {@link HttpServletRequest}
	 * of the WSRP call making it available to portlets through the portlet
	 * request.
	 * 
	 * @param messageContext
	 *            message context used to access the request
	 * @param userContextKeys
	 *            context keys map to bind
	 * @param userProfile
	 *            user profile map to bind
	 */
	private void makeContextDataAvailableToPortlets(
			SOAPMessageContext messageContext, Map userContextKeys,
			Map userProfile) {
		HttpServletRequest request = (HttpServletRequest) messageContext
				.get(MessageContext.SERVLET_REQUEST);
		if (userContextKeys != null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("UserContextKeys bound to request attribute "
						+ USER_CONTEXT_KEYS_KEY);
			}
			request.setAttribute(USER_CONTEXT_KEYS_KEY, userContextKeys);
		} else {
			mLog.error("UserContextKeys not found");
		}
		if (userProfile != null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("UserProfile bound to request attribute "
						+ USER_PROFILE_KEY);
			}
			request.setAttribute(USER_PROFILE_KEY, userProfile);
		}
	}

	/**
	 * Check current request is inbound or outbound
	 * 
	 * @param mc
	 *            message context
	 * @return if the request is inbound, then return true
	 */
	private boolean isRequest(MessageContext mc) {
		return !((Boolean) mc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY))
				.booleanValue();
	}

	/**
	 * Check current soap action is need to be handled
	 * 
	 * @param mc
	 * @return true/false
	 */
	private boolean isNeedHandledSOAPAction(MessageContext mc) {
		try {
			// SOAP action list which should be handled
			List list = Arrays.asList(SOAP_ACTION);

			// retrieve the current soap action
			String currentActionURI = (String) mc
					.get("javax.xml.ws.soap.http.soapaction.uri");

			return list.contains(currentActionURI);

		} catch (Exception ex) {
			mLog.debug("Retrieve soap action error:" + ex.getMessage());
		}
		return false;
	}
}
