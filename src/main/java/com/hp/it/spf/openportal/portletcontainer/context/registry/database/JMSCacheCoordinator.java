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
import com.hp.it.spf.xa.properties.PropertyResourceBundleManager;
import com.hp.it.spf.xa.misc.Environment;

/**
 * This class involves Weblogic JMS to support Eclipselink cache coordination.
 * 
 * @author <link href="ye.liu@hp.com">Liu Ye</link>
 * @version TBD
 */
public class JMSCacheCoordinator implements SessionCustomizer{

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
        String port = String.valueOf(Environment.singletonInstance.getManagedServerPort());
        //String port = "8002";
        hostUrl.append(host).append(":").append(port);
        System.out.println("Set TopicHostUrl = " + hostUrl.toString());
        transportMgr.setTopicHostUrl(hostUrl.toString());
        
        transportMgr.setTopicConnectionFactoryName("jms/WSRPTopicConnectionFactory");
        transportMgr.setTopicName("jms/EclipseLinkCacheTopic");
        
        ResourceBundle rb = PropertyResourceBundleManager.getBundle("ContainerJMSConfig");
        
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
}
