package com.hp.it.cas.spf.portal.localeresolver.setters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.it.spf.xa.misc.portal.Consts;

public class ServicePortalLocaleSetter implements ILocaleSetter {
	private ArrayList setters = new ArrayList();
	
	public ServicePortalLocaleSetter() {
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
