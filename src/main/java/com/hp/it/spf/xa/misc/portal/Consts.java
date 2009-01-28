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
	public static final String COOKIE_NAME_SPF_LOCALE = "HP_SPF_LOCALE";

	/**
	 * The name of the request attribute containing global help content. Set by
	 * the secondary page type action for use by the secondary page JSP.
	 */
	public static final String REQUEST_ATTR_GLOBAL_HELP_DATA = "SPF_GLOBAL_HELP_DATA";

	/**
	 * The "resume" URL to be presented to the user in the federated logout
	 * confirmation page. Set by the secondary page type action for use by the
	 * secondary page JSP.
	 */
	public static final String REQUEST_ATTR_FED_LOGOUT_RESUME_URL = "SPF_FED_LOGOUT_RESUME_URL";

	/**
	 * The "try again" URL to be presented to the user in the federation error
	 * page. Set by the secondary page type action for use in the secondary page
	 * JSP.
	 */
	public static final String REQUEST_ATTR_FED_ERROR_RETRY_URL = "SPF_FED_ERROR_RETRY_URL";

	/**
	 * The name of the session attribute storing portal pulse data. Set by the
	 * secondary page type action for use by the secondary page JSP.
	 */
	public static final String SESSION_ATTR_PORTAL_PULSE_DATA = "SPF_PORTAL_PULSE_DATA";

	/**
	 * The name of the response header from the portal pulse page which
	 * indicates if the site is available.
	 */
	public static final String RESP_HDR_X_SITE_AVAILABLE = "X-Site-Available";

	/**
	 * The Vignette User object property for user ID. This is the user ID, not
	 * the user person name.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_USER_NAME_ID = "username";

	/**
	 * The Vignette User object property for domain.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_DOMAIN_ID = "domain";

	/**
	 * The Vignette User object property for profile ID.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_PROFILE_ID = "profileid";

	/**
	 * The Vignette User object property for customer ID.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_CUSTOMER_ID = "customerid";

	/**
	 * The Vignette User object property for email contact preference.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_EMAIL_PREF_ID = "emailcontactpref";

	/**
	 * The Vignette User object property for phone contact preference.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_PHONE_PREF_ID = "phonecontactpref";

	/**
	 * The Vignette User object property for postal contact preference.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_POSTAL_PREF_ID = "postalcontactpref";

	/**
	 * The Vignette User object property for security level.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_SECURITYLEVEL_ID = "securitylevel";

	/**
	 * The Vignette User object property for last change timestamp.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_LAST_CHANGE_DATE_ID = "lastchangedate";

	/**
	 * The Vignette User object property for SPF-standard timezone.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_SP_TIMEZONE_ID = "sp_timezone";

	/**
	 * The Vignette User object property for Vignette-standard timezone (which
	 * is really just a GMT offset, not a timezone).
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_TIMEZONE_ID = "timezone";

	/**
	 * The Vignette User object property for first (given) name.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_FIRSTNAME_ID = "firstname";

	/**
	 * The Vignette User object property for last (family) name.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_LASTNAME_ID = "lastname";

	/**
	 * The Vignette User object property for language code.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_LANGUAGE_ID = "language";

	/**
	 * The Vignette User object property for country code.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_COUNTRY_ID = "country";

	/**
	 * The Vignette User object property for email address.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_EMAIL_ID = "email";

	/**
	 * The Vignette User object property for last used site DNS name.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_PRIMARY_SITE_ID = "primary_site_id";

	/**
	 * The Vignette User object property for logon. I do not know what this is.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_LOGON_ID = "logon";

	/**
	 * The Vignette User object property for user phone number.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_PHONE_ID = "day_phone";

	/**
	 * The Vignette User object property for user phone extension.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_PHONE_EXT_ID = "day_phone_ext";

	/**
	 * The Vignette User object property for last used portal hostname.
	 */
	// TODO - check this is still correct in new authentication solution. This
	// property key is used with the portal Utils.getUserProperty method - see
	// TODO there.
	public static final String PROPERTY_RECENT_ACCESS_HOST = "recent_access_host";

	// TODO - many user properties are listed above - add any ones which are
	// missing, so that they will be defined for portal component developers.
	// These property keys are used with the portal Utils.getUserProperty method
	// - see TODO there.

	/**
	 * Prefix your portal session attribute names with this string, and they
	 * will be <b>kept</b> after logout from the portal. (This is for portal
	 * session attributes only, not portlet.)
	 */
	public final static String STICKY_SESSION_ATTR_PREFIX = "SPF_RETAIN";

	/**
	 * Prefix your portal session attribute names with this string, and they
	 * will be <b>removed</b> for you upon logout from the portal. (This is for
	 * portal session attributes only, not portlet.)
	 */
	public final static String UNSTICKY_SESSION_ATTR_PREFIX = "SPF_";

	/**
	 * The default site name to assume during logout for the redirect target.
	 */
	public final static String LOGOUT_DEFAULT_SITE = "spf";

}