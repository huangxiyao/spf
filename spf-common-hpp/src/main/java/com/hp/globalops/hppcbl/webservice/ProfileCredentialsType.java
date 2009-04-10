/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: ProfileCredentialsType.java,v 1.1 2006/11/08 07:34:26 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class ProfileCredentialsType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:26 $
 */
public class ProfileCredentialsType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _password
     */
    private java.lang.String _password;

    /**
     * Field _passwordConfirm
     */
    private java.lang.String _passwordConfirm;


      //----------------/
     //- Constructors -/
    //----------------/

    public ProfileCredentialsType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.ProfileCredentialsType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'password'.
     * 
     * @return String
     * @return the value of field 'password'.
     */
    public java.lang.String getPassword()
    {
        return this._password;
    } //-- java.lang.String getPassword() 

    /**
     * Returns the value of field 'passwordConfirm'.
     * 
     * @return String
     * @return the value of field 'passwordConfirm'.
     */
    public java.lang.String getPasswordConfirm()
    {
        return this._passwordConfirm;
    } //-- java.lang.String getPasswordConfirm() 

    /**
     * Sets the value of field 'password'.
     * 
     * @param password the value of field 'password'.
     */
    public void setPassword(java.lang.String password)
    {
        this._password = password;
    } //-- void setPassword(java.lang.String) 

    /**
     * Sets the value of field 'passwordConfirm'.
     * 
     * @param passwordConfirm the value of field 'passwordConfirm'.
     */
    public void setPasswordConfirm(java.lang.String passwordConfirm)
    {
        this._passwordConfirm = passwordConfirm;
    } //-- void setPasswordConfirm(java.lang.String) 

}
