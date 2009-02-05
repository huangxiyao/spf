/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.group.manager;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * This is the test class for UserGroupRetrieverFactory.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 * @see com.hp.it.spf.user.group.manager.UserGroupRetrieverFactory
 */
public class UserGroupRetrieverFactoryTest {
    /**
     * Test functionality for CreateUserGroupImpl method.
     * 
     * @see UserGroupRetrieverFactory#createUserGroupImpl(String)
     */
    @Test
    public void testCreateUserGroupImpl() {
        IUserGroupRetriever retriever = UserGroupRetrieverFactory.createUserGroupImpl(null);
        assertNotNull("Should return a implimentation class", retriever);
        assertTrue("SSOUserGroupRetriever object should be created.",
                   retriever instanceof SSOUserGroupRetriever);
    }
}
