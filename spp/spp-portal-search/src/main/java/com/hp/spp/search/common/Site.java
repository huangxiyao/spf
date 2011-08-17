/**
 *
 */
package com.hp.spp.search.common;

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * @author bhujange
 *
 */
public class Site {
	String mId;
	String mName;

	public Site() {
		super();

	}

	/**
	 * @return Returns the mId.
	 */
	public String getId() {
		return mId;
	}
	/**
	 * @param id The mId to set.
	 */
	public void setId(String id) {
		mId = id;
	}
	/**
	 * @return Returns the mName.
	 */
	public String getName() {
		return mName;
	}
	/**
	 * @param name The mName to set.
	 */
	public void setName(String name) {
		mName = name;
	}
}
