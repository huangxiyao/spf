package com.sun.portal.portletcontainer.context.registry.database;

import com.sun.portal.portletcontainer.admin.database.PortletRegistryContextDBImpl;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryContext;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryContextFactory;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryException;

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

    public PortletRegistryContext getPortletRegistryContext(String portalId, String namespace) throws PortletRegistryException {
        return getPortletRegistryContext();
    }
}
