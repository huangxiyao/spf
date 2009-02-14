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
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;

import com.hp.it.spf.xa.i18n.I18nUtility;
import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;
import com.hp.it.spf.xa.misc.Utils;

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
 * <li><code>{INCLUDE:<i>key</i>}</code></li>
 * <li><code>{LOGGED-IN}</code></li>
 * <li><code>{LOGGED-OUT}</code></li>
 * <li><code>{LANGUAGE-CODE}</code></li>
 * <li><code>{COUNTRY-CODE}</code></li>
 * <li><code>{LANGUAGE-TAG}</code></li>
 * <li><code>{REQUEST-URL}</code></li>
 * <li><code>{REQUEST-URL:<i>spec</i>}</code></li>
 * <li><code>{EMAIL}</code></li>
 * <li><code>{NAME}</code></li>
 * <li><code>{SITE}</code></li>
 * <li><code>{SITE-URL}</code></li>
 * <li><code>{SITE-URL:<i>spec</i>}</code></li>
 * <li><code>{SITE:<i>names</i>}</code></li>
 * <li><code>{GROUP:<i>groups</i>}</code></li>
 * <li><code>{USER-PROPERTY:<i>key</i>}</code></li>
 * <li><code>{CONTENT-URL:<i>path</i>}</code></li>
 * <li><code>{LOCALIZED-CONTENT-URL:<i>path</i>}</code></li>
 * <li><code>{BEFORE:<i>date</i>}</code></li>
 * <li><code>{AFTER:<i>date</i>}</code></li>
 * </dl>
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.interpolate.portal.TokenParser</code><br>
 *      <code>com.hp.it.spf.xa.interpolate.portlet.TokenParser</code></br>
 *      <code>com.hp.it.spf.xa.interpolate.FileInterpolator</code><br>
 *      <code>com.hp.it.spf.xa.interpolate.portal.FileInterpolator</code><br>
 *      <code>com.hp.it.spf.xa.interpolate.portlet.FileInterpolator</code>
 */
public abstract class TokenParser {

	/**
	 * This class attribute will hold the pathname (relative to the class
	 * loader) of the token-substitution file to use by default. The file
	 * extension <code>.properties</code> is required and assumed.
	 */
	private static final String DEFAULT_SUBS_PATHNAME = "default_includes";

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
	private static final String TOKEN_INCLUDE = "INCLUDE";

	/**
	 * This class attribute is the legacy name for the include token.
	 * 
	 * @deprecated
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

	/**
	 * This class attribute is the name of the container token for a before-date
	 * section.
	 */
	private static final String TOKEN_BEFORE_CONTAINER = "BEFORE";

	/**
	 * This class attribute is the name of the container token for an after-date
	 * section.
	 */
	private static final String TOKEN_AFTER_CONTAINER = "AFTER";

	/**
	 * This is the {@link java.text.SimpleDateFormat} pattern used for the
	 * parameters to the before- and after-date containers.
	 */
	protected static String DATE_PATTERN = "M/d/yyyy h:mm:ss a z";

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
	private static char TOKEN_BEGIN_PARAM = ':';
	private static char DEPRECATED_TOKEN_BEGIN = '<';
	private static char DEPRECATED_TOKEN_END = '>';

	/**
	 * This class attribute holds the base filename of the token-substitution
	 * file to use.
	 */
	protected String subsFilePath = DEFAULT_SUBS_PATHNAME;

	/**
	 * The locale to use instead of the one in the current request.
	 */
	protected Locale locale = null;

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
	 * Get the locale to use. Different action by portal and portlet, so
	 * therefore this is an abstract method.
	 * 
	 * @return locale
	 */
	protected abstract Locale getLocale();

	/**
	 * Get the email address for the user. Different action by portal and
	 * portlet, so therefore this is an abstract method.
	 * 
	 * @return email
	 */
	protected abstract String getEmail();

	/**
	 * Get the first name for the user. Different action by portal and portlet,
	 * so therefore this is an abstract method.
	 * 
	 * @return first name
	 */
	protected abstract String getFirstName();

	/**
	 * Get the last name for the user. Different action by portal and portlet,
	 * so therefore this is an abstract method.
	 * 
	 * @return last name
	 */
	protected abstract String getLastName();

	/**
	 * Get the portal site name for the current portal site. Different action by
	 * portal and portlet, so therefore this is an abstract method.
	 * 
	 * @return site name
	 */
	protected abstract String getSite();

	/**
	 * Get the portal site URL for the portal site and page indicated by the
	 * given scheme, port, and URI. Different action by portal and portlet, so
	 * therefore this is an abstract method.
	 * 
	 * @param secure
	 *            If true, force use of <code>https</code>; if false, force
	 *            use of <code>http</code>. If null, use the current scheme.
	 * @param port
	 *            The port to use (an integer; if non-positive, use the current
	 *            port).
	 * @param uri
	 *            The site name (ie "site DNS name") and/or additional path (eg
	 *            a friendly URI or template friendly ID). (The part before the
	 *            first <code>/</code> is considered the site name.)
	 * @return site URL string
	 */
	protected abstract String getSiteURL(String URI, Boolean secure, int port);

	/**
	 * Get the current portal request URL, modified to use the given scheme and
	 * port. Different action by portal and portlet, so therefore this is an
	 * abstract method.
	 * 
	 * @param secure
	 *            If true, force use of <code>https</code>; if false, force
	 *            use of <code>http</code>. If null, use the current scheme.
	 * @param port
	 *            The port to use (an integer; if non-positive, use the current
	 *            port).
	 * @return request URL string
	 */
	protected abstract String getRequestURL(Boolean secure, int port);

	/**
	 * Get the authorization groups for the current request. Different action by
	 * portal and portlet, so therefore this is an abstract method.
	 * 
	 * @return array of groups
	 */
	protected abstract String[] getGroups();

	/**
	 * Return true if the user is logged-in and false otherwise. Different
	 * action by portal and portlet, so therefore this is an abstract method.
	 * 
	 * @return array of groups
	 */
	protected abstract boolean getLoginStatus();

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
	 * non-container, parameterized token like <code>{INCLUDE:key}</code>
	 * where "key" is the parameter passed to the getValue method.
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
	 * Parses the given string, performing all of the token substitutions and
	 * interpolations supported by this class. For a list and description of all
	 * the supported tokens, see the other parse methods - this method calls all
	 * of them in series. The order of token evaluation is as follows:
	 * </p>
	 * <dl>
	 * <dt><code>{INCLUDE:<i>key</i>}</code></dt>
	 * <dd>This token is parsed first, and the substituted content is added to
	 * the string. So subsequent substitutions operate against the value for the
	 * <i>key</i> - therefore that value may itself contain other tokens, and
	 * other tokens' parameters may contain that value.</dd>
	 * <dt><code>{LOGGED-IN}...{/LOGGED-IN}</code></dt>
	 * <dt><code>{LOGGED-OUT}...{/LOGGED-OUT}</code></dt>
	 * <dd>The unparameterized container tokens are parsed next, to eliminate
	 * as much content as possible. The other container tokens can't be parsed
	 * yet because they are parameterized.</dd>
	 * <dt><code>{LANGUAGE-CODE}</code></dt>
	 * <dt><code>{COUNTRY-CODE}</code></dt>
	 * <dt><code>{LANGUAGE-TAG}</code></dt>
	 * <dt><code>{REQUEST-URL}</code></dt>
	 * <dt><code>{EMAIL}</code></dt>
	 * <dt><code>{NAME}</code></dt>
	 * <dt><code>{SITE}</code></dt>
	 * <dt><code>{SITE-URL}</code></dt>
	 * <dd>All of the unparameterized non-container tokens are parsed next, so
	 * that their values can be substituted into the parameterized ones below if
	 * necessary.</dd>
	 * <dt><code>{BEFORE:<i>key</i>}...{/BEFORE}</code></dt>
	 * <dt><code>{AFTER:<i>key</i>}...{/AFTER}</code></dt>
	 * <dt><code>{SITE:<i>names</i>}...{/SITE}</code></dt>
	 * <dt><code>{GROUP:<i>groups</i>}...{/GROUP}</code></dt>
	 * <dd>The parameterized container tokens are parsed, to eliminate as much
	 * content as possible. Their parameters may include values from above.</dd>
	 * <dt><code>{USER-PROPERTY:<i>key</i>}</code></dt>
	 * <dt><code>{CONTENT-URL:<i>path</i>}</code></dt>
	 * <dt><code>{LOCALIZED-CONTENT-URL:<i>path</i>}</code></dt>
	 * <dt><code>{REQUEST-URL:<i>spec</i>}</code></dt>
	 * <dt><code>{SITE-URL:<i>spec</i>}</code></dt>
	 * <dd>Finally the remaining parameterized tokens are parsed last, so that
	 * the unparameterized ones (and also <code>{INCLUDE:<i>key</i>}</code>)
	 * can include values in their parameters.</dd>
	 * </dl>
	 * </p>
	 * <p>
	 * <b>Note:</b> The <code>&lt;</code> and <code>&gt;</code> symbols are
	 * also supported for the token boundaries, in place of <code>{</code> and
	 * <code>}</code>.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @return The interpolated string.
	 */
	public String parse(String content) {

		if (content == null) {
			return null;
		}

		// Start parsing and substituting the tokens:
		// Always do {INCLUDE:key} first.
		content = parseInclude(content);

		// Always do unparameterized containers second:
		content = parseLoggedInContainer(content);
		content = parseLoggedOutContainer(content);

		// Always do unparameterized non-containers third:
		content = parseLanguageCode(content);
		content = parseCountryCode(content);
		content = parseLanguageTag(content);
		content = parseRequestURL(content);
		content = parseEmail(content);
		content = parseName(content);
		content = parseSite(content);
		content = parseSiteURL(content);

		// Always do parameterized containers fourth:
		content = parseBeforeContainer(content);
		content = parseAfterContainer(content);
		content = parseSiteContainer(content);
		content = parseGroupContainer(content);

		// Always finish with parameterized non-containers:
		content = parseUserProperty(content);
		content = parseNoLocalizedContentURL(content);
		content = parseLocalizedContentURL(content);
		content = parseRequestURLParameterized(content);
		content = parseSiteURLParameterized(content);

		// Done.
		return (content);
	}

	/**
	 * <p>
	 * Parses the given string, substituting the <a
	 * href="http://www.loc.gov/standards/iso639-2/php/English_list.php">ISO
	 * 639-1</a> language code for the current locale in place of the
	 * <code>{LANGUAGE-CODE}</code> token. For example: <code>&lt;a
	 * href="https://ovsc.hp.com?lang={LANGUAGE-CODE}"&gt;go to
	 * OVSC&lt;/a&gt;</code>
	 * is changed to <code>&lt;a
	 * href="https://ovsc.hp.com?lang=ja"&gt;go to OVSC&lt;/a&gt;</code>
	 * when the locale is for Japan. The locale is determined from the concrete
	 * subclass {@link #getLocale()} method. If the locale is null, or one in
	 * which a language code is not specified, the token is replaced with blank.
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
	 * @return The interpolated string.
	 */
	public String parseLanguageCode(String content) {
		String lang = null;
		Locale loc = getLocale();
		if (loc != null) {
			lang = loc.getLanguage();
		}
		return parseUnparameterized(content, TOKEN_LANGUAGE_CODE, lang);
	}

	/**
	 * <p>
	 * Parses the given string, substituting the <a
	 * href="http://www.iso.org/iso/country_codes/iso_3166_code_lists/english_country_names_and_code_elements.htm">ISO
	 * 3166-1</a> country code for the current locale in place of the
	 * <code>{COUNTRY-CODE}</code> token. For example: <code>&lt;a
	 * href="https://ovsc.hp.com?cc={COUNTRY-CODE}"&gt;go to
	 * OVSC&lt;/a&gt;</code>
	 * is changed to <code>&lt;a
	 * href="https://ovsc.hp.com?cc=JP"&gt;go to OVSC&lt;/a&gt;</code>
	 * when the locale is for Japan. The locale is determined from the concrete
	 * subclass {@link #getLocale()} method. If the locale is null, or one in
	 * which a country is not specified, the token is replaced with blank. If
	 * you provide null content, null is returned.
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
	public String parseCountryCode(String content) {
		String cc = null;
		Locale loc = getLocale();
		if (loc != null) {
			cc = loc.getCountry();
		}
		return parseUnparameterized(content, TOKEN_COUNTRY_CODE, cc);
	}

	/**
	 * <p>
	 * Parses the given string, substituting the given <a
	 * href="href="http://www.faqs.org/rfcs/rfc3066.html">RFC 3066</a> language
	 * tag for the current locale in place of the <code>{LANGUAGE-TAG}</code>
	 * token. For example: <code>&lt;a
	 * href="https://ovsc.hp.com?lang={LANGUAGE-TAG}"&gt;go to
	 * OVSC&lt;/a&gt;</code>
	 * is changed to <code>&lt;a
	 * href="https://ovsc.hp.com?lang=zn-CN"&gt;go to OVSC&lt;/a&gt;</code>
	 * when the locale is Chinese (China). The locale is determined from the
	 * concrete subclass {@link #getLocale()} method. If the locale is null, the
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
	public String parseLanguageTag(String content) {
		Locale loc = getLocale();
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
	 * Parses the given string, substituting the current email address for the
	 * <code>{EMAIL}</code> token. For example: <code>&lt;a
	 * href="https://ovsc.hp.com?email={EMAIL}"&gt;go to OVSC&lt;/a&gt;</code>
	 * is changed to
	 * <code>&lt;a href="https://ovsc.hp.com?email=me@foo.com"&gt;go to
	 * OVSC&lt;/a&gt;</code>
	 * when the email address is "me@foo.com". The email address is obtained
	 * from the {@link #getEmail()} method. If it returns a null email, the
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
	public String parseEmail(String content) {
		return parseUnparameterized(content, TOKEN_EMAIL, getEmail());
	}

	/**
	 * <p>
	 * Parses the given string, substituting the current first and last names
	 * (in proper display order for the current locale) in place of the
	 * <code>{NAME}</code> token. For example: <code>Welcome, {NAME}!</code>
	 * is changed to <code>Welcome, <i>first</i> <i>last</i>!</code> for
	 * most locales (and by default - eg if the current locale is null). However
	 * in some Asian locales (eg Japan, China, etc), it would be changed to
	 * <code>Welcome, <i>last</i> <i>first</i>!</code>.
	 * </p>
	 * <p>
	 * The current names and locale are obtained from the
	 * {@link #getFirstName()}, {@link #getLastName()}, and
	 * {@link #getLocale()} methods. If they provide null first and last names,
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
	 * @return The interpolated string.
	 */
	public String parseName(String content) {
		String name = I18nUtility.getUserDisplayName(getFirstName(),
				getLastName(), getLocale());
		return parseUnparameterized(content, TOKEN_NAME, name);
	}

	/**
	 * <p>
	 * Parses the given string, substituting the current portal site name for
	 * the <code>{SITE}</code> token. The site name is the unique name in the
	 * portal URL for the virtual portal site. For example: <code>&lt;a
	 * href="https://ovsc.hp.com?site={SITE}"&gt;go to OVSC&lt;/a&gt;</code>
	 * is changed to <code>{a href="https://ovsc.hp.com?site=acme"&gt;go to
	 * OVSC&lt;/a&gt;</code>
	 * when the site name is "acme". The site name is obtained from
	 * {@link #getSite()} - if that method returns a null site name, the token
	 * is replaced with blank. If you provide null content, null is returned.
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
	public String parseSite(String content) {
		return parseUnparameterized(content, TOKEN_SITE, getSite());
	}

	/**
	 * <p>
	 * Parses the given string, substituting the current portal site home-page
	 * URL for the <code>{SITE-URL}</code> token.
	 * </p>
	 * 
	 * <p>
	 * For example: <code>&lt;a
	 * href="{SITE-URL}"&gt;go to home page&lt;/a&gt;</code>
	 * is changed to
	 * <code>&lt;a href="http://portal.hp.com/portal/site/acme/"&gt;go to
	 * home page&lt;/a&gt;</code>
	 * when the current site home-page URL is
	 * "http://portal.hp.com/portal/site/acme/".
	 * </p>
	 * <p>
	 * The site root URL is obtained from the
	 * {@link #getSiteURL(String,String,String)} method - if that method returns
	 * null, the token is replaced with blank. If you provide null content, null
	 * is returned.
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
	public String parseSiteURL(String content) {
		content = parseUnparameterized(content, TOKEN_SITE_URL, getSiteURL(
				null, null, -1));
		return (content);
	}

	/**
	 * <p>
	 * Parses the given string, substituting the respective portal site page URL
	 * for the <code>{SITE-URL:<i>spec</i>}</code> token. The <i>spec</i>
	 * specifies the particular page, scheme, and/or port. The format of the
	 * <i>spec</i> is:
	 * </p>
	 * 
	 * <code><i>[scheme[:port];]uri</i></code>
	 * 
	 * <ul>
	 * <li>The <code><i>scheme</i></code> may be <code>http</code> or
	 * <code>https</code>. If you omit a valid scheme, then the scheme used
	 * for the current request is assumed.</li>
	 * <li>The <code><i>port</i></code> is the port number to use. If you
	 * omit a port number, then the one used for the current request is assumed.</li>
	 * <li>The <code><i>uri</i></code> can be whatever string you need to
	 * come after (ie relative to) the site root URL (ie the home-page URL),
	 * including additional path, query string, etc. If the <i>uri</i> value
	 * begins with <code>/</code>, the page URL is built for this current
	 * portal site, with the <i>uri</i> used to identify the page at that
	 * current site. If it does not, then the first element in the <i>uri</i>
	 * is taken as the new portal site (so this lets you switch the site) and
	 * the rest is taken used for the page at that site.</li>
	 * </ul>
	 * <p>
	 * For example, when the current request URL is
	 * <code>http://portal.hp.com/portal/site/acme/template.PAGE/...</code>:
	 * </p>
	 * <dl>
	 * <dt><code>&lt;a href="{SITE-URL:/forums}"&gt;go to forums&lt;/a&gt;</code></dt>
	 * <dd>becomes
	 * <code>&lt;a href="http://portal.hp.com/portal/site/acme/forums"&gt;go to
	 * forums&lt;/a&gt;</code></dd>
	 * <dt><code>&lt;a href="{SITE-URL:itrc/forums}"&gt;go to ITRC forums&lt;/a&gt;</code></dt>
	 * <dd>becomes
	 * <code>&lt;a href="http://portal.hp.com/portal/site/itrc/forums"&gt;go to
	 * ITRC forums&lt;/a&gt;</code></dd>
	 * <dt><code>&lt;a href="{SITE-URL:itrc}"&gt;go to ITRC home page&lt;/a&gt;</code></dt>
	 * <dd>becomes
	 * <code>&lt;a href="http://portal.hp.com/portal/site/itrc/"&gt;go to ITRC home page&lt;/a&gt;</code></dd>
	 * <dt><code>&lt;a href="{SITE-URL:https;}"&gt;go to secure home page&lt;/a&gt;</code></dt>
	 * <dd>becomes
	 * <code>&lt;a href="https://portal.hp.com/portal/site/acme/"&gt;go to secure home page&lt;/a&gt;</code></dd>
	 * <dt><code>&lt;a href="{SITE-URL:http:7001;itrc}"&gt;go to unsecure ITRC home page&lt;/a&gt;</code></dt>
	 * <dd>becomes
	 * <code>&lt;a href="http://portal.hp.com:7001/portal/site/acme/"&gt;go to unsecure home page&lt;/a&gt;</code></dd>
	 * </dl>
	 * </p>
	 * <p>
	 * The site root URL is obtained from the
	 * {@link #getSiteURL(String,String,String)} method - if that method returns
	 * null, the token is replaced with just the <i>uri</i>. If you provide
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
	 * @return The interpolated string.
	 */
	public String parseSiteURLParameterized(String content) {
		content = parseParameterized(content, TOKEN_SITE_URL,
				new ValueProvider() {
					protected String getValue(String param) {
						return getSiteURL(getURI(param), getSecure(param),
								getPort(param));
					}
				});
		return (content);
	}

	/**
	 * <p>
	 * Parses the given string, modifying and substituting the current portal
	 * request URL for the <code>{REQUEST-URL:<i>spec</i>}</code> token. The
	 * <i>spec</i> specifies an alternate scheme and/or port to switch to. The
	 * format of the <i>spec</i> is:
	 * </p>
	 * 
	 * <code><i>scheme[:port]</i></code>
	 * 
	 * <ul>
	 * <li>The <code><i>scheme</i></code> may be <code>http</code> or
	 * <code>https</code>. If you omit a valid scheme, then the scheme used
	 * for the current request is assumed.</li>
	 * <li>The <code><i>port</i></code> is the port number to use. If you
	 * omit a port number, then the one used for the current request is assumed.</li>
	 * </ul>
	 * 
	 * <p>
	 * For example, when the current request URL is
	 * <code>http://portal.hp.com/portal/site/acme/template.PAGE/...</code>:
	 * </p>
	 * 
	 * <dl>
	 * <dt><code>&lt;a href="{REQUEST-URL:https}"&gt;switch to secure&lt;/a&gt;</code></dt>
	 * <dd>becomes
	 * <code>&lt;a href="https://portal.hp.com/portal/site/acme/template.PAGE/..."&gt;switch to secure&lt;/a&gt;</code></dd>
	 * <dt><code>&lt;a href="{REQUEST-URL:http:7001}"&gt;switch to unsecure&lt;/a&gt;</code></dt>
	 * <dd>becomes
	 * <code>&lt;a href="http://portal.hp.com:7001/portal/site/acme/template.PAGE/..."&gt;switch to unsecure&lt;/a&gt;</code></dd>
	 * </dl>
	 * 
	 * <p>
	 * The {@link #getRequestURL(String,String)} method is used to obtain the
	 * request URL; if it is null, the token is replaced with blank. If you
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
	 * @return The interpolated string.
	 */
	public String parseRequestURLParameterized(String content) {
		content = parseParameterized(content, TOKEN_REQUEST_URL,
				new ValueProvider() {
					protected String getValue(String param) {
						if (!param.endsWith(";"))
							param += ';';
						return getRequestURL(getSecure(param), getPort(param));
					}
				});
		return (content);
	}

	/**
	 * <p>
	 * Parses the given string, substituting the current portal request URL for
	 * the <code>{REQUEST-URL}</code> token. For example: <code>&lt;a
	 * href="{REQUEST-URL}"&gt;try again&lt;/a&gt;</code>
	 * is changed to
	 * <code>&lt;a href="http://portal.hp.com/portal/site/acme/template.PAGE/?..."&gt;try again&lt;/a&gt;</code>
	 * when the current request URL is
	 * "http://portal.hp.com/portal/site/acme/template.PAGE/?...". The
	 * {@link #getRequestURL(String,String)} method is used to obtain the
	 * request URL; if it is null, the token is replaced with blank. If you
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
	 * @return The interpolated string.
	 */
	public String parseRequestURL(String content) {
		return parseUnparameterized(content, TOKEN_REQUEST_URL, getRequestURL(
				null, -1));
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
	 * Parses the given string, converting the <code>{INCLUDE:<i>key</i>}</code>
	 * token into the value for that key in the token-substitutions file (<code>default_includes.properties</code>
	 * by default, unless overridden in the constructor). For example:
	 * <code>&lt;img src="{INCLUDE:url.image}"&gt;</code> is returned as:
	 * <code>&lt;img src="http://foo.hp.com/images/picture.jpg"&gt;</code>
	 * assuming the token-substitutions file contains the following property:
	 * <code>url.image=http://foo.hp.com/images/picture.jpg</code>. If the
	 * token key is not found in the file, then the token is replaced with
	 * blank. If you provide null content, null is returned.
	 * </p>
	 * <p>
	 * <b>Note:</b> For the token, you may use <code>&lt;</code> and
	 * <code>&gt;</code> instead of <code>{</code> and <code>}</code>, if
	 * you prefer. You may also use the <code>{INCLUDE:<i>key</i>}</code>
	 * token as a synonym for this one, but this is deprecated.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @return The interpolated string.
	 */
	public String parseInclude(String content) {
		content = parseParameterized(content, TOKEN_TOKEN, new ValueProvider() {
			protected String getValue(String param) {
				return getIncludeValue(param);
			}
		});
		content = parseParameterized(content, TOKEN_INCLUDE,
				new ValueProvider() {
					protected String getValue(String param) {
						return getIncludeValue(param);
					}
				});
		return (content);
	}

	/**
	 * <p>
	 * Parses the string for any <code>{SITE:<i>names</i>}</code> content;
	 * such content is deleted if the current portal site name does not match
	 * (otherwise only the special markup is removed). The site name is the
	 * unique name in the portal URL for the virtual portal site. The <i>names</i>
	 * may include one or more site names, delimited by "|" for a logical-or.
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
	 * If you provide null content, null is returned. The site name is obtained
	 * from the {@link #getSite()} method - if it returns null or an empty site
	 * name, all <code>{SITE:names}</code>-enclosed sections are removed from
	 * the content.
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
	public String parseSiteContainer(String content) {

		/**
		 * <code>ContainerMatcher</code> for site section parsing. The
		 * constructor stores a site name into the class. The match method
		 * returns true if the given site name is an exact match
		 * (case-insensitive) of the stored site name.
		 */
		class SiteContainerMatcher extends ContainerMatcher {

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

		return parseContainer(content, TOKEN_SITE_CONTAINER,
				new SiteContainerMatcher(getSite()));
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
	 * If you provide null content, null is returned. The
	 * {@link #getLoginStatus()} method is used to determine if the user is
	 * logged-in or not.
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
	public String parseLoggedInContainer(String content) {

		/**
		 * <code>ContainerMatcher</code> for logged-in section parsing. The
		 * constructor stores the login status into the class: true if
		 * logged-in, false otherwise. The match method just returns the login
		 * status. The passed container key is not used and is expected to be
		 * null.
		 */
		class LoggedInContainerMatcher extends ContainerMatcher {

			protected LoggedInContainerMatcher(boolean loggedIn) {
				super(new Boolean(loggedIn));
			}

			protected boolean match(String containerKey) {
				return ((Boolean) subjectOfComparison).booleanValue();
			}
		}

		return parseContainer(content, TOKEN_LOGGEDIN_CONTAINER,
				new LoggedInContainerMatcher(getLoginStatus()));
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
	 * If you provide null content, null is returned. The
	 * {@link #getLoginStatus()} method is used to determine if the user is
	 * logged-in or not.
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
	public String parseLoggedOutContainer(String content) {

		/**
		 * <code>ContainerMatcher</code> for logged-out section parsing. The
		 * constructor stores the login status into the class: true if
		 * logged-in, false otherwise. The match method just returns the inverse
		 * of this. The passed container key is not used and is expected to be
		 * null.
		 */
		class LoggedOutContainerMatcher extends ContainerMatcher {

			protected LoggedOutContainerMatcher(boolean loggedIn) {
				super(new Boolean(loggedIn));
			}

			protected boolean match(String containerKey) {
				return !((Boolean) subjectOfComparison).booleanValue();
			}
		}

		return parseContainer(content, TOKEN_LOGGEDOUT_CONTAINER,
				new LoggedOutContainerMatcher(getLoginStatus()));
	}

	/**
	 * <p>
	 * Parses the string for any <code>{GROUP:<i>groups</i>}</code> content;
	 * such content is deleted if the current user groups do not qualify
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
	 * If the current user groups include <code>abc</code>, <code>def</code>,
	 * and <code>xyz</code>, the returned content string is:
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
	 * But if the current user groups include only <code>abc</code>, the
	 * returned content string is:
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
	 * If you provide null content, null is returned. The current groups are
	 * obtained from the {@link #getGroups()} method; if it returns null or
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
	 * @return The interpolated string.
	 */
	public String parseGroupContainer(String content) {

		/**
		 * <code>ContainerMatcher</code> for group parsing. The constructor
		 * stores an array of group names into the class. The match method
		 * returns true if the given group name exactly matches
		 * (case-insensitive) any of the stored group names.
		 */
		class GroupContainerMatcher extends ContainerMatcher {

			protected GroupContainerMatcher(String[] groups) {
				super(groups);
			}

			protected boolean match(String containerKey) {
				return Utils.groupMatch((String[]) subjectOfComparison,
						containerKey);
			}
		}

		return parseContainer(content, TOKEN_GROUP_CONTAINER,
				new GroupContainerMatcher(getGroups()));
	}

	/**
	 * <p>
	 * Parses the string for any <code>{BEFORE:<i>date</i>}</code> tokens;
	 * such content is removed if the current time is after the
	 * <code><i>date</i></code>. The <code><i>date</i></code> string must
	 * conform to the {@link #DATE_PATTERN} which is a
	 * {@link java.text.SimpleDateFormat} pattern.
	 * </p>
	 * 
	 * <p>
	 * For example, consider the following content string:
	 * </p>
	 * 
	 * <pre>
	 *  This content is permanent.
	 *  {BEFORE:11/5/2008 12:00:00 AM GMT}
	 *  This content is only displayed until midnight (GMT) on November 5, 2008.
	 *  {/BEFORE}
	 * </pre>
	 * 
	 * <p>
	 * You can also nest this token with the <code>{AFTER:<i>date</i>}</code>
	 * token (see {@link #parseAfterContainer(String)}) to form a date range.
	 * </p>
	 * 
	 * <p>
	 * <b>Note:</b> Be careful the <code><i>date</i></code> you specify
	 * satisfies the expected {@link #DATE_PATTERN}. If there is a parsing
	 * error, the content will be included in the returned string (with the
	 * offending <code>{BEFORE:<i>date</i>}</code> and
	 * <code>{/BEFORE}</code> tokens removed).
	 * </p>
	 * 
	 * <p>
	 * If you provide null content, null is returned. <b>Note:</b> For the
	 * token, you may use <code>&lt;</code> and <code>&gt;</code> instead of
	 * <code>{</code> and <code>}</code>, if you prefer.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @return The interpolated string.
	 */
	public String parseBeforeContainer(String content) {

		class BeforeContainerMatcher extends ContainerMatcher {

			protected BeforeContainerMatcher() {
				super(new Date());
			}

			protected boolean match(String containerKey) {
				try {
					SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
					Date then = format.parse(containerKey);
					Date now = (Date) subjectOfComparison;
					return now.before(then);
				} catch (Exception e) { // match by default if parsing problem
					return true;
				}
			}
		}

		return parseContainer(content, TOKEN_BEFORE_CONTAINER,
				new BeforeContainerMatcher());
	}

	/**
	 * <p>
	 * Parses the string for any <code>{AFTER:<i>date</i>}</code> tokens;
	 * such content is removed if the current time is before the
	 * <code><i>date</i></code>. The <code><i>date</i></code> string must
	 * conform to the {@link #DATE_PATTERN} which is a
	 * {@link java.text.SimpleDateFormat} pattern.
	 * </p>
	 * 
	 * <p>
	 * For example, consider the following content string:
	 * </p>
	 * 
	 * <pre>
	 *  This content is permanent.
	 *  {AFTER:11/5/2008 12:00:00 AM GMT}
	 *  This content is only displayed after midnight (GMT) on November 5, 2008.
	 *  {/AFTER}
	 * </pre>
	 * 
	 * <p>
	 * You can also nest this token with the <code>{BEFORE:<i>date</i>}</code>
	 * token (see {@link #parseBeforeContainer(String)}) to form a date range.
	 * </p>
	 * 
	 * <p>
	 * <b>Note:</b> Be careful the <code><i>date</i></code> you specify
	 * satisfies the expected {@link #DATE_PATTERN}. If there is a parsing
	 * error, the content will be included in the returned string (with the
	 * offending <code>{AFTER:<i>date</i>}</code> and
	 * <code>{/AFTER}</code> tokens removed).
	 * </p>
	 * 
	 * <p>
	 * If you provide null content, null is returned. <b>Note:</b> For the
	 * token, you may use <code>&lt;</code> and <code>&gt;</code> instead of
	 * <code>{</code> and <code>}</code>, if you prefer.
	 * </p>
	 * 
	 * @param content
	 *            The content string.
	 * @return The interpolated string.
	 */
	public String parseAfterContainer(String content) {

		class AfterContainerMatcher extends ContainerMatcher {

			protected AfterContainerMatcher() {
				super(new Date());
			}

			protected boolean match(String containerKey) {
				try {
					SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
					Date then = format.parse(containerKey);
					Date now = (Date) subjectOfComparison;
					return now.after(then);
				} catch (Exception e) { // match by default if parsing problem
					return true;
				}
			}
		}

		return parseContainer(content, TOKEN_AFTER_CONTAINER,
				new AfterContainerMatcher());
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
	private String getIncludeValue(String key) {
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

		int j, k, n;
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
		 * the other until a later invokation of this method. Start token is
		 * like <FOO and end token is like </FOO> at this point.
		 */
		j = k = containerIndex;
		n = -1;
		do {
			j = containerOldContent.indexOf(startToken, j);
			if (j == -1) // if not found, bail - no match
				break;
			n = j + startToken.length();
			if (n >= containerOldContent.length()) {
				j = -1;
				break; // if found at very end, bail - no match
			}
			if (containerOldContent.charAt(n) == containerTokenEnd)
				break; // if <FOO> found, match
			if ((containerOldContent.charAt(n) == TOKEN_BEGIN_PARAM)
					&& ((n + 1) < containerOldContent.length()))
				break; // if <FOO:...> found, match
			// if <FOO... not <FOO> or <FOO:...> found, try again
			j = n;
		} while (true);
		k = containerOldContent.indexOf(endToken, k);

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
			 * Two cases: either the ending ">" of the start tag was found in
			 * the next char (meaning no key string - continue), or ":" was
			 * found in the next char (meaning what follows up to ">" is the key
			 * string - get it).
			 */
			containerKeyString = "";
			if (containerOldContent.charAt(n) == TOKEN_BEGIN_PARAM) {
				containerKeyString = containerOldContent.substring(n + 1, k);
			}
			/*
			 * Now we have the key string (or no key string), so evaluate the
			 * container - ie determine whether to include it or not.
			 */
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
		String token = tokenBegin + tokenName + TOKEN_BEGIN_PARAM;
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

	/**
	 * Parse a spec string for a page URL and return true if <code>https</code>,
	 * false if <code>http</code> (returns null if valid scheme was not
	 * specified).
	 */
	protected Boolean getSecure(String spec) {
		if (spec == null) {
			return null;
		}
		String scheme = null;
		spec = spec.trim();
		int i = spec.indexOf(';');
		if (i == -1) {
			return null;
		}
		int j = spec.substring(0, i).indexOf(':');
		if (j == -1) {
			scheme = spec.substring(0, i).trim();
		} else {
			scheme = spec.substring(0, j).trim();
		}
		if ("http".equalsIgnoreCase(scheme)) {
			return new Boolean(false);
		} else if ("https".equalsIgnoreCase(scheme)) {
			return new Boolean(true);
		} else {
			return null;
		}
	}

	/**
	 * Parse a spec string for a page URL and get the port if any (returns null
	 * if valid port was not specified).
	 */
	protected int getPort(String spec) {
		if (spec == null) {
			return -1;
		}
		String port = null;
		spec = spec.trim();
		int i = spec.indexOf(';');
		if (i == -1) {
			return -1;
		}
		int j = spec.substring(0, i).indexOf(':');
		if (j == -1) {
			return -1;
		} else {
			port = spec.substring(j + 1, i).trim();
		}
		try {
			int p = Integer.parseInt(port);
			return p;
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Parse a spec string for a page URL and get the URI if any (returns null
	 * if URI was not specified).
	 */
	protected String getURI(String spec) {
		if (spec == null) {
			return null;
		}
		spec = spec.trim();
		int i = spec.indexOf(';');
		if (i == -1) {
			return spec;
		} else if ((i + 1) < spec.length()) {
			return spec.substring(i + 1);
		} else {
			return null;
		}
	}
}
