package com.hp.it.spf.user.profile.manager;

import com.hp.it.spf.sso.portal.AuthenticationConsts;

/**
 * This class defines constants for user profile attributes which are available from HPP Web Services. 
 * The attributes include most required attributes as defined in Shared Portal Framework - Developer's Guide.
 * There are several required attributes which are not available from web services and are populated from Headers. 
 * 
 * @author <link href="zhizhong.zhao@hp.com">George Zhao</link>
 * @version 1.0 
 */
public class UserProfileConsts {
	
	// required fields populated from siteminder headers
	/**
	 * PROFILE_ID, USER_NANE, LAST_CHANGE_DATE, LAST_LOGIN_DATE 
	 */
	
	// required fields available from HPP/WS
	public static final String FIRST_NAME = AuthenticationConsts.KEY_FIRST_NAME;
	public static final String LAST_NAME = AuthenticationConsts.KEY_LAST_NAME;
	public static final String BUSINESS_ADDRESS_LINE1 = AuthenticationConsts.KEY_STREET;
	public static final String BUSINESS_CITY = AuthenticationConsts.KEY_CITY; 
	public static final String BUSINESS_STATE = AuthenticationConsts.KEY_STATE;
	public static final String BUSINESS_POSTAL_CODE = AuthenticationConsts.KEY_ZIP;
	public static final String BUSINESS_COUNTRY_CODE = AuthenticationConsts.KEY_COUNTRY;
	public static final String EMAIL = AuthenticationConsts.KEY_EMAIL;
	public static final String BUSINESS_TELEPHONE_NUMBER = AuthenticationConsts.KEY_PHONE_NUMBER;
	public static final String BUSINESS_TELEPHONE_EXTENSION = AuthenticationConsts.KEY_PHONE_NUMBER_EXT;
	public static final String BUSINESS_MAIL_STOP = AuthenticationConsts.KEY_MAIL_STOP;
	public static final String LANG_CODE = AuthenticationConsts.KEY_LANGUAGE;
	public static final String CL_TIMEZONE_HEADER = "CL_TimezoneHeader"; // legacy timezone code
	public static final String BUSINESS_TIMEZONE_CODE = "BusTimezoneCode"; // new time zone code 
	public static final String SECURITY_LEVEL = AuthenticationConsts.KEY_SECURITY_LEVEL;
	public static final String CONTACT_PREF_EMAIL = AuthenticationConsts.KEY_EMAIL_PREF;
	public static final String CONTACT_PREF_POST = AuthenticationConsts.KEY_POSTAL_PREF;
	public static final String CONTACT_PREF_TELEPHONE = AuthenticationConsts.KEY_PHONE_PREF;

	// additional extended fields
	public static final String TITLE = "Title";
	public static final String MIDDLE_NAME = "MiddleName";
	public static final String LOCALIZATION_CODE = "LocalizationCode";
	public static final String SEGMENT_NAME = "SegmentName";
	                                                     
	public static final String HOME_ADDRESS_LINE1 = "HomeAddressLine1";
	public static final String HOME_ADDRESS_LINE2 = "HomeAddressLine2";
	public static final String HOME_CITY = "HomeCity";
	public static final String HOME_STATE = "HomeState";
	public static final String HOME_POSTAL_CODE = "HomePostalCode";
	public static final String HOME_COUNTRY_CODE = "HomeCountryCode";
	public static final String HOME_PO_BOX = "HomePOBox";
	public static final String HOME_DISTRICT = "HomeDistrict";
	public static final String HOME_TELEPHONE_CITY_CODE = "HomeTelephoneCityCode";
	public static final String HOME_TELEPHONE_COUNTRY_CODE = "HomeTelephoneCountryCode";
	public static final String HOME_TELEPHONE_NUMBER = "HomeTelephoneNumber";
	                                                     
	public static final String BUSINESS_COMPANY_NAME = "BusCompanyName";
	public static final String BUSINESS_BUILDING_NAME = "BusBuildingName";
	public static final String BUSINESS_ADDRESS_LINE2 = "BusAddressLine2";
	public static final String BUSINESS_ADDRESS_LOCALIZATION_CODE = "BusAddressLocalizationCode";
	public static final String BUSINESS_DISTRICT = "BusDistrict";
	public static final String BUSINESS_FAX_CITY_CODE = "BusFaxCityCode";
	public static final String BUSINESS_FAX_COUNTRY_CODE = "BusFaxCountryCode";
	public static final String BUSINESS_FAX_EXTENSION = "BusFaxExtension";
	public static final String BUSINESS_FAX_NUMBER = "BusFaxNumber";
	public static final String BUSINESS_TELEPHONE_CITY_CODE = "BusTelephoneCityCode";
	public static final String BUSINESS_TELEPHONE_COUNTRY_CODE = "BusTelephoneCountryCode";

}
