/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.group.manager;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;

import com.hp.it.spf.user.exception.UserGroupsException;
import com.hp.it.spf.user.group.stub.ArrayOfString;
import com.hp.it.spf.user.group.stub.ArrayOfUserContext;
import com.hp.it.spf.user.group.stub.GetGroups;
import com.hp.it.spf.user.group.stub.GroupRequest;
import com.hp.it.spf.user.group.stub.GroupResponse;
import com.hp.it.spf.user.group.stub.SiteDoesNotExistException;
import com.hp.it.spf.user.group.stub.UGSRuntimeServiceXfireImpl;
import com.hp.it.spf.user.group.stub.UGSRuntimeServiceXfireImplHttpBindingStub;
import com.hp.it.spf.user.group.stub.UGSRuntimeServiceXfireImplLocator;
import com.hp.it.spf.user.group.stub.UserContext;
import com.hp.it.spf.user.group.utils.UGSParametersManager;

/**
 * This is the implimentation class of <tt>IUserGroupRetriever</tt>.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @version 1.0
 */
public class SSOUserGroupRetriever implements IUserGroupRetriever {
    private static final Logger LOG = Logger.getLogger(SSOUserGroupRetriever.class.toString());

    private UGSParametersManager ugsParametersManager = null;

    /**
     * Retrieve user groups by site name and user profiles.
     * 
     * @param siteName site name
     * @param userProfile user profiles
     * @return user groups set, if user has no groups, an empty Set will be
     *         returned.
     * @throws UserGroupsException if any exception occurs, an
     *             UserGroupsException will be thrown.
     */
    public Set<String> getGroups(String siteName,
                                 Map<String, Object> userProfile) throws UserGroupsException {
        Set<String> groupSet = new HashSet<String>();
		try {            
            GroupRequest serviceRequest = getServiceRequest(siteName, userProfile);
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("UGS invokeService");
            }

            GroupResponse serviceResponse = invokeService(serviceRequest);
            ArrayOfString groupListAS = serviceResponse.getGroupList();

            if (groupListAS != null) {
                String[] groupList = groupListAS.getString();
                for (String group : groupList) {
                    groupSet.add(group);
                }
                
                if (LOG.isLoggable(Level.FINEST)) {
                    StringBuffer groupStr = new StringBuffer();
                    if (groupList.length > 0) {
                        for (int i = 0; i < groupList.length; i++) {
                            groupStr.append(groupList[i]);
                            groupStr.append(",");
                        }
                        LOG.finest("Output group listing: " + groupStr);
                    }
                }
            }            
            return groupSet;
        } catch (SiteDoesNotExistException e) {
            String msg = "The site, "
                         + siteName
                         + ", doesn't exist in the UGS definition database";            
            throw new UserGroupsException(msg, e);
        } catch (Exception e) {            
            throw new UserGroupsException(e);
        }
    }

    /**
     * This method creates a GroupRequest object according to site name and user
     * profiles.
     * 
     * @param siteName site name
     * @param userProfile user profile map
     * @return GroupRequest object
     * @throws IllegalArgumentException if method parameters are invaild
     */
    private GroupRequest getServiceRequest(String siteName,
                                           Map<String, Object> userProfile) {
        if (siteName == null
            || userProfile == null
            || siteName.trim().equals("")
            || userProfile.isEmpty()) {
            throw new IllegalArgumentException("Create GroupRequest failed, site name or user profile map is not defined.");
        }

        // Populate request object
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setSiteName(siteName);
        groupRequest.setUserContext(new ArrayOfUserContext(convertToUserContext(userProfile)));

        if (LOG.isLoggable(Level.FINEST)) {
            LOG.finest("UGS request object: SiteName="
                       + siteName
                       + " User Profile="
                       + userProfile.toString());
        }

        return groupRequest;
    }

    /**
     * Convert user profile map to <tt>UserContext</tt> array.
     * 
     * @param map user profile map
     * @return <tt>UserContext</tt> array
     */
    private UserContext[] convertToUserContext(Map<String, Object> map) {
        UserContext[] context = null;
        if (map != null) {
            context = new UserContext[map.size()];
            int x = 0;
            for (String key : map.keySet()) {
                context[x] = new UserContext();
                context[x].setKey(key);
                context[x].setValue(map.get(key).toString());

                x++;
            }
        }
        return context;
    }

    /**
     * This method calls UGS web service to get group listing
     * 
     * @param groupRequest GroupRequest object
     * @throws RemoteException if remote server is not avaliable
     * @throws MalformedURLException if URL is malformed
     * @throws ServiceException if webservice is failed
     */
    private GroupResponse invokeService(GroupRequest groupRequest) throws RemoteException,
                                                                  MalformedURLException,
                                                                  ServiceException {
        ugsParametersManager = UGSParametersManager.getInstance();
        // Make a service
        UGSRuntimeServiceXfireImpl gms = new UGSRuntimeServiceXfireImplLocator();

        // Configure the url of call
        String ugsUrl = ugsParametersManager.getEndPoint();
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.finest("URL to call UGS : [" + ugsUrl + "]");
        }

        UGSRuntimeServiceXfireImplHttpBindingStub binding = (UGSRuntimeServiceXfireImplHttpBindingStub)gms.getUGSRuntimeServiceXfireImplHttpPort(new URL(ugsUrl));
        binding.setTimeout(Integer.valueOf(ugsParametersManager.getTimeout()));
        if (LOG.isLoggable(Level.FINEST)) {
            LOG.finest("UGS Timeout after setting: " + binding.getTimeout());
        }
        // Invoke the service
        return binding.getGroups(new GetGroups(groupRequest)).getOut();
    }

}
