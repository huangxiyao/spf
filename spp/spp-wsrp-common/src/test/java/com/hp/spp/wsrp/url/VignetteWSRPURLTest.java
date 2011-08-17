package com.hp.spp.wsrp.url;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Junit Class for VignetteWSRPURL.
 * 
 * @author mathieu.vidal@hp.com
 */
public class VignetteWSRPURLTest extends TestCase {
	/**
	 * Constructor of the test.
	 * 
	 * @param arg0 the name of the test
	 */
	public VignetteWSRPURLTest(String arg0) {
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

		suite.addTest(new VignetteWSRPURLTest("composeURLDoc"));
		suite.addTest(new VignetteWSRPURLTest("composeURLTwoParameter"));
		suite.addTest(new VignetteWSRPURLTest("composeURLParameterMultiValue"));
		suite.addTest(new VignetteWSRPURLTest("composeActionUrl"));
		
		return suite;
	}

	/**
	 * Test the construction of the url when the unique parameter is the doc id.
	 * 
	 * @throws Exception to make sure that each unexpected Exception will be noticed
	 */
	public void composeURLDoc() throws Exception {
		String site = "http://titi/site1";
		String pageId = "666";
		String portletId = "777";
		String docId = "888";
		VignetteWSRPURL url = new VignetteWSRPURL(new PageIdBaseUrlComposer(site, pageId));
		url.setParameter(portletId, "doc", docId);
		assertEquals(
			"Wrong construction of the url",
			"http://titi/site1/template.PAGE/menuitem.666/?javax.portlet.tpst=777"
			+ "&javax.portlet.prp_777_wsrp-navigationalState="
			+ "rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRo"
			+ "cmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAADZG9jdXIAE1tMamF2YS5sYW5nLlN0cmlu"
			+ "Zzut0lbn6R17RwIAAHhwAAAAAXQAAzg4OHg*"
			+ "&javax.portlet.begCacheTok=com.vignette.cachetoken"
			+ "&javax.portlet.endCacheTok=com.vignette.cachetoken",
			url.urlToString());
	}

	/**
	 * Test the construction of the url with two parameters.
	 * 
	 * @throws Exception to make sure that each unexpected Exception will be noticed
	 */
	public void composeURLTwoParameter() throws Exception {
		String site = "http://titi/site1";
		String pageId = "666";
		String portletId = "777";
		Map parameters = new HashMap();
		parameters.put("key1", "value1");
		parameters.put("key2", "value2");
		VignetteWSRPURL url = new VignetteWSRPURL(new PageIdBaseUrlComposer(site, pageId));
		url.setParameter(portletId, parameters);
		assertEquals(
			"Wrong construction of the url",
			"http://titi/site1/template.PAGE/menuitem.666/?javax.portlet.tpst=777"
			+ "&javax.portlet.prp_777_wsrp-navigationalState="
			+ "rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRo"
			+ "cmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAACdAAEa2V5MXVyABNbTGphdmEubGFuZy5TdHJp"
			+ "bmc7rdJW5-kde0cCAAB4cAAAAAF0AAZ2YWx1ZTF0AARrZXkydXEAfgADAAAAAXQABnZhbHVl"
			+ "Mng*&javax.portlet.begCacheTok=com.vignette.cachetoken"
			+ "&javax.portlet.endCacheTok=com.vignette.cachetoken",
			url.urlToString());
	}

	/**
	 * Test the construction of the url with a multivalued parameter.
	 * 
	 * @throws Exception to make sure that each unexpected Exception will be noticed
	 */
	public void composeURLParameterMultiValue() throws Exception {
		String site = "http://titi/site1";
		String pageId = "666";
		String portletId = "777";
		Map parameters = new HashMap();
		String[] values = {"value1", "value2"};
		parameters.put("key1", values);
		VignetteWSRPURL url = new VignetteWSRPURL(new PageIdBaseUrlComposer(site, pageId));
		url.setParameter(portletId, parameters);
		assertEquals(
			"Wrong construction of the url",
			"http://titi/site1/template.PAGE/menuitem.666/?javax.portlet.tpst=777"
			+ "&javax.portlet.prp_777_wsrp-navigationalState="
			+ "rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRo"
			+ "cmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAAEa2V5MXVyABNbTGphdmEubGFuZy5TdHJp"
			+ "bmc7rdJW5-kde0cCAAB4cAAAAAJ0AAZ2YWx1ZTF0AAZ2YWx1ZTJ4"
			+ "&javax.portlet.begCacheTok=com.vignette.cachetoken"
			+ "&javax.portlet.endCacheTok=com.vignette.cachetoken",
			url.urlToString());
	}
	
	/**
	 * Test the construction of an action URL.
	 * 
	 * @throws Exception to make sure that each unexpected Exception will be noticed
	 */
	public void composeActionUrl() throws Exception {
		String site = "http://titi/site1";
		String pageId = "666";
		String portletId = "777";
		String docId = "888";
		VignetteWSRPURL url = new VignetteWSRPURL(new PageIdActionUrlComposer(site, pageId));
		url.setParameter(portletId, "doc", docId);
		assertEquals(
			"Wrong construction of the url",
			"http://titi/site1/template.PAGE/action.process/menuitem.666/?"
			+ "javax.portlet.action=true"
			+ "&javax.portlet.tpst=777"
			+ "&javax.portlet.prp_777_wsrp-navigationalState="
			+ "rO0ABXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRo"
			+ "cmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAADZG9jdXIAE1tMamF2YS5sYW5nLlN0cmlu"
			+ "Zzut0lbn6R17RwIAAHhwAAAAAXQAAzg4OHg*"
			+ "&javax.portlet.begCacheTok=com.vignette.cachetoken"
			+ "&javax.portlet.endCacheTok=com.vignette.cachetoken",
			url.urlToString());
	}

	
}
