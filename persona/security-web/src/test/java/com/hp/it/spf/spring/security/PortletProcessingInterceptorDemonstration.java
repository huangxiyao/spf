package com.hp.it.spf.spring.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.mock.web.portlet.MockActionRequest;
import org.springframework.mock.web.portlet.MockActionResponse;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.ui.portlet.PortletProcessingInterceptor;

import com.hp.it.spf.xa.misc.Consts;

public class PortletProcessingInterceptorDemonstration {

	private static PortletProcessingInterceptor getPortletProcessingInterceptor() {
		boolean mockSecurity		= false;
		
		// The context configurations
		String directory			= "src/test/resources/";
		String applicationContext	= directory + "applicationContext.xml";
		String securityContext		= directory + "applicationContext-security.xml";
		String mockSecurityContext	= directory + "applicationContext-mockSecurity.xml";
		
		// Simulate contextConfigLocation in web.xml
		Collection<String> contexts = new ArrayList<String>();
		contexts.add(applicationContext);
		contexts.add(securityContext);
		if (mockSecurity) {
			contexts.add(mockSecurityContext);
		}
		
		FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext(contexts.toArray(new String[contexts.size()]));
		
		return (PortletProcessingInterceptor) context.getBean("portletProcessingInterceptor");
	}
	
	public static void main(String[] args) throws Exception {
		
		Logger logger = Logger.getRootLogger();
		logger.setLevel(Level.ALL);
		logger.addAppender(new ConsoleAppender());
		
		// The interceptor
		PortletProcessingInterceptor interceptor = getPortletProcessingInterceptor();
		
		// Mock userInfo
		Map<String, Object> userInfo = new HashMap<String, Object>();
		userInfo.put(Consts.KEY_PROFILE_ID, "j.doe@hp.com");
		
		List<String> groups = new ArrayList<String>();
		groups.add("ADMIN-PERSONA");
		userInfo.put(Consts.KEY_USER_GROUPS, groups);
		
		// Mock request
		MockActionRequest request = new MockActionRequest();
		request.setAttribute(PortletRequest.USER_INFO, userInfo);
		
		// Simulate a request going to the interceptor
		interceptor.preHandleAction(request, new MockActionResponse(), new Object());
		
		// Verify that the security context has been established
		System.out.println(SecurityContextHolder.getContext().getAuthentication());		
	}
}
