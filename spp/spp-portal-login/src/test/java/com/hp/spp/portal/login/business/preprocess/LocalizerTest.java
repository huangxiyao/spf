package com.hp.spp.portal.login.business.preprocess;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


import com.hp.spp.portal.login.dao.LoginDAOCacheImpl;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

/**
 * Test Class for Localizer.
 * 
 * @author mvidal@capgemini.fr
 */
public class LocalizerTest extends TestCase {

	public LocalizerTest(String name) {
		super(name);
	}

	/**
	 * The suite of test to run. Uncomment "suite.setUpAtEachTest()" if you need to have a
	 * setUp/tearDown for each test
	 * 
	 * @return the test suite
	 * @throws Exception to make sure that each unexpected Exception will be noticed
	 */
	public static Test suite() throws Exception {
		TestSuite suite = new TestSuite();

		suite.addTest(new LocalizerTest("testInitLocaleCodes"));

		suite.addTest(new LocalizerTest("testGetLanguageCodeByPathInfoVoid"));
		suite.addTest(new LocalizerTest("testGetLanguageCodeByPathInfoWithoutLocalization"));

		//Invalid test case as language code is 2 character code.
		//suite.addTest(new LocalizerTest("testGetLanguageCodeByPathInfoWithHPLocalization"));
		//suite.addTest(new LocalizerTest("testGetLanguageCodeByPathInfoWithUSEnglish"));
		
		suite.addTest(new LocalizerTest("testGetLanguageCodeByPathInfoWithoutCountry"));
		suite.addTest(new LocalizerTest("testGetLanguageCodeByPathInfoWithSmallPath"));
		
		//Invalid test case as language code is 2 character code.
		//suite.addTest(new LocalizerTest("testGetLanguageCodeByPathInfoWithDialect"));

		suite.addTest(new LocalizerTest("test1906"));
		suite.addTest(new LocalizerTest("test1906b"));

		suite.addTest(new LocalizerTest("testGetCountryCodeByPathInfoVoid"));
		suite.addTest(new LocalizerTest("testGetCountryCodeByPathInfoWithoutLocalization"));
		//Invalid test case as country is 2 character code.
		//suite.addTest(new LocalizerTest("testGetCountryCodeByPathInfoWithHPLocalization"));
		suite.addTest(new LocalizerTest("testGetCountryCodeByPathInfoWithoutCountry"));

		suite.addTest(new LocalizerTest("testGetLanguageCodeByURLVoid"));
		suite.addTest(new LocalizerTest("testGetLanguageCodeByURLWithHPLocalization"));
		suite.addTest(new LocalizerTest("testGetLanguageCodeByURLWithOnlyLanguage"));
		suite.addTest(new LocalizerTest("testGetLanguageCodeByURLWithOnlyLanguage4"));
		suite.addTest(new LocalizerTest("testGetLanguageCodeByURLWithLanguage4"));
		suite.addTest(new LocalizerTest("testGetLanguageCodeByURLWithOnlyDialect"));
		suite.addTest(new LocalizerTest("testGetLanguageCodeByURLWithDialect"));

		suite.addTest(new LocalizerTest("testGetCountryCodeByURLVoid"));
		suite.addTest(new LocalizerTest("testGetCountryCodeByURLWithHPLocalization"));
		suite.addTest(new LocalizerTest("testGetCountryCodeByURLWithOnlyCountry"));

		suite.addTest(new LocalizerTest("testGetLanguageCodeByCookieVoid"));
		suite.addTest(new LocalizerTest("testGetLanguageCodeByCookieWithHPLocalization"));
		suite.addTest(new LocalizerTest("testGetLanguageCodeByCookieWithOnlyLanguage"));
		suite.addTest(new LocalizerTest("testGetLanguageCodeByCookieWithOnlyCountry"));
		suite.addTest(new LocalizerTest("test1907"));

		suite.addTest(new LocalizerTest("testGetCountryCodeByCookieVoid"));
		suite.addTest(new LocalizerTest("testGetCountryCodeByCookieWithHPLocalization"));
		suite.addTest(new LocalizerTest("testGetCountryCodeByCookieWithOnlyLanguage"));
		suite.addTest(new LocalizerTest("testGetCountryCodeByCookieWithOnlyCountry"));

		suite.addTest(new LocalizerTest("testGetLanguageCodeByBrowserPreferenceVoid"));
		//Invalid test case with 4.0 implementation
		//suite.addTest(new LocalizerTest("testGetLanguageCodeByBrowserPreferenceUkrainian"));
		suite.addTest(new LocalizerTest("testGetLanguageCodeByBrowserPreferenceFrenchCanadian"));
		suite.addTest(new LocalizerTest("testGetLanguageCodeByBrowserPreferenceBrithishEnglish"));
		
		suite.addTest(new LocalizerTest("testRewriteURLPathWithLocalization"));
		suite.addTest(new LocalizerTest("testRewriteURLPathWithLocalizationAndParameters"));
		
		//Following test cases are for lookup for language and country code from URL/Cookies/Browser
		suite.addTest(new LocalizerTest("testLangCountyComboForLookupFromProperURLPath"));
		suite.addTest(new LocalizerTest("testLangCountyComboForLookupFromImproperURLPath"));
		suite.addTest(new LocalizerTest("testLangCountyComboForLookupFromURLQueryParams"));
		suite.addTest(new LocalizerTest("testLangCountyComboForLookupFromURLQueryParamsWithOnlyCC"));
		suite.addTest(new LocalizerTest("testLangCountyComboForLookupFromCookies"));
		suite.addTest(new LocalizerTest("testLangCountyComboForLookupFromCookiesWIthOnlyCC"));
		
		return suite;
	}
	
	/**
	 * This test case represents perfect case where in country/language are retrieved
	 * from URL path.
	 */
	public void testLangCountyComboForLookupFromProperURLPath() throws Exception{
		String queryPath = "/nameofsite/cn/zh/TEMPLATE.PRELOGIN";
		HttpServletRequest request = req(session(),null,queryPath,null,null,null);
		assertEquals("Expecting zh-cn","zh-cn",new Localizer().getLangCountyComboForLookup(request)[0]);
		assertEquals("Expecting cn","cn",new Localizer().getLangCountyComboForLookup(request)[1]);
	}
	
	/**
	 * This test case check for lang/country from URL and since the format is not
	 * proper it will try to check for query params.
	 */
	public void testLangCountyComboForLookupFromImproperURLPath() throws Exception{
		String[] langArray = {"fr"};
		String[] countryArray = {"fr"};
		Map map = new HashMap();
		map.put("lang",langArray);
		map.put("cc", countryArray);
		String queryPath = "/nameofsite/cnx/zh/TEMPLATE.PRELOGIN";
		HttpServletRequest request = req(session(),null,queryPath,map,null,null);
		assertEquals("Expecting fr-fr","fr-fr",new Localizer().getLangCountyComboForLookup(request)[0]);
		assertEquals("Expecting fr","fr",new Localizer().getLangCountyComboForLookup(request)[1]);
	}
	
	/**
	 * This test case check for lang/country from URL query params
	 */
	public void testLangCountyComboForLookupFromURLQueryParams() throws Exception{
		String[] langArray = {"fr"};
		String[] countryArray = {"fr"};
		Map map = new HashMap();
		map.put("lang",langArray);
		map.put("cc", countryArray);
		String queryPath = "/nameofsite/cnx/zh/TEMPLATE.PRELOGIN";
		HttpServletRequest request = req(session(),null,null,map,null,null);
		assertEquals("Expecting fr-fr","fr-fr",new Localizer().getLangCountyComboForLookup(request)[0]);
		assertEquals("Expecting fr","fr",new Localizer().getLangCountyComboForLookup(request)[1]);
	}
	
	/**
	 * This test case check for lang/country from URL query params
	 * but URL having only cc i.e contry. Should fall bace to cookie
	 */
	public void testLangCountyComboForLookupFromURLQueryParamsWithOnlyCC() throws Exception{
		Cookie[] cookies = new Cookie[2];
		cookies[0] = new Cookie("lang","zh-cn");
		cookies[1] = new Cookie("cc","cn");
	
		String[] countryArray = {"cn"};
		Map map = new HashMap();
		map.put("cc", countryArray);
	
		HttpServletRequest request = req(session(),null,null,map,cookies,null);
		assertEquals("Expecting zh-cn","zh-cn",new Localizer().getLangCountyComboForLookup(request)[0]);
		assertEquals("Expecting cn","cn",new Localizer().getLangCountyComboForLookup(request)[1]);
	}
	
	/**
	 * This test case check for lang/country from cookies 
	 */
	public void testLangCountyComboForLookupFromCookies() throws Exception{
		Cookie[] cookies = new Cookie[2];
		String[] langArray = {"fr"};
		String[] countryArray = {"fr"};
		cookies[0] = new Cookie("lang","fr");
		cookies[1] = new Cookie("cc","fr");
		String queryPath = "/nameofsite/cnx/zh/TEMPLATE.PRELOGIN";
		HttpServletRequest request = req(session(),null,null,null,cookies,null);
		assertEquals("Expecting fr-fr","fr-fr",new Localizer().getLangCountyComboForLookup(request)[0]);
		assertEquals("Expecting fr","fr",new Localizer().getLangCountyComboForLookup(request)[1]);
	}
	
	/**
	 * This test case check for lang/country from cookies 
	 * and lang cookies is not available. Should fallback
	 * to browser.
	 */
	public void testLangCountyComboForLookupFromCookiesWIthOnlyCC() throws Exception{
		Cookie[] cookies = new Cookie[2];
		String[] countryArray = {"fr"};
		cookies[1] = new Cookie("cc","fr");
		HttpServletRequest request = req(session(),null,null,null,cookies,new Locale("zh-cn"));
		assertEquals("Expecting zh-cn","zh-cn",new Localizer().getLangCountyComboForLookup(request)[0]);
	}
	
	/**
	 * Initialize the local codes for the test.
	 * 
	 * @throws Exception
	 */
	public void testInitLocaleCodes() throws Exception {
		Set languageCodes = new HashSet();
		languageCodes.add("en-emea_africa");
		languageCodes.add("en-us");
		languageCodes.add("en-gb");
		languageCodes.add("fr-fr");
		languageCodes.add("fr-ca");
		languageCodes.add("fr");
		languageCodes.add("en");
		GeneralCacheAdministrator cacheAdministrator = new GeneralCacheAdministrator();
		cacheAdministrator.putInCache("languageCodes", languageCodes);
		LoginDAOCacheImpl.setCacheAdministrator(cacheAdministrator);
	}

	/**
	 * This method tests if the language returned is null with a PathInfo null or "".
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByPathInfoVoid() throws Exception {
		String pathInfo = null;
		String result = (new Localizer()).getLanguageCodeByPathInfo(pathInfo);
		assertNull(result);
		pathInfo = "";
		result = (new Localizer()).getLanguageCodeByPathInfo(pathInfo);
		assertNull(result);
	}

	/**
	 * This method tests if the language returned is null with a PathInfo without localization.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByPathInfoWithoutLocalization() throws Exception {
		String pathInfo = "/nameofsite/TEMPLATE.PRELOGIN";
		String result = (new Localizer()).getLanguageCodeByPathInfo(pathInfo);
		assertNull(result);
	}

	/**
	 * This method tests if the language returned is correct with a PathInfo with localization.
	 * We test here gb english.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByPathInfoWithHPLocalization() throws Exception {
		String pathInfo = "/nameofsite/country/GB/en-GB/TEMPLATE.PRELOGIN";
		String result = (new Localizer()).getLanguageCodeByPathInfo(pathInfo);
		assertEquals("language not retrieved", "en-gb", result);
	}

	/**
	 * This method tests if the language returned is correct with a PathInfo with localization.
	 * We test here us english.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByPathInfoWithUSEnglish() throws Exception {
		String pathInfo = "/nameofsite/country/us/en-US/TEMPLATE.PRELOGIN";
		String result = (new Localizer()).getLanguageCodeByPathInfo(pathInfo);
		assertEquals("language not retrieved", "en-us", result);
	}

	/**
	 * This method tests if the language returned is correct with a PathInfo with localization.
	 * We test here us english.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByPathInfoWithoutCountry() throws Exception {
		String pathInfo = "/nameofsite/us/en/TEMPLATE.PRELOGIN";
		String result = (new Localizer()).getLanguageCodeByPathInfo(pathInfo);
		assertEquals("language not retrieved", "en", result);
	}

	/**
	 * This method tests if the language returned is null with small path info.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByPathInfoWithSmallPath() throws Exception {
		String pathInfo = "/titi";
		String result = (new Localizer()).getLanguageCodeByPathInfo(pathInfo);
		assertNull(result);
	}

	/**
	 * This method tests if the language returned is correct with a PathInfo with localization.
	 * We test here us english.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByPathInfoWithDialect() throws Exception {
		String pathInfo = "/nameofsite/country/us/en-emea_africa/TEMPLATE.PRELOGIN";
		String result = (new Localizer()).getLanguageCodeByPathInfo(pathInfo);
		assertEquals("language not retrieved", "en-emea_africa", result);
	}

	/**
	 * Bug 1906.
	 * 
	 * @throws Exception
	 */
	public void test1906() throws Exception {
		String pathInfo = "/publicsppportal/country/CA/fr";
		String result = (new Localizer()).getLanguageCodeByPathInfo(pathInfo);
		assertEquals("language not retrieved", "fr", result);
	}

	/**
	 * Bug 1906.
	 * 
	 * @throws Exception
	 */
	public void test1906b() throws Exception {
		String pathInfo = "/sppportal/template.PRELOGIN/";
		String result = (new Localizer()).getLanguageCodeByPathInfo(pathInfo);
		assertNull(result);
	}

	/**
	 * This method tests if the country returned is null with a PathInfo null or "".
	 * 
	 * @throws Exception
	 */
	public void testGetCountryCodeByPathInfoVoid() throws Exception {
		String pathInfo = null;
		String result = (new Localizer()).getCountryCodeByPathInfo(pathInfo);
		assertNull(result);
		pathInfo = "";
		result = (new Localizer()).getCountryCodeByPathInfo(pathInfo);
		assertNull(result);
	}

	/**
	 * This method tests if the language returned is correct with a PathInfo with localization.
	 * We test here us english.
	 * 
	 * @throws Exception
	 */
	public void testGetCountryCodeByPathInfoWithoutCountry() throws Exception {
		String pathInfo = "/nameofsite/US/en/TEMPLATE.PRELOGIN";
		String result = (new Localizer()).getCountryCodeByPathInfo(pathInfo);
		assertEquals("language not retrieved", "us", result);
	}

	/**
	 * This method tests if the country returned is null with a PathInfo without localization.
	 * 
	 * @throws Exception
	 */
	public void testGetCountryCodeByPathInfoWithoutLocalization() throws Exception {
		String pathInfo = "/nameofsite/TEMPLATE.PRELOGIN";
		String result = (new Localizer()).getCountryCodeByPathInfo(pathInfo);
		assertNull(result);
	}

	/**
	 * This method tests if the language returned is correct with a PathInfo with localization.
	 * We test here gb english.
	 * 
	 * @throws Exception
	 */
	public void testGetCountryCodeByPathInfoWithHPLocalization() throws Exception {
		String pathInfo = "/nameofsite/country/fr/en-GB/TEMPLATE.PRELOGIN";
		String result = (new Localizer()).getCountryCodeByPathInfo(pathInfo);
		assertEquals("language not retrieved", "fr", result);
	}

	/**
	 * This method tests if the language returned is null with a URL without parameters.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByURLVoid() throws Exception {
		HashMap parameters = new HashMap();
		String result = (new Localizer()).getLanguageCodeByURLParameters(parameters);
		assertNull(result);
	}

	/**
	 * This method tests if the language returned is correct with localization parameters. We
	 * test here gb english.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByURLWithHPLocalization() throws Exception {
		HashMap parameters = new HashMap();
		String[] languages = {"en"};
		parameters.put("lang", languages);
		String result = (new Localizer()).getLanguageCodeByURLParameters(parameters);
		assertEquals("language not retrieved", "en", result);
	}

	/**
	 * This method tests if the language returned is correct with localization parameters. We
	 * test here en-gb_IR.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByURLWithLanguage4() throws Exception {
		HashMap parameters = new HashMap();
		String[] languages = {"en-gb"};
		parameters.put("lang", languages);
		String result = (new Localizer()).getLanguageCodeByURLParameters(parameters);
		assertEquals("language not retrieved", "en-gb", result);
	}

	/**
	 * This method tests if the language returned is correct with localization parameters. We
	 * test here en-gb_IR.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByURLWithDialect() throws Exception {
		HashMap parameters = new HashMap();
		String[] languages = {"en-emea_africa"};
		parameters.put("lang", languages);
		String[] countries = {"IR"};
		parameters.put("cc", countries);
		String result = (new Localizer()).getLanguageCodeByURLParameters(parameters);
		assertEquals("language not retrieved", "en-emea_africa", result);
	}

	/**
	 * This method tests if the language returned is correct with language localization. We
	 * test here fr.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByURLWithOnlyLanguage() throws Exception {
		HashMap parameters = new HashMap();
		String[] languages = {"fr"};
		parameters.put("lang", languages);
		String result = (new Localizer()).getLanguageCodeByURLParameters(parameters);
		assertEquals("language not retrieved", "fr", result);
	}

	/**
	 * This method tests if the language returned is correct with language localization. We
	 * test here en-gb.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByURLWithOnlyLanguage4() throws Exception {
		HashMap parameters = new HashMap();
		String[] languages = {"en-gb"};
		parameters.put("lang", languages);
		String result = (new Localizer()).getLanguageCodeByURLParameters(parameters);
		assertEquals("language not retrieved", "en-gb", result);
	}

	/**
	 * This method tests if the language returned is correct with language localization. We
	 * test here en-gb.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByURLWithOnlyDialect() throws Exception {
		HashMap parameters = new HashMap();
		String[] languages = {"en-emea_africa"};
		parameters.put("lang", languages);
		String result = (new Localizer()).getLanguageCodeByURLParameters(parameters);
		assertEquals("language not retrieved", "en-emea_africa", result);
	}

	/**
	 * This method tests if the country returned is null with a URL without parameters.
	 * 
	 * @throws Exception
	 */
	public void testGetCountryCodeByURLVoid() throws Exception {
		HashMap parameters = new HashMap();
		String result = (new Localizer()).getCountryCodeByURLParameters(parameters);
		assertNull(result);
	}

	/**
	 * This method tests if the country returned is correct with localization parameters. We
	 * test here FR.
	 * 
	 * @throws Exception
	 */
	public void testGetCountryCodeByURLWithHPLocalization() throws Exception {
		HashMap parameters = new HashMap();
		String[] countries = {"FR"};
		parameters.put("cc", countries);
		String result = (new Localizer()).getCountryCodeByURLParameters(parameters);
		assertEquals("country not retrieved", "fr", result);
	}

	/**
	 * This method tests if the language returned is correct with language localization. We
	 * test here fr.
	 * 
	 * @throws Exception
	 */
	public void testGetCountryCodeByURLWithOnlyCountry() throws Exception {
		HashMap parameters = new HashMap();
		String[] countries = {"IR"};
		parameters.put("cc", countries);
		String result = (new Localizer()).getCountryCodeByURLParameters(parameters);
		assertEquals("country not retrieved", "ir", result);
	}

	/**
	 * This method tests if the language returned is null without cookie.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByCookieVoid() throws Exception {
		Cookie[] cookieTab = new Cookie[1];
		String result = (new Localizer()).getLanguageCodeByCookie(cookieTab);
		assertNull(result);
	}

	/**
	 * This method tests if the language returned is correct with localization cookies. We test
	 * here fr FR.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByCookieWithHPLocalization() throws Exception {
		Cookie cookieLang = new Cookie("lang", "fr");
		Cookie cookieCountry = new Cookie("cc", "FR");
		Cookie[] cookieTab = {cookieLang, cookieCountry};
		String result = (new Localizer()).getLanguageCodeByCookie(cookieTab);
		assertEquals("language not retrieved", "fr", result);
	}

	/**
	 * This method tests if the language returned is correct with language cookie. We test here
	 * english.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByCookieWithOnlyLanguage() throws Exception {
		Cookie cookieLang = new Cookie("lang", "en");
		Cookie[] cookieTab = {cookieLang};
		String result = (new Localizer()).getLanguageCodeByCookie(cookieTab);
		assertEquals("language not retrieved", "en", result);
	}

	/**
	 * This method tests if the country returned is null with language cookie.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByCookieWithOnlyCountry() throws Exception {
		Cookie cookieCountry = new Cookie("cc", "us");
		Cookie[] cookieTab = {cookieCountry};
		String result = (new Localizer()).getLanguageCodeByCookie(cookieTab);
		assertNull(result);
	}

	/**
	 * Bug 1907.
	 * 
	 * @throws Exception
	 */
	public void test1907() throws Exception {
		Cookie cookieLang = new Cookie("lang", "fr-ca");
		Cookie[] cookieTab = {cookieLang};
		String result = (new Localizer()).getLanguageCodeByCookie(cookieTab);
		assertEquals("language not retrieved", "fr-ca", result);
	}

	/**
	 * This method tests if the country returned is null without cookie.
	 * 
	 * @throws Exception
	 */
	public void testGetCountryCodeByCookieVoid() throws Exception {
		Cookie[] cookieTab = new Cookie[1];
		String result = (new Localizer()).getCountryCodeByCookie(cookieTab);
		assertNull(result);
	}

	/**
	 * This method tests if the country returned is correct with localization cookies. We test
	 * here fr FR.
	 * 
	 * @throws Exception
	 */
	public void testGetCountryCodeByCookieWithHPLocalization() throws Exception {
		Cookie cookieLang = new Cookie("lang", "fr");
		Cookie cookieCountry = new Cookie("cc", "FR");
		Cookie[] cookieTab = {cookieLang, cookieCountry};
		String result = (new Localizer()).getCountryCodeByCookie(cookieTab);
		assertEquals("country not retrieved", "fr", result);
	}

	/**
	 * This method tests if the country returned is null with language cookie. We test here
	 * english.
	 * 
	 * @throws Exception
	 */
	public void testGetCountryCodeByCookieWithOnlyLanguage() throws Exception {
		Cookie cookieLang = new Cookie("lang", "en");
		Cookie[] cookieTab = {cookieLang};
		String result = (new Localizer()).getCountryCodeByCookie(cookieTab);
		assertNull(result);
	}

	/**
	 * This method tests if the country returned is returned with country cookie. We test here
	 * IR.
	 * 
	 * @throws Exception
	 */
	public void testGetCountryCodeByCookieWithOnlyCountry() throws Exception {
		Cookie cookieCountry = new Cookie("cc", "IR");
		Cookie[] cookieTab = {cookieCountry};
		String result = (new Localizer()).getCountryCodeByCookie(cookieTab);
		assertEquals("country not retrieved", "ir", result);
	}

	/**
	 * This method tests if the language returned is null with null locale.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByBrowserPreferenceVoid() throws Exception {
		Locale locale = null;
		String result = (new Localizer()).getLanguageCodeBrowserPreference(locale);
		assertNull(result);
	}

	
	/**
	 * This method tests if the language returned is null with ukranian locale.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByBrowserPreferenceUkrainian() throws Exception {
		Locale locale = new Locale("uk");
		String result = (new Localizer()).getLanguageCodeBrowserPreference(locale);
		assertNull(result);
	}

	/**
	 * This method tests if the language returned is correct with language locale. We test here
	 * french canadian.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByBrowserPreferenceFrenchCanadian() throws Exception {
		Locale locale = new Locale("fr_CA");
		String result = (new Localizer()).getLanguageCodeBrowserPreference(locale);
		assertEquals("language not retrieved", "fr-ca", result);
	}
	
	/**
	 * This method tests if the language returned is correct with language locale. We test here
	 * british englis.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByBrowserPreferenceBrithishEnglish() throws Exception {
		Locale locale = new Locale("en_GB");
		String result = (new Localizer()).getLanguageCodeBrowserPreference(locale);
		assertEquals("language not retrieved", "en-gb", result);
	}
	
	/**
	 * This method tests if the language returned is correct with language locale. We test here
	 * english.
	 * 
	 * @throws Exception
	 */
	public void testGetLanguageCodeByBrowserPreference() throws Exception {
		Locale locale = new Locale("en");
		String result = (new Localizer()).getLanguageCodeBrowserPreference(locale);
		assertEquals("language not retrieved", "en", result);
	}

	/**
	 * This method tests if the insertion of localization in URL is correct.
	 * 
	 * @throws Exception
	 */
	public void testRewriteURLPathWithLocalization() throws Exception {
		StringBuffer result = new StringBuffer("http://host:8001/portal/site/sitename");
		String languageCode = "fr_FR";
		(new Localizer()).addLocaleParameters(result, languageCode, '?');
		assertEquals("wrong rewriting of url",
				"http://host:8001/portal/site/sitename?lang=fr&cc=FR", result.toString());
	}

	/**
	 * This method tests if the insertion of localization in URL is correct with parameters.
	 * 
	 * @throws Exception
	 */
	public void testRewriteURLPathWithLocalizationAndParameters() throws Exception {
		StringBuffer result = new StringBuffer("http://host:8001/portal/site/sitename?age=32");
		String languageCode = "fr_FR";
		(new Localizer()).addLocaleParameters(result, languageCode, '&');
		assertEquals("wrong rewriting of url",
				"http://host:8001/portal/site/sitename?age=32&lang=fr&cc=FR", result
						.toString());
	}

	private HttpSession session() {
		return
			(HttpSession) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {HttpSession.class},
				new InvocationHandler() {
					Map mAttributes = new HashMap();
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if ("setAttribute".equals(method.getName())) {
							mAttributes.put(args[0], args[1]);
						}
						else if ("removeAttribute".equals(method.getName())) {
							mAttributes.remove(args[0]);
						}
						else if ("getAttribute".equals(method.getName())) {
							return mAttributes.get(args[0]);
						}
						else if ("toString".equals(method.getName())) {
							return "MockSession=" + mAttributes;
						}
						else {
							throw new UnsupportedOperationException("Unexpected call for this mock object: " + method.getName());
						}
						return null;
					}
				});
	}
	
	private HttpServletResponse resp() {
		return
			(HttpServletResponse) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {HttpServletResponse.class},
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if ("toString".equals(method.getName())) {
							return "MockResponse";
						}
						return method.invoke(proxy, args);
					}
				});
	}

	private HttpServletRequest req(final HttpSession session, final String methodName, final String url, final Map params, final Cookie[] cookies,final Locale locale) {
		return
			(HttpServletRequest) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {HttpServletRequest.class},
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if ("toString".equals(method.getName())) {
							return "MockRequest=[" + methodName + ", " + url + ", " + params + "]";
						}
						else if ("getSession".equals(method.getName())) {
							return session;
						}
						else if ("getMethod".equals(method.getName())) {
							return methodName;
						}
						else if ("getRequestURL".equals(method.getName())) {
							int pos = url.indexOf('?');
							return new StringBuffer(pos == -1 ? url : url.substring(0, pos));
						}
						else if ("getRequestURI".equals(method.getName())) {
							int pos = url.indexOf("://");
							int pos2 = url.indexOf('/', pos + "://".length());
							int pos3 = url.indexOf('?');
							return pos3 == -1 ? url.substring(pos2) : url.substring(pos2, pos3);
						}
						else if ("getQueryString".equals(method.getName())) {
							int pos = url.indexOf('?');
							return pos == -1 ? null : url.substring(pos+1);
						}
						else if ("getParameterMap".equals(method.getName())) {
							return params;
						}
						else if ("getPathInfo".equals(method.getName())) {
							return url;
						}
						else if ("getCookies".equals(method.getName())) {
							return cookies;
						}
						else if ("getLocale".equals(method.getName())) {
							return locale;
						}
						else {
							throw new UnsupportedOperationException("Unsupported call for this mock object: " + method.getName());
						}
					}
				});
	}
	
}
