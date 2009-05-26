/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.hpweb;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hp.it.spf.xa.misc.portal.Consts;

/**
 * Provides a collection of all locales supported by a target environment, plus
 * a collection of locales allowed by the target.
 * 
 * @author Scott Jorgenson
 * @version $Revision: 2.0 $
 * @since $Revision: 2.0 $
 */
public abstract class TargetLocaleProvider {

	/**
	 * The name of the query parameter which can be used in the URL to force
	 * acceptance of a locale.
	 */
	public static final String ALLOW_LOCALE_PARAM = "allowLocale";

	/**
	 * If the {@link #ALLOW_LOCALE_PARAM} is used in a query string, then if the
	 * value is this (<code>on</code>) or {@link #ALLOW_LOCALE_YES},
	 * locales will be filtered by the set of all locales known to the target,
	 * not just the subset of allowed locales.
	 */
	public static final String ALLOW_LOCALE_ON = "on";

	/**
	 * If the {@link #ALLOW_LOCALE_PARAM} is used in a query string, then if the
	 * value is this (<code>yes</code>) or {@link #ALLOW_LOCALE_ON},
	 * locales will be filtered by the set of all locales known to the target,
	 * not just the subset of allowed locales.
	 */
	public static final String ALLOW_LOCALE_YES = "yes";

	/**
	 * If the {@link #ALLOW_LOCALE_PARAM} is used in a query string, then if the
	 * value is this (<code>off</code>) or {@link #ALLOW_LOCALE_NO},
	 * locales will be filtered by the subset of locales both known and allowed
	 * by the target.
	 */
	public static final String ALLOW_LOCALE_OFF = "off";

	/**
	 * If the {@link #ALLOW_LOCALE_PARAM} is used in a query string, then if the
	 * value is this (<code>no</code>) or {@link #ALLOW_LOCALE_OFF},
	 * locales will be filtered by the subset of locales both known and allowed
	 * by the target.
	 */
	public static final String ALLOW_LOCALE_NO = "no";

	private static final String ALLOW_LOCALE_SESSION_ATTR = Consts.STICKY_SESSION_ATTR_PREFIX
			+ "ALLOW_LOCALE";
	protected HttpServletRequest request;

	/**
	 * Constructor for the target locale provider, given the current HTTP
	 * servlet request.
	 */
	public TargetLocaleProvider(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * Returns a collection of all locales supported by a target environment.
	 * 
	 * @return set of locales or empty set.
	 */
	public abstract Collection getAllLocales();

	/**
	 * Returns a collection of locales allowed by a target environment.
	 * 
	 * @return set of locales or empty set.
	 */
	public abstract Collection getAllowedLocales();

	/**
	 * Returns the proper collection of locales to use for the target
	 * environment: only the supported, allowed locales, or all recognized
	 * locales, depending on whether (and with what value) the special query
	 * parameter has been set. The companion method {@link #allowLocale()} is
	 * used to determine whether to that special flag has been set or unset. The
	 * companion methods {@link #getAllowedLocales()} and
	 * {@link #getAllLocales()} are used to determine the necessary set of
	 * locales to return.
	 * 
	 * @return set of locales or empty set.
	 */
	public Collection getLocales() {
		if (this.allowLocale()) {
			return getAllLocales();
		} else {
			return getAllowedLocales();
		}
	}

	/**
	 * Returns true if the special parameter has been set which allows a locale
	 * to be accepted so long as it is recognized by the target environment,
	 * regardless of whether it is also allowed by the target environment. This
	 * is controlled by the {@link #ALLOW_LOCALE_PARAM} query parameter: if that
	 * parameter's value is {@link #ALLOW_LOCALE_ON} or
	 * {@link #ALLOW_LOCALE_YES} then return true, but if the value is
	 * {@link #ALLOW_LOCALE_OFF} or {@link #ALLOW_LOCALE_NO} then return false.
	 * Note this parameter is tracked in session as a side-effect, so this
	 * method returns true or false if the parameter is not present in the
	 * current request but was present with one of those values in a previous
	 * request. Note that if the parameter value is any other value, it is
	 * ignored.
	 */
	public boolean allowLocale() {
		if (this.request != null) {
			HttpSession session = this.request.getSession();
			if (session != null) {
				// Try the current query parameter first.
				String value = this.request.getParameter(ALLOW_LOCALE_PARAM);
				if (value != null) {
					value = value.trim();
				}
				if (ALLOW_LOCALE_ON.equalsIgnoreCase(value)
						|| ALLOW_LOCALE_YES.equalsIgnoreCase(value)) {
					session.setAttribute(ALLOW_LOCALE_SESSION_ATTR,
							new Boolean(true));
					return true;
				} else if (ALLOW_LOCALE_OFF.equalsIgnoreCase(value)
						|| ALLOW_LOCALE_NO.equalsIgnoreCase(value)) {
					session.setAttribute(ALLOW_LOCALE_SESSION_ATTR,
							new Boolean(false));
					return false;
				}
				// Look for a past query parameter, tracked in session.
				Boolean flag = (Boolean) session
						.getAttribute(ALLOW_LOCALE_SESSION_ATTR);
				if (flag != null) {
					return flag.booleanValue();
				}
			}
		}
		return false;
	}
}
