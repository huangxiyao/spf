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
        language = request.getParameter(Consts.PARAM_HPCOM_LANGUAGE);
        country = request.getParameter(Consts.PARAM_HPCOM_COUNTRY);
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
