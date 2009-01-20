/**
 * GroupRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.hp.it.spf.user.group.stub;

public class GroupRequest  implements java.io.Serializable {
    private java.lang.String siteName;

    private com.hp.it.spf.user.group.stub.UserContext[] userContext;

    public GroupRequest() {
    }

    public GroupRequest(
           java.lang.String siteName,
           com.hp.it.spf.user.group.stub.UserContext[] userContext) {
           this.siteName = siteName;
           this.userContext = userContext;
    }


    /**
     * Gets the siteName value for this GroupRequest.
     * 
     * @return siteName
     */
    public java.lang.String getSiteName() {
        return siteName;
    }


    /**
     * Sets the siteName value for this GroupRequest.
     * 
     * @param siteName
     */
    public void setSiteName(java.lang.String siteName) {
        this.siteName = siteName;
    }


    /**
     * Gets the userContext value for this GroupRequest.
     * 
     * @return userContext
     */
    public com.hp.it.spf.user.group.stub.UserContext[] getUserContext() {
        return userContext;
    }


    /**
     * Sets the userContext value for this GroupRequest.
     * 
     * @param userContext
     */
    public void setUserContext(com.hp.it.spf.user.group.stub.UserContext[] userContext) {
        this.userContext = userContext;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GroupRequest)) return false;
        GroupRequest other = (GroupRequest) obj;
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
              java.util.Arrays.equals(this.userContext, other.getUserContext())));
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
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getUserContext());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getUserContext(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GroupRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://shared.ugs.hp.com", "GroupRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("siteName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://shared.ugs.hp.com", "siteName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userContext");
        elemField.setXmlName(new javax.xml.namespace.QName("http://shared.ugs.hp.com", "userContext"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://shared.ugs.hp.com", "UserContext"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://shared.ugs.hp.com", "UserContext"));
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
