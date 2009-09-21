package com.hp.it.spf.xa.properties;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;

/**
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * 
 */
public class PropertyResourceBundleManagerTest extends TestCase {
	String propertiesFilename = "propertyresourcebundlemanagertest";
	String fileExtension = ".properties";
	String checkPeriodKey = "reload.checkPeriod";

	public void testGetBundleString() {
		String defaultCheckPeriodValue = getOriginalConfigedValue(
				propertiesFilename.concat(fileExtension), checkPeriodKey);

		System.out
				.println("1.1: Try to retrieve a non-existent resource bundle file from in-memory cache.");
		ResourceBundle rbNotExist = PropertyResourceBundleManager
				.getBundle("notexist");
		assertNull(rbNotExist);

		System.out
				.println("1.2: Try to retrieve value from specified properties which has no extension with the specified key.");
		ResourceBundle rbFileWithoutExtension = PropertyResourceBundleManager
				.getBundle(propertiesFilename);
		assertEquals(defaultCheckPeriodValue, rbFileWithoutExtension
				.getString(checkPeriodKey));

		System.out
				.println("1.3: Try to retrieve the same bundle when now specifying the file extension.");
		ResourceBundle rbFileWithExtension = PropertyResourceBundleManager
				.getBundle(propertiesFilename.concat(fileExtension));
		assertSame(rbFileWithoutExtension, rbFileWithExtension);

		System.out
				.println("1.4: Remove resource bundle file from filesystem during cache retention, then make sure it can still be retrieved.");
		// from in-memory cache if it already exists in cache
		removeFile(propertiesFilename.concat(fileExtension));
		ResourceBundle rbAfterFileRemoved = PropertyResourceBundleManager
				.getBundle(propertiesFilename);
		assertSame(rbFileWithoutExtension, rbAfterFileRemoved);
		recoverFile(propertiesFilename.concat(fileExtension).concat(".bak"),
				".bak");

		System.out
				.println("1.5: Change the reloadCheckMillis to 0, force time to expire, but the resource bundle is not modified, so make sure same bundle from cache is returned.");
		dynModifyReloadCheckMillisValue(0);
		// force thread to sleep 1 millisec, then do the test
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}
		ResourceBundle rbAfterPropertyNOChangedForceRefresh = PropertyResourceBundleManager
				.getBundle(propertiesFilename);
		assertSame(rbFileWithoutExtension, rbAfterPropertyNOChangedForceRefresh);
		// recover
		dynModifyReloadCheckMillisValue(new Integer(Integer
				.valueOf(defaultCheckPeriodValue) * 1000).intValue());

		System.out
				.println("1.6: Change the reload.checkPeriod to 1000, but time is not expired, so make sure cache will be used.");
		modifyValueInProperties(propertiesFilename.concat(fileExtension),
				checkPeriodKey, "1000");
		ResourceBundle rbAfterPropertyChanged = PropertyResourceBundleManager
				.getBundle(propertiesFilename);
		assertSame(rbFileWithoutExtension, rbAfterPropertyChanged);
		assertEquals(defaultCheckPeriodValue, rbAfterPropertyChanged
				.getString(checkPeriodKey));
		// recover
		modifyValueInProperties(propertiesFilename.concat(fileExtension),
				checkPeriodKey, defaultCheckPeriodValue);

		System.out
				.println("1.7: Change the reloadCheckMillis to 0, force time to expire, modify the properties, and make sure modified bundle is returned.");
		dynModifyReloadCheckMillisValue(0);
		modifyValueInProperties(propertiesFilename.concat(fileExtension),
				checkPeriodKey, "100000");
		// force thread to sleep 1 millisec, then do the test
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}
		ResourceBundle rbAfterPropertyChangedForceRefresh = PropertyResourceBundleManager
				.getBundle(propertiesFilename);
		assertNotSame(rbFileWithoutExtension,
				rbAfterPropertyChangedForceRefresh);
		assertEquals("100000", rbAfterPropertyChangedForceRefresh
				.getString(checkPeriodKey));
		// recover
		modifyValueInProperties(propertiesFilename.concat(fileExtension),
				checkPeriodKey, defaultCheckPeriodValue);
		dynModifyReloadCheckMillisValue(new Integer(Integer
				.valueOf(defaultCheckPeriodValue) * 1000).intValue());

		System.out
				.println("1.8: Remove resource bundle file from filesystem, then force refresh the cache, and make sure null is returned.");
		removeFile(propertiesFilename.concat(fileExtension));
		dynModifyReloadCheckMillisValue(0);
		// again force thread to sleep 1 millisec, then do the test
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}
		ResourceBundle rbAfterFileRemovedAndCacheRefresh = PropertyResourceBundleManager
				.getBundle(propertiesFilename);
		assertNull(rbAfterFileRemovedAndCacheRefresh);
		dynModifyReloadCheckMillisValue(new Integer(Integer
				.valueOf(defaultCheckPeriodValue) * 1000).intValue());
		recoverFile(propertiesFilename.concat(fileExtension).concat(".bak"),
				".bak");

		System.out
				.println("1.9: Recover resource bundle file to filesystem, then do not force-refresh the cache, and make sure null still returned.");
		ResourceBundle rbAfterFileRestoredAndNoRefresh = PropertyResourceBundleManager
				.getBundle(propertiesFilename);
		assertNull(rbAfterFileRestoredAndNoRefresh);

		System.out
				.println("1.10: Now file is recovered, force refresh the cache, and make sure bundle is returned.");
		dynModifyReloadCheckMillisValue(0);
		// again force thread to sleep 1 millisec, then do the test
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}
		ResourceBundle rbAfterFileRestoredAndRefresh = PropertyResourceBundleManager
				.getBundle(propertiesFilename);
		assertEquals(defaultCheckPeriodValue, rbAfterFileRestoredAndRefresh
				.getString(checkPeriodKey));
		// recover
		dynModifyReloadCheckMillisValue(new Integer(Integer
				.valueOf(defaultCheckPeriodValue) * 1000).intValue());

	}

	public void testGetBundleStringLocale() {
		String defaultCheckPeriodValue = getOriginalConfigedValue(
				propertiesFilename.concat(fileExtension), checkPeriodKey);

		System.out.println("2.1: Find properties for locale zh.");
		ResourceBundle rb_zh = PropertyResourceBundleManager.getBundle(
				propertiesFilename, new Locale("zh"));
		assertEquals("zh", rb_zh.getString("locale"));

		// Fix for QC CR #141 results in update to this test case. Before the
		// fix, this would find the zh file already loaded in cache, because
		// cache was global. So even though for zh-CN there is a better file
		// than zh which exists, it would not be found. Now, after CR #141 fix,
		// the locale is part of the cache key, so cache is no longer global but
		// is per-locale. Thus the zh-CN file will be found now.
		System.out.println("2.2: Find properties for locale zh-CN.");
		ResourceBundle rb_zh_CN = PropertyResourceBundleManager.getBundle(
				propertiesFilename, Locale.CHINA);
		assertNotSame(rb_zh, rb_zh_CN);
		assertEquals("zh_CN", rb_zh_CN.getString("locale"));

		System.out
				.println("2.3: Repeat 2.2 but force time to expire and make sure zh-CN is still returned.");
		// change the reloadCheckMillis to 0, force time to expire.
		dynModifyReloadCheckMillisValue(0);
		// sleep 1 milliseconds to avoid an issue
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}
		ResourceBundle rb_zh_CN_Refresh = PropertyResourceBundleManager
				.getBundle(propertiesFilename, Locale.CHINA);
		assertNotSame(rb_zh, rb_zh_CN_Refresh);
		assertEquals("zh_CN", rb_zh_CN_Refresh.getString("locale"));
		// recover
		dynModifyReloadCheckMillisValue(new Integer(Integer
				.valueOf(defaultCheckPeriodValue) * 1000).intValue());

		System.out
				.println("2.4: zh is still in cache so make sure it is still returned for locale zh.");
		// though the zh_CN is loaded, zh is still in cache and not modified
		ResourceBundle rb_zh_After_zh_CN_loaded = PropertyResourceBundleManager
				.getBundle(propertiesFilename, new Locale("zh"));
		assertSame(rb_zh, rb_zh_After_zh_CN_loaded);
		assertEquals("zh", rb_zh_After_zh_CN_loaded.getString("locale"));

		System.out
				.println("2.5: Make sure zh-CN is still in cache.");
		// zh_CN is still in cache and not modified
		ResourceBundle rb_zh_CN_After_zh_CN_loaded = PropertyResourceBundleManager
				.getBundle(propertiesFilename, Locale.CHINA);
		assertSame(rb_zh_CN, rb_zh_CN_After_zh_CN_loaded);
		assertEquals("zh_CN", rb_zh_CN_After_zh_CN_loaded.getString("locale"));

	}

	/**
	 * rename file to backup file
	 * 
	 * @param filename
	 *            file name
	 */
	private void removeFile(String filename) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();
		URL url = cl.getResource(filename);
		File file = new File(url.getPath());
		File target = new File(url.getPath().concat(".bak"));
		try {
			FileUtils.copyFile(file, target);
			FileUtils.deleteQuietly(file);
		} catch (IOException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	/**
	 * recover the file which is processed by removeFile method
	 * 
	 * @param filename
	 *            file name which contains the specified file extension
	 * @param fileExtension
	 *            '.bak' suffix
	 */
	private void recoverFile(String filename, String fileExtension) {
		if (filename.endsWith(fileExtension)) {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			URL url = cl.getResource(filename);
			File file = new File(url.getPath());
			File target = new File(url.getPath().substring(0,
					url.getPath().length() - fileExtension.length()));
			try {
				FileUtils.copyFile(file, target);
				FileUtils.deleteQuietly(file);
			} catch (IOException e) {
				assertFalse(e.getMessage(), true);
			}
		}
	}

	/**
	 * modify the value in specified properties with the specified key
	 * 
	 * @param filename
	 * @param key
	 * @param value
	 */
	private void modifyValueInProperties(String filename, String key,
			String value) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();

		Properties properties = new Properties();
		InputStream is = null;
		FileOutputStream fop = null;
		try {
			is = cl.getResourceAsStream(filename);
			properties.load(is);
			properties.setProperty(key, value);
			fop = new FileOutputStream(cl.getResource(filename).getPath());
			properties.store(fop, "write by junit test.");
		} catch (IOException e) {
			assertFalse(e.getMessage(), true);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			if (fop != null) {
				try {
					fop.close();
				} catch (IOException e) {
				}
			}
		}

	}

	/**
	 * get the configed value in the specified properties file with specified
	 * key
	 * 
	 * @param filename
	 * @param key
	 * @return the configed value
	 */
	private String getOriginalConfigedValue(String filename, String key) {
		ClassLoader cl = Thread.currentThread().getContextClassLoader();

		Properties properties = new Properties();
		InputStream is = null;
		try {
			is = cl.getResourceAsStream(filename);
			properties.load(is);
			return String.valueOf(properties.get(key));
		} catch (IOException e) {
			assertFalse(e.getMessage(), true);
			return null;
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * use java reflect to modify the reloadCheckMillis primitive value
	 * 
	 * @param i
	 */
	private void dynModifyReloadCheckMillisValue(int i) {
		Class clazz = PropertyResourceBundleManager.class;
		try {
			Field field = clazz.getDeclaredField("reloadCheckMillis");
			field.setAccessible(true);
			field.setInt(PropertyResourceBundleManager.class, i);
			field.setAccessible(false);
		} catch (SecurityException e) {
			assertFalse(e.getMessage(), true);
		} catch (NoSuchFieldException e) {
			assertFalse(e.getMessage(), true);
		} catch (IllegalArgumentException e) {
			assertFalse(e.getMessage(), true);
		} catch (IllegalAccessException e) {
			assertFalse(e.getMessage(), true);
		}
	}
}
