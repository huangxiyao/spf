/*
 * This class was automatically generated with
 * <a href="http://www.castor.org">Castor 0.9.9</a>, using an XML
 * Schema.
 * $Id: ItemList.java,v 1.1.1.1 2006/09/19 14:49:43 akashs Exp $
 */

package com.hp.spp.search.common;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class ItemList.
 *
 * @version $Revision: 1.1.1.1 $ $Date: 2006/09/19 14:49:43 $
 */
public class ItemList implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field mNavigationItemList
     */
    private java.util.List mNavigationItemList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ItemList()
     {
        super();
        mNavigationItemList = new ArrayList();
    } //-- com.hp.spp.search.common.ItemList()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addNavigationItem
     *
     *
     *
     * @param vNavigationItem
     */
    public void addNavigationItem(com.hp.spp.search.common.NavigationItem vNavigationItem)
        throws java.lang.IndexOutOfBoundsException
    {
        mNavigationItemList.add(vNavigationItem);
    } //-- void addNavigationItem(com.hp.spp.search.common.NavigationItem)

    /**
     * Method addNavigationItem
     *
     *
     *
     * @param index
     * @param vNavigationItem
     */
    public void addNavigationItem(int index, com.hp.spp.search.common.NavigationItem vNavigationItem)
        throws java.lang.IndexOutOfBoundsException
    {
        mNavigationItemList.add(index, vNavigationItem);
    } //-- void addNavigationItem(int, com.hp.spp.search.common.NavigationItem)

    /**
     * Method clearNavigationItem
     *
     */
    public void clearNavigationItem()
    {
        mNavigationItemList.clear();
    } //-- void clearNavigationItem()

    /**
     * Method enumerateNavigationItem
     *
     *
     *
     * @return Enumeration
     */
    public java.util.Enumeration enumerateNavigationItem()
    {
        return new org.exolab.castor.util.IteratorEnumeration(mNavigationItemList.iterator());
    } //-- java.util.Enumeration enumerateNavigationItem()

    /**
     * Method getNavigationItem
     *
     *
     *
     * @param index
     * @return NavigationItem
     */
    public com.hp.spp.search.common.NavigationItem getNavigationItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > mNavigationItemList.size())) {
            throw new IndexOutOfBoundsException();
        }

        return (com.hp.spp.search.common.NavigationItem) mNavigationItemList.get(index);
    } //-- com.hp.spp.search.common.NavigationItem getNavigationItem(int)

    /**
     * Method getNavigationItem
     *
     *
     *
     * @return NavigationItem
     */
    public com.hp.spp.search.common.NavigationItem[] getNavigationItem()
    {
        int size = mNavigationItemList.size();
        com.hp.spp.search.common.NavigationItem[] mArray = new com.hp.spp.search.common.NavigationItem[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (com.hp.spp.search.common.NavigationItem) mNavigationItemList.get(index);
        }
        return mArray;
    } //-- com.hp.spp.search.common.NavigationItem[] getNavigationItem()

    /**
     * Method getNavigationItemList
     *
     *
     *
     * @return List
     */
    public List getNavigationItemList()
    {
       
        return mNavigationItemList;
    }
    
    /**
     * Method getNavigationItemCount
     *
     *
     *
     * @return int
     */
    public int getNavigationItemCount()
    {
        return mNavigationItemList.size();
    } //-- int getNavigationItemCount()

    /**
     * Method isValid
     *
     *
     *
     * @return boolean
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid()

    /**
     * Method marshal
     *
     *
     *
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {

        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer)

    /**
     * Method marshal
     *
     *
     *
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {

        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler)

    /**
     * Method removeNavigationItem
     *
     *
     *
     * @param vNavigationItem
     * @return boolean
     */
    public boolean removeNavigationItem(com.hp.spp.search.common.NavigationItem vNavigationItem)
    {
        boolean removed = mNavigationItemList.remove(vNavigationItem);
        return removed;
    } //-- boolean removeNavigationItem(com.hp.spp.search.common.NavigationItem)

    /**
     * Method setNavigationItem
     *
     *
     *
     * @param index
     * @param vNavigationItem
     */
    public void setNavigationItem(int index, com.hp.spp.search.common.NavigationItem vNavigationItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > mNavigationItemList.size())) {
            throw new IndexOutOfBoundsException();
        }
        mNavigationItemList.set(index, vNavigationItem);
    } //-- void setNavigationItem(int, com.hp.spp.search.common.NavigationItem)

    /**
     * Method setNavigationItem
     *
     *
     *
     * @param navigationItemArray
     */
    public void setNavigationItem(com.hp.spp.search.common.NavigationItem[] navigationItemArray)
    {
        //-- copy array
        mNavigationItemList.clear();
        for (int i = 0; i < navigationItemArray.length; i++) {
            mNavigationItemList.add(navigationItemArray[i]);
        }
    } //-- void setNavigationItem(com.hp.spp.search.common.NavigationItem)

    /**
     * Method setNavigationItemList
     *
     *
     *
     * @param List
     */
    public void setNavigationItemList(List navigationItemList)
    {
       
        mNavigationItemList = navigationItemList;
    }
    
    /**
     * Method unmarshal
     *
     *
     *
     * @param reader
     * @return ItemList
     */
    public static com.hp.spp.search.common.ItemList unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.hp.spp.search.common.ItemList) Unmarshaller.unmarshal(com.hp.spp.search.common.ItemList.class, reader);
    } //-- com.hp.spp.search.common.ItemList unmarshal(java.io.Reader)

    /**
     * Method validate
     *
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate()

}
