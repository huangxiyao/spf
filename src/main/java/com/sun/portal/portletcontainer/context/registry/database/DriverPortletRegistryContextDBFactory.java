package com.sun.portal.portletcontainer.context.registry.database;

import com.sun.portal.portletcontainer.context.registry.PortletRegistryContext;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryContextFactory;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryException;

public class DriverPortletRegistryContextDBFactory implements PortletRegistryContextFactory {
	
	private static String implementation = "com.sun.portal.portletcontainer.admin.database.PortletRegistryContextDBImpl";
	
	/**
     * Returns a new instance of the Portlet Registry
     */    
    public PortletRegistryContext getPortletRegistryContext() throws PortletRegistryException {        
        Object classInstance = null;
        try {
            classInstance = Class.forName(implementation).newInstance();
        } catch (Exception ex) {
            throw new PortletRegistryException(ex);
        }
        PortletRegistryContext portletRegistryContext = (PortletRegistryContext) classInstance;
        
        return portletRegistryContext;        
    }

    public PortletRegistryContext getPortletRegistryContext(String portalId, String namespace) throws PortletRegistryException {
        return getPortletRegistryContext();
    }
}
