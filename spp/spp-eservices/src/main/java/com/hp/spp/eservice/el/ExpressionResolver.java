package com.hp.spp.eservice.el;

import java.util.Map;

import javax.servlet.jsp.el.ELException;
import javax.servlet.jsp.el.ExpressionEvaluator;

import org.apache.commons.el.ExpressionEvaluatorImpl;

/**
 * Class that resolve all SPP expression for Eservice
 * 
 * @author Adrien geymond
 */
public class ExpressionResolver {
	
	private ExpressionEvaluator _ee;
	private SPPVariableResolver _vr;
	private SPPFunctionMapper _fm;

	//FIXME (slawek) - commenting this out as it is not used. Will remove it later.
//	private static Set mGroupSet = null;

	public ExpressionResolver(Map userContext) throws ELException {
		_ee = new ExpressionEvaluatorImpl();
		_vr = new SPPVariableResolver();
		try{
			_fm = new SPPFunctionMapper();
		}catch (Exception e){
			throw new ELException("Problem during method lookup :"+e.getMessage());
		}
	
		_vr.setMVariables(userContext);

/*
		mGroupSet = new HashSet();
		String groupsString = (String) userContext.get("UserGroups");
		if (groupsString!=null){
			String[] groupsArray = groupsString.split(",");
			for (int i = 0; i < groupsArray.length; i++) {
				mGroupSet.add(groupsArray[i]);
			}
		}
*/
	}

	public Object eval(String expression, Class expectedType) throws ELException {
		return _ee.evaluate(expression, expectedType, _vr, _fm);
	}

}
