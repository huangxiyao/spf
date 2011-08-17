package com.hp.spp.groups.expressions;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.hp.spp.groups.expression.Equality;

/**
 * JUnit tests for the com.hp.spp.groups.expressions.Equality class.
 * @author PBRETHER
 *
 */
public class EqualityTest extends TestCase {

	/**
	 * Tests the isTrue method.
	 * 
	 * @throws Exception Unexpected exception to be handled by JUnit
	 */
	public void testIsTrue() throws Exception {
		// initialization

		// create map
		Map valuesMap1 = new HashMap();
		valuesMap1.put("attribute1", "value1");

		Map valuesMap2 = new HashMap();
		valuesMap2.put("attribute1", "value2");

		// create Equality expressions
		Equality expression1 = createEqualityExpression("attribute1", "value1");
		Equality expression2 = createEqualityExpression("attribute1", "value2");
		Equality expression3 = createEqualityExpression("attribute2", "value2");

		// assertions
		assertTrue("If the attribute1 is present and its value is value1 " +
				" isTrue should return true", 
				expression1.isTrue(valuesMap1, null));
		assertFalse("If the attribute1 is present and its value is value2 " +
				" isTrue should return false", 
				expression2.isTrue(valuesMap1, null));
		assertFalse("If the attribute1 is present and its value is value2 " +
				" isTrue should return false", 
				expression1.isTrue(valuesMap2, null));
		boolean exceptionThrown = false;
		try {
			expression3.isTrue(valuesMap1, null);
		}
		catch(IllegalArgumentException e)	{
			exceptionThrown = true;
		}
		assertTrue("If the attribute2 is not present " +
				" an illegal argument exception should be thrown", exceptionThrown);
	}
	
	
	/**
	 * Tests the isTrue for attributes that have a null value.
	 * 
	 * @throws Exception Unexpected exception to be handled by JUnit
	 */
	
	public void testNullAttributeValue() throws Exception {
		// initialization

		// create map
		Map valuesMap1 = new HashMap ();
		//Profile has an attribute
		valuesMap1.put("attribute1", "2356789789");

		// create Equality expressions, with null value
		Equality expression1 = createEqualityExpression("attribute1", null);
	
		// assertions
		assertFalse("If the attribute has null value and profile attrib has a valid value" +
				" isTrue should return false", 
				expression1.isTrue(valuesMap1, null));
	
	}
	
	public void testNullProfileAndAttributeValue() throws Exception {
		// initialization

		// create map
		Map profile = new HashMap ();
		//Profile has an attribute
		profile.put("attribute1", null);

		// create Equality expressions, with null value
		Equality expression1 = createEqualityExpression("attribute1", null);
	
		// assertions
		assertTrue("If the attribute and profile attribute have null values " +
				" isTrue should return true", 
				expression1.isTrue(profile, null));
	
	}
	
	
	/**
	 * Tests the isTrue for attributes that have a empty value.
	 * 
	 * @throws Exception Unexpected exception to be handled by JUnit
	 */
	
	public void testEmptyValue() throws Exception {
		// initialization

		// create map
		Map valuesMap1 = new HashMap ();
		//Profile has an attribute
		valuesMap1.put("attribute1", "2356789789");

		// create Equality expressions, with empty value
		Equality expression1 = createEqualityExpression("attribute1", "");
		
		// assertions
		assertFalse("If the attribute1 is present and its value is value1 " +
				" isTrue should return true", 
				expression1.isTrue(valuesMap1, null));
	}	
	
	
	
	
	/**
	 * Creates an equality expression from the attribute and the value passed.
	 * @param attribute Attribute whose value is compared
	 * @param value Value compared
	 * @return Equality expression
	 */
	private Equality createEqualityExpression(String attribute, String value) {
		Equality expression2 = new Equality();
		expression2.setAttributeName(attribute);
		expression2.setAttributeValue(value);
		return expression2;
	}

	/**
	 * To keep if you want to do something particular during the initialization.
	 * 
	 * @see com.hp.spp.AbstractSPPTestCase.setUp
	 * @throws Exception throw each Exception to make sure that the test fails for all
	 *         unexpected Exception.
	 */
	protected void setUp() throws Exception {
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
