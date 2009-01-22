/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.core.mock;

import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.localeresolver.core.hpweb.LocaleProvider;
import com.hp.it.spf.localeresolver.core.hpweb.LocaleProviderFactory;

/**
 * 
 * @author marcd
 *
 */
public class MockLocaleProviderFactory implements LocaleProviderFactory {
    private MockLocaleProvider localeProvider = new MockLocaleProvider();
    
    public LocaleProvider getLocaleProvider(HttpServletRequest request) {
        return localeProvider;
    }
    
    public void setLocales(Collection locales) {
        localeProvider.setLocales(locales);
    }
    
    public void addLocale(Locale locale) {
        localeProvider.addLocale(locale);
    }

}
