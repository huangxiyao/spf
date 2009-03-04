package com.hp.it.spf.sso.portal;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.epicentric.user.User;
import com.epicentric.user.UserManager;
import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.user.service.IUserService;
import com.hp.it.spf.persona.PersonaUserServiceFilter;

public class SessionInitializationFilterTest {
    private static Mockery context;

    private static HttpServletRequest athpRequest;
    
    private static HttpServletRequest hppRequest;
    
    private static HttpServletRequest fedRequest;
    
    private static HttpServletRequest anonRequest;
    
    private static HttpServletResponse response;
    
    private static FilterChain chain;

    private static User user;

    private static User guestUser;
    
    private static IUserService userService;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        context = MockeryUtils.createMockery();
        athpRequest = MockeryUtils.mockHttpServletRequestForAtHP(context);
        hppRequest = MockeryUtils.mockHttpServletRequestForHPP(context);
        fedRequest = MockeryUtils.mockHttpServletRequestForFed(context);
        anonRequest = MockeryUtils.mockHttpServletRequestForANON(context);
        response = MockeryUtils.mockHttpServletResponse(context);
        user = MockeryUtils.mockUser(context);
        guestUser = MockeryUtils.mockGuestUser(context);
        userService = context.mock(IUserService.class);
        
        chain = context.mock(FilterChain.class);
        context.checking(new Expectations() {
            {
                allowing(chain).doFilter(with(any(HttpServletRequest.class)), with(any(HttpServletResponse.class)));
                
                allowing(athpRequest).getAttribute(PersonaUserServiceFilter.class.getName() + ".USER_SERVICE");
                will(returnValue(userService));
                allowing(hppRequest).getAttribute(PersonaUserServiceFilter.class.getName() + ".USER_SERVICE");
                will(returnValue(userService));
                allowing(fedRequest).getAttribute(PersonaUserServiceFilter.class.getName() + ".USER_SERVICE");
                will(returnValue(userService));
                
                allowing(userService).createUser(with(any(EUserIdentifierType.class)), with(any(String.class)));
            }
        });
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        context = null;
        athpRequest = null;
        hppRequest = null;
        fedRequest = null;
        anonRequest = null;
        response = null;
        chain = null;
        user = null;
        guestUser = null;       
        userService = null;
        UserManager.cleanup();
    }

    @Test
    public void testInit() {
        assertTrue(true);
    }

    @Test
    public void testDestroy() {
        assertTrue(true);
    }

    @Test
    public void testDoFilter() throws IOException, ServletException {
        UserManager.createUser(user, guestUser);
        SessionInitializationFilter filter = new SessionInitializationFilter();
        // for @HP
        filter.doFilter(athpRequest, response, chain);        
        
        // for HPP
        filter.doFilter(hppRequest, response, chain);
        
        // for Fed
        filter.doFilter(fedRequest, response, chain);
        
        // for ANON
        filter.doFilter(anonRequest, response, chain);
    }

}
