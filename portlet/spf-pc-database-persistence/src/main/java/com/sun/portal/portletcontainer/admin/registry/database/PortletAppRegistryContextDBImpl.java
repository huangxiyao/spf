package com.sun.portal.portletcontainer.admin.registry.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.portal.container.ChannelMode;
import com.sun.portal.portletcontainer.admin.registry.PortletAppRegistryContext;
import com.sun.portal.portletcontainer.admin.registry.PortletRegistryTags;
import com.sun.portal.portletcontainer.admin.registry.database.dao.PortletAppRegistryDao;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletApp;
import com.sun.portal.portletcontainer.admin.registry.database.utils.PortletRegistryUtils;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryException;

public class PortletAppRegistryContextDBImpl implements PortletAppRegistryContext {
	PortletAppRegistryDao portletAppRegistryDao = null;
	
	public PortletAppRegistryContextDBImpl() {
		portletAppRegistryDao = new PortletAppRegistryDao();
	}

	public List<String> getAvailablePortlets() throws PortletRegistryException {
		List<PortletApp> portlets = portletAppRegistryDao.getAllPortlets();
		List<String> availablePortlets = new ArrayList<String>();
		for (PortletApp portlet : portlets) {
			String portletName = portlet.getPortletName();
			availablePortlets.add(portletName);
		}
		return availablePortlets;
	}

	public String getDescription(String portletName, String desiredLocale) throws PortletRegistryException {
		PortletApp portletApp = portletAppRegistryDao.getPortlet(portletName);
		Map<String, String> map = PortletRegistryUtils.getCollectionProperty(portletApp, PortletRegistryTags.DESCRIPTION_MAP_KEY);
        String description = null;
        if (map != null && map.size() > 0)
            description = (String)map.get(desiredLocale);
        return description;
	}

	public String getDisplayName(String portletName, String desiredLocale) throws PortletRegistryException {
		PortletApp portletApp = portletAppRegistryDao.getPortlet(portletName);
		Map<String, String> map = PortletRegistryUtils.getCollectionProperty(portletApp, PortletRegistryTags.DISPLAY_NAME_MAP_KEY);
        String displayName = null;
        if (map != null && map.size() > 0)
            displayName = (String)map.get(desiredLocale);
        return displayName;
	}

	public List<String> getKeywords(String portletName, String desiredLocale) throws PortletRegistryException {
		PortletApp portletApp = portletAppRegistryDao.getPortlet(portletName);
		Map<String, String> map = PortletRegistryUtils.getCollectionProperty(portletApp, PortletRegistryTags.KEYWORDS_KEY);
		List<String> keywords = null;
		if (map != null && map.size() > 0)
			keywords = PortletRegistryUtils.mapValuesToList(map);
		return keywords;
	}

	public List<String> getMarkupTypes(String portletName) throws PortletRegistryException {
		PortletApp portletApp = portletAppRegistryDao.getPortlet(portletName);
		Map<String, String> map = PortletRegistryUtils.getCollectionProperty(portletApp, PortletRegistryTags.SUPPORTED_CONTENT_TYPES_KEY);
		List<String> markupTypes = null;
		if (map != null && map.size() > 0)
            markupTypes = PortletRegistryUtils.mapValuesToList(map);
        return markupTypes;
	}

	public Map<String, String> getRoleMap(String portletName) throws PortletRegistryException {
		PortletApp portletApp = portletAppRegistryDao.getPortlet(portletName);
		Map<String, String> map = PortletRegistryUtils.getCollectionProperty(portletApp, PortletRegistryTags.ROLE_MAP_KEY);
		return map;
	}

	public String getShortTitle(String portletName, String desiredLocale) throws PortletRegistryException {
		PortletApp portletApp = portletAppRegistryDao.getPortlet(portletName);
		String shortTitle = portletApp.getShortTitle();			
		return shortTitle;
	}

	public String getTitle(String portletName, String desiredLocale) throws PortletRegistryException {
		PortletApp portletApp = portletAppRegistryDao.getPortlet(portletName);
		String title = portletApp.getTitle();			
		return title;
	}

	public Map<String, String> getUserInfoMap(String portletName) throws PortletRegistryException {
		PortletApp portletApp = portletAppRegistryDao.getPortlet(portletName);
		Map<String, String> userInfoMap = PortletRegistryUtils.getCollectionProperty(portletApp, PortletRegistryTags.USER_INFO_MAP_KEY);
		return userInfoMap;
	}

	public boolean hasEdit(String portletName) throws PortletRegistryException {
		PortletApp portletApp = portletAppRegistryDao.getPortlet(portletName);
		List<String> list = PortletRegistryUtils.getSupportedPortletModes(portletApp, portletName);
        return list.contains("EDIT");
	}

	public boolean hasHelp(String portletName) throws PortletRegistryException {
		PortletApp portletApp = portletAppRegistryDao.getPortlet(portletName);
		List<String> list = PortletRegistryUtils.getSupportedPortletModes(portletApp, portletName);
        return list.contains("HELP");
	}

	public boolean hasView(String portletName) throws PortletRegistryException {
		return true;
	}

	/**
	 * remove portlet by portlet name
	 */
	public void removePortlet(String portletName) throws PortletRegistryException {
		portletAppRegistryDao.removePortlet(portletName);		
	}
	
	public Map<String, List<ChannelMode>> getSupportedModes(String portletName) throws PortletRegistryException {
	    PortletApp portletApp = portletAppRegistryDao.getPortlet(portletName);        
        Map<String, List<String>> declaredPortletModes = PortletRegistryUtils.getSupportedModes(portletApp, PortletRegistryTags.SUPPORTS_MAP_KEY);
	    Map<String, List<ChannelMode>> wrappedPortletModes =
            new HashMap<String, List<ChannelMode>>(declaredPortletModes.size());
	    for (Map.Entry<String, List<String>> entry : declaredPortletModes.entrySet()) {

            List<ChannelMode> portletModes = new ArrayList<ChannelMode>();
            for (String mode : entry.getValue()) {
                portletModes.add(new ChannelMode(mode));
            }

            wrappedPortletModes.put(entry.getKey(), portletModes);
        }

        return wrappedPortletModes;    
	}
}
