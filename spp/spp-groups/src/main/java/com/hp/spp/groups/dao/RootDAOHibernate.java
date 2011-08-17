package com.hp.spp.groups.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.hp.spp.common.util.hibernate.HibernateUtil;





/**
 * Master class for all the Hibernate DAO implementations.
 * 
 */
public abstract class RootDAOHibernate {

	private static Logger logger = Logger.getLogger(RootDAOHibernate.class);
	
	/**
	 * Load the object from the table corresponding to refClass and having key as ID.
	 * 
	 * @param refClass - class to load
	 * @param key - primary key
	 * @return a RootDAO
	 * @throws HibernateException for Exception during retrieval
	 * @see com.hp.spp.groups.core.base.RootDAO#load(java.lang.Class, java.io.Serializable)
	 */
	public Object load(Class refClass, Serializable key) throws HibernateException {
		Session s = null;
		s = HibernateUtil.getSessionFactory().getCurrentSession();
		Object result = s.load(refClass, key);
		return result;
	}

	/**
	 * Find a list of entity according to a query.
	 * 
	 * @param query the query
	 * @return list of entities
	 * @throws HibernateException
	 */
	public List find(String query) throws HibernateException {
		List results = null;
		Session s = HibernateUtil.getSessionFactory().getCurrentSession();
		if (s != null) {
			Query sqlQuery = s.createQuery(query);
			results = sqlQuery.list();
		} else {
			logger.error("Hibernate session not found");
		}
		return results;
	}

	/**
	 * Save the entity in database.
	 * @param obj object to save
	 * @return status of the save operation
	 * @throws HibernateException
	 */
	public Long save(Object obj) throws HibernateException {
		Session s = null;
		s = HibernateUtil.getSessionFactory().getCurrentSession();
		java.lang.Long rtn = (java.lang.Long) s.save(obj);
		return rtn;
	}

	/**
	 * Update the entity in DB.
	 * @param obj object to update
	 * @throws HibernateException
	 */
	public void update(Object obj) throws HibernateException {
		Session s = null;
		s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.update(obj);
	}

	/**
	 * Delete an entity from DB.
	 * @param obj object to delete
	 * @throws HibernateException
	 */
	public void delete(Object obj) throws HibernateException {
		Session s = null;
		s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.delete(obj);
	}
}
