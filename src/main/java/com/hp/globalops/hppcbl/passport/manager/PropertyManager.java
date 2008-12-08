package com.hp.globalops.hppcbl.passport.manager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * property resource bundle manager.
 * load properties from inputstream, and contain them in the Hashmap,
 * if the property file is modified, then update the hashmap, otherwise, retrieve
 * the ResourceBundle from hashmap directly.
 * 
 * author <link href="ye.liu@hp.com">Liu Ye</link>
 */
public class PropertyManager {
    private static final Map p_map = new HashMap(); // maintain ResourceBundle objects
    
    private static final String SUFFIX = ".properties";
    
    public static synchronized ResourceBundle getBundle(String propertiesName) {
        //if propertiesName is not passed, then return null
        if (propertiesName == null || "".equals(propertiesName.trim())) {
            return null;
        }
        propertiesName = propertiesName.trim();
        
        if (p_map.containsKey(propertiesName)) {
            return getResourceBundleFromMap(propertiesName);
        } else {
            return setResourceBundleIntoMap(propertiesName);
        }
      
    }

    /**
     * retrieve value from properties with filename and key
     * @param propertiesName
     *            properties file name 
     * @param key
     *            key
     * @return
     *            value
     */
    public static String getString(String propertiesName, String key) {
        return getString(propertiesName, key, null);
    }
    
    /**
     * retrieve value from properties with filename and key
     * @param propertiesName
     *            properties file name 
     * @param key
     *            key
     * @param defaultValue
     *            defaultValue
     * @return
     *            value
     */
    public static String getString(String propertiesName, String key, String defaultValue) {
        ResourceBundle rb = getBundle(propertiesName);
        if (rb !=null ) {
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
     * append .properties file suffix if it doesn't exist, otherwise PropertiesConfiguration will 
     * throw an exception
     * @param filename 
     *           file name
     * @return real filename
     */
    private static String getFileName(String filename) {
        // if file suffix does not exist, then append suffix
        if (!filename.toLowerCase().endsWith(SUFFIX)) {
            filename = filename + SUFFIX;
        }
        return filename;
    }
    
    /**
     * get ResourceBundle from map with key, if the related file is modified, then 
     * update the relevant ResourceBundle
     * @param key
     * @return ResourceBundle
     */
    private static ResourceBundle getResourceBundleFromMap(String filename) {
        try {
            //if the file is modified
            if (isModified(filename)) {
                return updateResourceBundleInMap(filename);
            } else {
                return (ResourceBundle)((Object[])p_map.get(filename))[0];
            }
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * set PropertyResourceBundle into map
     * @param filename
     * @return ResourceBundle
     */
    private static ResourceBundle setResourceBundleIntoMap(String filename) {
        File file = null;
        try{
            file = getFile(filename);
        } catch (Exception e) {
            return null;
        }
        if (file != null) {
            try {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                InputStream in = new BufferedInputStream(cl.getResourceAsStream(getFileName(filename)));
                ResourceBundle rb = new PropertyResourceBundle(in);    
                //get lastModified
                Long lastModified = new Long(file.lastModified());
                p_map.put(filename, new Object[]{rb, lastModified});
                return rb;        
            } catch (Exception e) {
                return null;
            }
        } else {
            return null;
        }
    }
    
    /**
     * update PropertyResourceBundle in map
     * @param filename
     * @return
     */
    private static ResourceBundle updateResourceBundleInMap(String filename) {
        // remove the exist object. this step is not necessary, because hashmap's key
        // is unique, if you set the object with the same key, the lastest one will recover
        // the previous one
        p_map.remove(filename);
        return setResourceBundleIntoMap(filename);
    }
    
    /**
     * check if the file is modified
     * @param filename
     * @return true or false
     */
    private static boolean isModified(String filename) throws Exception{
        File file = getFile(filename);        
        if (file == null)
            return false;
        //last modified time
        long lastModified = file.lastModified();
        //the modified time saved in map
        Object[] obj = ((Object[])p_map.get(filename));
        long savedLastModified = ((Long)obj[1]).longValue();
        
        return lastModified > savedLastModified;
    }
    
    /**
     * get File object with filename
     * @param filename
     * @return File
     */
    private static File getFile(String filename) throws Exception {
        filename = getFileName(filename);
        if (filename == null)
            return null;

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource(filename);
        if (url == null)
            return null;

        File file = new File(url.getPath());
        return file;
    }
}

