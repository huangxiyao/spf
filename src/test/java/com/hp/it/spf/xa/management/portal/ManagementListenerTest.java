package com.hp.it.spf.xa.management.portal;

import java.rmi.RemoteException;
import java.util.Iterator;


import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.core.IsNull.*;
import static org.hamcrest.core.Is.*;

public class ManagementListenerTest {

	@Before
	public void setUp() throws Exception {
		for (Iterator it = System.getProperties().keySet().iterator(); it.hasNext();) {
			String propertyName = (String) it.next();
			if (propertyName.startsWith(ManagementListener.PROP_NAME_PREFIX)) {
				it.remove();
			}
		}
		ManagementListener.LOG = new NullLogWrapper(ManagementListener.class);
	}


	@Test
	public void testGetProperty() throws Exception {
		ManagementListener listener = new ManagementListener(null);

		assertThat("Undefined property's value",
				listener.getProperty("non_existing"), nullValue());

		String propertyName = ManagementListener.PROP_NAME_PREFIX + "test";
		System.setProperty(propertyName, "abc");
		assertThat("Value is read from system property",
				listener.getProperty(propertyName), is("abc"));

		listener = new ManagementListener("test_spfmanagement");
		assertThat("Value read from system property even though defined in file",
				listener.getProperty(propertyName), is("abc"));

		System.getProperties().remove(propertyName);
		assertThat("Value read from file if no corresponding system property defined",
				listener.getProperty(propertyName), is("xyz"));
	}

	@Test
	public void testGetManagementPort() throws Exception {
		ManagementListener listener = new ManagementListener(null);
		
		assertEquals("-1 returned if port system property not defined", 
				-1, listener.getManagementPort());
		
		System.setProperty(ManagementListener.MANAGEMENT_PORT_PROP_NAME, "abc");
		assertEquals("-1 returned if port is invalid", 
				-1, listener.getManagementPort());
		
		System.setProperty(ManagementListener.MANAGEMENT_PORT_PROP_NAME, "1234");
		assertEquals("Management port read from system property", 
				1234, listener.getManagementPort());
	}
	             
	@Test
	public void testGetUsePlatformMBeanServerFlag() throws Exception {
		ManagementListener listener = new ManagementListener(null);

		assertThat("Use platform MBeanServer flag is false if undefined",
				listener.getUsePlatformMBeanServerFlag(), is(false));

		System.setProperty(ManagementListener.USE_PLATFORM_MBREAN_SERVER_PROP_NAME, "");
		assertThat("Use platform MBeanServer flag is false if empty",
				listener.getUsePlatformMBeanServerFlag(), is(false));

		System.setProperty(ManagementListener.USE_PLATFORM_MBREAN_SERVER_PROP_NAME, "xyz");
		assertThat("Use platform MBeanServer flag is false is incorrect",
				listener.getUsePlatformMBeanServerFlag(), is(false));

		System.setProperty(ManagementListener.USE_PLATFORM_MBREAN_SERVER_PROP_NAME, "true");
		assertThat("Use platform MBeanServer flag is true is correctly specified",
				listener.getUsePlatformMBeanServerFlag(), is(true));
	}

	@Test
	public void testGetRealmName() throws Exception {
		ManagementListener listener = new ManagementListener(null);

		assertThat("Realm name is null if undefined",
				listener.getRealmName(), nullValue());

		System.setProperty(ManagementListener.REALM_PROP_NAME, "");
		assertThat("Realm name is null if defined as empty",
				listener.getRealmName(), nullValue());

		System.setProperty(ManagementListener.REALM_PROP_NAME, "abc");
		assertThat("Realm name is not null if defined correctly",
				listener.getRealmName(), is("abc"));
	}
	
	@Test
	public void testManagementFeaturesNotAvailableIfPortNotDefined() throws Exception {
		ManagementListener listener = new ManagementListener();
		listener.contextInitialized(null);
		assertTrue("Management features not available", !listener.managementFeaturesAavailable());
	}

	@Test
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
