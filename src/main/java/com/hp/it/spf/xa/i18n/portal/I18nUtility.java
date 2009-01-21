/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.xa.i18n.portal;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.epicentric.common.website.I18nUtils;
import com.epicentric.common.website.Localizer;
import com.epicentric.common.website.SessionInfo;
import com.epicentric.common.website.SessionUtils;
import com.epicentric.i18n.LocalizedBundle;
import com.epicentric.i18n.LocalizedBundleManager;
import com.epicentric.site.Site;
import com.epicentric.template.Style;
import com.epicentric.user.User;
import com.hp.it.spf.xa.help.ContextualHelpProvider;
import com.hp.it.spf.xa.help.ContextualHelpUtility;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;
import com.hp.it.spf.xa.help.portal.GlobalHelpUtility;
import com.hp.it.spf.xa.help.portal.GlobalHelpProvider;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * A class of useful internationalization/localization methods for use by
 * Vignette portal components.
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see com.hp.it.spf.xa.i18n.I18nUtility
 */

public class I18nUtility extends com.hp.it.spf.xa.i18n.I18nUtility {

	/**
	 * Logger instance.
	 */
	private static final LogWrapper LOG = new LogWrapper(I18nUtility.class);

	/**
	 * The name of the property file which stores additional locales for portal
	 * site. (The file extension .properties is assumed by the
	 * PropertyResourceBundleManager.)
	 */
	private static final String SITE_ADDL_LOCALE_CONFIG_FILE = "site_locale_support";

	/**
	 * Regular expression for opening <code>&lt;SPAN&gt;</code> tag.
	 */
	private static String OPEN_SPAN_REGEX = "(?i:<SPAN.*?>)";

	/**
	 * Regular expression for closing <code>&lt;SPAN&gt;</code> tag.
	 */
	private static String CLOSE_SPAN_REGEX = "(?i:</SPAN>)";

	/**
	 * Private to prevent external construction.
	 */
	private I18nUtility() {

	}

	/**
	 * Attempts to set the given locale into the given portal request, returning
	 * true or false depending on whether the attempt succeeded. Setting a
	 * locale into the portal request ensures that downstream localization for
	 * that request will be based on that locale. Feature services generally
	 * never need to call this method; the framework will manage it for them.
	 * This method works for both authenticated and anonymous (guest) users -
	 * the standard published Vignette API at this time of writing works only
	 * for authenticated users.
	 * 
	 * @param pReq
	 *            The portal request.
	 * @param pLocale
	 *            A locale.
	 * @return Success or failure.
	 */
	public static boolean setLocale(HttpServletRequest pReq, Locale pLocale) {
		if (null == pReq || null == pLocale) {
			return false;
		}
		try {
			HttpSession session = pReq.getSession();
			User currentUser = SessionUtils.getCurrentUser(session);
			if (currentUser.isGuestUser()) {
				Localizer localizerForGuest = (Localizer) I18nUtils
						.getLocalizer(session, pReq);
				localizerForGuest.setLocale(pLocale);
			} else {
				I18nUtils.setUserLocale(currentUser, pLocale);
			}
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	/**
	 * Returns the locale from the given portal request, or the current default
	 * locale for the portal site if no locale can be found in the request. If
	 * there is even no default locale enabled for the portal site, then the
	 * platform default locale is returned. This method works for both
	 * authenticated and anonymous (guest) users - the standard published
	 * Vignette API at this time of writing works only for authenticated users.
	 * 
	 * @param pReq
	 *            The portal request.
	 * @return The locale of the request, or else the site default locale.
	 */
	public static Locale getLocale(HttpServletRequest pReq) {
		Locale rLocale = null;
		try {
			if (pReq != null) {
				HttpSession session = pReq.getSession();
				User currentUser = SessionUtils.getCurrentUser(session);
				if (currentUser.isGuestUser()) {
					Localizer localizerForGuest = (Localizer) I18nUtils
							.getLocalizer(session, pReq);
					rLocale = localizerForGuest.getLocale();
				} else {
					rLocale = I18nUtils.getUserLocale(currentUser);
				}
			}
		} catch (Exception ex) {
		}
		if (null == rLocale) {
			rLocale = getDefaultLocale(pReq);
		}
		return rLocale;
	}

	/**
	 * <p>
	 * Returns a collection of all of the locales currently enabled for the
	 * portal site contained in the given request. This includes the default
	 * locale for the site, as well as any additional locales configured for the
	 * site in <code>site_locale_support.properties</code>. The standard
	 * published Vignette API at the time of writing returns only the default
	 * locale for the site; this method is tied-into the
	 * <code>site_locale_support.properties</code> so that each portal site in
	 * the environment can have a different set of enabled locales. (However the
	 * method masks those locales against the whole set of locales registered in
	 * the Vignette server. Any locale listed for the site in
	 * <code>site_locale_support.properties</code>, but not registered with
	 * Vignette, is ignored.)
	 * </p>
	 * <p>
	 * There should always be at least one locale enabled (ie the default locale
	 * for the site, which in turn defaults to the platform default locale) and
	 * thus present in the return from this method. An empty collection is
	 * returned if there is some internal error.
	 * </p>
	 * 
	 * @param request
	 *            The portal request.
	 * @return The locales enabled for the portal site indicated in the request.
	 */
	public static Collection getAvailableLocales(HttpServletRequest request) {
		Collection availableLocales = new HashSet();
		if (request != null) {
			SessionInfo sessionInfo = (SessionInfo) request.getSession()
					.getAttribute(SessionInfo.SESSION_INFO_NAME);
			if (sessionInfo != null) {
				Site currentSite = Utils.getEffectiveSite(request);
				availableLocales = getAvailableLocales(currentSite);
			}
		}
		return availableLocales;
	}

	/**
	 * <p>
	 * Returns a collection of all of the locales currently enabled for the
	 * given portal site. This includes the default locale for the site, as well
	 * as any additional locales configured for the site in
	 * <code>site_locale_support.properties</code>. The standard published
	 * Vignette API at the time of writing returns only the default locale for
	 * the site; this method is tied-into the
	 * <code>site_locale_support.properties</code> so that each portal site in
	 * the environment can have a different set of enabled locales. (However the
	 * method masks those locales against the whole set of locales registered in
	 * the Vignette server. Any locale listed for the site in
	 * <code>site_locale_support.properties</code>, but not registered with
	 * Vignette, is ignored.)
	 * </p>
	 * <p>
	 * There should always be at least one locale enabled (ie the default locale
	 * for the site, which in turn defaults to the platform default locale) and
	 * thus present in the return from this method. An empty collection is
	 * returned if there is some internal error.
	 * </p>
	 * 
	 * @param site
	 *            A portal site.
	 * @return The locales enabled for that site.
	 */
	public static Collection getAvailableLocales(Site site) {
		Collection availableLocales = new HashSet();

		if (site != null) {
			// get additional locales for the site from properties file, and
			// mask against server-registered locales
			Collection siteLocales = getSiteAdditionalLocales(site);
			if (siteLocales != null) {
				siteLocales.retainAll(I18nUtils.getRegisteredLocales());
				availableLocales = siteLocales;
			}

			// whether additional locales exist or not, default locale should be
			// included in the return
			Locale defaultLocale = getDefaultLocale(site);
			if (defaultLocale != null
					&& !availableLocales.contains(defaultLocale)) {
				availableLocales.add(defaultLocale);
			}
		}
		return availableLocales;
	}

	/**
	 * Get the additional locales for the given site which are configured in
	 * <code>site_locale_support.propeties</code>. This uses
	 * PropertyResourceBundleManager so that the file can be cached and
	 * hot-reloadable. Return null if the file cannot be loaded, or the site
	 * does not exist in the file, or the site exists in the file but without
	 * any parsable language tags. Return all registered locales if the site
	 * exists in the file with the special keyword <code>ALL</code>.
	 * 
	 * @param pSite
	 *            A portal site.
	 * @return The additional locales enabled for that site.
	 */
	private static Collection getSiteAdditionalLocales(Site pSite) {
		if (pSite == null) {
			return null;
		}
		Collection locales = null;
		try {
			// Get the site locale configuration from the properties file.
			String value = null;
			ResourceBundle rb = PropertyResourceBundleManager
					.getBundle(I18nUtility.SITE_ADDL_LOCALE_CONFIG_FILE);
			if (rb == null) {
				LOG
						.error("I18nUtility: getSiteAdditionalLocales failed to open "
								+ SITE_ADDL_LOCALE_CONFIG_FILE
								+ " properties file");
			} else {
				try {
					value = rb.getString(pSite.getDNSName().toLowerCase());
				} catch (Exception ex) {
				}
			}

			// Parse the locale information for the site into locales (if "ALL"
			// then assume all registered locales).
			if (value != null && !value.equals("")) {
				if (value.trim().equalsIgnoreCase("ALL")) {
					return I18nUtils.getRegisteredLocales();
				}
				String[] strLocales = value.split(",");
				if (strLocales != null) {
					int len = strLocales.length;
					locales = new ArrayList(len);
					Locale tempLocale = null;
					for (int i = 0; i < len; i++) {
						tempLocale = languageTagToLocale(strLocales[i].trim());
						if (tempLocale != null && !locales.contains(tempLocale)) {
							locales.add(tempLocale);
						}
					}
				}
			}
		} catch (Exception ex) {
		}
		return locales;
	}

	/**
	 * Return the default locale for the portal site contained in the given
	 * portal request. This method uses the standard Vignette published API to
	 * accomplish its work. If for some reason no locale default has been
	 * configured for the site, then the platform-default locale is returned.
	 * Null is returned if there is an internal error.
	 * 
	 * @param pReq
	 *            The portal request.
	 * @return The default locale for the portal site indicated in the request.
	 */
	public static Locale getDefaultLocale(HttpServletRequest pReq) {
		Locale defaultLocale = null;
		if (pReq != null) {
			try {
				SessionInfo sessionInfo = (SessionInfo) pReq.getSession()
						.getAttribute(SessionInfo.SESSION_INFO_NAME);
				if (sessionInfo != null) {
					Site currentSite = Utils.getEffectiveSite(pReq);
					defaultLocale = getDefaultLocale(currentSite);
				}
			} catch (Exception ex) {
			}
		}
		return defaultLocale;
	}

	/**
	 * Return the default locale for the given portal site. This method uses the
	 * standard Vignette published API to accomplish its work. If for some
	 * reason no locale default has been configured for the site, then the
	 * platform-default locale is returned. Null is returned if there is an
	 * internal error.
	 * 
	 * @param pReq
	 *            The portal request.
	 * @return The default locale for the portal site indicated in the request.
	 */
	private static Locale getDefaultLocale(Site pSite) {
		if (pSite == null) {
			return null;
		}
		Locale defaultLocale = pSite.getDefaultLocale();
		if (defaultLocale == null) {
			defaultLocale = Locale.getDefault();
		}
		return defaultLocale;
	}

	/**
	 * <p>
	 * Returns an input stream for a proper localized version of the given base
	 * filename in the indicated portal component. The given filename must be a
	 * base filename for a static resource bundle comprised of secondary support
	 * files, stored against the portal component indicated in the given portal
	 * context.
	 * </p>
	 * <p>
	 * This method uses the locale and portal component indicated in the given
	 * portal context, to find the best-candidate localized version of the given
	 * file among the resource bundles found among the component's secondary
	 * support files. The logic for finding the best-candidate is the same as
	 * that used by the Java-standard ResourceBundle class. Thus the input
	 * stream returned by this method will come from the best-candidate
	 * locale-tagged filename, or the given base filename if there is no better
	 * fit. If the resource bundle does not exist at all among the portal
	 * component's secondary support files, the method returns null. This method
	 * also returns null if any of its parameters are null.
	 * </p>
	 * <p>
	 * For example: among the portal component's secondary support files,
	 * consider that we have the files <code>foo.jpg</code>,
	 * <code>foo_fr_CA.jpg</code>, and <code>foo_fr.jpg</code>. Then:
	 * </p>
	 * 
	 * <dl>
	 * <dt><code>getLocalizedFilePath(context, "foo.jpg")</code> when
	 * <code>context</code> contains Canada French (<code>Locale.CANADA_FRENCH</code>)</dt>
	 * <dd>returns
	 * <code><i>root-path</i>/<i>portal-root-path</i>/<i>component-path</i>/foo_fr_CA.jpg</code>
	 * (where the various path values depend on the environment and are not
	 * documented here)</dd>
	 * 
	 * <dt><code>getLocalizedFilePath(context, "foo.jpg")</code> when
	 * <code>context</code> contains France French (<code>Locale.FRANCE</code>)</dt>
	 * <dd>returns
	 * <code><i>root-path</i>/<i>portal-root-path</i>/<i>component-path</i>/foo_fr.jpg</code></dd>
	 * 
	 * <dt><code>getLocalizedFilePath(request, "foo.jpg")</code> when
	 * <code>context</code> contains generic Italian (<code>Locale.ITALIAN</code>)</dt>
	 * <dd>returns
	 * <code><i>root-path</i>/<i>portal-root-path</i>/<i>component-path</i>/foo.jpg</code></dd>
	 * </dt>
	 * 
	 * <dt><code>getLocalizedFilePath(request, "bar.jpg")</code></dt>
	 * <dd>returns <code>null</code></dd>
	 * </dl>
	 * 
	 * <p>
	 * <b>Note:</b> On case-sensitive filesystems, lowercase is assumed for the
	 * langage and variant codes, and uppercase is assumed for the country code.
	 * Thus in the above examples, different results would obtain if
	 * <code>foo_fr.jpg</code> and/or <code>foo_fr_CA.jpg</code> were tagged
	 * with uppercase language or lowercase country. Be sure your resource
	 * bundles follow the convention.
	 * </p>
	 * <p>
	 * <b>Note:</b> As mentioned, this method automatically discovers the
	 * best-fit localized file for the locale. A common alternative technique
	 * for generating a localized file URL relies on the best-fit localized file
	 * already being stored in a message resource. Of course, this relies on you
	 * having arranged that, but if you would rather generate your localized
	 * file URL that way, use the companion
	 * <code>getLocalizedFileAsStream(PortalContext,String,String)</code>
	 * method.
	 * </p>
	 * 
	 * @param pContext
	 *            The portal context.
	 * @param pBaseFileName
	 *            The name of the base file to search.
	 * @return An input stream for the best-candidate localized file, or null if
	 *         none was found.
	 */
	public static InputStream getLocalizedFileAsStream(PortalContext pContext,
			String pBaseFileName) {
		return getLocalizedFileAsStream(pContext, pBaseFileName, true);
	}

	/**
	 * <p>
	 * Returns an input stream for the given version of the given base filename
	 * in the current portal component (e, the particular file chosen for the
	 * input stream is the one which is localized to best-fit the indicated
	 * locale, or not, per the boolean switch). The given filename must be a
	 * base filename for a static resource bundle comprised of secondary support
	 * files, stored against the portal component indicated in the given portal
	 * context.
	 * <p>
	 * If the boolean switch is set to <code>true</code>, this method works
	 * exactly like <code>getLocalizedFileAsStream(PortalContext,String)</code>
	 * (see). If the boolean switch is set to <code>false</code>, this method
	 * does not actually localize the returned filename. Instead, the returned
	 * filename points to the base filename if it existed, and null otherwise.
	 * </p>
	 * 
	 * @param pContext
	 *            The portal context.
	 * @param pBaseFileName
	 *            The name of the base file to search.
	 * @param pLocalized
	 *            The version of the file for which to search: the base file
	 *            (false) or the best-fitting localized file (true).
	 * @return An input stream for the best-candidate localized file, or null if
	 *         none was found.
	 */
	public static InputStream getLocalizedFileAsStream(PortalContext pContext,
			String pBaseFileName, boolean pLocalized) {

		if (pContext == null || pBaseFileName == null) {
			return null;
		}
		pBaseFileName = pBaseFileName.trim();
		pBaseFileName = slashify(pBaseFileName);
		if (pBaseFileName.length() == 0) {
			return null;
		}

		// get the relative path for the current portal component
		Style thisStyleObject = getCurrentComponent(pContext);
		String relPath = thisStyleObject.getUrlSafeRelativePath();

		// get the absolute path for the current portal component - this is
		// the path to the location of its secondary support files
		String absPath = pContext.getPortalRequest().getSession()
				.getServletContext().getRealPath(relPath)
				+ "/";
		Locale locale = getLocale(pContext.getPortalRequest().getRequest());
		String fileName = getLocalizedFileName(absPath, pBaseFileName, locale,
				pLocalized);

		// file path is just the absolute path + the filename (if not null -
		// otherwise return null)
		if (fileName != null) {
			fileName = slashify(absPath + "/" + fileName);
			try {
				return new FileInputStream(fileName);
			} catch (FileNotFoundException e) { // should never happen
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Returns a URL (suitable for presentation to the user) for downloading
	 * from the indicated portal component a proper localized version of the
	 * given base filename. The returned URL is a relative URL (ie relative to
	 * the scheme, hostname, and port used in the current request) and is ready
	 * for presentation to the user in the portal response. For example, you can
	 * take the return from this method, and express it as the <code>SRC=</code>
	 * attribute in an <code>&lt;IMG&gt;</code> tag.
	 * </p>
	 * <p>
	 * The given filename must be a base filename for a static resource bundle
	 * comprised of secondary support files, stored against the portal component
	 * indicated in the given portal context.
	 * </p>
	 * <p>
	 * This method uses the locale and portal component indicated in the given
	 * portal context, to find the best-candidate localized version of the given
	 * file among all the resource bundles among that component's secondary
	 * support files. The logic for finding the best-candidate is the same as
	 * that used by the Java-standard ResourceBundle class. Thus the URL
	 * returned by this method will point to the best-candidate locale-tagged
	 * filename, or the given base filename if there is no better fit. If the
	 * resource bundle does not exist at all among the portal component's
	 * secondary support files, the method returns a URL pointing to the base
	 * filename (of course, if subsequently opened by a browser, this URL will
	 * trigger an HTTP 404 error since the file does not exist). This method
	 * returns null if any of its parameters are null.
	 * </p>
	 * <p>
	 * For example: in the current portal component, consider that we have the
	 * the following secondary support files: <code>picture.jpg</code>,
	 * <code>picture_fr_CA.jpg</code>, and <code>picture_fr.jpg</code>.
	 * Then:
	 * </p>
	 * 
	 * <dl>
	 * <dt><code>getLocalizedFileURL(context, "picture.jpg")</code> when
	 * <code>context</code> indicates Canada French (<code>Locale.CANADA_FRENCH</code>)</dt>
	 * <dd>returns a relative secondary support file URL for
	 * <code>picture_fr_CA.jpg</code>. (The exact URL depends on Vignette
	 * implementation, and is not documented here. But note that it is a
	 * relative URL to the scheme, hostname, and port in the current request.)</dd>
	 * 
	 * <dt><code>getLocalizedFileURL(context, "picture.jpg")</code> when
	 * <code>context</code> indicates France French (<code>Locale.FRANCE</code>)</dt>
	 * <dd>returns a relative secondary support file URL for
	 * <code>picture_fr.jpg</code>.</dd>
	 * 
	 * <dt><code>getLocalizedFileURL(context, "picture.jpg")</code> when
	 * <code>context</code> indicates generic Italian (<code>Locale.ITALIAN</code>)</dt>
	 * <dd>returns a relative secondary support file URL for
	 * <code>picture.jpg</code></dd>
	 * </dt>
	 * 
	 * <dt><code>getLocalizedFileURL(context, "photo.jpg")</code></dt>
	 * <dd>returns a relative secondary support file URL for
	 * <code>photo.jpg</code> (if opened by the browser, this will trigger an
	 * HTTP 404 error)</dd>
	 * </dl>
	 * 
	 * <p>
	 * <b>Note:</b> On case-sensitive filesystems, lowercase is assumed for the
	 * langage and variant codes, and uppercase is assumed for the country code.
	 * Thus in the above examples, different results would obtain if
	 * <code>picture_fr.jpg</code> and/or <code>picture_fr_CA.jpg</code>
	 * were tagged with uppercase language or lowercase country. Be sure your
	 * resource bundles follow the convention.
	 * </p>
	 * <p>
	 * <b>Note:</b> As mentioned, this method automatically discovers the
	 * best-fit localized file for the locale. A common alternative technique
	 * for generating a localized file URL relies on the best-fit localized file
	 * already being stored in a message resource. Of course, this relies on you
	 * having arranged that, but if you would rather generate your localized
	 * file URL that way, use the companion
	 * <code>getLocalizedFileURL(PortalContext,String,String)</code> method.
	 * </p>
	 * 
	 * @param pContext
	 *            The portal context.
	 * @param pBaseFileName
	 *            The name of the base file to search.
	 * @return A URL for downloading the best-fit localized version of the base
	 *         file from the portal component.
	 */
	public static String getLocalizedFileURL(PortalContext pContext,
			String pBaseFileName) {
		return getLocalizedFileURL(pContext, pBaseFileName, true);
	}

	/**
	 * <p>
	 * Returns a URL (suitable for presentation to the user) for downloading
	 * from the indicated portal component the given version of the given base
	 * filename (ie, localized to best-fit the indicated locale, or not, per the
	 * boolean switch). The returned URL is a relative URL (ie relative to the
	 * scheme, hostname, and port used in the current request) and is ready for
	 * presentation to the user in the portal response. For example, you can
	 * take the return from this method, and express it as the <code>SRC=</code>
	 * attribute in an <code>&lt;IMG&gt;</code> tag.
	 * </p>
	 * <p>
	 * If the boolean switch is set to <code>true</code>, this method works
	 * exactly like <code>getLocalizedFileURL(PortalContext,String)</code>
	 * (see). If the boolean switch is set to <code>false</code>, this method
	 * does not actually localize the returned filename. Instead, the returned
	 * filename points to the base filename if it existed, and null otherwise.
	 * </p>
	 * 
	 * @param pContext
	 *            The portal context.
	 * @param pBaseFileName
	 *            The name of the base file to search.
	 * @param pLocalized
	 *            The version of the file for which to search: the base file
	 *            (false) or the best-fitting localized file (true).
	 * @return A URL for downloading the best-fit localized version of the base
	 *         file from the portal component.
	 */
	public static String getLocalizedFileURL(PortalContext pContext,
			String pBaseFileName, boolean pLocalized) {
		if (pContext == null || pBaseFileName == null) {
			return null;
		}
		pBaseFileName = pBaseFileName.trim();
		pBaseFileName = slashify(pBaseFileName);
		if (pBaseFileName.length() == 0) {
			return null;
		}
		// get the relative path for the current portal component
		Style thisStyleObject = getCurrentComponent(pContext);
		String relPath = thisStyleObject.getUrlSafeRelativePath();

		// get the absolute path for the current portal component - this is
		// the path to the location of its secondary support files
		String absPath = pContext.getPortalRequest().getSession()
				.getServletContext().getRealPath(relPath)
				+ "/";
		Locale locale = getLocale(pContext.getPortalRequest().getRequest());
		String fileName = getLocalizedFileName(absPath, pBaseFileName, locale,
				pLocalized);
		// relative URL is just the portal context root path + the relative
		// path to the component + the localized filename (if not null)
		String url = null;
		if (fileName != null) {
			url = slashify(pContext.getPortalHttpRoot() + "/" + relPath + "/"
					+ fileName);
		} else {
			url = slashify(pContext.getPortalHttpRoot() + "/" + relPath + "/"
					+ pBaseFileName);
		}
		// make sure the path includes the portal application context root
		String contextPath = pContext.getPortalRequest().getContextPath();
		if (!url.startsWith(contextPath)) {
			return contextPath + "/" + url;
		} else {
			return url;
		}
	}

	/**
	 * <p>
	 * Returns an input stream for a localized file in the indicated portal
	 * component, where the filename is retrieved (properly localized) from the
	 * component's message resources using the given key and default. The
	 * filename in the message resource must be that of a secondary support file
	 * for the same portal component.
	 * </p>
	 * <p>
	 * This method works like
	 * <code>getLocalizedFileAsStream(PortalContext,String)</code>, but where
	 * the localized filename string is taken from a message resource of the
	 * portal component, using the given key and default filename. Thus this
	 * method relies on the best-candidate filename for each locale already
	 * existing in the localized message properties for that locale, at the
	 * given key. (And if the key is not found, it applies the given default
	 * filename.) Thus the method just reads the filename from the message,
	 * assumes that file is the best one for that locale, and returns a path for
	 * it accordingly.
	 * </p>
	 * <p>
	 * The default filename is an optional parameter; if left null or blank, and
	 * the key is not found in the available messages, then null is returned.
	 * Null is also returned if the secondary support file matching the resolved
	 * name does not actually exist, or if the portal context or key parameters
	 * were null.
	 * </p>
	 * <p>
	 * <b>Note:</b> As mentioned, this method relies on you having placed the
	 * proper localized filenames for each locale into the message resources for
	 * each locale. If you would rather not do that, and would rather just have
	 * the system inspect the available files in the resource bundle vis-a-vis
	 * the locale automatically, use the companion
	 * <code>getLocalizedFilePath(PortalContext,String)</code> method.
	 * </p>
	 * 
	 * @param pContext
	 *            The portal context.
	 * @param pKey
	 *            The key for a message property containing the localized
	 *            filename.
	 * @param pDefault
	 *            The default filename to use if the message property does not
	 *            exist.
	 * @return An input stream for the best-candidate localized file, or null if
	 *         none was found.
	 */
	public static InputStream getLocalizedFileAsStream(PortalContext pContext,
			String pKey, String pDefault) {
		if (pContext == null || pKey == null) {
			return null;
		}
		pKey = pKey.trim();
		if (pKey.length() == 0) {
			return null;
		}
		if (pDefault != null) {
			pDefault = pDefault.trim();
			if (pDefault.length() == 0) {
				return null;
			}
		}

		// get the relative path for the current portal component
		Style thisStyleObject = getCurrentComponent(pContext);
		String relPath = thisStyleObject.getUrlSafeRelativePath();

		// get the absolute path for the current portal component - this is
		// the path to the location of its secondary support files
		String absPath = pContext.getPortalRequest().getSession()
				.getServletContext().getRealPath(relPath)
				+ "/";

		// get the filename from the message resources
		String fileName = getValue(pKey, pDefault, pContext);

		// file path is just the absolute path + the filename (if not null -
		// otherwise return null - also return null if file doesn't exist)
		if (fileName != null && !fileName.equals(pKey)
				&& (fileName.length() > 0)) {
			fileName = slashify(fileName);
			if (fileExists(absPath, fileName)) {
				fileName = slashify(absPath + "/" + fileName);
				try {
					return new FileInputStream(fileName);
				} catch (FileNotFoundException e) { // should never happen
				}
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Returns a URL (suitable for presentation to the user) for downloading a
	 * localized file from the indicated portal component, where the filename is
	 * retrieved (properly localized) from the component's message resources
	 * using the given key and default. The returned URL is a relative URL (ie
	 * relative to the scheme, hostname, and port used in the current request)
	 * and is ready for presentation to the user in the portal response. For
	 * example, you can take the return from this method, and express it as the
	 * <code>SRC=</code> attribute in an <code>&lt;IMG&gt;</code> tag.
	 * </p>
	 * <p>
	 * This method works like
	 * <code>getLocalizedFileURL(PortalContext,String)</code>, but where the
	 * localized filename string is taken from a message resource of the portal
	 * component, using the given key and default filename. Thus this method
	 * relies on the best-candidate filename for each locale already existing in
	 * the localized message properties for that locale, at the given key. (And
	 * if the key is not found, it applies the given default filename.) Thus the
	 * method just reads the filename from the message, assumes that file is the
	 * best one for that locale, and returns a URL for it accordingly.
	 * </p>
	 * <p>
	 * The default filename is an optional parameter; if left null or blank, and
	 * the key is not found in the available messages, then null is returned.
	 * Null is also returned if the portal context or key parameters were null.
	 * If the key was found in the available messages, but a file matching the
	 * message value does not exist in the current portal component, a URL
	 * pointing to the base filename will be returned anyway (of course, if a
	 * browser subsequently opens this URL, it will get an HTTP 404 error since
	 * the file does not exist).
	 * </p>
	 * <p>
	 * <b>Note:</b> As mentioned, this method relies on you having placed the
	 * proper localized filenames for each locale into the message resources for
	 * each locale. If you would rather not do that, and would rather just have
	 * the system inspect the available files in the resource bundle
	 * automatically to build the URL, use the companion
	 * <code>getLocalizedFileURL(PortalContext,String)</code> method.
	 * </p>
	 * 
	 * @param pContext
	 *            The portal context.
	 * @param pKey
	 *            The key for a message property containing the localized
	 *            filename.
	 * @param pDefault
	 *            The default filename to use if the message property does not
	 *            exist.
	 * @return A URL for downloading the best-fit localized version of the base
	 *         file from the portal component.
	 */
	public static String getLocalizedFileURL(PortalContext pContext,
			String pKey, String pDefault) {
		if (pContext == null || pKey == null) {
			return null;
		}
		pKey = pKey.trim();
		if (pKey.length() == 0) {
			return null;
		}
		if (pDefault != null) {
			pDefault = pDefault.trim();
			if (pDefault.length() == 0) {
				return null;
			}
		}
		// get the relative path for the current portal component
		Style thisStyleObject = getCurrentComponent(pContext);
		String relPath = thisStyleObject.getUrlSafeRelativePath();

		// get the absolute path for the current portal component - this is
		// the path to the location of its secondary support files
		String absPath = pContext.getPortalRequest().getSession()
				.getServletContext().getRealPath(relPath)
				+ "/";

		// get the localized filename from the message resources
		String fileName = I18nUtility.getValue(pKey, pDefault, pContext);

		// check if the file exists - if not, return its url anyway unless
		// message was not found or blank.
		// relative URL is just the portal context root path + the relative
		// path to the component + the localized filename (if not null)
		if (fileName != null && !fileName.equals(pKey)
				&& (fileName.length() > 0)) {
			fileName = slashify(fileName);
			String url = null;
			if (fileExists(absPath, fileName)) {
				url = slashify(pContext.getPortalHttpRoot() + "/" + relPath
						+ "/" + fileName);
			} else {
				url = slashify(pContext.getPortalHttpRoot() + "/" + relPath
						+ "/" + fileName);
			}
			// make sure the path includes the portal application context root
			String contextPath = pContext.getPortalRequest().getContextPath();
			if (!url.startsWith(contextPath)) {
				return slashify(contextPath + "/" + url);
			} else {
				return url;
			}
		}
		return null;
	}

	/**
	 * Indicates whether multiple locales are enabled for the portal site in the
	 * given portal request. This method just gets all of the enabled locales
	 * for that site, using the companion getAvailableLocales method, and
	 * returns true if there are more than one.
	 * 
	 * @param pReq
	 *            A portal request.
	 * @return True if multiple locales are enabled for the portal site in the
	 *         request.
	 */
	public static boolean multipleLocalesEnabled(HttpServletRequest pReq) {
		Collection locales = getAvailableLocales(pReq);
		if (locales == null || locales.size() < 2) {
			return false;
		}
		return true;
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * loaded against the current portal component; the returned string is
	 * localized for the current locale. This method is the same as calling the
	 * companion method,
	 * <code>getValue(String,String,String,Locale,Object[],GlobalHelpProvider[],ContextualHelpProvider[],boolean)</code>
	 * (see), where the component ID and locale are taken from the portal
	 * context, the key is passed-through, and the remaining parameters are set
	 * to null or false.
	 * </p>
	 * 
	 * @param pKey
	 *            A message key.
	 * @param pContext
	 *            The portal context, indicating the component and locale to
	 *            use.
	 * @return The message string, according to the above parameters.
	 */
	public static String getValue(String pKey, PortalContext pContext) {
		return getValue(pKey, null, pContext, null, null, null, false, false);
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * loaded against the current portal component; the returned string is
	 * localized for the current locale, and defaulted if needed with the given
	 * default message string. This method is the same as calling the companion
	 * method,
	 * <code>getValue(String,String,String,Locale,Object[],GlobalHelpProvider[],ContextualHelpProvider[],boolean)</code>
	 * (see), where the component ID and locale are taken from the portal
	 * context, the other given parameters are passed-through, and the remainder
	 * are set to null or false.
	 * </p>
	 * 
	 * @param pKey
	 *            A message key.
	 * @param pDefaultValue
	 *            A default message to return if the message string is not
	 *            found.
	 * @param pContext
	 *            The portal context, indicating the component and locale to
	 *            use.
	 * @return The message string, according to the above parameters.
	 */
	public static String getValue(String pKey, String pDefaultValue,
			PortalContext pContext) {
		return getValue(pKey, pDefaultValue, pContext, null, null, null, false,
				false);
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * loaded against the given component ID; the returned string is localized
	 * for the current locale, and defaulted with the given default message
	 * string if not found. This method is the same as calling the companion
	 * method,
	 * <code>getValue(String,String,String,Locale,Object[],GlobalHelpProvider[],ContextualHelpProvider[],boolean)</code>
	 * (see), where the locale is taken from the portal context, the other given
	 * parameters are passed-through, and the remainder are set to null or
	 * false.
	 * </p>
	 * 
	 * @param pUID
	 *            The portal component ID containing the message resources.
	 * @param pKey
	 *            A message key.
	 * @param pDefaultValue
	 *            A default message to return if the message string is not
	 *            found.
	 * @param pContext
	 *            The portal context, indicating the locale to use.
	 * @return The message string, according to the above parameters.
	 */
	public static String getValue(String pUID, String pKey,
			String pDefaultValue, PortalContext pContext) {
		return getValue(pUID, pKey, pDefaultValue, pContext, null, null, null,
				false, false);
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * loaded against the given component ID; the returned string is localized
	 * for the given locale, and defaulted with the given default message string
	 * if not found. This method is the same as calling the companion method,
	 * <code>getValue(String,String,String,Locale,Object[],GlobalHelpProvider[],ContextualHelpProvider[],boolean)</code>
	 * (see), where the given parameters are passed-through, and the remaining
	 * parameters are set to null or false.
	 * </p>
	 * 
	 * @param pUID
	 *            The portal component ID containing the message resources.
	 * @param pKey
	 *            A message key.
	 * @param pDefaultValue
	 *            A default message to return if the message string is not
	 *            found.
	 * @param pLocale
	 *            The locale to assume.
	 * @return The message string, according to the above parameters.
	 */
	public static String getValue(String pUID, String pKey,
			String pDefaultValue, Locale pLocale) {
		return getValue(pUID, pKey, pDefaultValue, pLocale, null, null, null,
				false, false);
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * loaded against the given component ID; the returned string is localized
	 * for the given locale, interpolated with the given parameters, and
	 * defaulted with the given default message string if not found. This method
	 * is the same as calling the companion method,
	 * <code>getValue(String,String,String,Locale,Object[],GlobalHelpProvider[],ContextualHelpProvider[],boolean)</code>
	 * (see), where the given parameters are passed-through, and the remaining
	 * parameters are set to null or false.
	 * </p>
	 * 
	 * @param pUID
	 *            The portal component ID containing the message resources.
	 * @param pKey
	 *            A message key.
	 * @param pDefaultValue
	 *            A default message to return if the message string is not
	 *            found.
	 * @param pLocale
	 *            The locale to assume.
	 * @param pArgs
	 *            An array of parameters to interpolate into the message string.
	 * @return The message string, according to the above parameters.
	 */
	public static String getValue(String pUID, String pKey,
			String pDefaultValue, Locale pLocale, Object[] pArgs) {
		return getValue(pUID, pKey, pDefaultValue, pLocale, pArgs, null, null,
				false, false);
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * loaded against the given component ID; the returned string is localized
	 * for the current locale, interpolated with the given parameters, and
	 * defaulted with the given default message string if not found. This method
	 * is the same as calling the companion method,
	 * <code>getValue(String,String,String,Locale,Object[],GlobalHelpProvider[],ContextualHelpProvider[],boolean)</code>
	 * (see), where the locale is taken from the portal context, the other given
	 * parameters are passed-through, and the remaining parameters are set to
	 * null or false.
	 * </p>
	 * 
	 * @param pUID
	 *            The portal component ID containing the message resources.
	 * @param pKey
	 *            A message key.
	 * @param pDefaultValue
	 *            A default message to return if the message string is not
	 *            found.
	 * @param pContext
	 *            The portal context, indicating the locale to use.
	 * @param pArgs
	 *            An array of parameters to interpolate into the message string.
	 * @return The message string, according to the above parameters.
	 */
	public static String getValue(String pUID, String pKey,
			String pDefaultValue, PortalContext pContext, Object[] pArgs) {
		return getValue(pUID, pKey, pDefaultValue, pContext, pArgs, null, null,
				false, false);
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * loaded against the current portal component; the returned string is
	 * localized for the current locale, interpolated with the given parameters,
	 * and defaulted if needed with the given default message string. This
	 * method is the same as calling the companion method,
	 * <code>getValue(String,String,String,Locale,Object[],GlobalHelpProvider[],ContextualHelpProvider[],boolean)</code>
	 * (see), where the component ID and locale are taken from the portal
	 * context, the other given parameters are passed-through, and the remaining
	 * parameters are set to null or false.
	 * </p>
	 * 
	 * @param pKey
	 *            A message key.
	 * @param pDefaultValue
	 *            A default message to return if the message string is not
	 *            found.
	 * @param pContext
	 *            The portal context, indicating the component and locale to
	 *            use.
	 * @param pArgs
	 *            An array of parameters to interpolate into the message string.
	 * @return The message string, according to the above parameters.
	 */
	public static String getValue(String pKey, String pDefaultValue,
			PortalContext pContext, Object[] pArgs) {
		return getValue(pKey, pDefaultValue, pContext, pArgs, null, null,
				false, false);
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * loaded against the current portal component; the returned string is
	 * localized for the current locale and interpolated with the given
	 * parameters. This method is the same as calling the companion method,
	 * <code>getValue(String,String,String,Locale,Object[],GlobalHelpProvider[],ContextualHelpProvider[],boolean)</code>
	 * (see), where the component ID and locale are taken from the portal
	 * context, the other given parameters are passed-through, and the remaining
	 * parameters are set to null or false.
	 * </p>
	 * 
	 * @param pKey
	 *            A message key.
	 * @param pContext
	 *            The portal context, indicating the component and locale to
	 *            use.
	 * @param pArgs
	 *            An array of parameters to interpolate into the message string.
	 * @return The message string, according to the above parameters.
	 */
	public static String getValue(String pKey, PortalContext pContext,
			Object[] pArgs) {
		return getValue(pKey, null, pContext, pArgs, null, null, false, false);
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * loaded against the current portal component; the returned string is
	 * localized for the current locale, interpolated with the given parameters,
	 * defaulted with the given default message string if not found, and
	 * HTML-escaped per the given boolean switch. This method is the same as
	 * calling the companion method,
	 * <code>getValue(String,String,String,Locale,Object[],GlobalHelpProvider[],ContextualHelpProvider[],boolean)</code>
	 * (see), where the component ID and locale are taken from the portal
	 * context.
	 * 
	 * @param pKey
	 *            A message key.
	 * @param pDefaultValue
	 *            A default message to return if the message string is not
	 *            found.
	 * @param pContext
	 *            The portal context, indicating the component and locale to
	 *            use.
	 * @param pArgs
	 *            An array of parameters to interpolate into the message string.
	 * @param gArgs
	 *            An array of global-help providers to interpolate into the
	 *            message string.
	 * @param cArgs
	 *            An array of contextual-help providers to interpolate into the
	 *            message string.
	 * @param pEscape
	 *            Whether to escape any HTML special characters found in the
	 *            final message string.
	 * @param pFilterSpan
	 *            Whether to remove any Vignette-generated
	 *            <code>&lt;SPAN&gt;</code> markup from the final message
	 *            string.
	 * @return The message string, according to the above parameters.
	 */
	public static String getValue(String pKey, String pDefaultValue,
			PortalContext pContext, Object[] pArgs, GlobalHelpProvider[] gArgs,
			ContextualHelpProvider[] cArgs, boolean pEscape, boolean pFilterSpan) {
		if (pDefaultValue == null) {
			pDefaultValue = pKey;
		}
		String value = pDefaultValue;
		if (pContext != null) {
			Style style = getCurrentComponent(pContext);
			if (style != null) {
				value = getValue(style.getUID(), pKey, pDefaultValue, pContext,
						pArgs, gArgs, cArgs, pEscape, pFilterSpan);
			}
		}
		return value;
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * loaded against the given component ID; the returned string is localized
	 * for the given locale, interpolated with the given parameters, defaulted
	 * with the given default message string if not found, and escaped or
	 * filtered as per the given boolean switches. This method uses both
	 * published and unpublished Vignette API's and goes beyond them to provide
	 * additional useful functionality (such as support for embedded contextual
	 * help).
	 * </p>
	 * <p>
	 * Selection of the particular localized message properties file is as per
	 * the Java standard behavior for ResourceBundle. Selection is based on the
	 * given locale. The message properties searched are the ones loaded into
	 * the portal component indicated by the given component ID.
	 * </p>
	 * 
	 * <p>
	 * Parameter substitution is supported. No parameter substitution is
	 * performed if the given parameters are empty or null, or the message
	 * string contains no parameter placeholders. Note there are 3 kinds of
	 * parameters: general string parameters, global help parameters, and
	 * contextual-help parameters:
	 * </p>
	 * <ul>
	 * <p>
	 * <li>General string parameters are provided via the Object array. The
	 * objects in this array must implement the toString method. They are
	 * populated, in order, into any placeholders in the message string, as per
	 * the Java-standard MessageFormat specification. Any extra parameters are
	 * ignored; any unfilled placeholders remain.</li>
	 * </p>
	 * <p>
	 * <li>Global-help parameters are provided via the GlobalHelpProvider
	 * array. These GlobalHelpProviders are used to populate any special
	 * global-help tokens (<code>&lt;Global_Help&gt;...&lt;/Global_Help&gt;</code>)
	 * found in the message string. Those tokens can be used to denote parts of
	 * the message string which are to be linked to the global help secondary
	 * page, where the global help is provided by the corresponding
	 * GlobalHelpProvider in the array.
	 * <ul>
	 * <li><code>&lt;Global_Help&gt;</code> tokens may not be nested.</li>
	 * <li>Each GlobalHelpProvider in the array, in order, provides the logic
	 * for populating the corresponding global-help token.</li>
	 * <li> The content surrounded by the global-help token is used as the link
	 * content for the GlobalHelpProvider. By the time you pass the
	 * GlobalHelpProvider to this method, you should already have used the other
	 * GlobalHelpProvider setters to set any other parameters for global-help.</li>
	 * <li> Any extra GlobalHelpProviders in the array beyond the number of
	 * <code>&lt;Global_Help&gt;</code> tokens in the string are ignored.</li>
	 * <li> Any unmated <code>&lt;Global_Help&gt;</code> tokens in the string
	 * beyond the number of GlobalHelpProviders are ignored.</li>
	 * <li> See the GlobalHelpProvider class hierarchy for more information.</li>
	 * </ul>
	 * </p>
	 * </ul>
	 * </p>
	 * <p>
	 * <li>Contextual-help parameters are provided via the
	 * ContextualHelpProvider array. These ContextualHelpProviders are used to
	 * populate any special contextual-help tokens (<code>&lt;Contextual_Help&gt;...&lt;/Contextual_Help&gt;</code>)
	 * found in the message string. Those tokens can be used to denote parts of
	 * the message string which are to be linked to contextual help, where the
	 * contextual help is provided by the corresponding ContextualHelpProvider
	 * in the array.
	 * <ul>
	 * <li><code>&lt;Contextual_Help&gt;</code> tokens may not be nested.</li>
	 * <li>Each ContextualHelpProvider in the array, in order, provides the
	 * logic for populating the corresponding contextual-help token.</li>
	 * <li> The content surrounded by the contextual-help token is used as the
	 * link content for the ContextualHelpProvider. By the time you pass the
	 * ContextualHelpProvider to this method, you should already have used the
	 * other ContextualHelpProvider setters to set the other parameters for
	 * contextual-help, such as help content.</li>
	 * <li> Any extra ContextualHelpProviders in the array beyond the number of
	 * <code>&lt;Contextual_Help&gt;</code> tokens in the string are ignored.</li>
	 * <li> Any unmated <code>&lt;Contextual_Help&gt;</code> tokens in the
	 * string beyond the number of ContextualHelpProviders are ignored.</li>
	 * <li> See the ContextualHelpProvider class hierarchy for more information.</li>
	 * </ul>
	 * </p>
	 * </ul>
	 * <p>
	 * If you enable the HTML-escaping switch, then any HTML special characters
	 * (eg <code>&lt;</code>) found in the final string (after parameter
	 * substitution) will be converted to their corresponding HTML character
	 * entities (eg <code>&amp;lt;</code>before returning to you.
	 * </p>
	 * <p>
	 * In some occasions, Vignette inserts
	 * <code>&lt;SPAN lang="<i>tag</i>"&gt;...&lt;/SPAN&gt;</code> markup
	 * around localized content. Even if this happens, normally it is not
	 * visible in the browser, and helps rendering by an assistive device (eg
	 * reader) the user may be using. But in some contexts (eg strings you are
	 * going to use within the HTML <code>&lt;TITLE&gt;</code> tag) it is
	 * visible in some browsers. So depending on your audience, you may want to
	 * remove this markup. Set the filter-span switch to true in that case.
	 * </p>
	 * <p>
	 * If the message string contains any special
	 * <code>&lt;No_Localization&gt;...&lt;/No_Localization&gt;</code> tokens,
	 * those are removed. (You can use these tokens in message properties to
	 * signal to translators that their contents should be untouched. Since the
	 * translators may not remove those tokens, they are stripped by this
	 * method.)
	 * </p>
	 * <p>
	 * The given default string (this too is HTML-escaped per the boolean
	 * switch) is returned if the message or its resource bundle is not found in
	 * the portlet application contxt. (If you gave a null default string, then
	 * the key itself is used.) This default is also returned if the required
	 * parameters (key and request) are themselves null.
	 * </p>
	 * 
	 * @param pUID
	 *            The portal component ID containing the message resources.
	 * @param pKey
	 *            A message key.
	 * @param pDefaultValue
	 *            A default message to return if the message string is not
	 *            found.
	 * @param pLocale
	 *            The locale to assume.
	 * @param pArgs
	 *            An array of parameters to interpolate into the message string.
	 * @param gArgs
	 *            An array of global-help providers to interpolate into the
	 *            message string.
	 * @param cArgs
	 *            An array of contextual-help providers to interpolate into the
	 *            message string.
	 * @param pEscape
	 *            Whether to escape any HTML special characters found in the
	 *            final message string.
	 * @param pFilterSpan
	 *            Whether to remove any Vignette-generated
	 *            <code>&lt;SPAN&gt;</code> markup from the final message
	 *            string.
	 * @return The message string, according to the above parameters.
	 */
	public static String getValue(String pUID, String pKey,
			String pDefaultValue, Locale pLocale, Object[] pArgs,
			GlobalHelpProvider[] gArgs, ContextualHelpProvider[] cArgs,
			boolean pEscape, boolean pFilterSpan) {
		if (pDefaultValue == null) {
			pDefaultValue = pKey;
		}
		if (pKey == null || pUID == null || pLocale == null) {
			return pDefaultValue;
		}

		// use Vignette unpublished API to get resource bundle for the component
		// (if not found, return default)
		LocalizedBundle localizedBundle = null;
		try {
			localizedBundle = LocalizedBundleManager.getInstance().getBundle(
					pUID, pLocale);
		} catch (Exception ex) {
			return pDefaultValue;
		}
		if (localizedBundle == null) {
			return pDefaultValue;
		}

		// use Vignette unpublished API get the message from the resource bundle
		String value = pDefaultValue;
		try {
			value = localizedBundle.getString(pKey, pDefaultValue);
			// filter unwanted tokens and markup
			value = filterNoLocalizationTokens(value);
			if (pFilterSpan) {
				value = filterSpan(value);
			}
			// substitute parameters
			value = MessageFormat.format(value, pArgs);
			GlobalHelpUtility g = new GlobalHelpUtility();
			value = g.parseGlobalHelpTokens(value, gArgs, pEscape);
			ContextualHelpUtility c = new ContextualHelpUtility();
			value = c.parseContextualHelpTokens(value, cArgs, pEscape);
			return value;
		} catch (Exception ex) {
		}
		return value;
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * loaded against the given component ID; the returned string is localized
	 * for the current locale, interpolated with the given parameters, defaulted
	 * with the given default message string if not found, and HTML-escaped per
	 * the given boolean switch. This method is the same as calling the
	 * companion method,
	 * <code>getValue(String,String,String,Locale,Object[],GlobalHelpProvider[],ContextualHelpProvider[],boolean)</code>
	 * (see), where the locale is taken from the portal context.
	 * </p>
	 * 
	 * @param pUID
	 *            The portal component ID containing the message resources.
	 * @param pKey
	 *            A message key.
	 * @param pDefaultValue
	 *            A default message to return if the message string is not
	 *            found.
	 * @param pContext
	 *            The portal context, indicating the locale to use.
	 * @param pArgs
	 *            An array of parameters to interpolate into the message string.
	 * @param gArgs
	 *            An array of global-help providers to interpolate into the
	 *            message string.
	 * @param cArgs
	 *            An array of contextual-help providers to interpolate into the
	 *            message string.
	 * @param pEscape
	 *            Whether to escape any HTML special characters found in the
	 *            final message string.
	 * @param pFilterSpan
	 *            Whether to remove any Vignette-generated
	 *            <code>&lt;SPAN&gt;</code> markup from the final message
	 *            string.
	 * @return The message string, according to the above parameters.
	 */
	public static String getValue(String pUID, String pKey,
			String pDefaultValue, PortalContext pContext, Object[] pArgs,
			GlobalHelpProvider[] gArgs, ContextualHelpProvider[] cArgs,
			boolean pEscape, boolean pFilterSpan) {
		if (pDefaultValue == null) {
			pDefaultValue = pKey;
		}
		String value = pDefaultValue;
		try {
			HttpServletRequest request = pContext.getPortalRequest()
					.getRequest();
			Locale locale = getLocale(request);
			return getValue(pUID, pKey, pDefaultValue, locale, pArgs, gArgs,
					cArgs, pEscape, pFilterSpan);
		} catch (Exception e) {
			return value;
		}
	}

	/**
	 * Returns the given message string, with any HTML <code>&lt;SPAN&gt;</code>
	 * markup removed. Vignette may insert these automatically into message
	 * strings, so this message can be called to remove them.
	 * 
	 * @param msg
	 *            A message string.
	 * @return The message string with <code>&lt;SPAN&gt;</code> markup
	 *         removed.
	 */
	public static String filterSpan(String msg) {
		if (msg == null) {
			return null;
		}
		msg = msg.replaceAll(OPEN_SPAN_REGEX, "");
		return msg.replaceAll(CLOSE_SPAN_REGEX, "");
	}

	/**
	 * Get the current portal component.
	 * 
	 * @param pContext
	 *            Current portal context.
	 * @return Current portal component.
	 */
	private static Style getCurrentComponent(PortalContext pContext) {
		Style thisStyleObject = pContext.getCurrentStyle();
		if (thisStyleObject == null) {
			thisStyleObject = pContext.getCurrentSecondaryPage();
		}
		return thisStyleObject;
	}
}
