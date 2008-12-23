package com.sun.portal.portletcontainer.admin.registry.database.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletWindowPreference;

public class PortletWindowPreferenceRegistryDao {
    private static Logger LOG = Logger
            .getLogger(PortletWindowPreferenceRegistryDao.class.toString());

    private EntityManagerFactory emFactory;

    public PortletWindowPreferenceRegistryDao() {
        emFactory = EntityManagerFactoryManager.getInstance().getFactory();
    }

    /**
     * create portlet window preferences
     * 
     * @param portletWindowPreferenceList
     */
    public void addPortletWindowPreferences(
            List<PortletWindowPreference> portletWindowPreferenceList) {
        EntityManager em = emFactory.createEntityManager();
        EntityTransaction tran = em.getTransaction();
        try {
            tran.begin();
            for (PortletWindowPreference portletWindowPreference : portletWindowPreferenceList) {
                em.persist(portletWindowPreference);
            }
            // persist the portlet windows into the database
            tran.commit();
        } catch (Exception ex) {
            if (tran.isActive())
                tran.rollback();
            LOG.warning("add portletWindows error. ");
            LOG.warning("error message: " + ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            if (em != null)
                em.close();
        }
    }

    /**
     * create the specified portlet window preference object
     * 
     * @param portletWindowPreference
     */
    public void addPortletWindowPreference(
            PortletWindowPreference portletWindowPreference) {
        EntityManager em = emFactory.createEntityManager();

        EntityTransaction tran = em.getTransaction();
        try {
            tran.begin();
            // persist the portlet window preference to persistence layer
            em.persist(portletWindowPreference);
            // persist the portlet window preference into the database
            tran.commit();
        } catch (Exception ex) {
            tran.rollback();
            LOG
                    .warning("add portletWindowPreference error, portletWindowName: "
                            + portletWindowPreference.getName());
            LOG.warning("error message: " + ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            if (em != null)
                em.close();
        }
    }

    /**
     * get specified portlet window preference object accroding to window name
     * and user name
     * 
     * @param portletWindowName
     * @param userName
     * @return
     */
    public PortletWindowPreference getPortletWindowPreference(
            String portletWindowName, String userName) {
        EntityManager em = emFactory.createEntityManager();

        String sql = "select x" + "  from PortletWindowPreference x"
                + " where x.name = :name and x.userName = :userName";
        LOG.fine("JPA SQL: " + sql);

        try {
            Query query = em.createQuery(sql);
            query.setParameter("name", portletWindowName);
            query.setParameter("userName", userName);
            Object obj = query.getSingleResult();
            PortletWindowPreference portletWindowPreference = null;
            if (obj != null) {
                portletWindowPreference = (PortletWindowPreference)obj;
            }
            return portletWindowPreference;
        } catch (Exception ex) {
            LOG
                    .warning("get portletWindowPreference error, portletWindowName: "
                            + portletWindowName + ", userName: " + userName);
            LOG.warning("error message: " + ex.getMessage());
        } finally {
            em.close();
        }
        return null;
    }

    /**
     * get specified portlet window preference object accroding to window name
     * and user name
     * 
     * @param portletWindowName
     * @param userName
     * @return
     */

    public Map getPortletWindowPreferences(String portletWindowName,
            String userName, String name) {
        EntityManager em = emFactory.createEntityManager();

        String sql = "select x.elementName, x.elementValue"
                + "  from PortletWindowPreferencePropertyCollection x"
                + " where x.name = :name and x.portletWindowPreference.name = :windowName and x.portletWindowPreference.userName = :userName";
        LOG.fine("JPA SQL: " + sql);

        try {
            Query query = em.createQuery(sql);
            query.setParameter("windowName", portletWindowName);
            query.setParameter("userName", userName);
            query.setParameter("name", name);
            List result = query.getResultList();
            Map resultMap = new HashMap();
            for (Object object : result) {
                Object[] element = (Object[])object;
                resultMap.put((String)element[0], (String)element[1]);
            }
            return resultMap;
        } catch (Exception ex) {
            LOG
                    .warning("get portletWindowPreference error, portletWindowName: "
                            + portletWindowName + ", userName: " + userName);
            LOG.warning("error message: " + ex.getMessage());
        } finally {
            em.close();
        }
        return null;
    }

    /**
     * remove specified Portlet Window Preference and all its related properties
     * 
     * @param portletName
     *            portlet name
     */
    public void removePortletWindowPreferences(String portletName) {
        EntityManager em = emFactory.createEntityManager();

        EntityTransaction tran = em.getTransaction();

        String sql = "select x from PortletWindowPreference x"
                + " where x.portletName = :portletName";
        LOG.fine("JPA SQL: " + sql);
        try {
            tran.begin();
            Query query = em.createQuery(sql);
            query.setParameter("portletName", portletName);
            List result = query.getResultList();
            for (Object object : result) {
                em.remove(object);
            }
            // excute the sql to delete all the items in the database
            tran.commit();
        } catch (Exception ex) {
            if (tran.isActive())
                tran.rollback();
            LOG.warning("delte portletWindows error, portletName: "
                    + portletName);
            LOG.warning("error message: " + ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            if (em != null)
                em.close();
        }
    }

    /**
     * update the specified portlet window preference object
     * 
     * @param portletWindowPreference
     *            portlet window name
     */
    public void updatePortletWindowPreference(
            PortletWindowPreference portletWindowPreference) {
        EntityManager em = emFactory.createEntityManager();

        EntityTransaction tran = em.getTransaction();
        String sql = "delete from PortletWindowPreferencePropertyCollection x"
                + " where x.portletWindowPreference.userName = :userName and x.portletWindowPreference.name = :portletWindowName";
        LOG.fine("JPA SQL: " + sql);
        try {
            tran.begin();
            Query query = em.createQuery(sql);
            query.setParameter("portletWindowName", portletWindowPreference
                    .getName());
            query.setParameter("userName", portletWindowPreference
                    .getUserName());
            query.executeUpdate();
            // persist the portlet window to persistence layer
            em.merge(portletWindowPreference);
            // persist the portlet windows into the database
            tran.commit();
        } catch (Exception ex) {
            if (tran.isActive())
                tran.rollback();
            LOG
                    .warning("add portletWindowPreference error, portletWindowName: "
                            + portletWindowPreference.getName());
            LOG.warning("error message: " + ex.getMessage());
            throw new RuntimeException(ex);
        } finally {
            if (em != null)
                em.close();
        }
    }
}
