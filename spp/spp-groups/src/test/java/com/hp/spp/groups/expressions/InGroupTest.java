package com.hp.spp.groups.expressions;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;

import com.hp.spp.groups.Group;
import com.hp.spp.groups.Site;
import com.hp.spp.groups.dao.SiteDAOCacheImpl;
import com.hp.spp.groups.expression.Equality;
import com.hp.spp.groups.expression.InGroup;

/**
 * JUnit tests for the com.hp.spp.groups.expressions.InGroup class.
 * @author PBRETHER
 *
 */
public class InGroupTest extends TestCase {
	
	Site mSite;

	/**
	 * Tests the isTrue method.
	 * 
	 * @throws Exception Unexpected exception to be handled by JUnit
	 */
	public void testIsInGroup() throws Exception {
		// initialization
		InGroup expression = new InGroup();
		expression.setValue("myGroup");
		Map validContext = new HashMap();
		validContext.put("attribute1", "value1");
		Map invalidContext = new HashMap();
		invalidContext.put("attribute1", "value2");
		
		// assertions
		assertTrue("With attribute1=value1 should return true", 
				expression.isTrue(validContext, "mySite"));
		assertFalse("With attribute1=value2 should return false", 
				expression.isTrue(invalidContext, "mySite"));
	}

	/**
	 * To keep if you want to do something particular during the initialization.
	 * 
	 * @throws Exception throw each Exception to make sure that the test fails for all
	 *         unexpected Exception.
	 */
	protected void setUp() throws Exception {
		mSite = new Site();
		mSite.setName("mySite");
		Group group = new Group();
		group.setName("myGroup");
		Equality simpleExpression = new Equality();
		simpleExpression.setAttributeName("attribute1");
		simpleExpression.setAttributeValue("value1");
		group.setExpression(simpleExpression);
		mSite.addGroup(group);
		SiteDAOCacheImpl.getInstance().putInCache(mSite);
	}

	/**
	 * To keep if you want to do something particular after the test or suite test was runned.
	 * 
	 * @throws Exception throw each Exception to make sure that the test fails for all
	 *         unexpected Exception.
	 */
	protected void tearDown() throws Exception {
	}
}
