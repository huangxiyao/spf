/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.xa.misc.portlet;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.portlet.context.PortletApplicationContextUtils;

import com.hp.it.spf.xa.misc.portlet.Consts;

/**
 * <p>
 * A container class for miscellaneous utility methods for portlets. Many of the
 * methods in this class let you access data passed from the SPF portal,
 * including:
 * </p>
 * <ul>
 * <li>user data (eg see {@link #getUserProperty(PortletRequest, String)} and
 * {@link #isAuthenticatedUser(PortletRequest)}</li>
 * <li>portal request data (eg see {@link #getPortalRequestURL(PortletRequest)}
 * and {@link #getPortalSiteURL(PortletRequest)}</li>
 * </ul>
 * <p>
 * Others are general-purpose utility Web programming utilities, many inherited
 * from {@link com.hp.it.spf.xa.misc.Utils} (see) in the SPF common utilities.
 * </p>
 * 
 * @author <link href="wei.teng@hp.com">Teng Wei</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.misc.Utils</code>
 */

public class Utils extends com.hp.it.spf.xa.misc.Utils {

	/**
	 * Returns the Spring {@link org.springframework.context.ApplicationContext}
	 * object from the given portlet request. Returns null if a null request was
	 * provided.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The application context.
	 */
	public static ApplicationContext getApplicationContext(
			PortletRequest portletRequest) {
		if (portletRequest == null) {
			return null;
		}
		PortletContext pc = portletRequest.getPortletSession()
				.getPortletContext();
		return PortletApplicationContextUtils.getWebApplicationContext(pc);
	}

	/**
	 * <p>
	 * Get the current value for the given user property from the SPF <i>user
	 * profile map</i> in the given portlet request. The map is obtained from
	 * the Java-standard attribute named
	 * {@link javax.portlet.PortletRequest#USER_INFO}. The method returns null
	 * if this property has not been set in the user profile map, or if the user
	 * profile map itself is null (which should never happen), or the portlet
	 * request or key provided were null.
	 * </p>
	 * <p>
	 * If the user is not logged-in, this method returns the value from the map
	 * for the given property for an anonymous (ie guest) user. In many cases,
	 * that means null or blank will be returned, but not necessarily - some
	 * default attributes may be returned. Which is the case for any given
	 * attribute, is not documented here. If it matters, you should first check
	 * whether the user is anonymous or authenticated, using the
	 * {@link #isAuthenticatedUser(PortletRequest)} method.
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param key
	 *            The user property name. See the property key name constants
	 *            (ie class attributes starting with <code>KEY_</code>) in
	 *            {@link com.hp.it.spf.xa.misc.portlet.Consts}, such as
	 *            {@link com.hp.it.spf.xa.misc.portlet.Consts#KEY_EMAIL}.
	 * @return The user property value.
	 */
	public static Object getUserProperty(PortletRequest request, String key) {
		try {
			Map userMap = getUserProfileMap(request);
			return userMap.get(key.trim());
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get the SPF <i>user profile map</i> from the given portlet request. The
	 * user profile map contains all of the SPF user attributes.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The user profile map.
	 */
	public static Map getUserProfileMap(PortletRequest request) {
		try {
			return (Map) request.getAttribute(PortletRequest.USER_INFO);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Return true if the given portlet request indicates the user is logged-in
	 * (ie authenticated), or false if it indicates the user is not logged-in
	 * (ie anonymous). This is based on the SPF <i>user profile map</i> present
	 * in the Java-standard attribute named
	 * {@link javax.portlet.PortletRequest#USER_INFO}. When that information
	 * indicates an authenticated user, this method returns true, but when it
	 * indicates a guest or null user, it returns false.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return True if the user is authenticated, false otherwise.
	 */
	public static boolean isAuthenticatedUser(PortletRequest request) {
		try {
			String username = (String) getUserProperty(request,
					Consts.KEY_USER_NAME);
			if (username != null
					&& !username.startsWith(Consts.ANON_USER_NAME_PREFIX)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Get the current user groups from the given portlet request. These are the
	 * authorization groups defined for the current request in the portal (thus
	 * they are portal elements, not part of the portlet application itself).
	 * The SPF framework propagates the user groups from the portal to the
	 * portlet where they are made accessible in the SPF <i>user profile map</i>
	 * in the request, so this method just returns them from the request. The
	 * method returns null if there were no groups in the request.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The list of groups (null if none).
	 */
	public static String[] getGroups(PortletRequest request) {
		try {
			Object groupList = getUserProperty(request, Consts.KEY_USER_GROUPS);
			if (groupList instanceof List) {
				return (String[]) ((List) groupList).toArray(new String[0]);
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns true if the given group is among the user groups found in the
	 * given portlet request. These groups are the ones returned by
	 * {@link #getGroups(PortletRequest)}. The group comparison is
	 * case-insensitive.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return True if the given group is among the user groups, otherwise
	 *         false.
	 */
	public static boolean isUserInGroup(PortletRequest request, String group) {
		return Utils.groupMatch(getGroups(request), group);
	}

	/**
	 * Get the current portal site name from the given portlet request. The
	 * portal site name is the unique identifier for the current portal site
	 * (thus it is not an element from the portlet application itself; it is an
	 * element from the portal). For Vignette Portal, this is the "site DNS
	 * name". The SPF framework propagates the portal site name from the portal
	 * to the portlet, where it is made accessible in the request. So this
	 * method just returns the site name from the given request. The method
	 * returns null if no site name was recorded in the request.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The portal site name.
	 */
	public static String getPortalSiteName(PortletRequest request) {
		String siteName = null;
		if (request != null) {
			Object o = request.getAttribute(Consts.PORTAL_CONTEXT_KEY);
			if (o != null) {
				try {
					Map contextMap = (Map) o;
					return (String) contextMap.get(Consts.KEY_PORTAL_SITE_NAME);
				} catch (Exception e) {
				}
			}
		}
		return siteName;
	}

	/**
	 * Use {@link #getPortalSiteName(PortletRequest)} instead.
	 * 
	 * @deprecated
	 */
	public static String getSiteName(PortletRequest request) {
		return getPortalSiteName(request);
	}

	/**
	 * Get the current portal site URL (home page URL) from the given portlet
	 * request. The SPF framework propagates the portal site URL from the portal
	 * to the portlet, where it is made accessible in the request. So this
	 * method just returns that URL from the given request. The method returns
	 * null if no such URL was recorded in the request.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The portal site home page URL.
	 */
	public static String getPortalSiteURL(PortletRequest request) {
		String siteRootURL = null;
		if (request != null) {
			Object o = request.getAttribute(Consts.PORTAL_CONTEXT_KEY);
			if (o != null) {
				try {
					Map contextMap = (Map) o;
					siteRootURL = (String) contextMap
							.get(Consts.KEY_PORTAL_SITE_URL);
				} catch (Exception e) { // should never happen
				}
			}
		}
		return (siteRootURL);
	}

	/**
	 * Use {@link #getPortalSiteURL(PortletRequest)} instead.
	 * 
	 * @deprecated
	 */
	public static String getSiteURL(PortletRequest request) {
		return getPortalSiteURL(request);
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
	 * This method returns null given a null request. When this method needs to
	 * default to data from the current request, it uses the portal request URL
	 * which SPF propagates from the portal to portlet (see
	 * {@link #getPortalRequestURL(PortletRequest, Boolean, String, int)}).
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
	 * @return The URL for the given site, in string form. This is an absolute
	 *         URL.
	 */
	public static String getPortalSiteURL(PortletRequest request,
			Boolean secure, String host, int port, String uri) {
		// TODO: This method should use the PortalURL API's instead.
		String siteURL = getPortalSiteURL(getPortalSiteURL(request), secure,
				host, port, uri);
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
	 * <p>
	 * Returns an absolute URL for a page at a portal site, based on the given
	 * request and site-relative URI. This method is equivalent to using the
	 * companion
	 * {@link #getPortalSiteURL(PortletRequest, Boolean, String, int, String)}
	 * method and passing
	 * <code>getPortalSiteURL(request, null, null, -1, uri)</code>.
	 * </p>
	 * 
	 * @param request
	 *            The current request.
	 * @param uri
	 *            The site name (ie "site DNS name") and/or additional path (eg
	 *            a friendly URI or template friendly ID). (The part before the
	 *            first <code>/</code> is considered the site name.)
	 * @return The URL for the given site, in string form. This is an absolute
	 *         URL.
	 */
	public static String getPortalSiteURL(PortletRequest request, String uri) {
		return getPortalSiteURL(request, null, null, -1, uri);
	}

	/**
	 * Use {@link #getPortalSiteURL(PortletRequest,Boolean,String,int,String)}
	 * instead.
	 * 
	 * @deprecated
	 */
	public static String getSiteURL(PortletRequest request, Boolean secure,
			String host, int port, String uri) {
		return getPortalSiteURL(request, secure, host, port, uri);
	}

	/**
	 * Use {@link #getPortalSiteURL(PortletRequest,String)} instead.
	 * 
	 * @deprecated
	 */
	public static String getSiteURL(PortletRequest request, String uri) {
		return getPortalSiteURL(request, uri);
	}

	/**
	 * Get the current portal request URL from the given portlet request. The
	 * SPF framework propagates the complete request URL from the portal to the
	 * portlet, where it is made accessible in the request. So this method just
	 * returns that URL from the given request. The method returns null if no
	 * such URL was recorded in the request.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The complete portal request URL.
	 */
	public static String getPortalRequestURL(PortletRequest request) {
		String requestURL = null;
		if (request != null) {
			Object o = request.getAttribute(Consts.PORTAL_CONTEXT_KEY);
			if (o != null) {
				try {
					Map contextMap = (Map) o;
					return (String) contextMap
							.get(Consts.KEY_PORTAL_REQUEST_URL);
				} catch (Exception e) { // should never happen
				}
			}
		}
		return requestURL;
	}

	/**
	 * <p>
	 * Get the current portal request URL from the given portlet request,
	 * modified to use the given security scheme, hostname, and/or port number.
	 * The SPF framework propagates the complete request URL from the portal to
	 * the portlet, where it is made accessible in the request. So this method
	 * just extracts that URL from the given request, and possibly modifies the
	 * scheme or port as follows:
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
	 * <p>
	 * The method returns null if no such URL was recorded in the request.
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param secure
	 *            If true, force use of <code>https</code>; if false, force
	 *            use of <code>http</code>. If null, use the current scheme.
	 * @param host
	 *            The hostname to use (if null, use the hostname already in the
	 *            URL).
	 * @param port
	 *            The port to use (an integer; if non-positive, use the current
	 *            port).
	 * @return The complete portal request URL.
	 */
	public static String getPortalRequestURL(PortletRequest request,
			Boolean secure, String host, int port) {
		// TODO: This method should use the PortalURL API's instead.
		return getPortalSiteURL(getPortalRequestURL(request), secure, host,
				port, null);
	}

	/**
	 * Use {@link #getPortalRequestURL(PortletRequest)} instead.
	 * 
	 * @deprecated
	 */
	public static String getRequestURL(PortletRequest request) {
		return getPortalRequestURL(request);
	}

	/**
	 * Use
	 * {@link #getPortalRequestURL(PortletRequest,Boolean,String,int,String)}
	 * instead.
	 * 
	 * @deprecated
	 */
	public static String getRequestURL(PortletRequest request, Boolean secure,
			String host, int port) {
		return getPortalRequestURL(request, secure, host, port);
	}

	/**
	 * Get the current portlet ID from the given portlet request. The portlet ID
	 * is the unique identifier for the portlet instance as provisioned in the
	 * portal (thus it is not an element from the portlet application itself; it
	 * is an element from the portal). For Vignette Portal, this is the "portlet
	 * friendly ID" configured by the portal administrator on the portlet
	 * instance. The SPF framework propagates the portlet ID from the from the
	 * portal to the portlet, where it is made accessible in the request. So
	 * this method just returns the portlet ID from the given request. The
	 * method returns null if no portlet ID was recorded in the request.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The portlet friendly ID for this portlet instance.
	 */
	public static String getPortalPortletID(PortletRequest request) {
		if (request != null) {
			Object o = request.getAttribute(Consts.PORTAL_CONTEXT_KEY);
			if (o != null) {
				try {
					Map contextMap = (Map) o;
					return (String) contextMap
							.get(Consts.KEY_PORTAL_PORTLET_ID);
				} catch (Exception e) { // should never happen
				}
			}
		}
		return null;
	}

	/**
	 * Get the last portal session clean date from the given portlet request.
	 * The value is in the format of System.currentTimeMillis(). 
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The last portal session clean date.
	 */
	@SuppressWarnings("unchecked")
	public static long getLastSessionCleanupDate(PortletRequest request) {
		long lastPortalSessionCleanupDate = 0;
		if (request != null) {
			Object o = request.getAttribute(Consts.PORTAL_CONTEXT_KEY);
			if (o != null) {
				try {
					Map contextMap = (Map) o;
					lastPortalSessionCleanupDate = Long.parseLong((String)contextMap
							.get(Consts.KEY_LAST_PORTAL_SESSION_CLEANUP_DATE));
		
				} catch (Exception e) { // should never happen
				}
			}
		}
		return lastPortalSessionCleanupDate;
	}

	/**
	 * Get the current page ID from the given portlet request. The page ID is
	 * the unique identifier within the portal site for the page on which this
	 * portlet instance appears (thus it is not an element from the portlet
	 * application itself; it is an element from the portal). For Vignette
	 * Portal, this is the "page friendly ID" configured by the portal
	 * administrator on the portlet instance. The SPF framework propagates the
	 * page ID from the from the portal to the portlet, where it is made
	 * accessible in the request. So this method just returns the page ID from
	 * the given request. The method returns null if no page ID was recorded in
	 * the request.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The page friendly ID for the page containing this portlet
	 *         instance.
	 */
	public static String getPortalPageID(PortletRequest request) {
		if (request != null) {
			Object o = request.getAttribute(Consts.PORTAL_CONTEXT_KEY);
			if (o != null) {
				try {
					Map contextMap = (Map) o;
					return (String) contextMap.get(Consts.KEY_PORTAL_PAGE_ID);
				} catch (Exception e) { // should never happen
				}
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Get the current navigation item ID from the given portlet request. The
	 * navigation item ID is the identifier within the portal site for the
	 * navigation item pointing to the page on which this portlet instance
	 * appears (thus it is not an element from the portlet application itself;
	 * it is an element from the portal). There could be multiple navigation
	 * items pointing to the page, but this method only operates on the current
	 * one (ie the one invoked by the user to request the page).
	 * </p>
	 * <p>
	 * For Vignette Portal, the navigation item ID returned is actually the
	 * "navigation item name" configured by the portal administrator. (Vignette
	 * does not support a friendly ID for navigation items, and the navigation
	 * item UID is unfriendly and not available to the portlet in SPF.) The SPF
	 * framework propagates the navigation item name from the portal to the
	 * portlet, where it is made accessible in the request. So this method just
	 * returns the navigation item name from the given request. The method
	 * returns null if no navigation item name was recorded in the request.
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The navigation item name for the current operative navigation
	 *         item for the page containing this portlet instance.
	 */
	public static String getPortalNavItemID(PortletRequest request) {
		if (request != null) {
			Object o = request.getAttribute(Consts.PORTAL_CONTEXT_KEY);
			if (o != null) {
				try {
					Map contextMap = (Map) o;
					return (String) contextMap
							.get(Consts.KEY_NAVIGATION_ITEM_NAME);
				} catch (Exception e) { // should never happen
				}
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Get the current navigation item URL from the given portlet request. This
	 * is the URL path for the navigation item relative to the portal site,
	 * which points to the page on which this portlet instance appears (thus it
	 * is not an element from the portlet application itself; it is an element
	 * from the portal). There could be multiple navigation items pointing to
	 * the page, but this method only operates on the current one (ie the one
	 * invoked by the user to request the page).
	 * </p>
	 * <p>
	 * For Vignette Portal, we return the "friendly URL" path used by the user
	 * to request the page. The SPF framework propagates the whole current
	 * request URL from the portal to the portlet, where it is made accessible
	 * in the request. So this method just returns the portion of the path of
	 * that URL which represents the navigation item friendly URL.
	 * </p>
	 * <p>
	 * <blockquote> <b>Note:</b> Generally, the value returned by this method
	 * will match the friendly URL path as configured in Vignette by the portal
	 * administrator for the current navigation item, since generally that is
	 * the exact friendly URL used by the user in a request. However for the
	 * default page (ie home page) of a site, Vignette allows that page to be
	 * accessed whenever the current request contains any unrecognized or blank
	 * friendly URL. So when this portlet instance is contained on the default
	 * or home page of a site, the friendly URL returned by this method will not
	 * necessarily match any friendly URL configured in Vignette.</blockquote>
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @return The navigation item friendly URL path, for the current operative
	 *         navigation item for the page containing this portlet instance.
	 */
	public static String getPortalNavItemURL(PortletRequest request) {
		String requestURL = getPortalRequestURL(request);
		String navItemURL = null;
		String remainder;
		int i, j;

		// return null if request URL is undefined or blank
		if (requestURL == null)
			return null;
		requestURL = requestURL.trim();
		if (requestURL.length() == 0)
			return null;

		// discard query string (if any) from request URL
		i = requestURL.indexOf('?');
		if (i != -1)
			requestURL = requestURL.substring(0, i);

		// find end of site URL in request URL - return null if can't be found
		if (requestURL.endsWith("/site/"))
			return null;
		if ((i = requestURL.indexOf("/site/")) == -1)
			return null;

		// find path after end of site URL in request URL - return blank if
		// can't be found
		i = requestURL.indexOf('/', i + 6);
		if ((i == -1) || (i == requestURL.length() - 1))
			return "";
		remainder = requestURL.substring(i + 1);

		// if template friendly ID exists, subtract it out - return blank if
		// nothing left
		if (remainder.startsWith("template.")) {
			i = remainder.indexOf('/');
			if ((i == -1) || (i == remainder.length() - 1))
				return "";
			remainder = remainder.substring(i + 1);
		}

		// if action.process exists, subtract it out - return blank if nothing
		// left
		if (remainder.startsWith("action.process")) {
			i = remainder.indexOf('/');
			if ((i == -1) || (i == remainder.length() - 1))
				return "";
			remainder = remainder.substring(i + 1);
		}

		// remainder is the friendly URL - remove leading and trailing slash if
		// any, and return
		navItemURL = remainder;
		return navItemURL;
	}
	
	/**
	 * Returns Diagnostic ID, containing SessionId + RequestId + PortletId.
	 * Converting PortletRequest object to HttpServletRequest object to get the attributes.
	 * @param portletRequest portlet request.
	 * @return Diagnostic ID.
	 */
	public static String getDiagnosticId(PortletRequest portletRequest)
	{
		// For local portlets the diagnostic ID should have been set by PortletDataPreDisplayAction
		// secondary page, used to share data with local portlets.
		String localPortletDiagnosticId = (String) portletRequest.getAttribute(com.hp.it.spf.xa.misc.Consts.DIAGNOSTIC_ID);
		if (localPortletDiagnosticId != null) {
			return localPortletDiagnosticId;
		}

		// If we don't have anything for local portlets, let's assume this is a remote portlet
		// and delegate to the helper method which can take the HttpServletRequest.
		// "javax.portlet.portletc.httpServletRequest" attribute is where OpenPortal binds the
		// incoming HttpServletRequest.
		HttpServletRequest request = (HttpServletRequest) portletRequest.getAttribute("javax.portlet.portletc.httpServletRequest");
		if (request != null) {
			return com.hp.it.spf.xa.misc.Utils.getDiagnosticId(request);
		}
		return null;
	}

	/**
	 * Use {@link #getPortalPortletID(PortletRequest)} instead.
	 * 
	 * @deprecated
	 */
	public static String getPortletID(PortletRequest request) {
		return getPortalPortletID(request);
	}

	/**
	 * Use {@link #getPortalPageID(PortletRequest)} instead.
	 * 
	 * @deprecated
	 */
	public static String getPageID(PortletRequest request) {
		return getPortalPageID(request);
	}

}
