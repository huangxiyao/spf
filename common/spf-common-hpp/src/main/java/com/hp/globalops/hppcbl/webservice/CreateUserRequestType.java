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

    /**
     * Field _groupName
     */
    private java.lang.String _groupName;

    /**
     * Field _koreanPrivacy
     */
    private java.lang.String _koreanPrivacy;

    /**
     * Field _emailTemplateList
     */
    private java.util.Vector _emailTemplateList;


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
     * Method addEmailTemplate
     * 
     * 
     * 
     * @param vEmailTemplate
     */
    public void addEmailTemplate(com.hp.globalops.hppcbl.webservice.EmailTemplate vEmailTemplate)
        throws java.lang.IndexOutOfBoundsException
    {
        if (!(_emailTemplateList.size() < 10)) {
            throw new IndexOutOfBoundsException("addEmailTemplate has a maximum of 10");
        }
        _emailTemplateList.addElement(vEmailTemplate);
    } //-- void addEmailTemplate(com.hp.globalops.hppcbl.webservice.EmailTemplate) 

    /**
     * Method addEmailTemplate
     * 
     * 
     * 
     * @param index
     * @param vEmailTemplate
     */
    public void addEmailTemplate(int index, com.hp.globalops.hppcbl.webservice.EmailTemplate vEmailTemplate)
        throws java.lang.IndexOutOfBoundsException
    {
        if (!(_emailTemplateList.size() < 10)) {
            throw new IndexOutOfBoundsException("addEmailTemplate has a maximum of 10");
        }
        _emailTemplateList.insertElementAt(vEmailTemplate, index);
    } //-- void addEmailTemplate(int, com.hp.globalops.hppcbl.webservice.EmailTemplate) 

    /**
     * Method enumerateEmailTemplate
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateEmailTemplate()
    {
        return _emailTemplateList.elements();
    } //-- java.util.Enumeration enumerateEmailTemplate() 

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
     * Method getEmailTemplate
     * 
     * 
     * 
     * @param index
     * @return EmailTemplate
     */
    public com.hp.globalops.hppcbl.webservice.EmailTemplate getEmailTemplate(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _emailTemplateList.size())) {
            throw new IndexOutOfBoundsException("getEmailTemplate: Index value '"+index+"' not in range [0.."+_emailTemplateList.size()+ "]");
        }
        
        return (com.hp.globalops.hppcbl.webservice.EmailTemplate) _emailTemplateList.elementAt(index);
    } //-- com.hp.globalops.hppcbl.webservice.EmailTemplate getEmailTemplate(int) 

    /**
     * Method getEmailTemplate
     * 
     * 
     * 
     * @return EmailTemplate
     */
    public com.hp.globalops.hppcbl.webservice.EmailTemplate[] getEmailTemplate()
    {
        int size = _emailTemplateList.size();
        com.hp.globalops.hppcbl.webservice.EmailTemplate[] mArray = new com.hp.globalops.hppcbl.webservice.EmailTemplate[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.hp.globalops.hppcbl.webservice.EmailTemplate) _emailTemplateList.elementAt(index);
        }
        return mArray;
    } //-- com.hp.globalops.hppcbl.webservice.EmailTemplate[] getEmailTemplate() 

    /**
     * Method getEmailTemplateCount
     * 
     * 
     * 
     * @return int
     */
    public int getEmailTemplateCount()
    {
        return _emailTemplateList.size();
    } //-- int getEmailTemplateCount() 

    /**
     * Returns the value of field 'groupName'.
     * 
     * @return String
     * @return the value of field 'groupName'.
     */
    public java.lang.String getGroupName()
    {
        return this._groupName;
    } //-- java.lang.String getGroupName() 

    /**
     * Returns the value of field 'koreanPrivacy'.
     * 
     * @return String
     * @return the value of field 'koreanPrivacy'.
     */
    public java.lang.String getKoreanPrivacy()
    {
        return this._koreanPrivacy;
    } //-- java.lang.String getKoreanPrivacy() 

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
     * Method removeAllEmailTemplate
     * 
     */
    public void removeAllEmailTemplate()
    {
        _emailTemplateList.removeAllElements();
    } //-- void removeAllEmailTemplate() 

    /**
     * Method removeEmailTemplate
     * 
     * 
     * 
     * @param index
     * @return EmailTemplate
     */
    public com.hp.globalops.hppcbl.webservice.EmailTemplate removeEmailTemplate(int index)
    {
        java.lang.Object obj = _emailTemplateList.elementAt(index);
        _emailTemplateList.removeElementAt(index);
        return (com.hp.globalops.hppcbl.webservice.EmailTemplate) obj;
    } //-- com.hp.globalops.hppcbl.webservice.EmailTemplate removeEmailTemplate(int) 

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
     * Method setEmailTemplate
     * 
     * 
     * 
     * @param index
     * @param vEmailTemplate
     */
    public void setEmailTemplate(int index, com.hp.globalops.hppcbl.webservice.EmailTemplate vEmailTemplate)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _emailTemplateList.size())) {
            throw new IndexOutOfBoundsException("setEmailTemplate: Index value '"+index+"' not in range [0.."+_emailTemplateList.size()+ "]");
        }
        if (!(index < 10)) {
            throw new IndexOutOfBoundsException("setEmailTemplate has a maximum of 10");
        }
        _emailTemplateList.setElementAt(vEmailTemplate, index);
    } //-- void setEmailTemplate(int, com.hp.globalops.hppcbl.webservice.EmailTemplate) 

    /**
     * Method setEmailTemplate
     * 
     * 
     * 
     * @param emailTemplateArray
     */
    public void setEmailTemplate(com.hp.globalops.hppcbl.webservice.EmailTemplate[] emailTemplateArray)
    {
        //-- copy array
        _emailTemplateList.removeAllElements();
        for (int i = 0; i < emailTemplateArray.length; i++) {
            _emailTemplateList.addElement(emailTemplateArray[i]);
        }
    } //-- void setEmailTemplate(com.hp.globalops.hppcbl.webservice.EmailTemplate) 

    /**
     * Sets the value of field 'groupName'.
     * 
     * @param groupName the value of field 'groupName'.
     */
    public void setGroupName(java.lang.String groupName)
    {
        this._groupName = groupName;
    } //-- void setGroupName(java.lang.String) 

    /**
     * Sets the value of field 'koreanPrivacy'.
     * 
     * @param koreanPrivacy the value of field 'koreanPrivacy'.
     */
    public void setKoreanPrivacy(java.lang.String koreanPrivacy)
    {
        this._koreanPrivacy = koreanPrivacy;
    } //-- void setKoreanPrivacy(java.lang.String) 

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
