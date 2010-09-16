package com.sun.portal.portletcontainer.admin.registry.database.dao;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.portal.portletcontainer.admin.registry.PortletAppRegistryContextImpl;
import com.sun.portal.portletcontainer.admin.registry.database.PortletAppRegistryContextDBImpl;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletApp;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletAppProperties;
import com.sun.portal.portletcontainer.admin.registry.database.utils.DatabaseInit;


public class PortletAppRegistryTest{
	private static PortletAppRegistryContextImpl portletAppRegistry = null;
	private static PortletAppRegistryContextDBImpl portletAppDBRegistry = null;
	
	private static String portletName = "portletdriver.WSRPProducerAdminPortlet";
		
	@BeforeClass
	public static  void setUp() throws Exception {
		portletAppRegistry = new PortletAppRegistryContextImpl();
		portletAppDBRegistry = new PortletAppRegistryContextDBImpl();
		DatabaseInit.generateDB();
		DatabaseInit.insertDataIntoTables();

	}

	@AfterClass
	public static void dropTables() throws Exception{
		EntityManagerFactory emf = EntityManagerFactoryManager.getInstance().getFactory(); 
		EntityManager em = emf.createEntityManager();
		em.clear();
		em.close();
		emf.close();
	}

	@Test
	public void testGetAvailablePortlets() throws Exception {
		
		List list = portletAppRegistry.getAvailablePortlets();
		List dblist = portletAppDBRegistry.getAvailablePortlets();
		Collections.sort(dblist);
		Collections.sort(list);
		assertEquals(dblist, list);
	}
	
	
	@Test
	public void testGetDescription() throws Exception {
		String str = portletAppRegistry.getDescription(portletName, "en-US");
		String dbStr = portletAppDBRegistry.getDescription(portletName, "en-US");
		assertEquals(dbStr, str);
	}
	
	@Test
	public void testGetDisplayName() throws Exception {
		String str = portletAppRegistry.getDisplayName(portletName, "en-US");
		String dbStr = portletAppDBRegistry.getDisplayName(portletName, "en-US");
		assertEquals(dbStr, str);
	}
	
	@Test
	public void testGetKeywords() throws Exception {
		List list = portletAppRegistry.getKeywords(portletName, "en-US");
		List dblist = portletAppDBRegistry.getKeywords(portletName, "en-US");
		if(dblist!=null) Collections.sort(dblist);
		if(list!=null) Collections.sort(list);
		assertEquals(dblist, list);
	}
	
	@Test
	public void testGetRoleMap() throws Exception {
		String portletToFind = "WelcomePortlet.WelcomePortlet";
		
		Map dbmap = portletAppDBRegistry.getRoleMap(portletToFind);
		Map map = portletAppRegistry.getRoleMap(portletToFind);
		assertEquals(dbmap, map);
	}
	
	@Test
	public void testGetMarkupTypes() throws Exception {
		List dblist = portletAppDBRegistry.getMarkupTypes(portletName);
		List list = portletAppRegistry.getMarkupTypes(portletName);	
		if(dblist!=null) Collections.sort(dblist);
		if(list!=null) Collections.sort(list);
		assertEquals(dblist, list);
	}
	
	@Test
	public void testGetShortTitle() throws Exception {
		String str = portletAppRegistry.getShortTitle(portletName, "en-US");
//		String dbStr = portletAppDBRegistry.getShortTitle(portletName, "en-US");
		// OpenPortal retrieve the Title for ShortTitle
		String dbStr = portletAppDBRegistry.getTitle(portletName, "en-US");
		assertEquals(dbStr, str);
	}
	
	@Test
	public void testGetTitle() throws Exception {
		String str = portletAppRegistry.getTitle(portletName, "en-US");
		String dbStr = portletAppDBRegistry.getTitle(portletName, "en-US");
		assertEquals(dbStr, str);
	}
	
	@Test
	public void testGetUserInfoMap() throws Exception {
		Map dbmap = portletAppDBRegistry.getUserInfoMap(portletName);
		Map map = portletAppRegistry.getUserInfoMap(portletName);
		assertEquals(dbmap, map);
	}
	
	@Test
	public void testHasEdit() throws Exception {
		boolean bool = portletAppRegistry.hasEdit(portletName);
		boolean dbbool = portletAppDBRegistry.hasEdit(portletName);
		assertEquals(dbbool, bool);
	}
	
	@Test
	public void testHasHelp() throws Exception {
		boolean bool = portletAppRegistry.hasHelp(portletName);
		boolean dbbool = portletAppDBRegistry.hasHelp(portletName);
		assertEquals(dbbool, bool);
	}
	
	@Test
	public void testHasView() throws Exception {
		boolean bool = portletAppRegistry.hasView(portletName);
		boolean dbbool = portletAppDBRegistry.hasView(portletName);
		assertEquals(dbbool, bool);
	}
	
	@Test
	public void testRemovePortlet() throws Exception {
//		portletAppDBRegistry.removePortlet(portletName);
//		portletAppRegistry.removePortlet(portletName);
	}

}
