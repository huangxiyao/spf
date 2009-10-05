/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.mock;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.localeresolver.hpweb.TargetLocaleProvider;

/**
 * @author Scott Jorgenson
 */
public class MockTargetLocaleProvider extends TargetLocaleProvider {
	private Collection locales = Collections.EMPTY_LIST;
	private boolean persistCookies;

	public MockTargetLocaleProvider(HttpServletRequest request) {
		super(request);
	}

	public void setLocales(Collection locales) {
		this.locales = locales;
	}

	public Collection getAllowedLocales() {
		return locales;
	}

	public Collection getAllLocales() {
		return locales;
	}

	public void setPersistSuppliedLocalesAsCookie(boolean persistCookies) {
		this.persistCookies = persistCookies;
	}

	public boolean persistSuppliedLocalesAsCookie() {
		return persistCookies;
	}

}
