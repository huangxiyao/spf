/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: CheckAuthorityResultType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class CheckAuthorityResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class CheckAuthorityResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _adminUserId
     */
    private java.lang.String _adminUserId;

    /**
     * Field _profileId
     */
    private java.lang.String _profileId;

    /**
     * Field _role
     */
    private java.lang.String _role;


      //----------------/
     //- Constructors -/
    //----------------/

    public CheckAuthorityResultType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.CheckAuthorityResultType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'adminUserId'.
     * 
     * @return String
     * @return the value of field 'adminUserId'.
     */
    public java.lang.String getAdminUserId()
    {
        return this._adminUserId;
    } //-- java.lang.String getAdminUserId() 

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
     * Returns the value of field 'role'.
     * 
     * @return String
     * @return the value of field 'role'.
     */
    public java.lang.String getRole()
    {
        return this._role;
    } //-- java.lang.String getRole() 

    /**
     * Sets the value of field 'adminUserId'.
     * 
     * @param adminUserId the value of field 'adminUserId'.
     */
    public void setAdminUserId(java.lang.String adminUserId)
    {
        this._adminUserId = adminUserId;
    } //-- void setAdminUserId(java.lang.String) 

    /**
     * Sets the value of field 'profileId'.
     * 
     * @param profileId the value of field 'profileId'.
     */
    public void setProfileId(java.lang.String profileId)
    {
        this._profileId = profileId;
    } //-- void setProfileId(java.lang.String) 

    /**
     * Sets the value of field 'role'.
     * 
     * @param role the value of field 'role'.
     */
    public void setRole(java.lang.String role)
    {
        this._role = role;
    } //-- void setRole(java.lang.String) 

}
