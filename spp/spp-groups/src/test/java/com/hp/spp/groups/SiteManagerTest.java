package com.hp.spp.groups;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Test;
import junit.framework.TestCase;

import org.w3c.dom.Document;

import com.hp.spp.groups.exception.XmlImportException;

public class SiteManagerTest extends TestCase {

	private Document mValidSiteDocument = null;

	private Document mInValidSiteDocument = null;

	private Site mSite = null;

	private byte[] mValidByteArray = null;

	private byte[] mInValidByteArray = null;

	private byte[] mInValidByteArray2 = null;

	private byte[] mMultipleGroupByteArray = null;

	private byte[] mWrongGroupReferenceByteArray = null;

	private byte[] mGroupCircularReferenceByteArray = null;

	private byte[] mInvalidAttributeByteArray = null;
	
	private Map mUserProfile;

	private byte[] mInvalidAttributesByteArray = null;

	public void testValidSite() throws Exception {

		SiteManager siteManager = new SiteManager(mSite);
		String eXceptionMessage = null;
		List results = null;
		try {
			results = siteManager.loadFromXml(mValidSiteDocument);
		} catch (XmlImportException e) {
			eXceptionMessage = e.getMessage();
		}
		assertNotSame("Exception message comparison", eXceptionMessage,
				"Site name in the Xml file is different than current site");
		assertEquals("Map Size comparison", results.size(), 3);

		GroupStatus status = (GroupStatus) results.get(0);
		assertEquals("Compare status for GENERIC_GROUP", status.getExistingFlag(),
				GroupStatus.EXISTING_GROUP);
		status = (GroupStatus) results.get(1);
		assertEquals("Compare status for PARTNER_GROUP_TEST", status.getExistingFlag(),
				GroupStatus.NEW_GROUP);
		status = (GroupStatus) results.get(2);
		assertEquals("Compare status for GENERIC_GROUP_TEST2", status.getExistingFlag(),
				GroupStatus.NEW_GROUP);
	}

	public void testWrongSite() throws Exception {

		SiteManager siteManager = new SiteManager(mSite);
		String eXceptionMessage = null;
		try {
			siteManager.loadFromXml(mInValidSiteDocument);
		} catch (XmlImportException e) {
			eXceptionMessage = e.getMessage();
		}
		assertEquals("Exception message comparison",
				"Site name in the Xml file is different than current site", eXceptionMessage);

	}

	public void testValidXsd() throws Exception {
		SiteManager siteManager = new SiteManager(mSite);
		String eXceptionMessage = null;
		List results = null;
		try {
			results = siteManager.loadFromByteArray(mUserProfile, mValidByteArray);
		} catch (XmlImportException e) {
			eXceptionMessage = e.getMessage();
		}
		assertNotSame("Exception message comparison",
				"Site name in the Xml file is different than current site", eXceptionMessage);
		assertEquals("Map Size comparison", results.size(), 3);

		GroupStatus status = (GroupStatus) results.get(0);
		assertEquals("Compare status for GENERIC_GROUP", status.getExistingFlag(),
				GroupStatus.EXISTING_GROUP);
		status = (GroupStatus) results.get(1);
		assertEquals("Compare status for PARTNER_GROUP_TEST", status.getExistingFlag(),
				GroupStatus.NEW_GROUP);
		status = (GroupStatus) results.get(2);
		assertEquals("Compare status for GENERIC_GROUP_TEST2", status.getExistingFlag(),
				GroupStatus.NEW_GROUP);

	}

	public void testInValidXsd() throws Exception {
		SiteManager siteManager = new SiteManager(mSite);
		String eXceptionMessage = null;
		try {
			siteManager.loadFromByteArray(mUserProfile, mInValidByteArray);
		} catch (Exception e) {
			eXceptionMessage = e.getMessage();
		}
		assertNotNull("Exception message is not null", eXceptionMessage);
		assertEquals("Compare Exception message", "Problem during Xml File parsing",
				eXceptionMessage);
	}

	public void testOnlyOneTopLevelExpressionAllowedForGroup() throws Exception {
		SiteManager siteManager = new SiteManager(mSite);
		String exceptionMessage = null;
		try {
			siteManager.loadFromByteArray(mUserProfile, mInValidByteArray2);
		}
		catch (Exception e) {
			exceptionMessage = e.getMessage();
		}
		assertNotNull("Exception message is not null", exceptionMessage);
		assertEquals("Compare Exception message", "Problem during Xml File parsing",
				exceptionMessage);
	}

	public void testInvalidAttribute() throws Exception {
		SiteManager siteManager = new SiteManager(mSite);
		String eXceptionMessage = null;
		try {
			siteManager.loadFromByteArray(mUserProfile, mInvalidAttributeByteArray);
		} catch (Exception e) {
			eXceptionMessage = e.getMessage();
		}
		assertNotNull("Exception message is not null", eXceptionMessage);
		assertEquals("Compare Exception message", "The following attribute(s) "
				+ "do(es) not exist in the user profile: [userstatus]", eXceptionMessage);
	}

	public void testInvalidAttributes() throws Exception {
		SiteManager siteManager = new SiteManager(mSite);
		String eXceptionMessage = null;
		try {
			siteManager.loadFromByteArray(mUserProfile, mInvalidAttributesByteArray);
		} catch (Exception e) {
			eXceptionMessage = e.getMessage();
		}
		assertNotNull("Exception message is not null", eXceptionMessage);
		assertEquals("Compare Exception message", "The following attribute(s) "
				+ "do(es) not exist in the user profile: [userstatus, nonexistentattribute]", eXceptionMessage);
	}

	public void testMultipleGroups() throws Exception {

		SiteManager siteManager = new SiteManager(mSite);
		String eXceptionMessage = null;
		try {
			siteManager.loadFromByteArray(mUserProfile, mMultipleGroupByteArray);
		} catch (XmlImportException e) {
			eXceptionMessage = e.getMessage();
		}
		assertEquals(
				"Exception message comparison",
				eXceptionMessage,
				"- Group [PARTNER_GROUP_TEST] is defined more than once is the XML file - Group [GENERIC_GROUP] is defined more than once is the XML file ");
	}

	public void testWrongInGroupReference() throws Exception {

		SiteManager siteManager = new SiteManager(mSite);
		String eXceptionMessage = null;
		try {
			siteManager.loadFromByteArray(mUserProfile, mWrongGroupReferenceByteArray);
		} catch (XmlImportException e) {
			eXceptionMessage = e.getMessage();
		}
		assertEquals(
				"Exception message comparison",
				eXceptionMessage,
				"- The group [PARTNER_GROUP_TEST_1] reference the group [GENERIC_GROUP_NOT_EXISTS] which does not exist - The group [PARTNER_GROUP_TEST_2] reference the group [ALT_GENERIC_GROUP_NOT_EXISTS_2] which does not exist ");
	}

	public void testGroupCircularReference() throws Exception {

		SiteManager siteManager = new SiteManager(mSite);
		String eXceptionMessage = null;
		try {
			siteManager.loadFromByteArray(mUserProfile, mGroupCircularReferenceByteArray);
		} catch (XmlImportException e) {
			eXceptionMessage = e.getMessage();
		}
		assertEquals(
				"Exception message comparison",
				eXceptionMessage,
				"- The group [CIRC_GROUP_2] contains a circular reference [CIRC_GROUP_2, CIRC_GROUP_3, CIRC_GROUP_1, CIRC_GROUP_2] ");
	}

	/**
	 * To keep if you want to do something particular during the initialization.
	 * 
	 * @see com.hp.spp.AbstractSPPTestCase.setUp
	 * @throws Exception throw each Exception to make sure that the test fails for all
	 *         unexpected Exception.
	 */
	protected void setUp() throws Exception {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

		URL xmlUrl = getClass().getClassLoader().getResource("rules/valid_site.xml");
		mValidSiteDocument = docBuilder.parse(xmlUrl.getFile());

		xmlUrl = getClass().getClassLoader().getResource("rules/invalid_site.xml");
		mInValidSiteDocument = docBuilder.parse(xmlUrl.getFile());

		xmlUrl = getClass().getClassLoader().getResource("rules/valid_site.xml");
		mValidByteArray = fileToByteArray(new File(xmlUrl.getFile()));

		xmlUrl = getClass().getClassLoader().getResource("rules/invalid_xsd.xml");
		mInValidByteArray = fileToByteArray(new File(xmlUrl.getFile()));

		xmlUrl = getClass().getClassLoader().getResource("rules/invalid_xsd2.xml");
		mInValidByteArray2 = fileToByteArray(new File(xmlUrl.getFile()));

		xmlUrl = getClass().getClassLoader().getResource("rules/invalid_attribute.xml");
		mInvalidAttributeByteArray  = fileToByteArray(new File(xmlUrl.getFile()));

		xmlUrl = getClass().getClassLoader().getResource("rules/invalid_attributes.xml");
		mInvalidAttributesByteArray   = fileToByteArray(new File(xmlUrl.getFile()));

		xmlUrl = getClass().getClassLoader().getResource("rules/multiple_groups.xml");
		mMultipleGroupByteArray = fileToByteArray(new File(xmlUrl.getFile()));

		xmlUrl = getClass().getClassLoader().getResource("rules/inGroup_wrong_reference.xml");
		mWrongGroupReferenceByteArray = fileToByteArray(new File(xmlUrl.getFile()));

		xmlUrl = getClass().getClassLoader().getResource(
				"rules/inGroup_circular_reference.xml");
		mGroupCircularReferenceByteArray = fileToByteArray(new File(xmlUrl.getFile()));

		mSite = new Site();
		mSite.setName("Smart Portal");
		Set groups = new HashSet();

		Group group1 = new Group();
		group1.setId(1);
		group1.setName("GENERIC_GROUP");
		groups.add(group1);
		Group group2 = new Group();
		group2.setId(2);
		group2.setName("GENERIC_ADMIN");
		groups.add(group2);
		mSite.setGroupList(groups);
		
		mUserProfile = getUserProfile();

	}

	/**
	 * To keep if you want to do something particular after the test or suite test was runned.
	 * 
	 * @throws Exception throw each Exception to make sure that the test fails for all
	 *         unexpected Exception.
	 */
	protected void tearDown() throws Exception {
	}

	private byte[] fileToByteArray(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		// You cannot create an array using a long type.
		// It needs to be an int type.
		// Before converting to an int type, check
		// to ensure that file is not larger than Integer.MAX_VALUE.
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}
		return bytes;
	}

	public HashMap getUserProfile() {

		HashMap profile = new HashMap();

		// these values are for test purposes and do not resemble the real
		// profile values
		profile.put("userStatus", "Active");
		profile.put("userProgramName", "EU-Competitor Supplies");
		profile.put("userCountryCode", "UK");
		profile.put("userRole", "Partner Portal Administrator");
		
		return profile;
	}

}
