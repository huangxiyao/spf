/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.mock;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.localeresolver.core.hpweb.LocaleProvider;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class MockLocaleProvider implements LocaleProvider {
    private Collection locales = Collections.EMPTY_LIST;
    private boolean persistCookies;

    public MockLocaleProvider(HttpServletRequest request) { }
    
    public void setLocales(Collection locales) {
        this.locales = locales;
    }

    public Collection getLocales() {
        return locales;
    }

    public void setPersistSuppliedLocalesAsCookie(boolean persistCookies) {
        this.persistCookies = persistCookies;
    }

    public boolean persistSuppliedLocalesAsCookie() {
        return persistCookies;
    }

}
