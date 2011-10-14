package com.hp.it.cas.persona.uav.service;

import com.hp.it.cas.xa.validate.Verifier;

/**
 * An immutable implementation of a user identifier.
 *
 * @author Quintin May
 */
public class UserIdentifier implements IUserIdentifier {

	private final EUserIdentifierType userIdentifierType;
	private final String userIdentifier;
	
	/**
	 * Creates a user identifier.
	 * @param userIdentifierType the type of identifier.
	 * @param userIdentifier the unique (within the type domain) user identifier.
	 */
	public UserIdentifier(EUserIdentifierType userIdentifierType, String userIdentifier) {
		new Verifier()
			.isNotNull(userIdentifierType, "User identifier type cannot be null.")
			.isNotNull(userIdentifier, "Identifier cannot be null.")
			.throwIfError();

		this.userIdentifierType = userIdentifierType;
		this.userIdentifier = userIdentifier;
	}
	
	public EUserIdentifierType getUserIdentifierType() {
		return userIdentifierType;
	}

	public String getUserIdentifier() {
		return userIdentifier;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userIdentifier == null) ? 0 : userIdentifier.hashCode());
		result = prime * result + ((userIdentifierType == null) ? 0 : userIdentifierType.hashCode());
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
		UserIdentifier other = (UserIdentifier) obj;
		if (userIdentifier == null) {
			if (other.userIdentifier != null)
				return false;
		} else if (!userIdentifier.equals(other.userIdentifier))
			return false;
		if (userIdentifierType == null) {
			if (other.userIdentifierType != null)
				return false;
		} else if (!userIdentifierType.equals(other.userIdentifierType))
			return false;
		return true;
	}

	public String toString() {
		return String.format("%s:%s", userIdentifierType, userIdentifier);
	}
}
