/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 * 
 * Common internationalization/localization utility for portlet framework.
 */
package com.hp.it.spf.xa.i18n.portlet;

import java.util.Locale;
import java.util.ResourceBundle;
import java.net.MalformedURLException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.hp.it.spf.xa.misc.portlet.Utils;
import com.hp.it.spf.xa.help.ContextualHelpProvider;
import com.hp.it.spf.xa.help.ContextualHelpUtility;
import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;

/**
 * <p>
 * A class of useful internationalization methods for use by portlets. These
 * methods include:
 * </p>
 * <ul>
 * <li>
 * <p>
 * Wrapper methods for Spring message resources (such as
 * {@link #getMessage(PortletRequest,String)}, which include SPF features such
 * as injection of contextual help into
 * <code>&lt;contextual_help&gt;...&lt;/contextual_help&gt;</code> markup, and
 * cleanup of any
 * <code>&lt;no_localization&gt;...&lt;/no_localization&gt;</code> markup
 * which the translator did not remove. These methods let you get localized
 * message strings using Spring, with the added SPF functionality.
 * </p>
 * </li>
 * <li>
 * <p>
 * Methods (like
 * {@link #getLocalizedFileURL(PortletRequest, PortletResponse, String)} to
 * generate a URL string pointing to a localized static resource, such as an
 * image. These methods let your portlet use static resource URL's for localized
 * resources - the resources may be kept either inside your portlet WAR, or
 * outside it, in the <i>portlet resource bundle folder</i>, or in a subfolder
 * thereof. In either case, these methods return encoded portlet URLs, ready to
 * surface in your portlet renderings (eg JSP's) - for example, you can take the
 * return from these methods and express it inside an HTML
 * <code>&lt;IMG SRC="..."&gt;</code> tag. (<b>Note:</b>: your portlet WAR
 * must also include and deploy the file relay servlet (see
 * {@link com.hp.it.spf.xa.relay.servlet.RelayServlet} if your resources are in
 * the portlet bundle folder, so that the URL's returned by these methods are
 * actually serviced when opened.)
 * </p>
 * </li>
 * <li>
 * <p>
 * Methods (like {@link #getLocalizedFileStream(PortletRequest, String)} which
 * do the same as the above, except returning an input stream to you instead of
 * a URL string.
 * </p>
 * </li>
 * <li>
 * <p>
 * And more.
 * </p>
 * </li>
 * </ul>
 * <p>
 * In addition, the portlet <code>I18nUtility</code> inherits many useful
 * methods from its superclass, the common
 * {@link com.hp.it.spf.xa.i18n.I18nUtility} (see).
 * </p>
 * 
 * @author <link href="liping.yan@hp.com">Liping Yan</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see com.hp.it.spf.xa.i18n.I18nUtility
 */

public class I18nUtility extends com.hp.it.spf.xa.i18n.I18nUtility {

	/**
	 * The name of the property file which stores internationalization
	 * configuration for the portlet framework. (The file extension .properties
	 * is assumed by the
	 * {@link com.hp.it.spf.xa.properties.PropertyResourceBundleManager}.)
	 */
	public static final String PORTLET_I18N_CONFIG_FILE = "i18n_portlet_config";

	/**
	 * Various properties in the portlet internationalization configuration
	 * file. See file for definition.
	 */
	public static final String PORTLET_I18N_CONFIG_PROP_BUNDLE_DIR = "portlet.bundleDirectory";
	public static final String PORTLET_I18N_CONFIG_PROP_RELAY_URL = "servlet.fileRelay.url";

	/**
	 * The default URL for the relay servlet.
	 */
	public static final String RELAY_PATH_DEFAULT = "/relay";

	/**
	 * The default portlet resource bundle directory.
	 */
	public static final String BUNDLE_DIR_DEFAULT = "/opt/sasuapps/spf/globalResources/portlet/i18n";

	/**
	 * Logger instance.
	 */
	private static Log logger = LogFactory.getLog(I18nUtility.class);

	/**
	 * Relay servlet name (actual value is configured in property file).
	 */
	private static String relayServletURL = RELAY_PATH_DEFAULT;

	/**
	 * Portlet resource bundle directory (actual value is configured in property
	 * file).
	 */
	private static String resourceBundleDir = BUNDLE_DIR_DEFAULT;

	/**
	 * Static block to initialize relay servlet info and portlet resource bundle
	 * directory from configuration file.
	 */
	static {
		ResourceBundle bundle = PropertyResourceBundleManager
				.getBundle(PORTLET_I18N_CONFIG_FILE);
		if (bundle == null) {
			logger.warn("I18nUtility: Failed to open "
					+ PORTLET_I18N_CONFIG_FILE + " file");
			relayServletURL = RELAY_PATH_DEFAULT;
			resourceBundleDir = BUNDLE_DIR_DEFAULT;
		} else {
			try {
				resourceBundleDir = "/"
						+ bundle.getString(PORTLET_I18N_CONFIG_PROP_BUNDLE_DIR)
						+ "/";
			} catch (Exception ex) { // error if prop not exist
				logger
						.warn("I18nUtility: property "
								+ PORTLET_I18N_CONFIG_PROP_BUNDLE_DIR
								+ " not found in " + PORTLET_I18N_CONFIG_FILE
								+ " file");
				logger.warn("I18nUtility: " + ex.getMessage());
				resourceBundleDir = BUNDLE_DIR_DEFAULT;
			}
			try {
				relayServletURL = "/"
						+ bundle.getString(PORTLET_I18N_CONFIG_PROP_RELAY_URL)
						+ "/";
				relayServletURL = slashify(relayServletURL);
			} catch (Exception ex) {
				logger.warn("I18nUtility: property "
						+ PORTLET_I18N_CONFIG_PROP_RELAY_URL + " not found in "
						+ PORTLET_I18N_CONFIG_FILE + " file");
				logger.warn("I18nUtility: " + ex.getMessage());
				relayServletURL = RELAY_PATH_DEFAULT;
			}
		}
		resourceBundleDir = slashify(resourceBundleDir);
		relayServletURL = slashify(relayServletURL);
	}

	/**
	 * Private to prevent external construction.
	 */
	private I18nUtility() {

	}

	/**
	 * <p>
	 * Returns an input stream for a proper localized version of the given base
	 * filename of a bundle of portlet resources. The filename must be a base
	 * filename for those resources. The bundle may be located in the <i>portlet
	 * resource bundle folder</i> on the portlet server (eg, outside of the
	 * portlet application). Alternatively, the bundle may be inside the portlet
	 * WAR, as a set of static resources. In either case, the given base
	 * filename may include a subfolder relative to the portlet resource bundle
	 * folder or portlet application root, respectively. (Note that the path to
	 * the portlet resource bundle folder is configured in
	 * <code>i18n_portlet_config.properties</code> and
	 * {@link #BUNDLE_DIR_DEFAULT} is the default.)
	 * </p>
	 * <p>
	 * This method uses the locale from the given portlet request to find the
	 * best-candidate localized version of the given file in the resource
	 * bundle. The logic for finding the best-candidate is the same as that used
	 * by the Java-standard ResourceBundle class. Thus the input stream returned
	 * by this method will come from the best-candidate locale-tagged filename,
	 * or the given base filename if there is no better fit. If the resource
	 * bundle does not exist at all (note that on a case-sensitive filesystem,
	 * the lookup into the portlet resource bundle folder is a case-sensitive
	 * lookup), the method returns null. This method also returns null if any of
	 * its parameters are null.
	 * </p>
	 * <p>
	 * For example: in the portlet resource bundle folder consider that we have
	 * the files <code>html/foo.htm</code>, <code>html/foo_fr_CA.htm</code>,
	 * and <code>html/foo_fr.htm</code>. Also, inside the portlet
	 * application, consider that we have the files <code>html/bar.htm</code>
	 * and <code>html/bar_it.htm</code>. Then:
	 * </p>
	 * 
	 * <dl>
	 * <dt><code>getLocalizedFileStream(request, "html/foo.htm")</code> when
	 * <code>request</code> contains Canada French (<code>Locale.CANADA_FRENCH</code>)</dt>
	 * <dd>returns a file input stream for
	 * <code><i>portlet-resource-folder</i>/html/foo_fr_CA.htm</code></dd>
	 * 
	 * <dt><code>getLocalizedFileStream(request, "/html/foo.htm")</code> when
	 * <code>request</code> contains France French (<code>Locale.FRANCE</code>)</dt>
	 * <dd>returns a file input stream for
	 * <code><i>portlet-resource-folder</i>/html/foo_fr.htm</code></dd>
	 * 
	 * <dt><code>getLocalizedFileStream(request, "html/foo.htm")</code> when
	 * <code>request</code> contains generic Italian (<code>Locale.ITALIAN</code>)</dt>
	 * <dd>returns a file input stream for
	 * <code><i>portlet-resource-folder</i>/html/foo.htm</code></dd>
	 * </dt>
	 * 
	 * <dt><code>getLocalizedFileStream(request, "html/bar.htm")</code> when
	 * <code>request</code> contains generic Italian (<code>Locale.ITALIAN</code>)</dt>
	 * <dd>returns an input stream for <code>/html/bar_it.htm</code> in the
	 * portlet application.</dd>
	 * 
	 * <dt><code>getLocalizedFileStream(request, "html/bar.htm")</code> when
	 * <code>request</code> contains generic French (<code>Locale.FRENCH</code>)</dt>
	 * <dd>returns an input stream for <code>/html/bar.htm</code> in the
	 * portlet application.</dd>
	 * 
	 * <dt><code>getLocalizedFileStream(request, "html/foobar.htm")</code></dt>
	 * <dd>returns <code>null</code></dd>
	 * </dl>
	 * 
	 * <p>
	 * <b>Note:</b> On case-sensitive filesystems, lowercase is assumed for the
	 * langage and variant codes, and uppercase is assumed for the country code.
	 * Thus in the above examples, different results from the portlet resource
	 * bundle folder would obtain if <code>foo_fr.htm</code> and/or
	 * <code>foo_fr_CA.htm</code> were tagged with uppercase language or
	 * lowercase country. Be sure your resource bundles follow the convention.
	 * </p>
	 * 
	 * @param pReq
	 *            The portlet request.
	 * @param pBaseFileName
	 *            The name of the base file to search.
	 * @return An input stream for the best-candidate localized file, or null if
	 *         none was found.
	 * 
	 */
	public static InputStream getLocalizedFileStream(PortletRequest pReq,
			String pBaseFileName) {
		return getLocalizedFileStream(pReq, pBaseFileName, true);
	}

	/**
	 * <p>
	 * Returns an input stream for the given version of the given portlet
	 * resource file (ie, localized to best-fit the locale in the given request,
	 * or not, per the boolean switch). If the boolean switch is set to
	 * <code>true</code>, this method works exactly like
	 * {@link #getLocalizedFileStream(PortletRequest,String)} (see). If the
	 * boolean switch is set to <code>false</code>, this method does not
	 * actually localize the given filename. Instead, the returned input stream
	 * points to the base filename if it existed, and null otherwise.
	 * </p>
	 * 
	 * @param pReq
	 *            The portlet request.
	 * @param pBaseFileName
	 *            The name of the base file to search.
	 * @param pLocalized
	 *            The version of the file for which to search: the base file
	 *            (false) or the best-fitting localized file (true).
	 * @return An input stream for the best-candidate localized file, or null if
	 *         none was found.
	 */
	public static InputStream getLocalizedFileStream(PortletRequest pReq,
			String pBaseFileName, boolean pLocalized) {
		return getLocalizedFileStream(pReq, pBaseFileName, null, pLocalized);
	}

	/**
	 * <p>
	 * Returns an input stream for a proper localized version for the given
	 * locale of the given base filename of a bundle of portlet resources. This
	 * method works exactly like
	 * {@link #getLocalizedFileStream(PortletRequest,String,boolean)} (see)
	 * except that the given locale is used instead. (If the given locale is
	 * null, then the locale inside the portlet request is used.)
	 * </p>
	 * 
	 * @param pReq
	 *            The portlet request.
	 * @param pBaseFileName
	 *            The name of the base file to search.
	 * @param pLocale
	 *            The locale to use (if null, uses the locale in the request).
	 * @param pLocalized
	 *            The version of the file for which to search: the base file
	 *            (false) or the best-fitting localized file (true).
	 * @return An input stream for the best-candidate localized file, or null if
	 *         none was found.
	 */
	public static InputStream getLocalizedFileStream(PortletRequest pReq,
			String pBaseFileName, Locale pLocale, boolean pLocalized) {

		if (resourceBundleDir == null || pReq == null || pBaseFileName == null) {
			return null;
		}
		pBaseFileName = pBaseFileName.trim();
		pBaseFileName = slashify(pBaseFileName);
		if (pBaseFileName.length() == 0) {
			return null;
		}
		if (pLocale == null) {
			pLocale = pReq.getLocale();
		}
		PortletContext pContext = pReq.getPortletSession().getPortletContext();

		// Get the localized filename by looking in the portlet resource bundle
		// folder.
		String fileName = getLocalizedFileName(pBaseFileName, pLocale,
				pLocalized);

		// If the localized filename was found, make and return an input stream
		// for it. Otherwise, look for the localized filename inside the portlet
		// application. If we find it there, make and return an input stream for
		// it. Finally, return null if still it was not found.
		if (fileName != null) {
			fileName = slashify(resourceBundleDir + "/" + fileName);
			try {
				return new FileInputStream(fileName);
			} catch (FileNotFoundException e) { // should never happen
			}
		}
		fileName = getLocalizedFileName(pReq, pBaseFileName, pLocalized);
		if (fileName != null) {
			return pContext.getResourceAsStream(fileName);
		}
		return null;
	}

	/**
	 * <p>
	 * Returns an input stream for a localized portlet resource bundle file,
	 * where the actual filename is retrieved (properly localized) from the
	 * portlet's message resources using the given key and default. The file
	 * itself may be located in the portlet resource bundle folder on the
	 * portlet server (eg, outside of the portlet application). Alternatively,
	 * the file may be inside the portlet application, as a static resource. In
	 * either case, the filename in the message resource may include a subfolder
	 * relative to the <i>portlet resource bundle folder</i> or portlet
	 * application root, respectively. (Note that the path to the portlet
	 * resource bundle folder is configured in
	 * <code>i18n_portlet_config.properties</code> and
	 * {@link #BUNDLE_DIR_DEFAULT} is the default.)
	 * </p>
	 * <p>
	 * This method works like
	 * {@link #getLocalizedFileStream(PortletRequest,String)}</code>, but
	 * where the localized filename string is taken from a message resource of
	 * the portlet, using the given key and default filename. Thus this method
	 * relies on the best-candidate filename for each locale already existing in
	 * the localized message properties for that locale, at the given key. (And
	 * if the key is not found, it applies the given default filename.) Thus the
	 * method just reads the filename from the message, assumes that file is the
	 * best one for that locale, and returns a path for it accordingly.
	 * </p>
	 * <p>
	 * The default filename is an optional parameter; if left null or blank, and
	 * the key is not found in the available messages, then null is returned.
	 * Null is also returned if the file named in the message resource does not
	 * actually exist, or if the portlet request or key parameters were null.
	 * </p>
	 * <p>
	 * <b>Note:</b> As mentioned, this method relies on you having placed the
	 * proper localized filenames for each locale into the message resources for
	 * each locale. If you would rather not do that, and would rather just have
	 * the system inspect the available files in the resource bundle
	 * automatically, use the companion
	 * {@link #getLocalizedFileStream(PortletRequest,String)}</code> method.
	 * </p>
	 * 
	 * @param pReq
	 *            The portlet request.
	 * @param pKey
	 *            The key for a message property containing the localized
	 *            filename.
	 * @param pDefault
	 *            The default filename to use if the message property does not
	 *            exist.
	 * @return The absolute pathname of the best-candidate localized file in the
	 *         portlet bundle folder, or null if no qualifying file was found.
	 */
	public static InputStream getLocalizedFileStream(PortletRequest pReq,
			String pKey, String pDefault) {
		return getLocalizedFileStream(pReq, pKey, pDefault, null);
	}

	/**
	 * <p>
	 * Returns an input stream for a localized portlet resource bundle file,
	 * where the actual filename is retrieved (properly localized) from the
	 * portlet's message resources using the given key, default, and locale.
	 * </p>
	 * <p>
	 * This method works exactly like
	 * {@link #getLocalizedFileStream(PortletRequest,String,String)} except that
	 * the locale used is the given locale. (If the given locale is null, then
	 * the locale inside the given request is used.)
	 * </p>
	 * 
	 * @param pReq
	 *            The portlet request.
	 * @param pKey
	 *            The key for a message property containing the localized
	 *            filename.
	 * @param pDefault
	 *            The default filename to use if the message property does not
	 *            exist.
	 * @param pLocale
	 *            The locale to use (if null, uses the locale inside the
	 *            request).
	 * @return The absolute pathname of the best-candidate localized file in the
	 *         portlet bundle folder, or null if no qualifying file was found.
	 */
	public static InputStream getLocalizedFileStream(PortletRequest pReq,
			String pKey, String pDefault, Locale pLocale) {
		if (resourceBundleDir == null || pReq == null || pKey == null) {
			return null;
		}
		pKey = pKey.trim();
		if (pKey.length() == 0) {
			return null;
		}
		if (pDefault != null) {
			pDefault = pDefault.trim();
			if (pDefault.length() == 0) {
				pDefault = null;
			}
		}
		if (pLocale == null) {
			pLocale = pReq.getLocale();
		}
		PortletContext pContext = pReq.getPortletSession().getPortletContext();

		// Get the localized filename from the message resources
		String fileName = getMessage(pReq, pKey, pDefault, null, null, pLocale,
				false);
		if (fileName != null)
			fileName = fileName.trim();

		// Check if the localized filename message was found (ie is not null and
		// not equal to the key). If so, then return an input stream for that.
		// But if the file was not found, or the message was not found, return
		// an input stream for the file inside the portlet application. This
		// will return null if the file still was not found.
		if (fileName != null && !fileName.equals(pKey)
				&& (fileName.length() > 0)) {
			fileName = slashify(resourceBundleDir + "/" + fileName);
			try {
				return new FileInputStream(fileName);
			} catch (FileNotFoundException e) {
				return pContext.getResourceAsStream(fileName);
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Returns a URL (suitable for presentation to the user) for downloading a
	 * localized portlet resource bundle file for the given base filename, where
	 * the localized filename is obtained by inspecting the contents of the
	 * system. The returned URL is ready for presentation to the user in the
	 * portlet response; it does not need to be encoded or rewritten. For
	 * example, you can take the return from this method, and express it as the
	 * <code>SRC</code> attribute in an HTML <code>&lt;IMG&gt;</code> tag.
	 * </p>
	 * <p>
	 * The given filename must be a base filename for a bundle of portlet
	 * resources. The base file, and its localized variants, may be located in
	 * the <i>portlet resource bundle folder</i> on the portlet server. In this
	 * case, the given filename may include a subfolder relative to that portlet
	 * resource bundle folder. (The path to the portlet resource bundle folder
	 * is configured in <code>i18n_portlet_config.properties</code>;
	 * {@link #BUNDLE_DIR_DEFAULT} is the default.) In this case, the returned
	 * URL will be a file-relay URL pointing to that particular localized file
	 * in the portlet resource bundle folder.
	 * </p>
	 * <p>
	 * Alternatively, the base and localized files may be located inside your
	 * portlet application, among the static resources. The given base filename,
	 * in this case, should include a folder (relative to the application root)
	 * where the image is located. In this case, the returned URL will be an
	 * encoded static resource URL for the particular localized file in the
	 * portlet application.
	 * </p>
	 * <p>
	 * This method uses the locale from the given portlet request to find the
	 * best-candidate localized file in the resource bundle (ie, in the set of
	 * base and localized files for the given base filename). The logic for
	 * finding the best-candidate is the same as that used by the Java-standard
	 * {@link ResourceBundle} class. Thus the URL returned by this method will
	 * point to the best-candidate locale-tagged filename, or the given base
	 * filename if there is no better fit. If the resource bundle does not exist
	 * at all (note that on a case-sensitive filesystem, this is a
	 * case-sensitive lookup), the method returns a URL referencing the base
	 * filename as a static resource inside the portlet application. (This will,
	 * of course, be a URL which points to a missing resource, causing an HTTP
	 * 404 error if opened.)
	 * </p>
	 * <p>
	 * For example: in the portlet resource bundle folder consider that we have
	 * the files <code>html/foo.htm</code>, <code>html/foo_fr_CA.htm</code>,
	 * and <code>html/foo_fr.htm</code>. Also consider that we have, in the
	 * portlet application, the files <code>html/bar.htm</code> and
	 * <code>html/bar_it.htm</code>. Then:
	 * </p>
	 * <dl>
	 * <dt><code>getLocalizedFileURL(request, response, "html/foo.htm")</code>
	 * when <code>request</code> contains Canada French (<code>Locale.CANADA_FRENCH</code>)</dt>
	 * <dd>returns a file-relay URL for <code>html/foo_fr_CA.htm</code> in
	 * the portlet resource bundle folder.</dd>
	 * 
	 * <dt><code>getLocalizedFileURL(request, response, "/html/foo.htm")</code>
	 * when <code>request</code> contains France French (<code>Locale.FRANCE</code>)</dt>
	 * <dd>returns a file-relay URL for <code>html/foo_fr.htm</code> in the
	 * portlet resource bundle folder.</dd>
	 * 
	 * <dt><code>getLocalizedFileURL(request, response, "html/foo.htm")</code>
	 * when <code>request</code> contains generic Italian (<code>Locale.ITALIAN</code>)</dt>
	 * <dd>returns a file-relay URL for <code>html/foo.htm</code> in the
	 * portlet resource bundle folder.</dd>
	 * 
	 * <dt><code>getLocalizedFileURL(request, response, "/html/bar.htm")</code>
	 * when <code>request</code> contains generic Italian (<code>Locale.ITALIAN</code>)</dt>
	 * <dd>returns an encoded resource URL for <code>/html/bar.htm</code>
	 * inside the portlet application.</dd>
	 * 
	 * <dt><code>getLocalizedFileURL(request, response, "html/foobar.htm")</code></dt>
	 * <dd>returns an encoded resource URL for <code>/html/foobar.htm</code>
	 * inside the portlet application. Since that file does not actually exist,
	 * the browser will get an HTTP 404 when requesting this URL.</dd>
	 * </dl>
	 * <p>
	 * <b>Note:</b> On case-sensitive filesystems, lowercase is assumed for the
	 * langage and variant codes, and uppercase is assumed for the country code.
	 * Thus in the above examples, different results would obtain if
	 * <code>foo_fr.htm</code> and/or <code>foo_fr_CA.htm</code> were tagged
	 * with uppercase language or lowercase country. Be sure your resource
	 * bundles follow the convention.
	 * </p>
	 * <p>
	 * <b>Note:</b> This method uses the
	 * {@link #getLocalizedFileName(PortletRequest, String, Locale, boolean)}
	 * method to determine the proper best-candidate localized file for the
	 * given filename, looking in the portlet resource bundle directory first,
	 * and the portlet application second. It also may use the
	 * {@link #getFileRelayURL(PortletRequest, PortletResponse, String)} method
	 * to generate a file-relay URL for it (ie, if the portlet resource bundle
	 * folder is where the file was found). Please see those other methods for
	 * additional documentation.
	 * </p>
	 * <p>
	 * <b>Note:</b> As mentioned, this method automatically discovers the
	 * best-fit localized file for the locale. An alternative technique for
	 * generating a localized file URL relies on the name of the best-fit
	 * localized file already being stored in a message resource. If you would
	 * rather generate your localized file URL that way, you should use the
	 * companion
	 * {@link #getLocalizedFileURL(PortletRequest, PortletResponse, String, String)}
	 * method instead of this one.
	 * </p>
	 * 
	 * @param pReq
	 *            The portlet request.
	 * @param pResp
	 *            The portlet response.
	 * @param pBaseFileName
	 *            The name of the base file to search.
	 * @return A URL for downloading the best-fit localized version of the base
	 *         file.
	 */
	public static String getLocalizedFileURL(PortletRequest pReq,
			PortletResponse pResp, String pBaseFileName) {
		return getLocalizedFileURL(pReq, pResp, pBaseFileName, true);
	}

	/**
	 * <p>
	 * Returns a URL (suitable for presentation to the user) for downloading the
	 * given version of the given portlet resource bundle base filename (ie,
	 * localized to best-fit the locale in the given request, or not, per the
	 * boolean switch). The returned URL is ready for presentation to the user
	 * in the portlet response; it does not need to be encoded or rewritten. For
	 * example, you can take the return from this method, and express it as the
	 * <code>SRC</code> attribute in an HTML <code>&lt;IMG&gt;</code> tag.
	 * </p>
	 * <p>
	 * If the given boolean switch is set to <code>true</code>, this method
	 * works exactly like
	 * {@link #getLocalizedFileURL(PortletRequest, PortletResponse, String)}
	 * (see). If the boolean switch is set to <code>false</code>, this method
	 * works mostly like the other one, but it does not actually localize the
	 * returned filename. Instead, the returned URL just references the base
	 * filename wherever that file was found (assumed to be in the portlet
	 * application, by default).
	 * </p>
	 * 
	 * @param pReq
	 *            The portlet request.
	 * @param pResp
	 *            The portlet response.
	 * @param pBaseFileName
	 *            The name of the base file to search.
	 * @param pLocalized
	 *            The version of the file for which to search: the base file
	 *            (false) or the best-fitting localized file (true).
	 * @return A URL for downloading the best-fit localized version of the base
	 *         file.
	 * 
	 */
	public static String getLocalizedFileURL(PortletRequest pReq,
			PortletResponse pResp, String pBaseFileName, boolean pLocalized) {

		return getLocalizedFileURL(pReq, pResp, pBaseFileName, null, pLocalized);
	}

	/**
	 * <p>
	 * Returns a URL (suitable for presentation to the user) for downloading a
	 * localized portlet resource bundle file for the given base filename and
	 * locale, where the localized filename is obtained by inspecting the
	 * contents of the system. The returned URL is ready for presentation to the
	 * user in the portlet response; it does not need to be encoded or
	 * rewritten. For example, you can take the return from this method, and
	 * express it as the <code>SRC</code> attribute in an HTML
	 * <code>&lt;IMG&gt;</code> tag.
	 * </p>
	 * <p>
	 * This method works exactly like
	 * {@link #getLocalizedFileURL(PortletRequest, PortletResponse, String, boolean)}
	 * except it uses the given locale instead of the locale inside the
	 * {@link PortletRequest}. (If the given locale is null, it does use the
	 * value in the <code>PortletRequest</code>.)
	 * </p>
	 * 
	 * @param pReq
	 *            The portlet request.
	 * @param pResp
	 *            The portlet response.
	 * @param pBaseFileName
	 *            The name of the base file to search.
	 * @param pLoc
	 *            The locale for which to search (if null, the method uses the
	 *            locale in the request).
	 * @param pLocalized
	 *            The version of the file for which to search: the base file
	 *            (false) or the best-fitting localized file (true).
	 * @return A URL for downloading the best-fit localized version of the base
	 *         file.
	 */
	public static String getLocalizedFileURL(PortletRequest pReq,
			PortletResponse pResp, String pBaseFileName, Locale pLoc,
			boolean pLocalized) {

		if (resourceBundleDir == null || pReq == null || pResp == null
				|| pBaseFileName == null) {
			return null;
		}
		pBaseFileName = pBaseFileName.trim();
		pBaseFileName = slashify(pBaseFileName);
		if (pBaseFileName.length() == 0) {
			return null;
		}
		if (pLoc == null) {
			pLoc = pReq.getLocale();
		}

		// Get the localized filename by looking in the portlet resource bundle
		// folder.
		String fileName = getLocalizedFileName(pBaseFileName, pLoc, pLocalized);

		// If the localized filename was found, make and return a file-relay URL
		// for it. Otherwise, look for the localized filename inside the portlet
		// application. If we find it there, make and return an encoded resource
		// URL pointing to it. If still we do not find it, make and return an
		// encoded resource URL pointing to the given base filename inside the
		// portlet application, by default.
		if (fileName != null) {
			return getFileRelayURL(pReq, pResp, fileName);
		} else {
			fileName = getLocalizedFileName(pReq, pBaseFileName, pLocalized);
			if (fileName != null) {
				return pResp.encodeURL(fileName);
			} else {
				return pResp.encodeURL(pBaseFileName);
			}
		}
	}

	/**
	 * <p>
	 * Returns a URL (suitable for presentation to the user) for downloading a
	 * localized portlet resource bundle file, where the filename is retrieved
	 * (properly localized) from the portlet's message resources using the given
	 * key and default. The returned URL is ready for presentation to the user
	 * in the portlet response; it does not need to be encoded or rewritten. For
	 * example, you can take the return from this method, and express it as the
	 * <code>SRC</code> attribute in an HTML <code>&lt;IMG&gt;</code> tag.
	 * </p>
	 * <p>
	 * This method works like
	 * {@link #getLocalizedFileURL(PortletRequest, PortletResponse, String)},
	 * but in which the localized filename is instead gotten from a message
	 * resource of the portlet, using the given key and default filename. Thus
	 * this method relies on the best-candidate filename for each locale already
	 * existing in the localized message properties for that locale, at the
	 * given key. (And if the key is not found, it applies the given default
	 * filename.) Thus the method just reads the filename from the message,
	 * assumes that file is the best one for that locale, and returns a URL for
	 * it accordingly.
	 * </p>
	 * <p>
	 * The file named in the message resource may be located in the <i>portlet
	 * resource bundle folder</i> on the portlet server. The filename from the
	 * message resource may include a subfolder relative to that portlet
	 * resource bundle folder. The path to the portlet resource bundle folder is
	 * configured in <code>i18n_portlet_config.properties</code> ({@link #BUNDLE_DIR_DEFAULT}
	 * is the default). In this case, the returned URL is a file-relay URL
	 * pointing to that file in the portlet resource bundle directory.
	 * </p>
	 * <p>
	 * Alternatively, the file named in the message resource may be located
	 * inside your portlet application, among the static resources. The filename
	 * from the message resource, in this case, should include a folder
	 * (relative to the applciation root) where the image is located. In this
	 * case, the returned URL is an encoded resource URL pointing to that file
	 * inside the portlet application.
	 * </p>
	 * <p>
	 * The default filename is an optional parameter; if left null or blank, and
	 * the key is not found in the available messages, then null is returned.
	 * Null is also returned if the portlet request, response, or key parameters
	 * were null. If the key is found in the available messages, but the file
	 * named in that message does not actually exist in either the portlet
	 * resource bundle folder or the portlet application, then a URL referencing
	 * the missing resource is returned anyway (and the brower will get an HTTP
	 * 404 error when opening that URL).
	 * </p>
	 * <p>
	 * <b>Note:</b> This method uses the
	 * {@link #getMessage(PortletRequest, String, String)} method to determine
	 * the proper filename. Then (if the file exists in the portlet resource
	 * bundle folder), it may use the
	 * {@link #getFileRelayURL(PortletRequest, PortletResponse, String)} method
	 * to generate a file-relay URL for it. Please see those other methods for
	 * additional documentation.
	 * </p>
	 * <p>
	 * <b>Note:</b> As mentioned, this method relies on you having placed the
	 * proper localized filenames for each locale into the message resources for
	 * each locale. If you would rather not do that, and would rather just have
	 * the system inspect the available files in the resource bundle
	 * automatically to build the URL, use the companion
	 * {@link #getLocalizedFileURL(PortletRequest, PortletResponse, String)}
	 * method instead of this one.
	 * </p>
	 * 
	 * @param pReq
	 *            The portlet request.
	 * @param pResp
	 *            The portlet response.
	 * @param pKey
	 *            The key for a message property containing the localized
	 *            filename.
	 * @param pDefault
	 *            The default filename to use if the message property does not
	 *            exist.
	 * @return A URL for downloading the best-fit localized version of the base
	 *         file.
	 */
	public static String getLocalizedFileURL(PortletRequest pReq,
			PortletResponse pResp, String pKey, String pDefault) {
		return getLocalizedFileURL(pReq, pResp, pKey, pDefault, null);
	}

	/**
	 * <p>
	 * Returns a URL (suitable for presentation to the user) for downloading a
	 * localized portlet resource bundle file, where the filename is retrieved
	 * (properly localized) from the portlet's message resources using the given
	 * key, default, and locale. The returned URL is ready for presentation to
	 * the user in the portlet response; it does not need to be encoded or
	 * rewritten. For example, you can take the return from this method, and
	 * express it as the <code>SRC</code> attribute in an HTML
	 * <code>&lt;IMG&gt;</code> tag.
	 * </p>
	 * <p>
	 * This method works like
	 * {@link #getLocalizedFileURL(PortletRequest, PortletResponse, String, String)},
	 * but the locale to use is taken from the parameters rather than the
	 * {@link PortletRequest}. (The locale inside the
	 * <code>PortletRequest</code> is used by default, if a null locale was
	 * provided.)
	 * </p>
	 * 
	 * @param pReq
	 *            The portlet request.
	 * @param pResp
	 *            The portlet response.
	 * @param pKey
	 *            The key for a message property containing the localized
	 *            filename.
	 * @param pDefault
	 *            The default filename to use if the message property does not
	 *            exist.
	 * @param pLoc
	 *            The locale to use (if null, uses the one inside the request).
	 * @return A URL for downloading the best-fit localized version of the base
	 *         file.
	 */
	public static String getLocalizedFileURL(PortletRequest pReq,
			PortletResponse pResp, String pKey, String pDefault, Locale pLoc) {

		if (resourceBundleDir == null || pReq == null || pResp == null
				|| pKey == null) {
			return null;
		}
		pKey = pKey.trim();
		if (pKey.length() == 0) {
			return null;
		}
		if (pDefault != null) {
			pDefault = pDefault.trim();
			if (pDefault.length() == 0) {
				pDefault = null;
			}
		}
		if (pLoc == null) {
			pLoc = pReq.getLocale();
		}

		// Get the localized filename from the message resources
		String fileName = getMessage(pReq, pKey, pDefault, null, null, pLoc,
				false);
		if (fileName != null)
			fileName = fileName.trim();

		// Check if the localized filename message was found or defaulted (ie is
		// not null and not equal to the key). If so, then check if that file
		// exists in the portlet resource bundle directory. If so, then return a
		// relay URL for that. If not, assume it exists as a static resource
		// inside the portlet application, and return an encoded resource URL
		// for that. If the filename was not found in the message resources,
		// though, return null.
		if (fileName != null && !fileName.equals(pKey)
				&& (fileName.length() > 0)) {
			fileName = slashify(fileName);
			if (fileExists(resourceBundleDir, fileName)) {
				return getFileRelayURL(pReq, pResp, fileName);
			} else {
				return pResp.encodeURL(fileName);
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Looks up the given base filename inside the given request's portlet
	 * application, and returns the filename of the best-fit localized version
	 * of that file for the locale in the portlet request.
	 * </p>
	 * <p>
	 * This method works like
	 * {@link #getLocalizedFileName(PortletRequest,String,Locale,boolean)} where
	 * the boolean switch is set to <code>true</code> (thus enabling localized
	 * file lookups) and the locale inside the given request is passed.
	 * </p>
	 * <p>
	 * <b>Note:</b> This method only looks inside the portlet application (ie
	 * portlet WAR). To look in the <i>portlet resource bundle folder</i>, use
	 * the companion {@link #getLocalizedFileName(String, Locale)} method.
	 * </p>
	 * 
	 * @param pReq
	 *            The portlet request for the application.
	 * @param pBaseFileName
	 *            The name of the base file to search (may include some path,
	 *            which is treated as relative to root of the portlet
	 *            application).
	 * @return The proper filename as per the parameters, or null if no
	 *         qualifying file was found.
	 */
	public static String getLocalizedFileName(PortletRequest pReq,
			String pBaseFileName) {
		return getLocalizedFileName(pReq, pBaseFileName, true);
	}

	/**
	 * <p>
	 * Looks up the given base filename inside the given request's portlet
	 * application, and returns as per the boolean switch: either the best-fit
	 * localized filename for the given locale inside that application, or the
	 * given base filename.
	 * </p>
	 * <p>
	 * This method works like
	 * {@link #getLocalizedFileName(PortletRequest,String,Locale,boolean)} where
	 * the current locale from the portlet request is passed.
	 * </p>
	 * <p>
	 * <b>Note:</b> This method only looks inside the portlet application (ie
	 * portlet WAR). To look in the <i>portlet resource bundle folder</i>, use
	 * the companion {@link #getLocalizedFileName(String,Locale,boolean)}
	 * method.
	 * </p>
	 * 
	 * @param pReq
	 *            The portlet request for the application.
	 * @param pBaseFileName
	 *            The name of the base file to search (may include some path,
	 *            which is treated as relative to root of the portlet
	 *            application).
	 * @param pLocalized
	 *            The version of the file for which to search: the base file
	 *            (false) or the best-fitting localized file (true).
	 * @return The proper filename as per the parameters, or null if no
	 *         qualifying file was found.
	 */
	public static String getLocalizedFileName(PortletRequest pReq,
			String pBaseFileName, boolean pLocalized) {
		return getLocalizedFileName(pReq, pBaseFileName, null, pLocalized);
	}

	/**
	 * <p>
	 * Looks up the given base filename inside the given request's portlet
	 * application, and returns the filename as per the boolean switch: either
	 * the best-fit localized filename for the given locale which exists in the
	 * application, or the given base filename. The given base filename may
	 * include some path relative to the context root of the application. If no
	 * appropriate file for that filename exists in the application, then null
	 * is returned. This method also returns null if any of its required
	 * parameters are null.
	 * </p>
	 * <p>
	 * If you set the boolean parameter to false, this method will just verify
	 * that the given base filename exists in the application (returning the
	 * given filename if so, otherwise returning null). You do not need to
	 * specify the locale parameter in this case; it is not used and you can set
	 * it to null. The base filename parameter is required.
	 * </p>
	 * <p>
	 * If you set the boolean parameter to true, then the base filename
	 * parameter is required and the locale parameter (if null) is defaulted to
	 * the locale inside the given request. This method will treat the given
	 * file as a resource bundle base, and look for the file which best-fits
	 * that base and locale inside the portlet application. The method follows
	 * the standard Java sequence in this (see {@link ResourceBundle} class
	 * documentation) and thus may return a locale-tagged localized pathname, or
	 * the base pathname if that is the best fit. Whatever the best-fit file,
	 * its filename is returned (including any path which was in the given
	 * filename). Otherwise null is returned.
	 * </p>
	 * <p>
	 * For example: in <code>html/</code> under the root of our application,
	 * consider that we have the files <code>foo.htm</code>,
	 * <code>foo_fr_CA.htm</code>, and <code>foo_fr.htm</code>. Then:
	 * </p>
	 * <dl>
	 * <dt><code>getLocalizedFileName("/files", "html/foo.htm", Locale.FRENCH, true)</code></dt>
	 * <dd>returns <code>html/foo_fr.htm</code></dd>
	 * <dt><code>getLocalizedFileName("/files", "html/foo.htm", Locale.FRANCE, true)</code></dt>
	 * <dd>returns <code>html/foo_fr.htm</code></dd>
	 * <dt><code>getLocalizedFileName("/files", "/html/foo.htm", Locale.CANADA_FRENCH,
	 * true)</code></dt>
	 * <dd>returns <code>/html/foo_fr_CA.htm</code></dd>
	 * <dt><code>getLocalizedFileName("/files", "/html/foo.htm", Locale.ITALIAN, true)</code></dt>
	 * <dd>returns <code>/html/foo.htm</code></dd>
	 * <dt><code>getLocalizedFileName("/files", "html/foo.htm", null, false)</code></dt>
	 * <dd>returns <code>html/foo.htm</code></dd>
	 * <dt><code>getLocalizedFileName("/files", "html/bar.htm", Locale.FRENCH, true)</code></dt>
	 * <dd>returns <code>null</code></dd>
	 * <dt><code>getLocalizedFileName("/files", "foo.htm", null, false)</code></dt>
	 * <dd>returns <code>null</code></dd>
	 * </dl>
	 * 
	 * <p>
	 * <b>Note:</b> This method only looks inside the portlet application (ie
	 * portlet WAR). To look in the <i>portlet resource bundle folder</i>, use
	 * the companion {@link #getLocalizedFileName(String, Locale, boolean)}
	 * method.
	 * </p>
	 * <p>
	 * <b>Note:</b> On case-sensitive filesystems, lowercase is assumed for the
	 * langage and variant codes, and uppercase is assumed for the country code.
	 * Thus in the above examples, different results from the portlet resource
	 * bundle folder would obtain if <code>foo_fr.htm</code> and/or
	 * <code>foo_fr_CA.htm</code> were tagged with uppercase language or
	 * lowercase country. Be sure your resource bundles follow the convention.
	 * </p>
	 * 
	 * @param pReq
	 *            The portlet request for the application.
	 * @param pBaseFileName
	 *            The name of the base file to search (may include some path,
	 *            which is treated as relative to root of the portlet
	 *            application).
	 * @param pLocale
	 *            The locale (not required if boolean parameter is false - if
	 *            boolean parameter is true and the locale is null, then the
	 *            locale inside the request is used by default).
	 * @param pLocalized
	 *            The version of the file for which to search: the base file
	 *            (false) or the best-fitting localized file (true).
	 * @return The proper filename as per the parameters, or null if no
	 *         qualifying file was found.
	 */
	public static String getLocalizedFileName(PortletRequest pReq,
			String pBaseFileName, Locale pLocale, boolean pLocalized) {

		if (pReq == null || pBaseFileName == null) {
			return null;
		}
		pBaseFileName = pBaseFileName.trim();
		pBaseFileName = slashify(pBaseFileName);
		if (pBaseFileName.length() == 0) {
			return null;
		}
		if (pLocale == null) {
			pLocale = pReq.getLocale();
		}
		PortletContext pContext = pReq.getPortletSession().getPortletContext();

		// Resolve localized file
		if (pLocalized) {
			String localizedFileName = null;
			String fileName = null;
			String extension = null;
			try {
				fileName = pBaseFileName.substring(0,
						pBaseFileName.lastIndexOf(".")).trim();
				extension = pBaseFileName.substring(
						pBaseFileName.lastIndexOf(".")).trim();
			} catch (Exception ex) {
				fileName = pBaseFileName;
				extension = "";
			}

			StringBuffer localizedFile = new StringBuffer(pBaseFileName
					.length() + 10);
			localizedFile.append(fileName);

			String language = pLocale.getLanguage().trim().toLowerCase();
			String country = pLocale.getCountry().trim().toUpperCase();
			String variant = pLocale.getVariant().trim().toLowerCase();

			if (language.length() > 1) {
				localizedFile.append("_").append(language);
				if (country.length() > 1) {
					localizedFile.append("_").append(country);
					if (variant.length() > 1) {
						localizedFile.append("_").append(variant);

						// filename_lang_cc_varint.extension
						localizedFile.append(extension);
						localizedFileName = localizedFile.toString();
						try {
							if (pContext.getResource(localizedFileName) != null) {
								return localizedFileName;
							}
						} catch (MalformedURLException e) {
						}
						localizedFile.delete(localizedFile.lastIndexOf("_"),
								localizedFile.length());
					}

					// filename_lang_cc.extension
					localizedFile.append(extension);
					localizedFileName = localizedFile.toString();
					try {
						if (pContext.getResource(localizedFileName) != null) {
							return localizedFileName;
						}
					} catch (MalformedURLException e) {
					}
					localizedFile.delete(localizedFile.lastIndexOf("_"),
							localizedFile.length());
				}

				// filename_lang.extension
				localizedFile.append(extension);
				localizedFileName = localizedFile.toString();
				try {
					if (pContext.getResource(localizedFileName) != null) {
						return localizedFileName;
					}
				} catch (MalformedURLException e) {
				}
			}
		}

		// filename.extension
		try {
			if (pContext.getResource(pBaseFileName) != null) {
				return pBaseFileName;
			}
		} catch (MalformedURLException e) {
		}
		// all the files do not exist
		return null;

	}

	/**
	 * <p>
	 * Looks up the given base filename in the <i>portlet resource bundle folder</i>,
	 * and returns the filename of the best-fit localized version of that file
	 * for the given locale which exists in that folder.
	 * </p>
	 * 
	 * <p>
	 * This method works the same as
	 * {@link #getLocalizedFileName(String,Locale,boolean)}, where the boolean
	 * parameter is set to <code>true</code> (thus enabling localized file
	 * lookups). Note also that this method only looks at the portlet bundle
	 * folder. To look inside your portlet application, use the companion
	 * {@link #getLocalizedFileName(PortletRequest,String, Locale)} method.
	 * </p>
	 * 
	 * @param pBaseFileName
	 *            The name of the base file to search (may include some path,
	 *            which is treated as relative to the portlet bundle folder).
	 * @param pLocale
	 *            The locale to use for the localized file lookup.
	 * @return The proper filename as per the parameters, or null if no
	 *         qualifying file was found.
	 */
	public static String getLocalizedFileName(String pBaseFileName,
			Locale pLocale) {
		return getLocalizedFileName(pBaseFileName, pLocale, true);
	}

	/**
	 * <p>
	 * Looks up the given base filename in the <i>portlet resource bundle folder</i>,
	 * and returns the filename as per the boolean switch: either the best-fit
	 * localized filename for the given locale which exists in that folder, or
	 * the given base filename. The given base filename may include some path
	 * relative to the portlet bundle folder. If no appropriate file for that
	 * filename exists in the portlet bundle folder, then null is returned. This
	 * method also returns null if any of its required parameters are null.
	 * 
	 * <p>
	 * <b>Note:</b> This method works the same as
	 * {@link com.hp.it.spf.xa.i18n.I18nUtility#getLocalizedFileName(String,String,Locale,boolean)},
	 * where the portlet bundle folder is passed as the first parameter. (The
	 * path for the portlet bundle folder is configured in
	 * <code>i18n_portlet_config.properties</code> and
	 * {@link #BUNDLE_DIR_DEFAULT} is the default.) Note also that this method
	 * only looks at the portlet bundle folder. To look inside your portlet
	 * application, use the companion
	 * {@link #getLocalizedFileName(PortletRequest, String, Locale, boolean)}
	 * method.
	 * </p>
	 * 
	 * @param pBaseFileName
	 *            The name of the base file to search (may include some path,
	 *            which is treated as relative to the portlet bundle folder).
	 * @param pLocale
	 *            The locale (not required if boolean parameter is false).
	 * @param pLocalized
	 *            The version of the file for which to search: the base file
	 *            (false) or the best-fitting localized file (true).
	 * @return The proper filename as per the parameters, or null if no
	 *         qualifying file was found.
	 */
	public static String getLocalizedFileName(String pBaseFileName,
			Locale pLocale, boolean pLocalized) {
		return getLocalizedFileName(resourceBundleDir, pBaseFileName, pLocale,
				pLocalized);
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * in any configured portlet resource bundle; the returned string is
	 * localized for the given locale, interpolated with the given parameters,
	 * defaulted with the given default message string if not found, and
	 * HTML-escaped per the given boolean switch. This method uses the Spring
	 * framework and provides additional useful functionality (such as support
	 * for embedded
	 * <code>&lt;contextual_help&gt;...&lt;/contextual_help&gt;</code>, and
	 * removal of
	 * <code>&lt;no_localization&gt;...&lt;/no_localization&gt;</code>
	 * markup).
	 * </p>
	 * <p>
	 * Selection of the particular localized message properties file is as per
	 * the Java standard behavior for {@link ResourceBundle}. Selection is
	 * based on the given locale; any locale contained in the given request is
	 * only used if the given locale is null.
	 * </p>
	 * <p>
	 * The resource bundles searched are the ones configured for your portlet in
	 * the Spring application context (ie in your portlet's
	 * <code>applicationContext.xml</code>). In your
	 * <code>applicationContext.xml</code>, we recommend you define your
	 * message <code>&lt;bean&gt;</code>'s using Spring's
	 * {@link ReloadableResourceBundleMessageSource} class, because then your
	 * messages will be hot-refreshed at runtime should they change (eg during a
	 * localization deployment, no restart will be needed).
	 * </p>
	 * <p>
	 * Your resource bundle files must be located somewhere findable by the
	 * class loader. For example, you could put them in the usual location,
	 * inside your portlet WAR. But for ease of administration (eg to permit
	 * localization by the administrator without having to touch the portlet
	 * WAR), you may put them in the <i>portlet resource bundle folder</i>
	 * dedicated for this purpose. The location of the portlet resource bundle
	 * folder is configured in the <code>i18n_portlet_config.properties</code>
	 * file ({@link #BUNDLE_DIR_DEFAULT} is the default); this folder or a
	 * parent folder should also be specified on the classpath so that the class
	 * loader can find it (if a parent folder, include the relative path between
	 * there and your portlet bundle folder in the Spring configuration).
	 * </p>
	 * <p>
	 * Parameter substitution is supported. No parameter substitution is
	 * performed if the given parameters are empty or null, or the message
	 * string contains no parameter placeholders. Note there are 2 kinds of
	 * parameters and their placeholders: general string parameters, and
	 * contextual-help parameters:
	 * </p>
	 * <ul>
	 * <p>
	 * <li>General string parameters are provided via the Object array. The
	 * objects in this array must implement the toString method. They are
	 * populated, in order, into any placeholders in the message string, as per
	 * the Java-standard {@link java.text.MessageFormat} specification. Any
	 * extra parameters are ignored; any unfilled placeholders remain.</li>
	 * </p>
	 * <p>
	 * <li>Contextual-help parameters are provided via the
	 * {@link ContextualHelpProvider} array. These ContextualHelpProviders are
	 * used to populate any special contextual-help tokens (<code>&lt;contextual_help&gt;...&lt;/contextual_help&gt;</code>)
	 * found in the message string. Those tokens can be used to denote parts of
	 * the message string which are to be linked to contextual help, where the
	 * contextual help is provided by the corresponding
	 * <code>ContextualHelpProvider</code> in the array.
	 * <ul>
	 * <li><code>&lt;contextual_help&gt;</code> tokens may not be nested.</li>
	 * <li>Each <code>ContextualHelpProvider</code> in the array, in order,
	 * provides the logic for populating the corresponding contextual-help
	 * token.</li>
	 * <li> The content surrounded by the contextual-help token is used as the
	 * link content for the <code>ContextualHelpProvider</code>. By the time
	 * you pass the <code>ContextualHelpProvider</code> to this method, you
	 * should already have used the other <code>ContextualHelpProvider</code>
	 * setters to set the other parameters for contextual-help, such as help
	 * content.</li>
	 * <li> Any extra <code>ContextualHelpProviders</code> in the array beyond
	 * the number of <code>&lt;contextual_help&gt;</code> tokens in the string
	 * are ignored.</li>
	 * <li> Any unmated <code>&lt;contextual_help&gt;</code> tokens in the
	 * string beyond the number of <code>ContextualHelpProviders</code> are
	 * ignored (and the tokens are removed).</li>
	 * <li> See the {@link ContextualHelpProvider} class hierarchy for more
	 * information - since that is an abstract class, the objects in your
	 * <code>ContextualHelpProvider</code> array will actually be concrete
	 * subclasses of that.</li>
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
	 * If the message string contains any special
	 * <code>&lt;no_localization&gt;...&lt;/no_localization&gt;</code> tokens,
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
	 * @param request
	 *            The portlet request.
	 * @param key
	 *            A message key.
	 * @param defaultMsg
	 *            A default message to return if the message string is not
	 *            found.
	 * @param params
	 *            An array of parameters to interpolate into the message string.
	 * @param cParams
	 *            An array of contextual-help providers to interpolate into the
	 *            message string.
	 * @param locale
	 *            The locale to assume (not necessarily the same as the one
	 *            inside the request).
	 * @param escapeHTML
	 *            Whether to escape any HTML special characters found in the
	 *            final message string.
	 * @return The message string, according to the above parameters.
	 */
	public static String getMessage(PortletRequest request, String key,
			String defaultMsg, Object[] params,
			ContextualHelpProvider[] cParams, Locale locale, boolean escapeHTML) {
		if (key != null) {
			key = key.trim();
		}
		if (defaultMsg == null) {
			defaultMsg = key;
		}
		String msg = defaultMsg;
		if (key != null && request != null) {
			if (locale == null) {
				locale = request.getLocale();
			}
			ApplicationContext ac = Utils.getApplicationContext(request);
			if (ac != null) {
				msg = ac.getMessage(key, params, defaultMsg, locale);
				msg = filterNoLocalizationTokens(msg);
				ContextualHelpUtility c = new ContextualHelpUtility();
				msg = c.parseContextualHelpTokens(msg, cParams, escapeHTML);
			}
		}
		return msg;
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * in any configured portlet resource bundle; the returned string is escaped
	 * (meaning that any HTML special characters it contains, like
	 * <code>&lt;</code>, are converted to their equivalent HTML character
	 * entities).
	 * </p>
	 * <p>
	 * This method works exactly like
	 * <code>getMessage(request,key,null,null,null,null,escapeHTML)</code>
	 * (see
	 * {@link #getMessage(PortletRequest, String, String, Object[], ContextualHelpProvider[], Locale, boolean)}).
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param key
	 *            A message key.
	 * @param escapeHTML
	 *            Whether to escape any HTML special characters found in the
	 *            final message string.
	 * @return The message string, according to the above parameters.
	 */
	public static String getMessage(PortletRequest request, String key,
			boolean escapeHTML) {
		return getMessage(request, key, null, null, null, null, escapeHTML);
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * in any configured portlet resource bundle; the returned string is
	 * localized for the given locale instead of the one inside the request.
	 * </p>
	 * <p>
	 * This method works exactly like
	 * <code>getMessage(request,key,null,null,null,locale,false)</code> (see
	 * {@link #getMessage(PortletRequest, String, String, Object[], ContextualHelpProvider[], Locale, boolean)}).
	 * 
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param key
	 *            A message key.
	 * @param locale
	 *            The locale to assume (instead of the one inside the request).
	 * @return The message string, according to the above parameters.
	 */
	public static String getMessage(PortletRequest request, String key,
			Locale locale) {
		return getMessage(request, key, null, null, null, locale, false);
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * in any configured portlet resource bundle; the returned string is
	 * localized for locale of the given request, and interpolated with the
	 * given contextual-help parameters.
	 * </p>
	 * <p>
	 * This method works exactly like
	 * <code>getMessage(request,key,null,null,cParams,null,false)</code> ((see
	 * {@link #getMessage(PortletRequest, String, String, Object[], ContextualHelpProvider[], Locale, boolean)}).
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param key
	 *            A message key.
	 * @param cParams
	 *            An array of contextual-help providers to interpolate into the
	 *            message string.
	 * @return The message string, according to the above parameters.
	 */
	public static String getMessage(PortletRequest request, String key,
			ContextualHelpProvider[] cParams) {
		return getMessage(request, key, null, null, cParams, null, false);
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * in any configured portlet resource bundle; the returned string is
	 * localized for the locale of the given request, and interpolated with the
	 * given parameters.
	 * </p>
	 * <p>
	 * This method works exactly like
	 * <code>getMessage(request,key,null,params,null,null,false)</code> (see
	 * {@link #getMessage(PortletRequest, String, String, Object[], ContextualHelpProvider[], Locale, boolean)}).
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param key
	 *            A message key.
	 * @param params
	 *            An array of parameters to interpolate into the message string.
	 * @return The message string, according to the above parameters.
	 */
	public static String getMessage(PortletRequest request, String key,
			Object[] params) {
		return getMessage(request, key, null, params, null, null, false);
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * in any configured portlet resource bundle; the returned string is
	 * localized for the locale of the given request. You can provide a default
	 * message string through this method. No parameter substitution is
	 * performed.
	 * </p>
	 * <p>
	 * This method works exactly like
	 * <code>getMessage(request,key,null,null,null,null,false)</code> (see
	 * {@link #getMessage(PortletRequest, String, String, Object[], ContextualHelpProvider[], Locale, boolean)}).
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param key
	 *            A message key.
	 * @param defaultMsg
	 *            A default message to return if the message string is not
	 *            found.
	 * @return The message string, according to the above parameters.
	 */
	public static String getMessage(PortletRequest request, String key,
			String defaultMsg) {
		return getMessage(request, key, defaultMsg, null, null, null, false);
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * in any configured portlet resource bundle; the returned string is
	 * localized for the locale of the given request. No parameter substitution
	 * is performed.
	 * </p>
	 * <p>
	 * This method works exactly like
	 * <code>getMessage(request,key,null,null,null,null,false)</code> (see
	 * {@link #getMessage(PortletRequest, String, String, Object[], ContextualHelpProvider[], Locale, boolean)}).
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param key
	 *            A message key.
	 * @return The message string, according to the above parameters.
	 */
	public static String getMessage(PortletRequest request, String key) {
		return getMessage(request, key, null, null, null, null, false);
	}

	/**
	 * <p>
	 * Returns the file-relay URL for the given filename and portlet response. A
	 * <i>file-relay URL</i> is a URL which will download a file from
	 * <i>outside the portlet WAR</i>. For example, you can make a file-relay
	 * URL using this method, and then embed it as the value of the
	 * <code>SRC</code> attribute in an HTML <code>&lt;IMG&gt;</code> tag;
	 * the end-user browser will then call that file-relay URL to download the
	 * image from where it is stored <i>outside</i> of your portlet WAR.
	 * </p>
	 * <p>
	 * The current implementation of file-relay in the Shared Portal Framework
	 * uses a servlet (the {@link com.hp.it.spf.xa.relay.servlet.RelayServlet})
	 * which can access files in the <i>portlet resource bundle folder</i>. So
	 * this method returns a relay servlet URL including the given filename
	 * (expected to exist in the portlet bundle folder) as a parameter for the
	 * servlet. The relay servlet may be deployed anywhere, so long as it is
	 * accessible to the end-user browser (via your portal is OK - eg you can
	 * deploy it inside your portlet WAR), and can access the portlet bundle
	 * folder. You can deploy it under any arbitrary location name in your
	 * portlet WAR, in the portal Web application, or in any other Web
	 * application which meets those criteria. We currently recommend you deploy
	 * it under the {@link #RELAY_PATH_DEFAULT} path in your portlet WAR (this
	 * is what is assumed by default; to override that, configure the actual
	 * location in <code>i18n_portlet_config.properties</code>).
	 * </p>
	 * <p>
	 * The behavior of this method depends on the configuration in
	 * <code>i18n_portlet_config.properties</code>:
	 * </p>
	 * <ul>
	 * <li>
	 * <p>
	 * If your <code>i18n_portlet_config.properties</code> gives a relative
	 * URL for the relay servlet's location, then this method looks for the
	 * relay servlet at that location inside the current portlet application. If
	 * it finds it, then this method uses the given portlet response to build
	 * and encode a resource URL for the servlet based on that relative URL.
	 * </p>
	 * </li>
	 * <li>
	 * <p>
	 * If your <code>i18n_portlet_config.properties</code> gives an absolute
	 * URL for the relay servlet's location, or the relative location you have
	 * configured is cannot be looked-up inside the portlet application, then
	 * this method just assumes the value you configured in
	 * <code>i18n_portlet_config.properties</code> is the literal URL for the
	 * servlet - and so it returns that URL.
	 * </p>
	 * </li>
	 * <li>
	 * <p>
	 * If you did not configure the relay servlet location in
	 * <code>i18n_portlet_config.properties</code> at all, then the method
	 * assumes {@link #RELAY_PATH_DEFAULT} for the location and proceeds as
	 * above. Thus if that servlet is actually found in your portlet WAR at that
	 * location, then the method will return an encoded resource URL for it; and
	 * if it is not found there, then {@link #RELAY_PATH_DEFAULT} itself will be
	 * returned.
	 * </ul>
	 * <p>
	 * <b>Note:</b> In each case above, the given filename is attached as
	 * additional-path information on the returned URL.
	 * </p>
	 * <b>Note:</b> This method does not verify that the given file actually
	 * exists, or where it exists. However, the filename is a required
	 * parameter; null is returned if you pass a null or blank filename.
	 * </p>
	 * 
	 * @param request
	 *            The request for the portlet whose WAR contains the relay
	 *            service.
	 * @param response
	 *            The response for the portlet whose WAR contains the relay
	 *            service.
	 * @param fileName
	 *            The file which the returned URL should include as additional
	 *            path.
	 * @return The file-relay URL, ready for emitting to the end-user browser.
	 */
	public static String getFileRelayURL(PortletRequest request,
			PortletResponse response, String fileName) {
		if (fileName == null || relayServletURL == null || request == null
				|| response == null) {
			return null;
		}
		fileName = fileName.trim();
		if (fileName.equals("")) {
			return null;
		}
		PortletContext context = request.getPortletSession()
				.getPortletContext();

		// If the configured relay URL is relative and exists inside the current
		// portlet application, then encode and return it. Otherwise just return
		// it unencoded (so it could be a portal-relative URL instead of a
		// portlet URL in that case, or it could be another absolute URL
		// entirely). In either case, attach the filename as additional path.
		try {
			if ((relayServletURL.startsWith("/"))
					&& (context.getResource(relayServletURL) != null)) {
				return response.encodeURL(slashify(relayServletURL + "/"
						+ fileName));
			}
		} catch (MalformedURLException e) {
		}
		return (slashify(relayServletURL + "/" + fileName));
	}
}
