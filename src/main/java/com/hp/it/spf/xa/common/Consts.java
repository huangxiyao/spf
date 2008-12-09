/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.xa.common;

/**
 * <p>
 * Container class for common portal/portlet constants which need
 * cross-portal/portlet exposure.
 * </p>
 * 
 * @version TBD
 */
public class Consts {

	/**
	 * The PortletRequest.USER_INFO P3P map key value for: email address.
	 * TODO - these key names will probably change to correspond with the SOAP 
	 * user object we create.
	 */
	public static final String KEY_USER_INFO_EMAIL = "user.business-info.online.email";
	public static final String KEY_USER_INFO_GIVEN_NAME = "user.name.given";
	public static final String KEY_USER_INFO_FAMILY_NAME = "user.name.family";

	/**
	 * PortletRequest attribute name, used to pass user groups from portal to
	 * portlet. This is non-standard (Vignette copies any HTTP servlet request
	 * attribute whose name starts with
	 * "com.hp.vignette.portal.attribute.portlet.*" into the portlet request),
	 * but there is no better easy way to do it at present.
	 */
	/*
	 * TODO - we will probably not need this. Delete this when we have setup
	 * passing groups through PortletRequest.USER_INFO public static final
	 * 
	 * String REQUEST_ATTR_GROUPS =
	 * "com.vignette.portal.attribute.portlet.spf.usergroups";
	 */

}
