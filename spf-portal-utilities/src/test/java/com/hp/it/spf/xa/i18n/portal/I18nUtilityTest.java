/**
 * 
 */
package com.hp.it.spf.xa.i18n.portal;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;

import com.epicentric.common.website.SessionInfo;
import com.epicentric.common.website.SessionUtils;
import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.entity.EntityType;
import com.epicentric.entity.UniquePropertyValueConflictException;
import com.epicentric.entity.implementations.generic.ChildEntityWrapper;
import com.epicentric.user.User;
import com.epicentric.user.UserGroup;
import com.epicentric.user.UserManager;
import com.epicentric.user.UserProvider;
import com.hp.it.spf.xa.mock.portal.MockUser;

import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalRequest;

/**
 * @author sebestyj
 *
 */
public class I18nUtilityTest {
	
	Mockery context = new JUnit4Mockery(){{
		setImposteriser(ClassImposteriser.INSTANCE);
	}};
	
	PortalContext mockPortalContext;
	PortalRequest mockPortalRequest;
	HttpServletRequest mockServletRequest;
	HttpSession mockHttpSession;
	
	SessionInfo info;
	User user;
	
	@Before
	public void setUp() throws Exception {
		mockPortalContext = context.mock(PortalContext.class);
		mockPortalRequest = context.mock(PortalRequest.class);
		mockServletRequest = context.mock(HttpServletRequest.class);
		mockHttpSession = context.mock(HttpSession.class);
		user = mockUser(context);

	}

	/**
	 * Test method for {@link com.hp.it.spf.xa.i18n.portal.I18nUtility#getValue(java.lang.String, java.lang.String, java.lang.String, com.vignette.portal.website.enduser.PortalContext, java.lang.Object[], com.hp.it.spf.xa.help.portal.GlobalHelpProvider[], com.hp.it.spf.xa.help.ContextualHelpProvider[], boolean, boolean)}.
	 */
	@Test
	public void testGetValueStringStringStringPortalContextObjectArrayGlobalHelpProviderArrayContextualHelpProviderArrayBooleanBoolean() {
		
		context.checking(new Expectations(){{
			exactly(1).of(mockPortalContext).getPortalRequest();
				will(returnValue(mockPortalRequest));
			exactly(1).of(mockPortalRequest).getRequest();
				will(returnValue(mockServletRequest));
			exactly(1).of(mockServletRequest).getSession();
				will(returnValue(mockHttpSession));
			exactly(1).of(mockHttpSession).getAttribute("epicentric_session_info");
			exactly(1).of(mockServletRequest).getPathInfo();
			exactly(1).of(mockServletRequest).getServletPath();
		}});
		
		SessionUtils.setCurrentUser(mockHttpSession, user);
		String returnValue = I18nUtility.getValue("componentid", "key", "default value", mockPortalContext, null, null, null, true, false);
		assertNotNull(returnValue);
		assertEquals("Test &amp; Test", returnValue);
		System.out.println(returnValue);
	}
	
	@Test
	public void thatGetValueReturnsSameContentIfNoEscapableCharactersPresent(){
		context.checking(new Expectations(){{
			exactly(1).of(mockPortalContext).getPortalRequest();
				will(returnValue(mockPortalRequest));
			exactly(1).of(mockPortalRequest).getRequest();
				will(returnValue(mockServletRequest));
			exactly(1).of(mockServletRequest).getSession();
				will(returnValue(mockHttpSession));
			exactly(1).of(mockHttpSession).getAttribute("epicentric_session_info");
			exactly(1).of(mockServletRequest).getPathInfo();
			exactly(1).of(mockServletRequest).getServletPath();
		}});
		
		SessionUtils.setCurrentUser(mockHttpSession, user);
		String returnValue = I18nUtility.getValue("componentid", "clean_key", "default value", mockPortalContext, null, null, null, true, false);
		assertNotNull(returnValue);
		assertEquals("Test and Test", returnValue);
		System.out.println(returnValue);		
	}
	@Test
	public void thatGetValueReturnsEscapedLessThanSymbol(){
		context.checking(new Expectations(){{
			exactly(1).of(mockPortalContext).getPortalRequest();
				will(returnValue(mockPortalRequest));
			exactly(1).of(mockPortalRequest).getRequest();
				will(returnValue(mockServletRequest));
			exactly(1).of(mockServletRequest).getSession();
				will(returnValue(mockHttpSession));
			exactly(1).of(mockHttpSession).getAttribute("epicentric_session_info");
			exactly(1).of(mockServletRequest).getPathInfo();
			exactly(1).of(mockServletRequest).getServletPath();
		}});
		
		SessionUtils.setCurrentUser(mockHttpSession, user);
		String returnValue = I18nUtility.getValue("componentid", "lt_key", "default value", mockPortalContext, null, null, null, true, false);
		assertNotNull(returnValue);
		assertEquals("Test &lt; Test", returnValue);
		System.out.println(returnValue);		
	}
	
    /**
     * Create Vignette user mock object and set expectations to it.
     * 
     * @param context mockery context
     * @return mocked User object
     * @throws EntityPersistenceException 
     * @throws UniquePropertyValueConflictException 
     * @see com.epicentric.user.User
     */
    public static User mockUser(Mockery context) throws UniquePropertyValueConflictException, EntityPersistenceException {
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

//                allowing(userGroup).getProperty(AuthenticationConsts.GROUP_TITLE);
//                will(returnValue("LOCAL_TEST_GROUP"));
//                
//                allowing(userGroup2).getProperty(AuthenticationConsts.GROUP_TITLE);
//                will(returnValue("LOCAL_PORTAL_LANG_EN"));

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

//                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_PROFILE_ID);
//                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_PROFILE_ID)));
//
//                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_USER_NAME_ID);
//                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_USER_NAME)));
//
//                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_EMAIL_ID);
//                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_EMAIL)));
//
//                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_FIRSTNAME_ID);
//                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_FIRST_NAME)));
//
//                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_LASTNAME_ID);
//                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_LAST_NAME)));
//
//                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_COUNTRY_ID);
//                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_COUNTRY)));
//
//                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_LANGUAGE_ID);
//                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_LANGUAGE)));
//
//                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_LAST_CHANGE_DATE_ID);
//                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_LAST_CHANGE_DATE)));
//
//                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_SPF_TIMEZONE_ID);
//                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_TIMEZONE)));
//                
//                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_PRIMARY_SITE_ID);
//                will(returnValue(UserProfile.get(AuthenticationConsts.PROPERTY_PRIMARY_SITE_ID)));
//                
//                allowing(entity).getProperty(AuthenticationConsts.PROPERTY_LAST_LOGIN_DATE_ID);
//                will(returnValue(UserProfile.get(AuthenticationConsts.KEY_LAST_LOGIN_DATE)));
//                
                allowing(entity).setProperty(with(any(String.class)), with(any(Object.class)));
                
                allowing(entity).save();
            }
        });
        User user = UserProvider.getUser(entity);
        return user;
    }


}
