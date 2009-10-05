/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: GetUserIdListRequestType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
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
 * Class GetUserIdListRequestType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class GetUserIdListRequestType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _profileIdList
     */
    private java.util.Vector _profileIdList;

    /**
     * Field _applicationRefIdList
     */
    private java.util.Vector _applicationRefIdList;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetUserIdListRequestType() 
     {
        super();
        _profileIdList = new Vector();
        _applicationRefIdList = new Vector();
    } //-- com.hp.globalops.hppcbl.webservice.GetUserIdListRequestType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addApplicationRefId
     * 
     * 
     * 
     * @param vApplicationRefId
     */
    public void addApplicationRefId(java.lang.String vApplicationRefId)
        throws java.lang.IndexOutOfBoundsException
    {
        _applicationRefIdList.addElement(vApplicationRefId);
    } //-- void addApplicationRefId(java.lang.String) 

    /**
     * Method addApplicationRefId
     * 
     * 
     * 
     * @param index
     * @param vApplicationRefId
     */
    public void addApplicationRefId(int index, java.lang.String vApplicationRefId)
        throws java.lang.IndexOutOfBoundsException
    {
        _applicationRefIdList.insertElementAt(vApplicationRefId, index);
    } //-- void addApplicationRefId(int, java.lang.String) 

    /**
     * Method addProfileId
     * 
     * 
     * 
     * @param vProfileId
     */
    public void addProfileId(java.lang.String vProfileId)
        throws java.lang.IndexOutOfBoundsException
    {
        _profileIdList.addElement(vProfileId);
    } //-- void addProfileId(java.lang.String) 

    /**
     * Method addProfileId
     * 
     * 
     * 
     * @param index
     * @param vProfileId
     */
    public void addProfileId(int index, java.lang.String vProfileId)
        throws java.lang.IndexOutOfBoundsException
    {
        _profileIdList.insertElementAt(vProfileId, index);
    } //-- void addProfileId(int, java.lang.String) 

    /**
     * Method enumerateApplicationRefId
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateApplicationRefId()
    {
        return _applicationRefIdList.elements();
    } //-- java.util.Enumeration enumerateApplicationRefId() 

    /**
     * Method enumerateProfileId
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateProfileId()
    {
        return _profileIdList.elements();
    } //-- java.util.Enumeration enumerateProfileId() 

    /**
     * Method getApplicationRefId
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String getApplicationRefId(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _applicationRefIdList.size())) {
            throw new IndexOutOfBoundsException("getApplicationRefId: Index value '"+index+"' not in range [0.."+_applicationRefIdList.size()+ "]");
        }
        
        return (String)_applicationRefIdList.elementAt(index);
    } //-- java.lang.String getApplicationRefId(int) 

    /**
     * Method getApplicationRefId
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String[] getApplicationRefId()
    {
        int size = _applicationRefIdList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_applicationRefIdList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getApplicationRefId() 

    /**
     * Method getApplicationRefIdCount
     * 
     * 
     * 
     * @return int
     */
    public int getApplicationRefIdCount()
    {
        return _applicationRefIdList.size();
    } //-- int getApplicationRefIdCount() 

    /**
     * Method getProfileId
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String getProfileId(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _profileIdList.size())) {
            throw new IndexOutOfBoundsException("getProfileId: Index value '"+index+"' not in range [0.."+_profileIdList.size()+ "]");
        }
        
        return (String)_profileIdList.elementAt(index);
    } //-- java.lang.String getProfileId(int) 

    /**
     * Method getProfileId
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String[] getProfileId()
    {
        int size = _profileIdList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_profileIdList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getProfileId() 

    /**
     * Method getProfileIdCount
     * 
     * 
     * 
     * @return int
     */
    public int getProfileIdCount()
    {
        return _profileIdList.size();
    } //-- int getProfileIdCount() 

    /**
     * Method removeAllApplicationRefId
     * 
     */
    public void removeAllApplicationRefId()
    {
        _applicationRefIdList.removeAllElements();
    } //-- void removeAllApplicationRefId() 

    /**
     * Method removeAllProfileId
     * 
     */
    public void removeAllProfileId()
    {
        _profileIdList.removeAllElements();
    } //-- void removeAllProfileId() 

    /**
     * Method removeApplicationRefId
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String removeApplicationRefId(int index)
    {
        java.lang.Object obj = _applicationRefIdList.elementAt(index);
        _applicationRefIdList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeApplicationRefId(int) 

    /**
     * Method removeProfileId
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String removeProfileId(int index)
    {
        java.lang.Object obj = _profileIdList.elementAt(index);
        _profileIdList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeProfileId(int) 

    /**
     * Method setApplicationRefId
     * 
     * 
     * 
     * @param index
     * @param vApplicationRefId
     */
    public void setApplicationRefId(int index, java.lang.String vApplicationRefId)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _applicationRefIdList.size())) {
            throw new IndexOutOfBoundsException("setApplicationRefId: Index value '"+index+"' not in range [0.."+_applicationRefIdList.size()+ "]");
        }
        _applicationRefIdList.setElementAt(vApplicationRefId, index);
    } //-- void setApplicationRefId(int, java.lang.String) 

    /**
     * Method setApplicationRefId
     * 
     * 
     * 
     * @param applicationRefIdArray
     */
    public void setApplicationRefId(java.lang.String[] applicationRefIdArray)
    {
        //-- copy array
        _applicationRefIdList.removeAllElements();
        for (int i = 0; i < applicationRefIdArray.length; i++) {
            _applicationRefIdList.addElement(applicationRefIdArray[i]);
        }
    } //-- void setApplicationRefId(java.lang.String) 

    /**
     * Method setProfileId
     * 
     * 
     * 
     * @param index
     * @param vProfileId
     */
    public void setProfileId(int index, java.lang.String vProfileId)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _profileIdList.size())) {
            throw new IndexOutOfBoundsException("setProfileId: Index value '"+index+"' not in range [0.."+_profileIdList.size()+ "]");
        }
        _profileIdList.setElementAt(vProfileId, index);
    } //-- void setProfileId(int, java.lang.String) 

    /**
     * Method setProfileId
     * 
     * 
     * 
     * @param profileIdArray
     */
    public void setProfileId(java.lang.String[] profileIdArray)
    {
        //-- copy array
        _profileIdList.removeAllElements();
        for (int i = 0; i < profileIdArray.length; i++) {
            _profileIdList.addElement(profileIdArray[i]);
        }
    } //-- void setProfileId(java.lang.String) 

}
