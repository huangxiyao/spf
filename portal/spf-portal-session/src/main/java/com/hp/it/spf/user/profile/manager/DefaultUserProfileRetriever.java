/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.user.profile.manager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * This is a concrete final class of IUserProfileRetriever interface to return a
 * default user profile map (usually is a empty map)
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public final class DefaultUserProfileRetriever implements IUserProfileRetriever {
    /**
     * Retrieve an empty user profiles map
     *
     * @param userIdentifier user identifier
     * @param request HttpServletRequest
     * @return an empty map
     */
    public Map<String, Object> getUserProfile(String userIdentifier,
                                              HttpServletRequest request) {
        return new HashMap<String, Object>();
    }
}
