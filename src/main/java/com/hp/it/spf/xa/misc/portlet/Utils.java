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
	 * Get the value for the given user property from the P3P user information
	 * in the given portlet request. The user information is obtained from the
	 * standard location (PortletRequest.USER_INFO). Returns null if this
	 * property has not been set in the user information, or if the user
	 * information itself is null or guest (eg, when the user is not logged-in),
	 * or the portlet request or key provided were null.
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
	 * Get the user groups from the given portlet request. These are the
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
			// TODO: Get groups from request using new authentication module. If
			// no groups found in request, return null. A key name for this is
			// defined in the common Consts class - see TODO there.
		}
		return groups;
	}

	/**
	 * Get the portal site name from the given portlet request. The portal site
	 * name is the unique identifier for the current portal site (thus it is not
	 * an element from the portlet application itself; it is an element from the
	 * portal). For Vignette Portal, this is the "site DNS name". The SPF
	 * framework propagates the portal site name from the portal to the portlet,
	 * where it is made accessible in the request. So this method just returns
	 * the site name from the given request. The method returns null if no site
	 * name was recorded in the request.
	 * 
	 * @param request
	 * @return
	 */
	public static String getSiteName(PortletRequest request) {
		String siteName = null;
		if (request != null) {
			// TODO: Get portal site name from request using new authentication
			// module. If not found in request, return null. A key name for this
			// is defined in the common Consts class - see TODO there.
		}
		return siteName;
	}

	/**
	 * Get the portlet ID from the given portlet request. The portlet ID is the
	 * unique identifier for the portlet instance as provisioned in the portal
	 * (thus it is not an element from the portlet application itself; it is an
	 * element from the portal). For Vignette Portal, this is the "portlet
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
			// TODO: Get portlet ID (currently, the Vignette portlet friendly
			// ID) from the request using the new authentication module. If not
			// found in request, return null. A key name for this is
			// defined in the common Consts class - see TODO there.
		}
		return portletId;
	}
}
