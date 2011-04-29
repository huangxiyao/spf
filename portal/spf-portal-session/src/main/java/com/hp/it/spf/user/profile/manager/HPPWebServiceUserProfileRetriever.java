/*
 * Project: Shared Portal Framework
 * Copyright (c) 2010 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.user.profile.manager;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.hp.globalops.hppcbl.passport.PassportService;
import com.hp.globalops.hppcbl.passport.PassportServiceException;
import com.hp.globalops.hppcbl.passport.manager.PassportParametersManager;
import com.hp.globalops.hppcbl.webservice.AdminViewUserResponseElement;
import com.hp.globalops.hppcbl.webservice.AdminViewUserResultTypeChoice;
import com.hp.globalops.hppcbl.webservice.LoginResponseElement;
import com.hp.globalops.hppcbl.webservice.PrivateData;
import com.hp.globalops.hppcbl.webservice.ProfileExtended;
import com.hp.globalops.hppcbl.webservice.ProfilePrivate;
import com.hp.it.spf.sso.portal.AuthenticatorHelper;
import com.hp.it.spf.user.exception.UserProfileException;
import com.vignette.portal.log.LogWrapper;

/**
 * This is a concrete class of IUserProfileRetriever interface. The class calls HPP web services to retrieve
 * user profile attributes and populates them in a map
 * 
 * @author <link href="zhizhong.zhao@hp.com">George Zhao</link>
 * @version 1.0
 */
public class HPPWebServiceUserProfileRetriever implements IUserProfileRetriever {
	
	private static final LogWrapper LOG = AuthenticatorHelper.getLog(HPPWebServiceUserProfileRetriever.class);
	private PassportService passportService = new PassportService();
	private PassportParametersManager wsManager = PassportParametersManager.getInstance();
	
    /**
     * Retrieve user profiles from HPP Web Services according to the specified user
     * identifier.
     * <p>
     * The profile map has the following structure:
     * <ul>
     * <li>Profile: Map</li>
     * <li>Map: (AttributeName : AttributeValue)*</li>
     * <li>AttributeName: String | Integer</li>
     * <li>AttributeValue: String | Map | List</li>
     * <li>List: (AttributeValue)*</li>
     * </ul>
     * 
     * @param userIdentifier user identifier, required. 
     * @param request HttpServletRequest required by API but ignored in implementation
     * @return user profile map or an empty map
     * @throws UserProfileException there is HPP Web Services error (PassportServiceException)
     */
    public Map<String, Object> getUserProfile(String userIdentifier,
                                              HttpServletRequest request) throws UserProfileException {
    	if (userIdentifier == null || userIdentifier.trim().length() == 0) {
    		throw new IllegalArgumentException("userIdentifier is required.");
    	}
        
    	Map<String, Object> userProfiles = new HashMap<String, Object>();
    	try {
    		passportService.setVersion("2"); // enables persistence and retrieval of business data fields
    		String adminSessionToken = null;
    		LoginResponseElement loginResponse = passportService.login(wsManager.getAdminUser(),
                    wsManager.getAdminPassword());
    		
    		if (loginResponse != null) {
    			adminSessionToken = loginResponse.getSessionToken();
    			
    			if (adminSessionToken != null) {
    				retrieveUserProfiles(passportService, adminSessionToken, userIdentifier, userProfiles);
    			}
    			else {
    				LOG.error("Retrieve user profile failed: HPP Webservices did not return admin Session token");
    			}
    		}
    		else {
    			LOG.error("Retrieve user profile failed: HPP Webservices did not response to login request");
    		}
    	} catch (PassportServiceException pse) {
    		LOG.error("Invoke HPP web Services failed, and got PassportServiceException", pse);
    		throw new UserProfileException("Invoke HPP web Services failed, and got PassportServiceException", pse);
    	} catch (Exception ex) {
    		LOG.error("Invoke HPP web Services failed and got other Exception", ex);
    		throw new UserProfileException("Invoke HPP web Services failed and got other Exception",ex);
    	}
        return userProfiles;
    }
    
    /**
     * populate user profile from HPP WS Data, the required fields are those from SPF Developer's Guide and must be present
     * in the user profile.
     * @param passportService PassportService
     * @param adminSessionToken String token used for web service calls
     * @param userId String the user id the user profile is for
     * @param userProfile Map<String, Object> the user profile map
     * @throws PassportServiceException
     */
    private void retrieveUserProfiles(PassportService passportService, 
    								String adminSessionToken, 
    								String userId, 
    								Map<String,	Object> userProfile) throws PassportServiceException {

    	
    	AdminViewUserResponseElement user = passportService.adminViewUser(adminSessionToken, userId, "userId");
    	
    	AdminViewUserResultTypeChoice choice = user.getAdminViewUserResultTypeChoice();
    	PrivateData privateData = choice.getPrivateData();
    	ProfilePrivate core = privateData.getProfilePrivate();
    	ProfileExtended ext = privateData.getProfileExtended();
    	
    	userProfile.put(UserProfileConsts.TITLE, core.getTitle());
    	userProfile.put(UserProfileConsts.FIRST_NAME, core.getFirstName());  // required field
    	userProfile.put(UserProfileConsts.MIDDLE_NAME, core.getMiddleName()); 
    	userProfile.put(UserProfileConsts.LAST_NAME, core.getLastName()); // required field
    	userProfile.put(UserProfileConsts.CONTACT_PREF_EMAIL, core.getContactPrefEmail());  // required field 
    	userProfile.put(UserProfileConsts.CONTACT_PREF_POST, core.getContactPrefPost());    // required field
    	userProfile.put(UserProfileConsts.CONTACT_PREF_TELEPHONE, core.getContactPrefTelephone());   // required field
    	userProfile.put(UserProfileConsts.EMAIL, core.getEmail());  // required field
    	userProfile.put(UserProfileConsts.LANG_CODE, core.getLangCode());   // required field
    	userProfile.put(UserProfileConsts.LOCALIZATION_CODE, core.getLocalizationCode());
    	String sl = core.getSecurityLevel();
    	if (StringUtils.isNotBlank(sl)) {
    		Float securityLevel = 0f;
    		try {
    			 securityLevel = Float.parseFloat(sl);
    			 userProfile.put(UserProfileConsts.SECURITY_LEVEL, securityLevel);   // required field
    		} catch (NumberFormatException e) {
    			LOG.warning(String.format("Security Level, %s, is not a float ", sl ), e);
    		}
    	}
    	
    	userProfile.put(UserProfileConsts.SEGMENT_NAME, core.getSegmentName());
    	userProfile.put(UserProfileConsts.HOME_ADDRESS_LINE1, ext.getHomeAddressLine1());
    	userProfile.put(UserProfileConsts.HOME_ADDRESS_LINE2, ext.getHomeAddressLine2());
    	userProfile.put(UserProfileConsts.HOME_CITY, ext.getHomeCity());
    	userProfile.put(UserProfileConsts.HOME_STATE, ext.getHomeState());
    	userProfile.put(UserProfileConsts.HOME_POSTAL_CODE, ext.getHomePostalCode());
    	userProfile.put(UserProfileConsts.HOME_COUNTRY_CODE, ext.getHomeCountryCode());
    	userProfile.put(UserProfileConsts.HOME_PO_BOX, ext.getHomePOBox());
    	userProfile.put(UserProfileConsts.HOME_DISTRICT, ext.getHomeDistrict());
    	userProfile.put(UserProfileConsts.HOME_TELEPHONE_CITY_CODE, ext.getHomeTelephoneCityCode());
    	userProfile.put(UserProfileConsts.HOME_TELEPHONE_COUNTRY_CODE, ext.getHomeTelephoneCountryCode());
    	userProfile.put(UserProfileConsts.HOME_TELEPHONE_NUMBER, ext.getHomeTelephoneNumber());
    	                                                     
    	userProfile.put(UserProfileConsts.BUSINESS_COMPANY_NAME, ext.getBusCompanyName());
    	userProfile.put(UserProfileConsts.BUSINESS_BUILDING_NAME, ext.getBusBuildingName());
    	userProfile.put(UserProfileConsts.BUSINESS_ADDRESS_LINE1, ext.getBusAddressLine1());  // required field
    	userProfile.put(UserProfileConsts.BUSINESS_ADDRESS_LINE2, ext.getBusAddressLine2());
    	userProfile.put(UserProfileConsts.BUSINESS_CITY, ext.getBusCity());    // required field
    	userProfile.put(UserProfileConsts.BUSINESS_STATE, ext.getBusState());   // required field
    	userProfile.put(UserProfileConsts.BUSINESS_POSTAL_CODE, ext.getBusPostalCode()); // required field
    	userProfile.put(UserProfileConsts.BUSINESS_COUNTRY_CODE, ext.getBusCountryCode()); // required field
    	userProfile.put(UserProfileConsts.BUSINESS_ADDRESS_LOCALIZATION_CODE, ext.getBusAddressLocalizationCode());
    	userProfile.put(UserProfileConsts.BUSINESS_DISTRICT, ext.getBusDistrict());
    	userProfile.put(UserProfileConsts.BUSINESS_FAX_CITY_CODE, ext.getBusFaxCityCode());
    	userProfile.put(UserProfileConsts.BUSINESS_FAX_COUNTRY_CODE, ext.getBusFaxCountryCode());
    	userProfile.put(UserProfileConsts.BUSINESS_FAX_EXTENSION, ext.getBusFaxExtension());
    	userProfile.put(UserProfileConsts.BUSINESS_FAX_NUMBER, ext.getBusFaxNumber());
    	userProfile.put(UserProfileConsts.BUSINESS_MAIL_STOP, ext.getBusMailStop());  // required field
    	userProfile.put(UserProfileConsts.BUSINESS_TELEPHONE_CITY_CODE, ext.getBusTelephoneCityCode());
    	userProfile.put(UserProfileConsts.BUSINESS_TELEPHONE_COUNTRY_CODE, ext.getBusTelephoneCountryCode());
    	userProfile.put(UserProfileConsts.BUSINESS_TELEPHONE_NUMBER, ext.getBusTelephoneNumber());  // required field
    	userProfile.put(UserProfileConsts.BUSINESS_TELEPHONE_EXTENSION, ext.getBusTelephoneExtension());  // required field
    	userProfile.put(UserProfileConsts.CL_TIMEZONE_HEADER, ext.getBusTimezoneCode());
    	userProfile.put(UserProfileConsts.BUSINESS_TIMEZONE_CODE, ext.getBusTimezoneCode());  // required field
    	
    	
}

}