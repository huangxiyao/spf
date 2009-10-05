/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.spring;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

import com.hp.it.spf.localeresolver.http.ContentNegotiator;

/**
 * A locale resolver that retrieves locales from a
 * {@link com.hp.it.spf.localeresolver.http.ContentNegotiator}.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public class NegotiatedLanguageLocaleResolver extends AbstractLocaleResolver {
    private final ContentNegotiator languageNegotiator;

    /**
     * Creates a locale resolver that gets the locale from the wrapped content
     * negotiator.
     * 
     * @param languageNegotiator
     *            the language negotiator from which to retrieve the locale.
     */
    public NegotiatedLanguageLocaleResolver(ContentNegotiator languageNegotiator) {
        this.languageNegotiator = languageNegotiator;
    }

    public Locale resolveLocale(HttpServletRequest request) {
        Locale locale = (Locale) languageNegotiator.negotiatedValue(request);
        return locale == null ? getDefaultLocale() : locale;
    }

    public void setLocale(HttpServletRequest request,
            HttpServletResponse response, Locale locale) {
        throw new UnsupportedOperationException(
                "Cannot change a negotiated locale.");
    }

}
