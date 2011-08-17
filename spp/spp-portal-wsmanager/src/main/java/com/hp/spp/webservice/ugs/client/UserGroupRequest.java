/**
 * UserGroupRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hp.spp.webservice.ugs.client;

public class UserGroupRequest  implements java.io.Serializable {
    private java.lang.String siteName;

    private java.util.HashMap userContext;

    public UserGroupRequest() {
    }

    public UserGroupRequest(
           java.lang.String siteName,
           java.util.HashMap userContext) {
           this.siteName = siteName;
           this.userContext = userContext;
    }


    /**
     * Gets the siteName value for this UserGroupRequest.
     * 
     * @return siteName
     */
    public java.lang.String getSiteName() {
        return siteName;
    }


    /**
     * Sets the siteName value for this UserGroupRequest.
     * 
     * @param siteName
     */
    public void setSiteName(java.lang.String siteName) {
        this.siteName = siteName;
    }


    /**
     * Gets the userContext value for this UserGroupRequest.
     * 
     * @return userContext
     */
    public java.util.HashMap getUserContext() {
        return userContext;
    }


    /**
     * Sets the userContext value for this UserGroupRequest.
     * 
     * @param userContext
     */
    public void setUserContext(java.util.HashMap userContext) {
        this.userContext = userContext;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UserGroupRequest)) return false;
        UserGroupRequest other = (UserGroupRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.siteName==null && other.getSiteName()==null) || 
             (this.siteName!=null &&
              this.siteName.equals(other.getSiteName()))) &&
            ((this.userContext==null && other.getUserContext()==null) || 
             (this.userContext!=null &&
              this.userContext.equals(other.getUserContext())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getSiteName() != null) {
            _hashCode += getSiteName().hashCode();
        }
        if (getUserContext() != null) {
            _hashCode += getUserContext().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UserGroupRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:UserGroupManager", "UserGroupRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("siteName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "siteName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userContext");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userContext"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
