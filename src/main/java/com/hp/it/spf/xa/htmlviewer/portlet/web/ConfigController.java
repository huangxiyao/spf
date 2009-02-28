/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.htmlviewer.portlet.web;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import com.hp.it.spf.xa.htmlviewer.portlet.exception.InputErrorException;
import com.hp.it.spf.xa.htmlviewer.portlet.util.Consts;
import com.hp.it.spf.xa.htmlviewer.portlet.util.Utils;
import com.hp.it.spf.xa.i18n.portlet.I18nUtility;

import com.hp.frameworks.wpa.portlet.transaction.Transaction;
import com.hp.frameworks.wpa.portlet.transaction.TransactionImpl;
import com.hp.websat.timber.model.StatusIndicator;

/**
 * The controller class for <code>config</code> mode of the
 * <code>html-viewer</code> portlet. <code>html-viewer</code> is a JSR-168
 * portlet. Its <code>view</code> mode displays the proper localized version
 * of a configured HTML file, interpolated by the portlet
 * {@link com.hp.it.spf.xa.interpolate.portlet.FileInterpolator} via the
 * {@link com.hp.it.spf.xa.interpolate.portlet.web.FileInterpolatorController}.
 * In <code>config</code> mode, the portlet administrator is able to specify
 * the HTML file to display, as well as an option to rewrite hyperlinks in the
 * HTML file so that they launch a buttonless child window.
 * 
 * @author <link href="xiao-bing.zuo@hp.com">Zuo Xiaobing</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class ConfigController extends AbstractController {

	/**
	 * Empty constructor.
	 */
	public ConfigController() {

	}

	/** The view name. */
	private String viewName;

	/**
	 * Sets the view name (always <code>config</code>).
	 * 
	 * @param viewName
	 *            The view name
	 */
	public void setViewName(String viewName) {
		this.viewName = "config";
	}

	/**
	 * <p>
	 * Invoked during the render phase, this method gets the current
	 * configuration information from the portlet preferences, and sets the
	 * information into the model, for rendering in the JSP. The information set
	 * into the model includes:
	 * </p>
	 * 
	 * <dl>
	 * <p>
	 * <dt>The view filename.</dt>
	 * <dd>Its value is set into the model element named
	 * {@link com.hp.it.spf.xa.htmlviewer.portlet.util.Consts#VIEW_FILENAME}
	 * (taken from the portlet preference element of the same name). If no view
	 * filename has yet been set in the preferences, then the default value put
	 * into the model is blank.</dd>
	 * </p>
	 * <p>
	 * <dt>The token substitutions filename (optional).</dt>
	 * <dd>Its value is set into the model element named
	 * {@link com.hp.it.spf.xa.htmlviewer.portlet.util.Consts#INCLUDES_FILENAME}
	 * (taken from the portlet preference element of the same name). If no view
	 * filename has yet been set in the preferences, then the default value put
	 * into the model is blank.</dd>
	 * </p>
	 * <p>
	 * <dt>The launch-buttonless option (optional).</dt>
	 * <dd>Its value is set into the model element named
	 * {@link com.hp.it.spf.xa.htmlviewer.portlet.util.Consts#LAUNCH_BUTTONLESS}
	 * (taken from the portlet preference element of the same name) and will be
	 * either <code>true</code> or <code>false</code>. If the option has
	 * not yet been set in the preferences, then the default value put into the
	 * model is <code>false</code>.</dd>
	 * </p>
	 * <p>
	 * <dt>Any error message from a preceding action process.</dt>
	 * <dd>If there was such an error, the action process will have set the
	 * particular error code into the
	 * {@link com.hp.it.spf.xa.htmlviewer.portlet.util.Consts#ERROR_CODE} render
	 * parameter. So if that parameter exists, this method gets a localized
	 * error message for it and adds it into the model under the
	 * {@link com.hp.it.spf.xa.htmlviewer.portlet.util.Consts#ERROR_MESSAGE}
	 * element name (this is a {@link java.util.ArrayList} of accumulated error
	 * messages). </dd>
	 * </p>
	 * <p>
	 * <dt>Any warning message from a preceding action process.</dt>
	 * <dd>If there was such a warning, the action process will have set the
	 * particular warning code into the
	 * {@link com.hp.it.spf.xa.htmlviewer.portlet.util.Consts#WARN_CODE} render
	 * parameter. So if that parameter exists, this method gets a localized
	 * warning message for it and adds it into the model under the
	 * {@link com.hp.it.spf.xa.htmlviewer.portlet.util.Consts#WARN_MESSAGE}
	 * element name (this is a {@link java.util.ArrayList} of accumulated
	 * warning messages). </dd>
	 * </p>
	 * <p>
	 * <dt>Any other status message from a preceding action process.</dt>
	 * <dd>If there was any info status to report from the preceding action,
	 * the action process will have set the particular status code into the
	 * {@link com.hp.it.spf.xa.htmlviewer.portlet.util.Consts#INFO_CODE} render
	 * parameter. So if that parameter exists, this method gets a localized
	 * status message for it and copies it into the model under the
	 * {@link com.hp.it.spf.xa.htmlviewer.portlet.util.Consts#INFO_MESSAGE}
	 * element name (this is a {@link java.util.ArrayList} of accumulated info
	 * messages).</dd>
	 * </p>
	 * </dl>
	 * <p>
	 * This method also double-checks the filename preferences to make sure they
	 * have no errors or warnings; if either does, and they have not already
	 * been reported as above by the action phase, then this method adds error
	 * or warnings messages similarly for them to the model.
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
		Transaction trans = TransactionImpl.getTransaction(request);
		if (trans != null)
			trans.setStatusIndicator(StatusIndicator.OK); // Assume OK for
		// now.

		// Get the portlet preferences and copy them into the model.

		PortletPreferences pp = request.getPreferences();
		ModelAndView modelView = new ModelAndView(viewName);
		String viewFilename = Utils.slashify(pp.getValue(Consts.VIEW_FILENAME,
				""));
		String includesFilename = Utils.slashify(pp.getValue(
				Consts.INCLUDES_FILENAME, ""));
		String launchButtonless = pp
				.getValue(Consts.LAUNCH_BUTTONLESS, "false");
		modelView.addObject(Consts.VIEW_FILENAME, viewFilename);
		modelView.addObject(Consts.INCLUDES_FILENAME, includesFilename);
		modelView.addObject(Consts.LAUNCH_BUTTONLESS, launchButtonless);

		// Get any status codes passed from action phase and copy their messages
		// into the model. Currently only info status might be passed from the
		// action phase, but check for errors and warnings too.

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

		// Finally check the portlet preferences in the model for errors or
		// warnings, and add messages to the model as needed.
		if ((errorCode = Utils
				.checkViewFilenameForErrors(request, viewFilename)) != null) {
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
		if ((warnCode = Utils.checkIncludesFilenameForWarnings(request, includesFilename)) != null) {
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
	 * {@link com.hp.it.spf.xa.htmlviewer.portlet.util.Consts#VIEW_FILENAME}
	 * request parameter, the new token substitutions filename is expected to be
	 * in the
	 * {@link com.hp.it.spf.xa.htmlviewer.portlet.util.Consts#INCLUDES_FILENAME}
	 * request parameter, and the new launch-buttonless option is expected to be
	 * in the
	 * {@link com.hp.it.spf.xa.htmlviewer.portlet.util.Consts#LAUNCH_BUTTONLESS}
	 * request parameter. They are persisted into portlet preference elements of
	 * the same name.
	 * </p>
	 * <p>
	 * If any error occurs, this method throws an appropriate exception which
	 * Spring can forward to the appropriate error-handling JSP (if configured).
	 * However, an
	 * {@link com.hp.it.spf.xa.htmlviewer.portlet.exception.InputErrorException}
	 * on the part of the administrator is caught, not thrown, by this method.
	 * The portlet preferences are not stored when this happens; instead, the
	 * method sets the
	 * {@link com.hp.it.spf.xa.htmlviewer.portlet.util.Consts#ERROR_CODE} render
	 * parameter with the relevant error code and returns, allowing Spring to
	 * proceed into the render phase as usual.
	 * </p>
	 * <p>
	 * Upon successfully storing the input parameters, this method sets the
	 * {@link com.hp.it.spf.xa.htmlviewer.portlet.util.Consts#INFO_CODE} render
	 * parameter with any relevant information and returns, allowing Spring to
	 * proceed into the render phase as usual.
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

			// First edit-check the view filename value. If errors found, do not
			// store any of the new preferences.

			String errorCode = Utils.checkViewFilenameForErrors(request,
					viewFilename);
			if (errorCode != null) {
				throw new InputErrorException(request, errorCode);
			}

			// Next edit-check the includes filename value. Actually because
			// this is an optional field, there are no error conditions.

			// Next determine the launch-buttonless value. No error conditions
			// possible here.

			if (launchButtonless != null
					&& launchButtonless.equals(Consts.LAUNCH_BUTTONLESS)) {
				launchButtonless = "true";
			} else {
				launchButtonless = "false";
			}

			// Save the preferences and set the "success" info code.
			PortletPreferences pp = request.getPreferences();
			pp.setValue(Consts.VIEW_FILENAME, Utils.slashify(viewFilename));
			pp.setValue(Consts.INCLUDES_FILENAME, Utils
					.slashify(includesFilename));
			pp.setValue(Consts.LAUNCH_BUTTONLESS, launchButtonless);
			pp.store();
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
