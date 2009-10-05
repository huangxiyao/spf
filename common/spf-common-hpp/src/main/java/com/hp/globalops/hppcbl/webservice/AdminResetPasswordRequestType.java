/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: AdminResetPasswordRequestType.java,v 1.2 2007/09/29 03:05:05 liuliyey Exp $
 */

package com.hp.globalops.hppcbl.webservice;

// ---------------------------------/
// - Imported classes and packages -/
// ---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Class AdminResetPasswordRequestType.
 * 
 * @version $Revision: 1.2 $ $Date: 2007/09/29 03:05:05 $
 */
public class AdminResetPasswordRequestType implements java.io.Serializable {

    // --------------------------/
    // - Class/Member Variables -/
    // --------------------------/

    /**
     * Field _adminSessionToken
     */
    private java.lang.String _adminSessionToken;

    /**
     * Field _profileId
     */
    private java.lang.String _profileId;

    /**
     * Field _emailTemplateList
     */
    private java.util.Vector _emailTemplateList;

    private java.lang.String _url;

    // ----------------/
    // - Constructors -/
    // ----------------/

    public AdminResetPasswordRequestType() {
        super();
        _emailTemplateList = new Vector();
    } // -- com.hp.globalops.hppcbl.webservice.AdminResetPasswordRequestType()

    // -----------/
    // - Methods -/
    // -----------/

    /**
     * Method addEmailTemplate
     * 
     * 
     * 
     * @param vEmailTemplate
     */
    public void addEmailTemplate(
            com.hp.globalops.hppcbl.webservice.EmailTemplate vEmailTemplate)
            throws java.lang.IndexOutOfBoundsException {
        /*
        if (!(_emailTemplateList.size() < 2)) {
            throw new IndexOutOfBoundsException(
                    "addEmailTemplate has a maximum of 2");
        }*/
        _emailTemplateList.addElement(vEmailTemplate);
    } // -- void
        // addEmailTemplate(com.hp.globalops.hppcbl.webservice.EmailTemplate)

    /**
     * Method addEmailTemplate
     * 
     * 
     * 
     * @param index
     * @param vEmailTemplate
     */
    public void addEmailTemplate(int index,
            com.hp.globalops.hppcbl.webservice.EmailTemplate vEmailTemplate)
            throws java.lang.IndexOutOfBoundsException {
        /*
        if (!(_emailTemplateList.size() < 2)) {
            throw new IndexOutOfBoundsException(
                    "addEmailTemplate has a maximum of 2");
        }*/
        _emailTemplateList.insertElementAt(vEmailTemplate, index);
    } // -- void addEmailTemplate(int,
        // com.hp.globalops.hppcbl.webservice.EmailTemplate)

    /**
     * Method enumerateEmailTemplate
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateEmailTemplate() {
        return _emailTemplateList.elements();
    } // -- java.util.Enumeration enumerateEmailTemplate()

    /**
     * Returns the value of field 'adminSessionToken'.
     * 
     * @return String
     * @return the value of field 'adminSessionToken'.
     */
    public java.lang.String getAdminSessionToken() {
        return this._adminSessionToken;
    } // -- java.lang.String getAdminSessionToken()

    /**
     * Method getEmailTemplate
     * 
     * 
     * 
     * @param index
     * @return EmailTemplate
     */
    public com.hp.globalops.hppcbl.webservice.EmailTemplate getEmailTemplate(
            int index) throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _emailTemplateList.size())) {
            throw new IndexOutOfBoundsException(
                    "getEmailTemplate: Index value '" + index
                            + "' not in range [0.." + _emailTemplateList.size()
                            + "]");
        }

        return (com.hp.globalops.hppcbl.webservice.EmailTemplate)_emailTemplateList
                .elementAt(index);
    } // -- com.hp.globalops.hppcbl.webservice.EmailTemplate
        // getEmailTemplate(int)

    /**
     * Method getEmailTemplate
     * 
     * 
     * 
     * @return EmailTemplate
     */
    public com.hp.globalops.hppcbl.webservice.EmailTemplate[] getEmailTemplate() {
        int size = _emailTemplateList.size();
        com.hp.globalops.hppcbl.webservice.EmailTemplate[] mArray = new com.hp.globalops.hppcbl.webservice.EmailTemplate[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.hp.globalops.hppcbl.webservice.EmailTemplate)_emailTemplateList
                    .elementAt(index);
        }
        return mArray;
    } // -- com.hp.globalops.hppcbl.webservice.EmailTemplate[]
        // getEmailTemplate()

    /**
     * Method getEmailTemplateCount
     * 
     * 
     * 
     * @return int
     */
    public int getEmailTemplateCount() {
        return _emailTemplateList.size();
    } // -- int getEmailTemplateCount()

    /**
     * Returns the value of field 'profileId'.
     * 
     * @return String
     * @return the value of field 'profileId'.
     */
    public java.lang.String getProfileId() {
        return this._profileId;
    } // -- java.lang.String getProfileId()

    /**
     * Method removeAllEmailTemplate
     * 
     */
    public void removeAllEmailTemplate() {
        _emailTemplateList.removeAllElements();
    } // -- void removeAllEmailTemplate()

    /**
     * Method removeEmailTemplate
     * 
     * 
     * 
     * @param index
     * @return EmailTemplate
     */
    public com.hp.globalops.hppcbl.webservice.EmailTemplate removeEmailTemplate(
            int index) {
        java.lang.Object obj = _emailTemplateList.elementAt(index);
        _emailTemplateList.removeElementAt(index);
        return (com.hp.globalops.hppcbl.webservice.EmailTemplate)obj;
    } // -- com.hp.globalops.hppcbl.webservice.EmailTemplate
        // removeEmailTemplate(int)

    /**
     * Sets the value of field 'adminSessionToken'.
     * 
     * @param adminSessionToken
     *            the value of field 'adminSessionToken'.
     */
    public void setAdminSessionToken(java.lang.String adminSessionToken) {
        this._adminSessionToken = adminSessionToken;
    } // -- void setAdminSessionToken(java.lang.String)

    /**
     * Method setEmailTemplate
     * 
     * 
     * 
     * @param index
     * @param vEmailTemplate
     */
    public void setEmailTemplate(int index,
            com.hp.globalops.hppcbl.webservice.EmailTemplate vEmailTemplate)
            throws java.lang.IndexOutOfBoundsException {
        // -- check bounds for index
        if ((index < 0) || (index > _emailTemplateList.size())) {
            throw new IndexOutOfBoundsException(
                    "setEmailTemplate: Index value '" + index
                            + "' not in range [0.." + _emailTemplateList.size()
                            + "]");
        }
        if (!(index < 2)) {
            throw new IndexOutOfBoundsException(
                    "setEmailTemplate has a maximum of 2");
        }
        _emailTemplateList.setElementAt(vEmailTemplate, index);
    } // -- void setEmailTemplate(int,
        // com.hp.globalops.hppcbl.webservice.EmailTemplate)

    /**
     * Method setEmailTemplate
     * 
     * 
     * 
     * @param emailTemplateArray
     */
    public void setEmailTemplate(
            com.hp.globalops.hppcbl.webservice.EmailTemplate[] emailTemplateArray) {
        // -- copy array
        _emailTemplateList.removeAllElements();
        for (int i = 0; i < emailTemplateArray.length; i++) {
            _emailTemplateList.addElement(emailTemplateArray[i]);
        }
    } // -- void
        // setEmailTemplate(com.hp.globalops.hppcbl.webservice.EmailTemplate)

    public java.lang.String getUrl() {
        return _url;
    }

    public void setUrl(java.lang.String url) {
        System.out.println("Enter setUrl and url = " + url);
        this._url = url;
    }

    /**
     * Sets the value of field 'profileId'.
     * 
     * @param profileId
     *            the value of field 'profileId'.
     */
    public void setProfileId(java.lang.String profileId) {
        this._profileId = profileId;
    } // -- void setProfileId(java.lang.String)

}
