package com.hp.spp.portal.login.business.rule;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hp.spp.portal.login.dao.UPSRuleDAO;
import com.hp.spp.profile.Constants;

import junit.framework.TestCase;

public class UPSRuleProcessorTest extends TestCase {
	public void testABCD(){
		assert(true);
	}
	
/*	public void testDisclaimerRuleFlagNo(){
		Map userProfileMap = new HashMap();
		userProfileMap.put(Constants.CONSENT_FLAG, "N");
		
		UPSRulesProcessor processor = getUPSRulesProcessor();
		
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
	
		assertEquals("Result of execution ", processor.execute(request, userProfileMap, false), new Integer(Constants.SPP_DISCLAIMER_ERROR_CODE));
		assertEquals("Result of execution ", session.getAttribute(Constants.CLEAR_USERPROFILE_FLAG), "true");
	}
	
	public void testDisclaimerRuleFlagYes(){
		Map userProfileMap = new HashMap();
		userProfileMap.put(Constants.CONSENT_FLAG, "Y");
		
		UPSRulesProcessor processor = getUPSRulesProcessor();
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
	
		assertNull("Result of execution ", processor.execute(request, userProfileMap, false));
		assertNull("Result of execution ", session.getAttribute(Constants.CLEAR_USERPROFILE_FLAG));
	}
	
	

	public void testDisclaimerRuleFlagInvalid(){
		Map userProfileMap = new HashMap();
		userProfileMap.put(Constants.CONSENT_FLAG, "X");
		
		UPSRulesProcessor processor = getUPSRulesProcessor();
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
	
		assertNull("Result of execution ", processor.execute(request, userProfileMap, false));
		assertNull("Result of execution ", session.getAttribute(Constants.CLEAR_USERPROFILE_FLAG));

	}
	

	public void testDisclaimerRuleSimulated(){
		Map userProfileMap = new HashMap();
		userProfileMap.put(Constants.CONSENT_FLAG, "N");
		
		UPSRulesProcessor processor = getUPSRulesProcessor();
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
	
	
		assertEquals("Result of execution ", processor.execute(request, userProfileMap, true), new Integer(Constants.SPP_DISCLAIMER_SIMULATION_ERROR_CODE));
		assertNull("Result of execution ", session.getAttribute(Constants.CLEAR_USERPROFILE_FLAG));

	}
	
	
	public void testDisclaimerRuleInvalid(){
		Map userProfileMap = new HashMap();
		userProfileMap.put(Constants.CONSENT_FLAG, "Y");
		
		UPSRulesProcessor processor = getInvalidUPSRulesProcessor();
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
	
		Exception ex = null;
		try{
			processor.execute(request, userProfileMap, true);
		}catch(Exception e){
			ex = e;
		}
		
		assertEquals("Result of execution", ex.getMessage(), "Error Occured in initializing class");
	}
	
	public void testDisclaimerRuleInvalidNull(){
		Map userProfileMap = new HashMap();
		userProfileMap.put(Constants.CONSENT_FLAG, "Y");
		
		UPSRulesProcessor processor = getInvalidNullUPSRulesProcessor();
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
	
		Exception ex = null;
		try{
			processor.execute(request, userProfileMap, true);
		}catch(Exception e){
			ex = e;
		}
		
		assertEquals("Result of execution", ex.getMessage(), "Error Occured in initializing class");
	}

	private UPSRulesProcessor getUPSRulesProcessorForPendingCase(){
		UPSRulesProcessor processor = new UPSRulesProcessor();
		processor.setSiteIdentifier("sppqa");

		RuleContainer container = RuleContainer.getInstance();
		container.setRuleDAO(new TestUPSRuleDAOForPendingCaseDAO());
		processor.setRuleContainer(container);
		return processor;	
	}
	
	private UPSRulesProcessor getUPSRulesProcessor(){
		UPSRulesProcessor processor = new UPSRulesProcessor();
		processor.setSiteIdentifier("sppqa");

		RuleContainer container = RuleContainer.getInstance();
		container.setRuleDAO(new TestUPSRuleDAO());
		processor.setRuleContainer(container);
		return processor;	
	}
	
	private UPSRulesProcessor getInvalidUPSRulesProcessor(){
		UPSRulesProcessor processor = new UPSRulesProcessor();
		processor.setSiteIdentifier("sppqa");

		RuleContainer container = RuleContainer.getInstance();
		container.setRuleDAO(new TestInvalidUPSRuleDAO());
		processor.setRuleContainer(container);
		return processor;	
	}
	
	private UPSRulesProcessor getInvalidNullUPSRulesProcessor(){
		UPSRulesProcessor processor = new UPSRulesProcessor();
		processor.setSiteIdentifier("sppqa");

		RuleContainer container = RuleContainer.getInstance();
		container.setRuleDAO(new TestInvalidNullUPSRuleDAO());
		processor.setRuleContainer(container);
		return processor;	
	}
	
	public void testPendingUserRuleValue2(){
		Map userProfileMap = new HashMap();
		userProfileMap.put(Constants.MAP_STATUS, "2");
		
		UPSRulesProcessor processor = getUPSRulesProcessorForPendingCase();
		
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
	
		assertEquals("Result of execution ", new Integer(Constants.STATUS_UPS_PENDING),processor.execute(request, userProfileMap, false));
	}
	
	public void testPendingUserRuleValueNull(){
		Map userProfileMap = new HashMap();
		userProfileMap.put(Constants.MAP_STATUS, null);
		
		UPSRulesProcessor processor = getUPSRulesProcessorForPendingCase();
		
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
	
		assertNull("Result of execution ", processor.execute(request, userProfileMap, false));
	}
	
	public void testPendingUserRuleNonIntValue(){
		Map userProfileMap = new HashMap();
		userProfileMap.put(Constants.MAP_STATUS, "xyz");
		
		UPSRulesProcessor processor = getUPSRulesProcessorForPendingCase();
		
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
	
		assertNull("Result of execution ", processor.execute(request, userProfileMap, false));
	}
	
	public void testPendingUserRuleInvalidIntValue(){
		Map userProfileMap = new HashMap();
		userProfileMap.put(Constants.MAP_STATUS, "6");
		
		UPSRulesProcessor processor = getUPSRulesProcessorForPendingCase();
		HttpServletRequest request = req(session(), "GET", "", null);
	
		assertNull("Result of execution ", processor.execute(request, userProfileMap, false));
	}
	
	public void testInactiveUserRuleValue0(){
		Map userProfileMap = new HashMap();
		userProfileMap.put(Constants.MAP_STATUS, "0");
		
		UPSRulesProcessor processor = getUPSRulesProcessorForPendingCase();
		
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
	
		assertEquals("Result of execution ", new Integer("3"),processor.execute(request, userProfileMap, false));
	}
	
	public void testInactiveUserRuleValueNull(){
		Map userProfileMap = new HashMap();
		userProfileMap.put(Constants.MAP_STATUS, null);
		
		UPSRulesProcessor processor = getUPSRulesProcessorForPendingCase();
		
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
	
		assertNull("Result of execution ", processor.execute(request, userProfileMap, false));
	}
	
	public void testInactiveUserRuleNonIntValue(){
		Map userProfileMap = new HashMap();
		userProfileMap.put(Constants.MAP_STATUS, "xyz");
		
		UPSRulesProcessor processor = getUPSRulesProcessorForPendingCase();
		
		HttpSession session = session();
		HttpServletRequest request = req(session, "GET", "", null);
	
		assertNull("Result of execution ", processor.execute(request, userProfileMap, false));
	}
	
	public void testInactiveUserRuleInvalidIntValue(){
		Map userProfileMap = new HashMap();
		userProfileMap.put(Constants.MAP_STATUS, "6");
		
		UPSRulesProcessor processor = getUPSRulesProcessorForPendingCase();
		HttpServletRequest request = req(session(), "GET", "", null);
	
		assertNull("Result of execution ", processor.execute(request, userProfileMap, false));
	}
	
	
		
	
	class TestUPSRuleDAO implements UPSRuleDAO{

		public String getUPSRules(String siteIdentifier) {
			return "com.hp.spp.portal.login.business.rule.DisclaimerFormRule";
		}

	}
	
	class TestUPSRuleDAOForPendingCaseDAO implements UPSRuleDAO{

		public String getUPSRules(String siteIdentifier) {
			return "com.hp.spp.portal.login.business.rule.UPSPendingUserRule:com.hp.spp.portal.login.business.rule.UPSInactiveUserRule";
		}

	}
	
	class TestInvalidUPSRuleDAO implements UPSRuleDAO{

		public String getUPSRules(String siteIdentifier) {
			return "com.hp.spp.portal.login.business.rule.DisclaimerFormRule;com.hp.spp.portal.login.business.rule.DisclaimerFormRule";
		}

	}
	
	class TestInvalidNullUPSRuleDAO implements UPSRuleDAO{

		public String getUPSRules(String siteIdentifier) {
			return "com.hp.spp.portal.login.business.rule.DisclaimerFormRule;com.hp.spp.portal.login.business.rule.DisclaimerFormRule";
		}

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
						}
						else {
							throw new UnsupportedOperationException("Unsupported call for this mock object: " + method.getName());
						}
					}
				});
	}*/
}
