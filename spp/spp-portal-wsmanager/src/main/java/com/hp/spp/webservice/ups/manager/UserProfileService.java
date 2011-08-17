package com.hp.spp.webservice.ups.manager;

import java.util.Map;

public interface UserProfileService {

	//FIXME (slawek) - refactor the method so it throws exceptions and return user profile map.
	//right now I'm only trying to fit this with minimum effort to the existin implementation.

	/**
	 * Construct argument names and values for WS call an pass the call to UPS.
	 *
	 * @param userProfile
	 *            map contening the user profile
	 * @param queryId
	 *            query ID passed to UPS
	 * @param userId
	 *            id of the user (SM_USERDN)
	 * @param hppId
	 *            hpp id of the user (SM_UNIVERSALID)
	 * @param site
	 *            name of the site accessed
	 * @param sessionToken
	 *            session token of the user
	 * @param adminSessionToken
	 *            session token of the admin used for admin query
	 * @param region
	 *            region of the site (for instance NA for North America)
	 * @param loginType
	 *            Standard or Simulated
	 * @return error code or <tt>null</tt> if no error occured
	 */
	Integer getUserProfile(Map userProfile, String queryId, String userId, String hppId, String site,
						   String sessionToken, String adminSessionToken,
						   String region, String loginType);
}
