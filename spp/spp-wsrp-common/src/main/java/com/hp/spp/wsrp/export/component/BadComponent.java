package com.hp.spp.wsrp.export.component;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hp.spp.wsrp.export.util.DOMUtils;

public class BadComponent {

	private String mMessage = null ;
	
	public BadComponent(String message) {
		super() ;
		mMessage = message;
	}

	public String getMessage() {
		return mMessage;
	}

	public void setMessage(String message) {
		mMessage = message;
	}
	
	public Element toElement(Document document) {
		return DOMUtils.createError(document, mMessage) ;
	}
}
