/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: GetGroupInfoResultType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

/**
 * Class GetGroupInfoResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class GetGroupInfoResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _groupName
     */
    private java.lang.String _groupName;

    /**
     * Field _groupId
     */
    private java.lang.String _groupId;

    /**
     * Field _parentGroupId
     */
    private java.lang.String _parentGroupId;

    /**
     * Field _distinguishedName
     */
    private java.lang.String _distinguishedName;

    /**
     * Field _securityLevel
     */
    private java.lang.String _securityLevel;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetGroupInfoResultType() 
     {
        super();
    } //-- com.hp.globalops.hppcbl.webservice.GetGroupInfoResultType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'distinguishedName'.
     * 
     * @return String
     * @return the value of field 'distinguishedName'.
     */
    public java.lang.String getDistinguishedName()
    {
        return this._distinguishedName;
    } //-- java.lang.String getDistinguishedName() 

    /**
     * Returns the value of field 'groupId'.
     * 
     * @return String
     * @return the value of field 'groupId'.
     */
    public java.lang.String getGroupId()
    {
        return this._groupId;
    } //-- java.lang.String getGroupId() 

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
     * Returns the value of field 'parentGroupId'.
     * 
     * @return String
     * @return the value of field 'parentGroupId'.
     */
    public java.lang.String getParentGroupId()
    {
        return this._parentGroupId;
    } //-- java.lang.String getParentGroupId() 

    /**
     * Returns the value of field 'securityLevel'.
     * 
     * @return String
     * @return the value of field 'securityLevel'.
     */
    public java.lang.String getSecurityLevel()
    {
        return this._securityLevel;
    } //-- java.lang.String getSecurityLevel() 

    /**
     * Sets the value of field 'distinguishedName'.
     * 
     * @param distinguishedName the value of field
     * 'distinguishedName'.
     */
    public void setDistinguishedName(java.lang.String distinguishedName)
    {
        this._distinguishedName = distinguishedName;
    } //-- void setDistinguishedName(java.lang.String) 

    /**
     * Sets the value of field 'groupId'.
     * 
     * @param groupId the value of field 'groupId'.
     */
    public void setGroupId(java.lang.String groupId)
    {
        this._groupId = groupId;
    } //-- void setGroupId(java.lang.String) 

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
     * Sets the value of field 'parentGroupId'.
     * 
     * @param parentGroupId the value of field 'parentGroupId'.
     */
    public void setParentGroupId(java.lang.String parentGroupId)
    {
        this._parentGroupId = parentGroupId;
    } //-- void setParentGroupId(java.lang.String) 

    /**
     * Sets the value of field 'securityLevel'.
     * 
     * @param securityLevel the value of field 'securityLevel'.
     */
    public void setSecurityLevel(java.lang.String securityLevel)
    {
        this._securityLevel = securityLevel;
    } //-- void setSecurityLevel(java.lang.String) 

}
