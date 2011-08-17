/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9</a>, using an XML
 * Schema.
 * $Id: AudiencingServiceResponse.java,v 1.1.1.1 2006/09/19 14:49:42 akashs Exp $
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
 * Class AudiencingServiceResponse.
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2006/09/19 14:49:42 $
 */
public class AudiencingServiceResponse implements java.io.Serializable {

	// --------------------------/
	// - Class/Member Variables -/
	// --------------------------/

	/**
	 * Internal choice value storage
	 */
	private java.lang.Object _choiceValue;

	/**
	 * Field _errorResponse
	 */
	private com.hp.spp.search.audience.service.ErrorResponse mErrorResponse;

	/**
	 * Field _successResponse
	 */
	private com.hp.spp.search.audience.service.SuccessResponse mSuccessResponse;

	// ----------------/
	// - Constructors -/
	// ----------------/

	public AudiencingServiceResponse() {
		super();
	} // -- org.sits.com.AudiencingServiceResponse()

	// -----------/
	// - Methods -/
	// -----------/

	/**
	 * Returns the value of field 'choiceValue'. The field 'choiceValue' has the
	 * following description: Internal choice value storage
	 * 
	 * @return Object
	 * @return the value of field 'choiceValue'.
	 */
	public java.lang.Object getChoiceValue() {
		return this._choiceValue;
	} // -- java.lang.Object getChoiceValue()

	/**
	 * Returns the value of field 'errorResponse'.
	 * 
	 * @return ErrorResponse
	 * @return the value of field 'errorResponse'.
	 */
	public com.hp.spp.search.audience.service.ErrorResponse getErrorResponse() {
		return this.mErrorResponse;
	} // -- org.sits.com.ErrorResponse getErrorResponse()

	/**
	 * Returns the value of field 'successResponse'.
	 * 
	 * @return SuccessResponse
	 * @return the value of field 'successResponse'.
	 */
	public com.hp.spp.search.audience.service.SuccessResponse getSuccessResponse() {
		return this.mSuccessResponse;
	} // -- org.sits.com.SuccessResponse getSuccessResponse()

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
	 * Sets the value of field 'errorResponse'.
	 * 
	 * @param errorResponse
	 *            the value of field 'errorResponse'.
	 */
	public void setErrorResponse(
			com.hp.spp.search.audience.service.ErrorResponse errorResponse) {
		this.mErrorResponse = errorResponse;
		this._choiceValue = errorResponse;
	} // -- void setErrorResponse(org.sits.com.ErrorResponse)

	/**
	 * Sets the value of field 'successResponse'.
	 * 
	 * @param successResponse
	 *            the value of field 'successResponse'.
	 */
	public void setSuccessResponse(
			com.hp.spp.search.audience.service.SuccessResponse successResponse) {
		this.mSuccessResponse = successResponse;
		this._choiceValue = successResponse;
	} // -- void setSuccessResponse(org.sits.com.SuccessResponse)

	/**
	 * Method unmarshal
	 * 
	 * 
	 * 
	 * @param reader
	 * @return AudiencingServiceResponse
	 */
	public static com.hp.spp.search.audience.service.AudiencingServiceResponse unmarshal(
			java.io.Reader reader)
			throws org.exolab.castor.xml.MarshalException,
			org.exolab.castor.xml.ValidationException {
		return (com.hp.spp.search.audience.service.AudiencingServiceResponse) Unmarshaller
				.unmarshal(
						com.hp.spp.search.audience.service.AudiencingServiceResponse.class,
						reader);
	} // -- org.sits.com.AudiencingServiceResponse unmarshal(java.io.Reader)

	/**
	 * Method validate
	 * 
	 */
	public void validate() throws org.exolab.castor.xml.ValidationException {
		org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
		validator.validate(this);
	} // -- void validate()

}
