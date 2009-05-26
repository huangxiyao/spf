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
}
