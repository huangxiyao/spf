package com.sun.portal.portletcontainer.admin.registry.database.dao;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.sun.portal.portletcontainer.admin.database.exception.PortletRegistryDBException;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletApp;

public class PortletAppRegistryDao {
	private static Logger LOG = Logger.getLogger(PortletAppRegistryDao.class
			.toString());

	private EntityManagerFactory emFactory;

	public PortletAppRegistryDao() {
		emFactory = EntityManagerFactoryManager.getInstance().getFactory();
		;
	}

	@SuppressWarnings("unchecked")
	public List<PortletApp> getAllPortlets() {
		EntityManager em = emFactory.createEntityManager();

		String sql = "select x" + "  from PortletApp x";
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("JPA SQL: " + sql);
		}

		try {
			Query query = em.createQuery(sql);
			List<PortletApp> list = query.getResultList();
			return list;
		} catch (Exception ex) {
			throw new PortletRegistryDBException("get all portlet error.", ex);
		} finally {
			em.close();
		}
	}

	/**
	 * retrieve portlet by portlet name
	 * 
	 * @param portletName
	 * @return portletApp object
	 */
	public PortletApp getPortlet(String portletName) {
		EntityManager em = emFactory.createEntityManager();

		String sql = "select x" + "  from PortletApp x"
				+ " where x.portletName = :portletName";
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("JPA SQL: " + sql);
		}

		try {
			Query query = em.createQuery(sql);
			query.setParameter("portletName", portletName);
			PortletApp portletApp = (PortletApp) query.getSingleResult();
			return portletApp;
		} catch (NoResultException ex) {
			return null;
		} catch (Exception ex) {
			throw new PortletRegistryDBException("get portlet error.", ex);
		} finally {
			em.close();
		}
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
			if (tran.isActive()) {
				tran.rollback();
			}
			throw new PortletRegistryDBException(
					"delte portlet error, portletName: " + portletName, ex);
		} finally {
			em.close();
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
			if (tran.isActive()) {
				tran.rollback();
			}
			throw new PortletRegistryDBException("create portlets error.", ex);
		} finally {
			em.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<PortletApp> getPortletsForArchiveName(String archiveName) {
		EntityManager em = emFactory.createEntityManager();

		String sql = "select x" + " from PortletApp x"
				+ " where x.archiveName = :archiveName";
		if (LOG.isLoggable(Level.FINE)) {
			LOG.fine("JPA SQL: " + sql);
		}

		try {
			Query query = em.createQuery(sql);
			query.setParameter("archiveName", archiveName);
			List<PortletApp> list = query.getResultList();
			return list;
		} catch (Exception ex) {
			throw new PortletRegistryDBException(
					"getPortletsForArchiveName error.", ex);
		} finally {
			em.close();
		}
	}
}
