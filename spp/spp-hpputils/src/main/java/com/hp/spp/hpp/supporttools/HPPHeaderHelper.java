package com.hp.spp.hpp.supporttools;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.hp.spp.profile.Constants;

public class HPPHeaderHelper {

    private static String getAuthenticatedUserHeaderValue(HttpServletRequest request, String headerName) {
		if (isSMSessionCookieLoggedOff(request) || isPublicPage(request)) {
			return null;
		}
		
		String attributeName = HPPHeaderHelper.class + "." + headerName;
		String headerValue = (String) request.getAttribute(attributeName);
		if (headerValue == null) {
			headerValue = Decoder.decode(request.getHeader(headerName));
			if (headerValue != null) {
				request.setAttribute(attributeName, headerValue);
			}
		}
		return headerValue;
	}

	/**
	 * This method returns the value of HPP SM_USERDN header if the user is authenticated.
	 * Before it returned the value of SM_USER header but its value is equal to the username
	 * the user used to sign in, which can be either the actual user ID or email address. SM_USERDN
	 * header contains always the user ID, regardless of what's been used to sign in.
	 * @param request incoming user request
	 * @return value of SM_USERDN header or <code>null</code> if the value is not present or the user
	 * is not authenticated or accesses a public site
	 */
	public static String getSMUser(HttpServletRequest request){
		return getAuthenticatedUserHeaderValue(request, Constants.SM_USERDN);
	}
	
	public static String getHPPId(HttpServletRequest request){
		return getAuthenticatedUserHeaderValue(request, Constants.SM_UNIVERSALID);
	}
	
	public static boolean isSMSessionCookieLoggedOff(HttpServletRequest request){
		Cookie[] cookieTab = request.getCookies();
		
		if (cookieTab == null){
			return true;
		}

		for (int i = 0; i < cookieTab.length; i++) {
			Cookie cookie = cookieTab[i];			
			if (cookie != null && (Constants.SMSESSION).equalsIgnoreCase(cookie.getName()) && (Constants.LOGGEDOFF).equalsIgnoreCase(cookie.getValue())){
				return true;
			}
		}
		return false;
	}
	
	public static boolean isPublicPage(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
	    return (requestURI.indexOf("/site/public") != -1 
	    || requestURI.indexOf("/site/console") != -1 || requestURI.indexOf("template.PRELOGIN") != -1);
	}
}
