package com.hp.it.spf.xa.i18n;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.hp.it.spf.xa.i18n.I18nUtility;

import junit.framework.TestCase;

public class I18nUtilityTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetLongTimezoneDisplayName() {
        TimeZone tz = TimeZone.getTimeZone("GMT+8");
        Locale locale = new Locale("zh", "cn");
        String name = I18nUtility.getLongTimezoneDisplayName(tz, locale);
        assertEquals("GMT+08:00", name);
        Date date = new Date();
        I18nUtility.getLongTimezoneDisplayName(date, tz, locale);
        assertEquals("GMT+08:00", name);
    }

    public void testGetShortTimezoneDisplayName() {
        TimeZone tz = TimeZone.getTimeZone("GMT+8");
        Locale locale = new Locale("zh", "cn");
        String name = I18nUtility.getShortTimezoneDisplayName(tz, locale);
        assertEquals("GMT+08:00", name);
        Date date = new Date();
        I18nUtility.getShortTimezoneDisplayName(date, tz, locale);
        assertEquals("GMT+08:00", name);
    }

    public void testHppLanguageToLocaleString() {
        String lCode = "12";
        Locale locale = I18nUtility.hppLanguageToLocale(lCode);
        assertEquals(new Locale("zh", "tw"), locale);
        String cCode = "aabb";
        locale = I18nUtility.hppLanguageToLocale(lCode, cCode);
        assertEquals(new Locale("zh", "tw"), locale);

        lCode = "13";
        locale = I18nUtility.hppLanguageToLocale(lCode);
        assertEquals(new Locale("zh", "cn"), locale);
        cCode = "cccasdf";
        locale = I18nUtility.hppLanguageToLocale(lCode, cCode);
        assertEquals(new Locale("zh", "cn"), locale);

        lCode = "aa";
        locale = I18nUtility.hppLanguageToLocale(lCode);
        assertEquals(new Locale("aa"), locale);
        cCode = "cccasdf";
        locale = I18nUtility.hppLanguageToLocale(lCode, cCode);
        assertEquals(new Locale("aa", "cccasdf"), locale);
    }

    public void testLocaleToHPPLanguage() {
        Locale locale = new Locale("en", "us");
        String hppCode = I18nUtility.localeToHPPLanguage(locale);
        assertEquals("en", hppCode);

        locale = new Locale("zh", "tw");
        hppCode = I18nUtility.localeToHPPLanguage(locale);
        assertEquals("12", hppCode);

        locale = new Locale("zh", "cn");
        hppCode = I18nUtility.localeToHPPLanguage(locale);
        assertEquals("13", hppCode);

        locale = new Locale("zh", "hk");
        hppCode = I18nUtility.localeToHPPLanguage(locale);
        assertEquals("12", hppCode);
    }

    public void testHppLanguageToISOLanguage() {
        String hppCode = "12";
        String isoCode = I18nUtility.hppLanguageToISOLanguage(hppCode);
        assertEquals("zh", isoCode);

        hppCode = "13";
        isoCode = I18nUtility.hppLanguageToISOLanguage(hppCode);
        assertEquals("zh", isoCode);

        hppCode = "11";
        isoCode = I18nUtility.hppLanguageToISOLanguage(hppCode);
        assertEquals("11", isoCode);

        hppCode = "EN";
        isoCode = I18nUtility.hppLanguageToISOLanguage(hppCode);
        assertEquals("en", isoCode);
    }

    public void testLocaleToLanguageTag() {
        Locale locale = new Locale("zh", "wwlala");
        String langtag = I18nUtility.localeToLanguageTag(locale);
        assertEquals("zh-WWLALA", langtag);
    }

    public void testLanguageTagToLocale() {
        Locale locale = I18nUtility.languageTagToLocale("zh-cN");
        assertEquals(new Locale("zh", "cn"), locale);
    }

    public void testSortLocalesbyLocaleCode() {
        Collection locales = new ArrayList(4);
        locales.add(new Locale("th"));
        locales.add(new Locale("en", "US"));
        locales.add(new Locale("zh"));
        locales.add(new Locale("en"));
        Collection result = I18nUtility.sortLocales(locales);
        Iterator iterator = result.iterator();

        Locale tempLocale = null;
        tempLocale = (Locale)iterator.next();
        assertEquals(new Locale("zh"), tempLocale);
        tempLocale = (Locale)iterator.next();
        assertEquals(new Locale("en"), tempLocale);
        tempLocale = (Locale)iterator.next();
        assertEquals(new Locale("en", "US"), tempLocale);
        tempLocale = (Locale)iterator.next();
        assertEquals(new Locale("th"), tempLocale);
    }

    public void testGetLocaleDisplayName() {
        Locale locale = new Locale("de", "DE");
        String name = I18nUtility.getLocaleDisplayName(locale);
//        assertEquals(name, "中国 - 中文");
        assertEquals("Deutsch-Deutschland", name);
    }

    public void testGetUserDisplayName() {
        String fn = "yingzhi";
        String ln = "wu";
        
        Locale plocale = Locale.CHINA;
        String userDisName = I18nUtility.getUserDisplayName(fn, ln, plocale);
        assertEquals("wu yingzhi", userDisName);
        
        plocale = Locale.TAIWAN;
        userDisName = I18nUtility.getUserDisplayName(fn, ln, plocale);
        assertEquals("wu yingzhi", userDisName);
        
        plocale = Locale.KOREA;
        userDisName = I18nUtility.getUserDisplayName(fn, ln, plocale);
        assertEquals("wu yingzhi", userDisName);
        
        plocale = Locale.JAPAN;
        userDisName = I18nUtility.getUserDisplayName(fn, ln, plocale);
        assertEquals("wu yingzhi", userDisName);
        
        plocale = Locale.US;
        userDisName = I18nUtility.getUserDisplayName(fn, ln, plocale);
        assertEquals("yingzhi wu", userDisName);
    }

    public void testGetShortDisplayDate() {
    	SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yyyy hh:mm:ss a z");
    	try {
    		TimeZone tz = TimeZone.getTimeZone("GMT");
    		Calendar cal = Calendar.getInstance(tz);
    		cal.set(2010, 0, 12, 23, 34, 56);
    		cal.setTimeZone(tz);
    		Date date = cal.getTime();
    		Locale locale = Locale.GERMANY;
    		tz = TimeZone.getTimeZone("Europe/Berlin");
    		String s = I18nUtility.getShortDisplayDate(date, tz, locale);
    		assertEquals("13.01.10 00:34", s);
    		s = I18nUtility.getShortDisplayDate(date, tz, locale, false);
    		assertEquals("13.01.10", s);
    	} catch (Exception e) {
    	}
    }
    
    public void testGetMediumDisplayDate() {
    	SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yyyy hh:mm:ss a z");
    	try {
    		TimeZone tz = TimeZone.getTimeZone("GMT");
    		Calendar cal = Calendar.getInstance(tz);
    		cal.set(2010, 0, 12, 23, 34, 56);
    		cal.setTimeZone(tz);
    		Date date = cal.getTime();
    		Locale locale = Locale.GERMANY;
    		tz = TimeZone.getTimeZone("Europe/Berlin");
    		String s = I18nUtility.getMediumDisplayDate(date, tz, locale);
    		assertEquals("13.01.2010 00:34:56", s);
    		s = I18nUtility.getMediumDisplayDate(date, tz, locale, false);
    		assertEquals("13.01.2010", s);
    	} catch (Exception e) {
    	}
    }

    public void testGetLongDisplayDate() {
    	SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yyyy hh:mm:ss a z");
    	try {
    		TimeZone tz = TimeZone.getTimeZone("GMT");
    		Calendar cal = Calendar.getInstance(tz);
    		cal.set(2010, 0, 12, 23, 34, 56);
    		cal.setTimeZone(tz);
    		Date date = cal.getTime();
    		Locale locale = Locale.GERMANY;
    		tz = TimeZone.getTimeZone("Europe/Berlin");
    		String s = I18nUtility.getLongDisplayDate(date, tz, locale);
    		assertEquals("13. Januar 2010 00:34:56 CET", s);
    		s = I18nUtility.getLongDisplayDate(date, tz, locale, false);
    		assertEquals("13. Januar 2010", s);
    	} catch (Exception e) {
    	}
    }

    public void testGetFullDisplayDate() {
    	SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yyyy hh:mm:ss a z");
    	try {
    		TimeZone tz = TimeZone.getTimeZone("GMT");
    		Calendar cal = Calendar.getInstance(tz);
    		cal.set(2010, 0, 12, 23, 34, 56);
    		cal.setTimeZone(tz);
    		Date date = cal.getTime();
    		Locale locale = Locale.GERMANY;
    		tz = TimeZone.getTimeZone("Europe/Berlin");
    		String s = I18nUtility.getFullDisplayDate(date, tz, locale);
    		assertEquals("Mittwoch, 13. Januar 2010 0.34 Uhr CET", s);
    		s = I18nUtility.getFullDisplayDate(date, tz, locale, false);
    		assertEquals("Mittwoch, 13. Januar 2010", s);
    	} catch (Exception e) {
    	}
    }

}
