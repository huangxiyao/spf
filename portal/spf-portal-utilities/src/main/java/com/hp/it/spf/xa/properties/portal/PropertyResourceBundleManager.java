package com.hp.it.spf.xa.properties.portal;

import com.epicentric.template.Style;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.vignette.portal.website.enduser.PortalContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <p>
 * A portal-specific property resource bundle management class which extends
 * from the common portal/portlet
 * {@link com.hp.it.spf.xa.properties.PropertyResourceBundleManager}. It
 * supports all the functionality of the parent class, but supports loading from
 * support files contained in the current Vignette portal component as well.
 * Specifically, the portal-specific <code>PropertyResourceBundleManager</code>
 * first attempts to load a file from the current portal component's secondary
 * support files; and if not found, it then delegates to the parent to load from
 * the classpath as usual.
 * </p>
 * <p>
 * In addition, like the common parent class, the portal-specific
 * <code>PropertyResourceBundleManager</code> provides an in-memory cache which
 * automatically refreshes if the file on disk has changed. This class also
 * supports getting best-fit localized versions of property files. And if you do
 * not put the property file into the current component's support files, you can
 * put it anywhere in the classpath accessible to the current thread context
 * classloader.
 * </p>
 * <p>
 * The minimum retention period for property resource bundles in the in-memory
 * cache can be configured in the
 * <code>propertyresourcebundlemanager.properties</code> file. This file may
 * exist anywhere in the classpath accessible to the context class loader.
 * (Note: Since this is a system-wide configuration for static behavior in this
 * class, this class does not load
 * <code>propertyresourcebundlemanager.properties</code> from the current
 * component's support files, so that one component cannot affect system-wide
 * behavior.) As with the common parent class, the
 * <code>propertyresourcebundlemanager.properties</code> may contain a property
 * key, <code>reload.checkPeriod</code>, set to an integer number of seconds;
 * that number is the minimum retention interval. For example (in
 * <code>propertyresourcebundlemanager.properties</code>):
 * </p>
 * <blockquote>
 * 
 * <pre>
 * reload.checkPeriod = 300  # 5 minute minimum cache retention
 * </pre>
 * 
 * </blockquote>
 * <p>
 * Property resource bundles in the cache will be retained in the cache at least
 * that long. After that period has expired, subsequent retrieval attempts will
 * use the context class loader again to check the file on disk for changes
 * first (such as a file modification, removal, or creation of a better-fit
 * localized file). The cache is retained and used if there have been no
 * changes, and refreshed if there have been changes.
 * </p>
 * <p>
 * A positive value for <code>reload.checkPeriod</code> means to cache bundle
 * lookup results for at least that many seconds. A negative value means to
 * cache forever (until restart). A zero value disables the cache; bundle
 * lookups will go against the filesystem every time. The default cache period
 * is currently the value of the
 * {@link PropertyResourceBundleManager#DEFAULT_RELOAD_CHECK_PERIOD} constant.
 * </p>
 * 
 * @author <a href="mailto:ying-zhiw@hp.com">Oliver</a>
 * @version 1.0
 */
public class PropertyResourceBundleManager extends com.hp.it.spf.xa.properties.PropertyResourceBundleManager {
	private static ThreadLocal<PortalContext> tlContext = new ThreadLocal<PortalContext>();

	// logger for the class
	private static final Log LOG = LogFactory.getLog(PropertyResourceBundleManager.class);

	static {
		polymorphismClazz = PropertyResourceBundleManager.class;
	}

    /**
     * <p>
     * Get the current resource bundle for the given properties file, without
     * localization. This method uses the in-memory "hot" cache (refreshed if
     * the file on disk is updated and the configurable cache lifetime has
     * expired). The search for the properties file first occurs in the current
     * Vignette component support files; if the properties file doesn't exist
     * there, then the method searches everywhere within the current classpath
     * using the current context class loader. The method returns null if the
     * properties file cannot be found or has been removed from disk. If there
     * was a problem opening or reading the properties file, a warning is also
     * logged (using Apache commons logging).
     * </p>
     * 
     * @param pContext
     *            Vignette {@code PortalContext}, which is used to lookup
     *            vignette component support files.
     * @param propertiesFilename
     *            The name of the properties file to access. The
     *            <code>.properties</code> file extension is assumed, so the
     *            value you provide does not need to have it (although it is OK
     *            if it does). The name may begin with any file path relative to
     *            the classpath, if desired.
     * @return The resource bundle (null if not found).
     */
	public static ResourceBundle getBundle(PortalContext pContext, String propertiesFilename) {
		return getBundle(pContext, propertiesFilename, null);
	}

    /**
     * <p>
     * Get the current resource bundle for the given base properties file and
     * locale. This method uses the in-memory "hot" cache (refreshed if the file
     * on disk is updated and the configurable cache lifetime has expired). The
     * search for the properties file first occurs in the current Vignette
     * component support files, and if the properties file doesn't exist there,
     * then everywhere within the current classpath using the current context
     * class loader. The search includes looking for the best-fit localized
     * version of the properties file, using the standard lookup sequence based
     * on the given locale (see {@link java.util.ResourceBundle}). The method
     * returns null if the best-fit properties file cannot be found or has been
     * removed from disk. If there was a problem opening or reading the
     * properties file, a warning is also logged (using Apache commons logging).
     * </p>
     * 
     * @param pContext
     *            Vignette {@code PortalContext}, which is used to lookup
     *            vignette component support files.
     * @param basePropertiesFilename
     *            The name of the base properties file for which to access the
     *            best candidate. The <code>.properties</code> extension is
     *            assumed, so the value you provide does not need to have it
     *            (although it is OK if it does). The name may begin with any
     *            file path relative to the classpath, if desired.
     * @param locale
     *            A locale to use to find the best candidate.
     * @return The resource bundle (null if not found).
     */
	public static ResourceBundle getBundle(PortalContext pContext, String basePropertiesFilename, Locale loc) {
		tlContext.set(pContext);
		return com.hp.it.spf.xa.properties.PropertyResourceBundleManager.getBundle(basePropertiesFilename, loc);
	}

    /**
     * <p>
     * Retrieve a property value from the current resource bundle for the given
     * properties file and key, without localization. This method uses the
     * in-memory "hot" cache (refreshed if the file on disk is updated and the
     * configurable cache lifetime has expired). The search for the properties
     * file first occurs in the current Vignette component support files, and if
     * the properties file doesn't exist there, then everywhere within the
     * current classpath using the current context class loader. The method
     * returns null if the properties file cannot be found or has been removed
     * from disk, or if the key is not found in it. If there was a problem
     * opening or reading the properties file, a warning is also logged (using
     * Apache commons logging).
     * </p>
     * 
     * @param pContext
     *            Vignette {@code PortalContext}, which is used to lookup
     *            vignette component support files.
     * @param propertiesFilename
     *            The name of the properties file to access. The
     *            <code>.properties</code> file extension is assumed, so the
     *            value you provide does not need to have it (although it is OK
     *            if it does). The name may begin with any file path relative to
     *            the classpath, if desired.
     * @param key
     *            A property key.
     * @return The property value (null if not found).
     */
	public static String getString(PortalContext pContext, String propertiesFilename, String key) {
		return getString(pContext, propertiesFilename, null, key, null);
	}

    /**
     * <p>
     * Retrieve a property value (or the given default) from the current
     * resource bundle for the given properties file and key, without
     * localization. This method uses the in-memory "hot" cache (refreshed if
     * file on disk is updated and the configurable cache lifetime has expired).
     * The search for the properties file first occurs in the current Vignette
     * component support files, and if the properties file doesn't exist, then
     * everywhere within the current classpath using the current context class
     * loader. The method returns the given default value if the properties file
     * cannot be found or has been removed from disk, or if the key is not found
     * in it. If there was a problem opening or reading the properties file, a
     * warning is also logged (using Apache commons logging).
     * </p>
     * 
     * @param pContext
     *            Vignette {@code PortalContext}, which is used to lookup
     *            vignette component support files.
     * @param propertiesFilename
     *            The name of the properties file to access. The
     *            <code>.properties</code> file extension is assumed, so the
     *            value you provide does not need to have it (although it is OK
     *            if it does). The name may begin with any file path relative to
     *            the classpath, if desired.
     * @param key
     *            A property key.
     * @param defaultValue
     *            A default property value.
     * @return The property value (or the default if not found).
     */
	public static String getString(PortalContext pContext, String propertiesFilename, String key, String defaultValue) {

		return getString(pContext, propertiesFilename, null, key, defaultValue);
	}

    /**
     * <p>
     * Retrieve a property value from the current resource bundle for the given
     * base properties file, key and locale. This method uses the in-memory
     * "hot" cache (refreshed if the file on disk is updated and the
     * configurable cache lifetime has expired). The search for the properties
     * file first occurs in the current Vignette component support files, and if
     * the properties file doesn't exist, then everywhere within the current
     * classpath using the current context class loader. The search includes
     * looking for the best-fit localized version of the properties file, using
     * the standard lookup sequence based on the given locale (see
     * {@link java.util.ResourceBundle}). The method returns null if the
     * best-fit properties file cannot be found or has been removed from disk,
     * or if the key is not found in it. If there was a problem opening or
     * reading the properties file, a warning is also logged (using Apache
     * commons logging).
     * </p>
     * 
     * @param pContext
     *            Vignette {@code PortalContext}, which is used to lookup
     *            vignette component support files.
     * @param basePropertiesFilename
     *            The name of the base properties file for which to access the
     *            best candidate. The <code>.properties</code> extension is
     *            assumed, so the value you provide does not need to have it
     *            (although it is OK if it does). The name may begin with any
     *            file path relative to the classpath, if desired.
     * @param locale
     *            A locale to use to find the best candidate.
     * @param key
     *            A property key.
     * @return The property value (null if not found).
     */
	public static String getString(PortalContext pContext, String basePropertiesFilename, Locale loc, String key) {

		return getString(pContext, basePropertiesFilename, loc, key, null);
	}

    /**
     * <p>
     * Retrieve a property value (or the given default) from the current
     * resource bundle for the given base properties file, key and locale. This
     * method uses the in-memory "hot" cache (refreshed if file on disk is
     * updated and the configurable cache lifetime has expired). The search for
     * the properties file first occurs in the current Vignette component
     * support files; then if the properties file doesn't exist, it occurs
     * everywhere within the current classpath using the current context class
     * loader. The search includes looking for the best-fit localized version of
     * the properties file, using the standard lookup sequence based on the
     * given locale (see {@link java.util.ResourceBundle}). The method returns
     * the given default if the best-fit properties file cannot be found or has
     * been removed from disk, or if the key is not found in it. If there was a
     * problem opening or reading the properties file, a warning is also logged
     * (using Apache commons logging).
     * </p>
     * 
     * @param pContext
     *            Vignette {@code PortalContext}, which is used to lookup
     *            vignette component support files.
     * @param basePropertiesFilename
     *            The name of the base properties file for which to access the
     *            best candidate. The <code>.properties</code> extension is
     *            assumed, so the value you provide does not need to have it
     *            (although it is OK if it does). The name may begin with any
     *            file path relative to the classpath, if desired.
     * @param locale
     *            A locale to use to find the best candidate.
     * @param key
     *            A property key.
     * @param defaultValue
     *            A default propery value.
     * @return The property value (or the default if not found).
     */
	public static String getString(PortalContext pContext,
								   String basePropertiesFilename,
								   Locale loc,
								   String key,
								   String defaultValue) {

		ResourceBundle rb = getBundle(pContext, basePropertiesFilename, loc);
		if (rb != null) {
			try {
				return rb.getString(key);
			} catch (Exception ex) {
				return defaultValue;
			}
		} else {
			return defaultValue;
		}
	}

	/**
	 * This method will try to retrieve the resource bundle from vignette component by the specified file name.
	 * <p>
	 * If the resource bundle doesn't exist, then delegate to the parent to lookup the corresponding resource bundle.
	 * </p>
	 *
	 * @param filename
	 *            resource bundle name.
	 * @return {@code File} object if exist, otherwise {@code null} will be returned.
	 */
	protected static File getFile(String filename) {
		File file = getFileFromVignette(filename);
		if (file == null) {
			file = com.hp.it.spf.xa.properties.PropertyResourceBundleManager.getFile(filename);
		}

		return file;
	}

	/**
	 * Retrieve the File from Vignette component by the specified file name.
	 *
	 * @param filename
	 *            resource bundle name
	 * @return {@code File} object if exist, otherwise {@code null} will be returned.
	 */
	private static File getFileFromVignette(String filename) {
		File file = null;
		PortalContext pContext = tlContext.get();
		if (pContext == null) {
			return null;
		}

		Style thisStyleObject = pContext.getCurrentStyle();
		if (thisStyleObject == null) {
			thisStyleObject = pContext.getCurrentSecondaryPage();
		}
		String relPath = thisStyleObject.getUrlSafeRelativePath();

		// get the absolute path for the current portal component - this
		// is the path to the location of its secondary support files
		String absPath = pContext.getPortalRequest().getSession().getServletContext().getRealPath(relPath) + "/";
		String filePath = Utils.slashify(absPath + "/" + filename);
		InputStream is = null;
		try {
			file = new File(filePath);

			file.exists();
			file.isFile();
			file.canRead();
			// need to check if the file is exist or not.
			is = new FileInputStream(file);
		} catch (Exception e) {
			LOG.warn("Problem instantiating property file ".concat(filename)
														   .concat(" from portal component ")
														   .concat(thisStyleObject.getUID())
														   .concat(", error message is: ")
														   .concat(e.getMessage()));
			file = null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOG.warn("Close input stream failed, file:" + filePath, e);
				}
			}
		}
		return file;
	}
}
