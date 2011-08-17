package com.hp.spp.wsrp.export;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.hp.spp.wsrp.export.exception.CommonException;
import com.hp.spp.wsrp.export.util.DOMUtils;

abstract class Common implements ICommon {

	public void write(Document document, Writer output) throws CommonException {
		try {
			DOMUtils.transform(document, output);
		} catch (TransformerConfigurationException e) {
			throw new CommonException("TransformerConfigurationException encountered while transforming DOMSource to StreamResult.", e);
		} catch (TransformerException e) {
			throw new CommonException("TransformerException encountered while transforming DOMSource to StreamResult.", e);
		} catch (TransformerFactoryConfigurationError e) {
			throw new CommonException("TransformerFactoryConfigurationError encountered while transforming DOMSource to StreamResult.", e);
		}
	}

	Transformer newTransformer() throws CommonException {
		try {
			return DOMUtils.newTransformer();
		} catch (TransformerConfigurationException e) {
			throw new CommonException("TransformerConfigurationException encountered while transforming DOMSource to StreamResult.", e);
		} catch (TransformerFactoryConfigurationError e) {
			throw new CommonException("TransformerFactoryConfigurationError encountered while transforming DOMSource to StreamResult.", e);
		}
	}

	Document newDocument() throws CommonException {
		try {
			return DOMUtils.newDocument();
		} catch (ParserConfigurationException e) {
			throw new CommonException("ParserConfigurationException encountered while creating DocumentBuilder.", e);
		} catch (FactoryConfigurationError e) {
			throw new CommonException("FactoryConfigurationError encountered while creating DocumentBuilder.", e);
		}
	}

	Document initializeDocument(Reader input) throws CommonException {
		try {
			return DOMUtils.initializeDocument(input);
		} catch (SAXException e) {
			throw new CommonException("SAXException encountered while creating Document.", e);
		} catch (IOException e) {
			throw new CommonException("IOException encountered while creating Document.", e);
		} catch (ParserConfigurationException e) {
			throw new CommonException("ParserConfigurationException encountered while creating DocumentBuilder.", e);
		} catch (FactoryConfigurationError e) {
			throw new CommonException("FactoryConfigurationError encountered while transforming DOMSource to StreamResult.", e);
		}
	}

	Element createInfo(Document document, String infoMessage) {
		return DOMUtils.createInfo(document, infoMessage) ;
	}

	Element createError(Document document, String errorMessage) {
		return DOMUtils.createError(document, errorMessage) ;
	}

	void createInfo(Document document, Element element, String infoMessage) {
		DOMUtils.createInfo(document, element, infoMessage) ;
	}

	void createError(Document document, Element element, String errorMessage) {
		DOMUtils.createError(document, element, errorMessage) ;
	}

	List splitIntoSubLists(List list, int maxSublistSize) {
		List result = new ArrayList();
		if (list.size() <= maxSublistSize) {
			result.add(list);
		}
		else {
			int numOfSublists = list.size() / maxSublistSize;
			for (int i = 0; i < numOfSublists; ++i) {
				result.add(list.subList(i*maxSublistSize, (i+1)*maxSublistSize));
			}
			if (list.size() % maxSublistSize != 0) {
				result.add(list.subList(numOfSublists * maxSublistSize, list.size()));
			}
		}

		return result;
	}

}
