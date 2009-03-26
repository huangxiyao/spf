package com.sun.portal.portletcontainer.admin.registry.database.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.sun.portal.portletcontainer.admin.database.exception.PortletRegistryDBException;
import com.sun.portal.portletcontainer.admin.registry.PortletRegistryTags;
import com.sun.portal.portletcontainer.admin.registry.database.entity.PortletUserWindow;

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
            List<PortletUserWindow> portletWindowPreferenceList) {
        EntityManager em = emFactory.createEntityManager();
        EntityTransaction tran = em.getTransaction();
        try {
            tran.begin();
            for (PortletUserWindow portletWindowPreference : portletWindowPreferenceList) {
                em.persist(portletWindowPreference);
            }
            // persist the portlet windows into the database
            tran.commit();
        } catch (Exception ex) {
            if (tran.isActive()){
                tran.rollback();            	
            }
            throw new PortletRegistryDBException("add portlet Window Preferences error.", ex);      
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
     * create the specified portlet window preference object
     * 
     * @param portletWindowPreference
     */
    public void addPortletWindowPreference(
            PortletUserWindow portletWindowPreference) {
        EntityManager em = emFactory.createEntityManager();

        EntityTransaction tran = em.getTransaction();
        try {
            tran.begin();
            // persist the portlet window preference to persistence layer
            em.persist(portletWindowPreference);
            // persist the portlet window preference into the database
            tran.commit();
        } catch (Exception ex) {
            if (tran.isActive()){
                tran.rollback();            	
            }
            throw new PortletRegistryDBException("add portlet Window Preference error, portletWindowName: " + portletWindowPreference.getWindowName(), ex);
        } finally {
            if (em != null) {
                em.close();            	
            }
        }
    }

    /**
     * get specified portlet window preference object according to window name
     * and user name
     * 
     * @param portletWindowName
     * @param userName
     * @return
     */
    public PortletUserWindow getPortletWindowPreference(
            String portletWindowName, String userName) {
        EntityManager em = emFactory.createEntityManager();

        String sql = "select x" + "  from PortletUserWindow x"
                + " where x.windowName = :windowName and x.userName = :userName";
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("JPA SQL: " + sql);
        }

        try {
            Query query = em.createQuery(sql);
            query.setParameter("windowName", portletWindowName);
            query.setParameter("userName", userName);

            Object obj = query.getSingleResult();
            PortletUserWindow portletWindowPreference = null;
            if (obj != null) {
                portletWindowPreference = (PortletUserWindow)obj;
            }
            return portletWindowPreference;
        } catch (NoResultException e) {
        	if (LOG.isLoggable(Level.WARNING)){
            	LOG.log(Level.WARNING, "not found portlet Window Preference error, portletWindowName: "
                        + portletWindowName + ", userName: " + userName);        		
        	}
            return null;
        } catch (Exception ex) {
        	String message = "get portlet Window Preference error, portletWindowName: "
                + portletWindowName + ", userName: " + userName;
        	throw new PortletRegistryDBException(message, ex);
        } finally {
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

    public Map getPortletWindowPreferences(String portletWindowName,
            String userName, String type) {
        EntityManager em = emFactory.createEntityManager();

        String sql = "select x.preferenceName, x.preferenceValue"
                + "  from PortletUserWindowPreference x"
                + " where x.type = :type and x.portletUserWindow.windowName = :windowName and x.portletUserWindow.userName = :userName";
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("JPA SQL: " + sql);
        }

        try {
            Query query = em.createQuery(sql);
            query.setParameter("windowName", portletWindowName);
            query.setParameter("userName", userName);
            query.setParameter("type", type);
            List result = query.getResultList();
            Map resultMap = new HashMap();
            for (Object object : result) {
                Object[] element = (Object[])object;
                resultMap.put((String)element[0], (String)element[1]);
            }
            return resultMap;
        } catch (NoResultException e) {
        	if (LOG.isLoggable(Level.WARNING)){
            	LOG.log(Level.WARNING, "not found portlet Window Preference error, portletWindowName: "
                        + portletWindowName + ", userName: " + userName);        		
        	}
            return null;
        } catch (Exception ex) {
        	String message = "get portlet Window Preference error, portletWindowName: "
                + portletWindowName + ", userName: " + userName;            
        	throw new PortletRegistryDBException(message, ex);
        } finally {
            em.close();
        }
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

        String sql = "select x from PortletUserWindow x"
                + " where x.portletName = :portletName";
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("JPA SQL: " + sql);
        }
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
            if (tran.isActive()) {
                tran.rollback();            	
            }
            String message = "delete portlet Window Preference error, portletName: " + portletName;
            throw new PortletRegistryDBException(message, ex);
        } finally {
            if (em != null) {
                em.close();            	
            }
        }
    }

    /**
     * remove specified Portlet Window Preference and all its related properties for none cloned portlet windows
     * 
     * @param portletName
     *            portlet name
     */
    public void removeNonClonedPortletWindowPreferences(String portletName) {
        EntityManager em = emFactory.createEntityManager();

        EntityTransaction tran = em.getTransaction();

        String sql = "select x from PortletUserWindow x"
                + " where x.portletName = x.windowName and x.portletName = :portletName";
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("JPA SQL: " + sql);
        }
        try {
            tran.begin();
            Query query = em.createQuery(sql);
            query.setParameter("portletName", portletName);
            List result = query.getResultList();
            for (Object object : result) {
                em.remove(object);
            }
            // execute the sql to delete all the items in the database
            tran.commit();
        } catch (Exception ex) {
            if (tran.isActive()) {
                tran.rollback();            	
            }
            String message = "removeNonClonedPortletWindowPreferences error, portletName: " + portletName;
            throw new PortletRegistryDBException(message, ex);
        } finally {
            if (em != null) {
                em.close();            	
            }
        }
    }

    /**
     * update the specified portlet window preference object
     * 
     * @param portletWindowPreference
     *            portlet window name
     */
    public void updatePortletWindowPreference(
            PortletUserWindow portletWindowPreference) {
        EntityManager em = emFactory.createEntityManager();

        EntityTransaction tran = em.getTransaction();
        String sql = "delete from PortletUserWindowPreference x"
                + " where x.portletUserWindow.userName = :userName and x.portletUserWindow.windowName = :portletWindowName";
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("JPA SQL: " + sql);
        }
        try {
            tran.begin();
            Query query = em.createQuery(sql);
            query.setParameter("portletWindowName", portletWindowPreference
                    .getWindowName());
            query.setParameter("userName", portletWindowPreference
                    .getUserName());
            query.executeUpdate();
            // persist the portlet window to persistence layer
            em.merge(portletWindowPreference);
            // persist the portlet windows into the database
            tran.commit();
        } catch (Exception ex) {
            if (tran.isActive()) {
                tran.rollback();            	
            }
            String message = "add portlet Window Preference error, portletWindowName: "
                + portletWindowPreference.getWindowName();
           throw new PortletRegistryDBException(message, ex);
        } finally {
            if (em != null) {
                em.close();            	
            }
        }
    }
    
    /**
     * get specified portlet window preference defined in portlet.xml
     * and user name
     * 
     * @param portletWindowName
     * @param userName
     * @return
     */

    public Map getDefaultPortletWindowPreferences(String portletWindowName,
            String userName, String type) {
        EntityManager em = emFactory.createEntityManager();

        StringBuilder sql = new StringBuilder("select x.preferenceName, x.preferenceValue");
        sql.append(" from PortletUserWindowPreference x");
        sql.append(" where x.type = :type");
        sql.append(" and x.portletUserWindow.windowName = x.portletUserWindow.portletName");
        sql.append(" and x.portletUserWindow.userName = :userName");
        sql.append(" and x.portletUserWindow.portletName in (");
        sql.append(" select w.portletName from PortletWindow w where w.name = :windowName");
        sql.append(")");
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("JPA SQL: " + sql);
        }

        try {
            Query query = em.createQuery(sql.toString());
            query.setParameter("windowName", portletWindowName);
            query.setParameter("userName", userName);
            query.setParameter("type", type);
            List result = query.getResultList();
            Map resultMap = new HashMap();
            for (Object object : result) {
                Object[] element = (Object[])object;
                resultMap.put((String)element[0], (String)element[1]);
            }
            return resultMap;
        } catch (NoResultException e) {
        	if (LOG.isLoggable(Level.WARNING)){
            	LOG.log(Level.WARNING, "not found predefined portlet Window Preference error, portletWindowName: "
                        + portletWindowName + ", userName: " + userName);        		
        	}
            return null;
        } catch (Exception ex) {
        	String message = "get portlet Window Preference error, portletWindowName: "
                + portletWindowName + ", userName: " + userName;            
        	throw new PortletRegistryDBException(message, ex);
        } finally {
            em.close();
        }
    }
    
    /**
     * get Portlet Window Preference for a specific criteria
     * 
     * @param portletName
     *            portlet name
     */
    public Map getPortletPreferenceByCriteria(String userName, String portletName, 
    		String type, String value) {
        EntityManager em = emFactory.createEntityManager();

        StringBuilder sql = new StringBuilder("select x.preferenceName, x.preferenceValue from PortletUserWindowPreference x");
        sql.append(" where x.portletUserWindow.windowName = :portletName");
        sql.append(" and x.portletUserWindow.userName=:userName and x.type = :type and x.preferenceValue = :value");
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("JPA SQL: " + sql);
        }
        try {
            Query query = em.createQuery(sql.toString());
            query.setParameter("portletName", portletName);
            query.setParameter("userName", userName);
            query.setParameter("type", type);
            query.setParameter("value", value);
            List result = query.getResultList();
            Map resultMap = new HashMap();
            for (Object object : result) {
                Object[] element = (Object[])object;
                resultMap.put((String)element[0], (String)element[1]);
            }
            return resultMap;
        } catch (NoResultException e) {
        	if (LOG.isLoggable(Level.WARNING)){
            	LOG.log(Level.WARNING, "getPortletPreferenceByCriteria error, portletName: "
                        + portletName + ", type: " + type + ", value: " + value);        		
        	}
            return null;
        } catch (Exception ex) {
        	String message = "getPortletPreferenceByCriteria error, portletName: "
                + portletName + ", type: " + type + ", value: " + value;            
        	throw new PortletRegistryDBException(message, ex);
        } finally {
            em.close();
        }
    }
}
