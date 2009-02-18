package com.hp.it.spf.sso.portal;

import static org.junit.Assert.*;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This is the test class for SPFSSOUserNameRetriever class.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 * @see com.hp.it.spf.sso.portal.SPFSSOUserNameRetriever
 */
@RunWith(JMock.class)
public class SPFSSOUserNameRetrieverTest {
    private static Mockery context;

    private static HttpServletRequest request;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        context = MockeryUtils.createMockery();

        request = MockeryUtils.mockHttpServletRequest(context);

        context.checking(new Expectations() {
            {
                // excute 3 times
                // 1. if (request.getAttribute(AuthenticationConsts.SSO_USERNAME) != null) {
                // 2. return (String)request.getAttribute(AuthenticationConsts.SSO_USERNAME);
                // 3. same as the step 1, but this time, sso_username attribute value is null
                exactly(3).of(request)
                          .getAttribute(AuthenticationConsts.SSO_USERNAME);
                will(onConsecutiveCalls(returnValue("username"),
                                        returnValue("username"),
                                        returnValue(null)));
            }
        });
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        context = null;
        request = null;
    }

    @Test
    public void testGetSSOUsername() {
        SPFSSOUserNameRetriever retriever = new SPFSSOUserNameRetriever();
        // username with the value 'username' can be retrieved from request
        String username = retriever.getSSOUsername(request);
        assertEquals("Retrieved user name is not correct.", username, "username");
        
        // No username exist in the request
        username = retriever.getSSOUsername(request);
        assertNull("Retrieved user name should be null", username);
    }

}
