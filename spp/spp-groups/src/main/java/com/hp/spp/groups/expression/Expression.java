package com.hp.spp.groups.expression;


import java.util.ArrayList;
import java.util.Map;

/**
 * @author bmollard
 *
 */
public interface Expression {
	
	/**
	 * @param userContext - the user Profile with its attributes
	 * @param siteName - the name of the site
	 * @return true or false
	 */
	boolean isTrue(Map userContext, String siteName);
	
	/**
	 * This method allows to get all the group refrecences by this espression
	 * This referenced groups exists only in hte inGroup expression
	 * This method is recursive
	 */
	public void getInGroupNames(ArrayList list);
}
