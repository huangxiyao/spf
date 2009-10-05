/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: ChangePasswordRequestType.java,v 1.1 2006/11/08 07:34:24 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class ChangePasswordRequestType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:24 $
 */
public class ChangePasswordRequestType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _userId
     */
    private java.lang.String _userId;

    /**
     * Field _currentPassword
     */
    private java.lang.String _currentPassword;

    /**
     * Field _newPassword
     */
    private java.lang.String _newPassword;

    /**
     * Field _newPasswordConfirm
     */
    private java.lang.String _newPasswordConfirm;


      //----------------/
     //- Constructors -/
    //----------------/

    public ChangePasswordRequestType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.ChangePasswordRequestType()


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
     * Returns the value of field 'newPassword'.
     * 
     * @return String
     * @return the value of field 'newPassword'.
     */
    public java.lang.String getNewPassword()
    {
        return this._newPassword;
    } //-- java.lang.String getNewPassword() 

    /**
     * Returns the value of field 'newPasswordConfirm'.
     * 
     * @return String
     * @return the value of field 'newPasswordConfirm'.
     */
    public java.lang.String getNewPasswordConfirm()
    {
        return this._newPasswordConfirm;
    } //-- java.lang.String getNewPasswordConfirm() 

    /**
     * Returns the value of field 'userId'.
     * 
     * @return String
     * @return the value of field 'userId'.
     */
    public java.lang.String getUserId()
    {
        return this._userId;
    } //-- java.lang.String getUserId() 

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
     * Sets the value of field 'newPassword'.
     * 
     * @param newPassword the value of field 'newPassword'.
     */
    public void setNewPassword(java.lang.String newPassword)
    {
        this._newPassword = newPassword;
    } //-- void setNewPassword(java.lang.String) 

    /**
     * Sets the value of field 'newPasswordConfirm'.
     * 
     * @param newPasswordConfirm the value of field
     * 'newPasswordConfirm'.
     */
    public void setNewPasswordConfirm(java.lang.String newPasswordConfirm)
    {
        this._newPasswordConfirm = newPasswordConfirm;
    } //-- void setNewPasswordConfirm(java.lang.String) 

    /**
     * Sets the value of field 'userId'.
     * 
     * @param userId the value of field 'userId'.
     */
    public void setUserId(java.lang.String userId)
    {
        this._userId = userId;
    } //-- void setUserId(java.lang.String) 

}
