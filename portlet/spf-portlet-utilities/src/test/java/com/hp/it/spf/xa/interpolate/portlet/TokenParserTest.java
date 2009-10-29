package com.hp.it.spf.xa.interpolate.portlet;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.portlet.MockPortletRequest;
import org.springframework.mock.web.portlet.MockPortletResponse;

public class TokenParserTest
{

	private TokenParser mTokenParser;
	private MockPortletRequest mPortletRequest;
	private MockPortletResponse mPortletResponse;

	@Before
	public void setUp() throws Exception
	{

		mPortletRequest = new MockPortletRequest();
		mPortletResponse = new MockPortletResponse();

		mPortletRequest.setAttribute("attri1", "attri1Value");
		mPortletRequest.setAttribute("attri2", "attri2Value");
		mPortletRequest.setAttribute("attri3", "attri4Value");

		mPortletRequest.setParameter("param1", "param1Value");
		mPortletRequest.setParameter("param2", "param2Value");
		mPortletRequest.setParameter("param3", "param4Value");

		mTokenParser = new TokenParser(mPortletRequest, mPortletResponse);

	}

	@Test
	public void testGetRequestPropertyNames()
	{

		Collection c = Collections.list(mTokenParser.getRequestPropertyNames());
		assertEquals("Test1 : Number of elements returned is not as expected", 6, c.size());

		mPortletRequest.setAttribute("attri4", new String[]{"arrayvalue1", "arrayvalue2", "arrayvalue3"});
		mTokenParser = new TokenParser(mPortletRequest, mPortletResponse);
		c = Collections.list(mTokenParser.getRequestPropertyNames());
		assertEquals("Test2 : Only String objects should be counted , element mismatch error ", 7, c.size());

	}

	@Test
	public void testGetRequestPropertyValue()
	{

		mPortletRequest.setAttribute("param1", "param1AttributeValue");
		mTokenParser = new TokenParser(mPortletRequest, mPortletResponse);
		assertEquals("Test5 : Assertion failed for attibute takes precedence over parameterValue ",
				"param1AttributeValue", mTokenParser.getRequestPropertyValue("param1"));

		mPortletRequest.getPortletSession().setAttribute("portletScopeAttrib", "portletScopeAttribValue");
		mTokenParser = new TokenParser(mPortletRequest, mPortletResponse);
		assertEquals("Test6 : Attribute Value not detected ",
				"portletScopeAttribValue", mTokenParser.getRequestPropertyValue("portletScopeAttrib"));

		mPortletRequest.setAttribute("ScopeCheck", "SSNValue");
		mPortletRequest.getPortletSession().setAttribute("ScopeCheck", "PortletScopeValue");
		mPortletRequest.getPortletSession().setAttribute("ScopeCheck", "ApplicationScopeValue");

		mTokenParser = new TokenParser(mPortletRequest, mPortletResponse);
		assertEquals("Test7 : Value returned in improper precedence order ",
				"SSNValue", mTokenParser.getRequestPropertyValue("ScopeCheck"));
		/*
	 mPortletRequest.removeAttribute("ScopeCheck");
	 mTokenParser = new TokenParser(mPortletRequest, mPortletResponse);
	 assertEquals("Test8 : Value returned in improper precedence order ",
		 "PortletScopeValue", mTokenParser.getRequestPropertyValue("ScopeCheck"));

	 mPortletRequest.getPortletSession().removeAttribute("ScopeCheck");
	 mTokenParser = new TokenParser(mPortletRequest, mPortletResponse);
	 assertEquals("Test9 : Value returned in improper precedence order ",
		 "ApplicationScopeValue", mTokenParser.getRequestPropertyValue("ScopeCheck"));
	 */

	}

}
