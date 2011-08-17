package com.hp.spp.eservice.core.expression;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.VariableResolver;

public class SPPVariableResolver implements VariableResolver {

	private Map mVariables = new HashMap();

	public Object resolveVariable(String pName) throws ELException {
		if (mVariables.containsKey(pName)) {
			return mVariables.get(pName);
		}
		throw new ELException("Variable '" + pName + "' not found");
	}

	public void setMVariables(Map variables) {
		mVariables = variables;
	}

	public Map getMVariables() {
		return mVariables;
	}

	
}
