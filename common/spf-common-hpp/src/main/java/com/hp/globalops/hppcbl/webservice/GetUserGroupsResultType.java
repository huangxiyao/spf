/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: GetUserGroupsResultType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
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
 * Class GetUserGroupsResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class GetUserGroupsResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _profileId
     */
    private java.lang.String _profileId;

    /**
     * Field _groupRoleList
     */
    private java.util.Vector _groupRoleList;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetUserGroupsResultType() 
     {
        super();
        _groupRoleList = new Vector();
    } //-- com.hp.globalops.hppcbl.webservice.GetUserGroupsResultType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addGroupRole
     * 
     * 
     * 
     * @param vGroupRole
     */
    public void addGroupRole(com.hp.globalops.hppcbl.webservice.GroupRole vGroupRole)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupRoleList.addElement(vGroupRole);
    } //-- void addGroupRole(com.hp.globalops.hppcbl.webservice.GroupRole) 

    /**
     * Method addGroupRole
     * 
     * 
     * 
     * @param index
     * @param vGroupRole
     */
    public void addGroupRole(int index, com.hp.globalops.hppcbl.webservice.GroupRole vGroupRole)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupRoleList.insertElementAt(vGroupRole, index);
    } //-- void addGroupRole(int, com.hp.globalops.hppcbl.webservice.GroupRole) 

    /**
     * Method enumerateGroupRole
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateGroupRole()
    {
        return _groupRoleList.elements();
    } //-- java.util.Enumeration enumerateGroupRole() 

    /**
     * Method getGroupRole
     * 
     * 
     * 
     * @param index
     * @return GroupRole
     */
    public com.hp.globalops.hppcbl.webservice.GroupRole getGroupRole(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupRoleList.size())) {
            throw new IndexOutOfBoundsException("getGroupRole: Index value '"+index+"' not in range [0.."+_groupRoleList.size()+ "]");
        }
        
        return (com.hp.globalops.hppcbl.webservice.GroupRole) _groupRoleList.elementAt(index);
    } //-- com.hp.globalops.hppcbl.webservice.GroupRole getGroupRole(int) 

    /**
     * Method getGroupRole
     * 
     * 
     * 
     * @return GroupRole
     */
    public com.hp.globalops.hppcbl.webservice.GroupRole[] getGroupRole()
    {
        int size = _groupRoleList.size();
        com.hp.globalops.hppcbl.webservice.GroupRole[] mArray = new com.hp.globalops.hppcbl.webservice.GroupRole[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.hp.globalops.hppcbl.webservice.GroupRole) _groupRoleList.elementAt(index);
        }
        return mArray;
    } //-- com.hp.globalops.hppcbl.webservice.GroupRole[] getGroupRole() 

    /**
     * Method getGroupRoleCount
     * 
     * 
     * 
     * @return int
     */
    public int getGroupRoleCount()
    {
        return _groupRoleList.size();
    } //-- int getGroupRoleCount() 

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
     * Method removeAllGroupRole
     * 
     */
    public void removeAllGroupRole()
    {
        _groupRoleList.removeAllElements();
    } //-- void removeAllGroupRole() 

    /**
     * Method removeGroupRole
     * 
     * 
     * 
     * @param index
     * @return GroupRole
     */
    public com.hp.globalops.hppcbl.webservice.GroupRole removeGroupRole(int index)
    {
        java.lang.Object obj = _groupRoleList.elementAt(index);
        _groupRoleList.removeElementAt(index);
        return (com.hp.globalops.hppcbl.webservice.GroupRole) obj;
    } //-- com.hp.globalops.hppcbl.webservice.GroupRole removeGroupRole(int) 

    /**
     * Method setGroupRole
     * 
     * 
     * 
     * @param index
     * @param vGroupRole
     */
    public void setGroupRole(int index, com.hp.globalops.hppcbl.webservice.GroupRole vGroupRole)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupRoleList.size())) {
            throw new IndexOutOfBoundsException("setGroupRole: Index value '"+index+"' not in range [0.."+_groupRoleList.size()+ "]");
        }
        _groupRoleList.setElementAt(vGroupRole, index);
    } //-- void setGroupRole(int, com.hp.globalops.hppcbl.webservice.GroupRole) 

    /**
     * Method setGroupRole
     * 
     * 
     * 
     * @param groupRoleArray
     */
    public void setGroupRole(com.hp.globalops.hppcbl.webservice.GroupRole[] groupRoleArray)
    {
        //-- copy array
        _groupRoleList.removeAllElements();
        for (int i = 0; i < groupRoleArray.length; i++) {
            _groupRoleList.addElement(groupRoleArray[i]);
        }
    } //-- void setGroupRole(com.hp.globalops.hppcbl.webservice.GroupRole) 

    /**
     * Sets the value of field 'profileId'.
     * 
     * @param profileId the value of field 'profileId'.
     */
    public void setProfileId(java.lang.String profileId)
    {
        this._profileId = profileId;
    } //-- void setProfileId(java.lang.String) 

}
