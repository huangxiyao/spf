package com.hp.it.spf.wsrp.sticky;

import java.util.HashMap;
import java.util.Map;

import org.apache.axis.MessageContext;

import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;

/**
 * Helper class holding a map of remote portlet application session cookie and the corresponding
 * VIP used to get the cookie.
 * This class is used as a temporary store for the VIP between the time initCookie and
 * the following markup method is called. The reason this is needed is because initCookie method
 * does not allow to retrieve the request and then session to store the DNS cache map.
 * 
 * @see StickyHandler
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 */
class CookieHolder {
	private static final LogWrapper LOG = new LogWrapper(CookieHolder.class);
	
	/**
	 * Maps <tt>cookie|host</tt> to VIP corresponding to the host and which was used to get
	 * the cookie.
	 */
	private Map<String, String> mCookies;

	CookieHolder()
	{
		this(new HashMap<String, String>());
	}

	/**
	 * Constructor used for testing purposes.
	 */
	CookieHolder(Map<String, String> cookies)
	{
		mCookies = cookies;
	}

	/**
	 * Removes the cookie for the target host.
	 */
	void removeCookie(MessageContext messageContext, String targetHost) {
		String cookie = getCookie(messageContext);
		if (cookie != null) {
			String cookieKey = getCookieKey(cookie, targetHost);
			if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
				LOG.debug("Removing cookie entry: " + cookieKey);
			}
			mCookies.remove(cookieKey);
		}
	}


	/**
	 * Retrieves the VIP corresponding to the host.
	 * 
	 * @param messageContext web service call message context
	 * @param targetHost web service request host
	 * @return VIP associated with the host and the session cookie for the session between 
	 * portal and portlet server.
	 */
	String getVipForCookie(MessageContext messageContext, String targetHost) {
		String cookie = getCookie(messageContext);
		if (cookie != null) {
			String cookieKey = getCookieKey(cookie, targetHost);
			String targetVIP = mCookies.get(cookieKey);
			if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
				LOG.debug("Getting target VIP for cookie: " + cookieKey + " => " + targetVIP);
			}
			return targetVIP;
		}
		return null;
	}


	/**
	 * Saves entry which maps cookie|host to the targetVIP.
	 * 
	 * @param messageContext web service call message context
	 * @param targetHost web service request host
	 * @param targetVIP VIP corresponding to the targetHost
	 */
	void saveCookie(MessageContext messageContext, String targetHost, String targetVIP) {
		String cookie = getCookie(messageContext);
		if (cookie != null) {
			String cookieKey = getCookieKey(cookie, targetHost);
			mCookies.put(cookieKey, targetVIP);
			if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
				LOG.debug("Cookie entry saved: " + cookieKey + " => " + targetVIP);
			}
		}
	}


	private String getCookieKey(String cookie, String targetHost) {
		return cookie + "|" + targetHost;
	}


	/**
	 * Retrieves the session cookie which was either set by the portlet server or is being
	 * sent by the portal. This method assumes that the cookie is named <tt>JSESSIONID</tt>. 
	 * 
	 * @param messageContext web service call message context
	 * @return JSESIONID cookie in the form of "JSESSIONID=cookievalue"
	 */
	private String getCookie(MessageContext messageContext) {
		final String PREFIX = "JSESSIONID=";
		Object cookieObj = messageContext.getProperty("Cookie");
		if (cookieObj instanceof String && ((String) cookieObj).startsWith(PREFIX)) {
			return (String) cookieObj;
		}
		else if (cookieObj instanceof String[]) {
			for (String cookieStr : (String[]) cookieObj) {
				if (cookieStr != null && cookieStr.startsWith(PREFIX)) {
					return cookieStr;
				}
			}
		}
		return null;
	}

}
