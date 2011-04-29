/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.ac.healthcheck.background;

/**
 * Represents that a connection with the HTTP server for a healthcheck could not
 * be established, or was prematurely lost. This may be for a variety of
 * reasons; typically an encased <code>String</code> or <code>Exception</code>
 * of some kind will serve to more-narrowly diagnose the problem.
 * <p>
 * 
 * A subclass, <code>TimedOutHealthcheckConnectionException</code>,
 * represents a connection that did not complete in time but otherwise
 * experienced no I/O problems.
 * <p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */

public class BrokenHealthcheckConnectionException extends Exception {

    final static private long serialVersionUID = -3931300179312176382L;

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
    public BrokenHealthcheckConnectionException(String m) {

        super(m);

    } // end constructor

    /**
     * Another alternative constructor.
     * <p>
     * 
     * @param <code>Exception</code> is a triggering exception which led to this
     *            BrokenHealthcheckConnectionException.
     *            <p>
     */
    public BrokenHealthcheckConnectionException(Exception e) {

        super(e);

    } // end constructor

    /**
     * Another alternative constructor.
     * <p>
     * 
     * @param <code>String</code> provides instance-specific troubleshooting
     *            information.
     * @param <code>Exception</code> is a triggering exception which led to this
     *            BrokenHealthcheckConnectionException.
     *            <p>
     */
    public BrokenHealthcheckConnectionException(String m, Exception e) {
        
        super (m, e);
        
    } // end constructor
    
} // end class
