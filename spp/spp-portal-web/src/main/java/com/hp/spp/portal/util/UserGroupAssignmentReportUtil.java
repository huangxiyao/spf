package com.hp.spp.portal.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import com.epicentric.common.listgenerator.PagedIterator;
import com.epicentric.navigation.MenuItem;
import com.epicentric.site.Site;
import com.epicentric.site.SiteListGenerator;
import com.epicentric.site.SiteManager;
import com.epicentric.uid.UIDException;
import com.epicentric.uid.UIDRegistry;
import com.epicentric.user.UserGroup;
import com.epicentric.user.UserGroupManager;
import com.hp.spp.db.DB;
import com.hp.spp.db.RowMapper;

/**
 * This class prepares the Label list and Data list to generate the User Group Assignment Report
 * @author Srikanth Adepu
 * @version SPP 4.7.2
 *
 */

public class UserGroupAssignmentReportUtil {
	
	private static final Logger mLog = Logger.getLogger(UserGroupAssignmentReportUtil.class);
		
	/**
	 * Generates the data list for the zipped CSV file
	 * @param sqlQuery contains the query to return the menuitems or portlets 
	 * @param listType indicates the type of menuItemsList
	 * @param placeHolder current site DNS name
	 * @return list contains NavigationItemDetails objects
	 */
	public static List<NavigationItemDetails> generateResult(String sqlQuery, final String listType, String placeHolder) {
		
		if (mLog.isDebugEnabled())  {
			mLog.debug("UGAR-In generateResult() :: " +listType);
		}		
		Collection<String> groupUIDs = new ArrayList<String>();
		try {
			//To ignore other site groups from the query result, we are geting the current site group uids to 
			//compare with the groups return by ResultSet.
			groupUIDs = getGroupUIDsForSite(placeHolder);
			if (mLog.isDebugEnabled())  {
				mLog.debug("UGAR-Site specific group UIDs from getGroupUIDsForSite() :: " +groupUIDs);
			}
		} catch (Exception e) {
			mLog.error("Exception occured while calling getGroupUIDsForSite() :: " +e);
		}
		final Collection<String> siteGroupUIDs = groupUIDs;
		final List<NavigationItemDetails> result = new ArrayList<NavigationItemDetails>();		
		
		RowMapper<NavigationItemDetails> rowMapper = new RowMapper<NavigationItemDetails>() {
			public NavigationItemDetails mapRow(ResultSet row, int rowNum) throws SQLException {
								
				String groupUID = row.getString("GROUP_UID");
				
				if(siteGroupUIDs.contains(groupUID)) {//if the query result group matches with the site specific groups
					String menuItemTitle = row.getString("TITLE");
					String menuItemID = row.getString("ID");
					String menuItemGroup = row.getString("GROUP_NAME");
					String menuItemType = "";
					if (listType.equals("menulist")) {
						menuItemType = getMenuItemType(menuItemID);
					} else {
						menuItemType = "Portlet";
					}
					
					Collection<String> groupList = new ArrayList<String>();
					groupList.add(menuItemGroup);
					
					if((result!= null) && (result.size() > 0)) {
						NavigationItemDetails obj = result.get(result.size()-1);
						if(menuItemID.equals(obj.getId())) {
							result.remove(result.size()-1);
							Collection<String> grpList = obj.getGroupList();					
							grpList.add(menuItemGroup);
								
							//This method will iterate the groupList and returns the semicolon separated group collection.
							Collection<String> groupListSeparatedBySemicolon = getGroupListSeparatedBySemicolon(grpList);
							if (mLog.isDebugEnabled())  {
								mLog.debug("UGAR-Group List for "+menuItemID+" :: " +groupListSeparatedBySemicolon);
							}
							result.add(new NavigationItemDetails(menuItemID, menuItemTitle, menuItemType, groupListSeparatedBySemicolon));							
						} else {
							if (mLog.isDebugEnabled())  {
								mLog.debug("UGAR-Group List for "+menuItemID+" :: " +groupList);
							}
							result.add(new NavigationItemDetails(menuItemID, menuItemTitle, menuItemType, groupList));
						}
					} else {
						if (mLog.isDebugEnabled())  {
							mLog.debug("UGAR-Group List for "+menuItemID+" :: " +groupList);
						}
						result.add(new NavigationItemDetails(menuItemID, menuItemTitle, menuItemType, groupList));
					}						
					if (mLog.isDebugEnabled())  {
						mLog.debug("UGAR-Final result list size in generateResult() :: " +result.size());
					}
					//just return null as we create this result ourselves
				}				
				return null;
			}
		};
		//do the query
		DB.query(sqlQuery, rowMapper, new Object[] {placeHolder}, new int[] {Types.VARCHAR} );			
		return result;
	}
	
	/**
	 * Get menuitem type for the zipped CSV file
	 * @param menuItemID indicated the unique id of the menuitem 
	 * @return String cotains the type of menuitem
	 */	
	public static String getMenuItemType(String menuItemID) {
		MenuItem menuItem = null;
		String menuItemType = "";
		
		UIDRegistry aUIDRegistry  = UIDRegistry.getInstance();							
		try {
			menuItem = (MenuItem)aUIDRegistry.getObjectFromUID(menuItemID);
			menuItemType = (menuItem.getLinkType()).replace("linktype_", "");
			if (mLog.isDebugEnabled())  {
				mLog.debug("UGAR-Menuitem Type in getMenuItemType() :: "+menuItemID+"---"+menuItemType);
			}
		} catch (UIDException aUIDException) {
			mLog.error("UIDException occured while retrieving MenuItem("+ menuItemID +") from UIDRegistry" +aUIDException.getMessage());					
		}				
		return menuItemType;
	}

	/**
	 * Iterates the groupList and returns the semicolon separated group collection.
	 * @param groupList contains the list of groups for the perticular menu item.
	 * @return Collection
	 */	
	public static Collection<String> getGroupListSeparatedBySemicolon(Collection<String> groupList) {
		if (mLog.isDebugEnabled())  {
			mLog.debug("UGAR-groupList before iterating in getGroupListSeparatedBySemicolon() :: " + groupList);
		}		
		String allGroups = "";
		for(String group : groupList) {
			allGroups = allGroups+group+""+UserGroupAssignmentReportConstants.GROUP_SEPARATOR;
		}
		allGroups = allGroups.substring(0, allGroups.length()-1);//to remove the last GROUP_SEPARATOR
		
		Collection<String> groupListSeparatedBySemicolon = new ArrayList<String>();
		groupListSeparatedBySemicolon.add(allGroups);		
		
		if (mLog.isDebugEnabled())  { 
			mLog.debug("UGAR-groupList after iterating in getGroupListSeparatedBySemicolon() :: " + groupListSeparatedBySemicolon);
		}
		return groupListSeparatedBySemicolon;
	}
	
	/**
	 * Generates the collection of groups for site.
	 * @param site Site object
	 * @return Collection
	 */	
	 public static Collection<UserGroup> getGroupsForSite(Site site) {
		 
		 Collection<UserGroup> groups = new ArrayList<UserGroup>();
		 try {
	        SiteListGenerator list = (SiteListGenerator)site.getListGeneratorForType(UserGroupManager.getInstance().getUserGroupEntityType().getID());
	        PagedIterator pagedIterator = list.getList("title", true, Collections.EMPTY_LIST, Integer.MAX_VALUE);
	        while (pagedIterator.hasNextPage()) {
	            groups.addAll(pagedIterator.nextPage());
	        }
			if (mLog.isDebugEnabled())  {
				mLog.debug("UGAR-Site specific groups collection size in getGroupsForSite() :: " + groups.size());
			}
		} catch (Exception e) {
			 mLog.error("Exception occured in getGroupsForSite() while getting the UserGroups for the "+site.getDNSName()+" :: " +e);
		 }
		return groups;
	  }
	 
	 /**
	 * Generates the collection of groups Unique IDs for site.
	 * @param siteDNSName current site DNS name
	 * @return Collection
	 */
	 public static Collection<String> getGroupUIDsForSite(String siteDNSName) {
		 
		 Collection<String> groupUIDs = new ArrayList<String>();
		 try {
			 Site securedSite = SiteManager.getInstance().getSiteFromDNSName(siteDNSName);
			 for (UserGroup ug : getGroupsForSite(securedSite)) {
				groupUIDs.add(ug.getUID());
		     }			
			 if (mLog.isDebugEnabled())  {
					mLog.debug("UGAR-Site specific group unique ids size in getGroupUIDsForSite() :: " + groupUIDs.size());
			 }
		 } catch (Exception e) {
			 mLog.error("Exception occured in getGroupUIDsForSite() while getting the site object for the "+siteDNSName+" :: " +e);
		 }
		 return groupUIDs;

	 }


}
