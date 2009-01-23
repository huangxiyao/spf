/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.hpweb;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * A locale provider that parses the language and country from HP.com standard
 * cookies.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public class CookieLocaleProvider extends AbstractLocaleProvider {
    private final String language;

    private final String country;

    /**
     * Creates an HP cookie locale provider.
     * 
     * @param request
     *            the servlet request.
     */
    public CookieLocaleProvider(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        language = getCookieValue(cookies, LANGUAGE);
        country = getCookieValue(cookies, COUNTRY);
    }

    protected String getLanguage() {
        return language;
    }

    protected String getCountry() {
        return country;
    }

    private String getCookieValue(Cookie[] cookies, String cookieName) {
        String value = null;
        if (cookies != null) {
            for (int i = 0; i < cookies.length && value == null; ++i) {
                if (cookieName.equals(cookies[i].getName())) {
                    value = cookies[i].getValue();
                }
            }
        }
        return value;
    }

    public boolean persistSuppliedLocalesAsCookie() {
        return false;
    }

}
