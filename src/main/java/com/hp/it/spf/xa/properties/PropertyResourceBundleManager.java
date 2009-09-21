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
import com.hp.it.spf.xa.i18n.I18nUtility;

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
		// lookup and return the appropriate properties (taking locale into
		// account) using memory cache and/or filesystem (depending on state)
		PropertyResourceBundleInfo info = getBestInfoFromMap(
				basePropertiesFilename, loc);
		if (info == null)
			info = getBestInfoFromFile(basePropertiesFilename, loc);
		if ((info == null) || (info.rb == null)) {
			LOG.warn("Cannot find property file "
					+ getFilenameWithExtension(basePropertiesFilename)
					+ " in cache or classpath.");
			return null;
		} else {
			return info.rb;
		}
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
	 * Search the in-memory cache for information about the best-fit file for
	 * the given properties base filename and locale, taking the cache retention
	 * period into account. Return null if not found in the cache or the
	 * retention period has expired.
	 * 
	 * @param basePropertiesFilename
	 *            (with or without extension)
	 * @param loc
	 * @return
	 */
	private static PropertyResourceBundleInfo getBestInfoFromMap(
			String basePropertiesFilename, Locale loc) {

		// by default use given base filename as the actual filename
		String filename = getFilenameWithExtension(basePropertiesFilename);
		String basename = getFilenameWithoutExtension(basePropertiesFilename);
		PropertyResourceBundleInfo info = null;

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
			if ((info = getInfoFromMap(filename, loc)) != null)
				return info;
			// try basename_lang_cc (given locale)
			filename = basename + "_" + lang + "_" + cc;
			filename = getFilenameWithExtension(filename);
			if ((info = getInfoFromMap(filename, loc)) != null)
				return info;
			// try basename_lang (given locale)
			filename = basename + "_" + lang;
			filename = getFilenameWithExtension(filename);
			if ((info = getInfoFromMap(filename, loc)) != null)
				return info;
			// try default locale next
			Locale defLoc = Locale.getDefault();
			String defLang = defLoc.getLanguage();
			String defCc = defLoc.getCountry();
			String defVar = defLoc.getVariant();
			// try basename_lang_cc_var (default locale)
			filename = basename + "_" + defLang + "_" + defCc + "_" + defVar;
			filename = getFilenameWithExtension(filename);
			if ((info = getInfoFromMap(filename, loc)) != null)
				return info;
			// try basename_lang_cc (default locale)
			filename = basename + "_" + defLang + "_" + defCc;
			filename = getFilenameWithExtension(filename);
			if ((info = getInfoFromMap(filename, loc)) != null)
				return info;
			// try basename_lang (default locale)
			filename = basename + "_" + defLang;
			filename = getFilenameWithExtension(filename);
			if ((info = getInfoFromMap(filename, loc)) != null)
				return info;
			// setup to try basename without locale (fix for QC CR #141 - DSJ
			// 2009/9/21)
			filename = getFilenameWithExtension(basename);
		}

		// if no info returned yet, search in map for base filename
		return getInfoFromMap(filename, loc);
	}

	/**
	 * Use the filesystem (via the classloader) to find the best-fit file for
	 * the given properties base filename and locale. If info for that file is
	 * found in the in-memory cache and the file has not changed, return the
	 * info from the cache. Otherwise (ie if the file is not in cache or it has
	 * changed) load the file info into cache before returning it. Return null
	 * if the file is not found.
	 * 
	 * @param basePropertiesFilename
	 *            (with or without extension)
	 * @param loc
	 * @return
	 */
	private static PropertyResourceBundleInfo getBestInfoFromFile(
			String basePropertiesFilename, Locale loc) {

		// by default use given base filename as the actual filename
		String filename = getFilenameWithExtension(basePropertiesFilename);
		String basename = getFilenameWithoutExtension(basePropertiesFilename);
		PropertyResourceBundleInfo info = null;

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
			if ((info = getInfoFromFile(filename, loc)) != null)
				return info;
			// try basename_lang_cc (given locale)
			filename = basename + "_" + lang + "_" + cc;
			filename = getFilenameWithExtension(filename);
			if ((info = getInfoFromFile(filename, loc)) != null)
				return info;
			// try basename_lang (given locale)
			filename = basename + "_" + lang;
			filename = getFilenameWithExtension(filename);
			if ((info = getInfoFromFile(filename, loc)) != null)
				return info;
			// try default locale next
			Locale defLoc = Locale.getDefault();
			String defLang = defLoc.getLanguage();
			String defCc = defLoc.getCountry();
			String defVar = defLoc.getVariant();
			// try basename_lang_cc_var (default locale)
			filename = basename + "_" + defLang + "_" + defCc + "_" + defVar;
			filename = getFilenameWithExtension(filename);
			if ((info = getInfoFromFile(filename, loc)) != null)
				return info;
			// try basename_lang_cc (default locale)
			filename = basename + "_" + defLang + "_" + defCc;
			filename = getFilenameWithExtension(filename);
			if ((info = getInfoFromFile(filename, loc)) != null)
				return info;
			// try basename_lang (default locale)
			filename = basename + "_" + defLang;
			filename = getFilenameWithExtension(filename);
			if ((info = getInfoFromFile(filename, loc)) != null)
				return info;
			// setup to try basename without locale (fix for QC CR #141 - DSJ
			// 2009/9/21)
			filename = getFilenameWithExtension(basename);
		}

		// if no info returned yet, search for base filename and if still not
		// found, enter info for a null file into the map
		if ((info = getInfoFromFile(filename, loc)) != null)
			return info;
		else
			return setInfoInMap(filename, loc, null);
	}

	/**
	 * Look in the in-memory cache for the given exact filename (including
	 * extension and locale tag if appropriate) and locale, and return its info
	 * or null if not found or the retention period has expired.
	 * 
	 * @param filename
	 *            filename (with extension and including locale tag if
	 *            appropriate)
	 * @param loc
	 *            locale for which that filename is being looked-up
	 * @return
	 */
	private static PropertyResourceBundleInfo getInfoFromMap(String filename,
			Locale loc) {

		// from ResourceBundle getBundle Javadoc: "Candidate bundle names where
		// the final component is an empty string are omitted." Here this is
		// indicated when the candidate filename ends with "_" (see calling
		// method: getFileFromCandidates). So return short in that case.
		if (getFilenameWithoutExtension(filename).endsWith("_"))
			return null;

		// lookup the filename in the in-memory cache (return null if not found)
		PropertyResourceBundleInfo info = p_map.get(getMapKey(filename, loc));
		if (info == null) {
			return null;
		}
		
		// if it is still recent, return the info from the cache - else null
		if (!isExpired(info)) {
			return info;
		} else {
			return null;
		}
	}

	/**
	 * Look in the filesystem (via the classloader) for the given exact filename
	 * (including extension and locale tag if appropriate) and locale, and
	 * return its info (loaded into cache as a side-effect) or null if not
	 * found. If the file was already in cache and not stale, though, just
	 * return its info from cache.
	 * 
	 * @param filename
	 *            filename (with extension and including locale tag if
	 *            appropriate)
	 * @param loc
	 *            locale for which that filename is being looked-up
	 * @return
	 */
	private static PropertyResourceBundleInfo getInfoFromFile(String filename,
			Locale loc) {

		// from ResourceBundle getBundle Javadoc: "Candidate bundle names where
		// the final component is an empty string are omitted." Here this is
		// indicated when the candidate filename ends with "_" (see calling
		// method: getFileFromCandidates). So return short in that case.
		if (getFilenameWithoutExtension(filename).endsWith("_"))
			return null;

		// lookup the filename in the in-memory cache and on the classpath
		File file = getFile(filename);
		PropertyResourceBundleInfo info = p_map.get(getMapKey(filename, loc));
		if (info != null) {
			// if not found on the filesystem, load null file info into cache
			// and return into
			if (file == null)
				return setInfoInMap(filename, loc, null);
			// if found on the filesystem and not modified, update the
			// check-time in cache and return it
			if (!isModified(file, info)) {
				return resetInfoInMap(filename, loc, info);
			}
		}

		// if not found on the filesystem, return null - otherwise file was on
		// the filesystem and either not in cache or cache was stale - so return
		// info from filesystem and load cache as side-effect
		if (file != null)
			return setInfoInMap(filename, loc, file);
		else
			return null;
	}

	/**
	 * Update the last-check-time for the info in the in-memory cache for the
	 * given exact filename and locale, and return it.
	 * 
	 * @param filename
	 *            filename (with extension and including locale tag if
	 *            appropriate)
	 * @param loc
	 *            locale for which that filename is being looked-up
	 * @param info
	 * @return
	 */
	private static PropertyResourceBundleInfo resetInfoInMap(String filename,
			Locale loc, PropertyResourceBundleInfo info) {
		info.ctime = System.currentTimeMillis();
		p_map.put(getMapKey(filename, loc), info);
		return info;
	}

	/**
	 * Add info for the given exact filename (including extension and locale
	 * tag, if appropriate), locale and file handle to the in-memory cache and
	 * return the info. If the given file handle is null, or there is a problem
	 * determining the file info, then null file info is set into the cache and
	 * returned.
	 * 
	 * @param filename
	 *            filename (with extension and including locale tag if
	 *            appropriate)
	 * @param loc
	 *            locale for which that filename is being looked-up
	 * @param file
	 *            (may be null)
	 * @return
	 */
	private static PropertyResourceBundleInfo setInfoInMap(String filename,
			Locale loc, File file) {

		// initialize new bundle info
		PropertyResourceBundleInfo info = new PropertyResourceBundleInfo();
		info.rb = null;
		info.mtime = 0;
		info.ctime = System.currentTimeMillis();

		// if file handle is null it means file did not exist so set
		// initialized bundle info into map and return
		if (file == null) {
			p_map.put(getMapKey(filename, loc), info);
			return info;
		}

		// otherwise load the properties file from the filesystem and set it
		// into the map and return (if error, then set initialized bundle info
		// into map and return)
		InputStream in = null;
		try {
			in = new BufferedInputStream(Utils
					.getResourceAsStream(getFilenameWithExtension(filename)));
			// in = new BufferedInputStream(file.toURL().openStream());
		} catch (Exception e) {
			LOG.warn("Problem opening property file " + filename + ": "
					+ e.getMessage(), e);
			p_map.put(getMapKey(filename, loc), info);
			return info;
		}
		ResourceBundle rb = null;
		try {
			rb = new PropertyResourceBundle(in);
		} catch (Exception e) {
			LOG.warn("Problem reading or parsing property file " + filename
					+ ": " + e.getMessage(), e);
			p_map.put(getMapKey(filename, loc), info);
			return info;
		}
		try {
			in.close();
		} catch (Exception e) {
			LOG.warn("Problem closing property file " + filename + ": "
					+ e.getMessage(), e);
		}
		// next cache the properties file and return it
		info.rb = rb;
		info.mtime = file.lastModified();
		p_map.put(getMapKey(filename, loc), info);
		return info;
	}

	/**
	 * Return true if the given bundle info has expired yet -- ie, the current
	 * clocktime is later than that last-check-time plus the retention period.
	 * The retention period is configured in the
	 * <code>propertyresourcebundlemanager.properties</code> file, as
	 * described in the class documentation (see). A negative retention period
	 * means no last-check-time in cache ever expires.
	 * 
	 * @param info
	 * @return
	 */
	private static boolean isExpired(PropertyResourceBundleInfo info) {
		if (reloadCheckMillis < 0)
			return false;
		long ctimeFromCache = info.ctime;
		long now = System.currentTimeMillis();
		return ctimeFromCache + reloadCheckMillis < now;
	}

	/**
	 * Return true if the given file has a different modify-timestamp than the
	 * given info from cache.
	 * 
	 * @param file
	 * @param info
	 * @return
	 */
	private static boolean isModified(File file, PropertyResourceBundleInfo info) {
		long mtimeFromCache = info.mtime;
		long mtime = file.lastModified();
		return mtimeFromCache != mtime;
	}

	/**
	 * Trim and append <code>.properties</code> file suffix if it doesn't
	 * exist. This method is idempotent.
	 * 
	 * @param filename
	 *            properties file name (with or without extension)
	 * @return properties file name with extension
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
	 *            properties file name (with or without extension)
	 * @return properties file name without extension
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
	 * @return file handle
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
			// the following predicates are used here just to test for
			// SecurityException, so return the file whether they evaluate true
			// or false - because if the file was found by the classloader
			// inside a JAR, these predicates will always return false - so we
			// cannot reliably use them to make sure the file exists, is not a
			// directory, and is readable - anyway when the file is used to
			// create a resource bundle later, then if those conditions are
			// false, it will be caught at that point
			file.exists();
			file.isFile();
			file.canRead();
			return file;
		} catch (Exception e) {
			LOG.warn("Problem instantiating property file " + filename + ": "
					+ e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Return a map key for the given filename and locale (which may be null).
	 */
	private static String getMapKey(String filename, Locale loc) {
		String key = filename;
		if (loc != null)
			key = I18nUtility.localeToLanguageTag(loc) + '/' + key;
		return key;
	}
}
