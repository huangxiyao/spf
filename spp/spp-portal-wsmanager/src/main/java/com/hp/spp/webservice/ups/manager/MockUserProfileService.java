package com.hp.spp.webservice.ups.manager;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

import com.epicentric.user.User;
import com.epicentric.user.UserManager;
import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.entity.EntityNotFoundException;
import com.hp.spp.profile.Constants;

/**
 * Retrieves user profile from the file located at /[site name]_[user name]_MockProfile.properties.
 * Each propery is set as en entry in the user profile. The file must be available in the classpath,
 * e.g. /WEB-INF/classes/sppqa_sppuser_MockProfile.properties
 */
public class MockUserProfileService implements UserProfileService {

	private static final Logger mLog = Logger.getLogger(UserProfileService.class);

	@SuppressWarnings("unchecked")
	public Integer getUserProfile(Map userProfile, String queryId,
			String userId, String hppId, String site, String sessionToken,
			String adminSessionToken, String region, String loginType) {
		// when called in simulation mode, userId is null. If this is the case let's try to get it from
		// database.
		if (userId == null) {
			userId = getUserNameFromPortalDatabase(hppId);
		}

		String mockProfileResourcePath = "/" + site + "_" + userId + "_MockProfile.properties";
		if (mLog.isDebugEnabled()) {
			mLog.debug("Reading mock profile from the resource '" + mockProfileResourcePath + "'");
		}
		InputStream is = getClass().getResourceAsStream(mockProfileResourcePath);
		if (is == null) {
			mLog.error("Unable to locate mock profile resource '" + mockProfileResourcePath + "'");
			return new Integer(0);
		}

		try {
			try {
				Properties props = new Properties();
				props.load(is);
				userProfile.putAll(props);
				userProfile.put(Constants.MAP_HPPID, hppId);
				userProfile.put(Constants.MAP_USERNAME, userId);
			}
			finally {
				is.close();
			}
		}
		catch (IOException e) {
			mLog.error("Error reading mock profile file '" + mockProfileResourcePath + "'", e);
			return new Integer(0);
		}

		// no error
		return null;
	}

	
	private String getUserNameFromPortalDatabase(String hppId) {
		String userId;
		try {
			User user = UserManager.getInstance().getUser(Constants.VGN_HPPID, hppId);
			userId = (String) user.getProperty(Constants.VGN_USERNAME);
		}
		catch (EntityPersistenceException e) {
			throw new RuntimeException("Unable to retrieve user from Vignette database", e);
		}
		catch (EntityNotFoundException e) {
			throw new IllegalStateException("User not found for HPPID: " + hppId, e);
		}
		return userId;
	}
}
