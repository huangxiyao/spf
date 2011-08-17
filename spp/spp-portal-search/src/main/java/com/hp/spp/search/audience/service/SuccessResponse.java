/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9</a>, using an XML
 * Schema.
 * $Id: SuccessResponse.java,v 1.1.1.1 2006/09/19 14:49:43 akashs Exp $
 */

package com.hp.spp.search.audience.service;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

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
 * Class SuccessResponse.
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2006/09/19 14:49:43 $
 */
public class SuccessResponse implements java.io.Serializable {

	// --------------------------/
	// - Class/Member Variables -/
	// --------------------------/

	/**
	 * Field _itemList
	 */
	private com.hp.spp.search.common.ItemList mItemList;

	// ----------------/
	// - Constructors -/
	// ----------------/

	public SuccessResponse() {
		super();
	} // -- org.sits.com.SuccessResponse()

	// -----------/
	// - Methods -/
	// -----------/

	/**
	 * Returns the value of field 'itemList'.
	 * 
	 * @return ItemList
	 * @return the value of field 'itemList'.
	 */
	public com.hp.spp.search.common.ItemList getItemList() {
		return this.mItemList;
	} // -- org.sits.com.ItemList getItemList()

	/**
	 * Method isValid
	 * 
	 * 
	 * 
	 * @return boolean
	 */
	public boolean isValid() {
		try {
			validate();
		} catch (org.exolab.castor.xml.ValidationException vex) {
			return false;
		}
		return true;
	} // -- boolean isValid()

	/**
	 * Method marshal
	 * 
	 * 
	 * 
	 * @param out
	 */
	public void marshal(java.io.Writer out)
			throws org.exolab.castor.xml.MarshalException,
			org.exolab.castor.xml.ValidationException {

		Marshaller.marshal(this, out);
	} // -- void marshal(java.io.Writer)

	/**
	 * Method marshal
	 * 
	 * 
	 * 
	 * @param handler
	 */
	public void marshal(org.xml.sax.ContentHandler handler)
			throws java.io.IOException, org.exolab.castor.xml.MarshalException,
			org.exolab.castor.xml.ValidationException {

		Marshaller.marshal(this, handler);
	} // -- void marshal(org.xml.sax.ContentHandler)

	/**
	 * Sets the value of field 'itemList'.
	 * 
	 * @param itemList
	 *            the value of field 'itemList'.
	 */
	public void setItemList(com.hp.spp.search.common.ItemList itemList) {
		this.mItemList = itemList;
	} // -- void setItemList(org.sits.com.ItemList)

	/**
	 * Method unmarshal
	 * 
	 * 
	 * 
	 * @param reader
	 * @return SuccessResponse
	 */
	public static com.hp.spp.search.audience.service.SuccessResponse unmarshal(
			java.io.Reader reader)
			throws org.exolab.castor.xml.MarshalException,
			org.exolab.castor.xml.ValidationException {
		return (com.hp.spp.search.audience.service.SuccessResponse) Unmarshaller
				.unmarshal(
						com.hp.spp.search.audience.service.SuccessResponse.class,
						reader);
	} // -- org.sits.com.SuccessResponse unmarshal(java.io.Reader)

	/**
	 * Method validate
	 * 
	 */
	public void validate() throws org.exolab.castor.xml.ValidationException {
		org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
		validator.validate(this);
	} // -- void validate()

}
