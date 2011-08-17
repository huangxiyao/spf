/**
 *
 */
package com.hp.spp.search.vap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import com.epicentric.authorization.AuthorizationException;
import com.epicentric.authorization.PrincipalSet;
import com.epicentric.common.AuthorizationDictionary;
import com.epicentric.common.website.AuthorizationUtils;
import com.epicentric.common.website.I18nUtils;
import com.epicentric.entity.EntityNotFoundException;
import com.epicentric.entity.EntityPersistenceException;
import com.epicentric.i18n.EditableLocalizedBundle;
import com.epicentric.navigation.MenuItem;
import com.epicentric.site.SiteException;
import com.epicentric.site.SiteManager;
import com.epicentric.template.Style;
import com.epicentric.uid.UIDException;
import com.epicentric.uid.UIDRegistry;
import com.epicentric.uid.UniquelyIdentifiable;
import com.epicentric.user.UserGroup;
import com.epicentric.user.UserGroupManager;
import com.epicentric.user.UserGroupQueryResults;
import com.epicentric.user.UserManager;
import com.hp.spp.profile.Constants;
import com.hp.spp.search.common.NavigationItem;
import com.hp.spp.search.common.Site;
import com.hp.spp.search.common.User;
import com.hp.spp.search.common.portal.ItemNotIndexableException;
import com.hp.spp.search.common.portal.PortalEngine;
import com.hp.spp.search.common.portal.PortalEngineException;

 //importing the Contstants interface


/**
 * This class is a concrete implementation of the PortalEngine interface. This
 * class is Vignette specific. It uses the Vignette APIs to implement the
 * contract defined in the interface.
 *
 * @author Akash
 *
 */

/** Revision History:
 *
 * Ver.   Modified By           Date           Notes
 *-----------------------------------------------------------------------------*
 * v1     Akash   		      11-Sep-2006      Created
 * v2     Shivashanker B      20-Sep-2006      Added getNavigationItem() method.
 */
public class VignettePortalEngine implements PortalEngine {
	private static final String SEARCH_KEY_WORDS = "SearchKeyWords";
	private static final String INDEX_FOR_SEARCH = "IndexForSearch";


	/**
	 * This method creates and populates values for NavigationItem.
	 * @see com.hp.spp.search.common.portal.PortalEngine#getNavigationItem(java.lang.String,
	 *      java.lang.String,java.lang.String,java.lang.String)
	 */
	public NavigationItem getNavigationItem(String navItemId, String siteName,
				String languageCode,String countryCode) throws PortalEngineException{


		 //Get the MenuItem from NavigationItemID.
		MenuItem aMenuItem = null;
		String docType = "Page";
		Long lastUpdate = -1L;
		
	    UIDRegistry aUIDRegistry  = UIDRegistry.getInstance();
		try {
			aMenuItem = (MenuItem)aUIDRegistry.getObjectFromUID(navItemId);
			String linkReference = aMenuItem.getLinkReference();
			if( null != linkReference){
				UniquelyIdentifiable item = aUIDRegistry.getObjectFromUID(linkReference);
				if( item.getUIDType().equals("JSP Includes")){
					String title = ((Style)item).getTitle(); 
					if( null != title){
						if( title.contains("EserviceProxy")){
							docType = "Application";
						}
					}
				}
				 lastUpdate = aMenuItem.getLastUpdate();

			}
		} catch (UIDException aUIDException) {
			//This may happen if the navigation item has been deleted by the admin.
			throw new PortalEngineException(
				"UIDException occured while retrieving MenuItem("+ navItemId +") from UIDRegistry");
		}
		
		//Create the NavigationItem and populate with values.
		NavigationItem aNavigationItem  = new NavigationItem();

	    //Set the Navigation Item ID
		aNavigationItem.setId(aMenuItem.getUID());
		aNavigationItem.setDocumentType(docType);
		aNavigationItem.setLastUpdate(lastUpdate);
		String url = "/portal/site/" + siteName + "/menuitem."+ aMenuItem.getUID(); 
		aNavigationItem.setUrl(url);

		//Get the all the  groups which have enabled access right to the navigation item
		Collection groupList = new ArrayList();
		UserGroupQueryResults uRslt = null;
		PrincipalSet pGrpSet = null;
		UserGroupManager ugMgr = UserGroupManager.getInstance();
		try {
			uRslt = ugMgr.getAllUserGroups();
		} catch (EntityPersistenceException aEntityPersistenceException) {
			throw new PortalEngineException(
					"EntityPersistenceException occured while retrieving user groups from "+
					"persistence store for menu item : "+aMenuItem.getTitle());
		}
		com.epicentric.site.Site aSite = null;
		try {
			aSite = SiteManager.getInstance().getSiteFromDNSName(siteName);
		} catch (SiteException aSiteException) {
			throw new PortalEngineException(
					"SiteException occured while retrieving " + siteName +
					" from persistence store for menu item : "+ aMenuItem.getTitle());
		}
		
		//Check if the IndexForSearch attribute is defined as False.
		//If so, throw ItemNotIndexableException which is sub-class of PortalEngineException
		Locale	aCurrentLocale= I18nUtils.getCurrentLocale(aSite);
		
		//To get MenuItemNode resource bundle properties
		EditableLocalizedBundle aLocalizedBundleForIndexFlag =
			EditableLocalizedBundle.getBundle(navItemId, aCurrentLocale);
		
		String indexForSearch = aLocalizedBundleForIndexFlag.getString(INDEX_FOR_SEARCH,"IndexForSearch_Not_Defined");
		if(indexForSearch != null && (indexForSearch.trim()).equalsIgnoreCase("false")){
			//Throw the ItemNotIndexableException
			throw new ItemNotIndexableException("This navigation item "+navItemId+" defines the value of False for the attribute IndexForSearch");
			
		}
		//END - check for IndexForSearch parameter.
		
		while (uRslt.hasNext()) {
			UserGroup uGp = (UserGroup)uRslt.next();
			try {
				pGrpSet = uGp.getAllParentPrincipals(true);
			} catch (AuthorizationException aAuthorizationException) {
				throw new PortalEngineException(
					"AuthorizationException thrown from iterateAndFilterItems() " +
					" when getting user's principal set");
			}
			boolean menuItemEnabledForGroup = AuthorizationUtils.isAuthorized(
				aMenuItem, pGrpSet, aSite,"enabled");
			if(menuItemEnabledForGroup){
				String groupTitle= (String)uGp.getProperty(UserGroup.TITLE_PROPERTY_ID);
				groupList.add(groupTitle);
			}
		}//while
		if(groupList!=null && groupList.size() > 0){
			aNavigationItem.setGroupList(groupList);
		}else{
			//We assumed that navigation item will belong to at least one group.
			// throw new PortalEngineException("Groups are not available for the menu item ("+ aMenuItem.getTitle()+")");
		}
		//Get the handle to the locale specific resource bundle.
		Locale aLocale =  new Locale(languageCode,countryCode);
		if(aLocale==null){
			aLocale = I18nUtils.getCurrentLocale(aSite);
		}
		EditableLocalizedBundle aEditableLocalizedBundle =
			EditableLocalizedBundle.getBundle(aMenuItem.getUID(),aLocale);
		/*
		EditableLocalizedBundle aEditableLocalizedBundle =
			EditableLocalizedBundle.getBundle(aMenuItem.getStyleID(),aLocale);
		*/


		//Set the title of the navigation item
		aNavigationItem.setTitle(aEditableLocalizedBundle.getString("title",Constants.TITLE_NOT_DEFINED));

		//Set the description of the navigation item.
		aNavigationItem.setDescription(aEditableLocalizedBundle.getString("description",Constants.DESCRIPTION_NOT_DEFINED));


		//set the locale object.
		aNavigationItem.setLocale(aLocale);

		//Get the search key words which are configured in resource bundle of navigation item
		//for a specific locale.
		String searchKeywords = aEditableLocalizedBundle.getString(SEARCH_KEY_WORDS, Constants.SEARCHKEYWORDS_NOT_DEFINED);
		aNavigationItem.setSearchKeywordsString(searchKeywords);
	 	return aNavigationItem;
	}

	/**
	 * @throws PortalEngineException
	 * @see com.hp.spp.search.common.portal.PortalEngine#audienceItems(com.hp.spp.search.common.Site,
	 *      com.hp.spp.search.common.User, java.util.Collection)
	 */
	public List audienceItems(Site site, User user, List navItems) throws PortalEngineException {
		List filteredItems = null;
		// Check the parameters
		if (site == null || user == null || navItems == null) {
			// Need to log
			throw new IllegalArgumentException("One or more arguments are null");
		}

		// Step 1: Get the Vignette Site reference from the passed in Site
		// object.
		com.epicentric.site.Site aSite = getVignetteSite(site);

		// Step 2: Get the Vignette user from the passed in User object
		String userHppId = user.getHppId();

		if (userHppId == null || userHppId.length() == 0) {
			// Need to log
			throw new IllegalArgumentException(
					"The HPPID passed in the User object is null or blank");
		}
		com.epicentric.user.User vapUser = null;

		try {
			vapUser = getUserFromProperty(Constants.VGN_HPPID, userHppId);

		} catch (EntityPersistenceException e) {
			throw new IllegalArgumentException(
			"EntityPersistenceException: The user object passed in cannot be resolved as a Vignette user "+
				" property name = "+Constants.VGN_HPPID+" property value = "+userHppId);

		} catch (EntityNotFoundException e) {
			throw new IllegalArgumentException(
					"EntityNotFoundException: The user object passed in cannot be resolved as a Vignette user "+
					" property name = "+Constants.VGN_HPPID+" property value = "+userHppId);
		}

		// Step 3: Iterate through the list of NavigationItems and return the
		// ones to which the given user has access to.
		filteredItems = iterateAndFilterItems(aSite, vapUser, navItems);
		return filteredItems;
	}

	private com.epicentric.site.Site getVignetteSite(Site site) {
		String dnsName = site.getName();
		com.epicentric.site.Site aSite = null;
		if (dnsName == null || dnsName.length() == 0) {
			// Need to log
			throw new IllegalArgumentException(
					"The dns name passed in the site object is null or blank");
		}

		SiteManager aSiteManager = SiteManager.getInstance();
		try {
			aSite = aSiteManager.getSiteFromDNSName(dnsName);
		} catch (SiteException e) {
			// Log
			throw new IllegalArgumentException(
					"The site object passed in cannot be resolved");
		}

		return aSite;
	}

	private UniquelyIdentifiable getObjectFromUID(String uid)
			throws UIDException {
		UniquelyIdentifiable uniqueObj = null;
		UIDRegistry uidRegistry = UIDRegistry.getInstance();
		uniqueObj = uidRegistry.getObjectFromUID(uid);
		return uniqueObj;
	}

	/*
	 * This method iterates through the list of Navigation item ids passed in,
	 * calculates if the user has access to it and if yes puts it in the list
	 * of 'allowed' navigation items. If one or more navigation item Ids passed
	 * as input parameters do not exist in Vignette or are null or empty, this
	 * method simpy skips them.
	 *
	 */
	private List iterateAndFilterItems(com.epicentric.site.Site aSite,
			com.epicentric.user.User vapUser, List navItems) throws PortalEngineException{
		Iterator navItr =  navItems.iterator();
		NavigationItem navItem = null;
		String navUid = null;
		MenuItem aMenuItem = null;

		List listToReturn = new ArrayList();
		/*
		 * Step 1: get the user's principal set. If there is an exception in
		 * getting the principal set, throw the system exception, PortalEngineException.
		 */
        PrincipalSet pSet = null;
        try
        {
        	//Check what happens if 'false' is passed in
            pSet = vapUser.getAllParentPrincipals(true);
        }
        catch(AuthorizationException e)
        {
        	//Throw the PortalEngineException
            throw new PortalEngineException("AuthorizationException thrown from iterateAndFilterItems() " +
            		"when getting user's principal set");
        }

		while(navItr.hasNext()) {
			navItem = (NavigationItem)navItr.next();
			navUid = navItem.getId();

			//Step 2: Get the menu item
			try {
				aMenuItem = (MenuItem) getObjectFromUID(navUid);
			}catch (UIDException uidexception) {
				// Log as debug using Vignette APIs..?
				//Suppress this exception. Continue
				continue;
			}
			//Step 3: calculate user's access to this MenuItem
			boolean isAuthorized = AuthorizationUtils.isAuthorized
									(aMenuItem, pSet, aSite, AuthorizationDictionary.ENABLED);
			//If user is authorized to see this MenuItem, return it, else do nothing
			if(isAuthorized){
				listToReturn.add(navItem);
			}
		}
		return listToReturn;
	}

	private com.epicentric.user.User getUserFromProperty(String propertyName, String propertyValue) throws EntityPersistenceException, EntityNotFoundException {
		UserManager userManager = UserManager.getInstance();

		com.epicentric.user.User userFromProperty = userManager.getUser(propertyName, propertyValue);
		return userFromProperty;
	}
}
