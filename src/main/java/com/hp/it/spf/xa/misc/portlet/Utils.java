/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.xa.misc.portlet;

import java.util.List;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequest;

import org.springframework.context.ApplicationContext;
import org.springframework.web.portlet.context.PortletApplicationContextUtils;

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
		if (request != null && key != null) {
			// See the property key name constants, defined in the common Consts
			// class.
			Object o = request.getAttribute(PortletRequest.USER_INFO);
			if (o != null) {
				try {
					Map userMap = (Map) o;
					return (String) userMap.get(key.trim());
				} catch (ClassCastException e) {
				}
			}
		}
		return null;
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
		if (request != null) {
			Object o = request.getAttribute(PortletRequest.USER_INFO);
			if (o != null) {
				try {
					Map userMap = (Map) o;
					String username = (String) userMap
							.get(Consts.KEY_USER_NAME);
					if (username != null
							&& !username
									.startsWith(Consts.ANON_USER_NAME_PREFIX)) {
						return true;
					}
				} catch (ClassCastException e) {
				}
			}
		}
		return false;
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
		String[] groups = null;
		if (request != null) {
			Object o = request.getAttribute(PortletRequest.USER_INFO);
			if (o != null) {
				try {
					Map userMap = (Map) o;
					Object groupList = userMap.get(Consts.KEY_USER_GROUPS);
					if (groupList instanceof List) {
						return (String[]) ((List) groupList)
								.toArray(new String[0]);
					}
				} catch (ClassCastException e) {
				}
			}
		}
		return groups;
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
				} catch (ClassCastException e) {
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
		return getPortalSiteURL(request, null);
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
	 * Returns a URL for a page at a portal site, based on the current portal
	 * site URL in the given portlet request, and the given URI. The SPF
	 * framework propagates the current portal site URL from the portal to the
	 * portlet, where it is made accessible in the request. So this method just
	 * gets that URL from the given request. Then it returns an adjusted portal
	 * site URL based on that current portal site URL and the given URI. As
	 * follows:
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
	 * For example, say that the current portal request is for the
	 * <code>abc</code> site at <code>http://host.hp.com</code>. Then:
	 * </p>
	 * <ul>
	 * <li> when the given URI is null, the returned URL is for the current
	 * portal site home page: <code>http://host.hp.com/portal/site/abc/</code></li>
	 * <li> when the given URI is <code>/template.ABC</code>, the returned
	 * URL is for that page at the current portal site:
	 * <code>http://host.hp.com/portal/site/abc/template.ABC</code></li>
	 * <li> when the given URI is <code>xyz</code>, the returned URL is for
	 * the <code>xyz</code> portal site home page:
	 * <code>http://host.hp.com/portal/site/xyz</code></li>
	 * <li> when the given URI is <code>xyz/template.ABC</code>, the returned
	 * URL is for that page at the the <code>xyz</code> portal site:
	 * <code>http://host.hp.com/portal/site/xyz/template.ABC</li>
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
	 * @param uri
	 *            The site name (ie "site DNS name") and/or additional path (eg a friendly URI or template friendly ID).
	 *            (The part before the first <code>/</code> is considered the site name.)
	 * @return The URL for the given site, in string form. This is an absolute
	 *         URL.
	 */
	public static String getPortalSiteURL(PortletRequest request, String uri) {
		String siteRootURL = null;
		if (request != null) {
			Object o = request.getAttribute(Consts.PORTAL_CONTEXT_KEY);
			if (o != null) {
				try {
					Map contextMap = (Map) o;
					siteRootURL = (String) contextMap
							.get(Consts.KEY_PORTAL_SITE_URL);
				} catch (ClassCastException e) { // should never happen
				}
			}
		}
		return getPortalSiteURL(siteRootURL, uri);
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
				} catch (ClassCastException e) { // should never happen
				}
			}
		}
		return requestURL;
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
	 * @return
	 */
	public static String getPortalPortletID(PortletRequest request) {
		String portletId = null;
		if (request != null) {
			// TODO: Finish this logic. Get portlet ID (currently, the Vignette
			// portlet friendly ID) from the request using the new
			// authentication module. If not found in request, return null. A
			// key name for this is defined in the common Consts class - see
			// TODO there.
		}
		return portletId;
	}

	/**
	 * Use {@link #getPortalPortletID(PortletRequest)} instead.
	 * 
	 * @deprecated
	 */
	public static String getPortletID(PortletRequest request) {
		return getPortalPortletID(request);
	}
}
