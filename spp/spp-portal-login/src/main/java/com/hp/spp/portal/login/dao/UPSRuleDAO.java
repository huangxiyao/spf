package com.hp.spp.portal.login.dao;

import java.util.List;

/*
 * Interface class to fetch list of rules
 * based on siteIdentifier. Uses the DAO pattern
 * 
 * @author Adi/girishsk
 * @since SPP 4.0
 * @see com.hp.spp.portal.login.dao.UPSRuleDAOCacheImpl
 * @see com.hp.spp.portal.login.dao.UPSRuleDAOSQLManagerImpl
 */

public interface UPSRuleDAO{
	List getUPSRules(String siteIdentifier);
}

