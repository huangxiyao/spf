/*
 * Project: Shared Portal Framework.
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.interpolate.portlet.web;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.mvc.AbstractController;

import com.hp.it.spf.xa.interpolate.portlet.FileInterpolator;


/**
 * <p>
 * An abstract base class for a Spring portlet controller which performs file
 * display with interpolation, using the portlet FileInterpolator. The file you
 * display must be loadable by the class loader from anywhere in the class path.
 * It may contain any of the special markup tokens supported by the portlet
 * FileInterpolator (see).
 * </p>
 * 
 * <p>
 * You can use this as the base class for a help-mode controller for your
 * portlet. You just need to implement:
 * 
 * <ul>
 * <li>The getFilename method, to return the filename of the text (eg HTML)
 * file you want to interpolate and display.</li>
 * <li>The execute method, to take the interpolated content of your file (input
 * to the method as a string automatically by this class) and render it.</li>
 * <li>If you want to specify a custom token-substitutions file (ie for the
 * <code>&lt;TOKEN:key&gt;</code> token), set the subsFileBase attribute
 * before calling the execute method.</li>
 * </ul>
 * </p>
 * 
 * @author <link href="jian-fa.dong@hp.com">dongji</link>
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 * @see com.hp.it.spf.xa.interpolate.portlet.FileInterpolator
 */
public abstract class FileInterpolatorController extends AbstractController {

	// added by ck for cr 1000790073
	/**
	 * Stores the name of the token-substitutions file to use for any file-based
	 * token substitutions. The default is null, which causes the
	 * default_token_subs.properties file to be assumed. You can override this
	 * in your constructor.
	 */
	protected String subsFileName = null;

	/**
	 * Implement this method to return the base filename of the text (eg HTML)
	 * file you want to interpolate for display. The returned filename should
	 * include sufficient path information that the file can be found using the
	 * standard system class loader.
	 * 
	 * @param request
	 *            The request
	 * @return The filename (including any needed path for loading by the class
	 *         loader)
	 * @throws Exception
	 *             An exception
	 */
	protected abstract String getFilename(RenderRequest request)
			throws Exception;

	/**
	 * Implement this method to render the file content string provided. That
	 * string contains the interpolated file text to display.
	 * 
	 * @param request
	 *            The request
	 * @param response
	 *            The response
	 * @param fileContent
	 *            The file content (already interpolated)
	 * @return ModelAndView ModelAndView
	 * @throws Exception
	 *             An exception
	 */
	protected abstract ModelAndView execute(RenderRequest request,
			RenderResponse response, String fileContent) throws Exception;

	/**
	 * Returns the user groups from the portlet request. It is assumed they were
	 * set into the proper namespaced attribute by the portal container (the SPF
	 * Vignette PreDisplayAction for the PageDisplay secondary page currently
	 * does this automatically); otherwise null is returned.
	 * 
	 * @param request
	 * @return String[] group names
	 */
	protected String[] getUserGroups(RenderRequest request) {
		
		/* TODO - replace with code to get groups from PortletRequest.USER_INFO map.
		 * 
		if (request
				.getAttribute(com.hp.it.cas.spf.common.utils.Consts.REQUEST_ATTR_GROUPS) != null) {
			return (String[]) request
					.getAttribute(com.hp.it.cas.spf.common.utils.Consts.REQUEST_ATTR_GROUPS);
		} else
			return null;
		*/
		
		return new String[] {};
	}

	/**
	 * Gets the filename from the request (using the concrete getFilename method
	 * you implement), uses the portlet FileInterpolator to get a string of text
	 * content from the file (interpolated with all of the proper dynamic
	 * values), and renders it using the concrete execute method you implement.
	 * 
	 * @param request
	 *            RenderRequest
	 * @param response
	 *            RenderResponse
	 * @return ModelAndView ModelAndView
	 * @throws Exception
	 *             Exception
	 */
	protected ModelAndView handleRenderRequestInternal(RenderRequest request,
			RenderResponse response) throws Exception {
		String relativeName = getFilename(request);
		String[] userGroups = getUserGroups(request);
		/* commented by ck for cr 1000790073 */
		// FileInterpolator f = new FileInterpolator(request, relativeName,
		// userGroups);
		// added by ck for cr 1000790073
		FileInterpolator f = new FileInterpolator(request, relativeName,
				userGroups, this.subsFileName);
		String fileContent = f.interpolate();
		return this.execute(request, response, fileContent);
	}
}
