package com.hp.it.spf.xa.management.portal;

import java.rmi.RemoteException;
import java.util.Map;

import com.vignette.portal.log.LogWrapper;

import junit.framework.TestCase;

public class ManagementListenerTest extends TestCase {

	@Override
	protected void setUp() throws Exception {
		System.getProperties().remove(ManagementListener.MANAGEMENT_PORT_PROP_NAME);
		LogWrapper nullLog = new LogWrapper(ManagementListener.class) {
			@Override public void debug(Object message, Map map, Throwable throwable) {}
			@Override public void debug(Object message, Map map) {}
			@Override public void debug(Object message, Throwable throwable) { }
			@Override public void debug(Object message) { }
			@Override public void debug(Throwable error) { }
			@Override public void error(Object message, Map map, Throwable throwable) { }
			@Override public void error(Object message, Map map) { }
			@Override public void error(Object message, Throwable throwable) { }
			@Override public void error(Object message) { }
			@Override public void error(Throwable error) { }
			@Override public void fatal(Object message, Map map, Throwable throwable) { }
			@Override public void fatal(Object message, Map map) { }
			@Override public void fatal(Object message, Throwable throwable) { }
			@Override public void fatal(Object message) { }
			@Override public void fatal(Throwable error) { }
			@Override public void info(Object message, Map map, Throwable throwable) { }
			@Override public void info(Object message, Map map) { }
			@Override public void info(Object message, Throwable throwable) { }
			@Override public void info(Object message) { }
			@Override public void info(Throwable error) { }
			@Override public void warning(Object message, Map map, Throwable throwable) { }
			@Override public void warning(Object message, Map map) { }
			@Override public void warning(Object message, Throwable throwable) { }
			@Override public void warning(Object message) { }
			@Override public void warning(Throwable error) { }
			@Override public boolean willLogAtLevel(int level) { return false; }
		};
		ManagementListener.LOG = nullLog;
	}

	
	public void testGetManagementPort() throws Exception {
		ManagementListener listener = new ManagementListener();
		
		assertEquals("-1 returned if port system property not defined", 
				-1, listener.getManagementPort());
		
		System.setProperty(ManagementListener.MANAGEMENT_PORT_PROP_NAME, "abc");
		assertEquals("-1 returned if port is invalid", 
				-1, listener.getManagementPort());
		
		System.setProperty(ManagementListener.MANAGEMENT_PORT_PROP_NAME, "1234");
		assertEquals("Management port read from system property", 
				1234, listener.getManagementPort());
	}
	
	
	public void testManagementFeaturesNotAvailableIfPortNotDefined() throws Exception {
		ManagementListener listener = new ManagementListener();
		listener.contextInitialized(null);
		assertTrue("Management features not available", !listener.managementFeaturesAavailable());
	}
	
	public void testManagementFeaturesNotAvailableIfCannotStartRegistry() throws Exception {
		ManagementListener listener = new ManagementListener() {
			@Override
			void createRMIRegistry(int portNumber) throws RemoteException {
				throw new RemoteException("Test error");
			}
			@Override 
			int getManagementPort() { return 1234; }
		};
		listener.contextInitialized(null);
		assertTrue("Management features not available", !listener.managementFeaturesAavailable());
	}
}
