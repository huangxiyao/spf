package com.hp.spp.portal.login.business.rule;

import java.util.List;
import org.apache.log4j.Logger;
import com.hp.spp.cache.Cache;
import com.hp.spp.portal.login.dao.UPSRuleDAO;
import com.hp.spp.portal.login.dao.UPSRuleDAOCacheImpl;
import com.hp.spp.portal.login.dao.UPSRuleDAOSQLManagerImpl;

/**
 * Singleton class that acts as holder object of
 * dao instance. Further it helps in encapsulating
 * the logic for lookup.
 * 
 * @since SPP 4.0
 * @see  com.hp.spp.portal.login.dao.UPSRuleDAO
*/

public class RuleContainer {
	private static Logger mLog = Logger.getLogger(RuleContainer.class);
	private static RuleContainer mRuleContainer;
	private UPSRuleDAO mUPSRuleDAO = null;
	
	private RuleContainer(){
		mUPSRuleDAO = new UPSRuleDAOCacheImpl(new UPSRuleDAOSQLManagerImpl(), Cache.getInstance());
	}
	
	public static RuleContainer getInstance(){
		if(mRuleContainer == null){
			mRuleContainer = new RuleContainer();
		}
		return mRuleContainer;
	}
	
	public List getRules(String siteIdentifier){
		return mUPSRuleDAO.getUPSRules(siteIdentifier);	
	}
}
