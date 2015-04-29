/*
 * Project: Shared Portal Framework Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.misc;

/**
 * <p>
 * Container class for common portal/portlet constants which need
 * cross-portal/portlet exposure. Portlets and portal components should import
 * the respective subclasses, not this one.
 * </p>
 * 
 * @version TBD
 * @see <code>com.hp.it.spf.xa.misc.portal.Consts</code><br>
 *      <code>com.hp.it.spf.xa.misc.portlet.Consts</code>
 */
public class Consts {

    /**
     * The user profile map contains all user attributes from HPP/ATHP, Persona
     * and etc. On portal side, it can be retrieved from the http session; on
     * portlet side, it can be retrieved with the method
     * <code>(Map)PortletRequest.getAttribute(PortletRequest.USER_INFO)</code>.
     */
    public static final String USER_PROFILE_KEY = "SPF_USER_PROFILE";

    private static final String GROUP_PREFIX = "LOCAL_PORTAL_";

    /**
     * The SPF-assigned user group indicating the user is accessing via standard
     * HP Passport.
     */
    public static final String GROUP_AUTH_TYPE_HPP = GROUP_PREFIX + "AUTH_HPP";

    /**
     * The SPF-assigned user group indicating the user is accessing via
     * federated HP Passport.
     */
    public static final String GROUP_AUTH_TYPE_FED = GROUP_PREFIX + "AUTH_FED";

    /**
     * The SPF-assigned user group indicating the user is accessing via HP
     * Enterprise Directory and the AtHP employee portal.
     */
    public static final String GROUP_AUTH_TYPE_ATHP = GROUP_PREFIX
	    + "AUTH_ATHP";

    /**
     * The SPF-assigned user group indicating the user is currently logged-in
     * (ie, authenticated).
     */
    public static final String GROUP_AUTH_STATUS_AUTHENTICATED = GROUP_PREFIX
	    + "AUTHENTICATED_USERS";

    /**
     * The SPF-assigned user group indicating the user is currently logged-out
     * (ie, anonymous or guest).
     */
    public static final String GROUP_AUTH_STATUS_ANONYMOUS = GROUP_PREFIX
	    + "ANONYMOUS_USERS";

	/**
	 * The SPF-assigned user group indicating the user is from HP company.
	 */
	public static final String GROUP_COMPANY_HP = GROUP_PREFIX
		+ "COMPANY_HP";

	/**
	 * The SPF-assigned user group indicating the user is from HPI company.
	 */
	public static final String GROUP_COMPANY_HPI = GROUP_PREFIX
		+ "COMPANY_HPI";

	/**
	 * The SPF-assigned user group indicating the user is from HPE company.
	 */
	public static final String GROUP_COMPANY_HPE = GROUP_PREFIX
		+ "COMPANY_HPE";

    /**
     * The prefix for the SPF-assigned user group for country - if the user is
     * in this group, it indicates his or her resolved locale includes that
     * country. Append this string with the <a href="http://www.iso.org/iso/country_codes/iso_3166_code_lists/english_country_names_and_code_elements.htm"
     * >ISO 3166-1</a> country code to get a complete group name - for example,
     * <code>GROUP_LOCALE_COUNTRY_PREFIX + "CN"</code> for China.
     */
    public static final String GROUP_LOCALE_COUNTRY_PREFIX = GROUP_PREFIX
	    + "COUNTRY_";

    /**
     * The prefix for the SPF-assigned user group for language - if the user is
     * in this group, it means his or her resolved locale includes that
     * language. Append this string with the <a
     * href="http://www.loc.gov/standards/iso639-2/php/English_list.php">ISO
     * 639-1</a> language code to make a complete group name - for example,
     * <code>GROUP_LOCALE_LANGUAGE_PREFIX + "ZH" for Chinese.
     */
    public static final String GROUP_LOCALE_LANGUAGE_PREFIX = GROUP_PREFIX
	    + "LANG_";

    /**
     * The HPP authentication type code - this is one of the possible values
     * keyed from the user profile map by {@link #KEY_AUTH_TYPE}.
     */
    public static final String AUTH_TYPE_HPP = "HPP";

    /**
     * The AtHP authentication type code - this is one of the possible values
     * keyed from the user profile map by {@link #KEY_AUTH_TYPE}.
     */
    public static final String AUTH_TYPE_ATHP = "ATHP";

    /**
     * The federated authentication type code - this is one of the possible
     * values keyed from the user profile map by {@link #KEY_AUTH_TYPE}.
     */
    public static final String AUTH_TYPE_FED = "FED";

    public static final String COMPANY_HPI = "HPI";

    public static final String COMPANY_HPE = "HPE";

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
    public static final String KEY_USER_GROUPS = "UserGroups";

    /**
     * The key to get the authentication type from user profile map.
     */
    public static final String KEY_AUTH_TYPE = "SPFAuthType";

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
     * The key to get the page friendly id from portal context map.
     */
    public static final String KEY_PORTAL_PAGE_ID = "PortalPageId";

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
     * The key to get the portlet friendly id from the portal context map.
     */
    public static final String KEY_PORTAL_PORTLET_ID = "com.vignette.portal.portlet.friendlyid";

    /**
     * The prefix for all template friendly ID's which are to be publicly
     * accessible (ie no login required).
     */
    public static final String PUBLIC_PAGE_FRIENDLY_ID_PREFIX = "PUBLIC_SPF_";
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
    public static final String PAGE_FRIENDLY_ID_AUTH_ERROR = PUBLIC_PAGE_FRIENDLY_ID_PREFIX
	    + "AUTH_ERROR";

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
    public static final String PAGE_FRIENDLY_ID_PORTAL_PULSE = PUBLIC_PAGE_FRIENDLY_ID_PREFIX
	    + "PORTAL_PULSE";

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
    public static final String PAGE_FRIENDLY_ID_GLOBAL_HELP = PUBLIC_PAGE_FRIENDLY_ID_PREFIX
	    + "GLOBAL_HELP";

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
    public static final String PAGE_FRIENDLY_ID_FED_LOGOUT = PUBLIC_PAGE_FRIENDLY_ID_PREFIX
	    + "FED_LOGOUT";

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
    public static final String PAGE_FRIENDLY_ID_FED_ERROR = PUBLIC_PAGE_FRIENDLY_ID_PREFIX
	    + "FED_ERROR";

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
    public static final String PAGE_FRIENDLY_ID_SELECT_LOCALE = PUBLIC_PAGE_FRIENDLY_ID_PREFIX
	    + "SELECT_LOCALE";

    /**
     * The template friendly URI for the SPF-standard locale selector secondary
     * page. This is a process-action secondary page, so this is a process
     * action template friendly URI. This is common because both portal and
     * portlets may want to create URLs pointing to this page.
     */
    public static final String PAGE_FRIENDLY_URI_SELECT_LOCALE = "template."
	    + PAGE_FRIENDLY_ID_SELECT_LOCALE + "/action.process/";

    /**
     * The template friendly ID for the SPF returns secondary page. This is the
     * page which can be linked to (eg by 3rd-party Web sites like HPP Central
     * Forms) for returning to SPF. This is common because both portal and
     * portlet may want to create URLs pointing to this page.
     */
    public static final String PAGE_FRIENDLY_ID_RETURN = PUBLIC_PAGE_FRIENDLY_ID_PREFIX
	    + "RETURN";

    /**
     * The template friendly URI for the SPF returns secondary page. This is the
     * page which can be linked to (eg by 3rd-party Web sites like HPP Central
     * Forms) for returning to SPF. This is common because both portal and
     * portlet may want to create URLs pointing to this page.
     */
    public static final String PAGE_FRIENDLY_URI_RETURN = "template."
	    + PAGE_FRIENDLY_ID_RETURN;

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
     * SPF sometimes needs to perform activities under the
     * {@link #SPF_CORE_SITE} Vignette site, but which are meant to be effective
     * for an actual portal site. In that case, the site name (ie Vignette "site
     * DNS name") for the actual portal site is carried in a query parameter
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

    /**
     * The name of the HTML viewer portlet view file request parameter, which
     * may be a render parameter or a portal query parameter containing the
     * basename of the view file to use for this request.
     */
    public final static String PARAM_HTML_VIEWER_VIEW_FILE = "spfViewFile";

    /**
     * The name of the HTML viewer portlet includes-file request parameter,
     * which may be a render parameter or a portal query parameter containing
     * the basename of the includes-file to use for this request.
     */
    public final static String PARAM_HTML_VIEWER_INCLUDES_FILE = "spfIncludesFile";

    /**
     * The name of the form parameter (query parameter) used by SiteMinder (both
     * HPP and AtHP versions) to record the target URL.
     */
    public final static String PARAM_SM_TARGET = "TARGET";

    /**
     * The name of the Diagnostic Id which is used in MDC, in request attribute
     * and in diagnostic context.
     */
    public final static String DIAGNOSTIC_ID = "SPF_DC_ID";

    /**
     * Prefix your portal/portlet session attribute names with this string, and
     * they will be <b>kept</b> after logout from the portal. (This is for
     * portal session attributes only, not portlet.)
     */
    public final static String STICKY_SESSION_ATTR_PREFIX = "SPF_RETAIN_";

    /**
     * Prefix your portal/portlet session attribute names with this string, and
     * they will be <b>removed</b> for you upon logout from the portal. (This is
     * for portal session attributes only, not portlet.)
     */
    public final static String UNSTICKY_SESSION_ATTR_PREFIX = "SPF_";

    /**
     * The key of session attribute storing when portal session clean up was
     * done This key is also used as a request attribute to be passed to portlet
     * producer in WSRP requests scope and portlet scope sessions
     */
    public final static String KEY_LAST_PORTAL_SESSION_CLEANUP_DATE = UNSTICKY_SESSION_ATTR_PREFIX
	    + "LastSessionCleanupDate";

}
