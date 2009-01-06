/*
 * Project: Service Portal
 * Copyright (c) 2006 HP. All Rights Reserved
 * 
 * 
 * FILENAME: MockLocaleProviderFactory.java
 * PACKAGE : com.hp.it.cas.spf.portal.localeresolver.filter
 * $Id: MockLocaleProviderFactory.java,v 1.2 2007/04/17 01:52:02 marcd Exp $
 * $Log: MockLocaleProviderFactory.java,v $
 * Revision 1.2  2007/04/17 01:52:02  marcd
 * add stuff for javadoc
 *
 *
 */ 
package com.hp.it.cas.spf.portal.localeresolver.mock;

import javax.servlet.http.HttpServletRequest;

import com.hp.fast.web.hpweb.LocaleProvider;
import com.hp.fast.web.hpweb.LocaleProviderFactory;

/**
 * @author <link href="marc.derosa@hp.com"></link>
 * @version $Revision 0$ $Date. 01/01/2007$
 */
public class MockLocaleProviderFactory implements LocaleProviderFactory {

    public LocaleProvider getLocaleProvider(HttpServletRequest request) {
        return new MockLocaleProvider(request);
    }

}
