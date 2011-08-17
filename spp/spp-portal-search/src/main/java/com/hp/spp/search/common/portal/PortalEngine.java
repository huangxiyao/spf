/**
 *
 */
package com.hp.spp.search.common.portal;

import java.util.List;

import com.hp.spp.search.common.NavigationItem;
import com.hp.spp.search.common.Site;
import com.hp.spp.search.common.User;

/**
 * This interface exposes all services expected of a portal engine.
 * Any concrete implementation of this interface will be for a
 * particular portal vendor.
 * @author Akash
 * @version %I%, %G%
 */

/** Revision History:
 *
 * Ver.   Modified By           Date           Notes
 *--------------------------------------------------------------------------------------*
 * v1     Akash   		      11-Sep-2006      Created
 * 
 */ 					
public interface PortalEngine {
	/**
	 * This method returns a particular NavigationItem.
	 * It takes as input the identifier of the navigation item
	 * and the site identifier & its locale properties such as language code 
	 * and country code.
	 * @param navItemId: String unique identifier of the navigation item
	 * @param siteName: String unique identifier of the site.
	 * @param languageCode : it is two digit language code such as "en"
	 * @param countrycode : it is two digit country code such as "EN"
	 *  This is usually the DNS name of the site
	 *
	 * @throws PortalEngineException: If an error occurs while in the PortalEngine while
	 * accessing the vendor specific APIs
	 *
	 * @return NavigationItem
	 */
	NavigationItem getNavigationItem(String navItemId,String siteName,String languageCode,String countryCode) 
		throws PortalEngineException;


	/**
	 * This method takes a list of navigation items, the user Object
	 * and the site. It then calculates if the user has access on
	 * each of the navigation item passed in. It will return only the
	 * navigation items on which the user has access.
	 *
	 * If one or more of the navigation items passed in is null or does
	 * not exist in Vignette, this method will log it as a <I>debug</I>
	 * message and skip it.
	 * Note: This can happen if the search indexing spider crawled this
	 * navigation item on day 1.
	 * On day 2 this navigation item was deleted from the Vignette portal site.
	 * A user carries out a search on day 3 which returns this navigation item
	 * as result.
	 * @param site: Site object. This represents the site to which the
	 * navigation items belong
	 * @param user: User object. The user for which the navigation items
	 * should be filtered.
	 * @param navItem: List of NavigationItem objects which should be
	 * filtered.
	 *
	 * @throws PortalEngineException: If an error occurs while in the PortalEngine while
	 * accessing the vendor specific APIs
	 *
	 * @return List: list of filtered NavigationItem objects.
	 * Only the objects the user is allowed to access.
	 */
	List audienceItems(Site site,User user,List navItem) throws PortalEngineException;

}
