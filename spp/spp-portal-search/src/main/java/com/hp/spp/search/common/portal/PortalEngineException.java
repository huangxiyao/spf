package com.hp.spp.search.common.portal;

/**
 * This represents the system level exceptions that may 
 * occur when a PortalEngine Object operates. 
 * This represents conditions which are not dependent upon the 
 * passed in parameters but conditions which may have to deal with some 
 * problem in accessing and using the various APIs from the Portal vendor.
 * 
 * @author Akash
 * @version %I%, %G%
 */
public class PortalEngineException extends Exception {

	public PortalEngineException(String string) {
		super(string);
	}

}
