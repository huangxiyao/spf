/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.sso.portal;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This is the test class for TestAuthenticator class.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 * @see com.hp.it.spf.sso.portal.TestAuthenticator
 */
@RunWith(JMock.class)
public class TestAuthenticatorTest {
    private static Mockery context;

    private static HttpServletRequest request;
    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        context = MockeryUtils.createMockery();
        request = MockeryUtils.mockHttpServletRequestForSandBox(context);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        context = null;
        request = null;
    }

    /**
     * Test method for {@link com.hp.it.spf.sso.portal.TestAuthenticator#execute()}.
     */
    @Test
    public void testExecute() {
        TestAuthenticator authenticator = new TestAuthenticator(request);
        authenticator.execute();
    }

    /**
     * Test method for {@link com.hp.it.spf.sso.portal.TestAuthenticator#mapHeaderToUserProfileMap()}.
     */
    @Test
    public void testMapHeaderToUserProfileMap() {
        TestAuthenticator authenticator = new TestAuthenticator(request);
        authenticator.mapHeaderToUserProfileMap();
        assertTrue(authenticator.userProfile.size() > 0);
    }

    /**
     * Test method for {@link com.hp.it.spf.sso.portal.TestAuthenticator#getUserGroup()}.
     */
    @Test
    public void testGetUserGroup() {
        TestAuthenticator authenticator = new TestAuthenticator(request);
        Set set = authenticator.getUserGroups();
        assertTrue(set.size() > 0);
    }

    /**
     * Test method for {@link com.hp.it.spf.sso.portal.TestAuthenticator#getValue(java.lang.String)}.
     */
    @Test
    public void testGetValue() {
        String value;
        TestAuthenticator authenticator = new TestAuthenticator(request);
        value = authenticator.getValue(null);
        assertNull(value);
        
        value = authenticator.getValue(AuthenticationConsts.HEADER_GROUP_NAME);
    }

    /**
     * Test method for {@link com.hp.it.spf.sso.portal.TestAuthenticator#TestAuthenticator(javax.servlet.http.HttpServletRequest)}.
     */
    @Test
    public void testTestAuthenticator() {
        TestAuthenticator authenticator = new TestAuthenticator(request);
        assertEquals(TestAuthenticator.class, authenticator.getClass());
    }

    /**
     * Test method for {@link com.hp.it.spf.sso.portal.TestAuthenticator#getUserProfile()}.
     */
    @Test
    public void testGetUserProfile() {
        TestAuthenticator authenticator = new TestAuthenticator(request);
        Map map = authenticator.getUserProfile();
        assertTrue(map.size() > 0);
    }

}
