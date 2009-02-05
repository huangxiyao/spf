/**
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 * 
 */
package com.hp.it.spf.xa.properties;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.it.spf.xa.misc.Utils;

/**
 * <p>
 * A manager class for getting a property file as a resource bundle, with an
 * in-memory cache which automatically refreshes if the file on disk has
 * changed. This class also supports getting localized versions of property
 * files.
 * </p>
 * 
 * @author <link href="ying-zhi.wu@hp.com">Oliver</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class PropertyResourceBundleManager {
	private static final Log LOG = LogFactory
			.getLog(PropertyResourceBundleManager.class);

	// in-memory cache of ResourceBundle objects
	private static final Map p_map = new HashMap();

	private static final String SUFFIX = ".properties";

	/**
	 * <p>
	 * Get the current resource bundle for the given properties file, without
	 * localization. This method uses the in-memory "hot" cache (refreshed if
	 * file on disk is updated). The properties file is searched for anywhere
	 * within the current classpath using the standard system class loader. The
	 * method returns null if the properties file cannot be found or has been
	 * removed from disk. If there was a problem opening or reading the
	 * properties file, a warning is also logged (using Apache commons logging).
	 * </p>
	 * 
	 * @param propertiesName
	 *            A properties file name.
	 * @return The resource bundle (null if not found).
	 */
	public static synchronized ResourceBundle getBundle(String propertiesName) {
		return getBundle(propertiesName, null);
	}

	/**
	 * <p>
	 * Get the current resource bundle for the given base properties file and
	 * locale. This method uses the in-memory "hot" cache (refreshed if file on
	 * disk is updated). The properties file is searched for anywhere within the
	 * current classpath using the standard system class loader. The search
	 * includes looking for the best-fit localized version of the properties
	 * file, using the standard search sequence (see ResourceBundle). The method
	 * returns null if the best-fit properties file cannot be found or has been
	 * removed from disk. If there was a problem opening or reading the
	 * properties file, a warning is also logged (using Apache commons logging).
	 * </p>
	 * 
	 * @param propertiesName
	 *            A base properties file name.
	 * @param locale
	 *            A locale to use to find the best candidate.
	 * @return The resource bundle (null if not found).
	 */
	public static synchronized ResourceBundle getBundle(String propertiesName,
			Locale loc) {
		// if propertiesName is not passed, then return null
		if (propertiesName == null || "".equals(propertiesName.trim()))
			return null;
		propertiesName = getFileName(propertiesName);
		// if locale is passed, then use it to find a candidate property file,
		// otherwise use the given property file - DSJ 2008/8/6
		if (loc != null)
			propertiesName = getFileNameFromCandidates(propertiesName, loc);
		// lookup and return the resultant property file from cache
		if (p_map.containsKey(propertiesName)) {
			return getResourceBundleFromMap(propertiesName);
		} else {
			return setResourceBundleIntoMap(propertiesName);
		}
	}

	/**
	 * <p>
	 * Retrieve a property value from the current resource bundle for the given
	 * properties file and key, without localization. This method uses the
	 * in-memory "hot" cache (refreshed if file on disk is updated). The
	 * properties file is searched for anywhere within the current classpath
	 * using the standard system class loader. The method returns null if the
	 * properties file cannot be found or has been removed from disk, or if the
	 * key is not found in the resource bundle. If there was a problem opening
	 * or reading the properties file, a warning is also logged (using Apache
	 * commons logging).
	 * </p>
	 * 
	 * @param propertiesName
	 *            A properties file name.
	 * @param key
	 *            A property key.
	 * @return The property value (null if not found).
	 */
	public static String getString(String propertiesName, String key) {
		return getString(propertiesName, null, key, null);
	}

	/**
	 * <p>
	 * Retrieve a property value (or the given default) from the current
	 * resource bundle for the given properties file and key, without
	 * localization. This method uses the in-memory "hot" cache (refreshed if
	 * file on disk is updated). The properties file is searched for anywhere
	 * within the current classpath using the standard system class loader. The
	 * method returns the given default value if the properties file cannot be
	 * found or has been removed from disk, or if the key is not found in the
	 * resource bundle. If there was a problem opening or reading the properties
	 * file, a warning is also logged (using Apache commons logging).
	 * </p>
	 * 
	 * @param propertiesName
	 *            A properties file name.
	 * @param key
	 *            A property key.
	 * @param defaultValue
	 *            A default property value.
	 * @return The property value (or the default if not found).
	 */
	public static String getString(String propertiesName, String key,
			String defaultValue) {
		return getString(propertiesName, null, key, defaultValue);
	}

	/**
	 * <p>
	 * Retrieve a property value from the current resource bundle for the given
	 * base properties file, key and locale. This method uses the in-memory
	 * "hot" cache (refreshed if file on disk is updated). The properties file
	 * is searched for anywhere within the current classpath using the standard
	 * system class loader. The search includes looking for the best-fit
	 * localized version of the properties file, using the standard search
	 * sequence (see ResourceBundle). The method returns null if the best-fit
	 * properties file cannot be found or has been removed from disk, or if the
	 * key is not found in the resource bundle. If there was a problem opening
	 * or reading the properties file, a warning is also logged (using Apache
	 * commons logging).
	 * </p>
	 * 
	 * @param propertiesName
	 *            A properties file name.
	 * @param locale
	 *            A locale to use to find the best candidate.
	 * @param key
	 *            A property key.
	 * @return The property value (null if not found).
	 */
	public static String getString(String propertiesName, Locale loc, String key) {
		return getString(propertiesName, loc, key, null);
	}

	/**
	 * <p>
	 * Retrieve a property value (or the given default) from the current
	 * resource bundle for the given base properties file, key and locale. This
	 * method uses the in-memory "hot" cache (refreshed if file on disk is
	 * updated). The properties file is searched for anywhere within the current
	 * classpath using the standard system class loader. The search includes
	 * looking for the best-fit localized version of the properties file, using
	 * the standard search sequence (see ResourceBundle). The method returns the
	 * given default if the best-fit properties file cannot be found or has been
	 * removed from disk, or if the key is not found in the resource bundle. If
	 * there was a problem opening or reading the properties file, a warning is
	 * also logged (using Apache commons logging).
	 * </p>
	 * 
	 * @param propertiesName
	 *            A base properties file name.
	 * @param locale
	 *            A locale to use to find the best candidate.
	 * @param key
	 *            A property key.
	 * @param defaultValue
	 *            A default propery value.
	 * @return The property value (or the default if not found).
	 */
	public static String getString(String propertiesName, Locale loc,
			String key, String defaultValue) {

		ResourceBundle rb = getBundle(propertiesName, loc);
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
	 * use the given locale to find the best candidate for the given properties
	 * filename (this uses the same file search sequence as the Java standard
	 * ResourceBundle getBundle method)
	 * 
	 * @param filename
	 *            properties file name
	 * @return best candidate properties file name for the locale
	 */
	private static String getFileNameFromCandidates(String filename, Locale loc) {

		String basename = getBaseName(filename); // Be sure we have the base
		// name.
		String lang = loc.getLanguage();
		String cc = loc.getCountry();
		String var = loc.getVariant();

		// See Javadoc for ResourceBundle.getBundle for explanation.
		filename = basename + "_" + lang + "_" + cc + "_" + var;
		if (!candidateExists(filename)) {
			filename = basename + "_" + lang + "_" + cc;
			if (!candidateExists(filename)) {
				filename = basename + "_" + lang;
				if (!candidateExists(filename)) {
					Locale defLoc = Locale.getDefault();
					lang = defLoc.getLanguage();
					cc = defLoc.getCountry();
					var = defLoc.getVariant();
					filename = basename + "_" + lang + "_" + cc + "_" + var;
					if (!candidateExists(filename)) {
						filename = basename + "_" + lang + "_" + cc;
						if (!candidateExists(filename)) {
							filename = basename + "_" + lang;
							if (!candidateExists(filename)) {
								filename = basename;
							}
						}
					}
				}
			}
		}
		return getFileName(filename); // Convert back to file name.
	}

	/**
	 * return true if the candidate property file exists, otherwise false
	 * 
	 * @param filename
	 *            candidate properties file name
	 * @return true if the candidate properties file exists, otherwise false
	 */
	private static boolean candidateExists(String filename) {
		// From ResourceBundle getBundle Javadoc: "Candidate bundle names where
		// the final component is an empty string are omitted." Here this is
		// indicated when the candidate filename ends with "_" (see calling
		// method: getFileFromCandidates).
		if (filename.endsWith("_")) {
			return false;
		}
		try {
			File file = getFile(filename);
			return (file != null) && file.exists() && file.isFile()
					&& file.canRead();
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * trim and append .properties file suffix if it doesn't exist, otherwise
	 * PropertiesConfiguration will throw an exception (this method is
	 * idempotent)
	 * 
	 * @param filename
	 *            properties file name
	 * @return real filename
	 */
	private static String getFileName(String filename) {
		// if file suffix does not exist, then append suffix
		filename = filename.trim();
		if (!filename.toLowerCase().endsWith(SUFFIX)) {
			filename = filename + SUFFIX;
		}
		return filename;
	}

	/**
	 * trim and remove .properties file suffix if it exists (this method is NOT
	 * idempotent)
	 * 
	 * @param filename
	 *            properties file name
	 * @return base filename
	 */
	private static String getBaseName(String filename) {
		// if file suffix exists, chop it off
		filename = filename.trim();
		if (filename.toLowerCase().endsWith(SUFFIX)) {
			filename = filename.substring(0, filename.length()
					- SUFFIX.length());
		}
		return filename;
	}

	/**
	 * get ResourceBundle from map with key, if the related file is modified,
	 * then update the relevant ResourceBundle
	 * 
	 * @param key
	 * @return ResourceBundle
	 */
	private static ResourceBundle getResourceBundleFromMap(String filename) {
		try {
			// if the file is modified
			if (isModified(filename)) {
				return updateResourceBundleInMap(filename);
			} else {
				return (ResourceBundle) ((Object[]) p_map.get(filename))[0];
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Set PropertyResourceBundle into map - log warning if failure (eg file not
	 * found).
	 * 
	 * @param filename
	 * @return ResourceBundle
	 */
	private static ResourceBundle setResourceBundleIntoMap(String filename) {
		File file = null;
		try {
			file = getFile(filename);
		} catch (Exception e) {
			LOG.warn("Cannot find properties file with system classloader: "
					+ filename);
			return null;
		}
		if (file != null) {
			try {
				// updated by ck for consistency change the code to likewise use
				// the new Utils.getResourceAsStream method to open the
				// properties file off the WEB-INF/classes/ and/or CLASSPATH.
				// (CR:1000776072 )
				// ClassLoader cl =
				// Thread.currentThread().getContextClassLoader();
				// InputStream in = new
				// BufferedInputStream(cl.getResourceAsStream(getFileName(filename)));
				InputStream in = new BufferedInputStream(Utils
						.getResourceAsStream(getFileName(filename)));
				ResourceBundle rb = new PropertyResourceBundle(in);
				// get lastModified
				Long lastModified = new Long(file.lastModified());
				p_map.put(filename, new Object[] { rb, lastModified });
				return rb;
			} catch (Exception e) {
				LOG.warn("Problem loading properties file: " + file
						+ "\nException detail: " + e);
				return null;
			}
		} else {
			LOG.warn("Cannot find properties file with system classloader: "
					+ filename);
			return null;
		}
	}

	/**
	 * update PropertyResourceBundle in map
	 * 
	 * @param filename
	 * @return
	 */
	private static ResourceBundle updateResourceBundleInMap(String filename) {
		// remove the exist object. this step is not necessary, because
		// hashmap's key
		// is unique, if you set the object with the same key, the lastest one
		// will recover
		// the previous one
		p_map.remove(filename);
		return setResourceBundleIntoMap(filename);
	}

	/**
	 * check if the file is modified
	 * 
	 * @param filename
	 * @return true or false
	 */
	private static boolean isModified(String filename) throws Exception {
		File file = getFile(filename);
		// file is removed from disk after stored in the cache
		// it can be considered as 'Modified' for its status
		// drop its cache for the file
		// java ResourceBundle will always retain one copy after first loaded,
		// if you want to retain the cache regardless of the existence of the
		// file, here should return false;
		if (file == null)
			return true;
		// last modified time
		long lastModified = file.lastModified();
		// the modified time saved in map
		Object[] obj = ((Object[]) p_map.get(filename));
		long savedLastModified = ((Long) obj[1]).longValue();

		return lastModified > savedLastModified;
	}

	/**
	 * get File object with filename
	 * 
	 * @param filename
	 * @return File
	 */
	private static File getFile(String filename) throws Exception {
		filename = getFileName(filename);

		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL url = cl.getResource(filename);
		if (url == null)
			return null;

		File file = new File(url.getPath());
		return file;
	}
}
