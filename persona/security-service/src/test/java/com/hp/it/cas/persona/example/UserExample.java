package com.hp.it.cas.persona.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.user.service.ICompoundUserAttributeValue;
import com.hp.it.cas.persona.user.service.IUser;

public class UserExample extends AbstractExample implements Runnable {

	private static final EUserIdentifierType USER_IDENTIFIER_TYPE = EUserIdentifierType.EXTERNAL_USER;
	private static final String USER_IDENTIFIER = "1234";
	
	private void populate() {
		IUser user = userService.createUser(USER_IDENTIFIER_TYPE, USER_IDENTIFIER);
		System.out.println("User: " + user);
		
		Map<String, Collection<String>> simpleAttributeValues = user.getSimpleAttributeValues();
		System.out.println("Initial simple attributes: " + simpleAttributeValues);
		
		user.addSimpleAttributeValue(TITLE, "Mr.");
		System.out.println("User attributes: " + user.getSimpleAttributeValues());
		
		user.addSimpleAttributeValue(NAME, "Snoopy");
		System.out.println("User with pet: " + user);
		System.out.println("User attributes: " + user.getSimpleAttributeValues());
		System.out.println("User pets: " + user.getSimpleAttributeValues().get(PET_NAME));
		
		// add to the collection of PET_NAME directly, equivalent to user.addSimpleAttributeValue(PET_NAME, "Red Baron")
		simpleAttributeValues.get(PET_NAME).add("Red Baron");
		System.out.println("User attributes: " + user.getSimpleAttributeValues());
		System.out.println("User pets: " + user.getSimpleAttributeValues().get(PET_NAME));
		
		Map<String, Collection<ICompoundUserAttributeValue>> compoundAttributeValues = user.getCompoundAttributeValues();
		System.out.println("Initial compound attributes: " + compoundAttributeValues);
		
		ICompoundUserAttributeValue name = user.addCompoundAttributeValue(NAME);
		name.put(GIVEN_NAME, "Charlie");
		name.put(FAMILY_NAME, "Brown");
		System.out.println("User compound attributes: " + user.getCompoundAttributeValues());
	}
	
	private void use() {
		IUser user = userService.createUser(USER_IDENTIFIER_TYPE, USER_IDENTIFIER);
		Map<String, Collection<String>> simpleAttributeValues = user.getSimpleAttributeValues();
		Map<String, Collection<ICompoundUserAttributeValue>> compoundAttributeValues = user.getCompoundAttributeValues();
		
		Map<String, String> name = compoundAttributeValues.get(NAME).iterator().next();
		Collection<String> petNames = simpleAttributeValues.get(PET_NAME);
		
		System.out.println(
				simpleAttributeValues.get(TITLE).iterator().next()
				+ " " + name.get(GIVEN_NAME)
				+ " " + name.get(FAMILY_NAME)
				+ " has " + petNames.size() + " pets"
				+ " named " + petNames
		);
	}
	
	@Transactional
	public void run() {
		configure();

		establishSecurityContext(USER);		
		populate();
		use();
	}
	
	public static void main(String[] args) {
		boolean mockSecurity		= false;
		boolean mockDao				= false;
		
		String directory			= "src/test/resources/example/";
		String applicationContext	= directory + "user-example.xml";
		String personaContext		= directory + "persona-services.xml";
		String securityContext		= directory + "application-security.xml";
		String jdbcDaoContext		= directory + "jdbc-dao.xml";
		
		String mockDaoContext		= directory + "mock-dao.xml";
		String mockSecurityContext	= directory + "mock-security.xml";
		
		Collection<String> contexts = new ArrayList<String>();
		contexts.add(applicationContext);
		contexts.add(personaContext);
		contexts.add(mockDao ? mockDaoContext : jdbcDaoContext);
		contexts.add(securityContext);
		if (mockSecurity) {
			contexts.add(mockSecurityContext);
		}
		
		// load & go
		BeanFactory beanFactory = new FileSystemXmlApplicationContext(contexts.toArray(new String[contexts.size()]));
		Runnable application = (Runnable) beanFactory.getBean("application");
		application.run();
	}
}
