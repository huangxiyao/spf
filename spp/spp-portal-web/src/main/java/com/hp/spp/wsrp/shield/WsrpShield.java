package com.hp.spp.wsrp.shield;

import com.hp.spp.cache.Cache;

import java.util.Set;
import java.util.List;
import java.util.TreeSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;

/**
 * WsrpShield tracks the information about the WSRP servers that have been disabled or enabled by
 * the application. This is different from the WSRP server availability, i.e. the server can be enabled
 * even though it is unavailable (it does not respond), or it can be disabled even though it is
 * available (it responds properly).
 * <p>The goal of disabling the remote servers is to prevent the portal to connect to them while we know
 * that they have problems responding. This way the connection fails fast and does not consume
 * portal resources such as threads waiting for a response from remote server for long time.</p>
 *
 * <p>To verify whether the server is enabled use {@link #isServerEnabled(String)} method. To disable or
 * enable the server use {@link #setRemoteServerEnabled(String, boolean)} method.</p>
 *
 * <p>This class is a singleton and the access to its instance is available through {@link #getInstance()}
 * method.</p>
 */
public class WsrpShield {
	private static WsrpShield mInstance;

	private RemoteServerDAO mDAO;


	protected WsrpShield(RemoteServerDAO dao) {
		mDAO = dao;
	}

	/**
	 * @param url URL of the WSRP server; this can be either WSDL URL or any of WSRP services' URLs
	 * @return <tt>true</tt> if the server is enabled, i.e. the portal will attempt to connect to
	 * that server, or <tt>false</tt> if the server is disabled, i.e. the portal will not even try
	 * to connect to the server
	 */
	public boolean isServerEnabled(String url) {
		return (!mDAO.getDisabledServerUrls().contains(url));
	}

	/**
	 * @return a set of records about remote servers registered in the portal
	 */
	public Set<RemoteServerInfo> getRemoteServerList() {
		Set<String> disabledServerUrls = mDAO.getDisabledServerUrls();
		List<PortalRemoteServerInfo> declaredServers = mDAO.getPortalDeclaredRemoteServers();
		Map<String, RemoteServerInfo> result = new TreeMap<String, RemoteServerInfo>();

		for (Iterator<PortalRemoteServerInfo> it = declaredServers.iterator(); it.hasNext();) {
			PortalRemoteServerInfo info = it.next();
			RemoteServerInfo resultInfo = result.get(info.getWsdlUrl());
			if (resultInfo == null) {
				resultInfo = new RemoteServerInfo(info.getWsdlUrl(), !disabledServerUrls.contains(info.getWsdlUrl()));
				result.put(info.getWsdlUrl(), resultInfo);
			}
			resultInfo.addName(info.getTitle());
		}

		return new TreeSet<RemoteServerInfo>(result.values());
	}

	/**
	 * @param remoteServerUrl remote server WSDL URL
	 * @param enabled flag indicating whether the server should be enabled (<tt>true</tt>) or disabled (<tt>false</tt>)
	 */
	public void setRemoteServerEnabled(String remoteServerUrl, boolean enabled) {
		mDAO.setUrlEnabled(remoteServerUrl, enabled);
	}

	/**
	 * @return singleton instance of this class
	 */
	public static synchronized WsrpShield getInstance() {
		if (mInstance == null) {
			mInstance = new WsrpShield(
					new CachingRemoteServerDAO(
							new WebServiceEndpointsDAO(
									new JDBCRemoteServerDAO()),
							Cache.getInstance()));
		}
		return mInstance;
	}

}
