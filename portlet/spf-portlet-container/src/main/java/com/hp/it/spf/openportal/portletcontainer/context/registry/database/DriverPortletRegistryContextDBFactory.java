/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.openportal.portletcontainer.context.registry.database;

import com.hp.it.spf.openportal.portletcontainer.admin.database.PortletRegistryContextDBImpl;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryContext;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryContextFactory;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryException;

/**
 * A factory class to create concrete DriverPortletRegistryContext implementation
 * 
 * @author Ding, Kai-Jian
 * 
 */
public class DriverPortletRegistryContextDBFactory implements PortletRegistryContextFactory {

    private static PortletRegistryContext implementation = null;
    private static Object syncObject = new Object();

    /**
     * Returns a new instance of the Portlet Registry
     */
    public PortletRegistryContext getPortletRegistryContext() throws PortletRegistryException {
        if (implementation == null) {
            synchronized (syncObject) {
                if (implementation == null) {
                    implementation = new PortletRegistryContextDBImpl();
                }
            }
        }

        return implementation;
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
