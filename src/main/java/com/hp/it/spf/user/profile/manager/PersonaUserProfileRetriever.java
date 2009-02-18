/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.user.profile.manager;

import java.util.HashMap;
import java.util.Map;

import com.hp.it.spf.user.exception.UserProfileException;

/**
 * This is a concrete class of IUserProfileRetriever interface.
 * 
 * @author wuyingzh
 * @version 1.0
 */
public class PersonaUserProfileRetriever implements IUserProfileRetriever {

//    private IUserService userService;

    /**
     * Retrieve user profiles from Persona according to the specified user identifier. 
     * 
     * @param userIdentifier user identifier
     * @return user profile map or an empty map
     * @throws UserProfileException if invoke Persona error
     */
    public Map<String, String> getUserProfile(String userIdentifier) throws UserProfileException {
        // TODO fulfill this logic
        // EUserIdentifierType USER_IDENTIFIER_TYPE =
        // EUserIdentifierType.EXTERNAL_USER;
        // IUser user = userService.createUser(USER_IDENTIFIER_TYPE,
        // userIdentifier);
        // user.getSimpleAttributeValues();
        // user.getCompoundAttributeValues();
        return new HashMap<String, String>();
    }
}