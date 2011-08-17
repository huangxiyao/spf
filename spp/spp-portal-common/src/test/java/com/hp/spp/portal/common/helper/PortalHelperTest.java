package com.hp.spp.portal.common.helper;

import junit.framework.TestCase;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.Arrays;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class PortalHelperTest extends TestCase
{

	private final PortalHelper mHelper = new PortalHelper();

	public void testGetSafeRequestURLShouldReturnUnchangedURLWhenNoIllegalCharactersUsed() {
		assertEquals("http://a/b/c?d=e&f=g",
				mHelper.getSafeRequestURL(createRequest("http://a/b/c",
						new HashMap<String, String[]>() {{
							put("d", arr("e"));
							put("f", arr("g"));
						}})));
		assertEquals("http://a:100/b/c?d=e&f=g",
				mHelper.getSafeRequestURL(createRequest("http://a:100/b/c",
						new HashMap<String, String[]>() {{
							put("d", arr("e"));
							put("f", arr("g"));
						}})));
		assertEquals("https://a", mHelper.getSafeRequestURL(createRequest("https://a", null)));
		assertEquals("https://a/", mHelper.getSafeRequestURL(createRequest("https://a/", null)));
	}

	public void testGetSafeRequestURLShouldEncodeParamNamesAndValuesWhenIllegalCharactersUsed() {
		HttpServletRequest request = createRequest("http://localhost:9001/portal/site/publicsppdev/menuitem.76277839d2b744005220128c837f0101/",
				new HashMap<String, String[]>() {{
					put("user", arr());
					put("error", arr("'\"><![CDATA[<script>alert('SPIXSS');</script>]]><![CDATA[<sCrIpT>alert(12367)</sCrIpT>]]>"));}});
		String safeUrl = mHelper.getSafeRequestURL(request);

		for (Character ch : Arrays.asList('<', '>', '[', ']', '\'', '!', ';')) {
			assertTrue(ch + " escaped", safeUrl.indexOf(ch) == -1);
		}
	}

	private String[] arr(String... args) {
		return args;
	}

	private HttpServletRequest createRequest(final String requestURL, final Map<String, String[]> parameterMap)
	{
		if (requestURL.indexOf('?') != -1) {
			throw new IllegalArgumentException("requestURL must not contain parameters");
		}
		return (HttpServletRequest) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {HttpServletRequest.class},
				new InvocationHandler()
				{
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
					{
						if ("getRequestURL".equals(method.getName())) {
							return new StringBuffer(requestURL);
						}
						if ("getParameterMap".equals(method.getName())) {
							return (parameterMap == null ? Collections.emptyMap() : parameterMap);
						}
						throw new IllegalStateException("Method not defined for this test implementation: " + method.getName());
					}
				});
	}
}
