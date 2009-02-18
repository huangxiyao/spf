/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.sso.portal;

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
		
		// retrieve HPP specific attributes. e.g. email pref, phone, etc.
		userProfile.put(AuthenticationConsts.KEY_EMAIL_PREF, 
		                getValue(AuthenticationConsts.HEADER_EMAIL_CONTACT_PREF_PROPERTY_NAME));		
		userProfile.put(AuthenticationConsts.KEY_PHONE_NUMBER_EXT, 
                        getValue(AuthenticationConsts.HEADER_PHONE_EXT));
		userProfile.put(AuthenticationConsts.KEY_PHONE_PREF, 
                        getValue(AuthenticationConsts.HEADER_PHONE_CONTACT_PREF_PROPERTY_NAME));
		userProfile.put(AuthenticationConsts.KEY_POSTAL_PREF, 
                        getValue(AuthenticationConsts.HEADER_POSTAL_CONTACT_PREF_PROPERTY_NAME));
		
		setPhone();
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
        
        String temp = getProperty(fieldName);
        String value = null;
        if (temp != null) {
            if (clHeaderList.indexOf(temp) != -1) {
                // Return field value from CL Header
            	value = AuthenticatorHelper.getValuesFromCLHeader(clHeaderHpp, temp);
            } else {
            	value = AuthenticatorHelper.getRequestHeader(request, temp, true);
            }
        }
        LOG.info("HPP getValue: " + fieldName + "=" + value);
        return value;
    }
    
    /**
     * This method is used to map the 4 http headers include phone info to
     * userProfile map. Construct phone number with country code, number and area code
     * from headers. Construct extension with the one from header.
     */
    @SuppressWarnings("unchecked")
    private void setPhone() {       
        String number = getValue(AuthenticationConsts.HEADER_PHONE_NUMBER_NAME);
        String country = getValue(AuthenticationConsts.HEADER_PHONE_COUNTRY_CODE);
        String area = getValue(AuthenticationConsts.HEADER_PHONE_AREA_CODE);
        String ext = getValue(AuthenticationConsts.HEADER_PHONE_EXT);

        StringBuffer phone = new StringBuffer("");
        if ((country != null) && !("".equals(country.trim()))) {
            phone.append("+").append(country.trim()).append(" ");
        }
        if ((area != null) && !("".equals(area.trim()))) {
            phone.append(area.trim()).append(" ");
        }
        if ((number != null) && !("".equals(number.trim()))) {
            phone.append(number.trim());
        }
        userProfile.put(AuthenticationConsts.KEY_PHONE_NUMBER, phone.toString());           

        if ((ext != null) && !("".equals(ext.trim()))) {
            userProfile.put(AuthenticationConsts.KEY_PHONE_NUMBER_EXT, ext.trim());
        } else {
            userProfile.put(AuthenticationConsts.KEY_PHONE_NUMBER_EXT, "");
        }
    }
}
