package com.hp.fast.web.hpweb;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 * @author marc derosa
 *
 */
public class NullLocaleProviderFactory implements LocaleProviderFactory {

    public LocaleProvider getLocaleProvider(HttpServletRequest request) {
        return new NullLocaleProvider();
    }

}
