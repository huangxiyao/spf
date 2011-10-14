package com.hp.it.cas.persona.configuration.service.impl;

import com.hp.it.cas.persona.configuration.service.IApplication;
import com.hp.it.cas.persona.configuration.service.IPermission;
import com.hp.it.cas.persona.configuration.service.IUserAttribute;

class Permission implements IPermission {

	private final IApplication application;
	private final IUserAttribute compoundUserAttribute;
	private final IUserAttribute simpleUserAttribute;
	
	Permission(IApplication application, IUserAttribute userAttribute) {
		this.application = application;
		if (userAttribute.isSimpleUserAttribute()) {
			this.compoundUserAttribute = null;
			this.simpleUserAttribute = userAttribute;
		} else {
			this.compoundUserAttribute = userAttribute;
			this.simpleUserAttribute = null;
		}
	}
	
	Permission(IApplication application, IUserAttribute compoundUserAttribute, IUserAttribute simpleUserAttribute) {
		this.application = application;
		this.compoundUserAttribute = compoundUserAttribute;
		this.simpleUserAttribute = simpleUserAttribute;
	}
	
	public IApplication getApplication() {
		return application;
	}

	public IUserAttribute getCompoundUserAttribute() {
		return compoundUserAttribute;
	}

	public IUserAttribute getSimpleUserAttribute() {
		return simpleUserAttribute;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((application == null) ? 0 : application.hashCode());
		result = prime * result + ((compoundUserAttribute == null) ? 0 : compoundUserAttribute.hashCode());
		result = prime * result + ((simpleUserAttribute == null) ? 0 : simpleUserAttribute.hashCode());
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
		Permission other = (Permission) obj;
		if (application == null) {
			if (other.application != null)
				return false;
		} else if (!application.equals(other.application))
			return false;
		if (compoundUserAttribute == null) {
			if (other.compoundUserAttribute != null)
				return false;
		} else if (!compoundUserAttribute.equals(other.compoundUserAttribute))
			return false;
		if (simpleUserAttribute == null) {
			if (other.simpleUserAttribute != null)
				return false;
		} else if (!simpleUserAttribute.equals(other.simpleUserAttribute))
			return false;
		return true;
	}

	public String toString() {
		return String.format("%s[%s, %s, %s]", getClass().getSimpleName(), application, compoundUserAttribute, simpleUserAttribute);
	}
}
