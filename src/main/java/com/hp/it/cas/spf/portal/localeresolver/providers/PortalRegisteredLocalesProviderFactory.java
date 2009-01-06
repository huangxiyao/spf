/*
 * Project: Service Portal
 * Copyright (c) 2006 HP. All Rights Reserved
 * 
 * 
 * FILENAME: VignettePortalRegisteredLocalesProviderFactory.java
 * PACKAGE : com.hp.it.cas.spf.portal.localeresolver.providers
 * $Id: PortalRegisteredLocalesProviderFactory.java,v 1.2 2007/05/17 07:30:08 marcd Exp $
 * $Log: PortalRegisteredLocalesProviderFactory.java,v $
 * Revision 1.2  2007/05/17 07:30:08  marcd
 * add javadoc
 *
 * Revision 1.1  2007/04/20 07:38:29  marcd
 * rename
 *
 * Revision 1.2  2007/04/17 01:41:34  marcd
 * add stuff for javadoc
 *
 *
 */ 
package com.hp.it.cas.spf.portal.localeresolver.providers;

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
