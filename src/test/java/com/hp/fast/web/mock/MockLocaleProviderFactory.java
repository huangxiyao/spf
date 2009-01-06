package com.hp.fast.web.mock;

import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.hp.fast.web.hpweb.LocaleProvider;
import com.hp.fast.web.hpweb.LocaleProviderFactory;

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
