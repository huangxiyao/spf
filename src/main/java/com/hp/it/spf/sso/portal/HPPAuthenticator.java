/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.vignette.portal.log.LogWrapper;

/**
 * This authenticator is for HPP users.
 * 
 * @author <link href="kaijian.ding@hp.com">dingk</link>
 * @author <link href="ye.liu@hp.com">liuye</link>
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * 
 * @version TBD
 * 
 * @see com.hp.it.spf.sso.portal.AbstractAuthenticator
 */
public class HPPAuthenticator extends AbstractAuthenticator {
    private static final long serialVersionUID = 1L;

    private static final LogWrapper LOG = AuthenticatorHelper.getLog(HPPAuthenticator.class);

    private static String clHeaderList = null;

    private String clHeaderHpp = null;

    /**
     * This is the constructor for HPP Authenticator. First, it will call the
     * constructor of AbstractAuthenticator which is its father class to do some
     * SSO initialization task. After that, it will do some initialization for
     * HPP web service calls. It will get CL Header List from property file
     * And get CL Header value from request header.
     * 
     * @param request
     *            HttpServletRequest object
     * @see com.hp.it.spf.sso.portal.AbstractAuthenticator
     *      #AbstractAuthenticator(javax.servlet.http.HttpServletRequest)
     */
    public HPPAuthenticator(HttpServletRequest request) {
        super(request);
              
    	// retrieve the cl_header list
    	if (clHeaderList == null) {
            clHeaderList = getProperty(AuthenticationConsts.HEADER_CL_HEADER_LIST_PROPERTY_NAME);
        }
    	clHeaderHpp = getValue(AuthenticationConsts.HEADER_CL_HEADER_PROPERTY_NAME);
    }    

    /**
     * @see AbstractAuthenticator#mapHeaderToUserProfileMap()
     */
    @SuppressWarnings("unchecked")
    protected void mapHeaderToUserProfileMap() {
    	super.mapHeaderToUserProfileMap();    	
    	
    	// Set lanuage, change language from HPP format to ISO standard
		String language = (String)userProfile.get(AuthenticationConsts.KEY_LANGUAGE);
		userProfile.put(AuthenticationConsts.KEY_LANGUAGE, 
						I18nUtility.hppLanguageToISOLanguage(language));
	}
    
    /**
     * Retrieve user profile for HPP
     */
    protected Map<String, String> getUserProfile() {
    	return null;
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
                return AuthenticatorHelper.getValuesFromCLHeader(clHeaderHpp, temp);
            } else {
                return AuthenticatorHelper.getRequestHeader(request, temp, true);
            }
        }
        return null;
    }
}
