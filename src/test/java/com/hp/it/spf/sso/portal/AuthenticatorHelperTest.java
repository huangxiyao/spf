/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.sso.portal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.epicentric.common.website.SessionUtils;
import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.entity.UniquePropertyValueConflictException;
import com.epicentric.site.Site;
import com.epicentric.user.User;
import com.epicentric.user.UserGroup;
import com.epicentric.user.UserManager;

/**
 * This is the test class for AuthenticatorHelper class.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
 */
@RunWith(JMock.class)
public class AuthenticatorHelperTest {
    private static Mockery context;
    
    private static HttpServletRequest athpRequest;
    
    private static HttpServletRequest hppRequest;
    
    private static HttpServletRequest fedRequest;
    
    private static HttpServletRequest anonRequest;
    
    private static User user;

    private static User guestUser;

    private static SSOUser ssoUser;

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
        user = MockeryUtils.mockUser(context);
        guestUser = MockeryUtils.mockGuestUser(context);

        ssoUser = new SSOUser();
        ssoUser.setCountry((String)UserProfile.get(AuthenticationConsts.KEY_COUNTRY));
        ssoUser.setUserName((String)UserProfile.get(AuthenticationConsts.KEY_USER_NAME));
        ssoUser.setEmail((String)UserProfile.get(AuthenticationConsts.KEY_EMAIL));
        ssoUser.setTimeZone((String)UserProfile.get(AuthenticationConsts.KEY_TIMEZONE));
        Set<String> groups = new HashSet<String>();
        groups.add("LOCAL_PORTAL_LANG_ZH");
        groups.add("USER_ADMIN_GROUP");
        ssoUser.setGroups(groups);

        UserManager.createUser(user, guestUser);
        SessionUtils.setCurrentUser(athpRequest.getSession(), user);
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
        user = null;
        guestUser = null;
        ssoUser = null;
        UserManager.cleanup();
        SessionUtils.cleanup();
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#createVAPUser(com.hp.it.spf.sso.portal.SSOUser)}
     * .
     * 
     * @throws EntityPersistenceException
     * @throws UniquePropertyValueConflictException
     */
    @Test
    public void testCreateVAPUser() throws UniquePropertyValueConflictException,
                                   EntityPersistenceException {
        User user = AuthenticatorHelper.createVAPUser(ssoUser);
        assertNotNull(user);
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#updateVAPUser(com.epicentric.user.User, com.hp.it.spf.sso.portal.SSOUser)}
     * .
     * 
     * @throws EntityPersistenceException
     * @throws UniquePropertyValueConflictException
     */
    @Test
    public void testUpdateVAPUser() throws UniquePropertyValueConflictException,
                                   EntityPersistenceException {
        AuthenticatorHelper.updateVAPUser(user, ssoUser);
        assertTrue(true);
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#getCurrentSite(javax.servlet.http.HttpServletRequest)}
     * .
     */
    @Test
    public void testGetCurrentSite() {
        Site site = AuthenticatorHelper.getCurrentSite(athpRequest);
        assertNull(site);
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#needUpdateGroup(java.util.Set, java.util.Set)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testNeedUpdateGroup() {        
        Set userGroups = AuthenticatorHelper.getUserGroupSet(user);
        Set newGroups = ssoUser.getGroups();
        boolean result = false;
        
        result = AuthenticatorHelper.needUpdateGroup(null, newGroups);
        assertTrue(result);
        
        result = AuthenticatorHelper.needUpdateGroup(userGroups, null);
        assertTrue(result);
        
        result = AuthenticatorHelper.needUpdateGroup(null, null);
        assertFalse(result);
        
        // different groups
        result = AuthenticatorHelper.needUpdateGroup(userGroups, newGroups);
        assertTrue(result);
        
        // same groups
        Set<String> groups = new HashSet<String>();
        groups.add("LOCAL_PORTAL_LANG_EN");
        result = AuthenticatorHelper.needUpdateGroup(userGroups, groups);
        assertFalse(result);
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#isPrimarySiteChanged(javax.servlet.http.HttpServletRequest)}
     * .
     */
    @Test
    public void testIsPrimarySiteChanged() {
        boolean result = false;
        result = AuthenticatorHelper.isPrimarySiteChanged(athpRequest);
        assertFalse(result);        
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#getUserGroupTitleSet(java.util.Set)}
     * .
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testGetUserGroupTitleSet() {
        Set userGroups = AuthenticatorHelper.getUserGroupSet(user);
        Set<String> groupsInUser = new HashSet<String>();        
        for (UserGroup group : (Set<UserGroup>)userGroups) {
            groupsInUser.add((String)group.getProperty(AuthenticationConsts.GROUP_TITLE));
        }
        
        Set groups = AuthenticatorHelper.getUserGroupTitleSet(userGroups);
        assertEquals(groups, groupsInUser);
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#getUserGroupSet(com.epicentric.user.User)}
     * .
     */
    @Test
    public void testGetUserGroupSet() {
       Set set = AuthenticatorHelper.getUserGroupSet(null);
       assertNull(set);
       
       set = AuthenticatorHelper.getUserGroupSet(guestUser);
       assertNull(set);
       
       set = AuthenticatorHelper.getUserGroupSet(user);
       assertNotNull(set);
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#updateUserGroup(com.epicentric.user.User, java.util.Set, java.util.Set)}
     * .
     */
    @Test    
    public void testUpdateUserGroup() {   
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#isFromHPP(javax.servlet.http.HttpServletRequest)}
     * .
     */
    @Test
    public void testIsFromHPP() {
        boolean reslut = false;
        reslut = AuthenticatorHelper.isFromHPP(hppRequest);
        assertTrue(reslut);
        
        reslut = AuthenticatorHelper.isFromHPP(athpRequest);
        assertFalse(reslut);
        
        reslut = AuthenticatorHelper.isFromHPP(fedRequest);
        assertFalse(reslut);
        
        reslut = AuthenticatorHelper.isFromHPP(anonRequest);
        assertFalse(reslut);        
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#isFromAtHP(javax.servlet.http.HttpServletRequest)}
     * .
     */
    @Test
    public void testIsFromAtHP() {
        boolean reslut = false;
        reslut = AuthenticatorHelper.isFromAtHP(hppRequest);
        assertFalse(reslut);
        
        reslut = AuthenticatorHelper.isFromAtHP(athpRequest);
        assertTrue(reslut);
        
        reslut = AuthenticatorHelper.isFromAtHP(fedRequest);
        assertFalse(reslut);
        
        reslut = AuthenticatorHelper.isFromAtHP(anonRequest);
        assertFalse(reslut); 
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#isFromFed(javax.servlet.http.HttpServletRequest)}
     * .
     */
    @Test
    public void testIsFromFed() {
        boolean reslut = false;
        reslut = AuthenticatorHelper.isFromFed(hppRequest);
        assertFalse(reslut);
        
        reslut = AuthenticatorHelper.isFromFed(athpRequest);
        assertFalse(reslut);
        
        reslut = AuthenticatorHelper.isFromFed(fedRequest);
        assertTrue(reslut);
        
        reslut = AuthenticatorHelper.isFromFed(anonRequest);
        assertFalse(reslut); 
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#isForceInitSession(javax.servlet.http.HttpServletRequest)}
     * .
     */
    @Test
    public void testIsForceInitSession() {
        boolean reslut = false;
        reslut = AuthenticatorHelper.isForceInitSession(hppRequest);
        assertFalse(reslut);
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#isVAPLoggedIn(javax.servlet.http.HttpServletRequest)}
     * .
     */
    @Test
    public void testIsVAPLoggedIn() {
        boolean reslut = false;
        reslut = AuthenticatorHelper.isVAPLoggedIn(hppRequest);
        assertTrue(reslut);
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#loggedIntoAtHP(javax.servlet.http.HttpServletRequest)}
     * .
     */
    @Test
    public void testLoggedIntoAtHP() {
        boolean reslut = false;
        reslut = AuthenticatorHelper.loggedIntoAtHP(athpRequest);
        assertTrue(reslut);
        
        reslut = AuthenticatorHelper.loggedIntoAtHP(hppRequest);
        assertFalse(reslut);
        
        reslut = AuthenticatorHelper.loggedIntoAtHP(fedRequest);
        assertFalse(reslut);
        
        reslut = AuthenticatorHelper.loggedIntoAtHP(anonRequest);
        assertFalse(reslut);
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#loggedIntoHPP(javax.servlet.http.HttpServletRequest)}
     * .
     */
    @Test
    public void testLoggedIntoHPP() {
        boolean reslut = false;
        reslut = AuthenticatorHelper.loggedIntoHPP(athpRequest);
        assertFalse(reslut);
        
        reslut = AuthenticatorHelper.loggedIntoHPP(hppRequest);
        assertTrue(reslut);
        
        reslut = AuthenticatorHelper.loggedIntoHPP(fedRequest);
        assertFalse(reslut);
        
        reslut = AuthenticatorHelper.loggedIntoHPP(anonRequest);
        assertFalse(reslut);
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#loggedIntoFed(javax.servlet.http.HttpServletRequest)}
     * .
     */
    @Test
    public void testLoggedIntoFed() {
        boolean reslut = false;
        reslut = AuthenticatorHelper.loggedIntoFed(athpRequest);
        assertFalse(reslut);
        
        reslut = AuthenticatorHelper.loggedIntoFed(hppRequest);
        assertFalse(reslut);
        
        reslut = AuthenticatorHelper.loggedIntoFed(fedRequest);
        assertTrue(reslut);
        
        reslut = AuthenticatorHelper.loggedIntoFed(anonRequest);
        assertFalse(reslut);
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#isSandBox()}.
     */
    @Test
    public void testIsSandBox() {
        boolean reslut = false;
        reslut = AuthenticatorHelper.isSandBox();
        assertFalse(reslut);
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#cleanupSession(javax.servlet.http.HttpServletRequest)}
     * .
     */
    @Test
    public void testCleanupSession() {
        AuthenticatorHelper.cleanupSession(hppRequest);
        context.assertIsSatisfied();
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#getRequestHeader(javax.servlet.http.HttpServletRequest, java.lang.String)}
     * .
     */
    @Test
    public void testGetRequestHeaderHttpServletRequestString() {
        String value = AuthenticatorHelper.getRequestHeader(athpRequest, "AuthSource");
        assertEquals("ATHP", value);
        
        value = AuthenticatorHelper.getRequestHeader(hppRequest, "AuthSource");
        assertEquals("HPP", value);
        
        value = AuthenticatorHelper.getRequestHeader(fedRequest, "AuthSource");
        assertEquals("HPP", value);
        
        value = AuthenticatorHelper.getRequestHeader(anonRequest, "AuthSource");
        assertEquals("", value);
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#getRequestHeader(javax.servlet.http.HttpServletRequest, java.lang.String, boolean)}
     * .
     */
    @Test
    public void testGetRequestHeaderHttpServletRequestStringBoolean() {
        String value = AuthenticatorHelper.getRequestHeader(hppRequest, "SM_UNIVERSALID", false);
        assertEquals("=?UTF-8?B?ZDMxNDBhNTAyODkyNTg1YTk0YTNlNjYxM2QzZWM3ZWU=?=", value);
        
        value = AuthenticatorHelper.getRequestHeader(hppRequest, "SM_UNIVERSALID", true);
        assertEquals("d3140a502892585a94a3e6613d3ec7ee", value);
    }

    /**
     * Test method for
     * {@link com.hp.it.spf.sso.portal.AuthenticatorHelper#getPrimarySiteUID(javax.servlet.http.HttpServletRequest)}
     * .
     */
    @Test
    public void testGetPrimarySiteUID() {
       String siteuid = AuthenticatorHelper.getPrimarySiteUID(hppRequest);
       assertEquals(UserProfile.get(AuthenticationConsts.PROPERTY_PRIMARY_SITE_ID), siteuid);
    }    
}
