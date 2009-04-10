/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.ac.healthcheck.background;

import java.lang.ClassLoader;
import java.io.InputStream;
import javax.servlet.ServletException;
import com.hp.bco.pl.transaction.util.logging.LogConfigServlet;

/**
 * Healthcheck logger configuration servlet. The healthcheck service and main
 * logs are configured via this servlet. It is essentially identical to the WPA
 * standard <code>LogConfigServlet</code>, except in how its
 * <code>init</code> method overrides the default search locations for the log
 * configuration file.
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */

public class HealthcheckLogConfigServlet extends LogConfigServlet {

    // ///////////////////////////////////////////////////////////////////
    /* PRIVATE ATTRIBUTES */
    // ///////////////////////////////////////////////////////////////////
    // Logger configuration (logger.xml) file name
    private static String HEALTHCHECK_LOG_CONFIG = "healthcheckLogger.xml";

    // ///////////////////////////////////////////////////////////////////
    /* PUBLIC METHODS */
    // ///////////////////////////////////////////////////////////////////
    /**
     * This method attempts to find a <code>healthcheckLogger.xml</code> log
     * configuration file on the classpath, and if it is found, that file (only)
     * is loaded (any further <code>logger.xml</code> files that might exist
     * inside the Web application are ignored). If a
     * <code>healthcheckLogger.xml</code> is not found on the classpath,
     * though, then the usual behavior of the WPA standard
     * <code>LogConfigServlet</code> ensues (eg, <code>logger.xml</code>
     * files in the Web application's <code>WEB-INF/</code> service
     * directories are loaded).
     * 
     */
    public void init() throws ServletException {

        InputStream in = ClassLoader
                .getSystemResourceAsStream(HEALTHCHECK_LOG_CONFIG);
        if (in != null) {
            System.out.println(this.getClass().getName()
                    + ": Found healthcheck log configuration file ["
                    + HEALTHCHECK_LOG_CONFIG + "] on classpath.  Loading.");
            this.loadConfigFile(in);
            this.initJmx();
        } else {
            System.out
                    .println(this.getClass().getName()
                            + ": Unable to load healthcheck log configuration file ["
                            + HEALTHCHECK_LOG_CONFIG
                            + "] on classpath.  "
                            + "Reverting to [logger.xml] files within this Web application (if any).");
            super.init();
        }

    } // end method init

} // end class
