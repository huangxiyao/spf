/*
 * Project: Shared Portal Framework.
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
package com.hp.it.spf.htmlviewer.portlet.web;

import java.util.ResourceBundle;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import com.hp.frameworks.wpa.portlet.transaction.Transaction;
import com.hp.frameworks.wpa.portlet.transaction.TransactionImpl;
import com.hp.it.spf.htmlviewer.portlet.exception.InternalErrorException;
import com.hp.it.spf.htmlviewer.portlet.util.Consts;
import com.hp.it.spf.htmlviewer.portlet.util.Utils;
import com.hp.it.spf.htmlviewer.portlet.util.ViewData;
import com.hp.it.spf.htmlviewer.portlet.util.ViewDataCache;
import com.hp.it.spf.xa.interpolate.portlet.TokenParser;
import com.hp.websat.timber.model.StatusIndicator;

/**
 * <p>
 * The controller class for <code>view</code> mode of the HTML viewer portlet.
 * The HTML viewer is a JSR-286 portlet. Its <code>view</code> mode displays
 * the proper localized version of a configured HTML file (called a <i>view file</i>),
 * interpolated according to the SPF token language supported by
 * {@link com.hp.it.spf.xa.interpolate.portlet.TokenParser}.
 * </p>
 * <p>
 * In <code>config</code> mode, the portlet administrator is able to specify
 * the HTML file to display, as well as other criteria such as a
 * token-substitutions file for the <code>{INCLUDE:<i>key</i>}</code> token
 * (called an <i>includes file</i>). These are stored as JSR 286 portlet
 * configuration preferences, which this <code>view</code> mode controller
 * accesses in order to identify the content to render.
 * </p>
 * <p>
 * As of SPF 1.2, caching is supported for all of the external resources used by
 * this <code>view</code> mode controller: the portlet preferences, the view
 * file content, and the includes file content. A new portlet preference
 * controls the retention period for the cache.
 * </p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version SPF 1.2
 * @see <code>com.hp.it.spf.htmlviewer.portlet.util.ViewData</code>
 * <code>com.hp.it.spf.htmlviewer.portlet.util.ViewDataCache</code>
 */
public class ViewController extends AbstractController {

	/** The view name. */
	private String viewName = "view";
	
	/**
	 * Empty constructor.
	 */
	public ViewController() {
	}

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
	 * Invoked during the render phase, this method gets the view data for the
	 * request, uses the portlet
	 * {@link com.hp.it.spf.xa.interpolate.portlet.TokenParser} to interpolate
	 * it, and finishes by returning the interpolated content inside the model.
	 * The interpolated content is returned as the
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts.VIEW_CONTENT} model
	 * attribute.
	 * </p>
	 * <p>
	 * The view and includes filenames are defined in the portlet configuration
	 * preferences. The default includes filename is
	 * {@link com.hp.it.spf.htmlviewer.portlet.util.Consts#DEFAULT_INCLUDES_FILENAME} (<code>html_viewer_includes.properties</code>).
	 * Interpolation primarily involves applying the SPF token language defined
	 * by <code>TokenParser</code>, but also involves rewriting anchor tags
	 * according to the launch-buttonless configuration preference, if set. See
	 * the documentation for {@link ConfigController} for more information about
	 * the configuration preferences which control how this <code>view</code>
	 * controller operates.
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
	 * If an exception occurs during this method, it will be caught and handled
	 * locally if it is recoverable. If it is not recoverable - for example,
	 * when the view filename is undefined or the view file cannot be found - an
	 * {@link com.hp.it.spf.htmlviewer.portlet.exception.InternalErrorException}
	 * will be generated; this method will allow that to propagate back to
	 * Spring. The HTML viewer portlet's Spring configuration is setup to
	 * forward such <code>InternalErrorExceptions</code> into the proper JSP
	 * automatically, using
	 * {@link com.hp.it.spf.xa.exception.portlet.handler.SimpleMappingExceptionResolver}.
	 * </p>
	 * <p>
	 * This method uses WPAP Timber logging to record the outcome. In the case
	 * of an business-logic error during this controller, the WPAP Timber error
	 * logs will include diagnostics about the error.
	 * </p>
	 * 
	 * @param request
	 *            The render request.
	 * @param response
	 *            The render response.
	 * @return ModelAndView The Spring model and view, containing the
	 *         interpolated content.
	 * @throws Exception
	 *             Some exception.
	 */
	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {

		// Setup for WPAP logging; assume OK for now. Put portlet friendly ID
		// (if any) into the log context data.
		Transaction trans = TransactionImpl.getTransaction(request);
		if (trans != null) {
			String portletID = Utils.getPortalPortletID(request);
			trans.addContextInfo("portletID", portletID);
			trans.setStatusIndicator(StatusIndicator.OK);
		}

		// Get the current view data, loading and backfilling into cache as
		// needed.
		ViewData data = ViewDataCache.getViewData(request);

		// Check for errors and warnings; branch to error view. No need
		// for explicit logging here; the WPAP logging interceptor will handle
		// it from the thrown exception.
		if ((data == null) || (!data.ok())) {
			throw new InternalErrorException(request, data.getErrors());
		}
		
		// Interpolate the view content.
		ResourceBundle includesContent = data.getIncludesContent();
		TokenParser t = new TokenParser(request, response, includesContent);
		String viewContent = t.parse(data.getViewContent());

		// Warning if the view content is blank/empty before or after
		// interpolation. Record this in the WPAP transaction object for
		// logging purposes, but proceed.
		if (viewContent == null)
			viewContent = "";
		viewContent = viewContent.trim();
		if (viewContent.length() == 0) {
			if (trans != null) {
				trans.addContextInfo("viewContent", "(empty)");
				trans.setStatusIndicator(StatusIndicator.FYI);
			}
		}
		ModelAndView modelView = new ModelAndView(viewName);

		// If launch-buttonless, update the view content accordingly.
		if (data.getLaunchButtonless() && viewContent.length() > 0) {
			modelView.addObject(Consts.LAUNCH_BUTTONLESS, "true");
			viewContent = Utils.addButtonlessChildLauncher(response,
					viewContent);
			if (trans != null) {
				trans.addContextInfo("launchButtonless", "true");
				trans.setStatusIndicator(StatusIndicator.FYI);
			}
		}

		// Set the file content to display into the model.
		modelView.addObject(Consts.VIEW_CONTENT, viewContent);
		return modelView;
	}
}
