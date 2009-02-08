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
public class Consts extends com.hp.it.spf.xa.misc.Consts {

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
	public static final String PROPERTY_USER_NAME_ID = "username";

	/**
	 * The Vignette User object property for domain.
	 */
	public static final String PROPERTY_DOMAIN_ID = "domain";

	/**
	 * The Vignette User object property for profile ID.
	 */
	public static final String PROPERTY_PROFILE_ID = "profileid";

	/**
	 * The Vignette User object property for last change timestamp.
	 */
	public static final String PROPERTY_LAST_CHANGE_DATE_ID = "lastchangedate";

	/**
	 * The Vignette User object property for SPF-standard timezone.
	 */
	public static final String PROPERTY_SPF_TIMEZONE_ID = "spf_timezone";

	/**
	 * The Vignette User object property for Vignette-standard timezone (which
	 * is really just a GMT offset, not a timezone).
	 */
	public static final String PROPERTY_TIMEZONE_ID = "timezone";

	/**
	 * The Vignette User object property for first (given) name.
	 */
	public static final String PROPERTY_FIRSTNAME_ID = "firstname";

	/**
	 * The Vignette User object property for last (family) name.
	 */
	public static final String PROPERTY_LASTNAME_ID = "lastname";

	/**
	 * The Vignette User object property for language code.
	 */
	public static final String PROPERTY_LANGUAGE_ID = "language";

	/**
	 * The Vignette User object property for country code.
	 */
	public static final String PROPERTY_COUNTRY_ID = "country";

	/**
	 * The Vignette User object property for email address.
	 */
	public static final String PROPERTY_EMAIL_ID = "email";

	/**
	 * The Vignette User object property for last used site DNS name.
	 */
	public static final String PROPERTY_PRIMARY_SITE_ID = "primary_site_id";

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

}