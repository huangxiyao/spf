package com.sun.portal.portletcontainer.admin.registry.database.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.sun.portal.portletcontainer.admin.registry.database.dao.EntityManagerFactoryManager;
import com.sun.portal.portletcontainer.admin.registry.database.dao.PortletAppRegistryDao;
import com.sun.portal.portletcontainer.admin.registry.database.dao.PortletDeploymentDescriptorDao;
import com.sun.portal.portletcontainer.admin.registry.database.dao.PortletWindowRegistryDao;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletApp;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletAppProperties;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletWindow;

public class DatabaseInit {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args[0].equalsIgnoreCase("create")) {
			System.out.println("Create tables...");
			generateDB();
			System.out.println("Create tables finished.");
		} else if (args[0].equalsIgnoreCase("insert")) {
			System.out.println("Init tables...");
			insertDataIntoTables();
			System.out.println("Init tables finished.");
		} else if (args[0].equalsIgnoreCase("delete")) {
			System.out.println("Clear tables...");
			clearData();
			System.out.println("Clear tables finished.");
		} else if (args[0].equalsIgnoreCase("test")) {
			test();
		}
	}
	
	public static void generateDB() {
		EntityManagerFactory emf = EntityManagerFactoryManager.getInstance().getFactory();
		if( !emf.isOpen() ){
			emf = Persistence.createEntityManagerFactory("portletregistry_datasource");
		}
		EntityManager em = emf.createEntityManager();
		em.close();
	}
	public static void insertDataIntoTables() {
		initPortletWindowTables();	
		initPortletAppTables();
		initPortletDescriptorTables();
	}
	
	public static void clearData() {
		deleteAllPortletWindowTables();
		deleteAllPortletAppTables();
	}
		
	public static void initPortletAppTables() {
		EntityManager em = EntityManagerFactoryManager.getInstance().getFactory().createEntityManager();
		EntityTransaction trans = em.getTransaction();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("lang", "");
		map1.put("name", "portletdriver.WSRPProducerAdminPortlet");
		map1.put("portletName", "portletdriver.WSRPProducerAdminPortlet");
		map1.put("remote", "");
		map1.put("version", "1.0");
		map1.put("username", "");
		map1.put("title", "WSRP Producer Admin Portlet");
		map1.put("archiveName", "portletdriver.war");
		map1.put("archiveType", "war");
		Map<String, Map<String, String>> cmap1 = new HashMap<String, Map<String, String>>();
		Map ccmap1 = new HashMap();
		ccmap1.put("text/html", "text/html");
		cmap1.put("supportedContentTypes", ccmap1);
		ccmap1 = new HashMap();
		ccmap1.put("text/html", "VIEW,EDIT,HELP");		
		cmap1.put("supportsMap", ccmap1);
		
		ccmap1 = new HashMap();
		ccmap1.put("user", "userInfo");
		cmap1.put("userInfoMap", ccmap1);
		
		
		PortletApp pa1 = createPortletApp(map1, cmap1);
		
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("lang", "");
		map2.put("name", "WelcomePortlet.WelcomePortlet");
		map2.put("portletName", "WelcomePortlet.WelcomePortlet");
		map2.put("remote", "");
		map2.put("version", "1.0");
		map2.put("username", "");
		map2.put("title", "Hello Portlet World");
		map2.put("archiveName", "WelcomePortlet.war");
		map2.put("description", "WelcomePortlet");
		map2.put("shortTitle", "Hello Portlet World");
		map2.put("archiveType", "war");
		Map<String, Map<String, String>> cmap2 = new HashMap<String, Map<String, String>>();
		Map ccmap2 = new HashMap();
		ccmap2.put("text/html", "text/html");
		cmap2.put("supportedContentTypes", ccmap2);
		ccmap2 = new HashMap();
		ccmap2.put("en-US", "WelcomePortlet");
		cmap2.put("descriptionMap", ccmap2);
		cmap2.put("displayNameMap", ccmap2);
		
		ccmap2 = new HashMap(); 
		ccmap2.put("text/html", "VIEW");
		cmap2.put("supportsMap", ccmap2);
		
		ccmap2 = new HashMap();
		ccmap2.put("role2", "EMPLOYEE_ROLE");
		cmap2.put("roleMap", ccmap2);
		
		
		PortletApp pa2 = createPortletApp(map2, cmap2);
		
		try {
			trans.begin();
			em.persist(pa1);
			em.persist(pa2);
			trans.commit();
		} catch(Exception ex) {
			trans.rollback();
			ex.printStackTrace();
		} finally {			
			em.close();
		}		
		
	}
	
	private static PortletApp createPortletApp(Map<String, String> map, Map<String, Map<String, String>> cMap) {
		PortletApp portletApp =  new PortletApp();

		portletApp.setName(map.get("name"));
		portletApp.setPortletName(map.get("portletName"));
		portletApp.setTitle(map.get("title"));
		portletApp.setArchiveName(map.get("archiveName"));
		portletApp.setDescription(map.get("description"));
		portletApp.setShortTitle(map.get("shortTitle"));
		portletApp.setArchiveType(map.get("archiveType"));


		Set<PortletAppProperties> collectionSet = portletApp.getPortletAppProperties();		
		
		for (Entry centry : (Set<Entry<String, Map<String, String>>>)cMap.entrySet()) {
			String name = (String)centry.getKey();
			
//			if (name.equals("supportsMap")) {
//				Map supportsMap = (Map)cMap.get(name);
//				for (String supportsMapElementVaule : ((String)supportsMap.get("text/html")).split("[,]")){
//					PortletAppPropertyCollection supportsMapCol = new PortletAppPropertyCollection();
//					supportsMapCol.setName(name);
//					supportsMapCol.setElementName("text/html");
//					supportsMapCol.setElementValue(supportsMapElementVaule);
//					supportsMapCol.setPortletApp(portletApp);
//					collectionSet.add(supportsMapCol);
//				}
//				break;
//			}
			PortletAppProperties collectionSetString = new PortletAppProperties();
			collectionSetString.setPropertyName(name);
			Map collectionMap = (Map)cMap.get((String)centry.getKey());
			
			for (Entry entry : (Set<Entry>)collectionMap.entrySet()) {
				collectionSetString.setSubElementName((String)entry.getKey());
				collectionSetString.setSubElementValue((String)entry.getValue());
			}
			collectionSetString.setPortletApp(portletApp);
			collectionSet.add(collectionSetString);			
		}
		
		return portletApp;
	}
	
	public static void initPortletWindowTables() {
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
	private static PortletWindow createPortletWindow(Map<String, String> map) {
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

	public static void deleteAllPortletWindowTables(){
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
	
	public static void deleteAllPortletAppTables() {
		EntityManager em = EntityManagerFactoryManager.getInstance().getFactory().createEntityManager();
		EntityTransaction trans = em.getTransaction();
		PortletAppRegistryDao dao = new PortletAppRegistryDao();
		List<PortletApp> list = dao.getAllPortlets();
		
		try {
			trans.begin();
			for (PortletApp portletApp : list) {
				em.remove(em.merge(portletApp));
			}
			trans.commit();		
		} catch(Exception ex) {
			if(trans.isActive()) trans.rollback();
			ex.printStackTrace();
		} finally {			
			em.close();
		}			
	}

	private static void initPortletDescriptorTables() {
		PortletDeploymentDescriptorDao dao = new PortletDeploymentDescriptorDao();
		dao.SaveOrUpdate("DisplayProfileRemotePortlet_portlet.xml",
				DatabaseInit.class.getClassLoader().getResourceAsStream(
						"DisplayProfileRemotePortlet_portlet.xml"));
		dao.SaveOrUpdate("portletdriver_portlet.xml", DatabaseInit.class
				.getClassLoader().getResourceAsStream(
						"portletdriver_portlet.xml"));
	}
	
	public static void test(){
		PortletWindowRegistryDao dao = new PortletWindowRegistryDao();
		PortletWindow portletWindow = dao.getPortletWindow("WelcomePortlet.WelcomePortlet");
		EntityManager em = EntityManagerFactoryManager.getInstance().getFactory().createEntityManager();
		EntityTransaction trans = em.getTransaction();
		
		String sql = "select x from PortletWindowPropertyMeta x where x.portletWindow.name = 'WelcomePortlet.WelcomePortlet'";
        
		try {
			trans.begin();
//			PortletWindow fp = em.merge(portletWindow);
			Query query = em.createQuery(sql);
//			List list = query.getResultList();
//			
//			PortletWindowPropertyMeta meta = new PortletWindowPropertyMeta();
//			meta.setId(new Long(235));
//			meta.setName("row");
//			meta.setValue("2");
//			meta.setPortletWindow(em.find(PortletWindow.class, new Long(2)));
//			em.merge(meta);
//			trans.commit();		

		} catch(Exception ex) {
			if(trans.isActive()) trans.rollback();
			ex.printStackTrace();
		} finally {			
			em.close();
		}			
	}

}
