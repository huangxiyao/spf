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
import com.hp.it.spf.user.exception.UserProfileException;

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
    public Map<String, Object> getUserProfile(String userIdentifier,
                                              HttpServletRequest request) throws UserProfileException {
        Map<String, Object> userProfiles = new HashMap<String, Object>();
        try {
            // get the user service from the request
            IUserService userService = getUserServiceFromRequest(request);

            // get the user from the service
			IUser user = getUserFromService(userIdentifier, request, userService);

            // retrieve simple attribute values and convert key from Integer type to String type
            Map<Integer, Collection<String>> simple = user.getSimpleAttributeValues();
			covertSimpleValue(userProfiles, simple);

			// retrieve compound attribute values and convert key from Integer type to String type
            Map<Integer, Collection<ICompoundUserAttributeValue>> compound = user.getCompoundAttributeValues();
			convertCompoundValue(userProfiles, compound);
		} catch (Exception ex) {
            throw new UserProfileException("Retrieve user profile from Persona error.", ex);
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


	private void convertCompoundValue(Map<String, Object> userProfiles, Map<Integer, Collection<ICompoundUserAttributeValue>> compound)
	{
		for (Map.Entry<Integer, Collection<ICompoundUserAttributeValue>> entry: compound.entrySet()) {

			String attributeName = String.valueOf(entry.getKey());
			Collection<ICompoundUserAttributeValue> origianlValue = entry.getValue();

			List<Map<String, String>> attributeValue = new ArrayList<Map<String, String>>();
			for (ICompoundUserAttributeValue compoundUserAttributeValue : origianlValue) {

				// convert the Map<Integer, String> to Map<String, String>
				Map<String, String> attributeValueItem = new LinkedHashMap<String, String>();
				for (Map.Entry<Integer, String> compuoundAttributeValueItem : compoundUserAttributeValue.entrySet()) {
					attributeValueItem.put(
							String.valueOf(compuoundAttributeValueItem.getKey()),
							compuoundAttributeValueItem.getValue());
				}
				attributeValue.add(attributeValueItem);
			}

			userProfiles.put(attributeName, attributeValue);
		}
	}


	private void covertSimpleValue(Map<String, Object> userProfiles, Map<Integer, Collection<String>> simple)
	{
		for (Map.Entry<Integer, Collection<String>> entry: simple.entrySet()) {
			String attributeName = String.valueOf(entry.getKey());
			Collection<String> originalValue = entry.getValue();
			// convert the Collection to List in case it's not a list
			List<String> attributeValue = new ArrayList<String>(originalValue);
			userProfiles.put(attributeName, attributeValue);
		}
	}
}