package com.hp.it.spf.portalurl.portal.filter;

import org.junit.Test;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.instanceOf;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Collections;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class PortalURLRewriterFilterTest
{
	@Test
	public void doFilterNoParams() throws Exception {
		HttpServletRequest request = TestHelper.createMockRequest(null);
		HttpServletResponse response = TestHelper.createMockResponse();
		TestChain chain = new TestChain();
		TestPortalURLRewriterFilter filter = new TestPortalURLRewriterFilter();

		filter.doFilter(request, response, chain);

		assertSame("Request not wrapped when no parameters present",
				request, chain.getRequest());
		assertThat("build map not called",
				filter.getPortletFriendlyIds(), nullValue());
	}

	@Test
	public void doFilterNoPortletParams() throws Exception {
		HttpServletRequest request = TestHelper.createMockRequest("a=1&b=2");
		HttpServletResponse response = TestHelper.createMockResponse();
		TestChain chain = new TestChain();
		TestPortalURLRewriterFilter filter = new TestPortalURLRewriterFilter();

		filter.doFilter(request, response, chain);

		assertSame("Request not wrapped when no portlet parameters present",
				request, chain.getRequest());
		assertThat("build map not called",
				filter.getPortletFriendlyIds(), nullValue());
	}

	@Test
	public void doFilterWithPortletParams() throws Exception {
		HttpServletRequest request = TestHelper.createMockRequest("a=1" +
				"&spf_p.tpst=p1" +
				"&spf_p.prp_p1=c%3D3" +
				"&javax.portlet.begCacheTok=com.vignette.cachetoken" +
				"&spf_p.pst=p2_ws_MX" +
				"&spf_p.pst=p3_pm_ED" +
				"&spf_p.pbp_p2=a%3D1" +
				"&spf_p.prp_p3=b%3D2" +
				"&javax.portlet.endCacheTok=com.vignette.cachetoken");
		HttpServletResponse response = TestHelper.createMockResponse();
		TestChain chain = new TestChain();
		TestPortalURLRewriterFilter filter = new TestPortalURLRewriterFilter();

		filter.doFilter(request, response, chain);

		assertThat("build ID to UID map called",
				filter.getPortletFriendlyIds(), notNullValue());
		assertThat("Created RequestWrapper",
				chain.getRequest(), instanceOf(RequestWrapper.class));

		Set<String> expectedIds = new HashSet<String>(Arrays.asList("p1", "p2", "p3"));
		assertThat("All portlet ID found", filter.getPortletFriendlyIds(), is(expectedIds));
	}

	@Test
	public void doFilterForResourceUrl() throws Exception {
		//normally only one portlet id is present but I want to test that it can be extracted from
		//two locations - tpst and rst_ parameters
		HttpServletRequest request = TestHelper.createMockRequest("a=1" +
				"&spf_p.tpst=p1_ws_BI" +
				"&spf_p.rst_p2=wsrp-url%3Dhttp%253A%252F%252F16.158.82.187%253A7002%252Fjsr168portlets%252FbinaryDocDisplay%26wsrp-requiresRewrite%3Dfalse");
		HttpServletResponse response = TestHelper.createMockResponse();
		TestChain chain = new TestChain();
		TestPortalURLRewriterFilter filter = new TestPortalURLRewriterFilter();

		filter.doFilter(request, response, chain);

		assertThat("build ID to UID map called",
				filter.getPortletFriendlyIds(), notNullValue());
		assertThat("Created RequestWrapper",
				chain.getRequest(), instanceOf(RequestWrapper.class));

		Set<String> expectedIds = new HashSet<String>(Arrays.asList("p1", "p2"));
		assertThat("All portlet ID found", filter.getPortletFriendlyIds(), is(expectedIds));
	}


	class TestPortalURLRewriterFilter extends PortalURLRewriterFilter {
		private Set<String> mPortletFriendlyIds;

		@Override
		Enumeration getParameterNamesByPrefix(HttpServletRequest request, String paramNamePrefix)
		{
			Set<String> result = new HashSet<String>();
			Enumeration<String> paramNames = (Enumeration<String>) request.getParameterNames();
			while (paramNames.hasMoreElements()) {
				String paramName = paramNames.nextElement();
				if (paramName.startsWith(paramNamePrefix)) {
					result.add(paramName);
				}
			}
			return Collections.enumeration(result);
		}

		@Override
		Enumeration getParameterValues(HttpServletRequest request, String parameterName)
		{

			String[] parameterValues = request.getParameterValues(parameterName);
			if (parameterValues == null) {
				return Collections.enumeration(Collections.emptySet());
			}
			return Collections.enumeration(Arrays.asList(parameterValues));
		}

		@Override
		Map<String, String> buildPortletFriendlyIdToUIDMap(Set<String> portletIds)
		{
			mPortletFriendlyIds = new HashSet<String>(portletIds);
			Map<String, String> result = new HashMap<String, String>();
			for (String portletId : portletIds) {
				result.put(portletId, portletId+"UID");
			}
			return result;
		}

		public Set<String> getPortletFriendlyIds()
		{
			return mPortletFriendlyIds;
		}
	}

	class TestChain implements FilterChain {

		private ServletRequest mRequest;

		public ServletRequest getRequest()
		{
			return mRequest;
		}

		public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException, ServletException
		{
			mRequest = servletRequest;
		}
	}
}
