/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.9.1</a>, using an XML
 * Schema.
 * $Id: GetUserGroupsResultTypeDescriptor.java,v 1.1 2006/11/08 07:34:25 yjie Exp $
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
 * Class GetUserGroupsResultTypeDescriptor.
 * 
 * @version $Revision: 1.1 $ $Date: 2006/11/08 07:34:25 $
 */
public class GetUserGroupsResultTypeDescriptor extends org.exolab.castor.xml.util.XMLClassDescriptorImpl {


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

    public GetUserGroupsResultTypeDescriptor() 
     {
        super();
        nsURI = "http://hppws.globalops.hp.com/PassportService";
        xmlName = "getUserGroupsResultType";
        
        //-- set grouping compositor
        setCompositorAsSequence();
        org.exolab.castor.xml.util.XMLFieldDescriptorImpl  desc           = null;
        org.exolab.castor.mapping.FieldHandler             handler        = null;
        org.exolab.castor.xml.FieldValidator               fieldValidator = null;
        //-- initialize attribute descriptors
        
        //-- initialize element descriptors
        
        //-- _profileId
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(java.lang.String.class, "_profileId", "profileId", org.exolab.castor.xml.NodeType.Element);
        desc.setImmutable(true);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                GetUserGroupsResultType target = (GetUserGroupsResultType) object;
                return target.getProfileId();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    GetUserGroupsResultType target = (GetUserGroupsResultType) object;
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
        //-- _groupRoleList
        desc = new org.exolab.castor.xml.util.XMLFieldDescriptorImpl(com.hp.globalops.hppcbl.webservice.GroupRole.class, "_groupRoleList", "groupRole", org.exolab.castor.xml.NodeType.Element);
        handler = new org.exolab.castor.xml.XMLFieldHandler() {
            public java.lang.Object getValue( java.lang.Object object ) 
                throws IllegalStateException
            {
                GetUserGroupsResultType target = (GetUserGroupsResultType) object;
                return target.getGroupRole();
            }
            public void setValue( java.lang.Object object, java.lang.Object value) 
                throws IllegalStateException, IllegalArgumentException
            {
                try {
                    GetUserGroupsResultType target = (GetUserGroupsResultType) object;
                    target.addGroupRole( (com.hp.globalops.hppcbl.webservice.GroupRole) value);
                }
                catch (java.lang.Exception ex) {
                    throw new IllegalStateException(ex.toString());
                }
            }
            public java.lang.Object newInstance( java.lang.Object parent ) {
                return new com.hp.globalops.hppcbl.webservice.GroupRole();
            }
        };
        desc.setHandler(handler);
        desc.setNameSpaceURI("http://hppws.globalops.hp.com/PassportService");
        desc.setMultivalued(true);
        addFieldDescriptor(desc);
        
        //-- validation code for: _groupRoleList
        fieldValidator = new org.exolab.castor.xml.FieldValidator();
        fieldValidator.setMinOccurs(0);
        { //-- local scope
        }
        desc.setValidator(fieldValidator);
    } //-- com.hp.globalops.hppcbl.webservice.GetUserGroupsResultTypeDescriptor()


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
        return com.hp.globalops.hppcbl.webservice.GetUserGroupsResultType.class;
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
