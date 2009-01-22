/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.core.hpweb;

import javax.servlet.http.HttpServletRequest;

/**
 * Factory that returns locale providers. Used by the {@link LocaleFilter} to
 * retrieve the application specific HP Passport user profile locale settings.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public interface LocaleProviderFactory {
    /**
     * Returns a locale provider based on the configuration of the current
     * request.
     * 
     * @param request
     *            the servlet request.
     * @return the locale provider.
     */
    LocaleProvider getLocaleProvider(HttpServletRequest request);
}
