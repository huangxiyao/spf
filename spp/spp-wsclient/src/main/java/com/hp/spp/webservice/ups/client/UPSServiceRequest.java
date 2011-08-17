/**
 * UPSServiceRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.hp.spp.webservice.ups.client;

public class UPSServiceRequest  implements java.io.Serializable {
    private com.hp.spp.webservice.ups.client.AttributeGroup[] attributeGroups;

    private com.hp.spp.webservice.ups.client.RequestHeader requestHeader;

    private com.hp.spp.webservice.ups.client.ServiceIdentifier serviceIdentifier;

    public UPSServiceRequest() {
    }

    public UPSServiceRequest(
           com.hp.spp.webservice.ups.client.AttributeGroup[] attributeGroups,
           com.hp.spp.webservice.ups.client.RequestHeader requestHeader,
           com.hp.spp.webservice.ups.client.ServiceIdentifier serviceIdentifier) {
           this.attributeGroups = attributeGroups;
           this.requestHeader = requestHeader;
           this.serviceIdentifier = serviceIdentifier;
    }


    /**
     * Gets the attributeGroups value for this UPSServiceRequest.
     * 
     * @return attributeGroups
     */
    public com.hp.spp.webservice.ups.client.AttributeGroup[] getAttributeGroups() {
        return attributeGroups;
    }


    /**
     * Sets the attributeGroups value for this UPSServiceRequest.
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
     * Gets the requestHeader value for this UPSServiceRequest.
     * 
     * @return requestHeader
     */
    public com.hp.spp.webservice.ups.client.RequestHeader getRequestHeader() {
        return requestHeader;
    }


    /**
     * Sets the requestHeader value for this UPSServiceRequest.
     * 
     * @param requestHeader
     */
    public void setRequestHeader(com.hp.spp.webservice.ups.client.RequestHeader requestHeader) {
        this.requestHeader = requestHeader;
    }


    /**
     * Gets the serviceIdentifier value for this UPSServiceRequest.
     * 
     * @return serviceIdentifier
     */
    public com.hp.spp.webservice.ups.client.ServiceIdentifier getServiceIdentifier() {
        return serviceIdentifier;
    }


    /**
     * Sets the serviceIdentifier value for this UPSServiceRequest.
     * 
     * @param serviceIdentifier
     */
    public void setServiceIdentifier(com.hp.spp.webservice.ups.client.ServiceIdentifier serviceIdentifier) {
        this.serviceIdentifier = serviceIdentifier;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UPSServiceRequest)) return false;
        UPSServiceRequest other = (UPSServiceRequest) obj;
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
            ((this.requestHeader==null && other.getRequestHeader()==null) || 
             (this.requestHeader!=null &&
              this.requestHeader.equals(other.getRequestHeader()))) &&
            ((this.serviceIdentifier==null && other.getServiceIdentifier()==null) || 
             (this.serviceIdentifier!=null &&
              this.serviceIdentifier.equals(other.getServiceIdentifier())));
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
        if (getRequestHeader() != null) {
            _hashCode += getRequestHeader().hashCode();
        }
        if (getServiceIdentifier() != null) {
            _hashCode += getServiceIdentifier().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UPSServiceRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "UPSServiceRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("attributeGroups");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "attributeGroups"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "AttributeGroup"));
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requestHeader");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "requestHeader"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "RequestHeader"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("serviceIdentifier");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "serviceIdentifier"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "ServiceIdentifier"));
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
