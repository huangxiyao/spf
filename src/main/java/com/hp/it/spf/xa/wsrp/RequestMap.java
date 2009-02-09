/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.xa.wsrp;

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

/**
 * Singleton map used to pass request information between {@link RequestBindingFilter} and {@link UserContextKeysInjector}.
 * @author Oliver, Kaijian Ding, Ye Liu
 * @version TBD
 */
public class RequestMap {
	/**
	 * Singleton instance.
	 */
	private static RequestMap mInstance = new RequestMap();

	/**
	 * Maps {@link String} request keys to {@link HttpServletRequest} objects.
	 */
	private Map mRequestMap = Collections.synchronizedMap(new HashMap());

	/**
	 * Private constructor to prevent creation of this class objects outside of this class.
	 */
	private RequestMap() {}

	/**
	 * @return singleton instance of the map
	 */
	public static RequestMap getInstance() {
		return mInstance;
	}

	/**
	 * Adds request to the map and associates it with the key returned by the method.
	 * @param request request to add
	 * @return request key that can be used later to retrieve the request from this map
	 */
	public String add(HttpServletRequest request) {
	   String key = generateKey(request);
	   mRequestMap.put(key, request);
	   return key;
	}

	/**
	 * Removes the request with the given <code>requestKey</code> from the map.
	 * @param requestKey key of the request to remove
	 */
	public void remove(String requestKey) {
	   mRequestMap.remove(requestKey);
	}

	/**
	 * @param key key of the request to retrieve
	 * @return request bound to the given key or <code>null</code> if the key doesn't represent any
	 * request in this map.
	 */
	public HttpServletRequest get(String key) {
	   return (HttpServletRequest) mRequestMap.get(key);
	}

	/**
	 * @param request request for which to generate a key
	 * @return key based on the hash code of the request 
	 */
	private String generateKey(HttpServletRequest request) {
	   return String.valueOf(request.hashCode());
	}
}
