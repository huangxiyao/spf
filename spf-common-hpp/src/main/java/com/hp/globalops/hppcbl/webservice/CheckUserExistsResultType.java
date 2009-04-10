/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: CheckUserExistsResultType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class CheckUserExistsResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class CheckUserExistsResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _status
     */
    private boolean _status;

    /**
     * keeps track of state for field: _status
     */
    private boolean _has_status;

    /**
     * Field _profileIdByUserId
     */
    private java.lang.String _profileIdByUserId;

    /**
     * Field _profileIdByEmail
     */
    private java.lang.String _profileIdByEmail;

    /**
     * Field _matchedProfileId
     */
    private java.lang.String _matchedProfileId;

    /**
     * Field _eProfileExistsByUserId
     */
    private boolean _eProfileExistsByUserId;

    /**
     * keeps track of state for field: _eProfileExistsByUserId
     */
    private boolean _has_eProfileExistsByUserId;

    /**
     * Field _eProfileExistsByEmail
     */
    private boolean _eProfileExistsByEmail;

    /**
     * keeps track of state for field: _eProfileExistsByEmail
     */
    private boolean _has_eProfileExistsByEmail;

    /**
     * Field _stagingExistsByUserId
     */
    private boolean _stagingExistsByUserId;

    /**
     * keeps track of state for field: _stagingExistsByUserId
     */
    private boolean _has_stagingExistsByUserId;

    /**
     * Field _stagingExistsByEmail
     */
    private boolean _stagingExistsByEmail;

    /**
     * keeps track of state for field: _stagingExistsByEmail
     */
    private boolean _has_stagingExistsByEmail;

    /**
     * Field _stagingAppRefIdByUserId
     */
    private java.lang.String _stagingAppRefIdByUserId;

    /**
     * Field _stagingAppRefIdByEmail
     */
    private java.lang.String _stagingAppRefIdByEmail;


      //----------------/
     //- Constructors -/
    //----------------/

    public CheckUserExistsResultType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.CheckUserExistsResultType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteEProfileExistsByEmail
     * 
     */
    public void deleteEProfileExistsByEmail()
    {
        this._has_eProfileExistsByEmail= false;
    } //-- void deleteEProfileExistsByEmail() 

    /**
     * Method deleteEProfileExistsByUserId
     * 
     */
    public void deleteEProfileExistsByUserId()
    {
        this._has_eProfileExistsByUserId= false;
    } //-- void deleteEProfileExistsByUserId() 

    /**
     * Method deleteStagingExistsByEmail
     * 
     */
    public void deleteStagingExistsByEmail()
    {
        this._has_stagingExistsByEmail= false;
    } //-- void deleteStagingExistsByEmail() 

    /**
     * Method deleteStagingExistsByUserId
     * 
     */
    public void deleteStagingExistsByUserId()
    {
        this._has_stagingExistsByUserId= false;
    } //-- void deleteStagingExistsByUserId() 

    /**
     * Method deleteStatus
     * 
     */
    public void deleteStatus()
    {
        this._has_status= false;
    } //-- void deleteStatus() 

    /**
     * Returns the value of field 'eProfileExistsByEmail'.
     * 
     * @return boolean
     * @return the value of field 'eProfileExistsByEmail'.
     */
    public boolean getEProfileExistsByEmail()
    {
        return this._eProfileExistsByEmail;
    } //-- boolean getEProfileExistsByEmail() 

    /**
     * Returns the value of field 'eProfileExistsByUserId'.
     * 
     * @return boolean
     * @return the value of field 'eProfileExistsByUserId'.
     */
    public boolean getEProfileExistsByUserId()
    {
        return this._eProfileExistsByUserId;
    } //-- boolean getEProfileExistsByUserId() 

    /**
     * Returns the value of field 'matchedProfileId'.
     * 
     * @return String
     * @return the value of field 'matchedProfileId'.
     */
    public java.lang.String getMatchedProfileId()
    {
        return this._matchedProfileId;
    } //-- java.lang.String getMatchedProfileId() 

    /**
     * Returns the value of field 'profileIdByEmail'.
     * 
     * @return String
     * @return the value of field 'profileIdByEmail'.
     */
    public java.lang.String getProfileIdByEmail()
    {
        return this._profileIdByEmail;
    } //-- java.lang.String getProfileIdByEmail() 

    /**
     * Returns the value of field 'profileIdByUserId'.
     * 
     * @return String
     * @return the value of field 'profileIdByUserId'.
     */
    public java.lang.String getProfileIdByUserId()
    {
        return this._profileIdByUserId;
    } //-- java.lang.String getProfileIdByUserId() 

    /**
     * Returns the value of field 'stagingAppRefIdByEmail'.
     * 
     * @return String
     * @return the value of field 'stagingAppRefIdByEmail'.
     */
    public java.lang.String getStagingAppRefIdByEmail()
    {
        return this._stagingAppRefIdByEmail;
    } //-- java.lang.String getStagingAppRefIdByEmail() 

    /**
     * Returns the value of field 'stagingAppRefIdByUserId'.
     * 
     * @return String
     * @return the value of field 'stagingAppRefIdByUserId'.
     */
    public java.lang.String getStagingAppRefIdByUserId()
    {
        return this._stagingAppRefIdByUserId;
    } //-- java.lang.String getStagingAppRefIdByUserId() 

    /**
     * Returns the value of field 'stagingExistsByEmail'.
     * 
     * @return boolean
     * @return the value of field 'stagingExistsByEmail'.
     */
    public boolean getStagingExistsByEmail()
    {
        return this._stagingExistsByEmail;
    } //-- boolean getStagingExistsByEmail() 

    /**
     * Returns the value of field 'stagingExistsByUserId'.
     * 
     * @return boolean
     * @return the value of field 'stagingExistsByUserId'.
     */
    public boolean getStagingExistsByUserId()
    {
        return this._stagingExistsByUserId;
    } //-- boolean getStagingExistsByUserId() 

    /**
     * Returns the value of field 'status'.
     * 
     * @return boolean
     * @return the value of field 'status'.
     */
    public boolean getStatus()
    {
        return this._status;
    } //-- boolean getStatus() 

    /**
     * Method hasEProfileExistsByEmail
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasEProfileExistsByEmail()
    {
        return this._has_eProfileExistsByEmail;
    } //-- boolean hasEProfileExistsByEmail() 

    /**
     * Method hasEProfileExistsByUserId
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasEProfileExistsByUserId()
    {
        return this._has_eProfileExistsByUserId;
    } //-- boolean hasEProfileExistsByUserId() 

    /**
     * Method hasStagingExistsByEmail
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasStagingExistsByEmail()
    {
        return this._has_stagingExistsByEmail;
    } //-- boolean hasStagingExistsByEmail() 

    /**
     * Method hasStagingExistsByUserId
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasStagingExistsByUserId()
    {
        return this._has_stagingExistsByUserId;
    } //-- boolean hasStagingExistsByUserId() 

    /**
     * Method hasStatus
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasStatus()
    {
        return this._has_status;
    } //-- boolean hasStatus() 

    /**
     * Sets the value of field 'eProfileExistsByEmail'.
     * 
     * @param eProfileExistsByEmail the value of field
     * 'eProfileExistsByEmail'.
     */
    public void setEProfileExistsByEmail(boolean eProfileExistsByEmail)
    {
        this._eProfileExistsByEmail = eProfileExistsByEmail;
        this._has_eProfileExistsByEmail = true;
    } //-- void setEProfileExistsByEmail(boolean) 

    /**
     * Sets the value of field 'eProfileExistsByUserId'.
     * 
     * @param eProfileExistsByUserId the value of field
     * 'eProfileExistsByUserId'.
     */
    public void setEProfileExistsByUserId(boolean eProfileExistsByUserId)
    {
        this._eProfileExistsByUserId = eProfileExistsByUserId;
        this._has_eProfileExistsByUserId = true;
    } //-- void setEProfileExistsByUserId(boolean) 

    /**
     * Sets the value of field 'matchedProfileId'.
     * 
     * @param matchedProfileId the value of field 'matchedProfileId'
     */
    public void setMatchedProfileId(java.lang.String matchedProfileId)
    {
        this._matchedProfileId = matchedProfileId;
    } //-- void setMatchedProfileId(java.lang.String) 

    /**
     * Sets the value of field 'profileIdByEmail'.
     * 
     * @param profileIdByEmail the value of field 'profileIdByEmail'
     */
    public void setProfileIdByEmail(java.lang.String profileIdByEmail)
    {
        this._profileIdByEmail = profileIdByEmail;
    } //-- void setProfileIdByEmail(java.lang.String) 

    /**
     * Sets the value of field 'profileIdByUserId'.
     * 
     * @param profileIdByUserId the value of field
     * 'profileIdByUserId'.
     */
    public void setProfileIdByUserId(java.lang.String profileIdByUserId)
    {
        this._profileIdByUserId = profileIdByUserId;
    } //-- void setProfileIdByUserId(java.lang.String) 

    /**
     * Sets the value of field 'stagingAppRefIdByEmail'.
     * 
     * @param stagingAppRefIdByEmail the value of field
     * 'stagingAppRefIdByEmail'.
     */
    public void setStagingAppRefIdByEmail(java.lang.String stagingAppRefIdByEmail)
    {
        this._stagingAppRefIdByEmail = stagingAppRefIdByEmail;
    } //-- void setStagingAppRefIdByEmail(java.lang.String) 

    /**
     * Sets the value of field 'stagingAppRefIdByUserId'.
     * 
     * @param stagingAppRefIdByUserId the value of field
     * 'stagingAppRefIdByUserId'.
     */
    public void setStagingAppRefIdByUserId(java.lang.String stagingAppRefIdByUserId)
    {
        this._stagingAppRefIdByUserId = stagingAppRefIdByUserId;
    } //-- void setStagingAppRefIdByUserId(java.lang.String) 

    /**
     * Sets the value of field 'stagingExistsByEmail'.
     * 
     * @param stagingExistsByEmail the value of field
     * 'stagingExistsByEmail'.
     */
    public void setStagingExistsByEmail(boolean stagingExistsByEmail)
    {
        this._stagingExistsByEmail = stagingExistsByEmail;
        this._has_stagingExistsByEmail = true;
    } //-- void setStagingExistsByEmail(boolean) 

    /**
     * Sets the value of field 'stagingExistsByUserId'.
     * 
     * @param stagingExistsByUserId the value of field
     * 'stagingExistsByUserId'.
     */
    public void setStagingExistsByUserId(boolean stagingExistsByUserId)
    {
        this._stagingExistsByUserId = stagingExistsByUserId;
        this._has_stagingExistsByUserId = true;
    } //-- void setStagingExistsByUserId(boolean) 

    /**
     * Sets the value of field 'status'.
     * 
     * @param status the value of field 'status'.
     */
    public void setStatus(boolean status)
    {
        this._status = status;
        this._has_status = true;
    } //-- void setStatus(boolean) 

}
