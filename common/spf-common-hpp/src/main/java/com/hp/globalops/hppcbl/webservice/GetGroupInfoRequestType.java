/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: GetGroupInfoRequestType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class GetGroupInfoRequestType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class GetGroupInfoRequestType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _adminSessionToken
     */
    private java.lang.String _adminSessionToken;

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

    public GetGroupInfoRequestType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.GetGroupInfoRequestType()


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
     * Sets the value of field 'groupName'.
     * 
     * @param groupName the value of field 'groupName'.
     */
    public void setGroupName(java.lang.String groupName)
    {
        this._groupName = groupName;
    } //-- void setGroupName(java.lang.String) 

    /**
     * Sets the value of field 'roleName'.
     * 
     * @param roleName the value of field 'roleName'.
     */
    public void setRoleName(java.lang.String roleName)
    {
        this._roleName = roleName;
    } //-- void setRoleName(java.lang.String) 

}
