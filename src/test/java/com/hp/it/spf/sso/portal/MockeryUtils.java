/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.sso.portal;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.lib.legacy.ClassImposteriser;

import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.entity.EntityType;
import com.epicentric.entity.implementations.generic.ChildEntityWrapper;
import com.epicentric.user.User;
import com.epicentric.user.UserGroup;
import com.epicentric.user.UserProvider;

/**
 * This is a utils class to create mockery context and create needed mock
 * objects
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class MockeryUtils {
    /**
     * Create Mockery context and init it
     * 
     * @return Mockery context
     */
    public static Mockery createMockery() {
        Mockery context = new Mockery();
        // set imposteriser to instance and CGLIB will be used,
        // If this value is not assigned, dynamic Proxy will be used.
        context.setImposteriser(ClassImposteriser.INSTANCE);

        context.setDefaultResultForType(Object.class, null);
        return context;
    }

    /**
     * Create HttpServletRequest mock object and set expectations to it.
     * 
     * @param context mockery context
     * @return mocked HttpServletRequest object
     */
    public static HttpServletRequest mockHttpServletRequest(Mockery context) {
        // mock HttpServletRequest
        final HttpServletRequest request = context.mock(HttpServletRequest.class);
        context.checking(new Expectations() {
            {
                allowing(request).getSession();
                allowing(request).getSession(with(any(Boolean.class)));

                allowing(request).getAttribute(AuthenticationConsts.SSO_USER_LOCALE);
                will(returnValue(new Locale("en", "US")));
            }
        });
        return request;
    }

    /**
     * Create Vignette user mock object and set expectations to it.
     * 
     * @param context mockery context
     * @return mocked User object
     * @see com.epicentric.user.User
     */
    public static User mockUser(Mockery context) {
        // mock User Entity, EntityType
        final ChildEntityWrapper entity = context.mock(ChildEntityWrapper.class);
        final EntityType entityType = context.mock(EntityType.class);
        final UserGroup userGroup = context.mock(UserGroup.class);
        final UserGroup userGroup2 = context.mock(UserGroup.class, "GROUP2");
        context.checking(new Expectations() {
            {
                atLeast(1).of(entity).getUID();
                will(returnValue("epi:user.standard;22e9b53cd770499db193561024836ff0"));

                allowing(entity).getEntityType();
                will(returnValue(entityType));

                allowing(userGroup).getProperty(AuthenticationConsts.GROUP_TITLE);
                will(returnValue("LOCAL_TEST_GROUP"));
                
                allowing(userGroup2).getProperty(AuthenticationConsts.GROUP_TITLE);
                will(returnValue("LOCAL_TEST_GROUP2"));

                try {
                    Set<UserGroup> set = new HashSet<UserGroup>();
                    set.add(userGroup);
                    set.add(userGroup2);
                    allowing(entity).getParents(with(any(EntityType.class)),
                                                with(any(Boolean.class)));
                    will(returnValue(set));
                } catch (EntityPersistenceException ex) {
                    will(throwException(ex));
                }

                allowing(entityType).getID();
                will(returnValue("epi:user.standard"));

                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_PROFILE_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_PROFILE_ID)));

                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_USER_NAME_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_USER_NAME)));

                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_EMAIL_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_EMAIL)));

                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_FIRSTNAME_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_FIRST_NAME)));

                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_LASTNAME_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_LAST_NAME)));

                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_COUNTRY_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_COUNTRY)));

                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_LANGUAGE_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_LANGUAGE)));

                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_LAST_CHANGE_DATE_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_LAST_CHANGE_DATE)));

                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_SPF_TIMEZONE_ID);
                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_TIMEZONE)));

            }
        });
        User user = UserProvider.getUser(entity);
        return user;
    }

    /**
     * Create Vignette guest user mock object and set expectations to it.
     * 
     * @param context mockery context
     * @return mocked User object
     * @see com.epicentric.user.User
     */
    public static User mockGuestUser(Mockery context) {
        // mock User Entity, EntityType
        final ChildEntityWrapper entity = context.mock(ChildEntityWrapper.class, "GUESTENTITY");
        final EntityType entityType = context.mock(EntityType.class, "GUESTEINTITYTYPT");
        final UserGroup userGroup = context.mock(UserGroup.class, "GUESTGROUP");
        final UserGroup userGroup2 = context.mock(UserGroup.class, "GUESTGROUP2");
        context.checking(new Expectations() {
            {
                atLeast(1).of(entity).getUID();
                will(returnValue("epi:user.guest"));

                allowing(entity).getEntityType();
                will(returnValue(entityType));

                allowing(userGroup).getProperty(AuthenticationConsts.GROUP_TITLE);
                will(returnValue("LOCAL_TEST_GROUP"));
                
                allowing(userGroup2).getProperty(AuthenticationConsts.GROUP_TITLE);
                will(returnValue("LOCAL_TEST_GROUP2"));

                try {
                    Set<UserGroup> set = new HashSet<UserGroup>();
                    set.add(userGroup);
                    set.add(userGroup);
                    allowing(entity).getParents(with(any(EntityType.class)),
                                                with(any(Boolean.class)));
                    will(returnValue(set));
                } catch (EntityPersistenceException ex) {
                    will(throwException(ex));
                }

                allowing(entityType).getID();
                will(returnValue("epi:user.guest"));
            }
        });
        User guestUser = UserProvider.getUser(entity);
        return guestUser;
    }
}
