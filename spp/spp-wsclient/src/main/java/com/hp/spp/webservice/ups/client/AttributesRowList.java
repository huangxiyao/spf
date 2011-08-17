/**
 * AttributesRowList.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.hp.spp.webservice.ups.client;

public class AttributesRowList  extends com.hp.spp.webservice.ups.client.AbstractAttribute  implements java.io.Serializable {
    private com.hp.spp.webservice.ups.client.AttributesRow[] rows;

    public AttributesRowList() {
    }

    public AttributesRowList(
           java.lang.String attributeId,
           java.lang.String fieldType,
           com.hp.spp.webservice.ups.client.AttributesRow[] rows) {
        super(
            attributeId,
            fieldType);
        this.rows = rows;
    }


    /**
     * Gets the rows value for this AttributesRowList.
     * 
     * @return rows
     */
    public com.hp.spp.webservice.ups.client.AttributesRow[] getRows() {
        return rows;
    }


    /**
     * Sets the rows value for this AttributesRowList.
     * 
     * @param rows
     */
    public void setRows(com.hp.spp.webservice.ups.client.AttributesRow[] rows) {
        this.rows = rows;
    }

    public com.hp.spp.webservice.ups.client.AttributesRow getRows(int i) {
        return this.rows[i];
    }

    public void setRows(int i, com.hp.spp.webservice.ups.client.AttributesRow _value) {
        this.rows[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AttributesRowList)) return false;
        AttributesRowList other = (AttributesRowList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.rows==null && other.getRows()==null) || 
             (this.rows!=null &&
              java.util.Arrays.equals(this.rows, other.getRows())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getRows() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getRows());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getRows(), i);
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
        new org.apache.axis.description.TypeDesc(AttributesRowList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "AttributesRowList"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("rows");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "rows"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "AttributesRow"));
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
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
