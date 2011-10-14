package com.hp.it.cas.persona.uav.service.impl;

import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.uav.service.IUserAttribute;
import com.hp.it.cas.persona.uav.service.IUserAttributeValue;
import com.hp.it.cas.persona.uav.service.IUserIdentifier;
import com.hp.it.cas.persona.uav.service.UserIdentifier;
import com.hp.it.cas.security.dao.UserAttrValu;

class UserAttributeValue implements IUserAttributeValue {

	private final UserAttrValu userAttrValu;
	private final UserAttribute userAttribute;
	private final UserIdentifier userIdentifier;
	
	UserAttributeValue(UserAttrValu userAttrValu) {
		this.userAttrValu = userAttrValu;
		this.userAttribute = new UserAttribute(userAttrValu.getKey().getCmpndUserAttrId(), userAttrValu.getKey().getSmplUserAttrId());
		this.userIdentifier = new UserIdentifier(
				EUserIdentifierType.valueOfUserIdentifierTypeCode(userAttrValu.getKey().getUserIdTypeCd()),
				userAttrValu.getKey().getUserId());
	}
	
	public String getInstanceIdentifier() {
		return userAttrValu.getKey().getUserAttrInstncId();
	}

	public IUserAttribute getUserAttribute() {
		return userAttribute;
	}

	public IUserIdentifier getUserIdentifier() {
		return userIdentifier;
	}

	public String getValue() {
		return userAttrValu.getUserAttrValuTx();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userAttrValu == null) ? 0 : userAttrValu.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserAttributeValue other = (UserAttributeValue) obj;
		if (userAttrValu == null) {
			if (other.userAttrValu != null)
				return false;
		} else if (!userAttrValu.equals(other.userAttrValu))
			return false;
		return true;
	}
	
	public String toString() {
		return String.format("%s[%s, %s, %s, %s]", getClass().getSimpleName(), userIdentifier, userAttribute, getInstanceIdentifier(), getValue());
	}
}
