package com.hp.spp.wsrp.context;

import junit.framework.TestCase;

import javax.portlet.PortletRequest;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;

import com.hp.spp.portlets.filter.context.UserContextFilter;

public class TestContextHelper extends TestCase {

	private Map mProfile;
	private Map mUserContextKeys;

	protected void setUp() throws Exception {
		mProfile = new HashMap();
		mProfile.put("email", "john@doe.com");

		mUserContextKeys = new HashMap();
		mUserContextKeys.put("PortalSite", "abc");
	}

	public void testGetUserProfileNotFound() {
		PortletRequest request = createMockRequest(new HashMap());
		assertNull("Null returned if cannot find profile", ContextHelper.getUserProfile(request));
	}

	public void testGetUserProfileWithDefaultAttribute() {
		Map attributes = new HashMap();
		attributes.put(UserContextExtractor.USER_PROFILE_KEY, mProfile);

		PortletRequest request = createMockRequest(attributes);
		assertNotNull("User profile map using default attribute", ContextHelper.getUserProfile(request));
		assertEquals("Value in map", "john@doe.com", ContextHelper.getUserProfile(request).get("email"));
	}

	public void testGetUserProfileWithCustomAttribute() {
		Map attributes = new HashMap();
		attributes.put("CustomName", mProfile);
		attributes.put(UserContextFilter.PROFILE_REF_ATTRIBUTE_KEY, "CustomName");
		PortletRequest request = createMockRequest(attributes);

		assertNotNull("User profile map using custom attribute", ContextHelper.getUserProfile(request));
		assertEquals("Value in map", "john@doe.com", ContextHelper.getUserProfile(request).get("email"));
	}

	public void testGetUserContextKeysNotFound() {
		PortletRequest request = createMockRequest(new HashMap());
		assertNull("Null returned if cannot find context keys", ContextHelper.getUserContextKeys(request));
	}

	public void testGetUserContextKeysWithDefaultAttribute() {
		Map attributes = new HashMap();
		attributes.put(UserContextExtractor.USER_CONTEXT_KEYS_KEY, mUserContextKeys);

		PortletRequest request = createMockRequest(attributes);
		assertNotNull("User context keys map using default attribute", ContextHelper.getUserContextKeys(request));
		assertEquals("Value in map", "abc", ContextHelper.getUserContextKeys(request).get("PortalSite"));
	}

	public void testGetUserContextKeysWithCustomAttribute() {
		Map attributes = new HashMap();
		attributes.put("CustomName", mUserContextKeys);
		attributes.put(UserContextFilter.USER_CONTEXT_KEYS_REF_ATTRIBUTE_KEY, "CustomName");
		PortletRequest request = createMockRequest(attributes);

		assertNotNull("User context keys map using custom attribute", ContextHelper.getUserContextKeys(request));
		assertEquals("Value in map", "abc", ContextHelper.getUserContextKeys(request).get("PortalSite"));
	}




	private PortletRequest createMockRequest(final Map attributes) {
		return (PortletRequest) Proxy.newProxyInstance(
				getClass().getClassLoader(),
				new Class[] {PortletRequest.class},
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if ("getAttribute".equals(method.getName())) {
							return attributes.get(args[0]);
						}
						throw new IllegalArgumentException("Don't know how to handle method " + method.getName());
					}
				});
	}
}
