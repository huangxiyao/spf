/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: IsMemberOfGroupRequestType.java,v 1.1 2006/11/08 07:34:26 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class IsMemberOfGroupRequestType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:26 $
 */
public class IsMemberOfGroupRequestType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _sessionToken
     */
    private java.lang.String _sessionToken;

    /**
     * Field _profileIdentity
     */
    private com.hp.globalops.hppcbl.webservice.ProfileIdentity _profileIdentity;

    /**
     * Field _groupName
     */
    private java.lang.String _groupName;

    /**
     * Field _roleName
     */
    private java.lang.String _roleName;


      //----------------/
     //- Constructors -/
    //----------------/

    public IsMemberOfGroupRequestType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.IsMemberOfGroupRequestType()


      //-----------/
     //- Methods -/
    //-----------/

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
     * Returns the value of field 'roleName'.
     * 
     * @return String
     * @return the value of field 'roleName'.
     */
    public java.lang.String getRoleName()
    {
        return this._roleName;
    } //-- java.lang.String getRoleName() 

    /**
     * Returns the value of field 'sessionToken'.
     * 
     * @return String
     * @return the value of field 'sessionToken'.
     */
    public java.lang.String getSessionToken()
    {
        return this._sessionToken;
    } //-- java.lang.String getSessionToken() 

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
     * Sets the value of field 'profileIdentity'.
     * 
     * @param profileIdentity the value of field 'profileIdentity'.
     */
    public void setProfileIdentity(com.hp.globalops.hppcbl.webservice.ProfileIdentity profileIdentity)
    {
        this._profileIdentity = profileIdentity;
    } //-- void setProfileIdentity(com.hp.globalops.hppcbl.webservice.ProfileIdentity) 

    /**
     * Sets the value of field 'roleName'.
     * 
     * @param roleName the value of field 'roleName'.
     */
    public void setRoleName(java.lang.String roleName)
    {
        this._roleName = roleName;
    } //-- void setRoleName(java.lang.String) 

    /**
     * Sets the value of field 'sessionToken'.
     * 
     * @param sessionToken the value of field 'sessionToken'.
     */
    public void setSessionToken(java.lang.String sessionToken)
    {
        this._sessionToken = sessionToken;
    } //-- void setSessionToken(java.lang.String) 

}
