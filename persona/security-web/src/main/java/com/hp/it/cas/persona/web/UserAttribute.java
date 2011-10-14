package com.hp.it.cas.persona.web;

import com.hp.it.cas.persona.uav.service.IUserAttributeValue;

public class UserAttribute implements Comparable<UserAttribute> {
	private String userIdentifier;
	private String compoundUserAttributeIdentifier;
	private String simpleUserAttributeIdentifier;
	private String compoundUserAttributeName;
	private String simpleUserAttributeName;
	private String instanceIdentifier;
	private String value;

	/**
	 * proxy object for user attribute output
	 * 
	 * @param compoundUserAttributeName
	 * @param simpleUserAttributeName
	 * @param userAttributeValue
	 */
	public UserAttribute(String compoundUserAttributeName,
			String simpleUserAttributeName,
			IUserAttributeValue userAttributeValue) {
		this.compoundUserAttributeName = compoundUserAttributeName == null ? ""
				: compoundUserAttributeName;
		this.simpleUserAttributeName = simpleUserAttributeName == null ? ""
				: simpleUserAttributeName;
		if (userAttributeValue != null) {
			this.userIdentifier = userAttributeValue.getInstanceIdentifier();
			this.compoundUserAttributeIdentifier = userAttributeValue
					.getUserAttribute().getCompoundUserAttributeIdentifier();
			this.simpleUserAttributeIdentifier = userAttributeValue.getUserAttribute()
					.getSimpleUserAttributeIdentifier();
			this.instanceIdentifier = userAttributeValue
					.getInstanceIdentifier();
			this.value = userAttributeValue.getValue();
		}
	}

	public String getUserIdentifier() {
		return userIdentifier;
	}

	public void setUserIdentifier(String userIdentifier) {
		this.userIdentifier = userIdentifier;
	}

	public String getCompoundUserAttributeIdentifier() {
		return compoundUserAttributeIdentifier;
	}

	public void setCompoundUserAttributeIdentifier(String compoundUserAttributeKey) {
		this.compoundUserAttributeIdentifier = compoundUserAttributeKey;
	}

	public String getSimpleUserAttributeIdentifier() {
		return simpleUserAttributeIdentifier;
	}

	public void setSimpleUserAttributeIdentifier(String simpleUserAttributeKey) {
		this.simpleUserAttributeIdentifier = simpleUserAttributeKey;
	}

	public String getCompoundUserAttributeName() {
		return compoundUserAttributeName;
	}

	public void setCompoundUserAttributeName(String compoundUserAttributeName) {
		this.compoundUserAttributeName = compoundUserAttributeName;
	}

	public String getSimpleUserAttributeName() {
		return simpleUserAttributeName;
	}

	public void setSimpleUserAttributeName(String simpleUserAttributeName) {
		this.simpleUserAttributeName = simpleUserAttributeName;
	}

	public String getInstanceIdentifier() {
		return instanceIdentifier;
	}

	public void setInstanceIdentifier(String instanceIdentifier) {
		this.instanceIdentifier = instanceIdentifier;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * compare by instanceIdentifier, compoundUserAttributeName,
	 * simpleUserAttributeName
	 * 
	 * @param UserAttribute
	 * @return int
	 */
	public int compareTo(UserAttribute o) {
		if (this.compoundUserAttributeName == null) {
			return -1;
		}
		if (o.compoundUserAttributeName == null) {
			return 1;
		}
		if (this.compoundUserAttributeName
				.compareTo(o.compoundUserAttributeName) != 0) {
			return this.compoundUserAttributeName
					.compareTo(o.compoundUserAttributeName);
		}

		if (this.instanceIdentifier == null) {
			return -1;
		}
		if (o.instanceIdentifier == null) {
			return 1;
		}
		if (this.instanceIdentifier.compareTo(o.instanceIdentifier) != 0) {
			return this.instanceIdentifier.compareTo(o.instanceIdentifier);
		}

		if (this.simpleUserAttributeName == null) {
			return -1;
		}
		if (o.simpleUserAttributeName == null) {
			return 1;
		}
		if (this.simpleUserAttributeName.compareTo(o.simpleUserAttributeName) != 0) {
			return this.simpleUserAttributeName
					.compareTo(o.simpleUserAttributeName);
		}
		return this.equals(o) ? 0 : 1;
	}
}
