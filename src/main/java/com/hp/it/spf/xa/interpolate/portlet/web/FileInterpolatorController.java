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
import com.hp.it.spf.xa.exception.portlet.SPFException;
import com.hp.it.spf.xa.misc.portlet.Utils;
import com.hp.websat.timber.logging.Log;

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

	/**
	 * Stores the name of the token-substitutions file to use for any file-based
	 * token substitutions. The default is null, which causes the
	 * default_token_subs.properties file to be assumed. You can override this
	 * in your constructor.
	 */
	protected String subsFileName = null;

	/**
	 * <p>
	 * Implement this method to return the base filename of the text (eg HTML)
	 * file you want to interpolate for display. The returned filename should
	 * include sufficient path information that the file can be found using the
	 * standard system class loader.
	 * </p>
	 * <p>
	 * If the concrete implementation of this method encounters an error, it
	 * should throw an exception. Any exception you throw from this message will
	 * be propagated to Spring (which will forward to your proper error-handling
	 * JSP, or otherwise a default error-handling JSP, if you have configured
	 * Spring correctly - eg using the SPF
	 * <code>SimpleMappingExceptionResolver</code>).
	 * </p>
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
	 * <p>
	 * Implement this method to render the file content string provided. That
	 * string contains the interpolated file text to display.
	 * </p>
	 * <p>
	 * If the concrete implementation of this method encounters an error, it
	 * should throw an exception. Any exception you throw from this message will
	 * be propagated to Spring (which will forward to your proper error-handling
	 * JSP, or otherwise a default error-handling JSP, if you have configured
	 * Spring correctly - eg using the SPF
	 * <code>SimpleMappingExceptionResolver</code>).
	 * </p>
	 * <p>
	 * <b>Note:</b> The concrete implementation of this method should expect
	 * and handle the case the the provided file content is null or empty. Some
	 * implementations may consider that an error condition and throw an
	 * exception accordingly. Others might not (for example, they might render
	 * some default content).
	 * </p>
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
	 * <p>
	 * Invoked during the render phase, this method gets the filename from the
	 * request (using the concrete getFilename method you implement), uses the
	 * portlet FileInterpolator to get a string of text content from the file
	 * (interpolated with all of the proper dynamic values), and finishes by
	 * calling the concrete execute method you implement.
	 * </p>
	 * <p>
	 * If your concrete getFilename or execute methods throw an exception, this
	 * method allows that to propagate back to Spring. If you have configured
	 * Spring correctly - eg using the SPF
	 * <code>SimpleMappingExceptionResolver</code> - then Spring will forward
	 * to the proper JSP for that exception automatically.
	 * </p>
	 * <p>
	 * <b>Note:</b> This method does not consider it an error if the
	 * interpolated file content is blank (or not found). It will just pass it
	 * to the concrete execute method anyway. It is the execute method's
	 * responsibility to check for that condition if desired (since not all
	 * implementations may consider empty or missing content to be an error).
	 * </p>
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

		Log.logInfo(this, "FileInterpolatorController: render phase invoked.");
		String relativeName = getFilename(request);
		Log
				.logInfo(
						this,
						"FileInterpolatorController: rendering file content from proper localized version of base file: "
								+ relativeName);
		FileInterpolator f = new FileInterpolator(request, response,
				relativeName, this.subsFileName);
		String fileContent = f.interpolate();
		return this.execute(request, response, fileContent);
	}
}
