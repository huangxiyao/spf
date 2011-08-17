package com.hp.spp.portlets.groups;

import java.util.ResourceBundle;

/**
 * Class to store all the parameters of the group portlet
 * @author MJULIENS
 *
 */
public class GroupParameters {

	private static ResourceBundle mResource;
	
	static 
	{
		mResource = ResourceBundle.getBundle("SPP_ServicesWeb");
	}
	
	public static ResourceBundle getResource()
	{
		return mResource;
	}
}
