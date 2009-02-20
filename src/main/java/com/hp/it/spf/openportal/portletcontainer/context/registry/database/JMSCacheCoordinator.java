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


public class JMSCacheCoordinator implements SessionCustomizer{

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
