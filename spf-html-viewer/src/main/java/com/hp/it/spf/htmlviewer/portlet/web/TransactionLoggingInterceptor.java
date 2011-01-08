package com.hp.it.spf.htmlviewer.portlet.web;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import com.hp.it.spf.htmlviewer.portlet.util.Utils;

/**
 * Extends the WPA Timber <code>TransactionLoggingInterceptor</code> to collect
 * log data common to all portlet phases and modes. This delegates to
 * {@link Utils#setupLogData(PortletRequest)} to collect the log data and set it
 * into the WPA Timber <code>Transaction</code> object, after invoking the WPA
 * Timber superclass to perform its standard processing. WPA Timber will then
 * output that data into the corresponding fields in the various logs, as
 * configured in the portlet application's logger.xml.
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
