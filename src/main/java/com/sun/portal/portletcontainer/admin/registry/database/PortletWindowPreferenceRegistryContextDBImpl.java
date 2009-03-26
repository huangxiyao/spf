/*
 * CDDL HEADER START
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 *
 * You can obtain a copy of the License at
 * http://www.sun.com/cddl/cddl.html and legal/CDDLv1.0.txt
 * See the License for the specific language governing
 * permission and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Copyright 2006 Sun Microsystems Inc. All Rights Reserved
 * CDDL HEADER END
 */

package com.sun.portal.portletcontainer.admin.registry.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.sun.portal.portletcontainer.admin.registry.PortletRegistryTags;
import com.sun.portal.portletcontainer.admin.registry.PortletWindowPreferenceRegistryContext;
import com.sun.portal.portletcontainer.admin.registry.database.dao.PortletWindowPreferenceRegistryDao;
import com.sun.portal.portletcontainer.admin.registry.database.dao.PortletWindowRegistryDao;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletUserWindow;
import com.sun.portal.portletcontainer.admin.registry.database.utils.PortletRegistryUtils;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryContext;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryException;

/**
 * PortletWindowPreferenceRegistryContextImpl is a concrete implementation of
 * the PortletWindowPreferenceRegistryContext interface.
 */
public class PortletWindowPreferenceRegistryContextDBImpl implements
        PortletWindowPreferenceRegistryContext {

    private PortletWindowPreferenceRegistryDao windowPreferenceRegistryDao = null;
    private PortletWindowRegistryDao windowRegistryDao = null;

    public PortletWindowPreferenceRegistryContextDBImpl()
            throws PortletRegistryException {
        windowPreferenceRegistryDao = new PortletWindowPreferenceRegistryDao();
        windowRegistryDao = new PortletWindowRegistryDao();
    }

    private Map getExistingPreferences(String portletWindowName,
            String userName, boolean readOnly) {
        String preferenceTag = readOnly ? PortletRegistryTags.PREFERENCE_READ_ONLY_KEY
                : PortletRegistryTags.PREFERENCE_PROPERTIES_KEY;
        return windowPreferenceRegistryDao.getPortletWindowPreferences(
                portletWindowName, userName, preferenceTag);
    }

    public Map getPreferencesReadOnly(String portletWindowName, String userName)
            throws PortletRegistryException {
    	// get readonly preference from default scope (portlet.xml)
    	String portletName = windowRegistryDao.getPortletNameByWindowName(portletWindowName);
        return getExistingPreferences(portletName, getDefaultUserName(), true);
    }

    public Map getPreferences(String portletWindowName, String userName)
            throws PortletRegistryException {
    	// preference from portlet.xml
    	Map predefinedPreMap = windowPreferenceRegistryDao.getDefaultPortletWindowPreferences(portletWindowName, 
    			getDefaultUserName(), PortletRegistryTags.PREFERENCE_PROPERTIES_KEY);
    	
        // preferences from config mode
        Map configModePrefMap = getExistingPreferences(portletWindowName,
                getDefaultUserName(), false);
        
        // preferences from other mode
        Map customizedPrefMap = getExistingPreferences(portletWindowName,
                userName, false);
        
        // merge all preferences defined in portlet.xml, config mode and edit mode
        Map userPrefMap;
        if (predefinedPreMap != null) {
            userPrefMap = new HashMap(predefinedPreMap);
        } else {
            userPrefMap = new HashMap();
        }

        if (configModePrefMap != null) {
            userPrefMap.putAll(configModePrefMap);
        }
        
        if (customizedPrefMap != null) {
            userPrefMap.putAll(customizedPrefMap);
        }
        return userPrefMap;
    }

    public void savePreferences(String portletName, String portletWindowName,
            String userName, Map prefMap) throws PortletRegistryException {
        createPreferences(portletName, portletWindowName, userName, prefMap,
                false);
    }

    public void savePreferences(String portletName, String portletWindowName,
            String userName, Map prefMap, boolean readOnly)
            throws PortletRegistryException {
        createPreferences(portletName, portletWindowName, userName, prefMap,
                false);
    }

    public void createPreferences(String portletName, String portletWindowName,
            String userName, Map prefMap, boolean create)
            throws PortletRegistryException {
        PortletUserWindow portletWindowPreference = windowPreferenceRegistryDao
                .getPortletWindowPreference(portletWindowName, userName);
        boolean needCreate = portletWindowPreference == null;
        if (needCreate) {
            portletWindowPreference = new PortletUserWindow();
            portletWindowPreference.setWindowName(portletWindowName);
            portletWindowPreference.setPortletName(portletName);
            portletWindowPreference.setUserName(userName);
        }

        Map userPrefMap = getExistingPreferences(portletWindowName, userName,
                false);
        // If there is an existing content, override it with fresh content
        if (userPrefMap == null) {
            userPrefMap = new HashMap();
        }
        // merge all user map
        userPrefMap.putAll(prefMap);
        
        // get all readonly preference
        Map readonlyPreference = windowPreferenceRegistryDao.getPortletPreferenceByCriteria(getDefaultUserName(), 
        		portletName, PortletRegistryTags.PREFERENCE_READ_ONLY_KEY, "true");
        Set readonlyKeySet = readonlyPreference.keySet();
        // remove readonly items
        for (Object item : readonlyKeySet) {
        	userPrefMap.remove(item);
        }

        PortletRegistryUtils.setCollectionProperty(portletWindowPreference,
                PortletRegistryTags.PREFERENCE_PROPERTIES_KEY, userPrefMap);

        // add all readonly map, if create, create readonly
        
//        Map readOnlyMap = create ? prefMap : getPreferencesReadOnly(
//                portletName, userName);
//        if (readOnlyMap != null) {
//            PortletRegistryUtils.setCollectionProperty(portletWindowPreference,
//                    PortletRegistryTags.PREFERENCE_READ_ONLY_KEY, readOnlyMap);
//        }
        if (needCreate) {
            windowPreferenceRegistryDao
                    .addPortletWindowPreference(portletWindowPreference);
        } else {
            windowPreferenceRegistryDao
                    .updatePortletWindowPreference(portletWindowPreference);
        }
    }

    public void removePreferences(String portletName)
            throws PortletRegistryException {
        windowPreferenceRegistryDao.removePortletWindowPreferences(portletName);
    }

    private String getDefaultUserName() {
        return PortletRegistryContext.USER_NAME_DEFAULT;
    }
}
