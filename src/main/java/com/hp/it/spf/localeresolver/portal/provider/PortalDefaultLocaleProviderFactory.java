/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.provider;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.localeresolver.core.hpweb.LocaleProvider;
import com.hp.it.spf.localeresolver.core.hpweb.LocaleProviderFactory;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class PortalDefaultLocaleProviderFactory implements
        LocaleProviderFactory {

    /**
     * Creates a new locale provider specific to the request
     * @param request the http servlet request
     * @return locale provider
     */
    public LocaleProvider getLocaleProvider(HttpServletRequest request) {
        return new PortalDefaultLocaleProvider(request);
    }

}
