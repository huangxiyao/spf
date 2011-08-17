package com.hp.spp.groups.expression;


import java.util.Iterator;
import java.util.Map;

/**
 * Represents a Disjunction.
 * @author bmollard
 *
 */
public class Disjunction extends Connector {
	/**
	 * Return true if the user context correpsonds to this disjuction.
	 * @return true if OK for the user context
	 * @param userContext user Context
	 * @param siteName name of the site
	 */
	public boolean isTrue(Map userContext, String siteName) {
		for (Iterator it = getExpressions().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			if (e.isTrue(userContext, siteName)) {
				return true;
			}
		}
		return false;
	}
}
