package com.hp.spp.wsrp.export.component.mapping;

import java.util.Arrays;
import java.util.List;

interface IComponentTypeConstants {

	public static final String GRIDS = "Grids" ;

	public static final String JSP_INCLUDES = "JSP Includes" ;

	public static final String MENUS = "Menus" ;

	public static final String MODULETYPES = "ModuleTypes" ;

	public static final String PAGES = "Pages" ;

	public static final String PORTLET = "Portlet" ;

	public static final String PORTLETTYPE = "PortletType" ;

	public static final String SECONDARY_PAGE_TYPES = "Secondary Page Types" ;

	public static final String SECONDARY_PAGES = "Secondary Pages" ;

	public static final String SETTINGS = "Settings" ;

	public static final String SITES = "Sites" ;

	public static final String STYLE_TYPES = "Style Types" ;

	public static final String STYLES = "Styles" ;

	public static final String THEMES = "Themes" ;
	
	public static final List LIST_OF_TYPES = Arrays.asList(
			new Object[] {
					GRIDS, // String
					JSP_INCLUDES, // String
					MENUS, // String
					MODULETYPES, // String
					PAGES, // String
					PORTLET, // String
					PORTLETTYPE, // String
					SECONDARY_PAGE_TYPES, // String
					SECONDARY_PAGES, // String
					SETTINGS, // String
					SITES, // String
					STYLE_TYPES, // String
					STYLES, // String
					THEMES // String
			}) ;
		
}
