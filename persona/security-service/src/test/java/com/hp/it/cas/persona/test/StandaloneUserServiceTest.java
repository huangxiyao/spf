package com.hp.it.cas.persona.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import com.hp.it.cas.persona.configuration.service.IUserAttribute;
import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.uav.service.IUserIdentifier;
import com.hp.it.cas.persona.user.service.ICompoundUserAttributeValue;
import com.hp.it.cas.persona.user.service.IUser;

public class StandaloneUserServiceTest extends AbstractPersonaTest {

    private static final String NAME = "name";
    private static final String  FAMILY_NAME = "familyName";
    private static final String  GIVEN_NAME = "givenName";
    private static final String  CHILD_NAME = "childName";
    private static final String  SPOKEN_LANGUAGE = "spokenLanguage";
	
	private IUser user;
	
	@Before
	public void setupBeforeStandaloneUserServiceTest() {
		establishAdminSecurityContext();
		
        IUserAttribute name = metadataConfigurationService.addCompoundUserAttribute(NAME, NAME, "Name", "Name");
        IUserAttribute familyName = metadataConfigurationService.addSimpleUserAttribute(FAMILY_NAME, FAMILY_NAME, "Family name", "Family name");
        IUserAttribute givenName = metadataConfigurationService.addSimpleUserAttribute(GIVEN_NAME, GIVEN_NAME, "Given name", "Given name");
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(name, familyName);
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(name, givenName);
		securityConfigurationService.addPermission(application, name);
		
        IUserAttribute childName = metadataConfigurationService.addCompoundUserAttribute(CHILD_NAME, CHILD_NAME, "Child Name", "Child Name");
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(childName, familyName);
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(childName, givenName);
		securityConfigurationService.addPermission(application, childName);
		
        IUserAttribute spokenLanguage = metadataConfigurationService.addSimpleUserAttribute(SPOKEN_LANGUAGE, SPOKEN_LANGUAGE, "Spoken language", "Spoken language");
		securityConfigurationService.addPermission(application, spokenLanguage);
		
		establishNonAuthenticatedSecurityContext();
		user = standaloneUserService.createUser(EUserIdentifierType.EMPLOYEE, USER_IDENTIFIER);
	}
	
	private void populateData() {
		establishUserSecurityContext();
		IUser user = userService.createUser(EUserIdentifierType.EMPLOYEE, USER_IDENTIFIER);
		
		ICompoundUserAttributeValue compound = user.addCompoundAttributeValue(NAME);
		compound.put(GIVEN_NAME, "Charles");
		compound.put(FAMILY_NAME, "Schultz");
		
		compound = user.addCompoundAttributeValue(CHILD_NAME);
		compound.put(GIVEN_NAME, "Charlie");
		compound.put(FAMILY_NAME, "Brown");
		
		compound = user.addCompoundAttributeValue(CHILD_NAME);
		compound.put(GIVEN_NAME, "Lucy");
		compound.put(FAMILY_NAME, "van Pelt");
		
		user.addSimpleAttributeValue(SPOKEN_LANGUAGE, "en");
		user.addSimpleAttributeValue(SPOKEN_LANGUAGE, "de");
		establishNonAuthenticatedSecurityContext();
		this.user = standaloneUserService.createUser(EUserIdentifierType.EMPLOYEE, USER_IDENTIFIER);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void createUserWithNullUserIdentifierTypeFails() {
		standaloneUserService.createUser(null, USER_IDENTIFIER);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void createUserWithNullUserIdentifierFails() {
		standaloneUserService.createUser(EUserIdentifierType.EMPLOYEE, null);
	}
	
	@Test
	public void createUserSucceeds() {
		standaloneUserService.createUser(EUserIdentifierType.EMPLOYEE, USER_IDENTIFIER);
	}
	
	@Test
	public void getIdentifierSucceeds() {
		IUserIdentifier userIdentifier = user.getIdentifier();
		assertEquals(EUserIdentifierType.EMPLOYEE, userIdentifier.getUserIdentifierType());
		assertEquals(USER_IDENTIFIER, userIdentifier.getUserIdentifier());
	}
	
	@Test
	public void userIdentifierEquality() {
		IUserIdentifier userIdentifier = user.getIdentifier();
		assertEquals(userIdentifier, userIdentifier);
		
		IUser user2 = userService.createUser(EUserIdentifierType.EMPLOYEE, USER_IDENTIFIER + "2");
		IUserIdentifier userIdentifier2 = user2.getIdentifier();
		assertFalse(userIdentifier.equals(userIdentifier2));
		
		user2 = userService.createUser(EUserIdentifierType.EMPLOYEE_SIMPLIFIED_EMAIL_ADDRESS, USER_IDENTIFIER);
		userIdentifier2 = user2.getIdentifier();
		assertFalse(userIdentifier.equals(userIdentifier2));
	}
	
	@Test
	public void userEquality() {
		IUserIdentifier userIdentifier = user.getIdentifier();
		
		IUser user2 = userService.createUser(EUserIdentifierType.EMPLOYEE, USER_IDENTIFIER);
		IUserIdentifier userIdentifier2 = user.getIdentifier();

		assertEquals(userIdentifier, userIdentifier2);
		//assertEquals(user, user2);
	}
	
	@Test
	public void getSimpleAttributeValuesWithNoAttributesReturnsEmptyMap() {
		assertNotNull(user.getSimpleAttributeValues());
		assertTrue(user.getSimpleAttributeValues().isEmpty());
	}
	
	@Test
	public void getCompoundAttributeValuesWithNoAttributesReturnsEmptyMap() {
		assertNotNull(user.getCompoundAttributeValues());
		assertTrue(user.getCompoundAttributeValues().isEmpty());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void addSimpleAttributeValueWithInvalidSimpleUserAttributeKeyFails() {
		user.addSimpleAttributeValue("999", "value");
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void addSimpleAttributeValueWithNullValueFails() {
		user.addSimpleAttributeValue(SPOKEN_LANGUAGE, null);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void addSimpleAttributeValueFails() {
		user.addSimpleAttributeValue(SPOKEN_LANGUAGE, "en");
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void addCompoundAttributeValueWithInvalidCompoundUserAttributeKeyFails() {
		user.addCompoundAttributeValue("999");
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void addCompoundAttributeValueFails() {
		user.addCompoundAttributeValue(NAME);
	}
	
	@Test
	public void compoundAttributeValuesWithSameKeyHaveDifferentInstanceIdentifiers() {
		populateData();
		Collection<ICompoundUserAttributeValue> children = user.getCompoundAttributeValues().get(CHILD_NAME);
		assertEquals(2, children.size());
		
		Iterator<ICompoundUserAttributeValue> childIterator = children.iterator();
		assertFalse(childIterator.next().getInstanceIdentifier().equals(childIterator.next().getInstanceIdentifier()));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void removeFails() {
		user.remove();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void putToSimpleAttributeValuesMapFails() {
		user.getSimpleAttributeValues().put(SPOKEN_LANGUAGE, Collections.singleton("en"));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void removeFromSimpleAttributeValuesMapFails() {
		user.getSimpleAttributeValues().remove(SPOKEN_LANGUAGE);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void putToCompoundAttributeValuesMapFails() {
		populateData();
		ICompoundUserAttributeValue compound = user.getCompoundAttributeValues().get(CHILD_NAME).iterator().next();
		user.getCompoundAttributeValues().put(NAME, Collections.singleton(compound));
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void removeFromCompoundAttributeValuesMapFails() {
		populateData();
		user.getCompoundAttributeValues().remove(CHILD_NAME);
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void addToSimpleAttributeValueListFails() {
		user.addSimpleAttributeValue(SPOKEN_LANGUAGE, "en");
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void removeFromSimpleAttributeValueListFails() {
		populateData();
		Collection<String> spokenLanguages = user.getSimpleAttributeValues().get(SPOKEN_LANGUAGE);
		spokenLanguages.remove("en");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void updateCompoundSimpleAttributeValueSucceeds() {
		populateData();
		ICompoundUserAttributeValue value = user.getCompoundAttributeValues().get(NAME).iterator().next();
		value.put(FAMILY_NAME, "Brown");
	}

	@Test(expected = UnsupportedOperationException.class)
	public void updateCompoundSimpleAttributeValueViaMapEntryFails() {
		populateData();
		ICompoundUserAttributeValue value = user.getCompoundAttributeValues().get(NAME).iterator().next();
		value.entrySet().iterator().next().setValue("Brown");
	}
}
