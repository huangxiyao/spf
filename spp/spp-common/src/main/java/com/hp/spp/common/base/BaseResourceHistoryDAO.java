package com.hp.spp.common.base;

import java.util.List;

import com.hp.spp.common.ResourceHistory;
import com.hp.spp.common.dao.ResourceHistoryDAO;

/**
 * 
 * !!!! WARNING !!!! This class must NOT be modified Any changes made here will be lost during
 * regeneration
 * 
 */
public abstract class BaseResourceHistoryDAO extends com.hp.spp.common.base._BaseRootDAO {

	public static ResourceHistoryDAO instance;

	/**
	 * Return a singleton of the DAO
	 */
	public static ResourceHistoryDAO getInstance() {
		if (null == instance)
			instance = new ResourceHistoryDAO();
		return instance;
	}

	/**
	 * Return the class of the current object
	 */
	public Class getReferenceClass() {
		return com.hp.spp.common.ResourceHistory.class;
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if
	 * there is a persistent instance with the same identifier in the current session.
	 * 
	 * @param person
	 *            a transient instance containing updated state
	 */
	public void update(com.hp.spp.common.ResourceHistory abstractResourceHistory)
			throws org.hibernate.HibernateException {
		super.update(abstractResourceHistory);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated
	 * with the receiving Session or a transient instance with an identifier associated with
	 * existing persistent state.
	 * 
	 * @param person
	 *            the instance to be removed
	 */
	public void delete(com.hp.spp.common.ResourceHistory abstractResourceHistory)
			throws org.hibernate.HibernateException {
		super.delete(abstractResourceHistory);
	}

	/**
	 * Used to check that the unicity constraint given in the xml model file
	 * (/module/component/attributes/attribute[@unique='true']) is not violated when a new
	 * ResourceHistory is created
	 * 
	 * @param ResourceHistory :
	 *            the object that must be created
	 * @return Long : the id of the newly created object
	 */
	public Long save(com.hp.spp.common.ResourceHistory abstractResourceHistory)
			throws org.hibernate.HibernateException {
		return super.save(abstractResourceHistory);

	}

	/**
	 * Used to check that the unicity constraint given in the xml model file
	 * (/module/component/attributes/attribute[@unique='true']) is not violated when an existing
	 * ResourceHistory is updated
	 * 
	 * @param ResourceHistory :
	 *            the object that must be updated
	 * @return Long : the id of the object
	 */
	public Long updateResourceHistory(
			ResourceHistory ResourceHistory)
			throws org.hibernate.HibernateException {

		return new Long(ResourceHistory.getId());

	}

	/**
	 * Used to load all the ResourceHistorys present in the database
	 */

	public List findAll() throws org.hibernate.HibernateException {
		StringBuffer query = new StringBuffer();
		query
				.append("select resourceHistory from com.hp.spp.common.ResourceHistory as resourceHistory ");
		List results = super.find(query.toString());
		return results;
	}

	/**
	 * Used to load the ResourceHistory having an ID equal to
	 * 
	 * @param long
	 *            id
	 */
	public com.hp.spp.common.ResourceHistory load(long id)
			throws org.hibernate.HibernateException {
		StringBuffer query = new StringBuffer();
		query
				.append("select resourceHistory from com.hp.spp.common.ResourceHistory as resourceHistory where resourceHistory.id ='"
						+ id + "'");
		com.hp.spp.common.ResourceHistory results = (com.hp.spp.common.ResourceHistory) super
				.find(query.toString()).get(0);
		return results;
	}

}
