package com.hp.spp.eservice.el;

import junit.framework.TestCase;

public class SPPFunctionsTest extends TestCase  {

	public void testReplaceDelimiterValidData() throws Exception {
		String result = SPPFunctions.replaceDelimiter("A;B;C;D;E", "$");
		
		assertEquals("Result of evaluation" + result, "A$B$C$D$E", result);
	}
	
	
	public void testReplaceDelimiterInValidDelim() throws Exception {
		String result = SPPFunctions.replaceDelimiter("A;B;C;D;E", null);
		
		assertNull("Result of evaluation", result);
	}
	
	public void testReplaceDelimiterInValidStr() throws Exception {
		String result = SPPFunctions.replaceDelimiter(null, ";");
		
		assertNull("Result of evaluation", result);
	}
	
	public void testFilterMultiValueAttributeFn() throws Exception {
		
		String multiValueString = "SPP_Group_ex1;SPP_Group_ex2;LOCAL_ADMIN;GPP_NA_USERS;GPP_EMEA_USERS";
		String patternString = "*USERS;LOCAL_ADMIN";
		String expectedResult = "GPP_NA_USERS;GPP_EMEA_USERS;LOCAL_ADMIN";
		String actualResult = SPPFunctions.filterMultiValueAttribute(multiValueString, patternString);
		
		assertTrue("Filter does not match as expected", expectedResult.equals(actualResult));
	}

}
