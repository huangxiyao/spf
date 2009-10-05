/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.it.spf.localeresolver.http;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Negotiates languages based on the HTTP <code>Accept-Language</code> header.
 *
 * @author Quintin May
 * @version $Revision: 1.1 $
 */
public class LanguageNegotiator implements ContentNegotiator
{
	private static final String HEADER = "Accept-Language";
	private static final String ACCEPTABLE = LanguageNegotiator.class.getName() + ".ACCEPTABLE";
	private static final String NEGOTIATED_VALUE = LanguageNegotiator.class.getName() + ".NEGOTIATED_VALUE";
	
	private final Collection acceptableLocales;
	
	/**
	 * Creates a language negotiator.
	 * @param acceptableLocales the acceptable locales.
	 */
	public LanguageNegotiator(Object[] acceptableLocales)
	{
		this.acceptableLocales = new ArrayList(Arrays.asList(acceptableLocales));
	}

	//@SuppressWarnings("unchecked")
	public boolean acceptable(HttpServletRequest request)
	{
		Boolean acceptable = (Boolean) request.getAttribute(ACCEPTABLE);
		
		if (acceptable == null)
		{
			List acceptedLocales = Collections.list(request.getLocales());
			Locale negotiatedLocale = Negotiators.resolveLocale(acceptedLocales, acceptableLocales);
			
			acceptable = Boolean.valueOf(negotiatedLocale != null);
			request.setAttribute(ACCEPTABLE, acceptable);
			
			if (acceptable.booleanValue())
			{
				request.setAttribute(NEGOTIATED_VALUE, negotiatedLocale);
			}
		}
		
		return acceptable.booleanValue();
	}

	public Object negotiatedValue(HttpServletRequest request)
	{
		return acceptable(request) ? (Locale) request.getAttribute(NEGOTIATED_VALUE) : null;
	}
	
	public void negotiate(HttpServletRequest request, HttpServletResponse response) throws NoAcceptableLanguageException
	{
		Locale negotiatedLocale = null;
		
		response.addHeader("Vary", HEADER);

		if (acceptable(request))
		{
			negotiatedLocale = (Locale) request.getAttribute(NEGOTIATED_VALUE);
			response.setLocale(negotiatedLocale);
		}
		else
		{
			throw new NoAcceptableLanguageException("No acceptable language could be negotiated.", acceptableLocales);
		}
	}
}
