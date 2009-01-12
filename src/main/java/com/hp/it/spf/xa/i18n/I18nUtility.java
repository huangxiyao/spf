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
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;

/**
 * A concrete base class including methods for internationalization/localization
 * held in common by both portal and portlet frameworks. The portal and portlet
 * I18nUtility classes extend this one. Portal and portlet code should not
 * import this class, but import the portal or portlet I18nUtility class
 * instead.
 * 
 * @author <link href="liping.yan@hp.com">Liping Yan</link>
 * @author <link href="ying-zhi.wu@hp.com">Oliver</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see com.hp.it.cas.spf.portal.common.utils.I18nUtility
 *      com.hp.it.cas.spf.portlet.common.utils.I18nUtility
 */

public class I18nUtility {

	/**
	 * The name of the internationalization configuration file. (The file
	 * extension .properties is assumed by the PropertyResourceBundleManager.)
	 */
	public static final String I18N_CONFIG_FILE = "i18n_config";

	/**
	 * Various properties in the internationalization configuration file. See
	 * file for definition.
	 */
	public static final String I18N_CONFIG_KEY_REVERSE_USERNAME_LANGS = "userDisplayName.reverse.lang";
	public static final String I18N_CONFIG_KEY_EMAIL_ENCODING_FOR = "emailTemplate.encoding";

	/**
	 * The HPP proprietary language code for traditional Chinese.
	 */
	public static final String HPP_TRAD_CHINESE_LANG = "12";

	/**
	 * The HPP proprietary language code for simplified Chinese.
	 */
	public static final String HPP_SIMP_CHINESE_LANG = "13";

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
	 * Protected to prevent external construction except by subclasses. Use the
	 * portal or portlet I18nUtility class instead.
	 */
	protected I18nUtility() {

	}

	/**
	 * <p>
	 * Get time zone display name localized in long format. If the timezone is
	 * one which supports daylight (summer) time offsets, the display name will
	 * indicate whether the time zone is currently in standard or daylight
	 * (summer) time. This is based on the current time. Returns null when given
	 * null parameters.
	 * </p>
	 * 
	 * @param pTz
	 *            A time zone.
	 * @param pLocale
	 *            A locale.
	 * @return The current localized timezone display name.
	 */
	public static String getLongTimezoneDisplayName(TimeZone pTz, Locale pLocale) {
		return getLongTimezoneDisplayName(null, pTz, pLocale);
	}

	/**
	 * <p>
	 * Get time zone display name localized in long format. If the timezone is
	 * one which supports daylight (summer) time offsets, the display name will
	 * indicate whether the time zone is in standard or daylight (summer) time
	 * at the given time. Returns null when given null parameters (except if the
	 * given time is null, the method assumes the current time for purposes of
	 * the daylight calculation).
	 * </p>
	 * 
	 * @param pDate
	 *            Time to use for deciding whether it is daylight time.
	 * @param pTz
	 *            A time zone.
	 * @param pLocale
	 *            A Locale.
	 * @return The localized timezone display name at that date and time.
	 */
	public static String getLongTimezoneDisplayName(Date pDate, TimeZone pTz,
			Locale pLocale) {
		if (pDate == null) {
			if (pTz == null || pLocale == null) {
				return null;
			} else {
				return pTz.getDisplayName(pTz.inDaylightTime(new Date()),
						TimeZone.LONG, pLocale);
			}
		}
		return pTz.getDisplayName(pTz.inDaylightTime(pDate), TimeZone.LONG,
				pLocale);
	}

	/**
	 * <p>
	 * Get time zone display name localized in short format. If the timezone is
	 * one which supports daylight (summer) time offsets, the display name will
	 * indicate whether the time zone is currently in standard or daylight
	 * (summer) time. This is based on the current time. Returns null when given
	 * null parameters.
	 * </p>
	 * 
	 * @param pTz
	 *            A time zone.
	 * @param pLocale
	 *            A locale.
	 * @return The current localized timezone display name.
	 */
	public static String getShortTimezoneDisplayName(TimeZone pTz,
			Locale pLocale) {
		return getShortTimezoneDisplayName(null, pTz, pLocale);
	}

	/**
	 * <p>
	 * Get time zone display name localized in short format. If the timezone is
	 * one which supports daylight (summer) time offsets, the display name will
	 * indicate whether the time zone is in standard or daylight (summer) time
	 * at the given time. Returns null when given null parameters (except if the
	 * given time is null, the method assumes the current time for purposes of
	 * the daylight calculation).
	 * </p>
	 * 
	 * @param pDate
	 *            Time to use for deciding whether it is daylight time.
	 * @param pTz
	 *            A time zone.
	 * @param pLocale
	 *            A locale.
	 * @return The localized timezone display name at that date and time.
	 */
	public static String getShortTimezoneDisplayName(Date pDate, TimeZone pTz,
			Locale pLocale) {
		if (pDate == null) {
			if (pTz == null || pLocale == null) {
				return null;
			} else {
				return pTz.getDisplayName(pTz.inDaylightTime(new Date()),
						TimeZone.SHORT, pLocale);
			}
		}
		return pTz.getDisplayName(pTz.inDaylightTime(pDate), TimeZone.SHORT,
				pLocale);
	}

	/**
	 * <p>
	 * Transform HP Passport language code and country code into locale. Returns
	 * null if given a null language code; the given country code is optional
	 * and ignored if null. The returned locale generally corresponds directly
	 * with the given language and country codes. However if the country code is
	 * null, a generic (ie language-only) locale is returned. And if the HPP
	 * proprietary language codes for Traditional or Simplified Chinese are
	 * provided ("12" or "13"), the returned locale must assume Taiwan Chinese
	 * or China Chinese respectively, regardless of the given country code.
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
			locale = new Locale("zh", "TW");
		} else if (HPP_SIMP_CHINESE_LANG.equalsIgnoreCase(pHppLangCode)) {
			locale = new Locale("zh", "CN");
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
	 * Simplified Chinese ("12" or "13") are provided, the returned locale must
	 * assume Taiwan Chinese or China Chinese respectively.
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
	 * locale is Taiwan Chinese or China Chinese, the returned HPP language code
	 * will be the HPP proprietary value for Traditional or Simplified Chinese
	 * respectively ("12" or "13").
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
		if ("zh".equalsIgnoreCase(language)) {
			if ("TW".equalsIgnoreCase(country)) {
				return HPP_TRAD_CHINESE_LANG;
			} else if ("CN".equalsIgnoreCase(country)) {
				return HPP_SIMP_CHINESE_LANG;
			}
		}
		return language;
	}

	/**
	 * <p>
	 * Transform an HP Passport language code into the equivalent ISO 639-1
	 * language code. Returns null if given a null parameter. Generally the
	 * returned ISO language code corresponds directly with the given HPP
	 * language code. Note that if the HPP language code is for Traditional or
	 * Simplified Chinese (ie "12" or "13"), then the ISO language code for
	 * Chinese ("zh") must be returned without distinction.
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
			return "zh";
		}
		return pHppLangCode;
	}

	/**
	 * <p>
	 * Transform a locale into the equivalent RFC 3066 language tag. Returns
	 * null if the given parameter is null.
	 * </p>
	 * 
	 * @param pLocale
	 *            A locale.
	 * @return The equivalent RFC 3066 language tag, such as "fr-CA" for Canada
	 *         French, or "fr" for generic French.
	 */
	public static String localeToLanguageTag(Locale pLocale) {
		if (pLocale == null) {
			return null;
		}
		String language = pLocale.getLanguage().trim().toLowerCase();
		String country = pLocale.getCountry().trim().toUpperCase();
		StringBuffer result = new StringBuffer(4);
		if (language.length() > 0) {
			result.append(language);
			if (country.length() > 0) {
				result.append('-').append(country);
			}
		} else {
			result.append(country);
		}
		return result.toString();
	}

	/**
	 * <p>
	 * Transform an RFC 3066 language tag into the equivalent locale. Returns
	 * null if the given parameter is null.
	 * </p>
	 * 
	 * @param pLangTag
	 *            The RFC 3066 language tag, such as "fr-CA" for Canada French
	 *            or "fr" for generic French.
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
	 * Sort any Collection of locales in ascending order by RFC 3066 language
	 * tag value. Returns the Collection in the form of an ArrayList. If the
	 * collection did not purely contain locales, it is returned unsorted.
	 * </p>
	 * 
	 * @param pLocales
	 *            Collection of locales.
	 * @return Sorted locales, in the form of an ArrayList.
	 */
	public static Collection sortLocales(Collection pLocales) {
		if (pLocales == null) {
			return null;
		}
		List list = new ArrayList();
		list.addAll(pLocales);
		try {
			Collections.sort(list, new Comparator() {
				public int compare(Object o1, Object o2) {
					return (((Locale) o1).getLanguage().trim().toLowerCase()
							+ "_" + ((Locale) o1).getCountry().trim()
							.toUpperCase()).compareTo(((Locale) o2)
							.getLanguage().trim().toLowerCase()
							+ "_"
							+ ((Locale) o2).getCountry().trim().toUpperCase());
				}
			});
		} catch (Exception e) {
			// Ignore exception and return unsorted list.
		}
		return list;
	}

	/**
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
	public static String getLocalizedFileName(String pPath,
			String pBaseFileName, Locale pLocale, boolean pLocalized) {
		if (pBaseFileName == null || (pLocalized == true && pLocale == null)) {
			return null;
		}
		pBaseFileName = pBaseFileName.trim();
		if (pPath == null) {
			pPath = "";
		}
		pPath = pPath.trim();
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
			File file = new File(slashify(pPath + "/" + pFileName));
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
	 * Returns the given path, with any consecutive file separators ("/" for
	 * Java) reduced to just one.  The given path is also trimmed of
	 * whitespace.
	 * </p>
	 * 
	 * @param pPath
	 *            The file path to clean-up.
	 * @return The cleaned-up file path.
	 */
	protected static String slashify(String pPath) {
		if (pPath == null) {
			return null;
		}
		pPath = pPath.trim();
		return pPath.replaceAll("/+", "/");
	}

	/**
	 * <p>
	 * Return the locale display name, localized for that same locale. For a
	 * country-specific locale, this will be in the format "language (country)".
	 * For a generic locale (ie language only, no country), this will be in the
	 * format "language". Returns null if the parameter is null.
	 * </p>
	 * 
	 * @param pLocale
	 *            A locale.
	 * @return The localized display name for the locale.
	 */
	public static String getLocaleDisplayName(Locale pLocale) {
		return getLocaleDisplayName(pLocale, pLocale);
	}

	/**
	 * <p>
	 * Return the display name of one locale, localized for another locale. For
	 * a country-specific locale, this will be in the format "language
	 * (country)". For a generic locale (ie language only, no country), this
	 * will be in the format "language". Returns null if either parameter is
	 * null.
	 * </p>
	 * 
	 * @param pLocale1
	 *            A locale.
	 * @param pLocale2
	 *            The locale in which to render the display name.
	 * @return The display name for the first locale, localized by the second
	 *         locale.
	 */
	public static String getLocaleDisplayName(Locale pLocale1, Locale pLocale2) {
		if (pLocale1 == null || pLocale2 == null) {
			return null;
		}
		return pLocale1.getDisplayName(pLocale2);
	}

	/**
	 * <p>
	 * Return the whole user name (given name and surname) in the customary
	 * order according to the given locale. Returns null if both of the name
	 * parameters are null. Defaults to the customary Western order (given name
	 * then surname).
	 * </p>
	 * 
	 * @param givenName
	 *            Given name.
	 * @param surName
	 *            Surname (ie family name).
	 * @param locale
	 *            A locale.
	 * @return The whole user name in correct order.
	 */
	public static String getUserDisplayName(String fn, String ln, Locale locale) {
		if (fn == null && ln == null) {
			return null;
		}
		fn = fn.trim();
		ln = ln.trim();

		// In some locales, only one name may be used.
		if (fn.length() == 0) {
			return ln;
		}
		if (ln.length() == 0) {
			return fn;
		}
		// Use Western order by default.
		if (locale == null) {
			return fn + " " + ln;
		}

		// Display name according to language, not country.
		String lang = locale.getLanguage().trim().toLowerCase();
		if (lang.length() == 0) {
			return fn + " " + ln;
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

		return reverseFlag ? (ln + " " + fn) : (fn + " " + ln);
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
}
