package com.hp.it.spf.user.group.manager;


public class UserGroupRetrieverFactory {
	/**
	 * create a concrete UserGroupsImpl class according to parameters
	 * @param params
	 * @return
	 */
	public static IUserGroupRetriever createUserGroupImpl() {
		return new SSOUserGroupRetriever();
	}
}
