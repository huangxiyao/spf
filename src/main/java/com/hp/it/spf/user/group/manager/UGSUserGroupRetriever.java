/*
 * Project: Shared Portal Framework 
 * Copyright (c) 2008 HP. All Rights Reserved.
 */
package com.hp.it.spf.user.group.manager;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.epicentric.site.Site;
import com.hp.it.spf.sso.portal.AuthenticatorHelper;
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
import com.hp.it.spf.xa.dc.portal.ErrorCode;
import com.hp.it.spf.xa.log.portal.Operation;
import com.hp.it.spf.xa.log.portal.TimeRecorder;
import com.hp.it.spf.xa.misc.portal.RequestContext;
import com.hp.it.spf.xa.misc.portal.Utils;
import com.vignette.portal.log.LogConfiguration;
import com.vignette.portal.log.LogWrapper;

/**
 * This is the implimentation class of <tt>IUserGroupRetriever</tt>.
 * 
 * @author <link href="ying-zhiw@hp.com">Oliver</link>
 * @author Slawek Zachcial (slawomir.zachcial@hp.com)
 * @version 1.0
 */
public class UGSUserGroupRetriever implements IUserGroupRetriever {    
    private static final LogWrapper LOG = AuthenticatorHelper.getLog(UGSUserGroupRetriever.class);
	/**
     * Retrieve user groups by site name and user profiles.
     * 
     * @param userProfile user profiles
     * @param request HttpServletRequest
     * @return user groups set, if user has no groups, an empty Set will be
     *         returned.
     * @throws UserGroupsException if any exception occurs, an
     *             UserGroupsException will be thrown.
     */
    public Set<String> getGroups(Map<String, Object> userProfile,
                                 HttpServletRequest request) throws UserGroupsException {
        Set<String> groupSet = new HashSet<String>();
        String siteName = null;
        
        TimeRecorder timeRecorder = RequestContext.getThreadInstance().getTimeRecorder();
        try {          
		    timeRecorder.recordStart(Operation.GROUPS_CALL);
		    Site site = Utils.getEffectiveSite(request);
		    
		    // user access VAP console
		    if (site == null) {
		    	return groupSet;
		    }
		    siteName = site.getDNSName();
            GroupRequest serviceRequest = getServiceRequest(siteName, userProfile);
            if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
                LOG.debug("UGS invokeService");
            }

            GroupResponse serviceResponse = invokeService(serviceRequest);
            ArrayOfString groupListAS = serviceResponse.getGroupList();

            if (groupListAS != null) {
                String[] groupList = groupListAS.getString();
                for (String group : groupList) {
                    groupSet.add(group);
                }
            }
			
            timeRecorder.recordEnd(Operation.GROUPS_CALL);
			if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
				LOG.debug("Group returned by UGS: " + groupSet);
			}
            return groupSet;
        } catch (SiteDoesNotExistException ex) {
            String msg = "The site, "
                         + siteName
                         + ", doesn't exist in the UGS definition database"; 
            timeRecorder.recordError(Operation.GROUPS_CALL, ex);
            RequestContext.getThreadInstance().getDiagnosticContext().setError(ErrorCode.GROUPS002, ex.getMessage());
            throw new UserGroupsException(msg, ex);
        } catch (Exception ex) {      
            timeRecorder.recordError(Operation.GROUPS_CALL, ex);
            RequestContext.getThreadInstance().getDiagnosticContext().setError(ErrorCode.GROUPS002, ex.getMessage());
            throw new UserGroupsException(ex);
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
		String ugsSiteId = getUGSSiteID(siteName);
        groupRequest.setSiteName(ugsSiteId);
		UserContext[] userContextItems = convertToUserContext(userProfile);
		groupRequest.setUserContext(new ArrayOfUserContext(userContextItems));

        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
			StringBuilder message = new StringBuilder("UGS request object: SiteName=");
			message.append(ugsSiteId);
			message.append(" User Profile=[");
			for (int i = 0, length = userContextItems.length; i < length; i++) {
				UserContext userContextItem = userContextItems[i];
				message.append(userContextItem.getKey()).append('=').append(userContextItem.getValue());
				if (i < length-1) {
					message.append(',');
				}
			}
			message.append("]");
            LOG.debug(message);
        }

        return groupRequest;
    }


	/**
	 * Returns the site ID expected by UGS.
	 * In UGS site ID is unique. This means that if we want to connect different portal environments
	 * (e.g. DEV, ITFT, ITG) to the same UGS instance and if each hosts the same sites, we need to
	 * uniquely identify these sites in UGS. To achieve this we can use environment specific prefix
	 * which is defined in UGSParameters.properties file.
	 *
	 * @param siteName portal site name
	 * @return UGS site ID
	 */
	private String getUGSSiteID(String siteName)
	{
		return UGSParametersManager.getInstance().getSiteNamePrefix() + siteName;
	}


    /**
     * Convert user profile map to <tt>UserContext</tt> array.
	 * <p>
	 * As UGS is expecting a set of name/value pairs the profile map which has an open structure
	 * must be converted to such prais. While all the information is represented in the result
	 * the structure is flattened and therefore is loosing some of its semantics. The different
	 * types of values from user profile map are converted as follows:
	 * <ul>
	 * <li>null - its value is set to null in the result</li>
	 * <li>String - included as-is </li>
	 * <li>List of strings - value is a concatention of list items separated by semi-colon</li>
	 * <li>Map - the resulting attribute names is {profile key}.{map key}; the value is
	 * the string representation of the map value</li>
	 * <li>List of maps - the result is somewhat similar to the map above except that the values
	 * are jointed lists of the values from the map items. For example for the following list of maps<br/>
	 * [{key1 : value1, key2 : value2}, {key1 : value3, key2 : value3}]<br/> the resuting UserContext
	 * entries will be as follows:<br /> {profile key}.key1 : value1;value3 and {profile key}.key2 : value2;value4.<br />
	 * In this context the semantics of the attribute are lost as the result does not contain any
	 * information about the fact that value1 and value2 come from the same entity. </li>
	 * <li>Everything else - the string represetation is used as the value in the map</li>
	 * </ul>
     * 
     * @param userProfile user profile map
     * @return <tt>UserContext</tt> array
     */
	@SuppressWarnings("unchecked")
    UserContext[] convertToUserContext(Map<String, Object> userProfile) {
		if (userProfile == null) {
			return new UserContext[0];
		}

		List<UserContext> result = new ArrayList<UserContext>();

		for (Map.Entry<String, Object> entry : userProfile.entrySet()) {
			final String attributeName = entry.getKey();
			final Object attributeNativeValue = entry.getValue();

			// If attribute is a string we include it as-is
			if (attributeNativeValue instanceof String) {
				result.add(new UserContext(attributeName, (String) attributeNativeValue));
			}
			else if (attributeNativeValue instanceof List) {
				// If attribute is a list we need to apply special handling for list of Strings
				// and list of maps.
				List listValue = (List) attributeNativeValue;
				convertList(result, attributeName, listValue);
			}
			else if (attributeNativeValue instanceof Map) {
				// If this attribute value is a map we just add each of its properties as separate
				// UserContext item with name being {attribute name}.{property name}
				Map<?, ?> entity = (Map<String, ?>) attributeNativeValue;
				convertMap(result, attributeName, entity);
			}
			else if (attributeNativeValue == null){
				// If this is a null we just add null
				result.add(new UserContext(attributeName, null));
			}
			else {
				// If we are not able to figure out what that is we simply add its string representation
			    if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
					LOG.debug("Don't know how to hanlde attribute '" + attributeName +
					"' of type '" + attributeNativeValue.getClass().getName() +
							"'. Adding its string representation.");
				}
				result.add(new UserContext(attributeName, String.valueOf(attributeNativeValue)));
			}

		}
		return result.toArray(new UserContext[0]);
    }

	private void convertList(List<UserContext> result, String attributeName, List listValue)
	{
		if (listValue.isEmpty()) {
			result.add(new UserContext(attributeName, null));
		}
		else {
			if (listValue.get(0) instanceof String) {
				// if attribute is a list of strings we join them using semi colon
				result.add(new UserContext(attributeName, join(listValue, ';')));
			}
			else if (listValue.get(0) instanceof Map) {
				// If attribute is a list of maps it becomes more complex so please
				// look into the method below where you'll find more information
				convertListOfMaps(result, attributeName, listValue);
			}
		}
	}

	private void convertMap(List<UserContext> result, String attributeName, Map<?, ?> entity)
	{
		for (Map.Entry<?, ?> property : entity.entrySet()) {
			result.add(new UserContext(
					attributeName + "." + property.getKey(),
					String.valueOf(property.getValue())));
		}
	}

	@SuppressWarnings("unchecked")
	private void convertListOfMaps(List<UserContext> result, String attributeName, List listValue)
	{
		// If attribute is a list of maps we create a flat set of name value pairs
		// where a name will be a property name and its value will be a join of
		// values from all the maps for that property. This is not ideal but
		// it's the best we can do.
		// For example if we have list of 2 entities for attribute called address:
		//  {city: Lyon, country: France}
		//  {city: Chicago, country: USA}
		// the result would be:
		//  address.city=Lyon;Chicago
		//  address.country=France;USA

		Map<String, StringBuilder> flatEntityProperties =
				new HashMap<String, StringBuilder>();
		List<Map<?, ?>> entityList = (List<Map<?, ?>>) listValue;

		// first let's collect the values using StringBuilder for efficiency
		for (Map<?, ?> entity : entityList) {
			for (Map.Entry<?, ?> property : entity.entrySet()) {
				String propertyAttributeName = attributeName + "." + property.getKey();
				StringBuilder propertyValue = flatEntityProperties.get(propertyAttributeName);
				if (propertyValue != null) {
					propertyValue.append(';').append(property.getValue());
				}
				else {
					flatEntityProperties.put(
							propertyAttributeName,
							new StringBuilder(String.valueOf(property.getValue())));
				}
			}
		}

		// then add to the result what we collected
		for (Map.Entry<String, StringBuilder> entityProperty : flatEntityProperties.entrySet()) {
			result.add(new UserContext(
					entityProperty.getKey(),
					entityProperty.getValue().toString()));
		}
	}

	private String join(List items, char delimiter) {
		StringBuilder result = new StringBuilder();
		for (Iterator it = items.iterator(); it.hasNext();) {
			Object item = it.next();
			result.append(item);
			if (it.hasNext()) {
				result.append(delimiter);
			}
		}
		return result.toString();
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
		UGSParametersManager ugsParametersManager = UGSParametersManager.getInstance();
        // Make a service
        UGSRuntimeServiceXfireImpl gms = new UGSRuntimeServiceXfireImplLocator();

        // Configure the url of call
        String ugsUrl = ugsParametersManager.getEndPoint();
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("URL to call UGS : [" + ugsUrl + "]");
        }

        UGSRuntimeServiceXfireImplHttpBindingStub binding = (UGSRuntimeServiceXfireImplHttpBindingStub)gms.getUGSRuntimeServiceXfireImplHttpPort(new URL(ugsUrl));
        binding.setTimeout(Integer.valueOf(ugsParametersManager.getTimeout()));
        if (LOG.willLogAtLevel(LogConfiguration.DEBUG)) {
            LOG.debug("UGS Timeout after setting: " + binding.getTimeout());
        }
        // Invoke the service
        return binding.getGroups(new GetGroups(groupRequest)).getOut();
    }

}
