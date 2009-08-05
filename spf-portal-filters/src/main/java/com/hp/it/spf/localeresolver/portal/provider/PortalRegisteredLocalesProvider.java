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
public class PortalRegisteredLocalesProvider extends TargetLocaleProvider {

	public PortalRegisteredLocalesProvider(HttpServletRequest request) {
		super(request);
	}

	public PortalRegisteredLocalesProvider(HttpServletRequest request,
			boolean expandLocales) {
		super(request, expandLocales);
	}

	/**
	 * @return All locales that the current portal site allows, possibly
	 *         expanded to include a simple locale for each allowed full locale,
	 *         depending on how the provider was constructed. May return an
	 *         empty list.
	 */
	public Collection getAllowedLocales() {
		Collection locales = I18nUtility.getAvailableLocales(request,
				this.expandLocales);
		if (locales == null) {
			locales = Collections.EMPTY_LIST;
		}
		return locales;
	}

	/**
	 * @return All locales that the current portal server supports. May return
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
