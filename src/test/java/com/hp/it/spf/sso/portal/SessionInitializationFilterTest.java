package com.hp.it.spf.sso.portal;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.epicentric.common.website.SessionUtils;
import com.epicentric.user.User;
import com.epicentric.user.UserManager;

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
        
        chain = context.mock(FilterChain.class);
        context.checking(new Expectations() {
            {
                allowing(chain).doFilter(with(any(HttpServletRequest.class)), with(any(HttpServletResponse.class)));
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
