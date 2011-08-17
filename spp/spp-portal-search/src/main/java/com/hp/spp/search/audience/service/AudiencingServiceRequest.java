/*
 * This class was automatically generated with
 * <a href="http://www.castor.org">Castor 0.9.9</a>, using an XML
 * Schema.
 * $Id: AudiencingServiceRequest.java,v 1.2 2006/09/27 12:27:04 akashs Exp $
 */

package com.hp.spp.search.audience.service;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class AudiencingServiceRequest.
 *
 * @version $Revision: 1.2 $ $Date: 2006/09/27 12:27:04 $
 */
public class AudiencingServiceRequest implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field mUser
     */
    private com.hp.spp.search.common.User mUser;

    /**
     * Field mSite
     */
    private com.hp.spp.search.common.Site mSite;

    /**
     * Field mItemList
     */
    private com.hp.spp.search.common.ItemList mItemList;


      //----------------/
     //- Constructors -/
    //----------------/

    public AudiencingServiceRequest()
     {
        super();
       
    } //-- com.hp.spp.search.common.AudiencingServiceRequest()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'itemList'.
     *
     * @return ItemList
     * @return the value of field 'itemList'.
     */
    public com.hp.spp.search.common.ItemList getItemList()
    {
        return this.mItemList;
    } //-- com.hp.spp.search.common.ItemList getItemList()

    /**
     * Returns the value of field 'site'.
     *
     * @return Site
     * @return the value of field 'site'.
     */
    public com.hp.spp.search.common.Site getSite()
    {
        return this.mSite;
    } //-- com.hp.spp.search.common.Site getSite()

    /**
     * Returns the value of field 'user'.
     *
     * @return User
     * @return the value of field 'user'.
     */
    public com.hp.spp.search.common.User getUser()
    {
        return this.mUser;
    } //-- com.hp.spp.search.common.User getUser()

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
        	vex.printStackTrace();
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
     * Sets the value of field 'itemList'.
     *
     * @param itemList the value of field 'itemList'.
     */
    public void setItemList(com.hp.spp.search.common.ItemList itemList)
    {
        this.mItemList = itemList;
    } //-- void setItemList(com.hp.spp.search.common.ItemList)

    /**
     * Sets the value of field 'site'.
     *
     * @param site the value of field 'site'.
     */
    public void setSite(com.hp.spp.search.common.Site site)
    {
        this.mSite = site;
    } //-- void setSite(com.hp.spp.search.common.Site)

    /**
     * Sets the value of field 'user'.
     *
     * @param user the value of field 'user'.
     */
    public void setUser(com.hp.spp.search.common.User user)
    {
    	this.mUser = user;
        
    } //-- void setUser(com.hp.spp.search.common.User)

    /**
     * Method unmarshal
     *
     *
     *
     * @param reader
     * @return AudiencingServiceRequest
     */
    public static com.hp.spp.search.audience.service.AudiencingServiceRequest unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (com.hp.spp.search.audience.service.AudiencingServiceRequest) Unmarshaller.unmarshal(com.hp.spp.search.audience.service.AudiencingServiceRequest.class, reader);
    } //-- com.hp.spp.search.common.AudiencingServiceRequest unmarshal(java.io.Reader)

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
