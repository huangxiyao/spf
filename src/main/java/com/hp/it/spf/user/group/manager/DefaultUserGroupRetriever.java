/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.group.manager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
     * @param siteName site name
     * @param userProfile user profiles
     * @return user groups set, if user has no groups, an empty Set will be
     *         returned.
     */    
    public Set<String> getGroups(String siteName,
                                 Map<String, Object> userProfile){
        return new HashSet<String>();
    }
}
