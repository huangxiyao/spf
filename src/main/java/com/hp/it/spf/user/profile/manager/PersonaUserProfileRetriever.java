/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.user.profile.manager;

import java.util.HashMap;
import java.util.Map;

import com.hp.it.spf.user.exception.UserProfileException;
import com.hp.it.spf.xa.log.portal.Operation;
import com.hp.it.spf.xa.log.portal.TimeRecorder;
import com.hp.it.spf.xa.misc.portal.RequestContext;

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
        // TODO fulfill the Persiona invoking logic    
        Map<String, String> userProfiles = new HashMap<String, String>();
		TimeRecorder timeRecorder = RequestContext.getThreadInstance().getTimeRecorder();
		try {
            timeRecorder.recordStart(Operation.PROFILE_CALL);
            // EUserIdentifierType USER_IDENTIFIER_TYPE =
            // EUserIdentifierType.EXTERNAL_USER;
            // IUser user = userService.createUser(USER_IDENTIFIER_TYPE,
            // userIdentifier);
            // user.getSimpleAttributeValues();
            // user.getCompoundAttributeValues();
            timeRecorder.recordEnd(Operation.PROFILE_CALL);
        } catch (Exception ex) {
            timeRecorder.recordError(Operation.PROFILE_CALL, ex);
            throw new UserProfileException(ex);
        } 
        return userProfiles;         
    }
}