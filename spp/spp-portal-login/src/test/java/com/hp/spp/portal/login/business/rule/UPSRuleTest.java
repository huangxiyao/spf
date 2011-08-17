package com.hp.spp.portal.login.business.rule;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hp.spp.profile.Constants;

import junit.framework.TestCase;



public class UPSRuleTest extends TestCase {
	
	
	
	public void testDisclaimerRuleFlagNo(){
		Map userProfileMap = new HashMap();
		userProfileMap.put(Constants.CONSENT_FLAG, "N");
		
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
	
		
		DisclaimerFormRule rule = new DisclaimerFormRule();
		assertEquals("Test case to check for disclaimer flag as 'N' ", rule.execute(request, userProfileMap, false), new Integer(Constants.SPP_DISCLAIMER_ERROR_CODE));
	}
	
	public void testDisclaimerRuleFlagYes(){
		Map userProfileMap = new HashMap();
		userProfileMap.put(Constants.CONSENT_FLAG, "Y");
	
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
	
		DisclaimerFormRule rule = new DisclaimerFormRule();
		assertNull("Test case to check for disclaimer flag as 'Y' ", rule.execute(request, userProfileMap, false));
	}
	
	public void testDisclaimerRuleFlagInvalid(){
		Map userProfileMap = new HashMap();
		userProfileMap.put(Constants.CONSENT_FLAG, "X");
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
	
		DisclaimerFormRule rule = new DisclaimerFormRule();
		assertNull("Test case to check for invalid disclaimer flag ", rule.execute(request, userProfileMap, false));
	
	}
	

	public void testDisclaimerRuleSimulated(){
		Map userProfileMap = new HashMap();
		userProfileMap.put(Constants.CONSENT_FLAG, "N");
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
	
		DisclaimerFormRule rule = new DisclaimerFormRule();
		assertEquals("Test case for Simulation ", rule.execute(request, userProfileMap, true), new Integer(Constants.SPP_DISCLAIMER_SIMULATION_ERROR_CODE));
	}
	
	public void testUPSPendingRuleNoSimulation(){
		Map userProfileMap = new HashMap();
		userProfileMap.put("Status", "2");
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
		UPSPendingUserRule rule = new UPSPendingUserRule();
		assertEquals("Pending User Rule Expecting 2 ",rule.execute(request, userProfileMap, false),new Integer(2));
	}
	
	public void testUPSPendingRuleSimulation(){
		Map userProfileMap = new HashMap();
		userProfileMap.put("Status", "2");
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
		UPSPendingUserRule rule = new UPSPendingUserRule();
		assertEquals("Pending User Rule Expecting 2 ",rule.execute(request, userProfileMap, true),new Integer(2));
	}
	
	public void testUPSPendingRuleForActive(){
		Map userProfileMap = new HashMap();
		userProfileMap.put("Status", "0");
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
		UPSPendingUserRule rule = new UPSPendingUserRule();
		assertEquals("Pending User Rule Expecting null ",rule.execute(request, userProfileMap, true),null);
	}
	
	public void testUPSInactiveRuleSimulation(){
		Map userProfileMap = new HashMap();
		userProfileMap.put("Status", "0");
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
		UPSInactiveUserRule rule = new UPSInactiveUserRule();
		assertEquals("Pending User Rule Expecting 3 ",rule.execute(request, userProfileMap, true),new Integer(3));
	}
	
	public void testUPSInactiveRuleNoSimulation(){
		Map userProfileMap = new HashMap();
		userProfileMap.put("Status", "0");
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
		UPSInactiveUserRule rule = new UPSInactiveUserRule();
		assertEquals("Pending User Rule Expecting 3 ",rule.execute(request, userProfileMap, false),new Integer(3));
	}
	
	public void testUPSInactiveRuleForActive(){
		Map userProfileMap = new HashMap();
		userProfileMap.put("Status", "1");
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
		UPSInactiveUserRule rule = new UPSInactiveUserRule();
		assertEquals("Pending User Rule Expecting null ",rule.execute(request, userProfileMap, false),null);
	}
			

	private HttpSession session() {
		return
			(HttpSession) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {HttpSession.class},
				new InvocationHandler() {
					Map mAttributes = new HashMap();
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if ("setAttribute".equals(method.getName())) {
							mAttributes.put(args[0], args[1]);
						}
						else if ("removeAttribute".equals(method.getName())) {
							mAttributes.remove(args[0]);
						}
						else if ("getAttribute".equals(method.getName())) {
							return mAttributes.get(args[0]);
						}
						else if ("toString".equals(method.getName())) {
							return "MockSession=" + mAttributes;
						}else if ("setAttribute".equals(method.getName())) {
							return null;
						}else if ("getAttribute".equals(method.getName())) {
							return null;
						}
						else {
							throw new UnsupportedOperationException("Unexpected call for this mock object: " + method.getName());
						}
						return null;
					}
				});
	}

	private HttpServletResponse resp() {
		return
			(HttpServletResponse) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {HttpServletResponse.class},
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if ("toString".equals(method.getName())) {
							return "MockResponse";
						}
						return method.invoke(proxy, args);
					}
				});
	}

	private HttpServletRequest req(final HttpSession session, final String methodName, final String url, final Map params) {
		return
			(HttpServletRequest) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {HttpServletRequest.class},
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if ("toString".equals(method.getName())) {
							return "MockRequest=[" + methodName + ", " + url + ", " + params + "]";
						}
						else if ("getSession".equals(method.getName())) {
							return session;
						}
						else if ("getMethod".equals(method.getName())) {
							return methodName;
						}
						else if ("getRequestURL".equals(method.getName())) {
							int pos = url.indexOf('?');
							return new StringBuffer(pos == -1 ? url : url.substring(0, pos));
						}
						else if ("getRequestURI".equals(method.getName())) {
							int pos = url.indexOf("://");
							int pos2 = url.indexOf('/', pos + "://".length());
							int pos3 = url.indexOf('?');
							return pos3 == -1 ? url.substring(pos2) : url.substring(pos2, pos3);
						}
						else if ("getQueryString".equals(method.getName())) {
							int pos = url.indexOf('?');
							return pos == -1 ? null : url.substring(pos+1);
						}
						else if ("getParameterMap".equals(method.getName())) {
							return params;
						}else if ("getParameter".equals(method.getName())) {
							return null;
						}else if ("setAttribute".equals(method.getName())) {
							return null;
						}
						else {
							throw new UnsupportedOperationException("Unsupported call for this mock object: " + method.getName());
						}
					}
				});
	}
}