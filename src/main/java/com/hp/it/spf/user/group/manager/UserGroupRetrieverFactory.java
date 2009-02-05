/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.group.manager;

/**
 * This is factory class to create impl class of <tt>IUserGroupRetriever</tt>.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class UserGroupRetrieverFactory {
	/**
	 * create a concrete UserGroupsImpl class
	 * 
	 * @return a implimentation class of IUserGroupRetriever
	 */
	public static IUserGroupRetriever createUserGroupImpl(String siteName) {
		return new SSOUserGroupRetriever();
	}
}
