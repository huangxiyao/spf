package com.hp.spp.eservice.core.expression;


import java.util.Map;

import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.ExpressionEvaluator;

import org.apache.commons.el.ExpressionEvaluatorImpl;

public class ExpressionResolver {

	// private Map mMap = new HashMap();
	private ExpressionEvaluator _ee;

	private SPPVariableResolver _vr;

	// private FunctionMapperImpl _fm;

	public ExpressionResolver(Map newMap) {

		_ee = new ExpressionEvaluatorImpl();
		_vr = new SPPVariableResolver();
		_vr.setMVariables(newMap);

	}

	public Object eval(String expression, Class expectedType) throws ELException {
		return _ee.evaluate(expression, expectedType, _vr, null);
	}

}
