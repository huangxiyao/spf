/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.wsrp.extractor.profile;

/**
 * Interface which contains the constant used.
 * <p>
 *
 * @author mvidal@capgemini.fr
 *
 */
public interface Constants {

	//Please respect the alphabetical order if you add some values
	/**
	 * Key to pass in the url to access site when public access is no more available.
	 */
	public static final String ACCESS_CODE = "accessCode";

	/**
	 * the default language code which is en_US
	 */
	public static final String DEFAULT_LANGUAGE = "en_US";

	/**
	 * This attribute indicates wheather description for navigation item is defined or not.
	 */
	public static final String DESCRIPTION_NOT_DEFINED = "Description_not_defined";

	/**
	 * the error displayed
	 */
	public static final String DISPLAY_ERROR = "DISPLAYERROR";

	/**
	 * the custom error
	 */
	public static final String ERROR = "ERROR";
	
	/**
	 * error code returned by rule engine for spp inactivated user
	 */
	public static final int ERRORCODE_RULE_SPPINACTIVE = 3;
	
	/**
	 * error code returned by rule engine for ups inactivated user
	 */
	public static final int ERRORCODE_RULE_UPSINACTIVE = 3;
	
	/**
	 * error code returned by rule engine for ups pending user
	 */
	public static final int ERRORCODE_RULE_UPSPENDING = 2;
	
	/**
	 * the Get parameter which allow to not display error message during deep link
	 */
	public static final String FROMLOGIN = "FROMLOGIN";

	/**
	 * the hpp app id
	 */
	public static final String HPAPPID = "HPAPPID";

	/**
	 * Session attribute name that contains the email address of HP LDAP-authenticated user.
	 * It's optional. If not specified this means user is not authenticated.
	 */
	public static final String HP_LDAP_AUTH_EMAIL = "com.hp.spp.HPLdapAuthenticatedEmail";

	/**
	 * the landing page
	 */
	public final static String LANDING_PAGE = "LANDING_PAGE";

	/**
	 * the locale
	 */
	public final static String LOCALE = "com.hp.spp.Locale";
	
	/**
	 * value of locale parameters when user is redirected to public page.
	 */
	public final static String LOCALE_PARAM = "LOCAL_PARAM";
	
	/**
	 * value of SMSESSION cookie when user is logged off
	 */
	public final static String LOGGEDOFF = "LOGGEDOFF";

	/**
	 * the name of the key company name used in profile map
	 */
	public final static String MAP_COMPANY_NAME = "CompanyName";
	
	/**
	 * the name of the key company number used in profile map
	 */
	public final static String MAP_COMPANY_NUMBER = "CompanyNumber";
	
	/**
	 * the name of the key company number used in profile map
	 */
	public final static String MAP_CURRENT_SITE_URL = "CurrentSiteUrl";
	
	/**
	 * the name of the attribute country in the user profile map
	 */
	public final static String MAP_COUNTRY = "Country";

	/**
	 * the name of the attribute email in the user profile map
	 */
	public final static String MAP_EMAIL = "Email";

	/**
	 * the name of the attribute first name in the user profile map
	 */
	public final static String MAP_FIRSTNAME = "FirstName";

	/**
	 * the name of the url of the home page in the user profile map
	 */
	public final static String MAP_HOMEPAGE = "HomePageUrl";

	/**
	 * the name of the hpp id in the user profile map
	 */
	public final static String MAP_HPPID = "HPPUserId";

	/**
	 * the name of the key Role used in profile Map
	 */
	public final static String MAP_HP_ROLE = "HPRole";
	
	/**
	 * the name of the attribute indicating the persons is simulating
	 */
	public final static String MAP_IS_SIMULATING = "IsSimulating";

	/**
	 * the name of the language in the user profile map
	 */
	public final static String MAP_LANGUAGE = "Language";

	/**
	 * the name of the date of last login in the user profile map
	 */
	public final static String MAP_LASTLOGINDATE = "LastLoginDate";

	/**
	 * the name of the attribute last name in the user profile map
	 */
	public final static String MAP_LASTNAME = "LastName";

	/**
	 * the name of the Vignette session id in the user profile map
	 */
	public final static String MAP_PORTALSESSIONID = "PortalSessionId";

	/**
	 * the name of the map of the simulator
	 */
	public final static String MAP_SIMULATOR = "SimulatingUser";

	/**
	 * the name of the attribute updated date in the user profile map
	 */
	public final static String MAP_UPDATEDDATE = "UpdatedDate";

	/**
	 * the name of the site in the user profile map
	 */
	public final static String MAP_SITE = "SiteName";

	/**
	 * the name of the site in the user profile map for UGS
	 */
	public final static String MAP_SITE_ID = "SiteIdentifier";

	/**
	 * the name of the status in the user profile map
	 */
	public final static String MAP_STATUS = "Status";
	
	/**
	 * the name of the list of groups in the user profile map
	 */
	public final static String MAP_USERGROUPS = "UserGroups";

	/**
	 * the name of the user name (SM_USER) in the user profile map
	 */
	public final static String MAP_USERNAME = "LoginId";

	/**
	 * the no message
	 */
	public static final String NOMESSAGE = "NOMESSAGE";

	/**
	 * the label password to display in login form
	 */
	public static final String PASSWORD = "login_password";
	
	/**
	 * user profile variable stored in session to get language preference for landing page
	 */

	public static final String PREFERREDLANGUAGECODE = "PreferredLanguageCode";
	/**
	 * prefix to put to attribute passed to local portlet
	 */
	public static final String PREFIX_VGN_LOGINPORTLET = "com.vignette.portal.attribute.portlet.LoginPortlet.";

	/**
	 * the name of the map which store the user profile in session
	 */
	public static final String PROFILE_MAP = "com.hp.spp.UserProfile";

	/**
	 * the HPP cookie SMAUTHREASON
	 */
	public static final String SMAUTHREASON = "SMAUTHREASON";

	/**
	 * the HPP cookie SMTRYNO
	 */
	public static final String SMTRYNO = "SMTRYNO";

	/**
	 * the HPP cookie SMSESSION
	 */
	public static final String SMSESSION = "SMSESSION" ;

	/**
	 * the HPP header SM_AUTHTYPE
	 */
	public static final String SM_AUTHTYPE = "SM_AUTHTYPE";

	/**
	 * the HPP header SM_USER
	 */
	public static final String SM_USER = "sm_user";

	/**
	 * the HPP header SM_UNIVERSALID
	 */
	public final static String SM_UNIVERSALID = "sm_universalid" ;

	/**
	 * the DNS name of the SPP QA site 
	 */
	//Used in SPPheader.jsp
	public final static String SPP_QA_SITE = "sppqa";

	/**
	 * the beginning String of all SSO Guest User
	 */
	public static final String SSO_GUEST_USER = "SSO_GUEST_USER";

	/**
	 * This attribute indicates wheather search key words for navigation item is defined or not.
	 */
	public static final String SEARCHKEYWORDS_NOT_DEFINED = "SearchKeywords_not_defined";

	/**
	 * the value coming from UPS of the status pending for the user
	 */
	public final static int STATUS_UPS_PENDING = 2 ;

	/**
	 * the value coming from UPS of the status pending for the user
	 */
	public final static int STATUS_UPS_ACTIVE = 1 ;

	/**
	 * the value coming from UPS of the status inactive for the user
	 */
	public final static int STATUS_UPS_INACTIVE = 0 ;

	/**
	 * the value in Vignette of the status active for the user
	 */
	public final static int STATUS_VIGNETTE_ACTIVE = 1 ;

	/**
	 * the value in Vignette of the status inactive for the user
	 */
	public final static int STATUS_VIGNETTE_INACTIVE = 0 ;

	/**
	 * the label submit to display in login form
	 */
	public static final String SUBMIT = "login_submitLabel";

	/**
	 * the label subtitle to display in login form
	 */
	public static final String SUBTITLE = "login_subtitle";

	/**
	 * the name of the HPP target
	 */
	public static final String TARGET = "TARGET";

	/**
	 * This attribute indicates wheather title for navigation item is defined or not.
	 */
	public static final String TITLE_NOT_DEFINED = "Title_not_defined";

	/**
	 * the label user id to display in login form
	 */
	public static final String USER_ID = "login_user_id";

	/**
	 * the attribute user name
	 */
	public static final String USER_NAME = "user_name";

	/**
	 * the attribute country in Vignette User
	 */
	public static final String VGN_COUNTRY = "country";

	/**
	 * the attribute domaine in Vignette User
	 */
	public static final String VGN_DOMAIN = "domain";

	/**
	 * the attribute email in Vignette User
	 */
	public static final String VGN_EMAIL = "email";

	/**
	 * the attribute firstname in Vignette User
	 */
	public static final String VGN_FIRSTNAME = "firstname";

	/**
	 * the attribute hpp id in Vignette User
	 */
	public static final String VGN_HPPID = "hpp_id";

	/**
	 * the attribute language in Vignette User
	 */
	public static final String VGN_LANGUAGE = "language";

	/**
	 * the attribute last login date in Vignette User
	 */
	public static final String VGN_LASTLOGINDATE = "last_login_date";

	/**
	 * the attribute lastname in Vignette User
	 */
	public static final String VGN_LASTNAME = "lastname";

	/**
	 * the attribute updated date in Vignette User
	 */
	public static final String VGN_UPDATEDDATE = "updated_date";

	/**
	 * the attribute user name in Vignette User
	 */
	public static final String VGN_USERNAME = "username";

	/**
	 * the attribute state in Vignette User
	 */
	public static final String VGN_STATE = "user_state";

	/**
	 * Message displayed in the login portlet when user clicks on the submit button;
	 */
	public static final String WAIT_MESSAGE = "login_waitMessage";
	

	/**
	 * Flag for  forced refresh of session 
	 */
	public static final String SPP_REFRESH_SESSION = "_spp_refreshSession";
	
	
	/**
	 * Eror code for disclaimer 
	 */
	public static final int SPP_DISCLAIMER_ERROR_CODE = 10001;
	
	/**
	 * Eror code for disclaimer simulation error 
	 */
	public static final int SPP_DISCLAIMER_SIMULATION_ERROR_CODE = 10002;

	/**
	 * Consent flag
	 */
	public static final String CONSENT_FLAG = "ConsentFlag";

	/**
	 * Clear user profile flag
	 */
	public static final String CLEAR_USERPROFILE_FLAG = "_clear_userprofile";
	
}
