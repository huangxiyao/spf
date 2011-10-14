package com.hp.it.cas.persona.user.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.uav.service.IUserAttributeValue;
import com.hp.it.cas.persona.uav.service.IUserAttributeValueService;
import com.hp.it.cas.persona.user.service.ICompoundUserAttributeValue;
import com.hp.it.cas.persona.user.service.IUser;
import com.hp.it.cas.persona.user.service.IUserService;
import com.hp.it.cas.xa.validate.Verifier;

/**
 * A user service implementation based on a user attribute value service. This implementation is optimized for reading. All user attributes are retrieved at
 * once and cached.
 *
 * @author Quintin May
 */
public class UserService implements IUserService {

	private final IUserAttributeValueService userAttributeValueService;
	
	/**
	 * Creates a user service.
	 * @param userAttributeValueService the user attribute value service that manages the user profiles.
	 */
	public UserService(IUserAttributeValueService userAttributeValueService) {
		new Verifier()
			.isNotNull(userAttributeValueService, "User attribute value service cannot be null.")
			.throwIfError();
		
		this.userAttributeValueService = userAttributeValueService;
	}

	public IUser createUser(EUserIdentifierType userIdentifierType, String userIdentifier) {
		new Verifier()
			.isNotNull(userIdentifierType, "User identifier type cannot be null.")
			.isNotNull(userIdentifier, "User identifier cannot be null.")
			.throwIfError();
		
		return new User(this, userIdentifierType, userIdentifier);
	}

	List<UserAttributeValue> findAttributes(User user) {
		List<UserAttributeValue> userAttributeValues = new ArrayList<UserAttributeValue>();
		for (IUserAttributeValue userAttributeValue : userAttributeValueService.findUserAttributeValues(user.getIdentifier())) {
			userAttributeValues.add(new UserAttributeValue(user, userAttributeValue));
		}
		return userAttributeValues;
	}

	ICompoundUserAttributeValue addUserAttributeGroup(User user, String compoundUserAttributeIdentifier) {
		return new CompoundUserAttributeValue(user, compoundUserAttributeIdentifier, UUID.randomUUID().toString());
	}

	void remove(UserAttributeValue userAttributeValue) {
		userAttributeValueService.remove(userAttributeValue);
	}

	void update(UserAttributeValue userAttributeValue, String newValue) {
		userAttributeValueService.putUserAttributeValue(
				userAttributeValue.getUser().getIdentifier(),
				userAttributeValue.getUserAttribute().getCompoundUserAttributeIdentifier(),
				userAttributeValue.getInstanceIdentifier(),
				userAttributeValue.getUserAttribute().getSimpleUserAttributeIdentifier(),
				newValue);
	}

	UserAttributeValue add(User user, String compoundUserAttributeIdentifier, String simpleUserAttributeIdentifier, String instanceIdentifier, String value) {
		return new UserAttributeValue(user,
    		userAttributeValueService.putUserAttributeValue(
    				user.getIdentifier(),
    				compoundUserAttributeIdentifier,
    				instanceIdentifier,
    				simpleUserAttributeIdentifier,
    				value));
	}
}
