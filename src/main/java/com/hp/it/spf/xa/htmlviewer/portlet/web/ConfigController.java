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
	 * Gets the current configuration information from the portlet preferences,
	 * and sets the information into the return, for rendering in the JSP.
	 * 
	 * @param request
	 *            The render request.
	 * @param response
	 *            The render response.
	 * @return The model containing the configuration information.
	 * @throws Some
	 *             exception.
	 */
	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {

		PortletPreferences pp = request.getPreferences();
		ModelAndView modelView = new ModelAndView(viewName);
		modelView.addObject(Consts.VIEW_FILENAME, pp.getValue(
				Consts.VIEW_FILENAME, null));
		modelView.addObject(Consts.LAUNCH_BUTTONLESS, pp.getValue(
				Consts.LAUNCH_BUTTONLESS, null));

		if (request.getParameter(Consts.ERROR_CODE) != null) {
			String errorMsg = I18nUtility.getMessage(request, request
					.getParameter(Consts.ERROR_CODE));
			modelView.addObject(Consts.ERROR_MESSAGE, errorMsg);
		}
		return modelView;
	}

	/**
	 * Saves updated configuration information into the portlet preferences,
	 * taken from the request.
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
		try {
			String viewFile = request.getParameter(Consts.VIEW_FILENAME);
			String buttonLess = request.getParameter(Consts.LAUNCH_BUTTONLESS);

			if (viewFile == null || viewFile.trim().length() == 0) {
				throw new InputErrorException(Consts.ERROR_CODE_VIEW_FILENAME_NULL);
			}
			if ((viewFile.indexOf("/") != -1) || (viewFile.indexOf("\\")) != -1) {
				throw new InputErrorException(Consts.ERROR_CODE_VIEW_FILENAME_PATH);
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
		} catch (InputErrorException e) {
			response.setRenderParameter(Consts.ERROR_CODE, e.getErrorCode());
		}
	}
}
