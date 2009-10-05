/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.group.manager;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.user.exception.UserGroupsException;

/**
 * User group operation interface which defines some methods to retrieve user
 * groups.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public interface IUserGroupRetriever {
    /**
     * Retrieve specified user groups.
     * 
     * @param userProfile user profile map
     * @param request HttpServletRequest
     * @return user groups set, if user has no groups, an empty Set will be
     *         returned.
     * @throws UserGroupsException if any exception occurs, an
     *             UserGroupsException will be thrown.
     */
    public Set<String> getGroups(Map<String, Object> userProfile,
                                 HttpServletRequest request) throws UserGroupsException;
}
