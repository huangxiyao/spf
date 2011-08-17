package com.hp.spp.portal.common.dao;

import java.util.Date;
import java.util.Set;


/**
 * Interface which describes the different access methods to the data.
 * <p>
 * 
 * @author mvidal@capgemini.fr
 *
 */
public interface CommonDAO {
	/**
	 * Get the url locator string.
	 * <p>
	 * @param userLangCode user language code
	 * @param userCountryCode user country code
	 * 
	 * @return the corresponding url locator string, null if nothing is found
	 */
	public String getUrlLocator(String userCountryCode, String userLangCode);

	/**
	 * Get the first available url locator string.
	 * <p>
	 * @param userLangCode user language code
	 * 
	 * @return the first available url locator string, null if nothing is found
	 */
	public String getUrlLocator(String userCountryCode);

	/**
	 * Get the first available url locator string by language.
	 * <p>
	 * @param userLangCode user language code
	 * 
	 * @return the first available url locator string, null if nothing is found
	 */
	public String getUrlLocatorByLanguage(String userLanguageCode);

	/**
	 * Return the list of defined sites.
	 * 
	 * @return
	 */
	Set getSupportedSites();

	/**
	 * Get the name of the query id send to UPS.
	 * 
	 * @param site the name of the site
	 * @return the name of UPS query id
	 */
	String getUPSQueryId(String site);

	/**
	 * Get the groups of a user.
	 * 
	 * @param hppid the hppid of the user
	 * @return the groups of a user.
	 */
	Set getVignetteUserGroup(String hppid);
	
	/**
	 * Get the groups defined in Vignette.
	 * 
	 * @return the groups defined in Vignette.
	 */
	Set getVignetteGroups();

	/**
	 * Update last login date of the user
	 * 
	 * @param hppid the hppid of the user
	 * @param date the last login date
	 */
	public void updateUserLastLoginDate(String hppid, Date date);
}
