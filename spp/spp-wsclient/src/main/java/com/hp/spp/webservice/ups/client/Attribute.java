/**
 * Attribute.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.hp.spp.webservice.ups.client;

public class Attribute  extends com.hp.spp.webservice.ups.client.AbstractAttribute  implements java.io.Serializable {
    private java.lang.String[] attributeValues;

    private java.lang.String fieldDataType;

    private int fieldSize;

    private com.hp.spp.webservice.ups.client.LOV[] fullLOVList;

    private java.lang.String localizedPrompt;

    private boolean mandatoryFlag;

    private boolean multiselectFlag;

    private java.lang.String operator;

    public Attribute() {
    }

    public Attribute(
           java.lang.String attributeId,
           java.lang.String fieldType,
           java.lang.String[] attributeValues,
           java.lang.String fieldDataType,
           int fieldSize,
           com.hp.spp.webservice.ups.client.LOV[] fullLOVList,
           java.lang.String localizedPrompt,
           boolean mandatoryFlag,
           boolean multiselectFlag,
           java.lang.String operator) {
        super(
            attributeId,
            fieldType);
        this.attributeValues = attributeValues;
        this.fieldDataType = fieldDataType;
        this.fieldSize = fieldSize;
        this.fullLOVList = fullLOVList;
        this.localizedPrompt = localizedPrompt;
        this.mandatoryFlag = mandatoryFlag;
        this.multiselectFlag = multiselectFlag;
        this.operator = operator;
    }


    /**
     * Gets the attributeValues value for this Attribute.
     * 
     * @return attributeValues
     */
    public java.lang.String[] getAttributeValues() {
        return attributeValues;
    }


    /**
     * Sets the attributeValues value for this Attribute.
     * 
     * @param attributeValues
     */
    public void setAttributeValues(java.lang.String[] attributeValues) {
        this.attributeValues = attributeValues;
    }

    public java.lang.String getAttributeValues(int i) {
        return this.attributeValues[i];
    }

    public void setAttributeValues(int i, java.lang.String _value) {
        this.attributeValues[i] = _value;
    }


    /**
     * Gets the fieldDataType value for this Attribute.
     * 
     * @return fieldDataType
     */
    public java.lang.String getFieldDataType() {
        return fieldDataType;
    }


    /**
     * Sets the fieldDataType value for this Attribute.
     * 
     * @param fieldDataType
     */
    public void setFieldDataType(java.lang.String fieldDataType) {
        this.fieldDataType = fieldDataType;
    }


    /**
     * Gets the fieldSize value for this Attribute.
     * 
     * @return fieldSize
     */
    public int getFieldSize() {
        return fieldSize;
    }


    /**
     * Sets the fieldSize value for this Attribute.
     * 
     * @param fieldSize
     */
    public void setFieldSize(int fieldSize) {
        this.fieldSize = fieldSize;
    }


    /**
     * Gets the fullLOVList value for this Attribute.
     * 
     * @return fullLOVList
     */
    public com.hp.spp.webservice.ups.client.LOV[] getFullLOVList() {
        return fullLOVList;
    }


    /**
     * Sets the fullLOVList value for this Attribute.
     * 
     * @param fullLOVList
     */
    public void setFullLOVList(com.hp.spp.webservice.ups.client.LOV[] fullLOVList) {
        this.fullLOVList = fullLOVList;
    }

    public com.hp.spp.webservice.ups.client.LOV getFullLOVList(int i) {
        return this.fullLOVList[i];
    }

    public void setFullLOVList(int i, com.hp.spp.webservice.ups.client.LOV _value) {
        this.fullLOVList[i] = _value;
    }


    /**
     * Gets the localizedPrompt value for this Attribute.
     * 
     * @return localizedPrompt
     */
    public java.lang.String getLocalizedPrompt() {
        return localizedPrompt;
    }


    /**
     * Sets the localizedPrompt value for this Attribute.
     * 
     * @param localizedPrompt
     */
    public void setLocalizedPrompt(java.lang.String localizedPrompt) {
        this.localizedPrompt = localizedPrompt;
    }


    /**
     * Gets the mandatoryFlag value for this Attribute.
     * 
     * @return mandatoryFlag
     */
    public boolean isMandatoryFlag() {
        return mandatoryFlag;
    }


    /**
     * Sets the mandatoryFlag value for this Attribute.
     * 
     * @param mandatoryFlag
     */
    public void setMandatoryFlag(boolean mandatoryFlag) {
        this.mandatoryFlag = mandatoryFlag;
    }


    /**
     * Gets the multiselectFlag value for this Attribute.
     * 
     * @return multiselectFlag
     */
    public boolean isMultiselectFlag() {
        return multiselectFlag;
    }


    /**
     * Sets the multiselectFlag value for this Attribute.
     * 
     * @param multiselectFlag
     */
    public void setMultiselectFlag(boolean multiselectFlag) {
        this.multiselectFlag = multiselectFlag;
    }


    /**
     * Gets the operator value for this Attribute.
     * 
     * @return operator
     */
    public java.lang.String getOperator() {
        return operator;
    }


    /**
     * Sets the operator value for this Attribute.
     * 
     * @param operator
     */
    public void setOperator(java.lang.String operator) {
        this.operator = operator;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof Attribute)) return false;
        Attribute other = (Attribute) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.attributeValues==null && other.getAttributeValues()==null) || 
             (this.attributeValues!=null &&
              java.util.Arrays.equals(this.attributeValues, other.getAttributeValues()))) &&
            ((this.fieldDataType==null && other.getFieldDataType()==null) || 
             (this.fieldDataType!=null &&
              this.fieldDataType.equals(other.getFieldDataType()))) &&
            this.fieldSize == other.getFieldSize() &&
            ((this.fullLOVList==null && other.getFullLOVList()==null) || 
             (this.fullLOVList!=null &&
              java.util.Arrays.equals(this.fullLOVList, other.getFullLOVList()))) &&
            ((this.localizedPrompt==null && other.getLocalizedPrompt()==null) || 
             (this.localizedPrompt!=null &&
              this.localizedPrompt.equals(other.getLocalizedPrompt()))) &&
            this.mandatoryFlag == other.isMandatoryFlag() &&
            this.multiselectFlag == other.isMultiselectFlag() &&
            ((this.operator==null && other.getOperator()==null) || 
             (this.operator!=null &&
              this.operator.equals(other.getOperator())));
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
        if (getAttributeValues() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAttributeValues());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAttributeValues(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getFieldDataType() != null) {
            _hashCode += getFieldDataType().hashCode();
        }
        _hashCode += getFieldSize();
        if (getFullLOVList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getFullLOVList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getFullLOVList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getLocalizedPrompt() != null) {
            _hashCode += getLocalizedPrompt().hashCode();
        }
        _hashCode += (isMandatoryFlag() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += (isMultiselectFlag() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getOperator() != null) {
            _hashCode += getOperator().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(Attribute.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "Attribute"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributeValues");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "attributeValues"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fieldDataType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "fieldDataType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fieldSize");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "fieldSize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fullLOVList");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "fullLOVList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "LOV"));
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("localizedPrompt");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "localizedPrompt"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mandatoryFlag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "mandatoryFlag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("multiselectFlag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "multiselectFlag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("operator");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "operator"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
