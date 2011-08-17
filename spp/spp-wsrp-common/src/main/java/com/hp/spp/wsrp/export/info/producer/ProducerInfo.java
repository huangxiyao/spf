package com.hp.spp.wsrp.export.info.producer;

import org.w3c.dom.Element;
import org.w3c.dom.Document;

import com.hp.spp.wsrp.export.exception.BadConstructorException;
import com.hp.spp.wsrp.export.util.DOMUtils;

public class ProducerInfo {
	
	public static final String PRODUCER_INFO = "producer-info" ;
	public static final String IMPORT_ID = "import-id" ;
	public static final String URL = "url" ;
	
	private String mUrl;
	private String mImportId;

	public ProducerInfo(Element element) throws BadConstructorException {
		String url = DOMUtils.getText(element, URL);
		if (url == null || url.trim().equals("")) {
			throw new BadConstructorException("Producer url not found");
		}
		String importId = DOMUtils.getText(element, IMPORT_ID);
		if (importId == null || importId.trim().equals("")) {
			throw new BadConstructorException("Producer import id not found");
		}
		mUrl = url;
		mImportId = importId;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url ;
	}

	public String getImportId() {
		return mImportId;
	}

	public Element toElement(Document doc) {
		Element result = doc.createElement(PRODUCER_INFO);
		result.appendChild(DOMUtils.createTextNode(doc, URL, mUrl));
		result.appendChild(DOMUtils.createTextNode(doc, IMPORT_ID, mImportId));
		return result;
	}

}
