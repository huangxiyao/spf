package com.sun.portal.portletcontainer.admin.registry.database.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.sun.portal.portletcontainer.admin.PortletRegistryElement;
import com.sun.portal.portletcontainer.admin.PortletRegistryHelper;
import com.sun.portal.portletcontainer.admin.PortletRegistryWriter;
import com.sun.portal.portletcontainer.admin.registry.PortletRegistryTags;
import com.sun.portal.portletcontainer.admin.registry.PortletWindowPreference;
import com.sun.portal.portletcontainer.admin.registry.PortletWindowPreferenceRegistryContextImpl;
import com.sun.portal.portletcontainer.admin.registry.PortletWindowPreferenceRegistryWriter;
import com.sun.portal.portletcontainer.admin.registry.database.PortletWindowPreferenceRegistryContextDBImpl;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletUserWindow;
import com.sun.portal.portletcontainer.admin.registry.database.utils.PortletRegistryUtils;
import com.sun.portal.portletcontainer.context.registry.PortletRegistryException;

public class PortletWindowPreferenceRegistryTest extends TestCase {
    private PortletWindowPreferenceRegistryContextImpl portletWindowPreferenceRegistry = null;

    private PortletWindowPreferenceRegistryContextDBImpl portletWindowPreferenceDBRegistry = null;

    private String portletWindowName = "testwindow";

    private String defaultUserName = "default";

    private String userName = "testuser";

    private String portletName = "portletdriver.WSRPProducerAdminPortlet";

    @Override
    protected void setUp() throws Exception {

        PortletWindowPreferenceRegistryContextImpl prePortletWindowPreferenceRegistry = new PortletWindowPreferenceRegistryContextImpl();
        PortletWindowPreferenceRegistryContextDBImpl prePortletWindowPreferenceDBRegistry = new PortletWindowPreferenceRegistryContextDBImpl();

        // remove all predefined preferences for env preparement
        prePortletWindowPreferenceRegistry.removePreferences(portletName);
        prePortletWindowPreferenceDBRegistry.removePreferences(portletName);

        // prepare test env

        // add preferences to default user for a specified portlet name

        // for prePortletWindowPreferenceRegistry
        List portletWindowPreferenceElementList = new ArrayList();
        String registryLocation = PortletRegistryHelper.getRegistryLocation();
        PortletRegistryWriter portletWindowPreferenceRegistryWriter = new PortletWindowPreferenceRegistryWriter(
                registryLocation);

        // for testportlet and default user
        Map readOnlyPrefMap = new HashMap();
        readOnlyPrefMap.put("producerPort", "false");
        readOnlyPrefMap.put("producerContext", "false");
        readOnlyPrefMap.put("producerHost", "false");
        readOnlyPrefMap.put("isLocal", "false");
        Map defaultPrefMap = new HashMap();
        defaultPrefMap.put("producerPort", "|8080");
        defaultPrefMap.put("producerContext", "|producer");
        defaultPrefMap.put("producerHost", "|localhost");
        defaultPrefMap.put("isLocal", "|true");
        
        
        PortletRegistryElement portletWindowPreference = new PortletWindowPreference();
        portletWindowPreference.setPortletName(portletName);
        // portlet window name for default user is equal to portlet name
        portletWindowPreference.setName(portletName);
        portletWindowPreference.setUserName(defaultUserName);
        portletWindowPreference.setCollectionProperty(
                PortletRegistryTags.PREFERENCE_READ_ONLY_KEY, readOnlyPrefMap);
        portletWindowPreference.setCollectionProperty(
                PortletRegistryTags.PREFERENCE_PROPERTIES_KEY, defaultPrefMap);
        portletWindowPreferenceElementList.add(portletWindowPreference);

        // for testwindow and defaultuser
        PortletRegistryElement portletWindowPreference2 = new PortletWindowPreference();
        portletWindowPreference2.setPortletName(portletName);
        portletWindowPreference2.setName(portletWindowName);
        portletWindowPreference2.setUserName(defaultUserName);
        portletWindowPreference2.setCollectionProperty(
                PortletRegistryTags.PREFERENCE_PROPERTIES_KEY, defaultPrefMap);
        portletWindowPreferenceElementList.add(portletWindowPreference2);

        // for testwindow and testuser
        PortletRegistryElement portletWindowPreference3 = new PortletWindowPreference();
        portletWindowPreference3.setPortletName(portletName);
        portletWindowPreference3.setName(portletWindowName);
        portletWindowPreference3.setUserName(userName);
        portletWindowPreference3.setCollectionProperty(
                PortletRegistryTags.PREFERENCE_PROPERTIES_KEY, defaultPrefMap);
        portletWindowPreferenceElementList.add(portletWindowPreference3);
        portletWindowPreferenceRegistryWriter
                .writeDocument(portletWindowPreferenceElementList);

        // for prePortletWindowPreferenceDBRegistry
        List portletWindowPreferenceList = new ArrayList();
        
        
		PortletUserWindow portletWindowPreferenceDB = new PortletUserWindow();
		portletWindowPreferenceDB.setPortletName(portletName);
		// portlet window name for default user is equal to portlet name
		portletWindowPreferenceDB.setWindowName(portletName);
		portletWindowPreferenceDB.setUserName(defaultUserName);
		PortletRegistryUtils.setCollectionProperty(portletWindowPreferenceDB,
				PortletRegistryTags.PREFERENCE_READ_ONLY_KEY, readOnlyPrefMap);
		PortletRegistryUtils.setCollectionProperty(portletWindowPreferenceDB,
				PortletRegistryTags.PREFERENCE_PROPERTIES_KEY, defaultPrefMap);
		portletWindowPreferenceList.add(portletWindowPreferenceDB);

		PortletUserWindow portletWindowPreferenceDB2 = new PortletUserWindow();
		portletWindowPreferenceDB2.setPortletName(portletName);
		// portlet window name for default user is equal to portlet name
		portletWindowPreferenceDB2.setWindowName(portletWindowName);
		portletWindowPreferenceDB2.setUserName(defaultUserName);
		PortletRegistryUtils.setCollectionProperty(portletWindowPreferenceDB2,
				PortletRegistryTags.PREFERENCE_PROPERTIES_KEY, defaultPrefMap);
		portletWindowPreferenceList.add(portletWindowPreferenceDB2);

		PortletUserWindow portletWindowPreferenceDB3 = new PortletUserWindow();
		portletWindowPreferenceDB3.setPortletName(portletName);
		// portlet window name for default user is equal to portlet name
		portletWindowPreferenceDB3.setWindowName(portletWindowName);
		portletWindowPreferenceDB3.setUserName(userName);
		PortletRegistryUtils.setCollectionProperty(portletWindowPreferenceDB3,
				PortletRegistryTags.PREFERENCE_PROPERTIES_KEY, defaultPrefMap);
		portletWindowPreferenceList.add(portletWindowPreferenceDB2);
		
		PortletWindowPreferenceRegistryDao windowPreferenceRegistryDao = new PortletWindowPreferenceRegistryDao();
        windowPreferenceRegistryDao.addPortletWindowPreferences(portletWindowPreferenceList);

        portletWindowPreferenceRegistry = new PortletWindowPreferenceRegistryContextImpl();
        portletWindowPreferenceDBRegistry = new PortletWindowPreferenceRegistryContextDBImpl();

        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetPreferencesReadOnly() throws Exception {
        Map value = portletWindowPreferenceRegistry.getPreferencesReadOnly(
                portletName, defaultUserName);
        Map dbvalue = portletWindowPreferenceDBRegistry.getPreferencesReadOnly(
                portletName, defaultUserName);
        assertEquals(value, dbvalue);
    }

    public void testGetPreferences() throws Exception {
        Map value = portletWindowPreferenceRegistry.getPreferences(portletName,
                userName);
        Map dbvalue = portletWindowPreferenceDBRegistry.getPreferences(
                portletName, userName);
        assertEquals(value, dbvalue);
    }

    public void testSavePreferences() throws Exception {
        // add preferences to specified portlet window and user portlet
        Map prefMap = new HashMap();
        prefMap.put("producerPort", "7002");
        prefMap.put("producerContext", "portletdriver");
        prefMap.put("producerHost", "localhost");
        prefMap.put("isLocal", "false");
        portletWindowPreferenceRegistry.savePreferences(portletName,
                portletWindowName, userName, prefMap, false);
        portletWindowPreferenceDBRegistry.savePreferences(portletName,
                portletWindowName, userName, prefMap, false);

        // test equal
        Map value = portletWindowPreferenceRegistry.getPreferences(
                portletWindowName, userName);
        Map dbvalue = portletWindowPreferenceDBRegistry.getPreferences(
                portletWindowName, userName);
        assertEquals(value, dbvalue);
    }

    public void testRemovePreferences() throws Exception {
        // remove
        portletWindowPreferenceRegistry.removePreferences(portletName);
        portletWindowPreferenceDBRegistry.removePreferences(portletName);

        // after remove
        try {
            portletWindowPreferenceRegistry.getPreferences(portletName,
                    userName);
        } catch (PortletRegistryException e) {
            assertTrue(e.getMessage().indexOf("does not exist") > 0);
        }
        try {
            portletWindowPreferenceDBRegistry.getPreferences(portletName,
                    userName);
        } catch (PortletRegistryException e) {
            assertTrue(e.getMessage().indexOf("does not exist") > 0);
        }
    }

}
