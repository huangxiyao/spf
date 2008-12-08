/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: AdminModifyUserResultType.java,v 1.1 2006/11/08 07:34:24 yjie Exp $
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
 * Class AdminModifyUserResultType.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:24 $
 */
public class AdminModifyUserResultType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _profileId
     */
    private java.lang.String _profileId;

    /**
     * Field _modifiedDataList
     */
    private java.util.Vector _modifiedDataList;


      //----------------/
     //- Constructors -/
    //----------------/

    public AdminModifyUserResultType() 
     {
        super();
        _modifiedDataList = new Vector();
    } //-- com.hp.globalops.hppcbl.webservice.AdminModifyUserResultType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addModifiedData
     * 
     * 
     * 
     * @param vModifiedData
     */
    public void addModifiedData(com.hp.globalops.hppcbl.webservice.ModifiedData vModifiedData)
        throws java.lang.IndexOutOfBoundsException
    {
        _modifiedDataList.addElement(vModifiedData);
    } //-- void addModifiedData(com.hp.globalops.hppcbl.webservice.ModifiedData) 

    /**
     * Method addModifiedData
     * 
     * 
     * 
     * @param index
     * @param vModifiedData
     */
    public void addModifiedData(int index, com.hp.globalops.hppcbl.webservice.ModifiedData vModifiedData)
        throws java.lang.IndexOutOfBoundsException
    {
        _modifiedDataList.insertElementAt(vModifiedData, index);
    } //-- void addModifiedData(int, com.hp.globalops.hppcbl.webservice.ModifiedData) 

    /**
     * Method enumerateModifiedData
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateModifiedData()
    {
        return _modifiedDataList.elements();
    } //-- java.util.Enumeration enumerateModifiedData() 

    /**
     * Method getModifiedData
     * 
     * 
     * 
     * @param index
     * @return ModifiedData
     */
    public com.hp.globalops.hppcbl.webservice.ModifiedData getModifiedData(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _modifiedDataList.size())) {
            throw new IndexOutOfBoundsException("getModifiedData: Index value '"+index+"' not in range [0.."+_modifiedDataList.size()+ "]");
        }
        
        return (com.hp.globalops.hppcbl.webservice.ModifiedData) _modifiedDataList.elementAt(index);
    } //-- com.hp.globalops.hppcbl.webservice.ModifiedData getModifiedData(int) 

    /**
     * Method getModifiedData
     * 
     * 
     * 
     * @return ModifiedData
     */
    public com.hp.globalops.hppcbl.webservice.ModifiedData[] getModifiedData()
    {
        int size = _modifiedDataList.size();
        com.hp.globalops.hppcbl.webservice.ModifiedData[] mArray = new com.hp.globalops.hppcbl.webservice.ModifiedData[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.hp.globalops.hppcbl.webservice.ModifiedData) _modifiedDataList.elementAt(index);
        }
        return mArray;
    } //-- com.hp.globalops.hppcbl.webservice.ModifiedData[] getModifiedData() 

    /**
     * Method getModifiedDataCount
     * 
     * 
     * 
     * @return int
     */
    public int getModifiedDataCount()
    {
        return _modifiedDataList.size();
    } //-- int getModifiedDataCount() 

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
     * Method removeAllModifiedData
     * 
     */
    public void removeAllModifiedData()
    {
        _modifiedDataList.removeAllElements();
    } //-- void removeAllModifiedData() 

    /**
     * Method removeModifiedData
     * 
     * 
     * 
     * @param index
     * @return ModifiedData
     */
    public com.hp.globalops.hppcbl.webservice.ModifiedData removeModifiedData(int index)
    {
        java.lang.Object obj = _modifiedDataList.elementAt(index);
        _modifiedDataList.removeElementAt(index);
        return (com.hp.globalops.hppcbl.webservice.ModifiedData) obj;
    } //-- com.hp.globalops.hppcbl.webservice.ModifiedData removeModifiedData(int) 

    /**
     * Method setModifiedData
     * 
     * 
     * 
     * @param index
     * @param vModifiedData
     */
    public void setModifiedData(int index, com.hp.globalops.hppcbl.webservice.ModifiedData vModifiedData)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _modifiedDataList.size())) {
            throw new IndexOutOfBoundsException("setModifiedData: Index value '"+index+"' not in range [0.."+_modifiedDataList.size()+ "]");
        }
        _modifiedDataList.setElementAt(vModifiedData, index);
    } //-- void setModifiedData(int, com.hp.globalops.hppcbl.webservice.ModifiedData) 

    /**
     * Method setModifiedData
     * 
     * 
     * 
     * @param modifiedDataArray
     */
    public void setModifiedData(com.hp.globalops.hppcbl.webservice.ModifiedData[] modifiedDataArray)
    {
        //-- copy array
        _modifiedDataList.removeAllElements();
        for (int i = 0; i < modifiedDataArray.length; i++) {
            _modifiedDataList.addElement(modifiedDataArray[i]);
        }
    } //-- void setModifiedData(com.hp.globalops.hppcbl.webservice.ModifiedData) 

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
