package com.sun.portal.portletcontainer.admin.registry.database.dao;

import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import com.sun.portal.container.EntityID;
import com.sun.portal.container.PortletLang;
import com.sun.portal.container.PortletType;
import com.sun.portal.portletcontainer.admin.registry.PortletWindowRegistryContextImpl;
import com.sun.portal.portletcontainer.admin.registry.database.PortletWindowRegistryContextDBImpl;

public class PortletWindowRegistryTest extends TestCase {
	private PortletWindowRegistryContextImpl portletWindowRegistry = null;
	private PortletWindowRegistryContextDBImpl portletWindowDBRegistry = null;
	
	private String portletWindowName ="portletdriver.WSRPProducerAdminPortlet";
	private String portletName = "portletdriver.WSRPProducerAdminPortlet";
	
	@Override
	protected void setUp() throws Exception {
		portletWindowRegistry = new PortletWindowRegistryContextImpl();
		portletWindowDBRegistry = new PortletWindowRegistryContextDBImpl();
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}
	
	@SuppressWarnings("unchecked")
	public void testGetAllPortletWindows() throws Exception {
		// local
		List<String> dbListLocal = portletWindowDBRegistry.getAllPortletWindows(PortletType.LOCAL);
		List listLocal = portletWindowRegistry.getAllPortletWindows(PortletType.LOCAL);
		if(dbListLocal!=null)Collections.sort(dbListLocal);
		if(listLocal!=null)Collections.sort(listLocal);
		assertEquals(dbListLocal, listLocal);
		
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
	
	public void testIsVisible() throws Exception {
		boolean dbvalue = portletWindowDBRegistry.isVisible(portletWindowName);
		boolean value = portletWindowRegistry.isVisible(portletWindowName);
		assertEquals(dbvalue, value);
	}
	
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
	
	public void testGetEntityId() throws Exception {
		EntityID dbEntityID = portletWindowDBRegistry.getEntityId(portletWindowName);
		EntityID entityID = portletWindowRegistry.getEntityId(portletWindowName);
		assertEquals(dbEntityID, entityID);
	}
	
	public void testGetPortletWindowTitle() throws Exception {
		String dbtitle = portletWindowDBRegistry.getPortletWindowTitle(portletWindowName);
		String title = portletWindowRegistry.getPortletWindowTitle(portletWindowName);
		assertEquals(dbtitle, title);
	}
	
	@SuppressWarnings("unchecked")
	public void testGetPortletWindows() throws Exception {
		List<String> dblist = portletWindowDBRegistry.getPortletWindows(portletName);
		List list = portletWindowRegistry.getPortletWindows(portletName);
		if(dblist!=null)Collections.sort(dblist);
		if(list!=null)Collections.sort(list);
		assertEquals(dblist, list);
	}
	
	public void testGetPortletID() throws Exception {
		String dbportletid = portletWindowDBRegistry.getPortletID(portletWindowName);
		String portletid = portletWindowRegistry.getPortletID(portletWindowName);
		assertEquals(dbportletid, portletid);
	}
	
	public void testGetConsumerID() throws Exception {
		String dbconsumerid = portletWindowDBRegistry.getConsumerID(portletWindowName);
		String consumerid = portletWindowRegistry.getConsumerID(portletWindowName);
		assertEquals(dbconsumerid, consumerid);
	}
	
	public void testGetPortletLang() throws Exception {
		PortletLang dblang = portletWindowDBRegistry.getPortletLang(portletWindowName);
		PortletLang lang = portletWindowRegistry.getPortletLang(portletWindowName);
		assertEquals(dblang, lang);
	}
	
	public void testGetPortletName() throws Exception {
		String dbPortletName = portletWindowDBRegistry.getPortletName(portletWindowName);
		String portletName = portletWindowRegistry.getPortletName(portletWindowName);
		assertEquals(dbPortletName, portletName);
	}
	
	public void testGetProducerEntityID() throws Exception {
		String dbid = portletWindowDBRegistry.getProducerEntityID(portletWindowName);
		String id = portletWindowRegistry.getProducerEntityID(portletWindowName);
		assertEquals(dbid, id);
	}
	
	public void testGetRemotePortletWindows() throws Exception {
		List<String> dbremote = portletWindowDBRegistry.getRemotePortletWindows();
		List<String> remote = portletWindowRegistry.getRemotePortletWindows();
		if(dbremote!=null)Collections.sort(dbremote);
		if(remote!=null)Collections.sort(remote);
		assertEquals(dbremote, remote);		
	}
	
	public void testGetRowNumber() throws Exception {
		Integer dbrow = portletWindowDBRegistry.getRowNumber(portletWindowName);
		Integer row = portletWindowRegistry.getRowNumber(portletWindowName);
		assertEquals(dbrow, row);
	}
	
	public void testGetWidth() throws Exception {
		String dbwidth = portletWindowDBRegistry.getWidth(portletWindowName);
		String width = portletWindowRegistry.getWidth(portletWindowName);
		assertEquals(dbwidth, width);
	}
	
	public void testIsRemote() throws Exception {
		boolean dbremote = portletWindowDBRegistry.isRemote(portletWindowName);
		boolean remote = portletWindowRegistry.isRemote(portletWindowName);
		assertEquals(dbremote, remote);
	}

	public void testCreatePortletWindow() throws Exception {
		portletWindowDBRegistry.createPortletWindow(portletName, "testWin", "testfe", null);
//		portletWindowRegistry.createPortletWindow(portletName, "testWin", null, null);
	}
	
	public void testRemovePortletWindow() throws Exception {
		portletWindowDBRegistry.removePortletWindow("testWin");
	}
	
	public void testRemovePortletWindows() throws Exception {
//		portletWindowDBRegistry.removePortletWindows(portletName);
	}
	
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
	
	public void testShowPortletWindow() throws Exception {
		boolean dbvisible = portletWindowDBRegistry.isVisible(portletWindowName);
		portletWindowDBRegistry.showPortletWindow(portletWindowName, !dbvisible);
		assertEquals(false, dbvisible == portletWindowDBRegistry.isVisible(portletWindowName));
		portletWindowDBRegistry.showPortletWindow(portletWindowName, dbvisible);				
	}
	
}
