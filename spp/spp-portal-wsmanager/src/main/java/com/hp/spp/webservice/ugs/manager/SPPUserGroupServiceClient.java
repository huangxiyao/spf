package com.hp.spp.webservice.ugs.manager;

import com.hp.spp.webservice.ugs.client.UserGroupRequest;
import com.hp.spp.webservice.ugs.client.UserGroupResponse;
import com.hp.spp.webservice.ugs.client.UserGroupManagerService;
import com.hp.spp.webservice.ugs.client.UserGroupManagerServiceLocator;
import com.hp.spp.webservice.ugs.client.UserGroupManagerSoapBindingStub;
import com.hp.spp.config.ConfigException;
import com.hp.spp.config.Config;
import com.hp.spp.portal.common.site.SiteManager;
import com.hp.spp.portal.common.site.Site;

import javax.xml.rpc.ServiceException;
import java.util.Map;
import java.util.HashMap;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * Retrieves the groups calling SPP User Group Service web service.
 */
public class SPPUserGroupServiceClient implements UserGroupService {

	public static final String UGS_URL_CONFIG_KEY = "SPP.ugs.url";

	private static final Logger mLog = Logger.getLogger(SPPUserGroupServiceClient.class);

	@SuppressWarnings("unchecked")
	public String[] getUserGroups(String siteName, Map userProfile) throws MalformedURLException, RemoteException, ServiceException, ConfigException {
		if (mLog.isDebugEnabled()) {
			mLog.debug("getServiceRequest");
		}
		UserGroupRequest serviceRequest = getServiceRequest(siteName, new HashMap(userProfile));

		if (mLog.isDebugEnabled()) {
			mLog.debug("invokeService");
		}
		UserGroupResponse serviceResponse = invokeService(serviceRequest);

		if (mLog.isDebugEnabled()) {
			mLog.debug("processServiceResponse");
		}
		return processServiceResponse(serviceResponse);
	}

	private UserGroupRequest getServiceRequest(String siteName, HashMap userProfile) {
		if (siteName == null || userProfile == null || siteName.equals("")
				|| userProfile.isEmpty()) {
			return null;
		}

		// Populate request bean
		UserGroupRequest serviceRequest = new UserGroupRequest();
		serviceRequest.setSiteName(siteName);
		serviceRequest.setUserContext(userProfile);

		return serviceRequest;
	}

	private UserGroupResponse invokeService(UserGroupRequest serviceRequest)
			throws RemoteException, ServiceException, MalformedURLException, ConfigException {

		String siteName = serviceRequest.getSiteName();
		// Make a service
		UserGroupManagerService gms = new UserGroupManagerServiceLocator();

		// Configure the url of call
		Site site = SiteManager.getInstance().getSite(siteName);
		String ugsUrl = site.getUGSUrl();

		//FIXME (slawek) - remove this ones we clean up the all site settings; this entry should be created in SPP_SITE_SETTING table.
		if (ugsUrl == null) {
			ugsUrl = Config.getValue(UGS_URL_CONFIG_KEY);
		}

		if (mLog.isDebugEnabled()){
			mLog.debug("URL to call UGS : [" + ugsUrl + "]");
		}

		UserGroupManagerSoapBindingStub binding = (UserGroupManagerSoapBindingStub)gms.getUserGroupManager(new URL(ugsUrl));
		//binding.setTimeout(Config.getIntValue(timeOutKey,Config.getIntValue(genericTimeout, 60000)));
		binding.setTimeout(site.getUGSTimeoutInMilliseconds());
		if(mLog.isDebugEnabled()){
			mLog.debug("Timeout after setting: "+binding.getTimeout());
		}
		// Invoke the service
		return binding.getGroups(serviceRequest);
	}

	private String[] processServiceResponse(UserGroupResponse serviceResponse) {
		// Process the response
		Object[] o = serviceResponse.getGroupList();

		// init ugs_Group
		String[] s = new String[o.length];
		for (int i = 0; i < o.length; i++) {
			try {
				s[i] = (String) o[i];
			} catch (Exception e) {
				s[i] = "";
			}
		}
		return s;
	}

}
