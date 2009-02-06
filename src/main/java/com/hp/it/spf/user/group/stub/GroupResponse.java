/**
 * GroupResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hp.it.spf.user.group.stub;

public class GroupResponse  implements java.io.Serializable {
    private com.hp.it.spf.user.group.stub.ArrayOfString groupList;

    private java.lang.Long transactionTime;

    public GroupResponse() {
    }

    public GroupResponse(
           com.hp.it.spf.user.group.stub.ArrayOfString groupList,
           java.lang.Long transactionTime) {
           this.groupList = groupList;
           this.transactionTime = transactionTime;
    }


    /**
     * Gets the groupList value for this GroupResponse.
     * 
     * @return groupList
     */
    public com.hp.it.spf.user.group.stub.ArrayOfString getGroupList() {
        return groupList;
    }


    /**
     * Sets the groupList value for this GroupResponse.
     * 
     * @param groupList
     */
    public void setGroupList(com.hp.it.spf.user.group.stub.ArrayOfString groupList) {
        this.groupList = groupList;
    }


    /**
     * Gets the transactionTime value for this GroupResponse.
     * 
     * @return transactionTime
     */
    public java.lang.Long getTransactionTime() {
        return transactionTime;
    }


    /**
     * Sets the transactionTime value for this GroupResponse.
     * 
     * @param transactionTime
     */
    public void setTransactionTime(java.lang.Long transactionTime) {
        this.transactionTime = transactionTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof GroupResponse)) return false;
        GroupResponse other = (GroupResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.groupList==null && other.getGroupList()==null) || 
             (this.groupList!=null &&
              this.groupList.equals(other.getGroupList()))) &&
            ((this.transactionTime==null && other.getTransactionTime()==null) || 
             (this.transactionTime!=null &&
              this.transactionTime.equals(other.getTransactionTime())));
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
        if (getGroupList() != null) {
            _hashCode += getGroupList().hashCode();
        }
        if (getTransactionTime() != null) {
            _hashCode += getTransactionTime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(GroupResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://shared.ugs.hp.com", "GroupResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("groupList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://shared.ugs.hp.com", "groupList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "ArrayOfString"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://shared.ugs.hp.com", "transactionTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
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
