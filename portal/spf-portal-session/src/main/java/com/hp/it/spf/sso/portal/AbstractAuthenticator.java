/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.sso.portal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.epicentric.common.website.SessionUtils;
import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.entity.UniquePropertyValueConflictException;
import com.epicentric.site.Site;
import com.epicentric.user.User;
import com.hp.it.spf.user.exception.UserProfileException;
import com.hp.it.spf.user.profile.manager.IUserProfileRetriever;
import com.hp.it.spf.user.profile.manager.UserProfileRetrieverFactory;
import com.hp.it.spf.xa.dc.portal.ErrorCode;
import com.hp.it.spf.xa.log.portal.Operation;
import com.hp.it.spf.xa.log.portal.TimeRecorder;
import com.hp.it.spf.xa.misc.portal.RequestContext;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;

/**
 * AbstractAuthenticator is the super authenticator and defines common process
 * logic. All other authenticators will be extended from this class.
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
     * The default constructor
     */
    protected AbstractAuthenticator() {
    }

    /**
     * The constructor of AbstractAuthenticator class. It will do some
     * initialization for authenticators
     * 
     * @param request HttpServletRequest object
     */
    public AbstractAuthenticator(HttpServletRequest request) {
        this.request = request;

        // retrieve corresponding resource bundle according to current
        // Authenticator class
        String rbFile = retrieveRbFile();
        try {
            rb = ResourceBundle.getBundle(rbFile);
            if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                LOG.debug("Get Resource Bundle File = " + rbFile);
            }
        } catch (Exception e) {
            LOG.error("No Resource Bundle File = " + rbFile, e);
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
     * <li>If user changes the site</li>
     * </ol>
     * if the user doesn't need to be synchronized to VAP, then return the
     * userName of the current user's name. Otherwise, synchronize user to VAP
     * database and map all user profiles and groups to session.
     * 
     * @see com.hp.it.spf.sso.portal.IAuthenticator#execute()
     */
    @SuppressWarnings("unchecked")
    public void execute() {
    	long startTime = System.currentTimeMillis();
    	
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug(String.format("Entering AbstractAuthenticator, userid: %s", ssoUser!=null?ssoUser.getUserName():"null"));
        }
        mapHeaderToUserProfileMap();

        if (AuthenticatorHelper.isVAPLoggedIn(request)) {
        	String userIdentifier = (String)userProfile.get(AuthenticationConsts.KEY_USER_NAME);
            if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                LOG.debug(String.format("User is Logged in, user: %s", userIdentifier));
            }
            if (AuthenticatorHelper.isForceCleanupSession(request)) {
                if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                    LOG.debug(String.format("Force session clean up, userid: %s", userIdentifier));
                }
                AuthenticatorHelper.cleanupSession(request);
            } else if (isDiffUser()) {
                if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                    LOG.debug(String.format("Different User, userid: %s", userIdentifier));
                }
                userProfile.put(AuthenticationConsts.KEY_LAST_LOGIN_DATE,
                                new Date());
                AuthenticatorHelper.cleanupSession(request);
            } else if (isUserRecentUpdated()) {
                if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                    LOG.debug(String.format("User is updated, userid: %s", userIdentifier));
                }
                // not cleanup session per CR 86
                //AuthenticatorHelper.cleanupSession(request);
            } else if (AuthenticatorHelper.isForceInitSession(request)) {
                if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                    LOG.debug(String.format("Force initSession tag found, userid: %s", userIdentifier));
                }
                // not cleanup session per CR 86
                //AuthenticatorHelper.cleanupSession(request);
            } else if (AuthenticatorHelper.isSiteChanged(request)) {
                if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                    LOG.debug(String.format("Site is changed, userid: %s", userIdentifier));
                }
                // not cleanup session per CR 86
                //AuthenticatorHelper.cleanupSession(request);
            } else {
                if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                    LOG.debug(String.format("Other Cases, userid: %s", userIdentifier));
                }
                // keep the current HTTP header user profile
                Map httpHeaderUserProfile = userProfile;

                // fetch userProfile from session, only need to refresh this user profile map
                userProfile = (Map)request.getSession()
                                          .getAttribute(AuthenticationConsts.USER_PROFILE_KEY);
                // get groups from userProfile
                List<String> sessionGroups = (List<String>)userProfile.get(AuthenticationConsts.KEY_USER_GROUPS);
                refreshLocaleRelatedGroups(sessionGroups);

                // refresh user timezone
                refreshUserTimezone(userProfile, httpHeaderUserProfile);
                
                userName = (String)userProfile.get(AuthenticationConsts.KEY_USER_NAME);
                return;
            }
        } else {
        	if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                LOG.debug(String.format("User is logging in as: %s", ssoUser!=null?ssoUser.getUserName():"null"));
            }
            userProfile.put(AuthenticationConsts.KEY_LAST_LOGIN_DATE,
                            new Date());
        }


        // Set the SPFAuthType attribute before calling group service so the group definitions can
        // use its value.
        setAuthTypeProfileAttribute();

        try {
            // Retrieve user group from UGS to SSOUser
            // and synchronous to VAP user, and return the
            // updated vap user
            User updatedVAPUser = syncVAPUser();
            saveUserProfile2Session(updatedVAPUser);
            userName = ssoUser.getUserName();
        } catch (Exception ex) {
            userName = null;
            LOG.error("Invoke Authenticator.execute error: " + ex.getMessage(),
                      ex);
            request.getSession()
                   .setAttribute(AuthenticationConsts.SESSION_ATTR_SSO_ERROR,
                                 "1");
            RequestContext.getThreadInstance()
                          .getDiagnosticContext()
                          .setError(ErrorCode.AUTH001, ex.toString());
        }
    	if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug(String.format("Time spent on AbstractAuthenticator.execute (sec): %s for user: %s", (System.currentTimeMillis()-startTime)/1000, userName));
        }
    }

    /**
     * This method is used to update user's locale groups. It will get
     * country and langauge from session and find out if they are different with
     * the one from locale resolver if different, update the groups in vignette
     * and save back to session
     */
    @SuppressWarnings("unchecked")
    protected void refreshLocaleRelatedGroups(List<String> sessionGroups) {
        Set<String> inputGroups = new HashSet<String>();
        String language = "";
        String country = "";
        Iterator<String> iterator = sessionGroups.iterator();
        while (iterator.hasNext()) {
            String group = iterator.next();
            if (!group.startsWith(AuthenticationConsts.LOCAL_PORTAL_LANG_PREFIX)
                && !group.startsWith(AuthenticationConsts.LOCAL_PORTAL_COUNTRY_PREFIX)) {
                // find all groups which are not locale groups
                inputGroups.add(group);
            }
            if (group.startsWith(AuthenticationConsts.LOCAL_PORTAL_LANG_PREFIX)) {
                language = group.replace(AuthenticationConsts.LOCAL_PORTAL_LANG_PREFIX,
                                         "");
            }
            if (group.startsWith(AuthenticationConsts.LOCAL_PORTAL_COUNTRY_PREFIX)) {
                country = group.replace(AuthenticationConsts.LOCAL_PORTAL_COUNTRY_PREFIX,
                                        "");
            }
        }
        Locale reqLocale = (Locale)request.getAttribute(AuthenticationConsts.SSO_USER_LOCALE);
        // if locale change
        if (!(reqLocale.getLanguage().equalsIgnoreCase(language) && reqLocale.getCountry()
                                                                             .equalsIgnoreCase(country))) {
            // add new locale group to the final group set
            inputGroups.addAll(getLocaleGroups());
            ssoUser.setGroups(inputGroups);
            try {
                // Retrieve user from session, in current situation user will
                // not be null
                User currentUser = SessionUtils.getCurrentUser(request.getSession());
                updateVAPUserGroups(currentUser);
                saveUserProfile2Session(currentUser);
            } catch (EntityPersistenceException ex) {
                LOG.error("Invoke Authenticator.execute error: updateVAPUserGroups error: "
                                  + ex.getMessage(),
                          ex);
            }
        }
    }

    /**
     * Refresh user timezone in the session if timezone is changed or timezone's offset is changed.
     * 
     * @param sessionUserProfile user profile saved in session
     * @param headerUserProfile user profile constructed by the http header
     */
    @SuppressWarnings("unchecked")
    protected void refreshUserTimezone(Map sessionUserProfile, Map headerUserProfile) {
        // User timezone in the user profile map should always have a valid value, therefor, don't need
        // to check the value is null or blank
        String headerTimezone = (String)headerUserProfile.get(AuthenticationConsts.KEY_TIMEZONE);
        String sessionTimezone = (String)sessionUserProfile.get(AuthenticationConsts.KEY_TIMEZONE);
        
        User currentUser = SessionUtils.getCurrentUser(request.getSession());
        boolean needRefreshTZ = false;
        if (headerTimezone.equals(sessionTimezone)) {
            // due to thread reason, when this method is being invoked, there will be a thread
            // already modified the vignette datebase, but the value in this end user's session 
            // is not refreshed, so the timezone should be checked between session and database again.
            String vgnTimezone = (String)currentUser.getProperty(AuthenticationConsts.PROPERTY_SPF_TIMEZONE_ID);
            if (headerTimezone.equals(vgnTimezone)) {
                // if timezone is same, then check their offset is same or not
                Integer tzVap = new Integer("0");
                if (currentUser.getProperty(AuthenticationConsts.PROPERTY_TIMEZONE_ID) != null) {
                    tzVap = (Integer)currentUser.getProperty(AuthenticationConsts.PROPERTY_TIMEZONE_ID);
                }
                if (!AuthenticatorHelper.getTimeZoneOffset(headerTimezone).equals(tzVap)) {
                    needRefreshTZ = true;
                }
            } else {
                needRefreshTZ = true;
            }
        } else {
            needRefreshTZ = true;
        }
        // refresh timezone in the session
        if (needRefreshTZ) {
            // the timezone http header in the user profile map also need to be updated.
            sessionUserProfile.put(getProperty(AuthenticationConsts.HEADER_TIMEZONE_PROPERTY_NAME),
                                   headerUserProfile.get(getProperty(AuthenticationConsts.HEADER_TIMEZONE_PROPERTY_NAME)));
            sessionUserProfile.put(AuthenticationConsts.KEY_TIMEZONE, headerTimezone);            
            AuthenticatorHelper.updateVAPUserTimeZone(currentUser,
                                                      headerTimezone);
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
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("userProfile.PROFILE_ID="
                      + userProfile.get(AuthenticationConsts.KEY_PROFILE_ID));
        }

        userProfile.put(AuthenticationConsts.KEY_USER_NAME,
                        getValue(AuthenticationConsts.HEADER_USER_NAME_PROPERTY_NAME));
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("userProfile.USERNAME="
                      + userProfile.get(AuthenticationConsts.KEY_USER_NAME));
        }

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

        // Set language, if null, set to default EN
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
                          + pe.getMessage(), pe);
            }
        }

        // Set timezone, default value if timezone not found
        String tz = getProperty(getValue(AuthenticationConsts.HEADER_TIMEZONE_PROPERTY_NAME));
        if (tz == null || ("").equals(tz.trim())) {
            Locale reqLocale = (Locale)request.getAttribute(AuthenticationConsts.SSO_USER_LOCALE);
            userProfile.put(AuthenticationConsts.KEY_TIMEZONE,
                            AuthenticatorHelper.getUserTimeZoneByLocale(reqLocale));
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
                LOG.error("Can't change security level "
                          + securitylevel
                          + " to float value. Will set default value as 0", ne);
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
            // set last login date if user is same
            if (userProfile.get(AuthenticationConsts.KEY_LAST_LOGIN_DATE) == null) {
                Date lastLoginDate = (Date)vapUser.getProperty(AuthenticationConsts.PROPERTY_LAST_LOGIN_DATE_ID);
                userProfile.put(AuthenticationConsts.KEY_LAST_LOGIN_DATE,
                                lastLoginDate);
            }

            // set user groups
            userProfile.put(AuthenticationConsts.KEY_USER_GROUPS,
                            Collections.list(Collections.enumeration(AuthenticatorHelper.getUserGroupTitleSet(AuthenticatorHelper.getUserGroupSet(vapUser)))));
        }
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("Saving user profile map in session: %s" + userProfile);
        }
        request.getSession()
               .setAttribute(AuthenticationConsts.USER_PROFILE_KEY, userProfile);
    }

    /**
     * Sets the authentication type attribute {@link AuthenticationConsts#KEY_AUTH_TYPE} value
     * in the user profile map.
     */
    @SuppressWarnings("unchecked")
    protected void setAuthTypeProfileAttribute()
    {
        if (AuthenticationUtility.isFromAtHP(request)) {
            // If the user is from atHP webagent, add a property SPFAuthType="ATHP"
            // into the user profile map.
            userProfile.put(AuthenticationConsts.KEY_AUTH_TYPE,
                    AuthenticationConsts.AUTH_TYPE_ATHP);
        } else if (AuthenticationUtility.isFromFed(request)) {
            // If the user is from HPP webagent and it's a federated site,
            // add a property SPFAuthType="FED" into the user profile map.
            userProfile.put(AuthenticationConsts.KEY_AUTH_TYPE,
                    AuthenticationConsts.AUTH_TYPE_FED);
        } else {
            // By default, add a property SPFAuthType="HPP" into the user profile map
            // because we think for most cases, the application is for HPP users.
            userProfile.put(AuthenticationConsts.KEY_AUTH_TYPE,
                    AuthenticationConsts.AUTH_TYPE_HPP);
        }
    }

    /**
     * If this user is first time login, create it in Vignette If error
     * occurred, an error flag will be set in the session.
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
                      + e.getMessage(), e);
            throw e;
        } catch (EntityPersistenceException e) {
            LOG.error("Entity persistence exception when creating user "
                      + e.getMessage(), e);
            throw e;
        }
    }

    /**
     * The method is used to update VAP user info It will: Update user's info
     * AND update user's group. If error occurred, set an error flag in the
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
            if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                LOG.debug("update basic info for " + ssoUser.getUserName());
            }
            AuthenticatorHelper.updateVAPUser(vapUser, ssoUser);

            // if user is not logged in, get the groups and
            // check if the user's group need to update
            // Get current user's groups
            updateVAPUserGroups(vapUser);

            // If user's info need to update, save this user
            vapUser.save();

            return vapUser;

        } catch (UniquePropertyValueConflictException e) {
            LOG.error("Required unique values conflict when updating user"
                      + e.getMessage(), e);
            throw e;
        } catch (EntityPersistenceException e) {
            LOG.error("Entity persistence exception when updating user"
                      + e.getMessage(), e);
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
            if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                LOG.debug("Update group info for " + ssoUser.getUserName());
            }
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
            if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                LOG.debug(String.format("profile_id_vap: %s; profileid: %s; userId: %s", 
                          profileIdVap,
                          userProfile.get(AuthenticationConsts.KEY_PROFILE_ID),
                          userProfile.get(AuthenticationConsts.KEY_USER_NAME)));
            }
            return true;
        }
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug(String.format("same user in session, profileid: %s; userid: %s", 
            			profileIdVap, 
            			userProfile.get(AuthenticationConsts.KEY_USER_NAME)));
        }
        return false;
    }

    /**
     * Check if the lastChangeDate indicates the User profile is updated.
     * <p>
     * The lastChangeDate retrieved from http request header should always be compared with
     * the same value in User Profile map saved in the http session.
     * </p>
     * <p>
     * Compare with the value in Vignette user object is not correct. Since some of the threads
     * may update the vignette database first. then the lastChangeDate in the db is modified.
     * Thus, this method will return false, which will cause user doesn't be updated in the session.
     * </p>
     * 
     * @return <code>true</code> if lastChangeDate shows the user profile is
     *         changed. Otherwise, <code>false</code>
     */
    protected boolean isUserRecentUpdated() {
        Date lastChangeDate = (Date)userProfile.get(AuthenticationConsts.KEY_LAST_CHANGE_DATE);

        // Retrieve current user profile from session  
        @SuppressWarnings("unchecked")
        Map sessionUserProfile = (Map)request.getSession().getAttribute(AuthenticationConsts.USER_PROFILE_KEY);

        Date lastChangeDateInSession = null;
        // Retrieve user's current timezone offset and last change date
        if (sessionUserProfile != null) {
            if (sessionUserProfile.get(AuthenticationConsts.KEY_LAST_CHANGE_DATE) != null) {
                lastChangeDateInSession = ((Date)sessionUserProfile.get(AuthenticationConsts.KEY_LAST_CHANGE_DATE));
            }            
        }

        // Judge
        if (lastChangeDate == null) {
            return false;
        } else if (lastChangeDateInSession == null) {
            return true;
        } else {
            return lastChangeDate.after(lastChangeDateInSession);
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
        userProfile.putAll(getUserProfile());

        mapUserProfile2SSOUser();
        Set groups = getUserGroups();
        groups.addAll(getLocaleGroups());
        ssoUser.setGroups(groups);

        // Retrieve user from Vignette.
        User vapUser = AuthenticatorHelper.retrieveUserByProperty(AuthenticationConsts.PROPERTY_PROFILE_ID,
                                                                  ssoUser.getProfileId());

        // If user has not been created in Vignette, then create this user
        if (vapUser == null) {
            // If employee number doesn't exist at the beginning, email will
            // be instead of profile id, so later, when employee number is
            // assigned,
            // the previous email based user should be removed from vignette.
            User emailUser = AuthenticatorHelper.retrieveUserByProperty(AuthenticationConsts.PROPERTY_PROFILE_ID,
                                                                        ssoUser.getEmail());
            if (emailUser != null) {
                emailUser.delete();
            }
            if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                LOG.debug("Not found this user in SP, now creating this user"
                          + ssoUser.getUserName());
            }
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
     * @return user profile map or an empty map
     */
    protected Map<String, Object> getUserProfile() {
        TimeRecorder timeRecorder = RequestContext.getThreadInstance()
                                                  .getTimeRecorder();
        String profileId = (String)userProfile.get(AuthenticationConsts.KEY_PROFILE_ID);

        Site site = Utils.getEffectiveSite(request);
        String siteDNSName = (site == null ? null : site.getDNSName());

        IUserProfileRetriever retriever = UserProfileRetrieverFactory.createUserProfileImpl(AuthenticationConsts.USER_PROFILE_RETRIEVER, siteDNSName);
        try {
            timeRecorder.recordStart(Operation.PROFILE_CALL);
            Map<String, Object> userProfile = retriever.getUserProfile(profileId,
                                                                       request);
            timeRecorder.recordEnd(Operation.PROFILE_CALL);
            return userProfile;
        } catch (UserProfileException ex) {
            timeRecorder.recordError(Operation.PROFILE_CALL, ex);
            RequestContext.getThreadInstance()
                          .getDiagnosticContext()
                          .setError(ErrorCode.PROFILE001, ex.toString());
            throw ex;
        }
    }

    /**
     * This is the abstract method used to retrieve user group info and return
     * as a Set
     * 
     * @return retrieved groups set or an empty set
     */
    protected abstract Set getUserGroups();

    /**
     * This is the method used to retrieve locale groups info and return as a
     * Set
     * 
     * @return retrieved groups set or an empty set
     */
    @SuppressWarnings("unchecked")
    protected Set getLocaleGroups() {
        Set<String> group = new HashSet<String>();

        // set group according to user locale retrieved by locale resolver
        Locale reqLocale = (Locale)request.getAttribute(AuthenticationConsts.SSO_USER_LOCALE);
        String language = reqLocale.getLanguage().toUpperCase();
        group.add(AuthenticationConsts.LOCAL_PORTAL_LANG_PREFIX + language);
        String country = reqLocale.getCountry().trim().toUpperCase();
        if (country.length() > 0) {
            group.add(AuthenticationConsts.LOCAL_PORTAL_COUNTRY_PREFIX
                      + country);
        }

        return group;
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
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper#getRequestHeader(javax.servlet.http.HttpServletRequest,
     *      String)
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
            LOG.error("Can't find key " + key + "in resource bundle file", ex);
            return null;
        }
    }

    /**
     * Get the userName
     * 
     * @return the userName
     */
    public String getUserName() {
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("Return userName: " + userName);
        }
        return userName;
    }
}
