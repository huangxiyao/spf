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
 * A class of useful internationalization/localization methods for use by
 * portlets.
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
	 * is assumed by the PropertyResourceBundleManager.)
	 */
	public static final String PORTLET_I18N_CONFIG_FILE = "i18n_portlet_config";

	/**
	 * Various properties in the portlet internationalization configuration
	 * file. See file for definition.
	 */
	public static final String PORTLET_I18N_CONFIG_PROP_BUNDLE_DIR = "portlet.bundleDirectory";
	public static final String PORTLET_I18N_CONFIG_PROP_RELAY_URL = "servlet.fileRelay.url";
	public static final String PORTLET_I18N_CONFIG_PROP_RELAY_IS_PORTLET = "servlet.fileRelay.isPortletResource";

	/**
	 * The default URL for the relay servlet.
	 */
	private static final String RELAY_PATH_DEFAULT = "/relay";
	private static final String RELAY_IS_PORTLET = "yes";
	private static final String RELAY_IS_NOT_PORTLET = "no";
	private static final String RELAY_IS_PORTLET_DEFAULT = RELAY_IS_PORTLET;

	/**
	 * Logger instance.
	 */
	private static Log logger = LogFactory.getLog(I18nUtility.class);

	/**
	 * Relay servlet name (actual value is configured in property file).
	 */
	private static String relayServletUrl = RELAY_PATH_DEFAULT;

	/**
	 * Is the relay servlet a portlet resource.
	 */
	private static String relayServletIsPortlet = RELAY_IS_PORTLET_DEFAULT;

	/**
	 * Portlet resource bundle directory (actual value is configured in property
	 * file).
	 */
	private static String resourceBundleDir = null;

	/**
	 * Static block to initialize relay servlet info and portlet resource bundle
	 * directory from configuration file.
	 */
	static {
		ResourceBundle bundle = PropertyResourceBundleManager
				.getBundle(PORTLET_I18N_CONFIG_FILE);
		if (bundle == null) {
			logger.error("I18nUtility: Failed to open "
					+ PORTLET_I18N_CONFIG_FILE + " file");
			relayServletUrl = RELAY_PATH_DEFAULT;
			relayServletIsPortlet = RELAY_IS_PORTLET_DEFAULT;
			resourceBundleDir = null;
		} else {
			try {
				resourceBundleDir = "/"
						+ bundle.getString(PORTLET_I18N_CONFIG_PROP_BUNDLE_DIR)
						+ "/";
				resourceBundleDir = slashify(resourceBundleDir);
			} catch (Exception ex) { // error if prop not exist
				logger
						.error("I18nUtility: Required property "
								+ PORTLET_I18N_CONFIG_PROP_BUNDLE_DIR
								+ " missing from " + PORTLET_I18N_CONFIG_FILE
								+ " file");
				logger.error("I18nUtility: " + ex.getMessage());
				resourceBundleDir = null;
			}
			try {
				relayServletUrl = "/"
						+ bundle.getString(PORTLET_I18N_CONFIG_PROP_RELAY_URL)
						+ "/";
				relayServletUrl = slashify(relayServletUrl);
			} catch (Exception ex) { // not an error if prop not exist
				relayServletUrl = RELAY_PATH_DEFAULT;
			}
			try {
				relayServletIsPortlet = bundle
						.getString(PORTLET_I18N_CONFIG_PROP_RELAY_IS_PORTLET);
				if (relayServletIsPortlet.equalsIgnoreCase("yes")
						|| relayServletIsPortlet.equalsIgnoreCase("true")
						|| relayServletIsPortlet.equalsIgnoreCase("")) {
					relayServletIsPortlet = RELAY_IS_PORTLET;
				} else {
					relayServletIsPortlet = RELAY_IS_NOT_PORTLET;
				}
			} catch (Exception ex) { // not an error if prop not exist
				relayServletIsPortlet = RELAY_IS_PORTLET_DEFAULT;
			}
		}
	}

	/**
	 * Private to prevent external construction.
	 */
	private I18nUtility() {

	}

	/**
	 * <p>
	 * Returns an input stream for a proper localized version of the given base
	 * filename of a portlet resource bundle. The filename must be a base
	 * filename for a portlet resource bundle. The bundle may be located in the
	 * portlet resource bundle folder on the portlet server (eg, outside of the
	 * portlet application). Alternatively, the bundle may be inside the portlet
	 * application, as a set of static resources. In either case, the given base
	 * filename may include a subfolder relative to the portlet resource bundle
	 * folder or portlet application root, respectively. (Note that the path to
	 * the portlet resource bundle folder is configured in
	 * <code>i18n_portlet_config.properties</code>.)
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
	 * <dt><code>getLocalizedFilePath(request, "html/foo.htm")</code> when
	 * <code>request</code> contains Canada French (<code>Locale.CANADA_FRENCH</code>)</dt>
	 * <dd>returns a file input stream for
	 * <code><i>portlet-resource-folder</i>/html/foo_fr_CA.htm</code></dd>
	 * 
	 * <dt><code>getLocalizedFilePath(request, "/html/foo.htm")</code> when
	 * <code>request</code> contains France French (<code>Locale.FRANCE</code>)</dt>
	 * <dd>returns a file input stream for
	 * <code><i>portlet-resource-folder</i>/html/foo_fr.htm</code></dd>
	 * 
	 * <dt><code>getLocalizedFilePath(request, "html/foo.htm")</code> when
	 * <code>request</code> contains generic Italian (<code>Locale.ITALIAN</code>)</dt>
	 * <dd>returns a file input stream for
	 * <code><i>portlet-resource-folder</i>/html/foo.htm</code></dd>
	 * </dt>
	 * 
	 * <dt><code>getLocalizedFilePath(request, "html/bar.htm")</code> when
	 * <code>request</code> contains generic Italian (<code>Locale.ITALIAN</code>)</dt>
	 * <dd>returns an input stream for <code>/html/bar_it.htm</code> in the
	 * portlet application.</dd>
	 * 
	 * <dt><code>getLocalizedFilePath(request, "html/bar.htm")</code> when
	 * <code>request</code> contains generic French (<code>Locale.FRENCH</code>)</dt>
	 * <dd>returns an input stream for <code>/html/bar.htm</code> in the
	 * portlet application.</dd>
	 * 
	 * <dt><code>getLocalizedFilePath(request, "html/foobar.htm")</code></dt>
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
	public static InputStream getLocalizedFileAsStream(PortletRequest pReq,
			String pBaseFileName) {
		return getLocalizedFileAsStream(pReq, pBaseFileName, true);
	}

	/**
	 * <p>
	 * Returns an input stream for the given version of the given portlet
	 * resource bundle file (ie, localized to best-fit the locale in the given
	 * request, or not, per the boolean switch). If the boolean switch is set to
	 * <code>true</code>, this method works exactly like
	 * <code>getLocalizedFileAsStream(PortletRequest,String)</code> (see). If
	 * the boolean switch is set to <code>false</code>, this method does not
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
	 * 
	 */
	public static InputStream getLocalizedFileAsStream(PortletRequest pReq,
			String pBaseFileName, boolean pLocalized) {

		if (resourceBundleDir == null || pReq == null || pBaseFileName == null) {
			return null;
		}
		pBaseFileName = pBaseFileName.trim();
		pBaseFileName = slashify(pBaseFileName);
		if (pBaseFileName.length() == 0) {
			return null;
		}
		PortletContext pContext = pReq.getPortletSession().getPortletContext();

		// Get the localized filename by looking in the portlet resource bundle
		// folder.
		String fileName = getLocalizedFileName(resourceBundleDir,
				pBaseFileName, pReq.getLocale(), pLocalized);

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
		fileName = getLocalizedFileName(pReq, pBaseFileName, pReq.getLocale(),
				pLocalized);
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
	 * relative to the portlet resource bundle folder or portlet application
	 * root, respectively. (Note that the path to the portlet resource bundle
	 * folder is configured in <code>i18n_portlet_config.properties</code>.)
	 * </p>
	 * <p>
	 * This method works like
	 * <code>getLocalizedFileAsStream(PortletRequest,String)</code>, but
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
	 * <code>getLocalizedFileAsStream(PortletRequest,String)</code> method.
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
	public static InputStream getLocalizedFileAsStream(PortletRequest pReq,
			String pKey, String pDefault) {
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
				return null;
			}
		}
		PortletContext pContext = pReq.getPortletSession().getPortletContext();

		// Get the localized filename from the message resources
		String fileName = getMessage(pReq, pKey, pDefault);

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
	 * The given filename must be a base filename for a portlet resource bundle.
	 * The base file, and its localized variants, may be located in the portlet
	 * resource bundle folder on the portlet server. In this case, the given
	 * filename may include a subfolder relative to that portlet resource bundle
	 * folder. (The path to the portlet resource bundle folder is configured in
	 * <code>i18n_portlet_config.properties</code>.) In this case, the
	 * returned URL will be a file-relay URL pointing to that particular
	 * localized file in the portlet resource bundle folder.
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
	 * ResourceBundle class. Thus the URL returned by this method will point to
	 * the best-candidate locale-tagged filename, or the given base filename if
	 * there is no better fit. If the resource bundle does not exist at all
	 * (note that on a case-sensitive filesystem, this is a case-sensitive
	 * lookup), the method returns a URL referencing the base filename as a
	 * static resource inside the portlet application. (This will, of course, be
	 * a URL which points to a missing resource, causing an HTTP 404 error if
	 * opened.)
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
	 * the portlet resource bundle folder. (The exact URL may vary depending on
	 * the implementation of this method, and is not documented here.)</dd>
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
	 * <b>Note:</b> The file-relay URL points to the location configured in
	 * <code>i18n_portlet_config.properties</code>. If those properties
	 * indicate the file-relay service (currently a servlet) is a resource
	 * within the current portlet application, then the given portlet response
	 * is used to generate an encoded resource URL for it. Otherwise the
	 * location property is used literally as the basis for the URL. The given
	 * filename is included as additional-path on the URL in either case.
	 * </p>
	 * <p>
	 * <b>Note:</b> This method uses the <code>getLocalizedFileName</code>
	 * methods to determine the proper best-candidate localized file for the
	 * given filename, looking in the portlet resource bundle directory first,
	 * and the portlet application second. It also may use the
	 * <code>getFileRelayURL</code> method to generate a file-relay URL for it
	 * (ie, if the portlet resource bundle folder is where the file was found).
	 * Please see those other methods for additional documentation.
	 * </p>
	 * <p>
	 * <b>Note:</b> As mentioned, this method automatically discovers the
	 * best-fit localized file for the locale. An alternative technique for
	 * generating a localized file URL relies on the name of the best-fit
	 * localized file already being stored in a message resource. If you would
	 * rather generate your localized file URL that way, you should use the
	 * companion
	 * <code>getLocalizedFileURL(PortletRequest,PortletResponse,String,String)</code>
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
	 * <code>getLocalizedFileURL(PortletRequest,PortletResponse,String)</code>
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

		if (resourceBundleDir == null || pReq == null || pResp == null
				|| pBaseFileName == null) {
			return null;
		}
		pBaseFileName = pBaseFileName.trim();
		pBaseFileName = slashify(pBaseFileName);
		if (pBaseFileName.length() == 0) {
			return null;
		}

		// Get the localized filename by looking in the portlet resource bundle
		// folder.
		String fileName = getLocalizedFileName(resourceBundleDir,
				pBaseFileName, pReq.getLocale(), pLocalized);

		// If the localized filename was found, make and return a file-relay URL
		// for it. Otherwise, look for the localized filename inside the portlet
		// application. If we find it there, make and return an encoded resource
		// URL pointing to it. If still we do not find it, make and return an
		// encoded resource URL pointing to the given base filename inside the
		// portlet application, by default.
		if (fileName != null) {
			return getFileRelayURL(pResp, fileName);
		} else {
			fileName = getLocalizedFileName(pReq, pBaseFileName, pReq
					.getLocale(), pLocalized);
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
	 * <code>getLocalizedFileURL(PortletRequest,PortletResponse,String)</code>,
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
	 * The file named in the message resource may be located in the portlet
	 * resource bundle folder on the portlet server. The filename from the
	 * message resource may include a subfolder relative to that portlet
	 * resource bundle folder. The path to the portlet resource bundle folder is
	 * configured in <code>i18n_portlet_config.properties</code>. In this
	 * case, the returned URL is a file-relay URL pointing to that file in the
	 * portlet resource bundle directory.
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
	 * <b>Note:</b> This method uses the <code>getMessage</code> method to
	 * determine the proper filename. Then (if the file exists in the portlet
	 * resource bundle folder), it may use the <code>getFileRelayURL</code>
	 * method in this class to generate a file-relay URL for it. Please see
	 * those other methods for additional documentation.
	 * </p>
	 * <p>
	 * <b>Note:</b> As mentioned, this method relies on you having placed the
	 * proper localized filenames for each locale into the message resources for
	 * each locale. If you would rather not do that, and would rather just have
	 * the system inspect the available files in the resource bundle
	 * automatically to build the URL, use the companion
	 * <code>getLocalizedFileURL(PortletRequest,PortletResponse,String)</code>
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
				return null;
			}
		}

		// Get the localized filename from the message resources
		String fileName = getMessage(pReq, pKey, pDefault);

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
				return getFileRelayURL(pResp, fileName);
			} else {
				return pResp.encodeURL(fileName);
			}
		}
		return null;
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
	 * specify the locale parameter in this case; you can set that to null. The
	 * base filename parameter is required.
	 * </p>
	 * <p>
	 * If you set the boolean parameter to true, both the base filename and
	 * locale parameters are required. This method will treat the given file as
	 * a resource bundle base, and look for the file which best-fits that base
	 * and locale inside the portlet application. The method follows the
	 * standard Java sequence in this (see ResourceBundle class documentation)
	 * and thus may return a locale-tagged localized pathname, or the base
	 * pathname if that is the best fit. Whatever the best-fit file, its
	 * filename is returned (including any path which was in the given
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
	 * @param pReq
	 *            The portlet request for the application.
	 * @param pBaseFileName
	 *            The name of the base file to search (may include some path,
	 *            which is treated as relative to root of the portlet
	 *            application).
	 * @param pLocale
	 *            The locale (not required if boolean parameter is false).
	 * @param pLocalized
	 *            The version of the file for which to search: the base file
	 *            (false) or the best-fitting localized file (true).
	 * @return The proper filename as per the parameters, or null if no
	 *         qualifying file was found.
	 */
	public static String getLocalizedFileName(PortletRequest pReq,
			String pBaseFileName, Locale pLocale, boolean pLocalized) {

		if (pReq == null || pBaseFileName == null
				|| (pLocalized == true && pLocale == null)) {
			return null;
		}
		pBaseFileName = pBaseFileName.trim();
		pBaseFileName = slashify(pBaseFileName);
		if (pBaseFileName.length() == 0) {
			return null;
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
	 * Returns a message string for the given key from a message properties file
	 * in any configured portlet resource bundle; the returned string is
	 * localized for the given locale, interpolated with the given parameters,
	 * defaulted with the given default message string if not found, and
	 * HTML-escaped per the given boolean switch. This method uses the Spring
	 * framework and provides additional useful functionality (such as support
	 * for embedded contextual help).
	 * </p>
	 * <p>
	 * Selection of the particular localized message properties file is as per
	 * the Java standard behavior for ResourceBundle. Selection is based on the
	 * given locale; any locale contained in the given request is only used if
	 * the given locale is null.
	 * </p>
	 * <p>
	 * The resource bundles searched are the ones configured for your portlet in
	 * the Spring application context (ie in your portlet's
	 * <code>applicationContext.xml</code>). In your
	 * <code>applicationContext.xml</code>, we recommend you define your
	 * message <code>&lt;bean&gt;</code>'s using Spring's
	 * ReloadableResourceBundleMessageSource class, because then your messages
	 * will be hot-refreshed at runtime should they change (eg during a
	 * localization deployment, no restart will be needed).
	 * </p>
	 * <p>
	 * Your resource bundle files must be located somewhere findable by the
	 * class loader. For example, you could put them in the usual location,
	 * inside your portlet WAR. But for ease of administration (eg to permit
	 * localization by the administrator without having to touch the portlet
	 * WAR), we recommend you put them in the portlet resource bundle folder
	 * dedicated for this purpose. The location of the portlet resource bundle
	 * folder is configured in the <code>i18n_portlet_config.properties</code>
	 * file; this folder should also be specified on the classpath so that the
	 * class loader can find it.
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
	 * the Java-standard MessageFormat specification. Any extra parameters are
	 * ignored; any unfilled placeholders remain.</li>
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
	 * (see).
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
	 * <code>getMessage(request,key,null,null,null,locale,false)</code> (see).
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
	 * <code>getMessage(request,key,null,null,cParams,null,false)</code>
	 * (see).
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
	 * <code>getMessage(request,key,null,params,null,null,false)</code> (see).
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
	 * <code>getMessage(request,key,null,null,null,null,false)</code> (see).
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
	 * <code>getMessage(request,key,null,null,null,null,false)</code> (see).
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
	 * file-relay URL is a URL which will download the file from the portlet
	 * bundle directory indicated by the given filename. For example, you can
	 * make a file-relay URL using this method, and then embed it as the value
	 * of the <code>SRC</code> attribute in an HTML <code>&lt;IMG&gt;</code>
	 * tag; the end-user browser will then call that file-relay URL to download
	 * the image from the portlet bundle directory.
	 * </p>
	 * <p>
	 * The current implementation of file-relay in the Shared Portal Framework
	 * uses a servlet (the relay servlet). The servlet may be deployed anywhere,
	 * so long as it is accessible to the end-user browser, and so long as its
	 * location is configured in the <code>i18n_portlet_config.properties</code>
	 * file. We currently recommend you deploy it under the <code>/relay</code>
	 * path in your portlet WAR (this is what is assumed when the location is
	 * left unconfigured in <code>i18n_portlet_config.properties</code>).
	 * </p>
	 * <p>
	 * The behavior of this method depends on the configuration in
	 * <code>i18n_portlet_config.properties</code>:
	 * </p>
	 * <ul>
	 * <li>
	 * <p>
	 * If your <code>i18n_portlet_config.properties</code> indicates the
	 * servlet is a portlet resource, and gives a relative URL for the location,
	 * then this method uses the given portlet response to build and encode a
	 * resource URL for the servlet based on that relative URL. The servlet is
	 * assumed to exist at that location inside the portlet WAR.
	 * </p>
	 * </li>
	 * <li>
	 * <p>
	 * Otherwise, this method just assumes the URL for the servlet in
	 * <code>i18n_portlet_config.properties</code> is the literal URL for the
	 * servlet - and so it returns that URL. This is also what happens in the
	 * above case, if the given portlet response was null.
	 * </p>
	 * </li>
	 * </ul>
	 * <p>
	 * In each case, the given filename is attached as additional-path
	 * information on the returned URL. Note that the method does not verify
	 * that the given file actually exists, or that the servlet is actually
	 * deployed at the configured location.
	 * </p>
	 * <p>
	 * The filename is a required parameter; null is returned if you pass a null
	 * filename.
	 * </p>
	 * 
	 * @param response
	 *            The response for the portlet whose WAR contains the relay
	 *            service.
	 * @param fileName
	 *            The file which the returned URL should include as additional
	 *            path.
	 * @return The file-relay URL, ready for emitting to the end-user browser.
	 */
	public static String getFileRelayURL(PortletResponse response,
			String fileName) {
		if (fileName == null || relayServletUrl == null
				|| relayServletIsPortlet == null) {
			return null;
		}
		fileName = fileName.trim();
		if (fileName.equals("")) {
			return null;
		}
		String url = null;
		if (response != null && relayServletUrl.startsWith("/")
				&& relayServletIsPortlet.equals(RELAY_IS_PORTLET)) {
			url = response
					.encodeURL(slashify(relayServletUrl + "/" + fileName));
		} else {
			url = slashify(relayServletUrl + "/" + fileName);
		}
		return url;
	}
}
