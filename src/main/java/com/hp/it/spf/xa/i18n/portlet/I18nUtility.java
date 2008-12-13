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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;

import com.hp.it.spf.xa.misc.portlet.Utils;
import com.hp.it.spf.xa.help.ContextualHelpProvider;
import com.hp.it.spf.xa.help.ContextualHelpUtility;

/**
 * A class of useful internationalization/localization methods for use by
 * portlets.
 * 
 * @author <link href="liping.yan@hp.com">Liping Yan</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see com.hp.it.cas.spf.common.utils.I18nUtility
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
	public static final String PORTLET_I18N_CONFIG_PROP_RELAY_SERVLET_PATH = "servlet.fileRelay.path";

	/**
	 * Logger instance.
	 */
	private static Log logger = LogFactory.getLog(I18nUtility.class);

	/**
	 * Relay servlet name (actual value is configured in property file).
	 */
	private static String relayServletPath = null;

	/**
	 * Portlet resource bundle directory (actual value is configured in property
	 * file).
	 */
	private static String resourceBundleDir = null;

	/**
	 * Static block to initialize relay servlet name and portlet resource bundle
	 * directory from configuration file. TODO: This may need to change to
	 * support remoted portlets. Ie instead of relay servlet, some other
	 * mechanism (eg portlet resource request) may be needed.
	 */
	static {
		try {
			ResourceBundle bundle = ResourceBundle
					.getBundle(I18nUtility.PORTLET_I18N_CONFIG_FILE);
			relayServletPath = "/"
					+ bundle
							.getString(I18nUtility.PORTLET_I18N_CONFIG_PROP_RELAY_SERVLET_PATH)
					+ "/";
			relayServletPath = slashify(relayServletPath);
			resourceBundleDir = "/"
					+ bundle
							.getString(I18nUtility.PORTLET_I18N_CONFIG_PROP_BUNDLE_DIR)
					+ "/";
			resourceBundleDir = slashify(resourceBundleDir);
		} catch (Exception ex) {
			logger
					.error("I18nUtility: Failed to open "
							+ I18nUtility.PORTLET_I18N_CONFIG_FILE
							+ " properties file");
			logger.error("I18nUtility: " + ex.getMessage());
			relayServletPath = null;
			resourceBundleDir = null;
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
	 * <dt><code>getLocalizedFileName(request, "html/foo.htm")</code> when
	 * <code>request</code> contains Canada French (<code>Locale.CANADA_FRENCH</code>)</dt>
	 * <dd>returns
	 * <code><i>portlet-resource-folder</i>/html/foo_fr_CA.htm</code></dd>
	 * 
	 * <dt><code>getLocalizedFileName(request, "/html/foo.htm")</code> when
	 * <code>request</code> contains France French (<code>Locale.FRANCE</code>)</dt>
	 * <dd>returns <code><i>portlet-resource-folder</i>/html/foo_fr.htm</code></dd>
	 * 
	 * <dt><code>getLocalizedFileName(request, "html/foo.htm")</code> when
	 * <code>request</code> contains generic Italian (<code>Locale.ITALIAN</code>)</dt>
	 * <dd>returns <code><i>portlet-resource-folder</i>/html/foo.htm</code></dd>
	 * </dt>
	 * 
	 * <dt><code>getLocalizedFileName(request, "html/bar.htm")</code></dt>
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
		String fileName = getLocalizedFilePath(resourceBundleDir,
				pBaseFileName, pReq.getLocale(), pLocalized);
		if (fileName == null) {
			return null;
		}
		return slashify(resourceBundleDir + fileName);
	}

	/**
	 * <p>
	 * Returns a URL (suitable for presentation to the user) for downloading a
	 * proper localized version of the given base filename of a portlet resource
	 * bundle. The returned URL is ready for presentation to the user in the
	 * portlet response; it does not need to be encoded or rewritten. For
	 * example, you can take the return from this method, and express it as the
	 * <code>SRC=</code> attribute in an <code>&lt;IMG&gt;</code> tag.
	 * </p>
	 * <p>
	 * The given filename must be a base filename for a portlet resource bundle,
	 * located in the portlet resource bundle folder on the portlet server. The
	 * given filename may include a subfolder relative to that portlet resource
	 * bundle folder. The path to the portlet resource bundle folder is
	 * configured in <code>i18n_portlet_config.properties</code>.
	 * </p>
	 * <p>
	 * This method uses the locale from the given portlet request to find the
	 * best-candidate localized version of the given file in the resource
	 * bundle. The logic for finding the best-candidate is the same as that used
	 * by the Java-standard ResourceBundle class. Thus the URL returned by this
	 * method will point to the best-candidate locale-tagged filename, or the
	 * given base filename if there is no better fit. If the resource bundle
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
	 * <dt><code>getLocalizedFileURL(request, "html/foo.htm")</code> when
	 * <code>request</code> contains Canada French (<code>Locale.CANADA_FRENCH</code>)</dt>
	 * <dd>returns a URL for <code>html/foo_fr_CA.htm</code> in the portlet
	 * resource bundle folder. (The exact URL may vary depending on the
	 * implementation of this method, and is not documented here.)</dd>
	 * 
	 * <dt><code>getLocalizedFileURL(request, "/html/foo.htm")</code> when
	 * <code>request</code> contains France French (<code>Locale.FRANCE</code>)</dt>
	 * <dd>returns a URL for <code>html/foo_fr.htm</code>.</dd>
	 * 
	 * <dt><code>getLocalizedFileURL(request, "html/foo.htm")</code> when
	 * <code>request</code> contains generic Italian (<code>Locale.ITALIAN</code>)</dt>
	 * <dd>returns a URL for <code>html/foo.htm</code></dd>
	 * </dt>
	 * 
	 * <dt><code>getLocalizedFileURL(request, "html/bar.htm")</code></dt>
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
	 * @return A URL for downloading the best-fit localized version of the base
	 *         file from the portlet resource bundle folder, or null if no
	 *         qualifying file was found.
	 * 
	 */
	public static String getLocalizedFileURL(PortletRequest pReq,
			String pBaseFileName) {
		return getLocalizedFileURL(pReq, pBaseFileName, true);
	}

	/**
	 * <p>
	 * Returns a URL (suitable for presentation to the user) for downloading the
	 * given version of the given portlet resource bundle file (ie, localized to
	 * best-fit the locale in the given request, or not, per the boolean
	 * switch). The returned URL is ready for presentation to the user in the
	 * portlet response; it does not need to be encoded or rewritten. For
	 * example, you can take the return from this method, and express it as the
	 * <code>SRC=</code> attribute in an <code>&lt;IMG&gt;</code> tag.
	 * </p>
	 * <p>
	 * If the boolean switch is set to <code>true</code>, this method works
	 * exactly like <code>getLocalizedFileURL(PortletRequest,String)</code>
	 * (see). If the boolean switch is set to <code>false</code>, this method
	 * does not actually localize the returned filename. Instead, the returned
	 * filename points to the base filename if it existed, and null otherwise.
	 * </p>
	 * 
	 * @param pReq
	 *            The portlet request.
	 * @param pBaseFileName
	 *            The name of the base file to search.
	 * @param pLocalized
	 *            The version of the file for which to search: the base file
	 *            (false) or the best-fitting localized file (true).
	 * @return A URL for downloading the best-fit localized version of the base
	 *         file from the portlet resource bundle folder, or null if no
	 *         qualifying file was found.
	 * 
	 */
	public static String getLocalizedFileURL(PortletRequest pReq,
			String pBaseFileName, boolean pLocalized) {

		if (relayServletPath == null || resourceBundleDir == null) {
			return null;
		}
		if (pReq == null || pBaseFileName == null) {
			return null;
		}
		String fileName = getLocalizedFilePath(resourceBundleDir,
				pBaseFileName, pReq.getLocale(), pLocalized);
		// TODO: at present the URL is formed by adding the relay servlet path
		// to the returned filename. This may need to change to support remoted
		// portlets.
		if (fileName != null) {
			fileName = slashify(relayServletPath + fileName);
		}
		return fileName;
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
	 * Parameter substitution is as per the Java standard behavior for
	 * MessageFormat. No parameter substitution is performed if the given
	 * parameters are empty or null, or the message string contains no parameter
	 * placeholders. Note there are 2 kinds of parameters: general string
	 * parameters, and contextual-help parameters:
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
	 * populate any special contextual-help tokens (<code>&lt;Contextual_Help&gt;...&lt;/Contextual_Help&gt;</code>
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
	 * switch) is returned if the message is not found. If you gave a null
	 * default string, then null is returned. If the given request or key are
	 * null, or the application context cannot be found, then null is returned.
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param key
	 *            A message key.
	 * @param params
	 *            An array of parameters to interpolate into the message string.
	 * @param locale
	 *            The locale to assume (not necessarily the same as the one
	 *            inside the request).
	 * @param defaultMsg
	 *            A default message to return if the message string is not
	 *            found.
	 * @param cParams
	 *            An array of contextual-help providers to interpolate into the
	 *            message string.
	 * @param escapeHTML
	 *            Whether to escape any HTML special characters found in the
	 *            final message string.
	 * @return The message string, according to the above parameters.
	 */
	public static String getI18nValue(PortletRequest request, String key,
			Object[] params, Locale locale, String defaultMsg,
			ContextualHelpProvider[] cParams, boolean escapeHTML) {

		if (key == null || request == null) {
			return null;
		}
		if (locale == null) {
			locale = request.getLocale();
		}
		String msg = null;
		ApplicationContext ac = Utils.getApplicationContext(request);
		if (ac != null) {
			msg = ac.getMessage(key, params, defaultMsg, locale);
			msg = parseNoLocalization(msg);
			msg = ContextualHelpUtility.parseContextualHelp(msg, cParams, escapeHTML);
		}
		return msg;
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * in any configured portlet resource bundle; the returned string is
	 * localized for the given locale, interpolated with the given parameters,
	 * and defaulted with the given default message string if not found. This
	 * method works exactly like
	 * <code>getI18nValue(PortletRequest,String,Object[],Locale,String,ContextualHelpProvider[],false)</code>
	 * (see).
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param key
	 *            A message key.
	 * @param params
	 *            An array of parameters to interpolate into the message string.
	 * @param locale
	 *            The locale to assume (not necessarily the same as the one
	 *            inside the request).
	 * @param defaultMsg
	 *            A default message to return if the message string is not
	 *            found.
	 * @return The message string, according to the above parameters.
	 */
	public static String getI18nValue(PortletRequest request, String key,
			Object[] params, Locale locale, String defaultMsg,
			ContextualHelpProvider[] cParams) {
		return getI18nValue(request, key, params, locale, defaultMsg, cParams,
				false);
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * in any configured portlet resource bundle; the returned string is
	 * localized for the given locale, interpolated with the given parameters,
	 * and defaulted with the given default message string if not found. This
	 * method works exactly like
	 * <code>getI18nValue(PortletRequest,String,Object[],Locale,String,null,false)</code>
	 * (see).
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param key
	 *            A message key.
	 * @param params
	 *            An array of parameters to interpolate into the message string.
	 * @param locale
	 *            The locale to assume (not necessarily the same as the one
	 *            inside the request).
	 * @param defaultMsg
	 *            A default message to return if the message string is not
	 *            found.
	 * @return The message string, according to the above parameters.
	 */
	public static String getI18nValue(PortletRequest request, String key,
			Object[] params, Locale locale, String defaultMsg) {
		return getI18nValue(request, key, params, locale, defaultMsg, null,
				false);
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * in any configured portlet resource bundle; the returned string is
	 * localized for the given locale and interpolated with the given
	 * parameters. This method works exactly like
	 * <code>getI18nValue(PortletRequest,String,Object[],Locale,null,null,false)</code>
	 * (see).
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param key
	 *            A message key.
	 * @param params
	 *            An array of parameters to interpolate into the message string.
	 * @param locale
	 *            The locale to assume (not necessarily the same as the one
	 *            inside the request).
	 * @return The message string, according to the above parameters.
	 */
	public static String getI18nValue(PortletRequest request, String key,
			Object[] params, Locale locale) {
		return getI18nValue(request, key, params, locale, null, null, false);
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * in any configured portlet resource bundle; the returned string is
	 * localized for the locale of the given request, and interpolated with the
	 * given parameters. This method works exactly like
	 * <code>getI18nValue(PortletRequest,String,Object[],null,null,null,false)</code>
	 * (see).
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
	public static String getI18nValue(PortletRequest request, String key,
			Object[] params) {
		return getI18nValue(request, key, params, null, null, null, false);
	}

	/**
	 * <p>
	 * Returns a message string for the given key from a message properties file
	 * in any configured portlet resource bundle; the returned string is
	 * localized for the locale of the given request. No parameter substitution
	 * is performed. This method works exactly like
	 * <code>getI18nValue(PortletRequest,String,null,null,null,null,false)</code>
	 * (see).
	 * </p>
	 * 
	 * @param request
	 *            The portlet request.
	 * @param key
	 *            A message key.
	 * @return The message string, according to the above parameters.
	 */
	public static String getI18nValue(PortletRequest request, String key) {
		return getI18nValue(request, key, null, null, null, null, false);
	}

}
