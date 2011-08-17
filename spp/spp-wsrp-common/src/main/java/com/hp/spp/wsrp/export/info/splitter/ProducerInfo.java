package com.hp.spp.wsrp.export.info.splitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hp.spp.wsrp.export.exception.BadConstructorException;
import com.hp.spp.wsrp.export.util.DOMUtils;

public class ProducerInfo {

	public static final String ROOT = "portlet-handle-list" ;
	public static final String PRODUCER_INFO = "producer-info" ;
	public static final String IMPORT_ID = "import-id" ;
	public static final String URL = "url" ;
	public static final String HANDLE = "handle" ;

	private String mImportId;
	private String mUrl;
	private List mHandles = new ArrayList();
	
	public ProducerInfo(String importId, String url) {
		mImportId = importId ;
		mUrl = url ;
	}

	public ProducerInfo(Element root) throws BadConstructorException {
		Element producer = (Element) DOMUtils.selectSingleNode(root, PRODUCER_INFO) ;
		String importId = DOMUtils.getText(producer, IMPORT_ID);
		if (importId == null || importId.trim().equals("")) {
			throw new BadConstructorException("Producer import id not found");
		}
		String url = DOMUtils.getText(producer, URL);
		if (url == null || url.trim().equals("")) {
			throw new BadConstructorException("Producer url not found");
		}
		List handles = DOMUtils.selectNodes(root, HANDLE) ;
		if(handles == null || handles.isEmpty()) {
			throw new BadConstructorException("Producer handle list not found");
		} else {
			for(int i = 0, len = handles.size(); i < len; ++i) {
				String handle = DOMUtils.getStringValue((Element) handles.get(i)) ;
				if(handles.equals(handle)) {
					throw new BadConstructorException("Producer handle is empty");
				} else {
					addHandle(handle) ;
				}
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
	
	public void addHandle(String handle) {
		mHandles.add(handle);
	}

	public boolean hasHandles() {
		return mHandles != null && !mHandles.isEmpty();
	}

	public List getHandles() {
		return Collections.unmodifiableList(mHandles);
	}

	public Element toElement(Document doc) {
		Element porletHandles = doc.createElement(ROOT);
		
		Element producerInfo = (Element) porletHandles.appendChild(doc.createElement(PRODUCER_INFO));
		producerInfo.appendChild(DOMUtils.createTextNode(doc, IMPORT_ID, mImportId));
		producerInfo.appendChild(DOMUtils.createTextNode(doc, URL, mUrl));

		if (hasHandles()) {
			for (Iterator it = mHandles.iterator(); it.hasNext();) {
				String handle = (String) it.next();
				porletHandles.appendChild(DOMUtils.createTextNode(doc, HANDLE, handle));
			}
		}

		return porletHandles;
	}

}
