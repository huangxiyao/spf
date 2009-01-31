/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 * 
 * This portlet utility class reads a file of text (eg an HTML file), substituting
 * dynamic values for various tokens, and returning the substituted
 * content to the calling class.
 */
package com.hp.it.spf.xa.interpolate.portlet;

import java.io.InputStream;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import java.util.Locale;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.it.spf.xa.misc.portlet.Consts;
import com.hp.it.spf.xa.misc.portlet.Utils;

/**
 * <p>
 * This portlet utility class reads a file of text (eg an HTML file) from the
 * class loader, substituting dynamic values for various tokens, and returning
 * the interpolated content to the calling class. Note that the base text
 * filename you provide is used to find a best-fit file for the current portlet
 * request's locale. This is all done in the interpolate method, based on
 * parameters you setup in the constructor.
 * </p>
 * <p>
 * This class uses the portlet <code>TokenParser</code> to do most of its
 * work. As of this writing, the following tokens are supported in the input
 * file (note: these are not XML - except where noted, tokens are case-sensitive
 * and do not require a closing tag):
 * </p>
 * 
 * <dl>
 * <dt><code>&lt;CONTENT-URL:<i>pathname</i>&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token to insert a URL for an <b>unlocalized</b> static resource
 * file into the interpolated content. You can put the file into the portlet
 * resource bundle directory on each portlet server (eg, for easy administrator
 * access). Or you can package the file as a static resource inside your portlet
 * WAR. Either way works. In either case, you can put the file into a
 * subdirectory of the portlet resource bundle directory or portlet application
 * root, respectively. For the <code><i>pathname</i></code> in the token, use
 * the filename and path to the subdirectory (if applicable), relative to the
 * portlet resource bundle directory or portlet application root.
 * </p>
 * <p>
 * For example, put <code>picture.jpg</code> into the portlet bundle directory
 * on each server, in the <code>images/</code> subdirectory. Or, put it into
 * the <code>images</code> subdirectory of your portlet WAR. Then put the
 * following markup into an HTML text file to be processed by the
 * FileInterpolator:
 * </p>
 * <p>
 * <code>&lt;IMG SRC="&lt;CONTENT-URL:/images/picture.jpg&gt;"&gt;</code>
 * </p>
 * <p>
 * The returned text string will contain the necessary URL for showing the image
 * to the user.
 * </p>
 * <p>
 * <b>Note:</b> For the portlet resource bundle directory to work, you must
 * have deployed the file-relay servlet into your portlet application.
 * </p>
 * <p>
 * <b>Note:</b> This token is for <b>unlocalized</b> content only. For
 * <b>localized</b> static content, see the
 * <code>&lt;LOCALIZED-CONTENT-URL:<i>pathname</i>&gt;</code> token.
 * Actually the <code>&lt;LOCALIZED-CONTENT-URL:<i>pathname</i>&gt;</code>
 * token works for unlocalized content too, so the
 * <code>&lt;CONTENT-URL:<i>pathname</i>&gt;</code> token is isn't really
 * necessary. It is retained for backward-compatibility.
 * </p>
 * 
 * <dt><code>&lt;EMAIL&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token to insert the email address of the user into the interpolated
 * content. The email address is taken from the user information passed from the
 * portal into the portlet request by SPF. For example,
 * <code>&lt;EMAIL&gt;</code> is replaced with <code>john.doe@acme.com</code>
 * for that user. (If the email address in the user profile map is null - eg the
 * user is not logged-in - then an empty string is inserted.)
 * </p>
 * </dd>
 * 
 * <dt><code>&lt;GROUP:<i>groups</i>&gt;...&lt;/GROUP&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token around a section of content which should only be included in
 * the interpolated content if the user is a member of any one (or more) of the
 * listed groups. For example, using this token, a single file can contain the
 * proper content for multiple kinds of users, simplifying administration of a
 * portlet which must display different content for different user groups.
 * </p>
 * <p>
 * The groups to be checked are provided to the FileInterpolator through the
 * portlet request given the constructor (see). SPF passes the groups from the
 * portal to the portlet in that request (this is a non-standard behavior).
 * </p>
 * <p>
 * In the <code><i>groups</i></code> parameter to the
 * <code>&lt;GROUP:<i>groups</i>&gt;</code> token, you can list just a
 * single group name, or multiple group names (use the <code>|</code>
 * character to delimit them). The content enclosed by the
 * <code>&lt;GROUP:<i>groups</i>&gt;</code> and <code>&lt;/GROUP&gt;</code>
 * tokens is omitted from the returned content unless the groups provided to the
 * constructor match one of those group names.
 * </p>
 * <p>
 * The content enclosed by the <code>&lt;GROUP:<i>groups</i>&gt;</code> and
 * <code>&lt;/GROUP&gt;</code> tokens can be anything, including any of the
 * special tokens listed here (even other
 * <code>&lt;GROUP:<i>groups</i>&gt;...&lt;/GROUP&gt;</code> sections - ie,
 * you can "nest" them).
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
 * Use this token to insert the ISO 639-1 language code from the current portlet
 * request (as provided in the locale of the request). Note that the country
 * code is not a part of this. For example, for a Japanese request,
 * <code>&lt;LANGUAGE-CODE&gt;</code> is replaced with <code>ja</code>.
 * </p>
 * </dd>
 * 
 * <dt><code>&lt;LANGUAGE-TAG&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token to insert the RFC 3066 language tag from the current portlet
 * request (as provided in the locale of the request). Note that the country
 * code is included in the language tag, if it was set in the locale (otherwise
 * the language tag consists of the language code only). For example, for a
 * French (Canada) request, <code>&lt;LANGUAGE-TAG&gt;</code> is replaced with
 * <code>fr-CA</code>.
 * </p>
 * </dd>
 * 
 * <dt><code>&lt;LOCALIZED-CONTENT-URL:<i>pathname</i>&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token to insert a URL for a (potentially localized) static resource
 * file into the interpolated content. This token lets you indicate the base
 * filename of the resource you want to display, via the
 * <code><i>pathname</i></code>. It will then find the best-candidate
 * localized version of that file for the user's locale which you have made
 * available. (For an unlocalized resource, the base file is used.) You can put
 * the base file and its localized variants into the portlet resource bundle
 * directory on each portlet server (eg, for easy administrator access). Or you
 * can package them as static resources inside your portlet WAR. Either way
 * works. In either case, you can put the file(s) into a subdirectory of the
 * portlet resource bundle directory or portlet application root, respectively.
 * For the <code><i>pathname</i></code> in the token, use the base filename
 * and path to the subdirectory (if applicable), relative to the portlet
 * resource bundle directory or portlet application root.
 * </p>
 * <p>
 * For example, put <code>picture.jpg</code> into the portlet bundle directory
 * on each server, in the <code>images/</code> subdirectory. Also put any
 * localized versions of the file (eg <code>picture_fr.jpg</code> for French,
 * <code>picture_fr_CA.jpg</code> for French (Canada), etc) into the same
 * location. (Or, put all those files into the <code>images/</code>
 * subdirectory of your portlet WAR.) Then put the following markup into an HTML
 * text file to be processed by the FileInterpolator:
 * </p>
 * <p>
 * <code>&lt;IMG SRC="&lt;LOCALIZED-CONTENT-URL:/images/picture.jpg&gt;"&gt;</code>
 * </p>
 * <p>
 * The returned text string will contain the necessary URL for showing the
 * best-fit image to the user. The logic for determining the best-fit resembles
 * that used by the Java-standard ResourceBundle class (see).
 * </p>
 * <p>
 * <b>Note:</b> For the portlet resource bundle directory to work, you must
 * have deployed the file-relay servlet into your portlet application.
 * </p>
 * 
 * <dt><code>&lt;LOGGED-IN&gt;...&lt;/LOGGED-IN&gt;</code><br>
 * <code>&lt;LoGGED-OUT&gt;...&lt;/LOGGED-OUT&gt;</code></dt>
 * <dd>
 * <p>
 * Use these tokens around sections of content which should only be included in
 * the interpolated content if the user is logged-in or logged-out,
 * respectively. The wrapped content will be omitted from the returned content
 * when the user login status does not match. (This token uses the portlet user
 * information which the SPF passes from the portal to the portlet.)
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
 * &lt;LOGGED-IN&gt;
 * This content is only for users who are logged-in.
 * &lt;/LOGGED-IN&gt;
 * &lt;LOGGED-OUT&gt;
 * This content is only for users who are logged-out.
 * &lt;/LOGGED-OUT&gt;
 * </pre>
 * 
 * </p>
 * </dd>
 * 
 * <dt><code>&lt;NAME&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token to insert the full name of the user into the interpolated
 * content, where the given (first) and family (last) names are in the customary
 * order for the user's locale. The name is taken from the user information
 * placed in the portlet request by SPF. The locale is also taken from the
 * current portlet request.
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
 * <dt><code>&lt;PORTLET:<i>portlets</i>&gt;...&lt;/PORTLET&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token around a section of content which should only be included in
 * the interpolated content if the current request is for a particular portlet
 * (as indicated by the portlet friendly ID in the request). For example, using
 * this token, a single file can contain content for multiple portlets, perhaps
 * making administration of the portlet contents easier.
 * </p>
 * <p>
 * In the <code><i>portlets</i></code> parameter to the
 * <code>&lt;PORTLET:<i>portlets</i>&gt;</code> token, you can list just a
 * single portlet friendly ID, or multiple (use the <code>|</code> character
 * to delimit them). The content enclosed by the
 * <code>&lt;PORTLET:<i>portlets</i>&gt;</code> and
 * <code>&lt;/PORTLET&gt;</code> tokens is omitted from the returned content
 * unless the portlet friendly ID in the request matches one of those values.
 * The match is a case-insensitive substring match.
 * </p>
 * <p>
 * This method assumes that the portlet friendly ID has been set by the portal
 * into the proper-namespaced attribute in the portlet request. Vignette Portal
 * does this automatically (a non-standard behavior).
 * </p>
 * <p>
 * The content enclosed by the <code>&lt;PORTLET:<i>portlets</i>&gt;</code>
 * and <code>&lt;/PORTLET&gt;</code> tokens can be anything, including any of
 * the special tokens supported by this class (including other
 * <code>&lt;PORTLET:<i>portlets</i>&gt;...&lt;/PORTLET&gt;</code> tokens -
 * ie you can "nest" them.
 * </p>
 * <p>
 * For example, the following markup selectively includes or omits the content
 * depending on the portlet as indicated:
 * </p>
 * <p>
 * 
 * <pre>
 * This content is for all portlets using this file to show.
 * &lt;PORTLET:portlet_A|portlet_B&gt;
 * This content is only for portlet_A or portlet_B to show.
 * &lt;PORTLET:B&gt;
 * This content is only for portlet_B to show.
 * &lt;/PORTLET&gt;
 * &lt;/PORTLET&gt;
 * </pre>
 * 
 * </p>
 * </dd>
 * 
 * <dt><code>&lt;REQUEST-URL&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token to insert into the content the complete current URL which the
 * browser used to access the current portlet and page. This URL is taken from a
 * non-standard attribute in the request which it is assumed the portal has set
 * (SPF sets this by default). For example, <code>&lt;REQUEST-URL&gt;</code>
 * is replaced with
 * <code>http://portal.hp.com/portal/site/itrc/template.PAGE/...</code> when
 * the portlet is requested from the <code>itrc</code> portal site on the
 * <code>portal.hp.com</code> server using HTTP.
 * </p>
 * </dd>
 * 
 * <dt><code>&lt;ROLE:<i>roles</i>&gt;...&lt;/ROLE&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token around a section of content which should only be included in
 * the interpolated content if the user is in any one (or more) of the listed
 * roles. For example, using this token, a single file can contain the proper
 * content for multiple kinds of users, simplifying administration of a portlet
 * which must display different content for different user roles.
 * </p>
 * <p>
 * The roles to be checked are those contained in the PortletRequest, provided
 * to the FileInterpolator through the constructor (see).
 * </p>
 * <p>
 * In the <code>roles</code> parameter to the
 * <code>&lt;ROLE:<i>roles</i>&gt;</code> token, you can list just a single
 * role name, or multiple role names (use the <code>|</code> character to
 * delimit them). The content enclosed by the
 * <code>&lt;ROLE:<i>roles</i>&gt;</code> and <code>&lt;/ROLE&gt;</code>
 * tokens is omitted from the returned content unless the PortletRequest
 * indicates the user is in one of those role(s).
 * </p>
 * <p>
 * The content enclosed by the <code>&lt;ROLE:<i>roles</i>&gt;</code> and
 * <code>&lt;/ROLE&gt;</code> tokens can be anything, including any of the
 * special tokens listed here (even other
 * <code>&lt;ROLE:<i>roles</i>&gt;...&lt;/ROLE&gt;</code> sections - ie, you
 * can "nest" them).
 * </p>
 * <p>
 * For example, the following markup selectively includes or omits the content
 * depending on the user roles as indicated:
 * </p>
 * <p>
 * 
 * <pre>
 * This content is for everybody.
 * &lt;ROLE:A&gt;
 * This content is only for users in role A.
 * &lt;ROLE:B|C&gt;
 * This content is only for users in roles A and B, or A and C.
 * &lt;/ROLE&gt;
 * &lt;/ROLE&gt;
 * </pre>
 * 
 * </p>
 * </dd>
 * 
 * <dt><code>&lt;SITE&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token to insert the site name of the current portal site into the
 * interpolated content. The site name is taken from a non-standard attribute in
 * the request which it is assumed the portal has set (SPF sets this by default -
 * the value set is the so-called "site DNS name"). For example,
 * <code>&lt;SITE&gt;</code> is replaced with <code>itrc</code> when the
 * portlet is requested from the <code>itrc</code> portal site.
 * </p>
 * </dd>
 * 
 * <dt><code>&lt;SITE:<i>names</i>&gt;...&lt;/SITE&gt;</code></dt>
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
 * <code>&lt;SITE:<i>names</i>&gt;</code> token, you can list just a single
 * site name, or multiple (use the <code>|</code> character to delimit them).
 * The content enclosed by the <code>&lt;SITE:<i>names</i>&gt;</code> and
 * <code>&lt;/SITE&gt;</code> tokens is omitted from the returned content
 * unless the site name in the request matches one of those values. The match is
 * case-insensitive.
 * </p>
 * <p>
 * This method assumes that the site name has been set by the portal into the
 * proper-namespaced attribute in the portlet request. SPF does this
 * automatically (a non-standard behavior).
 * </p>
 * <p>
 * The content enclosed by the <code>&lt;SITE:<i>names</i>&gt;</code> and
 * <code>&lt;/SITE&gt;</code> tokens can be anything, including any of the
 * special tokens supported by this class (including other
 * <code>&lt;SITE:<i>names</i>&gt;...&lt;/SITE&gt;</code> tokens - ie you
 * can "nest" them.
 * </p>
 * <p>
 * For example, the following markup selectively includes or omits the content
 * depending on the site as indicated:
 * </p>
 * <p>
 * 
 * <pre>
 * This content is for all portlets using this file to show.
 * &lt;SITE:site_A|site_B&gt;
 * This content is only to be shown when the portlet is in site_A or site_B.
 * &lt;SITE:site_B&gt;
 * This content is only to be shown when the portlet is in site_B.
 * &lt;/SITE&gt;
 * &lt;/SITE&gt;
 * </pre>
 * 
 * </p>
 * </dd>
 * 
 * <dt><code>&lt;SITE-URL&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token to insert the URL for the home page of the current portal site
 * into the interpolated content. This URL is taken from a non-standard
 * attribute in the request which it is assumed the portal has set (SPF sets
 * this by default). For example, <code>&lt;SITE-URL&gt;</code> is replaced
 * with <code>http://portal.hp.com/portal/site/itrc/</code> when the portlet
 * is requested from the <code>itrc</code> portal site on the
 * <code>portal.hp.com</code> server using HTTP.
 * </p>
 * </dd>
 * 
 * <dt><code>&lt;TOKEN:<i>key</i>&gt;</code></dt>
 * <dd>
 * <p>
 * Use this token to lookup a value for the given key in a property file, and
 * insert that value into the interpolated content. The property file, by
 * default, is <code>default_tokens.properties</code>, but you can override
 * that in the FileInterpolator constructor. The property value may itself
 * contain any content including any of the special markup tokens supported by
 * FileInterpolator, <b>except</b> another
 * <code>&lt;TOKEN:<i>key</i>&gt;</code> token.
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
 * taken from the user information placed by SPF in the current portlet request.
 * For example, <code>&lt;USER-PROPERTY:user.name.given&gt;</code> is replaced
 * with <code>Scott</code> for a user with such a first (given) name. The
 * property name <code><i>key</i></code>'s are not listed here and may vary
 * based on the portal implementation.
 * </p>
 * <p>
 * If the property is not found in the user object, or is not a string, then an
 * empty string will be inserted. Similarly, if the user object itself is guest
 * or null (ie the user is not logged-in), then an empty string is inserted.
 * </p>
 * </dd>
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
 * <ul>
 * <li><code>&lt;TOKEN:<i>key</i>&gt;</code></li>
 * <li><code>&lt;SITE&gt;</code></li>
 * <li><code>&lt;SITE-URL&gt;</code></li>
 * <li><code>&lt;REQUEST-URL&gt;</code></li>
 * <li><code>&lt;LANGUAGE-CODE&gt;</code></li>
 * <li><code>&lt;LANGUAGE-TAG&gt;</code></li>
 * <li><code>&lt;EMAIL&gt;</code></li>
 * <li><code>&lt;NAME&gt;</code></li>
 * <li><code>&lt;USER-PROPERTY:<i>key</i>&gt;</code></li>
 * <li><code>&lt;CONTENT-URL:<i>path</i>&gt;</code></li>
 * <li><code>&lt;LOCALIZED-CONTENT-URL:<i>path</i>&gt;</code></li>
 * <li><code>&lt;SITE:<i>names</i>&gt;</code></li>
 * <li><code>&lt;LOGGED-IN&gt;</code></li>
 * <li><code>&lt;LOGGED-OUT&gt;</code></li>
 * <li><code>&lt;GROUP:<i>groups</i>&gt;</code></li>
 * <li><code>&lt;PORTLET:<i>portlets</i>&gt;</code></li>
 * <li><code>&lt;ROLE:<i>roles</i>&gt;</code></li>
 * </ul>
 * </p>
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see com.hp.it.spf.xa.interpolate.FileInterpolator
 *      com.hp.it.spf.xa.interpolate.portlet.TokenParser
 *      com.hp.it.spf.xa.interpolate.TokenParser
 */
public class FileInterpolator extends
		com.hp.it.spf.xa.interpolate.FileInterpolator {

	/**
	 * Portlet request.
	 */
	private PortletRequest request = null;

	/**
	 * Portlet response.
	 */
	private PortletResponse response = null;

	/**
	 * Portlet log.
	 */
	private Log portletLog = LogFactory.getLog(FileInterpolator.class);

	/**
	 * <p>
	 * Constructs a new <code>FileInterpolator</code> for the given base
	 * content pathname, request, and response.
	 * </p>
	 * <p>
	 * The base content pathname provided should include any necessary path
	 * (relative to the class loader) followed by the base filename (including
	 * extension) for a resource bundle of one or more localized files. The
	 * interpolate method will open the best-fit localized file based on the
	 * locale in the given request.
	 * </p>
	 * <p>
	 * <b>Note:</b> No token-substitutions property file is passed in this
	 * constructor. Therefore any <code>&lt;TOKEN:<i>key</i>&gt;</code>
	 * tokens in the file content will be resolved against the default
	 * token-substitutions property file (<code>default_tokens.properties</code>).
	 * </p>
	 * 
	 * @param pRequest
	 *            The portlet request
	 * @param pResponse
	 *            The portlet response
	 * @param pBaseContentFilePath
	 *            The base filename and path relative to where the class loader
	 *            searches for the file content to interpolate
	 */
	public FileInterpolator(PortletRequest pRequest, PortletResponse pResponse,
			String pBaseContentFilePath) {
		this.request = pRequest;
		this.response = pResponse;
		this.baseContentFilePath = pBaseContentFilePath;
		if (pRequest != null && pResponse != null) {
			this.t = new TokenParser(pRequest, pResponse);
		}
	}

	/**
	 * <p>
	 * Constructs a new <code>FileInterpolator</code> for the given base
	 * content pathname, request, response, and token-substitutions pathname.
	 * </p>
	 * <p>
	 * The base content pathname provided should include any necessary path
	 * (relative to the class loader) followed by the base filename (including
	 * extension) for a resource bundle of one or more localized files. The
	 * interpolate method will open the best-fit localized file based on the
	 * locale in the given request.
	 * </p>
	 * <p>
	 * The token-substitutions pathname provided is to be used with any
	 * <code>&lt;TOKEN:<i>key</i>&gt;</code> tokens in the file content;
	 * they will be resolved against the file whose pathname you provide. The
	 * pathname should include any necessary path (relative to the class loader)
	 * followed by the filename (the extension <code>.properties</code> is
	 * required and assumed). If you know there are no such tokens in the file
	 * content, you can pass null for this parameter.
	 * </p>
	 * 
	 * @param pRequest
	 *            The portlet request
	 * @param pResponse
	 *            The portlet response
	 * @param pBaseContentFilePath
	 *            The base filename and path relative to where the class loader
	 *            searches for the file content to interpolate
	 * @param subsFilePath
	 *            The filename and path relative to where the class loader
	 *            searches for the token-substitutions property file (for
	 *            purposes of any <code>&lt;TOKEN:key&gt;</code> tokens in the
	 *            file content)
	 */
	public FileInterpolator(PortletRequest pRequest, PortletResponse pResponse,
			String pBaseContentFilePath, String subsFilePath) {
		this.request = pRequest;
		this.response = pResponse;
		this.baseContentFilePath = pBaseContentFilePath;
		if (pRequest != null && pResponse != null) {
			this.t = new TokenParser(pRequest, pResponse, subsFilePath);
		}
	}

	/**
	 * <p>
	 * Gets the best-fit localized version of the content file, reads it into a
	 * string, and substitutes the tokens found in the string with the proper
	 * dynamic values, returning the interpolated content. The content filename
	 * and location, the locale for which to find the best-candidate, and the
	 * proper substitution values for the tokens are all based on the
	 * information you provided when calling the constructor. Null is returned
	 * if there are problems interpolating (eg the file is not found or was
	 * empty, or the request or content file you provided when calling the
	 * constructor were null). See class documentation for more information
	 * about the tokens which are substituted.
	 * </p>
	 * 
	 * @return The interpolated file content
	 * @throws Exception
	 *             exception
	 */
	public String interpolate() throws Exception {

		if (request == null || baseContentFilePath == null) {
			return null;
		}
		String content = super.interpolate();
		TokenParser t = (TokenParser) this.t;

		// parse the portlet token
		// added by ck for cr 1000790086
		content = t.parsePortletContainer(content, getPortletID());
		// parse the portlet token ---end

		// parse the role token
		content = t.parseRoleContainer(content);

		return content;
	}

	/**
	 * Get an input stream for the best-candidate localized content file
	 * available in the portlet bundle directory or portlet application, based
	 * on the request locale and base content pathname provided to the
	 * constructor. Null is returned if the file is not found or the request or
	 * content file provided to the constructor were null.
	 * 
	 * @return The input stream for the file
	 */
	protected InputStream getLocalizedContentFileAsStream() {
		if (request == null || baseContentFilePath == null) {
			return null;
		}
		return com.hp.it.spf.xa.i18n.portlet.I18nUtility
				.getLocalizedFileStream(request, baseContentFilePath);
	}

	/**
	 * Get the locale from the portal request in the portal context provided to
	 * the constructor. Returns null if the portal context provided to the
	 * constructor was null.
	 * 
	 * @return The current preferred locale for the user
	 */
	protected Locale getLocale() {
		if (request == null) {
			return null;
		}
		return request.getLocale();
	}

	/**
	 * Get the email address from the user information (<code>PortletRequest.USER_INFO</code>)
	 * in the portlet request provided to the constructor. Returns null if this
	 * has not been set in the request (eg, when the user is not logged-in), or
	 * the request provided to the constructor was null.
	 * 
	 * @return email
	 */
	protected String getEmail() {
		if (request == null) {
			return null;
		}
		try {
			return (String) Utils.getUserProperty(request,
					Consts.KEY_USER_INFO_EMAIL);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Get the first (given) name from the user information (<code>PortletRequest.USER_INFO</code>)
	 * in the portlet request provided to the constructor. Returns null if this
	 * has not been set in the request (eg, when the user is not logged-in), or
	 * the request provided to the constructor was null.
	 * 
	 * @return first (given) name
	 */
	protected String getFirstName() {
		if (request == null) {
			return null;
		}
		try {
			return (String) Utils.getUserProperty(request,
					Consts.KEY_USER_INFO_GIVEN_NAME);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Get the last (family) name from the user information (<code>PortletRequest.USER_INFO</code>)
	 * in the portlet request provided to the constructor. Returns null if this
	 * has not been set in the request (eg, when the user is not logged-in), or
	 * the request provided to the constructor was null.
	 * 
	 * @return last (family) name
	 */
	protected String getLastName() {
		if (request == null) {
			return null;
		}
		try {
			return (String) Utils.getUserProperty(request,
					Consts.KEY_USER_INFO_FAMILY_NAME);
		} catch (ClassCastException e) {
			return null;
		}
	}

	/**
	 * Get the site name from the portlet request provided to the constructor.
	 * Returns null if this has not been set in the request, or the request
	 * provided to the constructor was null.
	 * 
	 * @return site name
	 */
	protected String getSite() {
		if (request == null) {
			return null;
		}
		return Utils.getPortalSiteName(request);
	}

	/**
	 * Get the portal site root (ie home page) URL for the current portal site,
	 * from the portlet request provided to the constructor. Returns null if
	 * this has not been set in the request, or the request provided to the
	 * constructor was null.
	 * 
	 * @return site URL string
	 */
	protected String getSiteURL() {
		if (request == null) {
			return null;
		}
		return Utils.getPortalSiteURL(request);
	}

	/**
	 * Get the portal request URL for the current request. This is the URL which
	 * was opened by the browser in order to invoke this portlet. It is obtained
	 * from the portlet request provided to the constructor. Returns null if
	 * this has not been set in the request, or the request provided to the
	 * constructor was null.
	 * 
	 * @return request URL string
	 */
	protected String getRequestURL() {
		if (request == null) {
			return null;
		}
		return Utils.getPortalRequestURL(request);
	}

	/**
	 * Get the authorization groups from the portlet request provided to the
	 * constructor. Returns null if these have not been set in the request, or
	 * the request provided to the constructor was null.
	 * 
	 * @return list of groups
	 */
	protected String[] getGroups() {
		if (request == null) {
			return null;
		}
		return Utils.getGroups(request);
	}

	/**
	 * Return true if the user is logged-in, false otherwise. The login status
	 * is indicated in the portlet request provided to the constructor; false is
	 * also returned if that request was null.
	 * 
	 * @return true if logged-in, false if not logged-in
	 */
	protected boolean getLoginStatus() {
		if (request == null) {
			return false;
		}
		return Utils.isAuthenticatedUser(request);
	}

	/**
	 * Get the portlet ID (ie the Vignette portlet friendly ID) from the portlet
	 * request provided to the constructor. Returns null if this has not been
	 * set in the request, or the request provided to the constructor was null.
	 * 
	 * @return portlet ID
	 */
	protected String getPortletID() {
		if (request == null) {
			return null;
		}
		return Utils.getPortletID(request);
	}

	/**
	 * Log warning.
	 * 
	 * @param msg
	 *            log message
	 */
	protected void logWarning(String msg) {
		if (portletLog.isWarnEnabled()) {
			portletLog.warn(msg);
		}
	}
}