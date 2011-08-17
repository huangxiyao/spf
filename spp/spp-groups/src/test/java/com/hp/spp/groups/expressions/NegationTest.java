package com.hp.spp.groups.expressions;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;

import com.hp.spp.groups.expression.Equality;
import com.hp.spp.groups.expression.Negation;

/**
 * JUnit tests for the com.hp.spp.groups.expressions.Negation class.
 * @author PBRETHER
 *
 */
public class NegationTest extends TestCase {

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

		// create Equality expressions
		Negation expression1 = new Negation();
		expression1.setExpression(createEqualityExpression("attribute1", "value1"));
		Negation expression2 = new Negation();
		expression2.setExpression(createEqualityExpression("attribute1", "value2"));

		// assertions
		assertFalse("If the expression returns true, then the negation should return false", 
				expression1.isTrue(valuesMap1, null));
		assertTrue("If the expression returns false, then the negation should return true", 
				expression2.isTrue(valuesMap1, null));
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
	 * @throws Exception throw each Exception to make sure that the test fails for all
	 *         unexpected Exception.
	 */
	protected void setUp() throws Exception {
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
