package com.sun.portal.portletcontainer.admin.registry.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        if(map != null)
            description = (String)map.get(desiredLocale);
        return description;
	}

	public String getDisplayName(String portletName, String desiredLocale) throws PortletRegistryException {
		PortletApp portletApp = portletAppRegistryDao.getPortlet(portletName);
		Map<String, String> map = PortletRegistryUtils.getCollectionProperty(portletApp, PortletRegistryTags.DISPLAY_NAME_MAP_KEY);
        String displayName = null;
        if(map != null)
            displayName = (String)map.get(desiredLocale);
        return displayName;
	}

	public List<String> getKeywords(String portletName, String desiredLocale) throws PortletRegistryException {
		PortletApp portletApp = portletAppRegistryDao.getPortlet(portletName);
		Map<String, String> map = PortletRegistryUtils.getCollectionProperty(portletApp, PortletRegistryTags.KEYWORDS_KEY);
		List<String> keywords = null;
		if (map != null)
			keywords = PortletRegistryUtils.mapValuesToList(map);
		return keywords;
	}

	public List<String> getMarkupTypes(String portletName) throws PortletRegistryException {
		PortletApp portletApp = portletAppRegistryDao.getPortlet(portletName);
		Map<String, String> map = PortletRegistryUtils.getCollectionProperty(portletApp, PortletRegistryTags.SUPPORTED_CONTENT_TYPES_KEY);
		List<String> markupTypes = null;
        if(map != null)
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
		String shortTitle = PortletRegistryUtils.getStringProperty(portletApp, PortletRegistryTags.SHORT_TITLE_KEY);
		return shortTitle;
	}

	public String getTitle(String portletName, String desiredLocale) throws PortletRegistryException {
		PortletApp portletApp = portletAppRegistryDao.getPortlet(portletName);
		String title = PortletRegistryUtils.getStringProperty(portletApp, PortletRegistryTags.TITLE_KEY);
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
}
