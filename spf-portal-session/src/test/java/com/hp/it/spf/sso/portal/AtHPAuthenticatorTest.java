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

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.api.Expectation;
import org.jmock.integration.junit4.JMock;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.epicentric.common.website.SessionInfo;
import com.epicentric.common.website.SessionUtils;
import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.entity.UniquePropertyValueConflictException;
import com.epicentric.user.User;
import com.epicentric.user.UserManager;
import com.hp.it.spf.user.profile.manager.IUserProfileRetriever;
import com.hp.it.spf.xa.misc.portal.RequestContext;

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

    Mockery aContext = new Mockery();
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
        assertEquals(authenticator.userProfile.size(), 0);
        authenticator.mapHeaderToUserProfileMap();
        assertTrue(authenticator.userProfile.size() > 0);
    }
    
    @Test
    public void thatAtHpAuthenticatorExecutesSuccessfully(){
    	AtHPAuthenticator authenticator = new AtHPAuthenticator(request);
    	HttpSession session = authenticator.request.getSession();
    	SessionInfo info = context.mock(SessionInfo.class);
    	session.setAttribute(SessionInfo.SESSION_INFO_NAME, info);
    	
    	User user = null;
    	User guestUser;
        try {
			user = MockeryUtils.mockUser(context);
	        guestUser = MockeryUtils.mockGuestUser(context);
	        UserManager.createUser(user, guestUser);	        
		} catch (UniquePropertyValueConflictException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EntityPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    	authenticator.execute();
    	if( null != user ) SessionUtils.setCurrentUser(request.getSession(), user);
    	
    	Map<String, Object> profile = authenticator.userProfile;
    	assertTrue(profile.containsKey("UserGroups"));
    	if( RequestContext.getThreadInstance().getDiagnosticContext().hasErrors()){
    		System.out.println(RequestContext.getThreadInstance().getDiagnosticContext().getDataAsString());
    	}
    	assertFalse(RequestContext.getThreadInstance().getDiagnosticContext().hasErrors());
    	assertNotNull(authenticator.getUserName());
    	
    	authenticator.execute();
    	
    	
    	
    }
    
    @Test
    public void thatIsUserRecentUpdatedReturnsTrueOnNewPersonaProfile(){
    	
    	final Map<String, Object>	fakePersonaMap = new HashMap<String, Object>();
    	
    	fakePersonaMap.put(AuthenticationConsts.KEY_LAST_CHANGE_DATE, new Date(System.currentTimeMillis()));
    	final Map<String, Object> fakeDifferentPersonaMap = new HashMap<String, Object>();
    	
    	fakeDifferentPersonaMap.put("TEST", "TEST");
    	
    	final HttpServletRequest aServletRequest = context.mock(HttpServletRequest.class);
    	final HttpSession aMockSession = context.mock(HttpSession.class);
    	Expectations exp = new Expectations();

    	context.checking(new Expectations() {
    		{
    			allowing(aServletRequest).getSession();
    				will(returnValue(aMockSession));
				allowing(aMockSession).getAttribute("SPF_USER_PROFILE");
					will(returnValue(fakeDifferentPersonaMap));
				allowing(aServletRequest).getHeader("SM_UNIVERSALID");
				allowing(aServletRequest).setAttribute(with(any(String.class)), with(any(Object.class)));
    		}
    	});
    	AtHPAuthenticator authenticatorTest = new AtHPAuthenticator(aServletRequest);
    	authenticatorTest.userProfile = fakePersonaMap;
    	assertTrue(authenticatorTest.isUserRecentUpdated() );
    }
    

    
}
