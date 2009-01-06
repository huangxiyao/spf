/*
 * Project: Service Portal
 * Copyright (c) 2006 HP. All Rights Reserved
 * 
 * 
 * FILENAME: VignettePortalRegisteredLocalesProvider.java
 * PACKAGE : com.hp.it.spf.localeresolver.provider
 * $Id: PortalRegisteredLocalesProvider.java,v 1.2 2007/05/17 07:37:32 marcd Exp $
 * $Log: PortalRegisteredLocalesProvider.java,v $
 * Revision 1.2  2007/05/17 07:37:32  marcd
 * add javadoc
 *
 * Revision 1.1  2007/04/20 07:38:29  marcd
 * rename
 *
 * Revision 1.4  2007/04/17 01:41:34  marcd
 * add stuff for javadoc
 *
 *
 */ 
package com.hp.it.spf.localeresolver.provider;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import com.hp.fast.web.hpweb.LocaleProvider;
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
