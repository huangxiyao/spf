package com.hp.spp.filters.access;

import junit.framework.TestCase;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.log4j.BasicConfigurator;
import com.hp.spp.profile.Constants;

public class UrlAccessFilterTest extends TestCase {

	static { BasicConfigurator.configure(); }

	private TestUrlAccessFilter mFilter;

	protected void setUp() throws Exception {
		mFilter = new TestUrlAccessFilter();
	}

	public void testExecuteWorkflowNoRuleFound() throws Exception {
		boolean continueWithFilterChain = mFilter.executeWorkflow(
				req(session(), "GET", "http://hp.com/some_other_non_defined_request", null),
				resp());
		assertTrue("Access is allowed when no rule matches", continueWithFilterChain);
	}

	public void testExecuteWorkflowAccessAllowed() throws Exception {
		boolean continueWithFilterChain = mFilter.executeWorkflow(
				req(session(), "GET", "http://hp.com/portal/site/sppqa/menuitem.12345/", null),
				resp());
		assertTrue("Access is allowed for a site page request", continueWithFilterChain);
	}

	public void testExecuteWorkflowAccessDenied() throws Exception {
		boolean continueWithFilterChain = mFilter.executeWorkflow(
				req(session(), "GET", "http://hp.com/portal/site/sppqa/template.MY_ACCOUNT", null),
				resp());
		assertFalse("Access is denied for template pages", continueWithFilterChain);
	}

	public void testExecuteWorkflowProtected() throws Exception {
		HttpSession session = session();

		boolean continueWithFilterChain = mFilter.executeWorkflow(
				req(session, "GET", "http://hp.com/portal/console/ctx_id.fd92f183f1c319fbc9d2cd8640108a0c/flg_clrctxst.1/", null),
				resp());
		assertFalse("Access is denied for protected pages", continueWithFilterChain);
		assertTrue("User redirected to login form", mFilter.called("showLoginForm"));

		// let's say user is logging in
		// let's say the first tentative fails
		mFilter.setSuccessfulLoginEmail(null);
		continueWithFilterChain = mFilter.executeWorkflow(
				req(session, "POST", "https://hp.com/portal/url_access_security_check", null),
				resp());
		assertFalse("Access is still denied as login failed", continueWithFilterChain);
		assertTrue("User redirected to login form", mFilter.called("showLoginForm"));

		// let's say that this 2nd tentative is successful
		mFilter.setSuccessfulLoginEmail("admin@hp.com");
		continueWithFilterChain = mFilter.executeWorkflow(
				req(session, "POST", "https://hp.com/portal/url_access_security_check", null),
				resp());
		assertFalse("User is redirected to the initial page therefore not continuing with this filter chain", continueWithFilterChain);
		assertTrue("Initial request performed", mFilter.called("performSavedRequest"));
	}

	public void testExecuteWorkflowProtectedWithAclAllowed() throws Exception {
		HttpSession session = session();

		boolean continueWithFilterChain = mFilter.executeWorkflow(
				req(session, "GET", "http://hp.com/portal/jsp/SPP/admin/log4jAdmin.jsp", null),
				resp());
		assertFalse("Access is denied as URL requires login", continueWithFilterChain);

		mFilter.setSuccessfulLoginEmail("admin@hp.com");
		continueWithFilterChain = mFilter.executeWorkflow(
				req(session, "POST", "http://hp.com/portal/url_access_security_check", null),
				resp());
		assertFalse("User is redirected to the initial page therefore not continuing with this filter chain", continueWithFilterChain);
		assertTrue("Initial request performed", mFilter.called("performSavedRequest"));
	}

	public void testExecuteWorkflowProtectedWithAclDenied() throws Exception {
		HttpSession session = session();

		boolean continueWithFilterChain = mFilter.executeWorkflow(
				req(session, "GET", "http://hp.com/portal/jsp/SPP/admin/log4jAdmin.jsp", null),
				resp());
		assertFalse("Access is denied as URL requires login", continueWithFilterChain);

		mFilter.setSuccessfulLoginEmail("user@hp.com");
		continueWithFilterChain = mFilter.executeWorkflow(
				req(session, "POST", "http://hp.com/portal/url_access_security_check", null),
				resp());
		assertFalse("User is redirected to the initial page therefore not continuing with this filter chain", continueWithFilterChain);
		assertTrue("Access denied page shown", mFilter.called("showAccessDenied"));
	}

	public void testExecuteWorkflowDefaultPage() throws Exception {
		mFilter.setSuccessfulLoginEmail("admin3@hp.com");
		boolean continueWithFilterChain = mFilter.executeWorkflow(
				req(session(), "POST", "http://hp.com/portal/url_access_security_check", null),
				resp());
		assertFalse("User is redirected to the default page therefore not continuing with the filter chain", continueWithFilterChain);
		assertTrue("Default page shown", mFilter.called("showDefaultPage"));
	}

	public void testPersistentSimulation() throws Exception {
		HttpSession session = session();
		Map userProfile = new HashMap();
		userProfile.put(Constants.MAP_IS_SIMULATING, "true");
		session.setAttribute(Constants.PROFILE_MAP, userProfile);

		boolean continueWithFilterChain = mFilter.executeWorkflow(
				req(session, "GET", "http://hp.com/portal/site/sppqa/", null),
				resp());
		assertFalse("Access is denied as user was simulating in the previous session", continueWithFilterChain);

		mFilter.setSuccessfulLoginEmail("user@hp.com");
		continueWithFilterChain = mFilter.executeWorkflow(
				req(session, "POST", "http://hp.com/portal/url_access_security_check", null),
				resp());
		assertFalse("User is redirected to the initial page therefore not continuing with this filter chain", continueWithFilterChain);
		assertTrue("Initial request performed", mFilter.called("performSavedRequest"));
	}

	private class TestUrlAccessRuleDAO implements UrlAccessRuleDAO {
		private List mRules;

		public TestUrlAccessRuleDAO() {
			mRules = new ArrayList();
			mRules.add(new SimulationLdapRule("/portal/site/*"));
			mRules.add(new LdapRule("*/template.STARTSIMULATION/*"));
			mRules.add(new LdapRule("/portal/console/*"));
			mRules.add(new LdapAclRule("/portal/jsp/SPP/admin/*", "admin@hp.com, admin2@hp.com | admin3@hp.com ; admin4@hp.com"));
			mRules.add(new DenyRule("*/template.*"));
			mRules.add(new AllowRule("/portal/site/*"));
		}

		public List findUrlAccessRules(String ruleSetName) {
			return findAllUrlAccessRules();
		}

		private List findAllUrlAccessRules() {
			return mRules;
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
	}

	private class TestUrlAccessFilter extends UrlAccessFilter {
		private String mSuccessfulLoginEmail;
		private List mActions = new ArrayList();

		public TestUrlAccessFilter() {
			setRuleDAO(new TestUrlAccessRuleDAO());

		}

		public boolean called(String methoName) {
			return mActions.indexOf(methoName) != -1;
		}

		public void setSuccessfulLoginEmail(String successfulLoginEmail) {
			mSuccessfulLoginEmail = successfulLoginEmail;
		}

		protected void showDefaultPage(HttpServletRequest request, HttpServletResponse response) {
			mActions.add("showDefaultPage");
		}

		protected void showAccessDenied(HttpServletRequest request, HttpServletResponse response) {
			mActions.add("showAccessDenied");
		}

		protected void performSavedRequest(HttpServletRequest request, HttpServletResponse response, RequestData data) {
			mActions.add("performSavedRequest");
		}

		protected boolean doLogin(HttpServletRequest request) {
			mActions.add("doLogin");
			if (mSuccessfulLoginEmail != null) {
				request.getSession().setAttribute(Constants.HP_LDAP_AUTH_EMAIL, mSuccessfulLoginEmail);
			}
			return mSuccessfulLoginEmail != null;
		}

		protected void showLoginForm(HttpServletRequest request, HttpServletResponse response, String errorCode) {
			mActions.add("showLoginForm");
		}
	}

}
