/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: GetMembersOfGroupResultType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
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
 * Class GetMembersOfGroupResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class GetMembersOfGroupResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _userIdList
     */
    private java.util.Vector _userIdList;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetMembersOfGroupResultType() 
     {
        super();
        _userIdList = new Vector();
    } //-- com.hp.globalops.hppcbl.webservice.GetMembersOfGroupResultType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addUserId
     * 
     * 
     * 
     * @param vUserId
     */
    public void addUserId(java.lang.String vUserId)
        throws java.lang.IndexOutOfBoundsException
    {
        _userIdList.addElement(vUserId);
    } //-- void addUserId(java.lang.String) 

    /**
     * Method addUserId
     * 
     * 
     * 
     * @param index
     * @param vUserId
     */
    public void addUserId(int index, java.lang.String vUserId)
        throws java.lang.IndexOutOfBoundsException
    {
        _userIdList.insertElementAt(vUserId, index);
    } //-- void addUserId(int, java.lang.String) 

    /**
     * Method enumerateUserId
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateUserId()
    {
        return _userIdList.elements();
    } //-- java.util.Enumeration enumerateUserId() 

    /**
     * Method getUserId
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String getUserId(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _userIdList.size())) {
            throw new IndexOutOfBoundsException("getUserId: Index value '"+index+"' not in range [0.."+_userIdList.size()+ "]");
        }
        
        return (String)_userIdList.elementAt(index);
    } //-- java.lang.String getUserId(int) 

    /**
     * Method getUserId
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String[] getUserId()
    {
        int size = _userIdList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_userIdList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getUserId() 

    /**
     * Method getUserIdCount
     * 
     * 
     * 
     * @return int
     */
    public int getUserIdCount()
    {
        return _userIdList.size();
    } //-- int getUserIdCount() 

    /**
     * Method removeAllUserId
     * 
     */
    public void removeAllUserId()
    {
        _userIdList.removeAllElements();
    } //-- void removeAllUserId() 

    /**
     * Method removeUserId
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String removeUserId(int index)
    {
        java.lang.Object obj = _userIdList.elementAt(index);
        _userIdList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeUserId(int) 

    /**
     * Method setUserId
     * 
     * 
     * 
     * @param index
     * @param vUserId
     */
    public void setUserId(int index, java.lang.String vUserId)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _userIdList.size())) {
            throw new IndexOutOfBoundsException("setUserId: Index value '"+index+"' not in range [0.."+_userIdList.size()+ "]");
        }
        _userIdList.setElementAt(vUserId, index);
    } //-- void setUserId(int, java.lang.String) 

    /**
     * Method setUserId
     * 
     * 
     * 
     * @param userIdArray
     */
    public void setUserId(java.lang.String[] userIdArray)
    {
        //-- copy array
        _userIdList.removeAllElements();
        for (int i = 0; i < userIdArray.length; i++) {
            _userIdList.addElement(userIdArray[i]);
        }
    } //-- void setUserId(java.lang.String) 

}
