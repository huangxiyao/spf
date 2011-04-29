package com.hp.it.spf.sso.portal;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Data object representing a valid entry in the input file.
 * Several of this object properties are calculated based on language and country.
 *
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
class AnonymousUserData {
	public static final String EMAIL_DOMAIN = "@sso_guest_user.com";
	private static final String USERNAME_PREFIX = "sso_guest_user_";
	private String mUsername;
	private String mEmail;
	private String mFirstName;
	private String mLastName;
	private String mLanguage;
	private String mCountry;
	private String mTimeZone;

	public AnonymousUserData(String language, String country, String timeZone) {
		if (language == null) {
			throw new IllegalArgumentException("Anonymous user's language cannot be null!");
		}
		if (timeZone == null) {
			throw new IllegalArgumentException("Anonymous user's timezone cannot be null!");
		}
		if (country != null && country.trim().equals("")) {
			country = null;
		}

		mLanguage = language;
		mCountry = country;
		mTimeZone = timeZone;

		String locale = mLanguage + (mCountry == null ? "" : "-" + mCountry);
		mUsername = (USERNAME_PREFIX + locale).toLowerCase();
		mEmail = (locale + EMAIL_DOMAIN).toLowerCase();
		mFirstName = "Guest";
		mLastName = USERNAME_PREFIX + locale;
	}

	public String getCountry() {
		return mCountry;
	}

	public String getEmail() {
		return mEmail;
	}

	public String getFirstName() {
		return mFirstName;
	}

	public String getLanguage() {
		return mLanguage;
	}

	public String getLastName() {
		return mLastName;
	}

	public String getTimeZone() {
		return mTimeZone;
	}

	public String getUsername() {
		return mUsername;
	}

	public String getProfileId() {
		return mUsername;
	}

	@Override
	public String toString() {
		return "username=" + mUsername +
				"; language=" + mLanguage +
				"; country=" + mCountry +
				"; timeZone=" + mTimeZone +
				"; email=" + mEmail +
				"; firstName=" + mFirstName +
				"; lastName=" + mLastName;
	}

	Map<String, Object> toVignetteUserProperties() {
		Map<String, Object> userProperties = new LinkedHashMap<String, Object>();
		userProperties.put(AuthenticationConsts.PROPERTY_PROFILE_ID, getProfileId());
		userProperties.put(AuthenticationConsts.PROPERTY_USER_NAME_ID, getUsername());
		userProperties.put(AuthenticationConsts.PROPERTY_LANGUAGE_ID, getLanguage());
		userProperties.put(AuthenticationConsts.PROPERTY_COUNTRY_ID, getCountry());
		userProperties.put(AuthenticationConsts.PROPERTY_EMAIL_ID, getEmail());
		userProperties.put(AuthenticationConsts.PROPERTY_LASTNAME_ID, getLastName());
		userProperties.put(AuthenticationConsts.PROPERTY_FIRSTNAME_ID, getFirstName());
		userProperties.put(AuthenticationConsts.PROPERTY_SPF_TIMEZONE_ID, getTimeZone());
		userProperties.put(AuthenticationConsts.PROPERTY_TIMEZONE_ID,
				AuthenticatorHelper.getTimeZoneOffset(getTimeZone()));
		return userProperties;
	}



}
