package com.hp.it.spf.portalurl.portal.filter;

import org.junit.Ignore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import java.util.Arrays;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
@Ignore("Helper class, not a test class")
class TestHelper
{
	static HttpServletRequest createMockRequest(final String queryString) {

		final Map<String, String[]> paramMap = new HashMap<String, String[]>();
		if (queryString != null) {
			paramMap.putAll(convertQueryString(queryString));
		}

		return (HttpServletRequest) Proxy.newProxyInstance(TestHelper.class.getClassLoader(),
				new Class[] { HttpServletRequest.class },
				new InvocationHandler()
				{
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
					{
						if ("getQueryString".equals(method.getName())) {
							return queryString;
						}
						if ("getParameterMap".equals(method.getName())) {
							return paramMap;
						}
						if ("getParameterNames".equals(method.getName())) {
							return Collections.enumeration(paramMap.keySet());
						}
						if ("getParameter".equals(method.getName())) {
							String[] value = paramMap.get(args[0]);
							return (value == null || value.length == 0 ? null : value[0]);
						}
						if ("getParameterValues".equals(method.getName())) {
							return paramMap.get(args[0]);
						}

						throw new IllegalStateException("Unepxected call to " + method.getName());
					}
				});
	}

	static HttpServletResponse createMockResponse() {
		return (HttpServletResponse) Proxy.newProxyInstance(TestHelper.class.getClassLoader(),
				new Class[] { HttpServletResponse.class },
				new InvocationHandler()
				{
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
					{
						throw new IllegalStateException("Unexpected call to " + method.getName());
					}
				});
	}

	static Map<String, String[]> convertQueryString(String queryString)
	{
		Map<String, String[]> paramMap = new HashMap<String, String[]>();
		for (String param : queryString.split("&")) {
			String[] paramItems = param.split("=");
			String paramName = paramItems[0];
			String paramValue = paramItems[1];

			String[] currentValue = paramMap.get(paramName);
			if (currentValue == null) {
				paramMap.put(paramName, new String[] {paramValue});
			}
			else {
				String[] newValue = new String[currentValue.length+1];
				System.arraycopy(currentValue, 0, newValue, 0, currentValue.length);
				newValue[currentValue.length] = paramValue;
				paramMap.put(paramName, newValue);
			}
		}
		return paramMap;
	}


	@SuppressWarnings("unchecked")
	static <T> T[] array(T... args) {
		return (T[]) Arrays.asList(args).toArray();
	}

}
