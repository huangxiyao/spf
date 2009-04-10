/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.ac;

import java.io.Serializable;
import java.lang.Integer;
import java.lang.String;
import java.util.Date;

import javax.servlet.ServletContext;

/**
 * This is the base class for the healthcheck status object hierarchy. It is
 * abstract and therefore is never intended to be instantiated itself.
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see <code>com.hp.it.spf.xa.ac.ClosedStatus</code><br>
 * <code>com.hp.it.spf.xa.ac.DownStatus</code><br>
 * <code>com.hp.it.spf.xa.ac.OpenStatus</code>
 */
public abstract class HealthcheckStatus implements Serializable {

	// ///////////////////////////////////////////////////////////////////
	/* PRIVATE ATTRIBUTES */
	// ///////////////////////////////////////////////////////////////////
	private Date timestamp = null;
	private String portalPulseSource = null;
	private String openSignSource = null;

	private static Object mutex = new Object();

	private static final String EFFECTIVE = "AC_EFFECTIVE_HEALTHCHECK_STATUS";

	private static final String PENDING = "AC_PENDING_HEALTHCHECK_STATUS";

	private static final String COUNTER = "AC_PENDING_HEALTHCHECK_STATUS_COUNTER";

	private static final Integer ZERO = new Integer(0);

	private static final Integer ONE = new Integer(1);

	// ///////////////////////////////////////////////////////////////////
	/* PUBLIC METHODS */
	// ///////////////////////////////////////////////////////////////////
	/**
	 * The common constructor for the <code>HealthcheckStatus</code>
	 * hierarchy. When any kind of <code>HealthcheckStatus</code> is
	 * constructed, this base class constructor should be called, which captures
	 * the current time and records it internally. This represents the time to
	 * which the status is considered to date, and is the value later returned
	 * by the <code>getDate</code> method (see).
	 */
	public HealthcheckStatus() {

		timestamp = new Date();

	} // end constructor

	/**
	 * Stores this <code>HealthcheckStatus</code> to an internally-known
	 * location in the application scope, <b>if</b> the
	 * <code>HealthcheckStatus</code> has changed sufficiently. This location
	 * is where the <code>retrieve</code> method (see) will be able to find it
	 * when called.
	 * <p>
	 * 
	 * First, the <code>retrieve</code> method is used to obtain whatever
	 * <code>HealthcheckStatus</code> was previously saved in the given
	 * <code>ServletContext</code>. If none was previously saved, then this
	 * one is stored.
	 * <p>
	 * 
	 * Otherwise, the class of the previously-saved
	 * <code>HealthcheckStatus</code> is checked. If the previously-saved
	 * <code>HealthcheckStatus</code> is different than this one, <b>and</b>
	 * there has been a string of successive occurences of this
	 * <code>HealthcheckStatus</code> equal to the given threshold, then the
	 * first one in the succession is stored.
	 * <p>
	 * 
	 * Otherwise, in all other circumstances, the previously-saved
	 * <code>HealthcheckStatus</code> is retained.
	 * <p>
	 * 
	 * Typically only the <code>HealthcheckDriver</code> should ever be
	 * calling this <code>save</code> method.
	 * <p>
	 */
	public void save(ServletContext sc, int threshold) {

		HealthcheckStatus effective = null;
		HealthcheckStatus pending = null;
		Integer ctr = null;
		int c = 1;

		if (sc == null)
			return;

		// Must ensure mutual exclusion around the following - the retrieve()
		// method (see) must do the same.

		synchronized (mutex) {

			// If this is the first time, there will be no previously-set
			// effective healthcheck status. So save the current healthcheck
			// status as effective, and zero-out the pending healthcheck data.

			effective = (HealthcheckStatus) sc.getAttribute(EFFECTIVE);
			if (effective == null) {
				sc.setAttribute(EFFECTIVE, this);
				sc.setAttribute(PENDING, null);
				sc.setAttribute(COUNTER, ZERO);
				return;
			}

			// Otherwise, there is a previous effective healthcheck status. So
			// if the current healthcheck status is the same kind, make sure the
			// pending data is zeroed-out and otherwise do nothing.

			if (this.getClass().getName()
					.equals(effective.getClass().getName())) {
				sc.setAttribute(PENDING, null);
				sc.setAttribute(COUNTER, ZERO);
				return;
			}

			// Otherwise, the current healthcheck status is different than the
			// one previously made effective. So check if any healthcheck status
			// has previously been made pending.
			//
			// In the case where none has been previously made pending:
			// Check the threshold for the current healthcheck status, as given.
			// If the threshold is more than 1, make the current healthcheck
			// status pending, set the pending counter to 1, and leave the
			// previously-made-effective status alone. Otherwise save the
			// current healthcheck status as the new effective one, and makes
			// suer the the pending data is zeroed-out.

			pending = (HealthcheckStatus) sc.getAttribute(PENDING);
			if (pending == null) {
				if (threshold > 1) {
					sc.setAttribute(PENDING, this);
					sc.setAttribute(COUNTER, ONE);
				} else {
					sc.setAttribute(EFFECTIVE, this);
					sc.setAttribute(PENDING, null);
					sc.setAttribute(COUNTER, ZERO);
				}
				return;
			}

			// In the case where the current and previously-effective
			// healthchecks differ (like above), but a healthcheck status is
			// pending (unlike above): Check if the pending status is same kind
			// as the current. If so, increment the pending counter and compare
			// it with the threshold for the current healthcheck status, given
			// us. If the threshold is still more than the incremented
			// counter, save the incremented counter, and leave the previous
			// healthcheck status alone. But if the counter is at the
			// threshold, save the pending healthcheck status as the new
			// effective status, and zero-out the pending data.

			if (this.getClass().getName().equals(pending.getClass().getName())) {
				ctr = (Integer) sc.getAttribute(COUNTER);
				if (ctr != null)
					c = ctr.intValue();
				c++;
				if (threshold > c) {
					sc.setAttribute(COUNTER, new Integer(c));
				} else {
					sc.setAttribute(EFFECTIVE, pending);
					sc.setAttribute(PENDING, null);
					sc.setAttribute(COUNTER, ZERO);
				}
				return;
			}

			// Final case: The current and previous healthchecks differ, and a
			// healthcheck status has previously been made pending that also
			// differs from the current. So check the threshold for the current
			// status, as given. If it is more than 1, make the current
			// healthcheck status pending, set the pending counter to 1, and
			// leave the previous status alone. Otherwise save the current
			// healthcheck status and zero-out the pending data.

			if (threshold > 1) {
				sc.setAttribute(PENDING, this);
				sc.setAttribute(COUNTER, ONE);
			} else {
				sc.setAttribute(EFFECTIVE, this);
				sc.setAttribute(PENDING, null);
				sc.setAttribute(COUNTER, ZERO);
			}
			return;
		} // end synchronized section

	} // end method save

	/**
	 * Returns the <code>HealthcheckStatus</code> which was previously stored
	 * in an internally-known attribute of the application scope. See the
	 * <code>save</code> method. Null is returned if no
	 * <code>HealthcheckStatus</code> was previously saved.
	 */
	public static HealthcheckStatus retrieve(ServletContext sc) {

		HealthcheckStatus hcs = null;

		if (sc == null)
			return null;

		// Must ensure mutual exclusion around the following - the save()
		// method (see) must do the same.

		synchronized (mutex) {
			hcs = (HealthcheckStatus) sc.getAttribute(EFFECTIVE);
		}
		return hcs;

	} // end method retrieve

	/**
	 * Returns the time of record for this <code>HealthcheckStatus</code>.
	 * See constructor documentation.
	 */
	public Date getDate() {

		return timestamp;

	} // end method getDate

	/**
	 * Abstract for returning a simple string describing the status represented
	 * by this <code>HealthcheckStatus</code> object. For example,
	 * <code>OpenStatus</code> will implement this to return the string
	 * <i>open</i>.
	 */
	public abstract String getSimpleName();

	/**
	 * Set a string recording information about where the portal pulse was
	 * performed, for informational reasons. Using this is optional.
	 */
	public void setPortalPulseSource(String source) {

		this.portalPulseSource = source;

	}

	/**
	 * Set a string recording information about where the open-sign check was
	 * performed, for informational reasons. Using this is optional.
	 */
	public void setOpenSignSource(String source) {

		this.openSignSource = source;

	}

	/**
	 * Return a string holding information about where the portal pulse was
	 * performed. This returns null if the information was unrecorded.
	 */
	public String getPortalPulseSource() {

		return this.portalPulseSource;

	}

	/**
	 * Return a string holding information about where the open-sign check was
	 * performed. This returns null if the information was unrecorded.
	 */
	public String getOpenSignSource() {
	
		return this.openSignSource;
		
	}

	// ///////////////////////////////////////////////////////////////////
	/* PRIVATE METHODS */
	// ///////////////////////////////////////////////////////////////////
} // end class
