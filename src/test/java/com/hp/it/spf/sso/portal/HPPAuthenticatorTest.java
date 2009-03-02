/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.sso.portal;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Mockery;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This is the test class for HPPAuthenticator class.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 * @see com.hp.it.spf.sso.portal.HPPAuthenticator
 */
public class HPPAuthenticatorTest {
    private static Mockery context;

    private static HttpServletRequest hppRequest;
    
    private static HttpServletRequest fedRequest;
    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        context = MockeryUtils.createMockery();
        hppRequest = MockeryUtils.mockHttpServletRequestForHPP(context);
        fedRequest = MockeryUtils.mockHttpServletRequestForFed(context);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        context = null;
        hppRequest = null;
        fedRequest = null;
    }
    
    /**
     * Test method for {@link com.hp.it.spf.sso.portal.HPPAuthenticator#HPPAuthenticator(javax.servlet.http.HttpServletRequest)}.
     */
    @Test
    public void testHPPAuthenticator() {
        HPPAuthenticator authenticator = new HPPAuthenticator(hppRequest);
        assertEquals(HPPAuthenticator.class, authenticator.getClass());
        
        authenticator = new HPPAuthenticator(fedRequest);
        assertEquals(HPPAuthenticator.class, authenticator.getClass());
    }
    
    /**
     * Test method for {@link com.hp.it.spf.sso.portal.HPPAuthenticator#mapHeaderToUserProfileMap()}.
     */
    @Test
    public void testMapHeaderToUserProfileMap() {
        HPPAuthenticator authenticator = new HPPAuthenticator(hppRequest);
        authenticator.mapHeaderToUserProfileMap();
        assertTrue(authenticator.userProfile.size() > 0);
        
        authenticator = new HPPAuthenticator(fedRequest);
        authenticator.mapHeaderToUserProfileMap();
        assertTrue(authenticator.userProfile.size() > 0);
    }

    /**
     * Test method for {@link com.hp.it.spf.sso.portal.HPPAuthenticator#getValue(java.lang.String)}.
     */
    @Test
    public void testGetValue() {
        HPPAuthenticator authenticator = new HPPAuthenticator(hppRequest);
        String profileid = authenticator.getValue(AuthenticationConsts.HEADER_PROFILE_ID_PROPERTY_NAME);
        assertEquals(UserProfile.get(AuthenticationConsts.KEY_PROFILE_ID), profileid);
        
        authenticator = new HPPAuthenticator(fedRequest);
        profileid = authenticator.getValue(AuthenticationConsts.HEADER_PROFILE_ID_PROPERTY_NAME);
        assertEquals(UserProfile.get(AuthenticationConsts.KEY_PROFILE_ID), profileid);
    }
}
