/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.user.profile.manager;

/**
 * This is a factory class to create a concrete implementation
 * of IUserProfileRetriever interface.
 * 
 * @author  wuyingzh
 * @version 1.0
 */
public class UserProfileRetrieverFactory {
	/**
	 * create a concrete IUserProfileRetriever class
	 * 
	 * @return a implementation class of IUserProfileRetriever
	 */
	public static IUserProfileRetriever createUserProfileImpl() {
		return new PersonaUserProfileRetriever();
	}
}
