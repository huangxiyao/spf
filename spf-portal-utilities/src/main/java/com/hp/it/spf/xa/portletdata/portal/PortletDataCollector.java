package com.hp.it.spf.xa.portletdata.portal;

import com.hp.it.spf.xa.misc.Consts;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;
import com.epicentric.site.Site;
import com.epicentric.common.website.MenuItemNode;
import com.epicentric.common.website.MenuItemUtils;
import com.epicentric.page.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.HashMap;

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

	/**
	 * Collects the data from the request which will be sent to portlets in user context map.
	 * @param request portal request
	 * @return user context keys map
	 */
	public Map<String, String> retrieveUserContextKeys(HttpServletRequest request) {
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
	 * Retrieves user profile map from portal session.
	 *
	 * @param request incoming user request
	 * @return map containing user profile.
	 */
	public Map retrieveUserProfile(HttpServletRequest request) {
		// synchronize this as multiple WSRP threads will access the request in
		// parallel and we don't know the underlying request implementation
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
	 * Retrieves portal site DNS name.
	 * @param request incoming user request
	 * @return portal site DNS name retrieved from Vignette <tt>PortalContext</tt> object
	 */
	/*private*/ String getPortalSiteName(HttpServletRequest request) {
		// synchronize this as multiple WSRP threads will access the request in
		// parallel and we don't know the underlying request implementation
		synchronized (request) {
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
	}

	/**
	 * Retrieves portal web application session ID (JSESSIONID value).
	 * @param request incoming user request
	 * @return {@link javax.servlet.http.HttpSession} ID of the request
	 */
	/*private*/ String getPortalSessionId(HttpServletRequest request) {
		// synchronize this as multiple WSRP threads will access the request in
		// parallel and we don't know the underlying request implementation
		synchronized (request) {
			return request.getSession(true).getId();
		}
	}

	/**
	 * Retrieves the value of HP Passport SMSESSION cookie.
	 * @param request incoming user request
	 * @return value of <code>SMSESSION</code> cookie set by HPP or empty string
	 *         if non could be found
	 */
	/*private*/ String getHppSessionToken(HttpServletRequest request) {
		Cookie[] cookies = null;
		// synchronize this as multiple WSRP threads will access the request in
		// parallel and we don't know the underlying request implementation
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
	 * Retrieves the navigation item name for the current request.
	 * @param request incoming user request
	 * @return navigation item name or empty string if none could be found
	 */
	/*private*/ String getNavigationItemName(HttpServletRequest request) {
		// synchronize this as multiple WSRP threads will access the request in
		// parallel and we don't know the underlying request implementation
		synchronized (request) {
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
		// synchronize this as multiple WSRP threads will access the request in
		// parallel and we don't know the underlying request implementation
		synchronized (request) {
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
	}

}
