/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.ac.healthcheck.background;

import java.lang.String;
import java.lang.Boolean;
import com.hp.bco.pl.transaction.util.logging.Log;
import com.hp.it.spf.ac.healthcheck.util.HealthcheckProperties;

/**
 * Wrapper class for the healthcheck log. The healthcheck log is configured in
 * <code>logger.xml</code> for the healthcheck service - see. At time of
 * writing, the healthcheck log is named <code>healthcheck.log</code>, but
 * the actual filename can be configured in <code>logger.xml</code> as usual.
 * This class is a wrapper API for accessing that log file.
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */

public class HealthcheckLogger {

    // ///////////////////////////////////////////////////////////////////
    /* PRIVATE ATTRIBUTES */
    // ///////////////////////////////////////////////////////////////////
    // Logger name - must match <logger name=...> in logger.xml
    private static String HEALTHCHECK_LOG = "HEALTHCHECK_LOG";

    // ///////////////////////////////////////////////////////////////////
    /* PUBLIC METHODS */
    // ///////////////////////////////////////////////////////////////////
    /**
     * Writes the given log message into the WPA admit service's healthcheck
     * log. No classname information is provided.
     */
    public static void log(String message) {

        String messageToLog = truncate(message);
        // Continue to use logServiceInfo even though it is deprecated, at least
        // for now. The replacement method, logInfo, requires 'this' as a
        // parameter, which will not work in this static context!
        Log.logServiceInfo(HEALTHCHECK_LOG, messageToLog);
        if (Boolean.getBoolean("lab.unit"))
            System.out.println(messageToLog);

    } // end method log

    /**
     * Writes the given log message with the given caller classname into the WPA
     * admit service's healthcheck log.
     */
    public static void log(Object caller, String message) {

        if (caller == null)
            return;

        String messageToLog = caller.getClass().getName() + " | "
                + truncate(message);
        // Continue to use logServiceInfo, at least for now, since the other log
        // method above must continue to use it (see).
        Log.logServiceInfo(HEALTHCHECK_LOG, messageToLog);
        if (Boolean.getBoolean("lab.unit"))
            System.out.println(messageToLog);

    } // end method log

    // ///////////////////////////////////////////////////////////////////
    /* PRIVATE METHODS */
    // ///////////////////////////////////////////////////////////////////
    private static String truncate(String big) {

        String little = big;
        HealthcheckProperties properties = HealthcheckProperties.instance();
        int howMuch = properties.getLogMessageLimit();
        if (howMuch <= 0)
            return big;

        if ((big != null) && (big.length() > howMuch)) {
            little = big.substring(0, howMuch);
            little += "... [truncated]";
        }
        return little;

    } // end method truncate

} // end class
