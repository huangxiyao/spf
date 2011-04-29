/*
 * Project: Shared Portal Framework
 * Copyright (c) 2008 HP. All Rights Reserved.
 *
 */
package com.hp.it.spf.openportal.portletcontainer.context.registry.database;

import java.net.InetAddress;
import java.util.ResourceBundle;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.coordination.CommandProcessor;
import org.eclipse.persistence.sessions.coordination.RemoteCommandManager;
import org.eclipse.persistence.sessions.coordination.jms.JMSTopicTransportManager;

import javax.management.ObjectName;
import javax.management.MBeanServer;
import javax.naming.InitialContext;

/**
 * This class involves Weblogic JMS to support Eclipselink cache coordination.
 * 
 * @author <link href="ye.liu@hp.com">Liu Ye</link>
 * @version TBD
 */
public class JMSCacheCoordinator implements SessionCustomizer{

	private int mManagedServerPort = -1;

	/* (non-Javadoc)
	 * @see org.eclipse.persistence.config.SessionCustomizer#customize(org.eclipse.persistence.sessions.Session)
	 */
	public void customize(Session session) throws Exception {
		// define the Command Manager
        RemoteCommandManager commandMgr = new RemoteCommandManager((CommandProcessor) session);
        
        // define the JMS transport
        JMSTopicTransportManager transportMgr = new JMSTopicTransportManager(commandMgr);
        
        StringBuilder hostUrl = new StringBuilder("t3://");
        String host = InetAddress.getLocalHost().getHostName();
        String port = String.valueOf(getManagedServerPort());
        //String port = "8002";
        hostUrl.append(host).append(":").append(port);
        
        transportMgr.setTopicHostUrl(hostUrl.toString());
        
        transportMgr.setTopicConnectionFactoryName("jms/WSRPTopicConnectionFactory");
        transportMgr.setTopicName("jms/EclipseLinkCacheTopic");

		// Slawek's note:
		// Previously this class referenced PropertyResourceBundleManager class defined in
		// spf-common-utilities module. Unfortunately the resulting dependency required
		// spf-common-utilities to be part of the system classpath this class must be in.
		// This prevented portlet applications to use different versions of spf-common-utilities
		// than the one referenced in the system classpath.
		// The drawback of this change though is that PropertyResourceBundleManager was able
		// to automatically detect changes of the underlying file and re-load it appropriately.
		// Now such a change would require the server restart - let's hope that does not happen too often.
        ResourceBundle rb = ResourceBundle.getBundle("ContainerJMSConfig");
        
        transportMgr.setUserName(rb.getString("UserName"));
        transportMgr.setPassword(rb.getString("Password"));

        // Initial context factory name is default to 
        // com.evermind.server.rmi.RMIInitialContextFactory for OracleAS
        transportMgr.setInitialContextFactoryName("weblogic.jndi.WLInitialContextFactory");

        // hook up the components
        commandMgr.setTransportManager(transportMgr);
        ((DatabaseSession)session).setCommandManager(commandMgr);
        
        // turn on distributed cache synchronization with RCM
        ((DatabaseSession)session).setShouldPropagateChanges(true);        

        // Start joining the cluster
        if(session.isConnected ()) {
        	((DatabaseSession)session).getCommandManager().initialize();
        } else {
        	((DatabaseSession)session).login();
        }
	}

	/**
	 * Retrieves this managed server's port number (ListenPort).
	 *
	 * <p><b>Note:</b>
	 * The implementation of this method duplicates some code from <code>com.hp.it.spf.xa.misc.Environment</code>
	 * class. The reason for this duplication is to avoid dependency on <code>spf-common-utilities</code>
	 * hosting that class. Otherwise <code>spf-common-utilities</code> would have to be part of
	 * system classpath as this class is and this would prevent portlet applications to bundle
	 * a different version of this library than the one referenced in the classpath.
	 * </p>
	 *
	 * @return  server's ListenPort
	 * @throws Exception If an error occurs when WebLogic JMX objects are queries to retrieve
	 * server listen port
	 */
	private synchronized int getManagedServerPort() throws Exception
	{
		if (mManagedServerPort == -1) {
			ObjectName runtimeService =
					new ObjectName("com.bea:Name=RuntimeService,Type=weblogic.management.mbeanservers.runtime.RuntimeServiceMBean");
			InitialContext ctx = new InitialContext();
			MBeanServer mbeanServer = (MBeanServer) ctx.lookup("java:comp/env/jmx/runtime");
			ObjectName mServerRuntime = (ObjectName) mbeanServer.getAttribute(runtimeService, "ServerRuntime");
			mManagedServerPort = ((Integer) mbeanServer.getAttribute(mServerRuntime, "ListenPort")).intValue();
		}

		return mManagedServerPort;
	}
}
