/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: AdminModifyUserRequestType.java,v 1.1 2006/11/08 07:34:24 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class AdminModifyUserRequestType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:24 $
 */
public class AdminModifyUserRequestType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _adminSessionToken
     */
    private java.lang.String _adminSessionToken;

    /**
     * Field _profileId
     */
    private java.lang.String _profileId;

    /**
     * Field _profileCore
     */
    private com.hp.globalops.hppcbl.webservice.ProfileCore _profileCore;

    /**
     * Field _profileExtended
     */
    private com.hp.globalops.hppcbl.webservice.ProfileExtended _profileExtended;

    /**
     * Field _emailTemplate
     */
    private com.hp.globalops.hppcbl.webservice.EmailTemplate _emailTemplate;


      //----------------/
     //- Constructors -/
    //----------------/

    public AdminModifyUserRequestType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.AdminModifyUserRequestType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'adminSessionToken'.
     * 
     * @return String
     * @return the value of field 'adminSessionToken'.
     */
    public java.lang.String getAdminSessionToken()
    {
        return this._adminSessionToken;
    } //-- java.lang.String getAdminSessionToken() 

    /**
     * Returns the value of field 'emailTemplate'.
     * 
     * @return EmailTemplate
     * @return the value of field 'emailTemplate'.
     */
    public com.hp.globalops.hppcbl.webservice.EmailTemplate getEmailTemplate()
    {
        return this._emailTemplate;
    } //-- com.hp.globalops.hppcbl.webservice.EmailTemplate getEmailTemplate() 

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
     * Returns the value of field 'profileId'.
     * 
     * @return String
     * @return the value of field 'profileId'.
     */
    public java.lang.String getProfileId()
    {
        return this._profileId;
    } //-- java.lang.String getProfileId() 

    /**
     * Sets the value of field 'adminSessionToken'.
     * 
     * @param adminSessionToken the value of field
     * 'adminSessionToken'.
     */
    public void setAdminSessionToken(java.lang.String adminSessionToken)
    {
        this._adminSessionToken = adminSessionToken;
    } //-- void setAdminSessionToken(java.lang.String) 

    /**
     * Sets the value of field 'emailTemplate'.
     * 
     * @param emailTemplate the value of field 'emailTemplate'.
     */
    public void setEmailTemplate(com.hp.globalops.hppcbl.webservice.EmailTemplate emailTemplate)
    {
        this._emailTemplate = emailTemplate;
    } //-- void setEmailTemplate(com.hp.globalops.hppcbl.webservice.EmailTemplate) 

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
     * Sets the value of field 'profileExtended'.
     * 
     * @param profileExtended the value of field 'profileExtended'.
     */
    public void setProfileExtended(com.hp.globalops.hppcbl.webservice.ProfileExtended profileExtended)
    {
        this._profileExtended = profileExtended;
    } //-- void setProfileExtended(com.hp.globalops.hppcbl.webservice.ProfileExtended) 

    /**
     * Sets the value of field 'profileId'.
     * 
     * @param profileId the value of field 'profileId'.
     */
    public void setProfileId(java.lang.String profileId)
    {
        this._profileId = profileId;
    } //-- void setProfileId(java.lang.String) 

}
