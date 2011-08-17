package com.hp.spp.eservice.el;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.el.FunctionMapper;

public class SPPFunctionMapper implements FunctionMapper {

	private Map mFunctionMap = new HashMap();

	public SPPFunctionMapper() throws Exception {
			buildMap();
	}

	private void buildMap() {
		//Add SPP functions with spp prefix
		Method methods[] = SPPFunctions.class.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method m = methods[i];
			if (Modifier.isStatic(m.getModifiers())) {
				mFunctionMap.put("spp:" + m.getName(), m);
			}
		}
		//Add Generic functions without prefix
		methods = GenericFunctions.class.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method m = methods[i];
			if (Modifier.isStatic(m.getModifiers())) {
				mFunctionMap.put(":" + m.getName(), m);
			}
		}
	}

	public Method resolveFunction(String prefix, String localName) {
		return (Method) mFunctionMap.get(prefix + ':' + localName);
	}

}