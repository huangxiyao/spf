package com.hp.it.spf.xa.properties.portal;

import com.epicentric.template.Style;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalRequest;
import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Unit tests for {@link com.hp.it.spf.xa.properties.portal.PropertyResourceBundleManager}.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 */
public class PropertyResourceBundleManagerTest extends TestCase {
	String propertiesFilename = "propertyresourcebundlemanagertest";
	String fileExtension = ".properties";
	String vignettecomponent = "vignettecomponet";
	String checkPeriodKey = "reload.checkPeriod";

	private Mockery context = null;
	private PortalContext pContext = null;

	@Override
	protected void setUp() throws Exception {
		context = new Mockery();
		// set imposteriser to instance and CGLIB will be used,
		// If this value is not assigned, dynamic Proxy will be used.
		context.setImposteriser(ClassImposteriser.INSTANCE);
		context.setDefaultResultForType(Object.class, null);

		pContext = context.mock(PortalContext.class);
		context.checking(new Expectations() {
			{
				PortalRequest pRequest = context.mock(PortalRequest.class);
				HttpSession session = context.mock(HttpSession.class);
				ServletContext sc = context.mock(ServletContext.class);
				Style style = context.mock(Style.class);

				allowing(pContext).getPortalRequest();
				will(returnValue(pRequest));

				allowing(pRequest).getSession();
				will(returnValue(session));

				allowing(session).getServletContext();
				will(returnValue(sc));

				ClassLoader cl = Thread.currentThread().getContextClassLoader();
				URL url = cl.getResource(vignettecomponent);
				allowing(sc).getRealPath(with(any(String.class)));
				will(returnValue(url.getPath()));

				allowing(pContext).getCurrentStyle();
				will(returnValue(style));

				allowing(style).getUID();
				will(returnValue("uid12345"));

				allowing(style).getUrlSafeRelativePath();
				will(returnValue("relatepath"));
			}
		});
		super.setUp();
	}

	public void testGetBundleString() {
		String defaultCheckPeriodValue = getOriginalConfigedValue(vignettecomponent.concat("/")
																				   .concat(propertiesFilename)
																				   .concat(fileExtension),
																  checkPeriodKey);

		System.out.println("1.1: Try to retrieve a non-existent resource bundle file from in-memory cache.");
		ResourceBundle rbNotExist = PropertyResourceBundleManager.getBundle(pContext, "notexist");
		assertNull(rbNotExist);

		System.out.println("1.2: Try to retrieve value from specified properties which has no extension with the specified key.");
		ResourceBundle rbFileWithoutExtension = PropertyResourceBundleManager.getBundle(pContext, propertiesFilename);
		assertEquals(defaultCheckPeriodValue, rbFileWithoutExtension.getString(checkPeriodKey));

		System.out.println("1.3: Try to retrieve the same bundle when now specifying the file extension.");
		ResourceBundle rbFileWithExtension = PropertyResourceBundleManager.getBundle(pContext,
																					 propertiesFilename.concat(fileExtension));
		assertSame(rbFileWithoutExtension, rbFileWithExtension);

		System.out.println("1.4: Remove resource bundle file from filesystem during cache retention, then make sure it can still be retrieved.");
		// from in-memory cache if it already exists in cache
		removeFile(vignettecomponent + "/" + propertiesFilename.concat(fileExtension));
		ResourceBundle rbAfterFileRemoved = PropertyResourceBundleManager.getBundle(pContext, propertiesFilename);
		assertSame(rbFileWithoutExtension, rbAfterFileRemoved);
		recoverFile(vignettecomponent + "/" + propertiesFilename.concat(fileExtension).concat(".bak"), ".bak");

		System.out.println("1.5: Change the reloadCheckMillis to 0, force time to expire, but the resource bundle is not modified, so make sure same bundle from cache is returned.");
		dynModifyReloadCheckMillisValue(0);
		// force thread to sleep 100 millis to avoid timing issue, then do the test
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		ResourceBundle rbAfterPropertyNOChangedForceRefresh = PropertyResourceBundleManager.getBundle(pContext,
																									  propertiesFilename);
		assertSame(rbFileWithoutExtension, rbAfterPropertyNOChangedForceRefresh);
		// recover
		dynModifyReloadCheckMillisValue(new Integer(Integer.valueOf(defaultCheckPeriodValue) * 1000).intValue());

		System.out.println("1.6: Change the reload.checkPeriod to 1000, but time is not expired, so make sure cache will be used.");
		modifyValueInProperties(vignettecomponent + "/" + propertiesFilename.concat(fileExtension),
								checkPeriodKey,
								"1000");
		ResourceBundle rbAfterPropertyChanged = PropertyResourceBundleManager.getBundle(pContext, propertiesFilename);
		assertSame(rbFileWithoutExtension, rbAfterPropertyChanged);
		assertEquals(defaultCheckPeriodValue, rbAfterPropertyChanged.getString(checkPeriodKey));
		// recover
		modifyValueInProperties(vignettecomponent + "/" + propertiesFilename.concat(fileExtension),
								checkPeriodKey,
								defaultCheckPeriodValue);

		System.out.println("1.7: Change the reloadCheckMillis to 0, force time to expire, modify the properties, and make sure modified bundle is returned.");
		dynModifyReloadCheckMillisValue(0);
		modifyValueInProperties(vignettecomponent + "/" + propertiesFilename.concat(fileExtension),
								checkPeriodKey,
								"100000");
		// force thread to sleep 100 millis to avoid timing issue, then do the test
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		ResourceBundle rbAfterPropertyChangedForceRefresh = PropertyResourceBundleManager.getBundle(pContext,
																									propertiesFilename);
		assertNotSame(rbFileWithoutExtension, rbAfterPropertyChangedForceRefresh);
		assertEquals("100000", rbAfterPropertyChangedForceRefresh.getString(checkPeriodKey));
		// recover
		modifyValueInProperties(vignettecomponent + "/" + propertiesFilename.concat(fileExtension),
								checkPeriodKey,
								defaultCheckPeriodValue);
		dynModifyReloadCheckMillisValue(new Integer(Integer.valueOf(defaultCheckPeriodValue) * 1000).intValue());

		System.out.println("1.8: Remove resource bundle file from filesystem (mimic vignette component scope), then force refresh the cache, and the properties on the thread local classpath should be retrieved");
		removeFile(vignettecomponent + "/" + propertiesFilename.concat(fileExtension));
		dynModifyReloadCheckMillisValue(0);
		// again force thread to sleep 100 millis to avoid timing issue, then do the test
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		ResourceBundle rbAfterFileRemovedAndCacheRefresh = PropertyResourceBundleManager.getBundle(pContext,
																								   propertiesFilename);

		String checkPeriodValueLoadedInClasspath = getOriginalConfigedValue(propertiesFilename.concat(fileExtension),
																			checkPeriodKey);
		assertEquals(checkPeriodValueLoadedInClasspath, rbAfterFileRemovedAndCacheRefresh.getString(checkPeriodKey));
		dynModifyReloadCheckMillisValue(new Integer(Integer.valueOf(defaultCheckPeriodValue) * 1000).intValue());
		recoverFile(vignettecomponent + "/" + propertiesFilename.concat(fileExtension).concat(".bak"), ".bak");

		System.out.println("1.9: Recover resource bundle file to filesystem, then do not force-refresh the cache, and make sure classpath bundle still be returned.");
		ResourceBundle rbAfterFileRestoredAndNoRefresh = PropertyResourceBundleManager.getBundle(pContext,
																								 propertiesFilename);
		assertSame(rbAfterFileRemovedAndCacheRefresh, rbAfterFileRestoredAndNoRefresh);

		System.out.println("1.10: Now file is recovered, force refresh the cache, and make sure vignette component scope bundle is returned.");
		dynModifyReloadCheckMillisValue(0);
		// again force thread to sleep 1 millis to avoid timing issue, then do the test
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		ResourceBundle rbAfterFileRestoredAndRefresh = PropertyResourceBundleManager.getBundle(pContext,
																							   propertiesFilename);
		assertEquals(defaultCheckPeriodValue, rbAfterFileRestoredAndRefresh.getString(checkPeriodKey));
		// recover
		dynModifyReloadCheckMillisValue(new Integer(Integer.valueOf(defaultCheckPeriodValue) * 1000).intValue());
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
			File target = new File(url.getPath().substring(0, url.getPath().length() - fileExtension.length()));
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
	private void modifyValueInProperties(String filename, String key, String value) {
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
	 * get the configed value in the specified properties file with specified key
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
	@SuppressWarnings("rawtypes")
	private void dynModifyReloadCheckMillisValue(int i) {
		Class clazz = com.hp.it.spf.xa.properties.PropertyResourceBundleManager.class;
		try {
			Field field = clazz.getDeclaredField("reloadCheckMillis");
			field.setAccessible(true);
			field.setInt(com.hp.it.spf.xa.properties.PropertyResourceBundleManager.class, i);
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
