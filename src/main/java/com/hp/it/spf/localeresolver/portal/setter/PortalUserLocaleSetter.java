/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.setter;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.epicentric.common.website.I18nUtils;
import com.epicentric.common.website.Localizer;
import com.epicentric.common.website.SessionInfo;
import com.epicentric.entity.ChildEntity;
import com.epicentric.site.Site;
import com.hp.it.spf.localeresolver.hpweb.LanguageNegotiator;
import com.hp.it.spf.localeresolver.portal.getter.IUserMediator;
import com.hp.it.spf.localeresolver.portal.getter.PortalUserMediator;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class PortalUserLocaleSetter implements ILocaleSetter {
    /**
     * a session location which holds a state indicator describing the state at the
     * last set action
     */
    public static final String SET_ACTION_INDICATOR = PortalUserLocaleSetter.class.getName()
        + "SET_ACTION_INDICATOR";
    private IUserMediator userMediator = new PortalUserMediator();
    
    /**
     * This can override the default provider -- used for testing.
     * @param userMediator any mediator implementation 
     */
    public void setUserMediator(IUserMediator userMediator) {
        this.userMediator = userMediator;
    }

    /**
     * Sets the user locale via the vignette api 
     * @param request the http sevlet request
     * @param response the http sevlet response
     * @param locale the locale which vignette will associate with the user
     */
    public void setLocale(HttpServletRequest request,
            HttpServletResponse response, final Locale locale) {
        ChildEntity user = userMediator.getCurrentUser(request);
        if (user == null) {
            return;
        }
        
        if (userMediator.isGuestUser(user)) {
            Localizer localizer = I18nUtils.getLocalizer(request.getSession(false), 
                    request);
            localizer.setLocale(locale);
        } else {
            I18nUtils.setUserLocale(user, locale);
        }
        
        String uid = userAndSiteCompositeUid(user, request);
        request.getSession().setAttribute(SET_ACTION_INDICATOR, uid);
    }

    /**
     * Detemines if either the object representing the user has changed or if the
     * user has changed sites.
     * <p>Either situation above means that the locale associated with the user has
     * to be reevaluated</p>
     * @param request the http servlet request
     * @return true if the locale has to be reevaluated and set
     */
    public boolean shouldResolveLocale(HttpServletRequest request) {
        if (localeHasBeenResolvedForThisRequest(request)) {
            return false;
        }
        ChildEntity user = userMediator.getCurrentUser(request);
        if (user == null) {
            return false;
        }
        if (userMediator.isGuestUser(user)) {
            return true;
        }
        String uid = userAndSiteCompositeUid(user, request);
        return (!uid.equals(request.getSession().getAttribute(SET_ACTION_INDICATOR)));
    }
    
    private boolean localeHasBeenResolvedForThisRequest(HttpServletRequest request) {
        Boolean acc = (Boolean) request.getAttribute(LanguageNegotiator.class.getName() + ".ACCEPTABLE");
        return (acc != null && acc.booleanValue());
    }
    
    private String userAndSiteCompositeUid(ChildEntity user, HttpServletRequest request) {
        SessionInfo sessionInfo = (SessionInfo) request.getSession().getAttribute(SessionInfo.SESSION_INFO_NAME);
        Site currentSite = sessionInfo.getSite();
        String dnsName = "";
        if (currentSite != null) {
               dnsName = currentSite.getDNSName();
        }
        return user.getUID() + dnsName;
    }
}
