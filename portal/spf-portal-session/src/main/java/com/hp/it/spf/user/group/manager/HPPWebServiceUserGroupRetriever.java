/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.group.manager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.hp.globalops.hppcbl.passport.PassportService;
import com.hp.globalops.hppcbl.passport.PassportServiceException;
import com.hp.globalops.hppcbl.passport.manager.PassportParametersManager;
import com.hp.globalops.hppcbl.webservice.GetUserGroupsResponseElement;
import com.hp.globalops.hppcbl.webservice.GroupRole;
import com.hp.globalops.hppcbl.webservice.ProfileIdentity;
import com.hp.it.spf.sso.portal.AuthenticationConsts;
import com.hp.it.spf.sso.portal.AuthenticatorHelper;
import com.hp.it.spf.user.exception.UserGroupsException;
import com.vignette.portal.log.LogWrapper;

/**
 * This is the implimentation class of <tt>IUserGroupRetriever</tt>.
 * <p>
 * This class retrieve user groups from HPP WebService.
 * </p>
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class HPPWebServiceUserGroupRetriever implements IUserGroupRetriever {
    private static final LogWrapper LOG = AuthenticatorHelper.getLog(HPPWebServiceUserGroupRetriever.class);

    /**
     * Retrieve user groups from HPP WebService.
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
        try {
            PassportService ws = new PassportService();
            PassportParametersManager wsManager = PassportParametersManager.getInstance();
            String adminSessionToken = (ws.login(wsManager.getAdminUser(),
                                                 wsManager.getAdminPassword())).getSessionToken();

            ProfileIdentity profileIdentity = new ProfileIdentity();
            profileIdentity.setUserId((String)userProfile.get(AuthenticationConsts.KEY_USER_NAME));

            GetUserGroupsResponseElement response = ws.getUserGroups(adminSessionToken,
                                                                     profileIdentity);
            for (int i = 0; i < response.getGroupRoleCount(); i++) {
                GroupRole groupRole = response.getGroupRole(i);
                groups.add(groupRole.getGroupName());
            }
        } catch (PassportServiceException pse) {
            LOG.error("Invoke HPP webservice failed, and got PassportServiceException",
                      pse);
        } catch (Exception ex) {
            LOG.error("Invoke HPP webservice failed and got other Exception",
                      ex);
        }
        return groups;
    }

}
