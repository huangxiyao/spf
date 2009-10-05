/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.ac;

import com.hp.it.spf.xa.ac.HealthcheckStatus;

/**
 * This class represents the <b>open</b> healthcheck status. <b>Open</b> means
 * that the site is operational (ie, is working), and the site administrator has
 * allowed end-users to be admitted.
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.ac.HealthcheckStatus</code>
 */
public class OpenStatus extends HealthcheckStatus {

    final private static long serialVersionUID = 1636739761191095170L;

    final static private String OPEN_STATUS = "open";

    // ///////////////////////////////////////////////////////////////////
    /* PRIVATE ATTRIBUTES */
    // ///////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////
    /* PUBLIC METHODS */
    // ///////////////////////////////////////////////////////////////////
    public OpenStatus() {

        super();

    } // end constructor

    /**
     * Returns the simple name for the healthcheck status represented by
     * <code>OpenStatus</code>: <i>open</i>.
     */
    public String getSimpleName() {

        return (OPEN_STATUS);

    } // end method

    // ///////////////////////////////////////////////////////////////////
    /* PRIVATE METHODS */
    // ///////////////////////////////////////////////////////////////////
} // end class
