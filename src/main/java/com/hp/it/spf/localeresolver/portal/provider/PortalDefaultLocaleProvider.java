/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.provider;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.epicentric.common.website.I18nUtils;
import com.hp.it.spf.localeresolver.core.hpweb.LocaleProvider;
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
