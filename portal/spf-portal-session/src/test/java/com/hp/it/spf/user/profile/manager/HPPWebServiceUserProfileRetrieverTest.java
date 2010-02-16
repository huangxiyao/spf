package com.hp.it.spf.user.profile.manager;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;

import com.hp.globalops.hppcbl.passport.PassportService;
import com.hp.globalops.hppcbl.passport.PassportServiceException;
import com.hp.globalops.hppcbl.passport.manager.PassportParametersManager;

@RunWith(JMock.class)
public class HPPWebServiceUserProfileRetrieverTest {
	
	Mockery context = new JUnit4Mockery(){{
		setImposteriser(ClassImposteriser.INSTANCE);
	}};

	private PassportService passportService;
	private PassportParametersManager parametersManager;
	private HttpServletRequest request;
	private final String adminUsername = "hpp_groupuser";
	private final String adminPassword = "password123";
	private final String securityToken = "11111111111";
	
	HPPWebServiceUserProfileRetriever userProfileRetriever = new HPPWebServiceUserProfileRetriever();
	
	@Before
	public void setUp() throws Exception {
		passportService = context.mock(PassportService.class);
		parametersManager = context.mock(PassportParametersManager.class);
		request = new MockHttpServletRequest();
	}
	
	@Test
	public void testGetUserProfile() throws PassportServiceException {
		context.checking(new Expectations() {{
			allowing(passportService).setVersion(with("2"));

			allowing(parametersManager).getAdminUser();
				will(returnValue(adminUsername));

			allowing(parametersManager).getAdminPassword();
				will(returnValue(adminPassword));

			allowing(passportService).login(adminUsername, adminPassword).getSessionToken();
				will(returnValue(securityToken));
				
		}});
	
		final String userId = "hpptest1";
		Map<String, Object> userProfile = userProfileRetriever.getUserProfile(userId, request);
		assertTrue(userProfile != null && userProfile.entrySet().size() > 0);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testGetUserProfile_UserIdRequiredLogic() {
		context.checking(new Expectations() {{
		}});
		
		final String userId = null;
		Map<String, Object> userProfile = userProfileRetriever.getUserProfile(userId, request);
		assert(userProfile.entrySet().size() == 0);
	}
	
}
