/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 * 
 * Common internationalization/localization utility for both portal and portlet.
 * This is the parent class of both portal I18nUtility and portlet I18nUtility classes.
 */
package com.hp.it.spf.xa.i18n;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;
import com.hp.it.spf.xa.misc.Utils;

/**
 * A concrete base class including methods for internationalization/localization
 * held in common by both portal and portlet frameworks. The portal
 * {@link com.hp.it.spf.xa.i18n.portal.I18nUtility} and portlet
 * {@link com.hp.it.spf.xa.i18n.portlet.I18nUtility} both extend this one.
 * Portal and portlet code should not import or try to instantiate this class,
 * but use the portal or portlet <code>I18nUtility</code> class instead, as
 * appropriate.
 * 
 * @author <link href="liping.yan@hp.com">Liping Yan</link>
 * @author <link href="ying-zhi.wu@hp.com">Oliver</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.i18n.portal.I18nUtility</code><br>
 *      <code>com.hp.it.spf.xa.i18n.portlet.I18nUtility</code>
 */
public class I18nUtility {

	/**
	 * The name of the internationalization configuration file. (The file
	 * extension <code>.properties</code> is assumed by the
	 * {@link com.hp.it.spf.xa.properties.PropertyResourceBundleManager}.)
	 */
	protected static final String I18N_CONFIG_FILE = "i18n_config";

	/**
	 * In the internationalization configuration file, this is the property
	 * keyname for the language codes of locales with family-name-first display
	 * convention. See {@link #getUserDisplayName(String, String, Locale)} for
	 * how this is used.
	 */
	protected static final String I18N_CONFIG_KEY_REVERSE_USERNAME_LANGS = "userDisplayName.reverse.lang";

	/**
	 * Not currently used.
	 */
	protected static final String I18N_CONFIG_KEY_EMAIL_ENCODING_FOR = "emailTemplate.encoding";

	/**
	 * The HPP proprietary language code for traditional Chinese.
	 */
	public static final String HPP_TRAD_CHINESE_LANG = "12";

	/**
	 * The HPP proprietary language code for simplified Chinese.
	 */
	public static final String HPP_SIMP_CHINESE_LANG = "13";

	/**
	 * A control flag for the <code>sortLocales</code> methods, to
	 * sort/display country first and language second.
	 */
	public static final int LOCALE_BY_COUNTRY = 1;

	/**
	 * A control flag for the <code>sortLocales</code> methods, to sort
	 * descending.
	 */
	public static final int LOCALE_DESCENDING = 2;

	/**
	 * Regular expression for an opening or closing
	 * <code>&lt;No_Localization&gt;</code> tag.
	 */
	private static String NO_LOCALIZATION_REGEX = "(?i:</?NO_LOCALIZATION>)";

	/**
	 * Logger for logging errors.
	 */
	private static final Log LOG = LogFactory.getLog(I18nUtility.class);

	/**
	 * The constructor is protected in order to discourage instantiation of this
	 * class by outside classes - they should use the portal
	 * {@link com.hp.it.spf.xa.i18n.portal.I18nUtility} or
	 * {@link com.hp.it.spf.xa.i18n.portlet.I18nUtility} instead.
	 */
	protected I18nUtility() {
	}

	/**
	 * <p>
	 * Get time zone display name localized in long format. If the timezone is
	 * one which supports daylight (summer) time offsets, the display name will
	 * indicate whether the time zone is currently in standard or daylight
	 * (summer) time. This is based on the current time, using the translations
	 * built into the JVM for the {@link java.util.TimeZone} class. Returns null
	 * when given null parameters.
	 * </p>
	 * 
	 * @param timezone
	 *            A time zone.
	 * @param inLocale
	 *            A locale.
	 * @return The current localized timezone display name.
	 */
	public static String getLongTimezoneDisplayName(TimeZone timezone,
			Locale inLocale) {
		return getLongTimezoneDisplayName(null, timezone, inLocale);
	}

	/**
	 * <p>
	 * Get time zone display name localized in long format. If the timezone is
	 * one which supports daylight (summer) time offsets, the display name will
	 * indicate whether the time zone is in standard or daylight (summer) time
	 * at the given time. Returns null when given null parameters (except if the
	 * given time is null, the method assumes the current time for purposes of
	 * the daylight calculation). This uses the translations built into the JVM
	 * for the {@link java.util.TimeZone} class.
	 * </p>
	 * 
	 * @param datetime
	 *            Time to use for deciding whether it is daylight time.
	 * @param timezone
	 *            A time zone.
	 * @param inLocale
	 *            A Locale.
	 * @return The localized timezone display name at that date and time.
	 */
	public static String getLongTimezoneDisplayName(Date datetime,
			TimeZone timezone, Locale inLocale) {
		if (timezone == null || inLocale == null) {
			return null;
		}
		if (datetime == null) {
			return timezone.getDisplayName(timezone.inDaylightTime(new Date()),
					TimeZone.LONG, inLocale);
		} else {
			return timezone.getDisplayName(timezone.inDaylightTime(datetime),
					TimeZone.LONG, inLocale);
		}
	}

	/**
	 * <p>
	 * Get time zone display name localized in short format. If the timezone is
	 * one which supports daylight (summer) time offsets, the display name will
	 * indicate whether the time zone is currently in standard or daylight
	 * (summer) time. This is based on the current time, using the translations
	 * built into the JVM for the {@link java.util.TimeZone} class. Returns null
	 * when given null parameters.
	 * </p>
	 * 
	 * @param timezone
	 *            A time zone.
	 * @param inLocale
	 *            A locale.
	 * @return The current localized timezone display name.
	 */
	public static String getShortTimezoneDisplayName(TimeZone timezone,
			Locale inLocale) {
		return getShortTimezoneDisplayName(null, timezone, inLocale);
	}

	/**
	 * <p>
	 * Get time zone display name localized in short format. If the timezone is
	 * one which supports daylight (summer) time offsets, the display name will
	 * indicate whether the time zone is in standard or daylight (summer) time
	 * at the given time. Returns null when given null parameters (except if the
	 * given time is null, the method assumes the current time for purposes of
	 * the daylight calculation). This uses the translations built into the JVM
	 * for the {@link java.util.TimeZone} class.
	 * </p>
	 * 
	 * @param datetime
	 *            Time to use for deciding whether it is daylight time.
	 * @param timezone
	 *            A time zone.
	 * @param inLocale
	 *            A locale.
	 * @return The localized timezone display name at that date and time.
	 */
	public static String getShortTimezoneDisplayName(Date datetime,
			TimeZone timezone, Locale inLocale) {
		if (timezone == null || inLocale == null) {
			return null;
		}
		if (datetime == null) {
			return timezone.getDisplayName(timezone.inDaylightTime(new Date()),
					TimeZone.SHORT, inLocale);
		} else {
			return timezone.getDisplayName(timezone.inDaylightTime(datetime),
					TimeZone.SHORT, inLocale);
		}
	}

	/**
	 * <p>
	 * Transform HP Passport language code and country code into locale. Returns
	 * null if given a null language code; the given country code is optional
	 * and ignored if null. The returned locale generally corresponds directly
	 * with the given language and country codes. However if the country code is
	 * null, a generic (ie language-only) locale is returned. And if the HPP
	 * proprietary language codes for Traditional or Simplified Chinese are
	 * provided (see {@link #HPP_TRAD_CHINESE_LANG} and
	 * {@link #HPP_SIMP_CHINESE_LANG}), the returned locale must assume Taiwan
	 * Chinese ({@link java.util.Locale#TAIWAN}) or China Chinese ({@link java.util.Locale#CHINA})
	 * respectively, regardless of the given country code.
	 * </p>
	 * 
	 * @param pHppLangCode
	 *            An HPP language code.
	 * @param pHppCountryCode
	 *            An HPP country code.
	 * @return The equivalent locale.
	 */
	public static Locale hppLanguageToLocale(String pHppLangCode,
			String pHppCountryCode) {
		if (pHppLangCode == null) {
			return null;
		}
		pHppLangCode = pHppLangCode.trim().toLowerCase();
		Locale locale = null;
		if (HPP_TRAD_CHINESE_LANG.equalsIgnoreCase(pHppLangCode)) {
			locale = Locale.TAIWAN;
		} else if (HPP_SIMP_CHINESE_LANG.equalsIgnoreCase(pHppLangCode)) {
			locale = Locale.CHINA;
		} else {
			if (pHppCountryCode != null) {
				pHppCountryCode = pHppCountryCode.trim().toUpperCase();
				locale = new Locale(pHppLangCode, pHppCountryCode);
			} else {
				locale = new Locale(pHppLangCode);
			}
		}
		return locale;
	}

	/**
	 * <p>
	 * Transform HP Passport language code into locale. Returns null if given a
	 * null language code. The returned locale generally is a generic (ie
	 * language-only) locale corresponding directly with the given language
	 * code. However, if the HPP proprietary language codes for Traditional or
	 * Simplified Chinese (see {@link #HPP_TRAD_CHINESE_LANG} and
	 * {@link #HPP_SIMP_CHINESE_LANG}) are provided, the returned locale must
	 * assume Taiwan Chinese ({@link java.util.Locale#TAIWAN}) or China
	 * Chinese ({@link java.util.Locale#CHINA}) respectively.
	 * </p>
	 * 
	 * @param pHppLangCode
	 *            An HPP language code.
	 * @return The equivalent locale.
	 */
	public static Locale hppLanguageToLocale(String pHppLangCode) {
		return hppLanguageToLocale(pHppLangCode, null);
	}

	/**
	 * <p>
	 * Transform a locale into the HP Passport language code. Returns null if
	 * given a null locale. Generally the returned HPP language code corresponds
	 * directly with the ISO language code inside the locale. However if the
	 * locale is Taiwan Chinese ({@link java.util.Locale#TAIWAN}) or China
	 * Chinese ({@link java.util.Locale#CHINA}), the returned HPP language
	 * code will be the HPP proprietary value for Traditional or Simplified
	 * Chinese respectively (see {@link #HPP_TRAD_CHINESE_LANG} and
	 * {@link #HPP_SIMP_CHINESE_LANG}).
	 * </p>
	 * 
	 * @param pLocale
	 *            A locale.
	 * @return The equivalent HPP language code.
	 */
	public static String localeToHPPLanguage(Locale pLocale) {
		if (pLocale == null) {
			return null;
		}
		String language = pLocale.getLanguage().trim().toLowerCase();
		String country = pLocale.getCountry().trim().toUpperCase();
		if ((Locale.TAIWAN.getLanguage().equalsIgnoreCase(language))
				&& (Locale.TAIWAN.getCountry().equalsIgnoreCase(country)))
			return HPP_TRAD_CHINESE_LANG;
		if (Locale.CHINA.getLanguage().equalsIgnoreCase(language))
			return HPP_SIMP_CHINESE_LANG;
		return language;
	}

	/**
	 * <p>
	 * Transform an HP Passport language code into the equivalent <a
	 * href="http://www.loc.gov/standards/iso639-2/php/English_list.php">ISO
	 * 639-1</a> language code. Returns null if given a null parameter.
	 * Generally the returned ISO language code corresponds directly with the
	 * given HPP language code. Note that if the HPP language code is for
	 * Traditional or Simplified Chinese (see {@link #HPP_TRAD_CHINESE_LANG} and
	 * {@link #HPP_SIMP_CHINESE_LANG}), then the ISO language code for Chinese ({@link java.util.Locale#CHINESE})
	 * must be returned without distinction.
	 * </p>
	 * 
	 * @param pHppLangCode
	 *            An HPP language code.
	 * @return The equivalent ISO 639-1 language code.
	 */
	public static String hppLanguageToISOLanguage(String pHppLangCode) {
		if (pHppLangCode == null) {
			return null;
		}
		pHppLangCode = pHppLangCode.trim().toLowerCase();
		if (HPP_TRAD_CHINESE_LANG.equalsIgnoreCase(pHppLangCode)
				|| HPP_SIMP_CHINESE_LANG.equalsIgnoreCase(pHppLangCode)) {
			return Locale.CHINESE.getLanguage();
		}
		return pHppLangCode;
	}

	/**
	 * <p>
	 * Transform a locale into the equivalent <a
	 * href="http://www.faqs.org/rfcs/rfc3066.html">RFC 3066</a> language tag.
	 * Returns null if the given parameter is null.
	 * </p>
	 * 
	 * @param pLocale
	 *            A locale.
	 * @return The equivalent RFC 3066 language tag, such as <code>fr-CA</code>
	 *         for Canada French, or <code>fr</code> for generic French.
	 */
	public static String localeToLanguageTag(Locale pLocale) {
		if (pLocale == null) {
			return null;
		}
		String language = pLocale.getLanguage().trim().toLowerCase();
		String country = pLocale.getCountry().trim().toUpperCase();
		String variant = pLocale.getVariant().trim();
		StringBuffer result = new StringBuffer();
		if (language.length() > 0) {
			result.append(language);
			if (country.length() > 0) {
				result.append('-').append(country);
				if (variant.length() > 0) {
					result.append('-').append(variant);
				}
			}
		}
		return result.toString();
	}

	/**
	 * <p>
	 * Transform an <a href="http://www.faqs.org/rfcs/rfc3066.html">RFC 3066</a>
	 * language tag into the equivalent locale. Returns null if the given
	 * parameter is null.
	 * </p>
	 * 
	 * @param pLangTag
	 *            The RFC 3066 language tag, such as <code>fr-CA</code> for
	 *            Canada French or <code>fr</code> for generic French.
	 * @return The equivalent locale.
	 */
	public static Locale languageTagToLocale(String pLangTag) {
		if (pLangTag == null) {
			return null;
		}
		pLangTag = pLangTag.trim().toLowerCase();
		Locale locale = null;
		// RFC 3066 specifies "-" as delimiter but we will accept "_" as
		// well
		String[] effLocale = pLangTag.split("[_-]");
		int length = effLocale.length;
		if (length <= 1) {
			locale = new Locale(effLocale[0].trim());
		} else if (length > 1 && length <= 2) {
			locale = new Locale(effLocale[0].trim(), effLocale[1].trim()
					.toUpperCase());
		} else if (length > 2) {
			locale = new Locale(effLocale[0].trim(), effLocale[1].trim()
					.toUpperCase(), effLocale[2].trim());
		}
		return locale;
	}

	/**
	 * <p>
	 * Sort the given collection of locales alphabetically. The sort is in
	 * ascending order by language first, country second, and variant third.
	 * They are sorted alphabetically according to the display names for those
	 * elements in the system default locale (ie,
	 * {@link java.util.Locale#getDefault()}). The method returns the
	 * collection in the form of an {@link java.util.ArrayList}. If the
	 * collection did not purely contain locales, it is returned unsorted.
	 * </p>
	 * 
	 * @param locales
	 *            Collection of locales.
	 * @return Sorted locales, in the form of an {@link java.util.ArrayList}.
	 */
	public static Collection<Locale> sortLocales(Collection<Locale> locales) {
		return sortLocales(locales, null, 0);
	}

	/**
	 * <p>
	 * Sort the given collection of locales alphabetically according to the
	 * given locale. The sort is in ascending order by language first, country
	 * second, and variant third. They are sorted alphabetically according to
	 * the display names for those elements in the given locale (or the system
	 * default locale, {@link java.util.Locale#getDefault()}, if the given
	 * locale is null). The method returns the collection in the form of an
	 * {@link java.util.ArrayList}. If there is a problem during the sort, the
	 * list is returned unsorted.
	 * </p>
	 * 
	 * @param locales
	 *            Collection of locales.
	 * @param Locale
	 *            The locale in which to alphabetize the collection.
	 * @return Sorted locales, in the form of an {@link java.util.ArrayList}.
	 */
	public static Collection<Locale> sortLocales(Collection<Locale> locales,
			Locale inLocale) {
		return sortLocales(locales, inLocale, 0);
	}

	/**
	 * <p>
	 * Sort the given collection of locales alphabetically according to the
	 * given locale and flags. The sort takes into account the language,
	 * country, and variant according to the flags (a bitmask):
	 * </p>
	 * <ul>
	 * <li>
	 * <p>
	 * If the flags include the {@link #LOCALE_BY_COUNTRY} bit, then the sort is
	 * by country first and language second. Otherwise it is by language first
	 * and country second. (In both cases, any variant is third.)
	 * </p>
	 * </li>
	 * <li>
	 * <p>
	 * If the flags include the {@link #LOCALE_DESCENDING} bit, then the sort is
	 * in descending order. Otherwise it is ascending.
	 * </p>
	 * </li>
	 * </ul>
	 * <p>
	 * The sort is performed in the given locale (the system default locale,
	 * {@link java.util.Locale#getDefault()}, is used if the given locale is
	 * null). The method returns the collection in the form of an
	 * {@link java.util.ArrayList}. If there was an unexpected problem during
	 * the sort, the list is returned unsorted.
	 * </p>
	 * 
	 * @param locales
	 *            Collection of locales.
	 * @param inLocale
	 *            The locale in which to alphabetize the collection.
	 * @param flags
	 *            A bitmask of control flags (see description above).
	 * @return Sorted locales, in the form of an {@link java.util.ArrayList}.
	 */
	public static Collection<Locale> sortLocales(Collection<Locale> locales,
			Locale inLocale, int flags) {

		/**
		 * The comparator class for the <code>sortLocales</code> methods.
		 * 
		 * @author djorgen
		 */
		class LocaleComparator implements Comparator<Locale> {
			int flags = 0;
			Locale inLocale = Locale.getDefault();

			public void setFlags(int pFlags) {
				flags = pFlags;
			}

			public void setInLocale(Locale pInLocale) {
				inLocale = pInLocale;
			}

			public int compare(Locale loc1, Locale loc2) {
				String s1 = "";
				String s2 = "";
				int outcome = 0;
				if (loc1 != null) {
					if ((flags & LOCALE_BY_COUNTRY) == LOCALE_BY_COUNTRY)
						s1 = loc1.getDisplayCountry(inLocale)
								+ loc1.getDisplayLanguage(inLocale)
								+ loc1.getDisplayVariant(inLocale);
					else
						s1 = loc1.getDisplayLanguage(inLocale)
								+ loc1.getDisplayCountry(inLocale)
								+ loc1.getDisplayVariant(inLocale);
				}
				if (loc2 != null) {
					if ((flags & LOCALE_BY_COUNTRY) == LOCALE_BY_COUNTRY)
						s2 = loc2.getDisplayCountry(inLocale)
								+ loc2.getDisplayLanguage(inLocale)
								+ loc2.getDisplayVariant(inLocale);
					else
						s2 = loc2.getDisplayLanguage(inLocale)
								+ loc2.getDisplayCountry(inLocale)
								+ loc2.getDisplayVariant(inLocale);
				}
				if ((flags & LOCALE_DESCENDING) == LOCALE_DESCENDING)
					outcome = s2.compareTo(s1);
				else
					outcome = s1.compareTo(s2);
				return (outcome);
			}
		}

		if (locales == null) {
			return null;
		}
		if (inLocale == null) {
			inLocale = Locale.getDefault();
		}

		// Make the locale comparator.
		LocaleComparator comp = new LocaleComparator();
		comp.setFlags(flags);
		comp.setInLocale(inLocale);

		// Sort the collection.
		List<Locale> list = new ArrayList<Locale>();
		list.addAll(locales);
		try {
			Collections.sort(list, comp);
		} catch (Exception e) {
			// Ignore exception and return unsorted list.
		}
		return list;
	}

	/**
	 * <p>
	 * This is a protected method - portal components should use one of the
	 * {@link com.hp.it.spf.xa.i18n.portal.I18nUtility}
	 * <code>getLocalizedFile*</code> methods, and portlets should use one of
	 * the {@link com.hp.it.spf.xa.i18n.portlet.I18nUtility}
	 * <code>getLocalizedFile*</code> methods.
	 * </p>
	 * 
	 * <p>
	 * Looks up the given base filename in the given path, and returns the
	 * filename as per the boolean switch: either the best-fit localized
	 * filename for the given locale which exists at that path, or the given
	 * base filename. The given base filename may include some path relative to
	 * the given path. If no appropriate file for that filename exists in the
	 * given path, then null is returned. This method also returns null if any
	 * of its required parameters are null. The given path should be an absolute
	 * path (otherwise the file lookup is relative to the current working
	 * directory; specify a blank or null path to search the current working
	 * directory). Note that on a case-sensitive filesystem, this is a
	 * case-sensitive lookup.
	 * </p>
	 * 
	 * <p>
	 * If you set the boolean parameter to false, this method will just verify
	 * that the given base filename exists at the given path (returning the
	 * given filename if so, otherwise returning null). You do not need to
	 * specify the locale parameter in this case; you can set that to null. The
	 * base filename parameter is required.
	 * </p>
	 * 
	 * <p>
	 * If you set the boolean parameter to true, both the base filename and
	 * locale parameters are required. This method will treat the given file and
	 * path as a resource bundle base, and look for the file which best-fits
	 * that base and locale. The method follows the standard Java sequence in
	 * this (see ResourceBundle class documentation) and thus may return a
	 * locale-tagged localized pathname, or the base pathname if that is the
	 * best fit. Whatever the best-fit file, its filename is returned (including
	 * any path which was in the given filename). Otherwise null is returned.
	 * </p>
	 * 
	 * <p>
	 * For example: in <code>/files/html</code> consider that we have the
	 * files <code>foo.htm</code>, <code>foo_fr_CA.htm</code>, and
	 * <code>foo_fr.htm</code>. Then:
	 * </p>
	 * 
	 * <dl>
	 * <dt><code>getLocalizedFileName("/files", "html/foo.htm", Locale.FRENCH, true)</code></dt>
	 * <dd>returns <code>html/foo_fr.htm</code></dd>
	 * <dt><code>getLocalizedFileName("/files", "html/foo.htm", Locale.FRANCE, true)</code></dt>
	 * <dd>returns <code>html/foo_fr.htm</code></dd>
	 * <dt><code>getLocalizedFileName("/files", "/html/foo.htm", Locale.CANADA_FRENCH,
	 * true)</code></dt>
	 * <dd>returns <code>/html/foo_fr_CA.htm</code></dd>
	 * <dt><code>getLocalizedFileName("/files", "/html/foo.htm", Locale.ITALIAN, true)</code></dt>
	 * <dd>returns <code>/html/foo.htm</code></dd>
	 * <dt><code>getLocalizedFileName("/files", "html/foo.htm", null, false)</code></dt>
	 * <dd>returns <code>html/foo.htm</code></dd>
	 * <dt><code>getLocalizedFileName("/files", "html/bar.htm", Locale.FRENCH, true)</code></dt>
	 * <dd>returns <code>null</code></dd>
	 * <dt><code>getLocalizedFileName("/files", "foo.htm", null, false)</code></dt>
	 * <dd>returns <code>null</code></dd>
	 * </dl>
	 * 
	 * <p>
	 * On case-sensitive filesystems, lowercase is assumed for the langage and
	 * variant codes, and uppercase is assumed for the country code. Thus in the
	 * above examples, different results would obtain if <code>foo_fr.htm</code>
	 * and/or <code>foo_fr_CA.htm</code> were tagged with uppercase language
	 * or lowercase country. Be sure your resource bundles follow the
	 * convention.
	 * </p>
	 * 
	 * @param pPath
	 *            The directory in which to search (recommended to use an
	 *            absolute path; otherwise is relative to current working
	 *            directory).
	 * @param pBaseFileName
	 *            The name of the base file to search (may include some path,
	 *            which is treated as relative to the path parameter).
	 * @param pLocale
	 *            The locale (not required if boolean parameter is false).
	 * @param pLocalized
	 *            The version of the file for which to search: the base file
	 *            (false) or the best-fitting localized file (true).
	 * @return The proper filename as per the parameters, or null if no
	 *         qualifying file was found.
	 * 
	 */
	protected static String getLocalizedFileName(String pPath,
			String pBaseFileName, Locale pLocale, boolean pLocalized) {
		if (pBaseFileName == null || (pLocalized == true && pLocale == null)) {
			return null;
		}
		pBaseFileName = pBaseFileName.trim();
		pBaseFileName = Utils.slashify(pBaseFileName);
		if (pPath == null) {
			pPath = "";
		}
		pPath = pPath.trim();
		pPath = Utils.slashify(pPath);
		if (pBaseFileName.length() == 0) {
			return null;
		}
		// Resolve localized file
		if (pLocalized) {
			String localizedFileName = null;
			String fileName = null;
			String extension = null;
			try {
				fileName = pBaseFileName.substring(0,
						pBaseFileName.lastIndexOf(".")).trim();
				extension = pBaseFileName.substring(
						pBaseFileName.lastIndexOf(".")).trim();
			} catch (Exception ex) {
				fileName = pBaseFileName;
				extension = "";
			}

			StringBuffer localizedFile = new StringBuffer(pBaseFileName
					.length() + 10);
			localizedFile.append(fileName);

			String language = pLocale.getLanguage().trim().toLowerCase();
			String country = pLocale.getCountry().trim().toUpperCase();
			String variant = pLocale.getVariant().trim().toLowerCase();

			if (language.length() > 1) {
				localizedFile.append("_").append(language);
				if (country.length() > 1) {
					localizedFile.append("_").append(country);
					if (variant.length() > 1) {
						localizedFile.append("_").append(variant);

						// filename_lang_cc_varint.extension
						localizedFile.append(extension);
						localizedFileName = localizedFile.toString();
						if (fileExists(pPath, localizedFileName)) {
							return localizedFileName;
						}
						localizedFile.delete(localizedFile.lastIndexOf("_"),
								localizedFile.length());
					}

					// filename_lang_cc.extension
					localizedFile.append(extension);
					localizedFileName = localizedFile.toString();
					if (fileExists(pPath, localizedFileName)) {
						return localizedFileName;
					}
					localizedFile.delete(localizedFile.lastIndexOf("_"),
							localizedFile.length());
				}

				// filename_lang.extension
				localizedFile.append(extension);
				localizedFileName = localizedFile.toString();
				if (fileExists(pPath, localizedFileName)) {
					return localizedFileName;
				}
			}
		}

		// filename.extension
		if (fileExists(pPath, pBaseFileName)) {
			return pBaseFileName;
		}
		// all the files do not exist
		return null;
	}

	/**
	 * <p>
	 * This is a protected method - portal components should use one of the
	 * {@link com.hp.it.spf.xa.i18n.portal.I18nUtility}
	 * <code>getLocalizedFile*</code> methods, and portlets should use one of
	 * the {@link com.hp.it.spf.xa.i18n.portlet.I18nUtility}
	 * <code>getLocalizedFile*</code> methods.
	 * </p>
	 * 
	 * <p>
	 * Returns true if the given file exists at the given location, otherwise
	 * false. Returns null if given a null filename. The given path should be an
	 * absolute path, or a path relative to the current working directory (to
	 * check the current working directory, you can leave the file path
	 * parameter blank or null). On case-sensitive filesystems, this is a
	 * case-sensitive lookup.
	 * </p>
	 * 
	 * @param pPath
	 *            the file path
	 * @param pFileName
	 *            the name of the file
	 * @return true or false
	 */
	protected static boolean fileExists(String pPath, String pFileName) {
		boolean result = false;
		if (pFileName == null) {
			return result;
		}
		if (pPath == null) {
			pPath = "";
		}
		pPath = pPath.trim();
		pFileName = pFileName.trim();
		try {
			File file = new File(Utils.slashify(pPath + "/" + pFileName));
			if (file.exists()) {
				result = true;
			}
		} catch (Exception e) {
			// Exception (eg file not found) - ignore and return false.
		}
		return result;
	}

	/**
	 * <p>
	 * Return the locale display name, localized for that same locale. For a
	 * country-specific locale, this will be in the format:
	 * <code><i>language</i>-<i>country</i></code>. For a generic locale
	 * (ie language only, no country), this will be in the format:
	 * <code><i>language</i></code>. In either case, if there is a variant
	 * in the locale, it will be included parenthetically at the end:
	 * <code><i>language</i>-<i>country</i> (<i>variant</i>)</code> or
	 * <code><i>language</i> (<i>variant</i>)</code>. Returns null if the
	 * parameter is null. This uses the translations built into the JVM for the
	 * {@link java.util.Locale} class.
	 * </p>
	 * 
	 * @param locale
	 *            A locale.
	 * @return The localized display name for the locale.
	 */
	public static String getLocaleDisplayName(Locale locale) {
		return getLocaleDisplayName(locale, locale, 0);
	}

	/**
	 * <p>
	 * Return the display name of one locale, localized for another locale. For
	 * a country-specific locale, this will be in the format
	 * <code><i>language</i>-
	 * <i>country</i></code>. For a generic locale
	 * (ie language only, no country), this will be in the format
	 * <code><i>language</i></code>. In either case, if there is a variant
	 * in the locale, it will be included parenthetically at the end:
	 * <code><i>language</i>-<i>country</i> (<i>variant</i>)</code> or
	 * <code><i>language</i> (<i>variant</i>)</code>. Returns null if the
	 * parameter is null. This uses the translations built into the JVM for the
	 * {@link java.util.Locale} class.
	 * </p>
	 * 
	 * @param locale
	 *            A locale.
	 * @param inLocale
	 *            The locale in which to render the display name.
	 * @return The display name for the first locale, localized by the second
	 *         locale.
	 */
	public static String getLocaleDisplayName(Locale locale, Locale inLocale) {
		return getLocaleDisplayName(locale, inLocale, 0);
	}

	/**
	 * <p>
	 * Return the display name of one locale, localized for another locale,
	 * according to the given flags. You can get the locale name formatted
	 * according to the current HP.com Web standard, using this method with the
	 * {@link #LOCALE_BY_COUNTRY} control bit.
	 * </p>
	 * <ul>
	 * <li>
	 * <p>
	 * By default (ie, when the flags are <code>0</code>), the language is
	 * given priority in the display name. For a country-specific locale, this
	 * will be in the format: <code><i>language</i>-<i>country</i></code>.
	 * For a generic locale (ie language only, no country), this will be in the
	 * format: <code><i>language</i></code>.
	 * </p>
	 * </li>
	 * <li>
	 * <p>
	 * When the flags (a bitmask) include the {@link #LOCALE_BY_COUNTRY} bit,
	 * the country is given priority. For a country-specific locale, it will be
	 * in the format: <code><i>country-language</i></code>. For a generic
	 * locale, it will be just: <code><i>language</i></code>. Note this is
	 * the current HP.com Web standard.
	 * </p>
	 * </li>
	 * </ul>
	 * <p>
	 * In either case, if there is a variant in the locale, it will be included
	 * parenthetically at the end:
	 * <code><i>language</i>-<i>country</i> (<i>variant</i>)</code>,
	 * <code><i>country</i>-<i>language</i> (<i>variant</i>)</code>, or
	 * <code><i>language</i> (<i>variant</i>)</code> as appropriate.
	 * Returns null if the parameter is null. This uses the translations built
	 * into the JVM for the {@link java.util.Locale} class.
	 * </p>
	 * 
	 * @param locale
	 *            A locale.
	 * @param inLocale
	 *            A locale in which to render the display name.
	 * @param flags
	 *            Control bits, see description above.
	 * @return The display name for the first locale, localized by the second
	 *         locale.
	 */
	public static String getLocaleDisplayName(Locale locale, Locale inLocale,
			int flags) {
		if (locale == null) {
			return null;
		}
		if (inLocale == null) {
			inLocale = Locale.getDefault();
		}
		String displayLanguage = locale.getDisplayLanguage(inLocale);
		String displayCountry = locale.getDisplayCountry(inLocale);
		String displayVariant = locale.getDisplayVariant(inLocale);
		if (displayLanguage != null)
			displayLanguage = displayLanguage.trim();
		if (displayCountry != null)
			displayCountry = displayCountry.trim();
		if (displayVariant != null)
			displayVariant = displayVariant.trim();
		String displayName = "";
		if ((flags & LOCALE_BY_COUNTRY) == LOCALE_BY_COUNTRY) {
			if (displayCountry.equals("") || displayLanguage.equals(""))
				displayName = displayCountry + displayLanguage;
			else
				displayName = displayCountry + "-" + displayLanguage;
		} else {
			if (displayCountry.equals("") || displayLanguage.equals(""))
				displayName = displayLanguage + displayCountry;
			else
				displayName = displayLanguage + "-" + displayCountry;
		}
		if (!displayVariant.equals("")) {
			displayName += " (" + displayVariant + ")";
		}
		return displayName;
	}

	/**
	 * <p>
	 * Return the whole user name (given name and surname) in the customary
	 * order according to the given locale. Returns null if both of the name
	 * parameters are null. Defaults to the customary Western order (given name
	 * first, then family name). In locales whose language codes are configured
	 * in the <code>i18n_config.properties</code> file, the East Asian order
	 * is used (family name first, then given name).
	 * </p>
	 * <p>
	 * The <code>i18n_config.properties</code> file can be located anywhere
	 * accessible to the system classloader. The property within that file which
	 * is used by this method is {@link #I18N_CONFIG_KEY_REVERSE_USERNAME_LANGS}.
	 * For configuring the property file, please see the file itself - it is
	 * contained inside the SPF common utilities JAR, and generally should never
	 * need to be customized by application developers or administrators.
	 * </p>
	 * 
	 * @param givenName
	 *            Given name.
	 * @param familyName
	 *            Surname (ie family name).
	 * @param inLocale
	 *            A locale.
	 * @return The whole user name in correct order.
	 */
	public static String getUserDisplayName(String givenName,
			String familyName, Locale inLocale) {
		if (givenName == null && familyName == null) {
			return null;
		}
		if (givenName == null) {
			givenName = "";
		}
		if (familyName == null) {
			familyName = "";
		}
		givenName = givenName.trim();
		familyName = familyName.trim();

		// In some locales, only one name may be used.
		if (givenName.length() == 0) {
			return familyName;
		}
		if (familyName.length() == 0) {
			return givenName;
		}
		// Use Western order by default.
		if (inLocale == null) {
			return givenName + " " + familyName;
		}

		// Display name according to language, not country.
		String lang = inLocale.getLanguage().trim().toLowerCase();
		if (lang.length() == 0) {
			return givenName + " " + familyName;
		}

		// the flag whether the order of the name need to be reversed
		boolean reverseFlag = false;

		// get langs from properties files
		String reverseList = null;
		ResourceBundle rb = PropertyResourceBundleManager
				.getBundle(I18N_CONFIG_FILE);
		if (rb == null) {
			LOG.error("I18nUtility: getUserDisplayName failed to open "
					+ I18N_CONFIG_FILE + " properties file");
		} else {
			try {
				reverseList = rb
						.getString(I18N_CONFIG_KEY_REVERSE_USERNAME_LANGS);
			} catch (Exception ex) {
				LOG.warn("I18nUtility: getUserDisplayName failed to find "
						+ I18N_CONFIG_KEY_REVERSE_USERNAME_LANGS
						+ " property in " + I18N_CONFIG_FILE
						+ " properties file");
				LOG.warn("I18nUtility: " + ex.getMessage());
			}
		}

		// the given locale's language is in the reverse list
		if (reverseList != null && !reverseList.trim().equals("")) {
			reverseFlag = Arrays.asList(
					reverseList.toLowerCase().split("[\\s,]*[,][\\s,]*"))
					.contains(lang);
		}

		return reverseFlag ? (familyName + " " + givenName)
				: (givenName + " " + familyName);
	}

	/**
	 * Returns the given message string, with any of the special no-localization
	 * tokens (<code>&lt;No_Localization&gt;</code> and the corresponding end
	 * token <code>&lt;/No_Localization&gt;</code>) removed. These tokens may
	 * be embedded in message properties to indicate to the translator that no
	 * translation should be performed. Since the translator will typically
	 * leave that markup in there, this method will be used to remove it. Note:
	 * the content surrounded by the tokens is retained; only the tokens
	 * themselves are removed.
	 * 
	 * @param msg
	 *            A message string.
	 * @return The message string with no-localization tokens removed.
	 */
	public static String filterNoLocalizationTokens(String msg) {
		if (msg == null) {
			return null;
		}
		return msg.replaceAll(NO_LOCALIZATION_REGEX, "");
	}

	/**
	 * <p>
	 * Get the displayable string for the given date and time, in the given
	 * timezone, localized in short format. This uses the translations and
	 * default date-format patterns built into the JVM for the
	 * {@link java.util.DateFormat} and {@link java.util.SimpleDateFormat}
	 * classes. Returns null when given null parameters (except if you give a
	 * null timezone, GMT is assumed).
	 * </p>
	 * 
	 * @param date
	 *            A date and time.
	 * @param tz
	 *            A timezone in which to format the date and time.
	 * @param inLocale
	 *            A locale.
	 * @return The localized string for that date and time.
	 */
	public static String getShortDisplayDate(Date date, TimeZone tz,
			Locale inLocale) {
		return getDisplayDate(date, tz, inLocale, DateFormat.SHORT);
	}

	/**
	 * <p>
	 * Get the displayable string for the given date and time, in the given
	 * timezone, localized in medium format. This uses the translations and
	 * default date-format patterns built into the JVM for the
	 * {@link java.util.DateFormat} and {@link java.util.SimpleDateFormat}
	 * classes. Returns null when given null parameters (except if you give a
	 * null timezone, GMT is assumed).
	 * </p>
	 * 
	 * @param date
	 *            A date and time.
	 * @param tz
	 *            A timezone in which to format the date and time.
	 * @param inLocale
	 *            A locale.
	 * @return The localized string for that date and time.
	 */
	public static String getMediumDisplayDate(Date date, TimeZone tz,
			Locale inLocale) {
		return getDisplayDate(date, tz, inLocale, DateFormat.MEDIUM);
	}

	/**
	 * <p>
	 * Get the displayable string for the given date and time, in the given
	 * timezone, localized in long format. This uses the translations and
	 * default date-format patterns built into the JVM for the
	 * {@link java.util.DateFormat} and {@link java.util.SimpleDateFormat}
	 * classes. Returns null when given null parameters (except if you give a
	 * null timezone, GMT is assumed).
	 * </p>
	 * 
	 * @param date
	 *            A date and time.
	 * @param tz
	 *            A timezone in which to format the date and time.
	 * @param inLocale
	 *            A locale.
	 * @return The localized string for that date and time.
	 */
	public static String getLongDisplayDate(Date date, TimeZone tz,
			Locale inLocale) {
		return getDisplayDate(date, tz, inLocale, DateFormat.LONG);
	}

	/**
	 * <p>
	 * Get the displayable string for the given date and time, in the given
	 * timezone, localized in full format. This uses the translations and
	 * default date-format patterns built into the JVM for the
	 * {@link java.util.DateFormat} and {@link java.util.SimpleDateFormat}
	 * classes. Returns null when given null parameters (except if you give a
	 * null timezone, GMT is assumed).
	 * </p>
	 * 
	 * @param date
	 *            A date and time.
	 * @param tz
	 *            A timezone in which to format the date and time.
	 * @param inLocale
	 *            A locale.
	 * @return The localized string for that date and time.
	 */
	public static String getFullDisplayDate(Date date, TimeZone tz,
			Locale inLocale) {
		return getDisplayDate(date, tz, inLocale, DateFormat.FULL);
	}

	/**
	 * Private method for getting display date for a particular style: short,
	 * medium, long or full.
	 */
	private static String getDisplayDate(Date date, TimeZone tz,
			Locale inLocale, int style) {
		if (date == null || inLocale == null) {
			return null;
		}
		if (tz == null) {
			tz = TimeZone.getTimeZone("GMT");
		}
		try {
			SimpleDateFormat formatter = (SimpleDateFormat) DateFormat
					.getDateTimeInstance(style, style, inLocale);
			formatter.setTimeZone(tz);
			return formatter.format(date);
		} catch (Exception e1) { // should never happen
			try {
				SimpleDateFormat formatter = (SimpleDateFormat) DateFormat
						.getDateTimeInstance(style, style, Locale.getDefault());
				return formatter.format(date);
			} catch (Exception e2) { // should really never happen
				return date.toString();
			}
		}
	}
}
