package com.hp.spp.wsrp.url;

import org.apache.log4j.Logger;

/**
 * Factory which determine what is the class used to construct the url.
 * 
 * @author mathieu.vidal@hp.com
 */
public class PortalURLFactory {

	private static Logger mLogger = Logger.getLogger(PortalURLFactory.class);
	private static boolean mIsInVignette;

	static {
		try {
			String classVignette = "com.vignette.portal.portlet.jsrcontainer.PortletCommandServlet";
			Class.forName(classVignette);
			mIsInVignette = true;
			if (mLogger.isInfoEnabled()) {
				mLogger.info("VignetteLocalURL will be used as PortalURL implementation");
			}
		} catch (ClassNotFoundException e) {
			mIsInVignette = false;
			if (mLogger.isInfoEnabled()) {
				mLogger.info("VignetteWSRPURL will be used as PortalURL implementation");
			}
		}
	}

	/**
	 * Check if some class is in the classloader to differientiate the local and
	 * remote context. Be Careful, this class could need change depending of the
	 * evolutions of Vignette or WSRP4J.
	 * 
	 * @param site -
	 *            the identifier of the site
	 * @param pageId -
	 *            the identifier of the page
	 * @return the class used to construct the url
	 *
	 * @deprecated This method requires Vignette internal UIDs to create portal URLs. This approach
	 * will not be suppored in the future. Use other methods in this class instead.
	 */
	public static PortalURL createURL(String site, String pageId) {
		if (mIsInVignette) {
			return new VignetteLocalURL(new PageIdBaseUrlComposer(site, pageId));
		}
		else {
			return new VignetteWSRPURL(new PageIdBaseUrlComposer(site, pageId));
		}
	}

	/**
	 * Creates new portal URL for the same site as in <tt>homePageUrl</tt> and page name. The protocol
	 * of the URL (http, https) will be the same as in homePageUrl parameter.
	 * @param homePageUrl current site home page URL
	 * @param pageName navigation item name in the current site
	 * @return portal URL to the same site and the specified page
	 *
	 * @see #createPageURL(String, String, String, boolean)
	 */
	public static PortalURL createPageURL(String homePageUrl, String pageName) {
		return createPageURL(homePageUrl, pageName, homePageUrl.toLowerCase().startsWith("https:"));
	}

	/**
	 * Creates new portal URL for the specified <tt>site</tt> and page name. The protocol of the result
	 * URL will be the same as in <tt>homePageUrl</tt>
	 * @param homePageUrl current site home page URL
	 * @param site another site we create link to
	 * @param pageName navigation item name in the target site
	 * @return portal URL to the specified site and page
	 *
	 * @see #createPageURL(String, String, String, boolean)
	 */
	public static PortalURL createPageURL(String homePageUrl, String site, String pageName) {
		return createPageURL(homePageUrl, pageName, site, homePageUrl.toLowerCase().startsWith("https:"));
	}

	/**
	 * Creates new portal URL for the same site as in <tt>homePageUrl</tt> and page name. The result
	 * URL will have "https" protocol if <tt>secure</tt> flag is true and <tt>homePageUrl</tt> is a
	 * complete url (protocol, host, path).
	 * @param homePageUrl current site home page URL
	 * @param pageName navigation item name in the current site
	 * @param secure flag indicating whether the result URL must be https or http; Note that this flag
	 * is ignored if the given <tt>homePageUrl</tt> contains only path but no protocol and host
	 * @return portal URL to the same site and the specified page
	 *
	 * @see #createPageURL(String, String, String, boolean)
	 */
	public static PortalURL createPageURL(String homePageUrl, String pageName, boolean secure) {
		return createPageURL(homePageUrl, null, pageName, secure);
	}

	/**
	 * Creates new portal URL to the specified site and the specified page. The result URL will be
	 * "https" is <tt>secure</tt> flag is true and <tt>homePageUrl</tt> contains protocol and host
	 * (i.e. is not server relative URL).<p>
	 * <tt>homePageUrl</tt> is available for portlets in user profile map as "HomePageUrl" entry. It
	 * contains a complete URL to the site user is currently logged into.
	 * @param homePageUrl current site home page URL
	 * @param site another site in the portal; if <tt>null</tt> the result URL will be created for
	 * the current site
	 * @param pageName navigation item name of the current site (site parameter of this method is null)
	 * or another site
	 * @param secure flag indicating whether the result url should be http or https; ignored if
	 * <tt>homePageUrl</tt> is not a complete URL (protocol, host, path)
	 * @return new portal URL to the specified site and page.
	 */
	public static PortalURL createPageURL(String homePageUrl, String site, String pageName, boolean secure) {
		if (mIsInVignette) {
			return new VignetteLocalURL(new FriendlyPageNameBaseUrlComposer(homePageUrl, site, pageName, secure));
		}
		else {
			return new VignetteWSRPURL(new FriendlyPageNameBaseUrlComposer(homePageUrl, site, pageName, secure));
		}
	}
}
