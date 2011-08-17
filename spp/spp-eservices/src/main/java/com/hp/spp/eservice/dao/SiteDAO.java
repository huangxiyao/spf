


package com.hp.spp.eservice.dao;

import org.hibernate.HibernateException;

import com.hp.spp.eservice.Site;
import com.hp.spp.eservice.base.BaseSiteDAO;

/**
 * 
 * This class can be modified and will not be regenerated 
 * 
 */

public class SiteDAO extends BaseSiteDAO {

	/**
	 * Load the Site from DB using its name.
	 * 
	 * @param siteName name of the site to load
	 * @return the Site
	 */
	public synchronized Site loadByName(String siteName) throws HibernateException {
		StringBuffer query = new StringBuffer();
		query.append("select site from com.hp.spp.eservice.Site as site where site.name ='"
				+ siteName + "'");
		Site results = (Site) super.find(query.toString()).get(0);
		return results;
	}
}

