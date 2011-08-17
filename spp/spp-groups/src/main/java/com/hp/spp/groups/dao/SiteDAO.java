package com.hp.spp.groups.dao;

import java.util.List;

import com.hp.spp.groups.Site;

/**
 * Interface for all the DataAccessObjects related to the SITE.
 * 
 * @author bmollard
 * 
 */
public interface SiteDAO {

	/**
	 * Load the Site.
	 * 
	 * @param id id of the site to load
	 * @return the Site
	 */
	Site load(long id);

	/**
	 * Return a list of Sites corresponding to the query.
	 * 
	 * @param query the Query
	 * @return list of sites
	 */
	List find(String query);

	/**
	 * Save the Site.
	 * 
	 * @param s site to save
	 * @return status
	 */
	Long save(Site s);

	/**
	 * Update the site.
	 * 
	 * @param s site to update
	 */
	void update(Site s);

	/**
	 * Delete the Site.
	 * 
	 * @param s the site to delete
	 */
	void delete(Site s);

}
