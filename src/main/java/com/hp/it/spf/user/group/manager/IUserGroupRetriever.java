/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.group.manager;

import java.util.Map;
import java.util.Set;

import com.hp.it.spf.user.exception.UserGroupsException;

/**
 * User group operation interface which defines some methods to interact with
 * UGS webservice
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public interface IUserGroupRetriever {
    /**
     * Retrieve specified user groups.
     * 
     * @param siteName current site's DNS name in session
     * @param userProfile user profile map
     * @return user groups set
     * @throws UserGroupsException if any exception occurs, an
     *             UserGroupsException will be thrown.
     */
    public Set<String> getGroups(String siteName,
                                 Map<String, Object> userProfile) throws UserGroupsException;
}
