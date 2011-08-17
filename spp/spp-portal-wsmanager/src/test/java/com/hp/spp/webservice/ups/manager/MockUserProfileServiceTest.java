package com.hp.spp.webservice.ups.manager;

import junit.framework.TestCase;

import java.util.Map;
import java.util.HashMap;

import com.hp.spp.profile.Constants;

public class MockUserProfileServiceTest extends TestCase {

	public void testRetrieveMockProfile() throws Exception {
		Map userProfile = new HashMap();
		MockUserProfileService mockUps = new MockUserProfileService();

		Integer result = mockUps.getUserProfile(userProfile, null, "user1", "hppidXYZ", "NON_EXISTING_SITE", null, null, null, null);
		assertNotNull("Error occured as mock profile file does not exist", result);

		// test user-specific profile specified in /test_user3_MockProfile.properties
		userProfile = new HashMap();
		result = mockUps.getUserProfile(userProfile, null, "user3", "hppidZZZ999ZZZ", "test", null, null, null, null);
		assertNull("No error occured", result);
		assertTrue("Profile read", !userProfile.isEmpty());
		assertEquals("HPPUserId set", "hppidZZZ999ZZZ", userProfile.get(Constants.MAP_HPPID));
		assertEquals("User name set", "user3", userProfile.get(Constants.MAP_USERNAME));
		assertEquals("Last name", "Profile3", userProfile.get(Constants.MAP_LASTNAME));
	}

}
