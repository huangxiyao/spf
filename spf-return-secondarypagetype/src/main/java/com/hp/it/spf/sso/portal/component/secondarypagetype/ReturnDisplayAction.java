/*
 * Project: Service Portal
 * Copyright (c) 2008 HP. All Rights Reserved
 * 
 * The comments
 *
 * FILENAME: ReturnDisplayAction.java
 * PACKAGE : com.hp.it.spf.sso.portal.component.secondarypagetype
 * $Id: $
 * $Log: $
 */
package com.hp.it.spf.sso.portal.component.secondarypagetype;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hp.it.spf.xa.exception.portal.ExceptionUtil;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.vignette.portal.log.LogWrapper;
import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalURI;
import com.vignette.portal.website.enduser.components.ActionException;
import com.vignette.portal.website.enduser.components.BaseAction;

public class ReturnDisplayAction extends BaseAction {
    /**
     * Log
     */
    private static final LogWrapper LOG = new LogWrapper(
            ReturnDisplayAction.class);

    /**
     * Get the return URL from session and redirect the user to it. If it is not
     * found in session, then redirect the user to the site home page URL.
     * 
     * @param portalContext
     *            The encapsulated PortalContext object of current secondary
     *            page.
     * @return PortalURI The address where user is redirected to after
     *         executing.
     * @throws ActionException
     *             throw out
     */
    public PortalURI execute(PortalContext context) throws ActionException {

        try {
            // Get the HttpServletRequest from portalContext
            HttpServletRequest request = context.getPortalRequest()
                    .getRequest();
            HttpSession session = request.getSession();

            // Get the return URL form session
            String url = (String)session
                    .getAttribute(Consts.SESSION_ATTR_RETURN_URL);
            if (url == null) {
                LOG
                        .info("ReturnDisplayAction: no return URL found ¨C redirecting to site home page.");
                // if the return URL is null , set it to the site home page URL
                url = Utils.getEffectiveSiteURL(request);
            }
            LOG.info("ReturnDisplayAction: redirecting to: " + url);
            // redirect user to the return URL
            context.getPortalResponse().getResponse().sendRedirect(url);
        } catch (Exception e) {
            LOG.error("ReturnDisplayAction error: " + e);
            return ExceptionUtil.redirectSystemErrorPage(context, null);
        }
        return null;
    }
}
