/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.ac.healthcheck.background;

/**
 * Represents that a <code>HealthcheckClient</code> could not be created. For
 * example, this may be because a host name was not provided. Typically an
 * encased <code>String</code> or <code>Exception</code> of some kind will
 * serve to more-narrowly diagnose the problem.
 * <p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */

public class CannotCreateHealthcheckClientException extends Exception {

    final static private long serialVersionUID = 2687154025382527898L;

    /**
     * Another alternative constructor.
     * <p>
     * 
     * @param <code>String</code> provides instance-specific troubleshooting
     *            information.
     */
    public CannotCreateHealthcheckClientException(String m) {

        super(m);

    } // end constructor

    /**
     * Another alternative constructor.
     * <p>
     * 
     * @param <code>Exception</code> is a triggering exception which led to this
     *            CannotCreateSwopClientException.
     *            <p>
     */
    public CannotCreateHealthcheckClientException(Exception e) {

        super(e);

    } // end constructor

    /**
     * Another alternative constructor.
     * <p>
     * 
     * @param <code>String</code> provides instance-specific troubleshooting
     *            information.
     * @param <code>Exception</code> is a triggering exception which led to this
     *            CannotCreateSwopClientException.
     *            <p>
     */
    public CannotCreateHealthcheckClientException(String m, Exception e) {

        super(m, e);

    } // end constructor

} // end class
