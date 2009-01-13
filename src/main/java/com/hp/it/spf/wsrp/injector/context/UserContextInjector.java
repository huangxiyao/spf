package com.hp.it.spf.wsrp.injector.context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;

import oasis.names.tc.wsrp.v2.types.GetMarkup;
import oasis.names.tc.wsrp.v2.types.PerformBlockingInteraction;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.message.RPCElement;
import org.apache.axis.message.RPCParam;

import com.epicentric.common.website.MenuItemNode;
import com.epicentric.common.website.MenuItemUtils;
import com.epicentric.site.Site;
import com.hp.it.spf.wsrp.injector.context.portal.filter.RequestBindingFilter;
import com.hp.it.spf.wsrp.injector.profile.Constants;
import com.hp.it.spf.wsrp.injector.profile.ProfileHelper;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * Retrieves user context (keys and profile) values from the incoming user request and put them in the SOAP header
 * of the outgoing WSRP request.
 * The incoming request is retrieved from the {@link RequestMap} by using the request key extracted
 * from <code>user-agent</code> value. There is an assumption that all user context values can
 * be retrieved either directly from the request or any object that is reachable from the request (e.g.
 * session, request attribute, etc...).
 * <p>
 * <b>Important:</b><br />
 * This class can be executed in parallel by several threads for the same HTTP request. In order to
 * prevent any concurrency issues, the access to the request must be synchronized. The scope of the
 * synchronized block must be limited to avoid bottlenecks.
 * </p>
 * @author Oliver, Kaijian Ding, Ye Liu
 * @version TBD
 */
public class UserContextInjector extends BasicHandler {
	private static final LogWrapper LOG = new LogWrapper(UserContextInjector.class, "[SP-INJECTION]" + UserContextInjector.class.getName());

	public static final ProfileHelper PROFILE_HELPER = new ProfileHelper();
	static final String WSRP_PROFILE_ERROR_FLAG = "WsrpProfileError";

    /**
     * Retrieve user profile map from session and Injcet user profile map into soap.
     * Add UserContextKeys and UserProfile to soap header section .
     * 
     * @param MessageContext
     *            messageContext
     */
	public void invoke(MessageContext messageContext) throws AxisFault {
		// We are only interested in getMarkup and performBlockingInteraction. If this is not the case
		// exit from this method.
		if (!isWsrpBaseCall(messageContext)) {
			return;
		}

		// handle only the request messages, i.e. before the pivot
		if (!messageContext.getPastPivot()) {
			HttpServletRequest request = null;
	        LOG.info("UserContextInjector invoke first try");
			try {
				SOAPMessage message = messageContext.getMessage();
				SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();

				request = retrieveRequest(envelope);
				if (request == null) {
					LOG.error("Unable to find request! User context will not be injected");
					return;
				}

				// log WL request thread name so we know what are the request logs associated
				// with the thread running this WSRP call - for getMarkup calls this is a
				// Vignette thread pool's thread
					LOG.info("Request thread is '" + request.getAttribute(RequestBindingFilter.THREAD_NAME_REQUEST_KEY));

				Map userContextKeys = retrieveUserContextKeys(request);
					LOG.info("User context keys: " + userContextKeys);

					// Using new SPP 2.5 way of serializing user profile and context keys
					Map userProfile = getUserProfileMap(request);
                    LOG.info("user profile: " + userProfile);
					injectUserContext(envelope, userContextKeys, userProfile);
			}
			catch (Throwable t) {
				LOG.error("Error occured while injecting user context", t);
				//save the fact that we got an error while injecting profile so we don't report it
				//2nd time in onFault method
				messageContext.setProperty(WSRP_PROFILE_ERROR_FLAG, Boolean.TRUE);
				throw new AxisFault("Error occured while injecting UserContext!", t);
			}
		}
	}

	/** 
	 * @param MessageContext messageContext
	 * @return if it is supported wsrp version, for now v1 and v2 are supported	 *            
	 */
	static boolean isWsrpBaseCall(MessageContext messageContext) {
		String actionURI = messageContext.getSOAPActionURI();
		return "urn:oasis:names:tc:wsrp:v1:getMarkup".equals(actionURI) ||
				"urn:oasis:names:tc:wsrp:v1:performBlockingInteraction".equals(actionURI)||"urn:oasis:names:tc:wsrp:v2:getMarkup".equals(actionURI) ||
                "urn:oasis:names:tc:wsrp:v2:performBlockingInteraction".equals(actionURI);
	}


	/**
	 * @param request user original request
	 * @return user context key map whose values are {@link String} objects
	 */
	private Map retrieveUserContextKeys(HttpServletRequest request) {
		Map userContext = new HashMap();
		userContext.put("PortalSite", getPortalSiteName(request));
		userContext.put("PortalSessionId", getPortalSessionId(request));
		userContext.put("HppUserId", getHppUserId(request));
		userContext.put("HppUserName", getHppUserName(request));
		userContext.put("HppSessionToken", getHppSessionToken(request));
		userContext.put("UserContextUpdateTimestamp", getUserContextUpdateTimestamp(request));
		userContext.put("NavigationItemName", getNavigationItemName(request));
		String httpPort = getHttpPortIfCustom();
		if (httpPort != null) {
			userContext.put("HttpPort", httpPort);
		}
		String httpsPort = getHttpsPortIfCustom();
		if (httpsPort != null) {
			userContext.put("HttpsPort", httpsPort);
		}
		return userContext;
	}

	/**
	 * Stores user context keys and user profile in a single SOAP header. The name of the header is
	 * <tt>UserContext</tt>. The header is a map serialized to string user {@link ProfileHelper#profileToString(java.util.Map)}.
	 * It's a 2-element map: <tt>com.hp.spp.UserContextKeys</tt> entry contains a map provided in
	 * <tt>userContextKeys</tt> method parameter, and <tt>com.hp.spp.UserProfile</tt> is a map provided
	 * int <tt>userProfile</tt> method parameter.
	 * Storing this data as string in SOAP header is much more efficient than XML or direct SOAP header
	 * elements. Based on the conducted tests we confirmed that the using SOAP header elements results
	 * in a response time proportional to the number of concurrent calls. Using plain text form
	 * avoids olso the verbosity of XML representation which makes the overall SOAP request size
	 * much smaller.
	 * @param envelope parent envelope for the SOAP header
	 * @param userContextKeys user context key map
	 * @param userProfile user profile map
	 * @throws SOAPException If an error occurs while manipulating the header
	 *
	 * @see ProfileHelper#profileFromString(String) for more information of the string encoding of
	 * the profile
	 */
	private void injectUserContext(SOAPEnvelope envelope, Map userContextKeys, Map userProfile) throws SOAPException {
		Map userContext = new HashMap();
		userContext.put("com.hp.spp.UserContextKeys", userContextKeys);
		userContext.put("com.hp.spp.UserProfile", userProfile);
        LOG.info("UserContextInjector userProfile ", userProfile);
		SOAPHeader header = envelope.getHeader();
		Name userContextHeaderName = envelope.createName("UserContext", "spp", "http://www.hp.com/spp");
		SOAPHeaderElement userContextElement = header.addHeaderElement(userContextHeaderName);
        LOG.info("UserContextInjector userProfile " + PROFILE_HELPER.profileToString(userContext));
		userContextElement.addTextNode(PROFILE_HELPER.profileToString(userContext));
	}

	/**
	 * @param request incoming user request
	 * @return portal site DNS name retrieved from Vignette <tt>PortalContext</tt> object
	 */
	private String getPortalSiteName(HttpServletRequest request) {
		//synchronize this as multiple WSRP threads will access the request in parallel and we don't
		// know the underlying request implemenation
		synchronized (request) {
			PortalContext portalContext = (PortalContext) request.getAttribute("portalContext");
			if (portalContext == null) {
					LOG.info("Unable to find PortalContext object - this happens when called from Vignette Console");
				return "";
			}
			Site currentSite = portalContext.getCurrentSite();
			if (currentSite == null) {
					LOG.info("No site for current request");
				return "";
			}
			return currentSite.getDNSName();
		}
	}

	/**
	 * @param request incoming user request
	 * @return {@link javax.servlet.http.HttpSession} ID of the request
	 */
	private String getPortalSessionId(HttpServletRequest request) {
		//synchronize this as multiple WSRP threads will access the request in parallel and we don't
		// know the underlying request implemenation
		synchronized(request) {
			return request.getSession(true).getId();
		}
	}

	/**
	 * @param request incoming user request
	 * @return internal HPP id (GUID) of the incoming user or empty string if non could be found
	 */
	private String getHppUserId(HttpServletRequest request) {
		String userId   = null;
		Map userProfile = getUserProfileMap(request);
		if (userProfile != null){
			userId = (String)userProfile.get(Constants.MAP_HPPID);
		}
		return (userId != null ? userId : "");
	}

	/**
	 * @param request incoming user request
	 * @return incoming user name; this is the name used by the user to login or empty string if
	 * none could be found
	 */
	private String getHppUserName(HttpServletRequest request) {
		String userName = null;
		Map userProfile = getUserProfileMap(request);
		if (userProfile != null){
			userName = (String)userProfile.get(Constants.MAP_USERNAME);
		}
		return (userName != null ? userName : "");
	}

	/**
	 * @param request incoming user request
	 * @return value of <code>SMSESSION</code> cookie set by HPP or empty string if non could be found
	 */
	private String getHppSessionToken(HttpServletRequest request) {
		Cookie[] cookies = null;
		//synchronize this as multiple WSRP threads will access the request in parallel and we don't
		// know the underlying request implemenation
		synchronized (request) {
			cookies = request.getCookies();
		}
		if (cookies != null) {
			for (int i = 0, len = cookies.length; i < len; i++) {
				Cookie cookie = cookies[i];
				if ("SMSESSION".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return "";
	}

	/**
	 * @param request incoming user request
	 * @return Last profile update timestamp (long value) as String or empty string if non could be found
	 */
	private String getUserContextUpdateTimestamp(HttpServletRequest request) {
		Map userProfile = getUserProfileMap(request);
		if (userProfile != null && userProfile.containsKey(Constants.MAP_UPDATEDDATE)) {
			return String.valueOf(userProfile.get(Constants.MAP_UPDATEDDATE));
		}
		else {
			if (userProfile == null) {
				LOG.warning("User profile not found in session under " + Constants.PROFILE_MAP);
			}
			return "";
		}
	}

	private String getNavigationItemName(HttpServletRequest request) {
		//FIXME (slawek) - use Cyril's MenuItemHelper to get that info
		synchronized(request) {
			PortalContext portalContext = (PortalContext) request.getAttribute("portalContext");
			if (portalContext == null) {
					LOG.info("Unable to find PortalContext object - this happens when called from Vignette Console");
				return "";
			}
			MenuItemNode node = MenuItemUtils.getSelectedMenuItemNode(portalContext);
			if (node == null) {
					LOG.info("Unable to find current menu item");
				return "";
			}
			if (node.getMenuItem() != null) {
				return node.getMenuItem().getTitle();
			}
		}
		return "";
	}

	/**
	 * @return HTTP port if it was defined in SPP configuration variable <tt>SPP.port.http</tt>
	 * and it is different from 80; <tt>null</tt> otherwise.
	 */
	private String getHttpPortIfCustom() {
        String port = "80";
		// String port = Config.getValue("SPP.port.http", "80");
		return ("80".equals(port) ? null : port);
	}

	/**
	 * @return HTTPS port if it was defined in SPP configuration variable <tt>SPP.port.https</tt>
	 * and it's different from 443; <tt>null</tt> otherwise.
	 */
	private String getHttpsPortIfCustom() {
        String port = "443";
		//String port = Config.getValue("SPP.port.https", "443");
		return ("443".equals(port) ? null : port);
	}

	/**
	 * Get user profile map from session by defined key
	 * @param request
	 * @return
	 */
	private Map getUserProfileMap(HttpServletRequest request) {
		//synchronize this as multiple WSRP threads will access the request in parallel and we don't
		// know the underlying request implemenation
		synchronized (request) {
			HttpSession session = request.getSession(true);
			Map userProfile = (Map) session.getAttribute(Constants.PROFILE_MAP);
			// profile not found in the standard attribute; try legacy name
			if (userProfile == null) {
				userProfile = (Map) session.getAttribute("StandardParameters");
			}
			return userProfile;
		}
	}

	/**
	 * Retrieves the request from the {@link RequestMap} based on the id stored in <code>user-agent</code>
	 * value.
	 * @param envelope parent envelope for WSRP request
	 * @return request based on the ID or <code>null</code> if the request could not be found
	 * @throws Exception If an error occurs when retrieving the request key from envelope
	 */
	private HttpServletRequest retrieveRequest(SOAPEnvelope envelope) throws Exception {
		String userAgentValue = findUserAgentValue(envelope);
		if (userAgentValue != null) {
			int pos = userAgentValue.lastIndexOf(RequestBindingFilter.KEY_PREFIX);
			if (pos != -1) {
				//this extraction should be done somehow by RequestWrapper
				// as it's the only class that know how this was encoded
				String requestKey = userAgentValue.substring(pos + RequestBindingFilter.KEY_PREFIX.length());
				return RequestMap.getInstance().get(requestKey);
			}
			else {
				LOG.error("SPP request key not found!");
			}
		}
		else {
			LOG.error("User-agent value not found!");
		}

		// we didn't find the request :-(
		return null;
	}

	/**
	 * Finds <code>userAgent</code> value and extracts the request key from it.
	 * @param envelope WSRP envelope to dig into
	 * @return request key or <code>null</code> if none could be found
	 * @throws Exception If an error occurs when accessing the envelope occurs
	 */
	private String findUserAgentValue(SOAPEnvelope envelope) throws Exception {
		SOAPBody body = envelope.getBody();
		Iterator it = body.getChildElements();
		if (it.hasNext()) {
			SOAPElement operation = (SOAPElement) it.next();
			boolean isGetMarkup = "getMarkup".equals(operation.getElementName().getLocalName());
			if (isGetMarkup || "performBlockingInteraction".equals(operation.getElementName().getLocalName())) {
				if (operation instanceof RPCElement) {
					String operationName = isGetMarkup ? "getMarkup" : "performBlockingInteraction";
					RPCParam rpcParam = ((RPCElement) operation).getParam(operationName);
					if (rpcParam != null) {
						Object value = rpcParam.getObjectValue();
						if (value != null) {
							if (isGetMarkup && value instanceof GetMarkup) {
								return ((GetMarkup) value).getMarkupParams().getClientData().getUserAgent();
							}
							else if (value instanceof PerformBlockingInteraction) {
								return ((PerformBlockingInteraction) value).getMarkupParams().getClientData().getUserAgent();
							}
						}
						else {
							LOG.error("Parameter value of '" + operationName + "' is null");
						}
					}
					else {
						LOG.error("Unable to find '" + operationName + "' parameter");
					}
				}
				else {
					LOG.error("Operation is not of type 'RPCElement': " + operation.getClass().getName());
				}
			}
		}
		return null;
	}
}
