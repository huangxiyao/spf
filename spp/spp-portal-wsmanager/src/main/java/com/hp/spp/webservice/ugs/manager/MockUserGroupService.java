package com.hp.spp.webservice.ugs.manager;

import com.hp.spp.profile.Constants;

import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Instead of calling a web service returns directly the user groups that are passed as part of
 * <tt>userProfile</tt>.
 */
public class MockUserGroupService implements UserGroupService {

	private static final Logger mLog = Logger.getLogger(MockUserGroupService.class);

	/**
	 * @param siteName name of the site
	 * @param userProfile mock user profile
	 * @return a user profile value present as String under {@link Constants.MAP_USERGROUPS} attribute;
	 * note that the string is comma-separated and is split before being returned.
	 */
	public String[] getUserGroups(String siteName, Map userProfile) {
		String userGroups = (String) userProfile.get(Constants.MAP_USERGROUPS);
		if (userGroups == null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug(Constants.MAP_USERGROUPS + " profile attribute is not present in the profile for site '" +
						siteName + "' and user '" + userProfile.get(Constants.MAP_USERNAME) + "'.");
			}
			return new String[0];
		}
		return userGroups.split("\\s*(;|,)\\s*");
	}

}
