package com.hp.it.spf.user.profile.manager;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.user.exception.UserProfileException;

/**
 * User profile operation interface which defines some methods to interact with
 * UPS webservice
 * 
 * @author  wuyingzh
 * @version 1.0
 */
public interface IUserProfileRetriever {
	/**
	 * Retrieve specified user profile 
	 * 
	 * @param profileId             user profile id
	 * @param request               httpservletrequest contain some metadata
	 * @returnuser                  profile map
	 * @throws UserProfileException user profile exception
	 */
	Map<String, String> getUserProfile(String profileId, HttpServletRequest request) throws UserProfileException;
}
