package com.hp.spp.groups.expression;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Represents a Conjunction.
 * 
 * @author bmollard
 * 
 */
public class Conjunction extends Connector {
	/**
	 * Return true if the user context correpsonds to this conjuction.
	 * @return true if OK for the user context
	 * @param userContext user Context
	 * @param siteName name of the site
	 */
	public boolean isTrue(Map userContext, String siteName) {
		for (Iterator it = getExpressions().iterator(); it.hasNext();) {
			Expression e = (Expression) it.next();
			if (!e.isTrue(userContext, siteName)) {
				return false;
			}
		}
		return true;
	}
}
