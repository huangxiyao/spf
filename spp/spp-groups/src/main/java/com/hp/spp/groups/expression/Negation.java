package com.hp.spp.groups.expression;


import java.util.ArrayList;
import java.util.Map;

/**
 * Used for negation expressions.
 * @author bmollard
 *
 */
public class Negation implements Expression {
	
	/**
	 * The expression.
	 */
	private Expression mExpression;

	/**
	 * constructor.
	 */
	public Negation() {
	}

	public Expression getExpression() {
		return mExpression;
	}

	public void setExpression(Expression expression) {
		mExpression = expression;
	}

	/**
	 * Return true if the user context corresponds to this negation.
	 * @return true if OK for the user context
	 * @param userContext user Context
	 * @param siteName name of the site
	 */
	public boolean isTrue(Map userContext, String siteName) {
		return !mExpression.isTrue(userContext, siteName);
	}

	public void getInGroupNames(ArrayList list) {
		if (mExpression!=null)
		{
			mExpression.getInGroupNames(list);
		}
	}
}
