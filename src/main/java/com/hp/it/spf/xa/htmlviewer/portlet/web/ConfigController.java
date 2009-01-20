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

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import com.hp.it.spf.xa.misc.portlet.Utils;
import com.hp.it.spf.xa.htmlviewer.portlet.exception.InputErrorException;
import com.hp.it.spf.xa.htmlviewer.portlet.util.Consts;
import com.hp.it.spf.xa.i18n.portlet.I18nUtility;
import com.hp.websat.timber.logging.Log;

/**
 * The controller class for config mode of the HTMLViewer portlet. HTMLViewer is
 * a JSR-168 portlet which displays the proper localized version of a configured
 * HTML file, as interpolated by the portlet FileInterpolator.
 * 
 * @author <link href="xiao-bing.zuo@hp.com">Zuo Xiaobing</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see com.hp.it.spf.xa.interpolate.portlet.web.FileInterpolatorController
 *      com.hp.it.spf.xa.interpolate.portlet.FileInterpolator
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
	 * <code>viewFilename</code>. If no view filename has yet been set in the
	 * preferences, then the default value put into the model is blank.</dd>
	 * </p>
	 * <p>
	 * <dt>The launch-buttonless option.</dt>
	 * <dd>Its value is set into the model element named
	 * <code>launchButtonless</code> and will be either <code>true</code> or
	 * <code>false</code>. If the option has not yet been set in the
	 * preferences, then the default value put into the model is
	 * <code>false</code>.</dd>
	 * </p>
	 * <p>
	 * <dt>Any error message from a preceding action process.</dt>
	 * <dd>If there was such an error, the action process will have set the
	 * particular error code into the <code>errorCode</code> render parameter.
	 * So if that parameter exists, this method gets a localized error message
	 * for it and copies it into the model under the <code>errorMessage</code>
	 * element name. </dd>
	 * </p>
	 * <p>
	 * <dt>Any other status message from a preceding action process.</dt>
	 * <dd>If there was any other (ie non-error) status to report from the
	 * preceding action (if any), the action process will have set the
	 * particular status code into the <code>infoCode</code> render parameter.
	 * So if that parameter exists, this method gets a localized status message
	 * for it and copies it into the model under the <code>infoMessage</code>
	 * element name.</dd>
	 * </p>
	 * </dl>
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

		Log.logInfo(this, "ConfigController: render phase invoked.");
		PortletPreferences pp = request.getPreferences();
		ModelAndView modelView = new ModelAndView(viewName);
		modelView.addObject(Consts.VIEW_FILENAME, pp.getValue(
				Consts.VIEW_FILENAME, ""));
		modelView.addObject(Consts.LAUNCH_BUTTONLESS, pp.getValue(
				Consts.LAUNCH_BUTTONLESS, "false"));

		String errorCode = null;
		if ((errorCode = request.getParameter(Consts.ERROR_CODE)) != null) {
			String errorMsg = I18nUtility.getMessage(request, errorCode);
			modelView.addObject(Consts.ERROR_MESSAGE, errorMsg);
		}
		String infoCode = null;
		if ((infoCode = request.getParameter(Consts.INFO_CODE)) != null) {
			String infoMsg = I18nUtility.getMessage(request, infoCode);
			modelView.addObject(Consts.INFO_MESSAGE, infoMsg);
		}
		return modelView;
	}

	/**
	 * <p>
	 * Invoked during the action phase, this method updates the submitted
	 * configuration information into the portlet preferences.
	 * </p>
	 * <p>
	 * If any error occurs, this method throws an appropriate exception, which
	 * Spring can forward to the appropriate error-handling JSP (if configured).
	 * However, an input-error exception on the part of the administrator is
	 * caught, not thrown, by this method. In that case, the particular error
	 * code is set into the <code>errorCode</code> render parameter and the
	 * method simply returns, allowing Spring to proceed into the render phase
	 * as usual.
	 * </p>
	 * <p>
	 * Upon successfully storing the input parameters, this method sets the
	 * <code>infoCode</code> render parameter with any relevant information
	 * and returns, allowing Spring to proceed into the render phase as usual.
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

		Log.logInfo(this, "ConfigController: action phase invoked.");
		try {
			String viewFile = request.getParameter(Consts.VIEW_FILENAME);
			String buttonLess = request.getParameter(Consts.LAUNCH_BUTTONLESS);

			if (viewFile == null || viewFile.trim().length() == 0) {
				// use info logging as this is a user error only, not a system
				// error
				Log
						.logInfo(this,
								"ConfigController: view filename from form is null or empty.");
				throw new InputErrorException(
						Consts.ERROR_CODE_VIEW_FILENAME_NULL);
			}
			if ((viewFile.indexOf("../") != -1)
					|| (viewFile.indexOf("..\\")) != -1) {
				// use info logging as this is a user error only, not a system
				// error
				Log
						.logInfo(this,
								"ConfigController: view filename from form contains outside path information.");
				throw new InputErrorException(
						Consts.ERROR_CODE_VIEW_FILENAME_PATH);
			}

			if (buttonLess != null
					&& buttonLess.equals(Consts.LAUNCH_BUTTONLESS)) {
				buttonLess = "true";
			} else {
				buttonLess = "false";
			}

			PortletPreferences pp = request.getPreferences();
			pp.setValue(Consts.VIEW_FILENAME, viewFile);
			pp.setValue(Consts.LAUNCH_BUTTONLESS, buttonLess);
			pp.store();
			response.setRenderParameter(Consts.INFO_CODE,
					Consts.INFO_CODE_PREFS_SAVED);
			Log.logInfo(this,
					"ConfigController: preferences saved: view filename: "
							+ viewFile + "; launch buttonless child window: "
							+ buttonLess);
		} catch (InputErrorException e) {
			response.setRenderParameter(Consts.ERROR_CODE, e.getErrorCode());
		}
	}
}
