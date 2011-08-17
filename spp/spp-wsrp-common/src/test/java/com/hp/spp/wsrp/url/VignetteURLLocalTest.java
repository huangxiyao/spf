package com.hp.spp.wsrp.url;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Junit Class for VignetteURLLocal.
 * 
 * @author mathieu.vidal@hp.com
 */
public class VignetteURLLocalTest extends TestCase {
	/**
	 * Constructor of the test.
	 * 
	 * @param arg0 the name of the test
	 */
	public VignetteURLLocalTest(String arg0) {
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

		suite.addTest(new VignetteURLLocalTest("composeURLDoc"));
		suite.addTest(new VignetteURLLocalTest("composeURLUniqueParameter"));
		suite.addTest(new VignetteURLLocalTest("composeURLTwoParameter"));
		suite.addTest(new VignetteURLLocalTest("composeURLParameterMultiValueHash"));
		suite.addTest(new VignetteURLLocalTest("composeURLParameterMultiValueArray"));
		
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
		VignetteLocalURL url = new VignetteLocalURL(new PageIdBaseUrlComposer(site, pageId));
		url.setParameter(portletId, "doc", docId);
		assertEquals(
			"Wrong construction of the url",
			"http://titi/site1/template.PAGE/menuitem.666/?javax.portlet.tpst=777"
			+ "&javax.portlet.prp_777_doc=888",
			url.urlToString());
	}

	/**
	 * Test the construction of the url when the unique parameter is defined.
	 * 
	 * @throws Exception to make sure that each unexpected Exception will be noticed
	 */
	public void composeURLUniqueParameter() throws Exception {
		String site = "http://titi/site1";
		String pageId = "666";
		String portletId = "777";
		Map parameters = new HashMap();
		parameters.put("key1", "value1");
		VignetteLocalURL url = new VignetteLocalURL(new PageIdBaseUrlComposer(site, pageId));
		url.setParameter(portletId, parameters);
		assertEquals(
			"Wrong construction of the url",
			"http://titi/site1/template.PAGE/menuitem.666/?javax.portlet.tpst=777"
			+ "&javax.portlet.prp_777_key1=value1",
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
		VignetteLocalURL url = new VignetteLocalURL(new PageIdBaseUrlComposer(site, pageId));
		url.setParameter(portletId, parameters);
		assertEquals(
			"Wrong construction of the url",
			"http://titi/site1/template.PAGE/menuitem.666/?javax.portlet.tpst=777"
			+ "&javax.portlet.prp_777_key1=value1&javax.portlet.prp_777_key2=value2",
			url.urlToString());
	}

	/**
	 * Test the construction of the url with a multivalued parameter stored in hashmap.
	 * 
	 * @throws Exception to make sure that each unexpected Exception will be noticed
	 */
	public void composeURLParameterMultiValueHash() throws Exception {
		String site = "http://titi/site1";
		String pageId = "666";
		String portletId = "777";
		Map parameters = new HashMap();
		String[] values = {"value1", "value2"};
		parameters.put("key1", values);
		VignetteLocalURL url = new VignetteLocalURL(new PageIdBaseUrlComposer(site, pageId));
		url.setParameter(portletId, parameters);
		assertEquals(
			"Wrong construction of the url",
			"http://titi/site1/template.PAGE/menuitem.666/?javax.portlet.tpst=777"
			+ "&javax.portlet.prp_777_key1=value1&javax.portlet.prp_777_key1=value2",
			url.urlToString());
	}

	/**
	 * Test the construction of the url with a multivalued parameter stored in array.
	 * 
	 * @throws Exception to make sure that each unexpected Exception will be noticed
	 */
	public void composeURLParameterMultiValueArray() throws Exception {
		String site = "http://titi/site1";
		String pageId = "666";
		String portletId = "777";
		String nameParameter = "key1";
		String[] values = {"value1", "value2"};
		VignetteLocalURL url = new VignetteLocalURL(new PageIdBaseUrlComposer(site, pageId));
		url.setParameter(portletId, nameParameter, values);
		assertEquals(
			"Wrong construction of the url",
			"http://titi/site1/template.PAGE/menuitem.666/?javax.portlet.tpst=777"
			+ "&javax.portlet.prp_777_key1=value1&javax.portlet.prp_777_key1=value2",
			url.urlToString());
	}
}
