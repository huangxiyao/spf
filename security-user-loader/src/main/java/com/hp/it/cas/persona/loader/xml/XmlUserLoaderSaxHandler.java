/* $Header: /var/data/cvs/contract-entitlement-service/src/com/hp/contract/es/summary/ESReplySaxHandler.java,v 1.3 2007/07/17 14:32:49 qmay Exp $ */

package com.hp.it.cas.persona.loader.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.uav.service.IUserAttributeValue;
import com.hp.it.cas.persona.uav.service.IUserAttributeValueService;
import com.hp.it.cas.persona.uav.service.IUserIdentifier;
import com.hp.it.cas.persona.uav.service.UserIdentifier;
import com.hp.it.cas.xa.logging.StopWatch;

/**
 * Parser for the corporate Entitlement Service contract summary response.
 * 
 * @author Quintin May
 * @version $Revision: 1.3 $
 */
class XmlUserLoaderSaxHandler extends DefaultHandler {
	private final Logger logger = LoggerFactory.getLogger(XmlUserLoaderSaxHandler.class);
	
	private static final String USER		= "user";
	private static final String ATTRIBUTE	= "attribute";
	private static final String COMPOUND	= "compound";
	private static final String USER_TYPE	= "userType";
	private static final String USER_ID		= "userId";
	private static final String ID			= "id";

	private final String defaultNamespaceUri;
	private final IUserAttributeValueService userAttributeValueService;
	private final StringBuilder textBuilder = new StringBuilder();

	private IUserIdentifier userIdentifier;
	private String simpleIdentifier;
	private String compoundIdentifier;
	private int totalUserCount;
	private int totalAttributeCount;
	private int userAttributeCount;
	private String instanceIdentifier;
	private StopWatch userStopWatch = new StopWatch();
	
	XmlUserLoaderSaxHandler(String defaultNamespaceUri, IUserAttributeValueService userAttributeValueService) {
		this.defaultNamespaceUri = defaultNamespaceUri;
		this.userAttributeValueService = userAttributeValueService;
	}

	public int getUserCount() {
		return totalUserCount;
	}

	public int getAttributeCount() {
		return totalAttributeCount;
	}

	public void startElement(String namespaceUri, String localName, String qName, Attributes attributes)
			throws SAXException {
		textBuilder.setLength(0);

		if (isElement(ATTRIBUTE, namespaceUri, localName)) {
			simpleIdentifier = attributes.getValue(ID);
		} else if (isElement(COMPOUND, namespaceUri, localName)) {
			compoundIdentifier = attributes.getValue(ID);
		} else if (isElement(USER, namespaceUri, localName)) {
			userStopWatch.start();
			String userIdentifierTypeString = attributes.getValue(USER_TYPE);
			EUserIdentifierType userIdentifierType = EUserIdentifierType.valueOf(userIdentifierTypeString);
			String userIdentifier = attributes.getValue(USER_ID);
			this.userIdentifier = new UserIdentifier(userIdentifierType, userIdentifier);
		}
	}

	public void characters(char[] ch, int start, int length) throws SAXException {
		textBuilder.append(ch, start, length);
	}

	public void endElement(String namespaceUri, String localName, String qName) throws SAXException {
		if (isElement(ATTRIBUTE, namespaceUri, localName)) {
			String text = textBuilder.toString();
			text = text.trim();
			text = "".equals(text) ? null : text;

			if (text != null) {
				addUserAttributeValue(text);
				++userAttributeCount;
			}
			simpleIdentifier = null;

		} else if (isElement(COMPOUND, namespaceUri, localName)) {
			compoundIdentifier = null;
			instanceIdentifier = null;

		} else if (isElement(USER, namespaceUri, localName)) {
			logger.info("Processed {} attributes for {} in {}.", new Object[] {userAttributeCount, userIdentifier, userStopWatch});
			userIdentifier = null;
			++totalUserCount;
			totalAttributeCount += userAttributeCount;
			userAttributeCount = 0;
		}
	}

	private boolean isElement(String desiredName, String namespaceUri, String localName) {
		return defaultNamespaceUri.equals(namespaceUri) && localName.equals(desiredName);
	}

	private void addUserAttributeValue(String value) {
		IUserAttributeValue userAttributeValue = userAttributeValueService.putUserAttributeValue(userIdentifier,
				compoundIdentifier, instanceIdentifier, simpleIdentifier, value);
		if (userAttributeValue.getUserAttribute().isCompoundUserAttribute()) {
			instanceIdentifier = userAttributeValue.getInstanceIdentifier();
		}
	}
}
