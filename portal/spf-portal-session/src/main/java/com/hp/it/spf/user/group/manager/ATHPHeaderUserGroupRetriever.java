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

import com.hp.it.spf.sso.portal.AtHPAuthenticator;
import com.hp.it.spf.sso.portal.AuthenticationConsts;
import com.hp.it.spf.sso.portal.AuthenticatorHelper;
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
public class ATHPHeaderUserGroupRetriever implements IUserGroupRetriever {
    private static final LogWrapper LOG = AuthenticatorHelper.getLog(ATHPHeaderUserGroupRetriever.class);

    /**
     * Retrieve user groups from ATHP header.
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

        // retrieve groups from http header
        String groupName = AuthenticatorHelper.getProperty(AtHPAuthenticator.class, AuthenticationConsts.HEADER_GROUP_NAME);
        String groupstring = AuthenticatorHelper.getRequestHeader(request, groupName);
        // groups are divided by ,
        if (groupstring != null) {
            StringTokenizer st = new StringTokenizer(groupstring, "^");
            while (st.hasMoreElements()) {
                String temp = (String)st.nextElement();
                if (temp.toLowerCase().startsWith(AuthenticationConsts.ATHP_GROUP_PREFIX)) {
                    String group = temp.substring(3, temp.indexOf(','));
                    if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                        LOG.debug("Get UserGroup = " + group);
                    }
                    groups.add(group);
                }
            }
        }
        return groups;
    }
}
