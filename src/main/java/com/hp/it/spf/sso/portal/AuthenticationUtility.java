/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import com.epicentric.common.website.SessionUtils;
import com.epicentric.entity.EntityType;
import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.user.User;
import com.epicentric.user.UserGroup;
import com.epicentric.user.UserGroupManager;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * AuthenticationUtility is the public interface open to others.
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @author <link href="ye.liu@hp.com">liuye</link>
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * 
 * @version TBD
 */
public class AuthenticationUtility {
    private static final com.vignette.portal.log.LogWrapper LOG = AuthenticatorHelper
            .getLog(AuthenticationUtility.class);

    /**
     * This is defined as a String for standard user.
     */
    public static final String SP_STANDARD_USER = "User";

    /**
     * This is defined as a String for super user.
     */
    public static final String SP_SUPER_USER = "User Plus";

    /**
     * This is defined as a String for HP agent user (AtHP user).
     */
    public static final String SP_HP_AGENT = "HP Agent";

    private AuthenticationUtility() {
    }

    /**
     * This method is open to judge whether the user is logged in AtHP
     * 
     * @param request
     *            HttpServletRequest object
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #loggedIntoAtHP(javax.servlet.http.HttpServletRequest)
     * @return true if user if logged in AtHP, otherwise false
     */
    public static boolean loggedIntoAtHP(HttpServletRequest request) {
        return AuthenticatorHelper.loggedIntoAtHP(request);
    }

    /**
     * This method is open to judge whether the user is logged in HHP
     * 
     * @param request
     *            HttpServletRequest object
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #loggedIntoHHP(javax.servlet.http.HttpServletRequest)
     * @return true if user if logged in HHP, otherwise false
     */
    public static boolean loggedIntoHPP(HttpServletRequest request) {
        return AuthenticatorHelper.loggedIntoHPP(request);
    }

    /**
     * This method is open to judge whether the user is logged in Federated IDM
     * 
     * @param request
     *            HttpServletRequest object
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #loggedIntoHHP(javax.servlet.http.HttpServletRequest)
     * @return true if user if logged in HHP, otherwise false
     */
    public static boolean loggedIntoFed(HttpServletRequest request) {
        return AuthenticatorHelper.loggedIntoFed(request);
    }

    /**
     * Return the role information for the current user The role information is
     * judged from user's group
     * 
     * @param user
     *            The user who is using SP
     * @return SP_STANDARD_USER or SP_SUPER_USER or SP_HP_AGENT
     */
    public static String getSPUserRole(User user) {
        if (user == null || user.isGuestUser()) {
            return null;
        }

        String hpagentGroup = AuthenticatorHelper.getProperty("hpagent_group");
        String siteGroupPrefix = AuthenticatorHelper
                .getProperty("site_group_prefix");
        String funGroupPrefix = AuthenticatorHelper
                .getProperty("fun_group_prefix");

        if (hpagentGroup == null || siteGroupPrefix == null
                || funGroupPrefix == null) {
            return null;
        }

        UserGroupManager userGroupManager = UserGroupManager.getInstance();
        boolean hasSiteGroup = false;
        boolean hasFunGroup = false;
        try {
            // Get userGroups in Vignette
            Set userGroups = user.getParents(userGroupManager
                    .getUserGroupEntityType(), false);

            for (Iterator iter = userGroups.iterator(); iter.hasNext();) {
                UserGroup temp = (UserGroup)iter.next();
                String grouptitle = (String)temp.getProperty("title");

                if (grouptitle != null && hpagentGroup.equals(grouptitle)) {
                    // if user is in SP_HP_AGENT group, return hp agent
                    return SP_HP_AGENT;
                } else if (grouptitle != null
                        && grouptitle.startsWith(siteGroupPrefix)) {
                    hasSiteGroup = true;
                } else if (grouptitle != null
                        && grouptitle.startsWith(funGroupPrefix)) {
                    hasFunGroup = true;
                }
            }

            if (hasSiteGroup && hasFunGroup) {
                // if user is in SP_ADMIN3 group, return super user
                return SP_SUPER_USER;
            } else if (hasSiteGroup) {
                // otherwise, return standard user
                return SP_STANDARD_USER;
            } else {
                return null;
            }
        } catch (EntityPersistenceException exception) {
            LOG.error("Entity Persistence Exception when getting user role.");
            LOG.error(exception);
            return null;
        }
    }

    /**
     * Method to judge whether the user is from HPP siteminder
     * 
     * @param request
     *            HttpServletRequest object
     * @return true for this user from HPP siteminder, otherwise false
     */
    public static boolean isFromHPP(HttpServletRequest request) {
        return AuthenticatorHelper.isFromHPP(request);
    }

    /**
     * Method to judge whether the user is from AtHP siteminder
     * 
     * @param request
     *            HttpServletRequest object
     * @return true for this user from AtHP siteminder, otherwise false
     */
    public static boolean isFromAtHP(HttpServletRequest request) {
        return AuthenticatorHelper.isFromAtHP(request);
    }

    /**
     * Method to judge whether the user is from Federated IDM
     * 
     * @param request
     *            HttpServletRequest object
     * @return true for this user from Federated IDM, otherwise false
     */
    public static boolean isFromFed(HttpServletRequest request) {
        return AuthenticatorHelper.isFromFed(request);
    }

    /**
     * Get groups list according to current user. If user not login and is a
     * guest user, then decide where he comes from. For example,
     * SP_FN_ATHP_NAME, SP_FN_FED_NAME, SP_FN_HPP_NAME
     * 
     * @param request
     *            HttpServletRequest
     * @return a String array of group name
     */
    public static String[] getGroupsFromCurrentUser(HttpServletRequest request) {
        try {
            String[] userGroups = null;

            // Get current user
            User currentUser = SessionUtils
                    .getCurrentUser(request.getSession());

            // Decide user type: login user or unlogin user
            if (currentUser != null && !currentUser.isGuestUser()) {
                // Login User
                EntityType userGroupType = UserGroupManager.getInstance()
                        .getUserGroupEntityType();
                Set groupsSet = currentUser.getParents(userGroupType, false);

                // Get all Vignette groups current user belongs to
                if (groupsSet != null && groupsSet.size() > 0) {
                    userGroups = new String[groupsSet.size()];
                    Iterator it = groupsSet.iterator();
                    int i = 0;
                    while (it.hasNext()) {
                        UserGroup userGroup = (UserGroup)it.next();
                        userGroups[i] = (String)userGroup.getProperty("title");
                        i++;
                    }
                }
            } else {

                // If user is unlogin, then decide user where to come from:
                // SP_FN_FED, SP_FN_ATHP, and SP_FN_HPP
                userGroups = new String[1];
                if (AuthenticationUtility.isFromAtHP(request)) {
                    userGroups[0] = AuthenticationConsts.SP_FN_ATHP_NAME;
                } else if (AuthenticationUtility.isFromFed(request)) {
                    userGroups[0] = AuthenticationConsts.SP_FN_FED_NAME;
                } else if (AuthenticationUtility.isFromHPP(request)) {
                    userGroups[0] = AuthenticationConsts.SP_FN_HPP_NAME;
                } else {
                    userGroups = null;
                }
            }
            return userGroups;
        } catch (Exception e) {
            LOG.error(e);
            return null;
        }
    }

    /**
     * Utitily method to ensure the request can only come from HPP. This utility
     * methods will be invoked in the display action and process action of HPP
     * facing secondary pages. If the user is not from HPP, it will send a
     * redirect to home page.
     * 
     * @param portalContext
     *            current portal context
     * @return true if the user is from HPP, otherwise false.
     */
    public static boolean ensureFromHPP(PortalContext portalContext) {

        HttpServletRequest request = portalContext.getPortalRequest()
                .getRequest();
        if (!isFromHPP(request)) {
            try {
                String homeURL = portalContext.getSiteURI(portalContext
                        .getCurrentSite().getDNSName());
                portalContext.getPortalResponse().getResponse().sendRedirect(
                        homeURL);
            } catch (IOException e) {
                LOG.error("Redirect Error - more details: " + e.getMessage());
            }
            // return false if it is not from HPP.
            return false;
        } else {
            // return true if it is from HPP.
            return true;
        }
    }

    /**
     * Utitily method to ensure the request can only come from federation. This
     * utility methods will be invoked in the display action and process action
     * of Federation facing secondary pages. If the user is not from federation,
     * it will send a redirect to home page.
     * 
     * @param portalContext
     *            current portal context
     * @return true if the user is from federation, otherwise false.
     */
    public static boolean ensureFromFed(PortalContext portalContext) {

        HttpServletRequest request = portalContext.getPortalRequest()
                .getRequest();
        if (!isFromFed(request)) {
            try {
                String homeURL = portalContext.getSiteURI(portalContext
                        .getCurrentSite().getDNSName());
                portalContext.getPortalResponse().getResponse().sendRedirect(
                        homeURL);
            } catch (IOException e) {
                LOG.error("Redirect Error - more details: " + e.getMessage());
            }
            // return false if it is not from federation.
            return false;
        } else {
            // return true if it is from federation.
            return true;
        }
    }
}
