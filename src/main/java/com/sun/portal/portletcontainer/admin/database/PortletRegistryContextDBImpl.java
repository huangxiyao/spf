package com.sun.portal.portletcontainer.admin.database;

import java.util.List;
import java.util.Map;

import com.sun.portal.container.EntityID;
import com.sun.portal.container.PortletLang;
import com.sun.portal.container.PortletType;
import com.sun.portal.portletcontainer.admin.PortletRegistryCache;
import com.sun.portal.portletcontainer.admin.registry.PortletAppRegistryContext;
import com.sun.portal.portletcontainer.admin.registry.PortletWindowPreferenceRegistryContext;
import com.sun.portal.portletcontainer.admin.registry.PortletWindowRegistryContext;
import com.sun.portal.portletcontainer.admin.registry.database.PortletAppRegistryContextDBImpl;
import com.sun.portal.portletcontainer.admin.registry.database.PortletWindowPreferenceRegistryContextDBImpl;
import com.sun.portal.portletcontainer.admin.registry.database.PortletWindowRegistryContextDBImpl;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryContext;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryException;

public class PortletRegistryContextDBImpl implements PortletRegistryContext {
	private static Object syncObject = new Object();
	
	private PortletAppRegistryContext portletAppRegistryContext;
    private PortletWindowRegistryContext portletWindowRegistryContext;
    private PortletWindowPreferenceRegistryContext portletWindowPreferenceRegistryContext;
    
	public PortletRegistryContextDBImpl() throws PortletRegistryException {
		synchronized (syncObject) {
			if(!PortletRegistryCache.readPortletAppRegistryCache() || PortletRegistryCache.getPortletAppRegistryContext() == null) {
                portletAppRegistryContext = new PortletAppRegistryContextDBImpl();
                PortletRegistryCache.setPortletAppRegistryContext(portletAppRegistryContext);
                PortletRegistryCache.refreshPortletAppRegistryCache(false);
            }
            portletAppRegistryContext = PortletRegistryCache.getPortletAppRegistryContext();
            if(!PortletRegistryCache.readPortletWindowRegistryCache() || PortletRegistryCache.getPortletWindowRegistryContext() == null) {
                portletWindowRegistryContext = new PortletWindowRegistryContextDBImpl();
                PortletRegistryCache.setPortletWindowRegistryContext(portletWindowRegistryContext);
                PortletRegistryCache.refreshPortletWindowRegistryCache(false);
            }
            portletWindowRegistryContext = PortletRegistryCache.getPortletWindowRegistryContext();
            if(!PortletRegistryCache.readPortletWindowPreferenceRegistryCache() || PortletRegistryCache.getPortletWindowPreferenceRegistryContext() == null) {
                portletWindowPreferenceRegistryContext = new PortletWindowPreferenceRegistryContextDBImpl();
                PortletRegistryCache.setPortletWindowPreferenceRegistryContext(portletWindowPreferenceRegistryContext);
                PortletRegistryCache.refreshPortletWindowPreferenceRegistryCache(false);
            }
            portletWindowPreferenceRegistryContext = PortletRegistryCache.getPortletWindowPreferenceRegistryContext();
		}
	}
	
	public List getMarkupTypes(String portletName) throws PortletRegistryException {
        return portletAppRegistryContext.getMarkupTypes(portletName);
    }
    
    public String getDescription(String portletName, String desiredLocale) throws PortletRegistryException {
        return portletAppRegistryContext.getDescription(portletName, desiredLocale);
    }
    
    public String getShortTitle(String portletName, String desiredLocale) throws PortletRegistryException {
        return portletAppRegistryContext.getShortTitle(portletName, desiredLocale);
    }
    
    public String getTitle(String portletName, String desiredLocale) throws PortletRegistryException {
        return portletAppRegistryContext.getTitle(portletName,desiredLocale);
    }
    
    public List getKeywords(String portletName, String desiredLocale) throws PortletRegistryException {
        return portletAppRegistryContext.getKeywords(portletName,desiredLocale);
    }
    
    public String getDisplayName(String portletName, String desiredLocale) throws PortletRegistryException {
        return portletAppRegistryContext.getDisplayName(portletName,desiredLocale);
    }
    
    public Map getRoleMap(String portletName) throws PortletRegistryException {
        return portletAppRegistryContext.getRoleMap(portletName);
    }
    
    public Map getUserInfoMap(String portletName) throws PortletRegistryException {
        return portletAppRegistryContext.getUserInfoMap(portletName);
    }
    
    public boolean hasView(String portletName) throws PortletRegistryException {
        return portletAppRegistryContext.hasView(portletName);
    }
    
    public boolean hasEdit(String portletName) throws PortletRegistryException {
        return portletAppRegistryContext.hasEdit(portletName);
    }
    
    public boolean hasHelp(String portletName) throws PortletRegistryException {
        return portletAppRegistryContext.hasHelp(portletName);
    }
    
    public List getAvailablePortlets() throws PortletRegistryException {
        return portletAppRegistryContext.getAvailablePortlets();
    }
    
    public List getVisiblePortletWindows(PortletType portletType) throws PortletRegistryException {
        return portletWindowRegistryContext.getVisiblePortletWindows(portletType);
    }
    
    public boolean isVisible(String portletWindowName) throws PortletRegistryException {
        return portletWindowRegistryContext.isVisible(portletWindowName);
    }
    
    public List<String> getAllPortletWindows(PortletType portletType) throws PortletRegistryException {
        return portletWindowRegistryContext.getAllPortletWindows(portletType);
    }
    
    public Integer getRowNumber(String portletWindowName) throws PortletRegistryException {
        return portletWindowRegistryContext.getRowNumber(portletWindowName);
    }
    
    public String getWidth(String portletWindowName) throws PortletRegistryException {
        return portletWindowRegistryContext.getWidth(portletWindowName);
    }
    
    public void setWidth(String portletWindowName, String width) throws PortletRegistryException {
        portletWindowRegistryContext.setWidth(portletWindowName, width);
    }
    
    public EntityID getEntityId(String portletWindowName) throws PortletRegistryException {
        return portletWindowRegistryContext.getEntityId(portletWindowName);
    }
    
    public List<EntityID> getEntityIds() throws PortletRegistryException {
        return portletWindowRegistryContext.getEntityIds();
    }
    
    public String getPortletWindowTitle(String portletWindowName) throws PortletRegistryException {
        return portletWindowRegistryContext.getPortletWindowTitle(portletWindowName);
    }
    
    public void setPortletWindowTitle(String portletWindowName, String title) throws PortletRegistryException {
        portletWindowRegistryContext.setPortletWindowTitle(portletWindowName, title);
    }
    
    public void createPortletWindow(String portletName, String portletWindowName) throws PortletRegistryException {
        portletWindowRegistryContext.createPortletWindow(portletName, portletWindowName);
    }
    
    public void createPortletWindow(String portletName, String portletWindowName, String title, String locale) throws PortletRegistryException {
        portletWindowRegistryContext.createPortletWindow(portletName, portletWindowName, title, locale);
        Map exisitingPreferences = getPreferences(portletName, PortletRegistryContext.USER_NAME_DEFAULT);
        savePreferences(portletName, portletWindowName, PortletRegistryContext.USER_NAME_DEFAULT, exisitingPreferences, true);    
    }
    
    public void removePortletWindow(String portletWindowName) throws PortletRegistryException {
        portletWindowRegistryContext.removePortletWindow(portletWindowName);
    }
    
    public void showPortletWindow(String portletWindowName, boolean visible) throws PortletRegistryException {
        portletWindowRegistryContext.showPortletWindow(portletWindowName, visible);
    }
    
    public void removePortlet(String portletName) throws PortletRegistryException {
        portletAppRegistryContext.removePortlet(portletName);
        portletWindowRegistryContext.removePortletWindows(portletName);
        portletWindowPreferenceRegistryContext.removePreferences(portletName);
    }
    
    public Map getPreferences(String portletWindowName, String userName) throws PortletRegistryException {
        return portletWindowPreferenceRegistryContext.getPreferences(portletWindowName, userName);
    }
    
    public Map getPreferencesReadOnly(String portletWindowName, String userName) throws PortletRegistryException {
        return portletWindowPreferenceRegistryContext.getPreferencesReadOnly(portletWindowName, userName);
    }
    
    public void savePreferences(String portletName, String portletWindowName, String userName, Map prefMap) throws PortletRegistryException {
        portletWindowPreferenceRegistryContext.savePreferences(portletName, portletWindowName, userName, prefMap);
    }
    
    private void savePreferences(String portletName, String portletWindowName, String userName, Map prefMap, boolean readOnly) throws PortletRegistryException {
        portletWindowPreferenceRegistryContext.savePreferences(portletName, portletWindowName, userName, prefMap, readOnly);
    }
    
    public String getPortletName(String portletWindowName) throws PortletRegistryException {
        return portletWindowRegistryContext.getPortletName(portletWindowName);
    }
    
    public List<String> getPortletWindows(String portletName) throws PortletRegistryException {
        return portletWindowRegistryContext.getPortletWindows(portletName);
    }
    
    public String getPortletID(String portletWindowName) throws PortletRegistryException {
        return portletWindowRegistryContext.getPortletID(portletWindowName);
    }
    
    public String getConsumerID(String portletWindowName) throws PortletRegistryException {
        return portletWindowRegistryContext.getConsumerID(portletWindowName);
    }
    
    public String getProducerEntityID(String portletWindowName) throws PortletRegistryException {
        return portletWindowRegistryContext.getProducerEntityID(portletWindowName);
    }
    
    public boolean isRemote(String portletWindowName) throws PortletRegistryException {
        return portletWindowRegistryContext.isRemote(portletWindowName);
    }
    
    public PortletLang getPortletLang(String portletWindowName) throws PortletRegistryException {
        return portletWindowRegistryContext.getPortletLang(portletWindowName);
    }  
}
