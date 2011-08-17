package com.hp.spp.common.util.parsers;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlParsingErrorHandler extends DefaultHandler{
	
	private static Logger logger = Logger.getLogger(XmlParsingErrorHandler.class);

	private boolean existErrors = false;
	
	StringBuffer mMessageBuffer = new StringBuffer();
	
	public boolean isExistErrors() {
		return existErrors;
	}
	
	protected String message(SAXParseException e){
		String message = "Message : "+e.getMessage()+"\n";
		message += "Line "+e.getLineNumber()+", column "+e.getColumnNumber()+"\n";
		return message;
	}

	public void warning(SAXParseException exception) throws SAXException{
		String message = message(exception);
		logger.warn(message, exception);
	}
	
	public void error(SAXParseException exception) throws SAXException{
		existErrors = true;
		String message = message(exception);
		logger.error(message, exception);
		mMessageBuffer.append(message);
	}

	public void fatalError(SAXParseException exception) throws SAXException{
		existErrors = true;
		String message = message(exception);
		SAXException se = new SAXException(message, exception);
		throw se;
	}
	
	public String getErrorMessages()	{
		return mMessageBuffer.toString();
	}

}