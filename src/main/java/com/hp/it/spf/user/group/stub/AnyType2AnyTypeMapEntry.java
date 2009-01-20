/**
 * AnyType2AnyTypeMapEntry.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.hp.it.spf.user.group.stub;

public class AnyType2AnyTypeMapEntry  implements java.io.Serializable {
    private java.lang.Object key;

    private java.lang.Object value;

    public AnyType2AnyTypeMapEntry() {
    }

    public AnyType2AnyTypeMapEntry(
           java.lang.Object key,
           java.lang.Object value) {
           this.key = key;
           this.value = value;
    }


    /**
     * Gets the key value for this AnyType2AnyTypeMapEntry.
     * 
     * @return key
     */
    public java.lang.Object getKey() {
        return key;
    }


    /**
     * Sets the key value for this AnyType2AnyTypeMapEntry.
     * 
     * @param key
     */
    public void setKey(java.lang.Object key) {
        this.key = key;
    }


    /**
     * Gets the value value for this AnyType2AnyTypeMapEntry.
     * 
     * @return value
     */
    public java.lang.Object getValue() {
        return value;
    }


    /**
     * Sets the value value for this AnyType2AnyTypeMapEntry.
     * 
     * @param value
     */
    public void setValue(java.lang.Object value) {
        this.value = value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AnyType2AnyTypeMapEntry)) return false;
        AnyType2AnyTypeMapEntry other = (AnyType2AnyTypeMapEntry) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.key==null && other.getKey()==null) || 
             (this.key!=null &&
              this.key.equals(other.getKey()))) &&
            ((this.value==null && other.getValue()==null) || 
             (this.value!=null &&
              this.value.equals(other.getValue())));
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
        if (getKey() != null) {
            _hashCode += getKey().hashCode();
        }
        if (getValue() != null) {
            _hashCode += getValue().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(AnyType2AnyTypeMapEntry.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">anyType2anyTypeMap>entry"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("key");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "key"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("value");
        elemField.setXmlName(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "value"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "anyType"));
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
