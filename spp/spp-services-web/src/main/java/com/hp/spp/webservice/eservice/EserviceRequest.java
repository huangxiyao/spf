/*
 * Copyright 2001-2004 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hp.spp.webservice.eservice;

import java.util.Map;

/**
 * This is a JavaBean which represents an order for some products.
 * 
 * @author Glen Daniels (gdaniels@apache.org)
 */
public class EserviceRequest {
	
	/**
	 * what site is calling the service.
	 */
	private String mSiteName;

	/**
	 * Which Eservice is requested.
	 */
	private String mEServiceName;

	
	/**
	 * map that contains user profile.
	 */
	private Map mUserContext;
	
	/**
	 * map that contains request parameters to owerwrite
	 */
	private Map mHttpRequestParameters;
	
	/**
	 * Url to owerwrite the urlProd 
	 */
	private String urlProdFromRequest;
	
	/**
	 * Url to owerwrite the urlTes 
	 */
	private String urlTestFromRequest;
		
	

//********************	
//Getters and Setters
//*********************	
	
	public String getUrlProdFromRequest() {
		return urlProdFromRequest;
	}


	public void setUrlProdFromRequest(String urlProdFromRequest) {
		this.urlProdFromRequest = urlProdFromRequest;
	}


	public Map getHttpRequestParameters() {
		return mHttpRequestParameters;
	}


	public void setHttpRequestParameters(Map httpRequestParameters) {
		mHttpRequestParameters = httpRequestParameters;
	}


	public String getEServiceName() {
		return mEServiceName;
	}


	public void setEServiceName(String serviceName) {
		mEServiceName = serviceName;
	}


	public String getSiteName() {
		return mSiteName;
	}


	public void setSiteName(String siteName) {
		mSiteName = siteName;
	}


	public Map getUserContext() {
		return mUserContext;
	}


	public void setUserContext(Map userContext) {
		mUserContext = userContext;
	}


	public String getUrlTestFromRequest() {
		return urlTestFromRequest;
	}


	public void setUrlTestFromRequest(String urlTestFromRequest) {
		this.urlTestFromRequest = urlTestFromRequest;
	}



	
}
