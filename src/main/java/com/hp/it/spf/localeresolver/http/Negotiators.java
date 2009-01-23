/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.http;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.springframework.util.StringUtils;

/**
 * Negotiation utilities.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public final class Negotiators {
    
    //msd: dont allow instantiation
    private Negotiators() { }
    /**
     * Returns the best locale match between two sets of locales.
     * 
     * @param subjectLocales
     *            the locales acceptable to the subject.
     * @param targetLocales
     *            the locales available in the target.
     * @return the best-match locale or null if no match could be determined.
     */
    // msd: (subjectLocale.getCountry() != null) --- getCountry will never yield
    // null - ever
    // msd: get rid of extra logic
    public static Locale resolveLocale(Collection subjectLocales,
            Collection targetLocales) {
        Locale resolvedLocale = null;

        // for every subject locale
        for (Iterator s = subjectLocales.iterator(); s.hasNext() && resolvedLocale == null;) {
            Locale subjectLocale = (Locale) s.next();

            // does the target provide an exact match
            if (targetLocales.contains(subjectLocale)) {
                resolvedLocale = subjectLocale;
            } else if (StringUtils.hasLength(subjectLocale.getCountry())) {
                // does the target provide a language-only locale that matches
                // the subject locale language
                Locale languageOnlySubjectLocale = new Locale(subjectLocale
                        .getLanguage());

                if (targetLocales.contains(languageOnlySubjectLocale)) {
                    resolvedLocale = languageOnlySubjectLocale;
                }
            }
        }

        return resolvedLocale;
    }

}
