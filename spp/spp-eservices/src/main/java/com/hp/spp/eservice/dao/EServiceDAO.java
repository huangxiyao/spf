


package com.hp.spp.eservice.dao;

import com.hp.spp.eservice.EService;
import com.hp.spp.eservice.base.BaseEServiceDAO;

/**
 * 
 * This class can be modified and will not be regenerated 
 * 
 */

public class EServiceDAO extends BaseEServiceDAO {

	public EService loadBySiteIdAndName(String name, long siteId) {
		
		StringBuffer query = new StringBuffer();
		query.append("select es from com.hp.spp.eservice.EService as es where es.name ='");
		query.append(name);
		query.append("' and es.site.id='").append(Long.toString(siteId)).append("'");
		
		EService results = (EService) super.find(query.toString()).get(0);
		return results;
	}
}

