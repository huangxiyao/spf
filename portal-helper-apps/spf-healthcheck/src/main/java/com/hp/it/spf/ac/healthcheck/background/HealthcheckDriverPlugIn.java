/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.ac.healthcheck.background;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.action.PlugIn;

import com.hp.it.spf.ac.healthcheck.background.HealthcheckDriver;
import com.hp.it.spf.ac.healthcheck.background.HealthcheckLogger;

/**
 * This is the Struts-compliant start-up class (ie <code>PlugIn</code> class)
 * for the <code>HealthcheckDriver</code> singleton. This classname needs to
 * be in the <code>struts-config.xml</code> file for the healthcheck service
 * (in the Admission Control healthcheck Web application).
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class HealthcheckDriverPlugIn implements PlugIn {

    private HealthcheckDriver driver = null;

    /**
     * Instantiates the <code>HealthcheckDriver</code> singleton, schedules it
     * for background execution, and logs the result. This is a Struts-compliant
     * <code>PlugIn.init</code> method, so it runs once during startup of the
     * healthcheck Web application. The signature requires declaring
     * <code>ServletException</code> when in fact no
     * <code>ServletException</code> will ever be thrown by this method.
     * <p>
     * 
     * This method is what starts the regular background health monitoring
     * performed by the healthcheck application.
     * <p>
     */
    public void init(ActionServlet servlet, ModuleConfig config)
            throws ServletException {

        HealthcheckLogger
                .log("-------------- Begin Healthcheck Startup --------------");
        this.driver = HealthcheckDriver.instance(true); // force load
        if (this.driver.initialized) {
            this.driver.schedule();
            HealthcheckLogger
                    .log("-------------- End Healthcheck Startup ----------------");
        } else
            HealthcheckLogger
                    .log("-------------- Healthcheck Startup FAILED -------------");

    } // end method init

    /**
     * Cancels background execution of the <code>HealthcheckDriver</code>
     * singleton, and logs the result. This is a Struts-compliant
     * <code>PlugIn.destroy</code> method, so it runs once during shutdown of
     * the healthcheck Web application. If a healthcheck is executing at the
     * time <code>destroy</code> is called, it is unaffected, but subsequent
     * scheduled healthchecks will be cancelled.
     * <p>
     */
    public void destroy() {

        HealthcheckLogger
                .log("-------------- Begin Healthcheck Shutdown -------------");
        this.driver.cancel();
        HealthcheckLogger
                .log("-------------- End Healthcheck Shutdown ---------------");

    } // end method destroy

} // end class
