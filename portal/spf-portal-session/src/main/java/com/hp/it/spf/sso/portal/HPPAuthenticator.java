/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.sso.portal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.spf.user.exception.UserProfileException;
import com.hp.it.spf.user.group.manager.IUserGroupRetriever;
import com.hp.it.spf.user.group.manager.UserGroupRetrieverFactory;
import com.hp.it.spf.user.profile.manager.HPPWebServiceUserProfileRetriever;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;

/**
 * This authenticator is for HPP users.
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @author <link href="ye.liu@hp.com">liuye</link>
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version TBD
 * @see com.hp.it.spf.sso.portal.AbstractAuthenticator
 */
public class HPPAuthenticator extends AbstractAuthenticator {
    private static final long serialVersionUID = 1L;

    private static final LogWrapper LOG = AuthenticatorHelper.getLog(HPPAuthenticator.class);

    /**
     * This is the constructor for HPP Authenticator. First, it will call the
     * constructor of AbstractAuthenticator which is its father class to do some
     * SSO initialization task. After that, it will do some initialization for
     * HPP web service calls. It will get CL Header List from property file And
     * get CL Header value from request header.
     * 
     * @param request HttpServletRequest object
     * @see com.hp.it.spf.sso.portal.AbstractAuthenticator
     *      #AbstractAuthenticator(javax.servlet.http.HttpServletRequest)
     */
    public HPPAuthenticator(HttpServletRequest request) {
        super(request);
    }

    /**
     * @see AbstractAuthenticator#mapHeaderToUserProfileMap()
     */
    @SuppressWarnings("unchecked")
    protected void mapHeaderToUserProfileMap() {
        // retrieve HPP account values from http header
        userProfile.putAll(this.getAccountHeaderValuesAsMap());

        // retrieve timezone from CL_TimezoneHeader and save it to the user profile map
        // it should be done before the super mapHeaderToUserProfileMap method.
        String timezone = getCLTimeZoneHeaderValue();
        userProfile.put(getProperty(AuthenticationConsts.HEADER_TIMEZONE_PROPERTY_NAME),
                        timezone);

        super.mapHeaderToUserProfileMap();

        // Set language, change language from HPP format to ISO standard
        String language = (String)userProfile.get(AuthenticationConsts.KEY_LANGUAGE);
        userProfile.put(AuthenticationConsts.KEY_LANGUAGE,
                        I18nUtility.hppLanguageToISOLanguage(language));

        // retrieve HPP specific attributes. e.g. email pref, phone, etc.
        userProfile.put(AuthenticationConsts.KEY_EMAIL_PREF,
                        getValue(AuthenticationConsts.HEADER_EMAIL_CONTACT_PREF_PROPERTY_NAME));
        userProfile.put(AuthenticationConsts.KEY_PHONE_PREF,
                        getValue(AuthenticationConsts.HEADER_PHONE_CONTACT_PREF_PROPERTY_NAME));
        userProfile.put(AuthenticationConsts.KEY_POSTAL_PREF,
                        getValue(AuthenticationConsts.HEADER_POSTAL_CONTACT_PREF_PROPERTY_NAME));

        setPhone();

        if (AuthenticatorHelper.isVAPLoggedIn(request)) {
        	if (isDiffUser() || isUserRecentUpdated()) {
                long startTime = System.currentTimeMillis();
		        // populate userProfile attributes from HPP WebServices retriever, if attributes already exist, 
        		// their values will be overridden by data by HPP/WS. If not, the new attributes will be added.
		        String userIdentifier = (String)userProfile.get(AuthenticationConsts.KEY_USER_NAME);
		        userProfile.putAll(new HPPWebServiceUserProfileRetriever().getUserProfile(userIdentifier, request));
		        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
		            LOG.debug(String.format("Time Spent on HPP WS call (sec): %s for user: %s", (System.currentTimeMillis()-startTime)/1000, userIdentifier));
		        }
        	}
        }
    }

    /**
     * Return corresponding field value carried in request header from user
     * profile map.
     * 
     * @param fieldName field name in request header
     * @return corresponding field in request header, null if field not found
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #getValuesFromCLHeader(java.lang.String, java.langString)
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #getRequestHeader(javax.servlet.http.HttpServletRequest,
     *      java.lang.String, boolean)
     */
    protected String getValue(String fieldName) {
        String temp = getProperty(fieldName);
        String value = null;
        if (temp != null) {
            value = (String)userProfile.get(temp);
        }
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("HPP getValue: " + fieldName + "=" + value);
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    private void setPhone() {
        String number = getValue(AuthenticationConsts.HEADER_PHONE_NUMBER_NAME);
        String ext = getValue(AuthenticationConsts.HEADER_PHONE_EXT);

        userProfile.put(AuthenticationConsts.KEY_PHONE_NUMBER, number);
        userProfile.put(AuthenticationConsts.KEY_PHONE_NUMBER_EXT, ext);
    }

    /**
     * Retrieve user groups from HPP/Fed header or webservice and invoke related
     * super method to retrieve user groups from other sources
     * 
     * @return retrieved groups set or an empty set
     */
    @SuppressWarnings("unchecked")
    protected Set getUserGroups() {
        Set<String> groups = new HashSet<String>();

        // login HPP/Fed
        if (AuthenticatorHelper.loggedIntoHPP(request)) {
            groups.add(AuthenticationConsts.LOCAL_HPP_NAME);
        } else if (AuthenticatorHelper.loggedIntoFed(request)) {
            groups.add(AuthenticationConsts.LOCAL_FED_NAME);
        }

        // retrieve groups from UserGroupRetriever
        IUserGroupRetriever retriever = UserGroupRetrieverFactory.createUserGroupImpl(AuthenticationConsts.HPP_USER_GROUP_RETRIEVER);
        groups.addAll(retriever.getGroups(userProfile, request));

        // set authenticated user group
        groups.add(AuthenticationConsts.LOCAL_PORTAL_AUTHENTICATED_USERS);
        return groups;
    }

    /**
     * Retrieve user profile from user profile retriever for HPP user.
     * 
     * @return user profile map or an empty map
     * @throws UserProfileException if retrieving user profiles error
     */
    protected Map<String, Object> getUserProfile() {
        request.setAttribute(AuthenticationConsts.USER_IDENTIFIER_TYPE,
                             EUserIdentifierType.EXTERNAL_USER);
        return super.getUserProfile();
    }

    /**
     * Retrieve all account values from request as a map.
     * <p>
     * It retrieves Account-Header, Account-BusHeader, Account-HomeHeader and
     * CL_Header values as a map from http request.
     * </p>
     * 
     * @return account value map or an empty map
     */
    private Map<String, String> getAccountHeaderValuesAsMap() {
        Map<String, String> map = new HashMap<String, String>();

        String ac = AuthenticatorHelper.getRequestHeader(request,
                                                         getProperty(AuthenticationConsts.ACCOUNT_HEADER),
                                                         true);
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("Retrieve ACCOUNT_HEADER: " + ac);
        }
        map.putAll(convertAccountHeaderValueToMap(ac));
        String ahc = AuthenticatorHelper.getRequestHeader(request,
                                                          getProperty(AuthenticationConsts.ACCOUNT_HOMEHEADER),
                                                          true);
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("Retrieve ACCOUNT_HOMEHEADER: " + ahc);
        }
        map.putAll(convertAccountHeaderValueToMap(ahc));
        String absc = AuthenticatorHelper.getRequestHeader(request,
                                                           getProperty(AuthenticationConsts.ACCOUNT_BUSHEADER),
                                                           true);
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("Retrieve ACCOUNT_BUSHEADER: " + absc);
        }
        map.putAll(convertAccountHeaderValueToMap(absc));
        String cl_header = AuthenticatorHelper.getRequestHeader(request,
                                                                getProperty(AuthenticationConsts.CL_HEADER),
                                                                true);
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("Retrieve CL_HEADER: " + cl_header);
        }
        map.putAll(convertAccountHeaderValueToMap(cl_header));

        return map;
    }

    /**
     * Convert String which format is
     * <code>|key=value|key1=value1|key2=value2</code> to map
     * 
     * @param value string value
     * @return value map or an empty map
     */
    private Map<String, String> convertAccountHeaderValueToMap(String value) {
        Map<String, String> map = new HashMap<String, String>();
        if (value != null && !"".equals(value.trim())) {
            StringTokenizer st = new StringTokenizer(value, "|");
            while (st.hasMoreTokens()) {
                String[] key_value = st.nextToken().split("=", 2);
                if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                    LOG.debug("Retrieve key_value: " + key_value);
                }
                if (key_value.length == 2) {
                    map.put(key_value[0].trim(), key_value[1].trim());
                }
            }
        }
        return map;
    }

    /**
     * Retrieve CL_TimezoneHeader from http request.
     * 
     * @return CL_TimezoneHeader value or null if not carried in the http
     *         request.
     */
    private String getCLTimeZoneHeaderValue() {
        String cl_tzheader = AuthenticatorHelper.getRequestHeader(request,
                                                                  getProperty(AuthenticationConsts.HEADER_TIMEZONE_PROPERTY_NAME),
                                                                  true);
        return cl_tzheader;
    }
}
