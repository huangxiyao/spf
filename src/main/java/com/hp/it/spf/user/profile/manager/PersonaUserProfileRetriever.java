/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.user.profile.manager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.user.service.IUser;
import com.hp.it.cas.persona.user.service.IUserService;
import com.hp.it.spf.persona.PersonaUserServiceFilter;
import com.hp.it.spf.user.exception.UserProfileException;
import com.hp.it.spf.xa.dc.portal.ErrorCode;
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
    /**
     * Retrieve user profiles from Persona according to the specified user identifier.
     *  
     * @param EUserIdentifierType user type
     * @param userIdentifier user identifier
     * @param request HttpServletRequest 
     * @return user profile map or an empty map
     * @throws UserProfileException if invoke Persona error
     */
    public Map<String, String> getUserProfile(EUserIdentifierType userIdentifierType, String userIdentifier, HttpServletRequest request) throws UserProfileException {
        // TODO fulfill the Persiona invoking logic    
        Map<String, String> userProfiles = new HashMap<String, String>();
		TimeRecorder timeRecorder = RequestContext.getThreadInstance().getTimeRecorder();
		try {
            timeRecorder.recordStart(Operation.PROFILE_CALL);
            // get the user service from the request
            IUserService userService = PersonaUserServiceFilter.getUserService(request);            
            // get the user from the service
            IUser user = userService.createUser(userIdentifierType, userIdentifier);
            
            Map simple = user.getSimpleAttributeValues();
            Map compound = user.getCompoundAttributeValues();
            timeRecorder.recordEnd(Operation.PROFILE_CALL);
        } catch (Exception ex) {
            timeRecorder.recordError(Operation.PROFILE_CALL, ex);
			RequestContext.getThreadInstance().getDiagnosticContext().setError(ErrorCode.PROFILE001, ex.toString());
            throw new UserProfileException(ex);
        } 
        return userProfiles;         
    }
}