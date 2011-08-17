package com.hp.spp.wsrp.export.info.vignette;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.hp.spp.wsrp.export.exception.BadConstructorException;
import com.hp.spp.wsrp.export.util.DOMUtils;

public class PortletInfo {

	public static final String PORTLET = "portlet" ;
	public static final String UID = "uid" ;
	public static final String HANDLE = "handle" ;

	private String mUID;
	private String mHandle;
	
	public PortletInfo(String uid, String handle) {
		mUID = uid ;
		mHandle = handle ;
	}
	
	public PortletInfo(Element portlet) throws BadConstructorException {
		String uid = DOMUtils.getText(portlet, UID);
		if (uid == null || uid.trim().equals("")) {
			throw new BadConstructorException("Portlet UID not found");
		}
		String handle = DOMUtils.getText(portlet, HANDLE);
		if (handle == null || handle.trim().equals("")) {
			throw new BadConstructorException("Portlet handle not found");
		}
		mUID = uid ;
		mHandle = handle ;
	}

	public String getHandle() {
		return mHandle;
	}

	public void setHandle(String handle) {
		mHandle = handle;
	}

	public String getUID() {
		return mUID;
	}

	public void setUID(String uid) {
		mUID = uid;
	}
	
	public Element toElement(Document doc) {
		Element portlet = doc.createElement(PORTLET);
		portlet.appendChild(DOMUtils.createTextNode(doc, UID, mUID));
		portlet.appendChild(DOMUtils.createTextNode(doc, HANDLE, mHandle));
		return portlet;
	}

}
