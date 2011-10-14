package com.hp.it.cas.persona.user.service.impl;

import java.util.concurrent.atomic.AtomicReference;

import com.hp.it.cas.persona.uav.service.IUserAttribute;
import com.hp.it.cas.persona.uav.service.IUserAttributeValue;
import com.hp.it.cas.persona.uav.service.IUserIdentifier;

class UserAttributeValue implements IUserAttributeValue {
	
	private final User user;
	private final IUserAttributeValue userAttributeValue;
	private final AtomicReference<String> value = new AtomicReference<String>();

	UserAttributeValue(User user, IUserAttributeValue userAttributeValue) {
		this.user = user;
		this.userAttributeValue = userAttributeValue;
		this.value.set(userAttributeValue.getValue());
	}
	
	public IUserIdentifier getUserIdentifier() {
		return userAttributeValue.getUserIdentifier();
	}	

	public IUserAttribute getUserAttribute() {
		return userAttributeValue.getUserAttribute();		
	}
	
	void remove() {
		user.remove(this);
	}

	User getUser() {
		return user;
	}
	
	public String getInstanceIdentifier() {
		return userAttributeValue.getInstanceIdentifier();
	}

	public String getValue() {
		return value.get();
	}

	public void setValue(String newValue) {
		synchronized (value) {
			if (! isSameValue(value.get(), newValue)) {
				// update data store first so that exceptions will not cause an out-of-sync cache
				user.update(this, newValue);
				value.set(newValue);
			}
		}
	}
	
	private boolean isSameValue(String value1, String value2) {
		return value1 == null ? value2 == null : value1.equals(value2);
	}

	public String toString() {
		return String.format("%s=\"%s\"", userAttributeValue.getUserAttribute(), value.get());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userAttributeValue == null) ? 0 : userAttributeValue.hashCode());
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
		if (userAttributeValue == null) {
			if (other.userAttributeValue != null)
				return false;
		} else if (!userAttributeValue.equals(other.userAttributeValue))
			return false;
		return true;
	}

}
