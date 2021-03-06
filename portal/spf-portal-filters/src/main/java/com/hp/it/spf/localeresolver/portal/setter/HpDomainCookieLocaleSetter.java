/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.setter;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.util.CookieGenerator;

import com.hp.it.spf.xa.misc.portal.Consts;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class HpDomainCookieLocaleSetter implements ILocaleSetter {
	private String cookieName;
	
	public HpDomainCookieLocaleSetter(String cookieName) {
		this.cookieName = cookieName;
	}
    /**
     * Sets and lang and cc cookies according to the HP.com specification.
     * <p>
     * Note that this setter is not likely to work in a test environment as a
     * test environment will not satisfy the rules for state management
     * specified in RFC2109 sec 4.3.2 -ie cookies are rejected when 'The
     * request-host is a FQDN (not IP address) and has the form HD, where D is
     * the value of the Domain attribute, and H is a string that contains one or
     * more dots.'
     * </p>
     * @param request the http sevlet request
     * @param response the http sevlet response
     * @param locale the locale to set
     */
    public void setLocale(HttpServletRequest request,
            HttpServletResponse response, final Locale locale) {

        CookieGenerator lang = initCookieGenerator(cookieName);
        String code = locale.toString().replace('_', '-');
        if (StringUtils.hasLength(code)) {
            lang.addCookie(response, code);
        }
    }

    private CookieGenerator initCookieGenerator(final String name) {
        CookieGenerator gen = new CookieGenerator();
        gen.setCookieDomain(Consts.HP_COOKIE_DOMAIN);
        gen.setCookieMaxAge(-1);
        gen.setCookieName(name);
        gen.setCookiePath(Consts.HP_COOKIE_PATH);
        return gen;
    }

    /**
     * @param request the http servlet request
     * @return true if the locale needs setting
     */
    public boolean shouldResolveLocale(HttpServletRequest request) {
        return true;
    }

}
