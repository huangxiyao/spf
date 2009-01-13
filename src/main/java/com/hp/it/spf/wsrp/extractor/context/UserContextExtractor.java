package com.hp.it.spf.wsrp.extractor.context;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.Name;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.log4j.Logger;

import com.hp.it.spf.wsrp.extractor.profile.ProfileHelper;

/**
 * Extracts user context data from the appropriate SOAP header elements and make them available to portlets
 * as map in portlet request.
 * <br/>
 * Vignette Portal does not allow sending different sets of profile attributes for different sites, 
 * and the profile structure supported by Vignette Portal must be predefined.
 * <br/> 
 * In SPF, this issue was addressed by injecting user profile information into the WSRP SOAP request. 
 * The injection occurred in the consumer with another project called wsrp-injector. 
 * On the producer side, this project can extract the profile from WSRP SOAP request.
 * 
 * @author wuyingzh
 * @version 1.0
 * @see com.hp.it.spf.wsrp.injector.context.UserContextInjector
 */
public class UserContextExtractor implements SOAPHandler {
	/*
	 * UserContextKeys object's key value, use this key to retrieve the UserContextKeys object
	 */
	public static final String USER_CONTEXT_KEYS_KEY = "com.hp.spp.UserContextKeys";
	
	/*
	 * UserProfile object's key value, use this key to retrieve the UserProfile object
	 */
	public static final String USER_PROFILE_KEY = "com.hp.spp.UserProfile";
	
	/*
	 * the signature of the soap method that need to be handled
	 */
	public static final String[] SOAP_ACTION = {"\"urn:oasis:names:tc:wsrp:v1:getMarkup\"",
												"\"urn:oasis:names:tc:wsrp:v2:getMarkup\"",
												"\"urn:oasis:names:tc:wsrp:v1:performBlockingInteraction\"",
												"\"urn:oasis:names:tc:wsrp:v2:performBlockingInteraction\"",
												"\"urn:oasis:names:tc:wsrp:v2:handleEvents\"",
												"\"urn:oasis:names:tc:wsrp:v2:getResource\""
	                                            };

	private static final Logger mLog = Logger.getLogger(UserContextExtractor.class);
	private static final Logger mTLog = Logger.getLogger("TIME." + UserContextExtractor.class.getName());
	
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
	 * Handle inbound message, if the soap action need to be handle, then extractor user
	 * profile information from soap message's head
	 * 
	 * @param mc
	 * 			message context
	 * @return
	 * 	        true/false
	 */
	public boolean handleMessage(MessageContext mc) {
		if(mc instanceof SOAPMessageContext){
			if(isRequest(mc) && isNeedHandledSOAPAction(mc)){
				invoke((SOAPMessageContext)mc);
			}
		}
		return true;
	}

	/**
	 * Handle fault if any error occurs
	 * 
	 * @param mc
	 * 			message context
	 * @return
	 * 			true/false
	 */
	public boolean handleFault(MessageContext mc) {
		return true;
	}
	
	/*
	 * @see javax.xml.ws.handler.Handler#close(javax.xml.ws.handler.MessageContext)
	 */
	public void close(MessageContext mc) {
		
	}    
    
	/**
	 * Extracts the context data and make it available to portlets.
	 * @param messageContext 
	 * 				current message context
	 */
	public void invoke(SOAPMessageContext messageContext) {		
		
		long start = 0;
		if (mTLog.isDebugEnabled()) {
			start = System.currentTimeMillis();
		}
		Map userContextKeys = null;
		Map userProfile = null;
		try {
			Map userContext = extractUserContext(messageContext.getMessage().getSOAPPart().getEnvelope());
			if (userContext != null) {
				userContextKeys = (Map) userContext.get("com.hp.spp.UserContextKeys");
				userProfile = (Map) userContext.get("com.hp.spp.UserProfile");
			}
			else {
				// SPP continues to send data in SPP 2.0 compatibility mode
				userContextKeys = extractUserContextKeys(messageContext.getMessage().getSOAPPart().getEnvelope());
				userProfile = extractUserProfile(messageContext.getMessage().getSOAPPart().getEnvelope());
			}
			if (mLog.isDebugEnabled()) {
				mLog.debug("User context keys: " + userContextKeys);
				mLog.debug("User profile: " + userProfile);
			}
			
			makeContextDataAvailableToPortlets(messageContext, userContextKeys, userProfile);
		}
		catch (SOAPException e) {
			mLog.error("Error while retrieving context keys", e);			
		}
		if (mTLog.isDebugEnabled()) {
			mTLog.debug("context keys extracted | " + (System.currentTimeMillis()-start));
		}
	}

	/**
	 * Extract user profile information from soap
	 * 
	 * @param envelope
	 * 				soap envelope
	 * @return
	 * 				user profile map
	 * @throws SOAPException
	 */
	private Map extractUserContext(SOAPEnvelope envelope) throws SOAPException {
		Name headerName = envelope.createName("UserContext", "spp", "http://www.hp.com/spp");
		SOAPHeader header = envelope.getHeader();
		Iterator it = header.getChildElements(headerName);
		if (it.hasNext()) {
			SOAPElement headerElement = (SOAPElement) it.next();
			if (headerElement.hasChildNodes()) {
				String userContextString = headerElement.getFirstChild().getNodeValue();
				try {
					return PROFILE_HELPER.profileFromString(userContextString);
				}
				catch (IllegalArgumentException e) {
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
	 * Extracts user context keys from SOAP header and returns them as map. The map keys are the
	 * same as the element names within <code>UserContextKeys</code> tag.
	 * @param envelope parent envelope for the SOAP header
	 * @return map containing user context key (String) names and their values
	 * @throws SOAPException If an error occurs during envelope processing
	 */
	private Map extractUserContextKeys(SOAPEnvelope envelope) throws SOAPException {
		Name headerName = envelope.createName("UserContextKeys", "spp", "http://www.hp.com/spp");
		SOAPHeader header = envelope.getHeader();
		Iterator it = header.getChildElements(headerName);
		if (it != null && it.hasNext()) {
			SOAPElement headerElement = (SOAPElement) it.next();
			Map userContextKeys = new HashMap();
			for (Iterator it2 = headerElement.getChildElements(); it2.hasNext();) {
				SOAPElement keyEl = (SOAPElement) it2.next();
				Iterator it3 = keyEl.getChildElements();
				if (it3.hasNext()) {
					userContextKeys.put(keyEl.getElementName().getLocalName(), ((Node) it3.next()).getValue());
				}
				else {
					String key = keyEl.getElementName().getLocalName();
					userContextKeys.put(key, "");
				}
			}
			return userContextKeys;
		}
		else {
			mLog.error("No elements in the header");
		}
		return null;
	}

	/**
	 * Extracts user profile from the SOAP header and returns it as a Map. The profile is encoded
	 * as a string in <tt>UserProfile</tt> tag.
	 * @param envelope parent envelope of the SOAP header
	 * @return profile map
	 * @throws SOAPException If an error occurs when manipulating SOAP elements.
	 * @see ProfileHelperff#profileFromString(String) for details about how the profile is encoded.
	 */
	private Map extractUserProfile(SOAPEnvelope envelope) throws SOAPException {
		Name headerName = envelope.createName("UserProfile", "spp", "http://www.hp.com/spp");
		SOAPHeader header = envelope.getHeader();
		Iterator it = header.getChildElements(headerName);
		if (it.hasNext()) {
			SOAPElement headerElement = (SOAPElement) it.next();
			if (headerElement.hasChildNodes()) {
				String userProfileString = headerElement.getFirstChild().getNodeValue();
				try {
					return PROFILE_HELPER.profileFromString(userProfileString);
				}
				catch (IllegalArgumentException e) {
					mLog.error("Error parsing profile", e);
				}
			}
		}

		// if you get here this means that we didn't find the node
		if (mLog.isDebugEnabled()) {
			mLog.debug("UserProfile node not found in the header");
		}
		return null;
	}

	/**
	 * Puts the context data (keys and profile) into {@link HttpServletRequest} of the WSRP call making it available
	 * to portlets through the portlet request.
	 * @param messageContext message context used to access the request
	 * @param userContextKeys context keys map to bind
	 * @param userProfile user profile map to bind
	 */
	private void makeContextDataAvailableToPortlets(SOAPMessageContext messageContext, Map userContextKeys, Map userProfile) {
		HttpServletRequest request =
				(HttpServletRequest) messageContext.get(MessageContext.SERVLET_REQUEST);
		if (userContextKeys != null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("UserContextKeys bound to request attribute " + USER_CONTEXT_KEYS_KEY);
			}
			request.setAttribute(USER_CONTEXT_KEYS_KEY, userContextKeys);
		}
		else {
			mLog.error("UserContextKeys not found");
		}
		if (userProfile != null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("UserProfile bound to request attribute " + USER_PROFILE_KEY);
			}
			request.setAttribute(USER_PROFILE_KEY, userProfile);
		}
	}
	
	/**
	 * Check currect request is inbound or outbound
	 * @param mc
	 * 			message context
	 * @return if the request is inbound, then return true
	 */
	private boolean isRequest(MessageContext mc){
		return !((Boolean)mc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY)).booleanValue();
	}
	
	/**
	 * Check currect soap action is need to be handled
	 * @param mc
	 * @return true/false
	 */
	private boolean isNeedHandledSOAPAction(MessageContext mc){
		try{
			// SOAP action list which should be handled
			List list = Arrays.asList(SOAP_ACTION);
			
			//retrieve the current soap action
			String currentActionURI = (String)mc.get("javax.xml.ws.soap.http.soapaction.uri");
			
			return list.contains(currentActionURI);
			
		}catch(Exception ex){
			mLog.debug("Retrieve soap action error:" + ex.getMessage());			
		}
		return false;
	}
}
