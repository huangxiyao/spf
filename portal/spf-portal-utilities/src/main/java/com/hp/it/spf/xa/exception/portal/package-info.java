/**
 * <p>
 * This package contains exception classes and utilities for SPF Vignette portal
 * components to use.
 * </p>
 * 
 * <a name="ExceptionUtil"> <h3>ExceptionUtil</h3> </a>
 * <p>
 * The {@link com.hp.it.spf.xa.exception.portal.ExceptionUtil} class is a
 * utility class for performing exception-handling operations that are common to
 * SPF portal components. At present there are only 2 similar methods provided
 * by this class:
 * {@link com.hp.it.spf.xa.exception.portal.ExceptionUtil#redirectSystemErrorPage(com.vignette.portal.website.enduser.PortalContext, String)}
 * and
 * {@link com.hp.it.spf.xa.exception.portal.ExceptionUtil#redirectSystemErrorPage(com.vignette.portal.website.enduser.PortalContext, String, String, String)}
 * for redirecting the user from your portal component into the SPF-provided
 * general-purpose <i>error secondary page</i> at your portal site. This
 * secondary page (named <b>Shared Portal Framework - System Error Page</b>,
 * component ID <code>spf-system-error-secondarypage</code>) is an instance of
 * the Vignette-standard error secondary page type (named <b>Error Page</b>). It
 * presents a general service-unavailable error message to the user, customized
 * with the arguments you pass into these methods.
 * </p>
 * <p>
 * The error message by default consists of some boilerplate error title and
 * text content, to which an optional error code you supply (such as
 * <code>myComponent-error-1234</code>) is affixed. As raw English text, it
 * looks like this:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * Could not open page
 * 
 * The service or information you requested is not available at this time. Please try again later.
 * (Error: &lt;error-code&gt;)
 * </pre>
 * 
 * </blockquote>
 * <p>
 * This is then surrounded with your grid and theme. The error-code is free-form
 * and can be whatever you like (if you do not pass one to the method, then a
 * default one is supplied: <code>portappl-internal-error</code>). The text is
 * localized for the user's current locale automatically.
 * </p>
 * <p>
 * You can optionally override the default title (
 * <code>Could not open page</code>) or text (
 * <code>The service or information...try again later.</code>) strings by
 * passing substitution strings as arguments to the method. You must pass the
 * already-localized strings, as the system error secondary page will not look
 * them up in your message resource bundle for you.
 * </p>
 * <p>
 * To work properly:
 * </p>
 * <ol>
 * <li>The SPF system error secondary page must have been configured as the
 * default secondary page to use for the Vignette error secondary page type, in
 * the Vignette server console.</li>
 * <li>In addition, the secondary page must have been shared to your portal site
 * by the Vignette server administrator.</li>
 * <li>And lastly, in Vignette site console for your site, you should have again
 * configured the SPF system error secondary page as the instance to use for the
 * Vignette error secondary page type. Also you should have applied your site's
 * grid and theme to it. (You do these steps in <b>Site Settings &gt; Appearance
 * &gt; Secondary Pages</b>.)</li>
 * </ol>
 * <p>
 * For example, here is what the SPF system error secondary page looks like when
 * rendered from the HP Support Center portal site (in English) using the
 * default error code, title and message text. (Your portal site will use its
 * own grid and theme, and thus the header and footer will look different.)
 * </p>
 * <img src="doc-files/spfSystemErrorSecondaryPage.jpg">
 * <p>
 * And here is what the code looked like to redirect to this page from the
 * action class of an HP Support Center custom secondary page:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * import com.hp.it.spf.xa.exception.portal;
 * ...
 * if (some_system_error_has_occurred) {
 *    return ExceptionUtil.redirectSystemErrorPage(portalContext, null);
 * }
 * </pre>
 * 
 * </blockquote>
 */
package com.hp.it.spf.xa.exception.portal;