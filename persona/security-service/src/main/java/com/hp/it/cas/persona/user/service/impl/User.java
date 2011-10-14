package com.hp.it.cas.persona.user.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.uav.service.IUserIdentifier;
import com.hp.it.cas.persona.uav.service.UserIdentifier;
import com.hp.it.cas.persona.user.service.ICompoundUserAttributeValue;
import com.hp.it.cas.persona.user.service.IUser;

class User implements IUser {

	private final UserService userService;
	private final UserIdentifier userIdentifier;
	private final AtomicReference<Collection<UserAttributeValue>> attributeValues = new AtomicReference<Collection<UserAttributeValue>>();
	private final SimpleUserAttributeValueMap simpleAttributeValues;
	private final CompoundUserAttributeValueMap compoundAttributeValues;
	
	User(UserService userService, EUserIdentifierType userIdentifierType, String userIdentifier) {
		this.userService = userService;
		this.userIdentifier = new UserIdentifier(userIdentifierType, userIdentifier);
		simpleAttributeValues = new SimpleUserAttributeValueMap(this);
		compoundAttributeValues = new CompoundUserAttributeValueMap(this);
	}

	public IUserIdentifier getIdentifier() {
		return userIdentifier;
	}

	public Map<String, Collection<ICompoundUserAttributeValue>> getCompoundAttributeValues() {
		return compoundAttributeValues;
	}

	public Map<String, Collection<String>> getSimpleAttributeValues() {
		return simpleAttributeValues;
	}

	public void addSimpleAttributeValue(String simpleUserAttributeIdentifier, String value) {
		add(null, simpleUserAttributeIdentifier, null, value);
	}
	
	public ICompoundUserAttributeValue addCompoundAttributeValue(String compoundUserAttributeIdentifier) {
		return userService.addUserAttributeGroup(this, compoundUserAttributeIdentifier);
	}

	public void remove() {
		getSimpleAttributeValues().clear();
		getCompoundAttributeValues().clear();
	}

	public String toString() {
		return String.format("%s[%s]", getClass().getSimpleName(), userIdentifier);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userIdentifier == null) ? 0 : userIdentifier.hashCode());
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
		User other = (User) obj;
		if (userIdentifier == null) {
			if (other.userIdentifier != null)
				return false;
		} else if (!userIdentifier.equals(other.userIdentifier))
			return false;
		return true;
	}

	/**
	 * Returns the full list of user attributes sorted by instance identifier. Manages caching and cache refresh.
	 * @return the list of raw user attributes.
	 */
	Collection<UserAttributeValue> getAttributeValues() {
		synchronized (this.attributeValues) {
			if (this.attributeValues.get() == null) {
				setAttributeValues(userService.findAttributes(this));
			}
			return this.attributeValues.get();
		}
	}

	void remove(UserAttributeValue userAttributeValue) {
		synchronized (this.attributeValues) {
			List<UserAttributeValue> attributes = new ArrayList<UserAttributeValue>(getAttributeValues());
			userService.remove(userAttributeValue);
			attributes.remove(userAttributeValue);
			setAttributeValues(attributes);
		}		
	}
	
	void add(String compoundUserAttributeIdentifier, String simpleUserAttributeIdentifier, String instanceIdentifier, String attributeValueText) {
		synchronized (this.attributeValues) {
    		List<UserAttributeValue> values = new ArrayList<UserAttributeValue>(getAttributeValues());
			UserAttributeValue userAttributeValue = userService.add(this, compoundUserAttributeIdentifier, simpleUserAttributeIdentifier, instanceIdentifier, attributeValueText);
    		values.add(userAttributeValue);
    		setAttributeValues(values);
		}
	}
	
	void update(UserAttributeValue userAttributeValue, String attributeValueText) {
		userService.update(userAttributeValue, attributeValueText);
	}
	
	private void setAttributeValues(List<UserAttributeValue> attributeValues) {
		synchronized (this.attributeValues) {
			Collections.sort(attributeValues, InstanceComparator.INSTANCE);
			this.attributeValues.set(Collections.unmodifiableCollection(attributeValues));
		}
	}
	
	/**
	 * Compares attributes for instance equality based on {@link UserAttributeValue#getInstanceIdentifier()}.
	 *
	 * @author Quintin May
	 */
	private static final class InstanceComparator implements Comparator<UserAttributeValue> {
		
		static final InstanceComparator INSTANCE = new InstanceComparator();
		
		private InstanceComparator() {}
		
		public int compare(UserAttributeValue userAttributeValue1, UserAttributeValue userAttributeValue2) {
			int comparison = userAttributeValue1.getInstanceIdentifier().compareTo(userAttributeValue2.getInstanceIdentifier());
			
			if (comparison == 0) {
				comparison = userAttributeValue1.getUserAttribute().getCompoundUserAttributeIdentifier().compareTo(
						userAttributeValue2.getUserAttribute().getCompoundUserAttributeIdentifier());
			}
			
			return comparison;
		}		
	}
}
