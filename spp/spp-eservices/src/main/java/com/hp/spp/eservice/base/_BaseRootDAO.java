

package com.hp.spp.eservice.base;


import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.hp.spp.common.util.hibernate.HibernateUtil;

/**
 * 
 * !!!! WARNING !!!!
 * This class must NOT be modified
 * Any changes made here will be lost during regeneration
 * 
 */
public abstract class _BaseRootDAO {

	/**
	 * Load the object froml the table corresponding to refClass
	 * and having key as ID
	 */
	protected Object load(Class refClass, Serializable key) throws HibernateException {
		Session s = null;
				s = HibernateUtil.getSessionFactory().getCurrentSession();
		Object result =  s.load(refClass,key);
				return result;
	}


	/**
	 * Execute a query. 
	 * @param query a query expressed in Hibernate's query language
	 * @return a distinct list of instances (or arrays of instances)
	 */
	public java.util.List find(String query) throws HibernateException {
			Session s = null;
		s = HibernateUtil.getSessionFactory().getCurrentSession();
		Query sqlQuery = s.createQuery(query);
		List results =  sqlQuery.list();
			return results;
	}


	/**
	 * Persist the given transient instance, first assigning a generated identifier. 
	 * (Or using the current value of the identifier property if the assigned generator is used.) 
	 */
	protected  java.lang.Long save(Object obj) throws HibernateException {
		//Transaction t = null;
		Session s = null;
		s = HibernateUtil.getSessionFactory().getCurrentSession();
		java.lang.Long rtn = (java.lang.Long)s.save(obj);
			return rtn;
	}

	
	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if there is a persistent
	 * instance with the same identifier in the current session.
	 * @param obj a transient instance containing updated state
	 */
	protected void update(Object obj) throws HibernateException {
			Session s = null;
		s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.update(obj);
	}
	

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated with the receiving
	 * Session or a transient instance with an identifier associated with existing persistent state. 
	 */
	protected void delete(Object obj) throws HibernateException {
			Session s = null;
		s = HibernateUtil.getSessionFactory().getCurrentSession();
		s.delete(obj);
			}
}
