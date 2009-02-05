package com.hp.it.spf.wsrp.openportal.producer.markup.impl;

import junit.framework.TestCase;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.sun.portal.wsrp.common.stubs.v2.PortletContext;
import com.sun.portal.wsrp.producer.ProducerException;
import com.sun.portal.portletcontainer.common.PortletContainerConstants;

/**
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
public class CloneBeforeWriteHandlerImplTest extends TestCase {

	public void testInitForProducerOfferedPortlet() {
		Map<String, Object> requestAttributes = new HashMap<String, Object>();
		HttpServletRequest request = createMockRequest(requestAttributes);
		PortletContext portletContext = new PortletContext();
		portletContext.setPortletHandle("webapp.portlet");

		CloneBeforeWriteHandlerImpl handler = new CloneBeforeWriteHandlerImpl();
		handler.init(null, request, null, portletContext, null);

		assertNotNull("Staging map", requestAttributes.get(PortletContainerConstants.PREFERENCE_STAGING_MAP));
	}

	public void testInitForConsumerConfiguredPortlet() {
		Map<String, Object> requestAttributes = new HashMap<String, Object>();
		HttpServletRequest request = createMockRequest(requestAttributes);
		PortletContext portletContext = new PortletContext();
		portletContext.setPortletHandle("__WSRP_CLONED__webapp.portlet_123_456");

		CloneBeforeWriteHandlerImpl handler = new CloneBeforeWriteHandlerImpl();
		handler.init(null, request, null, portletContext, null);

		assertNull("Stating map", requestAttributes.get(PortletContainerConstants.PREFERENCE_STAGING_MAP));
	}

	public void testHandleNotCloningIfItShouldNot() throws Exception {
		Map<String, Object> requestAttributes = new HashMap<String, Object>();
		HttpServletRequest request = createMockRequest(requestAttributes);
		PortletContext portletContext = new PortletContext();
		portletContext.setPortletHandle("__WSRP_CLONED__webapp.portlet_123_456");

		final Set<String> methodsCalled = new HashSet<String>(2);
		CloneBeforeWriteHandlerImpl handler = new CloneBeforeWriteHandlerImpl() {
			@Override
			protected PortletContext clonePortlet() throws ProducerException {
				methodsCalled.add("clonePortlet");
				return new PortletContext();
			}

			@Override
			protected void setPortletPreferences(PortletContext portletContext, Map<String, String> portletPreferences) throws ProducerException {
				methodsCalled.add("setPortletPreferences");
			}
		};
		handler.init(null, request, null, portletContext, null);
		handler.handle();

		assertTrue("clonePortlet not invoked if portlet should not be cloned",
				!methodsCalled.contains("clonePortlet"));
		assertTrue("setPortletPreferences not invoked if portlet should not be cloned",
				!methodsCalled.contains("setPortletPreferences"));
	}

	public void testHandleCloningIfItShould() throws Exception {
		Map<String, Object> requestAttributes = new HashMap<String, Object>();
		HttpServletRequest request = createMockRequest(requestAttributes);
		PortletContext portletContext = new PortletContext();
		portletContext.setPortletHandle("webapp.portlet_123_456");

		final Set<String> methodsCalled = new HashSet<String>(2);
		CloneBeforeWriteHandlerImpl handler = new CloneBeforeWriteHandlerImpl() {
			@Override
			protected PortletContext clonePortlet() throws ProducerException {
				methodsCalled.add("clonePortlet");
				return new PortletContext();
			}

			@Override
			protected void setPortletPreferences(PortletContext portletContext, Map<String, String> portletPreferences) throws ProducerException {
				methodsCalled.add("setPortletPreferences");
			}
		};
		handler.init(null, request, null, portletContext, null);
		handler.handle();

		assertTrue("clonePortlet not invoked if portlet should not be cloned",
				!methodsCalled.contains("clonePortlet"));
		assertTrue("setPortletPreferences not invoked if portlet should not be cloned",
				!methodsCalled.contains("setPortletPreferences"));
	}

	private HttpServletRequest createMockRequest(final Map<String, Object> requestAttributes) {
		return (HttpServletRequest) Proxy.newProxyInstance(
				getClass().getClassLoader(),
				new Class[] { HttpServletRequest.class },
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if ("setAttribute".equals(method.getName())) {
							requestAttributes.put((String) args[0], args[1]);
						}
						return null;
					}
				});
	}


}
