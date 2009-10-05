/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.localeresolver.portal.provider;

import javax.servlet.http.HttpServletRequest;

import com.hp.it.spf.localeresolver.hpweb.TargetLocaleProvider;
import com.hp.it.spf.localeresolver.hpweb.TargetLocaleProviderFactory;

/**
 * A factory class for returning the target locale provider for the SPF Vignette
 * portal.
 * 
 * @author <link href="marc.derosa@hp.com"></link>
 * @author Scott Jorgenson
 * @version $Revision 2.0 $
 */
public class PortalRegisteredLocalesProviderFactory implements
		TargetLocaleProviderFactory {

	/**
	 * Creates a new target locale provider specific to the request.
	 * 
	 * @param request
	 *            the http servlet request
	 * @return target locale provider
	 */
	public TargetLocaleProvider getTargetLocaleProvider(
			HttpServletRequest request) {
		return new PortalRegisteredLocalesProvider(request);
	}
}
