package com.hp.spp.portal.common.sql;

/**
 * Interface which describes the different access methods to the data.
 * <p>
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public interface PortalCommonDAO {

	/**
	 * Get the path info + menu item of the landing page of the site. For instance, the site
	 * spportal could return "publicsppportal/menuitem.1234".
	 * <p>
	 * 
	 * @param site the site name
	 * @return the path info + menu item of the landing page
	 */
	String getSiteIdentifier(String siteName);

	/**
	 * Determines if a site is accessible.
	 * <p>
	 * 
	 * @param site the site name
	 * @return the accessibility of the site
	 */
	boolean isSiteAccessible(String siteURI);

	/**
	 * Get the access code of a site.
	 * <p>
	 * 
	 * @param site the site name
	 * @return the access code of the site
	 */
	String getAccessCode(String siteURI);

	/**
	 * Get the home page URL for this site.
	 * <p>
	 * @param siteName
	 * @return the home page URL of this site
	 */
	String getPortalHomePageUrl(String siteName);
	
	/**
	 * Get the protocol for this site (http/https).
	 * <p>
	 * @param siteName
	 * @return the protocol of this site
	 */
	String getSiteProtocol(String siteName);
	
	/**
	 *  
	 * Set the AccessCode for this site
	 * <p>
	 * @param siteName
	 * @param accessCode
	 */
	void setAccessCode(String siteName,String accessCode);
	
	/**
	 * Set the AccessSite for this site
	 * <p>
	 * @param siteName
	 * @param accessSite
	 */
	void setSiteAccessible(String siteName,int accessSite);	

}
