package com.hp.spp.eservice.base;

import java.util.List;

import com.hp.spp.eservice.dao.SiteDAO;

public abstract class BaseSiteDAO extends com.hp.spp.eservice.base._BaseRootDAO {

	public static SiteDAO instance;

	/**
	 * Return a singleton of the DAO
	 */
	public static SiteDAO getInstance() {
		if (null == instance)
			instance = new SiteDAO();
		return instance;
	}

	/**
	 * Return the class of the current object
	 */
	public Class getReferenceClass() {
		return com.hp.spp.eservice.Site.class;
	}

	/**
	 * Update the persistent state associated with the given identifier. An exception is thrown if
	 * there is a persistent instance with the same identifier in the current session.
	 * 
	 * @param person
	 *            a transient instance containing updated state
	 */
	public void update(com.hp.spp.eservice.Site abstractSite)
			throws org.hibernate.HibernateException {
		super.update(abstractSite);
	}

	/**
	 * Remove a persistent instance from the datastore. The argument may be an instance associated
	 * with the receiving Session or a transient instance with an identifier associated with
	 * existing persistent state.
	 * 
	 * @param person
	 *            the instance to be removed
	 */
	public void delete(com.hp.spp.eservice.Site abstractSite)
			throws org.hibernate.HibernateException {
		super.delete(abstractSite);
	}

	/**
	 * Used to check that the unicity constraint given in the xml model file
	 * (/module/component/attributes/attribute[@unique='true']) is not violated when a new Site is
	 * created
	 * 
	 * @param Site :
	 *            the object that must be created
	 * @return Long : the id of the newly created object
	 */
	public Long save(com.hp.spp.eservice.Site abstractSite)
			throws org.hibernate.HibernateException {
		return super.save(abstractSite);

	}

	/**
	 * Used to check that the unicity constraint given in the xml model file
	 * (/module/component/attributes/attribute[@unique='true']) is not violated when an existing
	 * Site is updated
	 * 
	 * @param Site :
	 *            the object that must be updated
	 * @return Long : the id of the object
	 */
	public Long updateSite(com.hp.spp.eservice.Site abstractSite)
			throws org.hibernate.HibernateException {

		return new Long(abstractSite.getId());

	}

	/**
	 * Used to load all the Sites present in the database
	 */

	public List findAll() throws org.hibernate.HibernateException {
		StringBuffer query = new StringBuffer();
		query.append("select site from com.hp.spp.eservice.bean.Site as site ");
		List results = super.find(query.toString());
		return results;
	}

	/**
	 * Used to load the Site having an ID equal to
	 * 
	 * @param long
	 *            id
	 */
	public com.hp.spp.eservice.Site load(long id) throws org.hibernate.HibernateException {
		StringBuffer query = new StringBuffer();
		query.append("select site from com.hp.spp.eservice.bean.Site as site where site.id ='" + id
				+ "'");
		com.hp.spp.eservice.Site results = (com.hp.spp.eservice.Site) super.find(
				query.toString()).get(0);
		return results;
	}

}
