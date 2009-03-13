/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.group.manager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * This is the defalut implimentation class of <tt>IUserGroupRetriever</tt>.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class DefaultUserGroupRetriever implements IUserGroupRetriever {
    /**
     * Return an empty user groups set
     * 
     * @param userProfile user profiles
     * @param request HttpServletRequest
     * @return user groups set, if user has no groups, an empty Set will be
     *         returned.
     */    
    public Set<String> getGroups(Map<String, Object> userProfile,
                                 HttpServletRequest request){
        return new HashSet<String>();
    }
}
