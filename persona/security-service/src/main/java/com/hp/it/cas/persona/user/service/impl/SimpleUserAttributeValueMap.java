package com.hp.it.cas.persona.user.service.impl;

import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

class SimpleUserAttributeValueMap extends AbstractMap<String, Collection<String>> {

	final User user;
	private final AttributeEntrySet entries = new AttributeEntrySet();
	
	public SimpleUserAttributeValueMap(User user) {
		this.user = user;
	}

	@Override
	public Set<Entry<String, Collection<String>>> entrySet() {
		return entries;
	}

	private class AttributeEntrySet extends AbstractSet<Entry<String, Collection<String>>> {

		AttributeEntrySet() {}
		
		@Override
		public Iterator<Entry<String, Collection<String>>> iterator() {
			return new Iterator<Entry<String, Collection<String>>>() {
				private final Iterator<UserAttributeValue> iterator = getAttributes().iterator();
				private AttributeEntry current = null;
				private boolean canRemove = false;
				
				public boolean hasNext() {
					return iterator.hasNext();
				}

				public Entry<String, Collection<String>> next() {
					current = new AttributeEntry(iterator.next().getUserAttribute().getSimpleUserAttributeIdentifier());
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
				if (! (item.getUserAttribute().isSimpleUserAttribute() && keys.add(item.getUserAttribute().getSimpleUserAttributeIdentifier()))) {
					iterator.remove();
				}
			}
			
			return attributes;
		}
	}
	
	private class AttributeEntry implements Entry<String, Collection<String>> {

		private final String simpleUserAttributeIdentifier;
		private final SimpleUserAttributeValueCollection value;
		
		AttributeEntry(String simpleUserAttributeIdentifier) {
			this.simpleUserAttributeIdentifier = simpleUserAttributeIdentifier;
			this.value = new SimpleUserAttributeValueCollection(simpleUserAttributeIdentifier);
		}
		
		public String getKey() {
			return simpleUserAttributeIdentifier;
		}

		public Collection<String> getValue() {
			return value;
		}

		public Collection<String> setValue(Collection<String> value) {
			throw new UnsupportedOperationException("The user attribute value collection cannot be replaced. Manipulate the collection directly.");
		}
		
		void remove() {
			value.remove();
		}
	}
	
	private class SimpleUserAttributeValueCollection extends AbstractCollection<String> {

		private final String simpleUserAttributeIdentifier;
		
		SimpleUserAttributeValueCollection(String simpleUserAttributeIdentifier) {
			this.simpleUserAttributeIdentifier = simpleUserAttributeIdentifier;
		}
		
		@Override
		public Iterator<String> iterator() {
			return new Iterator<String>() {
				private final Iterator<UserAttributeValue> iterator = getAttributes().iterator();
				private UserAttributeValue current = null;
				private boolean canRemove = false;
				
				public boolean hasNext() {
					return iterator.hasNext();
				}

				public String next() {
					current = iterator.next();
					canRemove = true;
					return current.getValue();
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

		@Override
		public boolean add(String value) {
			user.add(null, simpleUserAttributeIdentifier, null, value);
			return true;
		}
		
		Collection<UserAttributeValue> getAttributes() {
			Collection<UserAttributeValue> attributes = new LinkedList<UserAttributeValue>(user.getAttributeValues());
			Iterator<UserAttributeValue> iterator = attributes.iterator();
			
			while (iterator.hasNext()) {
				UserAttributeValue item = iterator.next();
				if (! (item.getUserAttribute().isSimpleUserAttribute() && simpleUserAttributeIdentifier.equals(item.getUserAttribute().getSimpleUserAttributeIdentifier()))) {
					iterator.remove();
				}
			}
			
			return attributes;
		}
		
		void remove() {
			Iterator<String> iterator = iterator();
			while (iterator.hasNext()) {
				iterator.next();
				iterator.remove();
			}
		}
	}
}
