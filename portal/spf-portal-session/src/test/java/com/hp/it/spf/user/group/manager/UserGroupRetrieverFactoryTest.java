/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.group.manager;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import com.hp.it.spf.user.exception.UserGroupsException;
import com.hp.it.spf.user.profile.manager.CompoundUserProfileRetriever;
import org.junit.Test;

import com.hp.it.spf.sso.portal.AuthenticationConsts;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;


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
     * @see UserGroupRetrieverFactory#createUserGroupImpl(String, String)
     */
    @Test
    public void testCreateUserGroupImpl() {
        IUserGroupRetriever retriever = createRetriever(null);

        assertThat(retriever, is(notNullValue()));
    }


    @Test
    public void createUserProfileImplLoadsSiteSpecificRetriever() {
        IUserGroupRetriever retriever = createRetriever("testSite");

        assertThat(retriever, is(notNullValue()));
        assertThat(retriever.getClass().getName(), is(TestUserGroupRetriever.class.getName()));
    }


    @Test
    public void createUserProfileImplLoadsGeneralRetrieverIfNoSiteSpecificRetrieverDefined() {
        IUserGroupRetriever retriever = createRetriever("siteForWhichThereIsNoSpecificRetriever");

        assertThat(retriever, is(notNullValue()));
        assertThat(retriever.getClass().getName(), is(DefaultUserGroupRetriever.class.getName()));
    }


    @Test
    public void createsUserProfileImplLoadsSiteSpecificCompoundRetriever() {
        IUserGroupRetriever retriever = createRetriever("testSite2");

        assertThat(retriever, is(notNullValue()));
        assertThat(retriever.getClass().getName(), is(CompoundUserGroupRetriever.class.getName()));
    }


    @Test
    public void createUserProfileImplLoadsGeneralCompoundRetrieverIfNoSiteSpecificRetrieverDefined() {
        IUserGroupRetriever retriever = UserGroupRetrieverFactory.createUserGroupImpl("another_user_group_retriever", "siteForWhichThereIsNoSpecificRetriever");

        assertThat(retriever, is(notNullValue()));
        assertThat(retriever.getClass().getName(), is(CompoundUserGroupRetriever.class.getName()));
    }


    @Test
    public void createUserProfileImplCreatesRetrieverOnce() {
        IUserGroupRetriever retriever = createRetriever(null);
        IUserGroupRetriever retriever2 = createRetriever(null);

        assertThat(retriever2, is(sameInstance(retriever)));
    }


    private IUserGroupRetriever createRetriever(String siteDNSName) {
        return UserGroupRetrieverFactory.createUserGroupImpl("test_user_group_retriever", siteDNSName);
    }


    public static class TestUserGroupRetriever implements IUserGroupRetriever {

        public Set<String> getGroups(Map<String, Object> userProfile, HttpServletRequest request) throws UserGroupsException
        {
            throw new UnsupportedOperationException("not implemented yet");
        }
    }
}
