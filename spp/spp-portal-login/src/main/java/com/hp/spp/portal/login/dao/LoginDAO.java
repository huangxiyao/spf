package com.hp.spp.portal.login.dao;

import java.util.List;
import java.util.Set;

import com.hp.spp.portal.login.model.SPPGuestUser;

/**
 * Interface which describes the different access methods to the data.
 * <p>
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public interface LoginDAO {

	/**
	 * Get the path info + menu item of the landing page of the site. For instance, the site
	 * spportal could return "publicsppportal/menuitem.1234".
	 * <p>
	 * 
	 * @param site the site name
	 * @return the path info + menu item of the landing page
	 */
	String getPathMenuLandingPage(String site);

	/**
	 * Get the target path info + menu item of the portal depending of an HPP error code. For
	 * instance, the site spportal and error code 0 could return
	 * "publicsppportal/menuitem.1234". If the value returned is LANDING_PAGE, it means that
	 * the target is the landing page of the site.
	 * <p>
	 * 
	 * @param site the name of the portal
	 * @param errorCode the error code
	 * @return the path info + menu item
	 */
	String getPathMenuURL(String site, String errorCode);

	/**
	 * Get if the URL must be localized for a portal.
	 * <p>
	 * 
	 * @param site the name of the portal
	 * @return if the URL must be localized
	 */
	boolean getLocalizationInURL(String site);

	/**
	 * Get if the error message must be display on the login form.
	 * <p>
	 * 
	 * @param site the name of the portal
	 * @param errorCode the error code
	 * @return if the error page must be display
	 */
	boolean getDisplayErrorMessage(String site, String errorCode);

	/**
	 * Get the custom error message.
	 * <p>
	 * 
	 * @param site the name of the portal
	 * @param errorCode the error code
	 * @param localeCode the locale of the user
	 * @return the custom error message
	 */
	String getCustomErrorMessage(String site, String errorCode,
			String localeCode);

    /**
	 * Get the message corresponding to the locale and site name passed.
     * If the locale is complex, an attempt will be made also with the
     * simpler version. For instance, fr-ca will be first tried, and if
     * nothing is found then fr.
	 * <p>
	 *
	 * @param label the label
	 * @param localeCode the locale of the user
     * @param siteName name of the site
	 * @return the message
	 */
    String getMessageFromLabel(String label, String localeCode, String siteName);

    /**
	 * Get the message corresponding to the locale passed. If the locale is complex,
	 * an attempt will be made also with the simpler version. For instance, fr-ca will
	 * be first tried, and if nothing is found then fr.
	 * <p>
	 * 
	 * @param label the label
	 * @param localeCode the locale of the user
	 * @return the message
	 */
	String getMessageFromLabel(String label, String localeCode);

	/**
	 * Get the message.
	 * <p>
	 * 
	 * @param label the label
	 * @return the message
	 */
	String getMessageFromLabel(String label);

	/**
	 * Get the portlet login id of the site.
	 * <p>
	 * 
	 * @param site the site name
	 * @return the portlet login id
	 */
	String getPortletId(String site);

	/**
	 * Get the hpappid related to the site.
	 * <p>
	 * 
	 * @param site the site name
	 * @return the hpappid
	 */
	String getHpappid(String site);

	/**
	 * Get if the the simulation must persist during login/logout phases.
	 * <p>
	 * 
	 * @param site the name of the portal
	 * @return if the simulation persists
	 */
	boolean getPersistSimulation(String site);

	/**
	 * Get the menuitem use to the redirection after the logout phase.
	 * <p>
	 * 
	 * @param site the name of the portal
	 * @return the menuitem id for the redirect
	 */
	String getLogoutMenuItemId(String site) ;

	/**
	 * Get the mentitem use to redirect after the stop simulation phase.
	 * <p>
	 * 
	 * @param site the name of the portal
	 * @return the menuitem id for the redirect
	 */
	String getPathStopSimulationPage(String site);

	/**
	 * Get guest userbased on language code , country code and site identifier
	 * from SPP_LOCALE table
	 */
	SPPGuestUser getGuestUser(String languageCode,String countryCode,String siteIdentifier);
	
	/**
	 * Get guest user based on language code and site identifier
	 * from SPP_LOCALE table
	 */
	SPPGuestUser getGuestUserBySite(String languageCode,String siteIdentifier);
	
	/**
	 * Get guest user based on language code and country code
	 * from SPP_LOCALE table
	 */
	SPPGuestUser getGuestUserByCountry(String languageCode,String countryCode);
	

	/**
	 * Get guest user based on language code
	 * from SPP_LOCALE table
	 */
	SPPGuestUser getGuestUser(String languageCode);
}
