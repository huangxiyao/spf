package com.hp.it.cas.persona.uav.webservice.soap;

import com.hp.it.cas.persona.uav.service.IUserAttribute;
import com.hp.it.cas.persona.uav.service.IUserAttributeValue;
import com.hp.it.cas.persona.uav.service.IUserIdentifier;

class UserAttributeValue implements IUserAttributeValue {

	private final IUserIdentifier userIdentifier;
	private final IUserAttribute userAttribute;
	private final String instanceIdentifier;
	private final String value;
	
	UserAttributeValue(IUserIdentifier userIdentifier, String compoundUserAttributeIdentifier, String simpleUserAttributeIdentifier, String instanceIdentifier, String value) {
		this.userIdentifier = userIdentifier;
		this.userAttribute = new UserAttribute(compoundUserAttributeIdentifier, simpleUserAttributeIdentifier);
		this.instanceIdentifier = instanceIdentifier;
		this.value = value;
	}
	
	public String getInstanceIdentifier() {
		return instanceIdentifier;
	}

	public IUserAttribute getUserAttribute() {
		return userAttribute;
	}

	public IUserIdentifier getUserIdentifier() {
		return userIdentifier;
	}

	public String getValue() {
		return value;
	}

	private static class UserAttribute implements IUserAttribute {

		private final String compoundUserAttributeIdentifier;
		private final String simpleUserAttributeIdentifier;
		
		UserAttribute(String compoundUserAttributeIdentifier, String simpleUserAttributeIdentifier) {
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
	}
}
