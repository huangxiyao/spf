package com.sun.portal.portletcontainer.admin.registry.database.dao;

import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletWindow;

public class PortletWindowRegistryDao {
	private static Logger LOG = Logger.getLogger(PortletWindowRegistryDao.class.toString());
	private EntityManagerFactory emFactory;
	
	public PortletWindowRegistryDao() {
		emFactory = EntityManagerFactoryManager.getInstance().getFactory();;
	}	
		
	/**
	 * add the specified portlet window object
	 * @param portletWindowName
	 *                   portlet window name
	 */
	public void addPortletWindow(PortletWindow portletWindow) {
		EntityManager em = emFactory.createEntityManager();
				
		EntityTransaction tran = em.getTransaction();
		try {
			tran.begin();			
			// persist the portlet window to persistence layer
			em.persist(portletWindow);
			// persist the portlet windows into the database
			tran.commit();
		} catch (Exception ex) {
			if(tran.isActive()) tran.rollback();
			LOG.log(Level.WARNING, "add portletWindow error, portletWindowName: " + portletWindow.getName(), ex);			
			throw new RuntimeException(ex);
		} finally {			
			if (em!=null) em.close();
		}	
	}
	
	/**
	 * add  portlet window list
	 * @param portletWindows
	 *                   portlet window list
	 */
	public void addPortletWindows(List<PortletWindow> portletWindows) {
		EntityManager em = emFactory.createEntityManager();
				
		EntityTransaction tran = em.getTransaction();
		try {
			tran.begin();			
			for (PortletWindow portletWindow : portletWindows) {
				em.persist(portletWindow);
			}
			// persist the portlet windows into the database
			tran.commit();
		} catch (Exception ex) {
			if(tran.isActive()) tran.rollback();
			LOG.log(Level.WARNING, "add portletWindows error.", ex);			
			throw new RuntimeException(ex);
		} finally {			
			if (em!=null) em.close();
		}	
	}
	
	@SuppressWarnings("unchecked")
	public List<PortletWindow> getAllPortletWindows() {
		EntityManager em = emFactory.createEntityManager();
		
		String sql = "select x"
				   + "  from PortletWindow x";
		LOG.fine("JPA SQL: " + sql);
		
		try {
			Query query = em.createQuery(sql);
			List<PortletWindow> list = query.getResultList();
			return list;
		} catch (Exception ex) {
			LOG.log(Level.WARNING, "get all portletWindow error.", ex);
		} finally {		
			em.close();
		}
		return null;
	}
	
	/**
	 * get PortletWindow object accroding to window name
	 * @param portletWindowName
	 * @return
	 *        portletwindow object
	 */
	public PortletWindow getPortletWindow(String portletWindowName) {
		EntityManager em = emFactory.createEntityManager();
		
		String sql = "select x"
				   + "  from PortletWindow x"
				   + " where x.name = :name";
		LOG.fine("JPA SQL: " + sql);
		
		try {
			Query query = em.createQuery(sql);
			query.setParameter("name", portletWindowName);
			Object obj = query.getSingleResult();
			PortletWindow portletWindow = null;
			if (obj != null) {
				portletWindow = (PortletWindow)obj;
			}	
			return portletWindow;
		} catch (Exception ex) {
			LOG.log(Level.WARNING, "get portletWindow error, portletWindowName: " + portletWindowName, ex);			
		} finally {
			em.close();
		}		
		return null;
	}
	
	/**
	 * get PortletWindow object accroding to portlet name
	 * @param portletName
	 * @return
	 *        portletwindow object
	 */
	@SuppressWarnings("unchecked")
	public List<PortletWindow> getPortletWindowsByPortletName(String portletName) {
		EntityManager em = emFactory.createEntityManager();
		
		String sql = "select x"
				   + "  from PortletWindow x"
				   + " where x.portletName = :portletName";
		LOG.fine("JPA SQL: " + sql);
		
		try {
			Query query = em.createQuery(sql);
			query.setParameter("portletName", portletName);
			List<PortletWindow> portletWindows = query.getResultList();
			return portletWindows;
		} catch (Exception ex) {
			LOG.log(Level.WARNING, "get portletWindows error, portletName: " + portletName, ex);
		} finally {
			em.close();
		}
		return null;
	}
	
	
	/**
	 * remove specified PortletWindow and all its related properties
	 * @param portletWindowName
	 *               portlet window name
	 */
	public void removePortletWindow(String portletWindowName) {
		EntityManager em = emFactory.createEntityManager();
		
		// retrieve portlet window according to the portlet window name
		PortletWindow portletWindow = this.getPortletWindow(portletWindowName);
		if (portletWindow == null) {
			return;
		}
		
		EntityTransaction tran = em.getTransaction();
		
		// the entity here is retrieved by a closed entity manager, it should be
		// merged to the persistence layer
		PortletWindow mergedPortletWindow = em.merge(portletWindow);
		
		try {
			tran.begin();			
			// remove this PortletWindow and all its related properies from 
			// persistence layer
			em.remove(mergedPortletWindow);
			// excute the sql to delete all the items in the database
			tran.commit();
		} catch (Exception ex) {
			if(tran.isActive()) tran.rollback();
			LOG.log(Level.WARNING, "delte portletWindow error, portletWindowName: " + portletWindowName, ex);			
			throw new RuntimeException(ex);
		} finally {			
			if (em!=null) em.close();
		}		
	}
	
	/**
	 * remove all portlet windows and related properties 
	 * according to the portlet name
	 * @param portletName
	 *              portlet name
	 */
	public void removePortletWindows(String portletName) {
		EntityManager em = emFactory.createEntityManager();
		List<PortletWindow> portletWindows = getPortletWindowsByPortletName(portletName);
		
		EntityTransaction tran = em.getTransaction();
		
		try {
			tran.begin();			
			for (PortletWindow portletWindow : portletWindows) {
				// remove this PortletWindow and all its related properies from 
				// persistence layer
				em.remove(em.merge(portletWindow));
			}
			// excute the sql to delete all the items in the database
			tran.commit();
		} catch (Exception ex) {
			if(tran.isActive()) tran.rollback();
			LOG.log(Level.WARNING, "delte portletWindows error, portletName: " + portletName, ex);			
			throw new RuntimeException(ex);
		} finally {			
			if (em!=null) em.close();
		}		
	}
	
	/**
	 * update the specified portlet window object
	 * @param portletWindowName
	 *                   portlet window name
	 */
	public void updatePortletWindow(PortletWindow portletWindow) {
		EntityManager em = emFactory.createEntityManager();
				
		EntityTransaction tran = em.getTransaction();
		try {
			tran.begin();			
			// merge the unmanaged object into the entity manager
			em.merge(portletWindow);
			// automatically update the object in the persistence layer if necessary
			tran.commit();
		} catch (Exception ex) {
			if(tran.isActive()) tran.rollback();
			LOG.log(Level.WARNING, "update portletWindow error, portletWindowName: " + portletWindow.getName(), ex);
			throw new RuntimeException(ex);
		} finally {			
			if (em!=null) em.close();
		}	
	}	
	
	/**
	 * retrieve max row value of all portlet window properties
	 * @return
	 *         max row number
	 */
	@SuppressWarnings("unchecked")
	public int getMaxRow() {
		int maxRow = 0;
		EntityManager em = emFactory.createEntityManager();
		
		String sql = "select max(value)"
				   + "  from portlet_window_property_meta"
				   + " where name = 'row'";
		LOG.fine("JPA Native SQL: " + sql);
	
		try {
			Query query = em.createNativeQuery(sql);
			Object obj = query.getSingleResult();
			if (obj instanceof List) {
				String value = (String)((List)obj).get(0);
				maxRow = Integer.parseInt(value.toString());
			}				
		} catch (Exception ex) {
			LOG.log(Level.WARNING, "get max row error", ex);
		} finally {
			em.close();
		}
		return maxRow;
	}
}
