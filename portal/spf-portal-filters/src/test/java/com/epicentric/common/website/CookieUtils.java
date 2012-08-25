package com.epicentric.common.website;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * This is a surrogate class used by JUnit test to instead of
 * Vignette's <tt>CookieUtils</tt>
 * </p>
 * <p>
 * In the source code need to be test by JUnit, there are many static
 * methods invoked driectly from the classes of the third party projects,
 * such as Vigentte.
 * </p>
 * <p>
 * To avoid invoking that static methods mentioned above, custom classes will be added to
 * instand of that refered classes in the tested code at runtime.
 * </p>
 *
 * @author <link href="ye.liu@hp.com">Ye Liu</link>
 * @version 1.0
 */
public class CookieUtils {
	public static Cookie getCookie(HttpServletRequest request, String name) {
		if (request == null) {
			throw new IllegalArgumentException("CookieUtils.getCookie: request was null!");
		}

		if (name == null) {
			throw new IllegalArgumentException("CookieUtils.getCookie: cookie name was null!");
		}

		Cookie[] cookies = request.getCookies();

		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(name)) {
					return cookies[i];
				}
			}
		}

		return null;
	}

	public static String getCookieValue(HttpServletRequest request, String name) {
		Cookie cookie = getCookie(request, name);
		return cookie != null ? cookie.getValue() : null;
	}
}
