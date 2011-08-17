package com.hp.spp.portal.util;

/**
 * Class to store all the public static final variables used in User Group Assignment Report.
 * @author Srikanth Adepu
 * @version SPP 4.7.2
 *
 */
public class UserGroupAssignmentReportConstants {	
	
	//Default name of zip file
	public static final String DEFAULT_ZIP_FILE_NAME = "UserGroupAssignmentReport.zip";
	
	//The CSV seperator value
	public static final String CSV_SEPARATOR = ",";
	
	//Deafult date format.
	public static final String DEFAULT_DATE_FORMAT= "yyyyMMdd";
	
	//The Group seperator value
    public static final String GROUP_SEPARATOR = ";";
	
	//This query returns the Secured site menu items with the associated groups.
	public static final String MENUITEMS_QUERY = " select distinct (select value_small from settingsdictionary where uniqueid = " +
												 "(select value1_dictionary from settingscontents where uniqueid = a.settings_id and settings_key = 'menuitem_setting_title')) " +
												 "TITLE, a.id ID, usergroups.name GROUP_NAME, usergroups.UNIQUE_ID GROUP_UID from menuitem a, usergroups, authorizationmanagement " +
												 "where a.id in (select menu_item_id from MENUITEMASSOCIATION  m " +
												 "start with m.menu_id = (select distinct menu from REPOSITORYELEMREF rep, Sites s " +
												 "where s.MAIN_REPOSITORY =rep.SOURCE_REPOSITORY and s.DNS_NAME in ( ? ) ) " +
												 "connect by prior menu_item_id=menu_id) and authorizationmanagement.grantee = usergroups.unique_id " +
												 "and authorizationmanagement.permissionable in (a.id) " +
												 "and authorizationmanagement.permission = 'std:menu_item:enabled' order by 1,2 ";	
	
	//This query returns the Secured or Unsecured site portlets with the associated groups.
	public static final String PORTLETS_QUERY = "SELECT distinct p.TITLE TITLE, p.PORTLET_UID ID, ug.NAME GROUP_NAME, ug.UNIQUE_ID GROUP_UID " + 
											  "FROM AUTHORIZATIONMANAGEMENT authmgmt, USERGROUPS ug, PORTLETS p, SITES s, REPOSITORYELEMREF rep "+
											  "WHERE authmgmt.GRANTEE = ug.UNIQUE_ID AND p.PORTLET_UID = authmgmt.PERMISSIONABLE AND " +
											  "p.PORTLET_UID = rep.REPOSITORY_ELEMENT AND s.MAIN_REPOSITORY =rep.TARGET_REPOSITORY AND " +
											  "authmgmt.CONTEXT != 'epi_authorization_system_context' AND " +
											  "authmgmt.permission = 'std:module:enabled' AND " +
											  "s.DNS_NAME in ( ? ) order by p.TITLE, p.PORTLET_UID ";
	
	public static final String SPP_UGSREPORT_SECRETKEY = "Pmjq3PcyPb+UyB8CE/2/eT5o6tz3Mj2/";
	
}
