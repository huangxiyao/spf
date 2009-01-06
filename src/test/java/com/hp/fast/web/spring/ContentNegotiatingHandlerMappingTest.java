/* $Id: ContentNegotiatingHandlerMappingTest.java,v 1.1 2007/04/03 10:05:18 marcd Exp $ */

package com.hp.fast.web.spring;

import java.nio.charset.Charset;
import java.util.Collections;
import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletRequest;

import com.hp.fast.web.http.CharacterSetNegotiator;
import com.hp.fast.web.spring.ContentNegotiatingHandlerMapping;

public class ContentNegotiatingHandlerMappingTest extends TestCase
{
	private ContentNegotiatingHandlerMapping mapping;
	private MockHttpServletRequest request;
	
	public void setUp()
	{
		request = new MockHttpServletRequest();
		mapping = new ContentNegotiatingHandlerMapping();
	}
	
	
	public void testNoMatchingUrl() throws Exception
	{
		mapping.setUrlMap(Collections.singletonMap("/foo/bar/baz", new Object()));
		mapping.initApplicationContext();

		request.setRequestURI("/foo/bar");
		
		assertNull(mapping.getHandler(request));
	}

	
	public void testMatchingUrl() throws Exception
	{
		mapping.setUrlMap(Collections.singletonMap("/foo/bar", new Object()));
		mapping.initApplicationContext();
		
		request.setRequestURI("/foo/bar");
		
		assertNotNull(mapping.getHandler(request));
	}

	
	public void testNonMatchingNegotiation() throws Exception
	{
		mapping.setUrlMap(Collections.singletonMap("/foo/bar", new Object()));
		mapping.initApplicationContext();
		CharacterSetNegotiator negotiator = new CharacterSetNegotiator(new Object[] {Charset.forName("UTF8")});
		mapping.setNegotiators(new Object[] {negotiator});
		
		request.setRequestURI("/foo/bar");
		request.addHeader("Accept-Charset", "ISO-8859-1");
		
		assertNull(mapping.getHandler(request));
	}

	
	public void testMatchingNegotiation() throws Exception
	{
		mapping.setUrlMap(Collections.singletonMap("/foo/bar", new Object()));
		mapping.initApplicationContext();
		CharacterSetNegotiator negotiator = new CharacterSetNegotiator(new Object[] {Charset.forName("ISO-8859-1")});
		mapping.setNegotiators(new Object[] {negotiator});
		
		request.setRequestURI("/foo/bar");
		request.addHeader("Accept-Charset", "ISO-8859-1");
		
		assertNotNull(mapping.getHandler(request));
	}
}
