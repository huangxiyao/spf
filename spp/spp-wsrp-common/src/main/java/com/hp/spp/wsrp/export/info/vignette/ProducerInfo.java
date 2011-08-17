package com.hp.spp.wsrp.export.info.vignette;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hp.spp.wsrp.export.exception.BadConstructorException;
import com.hp.spp.wsrp.export.util.DOMUtils;

public class ProducerInfo {

	public static final String PRODUCER = "producer" ;
	public static final String IMPORT_ID = "import-id" ;
	public static final String URL = "url" ;

	private String mImportId;
	private String mUrl;
	private Map mPortlets = new TreeMap();
	
	public ProducerInfo(String importId, String url) {
		mImportId = importId ;
		mUrl = url ;
	}

	public ProducerInfo(Element producer) throws BadConstructorException {
		String importId = DOMUtils.getText(producer, IMPORT_ID);
		if (importId == null || importId.trim().equals("")) {
			throw new BadConstructorException("Producer import id not found");
		}
		String url = DOMUtils.getText(producer, URL);
		if (url == null || url.trim().equals("")) {
			throw new BadConstructorException("Producer url not found");
		}
		List portlets = DOMUtils.selectNodes(producer, PortletInfo.PORTLET) ;
		if(portlets == null || portlets.isEmpty()) {
			throw new BadConstructorException("Producer portlet list not found");
		} else {
			for(int i = 0, len = portlets.size(); i < len; ++i) {
				PortletInfo portletInfo = new PortletInfo((Element) portlets.get(i)) ;
				mPortlets.put(portletInfo.getUID(), portletInfo);
			}
		}
		mImportId = importId ;
		mUrl = url ;
	}

	public String getImportId() {
		return mImportId ;
	}
	
	public String getUrl() {
		return mUrl ;
	}
	
	public void addPortlet(String uid, String handle) {
		PortletInfo portletInfo = new PortletInfo(uid, handle) ;
		mPortlets.put(uid, portletInfo);
	}

	public boolean hasPortlets() {
		return mPortlets != null && !mPortlets.isEmpty();
	}

	public Map getPortlets() {
		return Collections.unmodifiableMap(mPortlets);
	}

	public Set getPortletUIDs() {
		return getPortlets().keySet();
	}

	public Element toElement(Document doc) {
		Element producer = doc.createElement(PRODUCER);
		producer.appendChild(DOMUtils.createTextNode(doc, IMPORT_ID, mImportId));
		producer.appendChild(DOMUtils.createTextNode(doc, URL, mUrl));
		if (hasPortlets()) {
			for (Iterator it = mPortlets.values().iterator(); it.hasNext();) {
				PortletInfo portletInfo = (PortletInfo) it.next() ;
				producer.appendChild(portletInfo.toElement(doc));
			}
		}
		return producer;
	}

}
