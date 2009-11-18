package com.hp.it.spf.xa.interpolate.portlet;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.portlet.PortletSession;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.portlet.MockPortletResponse;

/**
 * TestClass TokenParserTest at this point of time tests the results of 2 newly added tokens <code>{EXIST:<i>key</i>}</code>
 * and <code>{VALUE:<i>key</i>}</code>.
 * 
 * @author pranav
 */
public class TokenParserTest
{

	private TokenParser mTokenParser;
	private MockPortletRequest mPortletRequest;
	private MockPortletResponse mPortletResponse;

	/**
	 * This method sets up the initial request parameters and attributes, which are required for the test.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception
	{
	    mPortletRequest = new MockPortletRequest();
	    mPortletResponse = new MockPortletResponse();

	    mPortletRequest.setAttribute("RequestAttribute", "RequestAttributeValue");
	    mPortletRequest.setParameter("RequestParameter", "RequestParameterValue");
	    mPortletRequest.setParameter("PublicParameter", "PublicParameterValue");
	    mPortletRequest.getPortletSession().setAttribute("SSNAttribPortlet", "SSNAttribPortletValue" ,PortletSession.PORTLET_SCOPE);
	    mPortletRequest.getPortletSession().setAttribute("SSNAttribApplication", "SSNAttribApplicationValue" , PortletSession.APPLICATION_SCOPE);

	    mTokenParser = new TokenParser(mPortletRequest, mPortletResponse);

	}
	
	/**
	 * This method ensures that the request property names retrieved by the test case matches the expected results.
	 */
	@Test
	public void testGetRequestPropertyNames()
	{
	    List<String> expectedPropertyNames = new ArrayList<String>();

	    expectedPropertyNames.add("RequestAttribute");
	    expectedPropertyNames.add("RequestParameter");
	    expectedPropertyNames.add("PublicParameter");
	    expectedPropertyNames.add("SSNAttribPortlet");
	    expectedPropertyNames.add("SSNAttribApplication");

		Collection c = Collections.list((Enumeration<String>) mTokenParser.getRequestPropertyNames());
		Boolean expectedPropertyFound = true;
		for (Iterator<String> it = expectedPropertyNames.iterator(); it.hasNext();) {
			String value = it.next();
			if (!c.contains(value)) {
				expectedPropertyFound = false;
			}
		}

		assertEquals("Expected Property Names not found in getRequestPropertyNames() ", true, expectedPropertyFound);

	}

	/**
	 * This test method would test getRequestPropertyValue(String name) method for value's precedence order.
	 * The values should be returned in the following precedence order.
	 * 	<br>request attribute <br>request parameter <br>public render parameter 
	 * 	<br>portlet-scoped session attribute <br>application-scoped session attribute.
	 * 
	 */
	@Test
	public void testGetRequestPropertyValuePrecedenceScope()
	{
	    mPortletRequest.getPortletSession().setAttribute("ScopeCheck", "ScopeCheckSSNAttribApplicationValue" , PortletSession.APPLICATION_SCOPE);
	    mTokenParser = new TokenParser(mPortletRequest, mPortletResponse);
	    assertEquals("ScopeCheckFailed for application scope attribute : ", 
		    "ScopeCheckSSNAttribApplicationValue" , mTokenParser.getRequestPropertyValue("ScopeCheck"));

	    mPortletRequest.getPortletSession().setAttribute("ScopeCheck", "ScopeCheckSSNAttribPortletValue" ,PortletSession.PORTLET_SCOPE);
	    mTokenParser = new TokenParser(mPortletRequest, mPortletResponse);
	    assertEquals("ScopeCheckFailed for portlet scope attribute : ", 
		    "ScopeCheckSSNAttribPortletValue" , mTokenParser.getRequestPropertyValue("ScopeCheck"));

	    mPortletRequest.setParameter("ScopeCheck", "ScopeCheckPublicParameterValue");
	    mTokenParser = new TokenParser(mPortletRequest, mPortletResponse);
	    assertEquals("ScopeCheckFailed for application scope attribute : ", 
		    "ScopeCheckPublicParameterValue" , mTokenParser.getRequestPropertyValue("ScopeCheck"));

	    mPortletRequest.setParameter("ScopeCheck", "ScopeCheckRequestParameterValue");
	    mTokenParser = new TokenParser(mPortletRequest, mPortletResponse);
	    assertEquals("ScopeCheckFailed for application scope attribute : ", 
		    "ScopeCheckRequestParameterValue" , mTokenParser.getRequestPropertyValue("ScopeCheck"));

	    mPortletRequest.setAttribute("ScopeCheck", "ScopeCheckRequestAttributeValue");
	    mTokenParser = new TokenParser(mPortletRequest, mPortletResponse);
	    assertEquals("ScopeCheckFailed for application scope attribute : ", 
		    "ScopeCheckRequestAttributeValue" , mTokenParser.getRequestPropertyValue("ScopeCheck"));

	}
	
	/**
	 * This test method would test that only <code>String / String[]</code> type values are returned.
	 * in-case the attribute contains values like <code>{"value1","value2","value3"}</code> 
	 * value returned would be <code>"value1"</code>
	 *
	 */
	@Test
	public void testGetRequestPropertyValueSessionAttribute(){

	    mPortletRequest.setAttribute("SessionAttrValCheck", new Integer(5));
	    mTokenParser = new TokenParser(mPortletRequest, mPortletResponse);
	    assertNull("The expected value should be null ", mTokenParser.getRequestPropertyValue("SessionAttrValCheck"));

	    mPortletRequest.setAttribute("SessionAttrValCheck", new String[]{"value1","value2","value3"});
	    mTokenParser = new TokenParser(mPortletRequest, mPortletResponse);
	    assertEquals("Only the fisrt value in the array should be returned ", 
		    "value1", mTokenParser.getRequestPropertyValue("SessionAttrValCheck"));

	    mPortletRequest.setAttribute("SessionAttrValCheck", "StringValue");
	    mTokenParser = new TokenParser(mPortletRequest, mPortletResponse);
	    assertEquals("Only the fisrt value in the array should be returned ", 
		    "StringValue", mTokenParser.getRequestPropertyValue("SessionAttrValCheck"));	    

	}
	
	/**
	 * This test method is writen to ensure that null values are returned, 
	 * in-case the property value is not found in request. 
	 */
	@Test
	public void testGetRequestPropertyValueNull(){

	    assertNull("The requested property value is not present in the request [ attributes / parameters ] ", 
		    mTokenParser.getRequestPropertyValue("NullCheck"));
	}

}
