/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import com.hp.it.spf.xa.misc.portal.Consts;

class AuthenticationConsts {

	static final String SSO_USERNAME = "SSO_username";

    static final String HEADER_CONSTS_FILE_BASE = "SharedPortalSSO";

    static final String COOKIE_ATTR_SMSESSION = "SMSESSION";
    
    static final String SESSSION_ATTR_SP_TARGET = "SP_HPP_TARGET";
    
    static final String SP_FROM_HPP = "HPP";

    static final String SP_FROM_ATHP = "ATHP";
    
    static final String SP_FROM_FEDERATION = "FEDERATION";
    
    static final String AUTH_SOURCE_TAGE = "AuthSource";
    
    static final String PHONE_EXT_SPLIT = "ext.";

    /**
     * Following are property id that represents custom attributes added in
     * entity_management.xml
     * Some of them are already defined in com.hp.serviceportal.framework.portal.common.utils.Consts
     */
    static final String PROPERTY_USER_NAME_ID = Consts.PROPERTY_USER_NAME_ID;

    static final String PROPERTY_DOMAIN_ID = Consts.PROPERTY_DOMAIN_ID;

    static final String PROPERTY_PROFILE_ID = Consts.PROPERTY_PROFILE_ID;

    static final String PROPERTY_CUSTOMER_ID = Consts.PROPERTY_CUSTOMER_ID;

    static final String PROPERTY_EMAIL_PREF_ID = Consts.PROPERTY_EMAIL_PREF_ID;

    static final String PROPERTY_PHONE_PREF_ID = Consts.PROPERTY_PHONE_PREF_ID;

    static final String PROPERTY_POSTAL_PREF_ID = Consts.PROPERTY_POSTAL_PREF_ID;

    static final String PROPERTY_SECURITYLEVEL_ID = Consts.PROPERTY_SECURITYLEVEL_ID;

    static final String PROPERTY_LAST_CHANGE_DATE_ID = Consts.PROPERTY_LAST_CHANGE_DATE_ID;

    static final String PROPERTY_SP_TIMEZONE_ID = Consts.PROPERTY_SP_TIMEZONE_ID;

    static final String PROPERTY_TIMEZONE_ID = Consts.PROPERTY_TIMEZONE_ID;

    static final String PROPERTY_FIRSTNAME_ID = Consts.PROPERTY_FIRSTNAME_ID;

    static final String PROPERTY_LASTNAME_ID = Consts.PROPERTY_LASTNAME_ID;

    static final String PROPERTY_LANGUAGE_ID = Consts.PROPERTY_LANGUAGE_ID;

    static final String PROPERTY_COUNTRY_ID = Consts.PROPERTY_COUNTRY_ID;

    static final String PROPERTY_EMAIL_ID = Consts.PROPERTY_EMAIL_ID;

    static final String PROPERTY_PHONE_ID = Consts.PROPERTY_PHONE_ID;
    
    static final String PROPERTY_PHONE_EXT_ID = Consts.PROPERTY_PHONE_EXT_ID;

    static final String PROPERTY_ZIP_ID = "zip";

    static final String PROPERTY_STREET_ID = "address2";

    static final String PROPERTY_MAIL_STOP_ID = "address1";

    static final String PROPERTY_STATE_ID = "state";

    static final String PROPERTY_CITY_ID = "city";

    static final String PROPERTY_PRIMARY_SITE_ID = Consts.PROPERTY_PRIMARY_SITE_ID;
    
    static final String PROPERTY_RECENT_ACCESS_HOST = Consts.PROPERTY_RECENT_ACCESS_HOST;

    static final String DATASOURCE = "datasource";

    static final String RESOURCEBUDDLE = "resourcebuddle";

    /**
     * Property names for those attributes in http headers
     */
    static final  String HEADER_USER_NAME_PROPERTY_NAME = "USER_NAME";

    static final String HEADER_PROFILE_ID_PROPERTY_NAME = "PROFILE_ID";

    static final String HEADER_TIMEZONE_PROPERTY_NAME = "TIMEZONE";

    static final String HEADER_TIMEZONECODE_PROPERTY_NAME = "TIMEZONECODE";

    static final String HEADER_EMAIL_CONTACT_PREF_PROPERTY_NAME = "EMAIL_CONTACT_PREF";

    static final String HEADER_PHONE_CONTACT_PREF_PROPERTY_NAME = "PHONE_CONTACT_PREF";

    static final String HEADER_POSTAL_CONTACT_PREF_PROPERTY_NAME = "POSTAL_CONTACT_PREF";

    static final String HEADER_SECURITY_LEVEL_PROPERTY_NAME = "SECURITY_LEVEL";

    static final String HEADER_CL_HEADER_PROPERTY_NAME = "CL_HEADER";

    static final String HEADER_CL_HEADER_LIST_PROPERTY_NAME = "CL_HEADER_LIST";

    static final String HEADER_FIRST_NAME_PROPERTY_NAME = "FIRST_NAME";

    static final String HEADER_LAST_NAME_PROPERTY_NAME = "LAST_NAME";

    static final String HEADER_LANGUAGE_PROPERTY_NAME = "LANGUAGE";

    static final String HEADER_COUNTRY_PROPERTY_NAME = "COUNTRY";

    static final String HEADER_LAST_CHANGE_DATE_PROPERTY_NAME = "LAST_CHANGE_DATE";

    static final String HEADER_EMAIL_ADDRESS_PROPERTY_NAME = "EMAIL_ADDRESS";

    static final String HEADER_HPCLNAME_PROPERTY_NAME = "HPCLNAME";

    static final String HEADER_DATE_FORMAT_NAME = "DATE_FORMAT";

    static final String HEADER_GROUP_NAME = "GROUPS";

    static final String HEADER_CITY_NAME = "CITY";

    static final String HEADER_MAIL_STOP_NAME = "MAIL_STOP";

    static final String HEADER_STATE_NAME = "STATE";

    static final String HEADER_STREET_NAME = "STREET";

    static final String HEADER_ZIP_NAME = "ZIP";

    static final String HEADER_PHONE_NUMBER_NAME = "PHONE_NUMBER";
    
    static final String HEADER_PHONE_COUNTRY_CODE = "PHONE_COUNTRY_CODE";
    
    static final String HEADER_PHONE_AREA_CODE = "PHONE_AREA_CODE";
    
    static final String HEADER_PHONE_EXT = "PHONE_EXT";
    
    static final String HEADER_ATHP_FLAG = "SM_AUTHDIRNAME";

    /**
     * ServicePortal session attributes prefix
     */
    static final String RETAINED_PARAMETER_PREFIX = "SP_RETAIN_";

    static final String PARAMETER_PREFIX = "SP_";

    static final String ANON_IND_PROPERTY_NAME = "ANON_IND";

    static final String HPP_LOGGEDOFF_PROPERTY_NAME = "HPP_LOGGEDOFF";
    
    static final String SPTRYNO_COOKIE = "sptryno";

    /**
     * Config information for cookie
     */
    static final String COOKIE_REMEMBER_USER_FLAG = "HP_SP_SET_REMEMBER_USER_FLAG";

    static final String COOKIE_USER_ID = "HP_SP_USERID";

    static final String COOKIE_PATH = "/";

    static final String COOKIE_DOMAIN = ".hp.com";

    static final String SESSION_ATTR_SSO_ERROR = "SP_SSO_ERROR";
    
    /**
     * ServicePortal Group attributes
     */
    
    static final String GROUP_TITLE = "title";
    

    /**
     * Configuration file's properties
     */
    static final String PROPERTY_ATHP_FLAG = "flagForATHP";
    
    static final String YES = "YES";
    
    static final String SANDBOX_MODE = "SandboxMode";
    
    /**
     * Configuration for default values
     */
    static final String DEFAULT_TIMEZONE = "America/Los_Angeles";

    static final Integer DEFAULT_TIMEZONE_OFFSET = new Integer(-8);
    
    static final String DEFAULT_LANGUAGE = "en";
    
    static final Float DEFAULT_SECURITY_LEVEL = new Float(0);
    
    /**
     * Configuration for AtHP group prefix
     */
    static final String ATHP_GROUP_PREFIX = "cn=sp_";
    
    /**
     * Groups when user does not login.
     */
    public final static String SP_FN_ATHP_NAME = "SP_FN_ATHP";
    public final static String SP_FN_FED_NAME = "SP_FN_FED";
    public final static String SP_FN_HPP_NAME = "SP_FN_HPP";
}
