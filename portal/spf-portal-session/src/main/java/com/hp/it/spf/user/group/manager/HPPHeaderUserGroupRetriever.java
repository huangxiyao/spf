/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.group.manager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.sso.portal.AuthenticationConsts;
import com.hp.it.spf.sso.portal.AuthenticatorHelper;
import com.hp.it.spf.sso.portal.HPPAuthenticator;
import com.hp.it.spf.user.exception.UserGroupsException;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;

/**
 * This is the implimentation class of <tt>IUserGroupRetriever</tt>.
 * <p>
 * This class retrieve user groups from HPP http header.
 * </p>
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class HPPHeaderUserGroupRetriever implements IUserGroupRetriever {
    private static final LogWrapper LOG = AuthenticatorHelper.getLog(HPPHeaderUserGroupRetriever.class);

    /**
     * Retrieve user groups from HPP header.
     * 
     * @param userProfile user profiles
     * @param request HttpServletRequest
     * @return user groups set, if user has no groups, an empty Set will be
     *         returned.
     * @throws UserGroupsException if any exception occurs, an
     *             UserGroupsException will be thrown.
     */
    public Set<String> getGroups(Map<String, Object> userProfile,
                                 HttpServletRequest request) throws UserGroupsException {
        Set<String> groups = new HashSet<String>();
        String groupName = AuthenticatorHelper.getProperty(HPPAuthenticator.class, AuthenticationConsts.HEADER_GROUP_NAME);
        String groupString = AuthenticatorHelper.getRequestHeader(request, groupName, true);
        
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("The groups string got from HPP request header is: "
                      + groupString);
        }
        if (groupString != null) {
            StringTokenizer st = new StringTokenizer(groupString, "|");
            while (st.hasMoreTokens()) {
                groups.add(st.nextToken());
            }
        }
        return groups;
    }
}
