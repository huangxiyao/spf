/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.core.hpweb;

import javax.servlet.http.HttpServletRequest;

/**
 * A locale provider that parses the language and country from HP.com standard
 * query parameters.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public class UrlLocaleProvider extends AbstractLocaleProvider {
    private final String language;

    private final String country;

    /**
     * Creates an HP query parameter locale provider.
     * 
     * @param request
     *            the servlet request.
     */
    public UrlLocaleProvider(HttpServletRequest request) {
        language = request.getParameter(LANGUAGE);
        country = request.getParameter(COUNTRY);
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
