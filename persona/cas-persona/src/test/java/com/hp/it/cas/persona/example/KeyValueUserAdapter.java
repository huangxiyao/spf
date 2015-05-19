package com.hp.it.cas.persona.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;

import com.hp.it.cas.persona.user.service.IUser;

/**
 * An example that demonstrates converting a {@link IUser} into a list of key value pairs. Compound attributes are excluded from the key/value output.
 *
 * @author Quintin May
 */
public class KeyValueUserAdapter {
	
	private final IUser user;
	
	/**
	 * Creates a key/value wrapper for the specified user.
	 * @param user the user for whom to produce a key/value view.
	 */
	public KeyValueUserAdapter(IUser user) {
		this.user = user;
	}
	
	/**
	 * Returns the top-level user attributes (not those in groups) as key/value pairs.
	 * @return the key/value pairs where the key is the attribute identifier and the value is the attribute value.
	 */
	public Collection<KeyValuePair> getKeyValues() {
		Collection<KeyValuePair> pairs = new ArrayList<KeyValuePair>();
		for (Entry<String, Collection<String>> entry : user.getSimpleAttributeValues().entrySet()) {
			for (String value : entry.getValue()) {
				pairs.add(new KeyValuePair(entry.getKey(), value));
			}
		}
		return pairs;
	}
	
	/**
	 * A key/value pair.
	 *
	 * @author Quintin May
	 */
	public static final class KeyValuePair {
		private final String key;
		private final String value;
		
		KeyValuePair(String key, String value) {
			this.key = key;
			this.value = value;
		}
		
		/**
		 * Returns the key.
		 * @return the key.
		 */
		public String getKey() {
			return key;
		}
		
		/**
		 * Returns the value.
		 * @return the value.
		 */
		public String getValue() {
			return value;
		}
	}
}
