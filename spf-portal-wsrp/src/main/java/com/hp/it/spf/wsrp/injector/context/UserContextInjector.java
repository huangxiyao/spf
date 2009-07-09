/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.wsrp.injector.context;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;

import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.handlers.BasicHandler;
import org.apache.axis.message.Text;
import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DOMException;

import com.epicentric.common.website.MenuItemNode;
import com.epicentric.common.website.MenuItemUtils;
import com.epicentric.page.Page;
import com.epicentric.site.Site;
import com.hp.it.spf.wsrp.injector.context.portal.filter.RequestBindingFilter;
import com.hp.it.spf.wsrp.misc.Predicates;
import com.hp.it.spf.wsrp.misc.Utils;
import com.hp.it.spf.xa.misc.Consts;
import com.hp.it.spf.xa.wsrp.ProfileHelper;
import com.hp.it.spf.xa.wsrp.portal.RequestMap;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * Retrieves user context (keys and profile) values from the incoming user
 * request and put them in the SOAP header of the outgoing WSRP request. The
 * incoming request is retrieved from the {@link RequestMap} by using the
 * request key extracted from <code>user-agent</code> value. There is an
 * assumption that all user context values can be retrieved either directly from
 * the request or any object that is reachable from the request (e.g. session,
 * request attribute, etc...).
 * <p>
 * <b>Important:</b><br />
 * This class can be executed in parallel by several threads for the same HTTP
 * request. In order to prevent any concurrency issues, the access to the
 * request must be synchronized. The scope of the synchronized block must be
 * limited to avoid bottlenecks.
 * </p>
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com) 
 * @author Oliver, Kaijian Ding, Ye Liu
 * @version TBD
 */
@SuppressWarnings("serial")
public class UserContextInjector extends BasicHandler {
	private static final LogWrapper LOG = new LogWrapper(UserContextInjector.class);

	public static final ProfileHelper PROFILE_HELPER = new ProfileHelper();
	static final String WSRP_PROFILE_ERROR_FLAG = "WsrpProfileError";

	/**
	 * Retrieves user profile map from session and injects user profile map into
	 * soap. Add UserContextKeys and UserProfile to soap header section .
	 * 
	 * @param messageContext this web service call message context
	 */
	public void invoke(MessageContext messageContext) throws AxisFault {
		// We are only interested in getMarkup and performBlockingInteraction.
		// If this is not the case
		// exit from this method.
		if (!Predicates.isWsrpBaseCall(messageContext)) {
			return;
		}

		// handle only the request messages, i.e. before the pivot
		if (!messageContext.getPastPivot()) {
			HttpServletRequest request = null;
			if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
				LOG.debug("UserContextInjector invoke first try");
			}
			try {
				SOAPMessage message = messageContext.getMessage();
				SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();

				request = Utils.retrieveRequest(messageContext);
				if (request == null) {
					LOG.error("Unable to find request! User context will not be injected");
					return;
				}

				// log WL request thread name so we know what are the request
				// logs associated
				// with the thread running this WSRP call - for getMarkup calls
				// this is a
				// Vignette thread pool's thread
				if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
					LOG.debug("Request thread is '" +
							request.getAttribute(RequestBindingFilter.THREAD_NAME_REQUEST_KEY));
				}

				Map<String, String> userContextKeys = retrieveUserContextKeys(request);
				if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
					LOG.debug("User context keys: " + userContextKeys);
				}

				// Using new way of serializing user profile and context keys
				Map userProfile = getUserProfileMap(request);
				if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
					LOG.debug("user profile: " + userProfile);
				}
				injectUserContext(envelope, userContextKeys, userProfile);
			} catch (Throwable t) {
				LOG.error("Error occured while injecting user context", t);
				// save the fact that we got an error while injecting profile so
				// we don't report it
				// 2nd time in onFault method
				messageContext.setProperty(WSRP_PROFILE_ERROR_FLAG,
						Boolean.TRUE);
				throw new AxisFault(
						"Error occured while injecting UserContext!", t);
			}
		}
	}

	/**
	 * @param request
	 *            user original request
	 * @return user context key map whose values are {@link String} objects
	 */
	private Map<String, String> retrieveUserContextKeys(HttpServletRequest request) {
		Map<String, String> userContext = new HashMap<String, String>();

		userContext.put(Consts.KEY_PORTAL_SITE_URL, com.hp.it.spf.xa.misc.portal.Utils.getSiteURL(request));
		userContext.put(Consts.KEY_PORTAL_REQUEST_URL, com.hp.it.spf.xa.misc.portal.Utils.getRequestURL(request));
		userContext.put(Consts.KEY_PORTAL_SITE_NAME, getPortalSiteName(request));
		userContext.put(Consts.KEY_PORTAL_SESSION_ID, getPortalSessionId(request));
		userContext.put(Consts.KEY_SESSION_TOKEN, getHppSessionToken(request));
		userContext.put(Consts.KEY_NAVIGATION_ITEM_NAME, getNavigationItemName(request));
		userContext.put(Consts.KEY_PORTAL_PAGE_ID, getPageFriendlyId(request));
		return userContext;
	}

	/**
	 * Stores user context keys and user profile in a single SOAP header. The
	 * name of the header is <tt>UserContext</tt>. The header is a map
	 * serialized to string user
	 * {@link ProfileHelper#profileToString(java.util.Map)}. It's a 2-element
	 * map: <tt>UserContextKeys</tt> entry contains a map provided in
	 * <tt>userContextKeys</tt> method parameter, and
	 * <tt>UserProfile</tt> is a map provided int
	 * <tt>userProfile</tt> method parameter. Storing this data as string in
	 * SOAP header is much more efficient than XML or direct SOAP header
	 * elements. Based on the conducted tests we confirmed that the using SOAP
	 * header elements results in a response time proportional to the number of
	 * concurrent calls. Using plain text form avoids also the verbosity of XML
	 * representation which makes the overall SOAP request size much smaller.
	 * 
	 * @param envelope
	 *            parent envelope for the SOAP header
	 * @param userContextKeys
	 *            user context key map
	 * @param userProfile
	 *            user profile map
	 * @throws SOAPException
	 *             If an error occurs while manipulating the header
	 * @throws ParserConfigurationException 
	 * @throws DOMException 
	 * 
	 * @see com.hp.it.spf.xa.wsrp.ProfileHelper for more information of the
	 *      string encoding of the profile
	 */
	private void injectUserContext(SOAPEnvelope envelope, Map userContextKeys,
			Map userProfile) throws SOAPException, DOMException, ParserConfigurationException {
		Map userContext = new HashMap();
		userContext.put(Consts.PORTAL_CONTEXT_KEY, userContextKeys);
		userContext.put(Consts.USER_PROFILE_KEY, userProfile);
		if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
			LOG.debug("UserContextInjector userProfile ", userProfile);
		}
		SOAPHeader header = envelope.getHeader();
		Name userContextHeaderName = envelope.createName("UserContext", "spf",
				"http://www.hp.com/spf");
		SOAPHeaderElement userContextElement = header
				.addHeaderElement(userContextHeaderName);
		if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
			LOG.debug("UserContextInjector userProfile " + PROFILE_HELPER.profileToString(userContext));
		}
		// encapsulate user context in CDATA section
		String userContextStr = PROFILE_HELPER.profileToString(userContext);
		CDATASection userContextCDATA = XMLUtils.newDocument().createCDATASection(userContextStr);
		userContextElement.appendChild(new Text(userContextCDATA));
	}

	/**
	 * @param request
	 *            incoming user request
	 * @return portal site DNS name retrieved from Vignette
	 *         <tt>PortalContext</tt> object
	 */
	private String getPortalSiteName(HttpServletRequest request) {
		// synchronize this as multiple WSRP threads will access the request in
		// parallel and we don't
		// know the underlying request implementation
		synchronized (request) {
			PortalContext portalContext = (PortalContext) request
					.getAttribute("portalContext");
			if (portalContext == null) {
				if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
					LOG.debug("Unable to find PortalContext object - this happens when called from Vignette Console");
				}
				return "";
			}
			Site currentSite = portalContext.getCurrentSite();
			if (currentSite == null) {
				LOG.warning("No site for current request");
				return "";
			}
			return currentSite.getDNSName();
		}
	}

	/**
	 * @param request
	 *            incoming user request
	 * @return {@link javax.servlet.http.HttpSession} ID of the request
	 */
	private String getPortalSessionId(HttpServletRequest request) {
		// synchronize this as multiple WSRP threads will access the request in
		// parallel and we don't
		// know the underlying request implementation
		synchronized (request) {
			return request.getSession(true).getId();
		}
	}

	/**
	 * @param request
	 *            incoming user request
	 * @return value of <code>SMSESSION</code> cookie set by HPP or empty string
	 *         if non could be found
	 */
	private String getHppSessionToken(HttpServletRequest request) {
		Cookie[] cookies = null;
		// synchronize this as multiple WSRP threads will access the request in
		// parallel and we don't
		// know the underlying request implementation
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

	private String getNavigationItemName(HttpServletRequest request) {
		// FIXME (slawek) - use Cyril's MenuItemHelper to get that info
		synchronized (request) {
			PortalContext portalContext = (PortalContext) request
					.getAttribute("portalContext");
			if (portalContext == null) {
				if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
					LOG.debug("Unable to find PortalContext object - this happens when called from Vignette Console");
				}
				return "";
			}
			MenuItemNode node = MenuItemUtils.getSelectedMenuItemNode(portalContext);
			if (node == null) {
				LOG.warning("Unable to find current menu item");
				return "";
			}
			if (node.getMenuItem() != null) {
				return node.getMenuItem().getTitle();
			}
		}
		return "";
	}

	/**
	 * Get user profile map from session by defined key
	 * 
	 * @param request
	 * @return
	 */
	private Map getUserProfileMap(HttpServletRequest request) {
		// synchronize this as multiple WSRP threads will access the request in
		// parallel and we don't
		// know the underlying request implementation
		synchronized (request) {
			HttpSession session = request.getSession(true);
			Map userProfile = (Map) session.getAttribute(Consts.USER_PROFILE_KEY);
			// profile not found in the standard attribute; try legacy name
			if (userProfile == null) {
				userProfile = (Map) session.getAttribute("StandardParameters");
			}
			return userProfile;
		}
	}

	/**
	 * @param request
	 *            incoming user request
	 * @return page friendly id retrieved from Vignette
	 *         <tt>PortalContext</tt> object
	 */
	private String getPageFriendlyId(HttpServletRequest request) {
		// synchronize this as multiple WSRP threads will access the request in
		// parallel and we don't
		// know the underlying request implementation
		synchronized (request) {
			PortalContext portalContext = (PortalContext) request
					.getAttribute("portalContext");
			if (portalContext == null) {
				if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
					LOG.debug("Unable to find PortalContext object - this happens when called from Vignette Console");
				}
				return "";
			}
			Page page = portalContext.getResolvedPortletPage();
			if (page == null) {
				LOG.warning("Unable to get page object for current request");
				return "";
			}
			return page.getFriendlyID(portalContext);
		}
	}
}