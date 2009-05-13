/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.provider;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import com.epicentric.common.website.I18nUtils;
import com.hp.it.spf.localeresolver.hpweb.TargetLocaleProvider;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;

/**
 * A target locale provider class for Vignette portal in the SPF architecture.
 * 
 * @author <link href="marc.derosa@hp.com"></link>
 * @author Scott Jorgenson
 * @version $Revision 2.0 $
 */
public class PortalRegisteredLocalesProvider implements TargetLocaleProvider {
	private HttpServletRequest request;

	/**
	 * 
	 * @param request
	 *            the http servlet request
	 */
	public PortalRegisteredLocalesProvider(HttpServletRequest request) {
		this.request = request;
	}

	/**
	 * @return all locales that the current portal site allows. May return an
	 *         empty list.
	 */
	public Collection getAllowedLocales() {
		Collection locales = I18nUtility.getAvailableLocales(request);
		if (locales == null) {
			locales = Collections.EMPTY_LIST;
		}
		return locales;
	}

	/**
	 * @return all locales that the current portal server supports. May return
	 *         an empty list.
	 */
	public Collection getAllLocales() {
		Collection locales = I18nUtils.getRegisteredLocales();
		if (locales == null) {
			locales = Collections.EMPTY_LIST;
		}
		return locales;
	}
}
