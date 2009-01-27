package com.hp.it.spf.xa.portalurl;

//import org.apache.log4j.Logger;

/**
 * Factory used to create {@link PortalURL} objects.
 * Depending on whether the portlet runs as local or remote portlet it creates the appropriate
 * <code>PortalURL</code> implementation. Note that it is not possible to create in a local portlet
 * a URL to a remote portlet and vice versa.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class PortalURLFactory {

//	private static Logger mLog = Logger.getLogger(PortalURLFactory.class);
//	private static boolean mIsLocalPortlet;
//
//	static {
//		try {
//			String classVignette = "com.vignette.portal.portlet.jsrcontainer.PortletCommandServlet";
//			Class.forName(classVignette);
//			mIsLocalPortlet = true;
//		} catch (ClassNotFoundException e) {
//			mIsLocalPortlet = false;
//		}
//		if (mLog.isInfoEnabled()) {
//			mLog.info("Factory will create the PortalURL for the following type of portlet: " +
//				(mIsLocalPortlet ? "local" : "remote"));
//		}
//	}

	/**
	 * Private constructor to prevent creation of this factory instances knowing that all the methods
	 * are private.
	 */
	private PortalURLFactory() {}


	public static PortalURL createPageURL(String siteRootUrl, String anotherSite, String pageFriendlyUri, boolean secure) {
		//FIXME (slawek) - implement the remote URLs and URL type selection logic
		return new VignetteLocalPortalURL(siteRootUrl, anotherSite, pageFriendlyUri, secure);
	}

	public static PortalURL createPageURL(String siteRootUrl, String pageFriendlyUri, boolean secure) {
		return createPageURL(siteRootUrl, null, pageFriendlyUri, secure);
	}

	public static PortalURL createPageURL(String siteRootUrl, String anotherSite, String pageFriendlyUri) {
		return createPageURL(siteRootUrl, anotherSite, pageFriendlyUri, siteRootUrl.toLowerCase().startsWith("https:"));
	}

	public static PortalURL createPageURL(String siteRootUrl, String pageFriendlyUri) {
		return createPageURL(siteRootUrl, null, pageFriendlyUri, siteRootUrl.toLowerCase().startsWith("https:"));
	}


//	/**
//	 * Creates portal URLs that embed Vignette menu item IDs.
//	 *
//	 * @param site the identifier of the site
//	 * @param pageId the identifier of the page (i.e. menu item ID)
//	 * @return portal URL implementation
//	 *
//	 * @deprecated This method requires Vignette internal UIDs (menu item ids)to create portal URLs. This approach
//	 * will not be suppored in the future. Use other methods in this class instead.
//	 */
//	public static PortalURL createURL(String site, String pageId) {
//		if (mIsLocalPortlet) {
//			return new VignetteLocalURL(new PageIdBaseUrlComposer(site, pageId));
//		}
//		else {
//			return new VignetteWSRPURL(new PageIdBaseUrlComposer(site, pageId));
//		}
//	}
//
//	/**
//	 * Creates new portal URL for the same site as in <tt>homePageUrl</tt> and page name. The protocol
//	 * of the URL (http, https) will be the same as in homePageUrl parameter.
//	 * @param homePageUrl current site home page URL
//	 * @param pageName navigation item name in the current site
//	 * @return portal URL to the same site and the specified page
//	 *
//	 * @see #createPageURL(String, String, String, boolean)
//	 * @deprecated As of SPF 1.0 - see {@link #createPortalURL(String, String)}
//	 */
//	public static PortalURL createPageURL(String homePageUrl, String pageName) {
//		return createPageURL(homePageUrl, pageName, homePageUrl.toLowerCase().startsWith("https:"));
//	}
//
//	/**
//	 * Creates new portal URL for the specified <tt>site</tt> and page name. The protocol of the result
//	 * URL will be the same as in <tt>homePageUrl</tt>
//	 * @param homePageUrl current site home page URL
//	 * @param site another site we create link to
//	 * @param pageName navigation item name in the target site
//	 * @return portal URL to the specified site and page
//	 *
//	 * @see #createPageURL(String, String, String, boolean)
//	 * @deprecated As of SPF 1.0 - see {@link #createPortalURL(String, String, String)}
//	 */
//	public static PortalURL createPageURL(String homePageUrl, String site, String pageName) {
//		return createPageURL(homePageUrl, pageName, site, homePageUrl.toLowerCase().startsWith("https:"));
//	}
//
//	/**
//	 * Creates new portal URL for the same site as in <tt>homePageUrl</tt> and page name. The result
//	 * URL will have "https" protocol if <tt>secure</tt> flag is true and <tt>homePageUrl</tt> is a
//	 * complete url (protocol, host, path).
//	 * @param homePageUrl current site home page URL
//	 * @param pageName navigation item name in the current site
//	 * @param secure flag indicating whether the result URL must be https or http; Note that this flag
//	 * is ignored if the given <tt>homePageUrl</tt> contains only path but no protocol and host
//	 * @return portal URL to the same site and the specified page
//	 *
//	 * @see #createPageURL(String, String, String, boolean)
//	 * @deprecated As of SPF 1.0 - see {@link #createPortalURL(String, String, boolean)}
//	 */
//	public static PortalURL createPageURL(String homePageUrl, String pageName, boolean secure) {
//		return createPageURL(homePageUrl, null, pageName, secure);
//	}
//
//	/**
//	 * Creates new portal URL to the specified site and the specified page. The result URL will be
//	 * "https" is <tt>secure</tt> flag is true and <tt>homePageUrl</tt> contains protocol and host
//	 * (i.e. is not server relative URL).<p>
//	 * <tt>homePageUrl</tt> is available for portlets in user profile map as "HomePageUrl" entry. It
//	 * contains a complete URL to the site user is currently logged into.
//	 * @param homePageUrl current site home page URL
//	 * @param site another site in the portal; if <tt>null</tt> the result URL will be created for
//	 * the current site
//	 * @param pageName navigation item name of the current site (site parameter of this method is null)
//	 * or another site
//	 * @param secure flag indicating whether the result url should be http or https; ignored if
//	 * <tt>homePageUrl</tt> is not a complete URL (protocol, host, path)
//	 * @return new portal URL to the specified site and page.
//	 *
//	 * @deprecated As of SPF 1.0 - see {@link #createPortalURL(String, String, String, boolean)}
//	 */
//	public static PortalURL createPageURL(String homePageUrl, String site, String pageName, boolean secure) {
//		if (mIsLocalPortlet) {
//			return new VignetteLocalURL(new FriendlyPageNameBaseUrlComposer(homePageUrl, site, pageName, secure));
//		}
//		else {
//			return new VignetteWSRPURL(new FriendlyPageNameBaseUrlComposer(homePageUrl, site, pageName, secure));
//		}
//	}
}