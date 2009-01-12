/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 * 
 * Common internationalization/localization utility for portlet framework.
 */
package com.hp.it.spf.xa.i18n.portlet;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

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
	 * Returns the absolute pathname for a proper localized version of the given
	 * base filename of a portlet resource bundle. The filename must be a base
	 * filename for a portlet resource bundle, located in the portlet resource
	 * bundle folder on the portlet server. The given filename may include a
	 * subfolder relative to that portlet resource bundle folder. The path to
	 * the portlet resource bundle folder is configured in
	 * <code>i18n_portlet_config.properties</code>. Since this method returns
	 * an absolute pathname, the portlet resource bundle folder path will be
	 * included at the beginning of the returned value.
	 * </p>
	 * <p>
	 * This method uses the locale from the given portlet request to find the
	 * best-candidate localized version of the given file in the resource
	 * bundle. The logic for finding the best-candidate is the same as that used
	 * by the Java-standard ResourceBundle class. Thus the pathname returned by
	 * this method will include the best-candidate locale-tagged filename, or
	 * the given base filename if there is no better fit. If the resource bundle
	 * does not exist at all (note that on a case-sensitive filesystem, this is
	 * a case-sensitive lookup), the method returns null. This method also
	 * returns null if any of its parameters are null.
	 * </p>
	 * <p>
	 * For example: in the portlet resource bundle folder consider that we have
	 * the files <code>html/foo.htm</code>, <code>html/foo_fr_CA.htm</code>,
	 * and <code>html/foo_fr.htm</code>. Then:
	 * </p>
	 * 
	 * <dl>
	 * <dt><code>getLocalizedFilePath(request, "html/foo.htm")</code> when
	 * <code>request</code> contains Canada French (<code>Locale.CANADA_FRENCH</code>)</dt>
	 * <dd>returns
	 * <code><i>portlet-resource-folder</i>/html/foo_fr_CA.htm</code></dd>
	 * 
	 * <dt><code>getLocalizedFilePath(request, "/html/foo.htm")</code> when
	 * <code>request</code> contains France French (<code>Locale.FRANCE</code>)</dt>
	 * <dd>returns <code><i>portlet-resource-folder</i>/html/foo_fr.htm</code></dd>
	 * 
	 * <dt><code>getLocalizedFilePath(request, "html/foo.htm")</code> when
	 * <code>request</code> contains generic Italian (<code>Locale.ITALIAN</code>)</dt>
	 * <dd>returns <code><i>portlet-resource-folder</i>/html/foo.htm</code></dd>
	 * </dt>
	 * 
	 * <dt><code>getLocalizedFilePath(request, "html/bar.htm")</code></dt>
	 * <dd>returns <code>null</code></dd>
	 * </dl>
	 * 
	 * <p>
	 * <b>Note:</b> On case-sensitive filesystems, lowercase is assumed for the
	 * langage and variant codes, and uppercase is assumed for the country code.
	 * Thus in the above examples, different results would obtain if
	 * <code>foo_fr.htm</code> and/or <code>foo_fr_CA.htm</code> were tagged
	 * with uppercase language or lowercase country. Be sure your resource
	 * bundles follow the convention.
	 * </p>
	 * 
	 * @param pReq
	 *            The portlet request.
	 * @param pBaseFileName
	 *            The name of the base file to search.
	 * @return The absolute pathname of the best-candidate localized file in the
	 *         portlet bundle folder, or null if no qualifying file was found.
	 * 
	 */
	public static String getLocalizedFilePath(PortletRequest pReq,
			String pBaseFileName) {
		return getLocalizedFilePath(pReq, pBaseFileName, true);
	}

	/**
	 * <p>
	 * Returns the absolute pathname for the given version of the given portlet
	 * resource bundle file (ie, localized to best-fit the locale in the given
	 * request, or not, per the boolean switch). If the boolean switch is set to
	 * <code>true</code>, this method works exactly like
	 * <code>getLocalizedFilePath(PortletRequest,String)</code> (see). If the
	 * boolean switch is set to <code>false</code>, this method does not
	 * actually localize the returned filename. Instead, the returned filename
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
	 * @return The absolute pathname of the best-candidate localized file in the
	 *         portlet bundle folder, or null if no qualifying file was found.
	 * 
	 */
	public static String getLocalizedFilePath(PortletRequest pReq,
			String pBaseFileName, boolean pLocalized) {
		if (resourceBundleDir == null || pReq == null || pBaseFileName == null) {
			return null;
		}
		pBaseFileName = pBaseFileName.trim();
		if (pBaseFileName.length() == 0) {
			return null;
		}
		String fileName = getLocalizedFileName(resourceBundleDir,
				pBaseFileName, pReq.getLocale(), pLocalized);
		if (fileName == null) {
			return null;
		}
		return slashify(resourceBundleDir + "/" + fileName);
	}

	/**
	 * <p>
	 * Returns the absolute pathname for a localized portlet resource bundle
	 * file, where the filename is retrieved (properly localized) from the
	 * portlet's message resources using the given key and default. The filename
	 * in the message resource must be that of a secondary support file for the
	 * same portal component. <b>Important:</b> The component must be a style
	 * or secondary page; other components are not supported.
	 * </p>
	 * <p>
	 * This method works like
	 * <code>getLocalizedFilePath(PortletRequest,String)</code>, but where
	 * the localized filename string is taken from a message resource of the
	 * portlet, using the given key and default filename. Thus this method
	 * relies on the best-candidate filename for each locale already existing in
	 * the localized message properties for that locale, at the given key. (And
	 * if the key is not found, it applies the given default filename.) Thus the
	 * method just reads the filename from the message, assumes that file is the
	 * best one for that locale, and returns a path for it accordingly.
	 * </p>
	 * <p>
	 * The file named in the message resource must be located in the portlet
	 * resource bundle folder on the portlet server. The filename from the
	 * message resource may include a subfolder relative to that portlet
	 * resource bundle folder. The path to the portlet resource bundle folder is
	 * configured in <code>i18n_portlet_config.properties</code>.
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
	 * the system inspect the available files in the resource bundle vis-a-vis
	 * the locale automatically, use the companion
	 * <code>getLocalizedFilePath(PortletRequest,String)</code> method.
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
	public static String getLocalizedFilePath(PortletRequest pReq, String pKey,
			String pDefault) {
		if (resourceBundleDir == null || pReq == null || pKey == null) {
			return null;
		}
		pKey = pKey.trim();
		if (pKey.length() == 0) {
			return null;
		}
		if (pDefault != null) {
			pDefault = pDefault.trim();
		}
		try {
			// get the localized filename from the message resources
			String fileName = getMessage(pReq, pKey, pDefault);

			// check if the file exists - if not, return false.
			if (fileName != null && fileExists(resourceBundleDir, fileName)) {
				return slashify(resourceBundleDir + "/" + fileName);
			}
		} catch (Exception ex) {
		}
		return null;
	}

	/**
	 * Returns a file-relay URL (suitable for presentation to the user) for
	 * downloading the given version of the given portlet resource bundle base
	 * filename (ie, localized to best-fit the locale in the given request, or
	 * not, per the boolean switch). The returned URL is ready for presentation
	 * to the user in the portlet response; it does not need to be encoded or
	 * rewritten. For example, you can take the return from this method, and
	 * express it as the <code>SRC</code> attribute in an HTML
	 * <code>&lt;IMG&gt;</code> tag.
	 * </p>
	 * <p>
	 * The given filename must be a base filename for a portlet resource bundle,
	 * located in the portlet resource bundle folder on the portlet server. The
	 * given filename may include a subfolder relative to that portlet resource
	 * bundle folder. (The path to the portlet resource bundle folder is
	 * configured in <code>i18n_portlet_config.properties</code>.)
	 * </p>
	 * <p>
	 * This method uses the locale from the given portlet request to find the
	 * best-candidate localized version of the given file in the resource
	 * bundle. The logic for finding the best-candidate is the same as that used
	 * by the Java-standard ResourceBundle class. Thus the URL returned by this
	 * method will point to the best-candidate locale-tagged filename, or the
	 * given base filename if there is no better fit. If the resource bundle
	 * does not exist at all (note that on a case-sensitive filesystem, this is
	 * a case-sensitive lookup), the method returns null.
	 * </p>
	 * <p>
	 * For example: in the portlet resource bundle folder consider that we have
	 * the files <code>html/foo.htm</code>, <code>html/foo_fr_CA.htm</code>,
	 * and <code>html/foo_fr.htm</code>. Then:
	 * </p>
	 * <dl>
	 * <dt><code>getLocalizedFileURL(request, response, "html/foo.htm")</code>
	 * when <code>request</code> contains Canada French (<code>Locale.CANADA_FRENCH</code>)</dt>
	 * <dd>returns a file-relay URL for <code>html/foo_fr_CA.htm</code> in
	 * the portlet resource bundle folder. (The exact URL may vary depending on
	 * the implementation of this method, and is not documented here. But note
	 * that it is a relative URL to the scheme, hostname, and port in the
	 * current request.)</dd>
	 * 
	 * <dt><code>getLocalizedFileURL(request, response, "/html/foo.htm")</code>
	 * when <code>request</code> contains France French (<code>Locale.FRANCE</code>)</dt>
	 * <dd>returns a file-relay URL for <code>html/foo_fr.htm</code>.</dd>
	 * 
	 * <dt><code>getLocalizedFileURL(request, response, "html/foo.htm")</code>
	 * when <code>request</code> contains generic Italian (<code>Locale.ITALIAN</code>)</dt>
	 * <dd>returns a file-relay URL for <code>html/foo.htm</code></dd>
	 * </dt>
	 * 
	 * <dt><code>getLocalizedFileURL(request, response, "html/bar.htm")</code></dt>
	 * <dd>returns <code>null</code></dd>
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
	 * <b>Note:</b> This method uses the <code>getLocalizedFilePath</code>
	 * method to determine the proper best-candidate localized file for the
	 * given filename, looking in the portlet resource bundle directory. Then,
	 * it uses the <code>getFileRelayURL</code> method in this class to
	 * generate a file-relay URL for it. Please see those other methods for
	 * additional documentation.
	 * </p>
	 * <p>
	 * <b>Note:</b> As mentioned, this method automatically discovers the
	 * best-fit localized file for the locale. An alternative technique for
	 * generating a localized file URL relies on the best-fit localized file
	 * already being stored in a message resource. If you would rather generate
	 * your localized file URL that way, you should use the companion
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
	 * @return A file-relay URL, for downloading the best-fit localized version
	 *         of the base file from the portlet resource bundle folder, or null
	 *         if no qualifying file was found.
	 */
	public static String getLocalizedFileURL(PortletRequest pReq,
			PortletResponse pResp, String pBaseFileName) {
		return getLocalizedFileURL(pReq, pResp, pBaseFileName, true);
	}

	/**
	 * <p>
	 * Returns a file-relay URL (suitable for presentation to the user) for
	 * downloading the given version of the given portlet resource bundle base
	 * filename (ie, localized to best-fit the locale in the given request, or
	 * not, per the boolean switch). The returned URL is ready for presentation
	 * to the user in the portlet response; it does not need to be encoded or
	 * rewritten. For example, you can take the return from this method, and
	 * express it as the <code>SRC</code> attribute in an HTML
	 * <code>&lt;IMG&gt;</code> tag.
	 * </p>
	 * <p>
	 * If the given boolean switch is set to <code>true</code>, this method
	 * works exactly like
	 * <code>getLocalizedFileURL(PortletRequest,PortletResponse,String)</code>
	 * (see). If the boolean switch is set to <code>false</code>, this method
	 * works mostly like the other one, but it does not actually localize the
	 * returned filename. Instead, the returned URL just references the base
	 * filename if it existed, and if it did not exist, the returned URL is
	 * null.
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
	 * @return A file-relay URL, for downloading the best-fit localized version
	 *         of the base file from the portlet resource bundle folder, or null
	 *         if no qualifying file was found.
	 * 
	 */
	public static String getLocalizedFileURL(PortletRequest pReq,
			PortletResponse pResp, String pBaseFileName, boolean pLocalized) {

		if (resourceBundleDir == null) {
			return null;
		}
		if (pReq == null || pBaseFileName == null) {
			return null;
		}
		pBaseFileName = pBaseFileName.trim();
		if (pBaseFileName.length() == 0) {
			return null;
		}
		String fileName = getLocalizedFileName(resourceBundleDir,
				pBaseFileName, pReq.getLocale(), pLocalized);
		if (fileName != null) {
			fileName = getFileRelayURL(pResp, fileName);
		}
		return fileName;
	}

	/**
	 * <p>
	 * Returns a file-relay URL (suitable for presentation to the user) for
	 * downloading a localized portlet resource bundle file, where the filename
	 * is retrieved (properly localized) from the portlet's message resources
	 * using the given key and default. The returned URL is ready for
	 * presentation to the user in the portlet response; it does not need to be
	 * encoded or rewritten. For example, you can take the return from this
	 * method, and express it as the <code>SRC</code> attribute in an HTML
	 * <code>&lt;IMG&gt;</code> tag.
	 * </p>
	 * <p>
	 * This method works like
	 * <code>getLocalizedFileURL(PortletRequest,PortletResponse,String)</code>,
	 * but in which the localized filename string is instead taken from a
	 * message resource of the portlet, using the given key and default
	 * filename. Thus this method relies on the best-candidate filename for each
	 * locale already existing in the localized message properties for that
	 * locale, at the given key. (And if the key is not found, it applies the
	 * given default filename.) Thus the method just reads the filename from the
	 * message, assumes that file is the best one for that locale, and returns a
	 * URL for it accordingly.
	 * </p>
	 * <p>
	 * The file named in the message resource must be located in the portlet
	 * resource bundle folder on the portlet server. The filename from the
	 * message resource may include a subfolder relative to that portlet
	 * resource bundle folder. The path to the portlet resource bundle folder is
	 * configured in <code>i18n_portlet_config.properties</code>.
	 * </p>
	 * <p>
	 * The default filename is an optional parameter; if left null or blank, and
	 * the key is not found in the available messages, then null is returned.
	 * Null is also returned if the file named in the message resource does not
	 * actually exist, or if the portlet request or key parameters were null.
	 * </p>
	 * <p>
	 * <b>Note:</b> This method uses the <code>getMessage</code> method to
	 * determine the proper filename. Then, it uses the
	 * <code>getFileRelayURL</code> method in this class to generate a
	 * file-relay URL for it. Please see those other methods for additional
	 * documentation.
	 * </p>
	 * <p>
	 * <b>Note:</b> As mentioned, this method relies on you having placed the
	 * proper localized filenames for each locale into the message resources for
	 * each locale. If you would rather not do that, and would rather just have
	 * the system inspect the available files in the resource bundle vis-a-vis
	 * the locale automatically to build the URL, use the companion
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
	 *         file from the portlet resource bundle folder, or null if no
	 *         qualifying file was found.
	 */
	public static String getLocalizedFileURL(PortletRequest pReq,
			PortletResponse pResp, String pKey, String pDefault) {
		if (resourceBundleDir == null) {
			return null;
		}
		if (pReq == null || pKey == null) {
			return null;
		}
		pKey = pKey.trim();
		if (pKey.length() == 0) {
			return null;
		}
		if (pDefault != null) {
			pDefault = pDefault.trim();
		}
		try {
			// get the localized filename from the message resources
			String fileName = getMessage(pReq, pKey, pDefault);

			// check if the file exists - if not, return false.
			if (fileName != null && fileExists(resourceBundleDir, fileName)) {
				return getFileRelayURL(pResp, fileName);
			}
		} catch (Exception ex) {
		}
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
	 * class loader - preferably in the portlet resource bundle folder dedicated
	 * for this purpose. The location of the portlet resource bundle folder is
	 * configured in the <code>i18n_portlet_config.properties</code> file;
	 * this folder should also be specified on the classpath so that the class
	 * loader can find it.
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
