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
import java.util.LinkedHashMap;
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
	private boolean fatal = false;
	private LinkedHashMap<String, String> errors = new LinkedHashMap<String, String>();

	/**
	 * <p>
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
	 * </p>
	 * <p>
	 * Use the instance methods to find out whether the creation of the object
	 * succeeded or whether there was a fatal (or non-fatal) error.
	 * </p>
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
					// Don't flag an error since there is a reasonable default.
					this.checkSeconds = Integer
							.parseInt(Consts.DEFAULT_CHECK_SECONDS);
				}
			} catch (Exception e) {
				setFatal(Consts.ERROR_CODE_INTERNAL,
						"Unable to read portlet preferences; reason: " + e);
			} finally {
				// Now load the content corresponding to the preferences.
				loadViewContent(request);
				loadIncludesContent(request);
			}
		}
	}

	/**
	 * <p>
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
	 * </p>
	 * <p>
	 * Use the instance methods to find out whether the creation of the object
	 * (and save to the database) succeeded or whether there was a fatal (or
	 * non-fatal) error.
	 * </p>
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
				setFatal(Consts.ERROR_CODE_INTERNAL,
						"Unable to save portlet preferences; reason: " + e);
			} finally {
				// Lastly load the content corresponding to the preferences.
				loadViewContent(request);
				loadIncludesContent(request);
			}
		}
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
	 * without any fatal or non-fatal errors. If false is returned, that means
	 * there were fatal or non-fatal errors. In that case, you can use the
	 * {@link #getErrors()} method to get the set of errors, and the
	 * {@link #fatal()} method to check whether any were fatal.
	 * 
	 * @return true if there was no error during load, false otherwise
	 */
	public boolean ok() {
		return this.errors.isEmpty() && !this.fatal;
	}

	/**
	 * Returns true if a fatal error was encountered when this
	 * <code>ViewData</code> object was created (loaded). You can use the
	 * {@link #getErrors()} method to get the full set of errors (some of which
	 * may have been fatal and some of which may have been non-fatal).
	 * 
	 * @return true if there was a fatal error during load, false otherwise
	 */
	public boolean fatal() {
		return !this.errors.isEmpty() && this.fatal;
	}

	/**
	 * Returns the set of errors encountered when this <code>ViewData</code>
	 * object was created (loaded). The keys in the map are the error codes;
	 * each one points to an internal diagnostic message containing further
	 * information. Generally the diagnostic messages are not end-user-friendly
	 * and are not localized; they should be used for logging/reporting purposes
	 * only. If there were no errors, the returned map is empty.
	 * 
	 * @return the map of errors (empty if none)
	 */
	public LinkedHashMap<String,String> getErrors() {
		return this.errors;
	}

	/**
	 * Returns the base view filename that was loaded into this
	 * <code>ViewData</code> object. This corresponds with the base view
	 * filename preference in the portlet preferences. If there was a fatal
	 * error during load, this value may be unreliable.
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
	 * filename preference in the portlet preferences. If there was a fatal
	 * error during load, this value may be unreliable.
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
	 * launch-buttonless preference in the portlet preferences. If there was a
	 * fatal error during load, this value may be unreliable.
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
	 * portlet preferences. If there was a fatal error during load, this value
	 * may be unreliable.
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
	 * users and requests. If there was an error during load, this content may
	 * be unreliable.
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
	 * different users and requests. If there was an error during load, this
	 * content may be unreliable.
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
			setError(Consts.ERROR_CODE_VIEW_FILENAME_NULL);
			return;
		}
		if (viewFilename.indexOf("..") != -1) {
			setError(Consts.ERROR_CODE_VIEW_FILENAME_PATH, viewFilename);
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
				setError(Consts.ERROR_CODE_VIEW_FILE_NULL, this.viewFilename);
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
			setError(Consts.ERROR_CODE_INTERNAL, "Unable to read view file: "
					+ viewFilename + "; reason: " + e);
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
			setError(Consts.ERROR_CODE_INCLUDES_FILENAME_PATH, includesFilename);
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
				setError(Consts.ERROR_CODE_INTERNAL,
						"Unable to read includes file: " + includesFilename
								+ "; reason: " + e);
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
			setError(Consts.ERROR_CODE_INCLUDES_FILE_NULL, includesFilename);
		}
		return;
	}

	/**
	 * Flag a non-fatal error and store the given diagnostics.
	 * 
	 * @param warnCode
	 *            the diagnostic error code
	 */
	private void setError(String errorCode) {
		setError(errorCode, null);
	}

	/**
	 * Flag a non-fatal error and store the given diagnostics.
	 * 
	 * @param errorCode
	 *            the diagnostic error code
	 * @param errorParam
	 *            a diagnostic error parameter
	 */
	private void setError(String errorCode, String errorParam) {
		this.errors.put(errorCode, Utils.getDiagnostic(errorCode, errorParam));
	}

	/**
	 * Flag a fatal error and store the given diagnostics.
	 * 
	 * @param errorCode
	 *            the diagnostic error code
	 */
	private void setFatal(String errorCode) {
		setFatal(errorCode, null);
	}

	/**
	 * Flag a fatal error and store the given diagnostics.
	 * 
	 * @param errorCode
	 *            the diagnostic error code
	 * @param errorParam
	 *            a diagnostic error parameter
	 */
	private void setFatal(String errorCode, String errorParam) {
		setError(errorCode, errorParam);
		this.fatal = true;
	}
}
