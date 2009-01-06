/*
 * Project: Service Portal
 * Copyright (c) 2006 HP. All Rights Reserved
 * 
 * 
 * FILENAME: MockLocaleProvider.java
 * PACKAGE : com.hp.it.spf.localeresolver.filter
 * $Id: MockLocaleProvider.java,v 1.2 2007/04/17 01:52:02 marcd Exp $
 * $Log: MockLocaleProvider.java,v $
 * Revision 1.2  2007/04/17 01:52:02  marcd
 * add stuff for javadoc
 *
 *
 */ 
package com.hp.it.spf.localeresolver.mock;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import com.hp.fast.web.hpweb.LocaleProvider;

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
