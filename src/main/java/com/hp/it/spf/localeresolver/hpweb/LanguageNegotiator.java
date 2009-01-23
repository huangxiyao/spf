/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.hpweb;

import java.util.Collection;
import java.util.Locale;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hp.it.spf.localeresolver.http.ContentNegotiator;
import com.hp.it.spf.localeresolver.http.Negotiators;
import com.hp.it.spf.localeresolver.http.NoAcceptableLanguageException;

/**
 * A language negotiator implementation that incorporates all HP.com language
 * selection criteria, including URL, cookie, HP Passport (optional), and
 * user-agent language settings. Also persists language settings into HP.com
 * specified cookies when appropriate.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public class LanguageNegotiator implements ContentNegotiator {
    private static final String ACCEPTABLE = LanguageNegotiator.class.getName()
            + ".ACCEPTABLE";
    private static final String NEGOTIATED_VALUE = LanguageNegotiator.class
            .getName()
            + ".NEGOTIATED_VALUE";
    private static final String PERSIST_VALUE = LanguageNegotiator.class
            .getName()
            + ".PERSIST_VALUE";
    private static final String TARGET_LOCALES = LanguageNegotiator.class
            .getName()
            + ".TARGET_LOCALES";

    private LocaleProviderFactory targetLocaleProviderFactory = new DefaultLocaleProviderFactory();
    private LocaleProviderFactory passportLocaleProviderFactory;
    private LocaleProviderFactory defaultLocaleProviderFactory;

    private final CookieLocaleSaver cookieLocaleSaver = new CookieLocaleSaver();

    /**
     * Sets the locale provider factory that returns locale providers that
     * return the languages in which the application is localized.
     * 
     * @param targetLocaleProviderFactory
     *            application locale provider factory.
     */
    public void setTargetLocaleProviderFactory(
            LocaleProviderFactory targetLocaleProviderFactory) {
        this.targetLocaleProviderFactory = targetLocaleProviderFactory;
    }

    /**
     * Sets the locale provider factory that returns locale providers that in
     * turn return the user's HP Passport locale settings.
     * 
     * @param passportLocaleProviderFactory
     *            the HP Passport locale provider factory.
     */
    public void setPassportLocaleProviderFactory(
            LocaleProviderFactory passportLocaleProviderFactory) {
        this.passportLocaleProviderFactory = passportLocaleProviderFactory;
    }

    /**
     * Sets the last-resort locale provider factory. The first element returned
     * by {@link LocaleProvider#getLocales()} will be used as the negotiated
     * locale if no other source was able to provide a locale.
     * 
     * @param defaultLocaleProviderFactory
     *            the default locale provider factory.
     */
    public void setDefaultLocaleProviderFactory(
            LocaleProviderFactory defaultLocaleProviderFactory) {
        this.defaultLocaleProviderFactory = defaultLocaleProviderFactory;
    }

    public boolean acceptable(HttpServletRequest request) {
        Boolean acceptable = (Boolean) request.getAttribute(ACCEPTABLE);

        // if we haven't determined this before for this request
        if (acceptable == null) {
            Collection targetLocales = targetLocaleProviderFactory
                    .getLocaleProvider(request).getLocales();
            request.setAttribute(TARGET_LOCALES, targetLocales);

            // check URL
            Locale negotiatedLocale = Negotiators.resolveLocale(
                    new UrlLocaleProvider(request).getLocales(), targetLocales);
            if (negotiatedLocale == null) {
                // check cookies
                negotiatedLocale = Negotiators.resolveLocale(
                        new CookieLocaleProvider(request).getLocales(),
                        targetLocales);

                if (negotiatedLocale == null) {
                    if (passportLocaleProviderFactory != null) {
                        // check HP Passport
                        negotiatedLocale = Negotiators.resolveLocale(
                                passportLocaleProviderFactory
                                        .getLocaleProvider(request)
                                        .getLocales(), targetLocales);
                    }

                    if (negotiatedLocale == null) {
                        // check user agent
                        negotiatedLocale = Negotiators.resolveLocale(
                                new UserAgentLocaleProvider(request)
                                        .getLocales(), targetLocales);

                        if (negotiatedLocale == null
                                && defaultLocaleProviderFactory != null) {
                            // check for a default
                            Collection locales = defaultLocaleProviderFactory
                                    .getLocaleProvider(request).getLocales();
                            if (!locales.isEmpty()) {
                                negotiatedLocale = (Locale) locales.iterator()
                                        .next();
                            }
                        }
                    } else {
                        // write cookies if locale specified in HP Passport
                        request.setAttribute(PERSIST_VALUE, Boolean.TRUE);
                    }
                }
            } else {
                // write cookies if locale specified in URL
                request.setAttribute(PERSIST_VALUE, Boolean.TRUE);
            }

            acceptable = new Boolean(negotiatedLocale != null);
            request.setAttribute(ACCEPTABLE, acceptable);

            if (acceptable.booleanValue()) {
                request.setAttribute(NEGOTIATED_VALUE, negotiatedLocale);
            }
        }

        return acceptable.booleanValue();
    }

    public Object negotiatedValue(HttpServletRequest request) {
        return acceptable(request) ? (Locale) request
                .getAttribute(NEGOTIATED_VALUE) : null;
    }

    // @SuppressWarnings("unchecked")
    public void negotiate(HttpServletRequest request,
            HttpServletResponse response) throws NoAcceptableLanguageException {
        Locale negotiatedLocale = null;

        response.addHeader("Vary", "Cookie Accept-Language");

        if (acceptable(request)) {
            negotiatedLocale = (Locale) request.getAttribute(NEGOTIATED_VALUE);
            Boolean persist = (Boolean) request.getAttribute(PERSIST_VALUE);

            if (persist != null) {
                cookieLocaleSaver.saveLocale(request, response,
                        negotiatedLocale);
            }

            response.setLocale(negotiatedLocale);
        } else {
            throw new NoAcceptableLanguageException(
                    "No acceptable language could be negotiated.",
                    (Collection) request.getAttribute(TARGET_LOCALES));
        }
    }

    /**
     * @author Quintin May
     */
    static class CookieLocaleSaver {
        private static final String DOMAIN = ".hp.com";
        private static final String PATH = "/";

        void saveLocale(HttpServletRequest request,
                HttpServletResponse response, Locale locale) {
            if (locale != null) {
                addCookie(response, AbstractLocaleProvider.LANGUAGE, locale
                        .getLanguage());

                if (locale.getCountry() == null) {
                    deleteCookie(response, AbstractLocaleProvider.COUNTRY);
                } else {
                    addCookie(response, AbstractLocaleProvider.COUNTRY, locale
                            .getCountry());
                }
            }
        }

        private void addCookie(HttpServletResponse response, String name,
                String value) {
            response.addCookie(createCookie(name, value, -1)); // -1 = session
        }

        private void deleteCookie(HttpServletResponse response, String name) {
            response.addCookie(createCookie(name, "", 0));
        }

        private Cookie createCookie(String name, String value, int maxAge) {
            Cookie cookie = new Cookie(name, value);

            cookie.setDomain(DOMAIN);
            cookie.setPath(PATH);
            cookie.setMaxAge(maxAge);

            return cookie;
        }
    }

}
