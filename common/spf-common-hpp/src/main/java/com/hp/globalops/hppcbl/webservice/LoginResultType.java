/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: LoginResultType.java,v 1.1 2006/11/08 07:34:26 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class LoginResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:26 $
 */
public class LoginResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _sessionToken
     */
    private java.lang.String _sessionToken;


      //----------------/
     //- Constructors -/
    //----------------/

    public LoginResultType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.LoginResultType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'sessionToken'.
     * 
     * @return String
     * @return the value of field 'sessionToken'.
     */
    public java.lang.String getSessionToken()
    {
        return this._sessionToken;
    } //-- java.lang.String getSessionToken() 

    /**
     * Sets the value of field 'sessionToken'.
     * 
     * @param sessionToken the value of field 'sessionToken'.
     */
    public void setSessionToken(java.lang.String sessionToken)
    {
        this._sessionToken = sessionToken;
    } //-- void setSessionToken(java.lang.String) 

}
