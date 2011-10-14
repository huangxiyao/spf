package com.hp.it.cas.persona.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

import com.hp.it.cas.persona.configuration.service.IPermission;
import com.hp.it.cas.persona.configuration.service.IUserAttribute;
import com.hp.it.cas.persona.configuration.service.impl.MetadataConfigurationService;
import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.uav.service.IUserIdentifier;
import com.hp.it.cas.persona.uav.service.UserIdentifier;

public class MetadataConfigurationServiceTest extends AbstractPersonaTest {

    private static final String ATTRIBUTE_ID = "NM";
    private static final String ATTRIBUTE_ID_2 = "NM2";
	private static final String ATTRIBUTE_NAME = "Name";
	private static final String ATTRIBUTE_NAME_2 = "Name 2";
	private static final String ATTRIBUTE_DESCRIPTION = "Description";
	private static final String ATTRIBUTE_DEFINITION = "Definition";
	
	@Before
	public void setupBeforeMetadataConfigurationServiceTest() {
		establishAdminSecurityContext();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void nullConstructorArgumentFails()
	{
		new MetadataConfigurationService(null);
	}
	
	@Test
	public void freshDatabaseHasNoAttributes() {
		assertTrue(metadataConfigurationService.getCompoundUserAttributes().isEmpty());
		assertTrue(metadataConfigurationService.getSimpleUserAttributes().isEmpty());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addSimpleUserAttributeWithNullArgumentsFails() {
		metadataConfigurationService.addSimpleUserAttribute(null, null, null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addCompoundUserAttributeWithNullArgumentsFails() {
		metadataConfigurationService.addCompoundUserAttribute(null, null, null, null);
	}

	@Test
	public void addSimpleUserAttributeAddsAttribute() {
		IUserAttribute attribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		checkAttribute(attribute);
		Set<IUserAttribute> attributes = metadataConfigurationService.getSimpleUserAttributes();
		assertEquals(1, attributes.size());
		assertEquals(attribute, attributes.iterator().next());
		assertTrue(metadataConfigurationService.getCompoundUserAttributes().isEmpty());
	}

	@Test
	public void addCompoundUserAttributeAddsAttribute() {
		IUserAttribute attribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		checkAttribute(attribute);
		Set<IUserAttribute> attributes = metadataConfigurationService.getCompoundUserAttributes();
		assertEquals(1, attributes.size());
		assertEquals(attribute, attributes.iterator().next());
		assertTrue(metadataConfigurationService.getSimpleUserAttributes().isEmpty());
	}
	
	private void checkAttribute(IUserAttribute attribute) {
		assertNotNull(attribute);
		assertNotNull(attribute.getUserAttributeIdentifier());
		assertEquals(ATTRIBUTE_NAME, attribute.getUserAttributeName());
		assertEquals(ATTRIBUTE_DESCRIPTION, attribute.getUserAttributeDescription());
		assertEquals(ATTRIBUTE_DEFINITION, attribute.getUserAttributeDefinition());
		
		IUserAttribute foundAttribute = metadataConfigurationService.findUserAttribute(attribute.getUserAttributeIdentifier());
		assertNotNull(foundAttribute);
		assertEquals(attribute, foundAttribute);
		
		IUserAttribute foundByNameAttribute = metadataConfigurationService.findUserAttributeByName(ATTRIBUTE_NAME);
		assertNotNull(foundByNameAttribute);
		assertEquals(attribute, foundByNameAttribute);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void addSimpleAttributeWithSameNameFails() {
		metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
	}
	
	@Test
	public void editSimpleAttributeSavesChanges() {
		IUserAttribute attribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID, "NAME", "Desc", "Defn");
		attribute.setUserAttributeName(ATTRIBUTE_NAME);
		attribute.setUserAttributeDescription(ATTRIBUTE_DESCRIPTION);
		attribute.setUserAttributeDefinition(ATTRIBUTE_DEFINITION);
		attribute = metadataConfigurationService.putUserAttribute(attribute);
		checkAttribute(attribute);
		assertNull(metadataConfigurationService.findUserAttributeByName("NAME"));
	}

	@Test
	public void editCompoundAttributeSavesChanges() {
		IUserAttribute attribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, "NAME", "Desc", "Defn");
		attribute.setUserAttributeName(ATTRIBUTE_NAME);
		attribute.setUserAttributeDescription(ATTRIBUTE_DESCRIPTION);
		attribute.setUserAttributeDefinition(ATTRIBUTE_DEFINITION);
		attribute = metadataConfigurationService.putUserAttribute(attribute);
		checkAttribute(attribute);
		assertNull(metadataConfigurationService.findUserAttributeByName("NAME"));
	}
	
	@Test
	public void removeUserAttributeRemovesAttribute() {
		IUserAttribute attribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID, "NAME", "Desc", "Defn");
		metadataConfigurationService.removeUserAttribute(attribute);
		assertTrue(metadataConfigurationService.getSimpleUserAttributes().isEmpty());
	}
	
	@Test
	public void findUserInvalidUserAttributeReturnsNull() {
		IUserAttribute attribute = metadataConfigurationService.findUserAttribute("999");
		assertNull(attribute);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addCompoundAttributeSimpleAttributeWithNullCompoundAttributeFails() {
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(null, simpleUserAttribute);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addCompoundAttributeSimpleAttributeWithNullSimpleAttributeFails() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(compoundUserAttribute, null);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void addCompoundAttributeSimpleAttributeWithSimpleCompoundAttributeFails() {
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(simpleUserAttribute, simpleUserAttribute);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void addCompoundAttributeSimpleAttributeWithCompoundSimpleAttributeFails() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(compoundUserAttribute, compoundUserAttribute);
	}
	
	@Test
	public void addCompoundAttributeSimpleAttributeSucceeds() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME_2, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(compoundUserAttribute, simpleUserAttribute);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void addCompoundAttributeSimpleAttributeMoreThanOnceFails() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(compoundUserAttribute, simpleUserAttribute);
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(compoundUserAttribute, simpleUserAttribute);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void findSimpleAttributesWithNullArgumentFails() {
		metadataConfigurationService.findSimpleAttributes(null);
	}
	
	@Test
	public void findSimpleAttributesWithCompoundHavingNoSimpleReturnsEmpty() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		Set<IUserAttribute> simples = metadataConfigurationService.findSimpleAttributes(compoundUserAttribute);
		assertNotNull(simples);
		assertTrue(simples.isEmpty());
	}

	@Test
	public void findSimpleAttributesWithCompoundHavingSimpleReturnsSimple() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME_2, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(compoundUserAttribute, simpleUserAttribute);
		Set<IUserAttribute> simples = metadataConfigurationService.findSimpleAttributes(compoundUserAttribute);
		assertEquals(1, simples.size());
		assertEquals(simpleUserAttribute, simples.iterator().next());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void findCompoundAttributesWithNullArgumentFails() {
		metadataConfigurationService.findCompoundAttributes(null);
	}
	
	@Test
	public void findCompoundAttributesWithSimpleHavingNoCompoundReturnsEmpty() {
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		Set<IUserAttribute> compounds = metadataConfigurationService.findCompoundAttributes(simpleUserAttribute);
		assertNotNull(compounds);
		assertTrue(compounds.isEmpty());
	}

	@Test
	public void findCompoundAttributesWithSimpleHavingCompoundReturnsCompound() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME_2, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(compoundUserAttribute, simpleUserAttribute);
		Set<IUserAttribute> compounds = metadataConfigurationService.findCompoundAttributes(simpleUserAttribute);
		assertEquals(1, compounds.size());
		assertEquals(compoundUserAttribute, compounds.iterator().next());
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeCompoundAttributeSimpleAttributeWithNullCompoundAttributeFails() {
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		metadataConfigurationService.removeCompoundAttributeSimpleAttribute(null, simpleUserAttribute);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void removeCompoundAttributeSimpleAttributeWithNullSimpleAttributeFails() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		metadataConfigurationService.removeCompoundAttributeSimpleAttribute(compoundUserAttribute, null);
	}
	
	@Test
	public void removeCompoundAttributeSimpleAttributeWithNonChildSucceeds() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME_2, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		metadataConfigurationService.removeCompoundAttributeSimpleAttribute(compoundUserAttribute, simpleUserAttribute);
	}
	
	@Test
	public void removeCompoundAttributeSimpleAttributeWithChildSucceeds() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME_2, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(compoundUserAttribute, simpleUserAttribute);
		Set<IUserAttribute> simples = metadataConfigurationService.findSimpleAttributes(compoundUserAttribute);
		Set<IUserAttribute> compounds = metadataConfigurationService.findCompoundAttributes(simpleUserAttribute);
		assertEquals(simpleUserAttribute, simples.iterator().next());
		assertEquals(compoundUserAttribute, compounds.iterator().next());

		metadataConfigurationService.removeCompoundAttributeSimpleAttribute(compoundUserAttribute, simpleUserAttribute);
		simples = metadataConfigurationService.findSimpleAttributes(compoundUserAttribute);
		compounds = metadataConfigurationService.findCompoundAttributes(simpleUserAttribute);
		assertTrue(simples.isEmpty());
		assertTrue(compounds.isEmpty());
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void removeCompoundAttributeSimpleAttributeWithInUseFails() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME_2, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(compoundUserAttribute, simpleUserAttribute);
		
		securityConfigurationService.addPermission(application, compoundUserAttribute, simpleUserAttribute);
		
		IUserIdentifier userIdentifier = new UserIdentifier(EUserIdentifierType.EMPLOYEE, "123456");
		userAttributeValueService.putUserAttributeValue(userIdentifier, compoundUserAttribute.getUserAttributeIdentifier(), null, simpleUserAttribute.getUserAttributeIdentifier(), "John");
		metadataConfigurationService.removeCompoundAttributeSimpleAttribute(compoundUserAttribute, simpleUserAttribute);
	}
	
	@Test
	public void removeCompoundAttributeSimpleAttributeWithPermissionsDeletesPermissions() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME_2, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		metadataConfigurationService.addCompoundAttributeSimpleAttribute(compoundUserAttribute, simpleUserAttribute);
		
		securityConfigurationService.addPermission(application, compoundUserAttribute, simpleUserAttribute);
		
		Set<IPermission> permissions = securityConfigurationService.findPermissions(application);
		assertFalse(permissions.isEmpty());
		metadataConfigurationService.removeCompoundAttributeSimpleAttribute(compoundUserAttribute, simpleUserAttribute);
		permissions = securityConfigurationService.findPermissions(application);
		assertTrue(permissions.isEmpty());
	}

}
