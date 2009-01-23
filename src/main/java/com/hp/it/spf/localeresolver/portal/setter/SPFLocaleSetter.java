/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.setter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.it.spf.xa.misc.portal.Consts;

public class SPFLocaleSetter implements ILocaleSetter {
	private ArrayList setters = new ArrayList();
	
	public SPFLocaleSetter() {
		setters.add(new PortalUserLocaleSetter());
		setters.add(new HpDomainCookieLocaleSetter(Consts.COOKIE_NAME_LOCALE));
	}

	public void setLocale(HttpServletRequest request,
			HttpServletResponse response, Locale locale) {
		Iterator itr = setters.iterator();
		while (itr.hasNext()) {
			ILocaleSetter setter = (ILocaleSetter) itr.next();
			setter.setLocale(request, response, locale);
		}
	}

	public boolean shouldResolveLocale(HttpServletRequest request) {
		return true;
	}

}
