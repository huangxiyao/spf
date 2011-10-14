// $Header$
package com.hp.it.cas.persona.user.service.standalone;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.hp.it.cas.persona.user.service.ICompoundUserAttributeValue;

/**
 * A compound user attribute value implementation that can live outside of a SecurityContext.
 *
 * @author Quintin May
 */
class StandaloneCompoundUserAttributeValue extends AbstractMap<String, String> implements ICompoundUserAttributeValue {

	private final String instanceIdentifier;
	private final Map<String, String> values;
	
	/**
	 * Creates a compound user attribute value by doing a deep copy of the provided compound user attribute value.
	 * @param compoundUserAttributeValue the value to copy.
	 */
	StandaloneCompoundUserAttributeValue(ICompoundUserAttributeValue compoundUserAttributeValue) {
		this.instanceIdentifier = compoundUserAttributeValue.getInstanceIdentifier();
		values = Collections.unmodifiableMap(new HashMap<String, String> (compoundUserAttributeValue));	// copy
	}

	@Override
	public Set<Entry<String, String>> entrySet() {
		return values.entrySet();
	}

	public String getInstanceIdentifier() {
		return instanceIdentifier;
	}

	public void remove() {
		throw new UnsupportedOperationException("User is read-only.");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((instanceIdentifier == null) ? 0 : instanceIdentifier.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		StandaloneCompoundUserAttributeValue other = (StandaloneCompoundUserAttributeValue) obj;
		if (instanceIdentifier == null) {
			if (other.instanceIdentifier != null)
				return false;
		} else if (!instanceIdentifier.equals(other.instanceIdentifier))
			return false;
		return true;
	}
}