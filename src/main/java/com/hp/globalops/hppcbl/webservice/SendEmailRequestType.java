/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: SendEmailRequestType.java,v 1.1 2006/11/08 07:34:26 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Class SendEmailRequestType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:26 $
 */
public class SendEmailRequestType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _userId
     */
    private java.lang.String _userId;

    /**
     * Field _emailTemplateList
     */
    private java.util.Vector _emailTemplateList;


      //----------------/
     //- Constructors -/
    //----------------/

    public SendEmailRequestType() 
     {
        super();
        _emailTemplateList = new Vector();
    } //-- com.hp.globalops.hppcbl.webservice.SendEmailRequestType()


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
     * Sets the value of field 'userId'.
     * 
     * @param userId the value of field 'userId'.
     */
    public void setUserId(java.lang.String userId)
    {
        this._userId = userId;
    } //-- void setUserId(java.lang.String) 

}
