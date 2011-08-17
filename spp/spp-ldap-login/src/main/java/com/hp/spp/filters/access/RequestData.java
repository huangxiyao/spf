package com.hp.spp.filters.access;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

public class RequestData implements Serializable {
	private boolean mGet;
	private Map mParameterMap;
	private String mRequestUrl;
	private String mRequestUri;

	public RequestData(HttpServletRequest request) {
		if ("GET".equalsIgnoreCase(request.getMethod())) {
			mGet = true;
		}
		else if ("POST".equalsIgnoreCase(request.getMethod())) {
			mGet = false;
		}
		else {
			throw new IllegalArgumentException("Don't know how to handle HTTP method: " + request.getMethod());
		}

		StringBuffer requestURL = request.getRequestURL();
		if (isGet() && request.getQueryString() != null) {
			requestURL.append('?').append(request.getQueryString());
		}

		mRequestUrl = requestURL.toString();
		mParameterMap = isGet() ? null : new HashMap(request.getParameterMap());
		mRequestUri = request.getRequestURI();
	}

	public boolean isGet() {
		return mGet;
	}

	public String getRequestUrl() {
		return mRequestUrl;
	}

	public Map getParameterMap() {
		return mParameterMap;
	}

	public String getRequestUri() {
		return mRequestUri;
	}

	public String toString() {
		return new StringBuffer("RequestData={").append(isGet() ? "GET " : "POST ").
				append(mRequestUrl).append(", params=").append(mParameterMap).append('}').
				toString();
	}

}
