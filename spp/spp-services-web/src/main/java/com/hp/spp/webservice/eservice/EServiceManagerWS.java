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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.jsp.el.ELException;

import org.apache.log4j.Logger;

import com.hp.spp.common.util.Environment;
import com.hp.spp.eservice.EServiceManager;
import com.hp.spp.eservice.el.ExpressionResolver;
import com.hp.spp.profile.Constants;

/**
 * @author ageymond
 */
public class EServiceManagerWS {

	private static Logger mLog = Logger.getLogger(EServiceManagerWS.class);


	private final static long OPEN_IN_NEW_WINDOW = 1;

	private final static long ACTIVE_SECURITY_MODE = 1;

	private static String envPlatform = null;

	public EServiceResponse getEserviceInformation(EserviceRequest esRequest) {

		// GET SITE NAME
		String siteName = esRequest.getSiteName();

		// GET ESERVICE NAME
		String eServiceName = esRequest.getEServiceName();

		// MANAGE UserContext
		HashMap userContext = (HashMap) esRequest.getUserContext();
		userContext = (HashMap) transformFromFlatProfile(userContext);
		mLog.debug("Site is => " + siteName + "\n" + "Eservice " + eServiceName);

		if (envPlatform == null) {
			envPlatform = Environment.singletonInstance.getType();
		}

		userContext.put("environmentPlatform", envPlatform);

		mLog.debug("Environment is => " + envPlatform);

		// MANAGE HttpRequest parameter
		Map httpRequestParameters = esRequest.getHttpRequestParameters();
		mLog.debug("Http Request parameter " +httpRequestParameters);

		EServiceResponse response = null;
		java.util.List eServicelist = null;
		ExpressionResolver simulatingExpressionResolver = null;
		eServicelist = (java.util.List) com.hp.spp.eservice.dao.EServiceDAO.getInstance()
				.find(
						"select eservice from com.hp.spp.eservice.EService as eservice where eservice.site.name='"
								+ siteName + "' and eservice.name='" + eServiceName + "'");


		if (eServicelist.size() == 1) {

			com.hp.spp.eservice.EService eservice = (com.hp.spp.eservice.EService) eServicelist
					.get(0);

			//*******************************************************************************
			//                      Simulation Management
			//********************************************************************************

			String isSimulating = (String) userContext.get(Constants.MAP_IS_SIMULATING);

			if (isSimulating!=null && isSimulating.equalsIgnoreCase("true")){

				//Simulation is in place : Modify UserContext regarding simulationMode
				if (eservice.getSimulationMode()==2){
					mLog.warn("This Eservice does not support impersonation");
					//TODO : Modify it in order to throw an exception
					return null;
				}
				if (eservice.getSimulationMode()==3){
					//use simulated profile
					userContext.remove(Constants.MAP_SIMULATOR);
				}
				else if (eservice.getSimulationMode()==4){
					//use Admin profile (ie SimulatingUser)
					userContext = (HashMap)userContext.get(Constants.MAP_SIMULATOR);
				}
				// in other cases we keep both profiles
				else if (eservice.getSimulationMode()==1){
					try {
						simulatingExpressionResolver = new ExpressionResolver((HashMap)userContext.get(Constants.MAP_SIMULATOR));
					} catch (ELException e) {
						mLog.error("El Exception", e);
					}
				}
			}
			//*******************************************************************************
			//                     End of Simulation Management
			//********************************************************************************


			EServiceManager eserviceMgr = new EServiceManager(eservice);
			ExpressionResolver expressionResolver = null;
			try {
				expressionResolver = new ExpressionResolver(userContext);
			} catch (ELException e) {
				mLog.error("El Exception", e);
			}


			eserviceMgr.setExpressionResolver(expressionResolver);
			eserviceMgr.setHttpRequestParameters(httpRequestParameters);

			//Populate Response Object
			response = new EServiceResponse();
			response.setMethod(eserviceMgr.getMethod());
			response.setUrl(eserviceMgr.getEserviceUrl(esRequest.getUrlProdFromRequest(),esRequest.getUrlTestFromRequest()));
			
			response.setWindowParameters(eservice.getWindowParameters());
			
			response.setCharacterEncoding(eservice.getCharacterEncoding());

			if (eserviceMgr.getIsNewWindow() == OPEN_IN_NEW_WINDOW) {
				response.setOpenInNewWindow(true);
			} else {
				response.setOpenInNewWindow(false);
			}
			if (eserviceMgr.getSecurityMode() == ACTIVE_SECURITY_MODE) {
				response.setSecurityMode(true);
			} else {
				response.setSecurityMode(false);
			}
			response.setSimulationMode(eserviceMgr.getSimulationMode());



			if (simulatingExpressionResolver!=null){
				response.setParameters(getEserviceSimulationParameter(eserviceMgr,simulatingExpressionResolver));
			}
			else{
				response.setParameters(eserviceMgr.getEserviceParameters());
			}

		} else if (eServicelist.size() > 1) {
			mLog.error("Several definitions found in DB for site '" + siteName + "' and eservice '" + eServiceName + "'");
		} else {
			mLog.error("No definition found in DB for site '" + siteName + "' and eservice '" + eServiceName + "'");
		}

		return response;

	}



	private Map getEserviceSimulationParameter(EServiceManager eservMgr, ExpressionResolver simulatingProfile){

		//get parameter of simulated user
		Map eserviceParameter = eservMgr.getEserviceParameters();

		//switch expression resolver to simulated user
		eservMgr.setExpressionResolver(simulatingProfile);

		//analyse Map and retrieve parameters for Simulating User
		Map simulatingParameterMap = eservMgr.getEserviceParameters();

		if (simulatingParameterMap!=null){
			Iterator parameterIter = simulatingParameterMap.keySet().iterator();
			while (parameterIter.hasNext()){
				String key = (String) parameterIter.next();
				eserviceParameter.put(Constants.MAP_SIMULATOR+key, (String)simulatingParameterMap.get(key));
			}
		}
		mLog.debug("End of getEserviceSimulationParame");
		return eserviceParameter;

	}


	private Map transformFromFlatProfile(Map map){
		    HashMap mapToAdd = new HashMap();
		    Iterator it = map.entrySet().iterator();

			String isSimulating = (String) map.get(Constants.MAP_IS_SIMULATING);
			//If this is user is being simulated than restore.
			if (isSimulating!=null && isSimulating.equalsIgnoreCase("true")){

			while (it.hasNext()) {
		        Map.Entry pairs = (Map.Entry)it.next();
		        String pattern = Constants.MAP_SIMULATOR + ".";
				String key = (String)pairs.getKey();
				int index = key.indexOf(pattern);

				//If the map starts with "SimulatedUser"
				if(index >= 0){
					index = index + pattern.length();
					//Get the key minus the "SimulatedUser" tag with the "."
					key = key.substring(index, key.length());

					//Only add if this map is an instance of String
					if(pairs.getValue() instanceof String)
						mapToAdd.put(key, pairs.getValue());
					it.remove();
				}
			}

				map.put(Constants.MAP_SIMULATOR, mapToAdd);
			}
			return map;
	}

}
