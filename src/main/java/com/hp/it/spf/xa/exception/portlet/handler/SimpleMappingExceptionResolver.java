/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
package com.hp.it.spf.xa.exception.portlet.handler;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;

import com.hp.frameworks.wpa.portlet.handler.WPASimpleMappingExceptionResolver;
import com.hp.it.spf.xa.exception.portlet.ExceptionUtil;

/**
 * <p>
 * This class is a subclass of the WPAP framework's
 * <code>WPASimpleMappingExceptionResolver</code>, and includes logic to
 * store the exception object in a request attribute for JSP's to access. This
 * uses the portlet <code>ExceptionUtil</code> class to do so.
 * 
 * @author sunnyee, Scott Jorgenson
 * @version TBD
 */
public class SimpleMappingExceptionResolver extends
		WPASimpleMappingExceptionResolver {

	// ------------------------------------------------------------- Constants

	// -------------------------------------------------------- Public methods

	/**
	 * Call the super method to resolve the exception to a view, and store the
	 * exception object to request.
	 */
	public ModelAndView resolveException(RenderRequest request,
			RenderResponse response, Object handler, Exception ex) {

		ExceptionUtil.setException(request, ex);
		return super.resolveException(request, response, handler, ex);
	}
}
