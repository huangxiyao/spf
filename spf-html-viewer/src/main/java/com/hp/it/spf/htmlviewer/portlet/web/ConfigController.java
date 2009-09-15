/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.htmlviewer.portlet.web;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import com.hp.it.spf.htmlviewer.portlet.exception.InputErrorException;
import com.hp.it.spf.htmlviewer.portlet.exception.InternalErrorException;
import com.hp.it.spf.htmlviewer.portlet.util.Consts;
import com.hp.it.spf.htmlviewer.portlet.util.Utils;
import com.hp.it.spf.htmlviewer.portlet.util.ViewData;
import com.hp.it.spf.htmlviewer.portlet.util.ViewDataCache;
import com.hp.it.spf.xa.i18n.portlet.I18nUtility;

import com.hp.frameworks.wpa.portlet.transaction.Transaction;
import com.hp.frameworks.wpa.portlet.transaction.TransactionImpl;
import com.hp.websat.timber.model.StatusIndicator;

/**
 * <p>
 * The controller class for <code>config</code> mode of the HTML viewer
 * portlet. The HTML viewer is a JSR-286 portlet whose <code>view</code> mode
 * displays the interpolated content of a configured HTML file (called a <i>view
 * file</i>). In <code>config</code> mode, the portlet administrator is able
 * to specify the view file to display, as well as other criteria such as a
 * token-substitutions file for the <code>{INCLUDE:<i>key</i>}</code> token
 * (called an <i>includes file</i>). These are stored as JSR 286 portlet
 * configuration preferences.
 * </p>
 * <p>
 * As of SPF 1.2, caching is supported for all of the external resources used by
 * this <code>config</code> mode controller: the portlet preferences, the view
 * file content, and the includes file content. A new portlet preference
 * controls the retention period for the cache.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version SPF 1.2
 * @see <code>com.hp.it.spf.htmlviewer.portlet.util.ViewData</code>
 * <code>com.hp.it.spf.htmlviewer.portlet.util.ViewDataCache</code>
 */
public class ConfigController extends AbstractController {

	/** The view name. */
	private String viewName = "config";

	/**
	 * Empty constructor.
	 */
	public ConfigController() {
	}

	/**
	 * Sets the view name (always <code>config</code>).
	 * 
	 * @param viewName
	 *            The view name
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * <p>
	 * Invoked during the render phase, this method gets the current
	 * configuration information from the view data, and sets that information
	 * into the model, for rendering in the JSP. The information set into the
	 * model includes:
	 * </p>
	 * 
	 * <dl>
	 * <p>
	 * <dt>The view filename.</dt>
	 * <dd>Specifies the view file containing the content to interpolate and
	 * display during <code>view</code> mode. Its value is set into the model
	 * element named
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#VIEW_FILENAME} (taken
	 * from the portlet preference element of the same name). If no view
	 * filename has yet been set in the preferences, then the default value put
	 * into the model is blank.</dd>
	 * </p>
	 * <p>
	 * <dt>The token substitutions (ie includes) filename (optional).</dt>
	 * <dd>Specifies any include properties to insert into the interpolated
	 * view file content during <code>view</code> mode. Its value is set into
	 * the model element named
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#INCLUDES_FILENAME}
	 * (taken from the portlet preference element of the same name). If no view
	 * filename has yet been set in the preferences, then the default value put
	 * into the model is
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#DEFAULT_INCLUDES_FILENAME} (<code>html_viewer_includes.properties</code>).</dd>
	 * </p>
	 * <p>
	 * <dt>The launch-buttonless option (optional).</dt>
	 * <dd>Specifies whether to rewrite hyperlinks (anchor tags) during
	 * <code>view</code> mode, to launch links in a buttonless child window.
	 * Its value is set into the model element named
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#LAUNCH_BUTTONLESS}
	 * (taken from the portlet preference element of the same name) and will be
	 * either <code>true</code> or <code>false</code>. If the option has
	 * not yet been set in the preferences, then the default value put into the
	 * model is <code>false</code>.</dd>
	 * </p>
	 * <p>
	 * <dt>The check-seconds option (optional).</dt>
	 * <dd>Specifies for how long the configuration preferences are being
	 * cached, in seconds (<code>0</code> means no caching, and a negative
	 * number means caching forever). Its value is set into the model element
	 * named {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#CHECK_SECONDS}
	 * (taken from the portlet preference element of the same name). If the
	 * option has not yet been set in the preferences, then the default value
	 * put into the model is <code>0</code> (ie, no caching).</dd>
	 * </p>
	 * <p>
	 * <dt>Any error message from a preceding action phase.</dt>
	 * <dd>If there was such an error, the action process will have set the
	 * particular error code into the
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#ERROR_CODE} render
	 * parameter. So if that parameter exists, this method gets a localized
	 * error message for it and adds it into the model under the
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#ERROR_MESSAGE}
	 * element name (this is a {@link java.util.ArrayList} of accumulated error
	 * messages). </dd>
	 * </p>
	 * <p>
	 * <dt>Any warning message from a preceding action phase.</dt>
	 * <dd>If there was such a warning, the action process will have set the
	 * particular warning code into the
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#WARN_CODE} render
	 * parameter. So if that parameter exists, this method gets a localized
	 * warning message for it and adds it into the model under the
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#WARN_MESSAGE} element
	 * name (this is a {@link java.util.ArrayList} of accumulated warning
	 * messages). </dd>
	 * </p>
	 * <p>
	 * <dt>Any other status message from a preceding action phase.</dt>
	 * <dd>If there was any info status to report from the preceding action,
	 * the action process will have set the particular status code into the
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#INFO_CODE} render
	 * parameter. So if that parameter exists, this method gets a localized
	 * status message for it and copies it into the model under the
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#INFO_MESSAGE} element
	 * name (this is a {@link java.util.ArrayList} of accumulated info
	 * messages).</dd>
	 * </p>
	 * </dl>
	 * <p>
	 * This method also double-checks the view and includes filenames to make
	 * sure they have no errors or warnings; if either does, and they have not
	 * already been reported as above by the action phase, then this method adds
	 * error or warnings messages similarly for them to the model.
	 * </p>
	 * <p>
	 * If an exception occurs during this method, it will be caught and handled
	 * locally if it is recoverable. If it is not recoverable - for example,
	 * when none of the view data can be obtained - an
	 * {@link com.hp.it.spf.htmlviewer.portlet.exception.InternalErrorException}
	 * will be generated; this method will allow that to propagate back to
	 * Spring. The HTML viewer portlet's Spring configuration is setup to
	 * forward such <code>InternalErrorExceptions</code> into the proper JSP
	 * automatically, using
	 * {@link com.hp.it.spf.xa.exception.portlet.handler.SimpleMappingExceptionResolver}.
	 * </p>
	 * <p>
	 * As of SPF 1.2, a view data cache is used for the view data, so that in
	 * most normal operations, portlet preferences and the filesystem are not
	 * used since the data is entirely in cache. When the cache is initially
	 * empty, and also when the cache is invalidated, then the view data is
	 * obtained from portlet preferences and the filesystem and backfilled into
	 * the cache. The cache retention period is defined by the check-seconds
	 * configuration preference, and may be set to cache forever (value:
	 * <code>-1</code>), never cache (value: <code>0</code>), or cache for
	 * a positive number of seconds.
	 * </p>
	 * <p>
	 * This method uses WPAP Timber logging to record the outcome.
	 * </p>
	 * 
	 * @param request
	 *            The render request.
	 * @param response
	 *            The render response.
	 * @return The model containing the configuration information.
	 * @throws Exception
	 *             Some exception.
	 */
	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {

		String errorCode = null;
		String warnCode = null;
		String infoCode = null;

		// Setup for WPAP logging; assume OK for now.
		Transaction trans = TransactionImpl.getTransaction(request);
		if (trans != null)
			trans.setStatusIndicator(StatusIndicator.OK);

		// Get the current view data, loading and backfilling into cache as
		// needed. Throw exception if we can't get the data or there is a fatal
		// error (ie cannot read data from database).
		ModelAndView modelView = new ModelAndView(viewName);
		ViewData data = ViewDataCache.getViewData(request);
		if ((data == null) || (data.error())) {
			throw new InternalErrorException(request,
					Consts.ERROR_CODE_INTERNAL);
		}

		// Copy the current view data into the model.
		String viewFilename = data.getViewFilename();
		String includesFilename = data.getIncludesFilename();
		String launchButtonless = String.valueOf(data.getLaunchButtonless());
		String checkSeconds = String.valueOf(data.getCheckSeconds());
		modelView.addObject(Consts.VIEW_FILENAME, viewFilename);
		modelView.addObject(Consts.INCLUDES_FILENAME, includesFilename);
		modelView.addObject(Consts.LAUNCH_BUTTONLESS, launchButtonless);
		modelView.addObject(Consts.CHECK_SECONDS, checkSeconds);

		// Get any status codes passed from action phase and copy their messages
		// into the model. Currently only error and info status might be passed
		// from the action phase, but check for warnings too.
		if ((errorCode = request.getParameter(Consts.ERROR_CODE)) != null) {
			String errorMsg = I18nUtility.getMessage(request, errorCode);
			addMessage(modelView, Consts.ERROR_MESSAGES, errorMsg);
			if (trans != null)
				trans.setStatusIndicator(StatusIndicator.ERROR);
		}
		if ((warnCode = request.getParameter(Consts.WARN_CODE)) != null) {
			String warnMsg = I18nUtility.getMessage(request, warnCode);
			addMessage(modelView, Consts.WARN_MESSAGES, warnMsg);
			if (trans != null)
				trans.setStatusIndicator(StatusIndicator.WARNING);
		}
		if ((infoCode = request.getParameter(Consts.INFO_CODE)) != null) {
			String infoMsg = I18nUtility.getMessage(request, infoCode);
			addMessage(modelView, Consts.INFO_MESSAGES, infoMsg);
		}

		// Check the view data for exception conditions and add messages to the
		// model as needed. Some view data exception conditions are considered
		// just warnings, while others are considered errors. Most, but not all,
		// of these conditions also cause data.warning() to return true.
		if ((errorCode = Utils
				.checkViewFilenameForErrors(request, viewFilename)) != null) {
			String errorMsg = I18nUtility.getMessage(request, errorCode);
			addMessage(modelView, Consts.ERROR_MESSAGES, errorMsg);
			if (trans != null)
				trans.setStatusIndicator(StatusIndicator.ERROR);
		}
		if ((errorCode = Utils.checkIncludesFilenameForErrors(request,
				includesFilename)) != null) {
			String errorMsg = I18nUtility.getMessage(request, errorCode);
			addMessage(modelView, Consts.ERROR_MESSAGES, errorMsg);
			if (trans != null)
				trans.setStatusIndicator(StatusIndicator.ERROR);
		}
		if ((errorCode = Utils.checkSecondsForErrors(request, checkSeconds)) != null) {
			String errorMsg = I18nUtility.getMessage(request, errorCode);
			addMessage(modelView, Consts.ERROR_MESSAGES, errorMsg);
			if (trans != null)
				trans.setStatusIndicator(StatusIndicator.ERROR);
		}
		if ((warnCode = Utils.checkViewFilenameForWarnings(request,
				viewFilename)) != null) {
			String warnMsg = I18nUtility.getMessage(request, warnCode);
			addMessage(modelView, Consts.WARN_MESSAGES, warnMsg);
			if (trans != null)
				trans.setStatusIndicator(StatusIndicator.WARNING);
		}
		if ((warnCode = Utils.checkIncludesFilenameForWarnings(request,
				includesFilename)) != null) {
			String warnMsg = I18nUtility.getMessage(request, warnCode);
			addMessage(modelView, Consts.WARN_MESSAGES, warnMsg);
			if (trans != null)
				trans.setStatusIndicator(StatusIndicator.WARNING);
		}

		return modelView;
	}

	/**
	 * <p>
	 * Invoked during the action phase, this method updates the submitted
	 * configuration information into the portlet preferences. The new view
	 * filename is expected to be in the
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#VIEW_FILENAME}
	 * request parameter, the new token substitutions filename is expected to be
	 * in the
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#INCLUDES_FILENAME}
	 * request parameter, the new launch-buttonless option is expected to be in
	 * the
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#LAUNCH_BUTTONLESS}
	 * request parameter, and the new check-seconds option is expected to be in
	 * the {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#CHECK_SECONDS}
	 * request parameter. They are edit-checked and persisted into portlet
	 * preference elements of the same name.
	 * </p>
	 * <p>
	 * If any error occurs, this method throws an appropriate exception which
	 * Spring can forward to the appropriate error-handling JSP (if configured).
	 * However, an
	 * {@link com.hp.it.spf.htmlviewer.portlet.exception.InputErrorException} on
	 * the part of the administrator is caught, not thrown, by this method. The
	 * portlet preferences are not stored when this happens; instead, the method
	 * sets the {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#ERROR_CODE}
	 * render parameter with the relevant error code and returns, allowing
	 * Spring to proceed into the render phase as usual. The render phase will
	 * be responsible for printing error messages for these errors in the render
	 * UI.
	 * </p>
	 * <p>
	 * If no error occurs, then upon successfully storing the input parameters,
	 * this method sets the
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#INFO_CODE} render
	 * parameter with any relevant information and returns, allowing Spring to
	 * proceed into the render phase as usual. The render phase will be
	 * responsible for printing the info message to the user in the render UI.
	 * </p>
	 * <p>
	 * As of SPF 1.2, a view data cache is used by this portlet for the view
	 * data, so that in most normal operations, portlet preferences and the
	 * filesystem are not used since the data is entirely in cache. Since the
	 * action phase of <code>config</code> mode constitutes a change to this
	 * view data, the action phase clears the cache so subsequent renders of
	 * <code>view</code> or <code>config</code> modes do not have to wait
	 * for the cache to naturally expire.
	 * </p>
	 * <p>
	 * This method uses WPAP Timber logging to record the outcome.
	 * </p>
	 * 
	 * @param request
	 *            The action request.
	 * @param response
	 *            The action response.
	 * @throws Exception
	 *             Some exception.
	 */
	protected void handleActionRequestInternal(ActionRequest request,
			ActionResponse response) throws Exception {

		Transaction trans = TransactionImpl.getTransaction(request);
		try {
			String viewFilename = request.getParameter(Consts.VIEW_FILENAME);
			String includesFilename = request
					.getParameter(Consts.INCLUDES_FILENAME);
			String launchButtonless = request
					.getParameter(Consts.LAUNCH_BUTTONLESS);
			String checkSeconds = request.getParameter(Consts.CHECK_SECONDS);

			// First edit-check the view filename value. If errors found, do not
			// store any of the new preferences.
			String errorCode = Utils.checkViewFilenameForErrors(request,
					viewFilename);
			if (errorCode != null) {
				throw new InputErrorException(request, errorCode);
			}

			// Next edit-check the includes filename value. If errors found, do
			// not store any of the new preferences.
			errorCode = Utils.checkIncludesFilenameForErrors(request,
					includesFilename);
			if (errorCode != null) {
				throw new InputErrorException(request, errorCode);
			}

			// Next edit-check the check-seconds value (it must be an integer).
			// If error found, do not store any of the new preferences.
			errorCode = Utils.checkSecondsForErrors(request, checkSeconds);
			if (errorCode != null) {
				throw new InputErrorException(request, errorCode);
			}
			if ((checkSeconds == null) || (checkSeconds.trim().length() == 0)) {
				checkSeconds = Consts.DEFAULT_CHECK_SECONDS;
			}

			// Next determine the launch-buttonless value. No error conditions
			// possible here.
			if (launchButtonless != null
					&& launchButtonless.equals(Consts.LAUNCH_BUTTONLESS)) {
				launchButtonless = "true";
			} else {
				launchButtonless = "false";
			}

			// Save the view data to portlet preferences, flush the cache, and
			// set the "success" info code unless there was a fatal error.
			ViewDataCache.resetViewData(request);
			ViewData data = new ViewData(request, viewFilename,
					includesFilename, Boolean.parseBoolean(launchButtonless),
					Integer.parseInt(checkSeconds));
			if ((data == null) || (data.error())) {
				throw new InternalErrorException(request,
						Consts.ERROR_CODE_INTERNAL);
			}
			response.setRenderParameter(Consts.INFO_CODE,
					Consts.INFO_CODE_PREFS_SAVED);

			// Log success to WPAP logs, recording the parameters as context
			// info.
			if (trans != null) {
				trans.addContextInfo("viewFilename", viewFilename);
				trans.addContextInfo("includesFilename", includesFilename);
				trans.addContextInfo("launchButtonless", launchButtonless);
				trans.setStatusIndicator(StatusIndicator.OK);
			}
		} catch (InputErrorException e) {
			response.setRenderParameter(Consts.ERROR_CODE, e.getErrorCode());

			// Log error to WPAP logs. Do not generate an error/error-trace
			// however since those are only for system errors and this is a user
			// error.
			if (trans != null)
				trans.setStatusIndicator(StatusIndicator.ERROR);
		}
		// Note: exceptions thrown back to Spring are automatically logged by
		// WPAP (transaction logging interceptor) to the error/error-trace logs,
		// so no need to set thrown exception info here.
	}

	/**
	 * Add an error, warning, or info message to the model if it is not already
	 * there, while retaining what is already there.
	 */
	@SuppressWarnings("unchecked")
	private void addMessage(ModelAndView model, String msgType, String errMsg) {
		if (model != null && errMsg != null) {
			errMsg = errMsg.trim();
			if (errMsg.length() > 0) {
				Map modelMap = model.getModel();
				ArrayList<String> errMsgs = null;
				try {
					errMsgs = (ArrayList<String>) modelMap.get(msgType);
				} catch (ClassCastException e) {
					// should never happen
				}
				if (errMsgs == null) {
					errMsgs = new ArrayList<String>();
				}
				// suppress duplicate messages
				for (int i = 0; i < errMsgs.size(); i++) {
					if (errMsg.equals(errMsgs.get(i)))
						return;
				}
				errMsgs.add(errMsg);
				model.addObject(msgType, errMsgs);
			}
		}
	}

}
