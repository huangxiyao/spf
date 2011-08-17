package com.hp.spp.wsrp.shield;

import java.util.Set;
import java.util.List;

/**
 * Data access object allowing to get the information about registered WSRP servers, whether they
 * are enabled and finally to change their "enabled" state.
 */
public interface RemoteServerDAO {
	/**
	 * @return a set of server urls which were marked as disabled. The urls always point to the target
	 * servers even if in the server console they were declared using WSDL proxy url.
	 */
	public Set<String> getDisabledServerUrls();

	/**
	 * @return a list of server details as present in Vignette Portal database.
	 */
	public List<PortalRemoteServerInfo> getPortalDeclaredRemoteServers();

	/**
	 * Changes the remote server state whose.
	 * @param url remote server WSDL URL
	 * @param enabled <tt>true</tt> if server should be enabled, <tt>false</tt> otherwise
	 */
	public void setUrlEnabled(String url, boolean enabled); 
}
