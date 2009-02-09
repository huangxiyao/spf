/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.misc;

/**
 * <p>
 * Container class for common portal/portlet constants which need
 * cross-portal/portlet exposure.
 * </p>
 * 
 * @version TBD
 * @see com.hp.it.spf.xa.misc.portal.Consts com.hp.it.spf.xa.misc.portlet.Consts
 */
public class Consts {

	/**
	 * The key to identify which request needs injection and extract action.
	 */
	public static final String INJECTION_KEY_PREFIX = "__SPF";
	
	/**
	 * The key to identify which request needs injection and extract action.
	 */
	public static final String INJECTION_THREAD_NAME = "RequestBinding.ThreadName";

	/**
	 * The user profile map contains all user attributes from HPP/ATHP, Persona
	 * and etc. On portal side, it can be retrieved from the http session; on
	 * portlet side, it can be retrieved with the method
	 * <code>(Map)PortletRequest.getAttribute(PortletRequest.USER_INFO)</code>.
	 */
	public static final String USER_PROFILE_KEY = "userProfile";

	/**
	 * The key to get profile id from user profile map.
	 */
	public static final String KEY_PROFILE_ID = "ProfileId";

	/**
	 * The key to get user name from user profile map.
	 */
	public static final String KEY_USER_NAME = "LoginId";

	/**
	 * The key to get first name from user profile map.
	 */
	public static final String KEY_FIRST_NAME = "FirstName";

	/**
	 * The key to get last name from user profile map.
	 */
	public static final String KEY_LAST_NAME = "LastName";

	/**
	 * The key to get email from user profile map.
	 */
	public static final String KEY_EMAIL = "Email";

	/**
	 * The key to get language from user profile map.
	 */
	public static final String KEY_LANGUAGE = "Language";

	/**
	 * The key to get country from user profile map.
	 */
	public static final String KEY_COUNTRY = "Country";

	/**
	 * The key to get last login date from user profile map.
	 */
	public static final String KEY_LAST_LOGIN_DATE = "LastLoginDate";

	/**
	 * The key to get timezone (java object) from user profile map.
	 */
	public static final String KEY_TIMEZONE = "Timezone";

	/**
	 * The key to get last change date from user profile map.
	 */
	public static final String KEY_LAST_CHANGE_DATE = "LastChangeDate";

	/**
	 * The key to get email contact preference (from HPP) from user profile map.
	 */
	public static final String KEY_EMAIL_PREF = "EmailContactPref";

	/**
	 * The key to get phone contact preference (from HPP) from user profile map.
	 */
	public static final String KEY_PHONE_PREF = "PhoneContactPref";

	/**
	 * The key to get postal contact preference (from HPP) from user profile
	 * map.
	 */
	public static final String KEY_POSTAL_PREF = "PostalContactPref";

	/**
	 * The key to get security level from user profile map.
	 */
	public static final String KEY_SECURITY_LEVEL = "SecurityLevel";

	/**
	 * The key to get phone number from user profile map.
	 */
	public static final String KEY_PHONE_NUMBER = "PhoneNumber";

	/**
	 * The key to get phone number extension from user profile map.
	 */
	public static final String KEY_PHONE_NUMBER_EXT = "PhoneNumberExt";

	/**
	 * The key to get mail stop extension from user profile map.
	 */
	public static final String KEY_MAIL_STOP = "MailStop";

	/**
	 * The key to get street extension from user profile map.
	 */
	public static final String KEY_STREET = "Street";

	/**
	 * The key to get city from user profile map.
	 */
	public static final String KEY_CITY = "City";

	/**
	 * The key to get zip code from user profile map.
	 */
	public static final String KEY_ZIP = "Zip";

	/**
	 * The key to get state from user profile map.
	 */
	public static final String KEY_STATE = "State";

	/**
	 * The key to get user groups from user profile map.
	 */
	public static final String KEY_USER_GROUPS = "Groups";

	/**
	 * The portal context map contains some useful portal information which they
	 * portlet needs. On portal side, this map will be injected into SOAP
	 * message by the spf-portal-wsrp project; on portlet side, it can be
	 * retrieved with the method
	 * <code>(Map)PortletRequest.getAttribute(Consts.PORTAL_CONTEXT_KEY)</code>.
	 */
	public static final String PORTAL_CONTEXT_KEY = "ContextMap";

	/**
	 * The key to get the portal session id from portal context map.
	 */
	public static final String KEY_PORTAL_SESSION_ID = "PortalSessionId";

	/**
	 * The key to get the site name (ie what Vignette calls the "site DNS name")
	 * from portal context map.
	 */
	public static final String KEY_PORTAL_SITE_NAME = "PortalSite";

	/**
	 * The key to get the site URL (ie the portal home page URL for the current
	 * site) from portal context map.
	 */
	public static final String KEY_PORTAL_SITE_URL = "PortalSiteURL";

	/**
	 * The key to get the current portal request URL from portal context map.
	 */
	public static final String KEY_PORTAL_REQUEST_URL = "PortalRequestURL";

	/**
	 * The key to get the Session Token from portal context map.
	 */
	public static final String KEY_SESSION_TOKEN = "SessionToken";

	/**
	 * The key to get the Navigation Item Name from portal context map.
	 */
	public static final String KEY_NAVIGATION_ITEM_NAME = "NavigationItemName";

	/**
	 * The prefix of the anonymous user name.
	 */
	public static final String ANON_USER_NAME_PREFIX = "sso_guest_user_";

	/**
	 * The key for the portlet friendly id in the portlet request. This is
	 * non-standard. The SPF portal puts the friendly ID into this request
	 * attribute so the portlet can then get it.
	 */
	// TODO - this is the key name for accessing portlet friendly ID in the
	// portlet request. Check and change this as needed. Also see TODO in the
	// portlet Utils.getPortletID method.
	public static final String KEY_PORTAL_PORTLET_ID = "com.vignette.portal.portlet.friendlyid";

	/**
	 * The template friendly ID for the SPF-standard system error secondary
	 * page. This is common because both portal and portlets may want to create
	 * URLs pointing to this page.
	 */
	public static final String PAGE_FRIENDLY_ID_SYSTEM_ERROR = "ERROR";

	/**
	 * The template friendly URI for the SPF-standard system error secondary
	 * page. This is common because both portal and portlets may want to create
	 * URLs pointing to this page.
	 */
	public static final String PAGE_FRIENDLY_URI_SYSTEM_ERROR = "template."
			+ PAGE_FRIENDLY_ID_SYSTEM_ERROR + "/";

	/**
	 * The template friendly ID for the SPF-standard authorization error
	 * secondary page. This is common because both portal and portlets may want
	 * to create URLs pointing to this page.
	 */
	public static final String PAGE_FRIENDLY_ID_AUTH_ERROR = "ANON_SPF_AUTH_ERROR";

	/**
	 * The template friendly URI for the SPF-standard authorization error
	 * secondary page. This is common because both portal and portlets may want
	 * to create URLs pointing to this page.
	 */
	public static final String PAGE_FRIENDLY_URI_AUTH_ERROR = "template."
			+ PAGE_FRIENDLY_ID_AUTH_ERROR + "/";

	/**
	 * The template friendly ID for the SPF-standard portal pulse secondary
	 * page. This is common because both portal and portlets may want to create
	 * URLs pointing to this page.
	 */
	public static final String PAGE_FRIENDLY_ID_PORTAL_PULSE = "ANON_SPF_PORTAL_PULSE";

	/**
	 * The template friendly URI for the SPF-standard portal pulse secondary
	 * page. This is common because both portal and portlets may want to create
	 * URLs pointing to this page.
	 */
	public static final String PAGE_FRIENDLY_URI_PORTAL_PULSE = "template."
			+ PAGE_FRIENDLY_ID_PORTAL_PULSE + "/";

	/**
	 * The template friendly ID for the SPF-standard global help secondary page.
	 * This is common because both portal and portlets may want to create URLs
	 * pointing to this page.
	 */
	public static final String PAGE_FRIENDLY_ID_GLOBAL_HELP = "ANON_SPF_GLOBAL_HELP";

	/**
	 * The template friendly URI for the SPF-standard global help secondary
	 * page. This is common because both portal and portlets may want to create
	 * URLs pointing to this page.
	 */
	public static final String PAGE_FRIENDLY_URI_GLOBAL_HELP = "template."
			+ PAGE_FRIENDLY_ID_GLOBAL_HELP + "/";

	/**
	 * The template friendly ID for the SPF-standard federated logout secondary
	 * page. This is common because both portal and portlets may want to create
	 * URLs pointing to this page.
	 */
	public static final String PAGE_FRIENDLY_ID_FED_LOGOUT = "ANON_SPF_FED_LOGOUT";

	/**
	 * The template friendly URI for the SPF-standard federated logout secondary
	 * page. This is common because both portal and portlets may want to create
	 * URLs pointing to this page.
	 */
	public static final String PAGE_FRIENDLY_URI_FED_LOGOUT = "template."
			+ PAGE_FRIENDLY_ID_FED_LOGOUT + "/";

	/**
	 * The template friendly ID for the SPF-standard federation error secondary
	 * page. This is common because both portal and portlets may want to create
	 * URLs pointing to this page.
	 */
	public static final String PAGE_FRIENDLY_ID_FED_ERROR = "ANON_SPF_FED_ERROR";

	/**
	 * The template friendly URI for the SPF-standard federation error secondary
	 * page. This is common because both portal and portlets may want to create
	 * URLs pointing to this page.
	 */
	public static final String PAGE_FRIENDLY_URI_FED_ERROR = "template."
			+ PAGE_FRIENDLY_ID_FED_ERROR + "/";

	/**
	 * The template friendly ID for the SPF-standard locale selector secondary
	 * page. This is common because both portal and portlets may want to create
	 * URLs pointing to this page.
	 */
	public static final String PAGE_FRIENDLY_ID_SELECT_LOCALE = "ANON_SPF_SELECT_LOCALE";

	/**
	 * The template friendly URI for the SPF-standard locale selector secondary
	 * page. This is a process-action secondary page, so this is a process
	 * action template friendly URI. This is common because both portal and
	 * portlets may want to create URLs pointing to this page.
	 */
	public static final String PAGE_FRIENDLY_URI_SELECT_LOCALE = "template."
			+ PAGE_FRIENDLY_ID_SELECT_LOCALE + "/action.process/";

	/**
	 * The template friendly ID for the SPF-standard logout secondary page. This
	 * is common because both portal and portlets may want to create URLs
	 * pointing to this page.
	 */
	public static final String PAGE_FRIENDLY_ID_LOGOUT = "SPF_LOGOUT";

	/**
	 * The template friendly URI for the SPF-standard logout secondary page.
	 * This is common because both portal and portlets may want to create URLs
	 * pointing to this page.
	 */
	public static final String PAGE_FRIENDLY_URI_LOGOUT = "template."
			+ PAGE_FRIENDLY_ID_LOGOUT + "/";

	/**
	 * Use this path when setting an HP.com-wide cookie. Both portal and
	 * portlets may want to set cookies, so this is common.
	 */
	public static final String HP_COOKIE_PATH = "/";

	/**
	 * Use this domain when setting an HP.com-wide cookie. Both portal and
	 * portlets may want to set cookies, so this is common.
	 */
	public static final String HP_COOKIE_DOMAIN = ".hp.com";

	/**
	 * The name of the HP.com standard query parameter for language code. Both
	 * portal and portlets may want to set or inspect this parameter, so this is
	 * common.
	 */
	public static final String PARAM_HPCOM_LANGUAGE = "lang";

	/**
	 * The name of the HP.com standard query parameter for country code. Both
	 * portal and portlets may want to set or inspect this parameter, so this is
	 * common.
	 */
	public static final String PARAM_HPCOM_COUNTRY = "cc";

	/**
	 * The name of the HP.com standard cookie for language code. Both portal and
	 * portlets may want to set or inspect this cookie, so this is common.
	 */
	public static final String COOKIE_NAME_HPCOM_LANGUAGE = "lang";

	/**
	 * The name of the HP.com standard cookie for country code. Both portal and
	 * portlets may want to set or inspect this cookie, so this is common.
	 */
	public static final String COOKIE_NAME_HPCOM_COUNTRY = "cc";

	/**
	 * SPF has several activities that may need to occur under a "core" Vignette
	 * site (eg logout for the time being since SiteMinder can accept only one
	 * logout URL per physical Web site) - this variables holds the name of that
	 * site. This is the "site DNS name" in Vignette for that core site.
	 */
	public final static String SPF_CORE_SITE = "spf";

	/**
	 * SPF sometimes needs to perform activities under the {@link #SPF_CORE_SITE} Vignette site,
	 * but which are meant to be effective for an actual portal site.  In that case, the
	 * site name (ie Vignette "site DNS name") for the actual portal site is carried in a query parameter
	 * with this name.
	 */
	public final static String PARAM_EFFECTIVE_SITE = "spfSite";
	
	/**
	 * The default site name to assume during logout for the redirect target.
	 * This is also the site name under which the logout process runs. It is
	 * located in the common utilities so that both portals and portlets have
	 * access to it (eg so they can make logout URLs if needed). This is the
	 * "site DNS name" in Vignette for the logout site.
	 */
	public final static String LOGOUT_DEFAULT_SITE = SPF_CORE_SITE;

	/**
	 * The name of the query parameter containing the site name to use for the
	 * logout redirect target. Both portal and portlets may want to point to the
	 * logout process and pass a redirect target, so this is common.
	 */
	public final static String PARAM_LOGOUT_SITE = PARAM_EFFECTIVE_SITE;

	/**
	 * The name of the locale selector form parameter (ie this is the form
	 * parameter containing the chosen locale submitted by the user). Both
	 * portal and portlets may want to point to the locale selector process and
	 * pass a redirect target, so this is common.
	 */
	public final static String PARAM_SELECT_LOCALE = "spfSelectedLocale";

	/**
	 * The name of the locale selector redirect target form parameter (ie this
	 * is the form parameter containing the redirect target submitted by the
	 * user - typically hidden). Both portal and portlets may want to point to
	 * the locale selector process and pass a redirect target, so this is
	 * common.
	 */
	public final static String PARAM_SELECT_LOCALE_TARGET = "spfLocaleSelectorTarget";

}
