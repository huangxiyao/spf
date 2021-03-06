package com.hp.it.spf.htmlviewer.portlet.exception;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;
import com.hp.websat.timber.model.StatusIndicator;

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
	// themselves. For those that have already logged themselves, make sure
	// the status indicator is still set (this handles the corner-case of a
	// system error instantiated in action phase and now being resolved in
	// render phase - this makes sure the log data is still set, since it
	// gets reset between phases).
	if (!(ex instanceof InternalErrorException)
		&& !(ex instanceof BusinessException)) {
	    Utils.setupLogData(request, ex);
	} else if (ex instanceof InternalErrorException) {
	    Utils.setupLogData(request, StatusIndicator.FATAL);
	}

	// Next detect and log business errors that haven't already logged
	// themselves. For those that have already logged themselves, make sure
	// the status indicator is still set.
	if (!(ex instanceof InputErrorException)
		&& (ex instanceof BusinessException)) {
	    Utils.setupLogData(request, ex);
	} else if (ex instanceof InputErrorException) {
	    Utils.setupLogData(request, StatusIndicator.ERROR);
	}

	// Last we call super to resolve the exception (basic Spring
	// SimpleMappingExceptionResolver functionality) and store it into the
	// request as a side-effect (SPF value-add functionality, which allows
	// ExceptionUtil to then be used in the error-view JSP).
	return super.resolveException(request, response, handler, ex);
    }

}
