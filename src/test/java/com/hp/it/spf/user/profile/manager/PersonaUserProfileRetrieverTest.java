/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.profile.manager;

import static junit.framework.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

/**
 * This is the test class for PersonaUserProfileRetriever class.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 * @see com.hp.it.spf.user.group.manager.PersonaUserProfileRetriever
 */
public class PersonaUserProfileRetrieverTest {

    /**
     * Test method for {@link com.hp.it.spf.user.profile.manager.PersonaUserProfileRetriever#getUserProfile(java.lang.String)}.
     */
    @Test
    public void testGetUserProfile() {
        IUserProfileRetriever retriever = UserProfileRetrieverFactory.createUserProfileImpl();
        Map<String, String> userProfiles = retriever.getUserProfile(null);
        assertTrue(userProfiles.size() == 0);
    }

}
