/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: AdminModifyUserRequestTypeDescriptor.java,v 1.1 2006/11/08 07:34:24 yjie Exp $
 */

package com.hp.globalops.hppcbl.webservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.mapping.AccessMode;
import org.exolab.castor.xml.TypeValidator;
import org.exolab.castor.xml.XMLFieldDescriptor;
import org.exolab.castor.xml.validators.*;

/**
 * Class AdminModifyUserRequestTypeDescriptor.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:24 $
 */
public class AdminModifyUserRequestTypeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field nsPrefix
     */
    private java.lang.String nsPrefix;

    /**
     * Field nsURI
     */
    private java.lang.String nsURI;

    /**
     * Field xmlName
     */
    private java.lang.String xmlName;

    /**
     * Field identity
     */
    private org.exolab.castor.xml.XMLFieldDescriptor identity;


      //----------------/
     //- Constructors -/
    //----------------/

    public AdminModifyUserRequestTypeDescriptor() 
     {
        super();
        nsURI = "http://hppws.globalops.hp.com/PassportService";
        xmlName = "adminModifyUserRequestType";
        
        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _adminSessionToken
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_adminSessionToken", "adminSessionToken", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AdminModifyUserRequestType target = (AdminModifyUserRequestType) object;
                return target.getAdminSessionToken();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AdminModifyUserRequestType target = (AdminModifyUserRequestType) object;
                    target.setAdminSessionToken( (java.lang.String) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://hppws.globalops.hp.com/PassportService");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _adminSessionToken
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _profileId
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_profileId", "profileId", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AdminModifyUserRequestType target = (AdminModifyUserRequestType) object;
                return target.getProfileId();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AdminModifyUserRequestType target = (AdminModifyUserRequestType) object;
                    target.setProfileId( (java.lang.String) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return null;
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://hppws.globalops.hp.com/PassportService");
        desc.setRequired(true);
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _profileId
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(1);
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _profileCore
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.hp.globalops.hppcbl.webservice.ProfileCore.class, "_profileCore", "profileCore", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AdminModifyUserRequestType target = (AdminModifyUserRequestType) object;
                return target.getProfileCore();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AdminModifyUserRequestType target = (AdminModifyUserRequestType) object;
                    target.setProfileCore( (com.hp.globalops.hppcbl.webservice.ProfileCore) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new com.hp.globalops.hppcbl.webservice.ProfileCore();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://hppws.globalops.hp.com/PassportService");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _profileCore
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _profileExtended
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.hp.globalops.hppcbl.webservice.ProfileExtended.class, "_profileExtended", "profileExtended", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AdminModifyUserRequestType target = (AdminModifyUserRequestType) object;
                return target.getProfileExtended();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AdminModifyUserRequestType target = (AdminModifyUserRequestType) object;
                    target.setProfileExtended( (com.hp.globalops.hppcbl.webservice.ProfileExtended) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new com.hp.globalops.hppcbl.webservice.ProfileExtended();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://hppws.globalops.hp.com/PassportService");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _profileExtended
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
        //-- _emailTemplate
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.hp.globalops.hppcbl.webservice.EmailTemplate.class, "_emailTemplate", "emailTemplate", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                AdminModifyUserRequestType target = (AdminModifyUserRequestType) object;
                return target.getEmailTemplate();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    AdminModifyUserRequestType target = (AdminModifyUserRequestType) object;
                    target.setEmailTemplate( (com.hp.globalops.hppcbl.webservice.EmailTemplate) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new com.hp.globalops.hppcbl.webservice.EmailTemplate();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://hppws.globalops.hp.com/PassportService");
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _emailTemplate
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    } //-- com.hp.globalops.hppcbl.webservice.AdminModifyUserRequestTypeDescriptor()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method getAccessMode
     * 
     * 
     * 
     * @return AccessMode
     */
    public org.exolab.castor.mapping.AccessMode getAccessMode()
    {
        return null;
    } //-- org.exolab.castor.mapping.AccessMode getAccessMode() 

    /**
     * Method getExtends
     * 
     * 
     * 
     * @return ClassDescriptor
     */
    public org.exolab.castor.mapping.ClassDescriptor getExtends()
    {
        return null;
    } //-- org.exolab.castor.mapping.ClassDescriptor getExtends() 

    /**
     * Method getIdentity
     * 
     * 
     * 
     * @return FieldDescriptor
     */
    public org.exolab.castor.mapping.FieldDescriptor getIdentity()
    {
        return identity;
    } //-- org.exolab.castor.mapping.FieldDescriptor getIdentity() 

    /**
     * Method getJavaClass
     * 
     * 
     * 
     * @return Class
     */
    public java.lang.Class getJavaClass()
    {
        return com.hp.globalops.hppcbl.webservice.AdminModifyUserRequestType.class;
    } //-- java.lang.Class getJavaClass() 

    /**
     * Method getNameSpacePrefix
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String getNameSpacePrefix()
    {
        return nsPrefix;
    } //-- java.lang.String getNameSpacePrefix() 

    /**
     * Method getNameSpaceURI
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String getNameSpaceURI()
    {
        return nsURI;
    } //-- java.lang.String getNameSpaceURI() 

    /**
     * Method getValidator
     * 
     * 
     * 
     * @return TypeValidator
     */
    public org.exolab.castor.xml.TypeValidator getValidator()
    {
        return this;
    } //-- org.exolab.castor.xml.TypeValidator getValidator() 

    /**
     * Method getXMLName
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String getXMLName()
    {
        return xmlName;
    } //-- java.lang.String getXMLName() 

}
