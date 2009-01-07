package com.sun.portal.portletcontainer.admin.registry.database.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.sun.portal.portletcontainer.admin.database.exception.PortletRegistryDBException;
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
			if(tran.isActive()) {
				tran.rollback();
			}
			LOG.log(Level.WARNING, "add portletWindow error, portletWindowName: " + portletWindow.getName(), ex);			
			throw new PortletRegistryDBException("add portletWindow error.");
		} finally {			
			em.close();
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
			if(tran.isActive()) {
				tran.rollback();
			}
			LOG.log(Level.WARNING, "add portletWindows error.", ex);			
			throw new PortletRegistryDBException("add portletWindows error.");
		} finally {			
			em.close();
		}	
	}
	
	@SuppressWarnings("unchecked")
	public List<PortletWindow> getAllPortletWindows() {
		EntityManager em = emFactory.createEntityManager();
		
		String sql = "select x"
				   + "  from PortletWindow x";
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("JPA SQL: " + sql);
		}
		
		try {
			Query query = em.createQuery(sql);
			List<PortletWindow> list = query.getResultList();
			return list;
		} catch (Exception ex) {
			LOG.log(Level.WARNING, "get all portletWindow error.", ex);
			throw new PortletRegistryDBException("get all portletWindow error.");
		} finally {		
			em.close();
		}
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
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("JPA SQL: " + sql);
		}
		
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
			throw new PortletRegistryDBException("get portletWindow error.");
		} finally {
			em.close();
		}
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
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("JPA SQL: " + sql);
		}
		
		try {
			Query query = em.createQuery(sql);
			query.setParameter("portletName", portletName);
			List<PortletWindow> portletWindows = query.getResultList();
			return portletWindows;
		} catch (Exception ex) {
			LOG.log(Level.WARNING, "get portletWindows error, portletName: " + portletName, ex);
			throw new PortletRegistryDBException("get portletWindows error.");
		} finally {
			em.close();
		}		
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
			if(tran.isActive()) {
				tran.rollback();
			}
			LOG.log(Level.WARNING, "delte portletWindow error, portletWindowName: " + portletWindowName, ex);			
			throw new PortletRegistryDBException("delte portletWindow error.");
		} finally {			
			em.close();
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
			if(tran.isActive()) {
				tran.rollback();
			}
			LOG.log(Level.WARNING, "delte portletWindows error, portletName: " + portletName, ex);			
			throw new PortletRegistryDBException("delte portletWindows error.");
		} finally {			
			em.close();
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
			if(tran.isActive()) {
				tran.rollback();
			}
			LOG.log(Level.WARNING, "update portletWindow error, portletWindowName: " + portletWindow.getName(), ex);
			throw new PortletRegistryDBException("update portletWindow error.");
		} finally {			
			em.close();
		}	
	}	
}
