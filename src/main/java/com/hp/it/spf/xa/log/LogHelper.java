/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.xa.log;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

// Not used at present.
// import org.apache.commons.logging.Log;
// import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * A class to provide helper methods for logging.
 * </p>
 * 
 * @author <link href="ying-zhi.wu@hp.com">Oliver</link>
 * @version TBD
 */
public class LogHelper {

    // Not used at present.
    // private static final Log LOG = LogFactory.getLog(LogHelper.class);

    private static final String DEFAULT_SPLIT_TAG = "|"; // default separator

    private LogHelper() {

    }

    /**
     * <p>
     * Returns a formatted string of log parameter information taken from
     * key/value pairs in the given map. The formatted string looks like: "key:
     * value | key: value | ...". The keys and values should implement the
     * toString method to be useful. Any value which is itself a map, will
     * format recursively inside "{}" characters.
     * </p>
     * 
     * @param map
     *            The parameters to format, as key/value pairs.
     * @return A string of formatted log information.
     */
    public static String formatParaInfo(Map map) {
        return formatParaInfo(map, DEFAULT_SPLIT_TAG);
    }

    /**
     * <p>
     * Returns a formatted string of log parameter information taken from
     * key/value pairs in the given map, using the given delimiter to separate
     * the pairs in the formatted string. The keys and values should implement
     * the toString method to be useful. Any value which is itself a map, will
     * format recursively inside "{}" characters.
     * </p>
     * 
     * @param map
     *            The parameters to format, as key/value pairs.
     * @param splitTag
     *            The delimiter to separate formatted key/value pairs in the
     *            return.
     * @return A string of formatted log information.
     */
    public static String formatParaInfo(Map map, String splitTag) {

        String fmStr = ""; // formated string
        Set set = map.keySet();
        Iterator it = set.iterator();
        while (it.hasNext()) {
            fmStr += getKeyValueStr(map, it.next(), splitTag)
                    + (it.hasNext() ? " " + splitTag + " " : "");
        }

        return fmStr;
    }

    /**
     * <p>
     * Use key to get value from map, then join key and value to "key: value"
     * pattern.if the value is instance of map, it should be recursed, and the
     * value pattern may be like : key4: { key44: { key441: value441 | key442:
     * value442 } }
     * </p>
     * 
     * @param map
     *            The parameters to format, as key/value pairs.
     * @param key
     *            A key for a value in the map.
     * @param splitTag
     *            The delimiter to separate formatted key/value pairs in the
     *            return.
     * @return A string of formatted log information.
     */
    private static String getKeyValueStr(Map map, Object key, String splitTag) {
        String kvStr = "";
        if (map.containsKey(key)) {
            String value = "";
            Object valueObj = map.get(key);
            if (valueObj instanceof Map) {
                value = "{ " + formatParaInfo((Map) valueObj, splitTag) + " }";
            } else if (null != valueObj) {
                // otherwise call valueObject's toString method
                value = valueObj.toString();
            }
            kvStr = key.toString() + ": " + value;
        } else {
            kvStr = kvStr + "";
        }

        // It is the caller's responsibility to log; this method is just
        // supposed to format the string.
        // LOG.info(kvStr);

        return kvStr;
    }
}
