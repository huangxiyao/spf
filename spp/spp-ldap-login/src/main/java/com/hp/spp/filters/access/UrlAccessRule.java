package com.hp.spp.filters.access;

import javax.servlet.http.HttpSession;
import java.util.regex.Pattern;

public abstract class UrlAccessRule {
	private Pattern mPattern;

	public UrlAccessRule(String urlPattern) {
		if (urlPattern.toLowerCase().startsWith("re:")) {
			mPattern = Pattern.compile(urlPattern.substring("re:".length()));
		}
		else {
			mPattern = Pattern.compile(urlPattern.replaceAll("\\*", ".*"));
		}
	}

	public boolean matches(String requestURI) {
		return mPattern.matcher(requestURI).matches();
	}

	public abstract boolean isAccessAllowed(HttpSession session);

	public abstract boolean isLoginRequired();

	public String toString() {
		return
			new StringBuffer(getClass().getName()).
					append("={pattern=").append(mPattern.pattern()).
					append(",loginRequired=").append(isLoginRequired()).
					append('}').toString();
	}
}
