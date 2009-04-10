/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.provider;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.localeresolver.hpweb.LocaleProvider;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class PortalRegisteredLocalesProvider implements LocaleProvider {
    private HttpServletRequest request;

    /**
     * 
     * @param request the http servlet request
     */
    public PortalRegisteredLocalesProvider(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * @return all available locales that the site supports.  May return an
     * empty list
     */
    public Collection getLocales() {
        Collection locales = I18nUtility.getAvailableLocales(request);
        if (locales == null) {
            locales = Collections.EMPTY_LIST;
        }
        return locales;
    }

}
