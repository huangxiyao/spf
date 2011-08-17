package com.hp.spp.webservice.ugs.manager;

import com.hp.spp.config.ConfigException;

import javax.xml.rpc.ServiceException;
import java.util.Map;
import java.rmi.RemoteException;
import java.net.MalformedURLException;

/**
 * This class ecaplsulates the logic required to get the list of groups for the user group service.
 */
public interface UserGroupService {

	/**
	 * Retrieves the list (array) of groups from user group service.
	 *
	 * @param siteName name of the site for which the groups are retrieved
	 * @param userProfile profile of the user for whom the groups are retrieved
	 * @return a list of group names or an empty array
	 * 
	 * @throws RemoteException If an error occurs when calling the web service
	 * @throws ServiceException If an error occurs when calling the web service
	 * @throws MalformedURLException If an error occurs when instantiating the URL of the web service
	 * @throws ConfigException If the require configuration data is not available
	 */
	public String[] getUserGroups(String siteName, Map userProfile) throws RemoteException, ServiceException, MalformedURLException, ConfigException;

}
