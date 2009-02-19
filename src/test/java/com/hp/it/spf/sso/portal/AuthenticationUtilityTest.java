/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.sso.portal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Mockery;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * This is the test class for AuthenticationUtility class.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 * @see com.hp.it.spf.sso.portal.AuthenticationUtility
 */
public class AuthenticationUtilityTest {
    private static Mockery context;

    private static HttpServletRequest athpRequest;
    
    private static HttpServletRequest hppRequest;
    
    private static HttpServletRequest fedRequest;
    
    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        context = MockeryUtils.createMockery();
        athpRequest = MockeryUtils.mockHttpServletRequestForAtHP(context);
        hppRequest = MockeryUtils.mockHttpServletRequestForHPP(context);
        fedRequest = MockeryUtils.mockHttpServletRequestForFed(context);
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
    }

    /**
     * Test method for {@link com.hp.it.spf.sso.portal.AuthenticationUtility#loggedIntoAtHP(javax.servlet.http.HttpServletRequest)}.
     */
    @Test
    public void testLoggedIntoAtHP() {
        boolean isLoggedIntoAtHP = AuthenticationUtility.loggedIntoAtHP(athpRequest);
        assertTrue("Should be from atHP.", isLoggedIntoAtHP);
        
        isLoggedIntoAtHP = AuthenticationUtility.loggedIntoAtHP(null);
        assertFalse("Should be not from atHP", isLoggedIntoAtHP);
        
        isLoggedIntoAtHP = AuthenticationUtility.loggedIntoAtHP(hppRequest);
        assertFalse("Should be not from atHP", isLoggedIntoAtHP);
    }

    /**
     * Test method for {@link com.hp.it.spf.sso.portal.AuthenticationUtility#loggedIntoHPP(javax.servlet.http.HttpServletRequest)}.
     */
    @Test
    public void testLoggedIntoHPP() {
        boolean isLoggedIntoHPP = AuthenticationUtility.loggedIntoHPP(hppRequest);
        assertTrue("Should be from HPP.", isLoggedIntoHPP);
        
        isLoggedIntoHPP = AuthenticationUtility.loggedIntoHPP(athpRequest);
        assertFalse("Should be not from HPP", isLoggedIntoHPP);
        
        isLoggedIntoHPP = AuthenticationUtility.loggedIntoHPP(fedRequest);
        assertFalse("Should be not from HPP", isLoggedIntoHPP);
    }

    /**
     * Test method for {@link com.hp.it.spf.sso.portal.AuthenticationUtility#loggedIntoFed(javax.servlet.http.HttpServletRequest)}.
     * @throws IOException 
     */
    @Test
    public void testLoggedIntoFed() throws IOException {        
        boolean isLoggedIntoFed = AuthenticationUtility.loggedIntoFed(fedRequest);
        assertTrue("Should be from Fed.", isLoggedIntoFed);      
        
        isLoggedIntoFed = AuthenticationUtility.loggedIntoFed(hppRequest);
        assertFalse("Should be not from Fed", isLoggedIntoFed);
        
        isLoggedIntoFed = AuthenticationUtility.loggedIntoFed(athpRequest);
        assertFalse("Should be not from Fed", isLoggedIntoFed);
    }

    /**
     * Test method for {@link com.hp.it.spf.sso.portal.AuthenticationUtility#getSPUserRole(com.epicentric.user.User)}.
     */
    @Test
    @Deprecated
    public void testGetSPUserRole() {        
    }

    /**
     * Test method for {@link com.hp.it.spf.sso.portal.AuthenticationUtility#isFromHPP(javax.servlet.http.HttpServletRequest)}.
     */
    @Test
    public void testIsFromHPP() {
        boolean isFrom = AuthenticationUtility.isFromHPP(hppRequest);
        assertTrue(isFrom);
        
        isFrom = AuthenticationUtility.isFromHPP(athpRequest);
        assertFalse(isFrom);
        
        isFrom = AuthenticationUtility.isFromHPP(fedRequest);
        assertFalse(isFrom);
    }

    /**
     * Test method for {@link com.hp.it.spf.sso.portal.AuthenticationUtility#isFromAtHP(javax.servlet.http.HttpServletRequest)}.
     */
    @Test
    public void testIsFromAtHP() {
        boolean isFrom = AuthenticationUtility.isFromAtHP(athpRequest);
        assertTrue(isFrom);
        
        isFrom = AuthenticationUtility.isFromAtHP(hppRequest);
        assertFalse(isFrom);
        
        isFrom = AuthenticationUtility.isFromAtHP(fedRequest);
        assertFalse(isFrom);
    }

    /**
     * Test method for {@link com.hp.it.spf.sso.portal.AuthenticationUtility#isFromFed(javax.servlet.http.HttpServletRequest)}.
     */
    @Test
    public void testIsFromFed() {
        boolean isFrom = AuthenticationUtility.isFromFed(fedRequest);
        assertTrue(isFrom);
        
        isFrom = AuthenticationUtility.isFromFed(hppRequest);
        assertFalse(isFrom);
        
        isFrom = AuthenticationUtility.isFromFed(athpRequest);
        assertFalse(isFrom);
    }

    /**
     * Test method for {@link com.hp.it.spf.sso.portal.AuthenticationUtility#getGroupsFromCurrentUser(javax.servlet.http.HttpServletRequest)}.
     */
    @Test
    @Deprecated
    public void testGetGroupsFromCurrentUser() {
    }
}
