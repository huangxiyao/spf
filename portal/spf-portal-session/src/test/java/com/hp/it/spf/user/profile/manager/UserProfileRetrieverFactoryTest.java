/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.profile.manager;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.hp.it.spf.sso.portal.AuthenticationConsts;

/**
 * This is the test class for UserProfileRetrieverFactory.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 * @see com.hp.it.spf.user.profile.manager.UserProfileRetrieverFactory
 */
public class UserProfileRetrieverFactoryTest {

    /**
     * Test method for {@link com.hp.it.spf.user.profile.manager.UserProfileRetrieverFactory#createUserProfileImpl()}.
     */
    @Test
    public void testCreateUserProfileImpl() {
        IUserProfileRetriever retriever = UserProfileRetrieverFactory.createUserProfileImpl(AuthenticationConsts.USER_PROFILE_RETRIEVER);
        assertNotNull("Should return a implimentation class", retriever);        
    }

}