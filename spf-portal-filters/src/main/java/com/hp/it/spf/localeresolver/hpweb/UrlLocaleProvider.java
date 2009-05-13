/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.hpweb;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.xa.misc.portal.Consts;

/**
 * A locale provider that parses the language and country from HP.com standard
 * query parameters.
 * 
 * @author Quintin May
 * @author Scott Jorgenson
 * @version $Revision: 2.0 $
 */
public class UrlLocaleProvider extends AbstractLocaleProvider {

	private final String language;

	private final String country;

	private HttpServletRequest request;

	/**
	 * The name of the query parameter which can be used in the URL to force
	 * adoption of the locale in the URL so long as it is known to the target
	 * environment - even if it is not otherwise allowed in the target
	 * environment.
	 */
	public static final String ALLOW_LOCALE_PARAM = "allowLocale";

	/**
	 * If the {@link #ALLOW_LOCALE_PARAM} is used in a query string, then its
	 * valid values are this (<code>on</code>) or {@link #ALLOW_LOCALE_YES}.
	 * Other values are invalid and will be treated as if the query parameter
	 * had not been used.
	 */
	public static final String ALLOW_LOCALE_ON = "on";

	/**
	 * If the {@link #ALLOW_LOCALE_PARAM} is used in a query string, then its
	 * valid values are this (<code>yes</code>) or {@link #ALlOW_LOCALE_ON}.
	 * Other values are invalid and will be treated as if the query parameter
	 * had not been used.
	 */
	public static final String ALLOW_LOCALE_YES = "yes";

	/**
	 * Creates an HP query parameter locale provider.
	 * 
	 * @param request
	 *            the servlet request.
	 */
	public UrlLocaleProvider(HttpServletRequest request) {
		language = request.getParameter(Consts.PARAM_HPCOM_LANGUAGE);
		country = request.getParameter(Consts.PARAM_HPCOM_COUNTRY);
		this.request = request;
	}

	protected String getLanguage() {
		return language;
	}

	protected String getCountry() {
		return country;
	}

	public boolean persistSuppliedLocalesAsCookie() {
		return true;
	}

	/**
	 * Returns true if the special parameter exists in the given request for
	 * allowing a locale to be set via URL regardless of whether that locale
	 * would be allowed by the target environment otherwise. The name of the
	 * parameter is {@link #ALLOW_LOCALE_PARAM}, and its valid values are
	 * {@link #ALLOW_LOCALE_ON} and {@link #ALLOW_LOCALE_YES}
	 * (case-insensitive). Note the parameter at present only works to force a
	 * locale through the {@link UrlLocaleProvider}, not other providers, and
	 * it only works if the locale is known to the target environment.
	 */
	public boolean allowLocale() {
		String value = this.request.getParameter(ALLOW_LOCALE_PARAM);
		if (value != null) {
			value = value.trim();
		}
		if (ALLOW_LOCALE_ON.equalsIgnoreCase(value)
				|| ALLOW_LOCALE_YES.equalsIgnoreCase(value)) {
			return true;
		}
		return false;
	}
}
