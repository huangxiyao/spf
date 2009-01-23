/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 * 
 * This portal utility class reads a file of text (eg an HTML file), substituting
 * dynamic values for various tokens, and returning the substituted
 * content to the calling class.
 */
package com.hp.it.spf.xa.interpolate.portal;

import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.io.InputStream;
import com.epicentric.user.User;
import com.epicentric.common.website.SessionUtils;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * <p>
 * This portal utility class reads a text secondary support file (eg an HTML
 * file loaded to the current portal component as a secondary support file),
 * substituting dynamic values for various tokens, and returning the
 * interpolated content to the calling class. Note that the base text filename
 * you provide is used to find a best-fit file for the current portlet request's
 * locale. This is all done in the interpolate method, based on parameters you
 * setup in the constructor.
 * </p>
 * <p>
 * This class uses the portal TokenParser to do most of its work. As of this
 * writing, the following tokens are supported in the input file (note: these
 * are not XML - except where noted, tokens are case-sensitive and do not
 * require a closing tag):
 * </p>
 * 
 * <dl>
 * <dt><code>&lt;CONTENT-URL:<i>pathname</i>&gt;</code></dt>
 * <dd>
 * <p>
 * <b>Deprecated. Use <code>&lt;LOCALIZED-CONTENT-URL:pathname&gt;</code>
 * instead.</b>
 * </p>
 * <p>
 * Use this token to insert a URL for an <b>unlocalized</b> secondary support
 * file (such as an administrator-uploaded image) into the interpolated content.
 * For the <code><i>pathname</i></code> in the token, use the filename for
 * the particular secondary support file.
 * </p>
 * <p>
 * For example, upload <code>picture.jpg</code> as a secondary support file
 * for a particular component which uses this FileInterpolator. Then put the
 * following markup into an HTML text file to be processed by the
 * FileInterpolator, and upload that text file as another secondary support
 * file:
 * </p>
 * <p>
 * <code>&lt;IMG SRC="&lt;CONTENT-URL:picture.jpg&gt;"&gt;</code>
 * </p>
 * <p>
 * The returned text string will contain the necessary URL for showing the image
 * to the user.
 * </p>
 * <p>
 * <b>Note:</b> This token is for <b>unlocalized</b> content only. For
 * <b>localized</b> secondary support files, see the
 * <code>&lt;LOCALIZED-CONTENT-URL:pathname&gt;</code> token. Actually the
 * <code>&lt;LOCALIZED-CONTENT-URL:pathname&gt;</code> token works for
 * unlocalized content too, so the <code>&lt;CONTENT-URL:pathname&gt;</code>
 * token is deprecated. It is retained for backward-compatibility.
 * </p>
 * 
 * <dt><code>&lt;EMAIL&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token to insert the email address of the user into the interpolated
 * content. The email address is taken from the <code>email</code> property of
 * the portal User object. For example, <code>&lt;EMAIL&gt;</code> is replaced
 * with <code>john.doe@acme.com</code> for that user. (If the email address or
 * User object are null - eg the user is not logged-in - then an empty string is
 * inserted.)
 * </p>
 * </dd>
 * 
 * <dt><code>&lt;GROUP:<i>groups</i>&gt;...&lt;/GROUP&gt;</code></dt>
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
 * The groups to be checked are provided to the FileInterpolator through the
 * constructor (see). This lets you override the groups contained in the portal
 * context, if necessary (eg to augment them or derive them from elsewhere).
 * </p>
 * <p>
 * In the <code><i>groups</i></code> parameter to the
 * <code>&lt;GROUP:groups&gt;</code> token, you can list just a single group
 * name, or multiple group names (use the <code>|</code> character to delimit
 * them). The content enclosed by the <code>&lt;GROUP:groups&gt;</code> and
 * <code>&lt;/GROUP&gt;</code> tokens is omitted from the returned content
 * unless the groups provided to the constructor match one of those group names.
 * </p>
 * <p>
 * The content enclosed by the <code>&lt;GROUP:groups&gt;</code> and
 * <code>&lt;/GROUP&gt;</code> tokens can be anything, including any of the
 * special tokens listed here (even other
 * <code>&lt;GROUP:groups&gt;...&lt;/GROUP&gt;</code> sections - ie, you can
 * "nest" them).
 * </p>
 * <p>
 * For example, the following markup selectively includes or omits the content
 * depending on the user groups as indicated:
 * </p>
 * <p>
 * 
 * <pre>
 * This content is for everybody.
 * &lt;GROUP:A&gt;
 * This content is only for members of group A.
 * &lt;GROUP:B|C&gt;
 * This content is only for members of groups A and B, or A and C.
 * &lt;/GROUP&gt;
 * &lt;/GROUP&gt;
 * </pre>
 * 
 * </p>
 * </dd>
 * 
 * <dt><code>&lt;LANGUAGE-CODE&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token to insert the ISO 639-1 language code from the current portal
 * context (as provided in the locale of the request). Note that the country
 * code is not a part of this. For example, for a Japanese request,
 * <code>&lt;LANGUAGE-CODE&gt;</code> is replaced with <code>ja</code>.
 * </p>
 * </dd>
 * 
 * <dt><code>&lt;LANGUAGE-TAG&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token to insert the RFC 3066 language tag from the current portal
 * context (as provided in the locale of the request). Note that the country
 * code is included in the language tag, if it was set in the locale (otherwise
 * the language tag consists of the language code only). For example, for a
 * French (Canada) request, <code>&lt;LANGUAGE-TAG&gt;</code> is replaced with
 * <code>fr-CA</code>.
 * </p>
 * </dd>
 * 
 * <dl>
 * <dt><code>&lt;LOCALIZED-CONTENT-URL:<i>pathname</i>&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token to insert a URL for a (potentially localzied) secondary
 * support file (such as an administrator-uploaded image containing a picture of
 * some text) into the interpolated content. Upload the secondary support files
 * (both the base file and the localized versions) into the portal component
 * using this FileInterpolator. For the <code><i>pathname</i></code> in the
 * token, use the filename of the base file. The FileInterpolator will replace
 * this token with a URL for the best-fit candidate secondary support file for
 * the locale in the request.
 * </p>
 * <p>
 * For example, upload <code>picture.jpg</code> as a secondary support file
 * for a particular component which uses this FileInterpolator. Also upload any
 * localized versions of the file (eg <code>picture_fr.jpg</code> for French,
 * <code>picture_fr_CA.jpg</code> for French (Canada), etc), as additional
 * secondary support files for the same component. Then put the following markup
 * into an HTML text file to be processed by the FileInterpolator, and upload
 * that text file as another secondary support file:
 * </p>
 * <p>
 * <code>&lt;IMG SRC="&lt;LOCALIZED-CONTENT-URL:picture.jpg&gt;"&gt;</code>
 * </p>
 * <p>
 * The returned text string will contain the necessary URL for showing the
 * best-fit image to the user. The logic for determining the best-fit will
 * resemble that used by the Java-standard ResourceBundle class (see).
 * </p>
 * <p>
 * 
 * <dt><code>&lt;NAME&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token to insert the full name of the user into the interpolated
 * content, where the given (first) and family (last) names are in the customary
 * order for the user's locale. The name is taken from the
 * <code>firstname</code> and <code>lastname</code> properties of the portal
 * user object, and the locale is taken from the current portal request.
 * </p>
 * <p>
 * For example, <code>&lt;NAME&gt;</code> is replaced with
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
 * <dt><code>&lt;SITE&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token to insert the site name of the current portal site into the
 * interpolated content. The site name is taken from the portal context (it is
 * the Vignette "site DNS name" element). For example, <code>&lt;SITE&gt;</code>
 * is replaced with <code>itrc</code> when the portal component invoking this
 * FileInterpolator is requested from the <code>itrc</code> portal site.
 * </p>
 * </dd>
 * 
 * <dt><code>&lt;SITE:names&gt;...&lt;/SITE&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token around a section of content which should only be included in
 * the interpolated content if the current request is for a particular portal
 * site (as indicated by the site name in the request). For example, using this
 * token, a single file can contain content for multiple portal sites, perhaps
 * making administration of the sites easier.
 * </p>
 * <p>
 * In the <code>names</code> parameter to the <code>&lt;SITE:names&gt;</code>
 * token, you can list just a single site name, or multiple (use the
 * <code>|</code> character to delimit them). The content enclosed by the
 * <code>&lt;SITE:names&gt;</code> and <code>&lt;/SITE&gt;</code> tokens is
 * omitted from the returned content unless the site name in the request matches
 * one of those values. The match is case-insensitive. The site name in the
 * request is gotten from the portal context (it is the Vignette "site DNS
 * name").
 * </p>
 * <p>
 * The content enclosed by the <code>&lt;SITE:names&gt;</code> and
 * <code>&lt;/SITE&gt;</code> tokens can be anything, including any of the
 * special tokens supported by this class (including other
 * <code>&lt;SITE:names&gt;...&lt;/SITE&gt;</code> tokens - ie you can "nest"
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
 * &lt;SITE:site_A|site_B&gt;
 * This content is only to be shown in site_A or site_B.
 * &lt;SITE:site_B&gt;
 * This content is only to be shown in site_B.
 * &lt;/SITE&gt;
 * &lt;/SITE&gt;
 * </pre>
 * 
 * </p>
 * </dd>
 * 
 * <dt><code>&lt;TOKEN:key&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token to lookup a value for the given key in a property file, and
 * insert that value into the interpolated content. The property file, by
 * default, is <code>default_tokens.properties</code>, but you can override
 * that in the FileInterpolator constructor. The property value may itself
 * contain any content including any of the special markup tokens supported by
 * FileInterpolator, <b>except</b> another <code>&lt;TOKEN:key&gt;</code>
 * token.
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
 * <code>&lt;A HREF="&lt;TOKEN:url.hp-shopping&gt;"&gt;Go to HP shopping.&lt;/A&gt;</code>
 * </p>
 * <p>
 * Then the interpolated content will contain a hyperlink taking the user to the
 * URL value given in your property file:
 * </p>
 * <p>
 * <code>&lt;A HREF="http://shopping.hp.com"&gt;Go to HP shopping.&lt;/A&gt;</code>
 * </p>
 * </dd>
 * 
 * <dt><code>&lt;USER-PROPERTY:<i>key</i>&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token to insert a given string property of the user into the
 * interpolated content. The <code><i>key</i></code> parameter in the token
 * is the name of the user property, and the user properties themselves are
 * taken from the current portal user object. For example,
 * <code>&lt;USER-PROPERTY:day_phone&gt;</code> is replaced with
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
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see com.hp.it.spf.xa.interpolate.FileInterpolator
 *      com.hp.it.spf.xa.interpolate.portal.TokenParser
 *      com.hp.it.spf.xa.interpolate.TokenParser
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
	 * Constructs a new FileInterpolator for the given base content secondary
	 * support filename and portal context.
	 * </p>
	 * <p>
	 * The filename provided should be the base name of the secondary support
	 * file containing the content to interpolate. The interpolate method will
	 * open the best-fit localized version of that secondary support file, based
	 * on the locale in the given request.
	 * </p>
	 * <b>Note:</b> A token-substitutions property file (to be used with any
	 * <code>&lt;TOKEN:key&gt;</code> tokens in the file content) is not
	 * passed in this constructor. Therefore any <code>&lt;TOKEN:key&gt;</code>
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
			this.t = new TokenParser(portalContext);
		}
	}

	/**
	 * <p>
	 * Constructs a new FileInterpolator for the given base content secondary
	 * support filename, portal context, and token-substitutions pathname.
	 * </p>
	 * <p>
	 * The content filename provided should be the base name of the secondary
	 * support file containing the content to interpolate. The interpolate
	 * method will open the best-fit localized version of that secondary support
	 * file, based on the locale in the given request.
	 * </p>
	 * <p>
	 * The token-substitutions pathname provided is to be used with any
	 * <code>&lt;TOKEN:key&gt;</code> tokens in the file content; they will be
	 * resolved against the file whose pathname you provide. The pathname should
	 * include any necessary path (relative to the class loader) followed by the
	 * filename (the extension <code>.properties</code> is required and
	 * assumed). If you know there are no such tokens in the file content, you
	 * can pass null for this parameter.
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
	 *            purposes of any <code>&lt;TOKEN:key&gt;</code> tokens in the
	 *            file content)
	 */
	/* Added by CK for 1000790073 */
	public FileInterpolator(PortalContext portalContext,
			String relativeFilePath, String subsFileBase) {
		this.portalContext = portalContext;
		this.baseContentFilePath = relativeFilePath;
		if (portalContext != null) {
			this.t = new TokenParser(portalContext, subsFileBase);
		}
	}

	/**
	 * <p>
	 * Gets the best-fit localized version of the secondary support content
	 * file, reads it into a string, and substitutes the tokens found in the
	 * string with the proper dynamic values, returning the interpolated
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
	 *             exception
	 */
	public String interpolate() throws Exception {

		if (portalContext == null || baseContentFilePath == null) {
			return null;
		}
		return super.interpolate();
	}

	/**
	 * Get an input stream for the best-candidate localized content file
	 * available among the secondary support files of the current portal
	 * component, based on the request locale and base content filename provided
	 * to the constructor. Null is returned if the file is not found or the
	 * portal context or content file provided to the constructor was null.
	 * 
	 * @return The input stream for the file
	 */
	protected InputStream getLocalizedContentFileAsStream() {
		if (portalContext == null || baseContentFilePath == null) {
			return null;
		}
		return I18nUtility.getLocalizedFileAsStream(portalContext,
				baseContentFilePath);
	}

	/**
	 * Get the locale from the portal request in the portal context provided to
	 * the constructor. Returns null if the portal context provided to the
	 * constructor was null.
	 * 
	 * @return The current preferred locale for the user
	 */
	protected Locale getLocale() {
		if (portalContext == null) {
			return null;
		}
		return portalContext.getPortalRequest().getLocale();
	}

	/**
	 * Get the email address from the portal User object in the portal context
	 * (portal request) provided to the constructor. Returns null if this has
	 * not been set in the request (eg, when the user is not logged-in), or the
	 * portal context provided to the constructor was null.
	 * 
	 * @return email
	 */
	protected String getEmail() {
		if (portalContext == null) {
			return null;
		}
		try {
			return (String) Utils.getUserProperty(portalContext,
					Consts.PROPERTY_EMAIL_ID);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Get the first (given) name from the portal User object in the portal
	 * context (portal request) provided to the constructor. Returns null if
	 * this has not been set in the request (eg, when the user is not
	 * logged-in), or the portal context provided to the constructor was null.
	 * 
	 * @return first (given) name
	 */
	protected String getFirstName() {
		if (portalContext == null) {
			return null;
		}
		try {
			return (String) Utils.getUserProperty(portalContext,
					Consts.PROPERTY_FIRSTNAME_ID);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Get the last (family) name from the portal User object in the portal
	 * context (portal request) provided to the constructor. Returns null if
	 * this has not been set in the request (eg, when the user is not
	 * logged-in), or the portal context provided to the constructor was null.
	 * 
	 * @return last (family) name
	 */
	protected String getLastName() {
		if (portalContext == null) {
			return null;
		}
		try {
			return (String) Utils.getUserProperty(portalContext,
					Consts.PROPERTY_LASTNAME_ID);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Get the site name for the current portal request from the portal context
	 * provided to the constructor. Returns null if this has not been set in the
	 * request, or the portal context provided to the constructor was null.
	 * 
	 * @return site name
	 */
	/* Added by CK for 1000790073 */
	protected String getSite() {
		if (portalContext == null) {
			return null;
		}
		try {
			String siteName = portalContext.getCurrentSite().getDNSName();
			return siteName;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get the authorization groups from the portlet request provided to the
	 * constructor. Returns null if these have not been set in the request, or
	 * the request provided to the constructor was null.
	 * 
	 * @return list of groups
	 */
	protected String[] getGroups() {
		if (portalContext == null) {
			return null;
		}
		return Utils.getGroups(portalContext);
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