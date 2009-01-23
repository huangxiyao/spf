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
	 * The PortletRequest.USER_INFO map key value for: email address.
	 */
	// TODO - this is the property key for a portlet request property containing
	// email address. Check and change this as needed. Also see TODO in the
	// portlet Utils.getUserProperty method.
	public static final String KEY_USER_INFO_EMAIL = "user.business-info.online.email";

	/**
	 * The PortletRequest.USER_INFO map key value for: first (given) name.
	 */
	// TODO - this is the property key for a portlet request property containing
	// first (given) name. Check and change this as needed. Also see TODO in the
	// portlet Utils.getUserProperty method.
	public static final String KEY_USER_INFO_GIVEN_NAME = "user.name.given";

	/**
	 * The PortletRequest.USER_INFO P3P key value for: last (family) name.
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
	 * non-standard. SPF portal puts the friendly ID into this request attribute
	 * so portlet can then get it.
	 */
	// TODO - this is the key name for accessing portlet friendly ID in the
	// portlet request. Check and change this as needed. Also see TODO in the
	// portlet Utils.getPortletID method.
	public static final String KEY_PORTLET_FRIENDLY_ID = "com.vignette.portal.portlet.friendlyid";

	/**
	 * The key to get the site name (ie what Vignette calls the "site DNS name")
	 * from the portlet request. This is non-standard. SPF portal puts the
	 * friendly ID into this request attribute so portlet can then get it.
	 */
	// TODO - this is the key name for accessing portal site name in the portlet
	// request. Check and change this as needed. Also see TODO in the portlet
	// Utils.getSiteName method.
	public static final String KEY_PORTLET_SITE_NAME = "com.vignette.portal.site.dns";

	/**
	 * The key to get the authorization groups from the portlet request. This is
	 * non-standard (SPF puts the array of groups into this attribute
	 * automatically).
	 */
	// TODO - this is the key name for accessing portal site name in the portlet
	// request. Check and change this as needed. Also see TODO in the portlet
	// Utils.getGroups method.
	public static final String KEY_PORTLET_GROUPS = "what.is.it";

}
