package com.hp.spp.portal.simulate.bean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.portlet.PortletRequest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.hp.spp.portal.crypto.CryptoTools;
import com.hp.spp.portal.crypto.CryptoToolsImpl;
import com.hp.spp.profile.Constants;
import com.hp.spp.wsrp.context.UserContextExtractor;

public class SimulateBeanTest extends TestCase {

	private final static String PROFILEID = "6b8f94a9fdb9c17cf696ba3fa541e3e0" ;
	private final static String SESSIONTOKEN = "rjd30Yjwpt6nS9E2dlwArDNNuUr3J/30k4jXsgbcqjL55hae+nWfoeNtOGBft1CMvdBXvoMnBDCaVk/UaqKLTOSEMXCy9+wqGoMnZEwo5kIYclkkzlr83t4fuYgWiY0qJZL/pjmWJCH6KSgBfg/gJWM0JF907VRMn++JcczO4c9f2/5oaLN+fR0RjvYYTxFiQT4vMZDsshtSNheMxp9ihFJeBG/r29BmvxQeDzdXlzNMd1Agge0Ach3SVM0HDI/ysrP5V9ElJ6+clTXvHxoe0dFGzPbdegymRWbKEBID5OzDNDHrtJHYN6kxX2ErVGf7DY+XgwZuulbnfVXHvxgI2LAvrc8/PCzfg5HqsS8u3qINyYCWOhefKwCfW2QHqnxzFvWcCMf5Pn3YrwtD7g0KMG8CwY+x4npkxMjq9i2xUI5ZrYuxhxZT1FahZXRTkmXn8P6x7/Rn1G7SeT7c4JbVzPZGBz1TCu3IdDEz4bVFmRI9R5At8b7ZljJXK+6BK6bwbx88x+UcqYBL3f8OzuqUk7LGdmVAczQt+qokbSUkOmtpTI/3AIkLsycz8s6k34LFHQUZwbSnvd1zJH0c/+FIb8jpb9eHgVH5xi5sKs3VJ/8CCTAUx0ELFgM4hdLAi8wXoq+2djlMurxrKNbQpkLwm48UDdWnZPvi14URfiz7lP2aCBn15F/LS8mPxwArNrumG3/gxpfDpzUACK3h5ZnbyVHmfj3oUXRV3n16nZbAAbDMMZxH8m5JJsT0jt6eqoCrcPjP7fJ8IWB2D/KCpIplK9mSKh/E400hhGZtuxf2lKRcB/yk2VwmBDJMvqdl86c0wqyiJpBripHqquaTnwHj8aIztBnxEKN4ej5muqlxMGtyIG/55P5fxTvSt2CQrZvMVHMm6XqMMo+wnklwgBW4ZQ==" ;

	private SimulateBean bean = null ;
		

	@Override
	public void setUp() throws Exception {
		String profileId = "6b8f94a9fdb9c17cf696ba3fa541e3e0" ;
		String sessionToken = SESSIONTOKEN ;
		String redirectURL = "Success" ;
		String errorURL = "Error" ;

		Map userProfile = new HashMap() ;
		userProfile.put("PortalSessionId", "FBtQfNMY1BbqBy5Z1ZvkBlCnyPKVBr26792KcThtGgZ3nGNphzqD!-1828447346!1161932048764") ;
		userProfile.put(Constants.MAP_SITE, "sppportal") ;
		userProfile.put("HPPUserId", PROFILEID) ;
		userProfile.put("HomePageUrl", "http://sppdev.gre.hp.com:80/portal/site/sppportal/") ;
		userProfile.put(Constants.MAP_SITE, "sppportal") ;
		
		Map userContextKeys = new HashMap() ;
		userContextKeys.put("HttpsPort", "443") ;
		
		Map attributes = new HashMap() ;
		attributes.put(UserContextExtractor.USER_PROFILE_KEY, userProfile) ;
		attributes.put(UserContextExtractor.USER_CONTEXT_KEYS_KEY, userContextKeys) ;
		
		PortletRequest request = createMockRequest(attributes) ;
		
		bean = new SimulateBean();
		bean.setSimulatedProfileHPPId(profileId);
		bean.setPortletRequest(request);
		bean.setSessionId(sessionToken);
		bean.setRedirectURLName(redirectURL);
		bean.setErrorURLName(errorURL);
		assertNotNull("SimulateBean is null!", bean);
		
		profileId = null ;
		userProfile = null ;
		sessionToken = null ;
		redirectURL = null ;
		errorURL = null ;
	}

	public void testGetActionURL() throws Exception {
		String action = null ;
		action = bean.getInternalActionURL() ;
		assertNotNull("ActionURL is null!", action);
	}

	public void testGetFullURLWithParameters() throws Exception {
		String url = null ;
		url = bean.getFullURLWithParameters() ;
		assertNotNull("URLWithParameters is null!", url);
	}

	public void testGetParamMap() throws Exception {
		Map paramMap = null;
		paramMap = bean.getParamMap();
		assertNotNull("ParamMap is null!", paramMap);
		
		Iterator iterator = paramMap.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			assertNotNull("Key is null!", key);
			String value = (String) paramMap.get(key);
			assertNotNull("Value is null!", value);
		}
	}

	public void testEncryptDecrypt() throws Exception {
		Map paramMap = null;
		paramMap = bean.getParamMap();
		assertNotNull("ParamMap is null!", paramMap);
		
		String value = (String) paramMap.get(ISimulateBean.SPP_SimulationKey);
		
		String SECRETKEY = "Pmjq3PcyPb+UyB8CE/2/eT5o6tz3Mj2/" ;
		CryptoTools cryptoTools = new CryptoToolsImpl(SECRETKEY) ;
		String decrypt = cryptoTools.decrypt(value) ;
		
		StringTokenizer st = new StringTokenizer (decrypt, AbstractSimulateBean.ENCRYPTIONSEPARATOR, false);
		
		ArrayList list = new ArrayList();
		
		// add all line tokens into a vector
		while (st.hasMoreTokens()) { 
			String token = st.nextToken();
			
			// remove bad spaces
			token = token.trim();
			
			// remove bad tab
			//token = removeBadTab(token);
			
			if ("null".equals(token))
				token = null;
			
			list.add(token) ;
		}
		
		assertTrue("Bad number! Decryption not return 2 elements", list.size() == 2) ;
		value = (String) list.get(0) ;
		assertTrue("HPP Id is not the same!", value.equals(PROFILEID)) ;
		value = (String) list.get(1) ;
		assertTrue("Sessiontoken is not the same!", value.equals(SESSIONTOKEN)) ;
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
