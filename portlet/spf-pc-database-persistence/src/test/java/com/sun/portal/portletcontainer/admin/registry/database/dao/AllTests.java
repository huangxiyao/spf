package com.sun.portal.portletcontainer.admin.registry.database.dao;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for com.sun.portal.portletcontainer.admin.registry.dao");
		//$JUnit-BEGIN$
		suite.addTestSuite(PortletWindowRegistryTest.class);
		suite.addTestSuite(PortletAppRegistryTest.class);
		suite.addTestSuite(PortletWindowPreferenceRegistryTest.class);
		//$JUnit-END$
		return suite;
	}

}
