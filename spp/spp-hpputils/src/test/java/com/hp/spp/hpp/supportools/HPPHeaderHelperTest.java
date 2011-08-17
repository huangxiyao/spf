package com.hp.spp.hpp.supportools;

import com.hp.spp.hpp.supporttools.HPPHeaderHelper;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

public class HPPHeaderHelperTest extends TestCase {

	public void testSMSessionCookieWithValueLOGGEDOFF(){
		Cookie[] cookies = new Cookie[1];
		cookies[0] = new Cookie("SMSESSION","LOGGEDOFF");
		HttpServletRequest request = req(cookies,"sppuser",null);
		
		assertEquals("User is Logged Off: ",true,HPPHeaderHelper.isSMSessionCookieLoggedOff(request));
	}

	public void testSMSessionCookieABSENT() {
		HttpServletRequest request = req(null,null,null);
		
		assertEquals("SMSESSION cookies not present: ",true,HPPHeaderHelper.isSMSessionCookieLoggedOff(request));
	}
	
	public void testDecodeWithSMSESSIONNotLoggedOffAndHeaderNotEncoded() {
		Cookie[] cookies = new Cookie[1];
		cookies[0] = new Cookie("SMSESSION","aditya");
		HttpServletRequest request = req(cookies,"sppuser",null);
		
		assertEquals("Unencoded SMUSER value: ","sppuser",HPPHeaderHelper.getHPPId(request));
	}
	
	public void testDecodeWithSMSESSIONNotLoggedOffAndHeaderEncoded() {
		Cookie[] cookies = new Cookie[1];
		cookies[0] = new Cookie("SMSESSION","aditya");
		HttpServletRequest request = req(cookies,"?UTF-8?B?c3BwdXNlcg==",null);
		
		assertEquals("Unencoded SMUSER value: ","sppuser",HPPHeaderHelper.getHPPId(request));
	}
	
	public void testDecodeWithSMSESSIONLoggedOffAndHeaderEncoded() {
		Cookie[] cookies = new Cookie[1];
		cookies[0] = new Cookie("SMSESSION","LOGGEDOFF");
		HttpServletRequest request = req(cookies,"?UTF-8?B?c3BwdXNlcg==",null);
		
		assertEquals("Unencoded SMUSER value: ",null,HPPHeaderHelper.getHPPId(request));
	}
	
	public void testDecodeWithSMSESSIONNotLoggedOffAndHeaderTakenFromRequest() {
		Cookie[] cookies = new Cookie[1];
		cookies[0] = new Cookie("SMSESSION","yello");
		HttpServletRequest request = req(cookies,"?UTF-8?B?c3BwdXNlcg==","abcd");
		
		assertEquals("Unencoded SMUSER value: ","abcd",HPPHeaderHelper.getHPPId(request));
	}
	
	
	
	private HttpServletRequest req(final Cookie[] cookies, final String header, final String headerFromRequest) {
		return
			(HttpServletRequest) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {HttpServletRequest.class},
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						if("getHeader".equals(method.getName())){
							return header;
						}else if("getCookies".equals(method.getName())) {
							return cookies;
						}else if("getAttribute".equals(method.getName())){
							return headerFromRequest;
						}else if("setAttribute".equals(method.getName())){
							return null;
						}else if("getRequestURI".equals(method.getName())){
							return "";
						}else if("getContextPath".equals(method.getName())){
							return "portal";
						}
						else {
							throw new UnsupportedOperationException("Unsupported call for this mock object: " + method.getName());
						}
					}
				});
	}
}


