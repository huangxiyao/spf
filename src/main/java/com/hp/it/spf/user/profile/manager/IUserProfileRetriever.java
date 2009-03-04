/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.user.profile.manager;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.user.exception.UserProfileException;

/**
 * User profile operation interface which defines some methods to retrieve user
 * profile.
 * <p>
 * The profile map has the following structure:
 * <ul>
 * <li>Profile: Map</li>
 * <li>Map: (AttributeName : AttributeValue)*</li>
 * <li>AttributeName: String | Integer</li>
 * <li>AttributeValue: String | Map | List</li>
 * <li>List: (AttributeValue)*</li>
 * </ul>
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public interface IUserProfileRetriever {

    /**
     * Retrieve specified user profiles
     * 
     * @param userIdentifier user identifier
     * @param request portal request
     * @return user profile map or an empty map
     * @throws UserProfileException if retrieving user profile error occurs
     */
    Map<Object, Object> getUserProfile(String userIdentifier,
                                       HttpServletRequest request) throws UserProfileException;
}
