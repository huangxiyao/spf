/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: GetUserIdListResultType.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
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
 * Class GetUserIdListResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class GetUserIdListResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _profileIdResultList
     */
    private java.util.Vector _profileIdResultList;

    /**
     * Field _applicationRefIdResultList
     */
    private java.util.Vector _applicationRefIdResultList;


      //----------------/
     //- Constructors -/
    //----------------/

    public GetUserIdListResultType() 
     {
        super();
        _profileIdResultList = new Vector();
        _applicationRefIdResultList = new Vector();
    } //-- com.hp.globalops.hppcbl.webservice.GetUserIdListResultType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addApplicationRefIdResult
     * 
     * 
     * 
     * @param vApplicationRefIdResult
     */
    public void addApplicationRefIdResult(com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult vApplicationRefIdResult)
        throws java.lang.IndexOutOfBoundsException
    {
        _applicationRefIdResultList.addElement(vApplicationRefIdResult);
    } //-- void addApplicationRefIdResult(com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult) 

    /**
     * Method addApplicationRefIdResult
     * 
     * 
     * 
     * @param index
     * @param vApplicationRefIdResult
     */
    public void addApplicationRefIdResult(int index, com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult vApplicationRefIdResult)
        throws java.lang.IndexOutOfBoundsException
    {
        _applicationRefIdResultList.insertElementAt(vApplicationRefIdResult, index);
    } //-- void addApplicationRefIdResult(int, com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult) 

    /**
     * Method addProfileIdResult
     * 
     * 
     * 
     * @param vProfileIdResult
     */
    public void addProfileIdResult(com.hp.globalops.hppcbl.webservice.ProfileIdResult vProfileIdResult)
        throws java.lang.IndexOutOfBoundsException
    {
        _profileIdResultList.addElement(vProfileIdResult);
    } //-- void addProfileIdResult(com.hp.globalops.hppcbl.webservice.ProfileIdResult) 

    /**
     * Method addProfileIdResult
     * 
     * 
     * 
     * @param index
     * @param vProfileIdResult
     */
    public void addProfileIdResult(int index, com.hp.globalops.hppcbl.webservice.ProfileIdResult vProfileIdResult)
        throws java.lang.IndexOutOfBoundsException
    {
        _profileIdResultList.insertElementAt(vProfileIdResult, index);
    } //-- void addProfileIdResult(int, com.hp.globalops.hppcbl.webservice.ProfileIdResult) 

    /**
     * Method enumerateApplicationRefIdResult
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateApplicationRefIdResult()
    {
        return _applicationRefIdResultList.elements();
    } //-- java.util.Enumeration enumerateApplicationRefIdResult() 

    /**
     * Method enumerateProfileIdResult
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateProfileIdResult()
    {
        return _profileIdResultList.elements();
    } //-- java.util.Enumeration enumerateProfileIdResult() 

    /**
     * Method getApplicationRefIdResult
     * 
     * 
     * 
     * @param index
     * @return ApplicationRefIdResult
     */
    public com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult getApplicationRefIdResult(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _applicationRefIdResultList.size())) {
            throw new IndexOutOfBoundsException("getApplicationRefIdResult: Index value '"+index+"' not in range [0.."+_applicationRefIdResultList.size()+ "]");
        }
        
        return (com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult) _applicationRefIdResultList.elementAt(index);
    } //-- com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult getApplicationRefIdResult(int) 

    /**
     * Method getApplicationRefIdResult
     * 
     * 
     * 
     * @return ApplicationRefIdResult
     */
    public com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult[] getApplicationRefIdResult()
    {
        int size = _applicationRefIdResultList.size();
        com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult[] mArray = new com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult) _applicationRefIdResultList.elementAt(index);
        }
        return mArray;
    } //-- com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult[] getApplicationRefIdResult() 

    /**
     * Method getApplicationRefIdResultCount
     * 
     * 
     * 
     * @return int
     */
    public int getApplicationRefIdResultCount()
    {
        return _applicationRefIdResultList.size();
    } //-- int getApplicationRefIdResultCount() 

    /**
     * Method getProfileIdResult
     * 
     * 
     * 
     * @param index
     * @return ProfileIdResult
     */
    public com.hp.globalops.hppcbl.webservice.ProfileIdResult getProfileIdResult(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _profileIdResultList.size())) {
            throw new IndexOutOfBoundsException("getProfileIdResult: Index value '"+index+"' not in range [0.."+_profileIdResultList.size()+ "]");
        }
        
        return (com.hp.globalops.hppcbl.webservice.ProfileIdResult) _profileIdResultList.elementAt(index);
    } //-- com.hp.globalops.hppcbl.webservice.ProfileIdResult getProfileIdResult(int) 

    /**
     * Method getProfileIdResult
     * 
     * 
     * 
     * @return ProfileIdResult
     */
    public com.hp.globalops.hppcbl.webservice.ProfileIdResult[] getProfileIdResult()
    {
        int size = _profileIdResultList.size();
        com.hp.globalops.hppcbl.webservice.ProfileIdResult[] mArray = new com.hp.globalops.hppcbl.webservice.ProfileIdResult[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.hp.globalops.hppcbl.webservice.ProfileIdResult) _profileIdResultList.elementAt(index);
        }
        return mArray;
    } //-- com.hp.globalops.hppcbl.webservice.ProfileIdResult[] getProfileIdResult() 

    /**
     * Method getProfileIdResultCount
     * 
     * 
     * 
     * @return int
     */
    public int getProfileIdResultCount()
    {
        return _profileIdResultList.size();
    } //-- int getProfileIdResultCount() 

    /**
     * Method removeAllApplicationRefIdResult
     * 
     */
    public void removeAllApplicationRefIdResult()
    {
        _applicationRefIdResultList.removeAllElements();
    } //-- void removeAllApplicationRefIdResult() 

    /**
     * Method removeAllProfileIdResult
     * 
     */
    public void removeAllProfileIdResult()
    {
        _profileIdResultList.removeAllElements();
    } //-- void removeAllProfileIdResult() 

    /**
     * Method removeApplicationRefIdResult
     * 
     * 
     * 
     * @param index
     * @return ApplicationRefIdResult
     */
    public com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult removeApplicationRefIdResult(int index)
    {
        java.lang.Object obj = _applicationRefIdResultList.elementAt(index);
        _applicationRefIdResultList.removeElementAt(index);
        return (com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult) obj;
    } //-- com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult removeApplicationRefIdResult(int) 

    /**
     * Method removeProfileIdResult
     * 
     * 
     * 
     * @param index
     * @return ProfileIdResult
     */
    public com.hp.globalops.hppcbl.webservice.ProfileIdResult removeProfileIdResult(int index)
    {
        java.lang.Object obj = _profileIdResultList.elementAt(index);
        _profileIdResultList.removeElementAt(index);
        return (com.hp.globalops.hppcbl.webservice.ProfileIdResult) obj;
    } //-- com.hp.globalops.hppcbl.webservice.ProfileIdResult removeProfileIdResult(int) 

    /**
     * Method setApplicationRefIdResult
     * 
     * 
     * 
     * @param index
     * @param vApplicationRefIdResult
     */
    public void setApplicationRefIdResult(int index, com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult vApplicationRefIdResult)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _applicationRefIdResultList.size())) {
            throw new IndexOutOfBoundsException("setApplicationRefIdResult: Index value '"+index+"' not in range [0.."+_applicationRefIdResultList.size()+ "]");
        }
        _applicationRefIdResultList.setElementAt(vApplicationRefIdResult, index);
    } //-- void setApplicationRefIdResult(int, com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult) 

    /**
     * Method setApplicationRefIdResult
     * 
     * 
     * 
     * @param applicationRefIdResultArray
     */
    public void setApplicationRefIdResult(com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult[] applicationRefIdResultArray)
    {
        //-- copy array
        _applicationRefIdResultList.removeAllElements();
        for (int i = 0; i < applicationRefIdResultArray.length; i++) {
            _applicationRefIdResultList.addElement(applicationRefIdResultArray[i]);
        }
    } //-- void setApplicationRefIdResult(com.hp.globalops.hppcbl.webservice.ApplicationRefIdResult) 

    /**
     * Method setProfileIdResult
     * 
     * 
     * 
     * @param index
     * @param vProfileIdResult
     */
    public void setProfileIdResult(int index, com.hp.globalops.hppcbl.webservice.ProfileIdResult vProfileIdResult)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _profileIdResultList.size())) {
            throw new IndexOutOfBoundsException("setProfileIdResult: Index value '"+index+"' not in range [0.."+_profileIdResultList.size()+ "]");
        }
        _profileIdResultList.setElementAt(vProfileIdResult, index);
    } //-- void setProfileIdResult(int, com.hp.globalops.hppcbl.webservice.ProfileIdResult) 

    /**
     * Method setProfileIdResult
     * 
     * 
     * 
     * @param profileIdResultArray
     */
    public void setProfileIdResult(com.hp.globalops.hppcbl.webservice.ProfileIdResult[] profileIdResultArray)
    {
        //-- copy array
        _profileIdResultList.removeAllElements();
        for (int i = 0; i < profileIdResultArray.length; i++) {
            _profileIdResultList.addElement(profileIdResultArray[i]);
        }
    } //-- void setProfileIdResult(com.hp.globalops.hppcbl.webservice.ProfileIdResult) 

}
