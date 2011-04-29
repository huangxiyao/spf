/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.ac.healthcheck.background;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContext;

import com.hp.bco.pl.wpa.util.Environment;
import com.hp.it.spf.ac.healthcheck.background.CannotCreateHealthcheckClientException;
import com.hp.it.spf.ac.healthcheck.background.HealthcheckClient;
import com.hp.it.spf.ac.healthcheck.background.HealthcheckLogger;
import com.hp.it.spf.ac.healthcheck.background.OpenSignHealthcheckClient;
import com.hp.it.spf.ac.healthcheck.background.PortalPulseHealthcheckClient;
import com.hp.it.spf.ac.healthcheck.util.HealthcheckProperties;
import com.hp.it.spf.xa.ac.HealthcheckStatus;
import com.hp.it.spf.xa.ac.ClosedStatus;
import com.hp.it.spf.xa.ac.DownStatus;
import com.hp.it.spf.xa.ac.OpenStatus;

/**
 * This singleton class is the controller for the background healthcheck feature
 * of the Admission Control healthcheck Web application.
 * <p>
 * 
 * The <code>HealthcheckDriver</code> main method, <code>execute</code>,
 * performs an overall system healthcheck. Each overall system healthcheck in
 * turn comprises a <b>portal pulse healthcheck</b> (performed by
 * <code>PortalPulseHealthcheckClient</code> or a similar custom subclass of
 * <code>HealthcheckClient</code>, defined in
 * <code>healthcheck.properties</code>) and an <b>open sign check</b>
 * (performed by <code>OpenSignHealthcheckClient</code>).
 * <p>
 * 
 * The portal pulse healthcheck evaluates whether the current portal server is
 * working adequately, by requesting and evaluating its defined "portal pulse
 * page" (any page in the portal application which only works if all the
 * critical portal components are working, regardless of whether non-critical
 * components are working or not). The server host, port, and URL for the portal
 * pulse healthcheck are defined in <code>healthcheck.properties</code>. Also
 * the Java regular expression whose match signifies a successful portal pulse
 * page is defined in <code>healthcheck.properties</code>.
 * <p>
 * 
 * The open sign check evaluates whether the Admission Control open-sign Web
 * application is running or not, and thus whether the portal's administrators
 * have opened the site to end-users or not. The open-sign host, port, and URL
 * are also defined in <code>healthcheck.properties</code>.
 * <p>
 * 
 * The outcome of these two healthchecks is used to formulate an appropriate
 * kind of <code>HealthcheckStatus</code> - eg <code>DownStatus</code> - and
 * store it to application scope (the final step of the <code>execute</code>
 * method in this class). The status in application scope is thus readied for
 * access by the healthcheck Web application's front-end Web pages - eg
 * <code>IsWorkingAndOpenAction</code>.
 * <p>
 * 
 * The <code>execute</code> method is queued to run in a background thread
 * periodically, by the <code>schedule</code> method. A startup class named
 * <code>HealthcheckDriverPlugIn</code> calls the <code>schedule</code>
 * method at startup of the healthcheck application, to get the whole process
 * going.
 * <p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class HealthcheckDriver {

	// ///////////////////////////////////////////////////////////////////
	/* PRIVATE ATTRIBUTES */
	// ///////////////////////////////////////////////////////////////////
	private static HealthcheckDriver driver = new HealthcheckDriver();

	private HealthcheckProperties properties = null;

	private HealthcheckClient portalPulseHealthchecker = null;

	private OpenSignHealthcheckClient openSignHealthchecker = null;

	// The background job for driving healthchecks.
	private Timer timer = null;

	// What the background job will do at each iteration.
	private TimerTask job = null;

	/**
	 * Records whether the constructor succeeded or failed to initialize its
	 * healthcheck clients properly. If it failed, then <code>initialized</code>
	 * will be false, and you can call the <code>instance</code> method again
	 * to try to get a new, properly-initialized instance. Note however that if
	 * initialization fails once, it will probably fail again - most healthcheck
	 * client initialization problems are internal.
	 */
	public boolean initialized = false;

	// ///////////////////////////////////////////////////////////////////
	/* PUBLIC METHODS */
	// ///////////////////////////////////////////////////////////////////
	/**
	 * Returns the <b>one and only</b> <code>HealthcheckDriver</code>
	 * instance in this JVM. If it has not been previously initialized, then it
	 * is initialized as a side-effect. Initialization automatically uses
	 * <code>HealthcheckProperties</code> freshly-loaded from
	 * <code>healthcheck.properties</code>.
	 * <p>
	 */
	public synchronized static HealthcheckDriver instance() {

		return instance(false);

	}

	/**
	 * Returns the <b>one and only</b> <code>HealthcheckDriver</code>
	 * instance in this JVM: either the previous initialization of it, or a
	 * re-initialization of it, depending on the given parameter. If it has not
	 * been previously initialized, then initialization happens anyway as * a
	 * side-effect. Initialization automatically uses
	 * <code>HealthcheckProperties</code> freshly-loaded from
	 * <code>healthcheck.properties</code>. If re-initializing, any previous
	 * schedule is cancelled as a side-effect.
	 * <p>
	 */
	public synchronized static HealthcheckDriver instance(boolean reload) {

		if (!driver.initialized || reload) {
			if (driver.initialized)
				driver.cancel();
			driver.load();
		}
		return driver;

	} // end method

	/**
	 * Admin method for scheduling the <code>execute</code> method (see) to
	 * run in a background thread on a regular basis.
	 * 
	 * The <code>schedule</code> method uses several properties from
	 * <code>healthcheck.properties</code> via the
	 * <code>HealthcheckProperties<code> class (see).  If healthchecks have
	 * been disabled in <code>healthcheck.properties</code>, then this method
	 * schedules nothing (and in fact cancels anything previously scheduled).
	 * The initial delay, and repeated (successive) delay
	 * between healthchecks, are also picked up from respective properties in
	 * <code>healthcheck.properties</code>.<p>
	 *
	 * Note that this <code>schedule</code> method uses what
	 * Java calls <b>fixed delay</b> scheduling, meaning the delays are
	 * relative to the completion of the previous execution, not the initiation.
	 * <p>
	 *
	 * The <code>schedule</code> method simply returns if the healthcheck
	 * execution has already been scheduled.  Thus calling <code>schedule</code>
	 * in succession will not double-up on the job.  To cancel scheduled
	 * healthchecks, if any, use the <code>cancel</code> method (see).<p>
	 */
	public synchronized void schedule() {

		int initialDelay, repeatDelay;

		HealthcheckLogger.log(this, "Entered schedule().");

		// Return if the healthcheck driver had failed to initialize.
		if (!this.initialized) {
			HealthcheckLogger.log(this,
					"Healthchecks did not initialize properly.");
			HealthcheckLogger.log(this, "Finished schedule().");
			return;
		}

		// Return if healthchecks are disabled in healthcheck.properties.
		// Force cancellation too.
		if (!this.properties.areHealthchecksEnabled()) {
			HealthcheckLogger.log(this, "Healthchecks are disabled.");
			this.cancel();
			HealthcheckLogger.log(this, "Finished schedule().");
			return;
		}

		// Do nothing if we're already scheduled.
		if (this.timer != null) {
			HealthcheckLogger.log(this, "Healthchecks are already scheduled.");
			HealthcheckLogger.log(this, "Finished schedule().");
			return;
		}

		// Startup the Timer to kick off with the appropriate delays.
		// Remember the delays in healthcheck.properties are in seconds,
		// so must convert to millis.

		initialDelay = this.properties.getInitialDelay() * 1000;
		repeatDelay = this.properties.getRepeatDelay() * 1000;
		HealthcheckLogger.log(this,
				"Scheduling healthchecks with initial delay: [" + initialDelay
						+ "] millis and repeating delay: [" + repeatDelay
						+ "] millis.");
		this.job = new TimerTask() {
			public void run() {
				HealthcheckLogger
						.log("-------------- Begin Healthcheck ----------------------");
				execute();
				HealthcheckLogger
						.log("-------------- End Healthcheck ------------------------");
			}
		};
		this.timer = new Timer(true); // run as background daemon thread
		this.timer.schedule(this.job, initialDelay, repeatDelay);

		HealthcheckLogger.log(this, "Finished schedule().");

	} // end method schedule

	/**
	 * Admin method for canceling any previously-scheduled invocation of the
	 * <code>execute</code> method. <code>cancel</code> just cancels a
	 * previous <code>schedule</code>, if any - that is all. A healthcheck
	 * execution that happens to be in progress at the time <code>cancel</code>
	 * is called is unaffected - it will just be the last such execution.
	 * <p>
	 */
	public synchronized void cancel() {

		HealthcheckLogger.log(this, "Entered cancel().");
		if ((this.timer != null) || (this.job != null)) {
			HealthcheckLogger.log(this,
					"Cancelling previously-scheduled healthchecks.");
			if (this.timer != null)
				this.timer.cancel();
			this.timer = null;
			this.job = null;
		} else
			HealthcheckLogger.log(this,
					"No previously-scheduled healthchecks to cancel.");
		HealthcheckLogger.log(this, "Finished cancel().");

	} // end method cancel

	/**
	 * Controller method for performing an overall healthcheck and saving the
	 * results as appropriate into application scope. Typically this method is
	 * scheduled to run in the background, using the <code>schedule()</code>
	 * method - see. But it is made a public method so that you can perform/save
	 * healthchecks arbitrarily, if need be.
	 * <p>
	 * 
	 * First the login healthcheck is executed and evaluated, using a built-in
	 * <code>LoginHealthcheckClient</code>. Then an online-indicator
	 * healthcheck is executed and evaluated, using a built-in
	 * <code>OnlineHealthcheckClient</code>.
	 * <p>
	 * 
	 * <ul>
	 * <li> If the login healthcheck failed, then the current status is <b>down</b>
	 * (represented by <code>DownStatus</code>).
	 * <li> If the login and online-indicator healthchecks both succeeded, the
	 * current status is <b>online</b> (represented by
	 * <code>OnlineStatus</code>).
	 * <li> And if the login healthcheck succeeded, but the online-indicator one
	 * failed, the current status is <b>offline</b> (represented by
	 * <code>OfflineStatus</code>).
	 * </ul>
	 * <p>
	 * 
	 * The current status is then sent to application scope, as appropriate,
	 * using the <code>HealthcheckStatus.save</code> method. This method might
	 * save the current status, or might simply pend it, depending on how
	 * thresholds are configured in <code>healthcheck.properties</code>.
	 * <p>
	 * See documentation for <code>HealthcheckStatus.save</code> for more
	 * information.
	 * <p>
	 */
	public synchronized void execute() {

		HealthcheckStatus newStatus, oldStatus, finalStatus;
		ServletContext sc = Environment.getInstance().getContext();
		String portalPulseResponse = null;
		String openSignResponse = null;
		boolean portalPulseSucceeded = false;
		boolean openSignSucceeded = false;
		String m;
		int threshold;

		HealthcheckLogger.log(this, "Entered execute().");

		// Make sure we've got the necessary healthcheck clients.
		// If not, cancel and return.

		if ((this.portalPulseHealthchecker == null)
				|| (this.openSignHealthchecker == null) || !this.initialized) {
			m = "One or more HealthcheckClients failed previously to construct.  Healthchecks did not initialize properly.";
			HealthcheckLogger.log(this, m);
			this.cancel();
			HealthcheckLogger.log(this, "Finished execute().");
			return;
		}

		// Execute the healthcheck clients and evaluate their results.

		HealthcheckLogger.log(this,
				"Begin execution of portal pulse healthcheck.");
		try {
			portalPulseResponse = this.portalPulseHealthchecker.execute();
			portalPulseSucceeded = this.portalPulseHealthchecker
					.evaluate(portalPulseResponse);
		} catch (TimedOutHealthcheckConnectionException e) {
			m = "End execution of portal pulse healthcheck.  Caught TimedOutHealthcheckConnectionException.  Portal pulse healthcheck failed.";
			HealthcheckLogger.log(this, m);
		} catch (BrokenHealthcheckConnectionException e) {
			m = "End execution of portal pulse healthcheck.  Caught BrokenHealthcheckConnectionException.  Portal pulse healthcheck failed.";
			HealthcheckLogger.log(this, m);
		}

		if (portalPulseSucceeded) {
			m = "End execution of portal pulse healthcheck.  Portal pulse healthcheck passed.";
			HealthcheckLogger.log(this, m);

			HealthcheckLogger.log(this, "Begin execution of open sign check.");
			try {
				openSignResponse = this.openSignHealthchecker.execute();
				openSignSucceeded = this.openSignHealthchecker
						.evaluate(openSignResponse);
			} catch (TimedOutHealthcheckConnectionException e) {
				m = "End execution of open sign check.  Caught TimedOutHealthcheckConnectionException.  Open sign check failed.";
				HealthcheckLogger.log(this, m);
			} catch (BrokenHealthcheckConnectionException e) {
				m = "End execution of open sign check.  Caught BrokenHealthcheckConnectionException.  Open sign check failed.";
				HealthcheckLogger.log(this, m);
			}

			if (openSignSucceeded) {
				m = "End execution of open sign check.  Open sign check passed.";
				HealthcheckLogger.log(this, m);
				newStatus = new OpenStatus();
			} else {
				m = "End execution of open sign check.  Open sign check failed.";
				HealthcheckLogger.log(this, m);
				newStatus = new ClosedStatus();
			}
		} else {
			m = "End execution of portal pulse healthcheck.  Portal pulse healthcheck failed.  (Because portal pulse healthcheck failed, open sign check will not be attempted.)";
			HealthcheckLogger.log(this, m);
			newStatus = new DownStatus();
		}
		HealthcheckLogger.log(this,
				"Therefore current healthcheck status is: ["
						+ newStatus.getSimpleName().toUpperCase() + "] at ["
						+ newStatus.getDate() + "].");
		threshold = this.properties.getThreshold(newStatus);

		// Record in the new status object, where we got the status from.
		newStatus.setPortalPulseSource(this.portalPulseHealthchecker.getHost()
				+ ":" + this.portalPulseHealthchecker.getPort());
		newStatus.setOpenSignSource(this.openSignHealthchecker.getHost() + ":"
				+ this.openSignHealthchecker.getPort());

		// Save the new healthcheck status. Note this might not actually
		// save it, due to the threshold properties configured in
		// healthcheck.properties. See HealthcheckStatus.save method.

		m = "Comparing current healthcheck status with previous.";
		HealthcheckLogger.log(this, m);
		oldStatus = HealthcheckStatus.retrieve(sc);
		if (oldStatus == null)
			HealthcheckLogger.log(this,
					"Previous healthcheck status is: [null].");
		else
			HealthcheckLogger.log(this, "Previous healthcheck status is: ["
					+ oldStatus.getSimpleName().toUpperCase() + "] at ["
					+ oldStatus.getDate() + "].");
		newStatus.save(sc, threshold);
		finalStatus = HealthcheckStatus.retrieve(sc);
		if (finalStatus.equals(oldStatus))
			HealthcheckLogger.log(this, "Final healthcheck status UNCHANGED.");
		else
			HealthcheckLogger.log(this,
					"Final healthcheck status CHANGED to: ["
							+ finalStatus.getSimpleName().toUpperCase()
							+ "] at [" + finalStatus.getDate() + "].");

		HealthcheckLogger.log(this, "Finished execute().");

	} // end method execute

	// ///////////////////////////////////////////////////////////////////
	/* PRIVATE METHODS */
	// ///////////////////////////////////////////////////////////////////
	private HealthcheckDriver() {
		// Empty
	}

	private synchronized void load() {

		Class classObj = null;
		Constructor con;
		Class[] formals;
		Object[] actuals;
		String m;

		HealthcheckLogger.log(this, "Entered load().");

		// Assume the following will be successful.
		this.initialized = true;

		// Create the portal pulse healthchecker based on values in healthcheck.
		// properties. All of these method calls return the corresponding values
		// in healthcheck.properties, or reasonable defaults if undefined
		// (except getPortalPulseClassFromEnvironment returns null in that
		// case).

		this.properties = HealthcheckProperties.instance(true); // Force load
		String portalPulseHost = this.properties.getPortalPulseHost();
		String portalPulseUrl = this.properties.getPortalPulseUrl();
		String portalPulseClassname = this.properties.getPortalPulseClass();
		String portalPulsePattern = this.properties.getPortalPulsePattern();
		int portalPulsePort = this.properties.getPortalPulsePort();
		int portalPulseMaxLength = this.properties.getPortalPulseLimit();
		int portalPulseTimeout = this.properties.getPortalPulseTimeout();
		boolean portalPulseManageCookies = this.properties
				.managePortalPulseCookies();

		// If the portal pulse classname was undefined in
		// healthcheck.properties, default it to the
		// PortalPulseHealthcheckClient.

		if ((portalPulseClassname == null) || "".equals(portalPulseClassname))
			portalPulseClassname = PortalPulseHealthcheckClient.class.getName();

		m = "Constructing portal pulse healthchecker: [" + portalPulseClassname
				+ "] for host: [" + portalPulseHost + "], port: ["
				+ portalPulsePort + "], url: [" + portalPulseUrl + "].";
		HealthcheckLogger.log(this, m);
		try {
			classObj = Class.forName(portalPulseClassname);
			formals = new Class[] { String.class, int.class, String.class };
			actuals = new Object[] { portalPulseHost,
					new Integer(portalPulsePort), portalPulseUrl };
			con = classObj.getDeclaredConstructor(formals);
			this.portalPulseHealthchecker = (HealthcheckClient) con
					.newInstance(actuals);
			m = "Setting portal pulse healthchecker pattern: ["
					+ portalPulsePattern + "], timeout (secs, -1=off): ["
					+ portalPulseTimeout
					+ "], response size limit (bytes, -1=off): ["
					+ portalPulseMaxLength + "], manage cookies: ["
					+ portalPulseManageCookies + "].";
			HealthcheckLogger.log(this, m);
			this.portalPulseHealthchecker.setSuccessPattern(portalPulsePattern);
			this.portalPulseHealthchecker.setTimeout(portalPulseTimeout * 1000);
			this.portalPulseHealthchecker
					.setMaxResponseBytes(portalPulseMaxLength);
			this.portalPulseHealthchecker
					.setCookiePolicy(portalPulseManageCookies);
		} catch (InvocationTargetException e) {
			Throwable x = e.getTargetException();
			if (x instanceof CannotCreateHealthcheckClientException)
				m = "Caught CannotCreateHealthcheckClientException.  Failed to construct portal pulse healthchecker.";
			else
				m = "Failed to construct portal pulse healthchecker.  InvocationTargetException original exception: ["
						+ x.getClass().getName()
						+ "], original message: ["
						+ x.getMessage() + "].";
			HealthcheckLogger.log(this, m);
			this.portalPulseHealthchecker = null;
			this.initialized = false;
		} catch (Throwable e) {
			m = "Failed to construct portal pulse healthchecker.  Exception: ["
					+ e.getClass().getName() + "], message: [" + e.getMessage()
					+ "].";
			HealthcheckLogger.log(this, m);
			this.portalPulseHealthchecker = null;
			this.initialized = false;
		}

		// Now do likewise for the OpenSignHealthcheckClient.

		String openSignHost = this.properties.getOpenSignHost();
		String openSignUrl = this.properties.getOpenSignUrl();
		int openSignPort = this.properties.getOpenSignPort();
		int openSignTimeout = this.properties.getOpenSignTimeout();

		m = "Constructing open sign checker: ["
				+ OpenSignHealthcheckClient.class.getName() + "] for host: ["
				+ openSignHost + "], port: [" + openSignPort + "], url: ["
				+ openSignUrl + "].";
		HealthcheckLogger.log(this, m);
		try {
			this.openSignHealthchecker = new OpenSignHealthcheckClient(
					openSignHost, openSignPort, openSignUrl);
			m = "Setting open sign checker timeout (secs, -1=off): ["
					+ openSignTimeout + "].";
			HealthcheckLogger.log(this, m);
			this.openSignHealthchecker.setTimeout(openSignTimeout * 1000);
		} catch (CannotCreateHealthcheckClientException e) {
			m = "Caught CannotCreateHealthcheckClientException.  Failed to construct open sign checker.";
			HealthcheckLogger.log(this, m);
			this.openSignHealthchecker = null;
			this.initialized = false;
		}

		HealthcheckLogger.log(this, "Finished load().");

	} // end method load

} // end class
