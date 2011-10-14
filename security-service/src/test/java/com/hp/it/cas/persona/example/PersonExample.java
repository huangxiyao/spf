package com.hp.it.cas.persona.example;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.user.service.IUser;

/**
 * Demonstrates the use of the {@link Person} wrapper for {@link com.hp.it.cas.persona.user.service.IUser}.
 *
 * @author Quintin May
 */
public class PersonExample extends AbstractExample implements Runnable {

	private void use() {
		IUser user = userService.createUser(EUserIdentifierType.EXTERNAL_USER, "1234");
		Person person = new Person(user);
		
		person.setFamilyName("van Pelt");
		person.setGivenName("Linus");
		System.out.println("Name is: " + person.getGivenName() + " " + person.getFamilyName());
		
		Collection<String> contracts = person.getServiceAgreementIdentifiers();
		contracts.add("ABC");
		contracts.add("DEF");
		contracts.add("GHI");
		
		System.out.println("Person has 3 contracts: " + person.getServiceAgreementIdentifiers());
		
		contracts.remove("DEF");
		System.out.println("Remove contract 'DEF': " + person.getServiceAgreementIdentifiers());
	}

	@Transactional
	public void run() {
		configure();
		
		establishSecurityContext(USER);
		use();
	}
	
	public static void main(String[] args) {
		boolean mockSecurity		= true;
		boolean mockDao				= true;
		
		String directory			= "src/test/resources/example/";
		String applicationContext	= directory + "person-example.xml";
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
