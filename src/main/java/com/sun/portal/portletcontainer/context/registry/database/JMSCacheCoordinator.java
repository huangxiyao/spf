package com.sun.portal.portletcontainer.context.registry.database;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.DatabaseSession;
import org.eclipse.persistence.sessions.Session;
import org.eclipse.persistence.sessions.coordination.CommandProcessor;
import org.eclipse.persistence.sessions.coordination.RemoteCommandManager;
import org.eclipse.persistence.sessions.coordination.jms.JMSTopicTransportManager;

public class JMSCacheCoordinator implements SessionCustomizer{

	public void customize(Session session) throws Exception {
		// define the Command Manager
        RemoteCommandManager commandMgr = new RemoteCommandManager((CommandProcessor) session);
        
        // define the JMS transport
        JMSTopicTransportManager transportMgr = new JMSTopicTransportManager(commandMgr);
        transportMgr.setTopicHostUrl("t3://localhost:8001");
        transportMgr.setTopicConnectionFactoryName("jms/WSRPTopicConnectionFactory");
        transportMgr.setTopicName("jms/EclipseLinkCacheTopic");
        transportMgr.setUserName("weblogic");
        //transportMgr.setEncryptedPassword("ak#dkkf%ls");
        // or use the unencrypted password
        transportMgr.setPassword("weblogic");

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
