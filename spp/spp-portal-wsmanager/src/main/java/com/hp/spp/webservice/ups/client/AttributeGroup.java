/**
 * AttributeGroup.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hp.spp.webservice.ups.client;

public class AttributeGroup  implements java.io.Serializable {
    private java.lang.String attributeGroupName;

    private com.hp.spp.webservice.ups.client.Attribute[] attributes;

    private com.hp.spp.webservice.ups.client.AttributesRowList[] attributesList;

    public AttributeGroup() {
    }

    public AttributeGroup(
           java.lang.String attributeGroupName,
           com.hp.spp.webservice.ups.client.Attribute[] attributes,
           com.hp.spp.webservice.ups.client.AttributesRowList[] attributesList) {
           this.attributeGroupName = attributeGroupName;
           this.attributes = attributes;
           this.attributesList = attributesList;
    }


    /**
     * Gets the attributeGroupName value for this AttributeGroup.
     * 
     * @return attributeGroupName
     */
    public java.lang.String getAttributeGroupName() {
        return attributeGroupName;
    }


    /**
     * Sets the attributeGroupName value for this AttributeGroup.
     * 
     * @param attributeGroupName
     */
    public void setAttributeGroupName(java.lang.String attributeGroupName) {
        this.attributeGroupName = attributeGroupName;
    }


    /**
     * Gets the attributes value for this AttributeGroup.
     * 
     * @return attributes
     */
    public com.hp.spp.webservice.ups.client.Attribute[] getAttributes() {
        return attributes;
    }


    /**
     * Sets the attributes value for this AttributeGroup.
     * 
     * @param attributes
     */
    public void setAttributes(com.hp.spp.webservice.ups.client.Attribute[] attributes) {
        this.attributes = attributes;
    }

    public com.hp.spp.webservice.ups.client.Attribute getAttributes(int i) {
        return this.attributes[i];
    }

    public void setAttributes(int i, com.hp.spp.webservice.ups.client.Attribute _value) {
        this.attributes[i] = _value;
    }


    /**
     * Gets the attributesList value for this AttributeGroup.
     * 
     * @return attributesList
     */
    public com.hp.spp.webservice.ups.client.AttributesRowList[] getAttributesList() {
        return attributesList;
    }


    /**
     * Sets the attributesList value for this AttributeGroup.
     * 
     * @param attributesList
     */
    public void setAttributesList(com.hp.spp.webservice.ups.client.AttributesRowList[] attributesList) {
        this.attributesList = attributesList;
    }

    public com.hp.spp.webservice.ups.client.AttributesRowList getAttributesList(int i) {
        return this.attributesList[i];
    }

    public void setAttributesList(int i, com.hp.spp.webservice.ups.client.AttributesRowList _value) {
        this.attributesList[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AttributeGroup)) return false;
        AttributeGroup other = (AttributeGroup) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.attributeGroupName==null && other.getAttributeGroupName()==null) || 
             (this.attributeGroupName!=null &&
              this.attributeGroupName.equals(other.getAttributeGroupName()))) &&
            ((this.attributes==null && other.getAttributes()==null) || 
             (this.attributes!=null &&
              java.util.Arrays.equals(this.attributes, other.getAttributes()))) &&
            ((this.attributesList==null && other.getAttributesList()==null) || 
             (this.attributesList!=null &&
              java.util.Arrays.equals(this.attributesList, other.getAttributesList())));
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
        if (getAttributeGroupName() != null) {
            _hashCode += getAttributeGroupName().hashCode();
        }
        if (getAttributes() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAttributes());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAttributes(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getAttributesList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAttributesList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAttributesList(), i);
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
        new org.apache.axis.description.TypeDesc(AttributeGroup.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "AttributeGroup"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributeGroupName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "attributeGroupName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributes");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "attributes"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "Attribute"));
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributesList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "attributesList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "AttributesRowList"));
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
