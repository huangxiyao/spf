/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.mock;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.it.spf.localeresolver.portal.setter.ILocaleSetter;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class MockLocaleSetter implements ILocaleSetter {
    private boolean localeSet;
    private String language = "";
    private String country = "";

    public void setLocale(HttpServletRequest request,
            HttpServletResponse response, Locale locale) {
        localeSet = true;
        language = locale.getLanguage();
        country = locale.getCountry();
    }

    public boolean validate(String expectedLanguage, String expectedCountry) {
        return (localeSet && language.equals(expectedLanguage) && country
                .equals(expectedCountry));
    }

    public boolean shouldResolveLocale(HttpServletRequest request) {
        return true;
    }
}
