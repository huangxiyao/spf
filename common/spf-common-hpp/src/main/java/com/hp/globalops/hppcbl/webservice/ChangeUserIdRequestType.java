/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: ChangeUserIdRequestType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class ChangeUserIdRequestType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class ChangeUserIdRequestType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _sessionToken
     */
    private java.lang.String _sessionToken;

    /**
     * Field _newUserId
     */
    private java.lang.String _newUserId;

    /**
     * Field _currentPassword
     */
    private java.lang.String _currentPassword;


      //----------------/
     //- Constructors -/
    //----------------/

    public ChangeUserIdRequestType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.ChangeUserIdRequestType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'currentPassword'.
     * 
     * @return String
     * @return the value of field 'currentPassword'.
     */
    public java.lang.String getCurrentPassword()
    {
        return this._currentPassword;
    } //-- java.lang.String getCurrentPassword() 

    /**
     * Returns the value of field 'newUserId'.
     * 
     * @return String
     * @return the value of field 'newUserId'.
     */
    public java.lang.String getNewUserId()
    {
        return this._newUserId;
    } //-- java.lang.String getNewUserId() 

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
     * Sets the value of field 'currentPassword'.
     * 
     * @param currentPassword the value of field 'currentPassword'.
     */
    public void setCurrentPassword(java.lang.String currentPassword)
    {
        this._currentPassword = currentPassword;
    } //-- void setCurrentPassword(java.lang.String) 

    /**
     * Sets the value of field 'newUserId'.
     * 
     * @param newUserId the value of field 'newUserId'.
     */
    public void setNewUserId(java.lang.String newUserId)
    {
        this._newUserId = newUserId;
    } //-- void setNewUserId(java.lang.String) 

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
