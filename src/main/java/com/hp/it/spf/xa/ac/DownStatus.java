/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.ac;

import com.hp.it.spf.xa.ac.HealthcheckStatus;

/**
 * This class represents the <b>down</b> healthcheck status. <b>Down</b> means
 * that the site is not operational (not working), and should be considered
 * unavailable to everyone.
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class DownStatus extends HealthcheckStatus {

    final static private long serialVersionUID = 3562336022350583091L;

    final static private String DOWN_STATUS = "down";

    // ///////////////////////////////////////////////////////////////////
    /* PRIVATE ATTRIBUTES */
    // ///////////////////////////////////////////////////////////////////
    // ///////////////////////////////////////////////////////////////////
    /* PUBLIC METHODS */
    // ///////////////////////////////////////////////////////////////////
    public DownStatus() {

        super();

    } // end constructor

    /**
     * Returns the simple name for the healthcheck status represented by
     * <code>DownStatus</code>: <i>down</i>.
     */
    public String getSimpleName() {

        return (DOWN_STATUS);

    } // end method

    // ///////////////////////////////////////////////////////////////////
    /* PRIVATE METHODS */
    // ///////////////////////////////////////////////////////////////////
} // end class
