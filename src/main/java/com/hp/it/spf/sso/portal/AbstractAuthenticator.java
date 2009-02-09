/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.sso.portal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.epicentric.common.website.SessionUtils;
import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.entity.UniquePropertyValueConflictException;
import com.epicentric.site.Site;
import com.epicentric.user.User;
import com.hp.it.spf.user.exception.UserGroupsException;
import com.hp.it.spf.user.group.manager.IUserGroupRetriever;
import com.hp.it.spf.user.group.manager.UserGroupRetrieverFactory;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;

/**
 * AbstractAuthenticator is the father authenticator. All other authenticators
 * will be extended from this class.
 * <p>
 * This class defines the public process of doing session initialization.
 * </p>
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @author <link href="ye.liu@hp.com">liuye</link>
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version TBD
 */
public abstract class AbstractAuthenticator implements IAuthenticator {

    /**
     * serialVersionUID long
     */
    private static final long serialVersionUID = 1L;

    private static final LogWrapper LOG = AuthenticatorHelper.getLog(AbstractAuthenticator.class);

    /**
     * User profile information All attributes are retrieved from request
     * header, UPS, Persona, etc.
     */
    @SuppressWarnings("unchecked")
    protected Map userProfile = new HashMap();

    /**
     * rb ResourceBundle
     */
    protected ResourceBundle rb = null;

    /**
     * userName String
     */
    protected String userName = null;

    /**
     * This Object is used to synchronous some user profile inform with vignette
     * user
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
     * @param request HttpServletRequest object
     */
    public AbstractAuthenticator(HttpServletRequest request) {
        this.request = request;

        // retrieve corresponding resourcebundle according to current
        // Authenticator class
        String rbFile = retrieveRbFile();
        try {
            rb = ResourceBundle.getBundle(rbFile);
            LOG.info("Get Resource Bundle File = " + rbFile);
        } catch (Exception e) {
            LOG.error("No Resource Bundle File = " + rbFile);
            throw new RuntimeException("No Resource Bundle File = " + rbFile, e);
        }
    }

    /**
     * This method is used to perform all authenticate related tasks.
     * <ol>
     * <li>It will check if there is a user in session.</li>
     * <li>If there is a user in session, then check if it is different with the
     * user who has just logged in.</li>
     * <li>If the user is same, then check if user recent updated.</li>
     * <li>If the user is not changed, then check if the initSession tag is set</li>
     * <li>If the initSession tag is not set, then check if the primary site is
     * changed</li>
     * </ol>
     * if the user doesn't need to be synchronized to VAP, then return the
     * userName of the current user's name. Otherwise, synchronize user to VAP
     * database and map all user profiles and groups to session.
     * 
     * @see com.hp.it.spf.sso.portal.IAuthenticator#execute()
     */
    @SuppressWarnings("unchecked")
    public void execute() {
        mapHeaderToUserProfileMap();

        if (AuthenticatorHelper.isVAPLoggedIn(request)) {
            if (isDiffUser()) {
                userProfile.put(AuthenticationConsts.KEY_LAST_LOGIN_DATE,
                                new Date());
                AuthenticatorHelper.cleanupSession(request);
            } else if (isUserRecentUpdated()) {
                if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                    LOG.debug("User is updated.");
                }
                AuthenticatorHelper.cleanupSession(request);
            } else if (AuthenticatorHelper.isForceInitSession(request)) {
                if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                    LOG.debug("Force initSession tag found.");
                }
                AuthenticatorHelper.cleanupSession(request);
            } else {
                if (AuthenticatorHelper.needUpdatePrimarySite(request)) {
                    updatePrimarySite();
                }
                userName = (String)userProfile.get(AuthenticationConsts.KEY_USER_NAME);
                return;
            }
        } else {
            userProfile.put(AuthenticationConsts.KEY_LAST_LOGIN_DATE,
                            new Date());
        }

        try {
            // Retrieve user group from UGS to SSOUser
            // and synchronous to VAP user, and return the
            // updated vap user
            User updatedVAPUser = syncVAPUser();
            saveUserProfile2Session(updatedVAPUser);
            userName = ssoUser.getUserName();
        } catch (Exception ex) {
            userName = null;
            LOG.error("Invoke Authenticator.execute error: " + ex.getMessage());
            request.getSession()
                   .setAttribute(AuthenticationConsts.SESSION_ATTR_SSO_ERROR,
                                 "1");
        }
    }

    /**
     * Retrieve user profile attributes from http header, and put them into
     * userProfile map
     */
    @SuppressWarnings("unchecked")
    protected void mapHeaderToUserProfileMap() {
        userProfile.put(AuthenticationConsts.KEY_PROFILE_ID,
                        getValue(AuthenticationConsts.HEADER_PROFILE_ID_PROPERTY_NAME));
        LOG.info("userProfile.PROFILE_ID="
                 + userProfile.get(AuthenticationConsts.KEY_PROFILE_ID));

        userProfile.put(AuthenticationConsts.KEY_USER_NAME,
                        getValue(AuthenticationConsts.HEADER_USER_NAME_PROPERTY_NAME));
        LOG.info("userProfile.USERNAME="
                 + userProfile.get(AuthenticationConsts.KEY_USER_NAME));

        // set email
        String email = getValue(AuthenticationConsts.HEADER_EMAIL_ADDRESS_PROPERTY_NAME);
        if (email != null) {
            int index = email.indexOf("^");
            if (index > -1) {
                email = email.substring(0, index);
            }
        }
        userProfile.put(AuthenticationConsts.KEY_EMAIL, email);

        userProfile.put(AuthenticationConsts.KEY_FIRST_NAME,
                        getValue(AuthenticationConsts.HEADER_FIRST_NAME_PROPERTY_NAME));
        userProfile.put(AuthenticationConsts.KEY_LAST_NAME,
                        getValue(AuthenticationConsts.HEADER_LAST_NAME_PROPERTY_NAME));
        userProfile.put(AuthenticationConsts.KEY_COUNTRY,
                        getValue(AuthenticationConsts.HEADER_COUNTRY_PROPERTY_NAME));
        userProfile.put(AuthenticationConsts.KEY_PHONE_NUMBER, 
                        getValue(AuthenticationConsts.HEADER_PHONE_NUMBER_NAME));

        // Set lanuage, if null, set to default EN
        String language = getValue(AuthenticationConsts.HEADER_LANGUAGE_PROPERTY_NAME);
        if (language == null || ("").equals(language.trim())) {
            userProfile.put(AuthenticationConsts.KEY_LANGUAGE,
                            AuthenticationConsts.DEFAULT_LANGUAGE);
        } else {
            userProfile.put(AuthenticationConsts.KEY_LANGUAGE, language);
        }

        // Set Last Change Date in format
        SimpleDateFormat format = new SimpleDateFormat(getProperty(AuthenticationConsts.HEADER_DATE_FORMAT_NAME));
        String lastChangeStr = getValue(AuthenticationConsts.HEADER_LAST_CHANGE_DATE_PROPERTY_NAME);
        if (lastChangeStr != null) {
            try {
                userProfile.put(AuthenticationConsts.KEY_LAST_CHANGE_DATE,
                                format.parse(lastChangeStr));
            } catch (ParseException pe) {
                LOG.error("Can't set last change date as"
                          + lastChangeStr
                          + pe.getMessage());
            }
        }

        // Set timezone, default value if timezone not found
        String tz = getProperty(getValue(AuthenticationConsts.HEADER_TIMEZONE_PROPERTY_NAME));
        if (tz == null || ("").equals(tz.trim())) {
            userProfile.put(AuthenticationConsts.KEY_TIMEZONE,
                            AuthenticationConsts.DEFAULT_TIMEZONE);
        } else {
            userProfile.put(AuthenticationConsts.KEY_TIMEZONE, tz);
        }
        
        // Set security level, if null, set as default 0
        String securitylevel = getValue(AuthenticationConsts.HEADER_SECURITY_LEVEL_PROPERTY_NAME);
        if (securitylevel != null && (!("").equals(securitylevel.trim()))) {
            try {
                userProfile.put(AuthenticationConsts.KEY_SECURITY_LEVEL,
                                Float.valueOf(securitylevel));                
            } catch (NumberFormatException ne) {
                LOG.error("Can't change security level " + securitylevel
                        + " to float value. Will set default value as 0");
                userProfile.put(AuthenticationConsts.KEY_SECURITY_LEVEL, 
                                AuthenticationConsts.DEFAULT_SECURITY_LEVEL);
            }
        } else {
            userProfile.put(AuthenticationConsts.KEY_SECURITY_LEVEL, 
                            AuthenticationConsts.DEFAULT_SECURITY_LEVEL);
        }
    }

    /**
     * Map the essential attributes used every time from request to ssoUser.
     * These key values are used for sso every time.
     */
    protected void mapUserProfile2SSOUser() {
        ssoUser.setProfileId((String)userProfile.get(AuthenticationConsts.KEY_PROFILE_ID));
        ssoUser.setUserName((String)userProfile.get(AuthenticationConsts.KEY_USER_NAME));
        ssoUser.setEmail((String)userProfile.get(AuthenticationConsts.KEY_EMAIL));
        ssoUser.setFirstName((String)userProfile.get(AuthenticationConsts.KEY_FIRST_NAME));
        ssoUser.setLastName((String)userProfile.get(AuthenticationConsts.KEY_LAST_NAME));
        ssoUser.setCountry((String)userProfile.get(AuthenticationConsts.KEY_COUNTRY));
        ssoUser.setLanguage((String)userProfile.get(AuthenticationConsts.KEY_LANGUAGE));
        ssoUser.setTimeZone((String)userProfile.get(AuthenticationConsts.KEY_TIMEZONE));
        ssoUser.setLastChangeDate((Date)userProfile.get(AuthenticationConsts.KEY_LAST_CHANGE_DATE));
        ssoUser.setLastLoginDate((Date)userProfile.get(AuthenticationConsts.KEY_LAST_LOGIN_DATE));
        // Set current site, it will be used to update user's primary site
        ssoUser.setCurrentSite(AuthenticatorHelper.getPrimarySiteUID(request));
    }

    /**
     * Map user profile information and group information retrieved from vap
     * user into session as a map
     * 
     * @param vapUser vignette user
     */
    @SuppressWarnings("unchecked")
    protected void saveUserProfile2Session(User vapUser) {
        if (vapUser != null) {
            userProfile.put(AuthenticationConsts.KEY_USER_GROUPS,
                            (String[])AuthenticatorHelper.getUserGroupTitleSet(AuthenticatorHelper.getUserGroupSet(vapUser))
                                                         .toArray(new String[0]));
        }
        request.getSession()
               .setAttribute(AuthenticationConsts.USER_PROFILE_KEY, userProfile);
    }

    /**
     * If this user is first time login, create it in Vignette If error occured,
     * an error flag will be set in the session.
     * 
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #createVAPUser(com.hp.it.spf.sso.portal.SSOUser)
     * @return the new created VAP User
     * @throws UniquePropertyValueConflictException
     * @throws EntityPersistenceException
     */
    protected User createVAPUser() throws UniquePropertyValueConflictException,
                                  EntityPersistenceException {
        try {
            return AuthenticatorHelper.createVAPUser(ssoUser);
        } catch (UniquePropertyValueConflictException e) {
            LOG.error("Required unique values conflict when creating user "
                      + e.getMessage());
            throw e;
        } catch (EntityPersistenceException e) {
            LOG.error("Entity persistence exception when creating user "
                      + e.getMessage());
            throw e;
        }
    }

    /**
     * The method is used to update VAP user info It will: Update user's info
     * AND update user's group. If error occured, set an error flag in the
     * session
     * 
     * @param vapUser vignette user
     * @return updated vignette user
     * @throws UniquePropertyValueConflictException if failed
     * @throws EntityPersistenceException if failed
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #needUpdateVAPUser(com.epicentric.user.User, java.util.Date,
     *      java.lang.String)
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #updateVAPUser(com.epicentric.user.User,
     *      com.hp.it.spf.portal.authentication.ssoUser)
     * @see com.epicentric.user.User#save()
     */
    protected User updateVAPUser(User vapUser) throws UniquePropertyValueConflictException,
                                              EntityPersistenceException {
        try {
            // Update user's info if needed
            LOG.info("update basic info for " + ssoUser.getUserName());
            AuthenticatorHelper.updateVAPUser(vapUser, ssoUser);

            // if user is not logged in, get the groups and
            // check if the user's group need to update
            // Get current user's groups
            updateVAPUserGroups(vapUser);

            // Update user to the appropriate authentication groups.
            AuthenticatorHelper.assignUserToAuthenticationGroup(request,
                                                                vapUser);

            // If user's info need to update, save this user
            vapUser.save();

            return vapUser;

        } catch (UniquePropertyValueConflictException e) {
            LOG.error("Required unique values conflict when updating user"
                      + e.getMessage());
            throw e;
        } catch (EntityPersistenceException e) {
            LOG.error("Entity persistence exception when updating user"
                      + e.getMessage());
            throw e;
        }
    }

    /**
     * This method is used to update user's group First, it will retrieve user's
     * group from Vignette and request header If these two groups are different,
     * then update group from request header to Vignette If error occured, set
     * an error flag in the session.
     * 
     * @param user the user retrieved from vignette
     * @return true if user's group is updated, otherwise return false
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #needUpdateGroup(java.util.Set, java.util.Set)
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #updateUserGroup(com.epicentric.user.User, java,util.Set,
     *      java.util.Set)
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #getUserGroupSet(com.epicentric.user.User)
     * @throws EntityPersistenceException if update group failed
     */
    @SuppressWarnings("unchecked")
    protected boolean updateVAPUserGroups(User user) throws EntityPersistenceException {
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
     * The method is used to update VAP user's primary site
     */
    protected void updatePrimarySite() {
        // Update user's info if needed
        LOG.info("update primary site.");
        String siteUID = AuthenticatorHelper.getPrimarySiteUID(request);
        if (siteUID == null) {
            return;
        }
        try {
            User user = SessionUtils.getCurrentUser(request.getSession());
            user.setProperty(AuthenticationConsts.PROPERTY_PRIMARY_SITE_ID,
                             siteUID);

            user.save();

        } catch (UniquePropertyValueConflictException e) {
            LOG.error("Required unique values conflict when updating user"
                      + e.getMessage());
        } catch (EntityPersistenceException e) {
            LOG.error("Entity persistence exception when updating user"
                      + e.getMessage());
        }
    }

    /**
     * This method is used to check whether the user in the vignette session is
     * different with the user in the request header It only compares the
     * profile IDs as they are the the unique identifier in Vignette
     * 
     * @return <tt>true</tt> if the session user is different with user in the
     *         request header
     * @see com.epicentric.common.website.SessionUtils#getCurrentUser(javax.servlet.http.HttpSession)
     */
    protected boolean isDiffUser() {
        String profileIdVap = "";

        // Retrieve current user and its profile ID from session
        User currentUser = SessionUtils.getCurrentUser(request.getSession());
        if (currentUser.getProperty(AuthenticationConsts.PROPERTY_PROFILE_ID) != null) {
            profileIdVap = (String)currentUser.getProperty(AuthenticationConsts.PROPERTY_PROFILE_ID);
        }
        // If profile ID is different, return true
        if (!profileIdVap.equals(userProfile.get(AuthenticationConsts.KEY_PROFILE_ID))) {
            LOG.info("profile_id_vap:" + profileIdVap);
            LOG.info("profileid:"
                     + userProfile.get(AuthenticationConsts.KEY_PROFILE_ID));
            return true;
        }
        LOG.info("same user in session: " + profileIdVap);
        return false;
    }

    /**
     * Check if the lastChangeDate indicates the User profile is updated
     * 
     * @return <code>true</code> if lastChangeDate shows the user profile is
     *         changed. Otherwise, <code>false</code>
     */
    protected boolean isUserRecentUpdated() {
        // Retrieve timeZone, lastChangeDate from userProfile map
        String timeZone = (String)userProfile.get(AuthenticationConsts.KEY_TIMEZONE);
        Date lastChangeDate = (Date)userProfile.get(AuthenticationConsts.KEY_LAST_CHANGE_DATE);

        // Retrieve current user from session
        User currUser = SessionUtils.getCurrentUser(request.getSession());
        
        Date lastChangeDateVap = null;
        Integer tzVap = null;
        // Retrieve user's current timezone offset and last change date
        if (currUser != null) {
            if (currUser.getProperty(AuthenticationConsts.PROPERTY_LAST_CHANGE_DATE_ID) != null) {
                lastChangeDateVap = ((Date)currUser.getProperty(AuthenticationConsts.PROPERTY_LAST_CHANGE_DATE_ID));
            }
            if (currUser.getProperty(AuthenticationConsts.PROPERTY_TIMEZONE_ID) != null) {
                tzVap = (Integer)currUser.getProperty(AuthenticationConsts.PROPERTY_TIMEZONE_ID);
            }
        }
        // Retrieve timezone offset from request header
        Integer tzOffSet = new Integer("0");
        if (timeZone == null) {
            tzOffSet = tzVap;
        } else {
            tzOffSet = AuthenticatorHelper.getTimeZoneOffset(timeZone);
        }
        // Judge
        if (lastChangeDate == null || timeZone == null) {
            return false;
        } else if (lastChangeDateVap == null || tzVap == null) {
            return true;
        } else {
            return lastChangeDate.after(lastChangeDateVap)
                   || !tzVap.equals(tzOffSet);
        }
    }

    /**
     * This method is used to synchronize user's info. This class will create
     * user in Vignette if the user is first time here; or update user if
     * needed; or cleanup session if session user and request user are
     * different; or do nothing if session error flag is caught
     * 
     * @return updated or new vignette user
     * @throws UniquePropertyValueConflictException
     * @throws EntityPersistenceException
     */
    @SuppressWarnings("unchecked")
    protected User syncVAPUser() throws UniquePropertyValueConflictException,
                                EntityPersistenceException {
        // append all external user profile retrieved from UPS/Persona
        Map upMap = getUserProfile();
        if (upMap != null) {
            userProfile.putAll(upMap);
        }
        mapUserProfile2SSOUser();
        ssoUser.setGroups(getUserGroup());

        // Retrieve user from Vignette.
        User vapUser = AuthenticatorHelper.retrieveUserByProperty(AuthenticationConsts.PROPERTY_PROFILE_ID,
                                                                  ssoUser.getProfileId());

        // If user has not been created in Vignette, then create this user
        if (vapUser == null) {
            // If employee number doesn't exist at the beginning, email will
            // be instead of profileid, so later, when employee number is assigned,
            // the previous email based user should be removed from vignette.
            User emailUser = AuthenticatorHelper.retrieveUserByProperty(AuthenticationConsts.PROPERTY_PROFILE_ID,
                                                                        ssoUser.getEmail());
            if (emailUser != null) {
                emailUser.delete();
            }
            LOG.info("Not found this user in SP, now creating this user"
                     + ssoUser.getUserName());
            vapUser = createVAPUser();
        } else { // Try to update user info if needed
            vapUser = updateVAPUser(vapUser);
        }

        return vapUser;
    }

    /**
     * This is the abstract method used to retrieve user profile according to
     * different orignal of the logged in user's information
     * 
     * @return user profile map
     */
    protected Map<String, String> getUserProfile() {
        return null;
    }

    /**
     * This is the abstract method used to retrieve user group info and return
     * as a Set
     * 
     * @return retrieved groups set
     */
    @SuppressWarnings("unchecked")
    protected Set getUserGroup() {
        Site site = AuthenticatorHelper.getCurrentSite(request);
        if (site != null) {
            IUserGroupRetriever retriever = UserGroupRetrieverFactory.createUserGroupImpl(null);

            Set<String> group = null;
            try {
                group = retriever.getGroups(site.getDNSName(), userProfile);
            } catch (UserGroupsException e) {
            }
            return group;
        }
        return null;
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
     * Return corresponding field value in request header
     * 
     * @param fieldName field in request header
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
     * @param key key in resource bundle
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
}
