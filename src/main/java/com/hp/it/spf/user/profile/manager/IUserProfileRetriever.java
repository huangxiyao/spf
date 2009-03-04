/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.user.profile.manager;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.spf.user.exception.UserProfileException;

/**
 * User profile operation interface which defines some methods to retrieve user
 * profile.
 * 
 * @author wuyingzh
 * @version 1.0
 */
public interface IUserProfileRetriever {
    /**
     * Retrieve specified user profiles
     * 
     * @param EUserIdentifierType user type
     * @param userIdentifier user indentifier
     * @param request HttpServletRequest
     * @return user profile map or an empty map
     * @throws UserProfileException if retrieving user profiles errror
     */
    Map<String, String> getUserProfile(EUserIdentifierType userIdentifierType,
                                       String profileId,
                                       HttpServletRequest request) throws UserProfileException;
}
