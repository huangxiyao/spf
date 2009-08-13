/**
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 * 
 */
package com.hp.it.spf.xa.properties;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.it.spf.xa.misc.Utils;

/**
 * <p>
 * A manager class for getting a property file as a resource bundle, with an
 * in-memory cache which automatically refreshes if the file on disk has
 * changed. This class also supports getting best-fit localized versions of
 * property files. The property file may exist anywhere in the classpath
 * accessible to the context class loader.
 * </p>
 * <p>
 * The minimum retention period for property resource bundles in the in-memory
 * cache can be configured in the
 * <code>propertyresourcebundlemanager.properties</code> file. This file may
 * exist anywhere in the classpath accessible to the context class loader. It
 * may contain a property key, <code>reload.checkPeriod</code>, set to an
 * integer number of seconds; that number is the minimum retention interval. For
 * example (in <code>propertyresourcebundlemanager.properties</code>):
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
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class PropertyResourceBundleManager {

	// inner object for storing info about a property file for the cache
	protected static class PropertyResourceBundleInfo {
		ResourceBundle rb; // cached resource bundle from file
		long mtime; // file modify timestamp
		long ctime; // last time this record was checked for updates
	}

	// logger for the class
	private static final Log LOG = LogFactory
			.getLog(PropertyResourceBundleManager.class);

	// file extension assumed for all properties files
	private static final String SUFFIX = ".properties";

	// default minimum retention interval assumed for cached properties - in
	// seconds (negative means forever) -- set to 0 for backward-compatibility
	// (prior versions of this class did not support the retention period
	// feature -- ie their retention period was zero -- so set our default to
	// zero so the prior behavior reverts by default)
	protected static final int DEFAULT_RELOAD_CHECK_PERIOD = 0;

	// name of property file configuring minimum retention interval
	private static final String CONFIG_FILE = "propertyresourcebundlemanager.properties";

	// name of property configuring minimum retention interval
	private static final String RELOAD_CHECK_PERIOD_KEY = "reload.checkPeriod";

	// in-memory cache of ResourceBundle objects --
	// use ConcurrentHashMap because it is internally synchronized so none of
	// the logic in this class needs to be explicitly synchronized
	// DSJ 2009/8/11
	private static final Map<String, PropertyResourceBundleInfo> p_map = new ConcurrentHashMap<String, PropertyResourceBundleInfo>();

	// minimum retention interval, in seconds
	private static int reloadCheckPeriod = DEFAULT_RELOAD_CHECK_PERIOD;
	// same, in millis
	private static int reloadCheckMillis = reloadCheckPeriod * 1000;

	// static block to load the minimum retention interval from file
	static {
		try {
			// use context classloader instead of classloader for this class,
			// for consistency with getFile method (see) which also uses
			// context classloader
			ResourceBundle rb = ResourceBundle.getBundle(
					getFilenameWithoutExtension(CONFIG_FILE), Locale
							.getDefault(), Thread.currentThread()
							.getContextClassLoader());
			int configuredReloadCheckPeriod = DEFAULT_RELOAD_CHECK_PERIOD;
			try {
				configuredReloadCheckPeriod = new Integer(rb.getString(
						RELOAD_CHECK_PERIOD_KEY).trim()).intValue();
				reloadCheckPeriod = configuredReloadCheckPeriod;
				reloadCheckMillis = reloadCheckPeriod * 1000;
			} catch (Exception ex) {
				LOG.warn(
						"Cannot parse configuration file for PropertyResourceBundleManager ("
								+ CONFIG_FILE + "): " + ex.getMessage(), ex);
			}
		} catch (MissingResourceException mre) {
			LOG
					.warn("Cannot find configuration file for PropertyResourceBundleManager ("
							+ CONFIG_FILE
							+ ") using context classloader.  Reverting to default configuration.");
		}
		LOG.info("PropertyResourceBundleManager reload check period set to "
				+ reloadCheckPeriod + " seconds.");
	}

	/**
	 * <p>
	 * Get the current resource bundle for the given properties file, without
	 * localization. This method uses the in-memory "hot" cache (refreshed if
	 * the file on disk is updated within the configured retention interval).
	 * The search for the properties file occurs everywhere within the current
	 * classpath using the standard system class loader. The method returns null
	 * if the properties file cannot be found or has been removed from disk. If
	 * there was a problem opening or reading the properties file, a warning is
	 * also logged (using Apache commons logging).
	 * </p>
	 * 
	 * @param propertiesFilename
	 *            The name of the properties file to access. The
	 *            <code>.properties</code> file extension is assumed, so the
	 *            value you provide does not need to have it (although it is OK
	 *            if it does). The name may begin with any file path relative to
	 *            the classpath, if desired.
	 * @return The resource bundle (null if not found).
	 */
	public static ResourceBundle getBundle(String propertiesFilename) {
		return getBundle(propertiesFilename, null);
	}

	/**
	 * <p>
	 * Get the current resource bundle for the given base properties file and
	 * locale. This method uses the in-memory "hot" cache (refreshed if the file
	 * on disk is updated within the configured retention interval). The search
	 * for the properties file occurs everywhere within the current classpath
	 * using the standard system class loader. The search includes looking for
	 * the best-fit localized version of the properties file, using the standard
	 * lookup sequence based on the given locale (see
	 * {@link java.util.ResourceBundle}). The method returns null if the
	 * best-fit properties file cannot be found or has been removed from disk.
	 * If there was a problem opening or reading the properties file, a warning
	 * is also logged (using Apache commons logging).
	 * </p>
	 * 
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
	public static ResourceBundle getBundle(String basePropertiesFilename,
			Locale loc) {

		// if filename is not passed or blank, then return null
		if (basePropertiesFilename == null)
			return null;
		basePropertiesFilename = basePropertiesFilename.trim();
		if (basePropertiesFilename.length() == 0)
			return null;
		// lookup and return the resultant property file (taking locale into
		// account) using memory cache and/or filesystem (depending on state)
		ResourceBundle rb = getBestBundleFromMap(basePropertiesFilename, loc);
		if (rb == null)
			rb = getBestBundleFromFile(basePropertiesFilename, loc);
		if (rb == null)
			LOG.warn("Cannot find property file "
					+ getFilenameWithExtension(basePropertiesFilename)
					+ " using cache or context classloader.");
		return rb;
	}

	/**
	 * <p>
	 * Retrieve a property value from the current resource bundle for the given
	 * properties file and key, without localization. This method uses the
	 * in-memory "hot" cache (refreshed if the file on disk is updated within
	 * the configured retention interval). The search for the properties file
	 * occurs everywhere within the current classpath using the standard system
	 * class loader. The method returns null if the properties file cannot be
	 * found or has been removed from disk, or if the key is not found in it. If
	 * there was a problem opening or reading the properties file, a warning is
	 * also logged (using Apache commons logging).
	 * </p>
	 * 
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
	public static String getString(String propertiesFilename, String key) {
		return getString(propertiesFilename, null, key, null);
	}

	/**
	 * <p>
	 * Retrieve a property value (or the given default) from the current
	 * resource bundle for the given properties file and key, without
	 * localization. This method uses the in-memory "hot" cache (refreshed if
	 * file on disk is updated within the configured retention interval). The
	 * search for the properties file occurs everywhere within the current
	 * classpath using the standard system class loader. The method returns the
	 * given default value if the properties file cannot be found or has been
	 * removed from disk, or if the key is not found in it. If there was a
	 * problem opening or reading the properties file, a warning is also logged
	 * (using Apache commons logging).
	 * </p>
	 * 
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
	public static String getString(String propertiesFilename, String key,
			String defaultValue) {
		return getString(propertiesFilename, null, key, defaultValue);
	}

	/**
	 * <p>
	 * Retrieve a property value from the current resource bundle for the given
	 * base properties file, key and locale. This method uses the in-memory
	 * "hot" cache (refreshed if the file on disk is updated within the
	 * configured retention interval). The search for the properties file occurs
	 * everywhere within the current classpath using the standard system class
	 * loader. The search includes looking for the best-fit localized version of
	 * the properties file, using the standard lookup sequence based on the
	 * given locale (see {@link java.util.ResourceBundle}). The method returns
	 * null if the best-fit properties file cannot be found or has been removed
	 * from disk, or if the key is not found in it. If there was a problem
	 * opening or reading the properties file, a warning is also logged (using
	 * Apache commons logging).
	 * </p>
	 * 
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
	public static String getString(String basePropertiesFilename, Locale loc,
			String key) {
		return getString(basePropertiesFilename, loc, key, null);
	}

	/**
	 * <p>
	 * Retrieve a property value (or the given default) from the current
	 * resource bundle for the given base properties file, key and locale. This
	 * method uses the in-memory "hot" cache (refreshed if file on disk is
	 * updated within the configured retention interval). The search for the
	 * properties file occurs everywhere within the current classpath using the
	 * standard system class loader. The search includes looking for the
	 * best-fit localized version of the properties file, using the standard
	 * lookup sequence based on the given locale (see
	 * {@link java.util.ResourceBundle}). The method returns the given default
	 * if the best-fit properties file cannot be found or has been removed from
	 * disk, or if the key is not found in it. If there was a problem opening or
	 * reading the properties file, a warning is also logged (using Apache
	 * commons logging).
	 * </p>
	 * 
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
	public static String getString(String basePropertiesFilename, Locale loc,
			String key, String defaultValue) {

		ResourceBundle rb = getBundle(basePropertiesFilename, loc);
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
	 * Use the in-memory cache to find the best-fit version of the given
	 * properties base filename for the given locale, hot-refreshing the cache
	 * from the classloader as needed. Return null if not found in the cache.
	 * 
	 * @param basePropertiesFilename
	 *            (with or without extension)
	 * @param loc
	 * @return
	 */
	private static ResourceBundle getBestBundleFromMap(
			String basePropertiesFilename, Locale loc) {

		// by default use given base filename as the actual filename
		String filename = getFilenameWithExtension(basePropertiesFilename);
		String basename = getFilenameWithoutExtension(basePropertiesFilename);
		ResourceBundle rb = null;

		// if given a locale, search in map for best candidate -- this follows
		// the Java-standard lookup sequence and filenaming conventions based on
		// language, country and variant
		if (loc != null) {
			// try given locale first
			String lang = loc.getLanguage();
			String cc = loc.getCountry();
			String var = loc.getVariant();
			// try basename_lang_cc_var (given locale)
			filename = basename + "_" + lang + "_" + cc + "_" + var;
			filename = getFilenameWithExtension(filename);
			if ((rb = getBundleFromMap(filename)) != null)
				return rb;
			// try basename_lang_cc (given locale)
			filename = basename + "_" + lang + "_" + cc;
			filename = getFilenameWithExtension(filename);
			if ((rb = getBundleFromMap(filename)) != null)
				return rb;
			// try basename_lang (given locale)
			filename = basename + "_" + lang;
			filename = getFilenameWithExtension(filename);
			if ((rb = getBundleFromMap(filename)) != null)
				return rb;
			// try default locale next
			Locale defLoc = Locale.getDefault();
			String defLang = defLoc.getLanguage();
			String defCc = defLoc.getCountry();
			String defVar = defLoc.getVariant();
			// try basename_lang_cc_var (default locale)
			filename = basename + "_" + defLang + "_" + defCc + "_" + defVar;
			filename = getFilenameWithExtension(filename);
			if ((rb = getBundleFromMap(filename)) != null)
				return rb;
			// try basename_lang_cc (default locale)
			filename = basename + "_" + defLang + "_" + defCc;
			filename = getFilenameWithExtension(filename);
			if ((rb = getBundleFromMap(filename)) != null)
				return rb;
			// try basename_lang (default locale)
			filename = basename + "_" + defLang;
			filename = getFilenameWithExtension(filename);
			if ((rb = getBundleFromMap(filename)) != null)
				return rb;
		}

		// if no bundle returned yet, search in map for base filename
		return getBundleFromMap(filename);
	}

	/**
	 * Use the filesystem (via the classloader) to find the best-fit version of
	 * the given properties base filename for the given locale, and load it into
	 * the cache. Return null if the file is not found.
	 * 
	 * @param basePropertiesFilename
	 *            (with or without extension)
	 * @param loc
	 * @return
	 */
	private static ResourceBundle getBestBundleFromFile(
			String basePropertiesFilename, Locale loc) {

		// by default use given base filename as the actual filename
		String filename = getFilenameWithExtension(basePropertiesFilename);
		String basename = getFilenameWithoutExtension(basePropertiesFilename);
		ResourceBundle rb = null;

		// if given a locale, search for best candidate -- this follows
		// the Java-standard lookup sequence and filenaming conventions based on
		// language, country and variant
		if (loc != null) {
			// try given locale first
			String lang = loc.getLanguage();
			String cc = loc.getCountry();
			String var = loc.getVariant();
			// try basename_lang_cc_var (given locale)
			filename = basename + "_" + lang + "_" + cc + "_" + var;
			filename = getFilenameWithExtension(filename);
			if ((rb = getBundleFromFile(filename)) != null)
				return rb;
			// try basename_lang_cc (given locale)
			filename = basename + "_" + lang + "_" + cc;
			filename = getFilenameWithExtension(filename);
			if ((rb = getBundleFromFile(filename)) != null)
				return rb;
			// try basename_lang (given locale)
			filename = basename + "_" + lang;
			filename = getFilenameWithExtension(filename);
			if ((rb = getBundleFromFile(filename)) != null)
				return rb;
			// try default locale next
			Locale defLoc = Locale.getDefault();
			String defLang = defLoc.getLanguage();
			String defCc = defLoc.getCountry();
			String defVar = defLoc.getVariant();
			// try basename_lang_cc_var (default locale)
			filename = basename + "_" + defLang + "_" + defCc + "_" + defVar;
			filename = getFilenameWithExtension(filename);
			if ((rb = getBundleFromFile(filename)) != null)
				return rb;
			// try basename_lang_cc (default locale)
			filename = basename + "_" + defLang + "_" + defCc;
			filename = getFilenameWithExtension(filename);
			if ((rb = getBundleFromFile(filename)) != null)
				return rb;
			// try basename_lang (default locale)
			filename = basename + "_" + defLang;
			filename = getFilenameWithExtension(filename);
			if ((rb = getBundleFromFile(filename)) != null)
				return rb;
		}

		// if no bundle returned yet, search in map for base filename
		return getBundleFromFile(filename);
	}

	/**
	 * Look in the in-memory cache for the given filename and return its bundle
	 * or null if not found or the retention period has expired.
	 * 
	 * @param filename
	 *            (with extension)
	 * @return
	 */
	private static ResourceBundle getBundleFromMap(String filename) {

		// from ResourceBundle getBundle Javadoc: "Candidate bundle names where
		// the final component is an empty string are omitted." Here this is
		// indicated when the candidate filename ends with "_" (see calling
		// method: getFileFromCandidates). So return short in that case.
		if (getFilenameWithoutExtension(filename).endsWith("_"))
			return null;

		// lookup the filename in the in-memory cache (return null if not found)
		PropertyResourceBundleInfo info = p_map.get(filename);
		if (info == null)
			return null;
		ResourceBundle rb = info.rb;
		long ctime = info.ctime;

		// if it is still recent, return the bundle from the cache - otherwise
		// return null
		if (!isExpired(ctime))
			return rb;
		else
			return null;
	}

	/**
	 * Look in the filesystem (via the classloader) for the given filename and
	 * return its bundle (loaded into cache as a side-effect) or null if not
	 * found. If the file was already in cache and not stale
	 * 
	 * @param filename
	 *            (with extension)
	 * @return
	 */
	private static ResourceBundle getBundleFromFile(String filename) {

		// from ResourceBundle getBundle Javadoc: "Candidate bundle names where
		// the final component is an empty string are omitted." Here this is
		// indicated when the candidate filename ends with "_" (see calling
		// method: getFileFromCandidates). So return short in that case.
		if (getFilenameWithoutExtension(filename).endsWith("_"))
			return null;

		// use classloader to lookup file on the classpath
		File file = getFile(filename);

		// lookup the filename in the in-memory cache and if it exists then
		// refresh cache as needed and return from cache.
		PropertyResourceBundleInfo info = p_map.get(filename);
		if (info != null) {
			ResourceBundle rb = info.rb;
			long mtime = info.mtime;
			// if not found on the filesystem, remove it from cache and return
			// null
			if (file == null) {
				p_map.remove(filename);
				return null;
			}
			// if found on the filesystem and not modified, update the
			// check-time in
			// cache and return the cached bundle
			if (!isModified(file, mtime)) {
				info.ctime = System.currentTimeMillis();
				p_map.put(filename, info);
				return rb;
			}
		}

		// if not found on the filesystem, return null - otherwise file was on
		// the filesystem and either not in cache or cache was
		// stale - so return the file contents and load cache as side-effect
		if (file != null)
			return setBundleIntoMap(filename, file);
		else
			return null;
	}

	/**
	 * Add the given file information for the given filename to the in-memory
	 * cache. Return the resource bundle that was loaded or null if there was a
	 * problem.
	 * 
	 * @param filename
	 *            (with extension)
	 * @param file
	 * @return
	 */
	private static ResourceBundle setBundleIntoMap(String filename, File file) {
		// first load the properties file from the filesystem (return null if
		// error)
		ResourceBundle rb = null;
		InputStream in = null;
		try {
			in = new BufferedInputStream(Utils
					.getResourceAsStream(getFilenameWithExtension(filename)));
		} catch (Exception e) {
			LOG.warn("Problem opening property file: " + filename + ": "
					+ e.getMessage(), e);
			return null;
		}
		try {
			rb = new PropertyResourceBundle(in);
		} catch (Exception e) {
			LOG.warn("Problem reading or parsing property file: " + filename
					+ ": " + e.getMessage(), e);
			return null;
		}
		try {
			in.close();
		} catch (Exception e) {
			LOG.warn("Problem closing property file: " + filename + ": "
					+ e.getMessage(), e);
		}
		// next cache the properties file and return it
		PropertyResourceBundleInfo info = new PropertyResourceBundleInfo();
		info.rb = rb;
		info.mtime = file.lastModified();
		info.ctime = System.currentTimeMillis();
		p_map.put(filename, info);
		return rb;
	}

	/**
	 * Return true if the given last-check-time has expired yet -- ie, the
	 * current clocktime is later than that last-check-time plus the retention
	 * period. The retention period is configured in the
	 * <code>propertyresourcebundlemanager.properties</code> file, as
	 * described in the class documentation (see). A negative retention period
	 * means no last-check-time in cache ever expires.
	 * 
	 * @param ctimeFromCache
	 *            The last-check-time from cache.
	 * @return
	 */
	private static boolean isExpired(long ctimeFromCache) {
		if (reloadCheckMillis < 0)
			return false;
		long now = System.currentTimeMillis();
		return ctimeFromCache + reloadCheckMillis < now;
	}

	/**
	 * Return true if the given file has a different modify-timestamp than the
	 * given one from cache.
	 * 
	 * @param file
	 *            The file on disk.
	 * @param mtimeFromCache
	 *            The modify-timestamp from cache for the file of the same name.
	 * @return
	 */
	private static boolean isModified(File file, long mtimeFromCache) {
		long mtime = file.lastModified();
		return mtimeFromCache != mtime;
	}

	/**
	 * Trim and append <code>.properties</code> file suffix if it doesn't
	 * exist. This method is idempotent.
	 * 
	 * @param filename
	 *            properties file name
	 * @return real filename
	 */
	private static String getFilenameWithExtension(String filename) {
		// if file suffix does not exist, then append suffix
		if (filename != null) {
			filename = filename.trim();
			if (!filename.toLowerCase().endsWith(SUFFIX)) {
				filename = filename + SUFFIX;
			}
		}
		return filename;
	}

	/**
	 * Trim and remove <code>.properties</code> file suffix if it exists. This
	 * method is idempotent (assuming the <code>.properties</code> suffix
	 * occurs at most once).
	 * 
	 * @param filename
	 *            properties file name
	 * @return base filename
	 */
	private static String getFilenameWithoutExtension(String filename) {
		// if file suffix exists, chop it off
		if (filename != null) {
			filename = filename.trim();
			if (filename.toLowerCase().endsWith(SUFFIX)) {
				filename = filename.substring(0, filename.length()
						- SUFFIX.length());
			}
		}
		return filename;
	}

	/**
	 * Get a file for the given filename using the context classloader. Returns
	 * null if the file is not found, or is not a file, or is not readable, or
	 * any exception occurs.
	 * 
	 * @param filename
	 *            (with extension)
	 * @return File
	 */
	private static File getFile(String filename) {
		URL url = null;
		File file = null;
		// use the context classloader to lookup the file in the filesystem -
		// return null if not found
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			url = cl.getResource(filename);
			if (url == null)
				return null;
		} catch (Exception e) {
			LOG.warn("Problem finding property file " + filename
					+ " with context classloader: " + e.getMessage(), e);
			return null;
		}
		// now instantiate a File object for the file - return null if failure
		try {
			file = new File(url.getPath());
			// the following predicates should always be true at this point, but
			// serve to test for SecurityException and to make sure
			if (file.exists() && file.isFile() && file.canRead())
				return file;
			else
				return null;
		} catch (Exception e) {
			LOG.warn("Problem instantiating property file " + filename + ": "
					+ e.getMessage(), e);
			return null;
		}
	}
}
