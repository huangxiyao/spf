package com.hp.spp.common.util;

/**
 * Class to store all the public final statics variables commmon to all the SPP projects.
 * @author MJULIENS
 *
 */
public class SppConstants {

	/**
	 * Name of the logger to send email alert
	 */
	public final static String SPP_EMAIL_LOG_NAME = "sppEmailLog";
	
	/**
	 * Name of the UserContextKeyx Map stored in the request by the PassingParameters system
	 */
	public final static String SPP_USERCONTEXTKEYS_MAP_NAME = "com.hp.spp.UserContextKeys";
	
	/**
	 * Name of the attribute that contains the site name in the UserContext map
	 */
	public final static String SPP_USERCONTEXTKEYS_KEY_SITE_NAME = "PortalSite";
	
	/**
	 * Default site to use if not site can be read from the passing parameters
	 * This constant must be set to null (not initialized), if no default site in selected 
	 */
	//public final static String SPP_DEFAULT_SITE_NAME = "smartportal";
	
}
