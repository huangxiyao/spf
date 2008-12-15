/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.xa.misc.portal;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.epicentric.common.website.SessionInfo;
import com.epicentric.common.website.SessionUtils;
import com.epicentric.site.Site;
import com.epicentric.site.SiteException;
import com.epicentric.site.SiteManager;
import com.epicentric.user.User;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.util.StringUtils;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * A container class for miscellaneous utility methods for Vignette portal
 * components. *
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
			return isFederatedSite(getSiteDNS(pathInfo));
		} else {
			return false;
		}

	}

	/**
	 * Get the site name from the given portal path, according to the Vignette
	 * portal standard for "site DNS name". The portal path is the path relative
	 * to the portal root path - for example, in the URL
	 * <code>http://host.hp.com/portal/site/abc/template.SOMETHING</code>,
	 * the portal path is <code>/site/abc/template.SOMETHING</code>. This
	 * method returns the given path if the site name cannot be parsed from the
	 * given path.
	 * 
	 * @param pathInfo
	 *            The path to parse.
	 * @return The site name (ie "site DNS name").
	 */
	public static String getSiteDNS(String pathInfo) {

		if (StringUtils.isEmpty(pathInfo))
			return pathInfo;
		String siteDNS = null;
		if (pathInfo.startsWith("/"))
			pathInfo = pathInfo.substring(1);
		int index = pathInfo.indexOf("/");
		if (index == -1)
			siteDNS = pathInfo;
		else
			siteDNS = pathInfo.substring(0, index);
		return siteDNS;
	}

	/**
	 * Get the effective Site object for the request. Normally this is just the
	 * Site corresponding to the site name (ie "site DNS name", as Vignette
	 * calls it) in the current request, as determined by Vignette. But if the
	 * current site is the "sp" site, and a "site" parameter exists on the
	 * request (eg this is the case with the logout URL), the current site is
	 * defined to be the one whose DNS name is in that "site" parameter.
	 * 
	 * @param HttpServletRequest
	 *            The current portal request.
	 * @return The effective site.
	 */
	public static Site getEffectiveSite(HttpServletRequest request) {

		Site effectiveSite = null;
		SiteManager siteManager;
		SessionInfo sessionInfo = (SessionInfo) request.getSession()
				.getAttribute(SessionInfo.SESSION_INFO_NAME);
		if (sessionInfo != null) {
			effectiveSite = sessionInfo.getSite();
			if (effectiveSite != null) {
				if (Consts.LOGOUT_DEFAULT_SITE.equals(effectiveSite
						.getDNSName())) {
					String alternateSiteName = request
							.getParameter(Consts.LOGOUT_SITE_PARAM);
					if (alternateSiteName != null) {
						try {
							siteManager = SiteManager.getInstance();
							effectiveSite = siteManager
									.getSiteFromDNSName(alternateSiteName);
						} catch (SiteException ex) {
						}
					}
				}
			}
		}
		return effectiveSite;
	}

	/**
	 * Get the value for the given user property from the portal User object in
	 * the given portal context. Returns null if this property has not been set
	 * in the user object, or if the user object itself is null or guest (eg,
	 * when the user is not logged-in), or the portal context or key provided
	 * were null.
	 * 
	 * @param portalContext
	 *            The portal context.
	 * @param key
	 *            The user property name.
	 * @return The user property value.
	 */
	public static Object getUserProperty(PortalContext portalContext, String key) {
		if (portalContext == null || key == null) {
			return null;
		}
		HttpSession session = portalContext.getPortalRequest().getRequest()
				.getSession();
		User currentUser = SessionUtils.getCurrentUser(session);
		if (currentUser != null && !currentUser.isGuestUser()) {
			return currentUser.getProperty(key.trim());
		} else {
			return null;
		}
	}

	/**
	 * <p>
	 * Utility method to remove unwanted SiteMinder characters passed as part of
	 * the SiteMinder header string. If the given string does not contain the recognizable SiteMinder
	 * padding, then it is returned.
	 * </p>
	 * <p>
	 * Example: SM_USER in the headers has a value
	 * like this: <code>=?UTF-8?B?dmlnbmV0dGVBZG1pbg0=?=</code> and in that
	 * case, the example trimmed output required is:
	 * <code>dmlnbmV0dGVBZG1pbg0</code>
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
	 * @return
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

}
