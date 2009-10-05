/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.http;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.springframework.util.StringUtils;

/**
 * Negotiation utilities.
 * 
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public final class Negotiators {

	// msd: dont allow instantiation
	private Negotiators() {
	}

	/**
	 * Returns the best locale match between two sets of locales. This is
	 * defined as the first subject locale to either exist in the target locales
	 * exactly, or for which a language-only locale matching the subject
	 * locale's language exists in the target locales. This method does not
	 * perform any defaulting of locales.
	 * <p>
	 * For example, if the target locales include China Chinese (<code>zh-CN</code>)
	 * and Japanese (<code>ja</code>), and the subject locales are China
	 * Chinese (<code>zh-CN</code>) and Japan Japanese (<code>ja-JP</code>)
	 * (in that order), then China Chinese (<code>zh-CN</code>) would be
	 * returned. If the subject locales were reversed, then Japanese (<code>ja</code>)
	 * would be returned because - although the subject locale, Japan Japanese (<code>ja-JP</code>),
	 * does not exist in the target locales - its language-only locale, Japanese (<code>ja</code>),
	 * does.
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
		for (Iterator s = subjectLocales.iterator(); s.hasNext()
				&& resolvedLocale == null;) {
			Locale subjectLocale = (Locale) s.next();
			resolvedLocale = matchLocale(subjectLocale, targetLocales);
		}

		return resolvedLocale;
	}

	/**
	 * Returns the best locale match between two sets of locales, taking
	 * defaults into account. The best match is defined as the first subject
	 * locale to either exist in the target locales exactly, or for which a
	 * language-only locale matching the subject locale's language exists in the
	 * target locales. If still no match is found, the subject locale is mapped
	 * to a default locale, and the best match for it is returned.
	 * <p>
	 * For example, if the target locales include China Chinese (<code>zh-CN</code>)
	 * and Japanese (<code>ja</code>), and the subject locales are China
	 * Chinese (<code>zh-CN</code>) and Japan Japanese (<code>ja-JP</code>)
	 * (in that order), then China Chinese (<code>zh-CN</code>) would be
	 * returned. If the subject locales were reversed, then Japanese (<code>ja</code>)
	 * would be returned because - although the subject locale, Japan Japanese (<code>ja-JP</code>),
	 * does not exist in the target locales - its language-only locale, Japanese (<code>ja</code>),
	 * does.
	 * <p>
	 * Now consider when the target locales are China Chinese (<code>zh-CN</code>)
	 * and Japan Japanese (<code>ja-JP</code>). In this case, if the subject
	 * locale was Singapore Japanese (<code>ja-SG</code>) then null would
	 * normally be returned. However, if the defaults map Japanese (<code>ja</code>)
	 * to Japan (<code>JP</code>), then Japan Japanese would be returned.
	 * 
	 * @param subjectLocales
	 *            the locales acceptable to the subject.
	 * @param targetLocales
	 *            the locales available in the target.
	 * @param defaultCountriesForLanguages
	 *            the countries to assume for particular languages.
	 * @return the best-match locale or null if no match could be determined.
	 */
	public static Locale resolveLocale(Collection subjectLocales,
			Collection targetLocales, Map defaultCountriesForLanguages) {

		Locale resolvedLocale = null;
		Locale defaultLocale = null;
		String defaultCountry, defaultLanguage;

		// for every subject locale
		for (Iterator s = subjectLocales.iterator(); s.hasNext()
				&& resolvedLocale == null;) {
			Locale subjectLocale = (Locale) s.next();
			resolvedLocale = matchLocale(subjectLocale, targetLocales);
			if (resolvedLocale == null) {
				defaultLanguage = subjectLocale.getLanguage().toLowerCase();
				defaultCountry = (String) defaultCountriesForLanguages
						.get(defaultLanguage);
				if (defaultCountry != null) {
					defaultCountry = defaultCountry.toUpperCase();
					defaultLocale = new Locale(defaultLanguage, defaultCountry);
					resolvedLocale = matchLocale(defaultLocale, targetLocales);
				}
			}
		}

		return resolvedLocale;
	}

	/**
	 * Match the subject locale against the target locales as follows: try for
	 * an exact match first, then try for a language-only match. Return the
	 * matched locale if a match occurred, or null if no match.
	 */
	private static Locale matchLocale(Locale subjectLocale,
			Collection targetLocales) {

		Locale resolvedLocale = null;
		// does the target provide an exact match
		if (targetLocales.contains(subjectLocale)) {
			resolvedLocale = subjectLocale;
		} else if (StringUtils.hasText(subjectLocale.getCountry())) {
			// does the target provide a language-only locale that matches
			// the subject locale language
			Locale languageOnlySubjectLocale = new Locale(subjectLocale
					.getLanguage());
			if (targetLocales.contains(languageOnlySubjectLocale)) {
				resolvedLocale = languageOnlySubjectLocale;
			}
		}
		return resolvedLocale;
	}

}
