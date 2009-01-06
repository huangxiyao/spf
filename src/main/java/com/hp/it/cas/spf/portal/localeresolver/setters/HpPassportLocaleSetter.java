/*
 * Project: Service Portal
 * Copyright (c) 2006 HP. All Rights Reserved
 * 
 * 
 * FILENAME: HpPassportLocaleSetter.java
 * PACKAGE : com.hp.it.cas.spf.portal.localeresolver.setters
 * $Id: HpPassportLocaleSetter.java,v 1.4 2007/05/18 07:31:53 marcd Exp $
 * $Log: HpPassportLocaleSetter.java,v $
 * Revision 1.4  2007/05/18 07:31:53  marcd
 * interface
 *
 * Revision 1.3  2007/05/17 07:21:01  marcd
 * add javadoc
 *
 * Revision 1.2  2007/04/17 01:41:32  marcd
 * add stuff for javadoc
 *
 *
 */ 
package com.hp.it.cas.spf.portal.localeresolver.setters;

import java.util.HashSet;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import com.epicentric.common.website.CookieUtils;
import com.hp.globalops.hppcbl.passport.PassportService;
import com.hp.globalops.hppcbl.passport.PassportServiceException;
import com.hp.globalops.hppcbl.webservice.GetUserCoreResponseElement;
import com.hp.globalops.hppcbl.webservice.ProfileCore;
import com.hp.it.spf.xa.i18n.portal.I18nUtility;
import com.hp.it.spf.xa.misc.portal.Consts;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class HpPassportLocaleSetter implements ILocaleSetter {
    private static final com.vignette.portal.log.LogWrapper LOG = new com.vignette.portal.log.LogWrapper(
            HpPassportLocaleSetter.class);
    private static HashSet acceptedPreferredLangCodes;
    private PassportService hppService;

    static {
        HashSet codes = new HashSet();
        codes.add(I18nUtility.HPP_SIMP_CHINESE_LANG);
        codes.add(I18nUtility.HPP_TRAD_CHINESE_LANG);
        codes.add("cs");
        codes.add("da");
        codes.add("de");
        codes.add("el");
        codes.add("en");
        codes.add("es");
        codes.add("fi");
        codes.add("fr");
        codes.add("hu");
        codes.add("it");
        codes.add("ja");
        codes.add("ko");
        codes.add("nl");
        codes.add("no");
        codes.add("pl");
        codes.add("pt");
        codes.add("ru");
        codes.add("sv");
        codes.add("tr");
        acceptedPreferredLangCodes = codes;
    }

    /**
     * @param service web service class
     */
    public HpPassportLocaleSetter(PassportService service) {
        this.hppService = service;
    }

    /**
     * @return web service class
     */
    public PassportService getPassportService() {
        return hppService;
    }

    /**
     * Updates hpp.
     * @param request the http sevlet request
     * @param response the http sevlet response
     * @param locale the locale to write to the hpp database
     */
    public void setLocale(HttpServletRequest request,
            HttpServletResponse response, final Locale locale) {

        String sessionToken = CookieUtils.getCookieValue(request,
                Consts.COOKIE_NAME_SMSESSION);
        String langCode = this.getHppFormatLanguageCode(locale);

        if (hppProfileCanBeUpdated(langCode, sessionToken)) {
            try {
                GetUserCoreResponseElement rspGet = hppService
                        .getUserCore(sessionToken);
                ProfileCore profileCore = rspGet.getProfileCore();
                profileCore.setLangCode(langCode);
                /*ModifyUserResponseElement rspModify = */
                hppService.modifyUser(sessionToken, profileCore, null);

            } catch (PassportServiceException e) {
                LOG.error(e);
            }
        }

    }

    private String getHppFormatLanguageCode(Locale locale) {
        String lang = locale.getLanguage();
        String country = locale.getCountry();
        if (lang.equalsIgnoreCase("zh") && country.equalsIgnoreCase("tw")) {
            lang = I18nUtility.HPP_TRAD_CHINESE_LANG;
        } else {
            lang = I18nUtility.HPP_SIMP_CHINESE_LANG;
        }
        return lang;
    }

    private boolean hppProfileCanBeUpdated(String langCode, String sessionToken) {
        return (acceptedPreferredLangCodes.contains(langCode.toLowerCase()) && StringUtils
                .hasLength(sessionToken));
    }

    /**
     * @param request the http servlet request
     * @return true if the locale needs setting
     */
    public boolean shouldResolveLocale(HttpServletRequest request) {
        return true;
    }

}
