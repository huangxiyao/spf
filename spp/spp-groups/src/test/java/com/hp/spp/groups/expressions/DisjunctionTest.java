package com.hp.spp.groups.expressions;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;

import com.hp.spp.groups.expression.Disjunction;
import com.hp.spp.groups.expression.Equality;

/**
 * JUnit tests for the com.hp.spp.groups.expressions.Disjunction class.
 * @author PBRETHER
 *
 */
public class DisjunctionTest extends TestCase {

	private Equality mTrueExpression1;
	private Equality mTrueExpression2;
	private Equality mTrueExpression3;
	private Equality mFalseExpression1;
	private Equality mFalseExpression2;
	
	private Map mValuesMap;
	
	/**
	 * Tests Disjunction with two false expressions
	 * @throws Exception Unexpected exception handled by JUnit
	 */
	public void testTwoFalseExpressions() throws Exception {
		// create expression with two false expressions
		Disjunction twoFalse = new Disjunction();
		twoFalse.addExpression(mFalseExpression1);
		twoFalse.addExpression(mFalseExpression2);
		assertFalse("Two false expression should return false", 
				twoFalse.isTrue(mValuesMap, null));
	}

	/**
	 * Tests Disjunction with a true and a false expression
	 * @throws Exception Unexpected exception handled by JUnit
	 */
	public void testOneTrueOneFalseExpression() throws Exception {
		// create expression with one true and one false expression
		Disjunction trueFalse = new Disjunction();
		trueFalse.addExpression(mTrueExpression1);
		trueFalse.addExpression(mFalseExpression1);
		assertTrue("One true and one false expression should return true",
				trueFalse.isTrue(mValuesMap, null));
	}

	/**
	 * Tests Disjunction with three true expressions
	 * @throws Exception Unexpected exception handled by JUnit
	 */
	public void testThreeTrueExpressions() throws Exception {
		Disjunction threeExTrue = new Disjunction();
		threeExTrue.addExpression(mTrueExpression1);
		threeExTrue.addExpression(mTrueExpression2);
		threeExTrue.addExpression(mTrueExpression3);
		assertTrue("Three true expressions should return true",
				threeExTrue.isTrue(mValuesMap, null));
	}

	/**
	 * Tests Disjunction with two true expressions
	 * @throws Exception Unexpected exception handled by JUnit
	 */
	public void testTwoTrueExpressions() throws Exception {
		// create expression with two true expressions
		Disjunction twoExTrue = new Disjunction();
		twoExTrue.addExpression(mTrueExpression1);
		twoExTrue.addExpression(mTrueExpression2);
		assertTrue("Two true expressions should return true",
				twoExTrue.isTrue(mValuesMap, null));
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
		
		// create map
		mValuesMap = new HashMap();
		mValuesMap.put("attribute1", "value1");
		mValuesMap.put("attribute2", "value2");
		mValuesMap.put("attribute3", "value3");

		// create Equality expressions
		mTrueExpression1 = createEqualityExpression("attribute1", "value1");
		mTrueExpression2 = createEqualityExpression("attribute2", "value2");
		mTrueExpression3 = createEqualityExpression("attribute3", "value3");
		mFalseExpression1 = createEqualityExpression("attribute1", "value2");
		mFalseExpression2 = createEqualityExpression("attribute2", "value1");
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
