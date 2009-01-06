/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.provider;

import javax.servlet.http.HttpServletRequest;

import com.hp.fast.web.hpweb.LocaleProvider;
import com.hp.fast.web.hpweb.LocaleProviderFactory;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class PortalRegisteredLocalesProviderFactory implements
        LocaleProviderFactory {

    /**
     * Creates a new locale provider specific to the request
     * @param request the http servlet request
     * @return locale provider
     */
    public LocaleProvider getLocaleProvider(HttpServletRequest request) {
        return new PortalRegisteredLocalesProvider(request);
    }

}
