/**
 * UPSServiceResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.hp.spp.webservice.ups.client;

public class UPSServiceResponse  implements java.io.Serializable {
    private com.hp.spp.webservice.ups.client.AttributeGroup[] attributeGroups;

    private com.hp.spp.webservice.ups.client.ResponseHeader responseHeader;

    private com.hp.spp.webservice.ups.client.ServiceStatus serviceStatus;

    public UPSServiceResponse() {
    }

    public UPSServiceResponse(
           com.hp.spp.webservice.ups.client.AttributeGroup[] attributeGroups,
           com.hp.spp.webservice.ups.client.ResponseHeader responseHeader,
           com.hp.spp.webservice.ups.client.ServiceStatus serviceStatus) {
           this.attributeGroups = attributeGroups;
           this.responseHeader = responseHeader;
           this.serviceStatus = serviceStatus;
    }


    /**
     * Gets the attributeGroups value for this UPSServiceResponse.
     * 
     * @return attributeGroups
     */
    public com.hp.spp.webservice.ups.client.AttributeGroup[] getAttributeGroups() {
        return attributeGroups;
    }


    /**
     * Sets the attributeGroups value for this UPSServiceResponse.
     * 
     * @param attributeGroups
     */
    public void setAttributeGroups(com.hp.spp.webservice.ups.client.AttributeGroup[] attributeGroups) {
        this.attributeGroups = attributeGroups;
    }

    public com.hp.spp.webservice.ups.client.AttributeGroup getAttributeGroups(int i) {
        return this.attributeGroups[i];
    }

    public void setAttributeGroups(int i, com.hp.spp.webservice.ups.client.AttributeGroup _value) {
        this.attributeGroups[i] = _value;
    }


    /**
     * Gets the responseHeader value for this UPSServiceResponse.
     * 
     * @return responseHeader
     */
    public com.hp.spp.webservice.ups.client.ResponseHeader getResponseHeader() {
        return responseHeader;
    }


    /**
     * Sets the responseHeader value for this UPSServiceResponse.
     * 
     * @param responseHeader
     */
    public void setResponseHeader(com.hp.spp.webservice.ups.client.ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }


    /**
     * Gets the serviceStatus value for this UPSServiceResponse.
     * 
     * @return serviceStatus
     */
    public com.hp.spp.webservice.ups.client.ServiceStatus getServiceStatus() {
        return serviceStatus;
    }


    /**
     * Sets the serviceStatus value for this UPSServiceResponse.
     * 
     * @param serviceStatus
     */
    public void setServiceStatus(com.hp.spp.webservice.ups.client.ServiceStatus serviceStatus) {
        this.serviceStatus = serviceStatus;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UPSServiceResponse)) return false;
        UPSServiceResponse other = (UPSServiceResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.attributeGroups==null && other.getAttributeGroups()==null) || 
             (this.attributeGroups!=null &&
              java.util.Arrays.equals(this.attributeGroups, other.getAttributeGroups()))) &&
            ((this.responseHeader==null && other.getResponseHeader()==null) || 
             (this.responseHeader!=null &&
              this.responseHeader.equals(other.getResponseHeader()))) &&
            ((this.serviceStatus==null && other.getServiceStatus()==null) || 
             (this.serviceStatus!=null &&
              this.serviceStatus.equals(other.getServiceStatus())));
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
        if (getAttributeGroups() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getAttributeGroups());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAttributeGroups(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getResponseHeader() != null) {
            _hashCode += getResponseHeader().hashCode();
        }
        if (getServiceStatus() != null) {
            _hashCode += getServiceStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UPSServiceResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "UPSServiceResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributeGroups");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "attributeGroups"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "AttributeGroup"));
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("responseHeader");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "responseHeader"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "ResponseHeader"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "serviceStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "ServiceStatus"));
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
