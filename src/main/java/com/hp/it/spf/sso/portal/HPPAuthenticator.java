/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import com.hp.globalops.hppcbl.passport.manager.PassportParametersManager;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;

/**
 * This authenticator is for HPP users.
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @version TBD
 */
public class HPPAuthenticator extends AbstractAuthenticator {
    private static final long serialVersionUID = 1L;

    private static final com.vignette.portal.log.LogWrapper LOG = AuthenticatorHelper
            .getLog(HPPAuthenticator.class);

    private static String clHeaderList = null;

    private String clHeaderHpp = null;

    private String adminSessionToken = null;

    private String adminUserName = null;

    private String adminUserPWD = null;

    /**
     * This is the constructor for HPP Authenticator. First, it will call the
     * constructor of AbstractAuthenticator which is its father class to do some
     * SSO initialization task. After that, it will do some initialization for
     * HPP web service calls.
     * 
     * @param request
     *            HttpServletRequest object
     * @see com.hp.it.spf.sso.portal.AbstractAuthenticator
     *      #AbstractAuthenticator(javax.servlet.http.HttpServletRequest)
     */
    public HPPAuthenticator(HttpServletRequest request) {
        super(request);
        PassportParametersManager wsManagerInstance = PassportParametersManager
                .getInstance();
        adminUserName = wsManagerInstance.getAdminUser();
        adminUserPWD = wsManagerInstance.getAdminPassword();
    }

    /**
     * Return corresponding field value in request header, If field is in
     * cl_header,get its value from CL_HEADER Otherwise, return the decoded
     * value in request header.
     * 
     * @param fieldName
     *            field name in request header
     * @return corresponding field in request header, null if field not found
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #getValuesFromCLHeader(java.lang.String, java.langString)
     * @see com.hp.it.spf.sso.portal.AuthenticatorHelper
     *      #getRequestHeader(javax.servlet.http.HttpServletRequest,
     *      java.lang.String, boolean)
     */
    protected String getValue(String fieldName) {
        LOG.info("HPP getValue: " + fieldName);
        String temp = getProperty(fieldName);
        if (temp != null) {
            if (clHeaderList.indexOf(temp) != -1) {
                // Return field value from CL Header
                return AuthenticatorHelper.getValuesFromCLHeader(clHeaderHpp,
                        temp);
            } else {
                return AuthenticatorHelper
                        .getRequestHeader(request, temp, true);
            }
        }
        return null;
    }

    /**
     * This method is used to map the primary attributes used every time from
     * request to ssoUser. First, it will get CL Header List from property file
     * And get CL Header value from request header. Then it will call the
     * mapRequest2User method of AbstractAuthenticator which is its father class
     * 
     * @see com.hp.it.spf.sso.portal.AbstractAuthenticator
     *      #mapRequest2User()
     */
    protected void mapRequest2User() {
        if (clHeaderList == null) {
            clHeaderList = getProperty(AuthenticationConsts.HEADER_CL_HEADER_LIST_PROPERTY_NAME);
        }
        clHeaderHpp = getValue(AuthenticationConsts.HEADER_CL_HEADER_PROPERTY_NAME);
        super.mapRequest2User();
        ssoUser
                .setLanguage(I18nUtility
                        .hppLanguageToISOLanguage(getValue(AuthenticationConsts.HEADER_LANGUAGE_PROPERTY_NAME)));
    }

    /**
     * This method is used to get HPP groups. It will call HPP web service to
     * get groups. If failed to call web service, it will return a default group
     * for this user. The default group is constructed with user's current site.
     * 
     * @see com.hp.globalops.hppcbl.passport.PassportService
     *      #getUserGroups(jan.lang.String,
     *      com.hp.globalops.hppcbl.webservice.ProfileIdentity)
     * @return retrieved HPP groups
     */
    protected Set retrieveGroup() {
        String groupString = getValue(AuthenticationConsts.HEADER_GROUP_NAME);
        LOG.info("The groups string got from HPP request header is: "
                + groupString);

        Set group = new HashSet();
        StringTokenizer st = new StringTokenizer(groupString, "|");
        while(st.hasMoreTokens()) {
            group.add(st.nextToken());
        }
        return group;
    }
}
