/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.ac;

import com.hp.it.spf.xa.ac.HealthcheckStatus;

/**
 * This class represents the <b>closed</b> healthcheck status. <b>Closed</b>
 * means that the site is operational, but the site administrator has barred
 * end-users from admission to the site.
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.ac.HealthcheckStatus</code>
 */
public class ClosedStatus extends HealthcheckStatus {

	final static private long serialVersionUID = 8057218277720183814L;

	final static private String CLOSED_STATUS = "closed";

	// ///////////////////////////////////////////////////////////////////
	/* PRIVATE ATTRIBUTES */
	// ///////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////
	/* PUBLIC METHODS */
	// ///////////////////////////////////////////////////////////////////
	public ClosedStatus() {

		super();

	} // end constructor

	/**
	 * Returns the simple name for the healthcheck status represented by
	 * <code>ClosedStatus</code>: <i>closed</i>.
	 */
	public String getSimpleName() {

		return (CLOSED_STATUS);

	} // end method

	// ///////////////////////////////////////////////////////////////////
	/* PRIVATE METHODS */
	// ///////////////////////////////////////////////////////////////////
} // end class
