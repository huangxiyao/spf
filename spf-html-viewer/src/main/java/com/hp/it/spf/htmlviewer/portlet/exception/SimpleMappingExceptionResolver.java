package com.hp.it.spf.htmlviewer.portlet.exception;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;

import com.hp.it.spf.xa.exception.portlet.BusinessException;

import com.hp.it.spf.htmlviewer.portlet.exception.InternalErrorException;
import com.hp.it.spf.htmlviewer.portlet.exception.InputErrorException;
import com.hp.it.spf.htmlviewer.portlet.util.Utils;

/**
 * <p>
 * This class is a subclass of the SPF
 * {@link com.hp.it.spf.xa.exception.portlet.handler.SimpleMappingExceptionResolver}
 * and includes logic to log to Timber any exception which hasn't already logged
 * itself. {@link InputErrorException} and {@link InternalErrorException} will
 * already have logged themselves, so this subclass just logs any others. Then
 * in all cases it delegates to the SPF superclass.
 * </p>
 * 
 * @author djorgen
 * 
 */
public class SimpleMappingExceptionResolver
	extends
	com.hp.it.spf.xa.exception.portlet.handler.SimpleMappingExceptionResolver {

    public ModelAndView resolveException(RenderRequest request,
	    RenderResponse response, Object handler, Exception ex) {

	// First detect and log system errors that haven't already logged
	// themselves.
	if (!(ex instanceof InternalErrorException)
		&& !(ex instanceof BusinessException)) {
	    Utils.setupLogData(request, ex);
	}

	// Next detect and log business errors that haven't already logged
	// themselves.
	if (!(ex instanceof InputErrorException)
		&& (ex instanceof BusinessException)) {
	    Utils.setupLogData(request, ex);
	}

	// Last we call super to resolve the exception (basic Spring
	// SimpleMappingExceptionResolver functionality) and store it into the
	// request as a side-effect (SPF value-add functionality, which allows
	// ExceptionUtil to then be used in the error-view JSP).
	return super.resolveException(request, response, handler, ex);
    }

}
