package com.hp.spp.eservice.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.hp.spp.common.util.hibernate.HibernateUtil;
import com.hp.spp.eservice.EService;
import com.hp.spp.eservice.StandardParameterSet;
import com.hp.spp.eservice.base.BaseStandardParameterSetDAO;

/**
 * 
 * This class can be modified and will not be regenerated
 * 
 */

public class StandardParameterSetDAO extends BaseStandardParameterSetDAO {

	private static Logger mLogger = Logger.getLogger(StandardParameterSetDAO.class);

	public StandardParameterSet loadByName(String name,String siteName) {
		StandardParameterSet result = null;
		StringBuffer query = new StringBuffer();
		query
				.append("select sps from com.hp.spp.eservice.StandardParameterSet as sps where sps.name ='"
						+ name + "'" + "and sps.site.name='"+siteName+"'");
		List results = super.find(query.toString());
		if (results.size() > 0) {
			result = (StandardParameterSet) results.get(0);
		}
		return result;
	}

	public int getNumberOfReferencingEServices(StandardParameterSet standardParameterSet) {
		int count = 0;
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		List result = session.createCriteria(EService.class).setProjection(
				Projections.rowCount()).add(
				Restrictions.eq("standardParameterSet", standardParameterSet)).list();
		if (result != null && result.size() == 1) {
			count = ((Integer) result.get(0)).intValue();
		} else {
			mLogger.error("Could not get a result for the count of referenced param sets");
		}
		return count;
	}
}
