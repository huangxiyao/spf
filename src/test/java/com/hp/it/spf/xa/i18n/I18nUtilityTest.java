package com.hp.it.spf.xa.i18n;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Date;

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
        assertEquals(name, "GMT+08:00");
        Date date = new Date();
        I18nUtility.getLongTimezoneDisplayName(date, tz, locale);
        assertEquals(name, "GMT+08:00");
    }

    public void testGetShortTimezoneDisplayName() {
        TimeZone tz = TimeZone.getTimeZone("GMT+8");
        Locale locale = new Locale("zh", "cn");
        String name = I18nUtility.getShortTimezoneDisplayName(tz, locale);
        assertEquals(name, "GMT+08:00");
        Date date = new Date();
        I18nUtility.getShortTimezoneDisplayName(date, tz, locale);
        assertEquals(name, "GMT+08:00");
    }

    public void testHppLanguageToLocaleString() {
        String lCode = "12";
        Locale locale = I18nUtility.hppLanguageToLocale(lCode);
        assertEquals(locale, new Locale("zh", "tw"));
        String cCode = "aabb";
        locale = I18nUtility.hppLanguageToLocale(lCode, cCode);
        assertEquals(locale, new Locale("zh", "tw"));

        lCode = "13";
        locale = I18nUtility.hppLanguageToLocale(lCode);
        assertEquals(locale, new Locale("zh", "cn"));
        cCode = "cccasdf";
        locale = I18nUtility.hppLanguageToLocale(lCode, cCode);
        assertEquals(locale, new Locale("zh", "cn"));

        lCode = "aa";
        locale = I18nUtility.hppLanguageToLocale(lCode);
        assertEquals(locale, new Locale("aa"));
        cCode = "cccasdf";
        locale = I18nUtility.hppLanguageToLocale(lCode, cCode);
        assertEquals(locale, new Locale("aa", "cccasdf"));
    }

    public void testLocaleToHPPLanguage() {
        Locale locale = new Locale("en", "us");
        String hppCode = I18nUtility.localeToHPPLanguage(locale);
        assertEquals(hppCode, "en");

        locale = new Locale("zh", "tw");
        hppCode = I18nUtility.localeToHPPLanguage(locale);
        assertEquals(hppCode, "12");

        locale = new Locale("zh", "cn");
        hppCode = I18nUtility.localeToHPPLanguage(locale);
        assertEquals(hppCode, "13");

        locale = new Locale("zh", "hk");
        hppCode = I18nUtility.localeToHPPLanguage(locale);
        assertEquals(hppCode, "zh");
    }

    public void testHppLanguageToISOLanguage() {
        String hppCode = "12";
        String isoCode = I18nUtility.hppLanguageToISOLanguage(hppCode);
        assertEquals(isoCode, "zh");

        hppCode = "13";
        isoCode = I18nUtility.hppLanguageToISOLanguage(hppCode);
        assertEquals(isoCode, "zh");

        hppCode = "11";
        isoCode = I18nUtility.hppLanguageToISOLanguage(hppCode);
        assertEquals(isoCode, "11");

        hppCode = "EN";
        isoCode = I18nUtility.hppLanguageToISOLanguage(hppCode);
        assertEquals(isoCode, "en");
    }

    public void testLocaleToLanguageTag() {
        Locale locale = new Locale("zh", "wwlala");
        String langtag = I18nUtility.localeToLanguageTag(locale);
        assertEquals(langtag, "zh-WWLALA");
    }

    public void testLanguageTagToLocale() {
        Locale locale = I18nUtility.languageTagToLocale("zh-cN");
        assertEquals(locale, new Locale("zh", "cn"));
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
        assertEquals(tempLocale, new Locale("en"));
        tempLocale = (Locale)iterator.next();
        assertEquals(tempLocale, new Locale("en", "US"));
        tempLocale = (Locale)iterator.next();
        assertEquals(tempLocale, new Locale("th"));
        tempLocale = (Locale)iterator.next();
        assertEquals(tempLocale, new Locale("zh"));
    }

    /* public void testSlashify() {
        String result = I18nUtility.slashify("abc//ddd//f/fi///");
        assertEquals(result, "abc/ddd/f/fi/");
    }
    */

    public void testGetLocaleDisplayName() {
        Locale locale = new Locale("zh", "CN");
        String name = I18nUtility.getLocaleDisplayName(locale);
        assertEquals(name, "中文 (中国)");
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

}
