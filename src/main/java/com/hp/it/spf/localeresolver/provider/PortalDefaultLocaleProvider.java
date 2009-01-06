/*
 * Project: Service Portal
 * Copyright (c) 2006 HP. All Rights Reserved
 * 
 * 
 * FILENAME: VignettePortalDefaultLocaleProvider.java
 * PACKAGE : com.hp.it.spf.localeresolver.provider
 * $Id: PortalDefaultLocaleProvider.java,v 1.2 2007/05/17 07:37:32 marcd Exp $
 * $Log: PortalDefaultLocaleProvider.java,v $
 * Revision 1.2  2007/05/17 07:37:32  marcd
 * add javadoc
 *
 * Revision 1.1  2007/04/20 07:38:29  marcd
 * rename
 *
 * Revision 1.4  2007/04/17 06:45:42  marcd
 * compensate for errors in internationalization code
 *
 * Revision 1.3  2007/04/17 01:41:34  marcd
 * add stuff for javadoc
 *
 *
 */ 
package com.hp.it.spf.localeresolver.provider;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.epicentric.common.website.I18nUtils;
import com.hp.fast.web.hpweb.LocaleProvider;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class PortalDefaultLocaleProvider implements LocaleProvider {
    private HttpServletRequest request;

    /**
     * @param request the http servlet request
     */
    public PortalDefaultLocaleProvider(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * @return either the default locale for the site or the system default
     */
    public Collection getLocales() {
        Locale locale = I18nUtility.getDefaultLocale(request);
        if (locale == null) {
            locale = I18nUtils.getSystemWideDefaultLocale();
        }
        return Collections.singletonList(locale);
    }

}
