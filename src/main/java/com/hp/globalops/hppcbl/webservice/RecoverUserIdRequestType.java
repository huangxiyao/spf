/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: RecoverUserIdRequestType.java,v 1.1 2006/11/08 07:34:26 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class RecoverUserIdRequestType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:26 $
 */
public class RecoverUserIdRequestType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _firstName
     */
    private java.lang.String _firstName;

    /**
     * Field _lastName
     */
    private java.lang.String _lastName;

    /**
     * Field _emailAddress
     */
    private java.lang.String _emailAddress;


      //----------------/
     //- Constructors -/
    //----------------/

    public RecoverUserIdRequestType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.RecoverUserIdRequestType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'emailAddress'.
     * 
     * @return String
     * @return the value of field 'emailAddress'.
     */
    public java.lang.String getEmailAddress()
    {
        return this._emailAddress;
    } //-- java.lang.String getEmailAddress() 

    /**
     * Returns the value of field 'firstName'.
     * 
     * @return String
     * @return the value of field 'firstName'.
     */
    public java.lang.String getFirstName()
    {
        return this._firstName;
    } //-- java.lang.String getFirstName() 

    /**
     * Returns the value of field 'lastName'.
     * 
     * @return String
     * @return the value of field 'lastName'.
     */
    public java.lang.String getLastName()
    {
        return this._lastName;
    } //-- java.lang.String getLastName() 

    /**
     * Sets the value of field 'emailAddress'.
     * 
     * @param emailAddress the value of field 'emailAddress'.
     */
    public void setEmailAddress(java.lang.String emailAddress)
    {
        this._emailAddress = emailAddress;
    } //-- void setEmailAddress(java.lang.String) 

    /**
     * Sets the value of field 'firstName'.
     * 
     * @param firstName the value of field 'firstName'.
     */
    public void setFirstName(java.lang.String firstName)
    {
        this._firstName = firstName;
    } //-- void setFirstName(java.lang.String) 

    /**
     * Sets the value of field 'lastName'.
     * 
     * @param lastName the value of field 'lastName'.
     */
    public void setLastName(java.lang.String lastName)
    {
        this._lastName = lastName;
    } //-- void setLastName(java.lang.String) 

}
