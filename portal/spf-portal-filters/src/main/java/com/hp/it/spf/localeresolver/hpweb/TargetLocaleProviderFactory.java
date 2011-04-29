/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.hpweb;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.localeresolver.filter.LocaleFilter;

/**
 * Factory that returns target locale providers. Used by the
 * {@link LocaleFilter} to retrieve the application-specific target locale
 * provider for the current environment.
 * 
 * @author Scott Jorgenson
 * @version $Revision: 2.0 $
 * @since $Revision: 2.0 $
 */
public interface TargetLocaleProviderFactory {
	/**
	 * Returns a locale provider based on the configuration of the current
	 * request.
	 * 
	 * @param request
	 *            the servlet request.
	 * @return the locale provider.
	 */
	TargetLocaleProvider getTargetLocaleProvider(HttpServletRequest request);
}
