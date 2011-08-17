package com.hp.spp.wsrp.export.info.producer;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import com.hp.spp.wsrp.export.exception.BadConstructorException;
import com.hp.spp.wsrp.export.util.DOMUtils;

public class PreferenceInfo {

	public static final String PREFERENCE = "preference" ;
	public static final String NAME = "name" ;
	public static final String READ_ONLY = "read-only" ;
	public static final String VALUE = "value" ;

	private String mName;
	private List mValues;
	private String mReadOnly;

	public PreferenceInfo(String name, String value, String readOnly) {
		mName = name;
		mValues = new ArrayList(1);
		mValues.add(value);
		mReadOnly = readOnly;
	}

	public PreferenceInfo(Element preference) throws BadConstructorException {
		String name = DOMUtils.getText(preference, NAME);
		if (name == null || name.trim().equals("")) {
			throw new BadConstructorException("Preference name not found");
		}
		String readOnly = DOMUtils.getText(preference, READ_ONLY);
		if (readOnly == null || readOnly.trim().equals("")) {
			throw new BadConstructorException("Preference readOnly not found");
		}
		mValues = new ArrayList();
		NodeList values = preference.getElementsByTagName(VALUE);
		if (values != null && values.getLength() > 0) {
			for (int i = 0, len = values.getLength(); i < len; ++i) {
				mValues.add(DOMUtils.getStringValue(values.item(i)));
			}
		}
		mName = name;
		mReadOnly = readOnly;
	}

	public void addValue(String value) {
		mValues.add(value);
	}

	public String getName() {
		return mName;
	}

	public List getValues() {
		return mValues;
	}

	public String getReadOnly() {
		return mReadOnly;
	}

	public Element toElement(Document doc) {
		Element preference = doc.createElement(PREFERENCE);
		preference.appendChild(DOMUtils.createTextNode(doc, NAME, mName));
		preference.appendChild(DOMUtils.createTextNode(doc, READ_ONLY, mReadOnly));
		for (Iterator it = mValues.iterator(); it.hasNext();) {
			String value = (String) it.next();
			preference.appendChild(DOMUtils.createTextNode(doc, VALUE, value));
		}
		return preference;
	}
}
