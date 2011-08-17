package com.hp.spp.eservice.services.impl;

import java.util.HashSet;

import junit.framework.TestCase;

import com.hp.spp.eservice.EService;
import com.hp.spp.eservice.Site;
import com.hp.spp.eservice.StandardParameterSet;
import com.hp.spp.eservice.exception.XmlImportException;
import com.hp.spp.eservice.services.EServiceImportValidationService;

/**
 * 
 * @author PBRETHER
 * 
 */
public class EServiceImportValidationServiceTest extends TestCase {

	private Site mSite;


	public void testParamSetNonExistent() throws Exception	{
		
		EServiceImportValidationService service = new EServiceImportValidationServiceImpl();
		
		Site importedSite = createSiteWithEServiceRefToNonExistentParam();
		
		Exception exception = null;
		try {
			service.validateEServiceImport(importedSite, mSite);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull("An exception should have been thrown", exception);
		assertSame("The exception should be an XmlImportException", XmlImportException.class,
				exception.getClass());
		assertEquals(
				"The error message should be 'The parameter set [yiha] does not exist.'",
				"The parameter set [yiha] does not exist.", exception.getMessage());
		
	}

	private Site createSiteWithEServiceRefToNonExistentParam() {
		Site site = new Site();
		site.setName("MySite");
		EService service = new EService();
		service.setName("service");
		service.setStandardParameterSetName("yiha");
		site.addEService(service);
		return site;
	}

	public void testTwoEServicesSameName() throws Exception {

		EServiceImportValidationService service = new EServiceImportValidationServiceImpl();

		Site importedSite = createSiteWithTwoEServicesSameName();
		Exception exception = null;
		try {
			service.validateEServiceImport(importedSite, mSite);
		} catch (Exception e) {
			exception = e;
			e.printStackTrace(System.err);
		}
		assertNotNull("An exception should have been thrown", exception);
		assertSame("The exception should be an XmlImportException", XmlImportException.class,
				exception.getClass());
		assertEquals(
				"The error message should be 'at least two eServices have the same name'",
				"At least two eServices have the same name in the same import. Name: [MyName].", exception.getMessage());
	}
	
	


	private Site createSiteWithTwoEServicesSameName() {
		Site site = new Site();
		site.setName("site with two EServices");
		EService service1 = new EService();
		service1.setName("MyName");
		EService service2 = new EService();
		service2.setName("MyName");
		HashSet eServices = new HashSet(2);
		eServices.add(service1);
		eServices.add(service2);
		site.setEServiceList(eServices);
		return site;
	}

	public void testTwoParamSetsSameName() throws Exception {

		EServiceImportValidationService service = new EServiceImportValidationServiceImpl();

		Site importedSite = createSiteWithTwoParamSetsSameName();
		Exception exception = null;
		try {
			service.validateStandardParameterSetImport(importedSite, mSite);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull("An exception should have been thrown", exception);
		assertSame("The exception should be an XmlImportException", XmlImportException.class,
				exception.getClass());
		assertEquals(
				"The error message should be 'at least two StandardParameterSets have the same name'",
				"At least two StandardParameterSets have the same name in the same import. Name: [MyName].", exception.getMessage());
	}

	private Site createSiteWithTwoParamSetsSameName() {
		Site site = new Site();
		site.setName("site with two ParamSets");
		StandardParameterSet param1 = new StandardParameterSet();
		param1.setName("MyName");
		StandardParameterSet param2 = new StandardParameterSet();
		param2.setName("MyName");
		HashSet paramSets = new HashSet(2);
		paramSets.add(param1);
		paramSets.add(param2);
		site.setStandardParameterSet(paramSets);
		return site;
	}
	
	
	public void testEServicesWithInvalidCharset() throws Exception {

		EServiceImportValidationService service = new EServiceImportValidationServiceImpl();

		Site importedSite = createSiteWithInvalidCharset();
		Exception exception = null;
		try {
			service.validateEServiceImport(importedSite, mSite);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull("An exception should have been thrown", exception);
		assertSame("The exception should be an XmlImportException", XmlImportException.class,
				exception.getClass());
		assertEquals(
				"The error message should be 'The character encoding [char] is not supported'",
				"The character encoding [UTF-9999] is not supported", exception.getMessage());
		
	}
	
	
	private Site createSiteWithInvalidCharset() {
		Site site = new Site();
		EService invalidCharsetEservice = new EService();
		//Invalid character set 
		invalidCharsetEservice.setCharacterEncoding("UTF-9999");
		
		site.addEService(invalidCharsetEservice);
		// TODO Auto-generated method stub
		return site;
	}

	public void testEServicesWithValidCharset() throws Exception {

		EServiceImportValidationService service = new EServiceImportValidationServiceImpl();

		Site importedSite = createSiteWithValidCharset();
		Exception exception = null;
		try {
			service.validateEServiceImport(importedSite, mSite);
		} catch (Exception e) {
			exception = e;
		}
		this.assertNull("An exception should not have been thrown", exception);
	}
	
	
	private Site createSiteWithValidCharset() {
		Site site = new Site();
		EService validCharsetEService = new EService();
		//Invalid character set 
		validCharsetEService.setCharacterEncoding("UTF-8");
		
		site.addEService(validCharsetEService);
		// TODO Auto-generated method stub
		return site;
	}

	/**
	 * To keep if you want to do something particular during the initialization.
	 * 
	 * @see com.hp.spp.AbstractSPPTestCase.setUp
	 * @throws Exception throw each Exception to make sure that the test fails for all
	 *         unexpected Exception.
	 */
	protected void setUp() throws Exception {
		mSite = new Site();
		mSite.setName("smartportal");
		
		EService service1 = new EService();
		service1.setName("eService1");
		EService service2 = new EService();
		service2.setName("eService2");
		HashSet eServices = new HashSet(2);
		eServices.add(service1);
		eServices.add(service2);
		mSite.setEServiceList(eServices);
		
		StandardParameterSet param1 = new StandardParameterSet();
		param1.setName("paramSet1");
		StandardParameterSet param2 = new StandardParameterSet();
		param2.setName("paramSet2");
		HashSet paramSets = new HashSet(2);
		paramSets.add(param1);
		paramSets.add(param2);
		mSite.setStandardParameterSet(paramSets);
		
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
