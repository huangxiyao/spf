package com.hp.it.cas.persona.mock.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.hp.it.cas.config.dao.App;
import com.hp.it.cas.config.dao.AppKey;
import com.hp.it.cas.security.dao.AppCmpndAttrSmplAttrPrmsn;
import com.hp.it.cas.security.dao.AppCmpndAttrSmplAttrPrmsnKey;
import com.hp.it.cas.security.dao.AppUserAttrPrmsn;
import com.hp.it.cas.security.dao.AppUserAttrPrmsnKey;
import com.hp.it.cas.security.dao.CmpndAttrSmplAttr;
import com.hp.it.cas.security.dao.CmpndAttrSmplAttrKey;
import com.hp.it.cas.security.dao.UserAttr;
import com.hp.it.cas.security.dao.UserAttrKey;
import com.hp.it.cas.security.dao.UserAttrValu;
import com.hp.it.cas.security.dao.UserAttrValuKey;

public class MockDatabase {
	final Map<AppKey, App> appTable = new HashMap<AppKey, App>();
	final Map<CmpndAttrSmplAttrKey, CmpndAttrSmplAttr> cmpndAttrSmplAttrTable = new HashMap<CmpndAttrSmplAttrKey, CmpndAttrSmplAttr>();
	final Map<AppCmpndAttrSmplAttrPrmsnKey, AppCmpndAttrSmplAttrPrmsn> appCmpndAttrSmplAttrPrmsnTable = new HashMap<AppCmpndAttrSmplAttrPrmsnKey, AppCmpndAttrSmplAttrPrmsn>();
	final Map<AppUserAttrPrmsnKey, AppUserAttrPrmsn> appUserAttrPrmsnTable = new HashMap<AppUserAttrPrmsnKey, AppUserAttrPrmsn>();
	final Map<UserAttrKey, UserAttr> userAttrTable = new HashMap<UserAttrKey, UserAttr>();
	final Map<UserAttrValuKey, UserAttrValu> userAttrValuTable = new HashMap<UserAttrValuKey, UserAttrValu>();
	
	public MockDatabase() {
		reset();
	}
	
	public void reset() {
		appTable.clear();
		cmpndAttrSmplAttrTable.clear();
		appCmpndAttrSmplAttrPrmsnTable.clear();
		appUserAttrPrmsnTable.clear();
		userAttrTable.clear();
		userAttrValuTable.clear();
		
		loadReferenceData();		
	}
	
	private void loadReferenceData() {
		App app = new App();
		AppKey key = new AppKey();
		key.setAppPrtflId(new BigDecimal(999999));
		app.setKey(key);
		appTable.put(key, app);
	}
}
