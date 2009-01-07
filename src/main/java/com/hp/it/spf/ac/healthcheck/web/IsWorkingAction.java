/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.ac.healthcheck.web;

// Import J2EE packages.
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Import struts packages.
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;

// Import WPA packages.
import com.hp.bco.pl.wpa.action.WPAAction;
import com.hp.bco.pl.wpa.action.WPAActionForward;
import com.hp.bco.pl.wpa.util.Environment;
import com.hp.bco.pl.wpa.util.WPAGlobal;
import com.hp.bco.pl.transaction.TransactionProxy;
import com.hp.it.spf.ac.healthcheck.web.References;
import com.hp.it.spf.ac.status.ClosedStatus;
import com.hp.it.spf.ac.status.HealthcheckStatus;
import com.hp.it.spf.ac.status.OpenStatus;

// Import other service packages.

/**
 * This is a Struts WPAAction class for the healthcheck Web application,
 * responsible for rendering the current "is working" status for the monitored
 * portal to the client. This status indicates the actual status of the
 * monitored portal at this site - ie, whether it is operational or not. It does
 * not take into account whether the open-sign is running and, hence, whether
 * the site is open or not.
 * <p>
 * 
 * <code>IsWorkingAction</code> is mapped from an action mapping in
 * <code>struts-config.xml</code>. When a client (eg GSLB) requests the URL
 * in that mapping, <code>IsWorkingAction.perform</code> is invoked by the WPA
 * Struts controller (the default mapped servlet for the health- check Web
 * application). <code>perform</code> then determines the last healthcheck
 * status (stored in application scope by the <code>HealthcheckDriver</code> -
 * see), and returns a forward to the appropriate view (a static HTML file). It
 * also sets a custom HTTP response extension header,
 * <code>X-Site-Available</code>, with a flag value - this is for the client
 * (eg GSLB) to easily scan, should the user-visible view file eventually change
 * in future releases.
 * <p>
 * 
 * GSLB or SLB typically perform layer-7 healthchecks against
 * <code>IsWorkingAction</code> for monitoring site hostname status. For
 * monitoring pool hostname status, GSLB and SLB typically perform layer-7
 * healthchecks against <code>IsWorkingAndOpenAction</code> - see.
 * <p>
 * 
 * @author <link href="scott.jorgenson@hp.com">Scott Jorgenson</link>
 * @version TBD
 */
public class IsWorkingAction extends WPAAction {

    final static private String IS_WORKING_FORWARD = "isWorking";

    final static private String NOT_WORKING_FORWARD = "notWorking";

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {

        TransactionProxy transProxy = (TransactionProxy) request
                .getAttribute(WPAGlobal.TRANSACTION_KEY);
        HealthcheckStatus hcs = HealthcheckStatus.retrieve(Environment
                .getInstance().getContext());
        String pathToForward = "";
        String nameToLog = "";

        // Set the Cache-control: no-cache HTTP response header, in case
        // there are any caching proxies between us and the client, or in
        // case the client is inclined to cache responses itself (basically,
        // the response from this action should never be cached).

        response.setHeader(References.HTTP_PRAGMA_HEADER,
                References.HTTP_NO_CACHE);
        response.setHeader(References.HTTP_CACHE_CONTROL_HEADER,
                References.HTTP_NO_CACHE);
        
        // Set the custom X-Site-Available header to the proper value.
        
        if ((hcs instanceof OpenStatus) || (hcs instanceof ClosedStatus)) {
            response.setHeader(References.HTTP_X_SITE_AVAILABLE_HEADER,
                    References.HTTP_X_SITE_AVAILABLE);
            pathToForward = mapping.findForward(IS_WORKING_FORWARD).getPath();
            nameToLog = hcs.getSimpleName();
            transProxy.addBusinessInfo(References.HTTP_X_SITE_AVAILABLE_HEADER,
                    References.HTTP_X_SITE_AVAILABLE + "(" + nameToLog + ")");
        } else {
            response.setHeader(References.HTTP_X_SITE_AVAILABLE_HEADER,
                    References.HTTP_X_SITE_UNAVAILABLE);
            pathToForward = mapping.findForward(NOT_WORKING_FORWARD).getPath();
            nameToLog = (hcs == null) ? "null" : hcs.getSimpleName();
            transProxy.addBusinessInfo(References.HTTP_X_SITE_AVAILABLE_HEADER,
                    References.HTTP_X_SITE_UNAVAILABLE + "(" + nameToLog + ")");
        }

        return new WPAActionForward(pathToForward);

    } // end perform method

} // end class
