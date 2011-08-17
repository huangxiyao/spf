package com.hp.spp.webservice.groups;

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

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author bmollard
 */
public class UserGroupManager {

	private static Logger logger = Logger.getLogger(UserGroupManager.class);

	/**
	 * @param gRequest GroupRequest Object which contain the site name and the user profile.
	 * @return GroupResponse Object which contain a list of groups.
	 */
	public UserGroupResponse getGroups(UserGroupRequest gRequest) {

		String siteName = gRequest.getSiteName();
		Map userContext = gRequest.getUserContext();

		long startTime = System.currentTimeMillis();
		if (logger.isDebugEnabled()) {
			logger.debug("The User Map is ["+userContext+"]");
		}
		
		List groupList = (List) com.hp.spp.groups.GroupManager.getAvailableGroups(siteName,
				userContext);

		if (logger.isDebugEnabled()) {
			logger.debug("**** size of the group list in getGroups() : " + groupList.size());
		}

		UserGroupResponse gResponse = new UserGroupResponse();

		gResponse.setGroupList(groupList);
		
		long executionTime = System.currentTimeMillis() - startTime;
		if (logger.isDebugEnabled()) {
			logger.debug("Authentification WS execution time ["+executionTime+"] ms");
		}

		return gResponse;

	}

}
