package com.hp.it.spf.xa.portletdata.portal;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.epicentric.common.website.MenuItemNode;
import com.epicentric.common.website.MenuItemUtils;
import com.epicentric.page.Page;
import com.epicentric.site.Site;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * Collects the data which is shared with portlet. Is used for remote (WSRP) and local (JSR 286) portlets.
 * <p>
 * <b>Important:</b><br />
 * This class can be executed in parallel by several threads for the same incoming portal
 * request. In order to prevent any concurrency issues, the access to the request is synchronized.
 * The scope of the synchronized block is limited to avoid bottlenecks.
 * </p>
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class PortletDataCollector
{
	private static final LogWrapper LOG = new LogWrapper(PortletDataCollector.class);
	static final String REQUEST_USER_CONTEXT_KEYS_KEY = PortletDataCollector.class.getName() + ".UserContextKeys";
	static final String REQUEST_USER_PROFILE_KEY = PortletDataCollector.class.getName() + ".UserProfile";

	/**
	 * Collects the data from the request which will be sent to portlets in user context map.
	 * This method will save its result as the request attribute and will attempt to use that
	 * attribute value if it is already present. Invoking it from the main request thread
	 * prior to calling remote portlets should help preventing mutli-threaded access issues.
	 * @param request portal request
	 * @return user context keys map
	 */
	public Map<String, String> retrieveUserContextKeys(HttpServletRequest request) {
		// synchronize this as multiple WSRP threads will access the request in
		// parallel and we don't know the underlying request implementation
		synchronized (request) {
			Map<String, String> userContext = (Map<String, String>) request.getAttribute(REQUEST_USER_CONTEXT_KEYS_KEY);
			if (userContext != null) {
				if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
					LOG.debug("User context keys already present in request: " + userContext);
				}
				return userContext;
			}

			userContext = new HashMap<String, String>();

			userContext.put(Consts.KEY_PORTAL_SITE_URL, getPortalSiteURL(request));
			userContext.put(Consts.KEY_PORTAL_REQUEST_URL, getRequestURL(request));
			userContext.put(Consts.KEY_PORTAL_SITE_NAME, getPortalSiteName(request));
			userContext.put(Consts.KEY_PORTAL_SESSION_ID, getPortalSessionId(request));
			userContext.put(Consts.KEY_SESSION_TOKEN, getSessionToken(request));
			userContext.put(Consts.KEY_NAVIGATION_ITEM_NAME, getNavigationItemName(request));
			userContext.put(Consts.KEY_PORTAL_PAGE_ID, getPageFriendlyId(request));
			// add last session cleanup date in usercontext to be passed to portlets per CR 86
			userContext.put(Consts.KEY_LAST_PORTAL_SESSION_CLEANUP_DATE, getLastSessionCleanupDate(request));

			request.setAttribute(REQUEST_USER_CONTEXT_KEYS_KEY, userContext);

			if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
				LOG.debug("User context keys saved in request: " + userContext);
			}

			return userContext;
		}
	}

	/**
	 * Retrieves user profile map from portal session.
     * This method will save its result as the request attribute and will attempt to use that
     * attribute value if it is already present. Invoking it from the main request thread
     * prior to calling remote portlets should help preventing mutli-threaded access issues.
	 *
	 * @param request incoming user request
	 * @return map containing user profile.
	 */
	public Map retrieveUserProfile(HttpServletRequest request) {
		// synchronize this as multiple WSRP threads will access the request in
		// parallel and we don't know the underlying request implementation
		synchronized (request) {
			Map userProfile = (Map) request.getAttribute(REQUEST_USER_PROFILE_KEY);
			if (userProfile != null) {
				if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
					LOG.debug("User profile map already present in request: " + userProfile);
				}
				return userProfile;
			}

			HttpSession session = request.getSession(true);
			userProfile = (Map) session.getAttribute(Consts.USER_PROFILE_KEY);
			// profile not found in the standard attribute; try legacy name
			if (userProfile == null) {
				userProfile = (Map) session.getAttribute("StandardParameters");
			}

			if (userProfile != null) {
				request.setAttribute(REQUEST_USER_PROFILE_KEY, userProfile);

				if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
					LOG.debug("User profile map saved in request: " + userProfile);
				}
			}

			return userProfile;
		}
	}

	/**
	 * Retrieves request URL.
	 * @param request incoming user request
	 * @return request URL as calculated by {@link com.hp.it.spf.xa.misc.portal.Utils#getRequestURL(javax.servlet.http.HttpServletRequest)}
	 */
	private String getRequestURL(HttpServletRequest request)
	{
		return com.hp.it.spf.xa.misc.portal.Utils.getRequestURL(request);
	}

	/**
	 * Retrieves requested portal site URL.
	 * @param request incoming user request
	 * @return site URL as calculaged by {@link com.hp.it.spf.xa.misc.portal.Utils#getPortalSiteURL(javax.servlet.http.HttpServletRequest)}
	 */
	private String getPortalSiteURL(HttpServletRequest request)
	{
		return com.hp.it.spf.xa.misc.portal.Utils.getPortalSiteURL(request);
	}

	/**
	 * Retrieves portal site DNS name.
	 * @param request incoming user request
	 * @return portal site DNS name retrieved from Vignette <tt>PortalContext</tt> object
	 */
	/*private*/ String getPortalSiteName(HttpServletRequest request) {
		PortalContext portalContext = (PortalContext) request.getAttribute("portalContext");
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

	/**
	 * Retrieves portal web application session ID (JSESSIONID value).
	 * @param request incoming user request
	 * @return {@link javax.servlet.http.HttpSession} ID of the request
	 */
	/*private*/ String getPortalSessionId(HttpServletRequest request) {
		return request.getSession(true).getId();
	}

	/**
	 * Retrieves the value of Siteminder SESSION token cookie.
	 * @param request incoming user request
	 * @return value of SESSION token cookie set by HPP or @HP
	 *         if non could be found
	 */
	/*private*/ String getSessionToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (int i = 0, len = cookies.length; i < len; i++) {
				Cookie cookie = cookies[i];
				if (Consts.COOKIE_NAME_HPPSESSION.equals(cookie.getName())) {
					return cookie.getValue();
				} else if (Consts.COOKIE_NAME_HPISESSION.equals(cookie.getName())) {
				    return cookie.getValue();
				} else if (Consts.COOKIE_NAME_HPESESSION.equals(cookie.getName())) {
					return cookie.getValue();
				} else if (Consts.COOKIE_NAME_SMSESSION.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return "";
	}

	/**
	 * Retrieves the navigation item name for the current request.
	 * @param request incoming user request
	 * @return navigation item name or empty string if none could be found
	 */
	/*private*/ String getNavigationItemName(HttpServletRequest request) {
		PortalContext portalContext = (PortalContext) request.getAttribute("portalContext");
		if (portalContext == null) {
			if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
				LOG.debug("Unable to find PortalContext object - this happens when called from Vignette Console");
			}
			return "";
		}
		MenuItemNode node = getSelectedMenuItemNode(portalContext);
		if (node == null) {
			LOG.warning("Unable to find current menu item");
			return "";
		}
		if (node.getMenuItem() != null) {
			return node.getMenuItem().getTitle();
		}
		return "";
	}

	/**
	 * @param portalContext portal request context
	 * @return selected menu item node or <code>null</code> if none is available
	 */
	/*private*/ MenuItemNode getSelectedMenuItemNode(PortalContext portalContext) {
		return MenuItemUtils.getSelectedMenuItemNode(portalContext);
	}


	/**
	 * Retrieves the portal page friendly ID.
	 * @param request
	 *            incoming user request
	 * @return page friendly id retrieved from Vignette <tt>PortalContext</tt> object or empty string
	 * if none could be found
	 */
	/*private*/ String getPageFriendlyId(HttpServletRequest request) {
		PortalContext portalContext = (PortalContext) request.getAttribute("portalContext");
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

	/**
	 * Retrieves the last session clean up date stored in the http session.
	 * @param request
	 *            incoming user request
	 * @return last session clean up date from <tt>HttpSession</tt> object or empty string
	 * if none could be found
	 */
	String getLastSessionCleanupDate(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String epoch = (String)session.getAttribute(Consts.KEY_LAST_PORTAL_SESSION_CLEANUP_DATE);
		if (epoch == null) {
			epoch = "";
		}
		return epoch;
	}
}
