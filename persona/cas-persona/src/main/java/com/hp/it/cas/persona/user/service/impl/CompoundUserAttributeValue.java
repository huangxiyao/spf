package com.hp.it.cas.persona.user.service.impl;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import com.hp.it.cas.persona.user.service.ICompoundUserAttributeValue;

class CompoundUserAttributeValue extends AbstractMap<String, String> implements ICompoundUserAttributeValue {

	final User user;
	private final String compoundUserAttributeIdentifier;
	final String instanceIdentifier;
	private final AttributeEntrySet entrySet = new AttributeEntrySet();
	
	CompoundUserAttributeValue(User user, String compoundUserAttributeIdentifier, String instanceIdentifier) {
		this.user = user;
		this.compoundUserAttributeIdentifier = compoundUserAttributeIdentifier;
		this.instanceIdentifier = instanceIdentifier;
	}	

	public String getInstanceIdentifier() {
		return instanceIdentifier;
	}

	@Override
	public Set<Entry<String, String>> entrySet() {
		return entrySet;
	}

	public void remove() {
		entrySet.clear();
	}

	@Override
	public String put(String key, String value) {
		// optimized to eliminate a cache invalidation if the value hasn't changed
		Iterator<UserAttributeValue> attributes = user.getAttributeValues().iterator();
		UserAttributeValue existingAttribute = null;
		while (attributes.hasNext() && existingAttribute == null) {
			UserAttributeValue attribute = attributes.next();
			existingAttribute = instanceIdentifier.equals(attribute.getInstanceIdentifier())
				&& key.equals(attribute.getUserAttribute().getSimpleUserAttributeIdentifier()) ? attribute : null;
		}
		
		String result = null;
		
		if (existingAttribute == null) {
			user.add(compoundUserAttributeIdentifier, key, instanceIdentifier, value);
		} else {
			result = existingAttribute.getValue();
			existingAttribute.setValue(value);	// writes only if value is different
		}
		
		return result;
	}

	private class AttributeEntrySet extends AbstractSet<Entry<String, String>> {

		AttributeEntrySet() {}
		
		public Iterator<Entry<String, String>> iterator() {
			return new Iterator<Entry<String, String>>() {
				private Iterator<UserAttributeValue> iterator = getAttributes().iterator();
				private UserAttributeValue current = null;
				private boolean canRemove = false;
				
				public boolean hasNext() {
					return iterator.hasNext();
				}

				public Entry<String, String> next() {
					current = iterator.next();
					canRemove = true;
					return new UserAttributeValueEntry(current);
				}

				public void remove() {
					if (canRemove) {
						current.remove();
						canRemove = false;
					} else {
						throw new IllegalStateException("next() must be called prior to remove()");
					}
				}				
			};
		}

		public int size() {
			return getAttributes().size();
		}
		
		Collection<UserAttributeValue> getAttributes() {
			Collection<UserAttributeValue> attributes = new LinkedList<UserAttributeValue>(user.getAttributeValues());
			Iterator<UserAttributeValue> iterator = attributes.iterator();
			
			while(iterator.hasNext()) {
				UserAttributeValue item = iterator.next();
				if (! (item.getUserAttribute().isCompoundUserAttribute()
      						&& instanceIdentifier.equals(item.getInstanceIdentifier()))) {
					iterator.remove();
				}
			}
			
			return attributes;
		}
	}

	private static class UserAttributeValueEntry implements Entry<String, String> {

		private final UserAttributeValue attributeValue;
		
		UserAttributeValueEntry(UserAttributeValue attributeValue) {
			this.attributeValue = attributeValue;
		}
		
		public String getKey() {
			return attributeValue.getUserAttribute().getSimpleUserAttributeIdentifier();
		}

		public String getValue() {
			return attributeValue.getValue();
		}

		public String setValue(String newValue) {
			String previous = getValue();
			attributeValue.setValue(newValue);
			return previous;
		}				
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((compoundUserAttributeIdentifier == null) ? 0 : compoundUserAttributeIdentifier.hashCode());
        result = prime * result + ((instanceIdentifier == null) ? 0 : instanceIdentifier.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        CompoundUserAttributeValue other = (CompoundUserAttributeValue) obj;
        if (compoundUserAttributeIdentifier == null) {
            if (other.compoundUserAttributeIdentifier != null)
                return false;
        } else if (!compoundUserAttributeIdentifier.equals(other.compoundUserAttributeIdentifier))
            return false;
        if (instanceIdentifier == null) {
            if (other.instanceIdentifier != null)
                return false;
        } else if (!instanceIdentifier.equals(other.instanceIdentifier))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }

}
