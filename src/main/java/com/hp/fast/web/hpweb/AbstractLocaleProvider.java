/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.fast.web.hpweb;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Abstract base class for HP.com standard locale handling. Subclasses must
 * implement {@link #getCountry()} and {@link #getLanguage()}.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public abstract class AbstractLocaleProvider implements LocaleProvider {
    public static final String LANGUAGE = "lang"; // HP.com standard language
    // query parameter and
    // cookie name.

    public static final String COUNTRY = "cc"; // HP.com standard country query
    // parameter and cookie name.

    private static final Set LANGUAGES = new HashSet();

    private static final Set COUNTRIES = new HashSet();
    static {
        LANGUAGES.addAll(Arrays.asList(Locale.getISOLanguages()));
        COUNTRIES.addAll(Arrays.asList(Locale.getISOCountries()));
    }

    /**
     * Returns the country.
     * 
     * @return the country or null.
     */
    protected abstract String getCountry();

    /**
     * Returns the language.
     * 
     * @return the language or null.
     */
    protected abstract String getLanguage();

    public Collection getLocales() {
        Set locales = new HashSet();
        String language = getLanguage();

        if (language != null) {
            // hp.com and others botch the language to be more than just the
            // language
            if (language.length() > 2) {
                language = language.substring(0, 2);
            }

            language = language.toLowerCase(); // ISO-639

            if (LANGUAGES.contains(language)) {
                String country = getCountry();

                if (country == null) {
                    locales = Collections.singleton(new Locale(language));
                } else {
                    country = country.toUpperCase(); // ISO-3166
                    locales = Collections
                            .singleton(COUNTRIES.contains(country) ? new Locale(
                                    language, country)
                                    : new Locale(language));
                }
            }
        }

        return locales;
    }
}
