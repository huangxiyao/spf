package com.hp.spp.groups;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import junit.framework.TestCase;

import com.hp.spp.groups.dao.SiteDAOCacheImpl;

/**
 * JUnit test for the com.hp.spp.groups.GroupManager class.
 * 
 * @author PBRETHER
 * 
 */
public class GroupManagerTest extends TestCase {

	private Site mSite;
	private Group mValidGroup;
	private Group mAnotherValidGroup;
	private Group mInvalidGroup;

	/**
	 * Tests the getAvailableGroups method.
	 * 
	 * @throws Exception Unexpected exception to be caught by JUnit
	 */
	public void testAvailableGroups() throws Exception {

		// initialization
		List availableGroupsList = GroupManager.getAvailableGroups("mySite",
				getBundleAsMap("contexts/valid"));

		// assertions
		assertEquals("There should be two groups available", 
				availableGroupsList.size(), 2);
		assertTrue("The valid group should be available",
				availableGroupsList.contains(mValidGroup.getName()));
		assertTrue("The other valid group should be available",
				availableGroupsList.contains(mAnotherValidGroup.getName()));
		assertFalse("The invalid group should not be available",
				availableGroupsList.contains(mInvalidGroup.getName()));
	}

	/**
	 * Creates a Mock Blob object from the file given.
	 * 
	 * @param fileName name of the file containing the XML
	 * @return byte[] object containing XML in file
	 * @throws IOException exception retrieving the file
	 */
	private byte[] getXmlFromFile(String fileName) throws IOException {

		StringBuffer xml = new StringBuffer();
		if (fileName != null) {
			URL xmlUrl = getClass().getClassLoader().getResource(fileName);
			BufferedReader reader = new BufferedReader(new FileReader(xmlUrl.getFile()));
			String line = reader.readLine();
			while (line != null) {
				xml.append(line);
				line = reader.readLine();
			}
		}
		return xml.toString().getBytes();
	}

	/**
	 * Creates a map from the bundle that is named.
	 * @param bundleName Name of the bundle
	 * @return Map of key/value pairs from the bundle
	 */
	private Map getBundleAsMap(String bundleName) {
		Map context = new HashMap();

		ResourceBundle bundle = 
			ResourceBundle.getBundle(bundleName);

		Enumeration enumerator = bundle.getKeys();
		while (enumerator.hasMoreElements()) {
			String key = (String) enumerator.nextElement();
			context.put(key, bundle.getObject(key));
		}
		
		return context;
	}

	/**
	 * To keep if you want to do something particular during the initialization.
	 * 
	 * @see com.hp.spp.AbstractSPPTestCase.setUp
	 * @throws Exception throw each Exception to make sure that the test fails for all
	 *         unexpected Exception.
	 */
	protected void setUp() throws Exception {
		mValidGroup = new Group();
		mValidGroup.setRules(getXmlFromFile("rules/valid_group.xml"));
		mValidGroup.setName("GENERIC_GROUP");
		mAnotherValidGroup = new Group();
		mAnotherValidGroup.setRules(getXmlFromFile("rules/alt_valid_group.xml"));
		mAnotherValidGroup.setName("ALT_GENERIC_GROUP");
		mInvalidGroup = new Group();
		mInvalidGroup.setRules(getXmlFromFile("rules/invalid_group.xml"));
		mInvalidGroup.setName("INVALID_GROUP");
		mSite = new Site();
		mSite.setName("mySite");
		HashSet groups = new HashSet();
		groups.add(mAnotherValidGroup);
		groups.add(mValidGroup);
		groups.add(mInvalidGroup);
		mSite.setGroupList(groups);
		SiteDAOCacheImpl.getInstance().putInCache(mSite);
	}

	/**
	 * To keep if you want to do something particular after the test or suite test was runned.
	 * 
	 * @see com.hp.spp.AbstractSPPTestCase.tearDown
	 * @throws Exception throw each Exception to make sure that the test fails for all
	 *         unexpected Exception.
	 */
	protected void tearDown() throws Exception {
	}
}
