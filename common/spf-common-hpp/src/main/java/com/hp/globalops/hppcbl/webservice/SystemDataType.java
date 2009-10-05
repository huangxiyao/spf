/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: SystemDataType.java,v 1.1 2006/11/08 07:34:26 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class SystemDataType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:26 $
 */
public class SystemDataType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _creationAppId
     */
    private java.lang.String _creationAppId;

    /**
     * Field _profileCreationDate
     */
    private java.lang.String _profileCreationDate;

    /**
     * Field _profileLastUpdatedApplicationId
     */
    private java.lang.String _profileLastUpdatedApplicationId;

    /**
     * Field _profileLastUpdatedDate
     */
    private java.lang.String _profileLastUpdatedDate;

    /**
     * Field _profileLastUpdatedProfileId
     */
    private java.lang.String _profileLastUpdatedProfileId;

    /**
     * Field _createdByProfileId
     */
    private java.lang.String _createdByProfileId;

    /**
     * Field _cidNumber
     */
    private java.lang.String _cidNumber;

    /**
     * Field _migrationExceptionFlag
     */
    private java.lang.String _migrationExceptionFlag;

    /**
     * Field _brandedFlag
     */
    private java.lang.String _brandedFlag;

    /**
     * Field _lastSuccessfulLoginAppId
     */
    private java.lang.String _lastSuccessfulLoginAppId;

    /**
     * Field _lastSuccessfulLoginDate
     */
    private java.lang.String _lastSuccessfulLoginDate;

    /**
     * Field _lastAttemptedLoginDate
     */
    private java.lang.String _lastAttemptedLoginDate;

    /**
     * Field _lockoutDate
     */
    private java.lang.String _lockoutDate;

    /**
     * Field _lockoutCounter
     */
    private java.lang.String _lockoutCounter;

    /**
     * Field _passwordResetStatusCode
     */
    private java.lang.String _passwordResetStatusCode;

    /**
     * Field _regPasswordResetCode
     */
    private java.lang.String _regPasswordResetCode;

    /**
     * Field _passwordCreationDate
     */
    private java.lang.String _passwordCreationDate;

    /**
     * Field _changeSecurityQAStatusCode
     */
    private java.lang.String _changeSecurityQAStatusCode;


      //----------------/
     //- Constructors -/
    //----------------/

    public SystemDataType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.SystemDataType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'brandedFlag'.
     * 
     * @return String
     * @return the value of field 'brandedFlag'.
     */
    public java.lang.String getBrandedFlag()
    {
        return this._brandedFlag;
    } //-- java.lang.String getBrandedFlag() 

    /**
     * Returns the value of field 'changeSecurityQAStatusCode'.
     * 
     * @return String
     * @return the value of field 'changeSecurityQAStatusCode'.
     */
    public java.lang.String getChangeSecurityQAStatusCode()
    {
        return this._changeSecurityQAStatusCode;
    } //-- java.lang.String getChangeSecurityQAStatusCode() 

    /**
     * Returns the value of field 'cidNumber'.
     * 
     * @return String
     * @return the value of field 'cidNumber'.
     */
    public java.lang.String getCidNumber()
    {
        return this._cidNumber;
    } //-- java.lang.String getCidNumber() 

    /**
     * Returns the value of field 'createdByProfileId'.
     * 
     * @return String
     * @return the value of field 'createdByProfileId'.
     */
    public java.lang.String getCreatedByProfileId()
    {
        return this._createdByProfileId;
    } //-- java.lang.String getCreatedByProfileId() 

    /**
     * Returns the value of field 'creationAppId'.
     * 
     * @return String
     * @return the value of field 'creationAppId'.
     */
    public java.lang.String getCreationAppId()
    {
        return this._creationAppId;
    } //-- java.lang.String getCreationAppId() 

    /**
     * Returns the value of field 'lastAttemptedLoginDate'.
     * 
     * @return String
     * @return the value of field 'lastAttemptedLoginDate'.
     */
    public java.lang.String getLastAttemptedLoginDate()
    {
        return this._lastAttemptedLoginDate;
    } //-- java.lang.String getLastAttemptedLoginDate() 

    /**
     * Returns the value of field 'lastSuccessfulLoginAppId'.
     * 
     * @return String
     * @return the value of field 'lastSuccessfulLoginAppId'.
     */
    public java.lang.String getLastSuccessfulLoginAppId()
    {
        return this._lastSuccessfulLoginAppId;
    } //-- java.lang.String getLastSuccessfulLoginAppId() 

    /**
     * Returns the value of field 'lastSuccessfulLoginDate'.
     * 
     * @return String
     * @return the value of field 'lastSuccessfulLoginDate'.
     */
    public java.lang.String getLastSuccessfulLoginDate()
    {
        return this._lastSuccessfulLoginDate;
    } //-- java.lang.String getLastSuccessfulLoginDate() 

    /**
     * Returns the value of field 'lockoutCounter'.
     * 
     * @return String
     * @return the value of field 'lockoutCounter'.
     */
    public java.lang.String getLockoutCounter()
    {
        return this._lockoutCounter;
    } //-- java.lang.String getLockoutCounter() 

    /**
     * Returns the value of field 'lockoutDate'.
     * 
     * @return String
     * @return the value of field 'lockoutDate'.
     */
    public java.lang.String getLockoutDate()
    {
        return this._lockoutDate;
    } //-- java.lang.String getLockoutDate() 

    /**
     * Returns the value of field 'migrationExceptionFlag'.
     * 
     * @return String
     * @return the value of field 'migrationExceptionFlag'.
     */
    public java.lang.String getMigrationExceptionFlag()
    {
        return this._migrationExceptionFlag;
    } //-- java.lang.String getMigrationExceptionFlag() 

    /**
     * Returns the value of field 'passwordCreationDate'.
     * 
     * @return String
     * @return the value of field 'passwordCreationDate'.
     */
    public java.lang.String getPasswordCreationDate()
    {
        return this._passwordCreationDate;
    } //-- java.lang.String getPasswordCreationDate() 

    /**
     * Returns the value of field 'passwordResetStatusCode'.
     * 
     * @return String
     * @return the value of field 'passwordResetStatusCode'.
     */
    public java.lang.String getPasswordResetStatusCode()
    {
        return this._passwordResetStatusCode;
    } //-- java.lang.String getPasswordResetStatusCode() 

    /**
     * Returns the value of field 'profileCreationDate'.
     * 
     * @return String
     * @return the value of field 'profileCreationDate'.
     */
    public java.lang.String getProfileCreationDate()
    {
        return this._profileCreationDate;
    } //-- java.lang.String getProfileCreationDate() 

    /**
     * Returns the value of field
     * 'profileLastUpdatedApplicationId'.
     * 
     * @return String
     * @return the value of field 'profileLastUpdatedApplicationId'.
     */
    public java.lang.String getProfileLastUpdatedApplicationId()
    {
        return this._profileLastUpdatedApplicationId;
    } //-- java.lang.String getProfileLastUpdatedApplicationId() 

    /**
     * Returns the value of field 'profileLastUpdatedDate'.
     * 
     * @return String
     * @return the value of field 'profileLastUpdatedDate'.
     */
    public java.lang.String getProfileLastUpdatedDate()
    {
        return this._profileLastUpdatedDate;
    } //-- java.lang.String getProfileLastUpdatedDate() 

    /**
     * Returns the value of field 'profileLastUpdatedProfileId'.
     * 
     * @return String
     * @return the value of field 'profileLastUpdatedProfileId'.
     */
    public java.lang.String getProfileLastUpdatedProfileId()
    {
        return this._profileLastUpdatedProfileId;
    } //-- java.lang.String getProfileLastUpdatedProfileId() 

    /**
     * Returns the value of field 'regPasswordResetCode'.
     * 
     * @return String
     * @return the value of field 'regPasswordResetCode'.
     */
    public java.lang.String getRegPasswordResetCode()
    {
        return this._regPasswordResetCode;
    } //-- java.lang.String getRegPasswordResetCode() 

    /**
     * Sets the value of field 'brandedFlag'.
     * 
     * @param brandedFlag the value of field 'brandedFlag'.
     */
    public void setBrandedFlag(java.lang.String brandedFlag)
    {
        this._brandedFlag = brandedFlag;
    } //-- void setBrandedFlag(java.lang.String) 

    /**
     * Sets the value of field 'changeSecurityQAStatusCode'.
     * 
     * @param changeSecurityQAStatusCode the value of field
     * 'changeSecurityQAStatusCode'.
     */
    public void setChangeSecurityQAStatusCode(java.lang.String changeSecurityQAStatusCode)
    {
        this._changeSecurityQAStatusCode = changeSecurityQAStatusCode;
    } //-- void setChangeSecurityQAStatusCode(java.lang.String) 

    /**
     * Sets the value of field 'cidNumber'.
     * 
     * @param cidNumber the value of field 'cidNumber'.
     */
    public void setCidNumber(java.lang.String cidNumber)
    {
        this._cidNumber = cidNumber;
    } //-- void setCidNumber(java.lang.String) 

    /**
     * Sets the value of field 'createdByProfileId'.
     * 
     * @param createdByProfileId the value of field
     * 'createdByProfileId'.
     */
    public void setCreatedByProfileId(java.lang.String createdByProfileId)
    {
        this._createdByProfileId = createdByProfileId;
    } //-- void setCreatedByProfileId(java.lang.String) 

    /**
     * Sets the value of field 'creationAppId'.
     * 
     * @param creationAppId the value of field 'creationAppId'.
     */
    public void setCreationAppId(java.lang.String creationAppId)
    {
        this._creationAppId = creationAppId;
    } //-- void setCreationAppId(java.lang.String) 

    /**
     * Sets the value of field 'lastAttemptedLoginDate'.
     * 
     * @param lastAttemptedLoginDate the value of field
     * 'lastAttemptedLoginDate'.
     */
    public void setLastAttemptedLoginDate(java.lang.String lastAttemptedLoginDate)
    {
        this._lastAttemptedLoginDate = lastAttemptedLoginDate;
    } //-- void setLastAttemptedLoginDate(java.lang.String) 

    /**
     * Sets the value of field 'lastSuccessfulLoginAppId'.
     * 
     * @param lastSuccessfulLoginAppId the value of field
     * 'lastSuccessfulLoginAppId'.
     */
    public void setLastSuccessfulLoginAppId(java.lang.String lastSuccessfulLoginAppId)
    {
        this._lastSuccessfulLoginAppId = lastSuccessfulLoginAppId;
    } //-- void setLastSuccessfulLoginAppId(java.lang.String) 

    /**
     * Sets the value of field 'lastSuccessfulLoginDate'.
     * 
     * @param lastSuccessfulLoginDate the value of field
     * 'lastSuccessfulLoginDate'.
     */
    public void setLastSuccessfulLoginDate(java.lang.String lastSuccessfulLoginDate)
    {
        this._lastSuccessfulLoginDate = lastSuccessfulLoginDate;
    } //-- void setLastSuccessfulLoginDate(java.lang.String) 

    /**
     * Sets the value of field 'lockoutCounter'.
     * 
     * @param lockoutCounter the value of field 'lockoutCounter'.
     */
    public void setLockoutCounter(java.lang.String lockoutCounter)
    {
        this._lockoutCounter = lockoutCounter;
    } //-- void setLockoutCounter(java.lang.String) 

    /**
     * Sets the value of field 'lockoutDate'.
     * 
     * @param lockoutDate the value of field 'lockoutDate'.
     */
    public void setLockoutDate(java.lang.String lockoutDate)
    {
        this._lockoutDate = lockoutDate;
    } //-- void setLockoutDate(java.lang.String) 

    /**
     * Sets the value of field 'migrationExceptionFlag'.
     * 
     * @param migrationExceptionFlag the value of field
     * 'migrationExceptionFlag'.
     */
    public void setMigrationExceptionFlag(java.lang.String migrationExceptionFlag)
    {
        this._migrationExceptionFlag = migrationExceptionFlag;
    } //-- void setMigrationExceptionFlag(java.lang.String) 

    /**
     * Sets the value of field 'passwordCreationDate'.
     * 
     * @param passwordCreationDate the value of field
     * 'passwordCreationDate'.
     */
    public void setPasswordCreationDate(java.lang.String passwordCreationDate)
    {
        this._passwordCreationDate = passwordCreationDate;
    } //-- void setPasswordCreationDate(java.lang.String) 

    /**
     * Sets the value of field 'passwordResetStatusCode'.
     * 
     * @param passwordResetStatusCode the value of field
     * 'passwordResetStatusCode'.
     */
    public void setPasswordResetStatusCode(java.lang.String passwordResetStatusCode)
    {
        this._passwordResetStatusCode = passwordResetStatusCode;
    } //-- void setPasswordResetStatusCode(java.lang.String) 

    /**
     * Sets the value of field 'profileCreationDate'.
     * 
     * @param profileCreationDate the value of field
     * 'profileCreationDate'.
     */
    public void setProfileCreationDate(java.lang.String profileCreationDate)
    {
        this._profileCreationDate = profileCreationDate;
    } //-- void setProfileCreationDate(java.lang.String) 

    /**
     * Sets the value of field 'profileLastUpdatedApplicationId'.
     * 
     * @param profileLastUpdatedApplicationId the value of field
     * 'profileLastUpdatedApplicationId'.
     */
    public void setProfileLastUpdatedApplicationId(java.lang.String profileLastUpdatedApplicationId)
    {
        this._profileLastUpdatedApplicationId = profileLastUpdatedApplicationId;
    } //-- void setProfileLastUpdatedApplicationId(java.lang.String) 

    /**
     * Sets the value of field 'profileLastUpdatedDate'.
     * 
     * @param profileLastUpdatedDate the value of field
     * 'profileLastUpdatedDate'.
     */
    public void setProfileLastUpdatedDate(java.lang.String profileLastUpdatedDate)
    {
        this._profileLastUpdatedDate = profileLastUpdatedDate;
    } //-- void setProfileLastUpdatedDate(java.lang.String) 

    /**
     * Sets the value of field 'profileLastUpdatedProfileId'.
     * 
     * @param profileLastUpdatedProfileId the value of field
     * 'profileLastUpdatedProfileId'.
     */
    public void setProfileLastUpdatedProfileId(java.lang.String profileLastUpdatedProfileId)
    {
        this._profileLastUpdatedProfileId = profileLastUpdatedProfileId;
    } //-- void setProfileLastUpdatedProfileId(java.lang.String) 

    /**
     * Sets the value of field 'regPasswordResetCode'.
     * 
     * @param regPasswordResetCode the value of field
     * 'regPasswordResetCode'.
     */
    public void setRegPasswordResetCode(java.lang.String regPasswordResetCode)
    {
        this._regPasswordResetCode = regPasswordResetCode;
    } //-- void setRegPasswordResetCode(java.lang.String) 

}
