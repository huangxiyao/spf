/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 * 
 */
package com.hp.it.spf.xa.interpolate;

import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;

import com.hp.it.spf.xa.i18n.I18nUtility;
import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;

/**
 * <p>
 * An abstract base class for parsing strings looking for tokens to substitute
 * in portal and portlet contexts. See the concrete portal and portlet
 * subclasses for further information. This base class and its subclasses are
 * used heavily by the base, portal and portlet <code>FileInterpolator</code>
 * classes.
 * </p>
 * <p>
 * The following token substitutions are supported. See the method documentation
 * for further description. <b>Note:</b> The <code>&lt;</code> and
 * <code>&gt;</code> symbols may be used in place of <code>{</code> and
 * <code>}</code>, if you prefer.
 * </p>
 * <dl>
 * <dt><code>{TOKEN:<i>key</i>}</code></dt>
 * <dt><code>{LANGUAGE-CODE}</code></dt>
 * <dt><code>{COUNTRY-CODE}</code></dt>
 * <dt><code>{LANGUAGE-TAG}</code></dt>
 * <dt><code>{CONTENT-URL:</i>path</i>}</code></dt>
 * <dt><code>{LOCALIZED-CONTENT-URL:<i>path</i>}</code></dt>
 * <dt><code>{SITE}</code></dt>
 * <dt><code>{SITE-URL}</code></dt>
 * <dt><code>{REQUEST-URL}</code></dt>
 * <dt><code>{SITE:<i>names</i>}...{/SITE}</code></dt>
 * <dt><code>{LOGGED-IN}...{/LOGGED-IN}</code></dt>
 * <dt><code>{LOGGED-OUT}...{/LOGGED-OUT}</code></dt>
 * <dt><code>{GROUP:<i>groups</i>}...{/GROUP}</code></dt>
 * <dt><code>{EMAIL}</code></dt>
 * <dt><code>{NAME}</code></dt>
 * <dt><code>{USER-PROPERTY:<i>key</i>}</code></dt>
 * </dl>
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see com.hp.it.spf.xa.interpolate.portal.TokenParser<br>
 *      com.hp.it.spf.xa.interpolate.portlet.TokenParser</br>
 *      com.hp.it.spf.xa.interpolate.FileInterpolator<br>
 *      com.hp.it.spf.xa.interpolate.portal.FileInterpolator<br>
 *      com.hp.it.spf.xa.interpolate.portlet.FileInterpolator<br>
 * 
 */
public abstract class TokenParser {

	/**
	 * This class attribute will hold the pathname (relative to the class
	 * loader) of the token-substitution file to use by default. The file
	 * extension <code>.properties</code> is required and assumed.
	 */
	private static final String DEFAULT_SUBS_PATHNAME = "default_tokens";

	/**
	 * This class attribute is the token for the user's <a
	 * href="http://www.loc.gov/standards/iso639-2/php/English_list.php">ISO
	 * 639-1</a> language code.
	 */
	private static final String TOKEN_LANGUAGE_CODE = "LANGUAGE-CODE";

	/**
	 * This class attribute is the token for the user's <a
	 * href="http://www.iso.org/iso/country_codes/iso_3166_code_lists/english_country_names_and_code_elements.htm">ISO
	 * 3166-1</a> country code.
	 */
	private static final String TOKEN_COUNTRY_CODE = "COUNTRY-CODE";

	/**
	 * This class attribute is the token for the user's <a
	 * href="http://www.faqs.org/rfcs/rfc3066.html">RFC 3066</a> language tag.
	 */
	private static final String TOKEN_LANGUAGE_TAG = "LANGUAGE-TAG";

	/**
	 * This class attribute is the token for a localized content URL.
	 */
	private static final String TOKEN_LOCALIZED_CONTENT_URL = "LOCALIZED-CONTENT-URL";

	/**
	 * This class attribute is the token for an unlocalized content URL.
	 */
	private static final String TOKEN_CONTENT_URL = "CONTENT-URL";

	/**
	 * This class attribute is the token for a keyname to be substituted from
	 * the token-substitutions file.
	 */
	private static final String TOKEN_TOKEN = "TOKEN";

	/**
	 * This class attribute is the token for the user's email address.
	 */
	private static final String TOKEN_EMAIL = "EMAIL";

	/**
	 * This class attribute is the token for the user's email address.
	 */
	private static final String TOKEN_NAME = "NAME";

	/**
	 * This class attribute is the user property token.
	 */
	private static final String TOKEN_USER_PROPERTY = "USER-PROPERTY";

	/**
	 * This class attribute is the token for the current portal site.
	 */
	private static final String TOKEN_SITE = "SITE";

	/**
	 * This class attribute is the token for the current portal site URL.
	 */
	private static final String TOKEN_SITE_URL = "SITE-URL";

	/**
	 * This class attribute is the token for the current portal request URL.
	 */
	private static final String TOKEN_REQUEST_URL = "REQUEST-URL";

	/**
	 * This class attribute is the name of the container token for a site
	 * section.
	 */
	private static final String TOKEN_SITE_CONTAINER = "SITE";

	/**
	 * This class attribute is the name of the container token for a group
	 * section.
	 */
	private static final String TOKEN_GROUP_CONTAINER = "GROUP";

	/**
	 * This class attribute is the name of the container token for a logged-in
	 * section.
	 */
	private static final String TOKEN_LOGGEDIN_CONTAINER = "LOGGED-IN";

	/**
	 * This class attribute is the name of the container token for a logged-out
	 * section.
	 */
	private static final String TOKEN_LOGGEDOUT_CONTAINER = "LOGGED-OUT";

	private int containerIndex; // For container parsing.
	private int containerLevel; // For container parsing.
	private String containerNewContent; // For container parsing.
	private String containerOldContent; // For container parsing.
	private String containerToken; // For container parsing.
	private char containerTokenBegin; // For container parsing.
	private char containerTokenEnd; // For container parsing.
	private ContainerMatcher containerMatcher; // For container parsing.

	private static final int FOUND_CONTAINER_START_MATCH = 3;
	private static final int FOUND_CONTAINER_START_NOMATCH = 2;
	private static final int FOUND_CONTAINER_END = 1;
	private static final int FOUND_END = 0;
	private static final String TOKEN_CONTAINER_OR = "|";
	private static char TOKEN_BEGIN = '{';
	private static char TOKEN_END = '}';
	private static char DEPRECATED_TOKEN_BEGIN = '<';
	private static char DEPRECATED_TOKEN_END = '>';

	/**
	 * This class attribute holds the base filename of the token-substitution
	 * file to use.
	 */
	protected String subsFilePath = DEFAULT_SUBS_PATHNAME;

	/**
	 * <p>
	 * Get a URL for the given file pathname, localized (or not) depending on
	 * the boolean parameter (if true, the URL is for the best-candidate
	 * localized version of that file, otherwise it is just for the file
	 * itself). This should return null if the URL cannot be built (eg the file
	 * is not found). Different action by portal and portlet, so therefore this
	 * is an abstract method.
	 * </p>
	 * <p>
	 * The given file pathname should be a base filename and path relative to
	 * the portlet bundle directory.
	 * </p>
	 * 
	 * @param baseFilePath
	 *            A base filename including path relative to portlet bundle
	 *            directory if needed
	 * @param localized
	 *            Decide to localize or not
	 * @return The content URL
	 */
	protected abstract String getContentURL(String baseFilePath,
			boolean localized);

	/**
	 * <p>
	 * Get the value of the given user property. This should return null if the
	 * user property is not found (or is not a string), or the user object is
	 * guest or null (ie the user is not logged in). Different action by portal
	 * and portlet, so therefore this is an abstract method.
	 * </p>
	 * 
	 * @param key
	 *            The property name
	 * @return The property value
	 */
	protected abstract String getUserProperty(String key);

	/**
	 * An inner class interface for deciding whether a content section enclosed
	 * within a container token should be included in the interpolated content.
	 * Different action by portal and portlet, and also different depending on
	 * the type of container token, so therefore this is an interface. It should
	 * be implemented in the concrete subclass for each type of container token
	 * that subclass (portal or portlet) may encounter.
	 */
	protected abstract class ContainerMatcher {
		protected Object subjectOfComparison = null;

		/**
		 * Constructor.
		 * 
		 * @param subject
		 *            An object regarding the current user request against which
		 *            to compare the container keys.
		 */
		protected ContainerMatcher(Object subject) {
			subjectOfComparison = subject;
		}

		/**
		 * Return true if the subject of comparison matches the given container
		 * key. Otherwise return false.
		 * 
		 * @param containerKey
		 *            A key from a container token.
		 * @return true
		 */
		protected abstract boolean match(String containerKey);
	}

	/**
	 * An inner class interface for returning the value to substitute for a
	 * non-container, parameterized token like <code>{TOKEN:key}</code> where
	 * "key" is the parameter passed to the getValue method.
	 */
	protected abstract class ValueProvider {

		/**
		 * Return the value to substitute for the given parameter.
		 * 
		 * @param param
		 *            The part of the token between <code>:</code> and the end
		 *            symbol (<code>}</code> or <code>&gt;</code>).
		 * @return The value to replace the token with.
		 */
		protected abstract String getValue(String param);
	}

	/**
	 * <p>
	 * Parses the given string, substituting the <a
	 * href="http://www.loc.gov/standards/iso639-2/php/English_list.php">ISO
	 * 639-1</a> language code for the given locale in place of the
	 * <code>{LANGUAGE-CODE}</code> token. For example: <code>&lt;a
	 * href="https://ovsc.hp.com?lang={LANGUAGE-CODE}"&gt;go to
	 * OVSC&lt;/a&gt;</code>
	 * is changed to <code>&lt;a
	 * href="https://ovsc.hp.com?lang=ja"&gt;go to OVSC&lt;/a&gt;</code>
	 * when you provide a Japanese-language locale. If you provide a null
	 * locale, the token is replaced with blank. If you provide null content,
	 * null is returned.
	 * </p>
	 * <p>
	 * <b>Note:</b> For the token, you may use <code>&lt;</code> and
	 * <code>&gt;</code> instead of <code>{</code> and <code>}</code>, if
	 * you prefer.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @param loc
	 *            The locale.
	 * 
	 * @return The interpolated string.
	 */
	public String parseLanguageCode(String content, Locale loc) {
		String lang = null;
		if (loc != null) {
			lang = loc.getLanguage();
		}
		return parseUnparameterized(content, TOKEN_LANGUAGE_CODE, lang);
	}

	/**
	 * <p>
	 * Parses the given string, substituting the <a
	 * href="http://www.iso.org/iso/country_codes/iso_3166_code_lists/english_country_names_and_code_elements.htm">ISO
	 * 3166-1</a> country code for the given locale in place of the
	 * <code>{COUNTRY-CODE}</code> token. For example: <code>&lt;a
	 * href="https://ovsc.hp.com?cc={COUNTRY-CODE}"&gt;go to
	 * OVSC&lt;/a&gt;</code>
	 * is changed to <code>&lt;a
	 * href="https://ovsc.hp.com?cc=JP"&gt;go to OVSC&lt;/a&gt;</code>
	 * when you provide a Japan-country locale. If you provide a null locale, or
	 * a locale in which country is not specified, the token is replaced with
	 * blank. If you provide null content, null is returned.
	 * </p>
	 * <p>
	 * <b>Note:</b> For the token, you may use <code>&lt;</code> and
	 * <code>&gt;</code> instead of <code>{</code> and <code>}</code>, if
	 * you prefer.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @param loc
	 *            The locale.
	 * 
	 * @return The interpolated string.
	 */
	public String parseCountryCode(String content, Locale loc) {
		String cc = null;
		if (loc != null) {
			cc = loc.getCountry();
		}
		return parseUnparameterized(content, TOKEN_COUNTRY_CODE, cc);
	}

	/**
	 * <p>
	 * Parses the given string, substituting the given <a
	 * href="href="http://www.faqs.org/rfcs/rfc3066.html">RFC 3066</a> language
	 * tag for the given locale in place of the <code>{LANGUAGE-TAG}</code>
	 * token. For example: <code>&lt;a
	 * href="https://ovsc.hp.com?lang={LANGUAGE-TAG}"&gt;go to
	 * OVSC&lt;/a&gt;</code>
	 * is changed to <code>&lt;a
	 * href="https://ovsc.hp.com?lang=zn-CN"&gt;go to OVSC&lt;/a&gt;</code>
	 * when you provide the Chinese (China) locale. If you provide a null
	 * locale, the token is replaced with blank. If you provide null content,
	 * null is returned.
	 * </p>
	 * <p>
	 * <b>Note:</b> For the token, you may use <code>&lt;</code> and
	 * <code>&gt;</code> instead of <code>{</code> and <code>}</code>, if
	 * you prefer.
	 * </p>
	 * 
	 * @param lang
	 *            The RFC 3066 language tag.
	 * @param content
	 *            The content string.
	 * @return The interpolated string.
	 */
	public String parseLanguageTag(String content, Locale loc) {
		String lang = I18nUtility.localeToLanguageTag(loc);
		return parseUnparameterized(content, TOKEN_LANGUAGE_TAG, lang);
	}

	/**
	 * <p>
	 * Parses the given string, converting the
	 * <code>{LOCALIZED-CONTENT-URL:<i>pathname</i>}</code> token into a URL
	 * for the best-candidate localized form of that <i>pathname</i>. Actual
	 * implementation depends on portal or portlet context (eg for getting the
	 * locale). For example: <code>&lt;img
	 * src="{LOCALIZED-CONTENT-URL:/images/done.jpg}"&gt; is returned as:
	 * &lt;img src="/<i>implementation-dependent</i>/images/done_fr.jpg"&gt;</code>
	 * when the user is French and the French version of <code>done.jpg</code>
	 * (ie <code>done_fr.jpg</code>) exists. If a content URL cannot be
	 * determined (eg the file in the <i>pathname</i> is not found), then the
	 * token is replaced with blank. If you provide null content, null is
	 * returned.
	 * </p>
	 * <p>
	 * <b>Note:</b> For the token, you may use <code>&lt;</code> and
	 * <code>&gt;</code> instead of <code>{</code> and <code>}</code>, if
	 * you prefer.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @return The interpolated string.
	 */
	public String parseLocalizedContentURL(String content) {
		return parseParameterized(content, TOKEN_LOCALIZED_CONTENT_URL,
				new ValueProvider() {
					protected String getValue(String param) {
						return getContentURL(param, true);
					}
				});
	}

	/**
	 * <p>
	 * Parses the given string, converting the
	 * <code>{CONTENT-URL:<i>pathname</i>}</code> token into a URL for that
	 * <i>pathname</i>. Localization is not performed. Actual implementation
	 * depends on portal or portlet context. For example:
	 * <code>&lt;img src="{CONTENT-URL:/images/picture.jpg}"&gt;</code> is
	 * returned as:
	 * <code>&lt;img src="<i>implementation-dependent</i>/images/picture.jpg"&gt;</code>.
	 * If a content URL cannot be determined (eg the file in the <i>pathname</i>
	 * is not found), then the token is replaced with blank. If you provide null
	 * content, null is returned.
	 * </p>
	 * <p>
	 * <b>Note:</b> For the token, you may use <code>&lt;</code> and
	 * <code>&gt;</code> instead of <code>{</code> and <code>}</code>, if
	 * you prefer.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @return The interpolated string.
	 */
	public String parseNoLocalizedContentURL(String content) {
		return parseParameterized(content, TOKEN_CONTENT_URL,
				new ValueProvider() {
					protected String getValue(String param) {
						return getContentURL(param, false);
					}
				});
	}

	/**
	 * <p>
	 * Parses the given string, substituting the given email address for the
	 * <code>{EMAIL}</code> token. For example: <code>&lt;a
	 * href="https://ovsc.hp.com?email={EMAIL}"&gt;go to OVSC&lt;/a&gt;</code>
	 * is changed to
	 * <code>&lt;a href="https://ovsc.hp.com?email=me@foo.com"&gt;go to
	 * OVSC&lt;/a&gt;</code>
	 * when you provide the email address "me@foo.com". If you provide a null
	 * email, the token is replaced with blank. If you provide null content,
	 * null is returned.
	 * </p>
	 * <p>
	 * <b>Note:</b> For the token, you may use <code>&lt;</code> and
	 * <code>&gt;</code> instead of <code>{</code> and <code>}</code>, if
	 * you prefer.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @param email
	 *            The email address.
	 * @return The interpolated string.
	 */
	public String parseEmail(String content, String email) {
		return parseUnparameterized(content, TOKEN_EMAIL, email);
	}

	/**
	 * <p>
	 * Parses the given string, substituting the given first and last names (in
	 * proper display order for the given locale) in place of the
	 * <code>{NAME}</code> token. For example: <code>Welcome, {NAME}!</code>
	 * is changed to <code>Welcome, <i>first</i> <i>last</i>!</code> for
	 * most locales (and by default - eg if the given locale is null). However
	 * in some Asian locales (eg Japan, China, etc), it would be changed to
	 * <code>Welcome, <i>last</i> <i>first</i>!</code> If you provide null
	 * first and last names, the token is replaced with blank. If you provide
	 * null content, null is returned.
	 * </p>
	 * <p>
	 * <b>Note:</b> For the token, you may use <code>&lt;</code> and
	 * <code>&gt;</code> instead of <code>{</code> and <code>}</code>, if
	 * you prefer.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @param firstName
	 *            The first (given) name.
	 * @param lastName
	 *            The last (family) name.
	 * @param locale
	 *            The locale.
	 * @return The interpolated string.
	 */
	public String parseName(String content, String firstName, String lastName,
			Locale loc) {
		String name = I18nUtility.getUserDisplayName(firstName, lastName, loc);
		return parseUnparameterized(content, TOKEN_NAME, name);
	}

	/**
	 * <p>
	 * Parses the given string, substituting the given site name for the
	 * <code>{SITE}</code> token. The site name is intended to be the name for
	 * the virtual portal site. For example: <code>&lt;a
	 * href="https://ovsc.hp.com?site={SITE}"&gt;go to OVSC&lt;/a&gt;</code>
	 * is changed to <code>{a href="https://ovsc.hp.com?site=acme"&gt;go to
	 * OVSC&lt;/a&gt;</code>
	 * when you provide the site name "acme". If you provide a null site name,
	 * the token is replaced with blank. If you provide null content, null is
	 * returned.
	 * </p>
	 * <p>
	 * <b>Note:</b> For the token, you may use <code>&lt;</code> and
	 * <code>&gt;</code> instead of <code>{</code> and <code>}</code>, if
	 * you prefer.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @param siteName
	 *            The site name.
	 * @return The interpolated string.
	 */
	public String parseSite(String content, String siteName) {
		return parseUnparameterized(content, TOKEN_SITE, siteName);
	}

	/**
	 * <p>
	 * Parses the given string, substituting the given portal site home-page URL
	 * for the <code>{SITE-URL}</code> token. For example: <code>&lt;a
	 * href="{SITE-URL}"&gt;go to home page&lt;/a&gt;</code>
	 * is changed to
	 * <code>&lt;a href="http://portal.hp.com/portal/site/acme/"&gt;go to
	 * home page&lt;/a&gt;</code>
	 * when you provide the site URL "http://portal.hp.com/portal/site/acme/".
	 * If you provide a null site URL, the token is replaced with blank. If you
	 * provide null content, null is returned.
	 * </p>
	 * <p>
	 * <b>Note:</b> For the token, you may use <code>&lt;</code> and
	 * <code>&gt;</code> instead of <code>{</code> and <code>}</code>, if
	 * you prefer.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @param siteURL
	 *            The current portal site home-page URL.
	 * @return The interpolated string.
	 */
	public String parseSiteURL(String content, String siteURL) {
		return parseUnparameterized(content, TOKEN_SITE_URL, siteURL);
	}

	/**
	 * <p>
	 * Parses the given string, substituting the given current portal request
	 * URL for the <code>{REQUEST-URL}</code> token. For example: <code>&lt;a
	 * href="{REQUEST-URL}"&gt;try again&lt;/a&gt;</code>
	 * is changed to
	 * <code>&lt;a href="http://portal.hp.com/portal/site/acme/template.PAGE/?..."&gt;try again&lt;/a&gt;</code>
	 * when you provide the request URL
	 * "http://portal.hp.com/portal/site/acme/template.PAGE/?...". If you
	 * provide a null request URL, the token is replaced with blank. If you
	 * provide null content, null is returned.
	 * </p>
	 * <p>
	 * <b>Note:</b> For the token, you may use <code>&lt;</code> and
	 * <code>&gt;</code> instead of <code>{</code> and <code>}</code>, if
	 * you prefer.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @param requestURL
	 *            The current portal request URL.
	 * @return The interpolated string.
	 */
	public String parseRequestURL(String content, String requestURL) {
		return parseUnparameterized(content, TOKEN_REQUEST_URL, requestURL);
	}

	/**
	 * <p>
	 * Parses the given string, converting the
	 * <code>{USER-PROPERTY:<i>key</i>}</code> token into the value for that
	 * key in the user properties. Actual implementation depends on portal or
	 * portlet context. For example, in the portal context:
	 * <code>Your phone number is: {USER-PROPERTY:day_phone}</code> is
	 * returned as: <code>Your phone number is: 123 456 7890</code> assuming
	 * the <code>day_phone</code> property in the portal user object is set to
	 * that value. An example for the portlet context:
	 * <code>Hello {USER-PROPERTY:user.name.given}!</code> is returned as:
	 * <code>Hello Scott!</code> assuming the <code>user.name.given</code>
	 * property in the portlet request (ie PortletRequest.USER_INFO) is set to
	 * that value.
	 * </p>
	 * <p>
	 * If the given string property is not found (or the user properties are
	 * guest or null - eg the user is not logged-in), then the token is replaced
	 * by blank. If you provide null content, null is returned.
	 * </p>
	 * <p>
	 * <b>Note:</b> Portal and portlet contexts use different property names
	 * for the same elements. The list of property names is not provided here.
	 * </p>
	 * <p>
	 * <b>Note:</b> For the token, you may use <code>&lt;</code> and
	 * <code>&gt;</code> instead of <code>{</code> and <code>}</code>, if
	 * you prefer.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @return The interpolated string.
	 */
	public String parseUserProperty(String content) {
		return parseParameterized(content, TOKEN_USER_PROPERTY,
				new ValueProvider() {
					protected String getValue(String param) {
						return getUserProperty(param);
					}
				});
	}

	/**
	 * <p>
	 * Parses the given string, converting the <code>{TOKEN:<i>key</i>}</code>
	 * token into the value for that key in the token-substitutions file (<code>default_tokens.properties</code>
	 * by default, unless overridden in the constructor). For example:
	 * <code>&lt;img src="{TOKEN:url.image}"&gt;</code> is returned as:
	 * <code>&lt;img src="http://foo.hp.com/images/picture.jpg"&gt;</code>
	 * assuming the token-substitutions file contains the following property:
	 * <code>url.image=http://foo.hp.com/images/picture.jpg</code>. If the
	 * token key is not found in the file, then the token is replaced with
	 * blank. If you provide null content, null is returned.
	 * </p>
	 * <p>
	 * <b>Note:</b> For the token, you may use <code>&lt;</code> and
	 * <code>&gt;</code> instead of <code>{</code> and <code>}</code>, if
	 * you prefer.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @return The interpolated string.
	 */
	public String parseToken(String content) {
		return parseParameterized(content, TOKEN_TOKEN, new ValueProvider() {
			protected String getValue(String param) {
				return getToken(param);
			}
		});
	}

	/**
	 * <p>
	 * Parses the string for any <code>{SITE:<i>names</i>}</code> content;
	 * such content is deleted if the given site name does not match (otherwise
	 * only the special markup is removed). The <i>names</i> may include one or
	 * more site names, delimited by "|" for a logical-or.
	 * <code>{SITE:<i>names</i>}</code> markup may be nested for logical-and
	 * (however since any one site has only one site name, the desire to
	 * logical-and seems unlikely).
	 * </p>
	 * 
	 * <p>
	 * <b>Note:</b> As of this writing, the site names should be Vignette "site
	 * DNS name" strings. The current site name for the request is provided by
	 * Vignette. The match against site names in the token is case-insensitive.
	 * </p>
	 * 
	 * <p>
	 * For example, consider the following content string:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for all sites to display.
	 *  {SITE:abc|def}
	 *  This content is to be displayed only in the abc or def sites.
	 *  {/SITE}
	 * </pre>
	 * 
	 * <p>
	 * If the given site name is ABC, the returned content string is:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for all sites to display.
	 *  
	 *  This content is to be displayed only in the abc or def sites.
	 *     
	 * </pre>
	 * 
	 * <p>
	 * But if the given site name is abcd, the returned content string is:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for all sites to display.
	 *  
	 *  
	 * </pre>
	 * 
	 * <p>
	 * If you provide null content, null is returned. If you provide null or
	 * empty site name, all <code>{SITE:names}</code>-enclosed sections are
	 * removed from the content.
	 * </p>
	 * <p>
	 * <b>Note:</b> For the token, you may use <code>&lt;</code> and
	 * <code>&gt;</code> instead of <code>{</code> and <code>}</code>, if
	 * you prefer.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @param siteName
	 *            The portal site name.
	 * @return The interpolated string.
	 */
	public String parseSiteContainer(String content, String siteName) {

		return parseContainer(content, TOKEN_SITE_CONTAINER,
				new SiteContainerMatcher(siteName));
	}

	/**
	 * <code>ContainerMatcher</code> for site section parsing. The constructor stores a site
	 * name into the class. The match method returns true if the given site name
	 * is an exact match (case-insensitive) of the stored site name.
	 */
	protected class SiteContainerMatcher extends ContainerMatcher {

		protected SiteContainerMatcher(String siteName) {
			super(siteName);
		}

		protected boolean match(String containerKey) {
			String siteName = (String) subjectOfComparison;
			if (siteName != null && containerKey != null) {
				containerKey = containerKey.trim().toUpperCase();
				siteName = siteName.trim().toUpperCase();
				if (!siteName.equals("") && !containerKey.equals("")) {
					if (siteName.equalsIgnoreCase(containerKey))
						return true;
				}
			}
			return false;
		}
	}

	/**
	 * <code>ContainerMatcher</code> for logged-in section parsing. The constructor stores
	 * the login status into the class: true if logged-in, false otherwise. The
	 * match method just returns the login status. The passed container key is
	 * not used and is expected to be null.
	 */
	protected class LoggedInContainerMatcher extends ContainerMatcher {

		protected LoggedInContainerMatcher(boolean loggedIn) {
			super(new Boolean(loggedIn));
		}

		protected boolean match(String containerKey) {
			return ((Boolean) subjectOfComparison).booleanValue();
		}
	}

	/**
	 * <p>
	 * Parses the string for any <code>{LOGGED-IN}</code> tokens; such content
	 * is deleted if the user is not currently logged-in (otherwise only the
	 * special markup is removed).
	 * </p>
	 * 
	 * <p>
	 * For example, consider the following content string:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for everyone.
	 *  {LOGGED-IN}
	 *  This content is only for logged-in users.
	 *  {/LOGGED-IN}
	 * </pre>
	 * 
	 * <p>
	 * When the user is logged-in, the returned content string is:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for everyone.
	 *  
	 *  This content is only for logged-in users.
	 * </pre>
	 * 
	 * <p>
	 * When the user is logged-out, the returned content string is:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for everyone.
	 * </pre>
	 * 
	 * <p>
	 * If you provide null content, null is returned.
	 * </p>
	 * <p>
	 * <b>Note:</b> For the token, you may use <code>&lt;</code> and
	 * <code>&gt;</code> instead of <code>{</code> and <code>}</code>, if
	 * you prefer.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @param loggedIn
	 *            True if the user is logged-in, false otherwise.
	 * @return The interpolated string.
	 */
	public String parseLoggedInContainer(String content, boolean loggedIn) {

		return parseContainer(content, TOKEN_LOGGEDIN_CONTAINER,
				new LoggedInContainerMatcher(loggedIn));
	}

	/**
	 * <code>ContainerMatcher</code> for logged-out section parsing. The constructor stores
	 * the login status into the class: true if logged-in, false otherwise. The
	 * match method just returns the inverse of this. The passed container key
	 * is not used and is expected to be null.
	 */
	protected class LoggedOutContainerMatcher extends ContainerMatcher {

		protected LoggedOutContainerMatcher(boolean loggedIn) {
			super(new Boolean(loggedIn));
		}

		protected boolean match(String containerKey) {
			return !((Boolean) subjectOfComparison).booleanValue();
		}
	}

	/**
	 * <p>
	 * Parses the string for any <code>{LOGGED-OUT}</code> tokens; such
	 * content is deleted if the user is currently logged-in (otherwise only the
	 * special markup is removed).
	 * </p>
	 * 
	 * <p>
	 * For example, consider the following content string:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for everyone.
	 *  {LOGGED-OUT}
	 *  This content is only for logged-out users.
	 *  {/LOGGED-OUT}
	 * </pre>
	 * 
	 * <p>
	 * When the user is logged-in, the returned content string is:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for everyone.
	 * </pre>
	 * 
	 * <p>
	 * When the user is logged-out, the returned content string is:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for everyone.
	 *  
	 *  This content is only for logged-out users.
	 * </pre>
	 * 
	 * <p>
	 * If you provide null content, null is returned.
	 * </p>
	 * <p>
	 * <b>Note:</b> For the token, you may use <code>&lt;</code> and
	 * <code>&gt;</code> instead of <code>{</code> and <code>}</code>, if
	 * you prefer.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @param loggedIn
	 *            True if the user is logged-in, false otherwise.
	 * @return The interpolated string.
	 */
	public String parseLoggedOutContainer(String content, boolean loggedIn) {

		return parseContainer(content, TOKEN_LOGGEDOUT_CONTAINER,
				new LoggedOutContainerMatcher(loggedIn));
	}

	/**
	 * <p>
	 * Parses the string for any <code>{GROUP:<i>groups</i>}</code> content;
	 * such content is deleted if the given user groups do not qualify
	 * (otherwise only the special markup is removed). The <i>groups</i> may
	 * include one or more group names, delimited by "|" for a logical-or.
	 * <code>{GROUP:<i>groups</i>&GT;</code> markup may be nested for
	 * logical-and.
	 * </p>
	 * 
	 * <p>
	 * For example, consider the following content string:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for everyone.
	 *  {GROUP:abc|def}
	 *  This content is only for members of the abc or def groups.
	 *    {GROUP:xyz}
	 *    This content is only for members of both the xyz group 
	 *    and the abc or def groups.
	 *    {/GROUP}
	 *  {/GROUP}
	 * </pre>
	 * 
	 * <p>
	 * If the given user groups include abc, def, and xyz, the returned content
	 * string is:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for everyone.
	 *  
	 *  This content is only for members of the abc or def groups.
	 *  
	 *    This content is only for members of both the xyz group 
	 *    and the abc or def groups.
	 *    
	 *    
	 * </pre>
	 * 
	 * <p>
	 * But if the given user groups include only abc, the returned content
	 * string is:
	 * </p>
	 * 
	 * <pre>
	 *  This content is for everyone.
	 *  
	 *  This content is only for members of the abc or def groups.
	 *  
	 *  
	 * </pre>
	 * 
	 * <p>
	 * If you provide null content, null is returned. If you provide null or
	 * empty groups, all <code>{GROUP}</code>-enclosed sections are removed
	 * from the content.
	 * </p>
	 * <p>
	 * <b>Note:</b> For the token, you may use <code>&lt;</code> and
	 * <code>&gt;</code> instead of <code>{</code> and <code>}</code>, if
	 * you prefer.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @param userGroups
	 *            The user groups.
	 * @return The interpolated string.
	 */
	public String parseGroupContainer(String content, String[] userGroups) {

		return parseContainer(content, TOKEN_GROUP_CONTAINER,
				new GroupContainerMatcher(userGroups));
	}

	/**
	 * <code>ContainerMatcher</code> for group parsing. The constructor stores an array of
	 * group names into the class. The match method returns true if the given
	 * group name exactly matches (case-insensitive) any of the stored group
	 * names.
	 */
	protected class GroupContainerMatcher extends ContainerMatcher {

		protected GroupContainerMatcher(String[] groups) {
			super(groups);
		}

		protected boolean match(String containerKey) {
			String[] groups = (String[]) subjectOfComparison;
			if (groups != null && containerKey != null) {
				containerKey = containerKey.trim();
				if (!containerKey.equals("")) {
					for (int i = 0; i < groups.length; i++) {
						if (groups[i] != null) {
							if (groups[i].trim().equalsIgnoreCase(containerKey)) {
								return true;
							}
						}
					}
				}
			}
			return false;
		}
	}

	/**
	 * Get token key value from token substitutions file, using the
	 * {@link com.hp.it.spf.xa.properties.PropertyResourceBundleManager} to
	 * hot-load the properties file from anywhere searched by the class loader.
	 * 
	 * @param key
	 *            token key
	 * @return value
	 * @see {@link com.hp.it.spf.xa.properties.PropertyResourceBundleManager}
	 */
	private String getToken(String key) {
		String tokenValue = null;
		try {
			ResourceBundle resBundle = PropertyResourceBundleManager
					.getBundle(this.subsFilePath);
			if (resBundle != null) {
				tokenValue = resBundle.getString(key.trim());
			}
			return tokenValue;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * <p>
	 * Parses the given content string for any container content denoted by
	 * start and end tokens with the given name; such content is deleted if the
	 * user does not qualify for it (as determined by the given
	 * <code>ContainerMatcher</code>). Container content is denoted by a
	 * start tag like <code>{FOO:keys}</code> and a corresponding end tag like
	 * <code>{/FOO}</code>, where <code>FOO</code> is the given token name,
	 * and <code>keys</code> may include one or more attribute values,
	 * delimited by "|" for a logical-or. The given ContainerMatcher will be
	 * passed those attribute values to determine whether the user data (ie the
	 * subject of comparison) matches any of them. Container markup may be
	 * nested for logical-and. All markup for the particular container tokens is
	 * removed from the returned content regardless.
	 * </p>
	 * <p>
	 * If you provide null or blank content or token name, then the provided
	 * content is simply returned. If you provide a null
	 * <code>ContainerMatcher</code>, then all sections of content enclosed
	 * by the particular container tokens are removed from the content.
	 * Likewise, if the <code>keys</code> in the parsed container start token
	 * are null or blank, then that container-enclosed section will be removed.
	 * </p>
	 * <p>
	 * <b>Note:</b> For the token, this method supports both
	 * <code>&lt;...&gt;</code> and <code>{...}</code> characters.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @param tokenName
	 *            The name of the container token - eg <code>FOO</code> for
	 *            <code>{/FOO}</code> start and end tokens.
	 * @param matcher
	 *            The <code>ContainerMatcher</code>.
	 * @return The interpolated string.
	 */
	protected String parseContainer(String content, String tokenName,
			ContainerMatcher matcher) {

		if (content == null || tokenName == null) {
			return content;
		}
		tokenName = tokenName.toUpperCase().trim();
		if (content.equals("") || tokenName.equals("")) {
			return content;
		}

		// Initialize global variables for "{...}" and "<...>" parsing.
		containerMatcher = matcher;
		containerToken = tokenName;

		// Initialize global variables for "{...}" parsing, then parse.
		containerIndex = 0;
		containerLevel = 0;
		containerOldContent = content;
		containerNewContent = "";
		containerTokenBegin = TOKEN_BEGIN;
		containerTokenEnd = TOKEN_END;
		parseContainerRecursively(true);

		// Initialize global variables for "<...>" parsing (legacy).
		containerIndex = 0;
		containerLevel = 0;
		containerOldContent = containerNewContent;
		containerNewContent = "";
		containerTokenBegin = DEPRECATED_TOKEN_BEGIN;
		containerTokenEnd = DEPRECATED_TOKEN_END;
		parseContainerRecursively(true);

		// Return the parsed content.
		return containerNewContent;
	}

	/**
	 * Parses the container content recursively for parseContainer method. Used
	 * to parse a level block of content.
	 * 
	 * @param isIncluded
	 *            Controls whether contained-enclosed content is to be included
	 *            in the output content or skipped.
	 */
	private void parseContainerRecursively(boolean isIncluded) {

		int result;

		/*
		 * Get each next container token in turn, until end of content. This has
		 * the side -effect of buffering the content to display, as appropriate,
		 * into the containerNewContent global variable. If a start-container
		 * token is found, recurse a level (setting the display control variable
		 * based on whether the container match showed the user was qualified
		 * for the container or not). If an end-container token is found, and we
		 * have recursed at least a level, return - it means we are done parsing
		 * this recursion's job.
		 */
		while ((result = nextContainer((containerLevel == 0) || isIncluded)) != FOUND_END) {
			if (result == FOUND_CONTAINER_START_MATCH) {
				containerLevel++;
				parseContainerRecursively(isIncluded && true);
			} else if (result == FOUND_CONTAINER_START_NOMATCH) {
				containerLevel++;
				parseContainerRecursively(isIncluded && false);
			} else if ((result == FOUND_CONTAINER_END) && (containerLevel > 0)) {
				containerLevel--;
				break;
			}
		}
		return;
	}

	/**
	 * Parses the next container token (either a start or end token) from the
	 * content and returns a result code. The possible codes are: FOUND_END (end
	 * of content), FOUND_CONTAINER_END (found end token),
	 * FOUND_CONTAINER_START_MATCH (found start token and user is qualified to
	 * get the content - ie the ContainerMatch was successful), or
	 * FOUND_CONTAINER_END_MATCH (found end token and user is not qualified - ie
	 * the ContainerMatch failed). This method also has the side-effect of
	 * accumulating the parsed content into the containerNewContent global
	 * variable.
	 * 
	 * @param isIncluded
	 *            Controls whether contained-enclosed content is to be included
	 *            in the output content or skipped.
	 */
	private int nextContainer(boolean isIncluded) {

		int j, k;
		String containerKeyString;
		String[] containerKeys;
		boolean containerMatch;
		String startToken = containerTokenBegin + containerToken;
		String endToken = containerTokenBegin + "/" + containerToken
				+ containerTokenEnd;

		if (containerIndex >= containerOldContent.length()) {
			return FOUND_END;
		}

		/*
		 * Parse for start and end tokens. Use whichever comes first and ignore
		 * the other until a later invokation of this method.
		 */
		j = containerOldContent.indexOf(startToken, containerIndex);
		k = containerOldContent.indexOf(endToken, containerIndex);

		/*
		 * If start token found first, parse out the keys string and test the
		 * keys. Only logical-or is now supported in the keys string -
		 * logical-and is not supported (but usually can be accomplished by
		 * nesting container tokens). Return the proper result code depending on
		 * whether the test passed or not. Note: even if the test passed, it can
		 * still be reverted to no-include if an outer container token failed,
		 * or parseContainer (the top-level method) called with the no-include
		 * flag.
		 */
		if ((j != -1) && ((j < k) || (k == -1))) {
			/*
			 * Display all the content, if allowed by outer, up until the new
			 * start tag.
			 */
			if (isIncluded) {
				containerNewContent += containerOldContent.substring(
						containerIndex, j);
			}
			/*
			 * Find the ending ">" of the container start tag - if not found,
			 * finish, ignore the rest of the content.
			 */
			k = containerOldContent.indexOf(containerTokenEnd, j);
			if (k == -1) {
				containerIndex = containerOldContent.length();
				return FOUND_END;
			}
			/*
			 * Find the container key string - if any, it starts after ":"
			 */
			containerKeyString = "";
			if (containerOldContent.charAt(j + startToken.length()) == ':') {
				containerKeyString = containerOldContent.substring(j
						+ startToken.length() + 1, k);
			}
			containerKeyString = containerKeyString.trim();
			containerMatch = false;
			if (containerMatcher != null) {
				containerKeys = getContainerKeys(containerKeyString);
				for (int i = 0; i < containerKeys.length; i++) {
					if (containerMatcher.match(containerKeys[i])) {
						containerMatch = true;
						break;
					}
				}
			}
			isIncluded = isIncluded && containerMatch;
			containerIndex = k + 1;
			return isIncluded ? FOUND_CONTAINER_START_MATCH
					: FOUND_CONTAINER_START_NOMATCH;
		}

		/*
		 * If end token was found first, display all the rest of the content, up
		 * to the end token, if allowed, and finish. The rest of the content
		 * will be parsed by the caller in the recursion.
		 */
		if ((k != -1) && ((k < j) || (j == -1))) {
			if (isIncluded) {
				containerNewContent += containerOldContent.substring(
						containerIndex, k);
			}
			containerIndex = k + endToken.length();
			return FOUND_CONTAINER_END;
		}

		/*
		 * Otherwise we hit the end of the content. Display everything up to the
		 * end of the content, if allowed.
		 */
		if (isIncluded) {
			containerNewContent += containerOldContent
					.substring(containerIndex);
		}
		containerIndex = containerOldContent.length();
		return FOUND_END;
	}

	/**
	 * Get container keys by splitting on "|" character.
	 */
	private String[] getContainerKeys(String content) {
		String containerKeys[] = content.split("\\" + TOKEN_CONTAINER_OR);
		return containerKeys;
	}

	/**
	 * Parses the given content, searching for all occurrences of the given
	 * parameterized token (like <code>CONTENT-URL</code> for the
	 * <code>{CONTENT-URL:<i>pathname</i>}</code> token), and replacing them
	 * with the value generated by the given <code>ValueProvider</code>. This
	 * method recognizes both <code>{...}</code> and <code>&lt;...&gt;</code>
	 * symbols for the token boundaries.
	 * 
	 * @param content
	 *            The content string.
	 * @param tokenName
	 *            The name of the parameterized token - eg <code>FOO</code>
	 *            for the <code>{FOO:<i>param</i>}</code> or
	 *            <code>{FOO:<i>param</i>}</code> token.
	 * @param provider
	 *            Provides the value to substitute.
	 * @return The interpolated string.
	 */
	protected String parseParameterized(String content, String tokenName,
			ValueProvider provider) {
		if (content == null || tokenName == null || provider == null) {
			return content;
		}
		tokenName = tokenName.toUpperCase().trim();
		if (content.equals("") || tokenName.equals("")) {
			return content;
		}

		// First parse for "{...}" format.
		content = parseParameterizedIntl(content, tokenName, TOKEN_BEGIN,
				TOKEN_END, provider);

		// Second parse for legacy "<...>" format.
		content = parseParameterizedIntl(content, tokenName,
				DEPRECATED_TOKEN_BEGIN, DEPRECATED_TOKEN_END, provider);

		// Return the parsed content.
		return (content);
	}

	/**
	 * Parse a parameterized token and replace with the provided value.
	 */
	private String parseParameterizedIntl(String content, String tokenName,
			char tokenBegin, char tokenEnd, ValueProvider provider) {
		String str, param, value;
		// This makes a token like: {tokenName:
		String token = tokenBegin + tokenName + ":";
		// This makes a regex like: (\{tokenName:.*?\})
		String regex = "(\\" + token + ".*?\\" + tokenEnd + ")";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			str = m.group();
			param = str.substring(str.indexOf(token) + token.length(), str
					.length() - 1);
			value = provider.getValue(param.trim());
			// This makes a regex like: (\{tokenName:\Qparam\E\})
			regex = "(\\" + token + "\\Q" + param + "\\E\\" + tokenEnd + ")";
			if (value != null) {
				content = content.replaceAll(regex, value);
			} else {
				content = content.replaceAll(regex, "");
			}
		}
		return (content);
	}

	/**
	 * Parses the given content, searching for all occurrences of the given
	 * unparameterized token (like <code>EMAIL</code> for the
	 * <code>{EMAIL}</code> or <code>{EMAIL}</code> tokens), and replacing
	 * them with the given value. This method recognizes both <code>{...}</code>
	 * and <code>&lt;...&gt;</code> symbols for the token boundaries.
	 * 
	 * @param content
	 *            The content string.
	 * @param tokenName
	 *            The name of the unparameterized token - eg <code>FOO</code>
	 *            for the <code>{FOO}</code> or <code>&lt;FOO&gt;</code>
	 *            tokens.
	 * @param value
	 *            The value to substitute
	 * @return The interpolated string.
	 */
	protected String parseUnparameterized(String content, String tokenName,
			String value) {
		if (content == null || tokenName == null) {
			return content;
		}
		tokenName = tokenName.toUpperCase().trim();
		if (content.equals("") || tokenName.equals("")) {
			return content;
		}
		if (value == null) {
			value = "";
		}

		// First parse for "{...}" format.
		content = parseUnparameterizedIntl(content, tokenName, TOKEN_BEGIN,
				TOKEN_END, value);

		// Second parse for legacy "<...>" format.
		content = parseUnparameterizedIntl(content, tokenName,
				DEPRECATED_TOKEN_BEGIN, DEPRECATED_TOKEN_END, value);

		// Return the parsed content.
		return (content);
	}

	/**
	 * Parse an unparameterized token and replace with the provided value.
	 */
	private String parseUnparameterizedIntl(String content, String tokenName,
			char tokenBegin, char tokenEnd, String value) {
		String regex = "\\" + tokenBegin + tokenName + "\\" + tokenEnd;
		return content.replaceAll(regex, value);
	}
}
