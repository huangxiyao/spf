/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */

package com.hp.it.spf.openportal.portletcontainer.admin.registry.database;

import java.util.Map;

import com.sun.portal.portletcontainer.context.registry.PortletRegistryContext;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryException;

/**
 * PortletWindowPreferenceRegistryContextImpl is a concrete implementation of the PortletWindowPreferenceRegistryContext interface.
 */
public class PortletWindowPreferenceRegistryContextDBImpl
		extends
		com.sun.portal.portletcontainer.admin.registry.database.PortletWindowPreferenceRegistryContextDBImpl {

	// "U1BGX0NPTkZJR19BRE1JTg==" is the base64 encoded "SPF_CONFIG_ADMIN"
	private final String defaultBase64 = "U1BGX0NPTkZJR19BRE1JTg==";

	/**
	 * construction method
	 * 
	 * @throws PortletRegistryException
	 */
	public PortletWindowPreferenceRegistryContextDBImpl()
			throws PortletRegistryException {
		super();
	}

	/**
	 * get readonly preference map for special portlet window and user
	 * 
	 * @param String
	 *            portletWindowName
	 * @param String
	 *            userName
	 * @return readonly preference map
	 */
	public Map getPreferencesReadOnly(String portletWindowName, String userName)
			throws PortletRegistryException {
		return super.getPreferencesReadOnly(portletWindowName,
				formatDefaultUserName(userName));
	}

	/**
	 * get preference map for special portlet window and user
	 * 
	 * @param String
	 *            portletWindowName
	 * @param String
	 *            userName
	 * @return preference map
	 */
	public Map getPreferences(String portletWindowName, String userName)
			throws PortletRegistryException {
		return super.getPreferences(portletWindowName,
				formatDefaultUserName(userName));
	}

	/**
	 * save preferences for special portlet, portlet window and user
	 * 
	 * @param String
	 *            portletName
	 * @param String
	 *            portletWindowName
	 * @param String
	 *            userName
	 * @param Map
	 *            preference map
	 */
	public void savePreferences(String portletName, String portletWindowName,
			String userName, Map prefMap) throws PortletRegistryException {
		super.savePreferences(portletName, portletWindowName,
				formatDefaultUserName(userName), prefMap);
	}

	/**
	 * save preferences for special porlet, portlet window and user
	 * 
	 * @param String
	 *            portletName
	 * @param String
	 *            portletWindowName
	 * @param String
	 *            userName
	 * @param boolean is readonly
	 * @param Map
	 *            preference map
	 */
	public void savePreferences(String portletName, String portletWindowName,
			String userName, Map prefMap, boolean readOnly)
			throws PortletRegistryException {
		super.savePreferences(portletName, portletWindowName,
				formatDefaultUserName(userName), prefMap, readOnly);
	}

	/**
	 * create preferences for special portlet, portlet window and user
	 * 
	 * @param String
	 *            portletName
	 * @param String
	 *            portletWindowName
	 * @param String
	 *            userName
	 * @param boolean create to create
	 * @param Map
	 *            preference map
	 */
	public void createPreferences(String portletName, String portletWindowName,
			String userName, Map prefMap, boolean create)
			throws PortletRegistryException {
		super.createPreferences(portletName, portletWindowName,
				formatDefaultUserName(userName), prefMap, create);
	}

	private String formatDefaultUserName(String userNmae) {
		// for config mode hack, if it is base64 encoded "SPF_CONFIG_ADMIN", let
		// it be "default"
		return defaultBase64.equals(userNmae) ? PortletRegistryContext.USER_NAME_DEFAULT
				: userNmae;
	}

}
