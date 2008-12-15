/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.misc.portal;

/**
 * <p>
 * Container class for common portal constants which need exposure into multiple
 * portal classes.
 * </p>
 * 
 * @author <link href="ye.liu@hp.com">Liu Ye</link>
 * @version TBD
 */
public final class Consts extends com.hp.it.spf.xa.misc.Consts {

	/**
	 * TODO - remove this attribute after removing the HPP fault tag from the
	 * tags package (keeping it for now to avoid compile problems)
	 */
	public static final String SESSION_ATTR_HPP_USER_ERROR = "SPF_HPP_USER_ERROR";

	/**
	 * The name of the session attribute containing transient status message.
	 * Used by logout secondary page type action to set a logout message; can be
	 * retrieved and removed by a JSP to display.
	 */
	public static final String SESSION_ATTR_STATUS_MSG = "SPF_STATUS_MSG";

	/**
	 * The name of the SiteMinder session cookie (for both HPP and AtHP)
	 */
	public static final String COOKIE_NAME_SMSESSION = "SMSESSION";

	/**
	 * The name of the SPF-wide locale tracking cookie.
	 */
	public static final String COOKIE_NAME_LOCALE = "HP_SPF_LOCALE";

	/**
	 * The name of the request attribute containing global help content. Set by
	 * the secondary page type action for use by the secondary page JSP.
	 */
	public static final String REQUEST_ATTR_GLOBAL_HELP_DATA = "SPF_GLOBAL_HELP_DATA";

	/**
	 * Various secondary page friendly IDs.
	 */
	public static final String PAGE_SYSTEM_ERROR = "ANON_SPF_SYSTEM_ERROR";
	public static final String PAGE_AUTH_ERROR = "ANON_SPF_AUTH_ERROR";
	public static final String PAGE_HEALTH_CHECK = "ANON_SPF_HEALTH_CHECK";
	public static final String PAGE_GLOBAL_HELP = "ANON_SPF_GLOBAL_HELP";
	public static final String PAGE_FED_LOGOUT = "ANON_SPF_FED_LOGOUT";
	public static final String PAGE_FED_ERROR = "ANON_SPF_FED_ERROR";
	public static final String PAGE_SELECT_LOCALE = "ANON_SPF_SELECT_LOCALE";
	public static final String PAGE_LOGOUT = "SP_LOGOUT";

	/**
	 * The global help secondary page component ID (used by global help tag and
	 * other components referring to global help).
	 */
	public static final String COMPONENT_GLOBAL_HELP = "SpfGlobalHelpPage";

	/**
	 * Default title for non-localized system error page.
	 */
	public static final String ERROR_PAGE_TITLE = "Could not open page";

	/**
	 * Following are property IDs that represent custom attributes added in
	 * entity_management.xml
	 */
	public static final String PROPERTY_USER_NAME_ID = "username";
	public static final String PROPERTY_DOMAIN_ID = "domain";
	public static final String PROPERTY_PROFILE_ID = "profileid";
	public static final String PROPERTY_CUSTOMER_ID = "customerid";
	public static final String PROPERTY_EMAIL_PREF_ID = "emailcontactpref";
	public static final String PROPERTY_PHONE_PREF_ID = "phonecontactpref";
	public static final String PROPERTY_POSTAL_PREF_ID = "postalcontactpref";
	public static final String PROPERTY_SECURITYLEVEL_ID = "securitylevel";
	public static final String PROPERTY_LAST_CHANGE_DATE_ID = "lastchangedate";
	public static final String PROPERTY_SP_TIMEZONE_ID = "sp_timezone";
	public static final String PROPERTY_TIMEZONE_ID = "timezone";
	public static final String PROPERTY_FIRSTNAME_ID = "firstname";
	public static final String PROPERTY_LASTNAME_ID = "lastname";
	public static final String PROPERTY_LANGUAGE_ID = "language";
	public static final String PROPERTY_COUNTRY_ID = "country";
	public static final String PROPERTY_EMAIL_ID = "email";
	public static final String PROPERTY_PRIMARY_SITE_ID = "primary_site_id";
	public static final String PROPERTY_LOGON_ID = "logon";
	public static final String PROPERTY_PHONE_ID = "day_phone";
	public static final String PROPERTY_PHONE_EXT_ID = "day_phone_ext";
	public static final String PROPERTY_RECENT_ACCESS_HOST = "recent_access_host";

	/**
	 * Config information for setting cookies HP-wide.
	 */
	public static final String HP_COOKIE_PATH = "/";
	public static final String HP_COOKIE_DOMAIN = ".hp.com";

	/**
	 * HP.com standard names for country and language query parameters and
	 * cookies.
	 */
	public static final String HPCOM_LANG_PARAM = "lang";
	public static final String HPCOM_COUNTRY_PARAM = "cc";

	/**
	 * Prefix your session attribute names with this string, and they will be
	 * <b>kept</b> after logout from the portal.
	 */
	public final static String STICKY_SESSION_ATTR_PREFIX = "SPF_RETAIN";

	/**
	 * Prefix your session attribute names with this string, and they will be
	 * <b>removed</b> for you upon logout from the portal.
	 */
	public final static String UNSTICKY_SESSION_ATTR_PREFIX = "SPF_";

	/**
	 * Constants used for logout (shared between utilities and logout secondary
	 * page type).
	 */
	public final static String LOGOUT_DEFAULT_SITE = "spf";
	public final static String LOGOUT_SITE_PARAM = "site";

}