/**
 * GetUsersForGroup.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hp.it.spf.user.group.stub;

public class GetUsersForGroup  implements java.io.Serializable {
    private com.hp.it.spf.user.group.stub.GroupRequest groupRequest;

    public GetUsersForGroup() {
    }

    public GetUsersForGroup(
           com.hp.it.spf.user.group.stub.GroupRequest groupRequest) {
           this.groupRequest = groupRequest;
    }


    /**
     * Gets the groupRequest value for this GetUsersForGroup.
     * 
     * @return groupRequest
     */
    public com.hp.it.spf.user.group.stub.GroupRequest getGroupRequest() {
        return groupRequest;
    }


    /**
     * Sets the groupRequest value for this GetUsersForGroup.
     * 
     * @param groupRequest
     */
    public void setGroupRequest(com.hp.it.spf.user.group.stub.GroupRequest groupRequest) {
        this.groupRequest = groupRequest;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GetUsersForGroup)) return false;
        GetUsersForGroup other = (GetUsersForGroup) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.groupRequest==null && other.getGroupRequest()==null) || 
             (this.groupRequest!=null &&
              this.groupRequest.equals(other.getGroupRequest())));
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
        if (getGroupRequest() != null) {
            _hashCode += getGroupRequest().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GetUsersForGroup.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">getUsersForGroup"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("groupRequest");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "groupRequest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://shared.ugs.hp.com", "GroupRequest"));
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
