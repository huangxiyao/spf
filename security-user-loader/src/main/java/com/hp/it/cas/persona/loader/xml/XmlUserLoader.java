package com.hp.it.cas.persona.loader.xml;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.ClassUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.hp.it.cas.persona.loader.IUserLoader;
import com.hp.it.cas.persona.uav.service.IUserAttributeValueService;

/**
 * A user loader implemenation that processes XML data.
 *
 * @author Quintin May
 */
public class XmlUserLoader implements IUserLoader {

	private final Logger logger = LoggerFactory.getLogger(XmlUserLoader.class);

	private static final String DEFAULT_NAMESPACE_URI = "urn:X-hp:com.hp.it.cas.persona.schema.users";

	private final SAXParser saxParser;
	private final XmlUserLoaderSaxHandler saxHandler;

	/**
	 * Creates the user loader.
	 * @param userAttributeValueService the service into which data will be loaded.
	 * @param authentication the credentials of the application whose data is being loaded.
	 * @throws SAXException if there is a problem parsing the input data.
	 * @throws ParserConfigurationException if there is a problem configuring the XML parser.
	 */
	public XmlUserLoader(IUserAttributeValueService userAttributeValueService, Authentication authentication)
			throws SAXException, ParserConfigurationException {

		SecurityContextHolder.getContext().setAuthentication(authentication);

		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		InputStream schemaInputStream = null;
		try {
			SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			String schemaLocation = ClassUtils.classPackageAsResourcePath(XmlUserLoader.class) + "/users.xsd";

			schemaInputStream = getClass().getClassLoader().getResourceAsStream(schemaLocation);
			Source schemaSource = new StreamSource(schemaInputStream);
			Schema schema = schemaFactory.newSchema(schemaSource);

			saxParserFactory.setNamespaceAware(true);
			saxParserFactory.setSchema(schema);
			saxParserFactory.setValidating(true);
		} finally {
			if (schemaInputStream != null) {
				try {
					schemaInputStream.close();
				} catch (IOException e) {}
			}
		}

		this.saxParser = saxParserFactory.newSAXParser();
		this.saxHandler = new XmlUserLoaderSaxHandler(DEFAULT_NAMESPACE_URI, userAttributeValueService);
	}

	@Transactional
	public void load(String...files) {
		processInternal(files);
	}

	@Transactional
	public void validate(String...files) {
		// same processing as load but rollback at the end
		processInternal(files);
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
	}
	
	private void processInternal(String...files) {
		try {
			if (files.length == 0) {
				logger.info("Reading user data from: <stdin>");
				saxParser.parse(new InputSource(new InputStreamReader(new BufferedInputStream(System.in), Charset
						.forName("UTF-8"))), saxHandler);
			} else {
				for (String file : files) {
					logger.info("Reading user data from: {}", file);
					saxParser.parse(new InputSource(new InputStreamReader(
							new BufferedInputStream(new FileInputStream(file)), Charset.forName("UTF-8"))), saxHandler);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Unable to load users.", e);
		}
	}
	
	public int getAttributeLoadCount() {
		return saxHandler == null ? 0 : saxHandler.getAttributeCount();
	}

	public int getUserLoadCount() {
		return saxHandler == null ? 0 : saxHandler.getUserCount();
	}
}
