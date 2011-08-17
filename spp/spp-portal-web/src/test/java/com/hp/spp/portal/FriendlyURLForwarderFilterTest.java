package com.hp.spp.portal;

import junit.framework.TestCase;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.Arrays;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class FriendlyURLForwarderFilterTest extends TestCase {

	private HttpServletRequest mNullRequest;

	protected void setUp() throws Exception {
		mNullRequest =
				(HttpServletRequest) Proxy.newProxyInstance(
						getClass().getClassLoader(),
						new Class[] {HttpServletRequest.class},
						new InvocationHandler() {
							public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
								return null;
							}
						});
	}


	public void testReplacePortletIdsInQueryString() throws Exception {
		FriendlyURLForwarderFilter.RequestWrapper request;

		request = new FriendlyURLForwarderFilter.RequestWrapper(mNullRequest, new HashMap());
		assertEquals("Empty map, no portlet params", "a=b", request.replacePortletIds("a=b"));
		assertEquals("Empty map, 1 portlet params",
				"a=b&javax.portlet.tpst=abc&javax.portlet.prp_abc_FirstName=John",
				request.replacePortletIds("a=b&javax.portlet.tpst=abc&javax.portlet.prp_abc_FirstName=John")
				);

		Map idMap = new HashMap();
		idMap.put("YYY", "123");
		idMap.put("ZZZ", "456");
		request = new FriendlyURLForwarderFilter.RequestWrapper(mNullRequest, idMap);
		assertEquals("No portlet params", request.replacePortletIds("a=b"), "a=b");
		assertEquals("1 portlet, 2 params",
				"a=b&javax.portlet.tpst=456&javax.portlet.prp_456_p1=v1&javax.portlet.prp_456_p2=v2",
				request.replacePortletIds("a=b&javax.portlet.tpst=ZZZ&javax.portlet.prp_ZZZ_p1=v1&javax.portlet.prp_ZZZ_p2=v2")
				);
		assertEquals("2 portlets, 1 param each",
				"a=b&javax.portlet.tpst=456&javax.portlet.prp_456_p1=v1&javax.portlet.tpst=123&javax.portlet.prp_123_p2=v2",
				request.replacePortletIds("a=b&javax.portlet.tpst=ZZZ&javax.portlet.prp_ZZZ_p1=v1&javax.portlet.tpst=YYY&javax.portlet.prp_YYY_p2=v2")
				);
	}

	public void testReplacePortletIdsInParamMap() throws Exception {
		FriendlyURLForwarderFilter.RequestWrapper request;

		request = new FriendlyURLForwarderFilter.RequestWrapper(mNullRequest, new HashMap());
		assertTrue("Empty map, no portlet params", equals(
				asMap(new String[] {"a", "b"}),
				request.replacePortletIds(asMap(new String[] {"a", "b"}))
		));
		assertTrue("Empty map, 1 portlet params", equals(
				asMap(new String[] {"a", "b", "javax.portlet.tpst", "abc", "javax.portlet.prp_abc_FirstName", "John"}),
				request.replacePortletIds(asMap(new String[] {"a", "b", "javax.portlet.tpst", "abc", "javax.portlet.prp_abc_FirstName", "John"}))
		));

		Map idMap = new HashMap();
		idMap.put("YYY", "123");
		idMap.put("ZZZ", "456");
		request = new FriendlyURLForwarderFilter.RequestWrapper(mNullRequest, idMap);
		assertTrue("No portlet params", equals(
				asMap(new String[] {"a", "b"}),
				request.replacePortletIds(asMap(new String[] {"a", "b"}))
		));
		Map expected = asMap(new String[] {"a", "b", "javax.portlet.tpst", "456", "javax.portlet.prp_456_p1", "v1", "javax.portlet.prp_456_p2", "v2"});
		Map actual = request.replacePortletIds(
						asMap(new String[] {"a", "b", "javax.portlet.tpst", "ZZZ", "javax.portlet.prp_ZZZ_p1", "v1", "javax.portlet.prp_ZZZ_p2", "v2"})); 
		assertTrue("1 portlet, 2 params", equals(expected, actual));
		expected = asMap(new String[] {"a", "b", "javax.portlet.tpst", "456", "javax.portlet.prp_456_p1", "v1", "javax.portlet.tpst", "123", "javax.portlet.prp_123_p2", "v2"});
		actual = request.replacePortletIds(
						asMap(new String[] {"a", "b", "javax.portlet.tpst", "ZZZ", "javax.portlet.prp_ZZZ_p1", "v1", "javax.portlet.tpst", "YYY", "javax.portlet.prp_YYY_p2", "v2"}));
		assertTrue("2 portlets, 1 param each", equals(expected, actual));
	}

	private Map asMap(String[] data) {
		Map result = new HashMap();
		for (int i = 0; i < data.length; i+=2) {
			if (result.containsKey(data[i])) {
				String[] value = (String[]) result.get(data[i]);
				String[] newValue = new String[value.length+1];
				System.arraycopy(value, 0, newValue, 0, value.length);
				newValue[value.length] = data[i+1];
				result.put(data[i], newValue);
			}
			else {
				result.put(data[i], new String[] {data[i+1]});
			}
		}
		return result;
	}

	private boolean equals(Map paramMap1, Map paramMap2) {
		if (!paramMap1.keySet().equals(paramMap2.keySet())) {
			return false;
		}
		for (Iterator it = paramMap1.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry entry = (Map.Entry) it.next();
			if (!Arrays.equals((String[]) entry.getValue(), (String[]) paramMap2.get(entry.getKey()))) {
				return false;
			}
		}
		return true;
	}


}
