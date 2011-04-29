/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: AdminViewUserRequestType.java,v 1.1 2006/11/08 07:34:24 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class AdminViewUserRequestType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:24 $
 */
public class AdminViewUserRequestType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _adminSessionToken
     */
    private java.lang.String _adminSessionToken;

    /**
     * Field _userLookupCriteria
     */
    private com.hp.globalops.hppcbl.webservice.UserLookupCriteria _userLookupCriteria;


      //----------------/
     //- Constructors -/
    //----------------/

    public AdminViewUserRequestType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.AdminViewUserRequestType()


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
     * Returns the value of field 'userLookupCriteria'.
     * 
     * @return UserLookupCriteria
     * @return the value of field 'userLookupCriteria'.
     */
    public com.hp.globalops.hppcbl.webservice.UserLookupCriteria getUserLookupCriteria()
    {
        return this._userLookupCriteria;
    } //-- com.hp.globalops.hppcbl.webservice.UserLookupCriteria getUserLookupCriteria() 

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
     * Sets the value of field 'userLookupCriteria'.
     * 
     * @param userLookupCriteria the value of field
     * 'userLookupCriteria'.
     */
    public void setUserLookupCriteria(com.hp.globalops.hppcbl.webservice.UserLookupCriteria userLookupCriteria)
    {
        this._userLookupCriteria = userLookupCriteria;
    } //-- void setUserLookupCriteria(com.hp.globalops.hppcbl.webservice.UserLookupCriteria) 

}
