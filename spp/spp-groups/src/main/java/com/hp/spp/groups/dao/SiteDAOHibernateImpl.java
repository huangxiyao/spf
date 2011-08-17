package com.hp.spp.groups.dao;

import java.util.List;

import org.hibernate.HibernateException;

import com.hp.spp.groups.Site;

/**
 * 
 * Hibernate implementation of the DOA for the Site.
 * 
 */

public class SiteDAOHibernateImpl extends RootDAOHibernate implements SiteDAO {

	/**
	 * The instance for the singleton.
	 */
	private static SiteDAOHibernateImpl instance;

	/**
	 * Return a singleton of the DAO.
	 * 
	 * @return the instance
	 */
	public static SiteDAOHibernateImpl getInstance() {
		if (null == instance) {
			instance = new SiteDAOHibernateImpl();
		}
		return instance;
	}

	/**
	 * Returns the class definition.
	 * 
	 * @return class definition
	 */
	public Class getReferenceClass() {
		return com.hp.spp.groups.Site.class;
	}

	/**
	 * Update the site.
	 * 
	 * @param site site to update
	 */
	public void update(Site site) throws HibernateException {
		super.update(site);
	}

	/**
	 * Delete the Site.
	 * 
	 * @param site the site to delete
	 */
	public void delete(Site site) throws HibernateException {
		super.delete(site);
	}

	/**
	 * Save the Site.
	 * 
	 * @param site site to save
	 * @return status
	 */
	public Long save(Site site) throws HibernateException {

		return super.save(site);

	}

	/**
	 * Return all the Site in the DB.
	 * 
	 * @return all sites
	 * @throws HibernateException
	 */
	public List findAll() throws HibernateException {
		StringBuffer query = new StringBuffer();
		query.append("select site from com.hp.spp.groups.Site as site ");
		List results = super.find(query.toString());
		return results;
	}

	/**
	 * Load the Site from DB.
	 * 
	 * @param id id of the site to load
	 * @return the Site
	 */
	public synchronized Site load(long id) throws HibernateException {
		StringBuffer query = new StringBuffer();
		query.append("select site from com.hp.spp.groups.Site as site where site.id ='" + id
				+ "'");
		Site results = (Site) super.find(query.toString()).get(0);
		return results;
	}

	/**
	 * Load the Site from DB using its name.
	 * 
	 * @param siteName name of the site to load
	 * @return the Site
	 */
	public synchronized Site loadByName(String siteName) throws HibernateException {
		StringBuffer query = new StringBuffer();
		query.append("select site from com.hp.spp.groups.Site as site where site.name ='"
				+ siteName + "'");
		Site results = (Site) super.find(query.toString()).get(0);
		return results;
	}

}
