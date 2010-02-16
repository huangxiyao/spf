/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.hpweb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.hp.it.spf.xa.i18n.portal.I18nUtility;

/**
 * Abstract base class for HP.com standard locale handling. Subclasses must
 * implement {@link #getCountry()} and {@link #getLanguage()}.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public abstract class AbstractLocaleProvider implements LocaleProvider {

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
        List<Locale> locales = new ArrayList<Locale>();
        String language = getLanguage();

        if (language != null) {
        	
        	// if language contains country code, add the resulting locale in the first position of the collection
        	if (language.length() > 2 || language.indexOf("-") > 0) {
        		Locale locale = I18nUtility.languageTagToLocale(language);
        		locales.add(locale);
        		
        	}
        	
            // hp.com and others botch the language to be more than just the
            // language, and add the resulting locale in the second position
            if (language.length() > 2) {
                language = language.substring(0, 2);
            }

            language = language.toLowerCase(); // ISO-639

            if (LANGUAGES.contains(language)) {
                String country = getCountry();

                if (country == null) {
                    locales.add(new Locale(language));
                } else {
                    country = country.toUpperCase(); // ISO-3166
                    locales.add(COUNTRIES.contains(country) 
                    				? new Locale(language, country)
                                    : new Locale(language));
                }
            }
            
        }

        return locales;
    }
}
