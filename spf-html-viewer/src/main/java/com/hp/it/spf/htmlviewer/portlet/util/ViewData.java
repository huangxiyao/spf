/*
 * Project: Shared Portal Framework
 * Copyright (c) 2009 HP. All Rights Reserved.
 **/
package com.hp.it.spf.htmlviewer.portlet.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.PropertyResourceBundle;

import javax.portlet.ReadOnlyException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hp.it.spf.htmlviewer.portlet.util.Consts;
import com.hp.it.spf.htmlviewer.portlet.util.Utils;
import com.hp.it.spf.xa.i18n.portlet.I18nUtility;
import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;

/**
 * <p>
 * <code>ViewData</code> contains the necessary resources for the HTML viewer
 * portlet's configuration and view modes. These resources include the portlet
 * preferences used by the HTML viewer portlet - the base view filename, etc -
 * and the uninterpolated content best-fit for a particular locale - the view
 * file content and token-substitutions (ie includes) file content.
 * </p>
 * <p>
 * The single-argument constructor loads those view resources from the portlet
 * preferences database and the filesystem, and remembers the time of load. The
 * multi-argument constructor loads those view resources from the constructor
 * parameter list (storing them to the portlet preferences database as a
 * side-effect), and likewise remembers the time of load. The {@link #expired()}
 * method returns true if the loaded view resources are no longer valid - ie,
 * have timed out. This takes into account the previous load time, and the value
 * of the check-seconds portlet preference that was loaded at that time. The
 * other methods return the respective preferences or content, or test whether
 * the load was successful.
 * </p>
 * <p>
 * Thus <code>ViewData</code> acts as a wrapper around the portlet
 * preferences. All access to portlet preferences in the HTML viewer portlet
 * occurs through this wrapper.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @since SPF 1.2
 */
public class ViewData {

	// cached preference data
	private String viewFilename = null;
	private String includesFilename = null;
	private boolean launchButtonless = Boolean
			.parseBoolean(Consts.DEFAULT_LAUNCH_BUTTONLESS);
	private int checkSeconds = Integer.parseInt(Consts.DEFAULT_CHECK_SECONDS);

	// cached content
	private String viewContent = null;
	private ResourceBundle includesContent = null;

	// cache metadata
	private Locale locale = null;
	private long createMillis = System.currentTimeMillis();
	private boolean error = false;
	private boolean warning = false;

	// portlet log
	private Log portletLog = LogFactory.getLog(ViewData.class);

	/**
	 * Initialize the <code>ViewData</code> object from the portlet
	 * preferences for the given request. First the portlet preferences are
	 * fetched and stored inside the <code>ViewData</code> object. Then the
	 * base view filename and includes filename preferences are used to obtain
	 * the view file and includes file content, respectively. The proper
	 * localized version of the content is obtained for the locale in the given
	 * request. The view file is loaded from the portlet bundle directory or the
	 * portlet application. The includes file is loaded from the portlet bundle
	 * directory or portlet application, or the classpath. The content is <b>not</b>
	 * interpolated, so that it can be re-used with many different users and
	 * requests.
	 * 
	 * @param request
	 *            the user request
	 */
	public ViewData(PortletRequest request) {

		// Requires a non-null portlet request to load the preferences.
		if (request != null) {
			this.locale = request.getLocale();
			try {
				PortletPreferences pp = request.getPreferences();
				// When getting strings, apply defaults later
				this.viewFilename = Utils.slashify(pp.getValue(
						Consts.VIEW_FILENAME, ""));
				this.includesFilename = Utils.slashify(pp.getValue(
						Consts.INCLUDES_FILENAME, ""));
				// When getting other attributes, apply defaults now
				this.launchButtonless = Boolean.valueOf(pp.getValue(
						Consts.LAUNCH_BUTTONLESS,
						Consts.DEFAULT_LAUNCH_BUTTONLESS));
				try {
					this.checkSeconds = Integer
							.parseInt(pp.getValue(Consts.CHECK_SECONDS,
									Consts.DEFAULT_CHECK_SECONDS));
				} catch (NumberFormatException e) {
					// Don't flag a warning since there is a reasonable default.
					this.checkSeconds = Integer
							.parseInt(Consts.DEFAULT_CHECK_SECONDS);
				}
			} catch (Exception e) {
				handleError("Unable to read portlet preferences.", e);
			}
		}

		// Now load the content corresponding to the preferences.
		loadViewContent(request);
		loadIncludesContent(request);
	}

	/**
	 * Initialize the <code>ViewData</code> object from the given parameters,
	 * storing them to portlet preferences as a side-effect. First the
	 * parameters are stored inside the <code>ViewData</code> object. Then
	 * they are persisted into the portlet preferences database. Finally they
	 * are used to obtain the view file and includes file content. The proper
	 * localized version of the content is obtained for the locale in the given
	 * request. The view file is loaded from the portlet bundle directory or the
	 * portlet application. The includes file is loaded from the portlet bundle
	 * directory or portlet application, or the classpath. The content is <b>not</b>
	 * interpolated, so that it can be re-used with many different users and
	 * requests.
	 * 
	 * @param request
	 *            the user request
	 * @param viewFilename
	 *            the base view filename (relative to the portlet bundle
	 *            directory or portlet application)
	 * @param includesFilename
	 *            the base includes filename (relative to the portlet bundle
	 *            directory, portlet application, or classpath)
	 * @param launchButtonless
	 *            the launch-buttonless preference
	 * @param checkSeconds
	 *            the check-seconds preference
	 */
	public ViewData(PortletRequest request, String viewFilename,
			String includesFilename, boolean launchButtonless, int checkSeconds) {

		this.viewFilename = Utils.slashify(viewFilename);
		this.includesFilename = Utils.slashify(includesFilename);
		this.launchButtonless = launchButtonless;
		this.checkSeconds = checkSeconds;

		// Requires a non-null portlet request to set the preferences.
		if (request != null) {
			this.locale = request.getLocale();
			try {
				PortletPreferences pp = request.getPreferences();
				pp.setValue(Consts.VIEW_FILENAME, this.viewFilename);
				pp.setValue(Consts.INCLUDES_FILENAME, this.includesFilename);
				pp.setValue(Consts.LAUNCH_BUTTONLESS, Boolean
						.toString(this.launchButtonless));
				pp.setValue(Consts.CHECK_SECONDS, Integer
						.toString(this.checkSeconds));
				pp.store();
			} catch (Exception e) {
				handleError("Unable to save portlet preferences.", e);
			}
		}

		// Lastly load the content corresponding to the preferences.
		loadViewContent(request);
		loadIncludesContent(request);
	}

	/**
	 * <p>
	 * Returns true if this <code>ViewData</code> object has expired per the
	 * check-seconds portlet preference at the time it was created, and false
	 * otherwise. Thus if the check-seconds was negative, this will always
	 * return false, and if the check-seconds was zero, it will always return
	 * true.
	 * </p>
	 * <p>
	 * <b>Note:</b> Whether the load of this <code>ViewData</code> succeeded
	 * or not has no bearing on whether it is expired or not. In other words,
	 * this method can return true even if {@link #ok()} is false.
	 * </p>
	 * 
	 * @return true if expired, false if not yet expired
	 */
	public boolean expired() {

		// If OK to cache forever, always return true.
		if (checkSeconds < 0) {
			return false;
		}

		// Otherwise compare current time with the last-check time + the number
		// of seconds to cache.
		long nowMillis = System.currentTimeMillis();
		long expiresMillis = createMillis + (checkSeconds * 1000);
		return nowMillis >= expiresMillis;
	}

	/**
	 * Returns true if this <code>ViewData</code> object was created (loaded)
	 * without any errors or warnings. When true is returned, both
	 * {@link #error()} and {@link #warning()} will return false. Conversely,
	 * when false is returned by the <code>ok()</code> method, either
	 * {@link #error()} or {@link #warning()} or both will return true.
	 * 
	 * @return true if there was no error or warning during load, false
	 *         otherwise
	 */
	public boolean ok() {
		return !this.error && !this.warning;
	}

	/**
	 * Returns true if there was a fatal error when this <code>ViewData</code>
	 * object was created (ie loaded). Typically this happens when the database
	 * for the portlet preferences could not be accessed. The attributes
	 * returned by the <code>ViewData</code> getter methods should be
	 * considered corrupt and not be used. When true is returned by the
	 * <code>error()</code> method, the {@link #ok()} method will return
	 * false, and vice-versa.
	 * 
	 * @return true if there was a fatal error during load, false otherwise
	 */
	public boolean error() {
		return this.error;
	}

	/**
	 * Returns true if there was a non-fatal but unusual condition encountered
	 * when this <code>ViewData</code> object was created (ie loaded).
	 * Typically this happens when the content files for the view and include
	 * preferences could not be found or read. When true is returned by the
	 * <code>warning()</code> method, the {@link #ok()} method will return
	 * false, and vice-versa.
	 * 
	 * @return true if there was a non-fatal warning during load, false
	 *         otherwise
	 */
	public boolean warning() {
		return this.warning;
	}

	/**
	 * Returns the base view filename that was loaded into this
	 * <code>ViewData</code> object. This corresponds with the base view
	 * filename preference in the portlet preferences.
	 * 
	 * @return the base view filename (relative to the portlet bundle folder or
	 *         portlet application)
	 */
	public String getViewFilename() {
		return this.viewFilename;
	}

	/**
	 * Returns the base include filename that was loaded into this
	 * <code>ViewData</code> object. This corresponds with the base include
	 * filename preference in the portlet preferences.
	 * 
	 * @return the base include filename (relative to the portlet bundle folder,
	 *         portlet application, or classpath)
	 */
	public String getIncludesFilename() {
		return this.includesFilename;
	}

	/**
	 * Returns the launch-buttonless value that was loaded into this
	 * <code>ViewData</code> object. This corresponds with the
	 * launch-buttonless preference in the portlet preferences.
	 * 
	 * @return the launch-buttonless value
	 */
	public boolean getLaunchButtonless() {
		return this.launchButtonless;
	}

	/**
	 * Returns the check-seconds value that was loaded into this
	 * <code>ViewData</code> object. This is the number of seconds for which
	 * this <code>ViewData</code> object is considered valid from its
	 * creation/load. This corresponds with the check-seconds preference in the
	 * portlet preferences.
	 * 
	 * @return the check-seconds value
	 */
	public int getCheckSeconds() {
		return this.checkSeconds;
	}

	/**
	 * Returns the locale that was loaded into this <code>ViewData</code>
	 * object. This is the locale of the portlet request used to create the
	 * <code>ViewData</code>. It is the locale for which the best-candidate
	 * view content and includes content stored in this <code>ViewData</code>
	 * were determined.
	 * 
	 * @return the locale for this <code>ViewData</code>
	 */
	public Locale getLocale() {
		return this.locale;
	}

	/**
	 * Returns the <b>uninterpolated</b> view file content that was loaded into
	 * this <code>ViewData</code> object. This is the best-fit localized
	 * content for the base view filename in the portlet preferences given the
	 * locale. It is uninterpolated so that it can be reused by many different
	 * users and requests.
	 * 
	 * @return the localized, uninterpolated view file content
	 */
	public String getViewContent() {
		return this.viewContent;
	}

	/**
	 * Returns the <b>uninterpolated</b> includes file content that was loaded
	 * into this <code>ViewData</code> object. This is the best-fit localized
	 * content for the base includes filename in the portlet preferences given
	 * the locale. It is uninterpolated so that it can be reused by many
	 * different users and requests.
	 * 
	 * @return the localized, uninterpolated includes file content
	 */
	public ResourceBundle getIncludesContent() {
		return this.includesContent;
	}

	// ////////////////////////////////

	/**
	 * Load the view content. First get the input stream for the best-candidate
	 * version of the file given the user's current locale and the current base
	 * view filename. Then read that stream, until end of file, into the view
	 * content. If not found or not openable/readable, the view content is left
	 * null.
	 */
	private void loadViewContent(PortletRequest request) {

		String viewFilename = this.viewFilename;

		// Skip load and flag warning if view filename is improper.
		if ((viewFilename == null) || (viewFilename.length() == 0)) {
			handleWarning("View filename was undefined.");
			return;
		}
		if (viewFilename.indexOf("..") != -1) {
			handleWarning("View filename used illegal .. reference.");
			return;
		}

		// Get input stream for the view filename. Skip load and flag warning if
		// file not found.
		InputStream viewStream = I18nUtility.getLocalizedFileStream(request,
				viewFilename, this.locale, true);
		if (viewStream == null) {
			viewFilename = Utils.slashify(Consts.VIEW_FILE_DEFAULT_FOLDER
					+ this.viewFilename);
			viewStream = I18nUtility.getLocalizedFileStream(request,
					viewFilename, this.locale, true);
			if (viewStream == null) {
				handleWarning("No eligible view file was found (in either the portlet bundle directory or the portlet application) for the base file: "
						+ this.viewFilename + " or: " + viewFilename);
				return;
			}
		}

		// Read input stream into view content buffer. Skip load and flag
		// warning if read problem.
		StringBuffer sb = new StringBuffer();
		try {
			InputStreamReader is = new InputStreamReader(viewStream, "utf-8");
			char[] ch = new char[4096];
			int len = 0;
			while ((len = is.read(ch)) != -1) {
				sb.append(ch, 0, len);
			}
			is.close();
		} catch (IOException e) {
			handleWarning(
					"An eligible view file was found (in either the portlet bundle directory or the portlet application), but it could not be read, for the base file: "
							+ viewFilename, e);
			return;
		}

		// Finally store the buffer.
		this.viewContent = sb.toString();
		return;
	}

	/**
	 * Load the includes content. First get the input stream for the
	 * best-candidate version of the file given the user's current locale and
	 * the current base includes filename. Then read that stream, until end of
	 * file, into a resource bundle. If not found or not openable/readable, try
	 * to load from the classpath using the PropertyResourceBundleManager. If
	 * still not found or not openable/readable, the includes content is left
	 * null.
	 */
	private void loadIncludesContent(PortletRequest request) {

		String includesFilename = this.includesFilename;
		boolean isDefault = false;

		// If includes filename is not defined, revert to the default.
		if ((includesFilename == null) || (includesFilename.length() == 0)) {
			includesFilename = Consts.DEFAULT_INCLUDES_FILENAME;
			isDefault = true;
		}

		// Skip load and flag warning if includes filename is improper.
		if (includesFilename.indexOf("..") != -1) {
			handleWarning("Includes filename used illegal .. reference.");
			return;
		}

		// Get input stream for the includes filename. If this succeeds, try
		// making a resource bundle from it. If that succeeds, return (no
		// warning if empty), but if it fails, flag a warning and return.
		InputStream includesStream = I18nUtility.getLocalizedFileStream(
				request, includesFilename, this.locale, true);
		if (includesStream != null) {
			try {
				this.includesContent = new PropertyResourceBundle(
						includesStream);
				return;
			} catch (Exception e) {
				handleWarning(
						"An eligible includes file was found (in either the portlet bundle directory or the portlet application), but could not be loaded, for the base file: "
								+ includesFilename, e);
				return;
			}
		}

		// If the file was not found and loaded from the localized file stream,
		// try getting and loading it from the classpath (using
		// PropertyResourceBundleManager). If this also fails, log a warning
		// unless it was for the default includes file (which is OK if it does
		// not exist).
		this.includesContent = PropertyResourceBundleManager
				.getBundle(includesFilename);
		if ((this.includesContent == null) && !isDefault) {
			handleWarning("No eligible includes file was found (in either the portlet bundle directory, the portlet application, or the classpath) for the base file: "
					+ this.includesFilename);
		}
		return;
	}

	/**
	 * Flag an error.
	 */
	private void handleError() {
		error = true;
	}

	/**
	 * Flag an error and log an error message if error logging is enabled.
	 * 
	 * @param msg
	 *            log message
	 */
	private void handleError(String msg) {
		if (portletLog.isErrorEnabled()) {
			portletLog.error(msg);
		}
		error = true;
	}

	/**
	 * Flag an error and log an error message and caught exception, if error
	 * logging is enabled.
	 * 
	 * @param msg
	 *            log message
	 * @param ex
	 *            caught exception
	 */
	private void handleError(String msg, Exception e) {
		if (portletLog.isErrorEnabled()) {
			portletLog.error(msg, e);
		}
		error = true;
	}

	/**
	 * Flag a warning.
	 */
	private void handleWarning() {
		warning = true;
	}

	/**
	 * Flag a warning and log a warning message if warning logging is enabled.
	 * 
	 * @param msg
	 *            log message
	 */
	private void handleWarning(String msg) {
		if (portletLog.isWarnEnabled()) {
			portletLog.warn(msg);
		}
		warning = true;
	}

	/**
	 * Flag a warning and log a warning message and caught exception, if warning
	 * logging is enabled.
	 * 
	 * @param msg
	 *            log message
	 * @param ex
	 *            caught exception
	 */
	private void handleWarning(String msg, Exception e) {
		if (portletLog.isWarnEnabled()) {
			portletLog.warn(msg, e);
		}
		warning = true;
	}
}
