/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.profile.manager;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import com.hp.it.spf.user.exception.UserProfileException;
import org.junit.Test;

import com.hp.it.spf.sso.portal.AuthenticationConsts;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * This is the test class for UserProfileRetrieverFactory.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 * @see com.hp.it.spf.user.profile.manager.UserProfileRetrieverFactory
 */
public class UserProfileRetrieverFactoryTest {

    /**
     * Test method for {@link com.hp.it.spf.user.profile.manager.UserProfileRetrieverFactory#createUserProfileImpl(String, String)}.
     */
    @Test
    public void testCreateUserProfileImpl() {
        IUserProfileRetriever retriever = createRetriever(null);

        assertThat(retriever, is(notNullValue()));
    }


    @Test
    public void createUserProfileImplLoadsSiteSpecificRetriever() {
        IUserProfileRetriever retriever = createRetriever("testSite");

        assertThat(retriever, is(notNullValue()));
        assertThat(retriever.getClass().getName(), is(TestUserProfileRetriever.class.getName()));
    }


    @Test
    public void createUserProfileImplLoadsGeneralRetrieverIfNoSiteSpecificRetrieverDefined() {
        IUserProfileRetriever retriever = createRetriever("siteForWhichThereIsNoSpecificRetriever");

        assertThat(retriever, is(notNullValue()));
        assertThat(retriever.getClass().getName(), is(DefaultUserProfileRetriever.class.getName()));
    }


    @Test
    public void createsUserProfileImplLoadsSiteSpecificCompoundRetriever() {
        IUserProfileRetriever retriever = createRetriever("testSite2");

        assertThat(retriever, is(notNullValue()));
        assertThat(retriever.getClass().getName(), is(CompoundUserProfileRetriever.class.getName()));
    }


    @Test
    public void createUserProfileImplLoadsGeneralCompoundRetrieverIfNoSiteSpecificRetrieverDefined() {
        IUserProfileRetriever retriever = UserProfileRetrieverFactory.createUserProfileImpl("another_user_profile_retriever", "siteForWhichThereIsNoSpecificRetriever");

        assertThat(retriever, is(notNullValue()));
        assertThat(retriever.getClass().getName(), is(CompoundUserProfileRetriever.class.getName()));
    }


    @Test
    public void createUserProfileImplCreatesRetrieverOnce() {
        IUserProfileRetriever retriever = createRetriever(null);
        IUserProfileRetriever retriever2 = createRetriever(null);

        assertThat(retriever2, is(sameInstance(retriever)));
    }


    private IUserProfileRetriever createRetriever(String siteDNSName) {
        return UserProfileRetrieverFactory.createUserProfileImpl(AuthenticationConsts.USER_PROFILE_RETRIEVER, siteDNSName);
    }


    public static class TestUserProfileRetriever implements IUserProfileRetriever {

        public Map<String, Object> getUserProfile(String userIdentifier, HttpServletRequest request) throws UserProfileException
        {
            throw new UnsupportedOperationException("not implemented yet");
        }
    }
}
