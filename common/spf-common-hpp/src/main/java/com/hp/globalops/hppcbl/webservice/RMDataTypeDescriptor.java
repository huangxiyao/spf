/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: RMDataTypeDescriptor.java,v 1.1 2006/11/08 07:34:26 yjie Exp $
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
 * Class RMDataTypeDescriptor.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:26 $
 */
public class RMDataTypeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


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

    public RMDataTypeDescriptor() 
     {
        super();
        nsURI = "http://hppws.globalops.hp.com/PassportService";
        xmlName = "rMDataType";
        
        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _userId
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_userId", "userId", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getUserId();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setUserId( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _userId
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
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
                RMDataType target = (RMDataType) object;
                return target.getProfileId();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _profileId
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _title
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_title", "title", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getTitle();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setTitle( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _title
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _firstName
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_firstName", "firstName", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getFirstName();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setFirstName( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _firstName
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _middleName
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_middleName", "middleName", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getMiddleName();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setMiddleName( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _middleName
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _lastName
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_lastName", "lastName", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getLastName();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setLastName( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _lastName
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _email
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_email", "email", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getEmail();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setEmail( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _email
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _segmentName
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_segmentName", "segmentName", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getSegmentName();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setSegmentName( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _segmentName
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _residentCountryCode
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_residentCountryCode", "residentCountryCode", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getResidentCountryCode();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setResidentCountryCode( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _residentCountryCode
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _langCode
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_langCode", "langCode", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getLangCode();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setLangCode( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _langCode
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _localizationCode
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_localizationCode", "localizationCode", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getLocalizationCode();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setLocalizationCode( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _localizationCode
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _contactPrefEmail
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_contactPrefEmail", "contactPrefEmail", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getContactPrefEmail();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setContactPrefEmail( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _contactPrefEmail
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _contactPrefTelephone
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_contactPrefTelephone", "contactPrefTelephone", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getContactPrefTelephone();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setContactPrefTelephone( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _contactPrefTelephone
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _contactPrefPost
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_contactPrefPost", "contactPrefPost", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getContactPrefPost();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setContactPrefPost( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _contactPrefPost
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _securityLevel
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_securityLevel", "securityLevel", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getSecurityLevel();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setSecurityLevel( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _securityLevel
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _homeAddressLine1
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_homeAddressLine1", "homeAddressLine1", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getHomeAddressLine1();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setHomeAddressLine1( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _homeAddressLine1
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _homeAddressLine2
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_homeAddressLine2", "homeAddressLine2", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getHomeAddressLine2();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setHomeAddressLine2( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _homeAddressLine2
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _homeCity
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_homeCity", "homeCity", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getHomeCity();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setHomeCity( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _homeCity
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _homeDistrict
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_homeDistrict", "homeDistrict", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getHomeDistrict();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setHomeDistrict( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _homeDistrict
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _homeState
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_homeState", "homeState", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getHomeState();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setHomeState( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _homeState
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _homePostalCode
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_homePostalCode", "homePostalCode", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getHomePostalCode();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setHomePostalCode( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _homePostalCode
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _homeCountryCode
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_homeCountryCode", "homeCountryCode", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getHomeCountryCode();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setHomeCountryCode( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _homeCountryCode
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _homePOBox
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_homePOBox", "homePOBox", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getHomePOBox();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setHomePOBox( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _homePOBox
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _homeTelephoneCountryCode
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_homeTelephoneCountryCode", "homeTelephoneCountryCode", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getHomeTelephoneCountryCode();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setHomeTelephoneCountryCode( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _homeTelephoneCountryCode
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _homeTelephoneCityCode
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_homeTelephoneCityCode", "homeTelephoneCityCode", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getHomeTelephoneCityCode();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setHomeTelephoneCityCode( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _homeTelephoneCityCode
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _homeTelephoneNumber
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_homeTelephoneNumber", "homeTelephoneNumber", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getHomeTelephoneNumber();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setHomeTelephoneNumber( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _homeTelephoneNumber
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _creationAppId
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_creationAppId", "creationAppId", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getCreationAppId();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setCreationAppId( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _creationAppId
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _profileCreationDate
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_profileCreationDate", "profileCreationDate", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getProfileCreationDate();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setProfileCreationDate( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _profileCreationDate
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _profileLastUpdatedApplicationId
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_profileLastUpdatedApplicationId", "profileLastUpdatedApplicationId", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getProfileLastUpdatedApplicationId();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setProfileLastUpdatedApplicationId( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _profileLastUpdatedApplicationId
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _profileLastUpdatedDate
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_profileLastUpdatedDate", "profileLastUpdatedDate", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getProfileLastUpdatedDate();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setProfileLastUpdatedDate( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _profileLastUpdatedDate
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _profileLastUpdatedProfileId
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_profileLastUpdatedProfileId", "profileLastUpdatedProfileId", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getProfileLastUpdatedProfileId();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setProfileLastUpdatedProfileId( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _profileLastUpdatedProfileId
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _cidNumber
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_cidNumber", "cidNumber", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getCidNumber();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setCidNumber( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _cidNumber
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _migrationExceptionFlag
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_migrationExceptionFlag", "migrationExceptionFlag", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getMigrationExceptionFlag();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setMigrationExceptionFlag( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _migrationExceptionFlag
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busCompanyName
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busCompanyName", "busCompanyName", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusCompanyName();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusCompanyName( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busCompanyName
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busBuildingName
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busBuildingName", "busBuildingName", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusBuildingName();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusBuildingName( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busBuildingName
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busMailStop
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busMailStop", "busMailStop", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusMailStop();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusMailStop( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busMailStop
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busAddressLine1
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busAddressLine1", "busAddressLine1", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusAddressLine1();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusAddressLine1( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busAddressLine1
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busAddressLine2
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busAddressLine2", "busAddressLine2", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusAddressLine2();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusAddressLine2( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busAddressLine2
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busCity
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busCity", "busCity", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusCity();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusCity( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busCity
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busDistrict
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busDistrict", "busDistrict", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusDistrict();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusDistrict( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busDistrict
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busState
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busState", "busState", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusState();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusState( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busState
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busPostalCode
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busPostalCode", "busPostalCode", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusPostalCode();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusPostalCode( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busPostalCode
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busCountryCode
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busCountryCode", "busCountryCode", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusCountryCode();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusCountryCode( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busCountryCode
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busAddressLocalizationCode
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busAddressLocalizationCode", "busAddressLocalizationCode", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusAddressLocalizationCode();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusAddressLocalizationCode( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busAddressLocalizationCode
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busTimezoneCode
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busTimezoneCode", "busTimezoneCode", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusTimezoneCode();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusTimezoneCode( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busTimezoneCode
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busTelephoneCountryCode
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busTelephoneCountryCode", "busTelephoneCountryCode", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusTelephoneCountryCode();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusTelephoneCountryCode( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busTelephoneCountryCode
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busTelephoneCityCode
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busTelephoneCityCode", "busTelephoneCityCode", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusTelephoneCityCode();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusTelephoneCityCode( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busTelephoneCityCode
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busTelephoneNumber
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busTelephoneNumber", "busTelephoneNumber", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusTelephoneNumber();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusTelephoneNumber( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busTelephoneNumber
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busTelephoneExtension
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busTelephoneExtension", "busTelephoneExtension", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusTelephoneExtension();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusTelephoneExtension( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busTelephoneExtension
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busFaxCountryCode
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busFaxCountryCode", "busFaxCountryCode", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusFaxCountryCode();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusFaxCountryCode( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busFaxCountryCode
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busFaxCityCode
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busFaxCityCode", "busFaxCityCode", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusFaxCityCode();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusFaxCityCode( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busFaxCityCode
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busFaxNumber
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busFaxNumber", "busFaxNumber", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusFaxNumber();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusFaxNumber( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busFaxNumber
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
        //-- _busFaxExtension
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_busFaxExtension", "busFaxExtension", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                RMDataType target = (RMDataType) object;
                return target.getBusFaxExtension();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    RMDataType target = (RMDataType) object;
                    target.setBusFaxExtension( (java.lang.String) value);
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
        desc.setMultivalued(false);
        addFieldDescriptor(desc);
        
        //-- validation code for: _busFaxExtension
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        { //-- local scope
            StringValidator typeValidator = new StringValidator();
            typeValidator.setWhiteSpace("preserve");
            fieldValidator.setValidator(typeValidator);
        }
        desc.setValidator(fieldValidator);
    } //-- com.hp.globalops.hppcbl.webservice.RMDataTypeDescriptor()


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
        return com.hp.globalops.hppcbl.webservice.RMDataType.class;
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
