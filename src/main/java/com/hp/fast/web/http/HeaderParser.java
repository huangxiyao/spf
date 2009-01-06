/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 */

package com.hp.fast.web.http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

class HeaderParser
{
	private static final String QVALUE = "[01]\\.\\d{0,3}";
	private static final Pattern HEADER_VALUE = Pattern.compile("([^;, ]+)\\s*(?:;\\s*q\\s*=\\s*(" + QVALUE + "))?");
	
	private final String headerName;
	
	HeaderParser(String acceptHeaderName)
	{
		this.headerName = acceptHeaderName;
	}
	
	//@SuppressWarnings("unchecked")
	List values(HttpServletRequest request)
	{
		String attributeName = getClass().getName() + "-" + headerName;
		List values = (List) request.getAttribute(attributeName);
		
		if (values == null)
		{
			values = new ArrayList();
			request.setAttribute(attributeName, values);

			List headers = new ArrayList();
			for (Enumeration requestHeaders = request.getHeaders(headerName); requestHeaders.hasMoreElements();)
			{
				Matcher matcher = HEADER_VALUE.matcher((String) requestHeaders.nextElement());
				while (matcher.find())
				{
					if (matcher.group(2) == null)
					{
						headers.add(new Header(matcher.group(1)));
					}
					else
					{
						headers.add(new Header(matcher.group(1), matcher.group(2)));
					}
				}
			}			
			
			Collections.sort(headers, Collections.reverseOrder());
            Iterator itr = headers.iterator();
            while (itr.hasNext()) {
                Header header = (Header) itr.next();
                values.add(header.value);
            }
		}
		
		return values;
	}
	
	private class Header implements Comparable
	{
		String value;
		float weight;
		
		Header(String value)
		{
			this.value = value;
		}
		
		Header(String value, String quality)
		{
			this.value = value;
			this.weight = Float.parseFloat(quality);
		}
		
		public int compareTo(Object other)
		{
            Header temp = (Header) other;
			return this.weight < temp.weight ? -1 : this.weight > temp.weight ? 1 : 0;
		}
		
	}
}
