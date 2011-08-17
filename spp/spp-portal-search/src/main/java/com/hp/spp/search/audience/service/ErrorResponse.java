/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9</a>, using an XML
 * Schema.
 * $Id: ErrorResponse.java,v 1.1.1.1 2006/09/19 14:49:42 akashs Exp $
 */

package com.hp.spp.search.audience.service;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

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
 * Class ErrorResponse.
 * 
 * @version $Revision: 1.1.1.1 $ $Date: 2006/09/19 14:49:42 $
 */
public class ErrorResponse implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _type
     */
    //private com.hp.spp.search.audience.service.ErrorResponseType mType;
	private String mType = null;

    /**
     * Field _code
     */
    private java.lang.String _code;

    /**
     * Field _description
     */
    private java.lang.String _description;


      //----------------/
     //- Constructors -/
    //----------------/

    public ErrorResponse() 
     {
        super();
    } //-- org.sits.com.ErrorResponse()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'code'.
     * 
     * @return String
     * @return the value of field 'code'.
     */
    public java.lang.String getCode()
    {
        return this._code;
    } //-- java.lang.String getCode() 

    /**
     * Returns the value of field 'description'.
     * 
     * @return String
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return ErrorResponseTypeType
     * @return the value of field 'type'.
     
    public org.sits.com.types.ErrorResponseType getType()
    {
        return this._type;
    } //-- org.sits.com.types.ErrorResponseTypeType getType() 
    */
    /**
     * Method isValid
     * 
     * 
     * 
     * @return boolean
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * 
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'code'.
     * 
     * @param code the value of field 'code'.
     */
    public void setCode(java.lang.String code)
    {
        this._code = code;
    } //-- void setCode(java.lang.String) 

    /**
     * Sets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    /*
    public void setType(org.sits.com.types.ErrorResponseTypeType type)
    {
        this._type = type;
    } //-- void setType(org.sits.com.types.ErrorResponseTypeType) 
*/
    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return ErrorResponse
     */
    public static com.hp.spp.search.audience.service.ErrorResponse unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.hp.spp.search.audience.service.ErrorResponse) Unmarshaller.unmarshal(com.hp.spp.search.audience.service.ErrorResponse.class, reader);
    } //-- org.sits.com.ErrorResponse unmarshal(java.io.Reader) 

    /**
     * Method validate
     * 
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
