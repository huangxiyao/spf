package com.hp.it.spf.htmlviewer.portlet.exception;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.ModelAndView;

import com.hp.it.spf.xa.exception.portlet.BusinessException;

import com.hp.it.spf.htmlviewer.portlet.exception.InternalErrorException;
import com.hp.it.spf.htmlviewer.portlet.util.Utils;

/**
 * <p>
 * This class is a subclass of the SPF
 * {@link com.hp.it.spf.xa.exception.portlet.handler.SimpleMappingExceptionResolver}
 * and includes logic to wrap any exception indicating a system error inside
 * {@link InternalErrorException} (except for
 * <code>InternalErrorExceptions</code> themselves, of course). Exceptions
 * indicating system errors are considered to be:
 * </p>
 * <ul>
 * <li>any non-SPF exception such as any of the J2EE, Java Portlet, or J2SE
 * standard exceptions;</li>
 * <li>any SPF <code>SystemException</code></li>
 * </ul>
 * <p>
 * Since the <code>InternalErrorException</code> constructor includes WPAP
 * Timber logging for the exception data, using this resolver will ensure that
 * every system-error type of exception thrown from a handler gets logged before
 * being forwarded to a view.
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

	// First we wrap any default exception inside InternalErrorException, so
	// now all are guaranteed to be InternalErrorExceptions and be logged.
	// This is because we consider all default exceptions to be system
	// errors by definition. Note: business exceptions are not system errors
	// so should not be treated as such.
	if (!(ex instanceof InternalErrorException)
		&& !(ex instanceof BusinessException)) {
	    ex = new InternalErrorException(request, ex);
	}

	// Next we setup Timber log data for this exception, in case this has
	// not already happened.
	Utils.setupLogData(request, ex);

	// Last we call super to resolve the exception (basic Spring
	// SimpleMappingExceptionResolver functionality) and store it into the
	// request as a side-effect (SPF value-add functionality, which allows
	// ExceptionUtil to then be used in the error-view JSP).
	return super.resolveException(request, response, handler, ex);
    }

}
