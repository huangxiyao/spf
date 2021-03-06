/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.mock;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.localeresolver.hpweb.TargetLocaleProvider;

/**
 * 
 * @author Scott Jorgenson
 *
 */
public class MockTargetLocaleProvider extends TargetLocaleProvider {
    private Collection locales = Collections.singleton(new Locale("en", "US"));

	public MockTargetLocaleProvider(HttpServletRequest request) {
		super(request);
	}

    public void setLocales(Collection locales) {
        this.locales = locales;
    }
    
    public void addLocale(Locale locale) {
        locales.add(locale);
    }

    public Collection getAllowedLocales() {
        return locales;
    }

    public Collection getAllLocales() {
        return locales;
    }

    public boolean persistSuppliedLocalesAsCookie() {
        return false;
    }

}
