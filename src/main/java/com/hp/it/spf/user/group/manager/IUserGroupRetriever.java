package com.hp.it.spf.user.group.manager;

import java.util.Map;
import java.util.Set;

import com.hp.it.spf.user.exception.UserGroupsException;

/**
 * User group operation interface which defines some methods to interact with
 * UGS webservice
 * 
 * @author wuyingzh
 * @version 1.0
 */
public interface IUserGroupRetriever {
    /**
     * Retrieve specified user groups
     * 
     * @param siteName
     *            the name of the site which is visited by user
     * @param userProfile
     *            user profile map
     * @return user groups set
     * @throws UserGroupsException
     *             if any exception occurs, an UserGroupsException will be
     *             thrown
     */
    public Set<String> getGroups(String siteName,
                                 Map<String, String> userProfile) throws UserGroupsException;
}
