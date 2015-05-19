package com.hp.it.cas.persona.user.service;

import com.hp.it.cas.persona.uav.service.EUserIdentifierType;

/**
 * A user service is the Persona User API entry point for retrieving user profile information. It acts as a factory for {@link IUser Users}.
 * 
 * @author Quintin May
 */
public interface IUserService {

	/**
	 * Factory method for creating (and retrieving) a user.
	 * 
	 * @param userIdentifierType
	 *            the type of user identifier. 
	 * @param userIdentifier
	 *            the unique user identifier within the specified type domain.
	 * @return an existing user or a newly created user having the specified identifier.
	 */
	IUser createUser(EUserIdentifierType userIdentifierType, String userIdentifier);

	// Set<IUser> listAllUsers();
	//	
	// Set<IUser> listUsersHavingSimpleAttribute(int simpleUserAttributeKey);
	// Set<IUser> listUsersHavingCompoundAttribute(int compoundUserAttributeKey);
	// Set<IUser> listUsersHavingAttribute(int compoundUserAttributeKey, int simpleUserAttributeKey);
	//	
	// Set<IUser> listUsersHavingAttributeValue(int simpleUserAttributeKey, String attributeValueText);
	// Set<IUser> listUsersHavingAttributeValue(int compoundUserAttributeKey, int simpleUserAttributeKey, String attributeValueText);
}
