package com.hp.it.cas.persona.configuration.service.impl;

import com.hp.it.cas.persona.configuration.service.IUserAttribute;

/**
 * A basic user attribute implementation.
 *
 * @author Quintin May
 */
class UserAttribute implements IUserAttribute {

	private final String userAttributeIdentifier;
	private final boolean isCompoundAttribute;
	private String userAttributeDefinition;
	private String userAttributeDescription;
	private String userAttributeName;
	
	UserAttribute(String userAttributeIdentifier, boolean isCompoundAttribute) {
		this.userAttributeIdentifier = userAttributeIdentifier;
		this.isCompoundAttribute = isCompoundAttribute;
	}
	
	public String getUserAttributeIdentifier() {
		return userAttributeIdentifier;
	}

	public boolean isCompoundUserAttribute() {
		return isCompoundAttribute;
	}

	public boolean isSimpleUserAttribute() {
		return ! isCompoundAttribute;
	}

	public String getUserAttributeDefinition() {
		return userAttributeDefinition;
	}

	public String getUserAttributeDescription() {
		return userAttributeDescription;
	}

	public String getUserAttributeName() {
		return userAttributeName;
	}

	public void setUserAttributeDefinition(String userAttributeDefinition) {
		this.userAttributeDefinition = userAttributeDefinition;
	}

	public void setUserAttributeDescription(String userAttributeDescription) {
		this.userAttributeDescription = userAttributeDescription;
	}

	public void setUserAttributeName(String userAttributeName) {
		this.userAttributeName = userAttributeName;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((userAttributeIdentifier == null) ? 0 : userAttributeIdentifier.hashCode());
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
        UserAttribute other = (UserAttribute) obj;
        if (userAttributeIdentifier == null) {
            if (other.userAttributeIdentifier != null)
                return false;
        } else if (!userAttributeIdentifier.equals(other.userAttributeIdentifier))
            return false;
        return true;
    }

    public String toString() {
		return String.format("%s[%s: %s]", getClass().getSimpleName(), userAttributeIdentifier, userAttributeName);
	}
}
