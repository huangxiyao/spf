package com.hp.it.spf.xa.portletdata.portal;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.Mockery;
import org.jmock.Expectations;
import org.jmock.lib.legacy.ClassImposteriser;

import javax.servlet.http.HttpServletRequest;

import com.vignette.portal.website.enduser.PortalContext;
import com.vignette.portal.website.enduser.PortalRequest;
import com.vignette.portal.website.enduser.components.ActionException;
import com.epicentric.page.PageException;
import com.hp.it.spf.xa.misc.Consts;

import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
@RunWith(JMock.class)
public class PortletDataPreDisplayActionTest
{
	private final Mockery mContext = new JUnit4Mockery() {{
		setImposteriser(ClassImposteriser.INSTANCE);
	}};

	@Test
	public void testExecute() throws Exception {
		final Map<String, String> contextMap = asMap("key1", "value1", "key2", "value2");
		final Map userProfile = asMap("FirstName", "Jane", "LastName", "Smith");
		final Set<String> pagePortletUids = new HashSet<String>(Arrays.asList("portlet1uid", "portlet2uid"));
		final String diagnosticId = "diagnostic_id";
		final HttpServletRequest request = mContext.mock(HttpServletRequest.class);
		final PortalContext portalContext = mContext.mock(PortalContext.class);
		final PortalRequest portalRequest = mContext.mock(PortalRequest.class);
		final PortletDataCollector dataCollector = mContext.mock(PortletDataCollector.class);

		PortletDataPreDisplayAction action = new TestPortletDataPreDisplayAction(dataCollector, pagePortletUids);

		mContext.checking(new Expectations() {{
			allowing(portalContext).getPortalRequest(); will(returnValue(portalRequest));
			allowing(portalRequest).getRequest(); will(returnValue(request));
			allowing(dataCollector).retrieveUserProfile(request); will(returnValue(userProfile));
			allowing(dataCollector).retrieveUserContextKeys(request); will(returnValue(contextMap));
			allowing(request).getAttribute(Consts.DIAGNOSTIC_ID); will(returnValue(diagnosticId));

			oneOf(request).setAttribute("com.vignette.portal.attribute.portlet.portlet1uid_name.javax.portlet.userinfo", userProfile);
			oneOf(request).setAttribute("com.vignette.portal.attribute.portlet.portlet1uid_name.ContextMap", contextMap);
			oneOf(request).setAttribute("com.vignette.portal.attribute.portlet.portlet1uid_name.SPF_DC_ID", diagnosticId);
			oneOf(request).setAttribute("com.vignette.portal.attribute.portlet.portlet2uid_name.javax.portlet.userinfo", userProfile);
			oneOf(request).setAttribute("com.vignette.portal.attribute.portlet.portlet2uid_name.ContextMap", contextMap);
			oneOf(request).setAttribute("com.vignette.portal.attribute.portlet.portlet2uid_name.SPF_DC_ID", diagnosticId);
		}});

		action.execute(portalContext);
	}

	private static <T> Map<T, T> asMap(T... args) {
		Map<T, T> result = new TreeMap<T, T>();
		for(int i = 0; i < args.length; i+=2) {
			result.put(args[i], args[i+1]);
		}
		return result;
	}


	private class TestPortletDataPreDisplayAction extends PortletDataPreDisplayAction {
		private Set<String> mPagePortletUids;

		public TestPortletDataPreDisplayAction(PortletDataCollector dataCollector, Set<String> pagePortletUids)
		{
			super(dataCollector);
			mPagePortletUids = pagePortletUids;
		}

		@Override
		Set<String> collectCurrentPagePortletUIDs(PortalContext portalContext) throws PageException
		{
			return mPagePortletUids;
		}

		@Override
		Set<String> convertToPortletNames(Set<String> portletUIDs) throws ActionException
		{
			Set<String> result = new HashSet<String>();
			for (String portletUID : portletUIDs) {
				result.add(portletUID + "_name");
			}
			return result;
		}
	}
}
