package com.hp.spp.common.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.hp.spp.common.base.BaseResourceHistoryDAO;
import com.hp.spp.common.util.hibernate.HibernateUtil;

/**
 * 
 * This class can be modified and will not be regenerated
 * 
 */

public class ResourceHistoryDAO extends BaseResourceHistoryDAO {

	public List getResourceHistoryByType(int type, long siteId) {
		Session s = null;
		s = HibernateUtil.getSessionFactory().getCurrentSession();
		Query sqlQuery = s.createQuery("select rh from com.hp.spp.common.ResourceHistory as rh where rh.siteId = "
				+ siteId + " and rh.modificationType = "
				+ type +
				" ORDER BY rh.creationDate DESC");
		sqlQuery.setMaxResults(20);
		List results = sqlQuery.list();
		return results;
	}

}
