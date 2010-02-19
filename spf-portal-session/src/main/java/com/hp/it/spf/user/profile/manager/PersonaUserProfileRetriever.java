/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.user.profile.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.user.service.ICompoundUserAttributeValue;
import com.hp.it.cas.persona.user.service.IUser;
import com.hp.it.cas.persona.user.service.IUserService;
import com.hp.it.spf.sso.portal.AuthenticationConsts;
import com.hp.it.spf.sso.portal.AuthenticatorHelper;
import com.hp.it.spf.user.exception.UserProfileException;
import com.hp.it.spf.user.group.manager.UGSUserGroupRetriever;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;

/**
 * This is a concrete class of IUserProfileRetriever interface.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class PersonaUserProfileRetriever implements IUserProfileRetriever {
    private static final LogWrapper LOG = AuthenticatorHelper.getLog(PersonaUserProfileRetriever.class);

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
    public Map<String, Object> getUserProfile(String userIdentifier,
                                              HttpServletRequest request) throws UserProfileException {
        Map<String, Object> userProfiles = new HashMap<String, Object>();
        try {
            // get the user service from the request
            IUserService userService = getUserServiceFromRequest(request);

            // get the user from the service
			IUser user = getUserFromService(userIdentifier, request, userService);

            // retrieve simple attribute values and convert key from Integer type to String type
            Map<String, Collection<String>> simple = user.getSimpleAttributeValues();
            
			convertSimpleValues(userProfiles, simple);

			// retrieve compound attribute values and convert key from Integer type to String type
            Map<String, Collection<ICompoundUserAttributeValue>> compound = user.getCompoundAttributeValues();
			convertCompoundValues(userProfiles, compound);
		} catch (Exception ex) {
            throw new UserProfileException("Retrieve user profile from Persona error." + ex.getMessage(), ex);
        }
        return userProfiles;
    }

	private IUser getUserFromService(String userIdentifier, HttpServletRequest request, IUserService userService)
	{
		EUserIdentifierType userIdentifierType =
				(EUserIdentifierType)request.getAttribute(AuthenticationConsts.USER_IDENTIFIER_TYPE);
		return userService.createUser(userIdentifierType, userIdentifier);
	}

	private IUserService getUserServiceFromRequest(HttpServletRequest request)
	{
		return (IUserService) WebApplicationContextUtils.getWebApplicationContext(
				request.getSession(true).getServletContext()).
				getBean("standaloneUserService");
	}


	void convertCompoundValues(Map<String, Object> userProfile, Map<String, Collection<ICompoundUserAttributeValue>> compoundValues)
	{
		for (Map.Entry<String, Collection<ICompoundUserAttributeValue>> entry: compoundValues.entrySet()) {

			String attributeName = entry.getKey();
			Collection<ICompoundUserAttributeValue> origianlValue = entry.getValue();

			List<Map<String, String>> attributeValue = new ArrayList<Map<String, String>>();
			for (ICompoundUserAttributeValue compoundUserAttributeValue : origianlValue) {

				// convert the Map<String, String> to Map<String, String>
				Map<String, String> attributeValueItem = new LinkedHashMap<String, String>();
				for (Map.Entry<String, String> compuoundAttributeValueItem : compoundUserAttributeValue.entrySet()) {
					attributeValueItem.put(
							compuoundAttributeValueItem.getKey(),
							compuoundAttributeValueItem.getValue());
				}
				attributeValue.add(attributeValueItem);
			}

			userProfile.put(attributeName, attributeValue);
		}
	}


	void convertSimpleValues(Map<String, Object> userProfile, Map<String, Collection<String>> simpleValues)
	{
		for (Map.Entry<String, Collection<String>> entry: simpleValues.entrySet()) {
			String attributeName = entry.getKey();
			Collection<String> originalValue = entry.getValue();
			// convert the Collection to List in case it's not a list
			if (originalValue != null) {
				List<String> attributeValue = new ArrayList<String>(originalValue);
	            if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
	            	if( null != attributeValue ){
		            	for (String s : attributeValue){
		            		LOG.debug("Persona Attribute:  " + s);
		            	}
	            	}else{
	            		LOG.debug("convertSimpleValues:  Somehow unable to convert persona attribute to arraylist");
	            	}
	            }
				userProfile.put(attributeName, attributeValue);
			}
			else {
				userProfile.put(attributeName, null);
			}
		}
	}
}