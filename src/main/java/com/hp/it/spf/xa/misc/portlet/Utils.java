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
	 * expression for regular.
	 */
	private static final String REGEXP = "(1[0-2]{1}|[1-9]{1}"
			+ "|0[1-9]{1})/(([0-2]{0,1}\\d{1})|("
			+ "3[0-1]{1}))/(\\d{4})\\s((1[0-2]{1})"
			+ "|([0]{0,1}[0-9]{1})):([0-5]{0,1}\\d{1}):"
			+ "([0-5]{0,1}\\d{1})\\s([aApP]{1}[mM]{1})";

	/**
	 * const for month.
	 */
	private static final int MONTH = 12;

	/**
	 * const for temp year.
	 */
	private static final int TEMPYEAR = 5;

	/**
	 * const for temp time.
	 */
	private static final int TEMPTIME = 3;

	/**
	 * Date format used to format the date in String.
	 */
	private static final String DATEFORMAT_12 = "DD-MM-YYYY HH:mm PM/AM";

	/**
	 * Date format used to format the date in String.
	 */
	private static final String DATEFORMAT_24 = "HH:mm DD MM YYYY";

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
			Object o = request.getAttribute(PortletRequest.USER_INFO);
			if (o != null) {
				Map userMap = (Map) o;
				return (String) userMap.get(key.trim());
			}
		}
		return null;
	}

}
