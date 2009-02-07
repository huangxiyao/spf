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

import com.hp.it.spf.xa.i18n.portlet.I18nUtility;
import com.hp.it.spf.xa.misc.portlet.Consts;
import com.hp.it.spf.xa.misc.portlet.Utils;

/**
 * <p>
 * This portlet utility class reads a file of text (eg an HTML file) from the
 * class loader, substituting dynamic values for various tokens, and returning
 * the interpolated content to the calling class. Note that:
 * </p>
 * 
 * <ul>
 * <li> The base text filename you provide is used to find a best-fit file for
 * the locale you provide (by default, the locale in the portlet request you
 * provide), in the fashion of the Java standard for {@link ResourceBundle}.</li>
 * <li> The base text filename may be for an internal resource (ie contained
 * inside your portlet WAR), or an external resource (ie contained outside your
 * portlet WAR in the <i>portlet resource bundle folder</i>).</li>
 * <li> The base text filename may be a base filename for a bundle of localized
 * files, as per {@link ResourceBundle}. All of the localized files (except for
 * the the base file itself) must be tagged with the locale, as per
 * <code>ResourceBundle</code>.</li>
 * <li> <b>Important:</b> The files must by UTF-8 encoded.</li>
 * </ul>
 * 
 * <p>
 * The selection and loading of the proper localized text file, from the proper
 * location, and subsequent interpolation of its content, is all done in the
 * {@link #interpolate()} method, based on parameters you setup in the
 * constructor.
 * </p>
 * <p>
 * This class uses the {@link TokenParser} to do most of its work. As of this
 * writing, the following tokens are supported in the input file. <b>Note:</b>
 * Your content may use <code>&lt;</code> and <code>&gt;</code> instead of
 * <code>{</code> and <code>}</code> as the token delimiters, if desired.
 * The token names are case-sensitive.
 * 
 * </p>
 * 
 * <dl>
 * <dt><a name="content-url"><code>{CONTENT-URL:<i>pathname</i>}</code></a></dt>
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
 * <code>FileInterpolator</code>:
 * </p>
 * <p>
 * <code>&lt;IMG SRC="{CONTENT-URL:/images/picture.jpg}"&gt;</code>
 * </p>
 * <p>
 * The returned text string will contain the necessary URL for showing the image
 * to the user: either a file-relay servlet URL (if the file was found in the
 * portlet resource bundle folder) or a static resource URL (if the file was
 * found inside your WAR). In either case, the URL will be properly
 * portlet-encoded and ready for presentation to the browser. If the file was
 * not found, a URL pointing to that filename inside your WAR will be expressed
 * anyway. (This of course will cause an HTTP 404 error if the browser
 * subsequently opens the URL - this is intentional and will help you detect the
 * missing file.)
 * </p>
 * <p>
 * <b>Note:</b> For the portlet resource bundle directory to work, you must
 * have properly configured and deployed the file-relay servlet somewhere
 * accessible to the browser and the portlet bundle folder (eg, in your portlet
 * application). And you must have configured
 * <code>i18n_portlet_config.properties</code> with any non-default portlet
 * bundle folder or relay servlet URL. This is documented elsewhere.
 * </p>
 * <p>
 * <b>Note:</b> This token is for <b>unlocalized</b> content only. For
 * <b>localized</b> static content, see the
 * <code>{LOCALIZED-CONTENT-URL:<i>pathname</i>}</code> token. Actually the
 * <code>{LOCALIZED-CONTENT-URL:<i>pathname</i>}</code> token works for
 * unlocalized content too, so the <code>{CONTENT-URL:<i>pathname</i>}</code>
 * token is isn't really necessary. It is retained for backward-compatibility.
 * </p>
 * <p>
 * <b>Note:</b> Your <code><i>pathname</i></code> can "nest" any of the
 * other <i>non-parameterized</i> tokens listed here: <code>{SITE}</code>,
 * <code>{LANGUAGE-CODE}</code>, etc. Except for
 * <code>{INCLUDE:<i>key</i>}</code>, you cannot "nest" any
 * <i>parameterized</i> tokens inside your <code><i>pathname</i></code>.
 * </p>
 * </dd>
 * 
 * <dt><a name="country-code"><code>{COUNTRY-CODE}</code></a></dt>
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
 * <dt><a name="email"><code>{EMAIL}</code></a></dt>
 * <dd>
 * <p>
 * Use this token to insert the email address of the user into the interpolated
 * content. The email address is taken from the user profile map passed from the
 * portal into the portlet request by SPF. For example, <code>{EMAIL}</code>
 * is replaced with <code>john.doe@acme.com</code> for that user. (If the
 * email address in the user profile map is null - eg the user is not logged-in -
 * then an empty string is inserted.)
 * </p>
 * </dd>
 * 
 * <dt><a name="group"><code>{GROUP:<i>groups</i>}...{/GROUP}</code></a></dt>
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
 * <p>
 * Your <code><i>groups</i></code> can "nest" any of the other
 * <i>non-parameterized</i> tokens listed here: <code>{SITE}</code>,
 * <code>{LANGUAGE-CODE}</code>, etc. Except for
 * <code>{INCLUDE:<i>key</i>}</code>, you cannot "nest" any
 * <i>parameterized</i> tokens inside your <code><i>groups</i></code>.
 * </p>
 * </dd>
 * 
 * <dt><a name="language-code"><code>{LANGUAGE-CODE}</code></a></dt>
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
 * <dt><a name="token"><code>{INCLUDE:<i>key</i>}</code></a></dt>
 * <dd>
 * <p>
 * Use this token to lookup a value for the given key in a property file, and
 * insert that value into the interpolated content. The property file, by
 * default, is <code>default_includes.properties</code>, but you can override
 * that in the <code>FileInterpolator</code> constructor. Whether you use
 * <code>default_includes.properties</code> or your own token-substitution
 * property file, the file should be loaded into a location accessible to the
 * class loader. The property values may include any text content.
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
 * <code>&lt;A HREF="{INCLUDE:url.hp-shopping}"&gt;Go to HP shopping.&lt;/A&gt;</code>
 * </p>
 * <p>
 * Then the interpolated content will contain a hyperlink taking the user to the
 * URL value given in your property file:
 * </p>
 * <p>
 * <code>&lt;A HREF="http://shopping.hp.com"&gt;Go to HP shopping.&lt;/A&gt;</code>
 * </p>
 * <p>
 * Additionally, this token supports nesting with other tokens. As follows:
 * </p>
 * <ul>
 * <li>
 * <p>
 * The property value for this token may contain <b>any</b> of the other
 * special markup tokens supported by <code>FileInterpolator</code>,
 * <b>except</b> another <code>{INCLUDE:<i>key</i>}</code> token. In other
 * words, you can "nest" other tokens inside the propery values expressed by
 * this token.
 * </p>
 * <p>
 * For example, assume we have this in our property file:
 * </p>
 * <p>
 * <code>url.hp-shopping=http://shopping.hp.com?lang={LANGUAGE-CODE}&cc={COUNTRY-CODE}</code>
 * </p>
 * <p>
 * If your input file is as above, then the interpolated content will include
 * the user's language and country code in the URL from the property file. For
 * example, for a Brazil Portuguese user:
 * </p>
 * <p>
 * <code>&lt;A HREF="http://shopping.hp.com?lang=pt&cc=BR"&gt;Go to HP shopping.&lt;/A&gt;</code>
 * </p>
 * </li>
 * <li>
 * <p>
 * Vice-versa, the <code>{INCLUDE:<i>key</i>}</code> token can itself be
 * used within the parameter to <b>any</b> of the other tokens, except another
 * <code>{INCLUDE:<i>key</i>}</code> token. So you can "nest" this token
 * inside the parameter values to other tokens.
 * </p>
 * <p>
 * For example, assume we now have this in our property file:
 * </p>
 * <p>
 * <code>image.current-promo=december_sale.gif</code>
 * </p>
 * <p>
 * Also imagine the <code>december_sale.gif</code> is setup properly, as an
 * external (possibly localized) image serviced by the file relay servlet (see
 * discussion elsewhere). If your input file contains the following:
 * </p>
 * <p>
 * <code>&lt;IMG SRC="{LOCALIZED-CONTENT-URL:/images/{INCLUDE:image.current-promo}}"&gt;</code>
 * </p>
 * <p>
 * Then the interpolated content will display the proper localized version of
 * the <code>december_sale.gif</code> image to the user; like this, for
 * example, for a Japanese (Japan) user:
 * </p>
 * <p>
 * <code>&lt;IMG SRC="/relay/images/december_sale_ja_JP.gif"&gt;</code>
 * </p>
 * <p>
 * (In actuality, the URL would be portlet-encoded for the SPF portal; the
 * <i>unencoded</i> URL is shown above for simplicity.)
 * </p>
 * <blockquote>
 * <p>
 * <b>Note:</b> Although you can "nest" <code>{INCLUDE:<i>key</i>}</code>
 * within other token's parameters, you cannot nest other token parameters of
 * any kind inside the <code><i>key</i></code> parameter for
 * <code>{INCLUDE:<i>key</i>}</code>. In other words, the
 * <code><i>key</i></code> is always treated as a literal.
 * </p>
 * </blockquote> </li>
 * </ul>
 * <p>
 * A template for the token substitution property file, also named
 * <code>default_includes.properties</code> (you should rename your copy), is
 * available with the SPF.
 * </p>
 * </dd>
 * 
 * <dt><a name="language-tag"><code>{LANGUAGE-TAG}</code></a></dt>
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
 * <dt><a name="localized-content-url"><code>{LOCALIZED-CONTENT-URL:<i>pathname</i>}</code></a></dt>
 * <dd>
 * <p>
 * Use this token to insert a URL for a (potentially localized) static resource
 * file into the interpolated content. This token lets you indicate the base
 * filename of the resource you want to display, via the
 * <code><i>pathname</i></code>. It will then find the best-candidate
 * localized version of that file for the current locale which you have made
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
 * text file to be processed by the <code>FileInterpolator</code>:
 * </p>
 * <p>
 * <code>&lt;IMG SRC="{LOCALIZED-CONTENT-URL:/images/picture.jpg}"&gt;</code>
 * </p>
 * <p>
 * The returned text string will contain the necessary URL for showing the
 * best-fit image to the user. The logic for determining the best-fit resembles
 * that used by the Java-standard {@link ResourceBundle} class (see). The URL
 * will be either a file-relay servlet URL (if the file was found in the portlet
 * resource bundle folder) or a static resource URL (if the file was found
 * inside your WAR). In either case, the URL will be properly portlet-encoded
 * and ready for presentation to the browser. If the file was not found, a URL
 * pointing to the base filename inside your WAR will be expressed anyway. (This
 * of course will cause an HTTP 404 error if the browser subsequently opens the
 * URL - this is intentional and will help you detect the missing file.)
 * </p>
 * <p>
 * <b>Note:</b> For the portlet resource bundle directory to work, you must
 * have properly configured and deployed the file-relay servlet somewhere
 * accessible to the browser and the portlet bundle folder (eg, in your portlet
 * application). And you must have configured
 * <code>i18n_portlet_config.properties</code> with any non-default portlet
 * bundle folder or relay servlet URL. This is documented elsewhere.
 * </p>
 * <p>
 * <b>Note:</b> Your <code><i>pathname</i></code> can "nest" any of the
 * other <i>non-parameterized</i> tokens listed here: <code>{SITE}</code>,
 * <code>{LANGUAGE-CODE}</code>, etc. Except for
 * <code>{INCLUDE:<i>key</i>}</code>, you cannot "nest" any
 * <i>parameterized</i> tokens inside your <code><i>pathname</i></code>.
 * </p>
 * </dd>
 * 
 * <dt><a name="logged-in"><code>{LOGGED-IN}...{/LOGGED-IN}</code></a></dt>
 * <dt><a name="logged-out"><code>{LOGGED-OUT}...{/LOGGED-OUT}</code></a></dt>
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
 * <dt><a name="name"><code>{NAME}</code></a></dt>
 * <dd>
 * <p>
 * Use this token to insert the full name of the user into the interpolated
 * content, where the given (first) and family (last) names are in the customary
 * order for the current locale. The name is taken from the user profile map
 * placed in the portlet request by SPF.
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
 * <dt><a name="portlet"><code>{PORTLET:<i>portlets</i>}...{/PORTLET}</code></a></dt>
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
 * <code>{PORTLET:<i>portlets</i>}</code> token, you can list just a single
 * portlet friendly ID, or multiple (use the <code>|</code> character to
 * delimit them). The content enclosed by the
 * <code>{PORTLET:<i>portlets</i>}</code> and <code>{/PORTLET}</code>
 * tokens is omitted from the returned content unless the portlet friendly ID in
 * the request matches one of those values. The match is a case-insensitive
 * substring match.
 * </p>
 * <p>
 * This method assumes that the portlet friendly ID has been set by the portal
 * into the proper-namespaced attribute in the portlet request. Vignette Portal
 * does this automatically (a non-standard behavior).
 * </p>
 * <p>
 * The content enclosed by the <code>{PORTLET:<i>portlets</i>}</code> and
 * <code>{/PORTLET}</code> tokens can be anything, including any of the
 * special tokens supported by this class (including other
 * <code>{PORTLET:<i>portlets</i>}...{/PORTLET}</code> tokens - ie you can
 * "nest" them.
 * </p>
 * <p>
 * For example, the following markup selectively includes or omits the content
 * depending on the portlet as indicated:
 * </p>
 * <p>
 * 
 * <pre>
 * This content is for all portlets using this file to show.
 * {PORTLET:portlet_A|portlet_B}
 * This content is only for portlet_A or portlet_B to show.
 * {PORTLET:B}
 * This content is only for portlet_B to show.
 * {/PORTLET}
 * {/PORTLET}
 * </pre>
 * 
 * </p>
 * <p>
 * Your <code><i>portlets</i></code> can "nest" any of the other
 * <i>non-parameterized</i> tokens listed here: <code>{SITE}</code>,
 * <code>{LANGUAGE-CODE}</code>, etc. Except for
 * <code>{INCLUDE:<i>key</i>}</code>, you cannot "nest" any
 * <i>parameterized</i> tokens inside your <code><i>portlets</i></code>.
 * </p>
 * </dd>
 * 
 * <dt><a name="request-url"><code>{REQUEST-URL}</code></a></dt>
 * <dd>
 * <p>
 * Use this token to insert into the content the complete current URL which the
 * browser used to access the current portlet and page. This URL is taken from a
 * non-standard attribute in the request which it is assumed the portal has set
 * (SPF sets this by default). For example, <code>{REQUEST-URL}</code> is
 * replaced with
 * <code>http://portal.hp.com/portal/site/itrc/template.PAGE/...</code> when
 * the portlet is requested from the <code>itrc</code> portal site on the
 * <code>portal.hp.com</code> server using HTTP.
 * </p>
 * </dd>
 * 
 * <dt><a name="role"><code>{ROLE:<i>roles</i>}...{/ROLE}</code></a></dt>
 * <dd>
 * <p>
 * Use this token around a section of content which should only be included in
 * the interpolated content if the user is in any one (or more) of the listed
 * roles. For example, using this token, a single file can contain the proper
 * content for multiple kinds of users, simplifying administration of a portlet
 * which must display different content for different user roles.
 * </p>
 * <p>
 * The roles to be checked are those contained in the portlet request, provided
 * to the <code>FileInterpolator</code> through the constructor (see).
 * </p>
 * <p>
 * In the <code>roles</code> parameter to the <code>{ROLE:<i>roles</i>}</code>
 * token, you can list just a single role name, or multiple role names (use the
 * <code>|</code> character to delimit them). The content enclosed by the
 * <code>{ROLE:<i>roles</i>}</code> and <code>{/ROLE}</code> tokens is
 * omitted from the returned content unless the PortletRequest indicates the
 * user is in one of those role(s).
 * </p>
 * <p>
 * The content enclosed by the <code>{ROLE:<i>roles</i>}</code> and
 * <code>{/ROLE}</code> tokens can be anything, including any of the special
 * tokens listed here (even other <code>{ROLE:<i>roles</i>}...{/ROLE}</code>
 * sections - ie, you can "nest" them).
 * </p>
 * <p>
 * For example, the following markup selectively includes or omits the content
 * depending on the user roles as indicated:
 * </p>
 * <p>
 * 
 * <pre>
 * This content is for everybody.
 * {ROLE:A}
 * This content is only for users in role A.
 * {ROLE:B|C}
 * This content is only for users in roles A and B, or A and C.
 * {/ROLE}
 * {/ROLE}
 * </pre>
 * 
 * </p>
 * <p>
 * Your <code><i>roles</i></code> can "nest" any of the other
 * <i>non-parameterized</i> tokens listed here: <code>{SITE}</code>,
 * <code>{LANGUAGE-CODE}</code>, etc. Except for
 * <code>{INCLUDE:<i>key</i>}</code>, you cannot "nest" any
 * <i>parameterized</i> tokens inside your <code><i>roles</i></code>.
 * </p>
 * </dd>
 * 
 * <dt><a name="site"><code>{SITE}</code></a></dt>
 * <dd>
 * <p>
 * Use this token to insert the site name of the current portal site into the
 * interpolated content. The site name is taken from a non-standard attribute in
 * the request which it is assumed the portal has set (SPF sets this by default -
 * the value set is the so-called "site DNS name"). For example,
 * <code>{SITE}</code> is replaced with <code>itrc</code> when the portlet
 * is requested from the <code>itrc</code> portal site.
 * </p>
 * </dd>
 * 
 * <dt><a name="site_c"><code>{SITE:<i>names</i>}...{/SITE}</code></a></dt>
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
 * <code>{SITE:<i>names</i>}</code> token, you can list just a single site
 * name, or multiple (use the <code>|</code> character to delimit them). The
 * content enclosed by the <code>{SITE:<i>names</i>}</code> and
 * <code>{/SITE}</code> tokens is omitted from the returned content unless the
 * site name in the request matches one of those values. The match is
 * case-insensitive.
 * </p>
 * <p>
 * This method assumes that the site name has been set by the portal into the
 * proper-namespaced attribute in the portlet request. SPF does this
 * automatically (a non-standard behavior).
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
 * This content is for all portlets using this file to show.
 * {SITE:site_A|site_B}
 * This content is only to be shown when the portlet is in site_A or site_B.
 * {SITE:site_B}
 * This content is only to be shown when the portlet is in site_B.
 * {/SITE}
 * {/SITE}
 * </pre>
 * 
 * </p>
 * <p>
 * Your <code><i>names</i></code> can "nest" any of the other
 * <i>non-parameterized</i> tokens listed here: <code>{SITE}</code>,
 * <code>{LANGUAGE-CODE}</code>, etc. Except for
 * <code>{INCLUDE:<i>key</i>}</code>, you cannot "nest" any
 * <i>parameterized</i> tokens inside your <code><i>names</i></code>.
 * </p>
 * </dd>
 * 
 * <dt><a name="site-url"><code>{SITE-URL}</code></a></dt>
 * <dt><a name="site-url_p"><code>{SITE-URL:<i>uri</i>}</code></a></dt>
 * <dd>
 * <p>
 * Use these related tokens to insert URL's for pages at the current portal site
 * into the interpolated content. The <code>{SITE-URL}</code> token inserts
 * the site home page URL; the <code>{SITE-URL:<i>uri</i>}</code> token
 * inserts a URL for a page at that site, identified by the given URI (ie a
 * friendly URI for the page in Vignette, or a secondary page template name, or
 * etc - this can include query data if you like). The portal site URL is taken
 * from a non-standard attribute in the request which it is assumed the portal
 * has set (SPF sets this by default).
 * </p>
 * <p>
 * For example, <code>{SITE-URL}</code> is replaced with
 * <code>http://portal.hp.com/portal/site/itrc/</code> when the portlet is
 * requested from the <code>itrc</code> portal site on the
 * <code>portal.hp.com</code> server using HTTP. Similarly,
 * <code>{SITE-URL:/forums}</code> is replaced with
 * <code>http://portal.hp.com/portal/site/itrc/forums</code>,
 * <code>{SITE-URL:/template.ANON_SPF_GLOBAL_HELP}</code> is replaced with
 * <code>http://portal.hp.com/portal/site/itrc/template.ANON_SPF_GLOBAL_HELP</code>
 * (the global help secondary page), etc.
 * </p>
 * <p>
 * Your <code><i>uri</i></code> can "nest" any of the other
 * <i>non-parameterized</i> tokens listed here: <code>{SITE}</code>,
 * <code>{LANGUAGE-CODE}</code>, etc. Except for
 * <code>{INCLUDE:<i>key</i>}</code>, you cannot "nest" any
 * <i>parameterized</i> tokens inside your <code><i>uri</i></code>.
 * </p>
 * </dd>
 * 
 * <dt><a name="user-property"><code>{USER-PROPERTY:<i>key</i>}</code></a></dt>
 * <dd>
 * <p>
 * Use this token to insert a given string property of the user into the
 * interpolated content. The <code><i>key</i></code> parameter in the token
 * is the name of the user property, and the user properties themselves are
 * taken from the <i>user profile map</i> created by SPF. For example,
 * <code>{USER-PROPERTY:PhoneNumber}</code> is replaced with
 * <code>123 456 7890</code> for a user with such a phone number.
 * </p>
 * <p>
 * The property name <code><i>key</i></code>'s are not listed here; you can
 * lookup their values in the {@link com.hp.it.spf.xa.misc.portlet.Consts}
 * class. (They are the values of the <code>Consts</code> class attributes
 * whose names begin with <code>KEY_*</code> - for example,
 * {@link com.hp.it.spf.xa.misc.portlet.Consts#KEY_EMAIL}. Note you must use
 * the value - you cannot use the name of one of those <code>KEY_*</code>
 * attributes in your <code><i>key</i></code> for the
 * <code>{USER-PROPERTY:<i>key</i>}</code> token.
 * </p>
 * <p>
 * If the property is not found in the user object, or is not a string, then an
 * empty string will be inserted. Similarly, if the user object itself is guest
 * or null (ie the user is not logged-in), then an empty string is inserted.
 * </p>
 * <p>
 * Your <code><i>key</i></code> can "nest" any of the other
 * <i>non-parameterized</i> tokens listed here: <code>{SITE}</code>,
 * <code>{LANGUAGE-CODE}</code>, etc. Except for
 * <code>{INCLUDE:<i>key</i>}</code>, you cannot "nest" any
 * <i>parameterized</i> tokens inside your <code><i>key</i></code>.
 * </p>
 * </dd>
 * </dl>
 * </p>
 * 
 * <p>
 * The above tokens are parsed in the following order:
 * </p>
 * 
 * <p>
 * <ol>
 * <li><code>{INCLUDE:<i>key</i>}</code></li>
 * <li><code>{LOGGED-IN}</code></li>
 * <li><code>{LOGGED-OUT}</code></li>
 * <li><code>{LANGUAGE-CODE}</code></li>
 * <li><code>{COUNTRY-CODE}</code></li>
 * <li><code>{LANGUAGE-TAG}</code></li>
 * <li><code>{REQUEST-URL}</code></li>
 * <li><code>{EMAIL}</code></li>
 * <li><code>{NAME}</code></li>
 * <li><code>{SITE}</code></li>
 * <li><code>{SITE-URL}</code></li>
 * <li><code>{SITE:<i>names</i>}</code></li>
 * <li><code>{GROUP:<i>groups</i>}</code></li>
 * <li><code>{USER-PROPERTY:<i>key</i>}</code></li>
 * <li><code>{CONTENT-URL:<i>path</i>}</code></li>
 * <li><code>{LOCALIZED-CONTENT-URL:<i>path</i>}</code></li>
 * <li><code>{SITE-URL:<i>uri</i>}</code></li>
 * <li><code>{PORTLET:<i>portlets</i>}</code></li>
 * <li><code>{ROLE:<i>roles</i>}</code></li>
 * </ol>
 * </p>
 * 
 * @author <link href="jyu@hp.com">Yu Jie</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.interpolate.FileInterpolator</code><br>
 *      <code>com.hp.it.spf.xa.interpolate.portlet.TokenParser</code><br>
 *      <code>com.hp.it.spf.xa.interpolate.TokenParser</code>
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
	 * constructor. Therefore any <code>{INCLUDE:<i>key</i>}</code> tokens
	 * in the file content will be resolved against the default
	 * token-substitutions property file (<code>default_includes.properties</code>).
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
			this.parser = new TokenParser(pRequest, pResponse);
		}
	}

	/**
	 * <p>
	 * Constructs a new <code>FileInterpolator</code> for the given base
	 * content pathname, request, response, and token-substitutions pathname.
	 * </p>
	 * <p>
	 * This constructor works like
	 * {@link #FileInterpolator(PortletRequest, PortletResponse, String)} and
	 * allows a token-substitutions file to be specified as well. The
	 * token-substitutions pathname provided is to be used with any
	 * <code>{INCLUDE:<i>key</i>}</code> tokens in the file content; they
	 * will be resolved against the file whose pathname you provide. The
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
	 *            purposes of any <code>{INCLUDE:key}</code> tokens in the
	 *            file content)
	 */
	public FileInterpolator(PortletRequest pRequest, PortletResponse pResponse,
			String pBaseContentFilePath, String subsFilePath) {
		this.request = pRequest;
		this.response = pResponse;
		this.baseContentFilePath = pBaseContentFilePath;
		if (pRequest != null && pResponse != null) {
			this.parser = new TokenParser(pRequest, pResponse, subsFilePath);
		}
	}

	/**
	 * <p>
	 * Constructs a new <code>FileInterpolator</code> for the given base
	 * content pathname, request, response, token-substitutions pathname, and
	 * locale.
	 * </p>
	 * <p>
	 * This constructor works like
	 * {@link #FileInterpolator(PortletRequest, PortletResponse, String, String)}
	 * except that the given locale is used instead of the one in the request.
	 * However if the given locale is null, then the one in the request will be
	 * used.
	 * </p>
	 * 
	 * @param pRequest
	 *            The portlet request
	 * @param pResponse
	 *            The portlet response
	 * @param Locale
	 *            The locale to use (if null, uses the one in the request)
	 * @param pBaseContentFilePath
	 *            The base filename and path relative to where the class loader
	 *            searches for the file content to interpolate
	 * @param subsFilePath
	 *            The filename and path relative to where the class loader
	 *            searches for the token-substitutions property file (for
	 *            purposes of any <code>{INCLUDE:key}</code> tokens in the
	 *            file content)
	 */
	public FileInterpolator(PortletRequest pRequest, PortletResponse pResponse,
			Locale pLocale, String pBaseContentFilePath, String subsFilePath) {
		this.request = pRequest;
		this.response = pResponse;
		this.baseContentFilePath = pBaseContentFilePath;
		this.locale = pLocale;
		if (pRequest != null && pResponse != null) {
			this.parser = new TokenParser(pRequest, pResponse, pLocale,
					subsFilePath);
		}
	}

	/**
	 * <p>
	 * Gets the best-fit localized version of the content file (using
	 * {@link #getLocalizedContentFileAsStream()}, reads it into a string, and
	 * substitutes the tokens found in the string with the proper dynamic values
	 * (using {@link TokenParser#parse(String)}, returning the interpolated
	 * content. The content filename and location, the locale for which to find
	 * the best-candidate, and the proper substitution values for the tokens are
	 * all based on the information you provided when calling the constructor.
	 * Null is returned (and a warning is logged) if there are problems
	 * interpolating (eg the file is not found or was empty, or the request or
	 * content file you provided when calling the constructor were null). See
	 * class documentation for more information about the tokens which are
	 * substituted.
	 * </p>
	 * 
	 * @return The interpolated file content
	 * @throws Exception
	 *             Some exception
	 */
	public String interpolate() throws Exception {

		if ((request == null) || (response == null)) {
			logWarning("Portlet request or response was null.");
			return null;
		}
		// open and read the file and interpolate the common portal/portlet
		// tokens, all in the superclass interpolate method
		String content = super.interpolate();
		TokenParser portletParser = (TokenParser) this.parser;

		// parse the portlet-specific tokens last - we call them out
		// here, rather than use the portlet token parser's parse method,
		// because that would repeat (harmless but wasteful) the common parsing
		// just finished
		//
		// parse the portlet token
		content = portletParser.parsePortletContainer(content);

		// parse the role token
		content = portletParser.parseRoleContainer(content);

		return content;
	}

	/**
	 * Get an input stream for the best-candidate localized content file
	 * available from the <i>portlet resource bundle directory</i> or inside
	 * the portlet application, based on the locale and base content pathname
	 * provided to the constructor. If no specific locale was provided to the
	 * constructor, then the one in the request is used. Null is returned if the
	 * file is not found or the request or content file provided to the
	 * constructor were null. This method uses
	 * {@link com.hp.it.spf.xa.i18n.portlet.I18nUtility#getLocalizedFileStream(PortletRequest, String, Locale, boolean)}
	 * to select and open the file (see).
	 * 
	 * @return The input stream for the file
	 */
	protected InputStream getLocalizedContentFileAsStream() {
		if (request == null || baseContentFilePath == null) {
			return null;
		}
		return I18nUtility.getLocalizedFileStream(request, baseContentFilePath,
				locale, true);
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