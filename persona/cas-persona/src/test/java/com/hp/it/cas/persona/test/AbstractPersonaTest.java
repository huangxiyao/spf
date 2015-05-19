package com.hp.it.cas.persona.test;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.security.context.SecurityContextHolder;

import com.hp.it.cas.persona.configuration.service.IApplication;
import com.hp.it.cas.persona.configuration.service.IMetadataConfigurationService;
import com.hp.it.cas.persona.configuration.service.ISecurityConfigurationService;
import com.hp.it.cas.persona.mock.dao.MockDatabase;
import com.hp.it.cas.persona.mock.security.MockAuthenticationProvider;
import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.uav.service.IUserAttributeValueService;
import com.hp.it.cas.persona.user.service.IUserService;
import com.hp.it.cas.xa.security.IApplicationPrincipal;

public abstract class AbstractPersonaTest {

    private static final Logger logger = LoggerFactory.getLogger(AbstractPersonaTest.class);
    
	protected static final int APPLICATION_PORTFOLIO_IDENTIFIER
		= ((IApplicationPrincipal) MockAuthenticationProvider.getApplicationAuthentication().getPrincipal()).getApplicationPortfolioIdentifier();

	protected static final EUserIdentifierType USER_IDENTIFIER_TYPE = EUserIdentifierType.EXTERNAL_USER;
	protected static final String USER_IDENTIFIER = "1234";
	protected static final String FIRST_NAME = "John";
	protected static final String WORK_CITY = "Atlanta";
	protected static final String WORK_STATE = "GA";
	
    protected static IMetadataConfigurationService metadataConfigurationService;
    protected static ISecurityConfigurationService securityConfigurationService;
    protected static IUserAttributeValueService userAttributeValueService;
    protected static IUserService userService;
    protected static IUserService standaloneUserService;
    
	private static MockDatabase mockDatabase;
	private static FileSystemXmlApplicationContext springContext;
	protected IApplication application;
	
	@BeforeClass
	public static void setupBeforeClass() {
		String directory			= "src/test/resources/";
		String testContext			= directory + "test-context.xml";
		String personaContext		= directory + "persona-services.xml";
		String securityContext		= directory + "application-security.xml";
		String mockDaoContext		= directory + "mock-dao.xml";
		String mockSecurityContext	= directory + "mock-security.xml";
//      String mockDaoContext       = directory + "jdbc-dao.xml";

		Collection<String> contexts = new ArrayList<String>();
		contexts.add(testContext);
		contexts.add(personaContext);
		contexts.add(mockDaoContext);
		contexts.add(securityContext);
		contexts.add(mockSecurityContext);
		
		try {
	        springContext = new FileSystemXmlApplicationContext(contexts.toArray(new String[contexts.size()]));
	        metadataConfigurationService = (IMetadataConfigurationService) springContext.getBean("metadataConfigurationService");
	        securityConfigurationService = (ISecurityConfigurationService) springContext.getBean("securityConfigurationService");
	        userAttributeValueService = (IUserAttributeValueService) springContext.getBean("userAttributeValueService");
	        userService = (IUserService) springContext.getBean("userService");
	        standaloneUserService = (IUserService) springContext.getBean("standaloneUserService");
	        mockDatabase = (MockDatabase) springContext.getBean("database");
		} catch (RuntimeException e) {
		    logger.error("Unable to setup test.", e);
		    throw e;
		}
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		if (springContext != null) {
		    springContext.close();
		}
	}
	
	@Before
	public void setupBeforeTest() {
		mockDatabase.reset();
		establishUserSecurityContext();
		application = securityConfigurationService.findApplication(APPLICATION_PORTFOLIO_IDENTIFIER);
		SecurityContextHolder.clearContext();
	}

	protected void establishAdminSecurityContext() {
		SecurityContextHolder.getContext().setAuthentication(MockAuthenticationProvider.getAdminAuthentication());
	}

	protected void establishUserSecurityContext() {
		SecurityContextHolder.getContext().setAuthentication(MockAuthenticationProvider.getUserAuthentication());
	}
	
	protected void establishNonAuthenticatedSecurityContext() {
		SecurityContextHolder.getContext().setAuthentication(MockAuthenticationProvider.getAnonymousAuthentication());
	}
}
