/*
 * Project: Service Portal
 * Copyright (c) 2006 HP. All Rights Reserved
 * 
 * 
 * FILENAME: LocaleSetter.java
 * PACKAGE : com.hp.it.cas.spf.portal.localeresolver.setters
 * $Id: ILocaleSetter.java,v 1.1 2007/05/18 07:31:53 marcd Exp $
 * $Log: ILocaleSetter.java,v $
 * Revision 1.1  2007/05/18 07:31:53  marcd
 * interface
 *
 * Revision 1.3  2007/05/17 07:12:21  marcd
 * add javadoc
 *
 * Revision 1.2  2007/04/17 01:41:32  marcd
 * add stuff for javadoc
 *
 *
 */ 
package com.hp.it.cas.spf.portal.localeresolver.setters;

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
