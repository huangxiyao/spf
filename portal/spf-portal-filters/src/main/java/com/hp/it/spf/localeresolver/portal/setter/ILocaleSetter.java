/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.setter;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * LocaleSetters  are responsible for changing the locale 
 * state in external entities.
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public interface ILocaleSetter {
    
    /**
     * Assigns a locale to an environment.
     * @param request an incomming http servlet request
     * @param response an outgoing http servelet response
     * @param locale the locale to assign to the environment
     */
    void setLocale(HttpServletRequest request, HttpServletResponse response,
            Locale locale);
    /**
     * Read the current environment state and renders a decison as to 
     * whether the locale needs to be updated.
     * @param request the incomming http servlet request
     * @return true if the locale needs to be resolved and set
     */
    boolean shouldResolveLocale(HttpServletRequest request);
}
