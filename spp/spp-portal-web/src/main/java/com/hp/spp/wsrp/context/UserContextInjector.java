package com.hp.spp.wsrp.context;

import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.MessageContext;
import org.apache.axis.AxisFault;
import org.apache.axis.message.RPCElement;
import org.apache.axis.message.RPCParam;
import org.apache.log4j.Logger;

import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPHeaderElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;

import com.vignette.portal.website.enduser.PortalContext;
import com.epicentric.site.Site;
import com.epicentric.common.website.MenuItemUtils;
import com.epicentric.common.website.MenuItemNode;
import com.hp.spp.profile.Constants;
import com.hp.spp.profile.ProfileHelper;
import com.hp.spp.config.Config;
import com.hp.spp.perf.TimeRecorder;
import com.hp.spp.perf.Operation;
import com.hp.spp.portal.TimeRecordingFilter;
import oasis.names.tc.wsrp.v1.types.GetMarkup;
import oasis.names.tc.wsrp.v1.types.PerformBlockingInteraction;

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
 */
public class UserContextInjector extends BasicHandler {
	private static final Logger mLog = Logger.getLogger(UserContextInjector.class);

	public static final ProfileHelper PROFILE_HELPER = new ProfileHelper();
	static final String WSRP_PROFILE_ERROR_FLAG = "WsrpProfileError";
	static final String TIME_RECORDER_MC_KEY = TimeRecorder.class.getName();


	public void invoke(MessageContext messageContext) throws AxisFault {
		// We are only interested in getMarkup and performBlockingInteraction. If this is not the case
		// exit from this method.
		if (!isWsrpBaseCall(messageContext)) {
			return;
		}

		// handle only the request messages, i.e. before the pivot
		if (!messageContext.getPastPivot()) {
			TimeRecorder timeRecorder = null;
			HttpServletRequest request = null;
			try {
				SOAPMessage message = messageContext.getMessage();
				SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();

				request = retrieveRequest(envelope);
				if (request == null) {
					mLog.error("Unable to find request! User context will not be injected");
					return;
				}

				// log WL request thread name so we know what are the request logs associated
				// with the thread running this WSRP call - for getMarkup calls this is a
				// Vignette thread pool's thread
				if (mLog.isDebugEnabled()) {
					mLog.debug("Request thread is '" + request.getAttribute(RequestBindingFilter.THREAD_NAME_REQUEST_KEY));
				}

				timeRecorder = (TimeRecorder) request.getAttribute(TimeRecordingFilter.TIME_RECORDER_REQUEST_KEY);

				// TimeRecorder is null for console requests
				if (timeRecorder != null) {
					// set this TimeRecorder instance in messageContext as TimeRecordingHandler relies on it
					messageContext.setProperty(TIME_RECORDER_MC_KEY, timeRecorder);
					timeRecorder.recordStart(Operation.WSRP_PROFILE);
				}

				Map userContextKeys = retrieveUserContextKeys(request);
				if (mLog.isDebugEnabled()) {
					mLog.debug("User context keys: " + userContextKeys);
				}

				if (Config.getBooleanValue("SPP.UserContextInjector.SPP20CompatibilityMode", true)) {
					// Until all the portlet servers have deployed the updated version of UserContextExtractor
					// we have to stay in this compatibility mode. UserContextExtractor is able to work
					// in both modes though SPP 2.5 version is much more efficient
					String userProfileString = getUserProfile(request);
					if (mLog.isDebugEnabled()) {
						mLog.debug("user profile: " + userProfileString);
					}
					injectUserContext(envelope, userContextKeys, userProfileString);
				}
				else {
					// Using new SPP 2.5 way of serializing user profile and context keys
					Map userProfile = getUserProfileMap(request);
					if (mLog.isDebugEnabled()) {
						mLog.debug("user profile: " + userProfile);
					}
					injectUserContext(envelope, userContextKeys, userProfile);
				}
				if (timeRecorder != null) {
					timeRecorder.recordEnd(Operation.WSRP_PROFILE);
				}
			}
			catch (Throwable t) {
				mLog.error("Error occured while injecting user context", t);
				if (timeRecorder != null) {
					timeRecorder.recordError(Operation.WSRP_PROFILE, t);
				}
				//save the fact that we got an error while injecting profile so we don't report it
				//2nd time in onFault method
				messageContext.setProperty(WSRP_PROFILE_ERROR_FLAG, Boolean.TRUE);
				throw new AxisFault("Error occured while injecting UserContext!", t);
			}
		}
	}

	static boolean isWsrpBaseCall(MessageContext messageContext) {
		String actionURI = messageContext.getSOAPActionURI();
		return "urn:oasis:names:tc:wsrp:v1:getMarkup".equals(actionURI) ||
				"urn:oasis:names:tc:wsrp:v1:performBlockingInteraction".equals(actionURI);
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
	 * Stores user context keys and user profile as SOAP header elements. For the user context keys
	 * the name of the child elements come from <tt>userContextKeys</tt> map keys.
	 * User profile is stored as string to avoid the verbosity of XML representation.
	 * @param envelope parent envelope for the SOAP header
	 * @param userContextKeys user context key map
	 * @param userProfileString string representation of the user profile
	 * @throws SOAPException If an error occurs while manipulating the header
	 *
	 * @see ProfileHelper#profileFromString(String) for more information of the string encoding of
	 * the profile
	 */
	private void injectUserContext(SOAPEnvelope envelope, Map userContextKeys, String userProfileString) throws SOAPException {
		SOAPHeader header = envelope.getHeader();
		Name headerName = envelope.createName("UserContextKeys", "spp", "http://www.hp.com/spp");
		SOAPHeaderElement headerElement = header.addHeaderElement(headerName);
		for (Iterator it = userContextKeys.entrySet().iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			headerElement.addChildElement((String) entry.getKey()).addTextNode((String) entry.getValue());
		}
		// Add UserProfile as a separate header element. This way we can keep backward compatibility
		// with SPP 1.0 code
		Name profileHeaderName = envelope.createName("UserProfile", "spp", "http://www.hp.com/spp");
		SOAPHeaderElement profileHeaderElement = header.addHeaderElement(profileHeaderName);
		profileHeaderElement.addTextNode(userProfileString);
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
		SOAPHeader header = envelope.getHeader();
		Name userContextHeaderName = envelope.createName("UserContext", "spp", "http://www.hp.com/spp");
		SOAPHeaderElement userContextElement = header.addHeaderElement(userContextHeaderName);
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
				if (mLog.isDebugEnabled()) {
					mLog.debug("Unable to find PortalContext object - this happens when called from Vignette Console");
				}
				return "";
			}
			Site currentSite = portalContext.getCurrentSite();
			if (currentSite == null) {
				if (mLog.isDebugEnabled()) {
					mLog.debug("No site for current request");
				}
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
				mLog.warn("User profile not found in session under " + Constants.PROFILE_MAP);
			}
			return "";
		}
	}

	private String getNavigationItemName(HttpServletRequest request) {
		//FIXME (slawek) - use Cyril's MenuItemHelper to get that info
		synchronized(request) {
			PortalContext portalContext = (PortalContext) request.getAttribute("portalContext");
			if (portalContext == null) {
				if (mLog.isDebugEnabled()) {
					mLog.debug("Unable to find PortalContext object - this happens when called from Vignette Console");
				}
				return "";
			}
			MenuItemNode node = MenuItemUtils.getSelectedMenuItemNode(portalContext);
			if (node == null) {
				if (mLog.isDebugEnabled()) {
					mLog.debug("Unable to find current menu item");
				}
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
		String port = Config.getValue("SPP.port.http", "80");
		return ("80".equals(port) ? null : port);
	}

	/**
	 * @return HTTPS port if it was defined in SPP configuration variable <tt>SPP.port.https</tt>
	 * and it's different from 443; <tt>null</tt> otherwise.
	 */
	private String getHttpsPortIfCustom() {
		String port = Config.getValue("SPP.port.https", "443");
		return ("443".equals(port) ? null : port);
	}

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

	private String getUserProfile(HttpServletRequest request) {
		Map userProfile = getUserProfileMap(request);
		if (userProfile == null) {
			mLog.warn("User profile not found in session under " + Constants.PROFILE_MAP);
			return "";
		}
		else {
			//FIXME (slawek) - if this is long we may keep in the session this string represenation
			return PROFILE_HELPER.profileToString(userProfile);
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
				mLog.error("SPP request key not found!");
			}
		}
		else {
			mLog.error("User-agent value not found!");
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
							mLog.error("Parameter value of '" + operationName + "' is null");
						}
					}
					else {
						mLog.error("Unable to find '" + operationName + "' parameter");
					}
				}
				else {
					mLog.error("Operation is not of type 'RPCElement': " + operation.getClass().getName());
				}
			}
		}
		return null;
	}
}
