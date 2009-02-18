package com.hp.it.spf.xa.portalurl;

import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;

import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;

//import org.apache.log4j.Logger;

/**
 * Factory used to create {@link PortalURL} objects. Depending on whether the
 * portlet runs as local or remote portlet (as configured in
 * <code>portalurl.properties</code>, it creates the appropriate
 * <code>PortalURL</code> implementation. Note that it is not possible to
 * create in a local portlet a URL to a remote portlet and vice versa.
 * 
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class PortalURLFactory {

	static final String PROPERTY_FILE_RESOURCE_PATH = "portalurl.properties";
	static final String REMOTE_PROPERTY_NAME = "portalurl.portlet.remote";
	static final String HTTP_PORT_PROPERTY_NAME = "portalurl.port.http";
	static final String HTTPS_PORT_PROPERTY_NAME = "portalurl.port.https";

	// private static Logger mLog = Logger.getLogger(PortalURLFactory.class);

	// Since we now use PropertyResourceBundleManager for no restarts, we can no
	// longer use a static
	// initialized class variable - instead we call createsRemoteUrls(...) each
	// time in the factory
	// method. (Since PropertyResourceBundleManager caches, this is not a
	// performance concern.)
	// DSJ 2009/2/17
	// private static boolean mCreatesRemoteUrls =
	// createsRemoteUrls(PROPERTY_FILE_RESOURCE_PATH);

	/**
	 * Private constructor to prevent creation of this factory instance, knowing
	 * that all the methods are private.
	 */
	private PortalURLFactory() {
	}

	/**
	 * Creates a new portal URL to the specified <tt>anotherSite</tt> and the
	 * specified <tt>pageFriendlyUri</tt> . The result URL will be "https" if
	 * the <tt>secure</tt> flag is true and <tt>siteRootUrl</tt> contains
	 * protocol and host (i.e. is not a server relative URL). The
	 * <tt>anotherSite</tt>, if specified, should be the site name for that
	 * site (eg, in Vignette, the "site DNS name"). The <tt>pageFriendlyUri</tt>,
	 * if specified, should be the additional path beyond the portal site root,
	 * which identifies the particular portal page (eg, in Vignette, a
	 * navigation item friendly URI defined in the portal console; or a
	 * secondary page friendly template name). If a <tt>pageFriendlyUri</tt>
	 * is not specified, then the home page is assumed.
	 * <p>
	 * The <tt>siteRootUrl</tt> should be the home page URL for the current
	 * portal site. It is available for portlets using one of the following
	 * methods from the SPF portlet utilities:
	 * {@link com.hp.it.spf.xa.misc.portlet.Utils#getPortalSiteURL(javax.portlet.PortletRequest)}
	 * or
	 * {@link com.hp.it.spf.xa.misc.portlet.Utils#getPortalSiteURL(javax.portlet.PortletRequest,java.lang.Boolean,java.lang.String,int,java.lang.String)}
	 * (see). You can also pass the results of the portlet utilities'
	 * {@link com.hp.it.spf.xa.misc.portlet.Utils#getPortalRequestURL(javax.portlet.PortletRequest)}
	 * or
	 * {@link com.hp.it.spf.xa.misc.portlet.Utils#getPortalRequestURL(javax.portlet.PortletRequest,java.lang.Boolean,java.lang.String,int,java.lang.String)}
	 * methods as the <tt>siteRootUrl</tt> (everything in the passed URL
	 * beyond the site root is ignored). Note that all of those techniques pass
	 * an absolute URL, so if you need to switch the protocol using
	 * <tt>secure</tt>, you can.
	 * <p>
	 * For portal components, the <tt>siteRootUrl</tt> can be gotten from the
	 * Vignette API's such as
	 * {@link com.vignette.portal.website.enduser.PortalContext#getSiteURI(String)}
	 * (see). That returns a relative URL, though. For an absolute URL, use the
	 * portal equivalent methods to the ones for portlets above: ie, the portal
	 * utilities'
	 * {@link com.hp.it.spf.xa.misc.portal.Utils#getPortalSiteURL(javax.servlet.http.HttpServletRequest)} method,
	 * {@link com.hp.it.spf.xa.misc.portal.Utils#getPortalSiteURL(javax.servlet.http.HttpServletRequest, java.lang.Boolean, java.lang.String, int, java.lang.String)} method,
	 * etc.
	 * <p>
	 * For your interest, some secondary page template names for SPF-standard
	 * secondary pages are available through both the portlet
	 * {@link com.hp.it.spf.xa.misc.portlet.Consts} and portal 
	 * {@link com.hp.it.spf.xa.misc.portal.Consts} classes. For example, the
	 * template for the global help secondary page is available to portlets in
	 * {@link com.hp.it.spf.xa.misc.portlet.Consts#PAGE_FRIENDLY_URI_GLOBAL_HELP}.
	 * These names can be passed as the <tt>pageFriendlyUri</tt> parameter
	 * here, to make a portal URL for those pages.
	 * 
	 * @param siteRootUrl
	 *            current site home page URL (or you can pass any site page URL
	 *            and the method will reduce it to the site home page URL for
	 *            you)
	 * @param anotherSite
	 *            another site in the portal; if <tt>null</tt> the result URL
	 *            will be created for the current site
	 * @param pageFriendlyUri
	 *            identifies the page within the current site (when
	 *            <tt>anotherSite</tt> parameter of this method is null) or
	 *            another site (when <tt>anotherSite</tt> parameter is not
	 *            null) - can be a navigation item friendly URI as defined in
	 *            portal console for the site, or a secondary page template
	 *            friendly URI - if null or blank, the home page is assumed
	 * @param secure
	 *            flag indicating whether the result url should be http or
	 *            https; ignored if <tt>siteRootUrl</tt> is not a complete URL
	 *            (protocol, host, path)
	 * @return new portal URL to the specified site and page.
	 */
	public static PortalURL createPageURL(String siteRootUrl,
			String anotherSite, String pageFriendlyUri, boolean secure) {
		int nonStandardHttpPort = getNonstandardHttpPort(PROPERTY_FILE_RESOURCE_PATH);
		int nonStandardHttpsPort = getNonstandardHttpsPort(PROPERTY_FILE_RESOURCE_PATH);
		if (createsRemoteUrls(PROPERTY_FILE_RESOURCE_PATH)) {
			return new VignetteRemotePortalURL(siteRootUrl, anotherSite,
					pageFriendlyUri, secure, nonStandardHttpPort,
					nonStandardHttpsPort);
		} else {
			return new VignetteLocalPortalURL(siteRootUrl, anotherSite,
					pageFriendlyUri, secure, nonStandardHttpPort,
					nonStandardHttpsPort);
		}
	}

	/**
	 * Creates a new portal URL for the and the specified
	 * <tt>pageFriendlyUri</tt> in the same portal site as in the given
	 * <tt>siteRootUrl</tt>. The resulting portal URL will have "https"
	 * protocol if the <tt>secure</tt> flag is true and the
	 * <tt>siteRootUrl</tt> is a complete url (protocol, host, path).
	 * <p>
	 * This method is equivalent to calling
	 * {@link #createPageURL(String, String, String, boolean)} where the
	 * <tt>anotherSite</tt> parameter to that method is left null. Please see
	 * the documentation for that method for more information about the
	 * behavior.
	 * 
	 * @param siteRootUrl
	 *            current site home page URL (or you can pass any site page URL
	 *            and the method will reduce it to the site home page URL for
	 *            you)
	 * @param pageFriendlyUri
	 *            identifies the page within the current site (when
	 *            <tt>anotherSite</tt> parameter of this method is null) or
	 *            another site (when <tt>anotherSite</tt> parameter is not
	 *            null) - can be a navigation item friendly URI as defined in
	 *            portal console for the site, or a secondary page template
	 *            friendly URI - if null or blank, the home page is assumed
	 * @param secure
	 *            flag indicating whether the result url should be http or
	 *            https; ignored if <tt>siteRootUrl</tt> is not a complete URL
	 *            (protocol, host, path)
	 * @return portal URL to the same site and the specified page
	 * 
	 * @see #createPageURL(String, String, String, boolean)
	 */
	public static PortalURL createPageURL(String siteRootUrl,
			String pageFriendlyUri, boolean secure) {
		return createPageURL(siteRootUrl, null, pageFriendlyUri, secure);
	}

	/**
	 * Creates a new portal URL for the specified <tt>anotherSite</tt> and the
	 * <tt>pageFriendlyUri</tt>. The protocol of the result URL will be the
	 * same as in the given <tt>siteRootUrl</tt>.
	 * <p>
	 * This method is equivalent to calling
	 * {@link #createPageURL(String, String, String, boolean)} where the
	 * <tt>secure</tt> parameter to that method is set consistently with the
	 * <tt>siteRootUrl</tt>. Please see the documentation for that method for
	 * more information about the behavior.
	 * 
	 * @param siteRootUrl
	 *            current site home page URL (or you can pass any site page URL
	 *            and the method will reduce it to the site home page URL for
	 *            you)
	 * @param anotherSite
	 *            another site in the portal; if <tt>null</tt> the result URL
	 *            will be created for the current site
	 * @param pageFriendlyUri
	 *            identifies the page within the current site (when
	 *            <tt>anotherSite</tt> parameter of this method is null) or
	 *            another site (when <tt>anotherSite</tt> parameter is not
	 *            null) - can be a navigation item friendly URI as defined in
	 *            portal console for the site, or a secondary page template
	 *            friendly URI - if null or blank, the home page is assumed
	 * @return portal URL to the specified site and page
	 * 
	 * @see #createPageURL(String, String, String, boolean)
	 */
	public static PortalURL createPageURL(String siteRootUrl,
			String anotherSite, String pageFriendlyUri) {
		boolean secure = false;
		if (siteRootUrl != null) {
			secure = siteRootUrl.trim().toLowerCase().startsWith("https:");
		}
		return createPageURL(siteRootUrl, anotherSite, pageFriendlyUri, secure);
	}

	/**
	 * Creates new portal URL for the same site as in <tt>siteRootUrl</tt> and
	 * the <tt>pageFriendlyUri</tt>. The protocol of the URL (http, https)
	 * will be the same as in the given <tt>siteRootUrl</tt> parameter.
	 * <p>
	 * This method is equivalent to calling
	 * {@link #createPageURL(String, String, String, boolean)} where the
	 * <tt>secure</tt> parameter to that method is set consistently with the
	 * <tt>siteRootUrl</tt>, and where the <tt>anotherSite</tt> parameter
	 * to that method is left null. Please see the documentation for that method
	 * for more information about the behavior.
	 * 
	 * @param siteRootUrl
	 *            current site home page URL (or you can pass any site page URL
	 *            and the method will reduce it to the site home page URL for
	 *            you)
	 * @param pageFriendlyUri
	 *            identifies the page within the current site (when
	 *            <tt>anotherSite</tt> parameter of this method is null) or
	 *            another site (when <tt>anotherSite</tt> parameter is not
	 *            null) - can be a navigation item friendly URI as defined in
	 *            portal console for the site, or a secondary page template
	 *            friendly URI - if null or blank, the home page is assumed
	 * @return portal URL to the same site and the specified page
	 * 
	 * @see #createPageURL(String, String, String, boolean)
	 */
	public static PortalURL createPageURL(String siteRootUrl,
			String pageFriendlyUri) {
		boolean secure = false;
		if (siteRootUrl != null) {
			secure = siteRootUrl.trim().toLowerCase().startsWith("https:");
		}
		return createPageURL(siteRootUrl, null, pageFriendlyUri, secure);
	}

	/**
	 * Returns <tt>true</tt> if this factory create URLs for remote portlets.
	 * The method checks first whether a system property
	 * <tt>portalurl.portlet.remote</tt> exists and if yes, it uses its
	 * boolean value. Then a property file <tt>portalurl.properties</tt> is
	 * checked and if yes the boolean value of <tt>portalurl.portlet.remote</tt>
	 * property is used. If none of the above is defined, by default it creates
	 * remote portlet URLs.
	 * <p>
	 * This method uses the
	 * {@link com.hp.it.spf.xa.misc.properties.PortalResourceBundleManager}
	 * class to load the <code>portalurl.properties</code> file, so all the
	 * usual instructions apply regarding where you can put property files
	 * loaded by that class. This method is called each time the factory is
	 * invoked, so changes to the property file do not require a restart to take
	 * effect.
	 * 
	 * @return <tt>true</tt> if this class creates remote portlet URLs; false
	 *         otherwise
	 */
	public static boolean createsRemoteUrls() {
		return createsRemoteUrls(PROPERTY_FILE_RESOURCE_PATH);
	}

	/**
	 * Same as {@link #createsRemoteUrls()} but lets the property file
	 * <code>portalurl.properties</code> be overridden with another file of a
	 * different name.
	 * 
	 * @param propertyFileResourcePath
	 *            resource path of the optional property file, relative to where
	 *            the system classloader will search
	 * @return <tt>true</tt> if this class creates remote portlet URLs; false
	 *         otherwise
	 */
	static boolean createsRemoteUrls(String propertyFileResourcePath) {
		// first check system property
		if (System.getProperty(REMOTE_PROPERTY_NAME) != null) {
			return Boolean.valueOf(System.getProperty(REMOTE_PROPERTY_NAME));
		}

		// then check property file
		String isRemote = PropertyResourceBundleManager.getString(
				propertyFileResourcePath, REMOTE_PROPERTY_NAME);
		if (isRemote != null && !isRemote.trim().equals("")) {
			return Boolean.valueOf(isRemote);
		}

		// finally return default value
		return true;
	}

	/**
	 * Returns a non-standard port which should be used in this environment for
	 * HTTP, if any. The method checks first whether a system property
	 * <tt>portalurl.port.http</tt> exists and if it does, and has a positive
	 * integer value besides 80, then that value is returned. Otherwise, it
	 * checks the <tt>portalurl.properties</tt> file for a property of the
	 * same name - it it exists, and has a positive integer value besides 80,
	 * then that value is returned. Otherwise -1 is returned, which means that
	 * no non-standard HTTP port has been defined.
	 * 
	 * @return a non-standard port number for HTTP, if one has been defined
	 */
	public static int getNonstandardHttpPort() {
		return getNonstandardHttpPort(PROPERTY_FILE_RESOURCE_PATH);
	}
	
	/**
	 * Same as {@link #getNonstandardHttpPort()} but lets the property file
	 * <code>portalurl.properties</code> be overridden with another file of a
	 * different name.
	 * 
	 * @param propertyFileResourcePath
	 *            resource path of the optional property file, relative to where
	 *            the system classloader will search
	 * @return a non-standard port number for HTTP, if one has been defined
	 */
	static int getNonstandardHttpPort(String propertyFileResourcePath) {
		return getNonstandardPort(propertyFileResourcePath,
				HTTP_PORT_PROPERTY_NAME, 80);
	}

	/**
	 * Returns a non-standard port which should be used in this environment for
	 * HTTPS, if any. The method checks first whether a system property
	 * <tt>portalurl.port.https</tt> exists and if it does, and has a positive
	 * integer value besides 443, then that value is returned. Otherwise, it
	 * checks the <tt>portalurl.properties</tt> file for a property of the
	 * same name - it it exists, and has a positive integer value besides 443,
	 * then that value is returned. Otherwise -1 is returned, which means that
	 * no non-standard HTTPS port has been defined.
	 * 
	 * @return a non-standard port number for HTTPS, if one has been defined
	 */
	public static int getNonstandardHttpsPort() {
		return getNonstandardHttpsPort(PROPERTY_FILE_RESOURCE_PATH);
	}
		
	/**
	 * Same as {@link #getNonstandardHttpsPort()} but lets the property file
	 * <code>portalurl.properties</code> be overridden with another file of a
	 * different name.
	 * 
	 * @param propertyFileResourcePath
	 *            resource path of the optional property file, relative to where
	 *            the system classloader will search
	 * @return a non-standard port number for HTTPS, if one has been defined
	 */
	static int getNonstandardHttpsPort(String propertyFileResourcePath) {
		return getNonstandardPort(propertyFileResourcePath,
				HTTPS_PORT_PROPERTY_NAME, 443);
	}

	private static int getNonstandardPort(String propertyFileResourcePath,
			String propertyName, int standardPort) {
		int val = -1;
		String value = null;
		// first check system property
		if ((value = System.getProperty(propertyName)) != null) {
			try {
				val = Integer.parseInt(value);
				if ((val > 0) && (val != standardPort)) {
					return val;
				}
			} catch (NumberFormatException e) {
			}
		}

		// then check property file
		if ((value = PropertyResourceBundleManager.getString(
				propertyFileResourcePath, propertyName)) != null) {
			try {
				val = Integer.parseInt(value);
				if ((val > 0) && (val != standardPort)) {
					return val;
				}
			} catch (NumberFormatException e) {
			}
		}

		// return default
		return val;
	}
}