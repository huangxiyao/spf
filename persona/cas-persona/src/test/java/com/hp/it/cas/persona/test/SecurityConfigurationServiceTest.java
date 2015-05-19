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
import org.springframework.security.AccessDeniedException;

import com.hp.it.cas.persona.configuration.service.IApplication;
import com.hp.it.cas.persona.configuration.service.IPermission;
import com.hp.it.cas.persona.configuration.service.IUserAttribute;

public class SecurityConfigurationServiceTest extends AbstractPersonaTest {

    private static final String ATTRIBUTE_ID = "ID";
    private static final String ATTRIBUTE_ID_2 = "ID2";
	private static final String ATTRIBUTE_NAME = "NM";
	private static final String ATTRIBUTE_NAME_2 = "NM2";
	private static final String ATTRIBUTE_DESCRIPTION = "Description";
	private static final String ATTRIBUTE_DEFINITION = "Definition";
	
	@Before
	public void setupBeforeSecurityConfigurationServiceTest() {
		establishAdminSecurityContext();
	}
	
	@Test
	public void findApplicationWithValidApplicationPortfolioIdentifierReturnsApplication() {
		IApplication application = securityConfigurationService.findApplication(APPLICATION_PORTFOLIO_IDENTIFIER);
		assertNotNull(application);
	}
	
	@Test
	public void findApplicationWithInvalidApplicationPortfolioIdentifierReturnsNull() {
		IApplication application = securityConfigurationService.findApplication(0);
		assertNull(application);
	}

	@Test(expected = IllegalArgumentException.class)
	public void addPermissionWithNullApplicationFails() {
		IUserAttribute attribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		securityConfigurationService.addPermission(null, attribute);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addPermissionWithNullUserAttributeFails() {
		IApplication application = securityConfigurationService.findApplication(APPLICATION_PORTFOLIO_IDENTIFIER);
		securityConfigurationService.addPermission(application, null);
	}
	
	@Test
	public void addPermissionWithValidArgumentsSucceeds() {
		IApplication application = securityConfigurationService.findApplication(APPLICATION_PORTFOLIO_IDENTIFIER);
		IUserAttribute attribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IPermission permission = securityConfigurationService.addPermission(application, attribute);
		IPermission foundPermission = securityConfigurationService.findPermissions(application).iterator().next();
		assertEquals(permission.getApplication(), foundPermission.getApplication());
		assertEquals(permission.getCompoundUserAttribute(), foundPermission.getCompoundUserAttribute());
		assertEquals(permission.getSimpleUserAttribute(), foundPermission.getSimpleUserAttribute());
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void addPermissionMoreThanOnceFails() {
		IApplication application = securityConfigurationService.findApplication(APPLICATION_PORTFOLIO_IDENTIFIER);
		IUserAttribute attribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		securityConfigurationService.addPermission(application, attribute);
		securityConfigurationService.addPermission(application, attribute);
	}
	
	@Test(expected = AccessDeniedException.class)
	public void addPermissionWithNonAdminRoleFails() {
		establishUserSecurityContext();
		IApplication application = securityConfigurationService.findApplication(APPLICATION_PORTFOLIO_IDENTIFIER);
		IUserAttribute attribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		securityConfigurationService.addPermission(application, attribute);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addCompoundPermissionWithNullApplicationFails() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME_2, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		securityConfigurationService.addPermission(null, compoundUserAttribute, simpleUserAttribute);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addCompoundPermissionWithNullCompoundAttributeFails() {
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME_2, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		securityConfigurationService.addPermission(application, null, simpleUserAttribute);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void addCompoundPermissionWithNullSimpleAttributeFails() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		securityConfigurationService.addPermission(application, compoundUserAttribute, null);
	}
	
	@Test(expected = DataIntegrityViolationException.class)
	public void addCompoundPermissionWithSwappedSimpleCompoundAttributeFails() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME_2, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		securityConfigurationService.addPermission(application, simpleUserAttribute, compoundUserAttribute);
	}
	
	@Test
	public void addCompoundPermissionWithValidArgumentsSucceeds() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME_2, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		securityConfigurationService.addPermission(application, compoundUserAttribute, simpleUserAttribute);
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void addCompoundPermissionMoreThanOnceFails() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME_2, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		securityConfigurationService.addPermission(application, compoundUserAttribute, simpleUserAttribute);
		securityConfigurationService.addPermission(application, compoundUserAttribute, simpleUserAttribute);
	}

	@Test(expected = AccessDeniedException.class)
	public void addCompoundPermissionWithNonAdminRoleFails() {
		establishUserSecurityContext();
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME_2, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		securityConfigurationService.addPermission(application, compoundUserAttribute, simpleUserAttribute);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void findPermissionsWithNullApplicationFails() {
		securityConfigurationService.findPermissions(null);
	}
	
	@Test
	public void findPermissionsWithApplicationWithoutPermissionsReturnsEmptySet() {
		Set<IPermission> permissions = securityConfigurationService.findPermissions(application);
		assertNotNull(permissions);
		assertTrue(permissions.isEmpty());
	}
	
	@Test
	public void findPermissionsSucceeds() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME_2, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IPermission permission = securityConfigurationService.addPermission(application, compoundUserAttribute, simpleUserAttribute);
		Set<IPermission> permissions = securityConfigurationService.findPermissions(application);
		assertFalse(permissions.isEmpty());
		IPermission foundPermission = permissions.iterator().next();
		assertEquals(application, foundPermission.getApplication());
		assertEquals(compoundUserAttribute, foundPermission.getCompoundUserAttribute());
		assertEquals(simpleUserAttribute, foundPermission.getSimpleUserAttribute());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void removePermissionWithNullPermissionFails() {
		securityConfigurationService.removePermission(null);
	}
	
	@Test
	public void removeCompoundSimplePermissionSucceeds() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME_2, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IPermission permission = securityConfigurationService.addPermission(application, compoundUserAttribute, simpleUserAttribute);
		securityConfigurationService.removePermission(permission);
		Set<IPermission> permissions = securityConfigurationService.findPermissions(application);
		assertTrue(permissions.isEmpty());
	}
	
	@Test
	public void removeCompoundPermissionSucceeds() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IPermission permission = securityConfigurationService.addPermission(application, compoundUserAttribute);
		securityConfigurationService.removePermission(permission);
		Set<IPermission> permissions = securityConfigurationService.findPermissions(application);
		assertTrue(permissions.isEmpty());
	}
	
	@Test
	public void removeSimplePermissionSucceeds() {
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME_2, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IPermission permission = securityConfigurationService.addPermission(application, simpleUserAttribute);
		securityConfigurationService.removePermission(permission);
		Set<IPermission> permissions = securityConfigurationService.findPermissions(application);
		assertTrue(permissions.isEmpty());
	}
	
	@Test
	public void removePermissionMoreThanOnceSucceeds() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME_2, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IPermission permission = securityConfigurationService.addPermission(application, compoundUserAttribute, simpleUserAttribute);
		securityConfigurationService.removePermission(permission);
		securityConfigurationService.removePermission(permission);
	}

	@Test(expected = AccessDeniedException.class)
	public void removePermissionWithNonAdminRoleFails() {
		IUserAttribute compoundUserAttribute = metadataConfigurationService.addCompoundUserAttribute(ATTRIBUTE_ID, ATTRIBUTE_NAME, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IUserAttribute simpleUserAttribute = metadataConfigurationService.addSimpleUserAttribute(ATTRIBUTE_ID_2, ATTRIBUTE_NAME_2, ATTRIBUTE_DESCRIPTION, ATTRIBUTE_DEFINITION);
		IPermission permission = securityConfigurationService.addPermission(application, compoundUserAttribute, simpleUserAttribute);
		establishUserSecurityContext();
		securityConfigurationService.removePermission(permission);
	}
}
