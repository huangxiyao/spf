/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.epicentric.authentication.AuthenticationManager;
import com.epicentric.authentication.Realm;
import com.epicentric.common.website.CookieUtils;
import com.epicentric.common.website.SessionInfo;
import com.epicentric.common.website.SessionUtils;
import com.epicentric.entity.EntityNotFoundException;
import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.entity.UniquePropertyValueConflictException;
import com.epicentric.user.User;
import com.epicentric.user.UserGroup;
import com.epicentric.user.UserGroupManager;
import com.epicentric.user.UserManager;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;
import com.vignette.portal.log.LogWrapper;

/**
 * AuthenticatorHelper is the private utility class used for this SSO module.
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @version TBD
 */
public class AuthenticatorHelper {
    private static final com.vignette.portal.log.LogWrapper LOG = getLog(AuthenticatorHelper.class);

    private static ResourceBundle AUTH_CONSTS = null;

    private AuthenticatorHelper() {
    }

    private static ResourceBundle getAuthenticationConsts() {
        if (AUTH_CONSTS == null) {
            AUTH_CONSTS = PropertyResourceBundleManager
                    .getBundle(AuthenticationConsts.HEADER_CONSTS_FILE_BASE);
        }
        return AUTH_CONSTS;
    }

    /**
     * This method is used to create a new VAP user. It will retrieve values
     * from SSO User and save to VAP database.
     * 
     * @param ssoUser
     * @return the created user
     * @throws UniquePropertyValueConflictException
     *             if failed
     * @throws EntityPersistenceException
     *             if failed
     * @see com.epicentric.user.UserManager#createUser(java.util.Map)
     * @see com.epicentric.user.User#save()
     */
    static User createVAPUser(SSOUser ssoUser)
            throws UniquePropertyValueConflictException,
            EntityPersistenceException {

        LOG.info("creating new vap user start");
        // get realm id
        Realm rlm = AuthenticationManager.getDefaultAuthenticationManager()
                .getSSORealm();
        String realmId = rlm.getID();

        Map userProperties = new HashMap();
        // Setting VAP User object
        userProperties.put(AuthenticationConsts.PROPERTY_USER_NAME_ID, ssoUser
                .getUserName());
        userProperties.put(AuthenticationConsts.PROPERTY_DOMAIN_ID, realmId);
        userProperties.put(AuthenticationConsts.PROPERTY_EMAIL_ID, ssoUser
                .getEmail().toLowerCase());
        userProperties.put(AuthenticationConsts.PROPERTY_FIRSTNAME_ID, ssoUser
                .getFirstName());
        userProperties.put(AuthenticationConsts.PROPERTY_LASTNAME_ID, ssoUser
                .getLastName());
        userProperties.put(AuthenticationConsts.PROPERTY_LANGUAGE_ID, ssoUser
                .getLanguage());
        userProperties.put(AuthenticationConsts.PROPERTY_COUNTRY_ID, ssoUser
                .getCountry());

        try {
            User user = UserManager.getInstance().createUser(userProperties);
            addUser2Group(user, ssoUser.getGroups());
            // Save VAP User object
            user.save();
            LOG.info("saved new vap user " + ssoUser.getUserName());
            return user;
        } catch (UniquePropertyValueConflictException e) {
            LOG.error("Required unique values conflict when saving user: "
                    + ssoUser.getUserName());
            throw e;
        } catch (EntityPersistenceException e) {
            LOG.error("Entity Persistence exception when saving user: "
                    + ssoUser.getUserName());
            throw e;
        }
    }

    /**
     * This method is used to update the current user info and save to VAP
     * database.
     * 
     * @param user
     *            User from Vignette
     * @param ssoUser
     *            virtaul user mapped from request
     * @throws UniquePropertyValueConflictException
     *             if failed
     * @throws EntityPersistenceException
     *             if failed
     */
    static void updateVAPUser(User user, SSOUser ssoUser)
            throws UniquePropertyValueConflictException,
            EntityPersistenceException {
        LOG.info("updating vap user start");
        // Setting VAP User object
        try {
            // user.setProperty(AuthenticationConsts.PROPERTY_EMAIL_ID, ssoUser
            // .getEmail());
            user.setProperty(AuthenticationConsts.PROPERTY_FIRSTNAME_ID,
                    ssoUser.getFirstName());
            user.setProperty(AuthenticationConsts.PROPERTY_LASTNAME_ID, ssoUser
                    .getLastName());
            user.setProperty(AuthenticationConsts.PROPERTY_LANGUAGE_ID, ssoUser
                    .getLanguage());
            user.setProperty(AuthenticationConsts.PROPERTY_COUNTRY_ID, ssoUser
                    .getCountry());

            // updateUserGroup(user, ssoUser.getGroups());
            LOG.info("Updated user:" + ssoUser.getUserName());
        } catch (UniquePropertyValueConflictException e) {
            LOG.error("Required unique values conflict when updating user:"
                    + ssoUser.getUserName());
            throw e;
        } catch (EntityPersistenceException e) {
            LOG.error("Entity Persistence Exception when updating user:"
                    + ssoUser.getUserName());
            throw e;
        }
    }

    /**
     * This method is used to add user to group. User will only be added to
     * groups already exist in VAP
     * 
     * @param user
     *            User retrieved from Vignette
     * @param ssoGroups
     *            Groups from SSOUser
     * @see com.epicentric.user.UserGroupManager#getUserGroup(java.lang.String,
     *      java.lang.Object)
     * @see com.epicentric.user.User#addParent(com.epicentric.user.UserGroup)
     */
    private static void addUser2Group(User user, Set ssoGroups) {
        // Proceed only if this user can be located and he is not a guest
        if (user == null || user.isGuestUser() || ssoGroups == null
                || ssoGroups.size() == 0) {
            return;
        }
        UserGroupManager userGroupManager = UserGroupManager.getInstance();
        try {
            LOG.info("add user to group start");
            LOG.info("ssoGroups size is " + ssoGroups.size());
            // add vapUser to group
            for (Iterator iter = ssoGroups.iterator(); iter.hasNext();) {
                String newgroup = (String)iter.next();
                LOG.info("begin to add " + newgroup + " to vapuser");
                try {
                    UserGroup ug = (UserGroup)userGroupManager.getUserGroups(
                            AuthenticationConsts.GROUP_TITLE, newgroup).next();
                    user.addParent(ug);
                    LOG.info("add " + newgroup + " to vapuser success");
                } catch (NullPointerException e) {
                    LOG.info("not found group " + newgroup + " in database");
                }
            }
        } catch (EntityPersistenceException exception) {
            LOG
                    .error("Entity Persistence Exception when adding user to groups");
            LOG.error(exception);
        }
    }

    /**
     * This method is used to judge whether the group need to be updated
     * 
     * @param userGroups
     *            Current group retrieved from Vignette
     * @param newGroups
     *            New group which need to judged by
     * @return true if need to update group, otherwise false
     * @see isEqualedSet(java.util.Set, java.util.Set)
     */
    static boolean needUpdateGroup(Set userGroups, Set newGroups) {
        if (newGroups == null && userGroups == null) {
            return false;
        }
        if (newGroups == null || userGroups == null) {
            return true;
        }
        return !newGroups.equals(getUserGroupTitleSet(userGroups));
    }

    /**
     * This method is used to get the group title set based on user's group
     * 
     * @param userGroups
     *            groups
     * @return retrieved user group title set.
     */
    static Set getUserGroupTitleSet(Set userGroups) {
        Set groupTitles = new HashSet();
        if (userGroups != null) {
            for (Iterator iter = userGroups.iterator(); iter.hasNext();) {
                UserGroup temp = (UserGroup)iter.next();
                String groupTitle = (String)temp
                        .getProperty(AuthenticationConsts.GROUP_TITLE);
                groupTitles.add(groupTitle);
            }
        }
        return groupTitles;
    }

    /**
     * This method is used to get user's group info.
     * 
     * @param user
     * @return retrieved user gruop set.
     * @see com.epicentric.user.User#getParents(com.epicentric.entity.EntityType,
     *      boolean)
     */
    static Set getUserGroupSet(User user) {
        if (user == null || user.isGuestUser()) {
            return null;
        }
        try {
            UserGroupManager userGroupManager = UserGroupManager.getInstance();
            return user.getParents(userGroupManager.getUserGroupEntityType(),
                    false);
        } catch (EntityPersistenceException exception) {
            LOG
                    .error("Entity Persistence Exception when getting user group set");
            LOG.error(exception);
            return new HashSet();
        }
    }

    /**
     * This method is used to update user's group
     * 
     * @param user
     *            User from Vignette
     * @param userGroups
     *            Old group retrived from Vignette
     * @param ssoGroups
     *            New group which needs to be updated by
     * @throws EntityPersistenceException
     *             if failed
     * @see addUser2Group(com.epicentric.user.User, java.util.Set)
     */
    static void updateUserGroup(User user, Set userGroups, Set ssoGroups) {
        // Proceed only if this user can be located and he is not a guest
        if (user == null || user.isGuestUser() || userGroups == null
                || ssoGroups == null) {
            return;
        }
        LOG.info("updating vap user's group start");
        // false means no recursive groups
        // LOG.info("ssoGroups size is " + ssoGroups.size());
        // LOG.info("userGroups size is " + userGroups.size());
        for (Iterator iter = userGroups.iterator(); iter.hasNext();) {
            UserGroup temp = (UserGroup)iter.next();
            // if groups in vapuser and not in ssoUser, remove from vap user
            String grouptitle = (String)temp
                    .getProperty(AuthenticationConsts.GROUP_TITLE);
            // don't sync groups starting with LOCAL_
            if (grouptitle.startsWith("LOCAL_")) {
                continue;
            }
            if (!ssoGroups.contains(grouptitle)) {
                try {
                    user.removeParent(temp);
                    LOG.info("remove group " + grouptitle);
                } catch (EntityPersistenceException exception) {
                    LOG
                            .error("Entity Persistence Exception when updating user group");
                    LOG.error(exception);
                }
            } else {
                ssoGroups.remove(grouptitle);
            }
        }
        // if groups in ssoUser and not in vapUser, add vapUser to group
        addUser2Group(user, ssoGroups);
    }

    /* above methods are used to treat group update END */

    /**
     * Method to judge whether the user is from HPP siteminder
     * 
     * @param request
     *            HttpServletRequest object
     * @return true for this user from HPP siteminder, otherwise false
     */
    static boolean isFromHPP(HttpServletRequest request) {
        return isFromHPPGeneral(request) && !Utils.isFederatedSite(request);
    }

    /**
     * Method to judge whether the user is from AtHP siteminder
     * 
     * @param request
     *            HttpServletRequest object
     * @return true for this user from AtHP siteminder, otherwise false
     */
    static boolean isFromAtHP(HttpServletRequest request) {
        String authSource = request
                .getHeader(AuthenticationConsts.AUTH_SOURCE_TAGE);
        return AuthenticationConsts.SP_FROM_ATHP.equals(authSource);
    }

    /**
     * Method to judge whether the user is from Federated IDM
     * 
     * @param request
     *            HttpServletRequest object
     * @return true for this user from from Federated IDM, otherwise false
     */
    static boolean isFromFed(HttpServletRequest request) {
        return isFromHPPGeneral(request) && Utils.isFederatedSite(request);
    }

    /**
     * Method to judge whether the user is from a general HPP, including
     * standard HPP and federated HPP.
     * 
     * @param request
     *            HttpServletRequest object
     * @return true for this user from from general HPP, otherwise false
     */
    private static boolean isFromHPPGeneral(HttpServletRequest request) {
        String authSource = request
                .getHeader(AuthenticationConsts.AUTH_SOURCE_TAGE);
        return AuthenticationConsts.SP_FROM_HPP.equals(authSource);
    }

    /**
     * Check if the VAP user needs to be updated. Compare the initSession
     * parameter and true
     * 
     * @param request
     *            HttpServletRequest
     * @return true if this user needs to be updated, otherwise false
     */
    static boolean needUpdateVAPUser(HttpServletRequest request) {
        return "true".equalsIgnoreCase((String)request
                .getParameter("initSession"))
                || !AuthenticatorHelper.isVAPLoggedIn(request);
    }

    /**
     * Check if the user has loged into VAP. If user from session can be
     * retrieved and is not a guest user, which means user has logged in.
     * 
     * @param request
     *            current requeset.
     * @return true if user has logged in, otherwise false
     * @see com.epicentric.common.website.SessionUtils#getCurrentUser(javax.servlet.http.HttpSession)
     */
    static boolean isVAPLoggedIn(HttpServletRequest request) {
        User currentUser = SessionUtils.getCurrentUser(request.getSession());
        return currentUser != null && !currentUser.isGuestUser();
    }

    /**
     * Check if user has logged in athp siteminder'. This is judges by the AtHP
     * flag in the request header.
     * 
     * @param request
     *            current request
     * @return true if user has logged in AtHP, otherwise false
     */
    static boolean loggedIntoAtHP(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        String headerflag = request
                .getHeader(AuthenticationConsts.HEADER_ATHP_FLAG);
        return isFromAtHP(request)
                && headerflag != null
                && headerflag
                        .startsWith(getProperty(AuthenticationConsts.PROPERTY_ATHP_FLAG));
    }

    /**
     * Check if user has logged in HPP siteminder. This is judges by the
     * CL_Header in the request and the SMSession cookie value.
     * 
     * @param request
     *            current request
     * @return true if user has logged in HHP, otherwise false
     */
    static boolean loggedIntoHPP(HttpServletRequest request) {
        return isFromHPP(request) && loggedIntoHPPGeneral(request);
    }

    /**
     * Check if user has logged in general HPP siteminder. This is judges by the
     * CL_Header in the request and the SMSession cookie value. General HPP case
     * including standard HPP and federated HPP.
     * 
     * @param request
     *            current request
     * @return true if user has logged in HHP, otherwise false
     */
    private static boolean loggedIntoHPPGeneral(HttpServletRequest request) {
        String smSession = CookieUtils.getCookieValue(request,
                AuthenticationConsts.COOKIE_ATTR_SMSESSION);
        String clHeaderHpp = AuthenticatorHelper
                .getRequestHeader(
                        request,
                        getProperty(AuthenticationConsts.HEADER_CL_HEADER_PROPERTY_NAME),
                        true);
        String hpclName = AuthenticatorHelper
                .getValuesFromCLHeader(
                        clHeaderHpp,
                        getProperty(AuthenticationConsts.HEADER_HPCLNAME_PROPERTY_NAME));

        return isFromHPPGeneral(request)
                && smSession != null
                && smSession.trim().length() > 0
                && !smSession
                        .equals(getProperty(AuthenticationConsts.HPP_LOGGEDOFF_PROPERTY_NAME))
                && hpclName != null
                && hpclName.trim().length() > 0
                && !hpclName
                        .equals(getProperty(AuthenticationConsts.ANON_IND_PROPERTY_NAME));
        // return isHPPLoggedIn(smSession, hpclName,
        // getProperty(Consts.HPP_LOGGEDOFF_PROPERTY_NAME),
        // getProperty(Consts.ANON_IND_PROPERTY_NAME));
    }

    // static boolean isHPPLoggedIn(String smSession, String hpclname,
    // String loggedOffState, String anonIndicator) {
    // return smSession != null && smSession.trim().length()>0
    // && !smSession.equals(loggedOffState) && hpclname != null
    // &&hpclname.trim().length() > 0 && !hpclname.equals(anonIndicator);
    // }

    static boolean loggedIntoFed(HttpServletRequest request) {
        return isFromFed(request) && loggedIntoHPPGeneral(request);
    }

    static boolean isSandBox() {
        return AuthenticationConsts.YES
                .equalsIgnoreCase(getProperty(AuthenticationConsts.SANDBOX_MODE));
    }

    /**
     * Assign user to authentication groups: loggedIntoHPP -> SP_FN_HPP Vignette
     * group loggedIntoAtHP -> SP_FN_ATHP Vignette group loggedIntoFed ->
     * SP_FN_FED Vignette group
     * 
     * @param request
     * @param user
     */
    static boolean assignUserToAuthenticationGroup(HttpServletRequest request,
            User user) {

        // return if it is a guest user
        if (user == null || user.isGuestUser()) {
            return false;
        }

        boolean needToUpdate = false;

        // calculate the group name to assign
        String groupToAssign = null;
        if (loggedIntoFed(request)) {
            groupToAssign = AuthenticationConsts.SP_FN_FED_NAME;
        } else if (loggedIntoAtHP(request)) {
            groupToAssign = AuthenticationConsts.SP_FN_ATHP_NAME;
        } else if (loggedIntoHPP(request)) {
            groupToAssign = AuthenticationConsts.SP_FN_HPP_NAME;
        }

        // return at once
        if (groupToAssign == null) {
            return false;
        }

        // Get a list of groups
        List authGroups = Arrays.asList(new String[] {
                AuthenticationConsts.SP_FN_FED_NAME,
                AuthenticationConsts.SP_FN_ATHP_NAME,
                AuthenticationConsts.SP_FN_HPP_NAME });

        UserGroupManager userGroupManager = UserGroupManager.getInstance();
        // update groups
        try {
            boolean bContainToAssign = false;
            // remove user from other authentication groups
            Set userGroups = getUserGroupSet(user);
            // if group is empty, return at once.
            if (userGroups == null) {
                return false;
            }

            boolean bValidUser = true;
            Object objGroup = null;
            for (Iterator iter = userGroups.iterator(); iter.hasNext();) {
                objGroup = iter.next();
                if (objGroup == null || !(objGroup instanceof UserGroup)) {
                    // mark the user not valid
                    bValidUser = false;
                    LOG.warning("Invalid group value: " + objGroup);
                    break;
                }

                UserGroup group = (UserGroup)objGroup;
                String groupTitle = (String)group
                        .getProperty(AuthenticationConsts.GROUP_TITLE);

                // Remove the user from other authentication groups.
                if (authGroups.contains(groupTitle)
                        && !groupToAssign.equals(groupTitle)) {
                    needToUpdate = true;
                    LOG.info("Remove user " + user.getDisplayName()
                            + " from group " + groupTitle);
                    user.removeParent(group);
                }

                // Check if containing the group to assign
                if (groupToAssign.equals(groupTitle)) {
                    bContainToAssign = true;
                }
            }

            // assign user to the proper group
            if (!bContainToAssign && bValidUser) {
                needToUpdate = true;
                UserGroup ug = userGroupManager.getUserGroup(
                        AuthenticationConsts.GROUP_TITLE, groupToAssign);
                LOG.info("Assign user " + user.getDisplayName() + " to group "
                        + groupToAssign);
                user.addParent(ug);
            }

        } catch (Exception e) {
            LOG.error("Fail to assign user to authentication group: "
                    + groupToAssign);
            LOG.error(e);
        }

        return needToUpdate;
    }

    /**
     * Session cleanup when doing implicit logout. Only remove service portal
     * session attributes that does not start with SP_RETAIN. After cleaning, a
     * guest user will be set into session
     * 
     * @param request
     *            Current request
     * @see javax.servlet.http.HttpSession#removeAttribute(java.lang.String)
     * @see com.epicentric.common.website.SessionInfo#setUser(com.epicentric.user.User)
     */
    static void cleanupSession(HttpServletRequest request) {
        LOG.info("Cleaning up session start");
        HttpSession session = request.getSession(true);
        Enumeration it = session.getAttributeNames();
        // remove service portal specific session attributes except the ones
        // with "SP_RETAIN"
        while(it.hasMoreElements()) {
            String next = (String)it.nextElement();
            LOG.info("Session Attribute:" + next);
            if (next.indexOf(AuthenticationConsts.PARAMETER_PREFIX) >= 0) {
                if (next
                        .indexOf(AuthenticationConsts.RETAINED_PARAMETER_PREFIX) < 0) {
                    session.removeAttribute(next);
                    LOG.info("Removed Session Attribute:" + next);
                }
            }
        }
        // reset vignette session info to the guest user state
        SessionInfo sessionInfo = (SessionInfo)session
                .getAttribute(SessionInfo.SESSION_INFO_NAME);
        if (sessionInfo != null) {
            try {
                sessionInfo.setUser(UserManager.getInstance().getGuestUser());
                LOG.info("Set guest user to session info");
                session
                        .setAttribute(SessionInfo.SESSION_INFO_NAME,
                                sessionInfo);
            } catch (EntityPersistenceException e) {
                LOG
                        .error("Entity Persistence Exception when cleaning up session");
                LOG.error(e);
            }
        }
        // request.setAttribute("expireSessionCookie", "1");
        LOG.info("Cleaning up session end");
    }

    /**
     * Utility method to get request header value for the label passed. This
     * method returns no decoded, trimmed string.
     * 
     * @param request
     *            Current requeset
     * @param in
     *            field name
     * @return non-decoded field value in request header
     * @see getRequestHeader(javax.servlet.http.HttpServletRequest,
     *      java.lang.String, boolean)
     */
    static String getRequestHeader(HttpServletRequest request, String in) {
        return getRequestHeader(request, in, false);
    }

    /**
     * Utility method to get request header value for the field name passed.
     * This method can choose to decode the result or not.
     * 
     * @param request
     *            Current request
     * @param in
     *            Field name
     * @param needDecode
     *            Decode the result or not?
     * @return value in the request header, null if failed
     * @see decode(java.lang.String)
     * @see trimUnwanted(java.lang.String)
     */
    static String getRequestHeader(HttpServletRequest request, String in,
            boolean needDecode) {
        if (request == null) {
            return null;
        }

        String headerInfo = null;
        if ((in != null) && !"".equals(in)) {
            headerInfo = request.getHeader(in);
            if ((headerInfo != null) && !"".equals(headerInfo) && needDecode) {
                headerInfo = Utils.decodeBase64(Utils.trimUnwantedSmPadding(headerInfo));
            }
        }
        return headerInfo;
    }

    /**
     * Utility method to get value for the field name passed in CL_Header
     * request attribute.
     * 
     * @param clheader
     *            CL Header String
     * @param in
     *            Field name in the CL Header
     * @return field value, null if failed
     */
    static String getValuesFromCLHeader(String clheader, String in) {
        if (in == null) {
            return null;
        }
        String result = null;
        in = in + "=";
        if ((clheader != null) && !"".equals(clheader)) {
            String temp;
            int i = clheader.indexOf(in);
            if (i >= 0) {
                temp = "";
                if (i + in.length() < clheader.length()) {
                    temp = clheader.substring(i + in.length());
                }
                i = temp.indexOf('|');
                if (i >= 0) {
                    result = temp.substring(0, i);
                } else {
                    result = temp;
                }
            }
        }
        return result;
    }

    /**
     * Use resource bundle to get property.
     * 
     * @param key
     * @return
     */
    static String getProperty(String key) {
        try {
            return AuthenticatorHelper.getAuthenticationConsts().getString(key);
        } catch (Exception ex) {
            LOG.error("Can't find key " + key + " in resource bundle");
            return null;
        }
    }

    /**
     * Utility method to get cookie value.
     * 
     * @param request
     * @param name
     * @return
     */
    static String getCookieValue(HttpServletRequest request, String name) {
        if (null == request) {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            int len = cookies.length;
            for (int i = 0; i < len; i++) {
                if (null != cookies[i] && name.equals(cookies[i].getName())) {
                    return cookies[i].getValue();
                }
            }
        }
        return null;
    }

    /**
     * Utility method to set cookie value.
     * 
     * @param name
     * @param value
     * @param domain
     * @param path
     * @param maxAge
     * @return
     */
    static Cookie newCookie(String name, String value, String domain,
            String path, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        if (domain != null && !"".equals(domain)) {
            cookie.setDomain(domain);
        }
        cookie.setPath(path);
        cookie.setMaxAge(maxAge);
        return cookie;
    }

    /**
     * Retrieves a user based on a given email.
     * 
     * @param email
     *            email
     * @return an instance of the User object corresponding to the logon id or
     *         null, if not found
     */
    static User retrieveUserByEmail(String email) {
        try {
            LOG.info("Retrieving user " + email);
            User u = UserManager.getInstance().getUser(
                    AuthenticationConsts.PROPERTY_EMAIL_ID, email.toLowerCase());
            LOG.info("Retrieved user " + email);
            return u;
        } catch (EntityNotFoundException e) {
            LOG.info("User " + email + " not found");
            return null;
        } catch (EntityPersistenceException e) {
            LOG
                    .error("Entity Persistence Exception when retrieving user by email");
            LOG.error(e);
            return null;
        }
    }

    /**
     * Instantiates a new LogWrapper with this ServicePortalSSO framework
     * 
     * @param cls
     * @return
     */
    static LogWrapper getLog(Class cls) {
        return new LogWrapper(cls, "[SP-AUTH]" + cls.getName());
    }
}
