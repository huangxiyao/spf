package com.sun.portal.portletcontainer.admin.registry.database.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.portal.container.EntityID;
import com.sun.portal.container.PortletLang;
import com.sun.portal.container.PortletType;
import com.sun.portal.portletcontainer.admin.registry.PortletWindowRegistryContextImpl;
import com.sun.portal.portletcontainer.admin.registry.database.PortletWindowRegistryContextDBImpl;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletWindow;
import com.sun.portal.portletcontainer.admin.registry.database.utils.DatabaseInit;


public class PortletWindowRegistryTest {
	private static PortletWindowRegistryContextImpl portletWindowRegistry = null;
	private static PortletWindowRegistryContextDBImpl portletWindowDBRegistry = null;
	
	private String portletWindowName ="portletdriver.WSRPProducerAdminPortlet";
	private String portletName = "portletdriver.WSRPProducerAdminPortlet";
	
	@BeforeClass
	public static void initDatabase() throws Exception{
		portletWindowRegistry = new PortletWindowRegistryContextImpl();
		portletWindowDBRegistry = new PortletWindowRegistryContextDBImpl();
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
	@SuppressWarnings("unchecked")
	public void testGetAllPortletWindows() throws Exception {
		
		// local
		List<String> dbListLocal = portletWindowDBRegistry.getAllPortletWindows(PortletType.LOCAL);
		List listLocal = portletWindowRegistry.getAllPortletWindows(PortletType.LOCAL);
		if(dbListLocal!=null)Collections.sort(dbListLocal);
		if(listLocal!=null)Collections.sort(listLocal);
		assertEquals(listLocal, dbListLocal);
		
		List<String> dbListRemote = portletWindowDBRegistry.getAllPortletWindows(PortletType.REMOTE);
		List listRemote = portletWindowRegistry.getAllPortletWindows(PortletType.REMOTE);
		if(dbListRemote!=null)Collections.sort(dbListRemote);
		if(listRemote!=null)Collections.sort(listRemote);
		assertEquals(true, dbListRemote.equals(listRemote));
		
		List<String> dbListAll = portletWindowDBRegistry.getAllPortletWindows(PortletType.ALL);
		List listAll = portletWindowRegistry.getAllPortletWindows(PortletType.ALL);
		if(dbListAll!=null)Collections.sort(dbListAll);
		if(listAll!=null)Collections.sort(listAll);
		assertEquals(dbListAll, listAll);		
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testGetVisiblePortletWindows() throws Exception {
		// local
		List<String> dbListLocal = portletWindowDBRegistry.getVisiblePortletWindows(PortletType.LOCAL);
		List listLocal = portletWindowRegistry.getVisiblePortletWindows(PortletType.LOCAL);
		if(dbListLocal!=null)Collections.sort(dbListLocal);
		if(listLocal!=null)Collections.sort(listLocal);
		assertEquals(true, dbListLocal.equals(listLocal));
		
		List<String> dbListRemote = portletWindowDBRegistry.getVisiblePortletWindows(PortletType.REMOTE);
		List listRemote = portletWindowRegistry.getVisiblePortletWindows(PortletType.REMOTE);
		if(dbListRemote!=null)Collections.sort(dbListRemote);
		if(listRemote!=null)Collections.sort(listRemote);
		assertEquals(true, dbListRemote.equals(listRemote));
		
		List<String> dbListAll = portletWindowDBRegistry.getVisiblePortletWindows(PortletType.ALL);
		List listAll = portletWindowRegistry.getVisiblePortletWindows(PortletType.ALL);
		if(dbListAll!=null)Collections.sort(dbListAll);
		if(listAll!=null)Collections.sort(listAll);
		assertEquals(dbListAll, listAll);		
	}
	
	@Test
	public void testIsVisible() throws Exception {
		boolean dbvalue = portletWindowDBRegistry.isVisible(portletWindowName);
		boolean value = portletWindowRegistry.isVisible(portletWindowName);
		assertEquals(dbvalue, value);
	}

	@Test
	public void testGetEntityIds() throws Exception {
		List<EntityID> dblist = portletWindowDBRegistry.getEntityIds();
		List<EntityID> list = portletWindowRegistry.getEntityIds();
		if(dblist == null) assertTrue(false);
		boolean tag = true;
		
		for (Object id : dblist.toArray()) {
			if(!list.contains(id)) {
				tag = false; 
				break;
			}
		}
		if(tag) {
			assertTrue(true);
		} else {
			assertTrue(false);
		}
	}
	
	@Test
	public void testGetEntityId() throws Exception {
		EntityID dbEntityID = portletWindowDBRegistry.getEntityId(portletWindowName);
		EntityID entityID = portletWindowRegistry.getEntityId(portletWindowName);
		assertEquals(dbEntityID, entityID);
	}
	
	@Test
	public void testGetPortletWindowTitle() throws Exception {
		String dbtitle = portletWindowDBRegistry.getPortletWindowTitle(portletWindowName);
		String title = portletWindowRegistry.getPortletWindowTitle(portletWindowName);
		assertEquals(dbtitle, title);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testGetPortletWindows() throws Exception {
		List<String> dblist = portletWindowDBRegistry.getPortletWindows(portletName);
		List list = portletWindowRegistry.getPortletWindows(portletName);
		if(dblist!=null)Collections.sort(dblist);
		if(list!=null)Collections.sort(list);
		assertEquals(dblist, list);
	}
	
	@Test
	public void testGetPortletID() throws Exception {
		String dbportletid = portletWindowDBRegistry.getPortletID(portletWindowName);
		String portletid = portletWindowRegistry.getPortletID(portletWindowName);
		assertEquals(dbportletid, portletid);
	}
	
	@Test
	public void testGetConsumerID() throws Exception {
		String dbconsumerid = portletWindowDBRegistry.getConsumerID(portletWindowName);
		String consumerid = portletWindowRegistry.getConsumerID(portletWindowName);
		assertEquals(dbconsumerid, consumerid);
	}
	
	@Test
	public void testGetPortletLang() throws Exception {
		PortletLang dblang = portletWindowDBRegistry.getPortletLang(portletWindowName);
		PortletLang lang = portletWindowRegistry.getPortletLang(portletWindowName);
		assertEquals(dblang, lang);
	}
	
	@Test
	public void testGetPortletName() throws Exception {
		String dbPortletName = portletWindowDBRegistry.getPortletName(portletWindowName);
		String portletName = portletWindowRegistry.getPortletName(portletWindowName);
		assertEquals(dbPortletName, portletName);
	}
	
	@Test
	public void testGetProducerEntityID() throws Exception {
		String dbid = portletWindowDBRegistry.getProducerEntityID(portletWindowName);
		String id = portletWindowRegistry.getProducerEntityID(portletWindowName);
		assertEquals(dbid, id);
	}
	
	@Test
	public void testGetRemotePortletWindows() throws Exception {
		List<String> dbremote = portletWindowDBRegistry.getRemotePortletWindows();
		List<String> remote = portletWindowRegistry.getRemotePortletWindows();
		if(dbremote!=null)Collections.sort(dbremote);
		if(remote!=null)Collections.sort(remote);
		assertEquals(dbremote, remote);		
	}
	
	@Test
	public void testGetRowNumber() throws Exception {
		Integer dbrow = portletWindowDBRegistry.getRowNumber(portletWindowName);
		Integer row = portletWindowRegistry.getRowNumber(portletWindowName);
		assertEquals(dbrow, row);
	}
	
	@Test
	public void testGetWidth() throws Exception {
		String dbwidth = portletWindowDBRegistry.getWidth(portletWindowName);
		String width = portletWindowRegistry.getWidth(portletWindowName);
		assertEquals(dbwidth, width);
	}
	
	@Test
	public void testIsRemote() throws Exception {
		boolean dbremote = portletWindowDBRegistry.isRemote(portletWindowName);
		boolean remote = portletWindowRegistry.isRemote(portletWindowName);
		assertEquals(dbremote, remote);
	}

	@Test
	public void testCreatePortletWindow() throws Exception {
		portletWindowDBRegistry.createPortletWindow(portletName, "testWin", "testfe", null);
//		portletWindowRegistry.createPortletWindow(portletName, "testWin", null, null);
	}
	
	@Test
	public void testRemovePortletWindow() throws Exception {
		portletWindowDBRegistry.removePortletWindow("testWin");
	}
	
	@Test
	public void testRemovePortletWindows() throws Exception {
//		portletWindowDBRegistry.removePortletWindows(portletName);
	}
	
	@Test
	public void testSetPortletWindowTitle() throws Exception {
		String orginal_title = portletWindowDBRegistry.getPortletWindowTitle(portletWindowName);
		String title = "PORTLET TITLE";
		// set title value and retrieve it again to see if the update is success
		portletWindowDBRegistry.setPortletWindowTitle(portletWindowName, title);
		String changed_title = portletWindowDBRegistry.getPortletWindowTitle(portletWindowName);
		assertEquals(title, changed_title);
		// recover title value
		portletWindowDBRegistry.setPortletWindowTitle(portletWindowName, orginal_title);
	}
	
	@Test
	public void testSetWidth() throws Exception {
		String orginal_width = portletWindowDBRegistry.getWidth(portletWindowName);
		String width = "PORTLET WIDTH";
		// set wisth value and retrieve it again to see if the update is success
		portletWindowDBRegistry.setWidth(portletWindowName, width);
		String changed_width = portletWindowDBRegistry.getWidth(portletWindowName);
		assertEquals(width, changed_width);
		// recover width value
		portletWindowDBRegistry.setWidth(portletWindowName, orginal_width);
	}
	
	@Test
	public void testShowPortletWindow() throws Exception {
		boolean dbvisible = portletWindowDBRegistry.isVisible(portletWindowName);
		portletWindowDBRegistry.showPortletWindow(portletWindowName, !dbvisible);
		assertEquals(false, dbvisible == portletWindowDBRegistry.isVisible(portletWindowName));
		portletWindowDBRegistry.showPortletWindow(portletWindowName, dbvisible);				
	}
	
	private static void initPortletWindowTables() {
		EntityManager em = EntityManagerFactoryManager.getInstance().getFactory().createEntityManager();
		EntityTransaction trans = em.getTransaction();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("lang", "JAVA");
		map1.put("name", "portletdriver.WSRPProducerAdminPortlet");
		map1.put("portletName", "portletdriver.WSRPProducerAdminPortlet");
		map1.put("remote", "false");
		map1.put("version", "1.0");
		map1.put("username", "");
		map1.put("row", "1");
		map1.put("width", "thick");
		map1.put("title", "WSRP Producer Admin Portlet");
		map1.put("visible", "true");
		map1.put("entityIDPrefix", "portletdriver|WSRPProducerAdminPortlet");
		PortletWindow pw1 = createPortletWindow(map1);
		
		map1.put("lang", "JAVA");
		map1.put("name", "WelcomePortlet.WelcomePortlet");
		map1.put("portletName", "WelcomePortlet.WelcomePortlet");
		map1.put("remote", "false");
		map1.put("version", "1.0");
		map1.put("username", "");
		map1.put("row", "2");
		map1.put("width", "thick");
		map1.put("title", "Hello Portlet World");
		map1.put("visible", "true");
		map1.put("entityIDPrefix", "WelcomePortlet|WelcomePortlet");
		PortletWindow pw2 = createPortletWindow(map1);
		
		try {
			trans.begin();
			em.persist(pw1);
			em.persist(pw2);
			trans.commit();
		} catch(Exception ex) {
			trans.rollback();
			ex.printStackTrace();
		} finally {			
			em.close();
		}		
	}
	/**
	 * create portlet window from map value
	 * @param map
	 * @return
	 *        portlet window
	 */
	private static  PortletWindow createPortletWindow(Map<String, String> map) {
		// create PortletWindow
		PortletWindow pw = new PortletWindow();
		pw.setLang(map.get("lang"));
		pw.setName(map.get("name"));
		pw.setPortletName(map.get("portletName"));
		pw.setRemote(map.get("remote"));
		pw.setWidth(map.get("width"));
		pw.setTitle(map.get("title"));
		pw.setVisible(map.get("visible"));
		pw.setEntityIDPrefix(map.get("entityIDPrefix"));

		return pw;
	}
	
	private static void deleteAllPortletWindowTables(){
		EntityManager em = EntityManagerFactoryManager.getInstance().getFactory().createEntityManager();
		EntityTransaction trans = em.getTransaction();
		PortletWindowRegistryDao dao = new PortletWindowRegistryDao();
		List<PortletWindow> list = dao.getAllPortletWindows();
		
		try {
			trans.begin();
			for (PortletWindow win : list) {
				em.remove(em.merge(win));
			}
			trans.commit();		
		} catch(Exception ex) {
			if(trans.isActive()) trans.rollback();
			ex.printStackTrace();
		} finally {			
			em.close();
		}			
	}	

}
