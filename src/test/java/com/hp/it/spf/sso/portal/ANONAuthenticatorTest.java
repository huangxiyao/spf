/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.sso.portal;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.epicentric.common.website.SessionUtils;
import com.epicentric.user.User;
import com.epicentric.user.UserManager;

/**
 * This is the test class for ANONAuthenticator class.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 * @see com.hp.it.spf.sso.portal.ANONAuthenticator
 */
@RunWith(JMock.class)
public class ANONAuthenticatorTest {

    private static Mockery context;

    private static HttpServletRequest request;

    private static User user;

    private static User guestUser;

    /**
     * Init mock objects.
     * 
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        context = MockeryUtils.createMockery();
        request = MockeryUtils.mockHttpServletRequest(context);
        user = MockeryUtils.mockUser(context);
        guestUser = MockeryUtils.mockGuestUser(context);
    }

    /**
     * Claenup the resource. Set static pararmters to null, so the object they
     * refer to can be GCed by JVM.
     * 
     * @throws Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        context = null;
        request = null;
        user = null;
        guestUser = null;
        SessionUtils.cleanup();
        UserManager.cleanup();
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.ANONAuthenticator#ANONAuthenticator(javax.servlet.http.HttpServletRequest)}
     * .
     */
    @Test
    public void testANONAuthenticator() {
        ANONAuthenticator authenticator = new ANONAuthenticator(request);
        assertNotNull("ANONAuthenticator class should be created.",
                      authenticator);
        assertNotNull("HttpServletRequest should be assigned.",
                      authenticator.request);
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.ANONAuthenticator#saveUserProfile2Session(com.epicentric.user.User)}
     * .
     */
    @Test
    public void testSaveUserProfile2Session() {
        ANONAuthenticator authenticator = new ANONAuthenticator(request);

        // user is null
        try {
            authenticator.saveUserProfile2Session(null);      
            assertEquals(authenticator.userProfile.entrySet().size(), 0);
        } catch (IllegalArgumentException ex) {
            fail();
        }

        authenticator.saveUserProfile2Session(user);
        assertTrue(authenticator.userProfile.size() > 0);
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.ANONAuthenticator#execute()}.
     */
    @Test
    public void testExecute() {
        ANONAuthenticator authenticator = null;
        // Use custom SessionUtils to set mocked user
        // later mocked user can be retrieved from it.
        SessionUtils.setCurrentUser(request.getSession(), user);

        // Use custom UserManager to set user and guestUser object
        // later these mocked object can be retrieved from it.
        UserManager.createUser(user, guestUser);

        Locale locale = (Locale)request.getAttribute(AuthenticationConsts.SSO_USER_LOCALE);

        // exist sso_guest_user_<locale> user
        UserManager.setReturnUserType("both");
        authenticator = new ANONAuthenticator(request);
        authenticator.execute();
        assertEquals(authenticator.getUserName(),
                     AuthenticationConsts.ANON_USER_NAME_PREFIX
                             + locale.getLanguage().toLowerCase()
                             + "-"
                             + locale.getCountry().toLowerCase());

        // exist sso_guest_user_<language_from_locale> user
        UserManager.setReturnUserType("lang");
        authenticator = new ANONAuthenticator(request);
        authenticator.execute();
        assertEquals(authenticator.getUserName(),
                     AuthenticationConsts.ANON_USER_NAME_PREFIX
                             + locale.getLanguage().toLowerCase());

        // exist default user
        UserManager.setReturnUserType("en");
        authenticator = new ANONAuthenticator(request);
        authenticator.execute();
        assertEquals(authenticator.getUserName(),
                     AuthenticationConsts.ANON_USER_NAME_PREFIX + "en");
    }

}
