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
        // retrieve the default reload.checkPeriod value from properties.
        String defaultCheckPeriodValue = getOriginalConfigedValue(propertiesFilename.concat(fileExtension), checkPeriodKey);
        
        // try to retrieve a none existing resource bundle file from in-memory cache
        ResourceBundle rbNotExist = PropertyResourceBundleManager.getBundle("notexist");        
        assertNull(rbNotExist);
        
        // retrieve value from specified properties with has no Extension with the specified key
        ResourceBundle rbFileWithoutExtension = PropertyResourceBundleManager.getBundle(propertiesFilename);        
        assertEquals(defaultCheckPeriodValue, rbFileWithoutExtension.getString(checkPeriodKey));
        
        // retrieve the same bundle which has the been specified the file extension        
        ResourceBundle rbFileWithExtension = PropertyResourceBundleManager.getBundle(propertiesFilename.concat(fileExtension));
        assertSame(rbFileWithoutExtension, rbFileWithExtension);
                
        // remove resource bundle file from filesystem, then it can still be retrieved 
        // from in-memory cache if it already exists in cache
        removeFile(propertiesFilename.concat(fileExtension));
        ResourceBundle rbAfterFileRemoved = PropertyResourceBundleManager.getBundle(propertiesFilename);
        assertSame(rbFileWithoutExtension, rbAfterFileRemoved);       
        recoverFile(propertiesFilename.concat(fileExtension).concat(".bak"), ".bak");
        
        // change the reloadCheckMillis to 0, force time to expire. but the resource bundle is not modified. cache value will be returned.
        dynModifyReloadCheckMillisValue(0);        
        ResourceBundle rbAfterPropertyNOChangedForceRefresh = PropertyResourceBundleManager.getBundle(propertiesFilename);
        assertSame(rbFileWithoutExtension, rbAfterPropertyNOChangedForceRefresh);  
        // recover
        dynModifyReloadCheckMillisValue(new Integer(Integer.valueOf(defaultCheckPeriodValue)*1000).intValue());
        
        // change the reload.checkPeriod to 1000, but time is not expired, cache will be used.
        modifyValueInProperties(propertiesFilename.concat(fileExtension), checkPeriodKey, "1000");
        ResourceBundle rbAfterPropertyChanged = PropertyResourceBundleManager.getBundle(propertiesFilename);
        assertSame(rbFileWithoutExtension, rbAfterPropertyChanged);  
        assertEquals(defaultCheckPeriodValue, rbAfterPropertyChanged.getString(checkPeriodKey));
        // recover
        modifyValueInProperties(propertiesFilename.concat(fileExtension), checkPeriodKey, defaultCheckPeriodValue);
        
        // change the reloadCheckMillis to 0, and modify the properties, force cache to refresh.
        dynModifyReloadCheckMillisValue(0);
        modifyValueInProperties(propertiesFilename.concat(fileExtension), checkPeriodKey, "100000");
        ResourceBundle rbAfterPropertyChangedForceRefresh = PropertyResourceBundleManager.getBundle(propertiesFilename);
        assertNotSame(rbFileWithoutExtension, rbAfterPropertyChangedForceRefresh);  
        assertEquals("100000", rbAfterPropertyChangedForceRefresh.getString(checkPeriodKey));
        // recover
        modifyValueInProperties(propertiesFilename.concat(fileExtension), checkPeriodKey, defaultCheckPeriodValue);
        dynModifyReloadCheckMillisValue(new Integer(Integer.valueOf(defaultCheckPeriodValue)*1000).intValue());
                      
        // remove resource bundle file from filesystem, then force to refresh the cache, null will be returned.
        removeFile(propertiesFilename.concat(fileExtension));
        dynModifyReloadCheckMillisValue(0);
        ResourceBundle rbAfterFileRemovedAndCacheRefresh = PropertyResourceBundleManager.getBundle(propertiesFilename);
        assertNull(rbAfterFileRemovedAndCacheRefresh);  
        dynModifyReloadCheckMillisValue(new Integer(Integer.valueOf(defaultCheckPeriodValue)*1000).intValue());
        recoverFile(propertiesFilename.concat(fileExtension).concat(".bak"), ".bak");
        
    }

    public void testGetBundleStringLocale() {
        // locale zh properties
        ResourceBundle rb_zh = PropertyResourceBundleManager.getBundle(propertiesFilename, new Locale("zh"));
        assertEquals("zh", rb_zh.getString("locale"));
        
        // locale zh_CN properties, accoridng to the implemenation, locale resource bundle will be searched
        // in cache for "less good candidate"
        ResourceBundle rb_zh_CN = PropertyResourceBundleManager.getBundle(propertiesFilename, Locale.CHINA);
        assertSame(rb_zh, rb_zh_CN); 
        assertEquals("zh", rb_zh_CN.getString("locale"));
        
        /* issue here
         
        // change the reloadCheckMillis to 0, force time to expire.
        dynModifyReloadCheckMillisValue(0);
        ResourceBundle rb_zh_CN_Refresh = PropertyResourceBundleManager.getBundle(propertiesFilename, Locale.CHINA);
        System.out.println(rb_zh);
        System.out.println(rb_zh_CN_Refresh);
        assertNotSame(rb_zh, rb_zh_CN_Refresh); 
        assertEquals("zh_CN", rb_zh_CN_Refresh.getString("locale"));
        
        // though the zh_CN is loaded, zh is still in cache and not modified
        ResourceBundle rb_zh_After_zh_CN_loaded = PropertyResourceBundleManager.getBundle(propertiesFilename, new Locale("zh"));
        System.out.println(rb_zh);
        System.out.println(rb_zh_After_zh_CN_loaded);
        assertSame(rb_zh, rb_zh_After_zh_CN_loaded);
        
            
        */
    }
    
    /**
     * rename file to bakup file
     * 
     * @param filename file name
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
     * @param filename file name which contains the specified file extension
     * @param fileExtension '.bak' suffix
     */
    private void recoverFile(String filename, String fileExtension) {
        if (filename.endsWith(fileExtension)) {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            URL url = cl.getResource(filename);
            File file = new File(url.getPath());            
            File target = new File(url.getPath().substring(0, url.getPath().length()- fileExtension.length()));
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
