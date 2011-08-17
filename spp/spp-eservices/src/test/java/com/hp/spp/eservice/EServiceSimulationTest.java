package com.hp.spp.eservice;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.hp.spp.eservice.el.ExpressionResolver;

public class EServiceSimulationTest extends TestCase {


	private EService mValidEservice = null;

	
	private Map mUserProfile = null;
	private Map mSimulatingUserProfile = null;

	
	public void testSimulation() throws Exception {
		EServiceManager esm = new EServiceManager(mValidEservice);
		ExpressionResolver expressionResolver = new ExpressionResolver(mUserProfile);
		esm.setExpressionResolver(expressionResolver);
		assertEquals("check productionUrl", "http://productionURL", esm.getEserviceUrl(null,
				null));

		ExpressionResolver expressionResolverTest = new ExpressionResolver(mUserProfile);
		esm.setExpressionResolver(expressionResolverTest);
		
		Map result = esm.getEserviceParameters();
		assertEquals("Test on NB parameters", 5, result.keySet().size());
		assertEquals("Test on simulatingUserName", "sppTest",result.get("simulatingUsername") );
		assertEquals("Test on simulatingLanguage", "FR", result.get("simulatingLanguage"));
		
	}

	
	
	/**
	 * To keep if you want to do something particular during the initialization.
	 * 
	 * @see com.hp.spp.common.test.AbstractSPPTestCase#setUp
	 * @throws Exception throw each Exception to make sure that the test fails for all
	 *         unexpected Exception.
	 */
	protected void setUp() throws Exception {

		mValidEservice = new EService();
		mValidEservice.setName("eVoucher");
		mValidEservice.setMethod("GET");
		mValidEservice.setProductionUrl("http://productionURL");
		mValidEservice.setTestUrl("http://testURL");

	
		
		Set parameterList = new HashSet();
		Parameter parameter = new Parameter();
		parameter.setId(1);
		parameter.setName("SPP_Username");
		parameter.setExpression("${username}");
		parameterList.add(parameter);

		Parameter parameter2 = new Parameter();
		parameter2.setId(2);
		parameter2.setName("SPP_PartnerProId");
		parameter2.setExpression("${PartnerProId}");
		parameterList.add(parameter2);

		Parameter parameter3 = new Parameter();
		parameter3.setId(3);
		parameter3.setName("SPP_Version");
		parameter3.setExpression("1.3");
		parameterList.add(parameter3);

		Parameter parameter4 = new Parameter();
		parameter4.setId(4);
		parameter4.setName("simulatingUsername");
		parameter4.setExpression("${SimulatingUser.username}");
		parameterList.add(parameter4);
		mValidEservice.setParameterList(parameterList);
		
		Parameter parameter5 = new Parameter();
		parameter5.setId(5);
		parameter5.setName("simulatingLanguage");
		parameter5.setExpression("${SimulatingUser.language}");
		parameterList.add(parameter5);
		mValidEservice.setParameterList(parameterList);
			
			
		mUserProfile  = new java.util.HashMap();
		mUserProfile.put("environmentPlatform", "PROD");
		mUserProfile.put("username", "jdoe");
		mUserProfile.put("firstname", "Jhon");
		mUserProfile.put("lastname", "Doe");
		mUserProfile.put("language", "FR");
		mUserProfile.put("HPOrg", "zeze");
		mUserProfile.put("HPSetup", "  zeze  ");
		mUserProfile.put("PortalSessionId", "12121212");
		mUserProfile.put("PartnerProId", "11212");
		mUserProfile.put("HpInternalUser", "22323");
		mUserProfile.put("UserGroups", "GENERIC_ADMIN;LOCAL_ADMIN;OTHER_GROUP");

		mSimulatingUserProfile = new java.util.HashMap();
		mSimulatingUserProfile.put("environmentPlatform", "PROD");
		mSimulatingUserProfile.put("username", "sppTest");
		mSimulatingUserProfile.put("firstname", "");
		mSimulatingUserProfile.put("lastname", "Doe");
		mSimulatingUserProfile.put("language", "FR");
		//mUserTestProfile.put("PartnerProId", "ezzez");
		mSimulatingUserProfile.put("HPOrg", "zeze");
		mSimulatingUserProfile.put("PortalSessionId", "12121212");
		mSimulatingUserProfile.put("PartnerProId", "11212");
		mSimulatingUserProfile.put("HpInternalUser", "22323");

		mUserProfile.put("SimulatingUser",mSimulatingUserProfile);
		
	}

	/**
	 * To keep if you want to do something particular after the test or suite test was runned.
	 * 
	 * @see com.hp.spp.common.test.AbstractSPPTestCase#tearDown
	 * @throws Exception throw each Exception to make sure that the test fails for all
	 *         unexpected Exception.
	 */
	protected void tearDown() throws Exception {
	}

	
}
