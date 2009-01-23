package com.hp.it.spf.user.group.manager;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.rpc.ServiceException;

import com.hp.it.spf.user.exception.UserGroupsException;
import com.hp.it.spf.user.group.stub.GroupRequest;
import com.hp.it.spf.user.group.stub.GroupResponse;
import com.hp.it.spf.user.group.stub.SiteDoesNotExistException;
import com.hp.it.spf.user.group.stub.UGSRuntimeServiceXfireImpl;
import com.hp.it.spf.user.group.stub.UGSRuntimeServiceXfireImplHttpBindingStub;
import com.hp.it.spf.user.group.stub.UGSRuntimeServiceXfireImplLocator;
import com.hp.it.spf.user.group.stub.UserContext;
import com.hp.it.spf.user.group.utils.UGSParametersManager;

public class SSOUserGroupRetriever implements IUserGroupRetriever {
    private static final Logger LOG = Logger.getLogger(SSOUserGroupRetriever.class.toString());

    private UGSParametersManager ugsParametersManager = null;

    public Set<String> getGroups(String siteName,
                                 Map<String, String> userProfile) throws UserGroupsException {
        Set<String> groupSet = new HashSet<String>();
        try {
            GroupRequest serviceRequest = getServiceRequest(siteName,
                                                            new HashMap<String, String>(userProfile));
            if (LOG.isLoggable(Level.FINEST)) {
                LOG.finest("UGS invokeService");
            }

            GroupResponse serviceResponse = invokeService(serviceRequest);
            String[] groupList = serviceResponse.getGroupList();

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

            if (groupList != null) {
                for (String group : groupList) {
                    groupSet.add(group);
                }
            }
            return groupSet;

        } catch (SiteDoesNotExistException e) {
            String msg = "The site, "
                         + siteName
                         + ", doesn't exist in the UGS definition database";
            throw new UserGroupsException(msg, e);
        } catch (ServiceException e) {
            String msg = "Invoking UGS webservice raise a ServiceException";
            throw new UserGroupsException(msg, e);
        } catch (RemoteException e) {
            String msg = "Invoking UGS webservice raise a RemoteException";
            throw new UserGroupsException(msg, e);
        } catch (MalformedURLException e) {
            String msg = "Invoking UGS webservice with a malformed URL";
            throw new UserGroupsException(msg, e);
        } catch (Exception e) {
            throw new UserGroupsException(e);
        }
    }

    /*
     * This method creates a GroupRequest object and return it
     */
    private GroupRequest getServiceRequest(String siteName,
                                           HashMap<String, String> userProfile) {
        if (siteName == null
            || userProfile == null
            || siteName.trim().equals("")
            || userProfile.isEmpty()) {
            return null;
        }

        // Populate request object
        GroupRequest groupRequest = new GroupRequest();
        groupRequest.setSiteName(siteName);
        groupRequest.setUserContext(convertToUserContext(userProfile));

        if (LOG.isLoggable(Level.FINEST)) {
            LOG.finest("UGS request object: SiteName="
                      + siteName
                      + " User Profile="
                      + userProfile.toString());
        }

        return groupRequest;
    }

    public synchronized static UserContext[] convertToUserContext(Map<String, String> map) {
        UserContext[] context = null;
        if (map != null) {
            context = new UserContext[map.size()];
            int x = 0;
            for (String key : map.keySet()) {
                context[x] = new UserContext();
                context[x].setKey(key);
                context[x].setValue(map.get(key));
                x++;
            }
        }
        return context;
    }

    /*
     * This method calls UGS web service to get group listing
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
        return binding.getGroups(groupRequest);
    }

}
