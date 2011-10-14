package com.hp.it.cas.persona.user.service;

import java.util.Map;

/**
 * A compound user attribute value is a structure, for example an <em>address</em> containing <em>city</em> and <em>state</em>. A compound attribute value
 * can be manipulated as a unit. Each compound attribute has a unique instance identifier so that the value can be distinguished from others
 * stored with the same attribute.
 *
 * @author Quintin May
 */
public interface ICompoundUserAttributeValue extends Map<String, String> {
	
	/**
	 * Returns the unique identifier for this compound attribute value instance.
	 * @return the instance identifier.
	 */
	String getInstanceIdentifier();
	
	/**
	 * Removes this compound attribute value and all of its components from the user.
	 */
	void remove();
}
