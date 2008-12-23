/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import com.epicentric.common.website.SessionInfo;
import com.epicentric.common.website.SessionUtils;
import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.entity.UniquePropertyValueConflictException;
import com.epicentric.site.Site;
import com.epicentric.user.User;

/**
 * AbstractAuthenticator is the father authenticator. All other authenticators
 * will be extended from this class.
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @version TBD
 * 
 */
public class AbstractAuthenticator implements IAuthenticator {

    /**
     * serialVersionUID long
     */
    private static final long serialVersionUID = 1L;

    private static final com.vignette.portal.log.LogWrapper LOG = AuthenticatorHelper
            .getLog(AbstractAuthenticator.class);

    /**
     * profileMap Map
     */
    protected Map profileMap = new TreeMap();

    /**
     * VAP_LOGON_PROPERTY_NAME String
     */
    protected static final String VAP_LOGON_PROPERTY_NAME = "logon";

    protected static final String USER_PROFILE_KEY = "userProfile";

    /**
     * rb ResourceBundle
     */
    protected ResourceBundle rb = null;

    /**
     * userName String
     */
    protected String userName = null;

    /**
     * ssoUser SSOUser
     */
    protected SSOUser ssoUser = new SSOUser();

    /**
     * request HttpServletRequest
     */
    protected HttpServletRequest request = null;

    /**
     * The default consturctor
     */
    protected AbstractAuthenticator() {
    }

    /**
     * The constructor of AbstractAuthenticator class. It will do sime
     * initialization for anthenticators
     * 
     * @param request
     *            HttpServletRequest object
     */
    public AbstractAuthenticator(HttpServletRequest request) {
        this.request = request;
        String rbFile = retrieveRbFile();
        try {
            rb = ResourceBundle.getBundle(rbFile);
            LOG.info("Get Resource Bundle File = " + rbFile);
        } catch (Exception e) {
            LOG.error("No Resource Bundle File = " + rbFile);
        }
    }

    /**
     * Retreive the corresponding resourece bundle file for authenticator
     * 
     * @return resourece bundle file name
     */
    protected String retrieveRbFile() {
        String className = this.getClass().getName();
        String packageName = this.getClass().getPackage().getName() + ".";
        return className.replaceFirst(packageName, "");
    }

    /**
     * Map the primary attributes used every time from request to ssoUser. These
     * key values are used for sso every time.
     */
    protected void mapRequest2User() {
        ssoUser
                .setUserName(getValue(AuthenticationConsts.HEADER_USER_NAME_PROPERTY_NAME));
        String email = getValue(AuthenticationConsts.HEADER_EMAIL_ADDRESS_PROPERTY_NAME);
        if (email != null) {
            int index = email.indexOf("^");
            if (index > -1) {
                email = email.substring(0, index);
            }
        }
        ssoUser.setEmail(email);
        ssoUser
                .setFirstName(getValue(AuthenticationConsts.HEADER_FIRST_NAME_PROPERTY_NAME));
        ssoUser
                .setLastName(getValue(AuthenticationConsts.HEADER_LAST_NAME_PROPERTY_NAME));
        ssoUser
                .setCountry(getValue(AuthenticationConsts.HEADER_COUNTRY_PROPERTY_NAME));

        // Set lanuage into SSOUser, if null, set to default EN
        String language = getValue(AuthenticationConsts.HEADER_LANGUAGE_PROPERTY_NAME);
        if (language == null || ("").equals(language.trim())) {
            ssoUser.setLanguage(AuthenticationConsts.DEFAULT_LANGUAGE);
        } else {
            ssoUser.setLanguage(language);
        }
    }

    /**
     * Get group info and return as a Set
     * 
     * @return retrieved groups
     */
    protected Set retrieveGroup() {
        return new HashSet();
    }

    /**
     * Return corresponding field value in request header
     * 
     * @param fieldName
     *            field in request header
     * @return corresponding field in request header
     * @see com.hp.serviceportal.framework.portal.authentication.AuthenticatorHelper
     *      #getRequestHeader(javax.servlet.http.HttpServletRequest,
     *      java.lang.String);
     */
    protected String getValue(String fieldName) {
        return AuthenticatorHelper.getRequestHeader(request,
                getProperty(fieldName));
    }

    /**
     * Use resource bundle to get property
     * 
     * @param key
     *            key in resource bundle
     * @return corresponding value in resource bundle, null if not found
     * @see java.util.ResourceBundle#getString(java.lang.String)
     */
    protected String getProperty(String key) {
        try {
            if (null == key || ("").equals(key.trim())) {
                return null;
            }
            return rb.getString(key);
        } catch (Exception ex) {
            LOG.error("Can't find key " + key + "in resource bundle file");
            return null;
        }
    }

    /**
     * Get the userName
     * 
     * @return the userName
     */
    public String getUserName() {
        LOG.info("Return userName: " + userName);
        return userName;
    }

    /**
     * Get the property ssoUser
     * 
     * @return the ssoUser
     */
    protected SSOUser getRequestUser() {
        return ssoUser;
    }

    /**
     * If this user is first time login, create it in Vignette If error occured,
     * an error flag will be set in the session.
     * 
     * @see mapRequest2ExtentUser()
     * @see getGroups()
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #createVAPUser(com.hp.it.spf.sso.portal.SSOUser)
     * 
     * @return the new Created VAP User
     */
    protected User firstTimeUser() {
        ssoUser.setGroups(retrieveGroup());
        try {
            return AuthenticatorHelper.createVAPUser(ssoUser);
        } catch (UniquePropertyValueConflictException e) {
            userName = null;
            LOG.error("Required unique values conflict when creating user "
                    + e.getMessage());
            request.getSession().setAttribute(
                    AuthenticationConsts.SESSION_ATTR_SSO_ERROR, "1");
        } catch (EntityPersistenceException e) {
            userName = null;
            LOG.error("Entity persistence exception when creating user "
                    + e.getMessage());
            request.getSession().setAttribute(
                    AuthenticationConsts.SESSION_ATTR_SSO_ERROR, "1");
        }
        return null;
    }

    /**
     * The method is used to update VAP user info It will: Update user's primary
     * site AND update user's info if needed AND update user's group for when
     * user is loggin in If error occured, set an error flag in the session
     * 
     * @param vapUser
     *            input the vignette User Object
     * @param isLoggedIn
     *            input whether the user is logged in
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #needUpdatePrimarySite(com.hp.it.spf.sso.portal.SSOUser,
     *      com.epicentric.user.User)
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #updateUserPrimarySite(com.epicentric.user.User, java.lang.String)
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #needUpdateVAPUser(com.epicentric.user.User, java.util.Date,
     *      java.lang.String)
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #updateVAPUser(com.epicentric.user.User,
     *      com.hp.it.spf.portal.authentication.ssoUser)
     * @see com.epicentric.user.User#save()
     */
    protected void updateUser(User vapUser, boolean isLoggedIn) {
        try {
            // Update user's info if needed
            LOG.info("update basic info for " + ssoUser.getUserName());
            AuthenticatorHelper.updateVAPUser(vapUser, ssoUser);

            // if user is not logged in, get the groups and
            // check if the user's group need to update
            // Get current user's groups
            ssoUser.setGroups(retrieveGroup());
            updateUserGroups(vapUser);

            // Update user to the appropriate authentication groups.
            AuthenticatorHelper.assignUserToAuthenticationGroup(request,
                    vapUser);

            // If user's info need to update, save this user
            vapUser.save();

        } catch (UniquePropertyValueConflictException e) {
            LOG.error("Required unique values conflict when updating user"
                    + e.getMessage());
            request.getSession().setAttribute(
                    AuthenticationConsts.SESSION_ATTR_SSO_ERROR, "1");
        } catch (EntityPersistenceException e) {
            LOG.error("Entity persistence exception when updating user"
                    + e.getMessage());
            request.getSession().setAttribute(
                    AuthenticationConsts.SESSION_ATTR_SSO_ERROR, "1");
        }
    }

    /**
     * This method is used to update user's group First, it will retrieve user's
     * group from Vignette and request header If these two groups are different,
     * then update group from request header to Vignette If error occured, set
     * an error flag in the session.
     * 
     * @param user
     *            The user retrieved from vignette
     * @return true if user's group is updated, otherwise return false
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #needUpdateGroup(java.util.Set, java.util.Set)
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #updateUserGroup(com.epicentric.user.User, java,util.Set,
     *      java.util.Set)
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #getUserGroupSet(com.epicentric.user.User)
     * @throws EntityPersistenceException
     *             if update group failed
     */
    protected boolean updateUserGroups(User user)
            throws EntityPersistenceException {
        // Retrieve group from vignette
        Set oldGroups = AuthenticatorHelper.getUserGroupSet(user);

        Set newGroups = ssoUser.getGroups();

        // Update group info if needed
        if (AuthenticatorHelper.needUpdateGroup(oldGroups, newGroups)) {
            LOG.info("Update group info for " + ssoUser.getUserName());
            AuthenticatorHelper.updateUserGroup(user, oldGroups, newGroups);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method is used to check whether the user in the vignette session is
     * different with the user in the request header It only compares the
     * profile IDs as they are the the unique identifier in Vignette
     * 
     * @return true if the session user is different with user in the request
     *         header
     * @see com.epicentric.common.website.SessionUtils#getCurrentUser(javax.servlet.http.HttpSession)
     */
    protected boolean checkDiffUser() {
        String emailVap = "";

        // Retrieve current user and its email from session
        User currentUser = SessionUtils.getCurrentUser(request.getSession());
        if (currentUser.getProperty(AuthenticationConsts.PROPERTY_EMAIL_ID) != null) {
            emailVap = (String)currentUser
                    .getProperty(AuthenticationConsts.PROPERTY_EMAIL_ID);
        }
        // If email is different, return true
        if (!emailVap.equals(ssoUser.getEmail())) {
            LOG.info("email_vap:" + emailVap);
            LOG.info("email:" + ssoUser.getEmail());
            return true;
        }
        LOG.info("same user in session: " + emailVap);
        return false;
    }

    /**
     * This method is used to synchronize user's info. This class will create
     * user in Vignette if the user is first time here AND will update user if
     * needed AND will cleanup session if session user and request user are
     * different AND will do nothing if seesion error flag is catched
     * 
     * @see firstTimeUser()
     * @see updateUser(com.epicentric.user.User, boolean)
     * @see checkDiffUser()
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #isVAPLoggedIn(javax.servlet.http.HttpServletRequest)
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #retrieveUserByEmail(java.util.String)
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #cleanupSession(javax.servlet.http.HttpServletRequest)
     */
    protected void syncUser() {
        if (request == null) {
            return;
        }
        if (ssoUser.getUserName() == null) {
            LOG.info("can't sync from http request");
            return;
        }
        // if there is an error happened before in this user sysc process,
        // do not update again any more
        if (request.getSession().getAttribute(
                AuthenticationConsts.SESSION_ATTR_SSO_ERROR) != null) {
            LOG.error("catched by sso");
            userName = null;
            return;
        }

        // Is user logged in Vignette?
        boolean isVAPLoggedIn = AuthenticatorHelper.isVAPLoggedIn(request);

        // Retrieve user from Vignette by email.
        User vapUser = null;
        vapUser = AuthenticatorHelper.retrieveUserByEmail(ssoUser.getEmail());

        // If user has not been created in Vignette, then create this user
        if (vapUser == null) {
            LOG.info("Not found this user in SP, now creating this user"
                    + ssoUser.getUserName());
            vapUser = firstTimeUser();
        } else { // Try to update user info if needed
            updateUser(vapUser, isVAPLoggedIn);
        }

        addToProfile(getCustomizedProfile());
        profileMap.put(AuthenticationConsts.HEADER_USER_NAME_PROPERTY_NAME,
                ssoUser.getUserName());
        profileMap.put(AuthenticationConsts.HEADER_EMAIL_ADDRESS_PROPERTY_NAME,
                ssoUser.getEmail());
        profileMap.put(AuthenticationConsts.HEADER_FIRST_NAME_PROPERTY_NAME,
                ssoUser.getFirstName());
        profileMap.put(AuthenticationConsts.HEADER_LAST_NAME_PROPERTY_NAME,
                ssoUser.getLastName());
        profileMap.put(AuthenticationConsts.HEADER_LANGUAGE_PROPERTY_NAME,
                ssoUser.getLanguage());
        profileMap.put(AuthenticationConsts.HEADER_COUNTRY_PROPERTY_NAME,
                ssoUser.getCountry());
        profileMap.put(AuthenticationConsts.HEADER_GROUP_NAME, new TreeSet(
                AuthenticatorHelper.getUserGroupTitleSet(AuthenticatorHelper
                        .getUserGroupSet(vapUser))));
        request.getSession().setAttribute(USER_PROFILE_KEY, profileMap);
    }

    protected Map getCustomizedProfile() {
        return new HashMap();
    }

    /**
     * This method is used to perform all related tasks. 1. It will invoke the
     * mapRequest2User() method to map all the information from SSO product to
     * SSOUser object. 2. Invoke the syncUser() method to perform the sync from
     * SSOUser object to VAP. 3. Set the correct user name
     * 
     * @see com.hp.it.spf.sso.portal.IAuthenticator#execute()
     */
    public void execute() {
        mapRequest2User();
        // If user is logged in and session user is different with request user,
        // clean up session
        if (AuthenticatorHelper.isVAPLoggedIn(request) && checkDiffUser()) {
            AuthenticatorHelper.cleanupSession(request);
        }
        if (AuthenticatorHelper.needUpdateVAPUser(request)) {
            syncUser();
        }
        userName = ssoUser.getUserName();
    }

    /**
     * This method is used to return the current site object in session
     * 
     */
    public Site getCurrentSite() {
        SessionInfo sessionInfo = (SessionInfo)request.getSession()
                .getAttribute(SessionInfo.SESSION_INFO_NAME);
        return sessionInfo != null ? sessionInfo.getSite() : null;
    }

    /**
     * This method is used to return the current site object in session
     * 
     */
    public void addToProfile(Map properties) {
        if (properties == null) {
            return;
        }
        profileMap.putAll(properties);
    }
}
