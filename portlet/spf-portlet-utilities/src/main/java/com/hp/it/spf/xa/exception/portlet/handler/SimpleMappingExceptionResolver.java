/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 **/
package com.hp.it.spf.xa.exception.portlet.handler;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;

import com.hp.it.spf.xa.exception.portlet.ExceptionUtil;

/**
 * <p>
 * This class is a subclass of the Spring framework's
 * {@link org.springframework.web.portlet.handler.SimpleMappingExceptionResolver},
 * and includes logic to store the exception object in a request attribute for
 * JSP's to access, before calling the Spring class to resolve the view for the
 * exception. This uses the portlet {@link ExceptionUtil} class to store the
 * exception object.
 * 
 * @author sunnyee, Scott Jorgenson
 * @version TBD
 */
public class SimpleMappingExceptionResolver extends
		org.springframework.web.portlet.handler.SimpleMappingExceptionResolver {

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
