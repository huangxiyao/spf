/*
 * Project: Shared Portal Framework.
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
package com.hp.it.spf.xa.htmlviewer.portlet.web;

import javax.portlet.PortletPreferences;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;

import com.hp.frameworks.wpa.portlet.transaction.Transaction;
import com.hp.frameworks.wpa.portlet.transaction.TransactionImpl;
import com.hp.it.spf.xa.htmlviewer.portlet.exception.InternalErrorException;
import com.hp.it.spf.xa.i18n.portlet.I18nUtility;
import com.hp.it.spf.xa.htmlviewer.portlet.util.Consts;
import com.hp.it.spf.xa.htmlviewer.portlet.util.Utils;
import com.hp.it.spf.xa.interpolate.portlet.web.FileInterpolatorController;
import com.hp.websat.timber.model.StatusIndicator;

/**
 * The controller class for <code>view</code> mode of the
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
 * @see <code>com.hp.it.spf.xa.interpolate.portlet.web.FileInterpolatorController</code>
 * <code>com.hp.it.spf.xa.interpolate.portlet.FileInterpolator</code>
 */
public class ViewController extends FileInterpolatorController {

	/**
	 * Constructor which sets the token-substitutions filename to
	 * <code>html_viewer_includes.properties</code>.
	 */
	public ViewController() {
		// Override the default token-substitutions filename with a default for
		// this portlet.
		this.includeFileName = "html_viewer_includes";
	}

	/** The view name. */
	private String viewName = "view";

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
	 * <p>
	 * Gets the base filename of the view file resource bundle to interpolate
	 * and display, from the portlet preferences where the portlet config mode
	 * stored it (in the preference element named
	 * {@link com.hp.it.spf.xa.htmlviewer.portlet.util.Consts#VIEW_FILENAME}).
	 * Throws an
	 * {@link com.hp.it.spf.xa.htmlviewer.portlet.exception.InternalErrorException}
	 * if the filename cannot be found.
	 * </p>
	 * <p>
	 * This method, as a side-effect, also gets the name of the token
	 * substitutions file to use, also from the portlet preferences where the
	 * config mode stored it (in the element named
	 * {@link com.hp.it.spf.xa.htmlviewer.portlet.util.Consts#INCLUDES_FILENAME}).
	 * This is an optional element; if not found, then the default
	 * <code>html_viewer_includes.properties</code> set by the constructor is
	 * retained.
	 * </p>
	 * <p>
	 * The render-phase controller in the abstract superclass calls this method
	 * at the beginning of its execution, before interpolating the content.
	 * </p>
	 * 
	 * @param request
	 *            The render request.
	 * @return The base filename.
	 * @throws Exception
	 *             Any exception.
	 */
	public String getFilename(RenderRequest request) throws Exception {

		// Get the portlet preferences.
		PortletPreferences pp = request.getPreferences();
		String viewFilename = pp.getValue(Consts.VIEW_FILENAME, null);
		String includesFilename = pp.getValue(Consts.INCLUDES_FILENAME, null);

		// Check the view filename for possible errors. Otherwise finalize it by
		// making a valid relative path out of it.
		String errorCode = Utils.checkViewFilenameForErrors(request,
				viewFilename);
		if (errorCode != null) {
			throw new InternalErrorException(request, errorCode);
		}
		viewFilename = Utils.slashify(Consts.HTML_FILE_FOLD + viewFilename);

		// Finalize the includes filename by setting it into the class attribute
		// if defined.
		includesFilename = Utils.slashify(includesFilename);
		if (includesFilename != null) {
			this.includeFileName = includesFilename;
		}

		return (viewFilename);
	}

	/**
	 * <p>
	 * Parses the given file content (already interpolated by the portlet
	 * FileInterpolator) to rewrite hyperlinks according to the
	 * launch-buttonless config option, then sets the content into the model for
	 * display by the JSP. The model attribute name used for it is
	 * <code>viewContent</code>, so that is the request attribute which the
	 * JSP should display. However, an <code>InternalErrorException</code> is
	 * thrown if the given file content is null or empty.
	 * </p>
	 * <p>
	 * The render-phase controller in the abstract superclass calls this method
	 * at the end of its execution, after interpolating the content.
	 * </p>
	 * 
	 * @param request
	 *            The render request.
	 * @param fileContent
	 *            The interpolated file content.
	 * @param response
	 *            The render response.
	 * @return The model containing the file content to display, inside the
	 *         <code>viewContent</code> attribute.
	 * @throws Exception
	 *             Some exception.
	 */
	protected ModelAndView execute(RenderRequest request,
			RenderResponse response, String fileContent) throws Exception {

		Transaction trans = TransactionImpl.getTransaction(request);
		if (trans != null)
			trans.setStatusIndicator(StatusIndicator.OK); // Assume OK for
		// now.

		if (fileContent != null) {
			fileContent = fileContent.trim();
			if (fileContent.length() == 0) {
				// Warning if the file is blank/empty before or after
				// interpolation. Record this in the WPAP transaction object for
				// logging purposes.
				if (trans != null) {
					trans.addContextInfo("fileContent", "empty");
					trans.setStatusIndicator(StatusIndicator.WARNING);
				}
			}
		} else {
			// Error if file content is null (this means the file was not found
			// or could not be read/parsed). No need for explicit logging here;
			// the WPAP logging interceptor will handle it from the thrown
			// exception.
			throw new InternalErrorException(request,
					Consts.ERROR_CODE_VIEW_FILE_NULL);
		}

		// If launch-buttonless, update the file content accordingly.
		ModelAndView modelView = new ModelAndView(viewName);
		PortletPreferences pp = request.getPreferences();
		String buttonLess = pp.getValue(Consts.LAUNCH_BUTTONLESS, null);
		if (buttonLess != null && buttonLess.equals("true")) {
			modelView.addObject(Consts.LAUNCH_BUTTONLESS, "true");
			fileContent = Utils.addButtonlessChildLauncher(response,
					fileContent);
			if (trans != null) {
				trans.addContextInfo("launchButtonless", "true");
				trans.setStatusIndicator(StatusIndicator.FYI);
			}
		}

		// Set the file content to display into the model.
		modelView.addObject(Consts.VIEW_CONTENT, fileContent);
		return modelView;
	}
}
