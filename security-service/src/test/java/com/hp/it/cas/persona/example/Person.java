package com.hp.it.cas.persona.example;

import static com.hp.it.cas.persona.example.AbstractExample.FAMILY_NAME;
import static com.hp.it.cas.persona.example.AbstractExample.GIVEN_NAME;
import static com.hp.it.cas.persona.example.AbstractExample.NAME;
import static com.hp.it.cas.persona.example.AbstractExample.SERVICE_AGREEMENT_IDENTIFIER;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.hp.it.cas.persona.user.service.ICompoundUserAttributeValue;
import com.hp.it.cas.persona.user.service.IUser;

/**
 * A bean wrapper class for a {@link IUser}. Demonstrates simple property access as well as repeated values.
 *
 * @author Quintin May
 */
public class Person {
	
	private final IUser user;
	
	/**
	 * Creates a person.
	 * @param user the user wrapped by this person.
	 */
	Person(IUser user) {
		this.user = user;
	}
	
	/**
	 * Returns the user's last name.
	 * @return the last name or null.
	 */
	public String getFamilyName() {
		return findOrCreateUniqueCompoundAttributeValue(NAME).get(FAMILY_NAME);
	}

	/**
	 * Sets the user's last name.
	 * @param familyName the last name.
	 */
	public void setFamilyName(String familyName) {
		findOrCreateUniqueCompoundAttributeValue(NAME).put(FAMILY_NAME, familyName);
	}
	
	/**
	 * Returns the user's first name.
	 * @return the first name.
	 */
	public String getGivenName() {
		return findOrCreateUniqueCompoundAttributeValue(NAME).get(GIVEN_NAME);
	}

	/**
	 * Sets the person's first name.
	 * @param givenName the first name.
	 */
	public void setGivenName(String givenName) {
		findOrCreateUniqueCompoundAttributeValue(NAME).put(GIVEN_NAME, givenName);
	}
	
	/**
	 * Returns the SAIDs associated with this user. Adding and removing SAIDs from the list will update the user.
	 * @return a list of SAIDs or an empty list.
	 */
	public Collection<String> getServiceAgreementIdentifiers() {
		return new StringCollection(user, SERVICE_AGREEMENT_IDENTIFIER);
	}
	
	/**
	 * Returns an already existing group attribute or creates a new one.
	 * @param compoundUserAttributeIdentifier the name of the group attribute.
	 * @return the existing or newly created group.
	 */
	private ICompoundUserAttributeValue findOrCreateUniqueCompoundAttributeValue(String compoundUserAttributeIdentifier) {
		Collection<ICompoundUserAttributeValue> compoundAttributeValues = user.getCompoundAttributeValues().get(compoundUserAttributeIdentifier);
		return compoundAttributeValues == null ? user.addCompoundAttributeValue(compoundUserAttributeIdentifier) : compoundAttributeValues.iterator().next();
	}
	
	public String toString() {
		return user.toString();
	}

	private static class StringCollection extends AbstractCollection<String> {

		private final IUser user;
		private final String simpleUserAttributeIdentifier;
		
		StringCollection(IUser user, String simpleUserAttributeIdentifier) {
			this.user = user;
			this.simpleUserAttributeIdentifier = simpleUserAttributeIdentifier;
		}
		
		@Override
		public Iterator<String> iterator() {
			Collection<String> values = user.getSimpleAttributeValues().get(simpleUserAttributeIdentifier);
			return values == null ? Collections.<String>emptySet().iterator() : values.iterator();
		}

		@Override
		public boolean add(String value) {
			user.addSimpleAttributeValue(simpleUserAttributeIdentifier, value);
			return true;
		}

		@Override
		public int size() {
			Collection<String> values = user.getSimpleAttributeValues().get(simpleUserAttributeIdentifier);
			return values == null ? 0 : values.size();
		}		
	}
}
