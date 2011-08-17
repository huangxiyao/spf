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
import com.hp.spp.groups.expression.Conjunction;
import com.hp.spp.groups.expression.Disjunction;
import com.hp.spp.groups.expression.Equality;
import com.hp.spp.groups.expression.InGroup;
import com.hp.spp.groups.expression.Negation;

/**
 * JUnit test for the com.hp.spp.groups.Group class.
 * 
 * @author PBRETHER
 * 
 */
public class GroupTest extends TestCase {

	private Group mValidGroup;
	private Site mSite;


	/**
	 * Tests the method unmarshalXml. Test involves checking that all the elements in the
	 * rules.xml test file are properly marshalled into expression objects.
	 * 
	 * @throws Exception Unexpected exception to be caught by JUnit
	 */
	public void testUnmarshallXml() throws Exception {

		// initialization
		Conjunction conjunction = (Conjunction) mValidGroup.getExpression();
		List expressions = conjunction.getExpressions();
		Equality expression0 = (Equality) expressions.get(0);
		Equality expression1 = (Equality) expressions.get(1);
		Disjunction expression2 = (Disjunction) expressions.get(2);
		List orExpressions = expression2.getExpressions();
		Equality orExpression0 = (Equality) orExpressions.get(0);
		Equality orExpression1 = (Equality) orExpressions.get(1);
		Conjunction expression3 = (Conjunction) expressions.get(3);
		List andExpressions = expression3.getExpressions();
		InGroup inGroupExpression = (InGroup) andExpressions.get(0);
		Equality andExpression0 = (Equality) andExpressions.get(1);
		Negation andExpression1 = (Negation) andExpressions.get(2);
		Disjunction negExpression = (Disjunction) andExpression1.getExpression();
		List negOrExpressions = negExpression.getExpressions();
		Equality negOrExpression0 = (Equality) negOrExpressions.get(0);
		Equality negOrExpression1 = (Equality) negOrExpressions.get(1);

		// assertions
		assertNotNull("The conjuction should not be null", 
				conjunction);
		assertEquals("There should be four expressions", 
				4, expressions.size());
		// test expression0
		assertEquals("The attribute name should be 'user status'", 
				"userStatus", expression0.getAttributeName());
		assertEquals("The attribute value should be 'Active'",
				"Active", expression0.getAttributeValue());
		// test expression1
		assertEquals("The attribute name should be 'userProgramName'",
				"userProgramName", expression1.getAttributeName());
		assertEquals("The attibute value should be 'EU-Competitor Supplies'",
				"EU-Competitor Supplies", expression1.getAttributeValue());
		// test expression2
		assertEquals("The attribute name should be 'userCountryCode'",
				"userCountryCode", orExpression0.getAttributeName());
		assertEquals("The attribute value should be 'Switzerland'",
				"Switzerland", orExpression0.getAttributeValue());
		assertEquals("The attribute name should be 'userCountryCode'",
				"userCountryCode", orExpression1.getAttributeName());
		assertEquals("The attribute value should be 'Lichtenstein'",
				"Lichtenstein", orExpression1.getAttributeValue());
		// test expression3
		assertEquals("The value should be 'ALT_GENERIC_GROUP'",
				inGroupExpression.getValue(), "ALT_GENERIC_GROUP");
		assertEquals("The attibute name should be 'userRole'",
				"userRole", andExpression0.getAttributeName());
		assertEquals("The attribute value should be 'Partner Portal Administrator'",
				"Partner Portal Administrator", andExpression0.getAttributeValue());
		assertEquals("The attribute name should be 'userCountryCode'",
				"userCountryCode", negOrExpression0.getAttributeName());
		assertEquals("The attribute value should be 'UK'",
				"UK", negOrExpression0.getAttributeValue());
		assertEquals("The attribute name should be 'userCountryCode'",
				"userCountryCode", negOrExpression1.getAttributeName());
		assertEquals("The attribute value should be 'IRELAND'", 
				"IRELAND", negOrExpression1.getAttributeValue());
	}

	/**
	 * Tests the isMember method.
	 * 
	 * @throws Exception Unexpected exception to be caught by JUnit
	 */
	public void testIsMember() throws Exception {
		// initialization
		Map validContext = getBundleAsMap("contexts/valid");
		Map alternativeValidContext = getBundleAsMap("contexts/alt_valid");
		Map wrongStatus = getBundleAsMap("contexts/wrong_status");
		Map wrongProgram = getBundleAsMap("contexts/wrong_program");
		Map wrongCountry = getBundleAsMap("contexts/wrong_country");
		Map wrongRole = getBundleAsMap("contexts/wrong_role");

		// assertions
		assertTrue("With the valid context the member should be part of the group", 
				mValidGroup.isMember(validContext, "mySite"));
		assertTrue("With country=Lichtenstein the member should also be part of the group",
				mValidGroup.isMember(alternativeValidContext, "mySite"));
		assertFalse("With userStatus=Invalid the user should not be a member",
				mValidGroup.isMember(wrongStatus, "mySite"));
		assertFalse("With userProgramName=US-Competitor Supplies the user should not be a member",
				mValidGroup.isMember(wrongProgram, "mySite"));
		assertFalse("With userCountryCode=UK the user should not be a member",
				mValidGroup.isMember(wrongCountry, "mySite"));
		assertFalse("With userRole=Partner Portal User the user should not be a member",
				mValidGroup.isMember(wrongRole, "mySite"));
	}

	/**
	 * Tests the blob being empty.
	 * 
	 * @throws Exception Unexpected exception to be caught by JUnit
	 */
	public void testEmptyBlob() throws Exception {
		byte[] byteArray = getXmlFromFile(null);
		Group group = new Group();
		group.setRules(byteArray);
		// TODO is returning null and logging sufficient?
		assertNull("If the blob is empty the expression should be null", 
				group.getExpression());
	}

	/**
	 * Tests the XML with a missing beacon.
	 * 
	 * @throws Exception Unexpected exception to be caught by JUnit
	 */
	public void testMissingClosingElementInXml() throws Exception {
		byte[] byteArray = getXmlFromFile("rules/missing_closing_element.xml");
		Group group = new Group();
		group.setRules(byteArray);
		// TODO is returning null and logging sufficient?
		assertNull("If the XML is missing a closing element the expression should be null",
				group.getExpression());
	}

	/**
	 * Tests the XML with an unknown element.
	 * 
	 * @throws Exception Unexpected exception to be caught by JUnit
	 */
	public void testUnknownElementInXml() throws Exception {
		byte[] byteArray = getXmlFromFile("rules/unknown_element.xml");
		Group group = new Group();
		group.setRules(byteArray);
		// TODO is returning null and logging sufficient?
		assertNull("If the XML contains an unknown element the expression should be null",
				group.getExpression());
	}

	/**
	 * Tests the XML with an unknown element.
	 * 
	 * @throws Exception Unexpected exception to be caught by JUnit
	 */
	public void testInvalidRootInXml() throws Exception {
		byte[] byteArray = getXmlFromFile("rules/invalid_root.xml");
		Group group = new Group();
		group.setRules(byteArray);
		// TODO is returning null and logging sufficient?
		assertNull("If the XML has an invalid root the expression should be null",
				group.getExpression());
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
	 * To keep if you want to do something particular during the initialization.
	 * 
	 * @see com.hp.spp.AbstractSPPTestCase.setUp
	 * @throws Exception throw each Exception to make sure that the test fails for all
	 *         unexpected Exception.
	 */
	protected void setUp() throws Exception {
		mValidGroup = new Group();
		mValidGroup.setRules(getXmlFromFile("rules/valid_group.xml"));
		Group anotherValidGroup = new Group();
		anotherValidGroup.setRules(getXmlFromFile("rules/alt_valid_group.xml"));
		anotherValidGroup.setName("ALT_GENERIC_GROUP");
		mSite = new Site();
		mSite.setName("mySite");
		HashSet groups = new HashSet();
		groups.add(anotherValidGroup);
		groups.add(mValidGroup);
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
