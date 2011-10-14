package com.hp.it.cas.persona.example;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.user.service.IUser;
import com.hp.it.cas.persona.user.service.IUserService;

public class StandaloneUserServiceExample {

	private IUserService userService;

	@Before
	public void setup() {
		BeanFactory beanFactory = new FileSystemXmlApplicationContext("src/test/resources/example/StandaloneUserService-datasource.xml");
		userService = (IUserService) beanFactory.getBean("standaloneUserService");
	}

	@Test
	public void userCanBeRetrieved() {
		IUser user = userService.createUser(EUserIdentifierType.EXTERNAL_USER, "1234");
		assertNotNull(user);

		System.out.println(user);
		System.out.println(user.getSimpleAttributeValues());
		System.out.println(user.getCompoundAttributeValues());
	}
}
