package com.hp.spp.eservice.services.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.SAXException;

import com.hp.spp.common.util.parsers.XmlParsingErrorHandler;
import com.hp.spp.eservice.Site;
import com.hp.spp.eservice.exception.XmlImportException;
import com.hp.spp.eservice.services.UnmarshalService;

public class CastorUnmarshalServiceImpl implements UnmarshalService {

	private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";

	private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";

	private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";

	private static Logger logger = Logger.getLogger(CastorUnmarshalServiceImpl.class);

	public Site unmarshalEServices(byte[] xml) throws XmlImportException {

		Site site = unmarshalSite(xml, "EServiceCastorMapping.xml",
				"xsd/EServiceDefinition.xsd");

		if (logger.isDebugEnabled()) {
			logger.debug("Successfully unmarshalled EServices");
		}

		return site;
	}

	private Site unmarshalSite(byte[] xml, String mappingFile, String xsdFile)
			throws XmlImportException {

		if (logger.isDebugEnabled()) {
			logger.debug("Unmarshalling XML: \n" + new String(xml));
		}

		Site site = null;
		Mapping map = new Mapping();
		URL u = getClass().getClassLoader().getResource(mappingFile);
		try {
			validateSchema(xml, xsdFile);
			map.loadMapping(u);
			Unmarshaller unmarshaller = new Unmarshaller(map);
			site = (Site) unmarshaller.unmarshal(new InputStreamReader(
					new ByteArrayInputStream(xml)));
		} catch (MarshalException e) {
			logger.error("Could not unmarshal XML", e);
			throw new XmlImportException("The XML could not be unmarshalled. Reason: "
					+ e.getMessage());
		} catch (ValidationException e) {
			logger.info("Validation error unmarshalling XML: "+e.getMessage());
			throw new XmlImportException("The XML was not valid. Reason: " + e.getMessage());
		} catch (XmlImportException e)	{
			logger.info("XML was not valid");
			throw e;
		} catch (SAXException e)	{
			logger.info("XML parsing exception: " + e.getMessage());
			throw new XmlImportException("Problem during XML File parsing. " + e.getMessage());
		} catch (Exception e) {
			logger.error("Unexpected error when unmarshalling XML", e);
			throw new XmlImportException("There was a problem whilst "
					+ "trying to import the XML. Reason: " + e.getMessage());
		}

		return site;
	}

	private void validateSchema(byte[] xml, String xsdFile)
			throws ParserConfigurationException, SAXException, IOException, XmlImportException {

		URL schemaUrl = getClass().getClassLoader().getResource(xsdFile);
		

		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
		if (logger.isDebugEnabled())
		{
			logger.debug("The instance of the DocumentBuilderFactory is of Class (" + docBuilderFactory.getClass() + "]");
		}
		docBuilderFactory.setNamespaceAware(true);
		docBuilderFactory.setValidating(true);
		
		docBuilderFactory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
		docBuilderFactory.setAttribute(JAXP_SCHEMA_SOURCE, schemaUrl.openStream());

		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		if (logger.isDebugEnabled()) {
			logger.debug("The instance of the DocumentBuilder is of Class ("
					+ docBuilder.getClass() + "]");
		}

		XmlParsingErrorHandler errorHandler = new XmlParsingErrorHandler();
		docBuilder.setErrorHandler(errorHandler);

		docBuilder.parse(new ByteArrayInputStream(xml));

		if (errorHandler.isExistErrors()) {
			throw new XmlImportException("Problem during XML File parsing. Message: " + errorHandler.getErrorMessages());
		}
		
	}

	public Site unmarshalStandardParameterSets(byte[] xml) throws XmlImportException {

		Site site = unmarshalSite(xml, "StandardParameterSetCastorMapping.xml",
				"xsd/StandardParameterSetDefinition.xsd");

		if (logger.isDebugEnabled()) {
			logger.debug("Successfully unmarshalled StandardParameterSets");
		}

		return site;
	}

}
