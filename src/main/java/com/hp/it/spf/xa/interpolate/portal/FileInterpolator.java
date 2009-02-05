/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 * 
 * This portal utility class reads a file of text (eg an HTML file), substituting
 * dynamic values for various tokens, and returning the substituted
 * content to the calling class.
 */
package com.hp.it.spf.xa.interpolate.portal;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.io.InputStream;
import com.epicentric.user.User;
import com.epicentric.common.website.SessionUtils;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.interpolate.portal.TokenParser;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * <p>
 * This portal utility class reads a text secondary support file (eg an HTML
 * file loaded to the current portal component as a secondary support file),
 * substituting dynamic values for various tokens, and returning the
 * interpolated content to the calling class. This is all done in the
 * {@link #interpolate()} method, based on parameters you setup in the
 * constructor.
 * </p>
 * <p>
 * Note that the base text filename you provide is used to find a best-fit file
 * for the locale you provide (by default, the locale in the portal context you
 * provide), among all the files for that basename in a bundle of files (each
 * tagged with locale), in the manner of the Java standard for
 * {@link ResourceBundle}. All of the files in the bundle must be loaded as
 * secondary support files against the current portal component. <b>Important:</b>
 * Your files must be UTF-8 encoded.
 * </p>
 * <p>
 * This class uses the portal {@link TokenParser} to do most of its work. As of
 * this writing, the following tokens are supported in the input file. <b>Note:</b>
 * Your content may use <code>&lt;</code> and <code>&gt;</code> instead of
 * <code>{</code> and <code>}</code> as the token delimiters, if desired.
 * The token names are case-sensitive.
 * </p>
 * 
 * <dl>
 * <dt><code>{CONTENT-URL:<i>pathname</i>}</code></dt>
 * <dd>
 * <p>
 * Use this token to insert a URL for an <b>unlocalized</b> secondary support
 * file (such as an administrator-uploaded image) into the interpolated content.
 * For the <code><i>pathname</i></code> in the token, use the filename for
 * the particular secondary support file.
 * </p>
 * <p>
 * For example, upload <code>picture.jpg</code> as a secondary support file
 * for a particular component which uses this <code>FileInterpolator</code>.
 * Then put the following markup into an HTML text file to be processed by the
 * <code>FileInterpolator</code>, and upload that text file as another
 * secondary support file:
 * </p>
 * <p>
 * <code>&lt;IMG SRC="{CONTENT-URL:picture.jpg}"&gt;</code>
 * </p>
 * <p>
 * The returned text string will contain the necessary URL for showing the image
 * to the user.
 * </p>
 * <p>
 * <b>Note:</b> This token is for <b>unlocalized</b> content only. For
 * <b>localized</b> secondary support files, see the
 * <code>{LOCALIZED-CONTENT-URL:<i>pathname</i>}</code> token. Actually the
 * <code>{LOCALIZED-CONTENT-URL:<i>pathname</i>}</code> token works for
 * unlocalized content too, so the <code>{CONTENT-URL:<i>pathname</i>}</code>
 * token isn't really necessary. It is retained for backward-compatibility.
 * </p>
 * 
 * <dt><code>{COUNTRY-CODE}</code></dt>
 * <dd>
 * <p>
 * Use this token to insert the <a
 * href="http://www.iso.org/iso/country_codes/iso_3166_code_lists/english_country_names_and_code_elements.htm">ISO
 * 3166-1</a> country code from the current locale. Note that the language code
 * is not a part of this. For example, for a Japanese request,
 * <code>{COUNTRY-CODE}</code> is replaced with <code>JP</code>.
 * </p>
 * </dd>
 * 
 * <dt><code>{EMAIL}</code></dt>
 * <dd>
 * <p>
 * Use this token to insert the email address of the user into the interpolated
 * content. The email address is taken from the <code>email</code> property of
 * the portal <code>User</code> object. For example, <code>{EMAIL}</code> is
 * replaced with <code>john.doe@acme.com</code> for that user. (If the email
 * address or user object are null - eg the user is not logged-in - then an
 * empty string is inserted.)
 * </p>
 * </dd>
 * 
 * <dt><code>{GROUP:<i>groups</i>}...{/GROUP}</code></dt>
 * <dd>
 * <p>
 * Use this token around a section of content which should only be included in
 * the interpolated content if the user is a member of any one (or more) of the
 * listed <code><i>groups</i></code>. For example, using this token, a
 * single file can contain the proper content for multiple kinds of users,
 * simplifying administration of a portal component which must display different
 * content for different user groups.
 * </p>
 * <p>
 * The groups to be checked are provided to the <code>FileInterpolator</code>
 * through the constructor (see). This lets you override the groups contained in
 * the portal context, if necessary (eg to augment them or derive them from
 * elsewhere).
 * </p>
 * <p>
 * In the <code><i>groups</i></code> parameter to the
 * <code>{GROUP:<i>groups</i>}</code> token, you can list just a single
 * group name, or multiple group names (use the <code>|</code> character to
 * delimit them). The content enclosed by the <code>{GROUP:<i>groups</i>}</code>
 * and <code>{/GROUP}</code> tokens is omitted from the returned content
 * unless the groups provided to the constructor match one of those group names.
 * </p>
 * <p>
 * The content enclosed by the <code>{GROUP:<i>groups</i>}</code> and
 * <code>{/GROUP}</code> tokens can be anything, including any of the special
 * tokens listed here (even other <code>{GROUP:<i>groups</i>}...{/GROUP}</code>
 * sections - ie, you can "nest" them).
 * </p>
 * <p>
 * For example, the following markup selectively includes or omits the content
 * depending on the user groups as indicated:
 * </p>
 * <p>
 * 
 * <pre>
 * This content is for everybody.
 * {GROUP:A}
 * This content is only for members of group A.
 * {GROUP:B|C}
 * This content is only for members of groups A and B, or A and C.
 * {/GROUP}
 * {/GROUP}
 * </pre>
 * 
 * </p>
 * </dd>
 * 
 * <dt><code>{LANGUAGE-CODE}</code></dt>
 * <dd>
 * <p>
 * Use this token to insert the <a
 * href="http://www.loc.gov/standards/iso639-2/php/English_list.php">ISO 639-1</a>
 * language code from the current locale. Note that the country code is not a
 * part of this. For example, for a Japanese request,
 * <code>{LANGUAGE-CODE}</code> is replaced with <code>ja</code>.
 * </p>
 * </dd>
 * 
 * <dt><code>{LANGUAGE-TAG}</code></dt>
 * <dd>
 * <p>
 * Use this token to insert the <a
 * href="http://www.faqs.org/rfcs/rfc3066.html">RFC 3066</a> language tag from
 * the current locale. Note that the country code is included in the language
 * tag, if it was set in the locale (otherwise the language tag consists of the
 * language code only). For example, for a French (Canada) request,
 * <code>{LANGUAGE-TAG}</code> is replaced with <code>fr-CA</code>.
 * </p>
 * </dd>
 * 
 * <dl>
 * <dt><code>{LOCALIZED-CONTENT-URL:<i>pathname</i>}</code></dt>
 * <dd>
 * <p>
 * Use this token to insert a URL for a (potentially localized) secondary
 * support file (such as an administrator-uploaded image containing a picture of
 * some text) into the interpolated content. Upload the secondary support files
 * (both the base file and the localized versions) into the portal component
 * that uses this <code>FileInterpolator</code>. For the
 * <code><i>pathname</i></code> in the token, use the filename of the base
 * file. The <code>FileInterpolator</code> will replace this token with a URL
 * for the best-fit candidate secondary support file for the current locale.
 * </p>
 * <p>
 * For example, upload <code>picture.jpg</code> as a secondary support file
 * for a particular component which uses this <code>FileInterpolator</code>.
 * Also upload any localized versions of the file (eg
 * <code>picture_fr.jpg</code> for French, <code>picture_fr_CA.jpg</code>
 * for French (Canada), etc), as additional secondary support files for the same
 * component. Then put the following markup into an HTML text file to be
 * processed by the <code>FileInterpolator</code>, and upload that text file
 * as another secondary support file:
 * </p>
 * <p>
 * <code>&lt;IMG SRC="{LOCALIZED-CONTENT-URL:picture.jpg}"&gt;</code>
 * </p>
 * <p>
 * The returned text string will contain the necessary URL for showing the
 * best-fit image to the user. The logic for determining the best-fit will
 * resemble that used by the Java-standard {@link ResourceBundle} class (see).
 * </p>
 * <p>
 * 
 * <dt><code>{LOGGED-IN}...{/LOGGED-IN}</code><br>
 * <code>{LOGGED-OUT}...{/LOGGED-OUT}</code></dt>
 * <dd>
 * <p>
 * Use these tokens around sections of content which should only be included in
 * the interpolated content if the user is logged-in or logged-out,
 * respectively. The wrapped content will be omitted from the returned content
 * when the user login status does not match.
 * </p>
 * <p>
 * The content enclosed by the tokens can be anything, including any of the
 * other special tokens listed here.
 * </p>
 * <p>
 * For example, the following markup selectively includes or omits the content
 * depending on the user login status as indicated:
 * </p>
 * <p>
 * 
 * <pre>
 * This content is for everybody.
 * {LOGGED-IN}
 * This content is only for users who are logged-in.
 * {/LOGGED-IN}
 * {LOGGED-OUT}
 * This content is only for users who are logged-out.
 * {/LOGGED-OUT}
 * </pre>
 * 
 * </p>
 * </dd>
 * 
 * <dt><code>{NAME}</code></dt>
 * <dd>
 * <p>
 * Use this token to insert the full name of the user into the interpolated
 * content, where the given (first) and family (last) names are in the customary
 * order for the current locale. The name is taken from the
 * <code>firstname</code> and <code>lastname</code> properties of the portal
 * <code>User</code> object, and the locale is taken from the current portal
 * request.
 * </p>
 * <p>
 * For example, <code>{NAME}</code> is replaced with
 * <code>Scott Jorgenson</code> for that user by default (ie, for most
 * locales). For certain Asian locales, where it is customary to use the family
 * name first (eg Japan - the list of such locales is configured in
 * <code>i18n_config.properties</code>), it would be replaced with
 * <code>Jorgenson Scott</code> instead.
 * </p>
 * <p>
 * If the first and last names in the user object are null, or the user object
 * itself is guest or null (ie the user is not logged-in), then an empty string
 * is inserted.
 * </p>
 * </dd>
 * 
 * <dt><code>{REQUEST-URL}</code></dt>
 * <dd>
 * <p>
 * Use this token to insert into the content the complete current URL which the
 * browser used to access the current page. For example,
 * <code>{REQUEST-URL}</code> is replaced with
 * <code>http://portal.hp.com/portal/site/itrc/template.MY_ACCOUNT/...</code>
 * when the <code>template.MY_ACCOUNT</code> secondary page is requested from
 * the <code>itrc</code> portal site on the <code>portal.hp.com</code>
 * server using HTTP.
 * </p>
 * </dd>
 * 
 * <dt><code>{SITE}</code></dt>
 * <dd>
 * <p>
 * Use this token to insert the site name of the current portal site into the
 * interpolated content. The site name is taken from the portal context (it is
 * the Vignette "site DNS name" element). For example, <code>{SITE}</code> is
 * replaced with <code>itrc</code> when the portal component invoking this
 * <code>FileInterpolator</code> is requested from the <code>itrc</code>
 * portal site.
 * </p>
 * </dd>
 * 
 * <dt><code>{SITE:<i>names</i>}...{/SITE}</code></dt>
 * <dd>
 * <p>
 * Use this token around a section of content which should only be included in
 * the interpolated content if the current request is for a particular portal
 * site (as indicated by the site name in the request). For example, using this
 * token, a single file can contain content for multiple portal sites, perhaps
 * making administration of the sites easier.
 * </p>
 * <p>
 * In the <code><i>names</i></code> parameter to the
 * <code>{SITE:names}</code> token, you can list just a single site name, or
 * multiple (use the <code>|</code> character to delimit them). The content
 * enclosed by the <code>{SITE:<i>names</i>}</code> and <code>{/SITE}</code>
 * tokens is omitted from the returned content unless the site name in the
 * request matches one of those values. The match is case-insensitive. The site
 * name in the request is gotten from the portal context (it is the Vignette
 * "site DNS name").
 * </p>
 * <p>
 * The content enclosed by the <code>{SITE:<i>names</i>}</code> and
 * <code>{/SITE}</code> tokens can be anything, including any of the special
 * tokens supported by this class (including other
 * <code>{SITE:<i>names</i>}...{/SITE}</code> tokens - ie you can "nest"
 * them.
 * </p>
 * <p>
 * For example, the following markup selectively includes or omits the content
 * depending on the site as indicated:
 * </p>
 * <p>
 * 
 * <pre>
 * This content is for all sites to show.
 * {SITE:site_A|site_B}
 * This content is only to be shown in site_A or site_B.
 * {SITE:site_B}
 * This content is only to be shown in site_B.
 * {/SITE}
 * {/SITE}
 * </pre>
 * 
 * </p>
 * </dd>
 * 
 * <dt><code>{SITE-URL}</code></dt>
 * <dt><code>{SITE-URL:<i>uri</i>}</code></dt>
 * <dd>
 * <p>
 * Use these related tokens to insert URL's for pages at the current portal site
 * into the interpolated content. The <code>{SITE-URL}</code> token inserts
 * the site home page URL; the <code>{SITE-URL:<i>uri</i>}</code> token
 * inserts a URL for a page at that site, identified by the given URI (ie a
 * friendly URI for the page in Vignette, or a secondary page template name, or
 * etc - this can include query data if you like).
 * </p>
 * <p>
 * For example, <code>{SITE-URL}</code> is replaced with
 * <code>http://portal.hp.com/portal/site/itrc/</code> when the current portal component is
 * requested from the <code>itrc</code> portal site on the
 * <code>portal.hp.com</code> server using HTTP. Similarly,
 * <code>{SITE-URL:/forums}</code> is replaced with
 * <code>http://portal.hp.com/portal/site/itrc/forums</code>,
 * <code>{SITE-URL:/template.ANON_SPF_GLOBAL_HELP}</code> is replaced with
 * <code>http://portal.hp.com/portal/site/itrc/template.ANON_SPF_GLOBAL_HELP</code>
 * (the global help secondary page), etc.
 * </p>
 * </dd>
 * 
 * <dt><code>{TOKEN:<i>key</i>}</code></dt>
 * <dd>
 * <p>
 * Use this token to lookup a value for the given key in a property file, and
 * insert that value into the interpolated content. The property file, by
 * default, is <code>default_tokens.properties</code>, but you can override
 * that in the <code>FileInterpolator</code> constructor. The property value
 * may itself contain any content including any of the special markup tokens
 * supported by <code>FileInterpolator</code>, <b>except</b> another
 * <code>{TOKEN:<i>key</i>}</code> token.
 * </p>
 * <p>
 * Whether you use <code>default_tokens.properties</code> or your own
 * token-substitution property file, the file should be loaded into a location
 * accessible to the class loader.
 * </p>
 * <p>
 * For example, assume you have put this key/value pair into your property file:
 * </p>
 * <p>
 * <code>url.hp-shopping=http://shopping.hp.com</code>
 * </p>
 * <p>
 * If your input file contains the following:
 * </p>
 * <p>
 * <code>&lt;A HREF="{TOKEN:url.hp-shopping}"&gt;Go to HP shopping.&lt;/A&gt;</code>
 * </p>
 * <p>
 * Then the interpolated content will contain a hyperlink taking the user to the
 * URL value given in your property file:
 * </p>
 * <p>
 * <code>&lt;A HREF="http://shopping.hp.com"&gt;Go to HP shopping.&lt;/A&gt;</code>
 * </p>
 * <p>
 * A template for the token substitution property file, also named
 * <code>default_tokens.properties</code> (you should rename your copy), is
 * available with the SPF.
 * </p>
 * </dd>
 * 
 * <dt><code>{USER-PROPERTY:<i>key</i>}</code></dt>
 * <dd>
 * <p>
 * Use this token to insert a given string property of the user into the
 * interpolated content. The <code><i>key</i></code> parameter in the token
 * is the name of the user property, and the user properties themselves are
 * taken from the current portal <code>User</code> object. For example,
 * <code>{USER-PROPERTY:day_phone}</code> is replaced with
 * <code>123 456 7890</code> for a user with such a phone number. The property
 * name <code><i>key</i></code>'s are not listed here and may vary based on
 * the portal implementation.
 * </p>
 * <p>
 * If the property is not found in the user object, or is not a string, then an
 * empty string will be inserted. Similarly, if the user object itself is guest
 * or null (ie the user is not logged-in), then an empty string is inserted.
 * </p>
 * </dd>
 * 
 * </dl>
 * </p>
 * 
 * <p>
 * The above tokens are parsed in the following order (so content included from
 * the tokens may itself contain references to subsequent tokens - such
 * references will be interpolated):
 * </p>
 * 
 * <p>
 * <ol>
 * <li><code>{TOKEN:<i>key</i>}</code></li>
 * <li><code>{SITE:<i>names</i>}</code></li>
 * <li><code>{LOGGED-IN}</code></li>
 * <li><code>{LOGGED-OUT}</code></li>
 * <li><code>{GROUP:<i>groups</i>}</code></li>
 * <li><code>{SITE}</code></li>
 * <li><code>{SITE-URL}</code></li>
 * <li><code>{SITE-URL:<i>uri</i>}</code></li>
 * <li><code>{REQUEST-URL}</code></li>
 * <li><code>{LANGUAGE-CODE}</code></li>
 * <li><code>{COUNTRY-CODE}</code></li>
 * <li><code>{LANGUAGE-TAG}</code></li>
 * <li><code>{EMAIL}</code></li>
 * <li><code>{NAME}</code></li>
 * <li><code>{USER-PROPERTY:<i>key</i>}</code></li>
 * <li><code>{CONTENT-URL:<i>path</i>}</code></li>
 * <li><code>{LOCALIZED-CONTENT-URL:<i>path</i>}</code></li>
 * </ol>
 * </p>
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.interpolate.FileInterpolator</code><br>
 *      <code>com.hp.it.spf.xa.interpolate.portal.TokenParser</code><br>
 *      <code>com.hp.it.spf.xa.interpolate.TokenParser</code>
 */
public class FileInterpolator extends
		com.hp.it.spf.xa.interpolate.FileInterpolator {

	/**
	 * Portal context.
	 */
	private PortalContext portalContext = null;

	/**
	 * Portal log.
	 */
	private LogWrapper portalLog = new LogWrapper(FileInterpolator.class);

	/**
	 * <p>
	 * Constructs a new <code>FileInterpolator</code> for the given base
	 * content secondary support filename and portal context.
	 * </p>
	 * <p>
	 * The filename provided should be the base name of the secondary support
	 * file containing the content to interpolate. The interpolate method will
	 * open the best-fit localized version of that secondary support file, based
	 * on the locale in the given request.
	 * </p>
	 * <b>Note:</b> A token-substitutions property file (to be used with any
	 * <code>{TOKEN:<i>key</i>}</code> tokens in the file content) is not
	 * passed in this constructor. Therefore any <code>{TOKEN:<i>key</i>}</code>
	 * tokens in the file content will be resolved against the default
	 * token-substitutions property file (<code>default_tokens.properties</code>).
	 * </p>
	 * 
	 * @param portalContext
	 *            The portal context
	 * @param baseContentFile
	 *            The base filename of the text secondary support file to
	 *            interpolate
	 */
	public FileInterpolator(PortalContext portalContext, String baseContentFile) {
		this.portalContext = portalContext;
		this.baseContentFilePath = baseContentFile;
		if (portalContext != null) {
			this.parser = new TokenParser(portalContext);
		}
	}

	/**
	 * <p>
	 * Constructs a new <code>FileInterpolator</code> for the given base
	 * content secondary support filename, portal context, and
	 * token-substitutions pathname.
	 * </p>
	 * <p>
	 * This constructor works like
	 * {@link #FileInterpolator(PortalContext, String)} and allows a
	 * token-substitutions file to be specified as well. The token-substitutions
	 * pathname provided is to be used with any <code>{TOKEN:<i>key</i>}</code>
	 * tokens in the file content; they will be resolved against the file whose
	 * pathname you provide. The pathname should include any necessary path
	 * (relative to the class loader) followed by the filename (the extension
	 * <code>.properties</code> is required and assumed). If you know there
	 * are no such tokens in the file content, you can pass null for this
	 * parameter.
	 * </p>
	 * 
	 * @param portalContext
	 *            The portal context
	 * @param baseContentFile
	 *            The base filename of the text secondary support file to
	 *            interpolate
	 * @param subsFilePath
	 *            The filename and path relative to where the class loader
	 *            searches for the token-substitutions property file (for
	 *            purposes of any <code>{TOKEN:<i>key</i>}</code> tokens in
	 *            the file content)
	 */
	public FileInterpolator(PortalContext portalContext,
			String relativeFilePath, String subsFileBase) {
		this.portalContext = portalContext;
		this.baseContentFilePath = relativeFilePath;
		if (portalContext != null) {
			this.parser = new TokenParser(portalContext, subsFileBase);
		}
	}

	/**
	 * <p>
	 * Constructs a new <code>FileInterpolator</code> for the given base
	 * content secondary support filename, portal context, locale, and
	 * token-substitutions pathname.
	 * </p>
	 * <p>
	 * This constructor works like
	 * {@link #FileInterpolator(PortalContext, String, String)} except that the
	 * given locale is used instead of the one in the request in the portal
	 * context. However if the given locale is null, then the one in the request
	 * will be used.
	 * </p>
	 * 
	 * @param portalContext
	 *            The portal context
	 * @param pLocale
	 *            The locale to use (if null, uses the one in the portal context
	 *            instead)
	 * @param baseContentFile
	 *            The base filename of the text secondary support file to
	 *            interpolate
	 * @param subsFilePath
	 *            The filename and path relative to where the class loader
	 *            searches for the token-substitutions property file (for
	 *            purposes of any <code>{TOKEN:<i>key</i>}</code> tokens in
	 *            the file content)
	 */
	public FileInterpolator(PortalContext portalContext, Locale pLocale,
			String relativeFilePath, String subsFileBase) {
		this.portalContext = portalContext;
		this.baseContentFilePath = relativeFilePath;
		this.locale = pLocale;
		if (portalContext != null) {
			this.parser = new TokenParser(portalContext, pLocale, subsFileBase);
		}
	}

	/**
	 * <p>
	 * Gets the best-fit localized version of the secondary support file (using
	 * {@link #getLocalizedContentFileAsStream()}, reads it into a string, and
	 * substitutes the tokens found in the string with the proper dynamic values
	 * (using {@link TokenParser#parse(String)}, returning the interpolated
	 * content. The filename of the secondary support content file, the locale
	 * for which to find the best-candidate, and the proper substitution values
	 * for the tokens are all based on the information you provided when calling
	 * the constructor. Null is returned if there are problems interpolating (eg
	 * the file is not found or was empty, or the portal context or content file
	 * you provided when calling the constructor were null). See class
	 * documentation for more information about the tokens which are
	 * substituted.
	 * </p>
	 * 
	 * @return The interpolated file content
	 * @throws Exception
	 *             Some exception
	 */
	public String interpolate(Locale pLocale) throws Exception {

		if (portalContext == null) {
			logWarning("Portal context was null.");
			return null;
		}
		return super.interpolate();
	}

	/**
	 * Get an input stream for the best-candidate localized content file
	 * available among the secondary support files of the current portal
	 * component, based on the locale and base content filename provided to the
	 * constructor. If no specific locale was provided to the constructor, then
	 * the one in the portal context is used. Null is returned if the file is
	 * not found or the portal context or content file provided to the
	 * constructor was null. This method uses
	 * {@link com.hp.it.spf.xa.i18n.portal.I18nUtility#getLocalizedFileStream(PortalContext, String, Locale, boolean)}
	 * (see).
	 * 
	 * @return The input stream for the file
	 */
	protected InputStream getLocalizedContentFileAsStream() {
		if (portalContext == null || baseContentFilePath == null) {
			return null;
		}
		return I18nUtility.getLocalizedFileStream(portalContext,
				baseContentFilePath, locale, true);
	}

	/**
	 * Log warning.
	 * 
	 * @param msg
	 *            log message
	 */
	protected void logWarning(String msg) {
		portalLog.warning(msg);
	}

}