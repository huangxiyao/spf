package com.hp.spp.portal.login.dao;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/*
 * Concrete OSCache  implementation of the UPSRuleDAO interface that 
 * fetches rule from the database using the <em>delegate</em> UPSRuleDAO
 * instance 
 * @author Adi/girishsk
 * @since SPP 4.0
 * @see com.hp.spp.portal.login.dao.UPSRuleDAO
 */

public class UPSRuleDAOCacheImpl implements UPSRuleDAO {
	private UPSRuleDAO mDelegate;
	private GeneralCacheAdministrator mCache;
	private static final String mRulePrefix = "UPSRULES";
	
	public UPSRuleDAOCacheImpl(UPSRuleDAO delegate, GeneralCacheAdministrator cache){
		this.mDelegate = delegate;
		this.mCache = cache;
	}
	
	
	public List getUPSRules(String siteIdentifier) {
		List result = new ArrayList();
		
		String key = mRulePrefix + "-" + siteIdentifier;
	
		try {
			result = (List) mCache.getFromCache(key);
		}
		catch (NeedsRefreshException e) {
			boolean updated = false;
			try {
				result = mDelegate.getUPSRules(siteIdentifier);
				mCache.putInCache(key, result);
				updated = true;
			}
			finally {
				if (!updated) {
					mCache.cancelUpdate(key);
				}
			}
		}
		return result;
	}
		
	
	
	
}