/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.sso.portal;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This is the test class for AuthenticatorFactory class.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 * @see com.hp.it.spf.sso.portal.AuthenticatorFactory
 */
@RunWith(JMock.class)
public class AuthenticatorFactoryTest {

    private static Mockery context;

    private static HttpServletRequest athpRequest;
    
    private static HttpServletRequest hppRequest;
    
    private static HttpServletRequest fedRequest;
    
    private static HttpServletRequest anonRequest;
    
    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        context = MockeryUtils.createMockery();
        athpRequest = MockeryUtils.mockHttpServletRequestForAtHP(context);
        hppRequest = MockeryUtils.mockHttpServletRequestForHPP(context);
        fedRequest = MockeryUtils.mockHttpServletRequestForFed(context);
        anonRequest = MockeryUtils.mockHttpServletRequestForANON(context);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        context = null;
        athpRequest = null;
        hppRequest = null;
        fedRequest = null;
        anonRequest = null;
    }

    /**
     * Test method for {@link com.hp.it.spf.sso.portal.AuthenticatorFactory#createAuthenticator(HttpServletRequest)}.
     */
    @Test
    public void testCreateAuthenticator() {
        IAuthenticator authenticator = AuthenticatorFactory.createAuthenticator(null);
        assertNull(authenticator);
        
        authenticator = AuthenticatorFactory.createAuthenticator(athpRequest);
        assertEquals(AtHPAuthenticator.class, authenticator.getClass());
        
        authenticator = AuthenticatorFactory.createAuthenticator(hppRequest);
        assertEquals(HPPAuthenticator.class, authenticator.getClass());
        
        authenticator = AuthenticatorFactory.createAuthenticator(fedRequest);
        assertEquals(HPPAuthenticator.class, authenticator.getClass());
        
        authenticator = AuthenticatorFactory.createAuthenticator(anonRequest);
        assertEquals(ANONAuthenticator.class, authenticator.getClass());
    }

}
