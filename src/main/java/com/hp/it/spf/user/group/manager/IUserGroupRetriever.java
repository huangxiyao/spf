package com.hp.it.spf.user.group.manager;

import java.util.List;
import java.util.Map;

import com.hp.it.spf.user.exception.UserGroupsException;

/**
 * User group operation interface which defines some methods to interact with
 * UGS webservice
 * 
 * @author  wuyingzh
 * @version 1.0
 */
public interface IUserGroupRetriever {
	/**
	 * Retrieve specified user groups 
	 * 
	 * @param userProfile          user profile map
	 * @param ruleSetId
	 * 
	 * @return                     user group list
	 * @throws UserGroupsException user group exception
	 */
	List<String> getGroups(Map<String, String> userProfile, String ruleSetId) throws UserGroupsException;
}
