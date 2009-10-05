package com.sun.portal.portletcontainer.admin.registry.database.dao;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.sun.portal.portletcontainer.admin.registry.PortletAppRegistryContextImpl;
import com.sun.portal.portletcontainer.admin.registry.database.PortletAppRegistryContextDBImpl;

public class PortletAppRegistryTest extends TestCase {
	private PortletAppRegistryContextImpl portletAppRegistry = null;
	private PortletAppRegistryContextDBImpl portletAppDBRegistry = null;
	
	private static String portletName = "portletdriver.WSRPProducerAdminPortlet";
		
	protected void setUp() throws Exception {
		portletAppRegistry = new PortletAppRegistryContextImpl();
		portletAppDBRegistry = new PortletAppRegistryContextDBImpl();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetAvailablePortlets() throws Exception {
		List list = portletAppRegistry.getAvailablePortlets();
		List dblist = portletAppDBRegistry.getAvailablePortlets();
		Collections.sort(dblist);
		Collections.sort(list);
		assertEquals(dblist, list);
	}
	
	
	public void testGetDescription() throws Exception {
		String str = portletAppRegistry.getDescription(portletName, "en-US");
		String dbStr = portletAppDBRegistry.getDescription(portletName, "en-US");
		assertEquals(dbStr, str);
	}
	
	public void testGetDisplayName() throws Exception {
		String str = portletAppRegistry.getDisplayName(portletName, "en-US");
		String dbStr = portletAppDBRegistry.getDisplayName(portletName, "en-US");
		assertEquals(dbStr, str);
	}
	
	public void testGetKeywords() throws Exception {
		List list = portletAppRegistry.getKeywords(portletName, "en-US");
		List dblist = portletAppDBRegistry.getKeywords(portletName, "en-US");
		if(dblist!=null) Collections.sort(dblist);
		if(list!=null) Collections.sort(list);
		assertEquals(dblist, list);
	}
	
	public void testGetRoleMap() throws Exception {
		Map dbmap = portletAppDBRegistry.getRoleMap(portletName);
		Map map = portletAppRegistry.getRoleMap(portletName);
		assertEquals(dbmap, map);
	}
	
	public void testGetMarkupTypes() throws Exception {
		List dblist = portletAppDBRegistry.getMarkupTypes(portletName);
		List list = portletAppRegistry.getMarkupTypes(portletName);	
		if(dblist!=null) Collections.sort(dblist);
		if(list!=null) Collections.sort(list);
		assertEquals(dblist, list);
	}
	
	public void testGetShortTitle() throws Exception {
		String str = portletAppRegistry.getShortTitle(portletName, "en-US");
//		String dbStr = portletAppDBRegistry.getShortTitle(portletName, "en-US");
		// OpenPortal retrieve the Title for ShortTitle
		String dbStr = portletAppDBRegistry.getTitle(portletName, "en-US");
		assertEquals(dbStr, str);
	}
	
	public void testGetTitle() throws Exception {
		String str = portletAppRegistry.getTitle(portletName, "en-US");
		String dbStr = portletAppDBRegistry.getTitle(portletName, "en-US");
		assertEquals(dbStr, str);
	}
	
	public void testGetUserInfoMap() throws Exception {
		Map dbmap = portletAppDBRegistry.getUserInfoMap(portletName);
		Map map = portletAppRegistry.getUserInfoMap(portletName);
		assertEquals(dbmap, map);
	}
	
	public void testHasEdit() throws Exception {
		boolean bool = portletAppRegistry.hasEdit(portletName);
		boolean dbbool = portletAppDBRegistry.hasEdit(portletName);
		assertEquals(dbbool, bool);
	}
	
	public void testHasHelp() throws Exception {
		boolean bool = portletAppRegistry.hasHelp(portletName);
		boolean dbbool = portletAppDBRegistry.hasHelp(portletName);
		assertEquals(dbbool, bool);
	}
	
	public void testHasView() throws Exception {
		boolean bool = portletAppRegistry.hasView(portletName);
		boolean dbbool = portletAppDBRegistry.hasView(portletName);
		assertEquals(dbbool, bool);
	}
	
	public void testRemovePortlet() throws Exception {
//		portletAppDBRegistry.removePortlet(portletName);
//		portletAppRegistry.removePortlet(portletName);
	}
}
