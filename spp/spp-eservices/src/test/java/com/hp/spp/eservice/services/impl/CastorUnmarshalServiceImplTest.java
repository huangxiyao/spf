package com.hp.spp.eservice.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;

import junit.framework.TestCase;

import com.hp.spp.eservice.EService;
import com.hp.spp.eservice.Parameter;
import com.hp.spp.eservice.Site;
import com.hp.spp.eservice.StandardParameterSet;
import com.hp.spp.eservice.exception.XmlImportException;
import com.hp.spp.eservice.services.UnmarshalService;

/**
 * 
 * @author PBRETHER
 * 
 */
public class CastorUnmarshalServiceImplTest extends TestCase {


	public void testEServiceInvalidRoot() throws Exception {
		UnmarshalService unmarshalService = new CastorUnmarshalServiceImpl();
		Exception exception = null;
		try {
			unmarshalService
					.unmarshalEServices(getXmlFromFile("eservices/invalid_root_eservice.xml"));
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull("An exception should be thrown", exception);
		assertSame("The exception should be an XmlImportException", XmlImportException.class,
				exception.getClass());
		assertTrue("Compare Exception message", exception.getMessage().startsWith(
				"Problem during XML File parsing"));
	}

	public void testParamSetInvalidRoot() throws Exception {
		UnmarshalService unmarshalService = new CastorUnmarshalServiceImpl();
		Exception exception = null;
		try {
			unmarshalService
					.unmarshalEServices(getXmlFromFile("eservices/invalid_root_param_set.xml"));
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull("An exception should be thrown", exception);
		assertSame("The exception should be an XmlImportException", XmlImportException.class,
				exception.getClass());
		assertTrue("Compare Exception message", exception.getMessage().startsWith(
				"Problem during XML File parsing"));
	}

	public void testEServiceUnknownElement() throws Exception {
		UnmarshalService unmarshalService = new CastorUnmarshalServiceImpl();
		Exception exception = null;
		try {
			unmarshalService
					.unmarshalEServices(getXmlFromFile("eservices/unknown_element_eservice.xml"));
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull("An exception should be thrown", exception);
		assertSame("The exception should be an XmlImportException", XmlImportException.class,
				exception.getClass());
		assertTrue("Compare Exception message", exception.getMessage().startsWith(
				"Problem during XML File parsing"));
	}

	public void testParamSetUnknownElement() throws Exception {
		UnmarshalService unmarshalService = new CastorUnmarshalServiceImpl();
		Exception exception = null;
		try {
			unmarshalService
					.unmarshalEServices(getXmlFromFile("eservices/unknown_element_param_set.xml"));
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull("An exception should be thrown", exception);
		assertSame("The exception should be an XmlImportException", XmlImportException.class,
				exception.getClass());
		assertTrue("Compare Exception message", exception.getMessage().startsWith(
				"Problem during XML File parsing"));
	}

	public void testEServiceMissingClosingElement() throws Exception {
		UnmarshalService unmarshalService = new CastorUnmarshalServiceImpl();
		Exception exception = null;
		try {
			unmarshalService
					.unmarshalEServices(getXmlFromFile("eservices/missing_closing_element_4_eservice.xml"));
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull("An exception should be thrown", exception);
		assertSame("The exception should be an XmlImportException", XmlImportException.class,
				exception.getClass());
		assertTrue("Compare Exception message", exception.getMessage().startsWith(
				"Problem during XML File parsing"));
	}

	public void testParamSetMissingClosingElement() throws Exception {
		UnmarshalService unmarshalService = new CastorUnmarshalServiceImpl();
		Exception exception = null;
		try {
			unmarshalService
					.unmarshalEServices(getXmlFromFile("eservices/missing_closing_element_4_param_set.xml"));
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull("An exception should be thrown", exception);
		assertSame("The exception should be an XmlImportException", XmlImportException.class,
				exception.getClass());
		assertTrue("Compare Exception message", exception.getMessage().startsWith(
				"Problem during XML File parsing"));
	}

	public void testEServiceNoXml() throws Exception {
		UnmarshalService unmarshalService = new CastorUnmarshalServiceImpl();
		Exception exception = null;
		try {
			unmarshalService.unmarshalEServices(new byte[0]);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull("An exception should be thrown", exception);
		assertSame("The exception should be an XmlImportException", XmlImportException.class,
				exception.getClass());
		assertTrue("Compare Exception message", exception.getMessage().startsWith(
				"Problem during XML File parsing"));
	}

	public void testParamSetNoXml() throws Exception {
		UnmarshalService unmarshalService = new CastorUnmarshalServiceImpl();
		Exception exception = null;
		try {
			unmarshalService.unmarshalStandardParameterSets(new byte[0]);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull("An exception should be thrown", exception);
		assertSame("The exception should be an XmlImportException", XmlImportException.class,
				exception.getClass());
		assertTrue("Compare Exception message", exception.getMessage().startsWith(
				"Problem during XML File parsing"));
	}

	public void testInvalidEServiceSchema() throws Exception {
		UnmarshalService unmarshalService = new CastorUnmarshalServiceImpl();
		Exception exception = null;
		try {
			unmarshalService
					.unmarshalEServices(getXmlFromFile("eservices/invalid_eservice_schema.xml"));
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull("An exception should be thrown", exception);
		assertSame("The exception should be an XmlImportException", XmlImportException.class,
				exception.getClass());
		assertTrue("Compare Exception message", exception.getMessage().startsWith(
				"Problem during XML File parsing"));
	}

	public void testInvalidParamSetSchema() throws Exception {
		UnmarshalService unmarshalService = new CastorUnmarshalServiceImpl();
		Exception exception = null;
		try {
			unmarshalService
					.unmarshalStandardParameterSets((getXmlFromFile("eservices/invalid_eservice_schema.xml")));
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull("An exception should have been thrown", exception);
		assertSame("The exception should be an XmlImportException", XmlImportException.class,
				exception.getClass());
		assertTrue("Compare Exception message", exception.getMessage().startsWith(
				"Problem during XML File parsing"));
	}

	public void testInvalidEServiceXml() throws Exception {
		UnmarshalService unmarshalService = new CastorUnmarshalServiceImpl();
		Exception exception = null;
		try {
			unmarshalService
					.unmarshalStandardParameterSets((getXmlFromFile("eservices/invalid_eservice_xml.xml")));
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull("An exception should have been thrown", exception);
		assertSame("The exception should be an XmlImportException", XmlImportException.class,
				exception.getClass());
		assertTrue("Compare Exception message", exception.getMessage().startsWith(
				"Problem during XML File parsing. Message"));
	}

	public void testInvalidParamSetXml() throws Exception {
		UnmarshalService unmarshalService = new CastorUnmarshalServiceImpl();
		Exception exception = null;
		try {
			unmarshalService
					.unmarshalStandardParameterSets((getXmlFromFile("eservices/invalid_eservice_xml.xml")));
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull("An exception should have been thrown", exception);
		assertSame("The exception should be an XmlImportException", XmlImportException.class,
				exception.getClass());
		assertTrue("Compare Exception message", exception.getMessage().startsWith(
				"Problem during XML File parsing. Message"));
	}

	public void testUnmarshalStandardParamSet() throws Exception {

		UnmarshalService unmarshalService = new CastorUnmarshalServiceImpl();
		Site site = unmarshalService
				.unmarshalStandardParameterSets(getXmlFromFile("eservices/valid_param_set.xml"));
		Set paramSets = site.getStandardParameterSet();
		StandardParameterSet paramSet1 = (StandardParameterSet) paramSets.toArray()[1];
		StandardParameterSet paramSet2 = (StandardParameterSet) paramSets.toArray()[0];
		Set parameters = paramSet1.getParameterList();
		// FIXME: can't rely on this order
		Parameter lastNameParam = (Parameter) parameters.toArray()[0];
		Parameter userNameParam = (Parameter) parameters.toArray()[1];

		// assertions
		assertNotNull("The site should not be null", site);
		assertEquals("There should be one param set", 2, paramSets.size());
		assertEquals("The name of the param set should be EMEA_Parameters", "EMEA_Parameters",
				paramSet1.getName());
		assertEquals("The first parameter name should be 'userName'", "userName",
				userNameParam.getName());
		assertEquals("The expression should be '${username}'", "${username}", userNameParam
				.getExpression());
		assertEquals("The second parameter name should be 'lastName'", "lastName",
				lastNameParam.getName());
		assertEquals("The expression should be '${lastname}'", "${lastname}", lastNameParam
				.getExpression());
		assertNotNull("There should be a second param set", paramSet2);
	}

	public void testUnmarshalEServices() throws Exception {

		UnmarshalService unmarshalService = new CastorUnmarshalServiceImpl();
		Site site = unmarshalService
				.unmarshalEServices(getXmlFromFile("eservices/valid_eservice.xml"));
		Set eServices = site.getEServiceList();
		EService service = (EService) eServices.toArray()[0];
		Set parameters = service.getParameterList();
		// FIXME: can't rely on this order
		Parameter localAdminParam = (Parameter) parameters.toArray()[0];
		Parameter phoneParam = (Parameter) parameters.toArray()[1];

		// assertions
		assertNotNull("The site should not be null", site);
		assertEquals("There should be one eservice", 1, eServices.size());
		assertEquals("The name of the eService should be 'service1 & others'", "service1 & others", service
				.getName());
		assertEquals("The method should be 'GET'", "GET", service.getMethod());
		assertEquals("The ProductionUrl should be 'http://productionurl.com/page'",
				"http://productionurl.com/page", service.getProductionUrl());
		assertEquals("The TestUrl should be 'http://testurl.com/page'",
				"http://testurl.com/page", service.getTestUrl());
		assertEquals("The standard parameter set name should be 'EMEA_Parameters'",
				"EMEA_Parameters", service.getStandardParameterSetName());
		assertEquals("The parameter name should be 'phoneNumber'", "phoneNumber", phoneParam
				.getName());
		assertEquals("The parameter name should be 'LocalAdmin'", "LocalAdmin",
				localAdminParam.getName());
		assertEquals("The phone parameter expression should be '${phoneNumber}'",
				"${phoneNumber}", phoneParam.getExpression());
		assertEquals(
				"The local admin parameter expression should be '${spp:Ingroup(LOCAL_ADMIN)}'",
				"${spp:Ingroup(LOCAL_ADMIN)}", localAdminParam.getExpression());
	}

	private byte[] getXmlFromFile(String fileName) throws IOException {
		byte[] xml = null;
		if (fileName != null) {
			URL xmlUrl = getClass().getClassLoader().getResource(fileName);
			File file = new File(xmlUrl.getFile());
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
			xml = new byte[(int) length];

			// Read in the bytes
			int offset = 0;
			int numRead = 0;
			while (offset < xml.length
					&& (numRead = is.read(xml, offset, xml.length - offset)) >= 0) {
				offset += numRead;
			}
		}
		return xml;
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
