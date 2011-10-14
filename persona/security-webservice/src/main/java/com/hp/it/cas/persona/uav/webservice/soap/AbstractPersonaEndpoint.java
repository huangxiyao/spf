package com.hp.it.cas.persona.uav.webservice.soap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.ws.server.endpoint.AbstractDomPayloadEndpoint;
import org.springframework.xml.namespace.SimpleNamespaceContext;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.hp.it.cas.persona.uav.service.EUserIdentifierType;
import com.hp.it.cas.persona.uav.service.IUserAttributeValue;
import com.hp.it.cas.persona.uav.service.IUserIdentifier;
import com.hp.it.cas.persona.uav.service.UserIdentifier;

abstract class AbstractPersonaEndpoint extends AbstractDomPayloadEndpoint {
	
	private static final String USER_TYPE	= "userType";
	private static final String USER_ID		= "userId";
	private static final String ATTRIBUTE	= "attribute";
	private static final String SIMPLE_ID	= "simpleId";
	private static final String COMPOUND_ID = "compoundId";
	private static final String INSTANCE_ID	= "instance";

	private final String defaultNamespaceUri;	
	private final XPath xpath;
	private final XPathExpression attributeExpression;
	private final XPathExpression textExpression;
	
	protected AbstractPersonaEndpoint(String defaultNamespaceUri) throws XPathExpressionException {
		this.defaultNamespaceUri = defaultNamespaceUri;
		
		setNamespaceAware(true);
		setValidating(true);

		SimpleNamespaceContext namespaceContext = new SimpleNamespaceContext();
		namespaceContext.bindDefaultNamespaceUri(defaultNamespaceUri);
		
		xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(namespaceContext);		

		attributeExpression = xpath.compile("./:attribute");
		textExpression = xpath.compile("text()");
	}
	
	protected void addUserAttributeValue(Element parent, IUserAttributeValue userAttributeValue) {
		Element attributeElement = addElement(parent, ATTRIBUTE);
		
		putAttribute(attributeElement, USER_TYPE,	userAttributeValue.getUserIdentifier().getUserIdentifierType());
		putAttribute(attributeElement, USER_ID,		userAttributeValue.getUserIdentifier().getUserIdentifier());
		putAttribute(attributeElement, SIMPLE_ID,	userAttributeValue.getUserAttribute().getSimpleUserAttributeIdentifier());
		putAttribute(attributeElement, INSTANCE_ID,	userAttributeValue.getInstanceIdentifier());
		
		if (userAttributeValue.getUserAttribute().isCompoundUserAttribute()) {
			putAttribute(attributeElement, COMPOUND_ID, userAttributeValue.getUserAttribute().getCompoundUserAttributeIdentifier());
		}
		
		addText(attributeElement, userAttributeValue.getValue());
	}
	
	protected IUserAttributeValue createUserAttributeValue(Element element) throws XPathExpressionException {
		return new UserAttributeValue(
				getUserIdentifier(element),
				getStringAttributeValue(element, COMPOUND_ID),
				getStringAttributeValue(element, SIMPLE_ID),
				getStringAttributeValue(element, INSTANCE_ID),
				getText(element));
	}

	protected NodeList getUserAttributeValueElements(Element parent) throws XPathExpressionException {
		return (NodeList) attributeExpression.evaluate(parent, XPathConstants.NODESET);
	}
	
	protected Element addElement(Node parent, String elementName) {
		Element child = getDocument(parent).createElementNS(defaultNamespaceUri, elementName);
		parent.appendChild(child);
		return child;
	}
	
	protected IUserIdentifier getUserIdentifier(Element element) {
		String userIdentifierTypeString = getStringAttributeValue(element, USER_TYPE);
		String userIdentifierString = getStringAttributeValue(element, USER_ID);
		EUserIdentifierType userIdentifierType = EUserIdentifierType.valueOf(userIdentifierTypeString);
		IUserIdentifier userIdentifier = new UserIdentifier(userIdentifierType, userIdentifierString);
		return userIdentifier;
	}

	private String getText(Element parent) throws XPathExpressionException {
		String text = textExpression.evaluate(parent);
		return "".equals(text) ? null : text;
	}
	
	private void putAttribute(Element element, String attributeName, Object attributeValue) {
		// attributes for the default namespace must not be created with a NS (standard XML)
		Attr attribute = getDocument(element).createAttribute(attributeName);
		attribute.setValue(String.valueOf(attributeValue));
		element.setAttributeNodeNS(attribute);
	}
	
	private void addText(Node element, Object value) {
		element.appendChild(element.getOwnerDocument().createTextNode(String.valueOf(value)));
	}
	
	private Document getDocument(Node node) {
		return node.getNodeType() == Node.DOCUMENT_NODE ? (Document) node : node.getOwnerDocument();
	}
	
	private String getStringAttributeValue(Element element, String attributeName) {
		Attr attribute = element.getAttributeNode(attributeName);
		return attribute == null ? null : attribute.getValue();
	}	
}
