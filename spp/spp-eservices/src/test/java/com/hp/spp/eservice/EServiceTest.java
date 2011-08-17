package com.hp.spp.eservice;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.hp.spp.eservice.el.ExpressionResolver;

public class EServiceTest extends TestCase {

	private EService mNoParameterEservice = null;

	private EService mWrongParameterEservice = null;
	
	private EService mValidEservice = null;

	private EService mGetEservice = null;

	private EService mPostEservice = null;

	private EService mSTDParameterEservice = null;
	
	private Map mUserProdProfile = null;

	private Map mUserTestProfile = null;

	private Map mHttpRequestParameter = null;

	private EService mJSTLEservice  = null;
	
	public void testEserviceSimpleURL() throws Exception {
		EServiceManager esm = new EServiceManager(mValidEservice);
		ExpressionResolver expressionResolver = new ExpressionResolver(mUserProdProfile);
		esm.setExpressionResolver(expressionResolver);
		assertEquals("check productionUrl", "http://productionURL", esm.getEserviceUrl(null,
				null));

		ExpressionResolver expressionResolverTest = new ExpressionResolver(mUserTestProfile);
		esm.setExpressionResolver(expressionResolverTest);
		assertEquals("check testUrl", "http://testURL", esm.getEserviceUrl(null, null));
	}

	public void testEserviceDynamicURL() throws Exception {
		EServiceManager esm = new EServiceManager(mGetEservice);
		ExpressionResolver expressionResolver = new ExpressionResolver(mUserProdProfile);
		esm.setExpressionResolver(expressionResolver);
		assertEquals("check productionUrl", "http://production/FR/URL", esm.getEserviceUrl(
				null, null));

		ExpressionResolver expressionResolverTest = new ExpressionResolver(mUserTestProfile);
		esm.setExpressionResolver(expressionResolverTest);
		assertEquals("check testUrl", "http://test/FR/URL", esm.getEserviceUrl(null, null));
	}

	public void testHttpMethod() throws Exception {

		EServiceManager esm = new EServiceManager(mPostEservice);
		// ExpressionResolver expressionResolver = new ExpressionResolver(mUserProdProfile);
		assertEquals("check POST method", "POST", esm.getMethod());

		EServiceManager esm2 = new EServiceManager(mGetEservice);
		// ExpressionResolver expressionResolver = new ExpressionResolver(mUserProdProfile);
		assertEquals("check GET method", "GET", esm2.getMethod());
	}

	public void testSimpleParameterExpression() throws Exception {
		EServiceManager esm = new EServiceManager(mValidEservice);
		ExpressionResolver expressionResolver = new ExpressionResolver(mUserProdProfile);
		esm.setExpressionResolver(expressionResolver);
		Map result = esm.getEserviceParameters();
		assertEquals("Check for version Parameter", "1.3", result.get("SPP_Version"));

		esm = new EServiceManager(mNoParameterEservice);
		expressionResolver = new ExpressionResolver(mUserProdProfile);
		esm.setExpressionResolver(expressionResolver);
		result = esm.getEserviceParameters();
		assertEquals("Number of parameter", 0, result.size());

	}

	public void testDynamicParameterExpression() throws Exception {

		EServiceManager esm = new EServiceManager(mValidEservice);
		ExpressionResolver expressionResolver = new ExpressionResolver(mUserProdProfile);
		esm.setExpressionResolver(expressionResolver);
		Map result = esm.getEserviceParameters();
		assertEquals("Test on NB parameters", 7, result.keySet().size());
		assertEquals("Check for Username Parameter", "jdoe", result.get("SPP_Username"));
		assertEquals("Check for lastName Parameter", "Jhon, Doe", result.get("SPP_fullname"));
		assertEquals("Check for lastName Parameter", "11212", result.get("SPP_PartnerProId"));
	}

	public void testDeepLinkParameters() throws Exception {
		EServiceManager esm = new EServiceManager(mValidEservice);
		ExpressionResolver expressionResolver = new ExpressionResolver(mUserProdProfile);
		esm.setHttpRequestParameters(mHttpRequestParameter);
		esm.setExpressionResolver(expressionResolver);

		Map result = esm.getEserviceParameters();
		assertEquals("Test on NB parameters", 8, result.keySet().size());
		assertEquals("Check for Username Parameter", "jdoe", result.get("SPP_Username"));
		assertEquals("Check for fullName Parameter", "Jhon, Doe", result.get("SPP_fullname"));
		assertEquals("Check for SPP_PartnerProId Parameter", "11212", result.get("SPP_PartnerProId"));
		assertEquals("Check for SPP_NewParam Parameter", "TestValue", result.get("SPP_NewParam"));
	}

	public void testMissingParameters() throws Exception {
		ExpressionResolver expressionResolver = new ExpressionResolver(mUserProdProfile);
		EServiceManager esm = new EServiceManager(mWrongParameterEservice);
		esm.setExpressionResolver(expressionResolver);
		
		Map result = esm.getEserviceParameters();
		assertEquals("Test on NB parameters", 0, result.keySet().size());

	}

	public void testUpperCase() throws Exception {
		ExpressionResolver expressionResolver = new ExpressionResolver(mUserProdProfile);
		EServiceManager esm = new EServiceManager(mValidEservice);
		esm.setHttpRequestParameters(mHttpRequestParameter);
		esm.setExpressionResolver(expressionResolver);

		Map result = esm.getEserviceParameters();
		// ExpressionResolver expressionResolver = new ExpressionResolver(mUserProdProfile);
		// TestEl testEl = new TestEl();
		assertEquals("Check for uppercase", "JHON", result.get("testUpperCase"));
	}

	public void testJSTL() throws Exception {
		ExpressionResolver expressionResolver = new ExpressionResolver(mUserProdProfile);
		EServiceManager esm = new EServiceManager(mJSTLEservice);
		//esm.setHttpRequestParameters(mHttpRequestParameter);
		esm.setExpressionResolver(expressionResolver);

		Map result = esm.getEserviceParameters();
		// ExpressionResolver expressionResolver = new ExpressionResolver(mUserProdProfile);
		// TestEl testEl = new TestEl();
		assertEquals("Check for lowercase", "jhon", result.get("lowerCase"));
		assertEquals("Check for trim", "zeze", result.get("trim"));
		assertEquals("Check for contains", "yes", result.get("contains"));
		assertEquals("Check for containsIgnoreCase", "yes", result.get("containsIgnoreCase"));
		assertEquals("Check for length", "4", result.get("length"));	
	//	assertEquals("Check for join", "a*b*c", result.get("join"));
		assertEquals("Check for uppercase", "JHON", result.get("uppercase"));
		assertEquals("Check for subString", "ho", result.get("subString"));
		assertEquals("Check for subStringAfter", "n", result.get("substringAfter"));
		assertEquals("Check for subStringBefore", "J", result.get("substringBefore"));
		
		assertEquals("Check for replace", "adri** finish at the **d", result.get("replace"));
	}


	public void testInGroup() throws Exception {
		// >${spp:InGroup(LOCAL_ADMIN)}</
		ExpressionResolver expressionResolver = new ExpressionResolver(mUserProdProfile);
		EServiceManager esm = new EServiceManager(mValidEservice);
		esm.setHttpRequestParameters(mHttpRequestParameter);
		esm.setExpressionResolver(expressionResolver);

		Map result = esm.getEserviceParameters();
		// ExpressionResolver expressionResolver = new ExpressionResolver(mUserProdProfile);
		// TestEl testEl = new TestEl();
		assertEquals("Check for in Group", "T", result.get("localAdmin"));
		// assertEquals("function - add; default ns","True",
		// testEl.eval("${spp:InGroup('toto')}", String.class));
		assertEquals("Check for in Group", "F", result.get("localDev"));

	}

	public void testStdParameterSet() throws Exception {
		ExpressionResolver expressionResolver = new ExpressionResolver(mUserProdProfile);
		EServiceManager esm = new EServiceManager(mSTDParameterEservice);
		esm.setExpressionResolver(expressionResolver);

		Map result = esm.getEserviceParameters();
		// ExpressionResolver expressionResolver = new ExpressionResolver(mUserProdProfile);
		// TestEl testEl = new TestEl();
		assertEquals("Check for added parameter", "jdoe", result.get("STD_Param1"));
		// assertEquals("function - add; default ns","True",
		// testEl.eval("${spp:InGroup('toto')}", String.class));
		assertEquals("Check for overrided parameter", "11212", result.get("SPP_PartnerProId"));

	}
	
	public void testWrongParameterExpression() throws Exception {
		ExpressionResolver expressionResolver = new ExpressionResolver(mUserProdProfile);
		EServiceManager esm = new EServiceManager(mWrongParameterEservice);
		esm.setExpressionResolver(expressionResolver);

		Map result = esm.getEserviceParameters();
	
	}
	
	public void testDeepLinkUrl() throws Exception {

		EServiceManager esm = new EServiceManager(mValidEservice);
		
		ExpressionResolver expressionResolver = new ExpressionResolver(mUserTestProfile);
		esm.setHttpRequestParameters(mHttpRequestParameter);
		esm.setExpressionResolver(expressionResolver);

		assertEquals("Check for Deep link url", "http://www.google.fr", esm.getEserviceUrl(null,"http://www.google.fr"));
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

		mGetEservice = new EService();
		mGetEservice.setName("eVoucher");
		mGetEservice.setMethod("GET");
		mGetEservice.setProductionUrl("http://production/${language}/URL");
		mGetEservice.setTestUrl("http://test/${language}/URL");

		mNoParameterEservice = new EService();
		mNoParameterEservice.setName("eVoucher");
		mNoParameterEservice.setMethod("GET");
		mNoParameterEservice.setProductionUrl("http://production/${language}/URL");
		mNoParameterEservice.setTestUrl("http://test/${language}/URL");

		mPostEservice = new EService();
		mPostEservice.setName("eVoucher");
		mPostEservice.setMethod("POST");
		mPostEservice.setProductionUrl("http://productionURL");
		mPostEservice.setTestUrl("http://testURL");

		
		mSTDParameterEservice = new EService();
		mSTDParameterEservice.setName("eVoucher");
		mSTDParameterEservice.setMethod("GET");
		mSTDParameterEservice.setProductionUrl("http://production/${language}/URL");
		mSTDParameterEservice.setTestUrl("http://test/${language}/URL");
		
		mWrongParameterEservice = new EService();
		mWrongParameterEservice.setName("eVoucher");
		mWrongParameterEservice.setMethod("GET");
		Site site = new Site();
		site.setName("SmartPortal");
		mWrongParameterEservice.setSite(site);
		
		mWrongParameterEservice.setProductionUrl("http://production/${language}/URL");
		mWrongParameterEservice.setTestUrl("http://test/${language}/URL");
		
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
		parameter4.setName("SPP_fullname");
		parameter4.setExpression("${firstname}, ${lastname}");
		parameterList.add(parameter4);

		Parameter parameter5 = new Parameter();
		parameter5.setId(5);
		parameter5.setName("localAdmin");
		parameter5.setExpression("${spp:inGroup('LOCAL_ADMIN',UserGroups)}");
		parameterList.add(parameter5);

		Parameter parameter6 = new Parameter();
		parameter6.setId(6);
		parameter6.setName("localDev");
		parameter6.setExpression("${spp:inGroup('LOCAL_Dev',UserGroups)}");
		parameterList.add(parameter6);
		mValidEservice.setParameterList(parameterList);

		Parameter parameter7 = new Parameter();
		parameter7.setId(7);
		parameter7.setName("testUpperCase");
		parameter7.setExpression("${toUpperCase(firstname)}");
		parameterList.add(parameter7);

//		Parameter parameter8 = new Parameter();
//		parameter8.setId(8);
//		parameter8.setName("testConcat");
//		parameter8.setExpression("${concat(firstname,lastname)}");
//		parameterList.add(parameter8);

		Set mWrongparameterList = new HashSet();
		parameter = new Parameter();
		parameter.setId(1);
		parameter.setName("SPP_Wrong");
		parameter.setExpression("${spp_WusernameW}");
		mWrongparameterList.add(parameter);
		mWrongParameterEservice.setParameterList(mWrongparameterList);
		
		mValidEservice.setParameterList(parameterList);
		mSTDParameterEservice.setParameterList(parameterList);
		
		mUserProdProfile = new java.util.HashMap();
		mUserProdProfile.put("environmentPlatform", "PROD");
		mUserProdProfile.put("username", "jdoe");
		mUserProdProfile.put("firstname", "Jhon");
		mUserProdProfile.put("lastname", "Doe");
		mUserProdProfile.put("language", "FR");
		//mUserProdProfile.put("PartnerProId", "ezzez");
		mUserProdProfile.put("HPOrg", "zeze");
		mUserProdProfile.put("HPSetup", "  zeze  ");
		mUserProdProfile.put("PortalSessionId", "12121212");
		mUserProdProfile.put("PartnerProId", "11212");
		mUserProdProfile.put("HpInternalUser", "22323");
		mUserProdProfile.put("UserGroups", "GENERIC_ADMIN;LOCAL_ADMIN;OTHER_GROUP");

		mUserTestProfile = new java.util.HashMap();
		mUserTestProfile.put("environmentPlatform", "DEV");
		mUserTestProfile.put("username", "jdoe");
		mUserTestProfile.put("firstname", "Jhon");
		mUserTestProfile.put("lastname", "Doe");
		mUserTestProfile.put("language", "FR");
		//mUserTestProfile.put("PartnerProId", "ezzez");
		mUserTestProfile.put("HPOrg", "zeze");
		mUserTestProfile.put("PortalSessionId", "12121212");
		mUserTestProfile.put("PartnerProId", "11212");
		mUserTestProfile.put("HpInternalUser", "22323");

		
		Set stdParameterList = new HashSet();
		Parameter stdParameter = new Parameter();
		stdParameter.setId(1);
		stdParameter.setName("STD_Param1");
		stdParameter.setExpression("${username}");
		stdParameterList.add(stdParameter);

		Parameter stdParameter2 = new Parameter();
		stdParameter2.setId(2);
		stdParameter2.setName("SPP_PartnerProId");
		stdParameter2.setExpression("From Std ${PartnerProId}");
		stdParameterList.add(stdParameter2);
		
		StandardParameterSet stdParamSet = new StandardParameterSet();
		stdParamSet.setParameterList(stdParameterList);
		mSTDParameterEservice.addStandardParameterSet(stdParamSet);
		
		
		mHttpRequestParameter = new java.util.HashMap();
		mHttpRequestParameter.put("SPP_PartnerProId", "9999");
		mHttpRequestParameter.put("SPP_NewParam", "TestValue");

		initilalizeJSTLEservice();
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

	private void initilalizeJSTLEservice(){
		mJSTLEservice = new EService();
		mJSTLEservice.setName("eVoucher");
		mJSTLEservice.setMethod("GET");
		mJSTLEservice.setProductionUrl("http://production/${language}/URL");
		mJSTLEservice.setTestUrl("http://test/${language}/URL");
		Site site = new Site();
		site.setName("SmartPortal");
		mJSTLEservice.setSite(site);
		addParameter(mJSTLEservice,"lowerCase","${toLowerCase(firstname)}");
		addParameter(mJSTLEservice,"trim","${trim(HPSetup)}");
		addParameter(mJSTLEservice,"contains","${contains(HPSetup,'ze')?'yes':'no'}");
		addParameter(mJSTLEservice,"containsIgnoreCase","${containsIgnoreCase(firstname,'jh')?'yes':'no'}");
		addParameter(mJSTLEservice,"length","${length(username)}");
		addParameter(mJSTLEservice,"join","${join({'a','b','c'},'*')}");
		addParameter(mJSTLEservice,"split","${split('a*b*c','*')}");
		addParameter(mJSTLEservice,"startWith","${startWith}");
		addParameter(mJSTLEservice,"startsWith","${startsWith}");
		addParameter(mJSTLEservice,"subString","${subString(firstname,1,3)}");
		addParameter(mJSTLEservice,"substring","${substring(firstname,1,3)}");
		addParameter(mJSTLEservice,"substringAfter","${substringAfter(firstname,'ho')}");
		addParameter(mJSTLEservice,"substringBefore","${substringBefore(firstname,'ho')}");
		addParameter(mJSTLEservice,"uppercase","${toUpperCase(firstname)}");
		addParameter(mJSTLEservice,"replace","${replace('adrien finish at the end','en','**')}");
	}
	
	private void addParameter(EService eser,String paramName, String paramValue){
		Parameter parameter6 = new Parameter();
		parameter6.setId(6);
		parameter6.setName(paramName);
		parameter6.setExpression(paramValue);
		Set param = eser.getParameterList();
		if (param ==null) param = new HashSet();
		param.add(parameter6);
		eser.setParameterList(param);
	}
	
}
