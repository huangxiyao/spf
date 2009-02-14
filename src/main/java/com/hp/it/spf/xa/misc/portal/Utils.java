/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.xa.misc.portal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.epicentric.site.Site;
import com.epicentric.site.SiteException;
import com.epicentric.site.SiteManager;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.util.StringUtils;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * A container class for miscellaneous utility methods for Vignette portal
 * components.
 * 
 * @author <link href="ye.liu@hp.com">Liu Ye</link>
 * @version TBD
 */
public class Utils extends com.hp.it.spf.xa.misc.Utils {

	/**
	 * The name of the property file which stores federation configuration for
	 * the portal framework. (The file extension .properties is assumed by the
	 * PropertyResourceBundleManager.)
	 */
	private static final String SITE_FEDERATION = "site_federation";

	/**
	 * Part of the is-federation-enabled property key, in
	 * site_federation.properties.
	 */
	private static final String FED_PROP_SUFFIX = ".federated";

	/**
	 * The is-federation-enabled property value, in site_federatino.properties.
	 */
	private static final String VALUE_YES = "yes";

	/**
	 * Logger instance.
	 */
	private static final LogWrapper LOG = new LogWrapper(I18nUtility.class);

	/**
	 * Get the value of the given request header from the given request. This
	 * method returns no decoded, trimmed string; the value is returned raw
	 * (compare with the companion
	 * <code>getRequestHeader(HttpServletRequest,String,boolean)</code>
	 * method). If the header does not exist, or the given parameters are null,
	 * then null is returned.
	 * 
	 * @param request
	 *            Current request
	 * @param in
	 *            Header name
	 * @return non-decoded field value of that request header
	 * @see getRequestHeader(javax.servlet.http.HttpServletRequest,
	 *      java.lang.String, boolean)
	 */
	public static String getRequestHeader(HttpServletRequest request, String in) {
		return getRequestHeader(request, in, false);
	}

	/**
	 * Get the value of the given request header from the given request, and
	 * return it optionally decoded and trimmed based on the given boolean
	 * switch. When the boolean is false, the value is returned raw (same as the
	 * companion (<code>getRequestHeader(HttpServletRequest,String)</code>
	 * method). When the boolean is true, the value is assumed to be a padded
	 * and base-64-encoded string (as used by SiteMinder to pass header
	 * information and user attributes); the returned value is trimmed of the
	 * SiteMinder padding, and base-64-decoded. If the header does not exist, or
	 * the given parameters are null, then null is returned.
	 * 
	 * @param request
	 *            Current request
	 * @param in
	 *            Header name
	 * @param needDecode
	 *            Trim and decode the result or not?
	 * @return Value in the request header, trimmed/decoded as per the
	 *         parameters
	 */
	public static String getRequestHeader(HttpServletRequest request,
			String in, boolean needDecode) {
		if (request == null) {
			return null;
		}

		String headerInfo = null;
		if ((in != null) && !"".equals(in)) {
			in = in.trim();
			headerInfo = request.getHeader(in);
			if ((headerInfo != null) && !"".equals(headerInfo) && needDecode) {
				headerInfo = decodeBase64(trimUnwantedSmPadding(headerInfo));
			}
		}
		return headerInfo;
	}

	/**
	 * Parses the given decoded <code>CL_Header</code> string, returning the
	 * value of the given property name. It is assumed that the provided string
	 * is already base-64-decoded and the method will not parse properly if it
	 * is not. If the particular property does not exist, or the given
	 * parameters are null, then null is returned.
	 * 
	 * @param cl_header
	 *            The trimmed and decoded <code>CL_Header</code> string.
	 * @param in
	 *            The property to retrieve from the <code>CL_Header</code>
	 *            information.
	 * @return The property value.
	 */
	public static String getValueFromCLHeader(String cl_header, String in) {

		if (in == null) {
			return null;
		}
		String result = null;
		in = in.trim() + "=";
		if ((cl_header != null) && !cl_header.equals("")) {
			String temp;
			int i = cl_header.indexOf(in);
			if (i >= 0) {
				temp = "";
				if (i + in.length() < cl_header.length()) {
					temp = cl_header.substring(i + in.length());
				}
				i = temp.indexOf('|');
				if (i >= 0) {
					result = temp.substring(0, i);
				} else {
					result = temp;
				}
			}
		}
		return result;
	}

	/**
	 * <p>
	 * Returns true if the portal site indicated by the given site name is
	 * federated, otherwise false. This method uses the
	 * <code>site_federation.properties</code> file. If the site is listed in
	 * that file as being a federated site, then true is returned. In any other
	 * circumstance, false is returned.
	 * </p>
	 * <p>
	 * The PropertyResourceBundleManager is used to load that file from anywhere
	 * in the classpath, using a hot-reloadable cache (so changes to the file
	 * take effect immediately without restart). The desired site is indicated
	 * by the site name (what Vignette calls "site DNS name").
	 * </p>
	 * 
	 * @param siteName
	 *            The site name to check.
	 * @return true if federated, otherwise false.
	 */
	public static boolean isFederatedSite(String siteDNS) {

		// return false if the parameter is malformed.
		if (siteDNS == null || siteDNS.length() == 0)
			return false;
		else
			siteDNS = siteDNS.trim();

		// get resource bundle object
		ResourceBundle rb = PropertyResourceBundleManager
				.getBundle(Utils.SITE_FEDERATION);
		String key = siteDNS + Utils.FED_PROP_SUFFIX;

		if (rb == null) {
			LOG.error("I18nUtility: isFederatedSite failed to open "
					+ Utils.SITE_FEDERATION + " properties file");
			return false;
		} else {
			// if the property exists and equals to yes, then return true
			try {
				return Utils.VALUE_YES
						.equalsIgnoreCase(org.apache.commons.lang.StringUtils
								.trim(rb.getString(key)));
			} catch (MissingResourceException e) {
				return false;
			}
		}
	}

	/**
	 * <p>
	 * Returns true if the current portal site indicated by the given request is
	 * federated, otherwise false. This method uses the companion
	 * <code>isFederatedSite(String)</code> method (see). The site name is
	 * taken from the URL in the given request, as per the Vignette portal
	 * standard for keeping "site DNS name" in the URL path.
	 * </p>
	 * 
	 * @param request
	 *            The current portal request.
	 * @return true if federated, otherwise false.
	 */
	public static boolean isFederatedSite(HttpServletRequest request) {

		String pathInfo = request.getPathInfo();
		String servletPath = request.getServletPath();

		if (servletPath.equals("/site")) {
			return isFederatedSite(getEffectiveSiteDNS(request));
		} else {
			return false;
		}

	}

	/**
	 * <p>
	 * Get the site name from the given request, according to the Vignette
	 * portal standard for "site DNS name". This method always returns the "site
	 * DNS name" for the URL in the given request; this may not be the same as
	 * the effective site, however (see
	 * {@link #getEffectiveSiteDNS(HttpServletRequest)}).
	 * </p>
	 * <p>
	 * The "site DNS name" in a Vignette URL is the second element in the
	 * portal-root-relative path (ie the first element in the additional-path or
	 * path info). For example, in the URL
	 * <code>http://host.hp.com/portal/site/abc/template.SOMETHING</code>,
	 * the portal-root-relative path is
	 * <code>/site/abc/template.SOMETHING</code>, and so the site name is
	 * <code>site</code>.
	 * <p>
	 * <p>
	 * This method returns null if the site cannot be determined from the given
	 * request. <b>Note:</b> This method does not use Vignette's
	 * <code>SessionInfo</code> in case it is null (that is the case before
	 * the Vignette session initialization module runs, and sometimes this code
	 * may need to run before then). It just parses the URL.
	 * </p>
	 * 
	 * @param request
	 *            The current HTTP servlet request.
	 * @return The actual site name (ie "site DNS name").
	 */
	public static String getSiteDNS(HttpServletRequest request) {

		if (request == null) {
			return null;
		}
		String pathInfo = request.getPathInfo();
		String servletPath = request.getServletPath();
		String siteDNS = null;

		if (servletPath.equals("/site")) {
			if (StringUtils.isEmpty(pathInfo))
				return siteDNS;
			if (pathInfo.startsWith("/"))
				pathInfo = pathInfo.substring(1);
			int index = pathInfo.indexOf("/");
			if (index == -1)
				siteDNS = pathInfo;
			else
				siteDNS = pathInfo.substring(0, index);
			siteDNS = siteDNS.trim();
		}
		return siteDNS;
	}

	/**
	 * <p>
	 * Get the <i>effective</i> site name from the given request, according to
	 * the Vignette portal and SPF standards.
	 * </p>
	 * <ul>
	 * <li> Generally, this method returns the Vignette "site DNS name", using
	 * {@link #getSiteDNS(HttpServletRequest)}.</li>
	 * <li> However, this method also takes into account the SPF standard for
	 * overriding the "site DNS name". In SPF, any URL for the "core" SPF site
	 * {@link Consts#CORE_SPF_SITE} may contain the effective Vignette "site DNS
	 * name" in a query parameter named {@link Consts#PARAM_EFFECTIVE_SITE}. If
	 * the current request is for the core site, and it contains an
	 * effective-site parameter, then that is the site name returned from this
	 * method.</li>
	 * </ul>
	 * <p>
	 * This method returns null if the site cannot be determined from the given
	 * request. <b>Note:</b> This method does not use Vignette's
	 * <code>SessionInfo</code> in case it is null (that is the case before
	 * the Vignette session initialization module runs, and sometimes this code
	 * may need to run before then). It just parses the URL.
	 * </p>
	 * 
	 * @param request
	 *            The current HTTP servlet request.
	 * @return The site name (ie "site DNS name").
	 */
	public static String getEffectiveSiteDNS(HttpServletRequest request) {

		if (request == null)
			return null;
		String siteDNS = getSiteDNS(request);

		// check if current request site is the core site - if so, look for
		// effective site and return it instead - note in Vignette, site DNS is
		// case-sensitive
		if (Consts.SPF_CORE_SITE.equals(siteDNS)) {
			String alternateSiteDNS = request
					.getParameter(Consts.PARAM_EFFECTIVE_SITE);
			if (alternateSiteDNS != null) {
				siteDNS = alternateSiteDNS;
			}
			siteDNS = siteDNS.trim();
		}
		return (siteDNS);
	}

	/**
	 * <p>
	 * Get the effective Vignette <code>Site</code> object for the request,
	 * using {@link #getEffectiveSiteDNS(HttpServletRequest)} to determine the
	 * proper site. Normally this is just the <code>Site</code> corresponding
	 * to the site name (ie "site DNS name", as Vignette calls it) in the
	 * current request. But if the current site is the core SPF site whose name
	 * is {@link Consts#CORE_SPF_SITE}, and an effective-site parameter (named
	 * {@link Consts#PARAM_EFFECTIVE_SITE} exists on the request, that is the
	 * effective site and its <code>Site</code> is returned.
	 * </p>
	 * <p>
	 * This method returns null if the site cannot be determined from the given
	 * request. <b>Note:</b> This method does not use Vignette's
	 * <code>SessionInfo</code> in case it is null (that is the case before
	 * the Vignette session initialization module runs, and sometimes this code
	 * may need to run before then). It just parses the URL for the site, then
	 * uses Vignette's static <code>SiteManager</code> singleton to
	 * instantiate the <code>Site</code>.
	 * </p>
	 * 
	 * @param HttpServletRequest
	 *            The current portal request.
	 * @return The effective site.
	 */
	public static Site getEffectiveSite(HttpServletRequest request) {

		if (request == null)
			return null;
		String effectiveSiteDNS = getEffectiveSiteDNS(request);
		Site effectiveSite = null;
		if (effectiveSiteDNS != null) {
			try {
				SiteManager siteManager = SiteManager.getInstance();
				effectiveSite = siteManager
						.getSiteFromDNSName(effectiveSiteDNS);
			} catch (SiteException ex) { // should never happen
			}
		}
		return (effectiveSite);
	}

	/**
	 * Get the value for the given user property from the SPF <i>user profile
	 * map</i> in the given portal context. Returns null if this property has
	 * not been set in the user object, or if the user object itself is null or
	 * guest (eg, when the user is not logged-in), or the portal context or key
	 * provided were null.
	 * 
	 * @param portalContext
	 *            The portal context.
	 * @param key
	 *            The user property name.
	 * @return The user property value.
	 */
	public static Object getUserProperty(PortalContext portalContext, String key) {
		Map userProfile = getUserProfileMap(portalContext);
		if (userProfile == null) {
			return null;
		} else {
			return userProfile.get(key);
		}
	}

	/**
	 * Get the SPF <i>user profile map</i> from the given portal context. The
	 * user profile map contains all of the SPF user attributes.
	 * 
	 * @param portalContext
	 *            The portal context.
	 * @return The user profile map.
	 */
	public static Map getUserProfileMap(PortalContext portalContext) {
		if (portalContext == null) {
			return null;
		}
		try {
			HttpSession session = portalContext.getPortalRequest().getRequest()
					.getSession();
			return (Map) session.getAttribute(Consts.USER_PROFILE_KEY);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get the user groups from the given portal context. These are the
	 * authorization groups defined for the current request in the portal. The
	 * method returns null if there were no groups in the request.
	 * 
	 * @param portalContext
	 *            The portal context.
	 * @return The list of groups (null if none).
	 */
	public static String[] getGroups(PortalContext portalContext) {
		Map userProfile = getUserProfileMap(portalContext);
		String[] groups = null;
		if (userProfile != null) {
			try {
				groups = (String[]) userProfile.get(Consts.KEY_USER_GROUPS);
			} catch (ClassCastException e) { // should never happen
			}
		}
		return groups;
	}

	/**
	 * Returns true if the given group is among the user groups found in the
	 * given portal context. These groups are the authorization groups defined
	 * for the current request in the portal. The group comparison is
	 * case-insensitive.
	 * 
	 * @param portalContext
	 *            The portal context.
	 * @return True if the given group is among the user groups, otherwise
	 *         false.
	 */
	public static boolean isUserInGroup(PortalContext portalContext,
			String group) {
		return Utils.groupMatch(getGroups(portalContext), group);
	}

	/**
	 * <p>
	 * Utility method to remove unwanted SiteMinder characters passed as part of
	 * the SiteMinder header string. If the given string does not contain the
	 * recognizable SiteMinder padding, then it is returned.
	 * </p>
	 * <p>
	 * Example: SM_USER in the headers has a value like this:
	 * <code>=?UTF-8?B?dmlnbmV0dGVBZG1pbg0=?=</code> and in that case, the
	 * example trimmed output required is: <code>dmlnbmV0dGVBZG1pbg0</code>
	 * </p>
	 * 
	 * @param in
	 *            Header value to be trimmed.
	 * @return The trimmed value.
	 */
	public static String trimUnwantedSmPadding(String in) {
		String result = in;
		if ((in != null) && !in.equals("")) {
			int i = in.indexOf("?UTF-8?");
			if (i > -1) {
				result = in.substring(i + 9);
				if (result.endsWith("?=")) {
					result = result.substring(0, result.length() - 2);
				}
			}
		}
		return result;
	}

	/**
	 * Helper method for creating a new cookie.
	 * 
	 * @param name
	 *            The cookie name.
	 * @param value
	 *            The cookie value.
	 * @param domain
	 *            The cookie domain.
	 * @param path
	 *            The cookie path.
	 * @param maxAge
	 *            The cookie expiration timer (negative for session cookie).
	 * @return The new cookie.
	 */
	public static Cookie newCookie(String name, String value, String domain,
			String path, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		if (domain != null) {
			cookie.setDomain(domain);
		}
		if (path != null) {
			cookie.setPath(path);
		}
		cookie.setMaxAge(maxAge);
		return cookie;
	}

	/**
	 * Returns an absolute URL for the given request. This includes the scheme,
	 * hostname and port used by the browser, the path (including context root
	 * path, context relative path, and any additional path) and the form
	 * parameters (attached as a query string regardless of whether they were
	 * submitted in a query string via GET, or in the request body via POST).
	 * This method returns null given a null request.
	 * 
	 * @param request
	 *            The current request.
	 * @return The URL in string form.
	 */
	public static String getRequestURL(HttpServletRequest request) {
		if (request == null) {
			return null;
		}
		String url = "";
		Enumeration params;
		String[] values;
		String scheme, hostAndPort, host, context, path, info, query, name, value;
		int p, i;

		// We could probably use request.getRequestURL for most of the below,
		// but to ensure we get the URL in the form we want, we will piece it
		// together using the more granular API's instead.
		// First get the scheme used by the browser: http or https.
		scheme = request.getScheme();
		if (scheme == null)
			scheme = "http";
		scheme = scheme.toLowerCase();

		// Next get the host and port used by the browser. This comes from the
		// Host header which should always be present (otherwise it is assumed
		// to be the current server name and port). The Host header should
		// contain the hostname plus any non-standard port; use the non-standard
		// port if found, otherwise use the default port. Override port with the
		// parameter if positive.
		hostAndPort = request.getHeader("Host");
		if (hostAndPort == null) {
			host = request.getServerName();
			p = request.getServerPort();
		} else {
			if ((i = hostAndPort.indexOf(":")) != -1) {
				host = hostAndPort.substring(0, i);
				if (i < (hostAndPort.length() - 1)) {
					try {
						p = Integer.parseInt(hostAndPort.substring(i + 1));
					} catch (NumberFormatException e) {
						p = 0;
					}
				} else {
					p = 0;
				}
			} else {
				host = hostAndPort;
				p = 0;
			}
		}
		host = host.toLowerCase();
		if (scheme.equals("http") && (p == 80))
			p = 0;
		if (scheme.equals("https") && (p == 443))
			p = 0;
		hostAndPort = host;
		if (p > 0)
			hostAndPort += ":" + p;

		// Next get the root path.
		context = request.getContextPath();
		if (context == null)
			context = "";

		// Next get the path.
		path = request.getServletPath();
		if (path == null)
			path = "";

		// Next get any additional path info.
		info = request.getPathInfo();
		if (info == null)
			info = "";

		// Lastly get the query string. In the case of a GET, this basically
		// just builds a duplicate query string. In the case of a POST, any
		// POST'ed request parameters are copied into the query string too, so
		// that the returned URL stands a better chance of accurately reflecting
		// the whole original request.
		query = "";
		params = request.getParameterNames();
		if (params != null) {
			while (params.hasMoreElements()) {
				name = (String) params.nextElement();
				values = request.getParameterValues(name);
				if (values != null) {
					for (i = 0; i < values.length; i++) {
						value = values[i];
						if (!"".equals(query))
							query += "&";
						try {
							query += URLEncoder.encode(name, "UTF-8");
							query += "=";
							query += URLEncoder.encode(value, "UTF-8");
						} catch (UnsupportedEncodingException e) {
							// should never happen
						}
					}
				}
			}
		}
		if (!"".equals(query))
			query = "?" + query;

		// Now put the redirect URL together.
		url = scheme + "://" + hostAndPort + context + path + info + query;
		return url;
	}

	/**
	 * <p>
	 * Returns an absolute URL for the given request. This method obtains the
	 * current request URL like the {@link #getRequestURL(HttpServletRequest)}
	 * method does, and modifies its scheme, hostname, and/or port as follows:
	 * </p>
	 * <ul>
	 * <li>if the given security scheme is <code>true</code>, then the
	 * scheme in the returned URL is forced to <code>https://</code>; if it
	 * is <code>false</code>, it is forced to <code>http://</code>;
	 * otherwise the current request scheme is retained</li>
	 * <li>if the given hostname is not blank or null, then the hostname in the
	 * returned URL is set to it</li>
	 * <li>if the given port is a positive number, then the port in the
	 * returned URL is set to it</li>
	 * </ul>
	 * 
	 * @param request
	 *            The current request.
	 * @param secure
	 *            If true, force use of <code>https</code>; if false, force
	 *            use of <code>http</code>. If null, use the current scheme.
	 * @param port
	 *            The port to use (an integer; if non-positive, use the current
	 *            port).
	 * @param host
	 *            The hostname to use (if null, use the hostname already in the
	 *            URL).
	 * @return The URL in string form.
	 */
	public static String getRequestURL(HttpServletRequest request,
			Boolean secure, String host, int port) {
		// TODO: This method should use the PortalURL API's instead.
		return getPortalSiteURL(getRequestURL(request), secure, host, port,
				null);
	}

	/**
	 * Returns an absolute URL for the the current portal site root (ie portal
	 * site home page) for the given request. This includes the scheme, hostname
	 * and port used by the browser. This method returns null given a null
	 * request.
	 * 
	 * @param request
	 *            The current request.
	 * @return The URL for the current site, in string form. This is an absolute
	 *         URL.
	 */
	public static String getPortalSiteURL(HttpServletRequest request) {
		return getPortalSiteURL(request, null, null, -1, null);
	}

	/**
	 * Use {@link #getPortalSiteURL(HttpServletRequest)} instead.
	 * 
	 * @deprecated
	 */
	public static String getSiteURL(HttpServletRequest request) {
		return getPortalSiteURL(request);
	}

	/**
	 * Returns an absolute URL for the the portal site root (ie portal site home
	 * page) for the <i>effective</i> site in the given request. This includes
	 * the scheme, hostname and port used by the browser. The effective site is
	 * determined by using the {@link #getEffectiveSite(HttpServletRequest)}
	 * method. This method returns null given a null request.
	 * 
	 * @param request
	 *            The current request.
	 * @return The URL for the current site, in string form. This is an absolute
	 *         URL.
	 */
	public static String getEffectiveSiteURL(HttpServletRequest request) {
		return getEffectiveSiteURL(request, null, null, -1, null);
	}

	/**
	 * Returns an absolute URL for a page at the <i>effective</i> portal site,
	 * based on the given request and site URI. This method is the same as the
	 * {@link #getPortalSiteURL(HttpServletRequest, Boolean, String, int, String)}
	 * method - except that if the given URI does not indicate a specific portal
	 * site, this method defaults to using the <i>effective</i> site (from the
	 * {@link #getEffectiveSiteDNS(HttpServletRequest)} method) instead of the
	 * current site.
	 * 
	 * @param request
	 *            The current request.
	 * @param uri
	 *            The site name (ie "site DNS name") and/or additional path (eg
	 *            a friendly URI or template friendly ID). (The part before the
	 *            first <code>/</code> is considered the site name.)
	 * @param secure
	 *            If true, force use of <code>https</code>; if false, force
	 *            use of <code>http</code>. If null, use the current scheme.
	 * @param port
	 *            The port to use (an integer; if non-positive, use the current
	 *            port).
	 * @param host
	 *            The hostname to use (if null, use the hostname already in the
	 *            URL).
	 * @return The URL for the current site, in string form. This is an absolute
	 *         URL.
	 */
	public static String getEffectiveSiteURL(HttpServletRequest request,
			Boolean secure, String host, int port, String uri) {
		String effectiveSiteDNS = getEffectiveSiteDNS(request);
		if (effectiveSiteDNS != null) {
			if (uri == null) {
				uri = effectiveSiteDNS;
			} else if (uri.startsWith("/")) {
				uri = effectiveSiteDNS + uri;
			}
		}
		return getPortalSiteURL(request, secure, host, port, uri);
	}

	/**
	 * <p>
	 * Returns an absolute URL for a page at a portal site, based on the given
	 * request, hostname, security scheme, port number, and site-relative URI.
	 * The elements of the URL are populated as follows:
	 * </p>
	 * <ul>
	 * <li>
	 * <p>
	 * The scheme to use can be indicated by the optional boolean parameter. If
	 * it is true, then <code>https://</code> is used; if it is false, then
	 * <code>http://</code> is used; and if it is null, then the same scheme
	 * is used as was used by the browser in the current request.
	 * </p>
	 * </li>
	 * <li>
	 * <p>
	 * The hostname to use can be indicated by the string parameter. If it is
	 * blank or null, then the same hostname is used as was used by the browser
	 * in the current request.
	 * </p>
	 * </li>
	 * <li>
	 * <p>
	 * The port number to use can be indicated by the integer parameter. If it
	 * is a non-positive integer, then the same port is used as was used by the
	 * browser in the current request.
	 * </p>
	 * </li>
	 * <li>
	 * <p>
	 * The portal site name, and any additional path (eg a friendly URI, a
	 * template friendly ID, a query string, etc) are taken from the given
	 * site-relative URI string, as follows:
	 * </p>
	 * <ul>
	 * <li> if the given URI starts with <code>/</code> then the returned URL
	 * is for the current portal site (ie the one in the request), and the given
	 * URI is used as additional path for it</li>
	 * <li> otherwise the first part of the given URI (up to the first
	 * <code>/</code>) is used as the site name (ie the Vignette "site DNS
	 * name") in the returned URL, and the remainder of the given URI is used as
	 * additional path for it</li>
	 * </ul>
	 * <p>
	 * If the given URI string is blank or null, then a site root URL is
	 * returned.
	 * </p>
	 * </li>
	 * </ul>
	 * <p>
	 * For example, say that the current portal request is for the
	 * <code>abc</code> site at <code>http://host.hp.com</code>. Then:
	 * </p>
	 * <ul>
	 * <li> when the given URI is null, the given hostname is null, the given
	 * scheme is null, and the given port is 0, the returned URL is for the
	 * current portal site home page, using the current scheme and port:
	 * <code>http://host.hp.com/portal/site/abc/</code></li>
	 * <li> when the given URI is <code>/template.ABC</code> instead, the
	 * returned URL is for that page at the current portal site (again using the
	 * current scheme and port):
	 * <code>http://host.hp.com/portal/site/abc/template.ABC</code></li>
	 * <li> when the given URI is <code>xyz</code> instead, the returned URL
	 * is for the <code>xyz</code> portal site home page (again with current
	 * scheme and port): <code>http://host.hp.com/portal/site/xyz</code></li>
	 * <li> when the given URI is <code>xyz/template.ABC</code> instead, the
	 * returned URL is for that page at the <code>xyz</code> portal site
	 * (again with current scheme and port):
	 * <code>http://host.hp.com/portal/site/xyz/template.ABC</code></li>
	 * <li> when the given URI is null, the given hostname is null, the given
	 * security scheme is true, and the given port is 8002, the returned URL is
	 * for the current portal site home, using <code>https</code> at port
	 * 8002: <code>https://host.hp.com:8002/portal/site/abc/</code></li>
	 * <li>when the given URI is <code>/template.ABC</code>, the given
	 * security scheme is true, the given hostname is
	 * <code>another.hp.com</code>, and the given port is 0, the returned URL
	 * is for that page at the current portal site, on the other host, using
	 * <code>https</code> at the default port (443):
	 * <code>https://another.hp.com/portal/site/abc/</code></li>
	 * </ul>
	 * <p>
	 * This method returns null given a null request.
	 * </p>
	 * <p>
	 * <b>Note:</b> This method does not check if the given URI actually exists /
	 * is valid in the portal; it just makes a URL of the proper format for it.
	 * </p>
	 * 
	 * @param request
	 *            The current request.
	 * @param host
	 *            The hostname to use (if null, use the hostname already in the
	 *            URL).
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
	 * @return The URL for the given site, in string form.
	 */
	public static String getPortalSiteURL(HttpServletRequest request,
			Boolean secure, String host, int port, String uri) {
		// TODO: This method should use the PortalURL API's instead.
		String siteURL = getPortalSiteURL(getRequestURL(request), secure, host,
				port, uri);
		// The parent method leaves the request URL path on there when the URI
		// was null/blank. So we must remove it now.
		if ((uri == null) || (uri.trim().length() == 0)) {
			if (siteURL != null) {
				int j = siteURL.indexOf("/site/");
				if ((j != -1) && ((j + 6) < siteURL.length())) {
					int i = siteURL.indexOf('/', j + 6);
					if (i != -1) {
						siteURL = siteURL.substring(0, i + 1);
					}
				}
			}
		}
		return (siteURL);
	}

	/**
	 * Use
	 * {@link #getPortalSiteURL(HttpServletRequest, Boolean, String, int, String)}
	 * instead.
	 * 
	 * @deprecated
	 */
	public static String getSiteURL(HttpServletRequest request, Boolean secure,
			String host, int port, String uri) {
		return getPortalSiteURL(request, secure, host, port, uri);
	}
}
