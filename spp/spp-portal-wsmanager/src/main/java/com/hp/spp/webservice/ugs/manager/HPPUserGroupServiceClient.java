package com.hp.spp.webservice.ugs.manager;

import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.hp.spp.common.util.DiagnosticContext;
import com.hp.runtime.ugs.asl.service.GroupRequest;
import com.hp.runtime.ugs.asl.service.GroupResponse;
import com.hp.runtime.ugs.asl.service.UGSRuntimeServiceXfireImplLocator;
import com.hp.runtime.ugs.asl.service.UGSRuntimeServiceXfireImpl;
import com.hp.runtime.ugs.asl.service.UGSRuntimeServiceXfireImplHttpBindingStub;
import com.hp.runtime.ugs.asl.service.AnyType2AnyTypeMapEntry;
import com.hp.runtime.ugs.asl.service.InvalidGroupRequestException;
import com.hp.runtime.ugs.asl.service.NoRulesOrGroupsForSiteException;
import com.hp.runtime.ugs.asl.service.SiteDoesNotExistException;
import com.hp.runtime.ugs.asl.service.UGSSystemException;
import com.hp.runtime.ugs.asl.service.UserContext;
import com.hp.spp.config.ConfigException;
import com.hp.spp.portal.common.site.Site;
import com.hp.spp.portal.common.site.SiteManager;
import com.hp.spp.webservice.ugs.client.UserGroupRequest;
import com.hp.spp.webservice.ugs.client.UserGroupResponse;

/**
 * Retrieves the list of user groups calling HP Passport User Group Service.
 */
public class HPPUserGroupServiceClient implements UserGroupService {

	private static final Logger mLog = Logger.getLogger(HPPUserGroupServiceClient.class);

   /*
    * This method gets a group listing for a site and user profile information
   */	
	public String[] getUserGroups(String siteName, Map userProfile) 
		throws RemoteException, ServiceException, MalformedURLException, ConfigException, UGSSystemException, InvalidGroupRequestException, NoRulesOrGroupsForSiteException {
		
		String groupList[];
		
		if (mLog.isDebugEnabled()) {
			mLog.debug("HPP UGS getServiceRequest");
		}
		try {
			GroupRequest serviceRequest = getServiceRequest(siteName, new HashMap(userProfile));

			if (mLog.isDebugEnabled()) {
				mLog.debug("HPP UGS invokeService");
			}

			GroupResponse serviceResponse = invokeService(serviceRequest);
			groupList = serviceResponse.getGroupList();

			if (mLog.isDebugEnabled()) {
				StringBuffer groupStr = new StringBuffer();
				if (groupList.length > 0) {
					for (int i = 0; i < groupList.length; i++) {
						groupStr.append(groupList[i]);
						groupStr.append(",");
					}
					mLog.debug("Output group listing: " + groupStr);
				}
			}
			
			// To match with the old SPP UGS calls, 
			// return an empty group listing if groupList is null.
			if (groupList == null) {
				groupList = new String[0];
			}
			
			return groupList;		
			
		} catch (SiteDoesNotExistException e) {
			// To match with the old SPP UGS calls, return an empty group listing if sitename doesn't exist.
			// (Especially, for the "console" site which doesn't exist in the UGS DB, 
			// we still need to return empty group listing, not exception
			if (mLog.isDebugEnabled()) {
				mLog.debug("The site, " + siteName + ", doesn't exist in the UGS definition database");
			}
			groupList = new String[0];
			return groupList;
		}
	}
	
   /*
	* This method creates a GroupRequest object and return it
	*/	
	private GroupRequest getServiceRequest(String siteName, HashMap userProfile) {
		if (siteName == null || userProfile == null || siteName.equals("")
				|| userProfile.isEmpty()) {
			return null;
		}

		// Populate request object
		GroupRequest groupRequest = new GroupRequest();
		groupRequest.setSiteName(siteName);
		groupRequest.setUserContext(convertToUserContext(userProfile));
		if (mLog.isDebugEnabled()){
			mLog.debug("HPP UGS request object: SiteName=" + siteName + " User Profile=" + userProfile.toString());
		}

		return groupRequest;
	}

	/*
	* This method calls HPP UGS web service to get group listing
	*/	
	private GroupResponse invokeService(GroupRequest groupRequest)
		throws RemoteException, ServiceException, MalformedURLException, ConfigException, UGSSystemException, SiteDoesNotExistException, InvalidGroupRequestException, NoRulesOrGroupsForSiteException {
		String siteName = groupRequest.getSiteName();
		// Make a service
		UGSRuntimeServiceXfireImpl gms = new UGSRuntimeServiceXfireImplLocator();

		// Configure the url of call
		Site site = SiteManager.getInstance().getSite(siteName);
		String ugsUrl = site.getUGSUrl();
		if (mLog.isDebugEnabled()){
			mLog.debug("HPP UGS URL to call UGS : [" + ugsUrl + "]");
		}

		UGSRuntimeServiceXfireImplHttpBindingStub binding = (UGSRuntimeServiceXfireImplHttpBindingStub)gms.getUGSRuntimeServiceXfireImplHttpPort(new URL(ugsUrl));
		binding.setTimeout(site.getUGSTimeoutInMilliseconds());
		if(mLog.isDebugEnabled()){
			mLog.debug("HPP UGS Timeout after setting: "+binding.getTimeout());
		}
		// Invoke the service
		return binding.getGroups(groupRequest);
	}

    public synchronized static UserContext[] convertToUserContext(Map<String,String> map){

        UserContext[] context =null;

        if (map != null){

              context = new UserContext[map.size()];

              int x=0;

              for (String key : map.keySet()){

                    context[x] = new UserContext();

                    context[x].setKey(key);

                    context[x].setValue(map.get(key));

                    x++;

              }

        }

        return context;

  }
	
	
}
