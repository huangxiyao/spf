/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: ValidatePinRequestType.java,v 1.1 2006/11/08 07:34:26 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class ValidatePinRequestType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:26 $
 */
public class ValidatePinRequestType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _guid
     */
    private java.lang.String _guid;

    /**
     * Field _pin
     */
    private java.lang.String _pin;

    /**
     * Field _newPassword
     */
    private java.lang.String _newPassword;

    /**
     * Field _newPasswordConfirm
     */
    private java.lang.String _newPasswordConfirm;

    /**
     * Field _securityQuestion
     */
    private java.lang.String _securityQuestion;

    /**
     * Field _securityAnswer
     */
    private java.lang.String _securityAnswer;


      //----------------/
     //- Constructors -/
    //----------------/

    public ValidatePinRequestType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.ValidatePinRequestType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'guid'.
     * 
     * @return String
     * @return the value of field 'guid'.
     */
    public java.lang.String getGuid()
    {
        return this._guid;
    } //-- java.lang.String getGuid() 

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
     * Returns the value of field 'pin'.
     * 
     * @return String
     * @return the value of field 'pin'.
     */
    public java.lang.String getPin()
    {
        return this._pin;
    } //-- java.lang.String getPin() 

    /**
     * Returns the value of field 'securityAnswer'.
     * 
     * @return String
     * @return the value of field 'securityAnswer'.
     */
    public java.lang.String getSecurityAnswer()
    {
        return this._securityAnswer;
    } //-- java.lang.String getSecurityAnswer() 

    /**
     * Returns the value of field 'securityQuestion'.
     * 
     * @return String
     * @return the value of field 'securityQuestion'.
     */
    public java.lang.String getSecurityQuestion()
    {
        return this._securityQuestion;
    } //-- java.lang.String getSecurityQuestion() 

    /**
     * Sets the value of field 'guid'.
     * 
     * @param guid the value of field 'guid'.
     */
    public void setGuid(java.lang.String guid)
    {
        this._guid = guid;
    } //-- void setGuid(java.lang.String) 

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
     * Sets the value of field 'pin'.
     * 
     * @param pin the value of field 'pin'.
     */
    public void setPin(java.lang.String pin)
    {
        this._pin = pin;
    } //-- void setPin(java.lang.String) 

    /**
     * Sets the value of field 'securityAnswer'.
     * 
     * @param securityAnswer the value of field 'securityAnswer'.
     */
    public void setSecurityAnswer(java.lang.String securityAnswer)
    {
        this._securityAnswer = securityAnswer;
    } //-- void setSecurityAnswer(java.lang.String) 

    /**
     * Sets the value of field 'securityQuestion'.
     * 
     * @param securityQuestion the value of field 'securityQuestion'
     */
    public void setSecurityQuestion(java.lang.String securityQuestion)
    {
        this._securityQuestion = securityQuestion;
    } //-- void setSecurityQuestion(java.lang.String) 

}
