package com.hp.spp.portal.login.dao;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hp.spp.db.DB;
import com.hp.spp.portal.login.model.UPSRule;
import com.hp.spp.portal.login.model.UPSRuleMapper;

/*
 * JDBC implementation of the UPSRuleDAO interface
 * @author girishsk
 * @since SPP 4.0
 * @see com.hp.spp.portal.login.dao.UPSRuleDAO
 */

public class UPSRuleDAOSQLManagerImpl implements UPSRuleDAO{
	private static final Logger mLog = Logger.getLogger(UPSRuleDAOSQLManagerImpl.class);
	private static final String upsrule = "UPSRULE";
	

	public List getUPSRules(String siteIdentifier) {
		Object[] args = { siteIdentifier, upsrule };
		String query = "SELECT RULE_TYPE, RULE_CLASSES, SITE_IDENTIFIER FROM SPP_LOGIN_RULE WHERE SITE_IDENTIFIER = ? AND RULE_TYPE = ?";
		Object result = DB.queryForObject(query, new UPSRuleMapper(), args, new int[] { Types.VARCHAR , Types.VARCHAR });	
		if (result != null && result instanceof UPSRule){
			return getRuleInstances(((UPSRule)result).getRuleClasses());
		}
		return null;
	}
	
	private List getRuleInstances(String result){	
		String[] classNameArray = result.split(":");
		List ruleInstances = new ArrayList();
		for(int i = 0; i <= classNameArray.length - 1 ; ++i){
			try{
				Class ruleClass = Class.forName(classNameArray[i]);
				Object ruleObject = ruleClass.newInstance();
				ruleInstances.add(ruleObject);
			}catch(Exception e){
				throw new IllegalStateException("Error Occured in initializing class", e);
			}
		}
		return ruleInstances;
	}
}