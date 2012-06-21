package com.hp.it.spf.localeresolver.hpweb;

/**
 * Locale mapper utilities based on HP.COM standard.
 *
 * @author Ye Liu (ye.liu@hp.com)
 */
public class HpLocaleMapper
{
	private static final String HPCOM_LANGUAGE_EN_UNITED_KINGDOM = "en-uk";
	private static final String ISO_LANGUAGE_EN_UNITED_KINGDOM = "en-gb";
	private static final String HPCOM_COUNTRY_UNITED_KINGDOM = "uk";
	private static final String ISO_COUNTRY_UNITED_KINGDOM = "gb";

	public static String mapLanguage(String language) {
		if (HPCOM_LANGUAGE_EN_UNITED_KINGDOM.equalsIgnoreCase(language)) {
			return ISO_LANGUAGE_EN_UNITED_KINGDOM;
		} else {
			return language;
		}
	}

	public static String mapCountry(String country) {
		if (HPCOM_COUNTRY_UNITED_KINGDOM.equalsIgnoreCase(country)) {
			return ISO_COUNTRY_UNITED_KINGDOM;
		} else {
			return country;
		}
	}
}
