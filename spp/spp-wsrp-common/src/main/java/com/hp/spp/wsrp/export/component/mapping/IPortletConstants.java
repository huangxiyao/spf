package com.hp.spp.wsrp.export.component.mapping;

import java.util.Arrays;
import java.util.List;

interface IPortletConstants {

	public static final String KIND_REMOTE = "REMOTE" ;
	
	public static final String KIND_LOCAL = "LOCAL" ;
	
	public static final String JSR_PORTLET = "JSR" ;
	
	public static final String PORTAL_BEAN = "PORTALBEAN" ;
	
	public static final String WSRP_PORTLET = "WSRP" ;
	
	public static final List LIST_OF_TYPES = Arrays.asList(
			new Object[] {
					JSR_PORTLET, // String
					PORTAL_BEAN, // String
					WSRP_PORTLET, // String
			}) ;
		
}
