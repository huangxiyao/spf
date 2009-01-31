/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.xa.misc.portlet;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.portlet.PortletContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.servlet.http.HttpSession;

import org.springframework.context.ApplicationContext;
import org.springframework.web.portlet.context.PortletApplicationContextUtils;

/**
 * A container class for miscellaneous utility methods for portlets.
 * 
 * @author <link href="wei.teng@hp.com">Teng Wei</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see com.hp.it.spf.xa.misc.Utils
 */

public class Utils extends com.hp.it.spf.xa.misc.Utils {

	/**
	 * Returns the Spring ApplicationContext object from the given portlet
	 * request. Returns null if a null request was provided.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The application context.
	 */
	public static ApplicationContext getApplicationContext(
			PortletRequest portletRequest) {
		if (portletRequest == null) {
			return null;
		}
		PortletContext pc = portletRequest.getPortletSession()
				.getPortletContext();
		return PortletApplicationContextUtils.getWebApplicationContext(pc);
	}

	/**
	 * Get the current value for the given user property from the P3P user
	 * information in the given portlet request. The user information is
	 * obtained from the standard location (PortletRequest.USER_INFO). Returns
	 * null if this property has not been set in the user information, or if the
	 * user information itself is null or guest (eg, when the user is not
	 * logged-in), or the portlet request or key provided were null.
	 * 
	 * @param request
	 *            The portlet request.
	 * @param key
	 *            The user property name.
	 * @return The user property value.
	 */
	public static Object getUserProperty(PortletRequest request, String key) {
		if (request != null && key != null) {
			// TODO: Check that this logic integrates correctly with new
			// authentication module. May need to change this method to get user
			// attributes from different location in the request. Also see the
			// property key name constants, defined in the common Consts class -
			// they are marked TODO as well.
			Object o = request.getAttribute(PortletRequest.USER_INFO);
			if (o != null) {
				Map userMap = (Map) o;
				return (String) userMap.get(key.trim());
			}
		}
		return null;
	}

	/**
	 * Return true if the given portlet request indicates the user is logged-in
	 * (ie authenticated), or false if it indicates the user is not logged-in
	 * (ie anonymous). This is based on the user information present in the
	 * standard location (PortletRequest.USER_INFO) - eg, when that information
	 * indicates an authenticated user, this method returns true, but when it
	 * indicates a guest or null user, it returns false.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return True if the user is authenticated, false otherwise.
	 */
	public static boolean isAuthenticatedUser(PortletRequest request) {
		if (request != null) {
			// TODO: Finish this logic so it integrates correctly with new
			// authentication module. Check the proper attribute(s) and return
			// true or false accordingly.
		}
		return false;
	}

	/**
	 * Get the current user groups from the given portlet request. These are the
	 * authorization groups defined for the current request in the portal (thus
	 * they are portal elements, not part of the portlet application itself).
	 * The SPF framework propagates the user groups from the portal to the
	 * portlet where they are made accessible in the request, so this method
	 * just returns them from the request. The method returns null if there were
	 * no groups in the request.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The list of groups (null if none).
	 */
	public static String[] getGroups(PortletRequest request) {
		String[] groups = null;
		if (request != null) {
			// TODO: Finish this logic. Get groups from request using new
			// authentication module. If no groups found in request, return
			// null. A key name for this is defined in the common Consts class -
			// see TODO there.
		}
		return groups;
	}

	/**
	 * Get the current portal site name from the given portlet request. The
	 * portal site name is the unique identifier for the current portal site
	 * (thus it is not an element from the portlet application itself; it is an
	 * element from the portal). For Vignette Portal, this is the "site DNS
	 * name". The SPF framework propagates the portal site name from the portal
	 * to the portlet, where it is made accessible in the request. So this
	 * method just returns the site name from the given request. The method
	 * returns null if no site name was recorded in the request.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The portal site name.
	 */
	public static String getPortalSiteName(PortletRequest request) {
		String siteName = null;
		if (request != null) {
			// TODO: Finish this logic. Get portal site name from request using
			// new authentication module. If not found in request, return null.
			// A key name for this is defined in the common Consts class - see
			// TODO there.
		}
		return siteName;
	}

	/**
	 * Use {@link #getPortalSiteName(PortletRequest)} instead.
	 * 
	 * @deprecated
	 */
	public static String getSiteName(PortletRequest request) {
		return getPortalSiteName(request);
	}

	/**
	 * Get the current portal site URL (home page URL) from the given portlet
	 * request. The SPF framework propagates the portal site URL from the portal
	 * to the portlet, where it is made accessible in the request. So this
	 * method just returns that URL from the given request. The method returns
	 * null if no such URL was recorded in the request.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The portal site home page URL.
	 */
	public static String getPortalSiteURL(PortletRequest request) {
		String siteURL = null;
		if (request != null) {
			// TODO: Finish this logic. Get portal site URL from request using
			// new authentication module. If not found in request, return null.
			// A key name for this is defined in the common Consts class - see
			// TODO there.
		}
		return siteURL;
	}

	/**
	 * Use {@link #getPortalSiteURL(PortletRequest)} instead.
	 * 
	 * @deprecated
	 */
	public static String getSiteURL(PortletRequest request) {
		return getPortalSiteURL(request);
	}

	/**
	 * Get the current portal request URL from the given portlet request. The
	 * SPF framework propagates the complete request URL from the portal to the
	 * portlet, where it is made accessible in the request. So this method just
	 * returns that URL from the given request. The method returns null if no
	 * such URL was recorded in the request.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The complete portal request URL.
	 */
	public static String getPortalRequestURL(PortletRequest request) {
		String requestURL = null;
		if (request != null) {
			// TODO: Finish this logic. Get portal request URL from request
			// using new authentication module. If not found in request, return
			// null. A key name for this is defined in the common Consts class -
			// see TODO there.
		}
		return requestURL;
	}

	/**
	 * Use {@link #getPortalRequestURL(PortletRequest)} instead.
	 * 
	 * @deprecated
	 */
	public static String getRequestURL(PortletRequest request) {
		return getPortalRequestURL(request);
	}

	/**
	 * Get the current portlet ID from the given portlet request. The portlet ID
	 * is the unique identifier for the portlet instance as provisioned in the
	 * portal (thus it is not an element from the portlet application itself; it
	 * is an element from the portal). For Vignette Portal, this is the "portlet
	 * friendly ID" configured by the portal administrator on the portlet
	 * instance. The SPF framework propagates the portlet ID from the from the
	 * portal to the portlet, where it is made accessible in the request. So
	 * this method just returns the portlet ID from the given request. The
	 * method returns null if no portlet ID was recorded in the request.
	 * 
	 * @param request
	 * @return
	 */
	public static String getPortletID(PortletRequest request) {
		String portletId = null;
		if (request != null) {
			// TODO: Finish this logic. Get portlet ID (currently, the Vignette
			// portlet friendly ID) from the request using the new
			// authentication module. If not found in request, return null. A
			// key name for this is defined in the common Consts class - see
			// TODO there.
		}
		return portletId;
	}
}
