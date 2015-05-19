// $Header$
package com.hp.it.cas.persona.user.service.standalone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.hp.it.cas.persona.uav.service.IUserIdentifier;
import com.hp.it.cas.persona.user.service.ICompoundUserAttributeValue;
import com.hp.it.cas.persona.user.service.IUser;

/**
 * A user implementation that can live outside of a SecurityContext.
 *
 * @author Quintin May
 */
class StandaloneUser implements IUser {

	private final IUserIdentifier userIdentifier;
	private final Map<String, Collection<String>> simpleAttributeValues;
	private final Map<String, Collection<ICompoundUserAttributeValue>> compoundAttributeValues;

	/**
	 * Creates a standalone user by doing a deep copy of the provided user.
	 * @param user the user to copy.
	 */
	StandaloneUser(IUser user) {
		this.userIdentifier = user.getIdentifier();
		
		// deep-copy simple attribute values
		Map<String, Collection<String>> simpleAttributeValues = new HashMap<String, Collection<String>>();
		for (Entry<String, Collection<String>> entry : user.getSimpleAttributeValues().entrySet()) {
			Collection<String> values = new ArrayList<String>(entry.getValue());	// copy
			simpleAttributeValues.put(entry.getKey(), Collections.unmodifiableCollection(values));
		}
		this.simpleAttributeValues = Collections.unmodifiableMap(simpleAttributeValues);
		
		// deep-copy compound attribute values
		Map<String, Collection<ICompoundUserAttributeValue>> compoundAttributeValues = new HashMap<String, Collection<ICompoundUserAttributeValue>>();
		for (Entry<String, Collection<ICompoundUserAttributeValue>> entry : user.getCompoundAttributeValues().entrySet()) {
			Collection<ICompoundUserAttributeValue> values = new ArrayList<ICompoundUserAttributeValue>();
			for (ICompoundUserAttributeValue compoundUserAttributeValue : entry.getValue()) {
				values.add(new StandaloneCompoundUserAttributeValue(compoundUserAttributeValue));	// copy
			}
			compoundAttributeValues.put(entry.getKey(), Collections.unmodifiableCollection(values));
		}
		this.compoundAttributeValues = Collections.unmodifiableMap(compoundAttributeValues);
	}
	
	public IUserIdentifier getIdentifier() {
		return userIdentifier;
	}

	public Map<String, Collection<String>> getSimpleAttributeValues() {
		return simpleAttributeValues;
	}

	public Map<String, Collection<ICompoundUserAttributeValue>> getCompoundAttributeValues() {
		return compoundAttributeValues;
	}

	public ICompoundUserAttributeValue addCompoundAttributeValue(String compoundUserAttributeIdentifier) {
		throw new UnsupportedOperationException("User is read-only.");
	}

	public void addSimpleAttributeValue(String simpleUserAttributeIdentifier, String value) {
		throw new UnsupportedOperationException("User is read-only.");
	}

	public void remove() {
		throw new UnsupportedOperationException("User is read-only.");
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
		StandaloneUser other = (StandaloneUser) obj;
		if (userIdentifier == null) {
			if (other.userIdentifier != null)
				return false;
		} else if (!userIdentifier.equals(other.userIdentifier))
			return false;
		return true;
	}
}