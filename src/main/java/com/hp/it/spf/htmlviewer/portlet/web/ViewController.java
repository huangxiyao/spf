/*
 * Project: Shared Portal Framework.
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
package com.hp.it.spf.htmlviewer.portlet.web;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;

import com.hp.it.spf.htmlviewer.portlet.util.Consts;
import com.hp.it.spf.htmlviewer.portlet.exception.InternalErrorException;
import com.hp.it.spf.xa.interpolate.portlet.FileInterpolatorController;

/**
 * The controller class for view mode of the HTMLViewer portlet. HTMLViewer is a
 * JSR-168 portlet which displays the proper localized version of a configured
 * HTML file, as interpolated by the portlet FileInterpolator.
 * 
 * @author <link href="xiao-bing.zuo@hp.com">Zuo Xiaobing</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see com.hp.it.spf.xa.interpolate.portlet.FileInterpolatorController
 *      com.hp.it.spf.xa.interpolate.portlet.FileInterpolator
 */
public class ViewController extends FileInterpolatorController {

	/**
	 * Constructor which sets the token-substitutions filename to
	 * html_viewer_tokens.properties.
	 */
	public ViewController() {
		// Override the default token-substitutions filename.
		this.subsFileName = "html_viewer_tokens";
	}

	/** The view name. */
	private String viewName;

	/**
	 * Sets the view name.
	 * 
	 * @param viewName
	 *            The view name.
	 */
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/**
	 * Gets the base filename of the file resource bundle to interpolate and
	 * display, from the portlet preferences where the portlet config mode
	 * stored it.
	 * 
	 * @param request
	 *            The render request.
	 * @return The base filename.
	 * @throws Exception
	 *             Any exception.
	 */
	public String getFilename(RenderRequest request) throws Exception {

		PortletPreferences pp = request.getPreferences();
		String viewFileName = pp.getValue(Consts.VIEW_FILENAME, null);
		return Consts.HTML_FILE_FOLD + viewFileName;
	}

	/**
	 * Parse the given file content (already interpolated by the portlet
	 * FileInterpolator) to markup hyperlinks according to the launch-buttonless
	 * config option, then set the content into the model for display by the
	 * JSP.
	 * 
	 * @param request
	 *            The render request.
	 * @param fileContent
	 *            The interpolated file content.
	 * @param response
	 *            The render response.
	 * @return The model containing the file content to display.
	 * @throws Exception
	 *             Some exception.
	 */
	protected ModelAndView execute(RenderRequest request,
			RenderResponse response, String fileContent) throws Exception {
		if (fileContent != null) {
			fileContent = fileContent.trim();
		}
		if (fileContent == null || fileContent.length() == 0) {
			throw new InternalErrorException(Consts.ERROR_CODE_FILE_NULL);
		}

		ModelAndView modelView = new ModelAndView(viewName);
		modelView.addObject(Consts.VIEW_CONTENT, fileContent);

		PortletPreferences pp = request.getPreferences();
		String buttonLess = pp.getValue(Consts.LAUNCH_BUTTONLESS, null);
		if (buttonLess != null && buttonLess.equals("true")) {
			modelView.addObject(Consts.LAUNCH_BUTTONLESS, "true");
		}

		String linkClass = pp.getValue(Consts.LINK_CLASS, null);
		if (linkClass != null && linkClass.trim().length() > 0) {
			modelView.addObject(Consts.LINK_CLASS, linkClass);
		}

		return modelView;
	}

}
