package com.hp.fast.web.mock;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import com.hp.fast.web.hpweb.LocaleProvider;

/**
 * 
 * @author marc derosa
 *
 */
public class MockLocaleProvider implements LocaleProvider {
    private Collection locales = Collections.singleton(new Locale("en", "US"));

    public void setLocales(Collection locales) {
        this.locales = locales;
    }
    
    public void addLocale(Locale locale) {
        locales.add(locale);
    }

    public Collection getLocales() {
        return locales;
    }

    public boolean persistSuppliedLocalesAsCookie() {
        return false;
    }

}
