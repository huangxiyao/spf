package com.hp.it.cas.persona.user.service.impl;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import com.hp.it.cas.persona.user.service.ICompoundUserAttributeValue;

class CompoundUserAttributeValueMap extends AbstractMap<String, Collection<ICompoundUserAttributeValue>> {

	final User user;
	private final AttributeEntrySet entries = new AttributeEntrySet();

	CompoundUserAttributeValueMap(User user) {
		this.user = user;
	}
	
	@Override
	public Set<Entry<String, Collection<ICompoundUserAttributeValue>>> entrySet() {
		return entries;
	}

	private class AttributeEntrySet extends AbstractSet<Entry<String, Collection<ICompoundUserAttributeValue>>> {

		AttributeEntrySet() {}
		
		@Override
		public Iterator<Entry<String, Collection<ICompoundUserAttributeValue>>> iterator() {
			return new Iterator<Entry<String, Collection<ICompoundUserAttributeValue>>>() {
				private Iterator<UserAttributeValue> iterator = getAttributes().iterator();
				private AttributeEntry current = null;
				private boolean canRemove = false;
				
				public boolean hasNext() {
					return iterator.hasNext();
				}

				public Entry<String, Collection<ICompoundUserAttributeValue>> next() {
					current = new AttributeEntry(iterator.next().getUserAttribute().getCompoundUserAttributeIdentifier());
					canRemove = true;
					return current;
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

		@Override
		public int size() {
			return getAttributes().size();
		}

		Collection<UserAttributeValue> getAttributes() {
			Collection<UserAttributeValue> attributes = new LinkedList<UserAttributeValue>(user.getAttributeValues());
			Iterator<UserAttributeValue> iterator = attributes.iterator();
			Set<String> keys = new HashSet<String>();
			
			while (iterator.hasNext()) {
				UserAttributeValue item = iterator.next();
				if (! (item.getUserAttribute().isCompoundUserAttribute()
						&& keys.add(item.getUserAttribute().getCompoundUserAttributeIdentifier()))) {
					iterator.remove();
				}
			}
			
			return attributes;
		}
	}

	private class AttributeEntry implements Entry<String, Collection<ICompoundUserAttributeValue>> {

		private final String compoundUserAttributeIdentifier;
		private final CompoundUserAttributeValueCollection value;
		
		AttributeEntry(String compoundUserAttributeIdentifier) {
			this.compoundUserAttributeIdentifier = compoundUserAttributeIdentifier;
			this.value = new CompoundUserAttributeValueCollection(compoundUserAttributeIdentifier);
		}
		
		public String getKey() {
			return compoundUserAttributeIdentifier;
		}

		public Collection<ICompoundUserAttributeValue> getValue() {
			return value;
		}

		public Collection<ICompoundUserAttributeValue> setValue(Collection<ICompoundUserAttributeValue> value) {
			throw new UnsupportedOperationException("The user attribute value collection cannot be replaced. Manipulate the collection directly.");
		}
		
		void remove() {
			value.clear();
		}
	}

	private class CompoundUserAttributeValueCollection extends AbstractCollection<ICompoundUserAttributeValue> {

		final String compoundUserAttributeIdentifier;
		
		CompoundUserAttributeValueCollection(String compoundUserAttributeIdentifier) {
			this.compoundUserAttributeIdentifier = compoundUserAttributeIdentifier;
		}
		
		@Override
		public Iterator<ICompoundUserAttributeValue> iterator() {
			return new Iterator<ICompoundUserAttributeValue>() {
				private Iterator<UserAttributeValue> iterator = getAttributes().iterator();
				private CompoundUserAttributeValue current = null;
				private boolean canRemove = false;
				
				public boolean hasNext() {
					return iterator.hasNext();
				}

				public ICompoundUserAttributeValue next() {
					current = new CompoundUserAttributeValue(user, compoundUserAttributeIdentifier, iterator.next().getInstanceIdentifier());
					canRemove = true;
					return current;
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

		@Override
		public int size() {
			return getAttributes().size();
		}
		
		Collection<UserAttributeValue> getAttributes() {
			Collection<UserAttributeValue> attributes = new LinkedList<UserAttributeValue>(user.getAttributeValues());
			Iterator<UserAttributeValue> iterator = attributes.iterator();
			String previousInstanceIdentifier = null;
			
			while (iterator.hasNext()) {
				UserAttributeValue item = iterator.next();
				if (! (item.getUserAttribute().isCompoundUserAttribute()
						&& (previousInstanceIdentifier == null || ! previousInstanceIdentifier.equals(item.getInstanceIdentifier()))
						&& (compoundUserAttributeIdentifier.equals(item.getUserAttribute().getCompoundUserAttributeIdentifier())))) {
					iterator.remove();
				}
				previousInstanceIdentifier = item.getInstanceIdentifier();
			}
			
			return attributes;
		}
	}
}
