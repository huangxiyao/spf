package com.hp.spp.portal.login.model;

public class SPPGuestUser {
	private String mCountryCode;
	private String mLanguageCode;
	private String mSiteIdentifier;
	private String mGuestUser;
	private String mPreferredLanguageCode;
	
	
	public SPPGuestUser(String languageCode, String countryCode, String siteIdentifier, String guestUser, String preferredLanguageCode) {
		mCountryCode = countryCode;
		mLanguageCode = languageCode;
		mSiteIdentifier = siteIdentifier;
		mGuestUser = guestUser;
		mPreferredLanguageCode = preferredLanguageCode;
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
	public String getSiteIdentifier() {
		return mSiteIdentifier;
	}
	public void setSiteIdentifier(String siteIdentifier) {
		mSiteIdentifier = siteIdentifier;
	}
	public String getGuestUser() {
		return mGuestUser;
	}
	public void setGuestUser(String guestUser) {
		mGuestUser = guestUser;
	}
	public String getPreferredLanguageCode() {
		return mPreferredLanguageCode;
	}
	public void setPreferredLanguageCode(String preferredLanguageCode) {
		mPreferredLanguageCode = preferredLanguageCode;
	}
	
	
}
