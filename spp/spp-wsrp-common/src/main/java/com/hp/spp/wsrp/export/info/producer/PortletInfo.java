package com.hp.spp.wsrp.export.info.producer;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.Map;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Iterator;

import com.hp.spp.wsrp.export.exception.BadConstructorException;
import com.hp.spp.wsrp.export.util.DOMUtils;

public class PortletInfo {
	
	public static final String PORTLET = "portlet" ;
	public static final String HANDLE = "handle" ;
	
	private String mHandle;
	private Map mPreferences = new TreeMap();

	public PortletInfo(String handle) {
		mHandle = handle;
	}

	public PortletInfo(Element portlet) throws BadConstructorException {
		String handle = DOMUtils.getText(portlet, HANDLE);
		if (handle == null || handle.trim().equals("")) {
			throw new BadConstructorException("Portlet handle not found");
		}
		NodeList preferences = portlet.getElementsByTagName(PreferenceInfo.PREFERENCE);
		if (preferences != null && preferences.getLength() > 0) {
			for (int i = 0, len = preferences.getLength(); i < len; ++i) {
				PreferenceInfo prefInfo = new PreferenceInfo((Element) preferences.item(i));
				mPreferences.put(prefInfo.getName(), prefInfo);
			}
		}
		mHandle = handle;
	}

	public String getHandle() {
		return mHandle;
	}

	public void setHandle(String handle) {
		mHandle = handle;
	}

	public void addPreference(String name, String value, String readOnly) {
		if (mPreferences.containsKey(name)) {
			PreferenceInfo prefInfo = (PreferenceInfo) mPreferences.get(name);
			prefInfo.addValue(value);
		}
		else {
			PreferenceInfo prefInfo = new PreferenceInfo(name, value, readOnly);
			mPreferences.put(name, prefInfo);
		}
	}

	public boolean hasPreferences() {
		return mPreferences != null && !mPreferences.isEmpty();
	}

	public Map getPreferences() {
		return Collections.unmodifiableMap(mPreferences);
	}

	public Element toElement(Document doc) {
		Element portlet = doc.createElement(PORTLET);
		portlet.appendChild(DOMUtils.createTextNode(doc, HANDLE, mHandle));
		if (hasPreferences()) {
			for (Iterator it = mPreferences.values().iterator(); it.hasNext();) {
				PreferenceInfo prefInfo = (PreferenceInfo) it.next();
				portlet.appendChild(prefInfo.toElement(doc));
			}
		}

		return portlet;
	}
}
