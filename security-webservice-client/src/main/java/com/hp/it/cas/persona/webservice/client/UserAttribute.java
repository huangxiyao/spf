package com.hp.it.cas.persona.webservice.client;

import com.hp.it.cas.persona.uav.service.IUserAttribute;
import com.hp.it.cas.xa.validate.Verifier;

/**
 * An immutable implementation of a user attribute.
 *
 * @author Quintin May
 */
class UserAttribute implements IUserAttribute {

	private final String compoundUserAttributeIdentifier;
	private final String simpleUserAttributeIdentifier;
	
	/**
	 * Creates a user attribute.
	 * @param compoundUserAttributeIdentifier the compound attribute identifier if this attribute is part of a compound structure. Zero (0) indicates this
	 * is a simple user attribute.
	 * @param simpleUserAttributeIdentifier the simple attribute identifier (the key in a compound structure).
	 */
	UserAttribute(String compoundUserAttributeIdentifier, String simpleUserAttributeIdentifier) {
		new Verifier()
			.isNotNull(simpleUserAttributeIdentifier, "Simple user attribute identifier cannot be null.")
			.throwIfError();
		
		this.compoundUserAttributeIdentifier = compoundUserAttributeIdentifier == null ? "" : compoundUserAttributeIdentifier;
		this.simpleUserAttributeIdentifier = simpleUserAttributeIdentifier;
	}
	
	public String getCompoundUserAttributeIdentifier() {
		return compoundUserAttributeIdentifier;
	}

	public String getSimpleUserAttributeIdentifier() {
		return simpleUserAttributeIdentifier;
	}

	public boolean isCompoundUserAttribute() {
		return ! "".equals(compoundUserAttributeIdentifier);
	}

	public boolean isSimpleUserAttribute() {
		return "".equals(compoundUserAttributeIdentifier);
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((compoundUserAttributeIdentifier == null) ? 0 : compoundUserAttributeIdentifier.hashCode());
        result = prime * result
                + ((simpleUserAttributeIdentifier == null) ? 0 : simpleUserAttributeIdentifier.hashCode());
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
        if (compoundUserAttributeIdentifier == null) {
            if (other.compoundUserAttributeIdentifier != null)
                return false;
        } else if (!compoundUserAttributeIdentifier.equals(other.compoundUserAttributeIdentifier))
            return false;
        if (simpleUserAttributeIdentifier == null) {
            if (other.simpleUserAttributeIdentifier != null)
                return false;
        } else if (!simpleUserAttributeIdentifier.equals(other.simpleUserAttributeIdentifier))
            return false;
        return true;
    }

    public String toString() {
		return isSimpleUserAttribute() ? String.valueOf(simpleUserAttributeIdentifier) : String.format("%s.%s", compoundUserAttributeIdentifier, simpleUserAttributeIdentifier);
	}
}
