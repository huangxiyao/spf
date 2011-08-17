package com.hp.spp.groups.expression;

import java.util.ArrayList;
import java.util.List;

/**
 * Connector.
 * @author bmollard
 * 
 */
public abstract class Connector implements Expression {
	/**
	 * List of Expressions.
	 */
	private List mExpressions = new ArrayList();

	/**
	 * @param e - Expression to be added
	 */
	public void addExpression(Expression e) {
		mExpressions.add(e);
	}

	public List getExpressions() {
		return mExpressions;
	}
	
	public void getInGroupNames(ArrayList list) {
		if (getExpressions()!=null)
		{
			for (int i=0; i<getExpressions().size(); i++)
			{
				Expression exp = (Expression) getExpressions().get(i);
				exp.getInGroupNames(list);
			}
		}
	}
}
