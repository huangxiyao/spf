package com.hp.spp.portal.common.util;

import junit.framework.TestCase;

import com.hp.spp.portal.common.dao.CommonDAOCacheImpl;

public class HPUrlLocatorTest extends TestCase {

	HPUrlLocator mHPUrlLocator = HPUrlLocator.getInstance();

	/**
	 * Initialize the local codes for the test
	 * 
	 * @throws Exception
	 */
	public void testInitUrlLocators() throws Exception {
		CommonDAOCacheImpl.mCacheAdministrator.putInCache("urlLocator-de-de",
				"/country/de/de");
		CommonDAOCacheImpl.mCacheAdministrator.putInCache("urlLocator-de-en",
				"/country/de/de");
		CommonDAOCacheImpl.mCacheAdministrator.putInCache("urlLocator-fr-fr",
				"/country/fr/fr");
		CommonDAOCacheImpl.mCacheAdministrator.putInCache(
				"urlLocator-be-ByCountry", "/country/be/fr");
		CommonDAOCacheImpl.mCacheAdministrator.putInCache(
				"urlLocator-fr-ByLanguage", "/country/fr/fr");
		CommonDAOCacheImpl.mCacheAdministrator.putInCache(
				"urlLocator-ru-ru", "/country/ru/ru");
	}

	/**
	 * Test that without country, the default string locator is retrieved
	 * 
	 * @throws Exception
	 */
	public void testCountryCodeNull() throws Exception {
		String result = mHPUrlLocator.getUserStrLoc(null, "DE");
		assertEquals("no default retrieved",
				HPUrlLocator.DEFAULT_STRING_LOCATOR, result);
	}

	/**
	 * Test that with country void, the default string locator is retrieved
	 * 
	 * @throws Exception
	 */
	public void testCountryCodeVoid() throws Exception {
		String result = mHPUrlLocator.getUserStrLoc("", "DE");
		assertEquals("no default retrieved",
				HPUrlLocator.DEFAULT_STRING_LOCATOR, result);
	}

	/**
	 * Test that without language but with combinaison country + "EN" existing,
	 * a specific string locator is retrieved.
	 * 
	 * @throws Exception
	 */
	public void testLanguageCodeNullWithCountryDetermined() throws Exception {
		String result = mHPUrlLocator.getUserStrLoc("DE", null);
		assertEquals("no specific retrieved", "/country/de/de", result);
	}

	/**
	 * Test that without language and without combinaison country + "EN"
	 * existing, default locator is retrieved.
	 * 
	 * @throws Exception
	 */
	public void testLanguageCodeNullWithCountryNotDetermined() throws Exception {
		String result = mHPUrlLocator.getUserStrLoc("FR", null);
		assertEquals("no default retrieved",
				HPUrlLocator.DEFAULT_STRING_LOCATOR, result);
	}

	/**
	 * Test that with language void but with combinaison country + "EN"
	 * existing, a specific string locator is retrieved.
	 * 
	 * @throws Exception
	 */
	public void testLanguageCodeVoidWithCountryDetermined() throws Exception {
		String result = mHPUrlLocator.getUserStrLoc("DE", "");
		assertEquals("no specific retrieved", "/country/de/de", result);
	}

	/**
	 * Test that with language void and without combinaison country + "EN"
	 * existing, default locator is retrieved.
	 * 
	 * @throws Exception
	 */
	public void testLanguageCodeVoidWithCountryNotDetermined() throws Exception {
		String result = mHPUrlLocator.getUserStrLoc("FR", "");
		assertEquals("no default retrieved",
				HPUrlLocator.DEFAULT_STRING_LOCATOR, result);
	}

	/**
	 * Test that with country in minor case, a specific string locator is
	 * retrieved.
	 * 
	 * @throws Exception
	 */
	public void testCountryCodeMinorCase() throws Exception {
		String result = mHPUrlLocator.getUserStrLoc("de", "DE");
		assertEquals("no specific retrieved", "/country/de/de", result);
	}

	/**
	 * Test that with language in minor case, a specific string locator is
	 * retrieved.
	 * 
	 * @throws Exception
	 */
	public void testLanguageCodeMinorCase() throws Exception {
		String result = mHPUrlLocator.getUserStrLoc("DE", "de");
		assertEquals("no specific retrieved", "/country/de/de", result);
	}

	/**
	 * Test that with existing combinaison, a specific string locator is
	 * retrieved.
	 * 
	 * @throws Exception
	 */
	public void testExistingCombinaison() throws Exception {
		String result = mHPUrlLocator.getUserStrLoc("FR", "FR");
		assertEquals("no specific retrieved", "/country/fr/fr", result);
	}

	/**
	 * Test that with existing combinaison, a specific string locator is
	 * retrieved.
	 * 
	 * @throws Exception
	 */
	public void testNonExistingCombinaison() throws Exception {
		String result = mHPUrlLocator.getUserStrLoc("CA", "FR");
		assertEquals("no default retrieved",
				HPUrlLocator.DEFAULT_STRING_LOCATOR, result);
	}

	/**
	 * Test that with existing combinaison only for country, a specific string
	 * locator is retrieved.
	 * 
	 * @throws Exception
	 */
	public void testExistingCountryWithDefaultLanguage() throws Exception {
		String result = mHPUrlLocator.getUserStrLoc("DE", "IT");
		assertEquals("no specific retrieved", "/country/de/de", result);
	}

	/**
	 * Test that with existing combinaison only for country, but default
	 * language different of en, a specific string locator is retrieved.
	 * 
	 * @throws Exception
	 */
	public void testExistingCountryWithDefaultLanguageDifferentFromEn()
			throws Exception {
		String result = mHPUrlLocator.getUserStrLoc("BE", "NL");
		assertEquals("no specific retrieved", "/country/be/fr", result);
	}

	/**
	 * Test that without existing combinaison only for country, a default string
	 * locator is retrieved.
	 * 
	 * @throws Exception
	 */
	public void testNonExistingCountryWithDefaultLanguage() throws Exception {
		String result = mHPUrlLocator.getUserStrLoc("NO", "NO");
		assertEquals("no default retrieved",
				HPUrlLocator.DEFAULT_STRING_LOCATOR, result);
	}

	/**
	 * Test that without language and research focused on language, default
	 * combination is retrieved.
	 * 
	 * @throws Exception
	 */
	public void testLanguageCodeNull() throws Exception {
		String result = mHPUrlLocator.getUserStrLocByLanguage("DE", null);
		assertEquals("no default retrieved",
				HPUrlLocator.DEFAULT_STRING_LOCATOR, result);
	}

	/**
	 * Test that with language void and research focused on language, default
	 * combination is retrieved.
	 * 
	 * @throws Exception
	 */
	public void testLanguageCodeVoid() throws Exception {
		String result = mHPUrlLocator.getUserStrLocByLanguage("DE", "");
		assertEquals("no default retrieved",
				HPUrlLocator.DEFAULT_STRING_LOCATOR, result);
	}

	/**
	 * Test that with country not existing and research focused on language,
	 * combination is retrieved by using country code of ssoguestuser.
	 * 
	 * @throws Exception
	 */
	public void testCountryCodeNotExistingButInSsoGuestUser() throws Exception {
		String result = mHPUrlLocator.getUserStrLocByLanguage("CH", "FR");
		assertEquals("no specific retrieved", "/country/fr/fr", result);
	}

	/**
	 * Test that with country not existing and research focused on language and
	 * no associated guest user, default combination is retrieved.
	 * 
	 * @throws Exception
	 */
	public void testCountryCodeNotExistingAndNoSsoGuestUser()
			throws Exception {
		String result = mHPUrlLocator.getUserStrLocByLanguage("UA", "uk");
		assertEquals("no default retrieved",
				HPUrlLocator.DEFAULT_STRING_LOCATOR, result);
	}

	/**
	 * Test that with country not existing and research focused on language and
	 * no associated guest user, default combination is retrieved.
	 * 
	 * @throws Exception
	 */
	public void testExistingCombinaisonFocussedOnLanguage()
			throws Exception {
		String result = mHPUrlLocator.getUserStrLocByLanguage("RU", "ru");
		assertEquals("no specific retrieved", "/country/ru/ru", result);
	}

	
}
