/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: GetUserGroupsRequestType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class GetUserGroupsRequestType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class GetUserGroupsRequestType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _adminSessionToken
     */
    private java.lang.String _adminSessionToken;

    /**
     * Field _profileIdentity
     */
    private com.hp.globalops.hppcbl.webservice.ProfileIdentity _profileIdentity;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetUserGroupsRequestType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.GetUserGroupsRequestType()


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
     * Sets the value of field 'profileIdentity'.
     * 
     * @param profileIdentity the value of field 'profileIdentity'.
     */
    public void setProfileIdentity(com.hp.globalops.hppcbl.webservice.ProfileIdentity profileIdentity)
    {
        this._profileIdentity = profileIdentity;
    } //-- void setProfileIdentity(com.hp.globalops.hppcbl.webservice.ProfileIdentity) 

}
