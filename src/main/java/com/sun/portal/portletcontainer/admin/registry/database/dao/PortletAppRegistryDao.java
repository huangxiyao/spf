package com.sun.portal.portletcontainer.admin.registry.database.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletApp;

public class PortletAppRegistryDao {
	private static Logger LOG = Logger.getLogger(PortletAppRegistryDao.class.toString());
	
	private EntityManagerFactory emFactory;
	
	public PortletAppRegistryDao() {
		emFactory = EntityManagerFactoryManager.getInstance().getFactory();;
	}
	
	@SuppressWarnings("unchecked")
	public List<PortletApp> getAllPortlets() {
		EntityManager em = emFactory.createEntityManager();
		
		String sql = "select x"
				   + "  from PortletApp x";
		LOG.fine("JPA SQL: " + sql);
		
		try {
			Query query = em.createQuery(sql);
			List<PortletApp> list = query.getResultList();
			return list;
		} catch (Exception ex) {
			LOG.log(Level.WARNING, "get all portlet error", ex);			
		} finally {		
			em.close();
		}
		return null;
	}
	
	/**
	 * retrieve portlet by portlet name
	 * @param portletName
	 * @return
	 *         portletApp object
	 */
	public PortletApp getPortlet(String portletName) {
		EntityManager em = emFactory.createEntityManager();
		
		String sql = "select x"
				   + "  from PortletApp x" 
				   + " where x.portletName = :portletName";
		LOG.fine("JPA SQL: " + sql);
		
		try {
			Query query = em.createQuery(sql);
			query.setParameter("portletName", portletName);
			PortletApp portletApp = (PortletApp)query.getSingleResult();
			return portletApp;
		} catch (Exception ex) {
			LOG.log(Level.WARNING, "get portlet error", ex);
		} finally {		
			em.close();
		}
		return null;
	}
	
	public void removePortlet(String portletName) {
		EntityManager em = emFactory.createEntityManager();
		
		// retrieve portlet app according to the portlet name
		PortletApp portletApp = this.getPortlet(portletName);
		if (portletApp == null) {
			return;
		}
		
		EntityTransaction tran = em.getTransaction();
		
		// the entity here is retrieved by a closed entity manager, it should be
		// merged to the persistence layer
		PortletApp mergedPortletApp = em.merge(portletApp);
		
		try {
			tran.begin();			
			// remove this PortletWindow and all its related properies from 
			// persistence layer
			em.remove(mergedPortletApp);
			// excute the sql to delete all the items in the database
			tran.commit();
		} catch (Exception ex) {
			if(tran.isActive()) tran.rollback();
			LOG.log(Level.WARNING, "delte portlet error, portletName: " + portletName, ex);		
			throw new RuntimeException(ex);
		} finally {			
			if (em!=null) em.close();
		}	
	}
	
	public void addPortlets(List<PortletApp> portlets) {
		EntityManager em = emFactory.createEntityManager();		
		EntityTransaction tran = em.getTransaction();
				
		try {
			tran.begin();			
			for (PortletApp portlet : portlets) {
				em.persist(portlet);
			}
			// excute the sql to delete all the items in the database
			tran.commit();
		} catch (Exception ex) {
			if(tran.isActive()) tran.rollback();
			LOG.log(Level.WARNING, "create portlets error.", ex);
			throw new RuntimeException(ex);
		} finally {			
			if (em!=null) em.close();
		}	
	}
}
