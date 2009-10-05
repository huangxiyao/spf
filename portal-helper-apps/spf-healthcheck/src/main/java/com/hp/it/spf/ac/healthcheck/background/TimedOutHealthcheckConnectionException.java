/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.ac.healthcheck.background;

import com.hp.it.spf.ac.healthcheck.background.BrokenHealthcheckConnectionException;

/**
 * Represents that a connection with the HTTP healthcheck server did not
 * complete in time. It may be that the initial <code>connect</code> did not
 * establish in time, or it may be that a subsequent blocking <code>read</code>
 * did not receive data in time. Typically an encased <code>String</code> or
 * <code>Exception</code> of some kind will serve to more-narrowly diagnose
 * the problem.
 * <p>
 * 
 * This subclass is an extension of
 * <code>BrokenHealthcheckConnectionException</code>.
 * <p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */

public class TimedOutHealthcheckConnectionException extends
        BrokenHealthcheckConnectionException {

    final static private long serialVersionUID = 5314990849220753435L;

    // ///////////////////////////////////////////////////////////////////
    /* PRIVATE ATTRIBUTES */
    // ///////////////////////////////////////////////////////////////////

    // ///////////////////////////////////////////////////////////////////
    /* PUBLIC METHODS */
    // ///////////////////////////////////////////////////////////////////
    /**
     * Another alternative constructor.
     * <p>
     * 
     * @param <code>String</code> provides instance-specific troubleshooting
     *            information.
     */
    public TimedOutHealthcheckConnectionException(String m) {

        super(m);

    } // end constructor

    /**
     * Another alternative constructor.
     * <p>
     * 
     * @param <code>Exception</code> is a triggering exception which led to this
     *            TimedOutHealthcheckConnectionException.
     *            <p>
     */
    public TimedOutHealthcheckConnectionException(Exception e) {

        super(e);

    } // end constructor

    /**
     * Another alternative constructor.
     * <p>
     * 
     * @param <code>String</code> provides instance-specific troubleshooting
     *            information.
     * @param <code>Exception</code> is a triggering exception which led to this
     *            TimedOutHealthcheckConnectionException.
     *            <p>
     */
    public TimedOutHealthcheckConnectionException(String m, Exception e) {

        super(m, e);

    } // end constructor

} // end class
