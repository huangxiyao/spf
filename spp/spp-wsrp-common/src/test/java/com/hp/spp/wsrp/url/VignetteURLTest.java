package com.hp.spp.wsrp.url;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Junit Class for VignetteURL.
 * 
 * @author mathieu.vidal@hp.com
 */
public class VignetteURLTest extends TestCase {
	/**
	 * Constructor of the test.
	 * 
	 * @param arg0 the name of the test
	 */
	public VignetteURLTest(String arg0) {
		super(arg0);
	}

	/**
	 * The suite of test to run.
	 * 
	 * @return the test suite
	 * @throws Exception to make sure that each unexpected Exception will be noticed
	 */
	public static Test suite() throws Exception {
		TestSuite suite = new TestSuite();

		suite.addTest(new VignetteURLTest("composeRootUrl"));

		return suite;
	}

	/**
	 * Test the construction of the url when the unique parameter is the doc id.
	 * 
	 * @throws Exception to make sure that each unexpected Exception will be noticed
	 */
	public void composeRootUrl() throws Exception {
		String site = "http://titi/site1";
		String pageId = "666";
		String url = VignetteURL.composeRootUrl(site, pageId);
		assertEquals("Wrong construction of the root url",
				"http://titi/site1/template.PAGE/menuitem.666", url);
	}
}
