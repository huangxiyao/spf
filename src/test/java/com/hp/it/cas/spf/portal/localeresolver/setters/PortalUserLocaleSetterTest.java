/*
 * Project: Service Portal
 * Copyright (c) 2006 HP. All Rights Reserved
 * 
 * 
 * FILENAME: VignetteUserLocaleSetterTest.java
 * PACKAGE : com.hp.it.cas.spf.portal.localeresolver.filter
 * $Id: PortalUserLocaleSetterTest.java,v 1.4 2007/06/28 07:14:40 marcd Exp $
 * $Log: PortalUserLocaleSetterTest.java,v $
 * Revision 1.4  2007/06/28 07:14:40  marcd
 * add restrictions for multiple requests
 *
 * Revision 1.3  2007/06/20 05:59:39  marcd
 * open filter for setting action on guest users
 *
 * Revision 1.2  2007/05/29 08:23:51  marcd
 * modified mock location
 *
 * Revision 1.1  2007/04/20 07:39:26  marcd
 * rename to follow the main directory and finish up a bit on the unit testing
 *
 * Revision 1.3  2007/04/17 01:52:04  marcd
 * add stuff for javadoc
 *
 *
 */ 
package com.hp.it.cas.spf.portal.localeresolver.setters;

import java.util.Locale;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import com.epicentric.common.website.SessionInfo;
import com.hp.fast.web.hpweb.LanguageNegotiator;
import com.hp.it.cas.spf.portal.localeresolver.mock.MockUserMediator;
import com.hp.it.cas.spf.portal.localeresolver.setters.PortalUserLocaleSetter;
import com.hp.it.spf.xa.common.portal.mock.MockUser;
import com.hp.it.spf.xa.common.portal.surrogate.SurrogateSessionInfo;
import com.hp.it.spf.xa.common.portal.surrogate.SurrogateSite;

import junit.framework.TestCase;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class PortalUserLocaleSetterTest extends TestCase {
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockHttpSession session;

    protected void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        session = new MockHttpSession();
    }

    /**
     * In this case the session has not been initialized.
     * We want to be sure that no exceptions are thrown and that no
     * set actions take place.
     */
    public void testSetLocaleForSessionNotReady() {
        PortalUserLocaleSetter setter = new PortalUserLocaleSetter();
        
        setter.setLocale(request, response, Locale.FRANCE);
        assertNull(request.getSession(false));
        
        String indicator = (String) request.getAttribute(PortalUserLocaleSetter.SET_ACTION_INDICATOR);
        assertNull(indicator);
    }

    /**
     * In this case there is a session but no SessionUtils initialized
     * by Vignette.
     * Again no set actions should take place no exceptions should be thrown
     */
    public void testSetLocaleForSessionReadyButNoSessionInfo() {
        request.setSession(session);
        PortalUserLocaleSetter setter = new PortalUserLocaleSetter();
        setter.setLocale(request, response, Locale.FRANCE);
        assertNull(request.getSession().getAttribute(
                SessionInfo.SESSION_INFO_NAME));
        
        String indicator = (String) request.getAttribute(PortalUserLocaleSetter.SET_ACTION_INDICATOR);
        assertNull(indicator);
    }
    
    /** 
     * A guest user has been just been initialized for the first time.  
     * <p>Note that we will
     * want to set the locale for each request with a guest user.  This protects against the
     * following situation: a user goes to the login page, obtains a guest user, the guest 
     * users locale is set, the user refreshes the page, obtains a new guest user, the guest
     * users locale is not set.
     * </p>
     */
    public void testShouldResolveLocaleForNewGuestUser() {
        SurrogateSessionInfo sessionInfo = new SurrogateSessionInfo();
        sessionInfo.setCurrentSite(new SurrogateSite());
        //MockHttpSession session = new MockHttpSession();
        session.setAttribute(SessionInfo.SESSION_INFO_NAME, sessionInfo);
        request.setSession(session);
        
        PortalUserLocaleSetter setter = new PortalUserLocaleSetter();       
        MockUserMediator mediator = new MockUserMediator();
        mediator.setCurrentUser(new MockUser());
        mediator.setGuestUser(true);
        setter.setUserMediator(mediator);
        
        //no setting action has occurred before
        boolean expected = setter.shouldResolveLocale(request);
        assertTrue(expected);
        
        //a previus setting action has occurred
        request.setAttribute(PortalUserLocaleSetter.SET_ACTION_INDICATOR, "epi:guest");
        expected = setter.shouldResolveLocale(request);
        assertTrue(expected);
        
        //now the user changes to a standard user
        MockUser standard = new MockUser();
        standard.setUid("epi:standard:899898766");
        mediator.setCurrentUser(standard);
        mediator.setGuestUser(false);
        expected = setter.shouldResolveLocale(request);
        assertTrue(true);
    }
    
    /**
     * Added after opening up the resolver for each request by a guest user.  We can restrict
     * a bit more by checking the ACCEPTABLE parameter and this turns out to be needed to 
     * prevent vignettes SessionUtils from throwing a ClassCastException (internal to their code)
     */
    public void testRequestHasAlreadyBeenResolved() {
        request.setAttribute(LanguageNegotiator.class.getName() + ".ACCEPTABLE", Boolean.TRUE);
        request.setSession(session);
        PortalUserLocaleSetter setter = new PortalUserLocaleSetter();  
        setter.setUserMediator(new MockUserMediator());
        boolean actual = setter.shouldResolveLocale(request);
        assertFalse(actual);
        
    }
}
