/*
 * Project: Service Portal
 * Copyright (c) 2006 HP. All Rights Reserved
 * 
 * 
 * FILENAME: MockLocaleSetter.java
 * PACKAGE : com.hp.it.cas.spf.portal.localeresolver.filter
 * $Id: MockLocaleSetter.java,v 1.3 2007/05/29 08:23:49 marcd Exp $
 * $Log: MockLocaleSetter.java,v $
 * Revision 1.3  2007/05/29 08:23:49  marcd
 * modified mock location
 *
 * Revision 1.2  2007/04/17 01:52:02  marcd
 * add stuff for javadoc
 *
 *
 */ 
package com.hp.it.cas.spf.portal.localeresolver.mock;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.it.cas.spf.portal.localeresolver.setters.ILocaleSetter;

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
