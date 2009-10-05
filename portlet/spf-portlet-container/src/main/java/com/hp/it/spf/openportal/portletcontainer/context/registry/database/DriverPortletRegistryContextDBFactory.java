/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.openportal.portletcontainer.context.registry.database;

import com.sun.portal.portletcontainer.context.registry.PortletRegistryContext;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryContextFactory;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryException;

/**
 * A factory class to create concrete DriverPortletRegistryContext implementation
 * 
 * @author Ding, Kai-Jian
 * 
 */
public class DriverPortletRegistryContextDBFactory implements
		PortletRegistryContextFactory {

	private static String implementation = "com.hp.it.spf.openportal.portletcontainer.admin.database.PortletRegistryContextDBImpl";

	/**
	 * Returns a new instance of the Portlet Registry
	 */
	public PortletRegistryContext getPortletRegistryContext()
			throws PortletRegistryException {
		Object classInstance = null;
		try {
			classInstance = Class.forName(implementation).newInstance();
		} catch (Exception ex) {
			throw new PortletRegistryException(ex);
		}
		PortletRegistryContext portletRegistryContext = (PortletRegistryContext) classInstance;

		return portletRegistryContext;
	}

	/**
	 * get portlet registry context
	 * 
	 * @param String
	 *            portalId
	 * @param String
	 *            namespace
	 * 
	 * @return PortletRegistryContext
	 */
	public PortletRegistryContext getPortletRegistryContext(String portalId,
			String namespace) throws PortletRegistryException {
		return getPortletRegistryContext();
	}
}
