package com.hp.it.spf.portalurl.portal.filter;

import org.junit.Test;
import static org.junit.matchers.JUnitMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsNot.not;

import java.util.Map;
import java.util.HashMap;


/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class RequestWrapperTest
{
	private static final Map<String, String> PORTLET_FRIENDLY_ID_MAP =
			new HashMap<String, String>() {{
				put("p1", "uid1");
				put("p2", "uid2");
				put("p3", "uid3");
			}};

	@Test
	public void testReplacePortletIdsInQueryString() {
		String queryString = "a=1&b=2";
		RequestWrapper wrapper = createWrapper(queryString);
		assertThat("Nothing replaced when no portlet frienldy ids present",
				wrapper.replacePortletIds(queryString), is(queryString));

		assertThat("All values replaced",
				wrapper.replacePortletIds(
						"a=1&spf_p.tpst=p1&" +
								"spf_p.pst=p2&" +
								"spf_p.prp_p3=x%3D1&" +
								"spf_p.pbp_p1=y%3D2%26z%3D3&" +
								"b=2&" +
								"spf_p.prp_p2_a=1&spf_p.prp_p2_b=2&" +
								"spf_p.pbp_p3_z=3&" +
								"c=3"
				),
				is("a=1&javax.portlet.tpst=uid1&" +
						"javax.portlet.pst=uid2&" +
						"javax.portlet.prp_uid3=x%3D1&" +
						"javax.portlet.pbp_uid1=y%3D2%26z%3D3&" +
						"b=2&" +
						"javax.portlet.prp_uid2_a=1&javax.portlet.prp_uid2_b=2&" +
						"javax.portlet.pbp_uid3_z=3&" +
						"c=3"));
	}

	@Test
	public void testGetQueryString() {
		RequestWrapper wrapper = createWrapper(null);
		assertThat("Null returned when no parameters", wrapper.getQueryString(), nullValue());

		wrapper = createWrapper("a=1&b=2");
		assertThat("Nothing modified if no prefixed parameters",
				wrapper.getQueryString(), is("a=1&b=2"));

		wrapper = createWrapper("a=1&spf_p.tpst=p1&spf_p.prp_p1=c%3D3&javax.portlet.begCacheTok=com.vignette.cachetoken&spf_p.prp_p2=a%3D1&spf_p.prp_p3=b%3D2&javax.portlet.endCacheTok=com.vignette.cachetoken");
		assertThat("Friendly IDs replaced",
				wrapper.getQueryString(), is(
						"a=1&javax.portlet.tpst=uid1&javax.portlet.prp_uid1=c%3D3&javax.portlet.begCacheTok=com.vignette.cachetoken&javax.portlet.prp_uid2=a%3D1&javax.portlet.prp_uid3=b%3D2&javax.portlet.endCacheTok=com.vignette.cachetoken"
				));
	}

	@Test
	public void testReplacePortletIdsInParamMap() {
		String queryString = "a=1&spf_p.tpst=p1&spf_p.prp_p1=c%3D3&javax.portlet.begCacheTok=com.vignette.cachetoken&spf_p.pst=p2_ws_MX&spf_p.pst=p3_pm_ED&spf_p.prp_p2=a%3D1&spf_p.prp_p3=b%3D2&javax.portlet.endCacheTok=com.vignette.cachetoken";
		RequestWrapper wrapper = createWrapper(queryString);
		Map<String, String[]> paramMap = TestHelper.convertQueryString(queryString);

		Map<String, String[]> resultParamMap = wrapper.replacePortletIds(paramMap);
		for (Map.Entry<String, String[]> param : resultParamMap.entrySet()) {
			assertThat("spf_p prefixed parameters removed", param.getKey(), not(containsString("spf_p.")));
		}
		assertArrayEquals("a param", TestHelper.array("1"), paramMap.get("a"));
		assertArrayEquals("javax.portlet.tpst param",
				TestHelper.array("uid1"), resultParamMap.get("javax.portlet.tpst"));
		assertArrayEquals("javax.portlet.prp_uid1 param",
				TestHelper.array("c%3D3"), resultParamMap.get("javax.portlet.prp_uid1"));
		assertArrayEquals("javax.portlet.begCacheTok param",
				TestHelper.array("com.vignette.cachetoken"), resultParamMap.get("javax.portlet.begCacheTok"));
		assertArrayEquals("javax.portlet.pst param",
				TestHelper.array("uid2_ws_MX", "uid3_pm_ED"), resultParamMap.get("javax.portlet.pst"));
		assertArrayEquals("javax.portlet.prp_uid2 param",
				TestHelper.array("a%3D1"), resultParamMap.get("javax.portlet.prp_uid2"));
		assertArrayEquals("javax.portlet.prp_uid2 param",
				TestHelper.array("a%3D1"), resultParamMap.get("javax.portlet.prp_uid2"));
		assertArrayEquals("javax.portlet.prp_uid3 param",
				TestHelper.array("b%3D2"), resultParamMap.get("javax.portlet.prp_uid3"));
		assertArrayEquals("javax.portlet.endCacheTok param",
				TestHelper.array("com.vignette.cachetoken"), resultParamMap.get("javax.portlet.endCacheTok"));
	}


	private RequestWrapper createWrapper(String queryString) {
		return new RequestWrapper(TestHelper.createMockRequest(queryString), PORTLET_FRIENDLY_ID_MAP);
	}



}
