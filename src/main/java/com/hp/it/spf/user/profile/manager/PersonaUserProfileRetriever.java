/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.user.profile.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.user.service.ICompoundUserAttributeValue;
import com.hp.it.cas.persona.user.service.IUser;
import com.hp.it.cas.persona.user.service.IUserService;
import com.hp.it.spf.sso.portal.AuthenticationConsts;
import com.hp.it.spf.user.exception.UserProfileException;
import com.hp.it.spf.xa.dc.portal.ErrorCode;
import com.hp.it.spf.xa.log.portal.Operation;
import com.hp.it.spf.xa.log.portal.TimeRecorder;
import com.hp.it.spf.xa.misc.portal.RequestContext;

/**
 * This is a concrete class of IUserProfileRetriever interface.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class PersonaUserProfileRetriever implements IUserProfileRetriever {
    /**
     * Retrieve user profiles from Persona according to the specified user
     * identifier.
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
     * @param userIdentifier user identifier
     * @param request HttpServletRequest
     * @return user profile map or an empty map
     * @throws UserProfileException if invoke Persona error
     */
    public Map<Object, Object> getUserProfile(String userIdentifier,
                                              HttpServletRequest request) throws UserProfileException {
        // TODO fulfill the Persiona invoking logic
        Map<Object, Object> userProfiles = new HashMap<Object, Object>();
        TimeRecorder timeRecorder = RequestContext.getThreadInstance()
                                                  .getTimeRecorder();
        try {
            timeRecorder.recordStart(Operation.PROFILE_CALL);
            // get the user service from the request
            IUserService userService = (IUserService)WebApplicationContextUtils.getWebApplicationContext(request.getSession(true)
                                                                                                                .getServletContext())
                                                                               .getBean("standaloneUserService");

            // get the user from the service
            EUserIdentifierType userIdentifierType = (EUserIdentifierType)request.getAttribute(AuthenticationConsts.USER_IDENTIFIER_TYPE);
            IUser user = userService.createUser(userIdentifierType,
                                                userIdentifier);

            Map<Integer, Collection<String>> simple = user.getSimpleAttributeValues();
            userProfiles.putAll(simple);

            Map<Integer, Collection<ICompoundUserAttributeValue>> compound = user.getCompoundAttributeValues();
            userProfiles.putAll(compound);

            timeRecorder.recordEnd(Operation.PROFILE_CALL);
        } catch (Exception ex) {
            timeRecorder.recordError(Operation.PROFILE_CALL, ex);
            RequestContext.getThreadInstance()
                          .getDiagnosticContext()
                          .setError(ErrorCode.PROFILE001, ex.toString());
            throw new UserProfileException(ex);
        }
        return userProfiles;
    }
}