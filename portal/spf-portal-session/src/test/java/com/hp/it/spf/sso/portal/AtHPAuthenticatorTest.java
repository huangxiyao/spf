/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.sso.portal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This is the test class for AtHPAuthenticator class.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 * @see com.hp.it.spf.sso.portal.AtHPAuthenticator
 */
@RunWith(JMock.class)
public class AtHPAuthenticatorTest {
    private static Mockery context;

    private static HttpServletRequest request;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        context = MockeryUtils.createMockery();
        request = MockeryUtils.mockHttpServletRequestForAtHP(context);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        context = null;
        request = null;
    }
    
    @Test
    public void testAtHPAuthenticator() {
        AtHPAuthenticator authenticator = new AtHPAuthenticator(request);
        assertEquals(AtHPAuthenticator.class, authenticator.getClass());
    }
    
    @Test
    public void testMapHeaderToUserProfileMap() {
        AtHPAuthenticator authenticator = new AtHPAuthenticator(request);
        authenticator.mapHeaderToUserProfileMap();
        assertTrue(authenticator.userProfile.size() > 0);
    }
}
