package com.hp.it.spf.xa.portalurl;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

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

	static final String PROPERTY_FILE_RESOURCE_PATH = "/portalurl.properties";
	static final String PROPERTY_NAME = "PortalURLFactory.remote";
	
//	private static Logger mLog = Logger.getLogger(PortalURLFactory.class);
	private static boolean mCreatesRemoteUrls = createsRemoteUrls(PROPERTY_FILE_RESOURCE_PATH);

	/**
	 * Private constructor to prevent creation of this factory instances knowing that all the methods
	 * are private.
	 */
	private PortalURLFactory() {}

	/**
	 * Creates new portal URL to the specified <tt>anotherSite</tt> and the specified <tt>pageFriendlyUri</tt> .
	 * The result URL will be "https" is <tt>secure</tt> flag is true and <tt>siteRootUrl</tt> contains protocol
	 * and host (i.e. is not server relative URL).<p>
	 * <tt>siteRootUrl</tt> is available for portlets in user profile map as "HomePageUrl" entry. It
	 * contains a complete URL to the site user is currently logged into.
	 * @param siteRootUrl current site home page URL
	 * @param anotherSite another site in the portal; if <tt>null</tt> the result URL will be created for
	 * the current site
	 * @param pageFriendlyUri navigation item friendly URI as defined in portal console for the current site
	 * (anotherSite parameter of this method is null) or another site (anotherSite parameter is not null)
	 * @param secure flag indicating whether the result url should be http or https; ignored if
	 * <tt>siteRootUrl</tt> is not a complete URL (protocol, host, path)
	 * @return new portal URL to the specified site and page.
	 */
	public static PortalURL createPageURL(String siteRootUrl, String anotherSite, String pageFriendlyUri, boolean secure) {
		if (mCreatesRemoteUrls) {
			return new VignetteRemotePortalURL(siteRootUrl, anotherSite, pageFriendlyUri, secure);
		}
		else {
			return new VignetteLocalPortalURL(siteRootUrl, anotherSite, pageFriendlyUri, secure);
		}
	}

	/**
	 * Creates new portal URL for the same site as in <tt>siteRootUrl</tt> and the specified
	 * <tt>pageFriendlyUri</tt>. The result URL will have "https" protocol if <tt>secure</tt> flag
	 * is true and <tt>homePageUrl</tt> is a complete url (protocol, host, path).
	 * @param siteRootUrl current site home page URL
	 * @param pageFriendlyUri navigation item friendly URI as defined in portal console for the current site
	 * (anotherSite parameter of this method is null) or another site (anotherSite parameter is not null)
	 * @param secure flag indicating whether the result url should be http or https; ignored if
	 * <tt>siteRootUrl</tt> is not a complete URL (protocol, host, path)
	 * @return portal URL to the same site and the specified page
	 *
	 * @see #createPageURL(String, String, String, boolean)
	 */
	public static PortalURL createPageURL(String siteRootUrl, String pageFriendlyUri, boolean secure) {
		return createPageURL(siteRootUrl, null, pageFriendlyUri, secure);
	}

	/**
	 * Creates new portal URL for the specified <tt>anotherSite</tt> and the <tt>pageFriendlyUri</tt>.
	 * The protocol of the result URL will be the same as in <tt>siteRootUrl</tt>
	 * @param siteRootUrl current site home page URL
	 * @param anotherSite another site in the portal; if <tt>null</tt> the result URL will be created for
	 * the current site
	 * @param pageFriendlyUri navigation item friendly URI as defined in portal console for the current site
	 * (anotherSite parameter of this method is null) or another site (anotherSite parameter is not null)
	 * @return portal URL to the specified site and page
	 *
	 * @see #createPageURL(String, String, String, boolean)
	 */
	public static PortalURL createPageURL(String siteRootUrl, String anotherSite, String pageFriendlyUri) {
		return createPageURL(siteRootUrl, anotherSite, pageFriendlyUri, siteRootUrl.toLowerCase().startsWith("https:"));
	}

	/**
	 * Creates new portal URL for the same site as in <tt>siteRootUrl</tt> and the <tt>pageFriendlyUri</tt>.
	 * The protocol of the URL (http, https) will be the same as in siteRootUrl parameter.
	 * @param siteRootUrl current site home page URL
	 * @param pageFriendlyUri navigation item friendly URI as defined in portal console for the current site
	 * (anotherSite parameter of this method is null) or another site (anotherSite parameter is not null)
	 * @return portal URL to the same site and the specified page
	 *
	 * @see #createPageURL(String, String, String, boolean)
	 */
	public static PortalURL createPageURL(String siteRootUrl, String pageFriendlyUri) {
		return createPageURL(siteRootUrl, null, pageFriendlyUri, siteRootUrl.toLowerCase().startsWith("https:"));
	}

	/**
	 * Returns <tt>true</tt> if this factory create URLs for remote portlets. This method is called
	 * only once during class initialization.
	 * <p>
	 * It checks first whether a system property <tt>PortalURLFactory.remote</tt> exists
	 * and if yes, it uses its boolean value. Then a property file <tt>portalurl.properties</tt> is
	 * checked and if yes the boolean value of <tt>PortalURLFactory.remote</tt> property is used.
	 * If none of the above is defined, by default it creates remote portlet URLs.
	 *
	 * @param propertyFileResourcePath resource path of the optional property file
	 * @return <tt>true</tt> if this class creates remote portlet URLs;
	 */
	static boolean createsRemoteUrls(String propertyFileResourcePath) {
		// first check system property
		if (System.getProperty(PROPERTY_NAME) != null) {
			return Boolean.valueOf(System.getProperty(PROPERTY_NAME));
		}

		// then check property file
		InputStream is = PortalURLFactory.class.getResourceAsStream(propertyFileResourcePath);
		try {
			if (is != null) {
				try {
					Properties props = new Properties();
					props.load(is);
					return Boolean.valueOf(props.getProperty(PROPERTY_NAME));
				}
				finally {
					is.close();
				}
			}
		}
		catch (IOException e) {
			throw new RuntimeException("Error loading property file: " + propertyFileResourcePath, e);
		}

		// finally return default value
		return true;
	}
}