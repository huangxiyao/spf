package com.hp.spp.wsrp.context;

import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.MessageContext;
import org.apache.axis.AxisFault;
import org.apache.axis.transport.http.HTTPConstants;
import org.apache.log4j.Logger;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.Node;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Iterator;
import java.util.HashMap;

import com.hp.spp.profile.ProfileHelper;

/**
 * Extracts user context data from the appropriate SOAP header elements and make them available to portlets
 * as map in portlet request.
 */
public class UserContextExtractor extends BasicHandler {

	public static final String USER_CONTEXT_KEYS_KEY = "com.hp.spp.UserContextKeys";
	public static final String USER_PROFILE_KEY = "com.hp.spp.UserProfile";

	private static final Logger mLog = Logger.getLogger(UserContextExtractor.class);
	private static final Logger mTLog = Logger.getLogger("TIME." + UserContextExtractor.class.getName());
	public static final ProfileHelper PROFILE_HELPER = new ProfileHelper();

	/**
	 * Extracts the context data and make it available to portlets.
	 * @param messageContext current message context
	 * @throws AxisFault If unexpected error occurs during the processing.
	 */
	public void invoke(MessageContext messageContext) throws AxisFault {
		// handle only request, so before pivot occured
		if (!messageContext.getPastPivot()) {
			// handle only markup requests
			String actionURI = messageContext.getSOAPActionURI();
			if ("urn:oasis:names:tc:wsrp:v1:getMarkup".equals(actionURI) ||
					"urn:oasis:names:tc:wsrp:v1:performBlockingInteraction".equals(actionURI))
			{
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
					throw AxisFault.makeFault(e);
				}
				if (mTLog.isDebugEnabled()) {
					mTLog.debug("context keys extracted | " + (System.currentTimeMillis()-start));
				}
			}
		}
	}


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
	 * @see ProfileHelper#profileFromString(String) for details about how the profile is encoded.
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
	private void makeContextDataAvailableToPortlets(MessageContext messageContext, Map userContextKeys, Map userProfile) {
		HttpServletRequest request =
				(HttpServletRequest) messageContext.getProperty(HTTPConstants.MC_HTTP_SERVLETREQUEST);
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

}
