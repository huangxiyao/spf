package com.hp.it.cas.persona.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.PermissionDeniedDataAccessException;

import com.hp.it.cas.persona.configuration.service.IUserAttribute;
import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.uav.service.IUserIdentifier;
import com.hp.it.cas.persona.user.service.ICompoundUserAttributeValue;
import com.hp.it.cas.persona.user.service.IUser;

public class UserServiceTest extends AbstractPersonaTest {

	private static final String NAME = "name";
	private static final String FAMILY_NAME = "familyName";
	private static final String GIVEN_NAME = "givenName";
	private static final String CHILD_NAME = "childName";
	private static final String SPOKEN_LANGUAGE = "spokenLanguage";
	
	private IUser user;
	
	@Before
	public void setupBeforeUserServiceTest() {
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
		
		establishUserSecurityContext();
		
		user = userService.createUser(EUserIdentifierType.EMPLOYEE, USER_IDENTIFIER);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void createUserWithNullUserIdentifierTypeFails() {
		userService.createUser(null, USER_IDENTIFIER);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void createUserWithNullUserIdentifierFails() {
		userService.createUser(EUserIdentifierType.EMPLOYEE, null);
	}
	
	@Test
	public void createUserSucceeds() {
		userService.createUser(EUserIdentifierType.EMPLOYEE, USER_IDENTIFIER);
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
		assertEquals(user, user2);
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

	@Test(expected = PermissionDeniedDataAccessException.class)
	public void addSimpleAttributeValueWithInvalidSimpleUserAttributeKeyFails() {
		user.addSimpleAttributeValue("999", "value");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addSimpleAttributeValueWithNullValueFails() {
		user.addSimpleAttributeValue(SPOKEN_LANGUAGE, null);
	}
	
	@Test
	public void addSimpleAttributeValueSucceeds() {
		user.addSimpleAttributeValue(SPOKEN_LANGUAGE, "en");
		assertEquals("en", user.getSimpleAttributeValues().get(SPOKEN_LANGUAGE).iterator().next());
	}
	
	@Test
	public void addSimpleAttributeValueWithSameKeyAddsToValueList() {
		user.addSimpleAttributeValue(SPOKEN_LANGUAGE, "en");
		user.addSimpleAttributeValue(SPOKEN_LANGUAGE, "de");
		assertEquals(2, user.getSimpleAttributeValues().get(SPOKEN_LANGUAGE).size());
	}
	
	@Test(expected = PermissionDeniedDataAccessException.class)
	public void addCompoundAttributeValueWithInvalidCompoundUserAttributeKeyFails() {
		ICompoundUserAttributeValue compound = user.addCompoundAttributeValue("999");
		compound.put(FAMILY_NAME, "Doe");
	}
	
	@Test
	public void addCompoundAttributeValueSucceeds() {
		ICompoundUserAttributeValue compound = user.addCompoundAttributeValue(NAME);
		compound.put(FAMILY_NAME, "Doe");
		assertEquals("Doe", compound.get(FAMILY_NAME));
	}
	
	@Test
	public void addCompoundAttributeValueWithSameKeyAddsToValueList() {
		ICompoundUserAttributeValue child1 = user.addCompoundAttributeValue(CHILD_NAME);
		child1.put(GIVEN_NAME, "Sally");
		
		ICompoundUserAttributeValue child2 = user.addCompoundAttributeValue(CHILD_NAME);
		child2.put(GIVEN_NAME, "Charlie");
		
		assertEquals(1, user.getCompoundAttributeValues().size());
		assertEquals(2, user.getCompoundAttributeValues().get(CHILD_NAME).size());
	}
	
	@Test
	public void compoundAttributeValuesWithSameKeyHaveDifferentInstanceIdentifiers() {
		ICompoundUserAttributeValue child1 = user.addCompoundAttributeValue(CHILD_NAME);
		child1.put(GIVEN_NAME, "Sally");
		
		ICompoundUserAttributeValue child2 = user.addCompoundAttributeValue(CHILD_NAME);
		child2.put(GIVEN_NAME, "Charlie");
		
		assertFalse(child1.getInstanceIdentifier().equals(child2.getInstanceIdentifier()));
	}
	
	@Test
	public void removeSucceeds() {
		user.addSimpleAttributeValue(SPOKEN_LANGUAGE, "en");
		ICompoundUserAttributeValue child1 = user.addCompoundAttributeValue(CHILD_NAME);
		child1.put(GIVEN_NAME, "Sally");
		
		user.remove();
		assertTrue(user.getSimpleAttributeValues().isEmpty());
		assertTrue(user.getCompoundAttributeValues().isEmpty());
	}
	
	@Test
	public void removeMoreThanOnceSucceeds() {
		user.addSimpleAttributeValue(SPOKEN_LANGUAGE, "en");
		ICompoundUserAttributeValue child1 = user.addCompoundAttributeValue(CHILD_NAME);
		child1.put(GIVEN_NAME, "Sally");
		
		user.remove();
		user.remove();
	}
	
	@Test(expected = UnsupportedOperationException.class)
	public void putToSimpleAttributeValuesMapFails() {
		user.getSimpleAttributeValues().put(SPOKEN_LANGUAGE, Collections.singleton("en"));
	}
	
	@Test
	public void removeFromSimpleAttributeValuesMapSucceeds() {
		user.addSimpleAttributeValue(SPOKEN_LANGUAGE, "en");
		user.addSimpleAttributeValue(SPOKEN_LANGUAGE, "de");
		user.getSimpleAttributeValues().remove(SPOKEN_LANGUAGE);
		assertTrue(user.getSimpleAttributeValues().isEmpty());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void putToCompoundAttributeValuesMapFails() {
		ICompoundUserAttributeValue compound = user.addCompoundAttributeValue(NAME);
		compound.put(FAMILY_NAME, "Brown");
		user.getCompoundAttributeValues().remove(NAME);
		user.getCompoundAttributeValues().put(NAME, Collections.singleton(compound));
	}
	
	@Test
	public void removeFromCompoundAttributeValuesMapSucceeds() {
		ICompoundUserAttributeValue child1 = user.addCompoundAttributeValue(CHILD_NAME);
		child1.put(GIVEN_NAME, "Linus");
		
		ICompoundUserAttributeValue child2 = user.addCompoundAttributeValue(CHILD_NAME);
		child2.put(GIVEN_NAME, "Lucy");
		
		user.getCompoundAttributeValues().remove(CHILD_NAME);
		assertTrue(user.getCompoundAttributeValues().isEmpty());
	}
	
	@Test
	public void addToSimpleAttributeValueListSucceeds() {
		user.addSimpleAttributeValue(SPOKEN_LANGUAGE, "en");
		Collection<String> spokenLanguages = user.getSimpleAttributeValues().get(SPOKEN_LANGUAGE);
		spokenLanguages.add("de");
		assertEquals(2, spokenLanguages.size());
		assertEquals(2, user.getSimpleAttributeValues().get(SPOKEN_LANGUAGE).size());
	}
	
	@Test
	public void removeFromSimpleAttributeValueListSucceeds() {
		user.addSimpleAttributeValue(SPOKEN_LANGUAGE, "en");
		user.addSimpleAttributeValue(SPOKEN_LANGUAGE, "de");
		Collection<String> spokenLanguages = user.getSimpleAttributeValues().get(SPOKEN_LANGUAGE);
		spokenLanguages.remove("en");
		assertEquals(1, user.getSimpleAttributeValues().get(SPOKEN_LANGUAGE).size());
		assertEquals("de", user.getSimpleAttributeValues().get(SPOKEN_LANGUAGE).iterator().next());
	}

	@Test
	public void updateCompoundSimpleAttributeValueSucceeds() {
		ICompoundUserAttributeValue compound = user.addCompoundAttributeValue(NAME);
		compound.put(FAMILY_NAME, "Brown");
		compound.put(FAMILY_NAME, "Schultz");
		assertEquals("Schultz", user.getCompoundAttributeValues().get(NAME).iterator().next().get(FAMILY_NAME));
	}

	@Test
	public void updateCompoundSimpleAttributeValueViaMapEntrySucceeds() {
		ICompoundUserAttributeValue compound = user.addCompoundAttributeValue(NAME);
		compound.put(FAMILY_NAME, "Brown");
		compound.entrySet().iterator().next().setValue("Schultz");
		assertEquals("Schultz", user.getCompoundAttributeValues().get(NAME).iterator().next().get(FAMILY_NAME));
	}
}
