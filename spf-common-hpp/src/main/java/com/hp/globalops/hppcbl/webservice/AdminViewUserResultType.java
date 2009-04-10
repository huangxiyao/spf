/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: AdminViewUserResultType.java,v 1.1 2006/11/08 07:34:24 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class AdminViewUserResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:24 $
 */
public class AdminViewUserResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _isEditable
     */
    private boolean _isEditable;

    /**
     * keeps track of state for field: _isEditable
     */
    private boolean _has_isEditable;

    /**
     * Field _profileIdentity
     */
    private com.hp.globalops.hppcbl.webservice.ProfileIdentity _profileIdentity;

    /**
     * Field _adminViewUserResultTypeChoice
     */
    private com.hp.globalops.hppcbl.webservice.AdminViewUserResultTypeChoice _adminViewUserResultTypeChoice;


      //----------------/
     //- Constructors -/
    //----------------/

    public AdminViewUserResultType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.AdminViewUserResultType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteIsEditable
     * 
     */
    public void deleteIsEditable()
    {
        this._has_isEditable= false;
    } //-- void deleteIsEditable() 

    /**
     * Returns the value of field 'adminViewUserResultTypeChoice'.
     * 
     * @return AdminViewUserResultTypeChoice
     * @return the value of field 'adminViewUserResultTypeChoice'.
     */
    public com.hp.globalops.hppcbl.webservice.AdminViewUserResultTypeChoice getAdminViewUserResultTypeChoice()
    {
        return this._adminViewUserResultTypeChoice;
    } //-- com.hp.globalops.hppcbl.webservice.AdminViewUserResultTypeChoice getAdminViewUserResultTypeChoice() 

    /**
     * Returns the value of field 'isEditable'.
     * 
     * @return boolean
     * @return the value of field 'isEditable'.
     */
    public boolean getIsEditable()
    {
        return this._isEditable;
    } //-- boolean getIsEditable() 

    /**
     * Returns the value of field 'profileIdentity'.
     * 
     * @return ProfileIdentity
     * @return the value of field 'profileIdentity'.
     */
    public com.hp.globalops.hppcbl.webservice.ProfileIdentity getProfileIdentity()
    {
        return this._profileIdentity;
    } //-- com.hp.globalops.hppcbl.webservice.ProfileIdentity getProfileIdentity() 

    /**
     * Method hasIsEditable
     * 
     * 
     * 
     * @return boolean
     */
    public boolean hasIsEditable()
    {
        return this._has_isEditable;
    } //-- boolean hasIsEditable() 

    /**
     * Sets the value of field 'adminViewUserResultTypeChoice'.
     * 
     * @param adminViewUserResultTypeChoice the value of field
     * 'adminViewUserResultTypeChoice'.
     */
    public void setAdminViewUserResultTypeChoice(com.hp.globalops.hppcbl.webservice.AdminViewUserResultTypeChoice adminViewUserResultTypeChoice)
    {
        this._adminViewUserResultTypeChoice = adminViewUserResultTypeChoice;
    } //-- void setAdminViewUserResultTypeChoice(com.hp.globalops.hppcbl.webservice.AdminViewUserResultTypeChoice) 

    /**
     * Sets the value of field 'isEditable'.
     * 
     * @param isEditable the value of field 'isEditable'.
     */
    public void setIsEditable(boolean isEditable)
    {
        this._isEditable = isEditable;
        this._has_isEditable = true;
    } //-- void setIsEditable(boolean) 

    /**
     * Sets the value of field 'profileIdentity'.
     * 
     * @param profileIdentity the value of field 'profileIdentity'.
     */
    public void setProfileIdentity(com.hp.globalops.hppcbl.webservice.ProfileIdentity profileIdentity)
    {
        this._profileIdentity = profileIdentity;
    } //-- void setProfileIdentity(com.hp.globalops.hppcbl.webservice.ProfileIdentity) 

}
