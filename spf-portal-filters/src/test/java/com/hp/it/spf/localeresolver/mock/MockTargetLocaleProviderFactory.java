/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.mock;

import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.localeresolver.hpweb.TargetLocaleProvider;
import com.hp.it.spf.localeresolver.hpweb.TargetLocaleProviderFactory;

/**
 * 
 * @author Scott Jorgenson
 * 
 */
public class MockTargetLocaleProviderFactory implements
		TargetLocaleProviderFactory {
	private MockTargetLocaleProvider targetLocaleProvider = new MockTargetLocaleProvider(
			null);

	public TargetLocaleProvider getTargetLocaleProvider(
			HttpServletRequest request) {
		return targetLocaleProvider;
	}

	public void setLocales(Collection locales) {
		targetLocaleProvider.setLocales(locales);
	}

	public void addLocale(Locale locale) {
		targetLocaleProvider.addLocale(locale);
	}
}
