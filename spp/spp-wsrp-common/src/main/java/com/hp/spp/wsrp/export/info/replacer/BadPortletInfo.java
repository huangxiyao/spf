package com.hp.spp.wsrp.export.info.replacer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hp.spp.wsrp.export.util.DOMUtils;

public class BadPortletInfo {
	
	private String mError;

	public BadPortletInfo(String error) {
		mError = error;
	}

	public Element toElement(Document doc) {
		return DOMUtils.createError(doc, mError) ;
	}

}
