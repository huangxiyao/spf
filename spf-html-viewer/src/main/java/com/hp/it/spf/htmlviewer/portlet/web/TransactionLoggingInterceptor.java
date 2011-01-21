package com.hp.it.spf.htmlviewer.portlet.web;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import com.hp.it.spf.htmlviewer.portlet.util.Utils;

/**
 * Extends the WPA Timber <code>TransactionLoggingInterceptor</code> to collect
 * log data common to all portlet phases and modes.
 * 
 * This delegates to {@link LogHelper#setupLogData(PortletRequest)} (see) after
 * invoking the WPAP Timber superclass to perform its usual
 * <code>preHandle</code> processing. It also disables any exception from being
 * logged to the Timber error logs by overriding the
 * <code>afterCompletion</code> method to suppress passing the exception into
 * the superclass. (We do this because all exceptions considered system errors
 * will already have been logged to the Timber error logs, either from the
 * {@link com.hp.it.spf.htmlviewer.portlet.exception.InternalErrorException}
 * constructor, or from the
 * {@link com.hp.it.spf.htmlviewer.portlet.exception.SimpleMappingExceptionResolver}
 * , so any additional logging to the error logs by the superclass would be
 * redundant. As for other kinds of exception, since they represent non-system
 * errors, we would not want them logged anyway; the Timber error logs are
 * reserved for system errors.)
 * 
 * @author djorgen
 */
public class TransactionLoggingInterceptor extends
	com.hp.frameworks.wpa.portlet.handler.TransactionLoggingInterceptor {

    /**
     * Delegates to the superclass for standard WPA Timber pre-handle
     * processing, then delegates to {@link Utils#setupLogData(PortletRequest)}
     * to collect the common log data and store it into the
     * <code>Transaction</code> for subsequent logging per logger.xml.
     */
    public boolean preHandle(PortletRequest request, PortletResponse response,
	    Object handler) throws Exception {

	boolean result = super.preHandle(request, response, handler);
	Utils.setupLogData(request);
	return result;
    }

    /**
     * Delegates to the superclass for standard WPA Timber post-completion
     * processing, except the exception is suppressed so that duplicate logging
     * does not take place (the exception either already will have logged itself
     * by this point in the workflow, or will log itself later downstream in the
     * workflow).
     */
    public void afterCompletion(PortletRequest request,
	    PortletResponse response, Object handler, Exception ex)
	    throws Exception {

	super.afterCompletion(request, response, handler, null);
    }

}
