/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: CreateUserRequestType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class CreateUserRequestType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class CreateUserRequestType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _userId
     */
    private java.lang.String _userId;

    /**
     * Field _profileCredentials
     */
    private com.hp.globalops.hppcbl.webservice.ProfileCredentials _profileCredentials;

    /**
     * Field _profileCore
     */
    private com.hp.globalops.hppcbl.webservice.ProfileCore _profileCore;

    /**
     * Field _profileExtended
     */
    private com.hp.globalops.hppcbl.webservice.ProfileExtended _profileExtended;

    /**
     * Field _applicationId
     */
    private java.lang.String _applicationId;


      //----------------/
     //- Constructors -/
    //----------------/

    public CreateUserRequestType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.CreateUserRequestType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'applicationId'.
     * 
     * @return String
     * @return the value of field 'applicationId'.
     */
    public java.lang.String getApplicationId()
    {
        return this._applicationId;
    } //-- java.lang.String getApplicationId() 

    /**
     * Returns the value of field 'profileCore'.
     * 
     * @return ProfileCore
     * @return the value of field 'profileCore'.
     */
    public com.hp.globalops.hppcbl.webservice.ProfileCore getProfileCore()
    {
        return this._profileCore;
    } //-- com.hp.globalops.hppcbl.webservice.ProfileCore getProfileCore() 

    /**
     * Returns the value of field 'profileCredentials'.
     * 
     * @return ProfileCredentials
     * @return the value of field 'profileCredentials'.
     */
    public com.hp.globalops.hppcbl.webservice.ProfileCredentials getProfileCredentials()
    {
        return this._profileCredentials;
    } //-- com.hp.globalops.hppcbl.webservice.ProfileCredentials getProfileCredentials() 

    /**
     * Returns the value of field 'profileExtended'.
     * 
     * @return ProfileExtended
     * @return the value of field 'profileExtended'.
     */
    public com.hp.globalops.hppcbl.webservice.ProfileExtended getProfileExtended()
    {
        return this._profileExtended;
    } //-- com.hp.globalops.hppcbl.webservice.ProfileExtended getProfileExtended() 

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
     * Sets the value of field 'applicationId'.
     * 
     * @param applicationId the value of field 'applicationId'.
     */
    public void setApplicationId(java.lang.String applicationId)
    {
        this._applicationId = applicationId;
    } //-- void setApplicationId(java.lang.String) 

    /**
     * Sets the value of field 'profileCore'.
     * 
     * @param profileCore the value of field 'profileCore'.
     */
    public void setProfileCore(com.hp.globalops.hppcbl.webservice.ProfileCore profileCore)
    {
        this._profileCore = profileCore;
    } //-- void setProfileCore(com.hp.globalops.hppcbl.webservice.ProfileCore) 

    /**
     * Sets the value of field 'profileCredentials'.
     * 
     * @param profileCredentials the value of field
     * 'profileCredentials'.
     */
    public void setProfileCredentials(com.hp.globalops.hppcbl.webservice.ProfileCredentials profileCredentials)
    {
        this._profileCredentials = profileCredentials;
    } //-- void setProfileCredentials(com.hp.globalops.hppcbl.webservice.ProfileCredentials) 

    /**
     * Sets the value of field 'profileExtended'.
     * 
     * @param profileExtended the value of field 'profileExtended'.
     */
    public void setProfileExtended(com.hp.globalops.hppcbl.webservice.ProfileExtended profileExtended)
    {
        this._profileExtended = profileExtended;
    } //-- void setProfileExtended(com.hp.globalops.hppcbl.webservice.ProfileExtended) 

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
