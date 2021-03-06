/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.sso.portal;

import com.hp.it.spf.xa.misc.portal.Consts;

public class AuthenticationConsts {

	static final String SSO_USERNAME = Consts.UNSTICKY_SESSION_ATTR_PREFIX + "SSO_USERNAME";
		
	static final String SSO_USER_LOCALE = Consts.UNSTICKY_SESSION_ATTR_PREFIX + "USER_LOCALE";	
	
	static final String DEFAULT_PRIMARY_SITE_NAME = Consts.LOGOUT_DEFAULT_SITE;
	
	static final String ANON_USER_NAME_PREFIX = Consts.ANON_USER_NAME_PREFIX;	
	
	static final String USER_PROFILE_KEY = Consts.USER_PROFILE_KEY;

	public static final String SHARED_PORTAL_SSO_FILE_BASE = "SharedPortalSSO";

	static final String COOKIE_ATTR_SMSESSION = "SMSESSION";

	static final String COOKIE_ATTR_HPPSESSION = "HPPSESSION";

	static final String SP_FROM_HPP = "HPP";

	static final String SP_FROM_ATHP = "ATHP";

	static final String SP_FROM_FEDERATION = "FEDERATION";

	/**
	 * The access type code - this is one of the possible values
	 * from the http request header by {@link #HEADER_ACCESS_TYPE}.
	 */
	static final String ACCESS_TYPE_INTERNET = "Internet";

	/**
	 * The access type code - this is one of the possible values
	 * from the http request header by {@link #HEADER_ACCESS_TYPE}.
	 */
	static final String ACCESS_TYPE_INTRANET = "Intranet";

	/**
	 * The HPP authentication type code - this is one of the possible values
	 * keyed from the user profile map by {@link #KEY_AUTH_TYPE}.
	 */
	static final String AUTH_TYPE_HPP = Consts.AUTH_TYPE_HPP;

	/**
	 * The AtHP authentication type code - this is one of the possible values
	 * keyed from the user profile map by {@link #KEY_AUTH_TYPE}.
	 */
	static final String AUTH_TYPE_ATHP = Consts.AUTH_TYPE_ATHP;

	/**
	 * The federated authentication type code - this is one of the possible
	 * values keyed from the user profile map by {@link #KEY_AUTH_TYPE}.
	 */
	public static final String AUTH_TYPE_FED = Consts.AUTH_TYPE_FED;

	static final String AUTH_SOURCE_TAGE = "AuthSource";

	static final String PHONE_EXT_SPLIT = "ext.";

	static final String ACCOUNT_HEADER = "ACCOUNT_HEADER";

	static final String ACCOUNT_HOMEHEADER = "ACCOUNT_HOMEHEADER";

	static final String ACCOUNT_BUSHEADER = "ACCOUNT_BUSHEADER";

	static final String CL_HEADER = "CL_HEADER";

	public static final String USER_IDENTIFIER_TYPE =  Consts.UNSTICKY_SESSION_ATTR_PREFIX + "USERIDENTIFIERTYPE";

	/**
	 * Following are property id that represents custom attributes added in
	 * entity_management.xml
	 * Some of them are already defined in com.hp.serviceportal.framework.portal.common.utils.Consts
	 */
	static final String PROPERTY_USER_NAME_ID = Consts.PROPERTY_USER_NAME_ID;

	static final String PROPERTY_DOMAIN_ID = Consts.PROPERTY_DOMAIN_ID;

	static final String PROPERTY_PROFILE_ID = Consts.PROPERTY_PROFILE_ID;

	static final String PROPERTY_LAST_CHANGE_DATE_ID = Consts.PROPERTY_LAST_CHANGE_DATE_ID;

	static final String PROPERTY_SPF_TIMEZONE_ID = Consts.PROPERTY_SPF_TIMEZONE_ID;

	static final String PROPERTY_TIMEZONE_ID = Consts.PROPERTY_TIMEZONE_ID;

	static final String PROPERTY_FIRSTNAME_ID = Consts.PROPERTY_FIRSTNAME_ID;

	static final String PROPERTY_LASTNAME_ID = Consts.PROPERTY_LASTNAME_ID;

	static final String PROPERTY_LANGUAGE_ID = Consts.PROPERTY_LANGUAGE_ID;

	static final String PROPERTY_COUNTRY_ID = Consts.PROPERTY_COUNTRY_ID;

	static final String PROPERTY_EMAIL_ID = Consts.PROPERTY_EMAIL_ID;

	static final String PROPERTY_LAST_LOGIN_DATE_ID = "lastlogindate";

	static final String PROPERTY_PRIMARY_SITE_ID = Consts.PROPERTY_PRIMARY_SITE_ID;

	/**
	 * Property names for those attributes in http headers
	 */
	static final String HEADER_USER_NAME_PROPERTY_NAME = "USER_NAME";

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

	public static final String HEADER_GROUP_NAME = "GROUPS";

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
	 * The http request header name to judge where the user comes from.
	 */
	static final String HEADER_ACCESS_TYPE = "AccessType";

	/**
	 * keys for those attributes in userProfile map in http session
	 */
	static final String KEY_PROFILE_ID = Consts.KEY_PROFILE_ID;

	public static final String KEY_USER_NAME = Consts.KEY_USER_NAME;

	public static final String KEY_FIRST_NAME = Consts.KEY_FIRST_NAME;

	public static final String KEY_LAST_NAME = Consts.KEY_LAST_NAME;

	public static final String KEY_EMAIL = Consts.KEY_EMAIL;

	public static final String KEY_LANGUAGE = Consts.KEY_LANGUAGE;

	public static final String KEY_COUNTRY = Consts.KEY_COUNTRY;

	static final String KEY_LAST_LOGIN_DATE = Consts.KEY_LAST_LOGIN_DATE;

	static final String KEY_TIMEZONE = Consts.KEY_TIMEZONE;

	static final String KEY_LAST_CHANGE_DATE = Consts.KEY_LAST_CHANGE_DATE;

	public static final String KEY_EMAIL_PREF = Consts.KEY_EMAIL_PREF;

	public static final String KEY_PHONE_PREF = Consts.KEY_PHONE_PREF;

	public static final String KEY_POSTAL_PREF = Consts.KEY_POSTAL_PREF;

	public static final String KEY_SECURITY_LEVEL = Consts.KEY_SECURITY_LEVEL;

	public static final String KEY_PHONE_NUMBER = Consts.KEY_PHONE_NUMBER;

	public static final String KEY_PHONE_NUMBER_EXT = Consts.KEY_PHONE_NUMBER_EXT;

	public static final String KEY_MAIL_STOP = Consts.KEY_MAIL_STOP;

	public static final String KEY_STREET = Consts.KEY_STREET;

	public static final String KEY_CITY = Consts.KEY_CITY;

	public static final String KEY_ZIP = Consts.KEY_ZIP;

	public static final String KEY_STATE = Consts.KEY_STATE;

	public static final String KEY_USER_GROUPS = Consts.KEY_USER_GROUPS;

	/**
	 * The key to get the authentication type from user profile map.
	 */
	public static final String KEY_AUTH_TYPE = Consts.KEY_AUTH_TYPE;

	/**
	 * SharedPortal session attributes prefix
	 */
	static final String RETAINED_PARAMETER_PREFIX = Consts.STICKY_SESSION_ATTR_PREFIX;

	static final String PARAMETER_PREFIX = Consts.UNSTICKY_SESSION_ATTR_PREFIX;

	static final String ANON_IND_PROPERTY_NAME = "ANON_IND";

	static final String HPP_LOGGEDOFF_PROPERTY_NAME = "HPP_LOGGEDOFF";

	static final String SPTRYNO_COOKIE = "sptryno";

	/**
	 * Config information for cookie
	 */
	static final String SESSION_ATTR_SSO_ERROR = Consts.UNSTICKY_SESSION_ATTR_PREFIX + "SSO_ERROR";

	/**
	 * SharedPortal Group attributes
	 */

	static final String GROUP_TITLE = "title";


	/**
	 * Configuration file's properties
	 */
	static final String PROPERTY_ATHP_FLAG = "flagForATHP";

	static final String PROPERTY_ATHPI_FLAG = "flagForATHPI";

	static final String PROPERTY_ATHPE_FLAG = "flagForATHPE";
	static final String YES = "YES";

	static final String SANDBOX_MODE = "SandboxMode";

	static final String ENABLE_HPI_HPE = "Enable_HPI_HPE";
	static final String HPPGROUPS_FROM_WEBSERVICE = "hppgroups_from_webservice";

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
	public static final String ATHP_GROUP_PREFIX = "cn=";

	/**
	 * Groups stands for the user is from HPP/FED/@HP.
	 */
	public final static String LOCAL_ATHP_NAME = Consts.GROUP_AUTH_TYPE_ATHP;
	public final static String LOCAL_FED_NAME = Consts.GROUP_AUTH_TYPE_FED;
	public final static String LOCAL_HPP_NAME = Consts.GROUP_AUTH_TYPE_HPP;

	/**
	 * Groups that indicate the user is authenticated or not.
	 */
	public final static String LOCAL_PORTAL_AUTHENTICATED_USERS = Consts.GROUP_AUTH_STATUS_AUTHENTICATED;
	public final static String LOCAL_PORTAL_ANONYMOUS_USERS = Consts.GROUP_AUTH_STATUS_ANONYMOUS;

	/**
	 * Groups stands for the user locale.
	 */
	public final static String LOCAL_PORTAL_LANG_PREFIX = Consts.GROUP_LOCALE_LANGUAGE_PREFIX;
	public final static String LOCAL_PORTAL_COUNTRY_PREFIX = Consts.GROUP_LOCALE_COUNTRY_PREFIX;

	/**
	 * Keys of user profile and group retriever defined in properties
	 */
	public final static String USER_PROFILE_RETRIEVER = "user_profile_retriever";
	public final static String HPP_USER_GROUP_RETRIEVER = "hpp_user_group_retriever";
	public final static String ATHP_USER_GROUP_RETRIEVER = "athp_user_group_retriever";

	/**
	 * Key of portal session attribute storing the latest date/time when AuthenticatorHelper.cleanupSession() is called
	 */
	public static final String LAST_PORTAL_SESSION_CLEAN_UP_DATE = Consts.KEY_LAST_PORTAL_SESSION_CLEANUP_DATE;
}

