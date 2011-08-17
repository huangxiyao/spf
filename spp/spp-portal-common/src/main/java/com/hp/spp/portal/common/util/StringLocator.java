package com.hp.spp.portal.common.util;

/**
 * Object which allows the localization of an Url.
 *  
 * @author Mathieu Vidal
 *
 */
public class StringLocator {

	private String mCountryCode;
	
	private String mLanguageCode;
	
	private String mStringLocator;

	public StringLocator(String countryCode, String languageCode, String stringLocator) {
		mCountryCode = countryCode;
		mLanguageCode = languageCode;
		mStringLocator = stringLocator;
	}

	public String getCountryCode() {
		return mCountryCode;
	}

	public void setCountryCode(String countryCode) {
		mCountryCode = countryCode;
	}

	public String getLanguageCode() {
		return mLanguageCode;
	}

	public void setLanguageCode(String languageCode) {
		mLanguageCode = languageCode;
	}

	public String getStringLocator() {
		return mStringLocator;
	}

	public void setStringLocator(String stringLocator) {
		mStringLocator = stringLocator;
	}

}
