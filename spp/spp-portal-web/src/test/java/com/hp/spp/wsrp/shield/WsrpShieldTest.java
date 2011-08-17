package com.hp.spp.wsrp.shield;

import junit.framework.TestCase;

import java.util.Set;
import java.util.TreeSet;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

public class WsrpShieldTest extends TestCase {

	public void testIsServerEnabled() throws Exception {
		RemoteServerDAO dao = new RemoteServerDAO() {
			public Set<String> getDisabledServerUrls() {
				return new TreeSet<String>(Arrays.asList(
						"http://wsrp_server/portlet1/wsdl/wsrp_wsdl.jsp"));
			}

			public List<PortalRemoteServerInfo> getPortalDeclaredRemoteServers() {
				return null;
			}

			public void setUrlEnabled(String url, boolean enabled) {
			}
		};
		WsrpShield shield = new WsrpShield(dao);

		assertTrue("Server not returned by DAO is enabled",
				shield.isServerEnabled("http://wsrp_server/PerfTestPortlet/wsdl/wsrp_wsdl.jsp"));
		assertFalse("Server returned by DAO is disabled",
				shield.isServerEnabled("http://wsrp_server/portlet1/wsdl/wsrp_wsdl.jsp"));
	}

	public void testGetRemoteServerList() throws Exception {
		RemoteServerDAO dao = new RemoteServerDAO() {
			public Set<String> getDisabledServerUrls() {
				return new TreeSet<String>(Arrays.asList(
						"http://wsrp_server/portlet/wsdl/wsrp_wsdl.jsp"));
			}

			public List<PortalRemoteServerInfo> getPortalDeclaredRemoteServers() {
				return Arrays.asList(
						new PortalRemoteServerInfo("Server 1", "http://wsrp_server/portlet/wsdl/wsrp_wsdl.jsp"),
						new PortalRemoteServerInfo("Server 2", "http://wsrp_server_2/portlet/wsdl/wsrp_wsdl.jsp"),
						new PortalRemoteServerInfo("Server 2 (duplicate)", "http://wsrp_server_2/portlet/wsdl/wsrp_wsdl.jsp"));
			}

			public void setUrlEnabled(String url, boolean enabled) {
			}
		};
		WsrpShield shield = new WsrpShield(dao);

		Set<RemoteServerInfo> servers = shield.getRemoteServerList();
		assertNotNull("Server list is not null", servers);
		assertEquals("Number of servers on the list", 2, servers.size());

		Iterator<RemoteServerInfo> it = servers.iterator();

		//servers should be sorted by URL
		RemoteServerInfo server1 = it.next();
		assertEquals("Server 1 url", "http://wsrp_server/portlet/wsdl/wsrp_wsdl.jsp", server1.getUrl());
		assertEquals("Server 1 title", Arrays.asList("Server 1"), server1.getNames());
		assertEquals("Server 1 is enabled", false, server1.isEnabled());

		RemoteServerInfo server2 = it.next();
		assertEquals("Server 2 url", "http://wsrp_server_2/portlet/wsdl/wsrp_wsdl.jsp", server2.getUrl());
		assertEquals("Server 2 titles",
				new TreeSet<String>(Arrays.asList("Server 2", "Server 2 (duplicate)")),
				new TreeSet<String>(server2.getNames()));
		assertEquals("Server 2 is enabled", true, server2.isEnabled());

	}



}
