/**
 * ServiceStatus.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.hp.spp.webservice.ups.client;

public class ServiceStatus  implements java.io.Serializable {
    private java.lang.String requestStatus;

    private java.lang.String revisionVersion;

    public ServiceStatus() {
    }

    public ServiceStatus(
           java.lang.String requestStatus,
           java.lang.String revisionVersion) {
           this.requestStatus = requestStatus;
           this.revisionVersion = revisionVersion;
    }


    /**
     * Gets the requestStatus value for this ServiceStatus.
     * 
     * @return requestStatus
     */
    public java.lang.String getRequestStatus() {
        return requestStatus;
    }


    /**
     * Sets the requestStatus value for this ServiceStatus.
     * 
     * @param requestStatus
     */
    public void setRequestStatus(java.lang.String requestStatus) {
        this.requestStatus = requestStatus;
    }


    /**
     * Gets the revisionVersion value for this ServiceStatus.
     * 
     * @return revisionVersion
     */
    public java.lang.String getRevisionVersion() {
        return revisionVersion;
    }


    /**
     * Sets the revisionVersion value for this ServiceStatus.
     * 
     * @param revisionVersion
     */
    public void setRevisionVersion(java.lang.String revisionVersion) {
        this.revisionVersion = revisionVersion;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ServiceStatus)) return false;
        ServiceStatus other = (ServiceStatus) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.requestStatus==null && other.getRequestStatus()==null) || 
             (this.requestStatus!=null &&
              this.requestStatus.equals(other.getRequestStatus()))) &&
            ((this.revisionVersion==null && other.getRevisionVersion()==null) || 
             (this.revisionVersion!=null &&
              this.revisionVersion.equals(other.getRevisionVersion())));
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
        if (getRequestStatus() != null) {
            _hashCode += getRequestStatus().hashCode();
        }
        if (getRevisionVersion() != null) {
            _hashCode += getRevisionVersion().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ServiceStatus.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "ServiceStatus"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("requestStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "requestStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("revisionVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "revisionVersion"));
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
