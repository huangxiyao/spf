/*
 * Project: Shared Portal Framework Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.exception.portlet.handler;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.springframework.web.portlet.ModelAndView;

import com.hp.it.spf.xa.exception.portlet.ExceptionUtil;

/**
 * <p>
 * This class is a subclass of the Spring framework's
 * {@link org.springframework.web.portlet.handler.SimpleMappingExceptionResolver}
 * , and includes logic to store the exception object in a request attribute for
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
     * Store the exception object to the render request using
     * {@link ExceptionUtil#setException(javax.portlet.PortletRequest, Exception)}
     * , then call the superclass method to resolve the exception to a view in
     * the normal manner. The exception is stored in such a way that the various
     * methods to inspect and/or get information about it, like
     * {@link ExceptionUtil#getException(javax.portlet.PortletRequest)}, will
     * find it.
     */
    public ModelAndView resolveException(RenderRequest request,
	    RenderResponse response, Object handler, Exception ex) {

	ExceptionUtil.setException(request, ex);
	return super.resolveException(request, response, handler, ex);
    }
    
    /**
     * Store the exception object to the render request using
     * {@link ExceptionUtil#setException(javax.portlet.PortletRequest, Exception)}
     * , then call the superclass method to resolve the exception to a view in
     * the normal manner. The exception is stored in such a way that the various
     * methods to inspect and/or get information about it, like
     * {@link ExceptionUtil#getException(javax.portlet.PortletRequest)}, will
     * find it.
     */
    /**
     * TODO uncomment this when Spring is upgrade to 3.x
    public ModelAndView resolveException(ResourceRequest request,
	    ResourceResponse response, Object handler, Exception ex) {

	ExceptionUtil.setException(request, ex);
	return super.resolveException(request, response, handler, ex);
    }
     */

}
