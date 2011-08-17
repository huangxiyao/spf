package com.hp.spp.portal.common.helper;

import javax.servlet.http.HttpServletRequest;

/**
 * Class which contains methods useful for the determination of site and URLS.
 * <p>
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public class SiteURLHelper {

	/**
	 * Determine the master site name from the path info extracted from the
	 * incoming request. We simply remove "public" at the beginning of the site
	 * name. For instance, for publicsppportal, we returne sppportal But for
	 * spportal, we returne sppportal
	 * <p>
	 * 
	 * @param pathInfo
	 *            the path info of the request
	 * @return the site name
	 */
	public static String determineMasterSite(HttpServletRequest request) {
		String site = null;
		// Access to console
		String servletPath = request.getServletPath();
		if (servletPath.startsWith("/console"))
			site = "console";

		// Access to other sites
		if (site == null)
			site = SiteURLHelper.determineSite(request.getPathInfo());

		// Remove public if needed
		site = site.replaceFirst("public", "");
		return site;
	}

	/**
	 * Determine the site name from the path info extracted from the incoming
	 * request.
	 * <p>
	 * 
	 * @param pathInfo
	 *            the path info of the request
	 * @return the site name
	 */
	public static String determineSite(String pathInfo) {
		if (pathInfo == null)
			return null;

		if (pathInfo.charAt(0) == '/')
			pathInfo = pathInfo.substring(1);
		int index = pathInfo.indexOf('/');

		String site = null;
		if (index == -1)
			site = pathInfo;
		else
			site = pathInfo.substring(0, index);

		return site;
	}

	/**
	 * Determine the url of the home page from the incoming request.
	 * <p>
	 * 
	 * @param request
	 *            the incoming request
	 * @return the url of the landing page
	 */
	public static String getURLHomePage(HttpServletRequest request) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int port = request.getServerPort();
		String sport = ":" + String.valueOf(port);
		if ("http".equalsIgnoreCase(scheme) && port == 80)
			sport = "";
		if ("https".equalsIgnoreCase(scheme) && port == 443)
			sport = "";
		String site = determineMasterSite(request);
		String urlHomePage = null;
		if ("console".equals(site))
			urlHomePage = scheme + "://" + serverName + sport + "/portal/"
					+ site + "/";
		else
			urlHomePage = scheme + "://" + serverName + sport + "/portal/site/"
					+ site + "/";
		return urlHomePage;
	}

	/**
	 * Determine an url from the site name, path of the page and home page of
	 * the site.
	 * <p>
	 * 
	 * @param site
	 *            the name of the site
	 * @param pathMenuPage
	 *            the path and menu id for the page (for instance
	 *            sppportal/menuitem.123)
	 * @param urlHomePage
	 *            the url of the home page (for instance
	 *            http://sppdev.gre.hp.com/portal/site/sppportal/)
	 * @return the final url
	 */
	public static String getURLPage(String site, String pathMenuPage,
			String urlHomePage) {
		String finalURL = null;
		finalURL = urlHomePage.replaceFirst(site, pathMenuPage);
		return finalURL;
	}

	/**
	 * Determine the url of the current site, public or private.
	 * 
	 * @param request
	 *            the request of the user
	 * @return the url of the current site, public or private
	 */
	public static String getUrlCurrentSite(HttpServletRequest request) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int port = request.getServerPort();
		String sport = ":" + String.valueOf(port);
		if ("http".equalsIgnoreCase(scheme) && port == 80)
			sport = "";
		if ("https".equalsIgnoreCase(scheme) && port == 443)
			sport = "";
		String contextPath = request.getContextPath();
		String servletPath = request.getServletPath();
		String site = determineSite(request.getPathInfo());

		return scheme + "://" + serverName + sport + contextPath + servletPath
				+ "/" + site + "/";
	}
}
