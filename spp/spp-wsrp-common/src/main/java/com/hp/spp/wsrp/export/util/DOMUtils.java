package com.hp.spp.wsrp.export.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DOMUtils {

	public static final String ROOT = "//" ;
	public static final String CHILD = "/" ;
	public static final String ATTRIBUTE = "@" ;

	public static final String ERROR = "error" ;
	public static final String INFO = "info" ;

	public static String getStringValue(Node node) {
		if (node != null && node.hasChildNodes()) {
			node.normalize();
			return node.getFirstChild().getNodeValue();
		} else {
			return "";
		}
//		return node.getStringValue() ;
	}

	public static String getText(Element parentElement, String childElementWithTextName) {
		NodeList nl = parentElement.getElementsByTagName(childElementWithTextName);
		if (nl == null || nl.getLength() == 0) {
			return null;
		}
		return getStringValue(nl.item(0));
	}

	public static Node selectSingleNode(Node node, String descriptor) {
		List nodes = selectNodes(node, descriptor) ;
		if(nodes != null && !nodes.isEmpty())
			return (Node) nodes.get(0) ;
		return null;
//		return node.selectSingleNode(ROOT.concat(descriptor)) ;
	}

	public static List selectNodes(Node node, String descriptor) {
		List list = null ;

		if(node != null && node.hasChildNodes()) {
			NodeList nodeList = node.getChildNodes() ;
			for(int index = 0, len = nodeList.getLength(); index < len; index++) {
				Node element = nodeList.item(index) ;
				if(descriptor.equals(element.getNodeName())) {
					if(list == null)
						list = new ArrayList() ;
					list.add(element) ;
				}
			}
		}

		return list;
//		return node.selectNodes(ROOT.concat(descriptor)) ;
	}

	public static String getAttribute(Element element, String attribute) {
		return element.getAttribute(attribute) ;
//		return element.valueOf(ATTRIBUTE.concat(attribute));
	}

	public static Document newDocument() throws ParserConfigurationException, FactoryConfigurationError {
		return  newDocumentBuilder(false).newDocument();
	}

	public static Document initializeDocument(InputStream stream) throws ParserConfigurationException, SAXException, IOException {
		return newDocumentBuilder(false).parse(stream);
	}

	public static Document initializeDocument(InputSource source) throws ParserConfigurationException, SAXException, IOException {
		return newDocumentBuilder(false).parse(source);
	}

	public static Document initializeDocument(Reader reader) throws ParserConfigurationException, SAXException, IOException {
		return initializeDocument(new InputSource(reader));
	}

	private static DocumentBuilder newDocumentBuilder(boolean validating) throws ParserConfigurationException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setValidating(validating);
		DocumentBuilder builder = domFactory.newDocumentBuilder();
//		builder.setEntityResolver(new EntityResolver());

		return builder;
	}

	public static Transformer newTransformer() throws TransformerConfigurationException, TransformerFactoryConfigurationError {
		return TransformerFactory.newInstance().newTransformer();
	}

	public static Transformer newTransformer(InputStream input) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
		return TransformerFactory.newInstance().newTransformer(new StreamSource(input));
	}

	public static Transformer newTransformer(Reader reader) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
		return TransformerFactory.newInstance().newTransformer(new StreamSource(reader));
	}

	public static Transformer newTransformer(Source source) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
		return TransformerFactory.newInstance().newTransformer(source);
	}

	public static TransformerFactory newTransformerFactory() {
		return TransformerFactory.newInstance();
	}

	public static Element createTextNode(Document doc, String tagName, String textValue) {
		Element result = doc.createElement(tagName);
		result.appendChild(doc.createTextNode(textValue));
		return result;
	}

	public static Element createInfo(Document document, String infoMessage) {
		return createTextNode(document, INFO, infoMessage) ;
	}

	public static Element createError(Document document, String errorMessage) {
		return createTextNode(document, ERROR, errorMessage) ;
	}

	public static void createInfo(Document document, Element element, String infoMessage) {
		element.appendChild(createInfo(document, infoMessage)) ;
	}

	public static void createError(Document document, Element element, String errorMessage) {
		element.appendChild(createError(document, errorMessage)) ;
	}

	public static void transform(Document document, Writer output) throws TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError {
		// use the identity transformer to create stream representation of the document
		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(output);
		
		InputStream is = null ;
		try {
			is = DOMUtils.class.getResourceAsStream("/com/hp/spp/wsrp/export/indentXMLFile.xsl");
			newTransformer(is).transform(source, result);
		} catch (Exception e) {
			newTransformer().transform(source, result);
		} catch (Error e) {
			newTransformer().transform(source, result);
		} finally {
			if(is != null) {
				try {
					is.close() ;
				} catch (IOException e) {
				}
			}
		}
		
	}

}
