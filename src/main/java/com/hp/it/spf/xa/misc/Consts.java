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
	 * The PortletRequest.USER_INFO map key value for: email address. This is
	 * common because the SPF portal sets it so the portlet can read it.
	 */
	// TODO - this is the property key for a portlet request property containing
	// email address. Check and change this as needed. Also see TODO in the
	// portlet Utils.getUserProperty method.
	public static final String KEY_USER_INFO_EMAIL = "user.business-info.online.email";

	/**
	 * The PortletRequest.USER_INFO map key value for: first (given) name. This
	 * is common because the SPF portal sets it so the portlet can read it.
	 */
	// TODO - this is the property key for a portlet request property containing
	// first (given) name. Check and change this as needed. Also see TODO in the
	// portlet Utils.getUserProperty method.
	public static final String KEY_USER_INFO_GIVEN_NAME = "user.name.given";

	/**
	 * The PortletRequest.USER_INFO P3P key value for: last (family) name. This
	 * is common because the SPF portal sets it so the portlet can read it.
	 */
	// TODO - this is the property key for a portlet request property containing
	// last (family) name. Check and change this as needed. Also see TODO in the
	// portlet Utils.getUserProperty method.
	public static final String KEY_USER_INFO_FAMILY_NAME = "user.name.family";

	// TODO - the email, first name, and last name keys are defined above -
	// define all the rest of the keys for all the other user attributes which
	// are passed, here. This is for possible future use in portlets which need
	// to get those attributes using portlet Utils.getUserProperty method.

	/**
	 * The key for the portlet friendly id in the portlet request. This is
	 * non-standard. The SPF portal puts the friendly ID into this request
	 * attribute so the portlet can then get it.
	 */
	// TODO - this is the key name for accessing portlet friendly ID in the
	// portlet request. Check and change this as needed. Also see TODO in the
	// portlet Utils.getPortletID method.
	public static final String KEY_PORTLET_FRIENDLY_ID = "com.vignette.portal.portlet.friendlyid";

	/**
	 * The key to get the site name (ie what Vignette calls the "site DNS name")
	 * from the portlet request. This is non-standard. The SPF portal puts the
	 * friendly ID into this request attribute so the portlet can then get it.
	 */
	// TODO - this is the key name for accessing portal site name in the portlet
	// request. Check and change this as needed. Also see TODO in the portlet
	// Utils.getSiteName method.
	public static final String KEY_PORTLET_SITE_NAME = "com.vignette.portal.site.dns";

	/**
	 * The key to get the authorization groups from the portlet request. This is
	 * non-standard. The SPF portal puts the array of groups into this attribute
	 * automatically so the portlet can get them.
	 */
	// TODO - this is the key name for accessing portal site name in the portlet
	// request. Check and change this as needed. Also see TODO in the portlet
	// Utils.getGroups method.
	public static final String KEY_PORTLET_GROUPS = "what.is.it";

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
	 * The name of the query parameter containing the site name to use for the
	 * logout redirect target. Both portal and portlets may want to point to the
	 * logout process and pass a redirect target, so this is common.
	 */
	public final static String PARAM_LOGOUT_SITE = "spfSite";

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
