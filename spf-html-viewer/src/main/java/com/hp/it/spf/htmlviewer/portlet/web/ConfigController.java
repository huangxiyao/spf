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

	private String ACTION_FLAG = "action";

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
	 * configuration information, and sets that information into the model, for
	 * rendering in the JSP. The information set into the model includes:
	 * </p>
	 * 
	 * <dl>
	 * <p>
	 * <dt>The view filename.</dt>
	 * <dd>Specifies the view file containing the content to interpolate and
	 * display during <code>view</code> mode. Its value is set into the model
	 * element named
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#VIEW_FILENAME} (taken
	 * from the portlet preference element of the same name). The view filename
	 * is obtained from a render parameter set by the action phase; otherwise it
	 * is obtained from the current view data (portlet preferences).</dd>
	 * </p>
	 * <p>
	 * <dt>The token substitutions (ie includes) filename (optional).</dt>
	 * <dd>Specifies any include properties to insert into the interpolated
	 * view file content during <code>view</code> mode. Its value is set into
	 * the model element named
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#INCLUDES_FILENAME}
	 * (taken from the portlet preference element of the same name). The
	 * includes filename is obtained from a render parameter set by the action
	 * phase; otherwise it is obtained from the current view data (portlet
	 * preferences).
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
	 * model is <code>false</code>. The launch-buttonless option is obtained
	 * from a render parameter set by the action phase; otherwise it is obtained
	 * from the current view data (portlet preferences).</dd>
	 * </p>
	 * <p>
	 * <dt>The check-seconds option (optional).</dt>
	 * <dd>Specifies for how long the configuration preferences are being
	 * cached, in seconds (<code>0</code> means no caching, and a negative
	 * number means caching forever). Its value is set into the model element
	 * named {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#CHECK_SECONDS}
	 * (taken from the portlet preference element of the same name). If the
	 * option has not yet been set in the preferences, then the default value
	 * put into the model is <code>900</code> (ie, cache for 15 minutes). The
	 * check-seconds option is obtained from a render parameter set by the
	 * action phase; otherwise it is obtained from the current view data
	 * (portlet preferences).</dd>
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

		ArrayList<String> errorCodes = new ArrayList<String>();
		ArrayList<String> warnCodes = new ArrayList<String>();
		ArrayList<String> infoCodes = new ArrayList<String>();
		String[] aErrorCodes, aWarnCodes, aInfoCodes;
		String errorMsg, warnMsg, infoMsg, errorCode, warnCode;
		String viewFilename, includesFilename, launchButtonless, checkSeconds;
		ModelAndView modelView = new ModelAndView(viewName);

		// Setup for WPAP logging; assume OK for now.
		Transaction trans = TransactionImpl.getTransaction(request);
		if (trans != null)
			trans.setStatusIndicator(StatusIndicator.OK);

		// Processing differs somewhat if we are following a preceding action
		// phase or not. When following an action phase, we want to render the
		// user inputs even if invalid, so we get the model data from the render
		// parameters set by the action phase. We also need to copy along any
		// errors recorded during the action phase so that they can be rendered
		// too. When not following an action phase, though, we must get the
		// model data from the view data cache (reading from portlet preferences
		// and backfilling into cache as needed).
		if (request.getParameter(ACTION_FLAG) != null) {

			// Get the model data from the render parameters, and copy it into
			// the model.
			viewFilename = request.getParameter(Consts.VIEW_FILENAME);
			if (viewFilename == null) 
				viewFilename = "";
			modelView.addObject(Consts.VIEW_FILENAME, viewFilename);
			
			includesFilename = request.getParameter(Consts.INCLUDES_FILENAME);
			if (includesFilename == null)
				includesFilename = "";
			modelView.addObject(Consts.INCLUDES_FILENAME, includesFilename);
			
			checkSeconds = request.getParameter(Consts.CHECK_SECONDS);
			modelView.addObject(Consts.CHECK_SECONDS, checkSeconds);

			launchButtonless = request.getParameter(Consts.LAUNCH_BUTTONLESS);
			modelView.addObject(Consts.LAUNCH_BUTTONLESS, launchButtonless);

			// Get any status codes from the render parameters and copy their
			// messages into the model. Currently only error and info status
			// might be passed from the action phase, but check for warnings
			// too.
			if ((aErrorCodes = request.getParameterValues(Consts.ERROR_CODE)) != null) {
				for (int i = 0; i < aErrorCodes.length; i++) {
					errorCodes.add(aErrorCodes[i]);
				}
			}
			if ((aWarnCodes = request.getParameterValues(Consts.WARN_CODE)) != null) {
				for (int i = 0; i < aWarnCodes.length; i++) {
					warnCodes.add(aWarnCodes[i]);
				}
			}
			if ((aInfoCodes = request.getParameterValues(Consts.INFO_CODE)) != null) {
				for (int i = 0; i < aInfoCodes.length; i++) {
					infoCodes.add(aInfoCodes[i]);
				}
			}

		} else {

			// Get the data from the current view data cache, and copy it into
			// the model. Throw an exception if we fail to get the data or there
			// was an error loading it (eg unable to read portlet preferences
			// database).
			ViewData data = ViewDataCache.getViewData(request);
			if ((data == null) || (data.error())) {
				throw new InternalErrorException(request,
						Consts.ERROR_CODE_INTERNAL);
			}
			viewFilename = data.getViewFilename();
			if (viewFilename == null) 
				viewFilename = "";
			modelView.addObject(Consts.VIEW_FILENAME, viewFilename);

			includesFilename = data.getIncludesFilename();
			if (includesFilename == null)
				includesFilename = "";
			modelView.addObject(Consts.INCLUDES_FILENAME, includesFilename);

			launchButtonless = String.valueOf(data.getLaunchButtonless());
			modelView.addObject(Consts.LAUNCH_BUTTONLESS, launchButtonless);

			checkSeconds = String.valueOf(data.getCheckSeconds());
			modelView.addObject(Consts.CHECK_SECONDS, checkSeconds);
		}

		// Next, check the view data for exception conditions and add
		// messages to the model as needed (some of these conditions might
		// already have been reported as errors over them if that is the case).
		// Some view data exception conditions are considered just warnings,
		// while others are considered errors.
		if ((errorCode = Utils
				.checkViewFilenameForErrors(request, viewFilename)) != null) {
			errorCodes.add(errorCode);
		}
		if ((errorCode = Utils.checkIncludesFilenameForErrors(request,
				includesFilename)) != null) {
			errorCodes.add(errorCode);
		}
		if ((errorCode = Utils.checkSecondsForErrors(request, checkSeconds)) != null) {
			errorCodes.add(errorCode);
		}
		if ((warnCode = Utils.checkViewFilenameForWarnings(request,
				viewFilename)) != null) {
			warnCodes.add(warnCode);
		}
		if ((warnCode = Utils.checkIncludesFilenameForWarnings(request,
				includesFilename)) != null) {
			warnCodes.add(warnCode);
		}

		// Finally, convert all the error codes to messages. If there were no
		// errors, convert all the warning codes and info codes to messages.
		// (When there are errors, we only report errors, not warnings or
		// infos.) Set the messages into the model for rendering in the view.
		if (!errorCodes.isEmpty()) {
			for (int i = 0; i < errorCodes.size(); i++) {
				errorMsg = I18nUtility.getMessage(request, errorCodes.get(i));
				addMessage(modelView, Consts.ERROR_MESSAGES, errorMsg);
			}
			if (trans != null)
				trans.setStatusIndicator(StatusIndicator.ERROR);
			return modelView;
		}
		if (!warnCodes.isEmpty()) {
			for (int i = 0; i < warnCodes.size(); i++) {
				warnMsg = I18nUtility.getMessage(request, warnCodes.get(i));
				addMessage(modelView, Consts.WARN_MESSAGES, warnMsg);
			}
			if (trans != null)
				trans.setStatusIndicator(StatusIndicator.WARNING);
		}
		for (int i = 0; i < infoCodes.size(); i++) {
			infoMsg = I18nUtility.getMessage(request, infoCodes.get(i));
			addMessage(modelView, Consts.INFO_MESSAGES, infoMsg);
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
	 * preference elements of the same name. They are also copied into render
	 * parameters of the same name, for the render phase to display.
	 * </p>
	 * <p>
	 * If any error occurs, this method throws an appropriate exception which
	 * Spring can forward to the appropriate error-handling JSP (if configured).
	 * However, any
	 * {@link com.hp.it.spf.htmlviewer.portlet.exception.InputErrorException}
	 * conditions on the part of the administrator are caught, not thrown, by
	 * this method. The portlet preferences are not stored when this happens;
	 * instead, the method sets the
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#ERROR_CODE} render
	 * parameter with the relevant error code(s) and returns, allowing Spring to
	 * proceed into the render phase as usual. The render phase will be
	 * responsible for printing error messages for these errors in the render
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

		ArrayList<String> errorCodes = new ArrayList<String>();
		Transaction trans = TransactionImpl.getTransaction(request);
		try {
			// Get the parameters and stage them to be copied to render phase.
			String viewFilename = Utils.slashify(request
					.getParameter(Consts.VIEW_FILENAME));
			if (viewFilename == null)
				viewFilename = "";
			response.setRenderParameter(Consts.VIEW_FILENAME, viewFilename);

			String includesFilename = Utils.slashify(request
					.getParameter(Consts.INCLUDES_FILENAME));
			if (includesFilename == null)
				includesFilename = "";
			response.setRenderParameter(Consts.INCLUDES_FILENAME,
					includesFilename);

			String checkSeconds = request.getParameter(Consts.CHECK_SECONDS);
			if ((checkSeconds == null) || (checkSeconds.trim().length() == 0))
				checkSeconds = Consts.DEFAULT_CHECK_SECONDS;
			response.setRenderParameter(Consts.CHECK_SECONDS, checkSeconds);

			String launchButtonless = request
					.getParameter(Consts.LAUNCH_BUTTONLESS);
			if (launchButtonless != null
					&& launchButtonless.equals(Consts.LAUNCH_BUTTONLESS)) {
				launchButtonless = "true";
			} else {
				launchButtonless = "false";
			}
			response.setRenderParameter(Consts.LAUNCH_BUTTONLESS,
					launchButtonless);
			response.setRenderParameter(ACTION_FLAG, "true");

			// First edit-check the view filename value. If errors found, do not
			// store any of the new preferences.
			String errorCode = Utils.checkViewFilenameForErrors(request,
					viewFilename);
			if (errorCode != null) {
				errorCodes.add(errorCode);
			}

			// Next edit-check the includes filename value. If errors found, do
			// not store any of the new preferences.
			errorCode = Utils.checkIncludesFilenameForErrors(request,
					includesFilename);
			if (errorCode != null) {
				errorCodes.add(errorCode);
			}

			// Next edit-check the check-seconds value (it must be an integer).
			// If error found, do not store any of the new preferences.
			errorCode = Utils.checkSecondsForErrors(request, checkSeconds);
			if (errorCode != null) {
				errorCodes.add(errorCode);
			}

			// Throw exception if there were any edit-check failures above.
			if (!errorCodes.isEmpty()) {
				errorCodes.add(Consts.ERROR_CODE_PREFS_UNSAVED);
				throw new InputErrorException(request);
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
					new String[] { Consts.INFO_CODE_PREFS_SAVED });

			// Log success to WPAP logs, recording the parameters as context
			// info.
			if (trans != null) {
				trans.addContextInfo("viewFilename", viewFilename);
				trans.addContextInfo("includesFilename", includesFilename);
				trans.addContextInfo("launchButtonless", launchButtonless);
				trans.addContextInfo("checkSeconds", checkSeconds);
				trans.setStatusIndicator(StatusIndicator.OK);
			}
		} catch (InputErrorException e) {
			response.setRenderParameter(Consts.ERROR_CODE, errorCodes
					.toArray(new String[] {}));

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
