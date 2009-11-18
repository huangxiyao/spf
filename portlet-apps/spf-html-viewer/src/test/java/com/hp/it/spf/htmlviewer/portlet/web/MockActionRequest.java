package com.hp.it.spf.htmlviewer.portlet.web;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The Spring JSR-286 release is not final yet as of this writing - and we need
 * a JSR-286 Spring mock method, getWindowID - so rather than introduce a
 * dependency on Spring Beta code, we just have our own wrapper class for this.
 */
public class MockActionRequest extends
		org.springframework.mock.web.portlet.MockActionRequest {

	public String getWindowID() {
		return "12345";
	}
	
	public Map<String, String[]> getPrivateParameterMap() {
	    Map<String, String[]> m = new LinkedHashMap<String, String[]>();	
	    m.put("privateParam1", new String[]{"privateParam1value"});
	    m.put("privateAttr1", new String[]{"privateAttr1value"});
	    m.put("privateAttr2", new String[]{"privateAttr2value1","privateAttr2value2"});
	    return m;
	}

	public Map<String, String[]> getPublicParameterMap() {
	    Map<String, String[]> m = new LinkedHashMap<String, String[]>();	
	    m.put("publicParam1", new String[]{"param1value"});
	    m.put("publicAttr1", new String[]{"attr1value"});
	    m.put("publicAttr2", new String[]{"attr2value1","attr2value2"});
	    return m;
	}
}
