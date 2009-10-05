/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.provider;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.util.WebUtils;

import com.hp.it.spf.localeresolver.hpweb.AbstractLocaleProvider;
import com.hp.it.spf.localeresolver.hpweb.LocaleProvider;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.misc.portal.Consts;
import com.hp.it.spf.xa.misc.portal.Utils;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class HppHeaderLocaleProvider extends AbstractLocaleProvider implements LocaleProvider {
    private static final String CL_HEADER = "CL_Header";
    private static final String PREFERRED_LANG = "preferredlanguage";
    private static final String RESIDENT_COUNTRY = "hpresidentcountrycode";
    private static final String CHINESE = "zh";
    private static final String TAIWAN = "tw";
    private static final String CHINA = "cn";
    private Object preferredLanguageExtractor;
    private HttpServletRequest request;

    /**
     * @param request the http servlet request
     */
    public HppHeaderLocaleProvider(HttpServletRequest request) {
        this.request = request;
    }
    
    private boolean userNotLoggedIn() {
        Cookie cookie = WebUtils.getCookie(request, Consts.COOKIE_NAME_SMSESSION);
        return (cookie == null);
    }

    /**
     * Enables this class to be tested easily at the moment but could have other
     * uses if this resolver set up becomes general use.
     * @param extractor a user provided object the other member of this class are responsible
     * for casting to extract the infomation from the extractor
     */
    public void setPreferredLanguageExtractor(Object extractor) {
        this.preferredLanguageExtractor = extractor;
    }
    
    /**
     * @return a two character ansi language code
     */
    public String getLanguage() {
        if (userNotLoggedIn()) {
            return null;
        }
        String preferredLanguage;
        if (this.preferredLanguageExtractor == null) {
            String decodedHeaderInfo = Utils.getRequestHeader(request, CL_HEADER, true);
            preferredLanguage = Utils.getValueFromCLHeader(decodedHeaderInfo,
                    PREFERRED_LANG);
        } else {
            preferredLanguage = this.preferredLanguageExtractor.toString();
        }
        
        if (I18nUtility.HPP_TRAD_CHINESE_LANG.equals(preferredLanguage) 
                || I18nUtility.HPP_SIMP_CHINESE_LANG.equals(preferredLanguage)) {
            preferredLanguage = CHINESE;
        }
        return preferredLanguage;
    }
    
    /**
     * @return a two character ansi country code
     */
    public String getCountry() {
        if (userNotLoggedIn()) {
            return null;
        }
        String preferredCountry = "";
        String preferredLanguage = "";
        if (this.preferredLanguageExtractor == null) {
            String decodedHeaderInfo = Utils.getRequestHeader(request, CL_HEADER, true);
            preferredLanguage = Utils.getValueFromCLHeader(decodedHeaderInfo,
                    PREFERRED_LANG);
            preferredCountry = Utils.getValueFromCLHeader(decodedHeaderInfo,
                    RESIDENT_COUNTRY);
        } else {
            preferredLanguage = this.preferredLanguageExtractor.toString();
        }
        if (I18nUtility.HPP_TRAD_CHINESE_LANG.equals(preferredLanguage)) {
            preferredCountry = TAIWAN;
        } else if (I18nUtility.HPP_SIMP_CHINESE_LANG.equals(preferredLanguage)) {
            preferredCountry = CHINA;
        }
        return preferredCountry;
    }


}
