/**
 *
 */
package com.hp.spp.search.common;

import java.io.IOException;
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
public class User {
	String mId;
	String mName;
	String mHppId;

	public User() {
	    super();
    }

	/**
	 * @return Returns the mHppId.
	 */
	public String getHppId() {
		return mHppId;
	}
	/**
	 * @param hppId The mHppId to set.
	 */
	public void setHppId(String hppId) {
		mHppId = hppId;
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
