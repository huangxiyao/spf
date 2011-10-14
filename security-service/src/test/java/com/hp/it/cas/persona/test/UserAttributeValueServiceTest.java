package com.hp.it.cas.persona.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.security.AuthenticationException;

import com.hp.it.cas.persona.configuration.service.IUserAttribute;
import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.uav.service.IUserAttributeValue;
import com.hp.it.cas.persona.uav.service.IUserIdentifier;
import com.hp.it.cas.persona.uav.service.UserIdentifier;

public class UserAttributeValueServiceTest extends AbstractPersonaTest {

    private static final String COMPOUND_ATTRIBUTE_ID = "NM";
	private static final String COMPOUND_ATTRIBUTE_NAME = "Name";
	
    private static final String COMPOUND_SIMPLE_ATTRIBUTE_ID = "FMLY_NM";
	private static final String COMPOUND_SIMPLE_ATTRIBUTE_NAME = "FamilyName";
	
    private static final String SIMPLE_ATTRIBUTE_ID = "FVR_CLR";
	private static final String SIMPLE_ATTRIBUTE_NAME = "FavoriteColor";
	
	private static final String ATTRIBUTE_DESCRIPTION = "Description";
	private static final String ATTRIBUTE_DEFINITION = "Definition";
	private static final String VALUE = "value";
	
	private IUserIdentifier userIdentifier;
	
	@Before
	public void setupBeforeUserAttributeValueTest() {
		userIdentifier = new UserIdentifier(EUserIdentifierType.EMPLOYEE, "1234");
		
		establishAdminSecurityContext();
		
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(COMPOUND_ATTRIBUTE_ID, COMPOUND_ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute compoundSimpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(COMPOUND_SIMPLE_ATTRIBUTE_ID, COMPOUND_SIMPLE_ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(SIMPLE_ATTRIBUTE_ID, SIMPLE_ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(compoundUserAttribute, compoundSimpleUserAttribute);
		securityConfigurationService.addPermission(application, compoundUserAttribute);
		securityConfigurationService.addPermission(application, simpleUserAttribute);
		
		establishUserSecurityContext();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void findUserAttributeValuesWithNullUserIdentifierFails() {
		userAttributeValueService.findUserAttributeValues(null);
	}
	@Test
	public void findUserAttributeValuesWithUnknownUserReturnsEmptySet() {
		Set<IUserAttributeValue> values = userAttributeValueService.findUserAttributeValues(userIdentifier);
		assertNotNull(values);
		assertTrue(values.isEmpty());
	}
	
	@Test
	public void findUserAttributeValuesSucceeds() {
		userAttributeValueService.addUserAttributeValue(userIdentifier, SIMPLE_ATTRIBUTE_ID, VALUE);
		Set<IUserAttributeValue> values = userAttributeValueService.findUserAttributeValues(userIdentifier);
		assertFalse(values.isEmpty());
		assertEquals(VALUE, values.iterator().next().getValue());
	}
	
	@Test(expected = AuthenticationException.class)
	public void findUserAttributeValuesNonAuthenticatedFails() {
		establishNonAuthenticatedSecurityContext();
		userAttributeValueService.findUserAttributeValues(userIdentifier);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addUserAttributeValueSimpleWithNullUserIdentifierFails() {
		userAttributeValueService.addUserAttributeValue(null, SIMPLE_ATTRIBUTE_ID, VALUE);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addUserAttributeValueSimpleWithNullUserAttributeKeyFails() {
		userAttributeValueService.addUserAttributeValue(userIdentifier, null, VALUE);
	}
	
	@Test(expected = PermissionDeniedDataAccessException.class)
	public void addUserAttributeValueSimpleWithInvalidUserAttributeKeyFails() {
		userAttributeValueService.addUserAttributeValue(userIdentifier, "999", VALUE);
	}
	
	@Test
	public void addUserAttributeValueSimpleSucceeds() {
		userAttributeValueService.addUserAttributeValue(userIdentifier, SIMPLE_ATTRIBUTE_ID, VALUE);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addUserAttributeValueSimpleWithNullValueFails() {
		userAttributeValueService.addUserAttributeValue(userIdentifier, SIMPLE_ATTRIBUTE_ID, null);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void addUserAttributeValueSimpleWithCompoundUserAttributeKeyFails() {
		userAttributeValueService.addUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, VALUE);
	}
	
	@Test(expected = AuthenticationException.class)
	public void addUserAttributeValueSimpleNonAuthenticatedFails() {
		establishNonAuthenticatedSecurityContext();
		userAttributeValueService.addUserAttributeValue(userIdentifier, SIMPLE_ATTRIBUTE_ID, VALUE);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addUserAttributeValueCompoundWithNullUserIdentifierFails() {
		userAttributeValueService.addUserAttributeValue(null, COMPOUND_ATTRIBUTE_ID, COMPOUND_SIMPLE_ATTRIBUTE_ID, VALUE);
	}

	@Test(expected = PermissionDeniedDataAccessException.class)
	public void addUserAttributeValueCompoundWithInvalidCompoundUserAttributeKeyFails() {
		userAttributeValueService.addUserAttributeValue(userIdentifier, "999", COMPOUND_SIMPLE_ATTRIBUTE_ID, VALUE);
	}

	@Test
	public void addUserAttributeValueCompoundWithZeroCompoundUserAttributeKeySucceeds() {
		userAttributeValueService.addUserAttributeValue(userIdentifier, null, SIMPLE_ATTRIBUTE_ID, VALUE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addUserAttributeValueCompoundWithNullSimpleUserAttributeKeyFails() {
		userAttributeValueService.addUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, null, VALUE);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void addUserAttributeValueCompoundWithInvalidSimpleUserAttributeKeyFails() {
		userAttributeValueService.addUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, "999", VALUE);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void addUserAttributeValueCompoundWithInvalidCompoundSimpleUserAttributeKeyFails() {
		userAttributeValueService.addUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, SIMPLE_ATTRIBUTE_ID, VALUE);
	}

	@Test
	public void addUserAttributeValueCompoundSucceeds() {
		userAttributeValueService.addUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, COMPOUND_SIMPLE_ATTRIBUTE_ID, VALUE);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addUserAttributeValueCompoundWithNullValueFails() {
		userAttributeValueService.addUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, COMPOUND_SIMPLE_ATTRIBUTE_ID, null);
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void addUserAttributeValueCompoundWithSimpleCompoundUserAttributeKeyFails() {
		userAttributeValueService.addUserAttributeValue(userIdentifier, SIMPLE_ATTRIBUTE_ID, COMPOUND_SIMPLE_ATTRIBUTE_ID, null);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void addUserAttributeValueCompoundWithCompoundSimpleAttributeKeyFails() {
		userAttributeValueService.addUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, COMPOUND_ATTRIBUTE_ID, null);
	}
	
	@Test(expected = AuthenticationException.class)
	public void addUserAttributeValueCompoundNonAuthenticatedFails() {
		establishNonAuthenticatedSecurityContext();
		userAttributeValueService.addUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, COMPOUND_SIMPLE_ATTRIBUTE_ID, VALUE);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void putUserAttributeValueWithNullUserIdentifierFails() {
		userAttributeValueService.putUserAttributeValue(null, COMPOUND_ATTRIBUTE_ID, null, COMPOUND_SIMPLE_ATTRIBUTE_ID, VALUE);
	}

	@Test(expected = PermissionDeniedDataAccessException.class)
	public void putUserAttributeValueWithInvalidCompoundUserAttributeKeyFails() {
		userAttributeValueService.putUserAttributeValue(userIdentifier, "999", null, COMPOUND_SIMPLE_ATTRIBUTE_ID, VALUE);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void putUserAttributeValueWithInvalidInstanceIdentifierFails() {
		userAttributeValueService.putUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, "ABC", COMPOUND_SIMPLE_ATTRIBUTE_ID, VALUE);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void putUserAttributeValueWithInvalidSimpleUserAttributeKeyFails() {
		userAttributeValueService.putUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, null, SIMPLE_ATTRIBUTE_ID, VALUE);
	}
	
	@Test
	public void putUserAttributeValueSucceeds() {
		userAttributeValueService.putUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, UUID.randomUUID().toString(), COMPOUND_SIMPLE_ATTRIBUTE_ID, VALUE);
	}
	
	@Test
	public void putUserAttributeValueWithNullInstanceIdentifierSucceeds() {
		userAttributeValueService.putUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, null, COMPOUND_SIMPLE_ATTRIBUTE_ID, VALUE);
	}
	
	@Test
	public void putUserAttributeValueMoreThanOnceSucceeds() {
		IUserAttributeValue userAttributeValue = userAttributeValueService.putUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, null, COMPOUND_SIMPLE_ATTRIBUTE_ID, VALUE);
		String newValue = "VALUE2";
		userAttributeValueService.putUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, userAttributeValue.getInstanceIdentifier(), COMPOUND_SIMPLE_ATTRIBUTE_ID, newValue);
		Set<IUserAttributeValue> values = userAttributeValueService.findUserAttributeValues(userIdentifier);
		assertEquals(1, values.size());
		assertEquals(newValue, values.iterator().next().getValue());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void putUserAttributeValueWithNullValueFails() {
		userAttributeValueService.putUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, null, COMPOUND_SIMPLE_ATTRIBUTE_ID, null);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void putUserAttributeValueWithSimpleCompoundUserAttributeKeyFails() {
		userAttributeValueService.putUserAttributeValue(userIdentifier, SIMPLE_ATTRIBUTE_ID, null, COMPOUND_SIMPLE_ATTRIBUTE_ID, VALUE);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void putUserAttributeValueWithCompoundSimpleAttributeKeyFails() {
		userAttributeValueService.putUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, null, COMPOUND_ATTRIBUTE_ID, VALUE);
	}
	
	@Test(expected = AuthenticationException.class)
	public void putUserAttributeValueNonAuthenticatedFails() {
		establishNonAuthenticatedSecurityContext();
		userAttributeValueService.putUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, null, COMPOUND_SIMPLE_ATTRIBUTE_ID, VALUE);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void removeWithNullUserAttributeValueFails() {
		userAttributeValueService.remove(null);
	}
	
	@Test
	public void removeWithUserAttributeValueSucceeds() {
		IUserAttributeValue userAttributeValue = userAttributeValueService.putUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, null, COMPOUND_SIMPLE_ATTRIBUTE_ID, VALUE);
		userAttributeValueService.remove(userAttributeValue);
	}
	
	@Test
	public void removeMoreThanOnceSucceeds() {
		IUserAttributeValue userAttributeValue = userAttributeValueService.putUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, null, COMPOUND_SIMPLE_ATTRIBUTE_ID, VALUE);
		userAttributeValueService.remove(userAttributeValue);
		userAttributeValueService.remove(userAttributeValue);
	}

	@Test(expected = AuthenticationException.class)
	public void removeNonAuthenicatedFails() {
		IUserAttributeValue userAttributeValue = userAttributeValueService.putUserAttributeValue(userIdentifier, COMPOUND_ATTRIBUTE_ID, null, COMPOUND_SIMPLE_ATTRIBUTE_ID, VALUE);
		establishNonAuthenticatedSecurityContext();
		userAttributeValueService.remove(userAttributeValue);
	}
}
