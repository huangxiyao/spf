/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.user.profile.manager;

import java.util.Map;

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
	 * Retrieve specified user profiles 
	 * 
	 * @param userIdentifier  user indentifier
	 * @return user profile map or an empty map
	 * @throws UserProfileException if retrieving user profiles errror
	 */
	Map<String, String> getUserProfile(String profileId) throws UserProfileException;
}
