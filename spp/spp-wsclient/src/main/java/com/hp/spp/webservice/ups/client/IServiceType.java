/**
 * IServiceType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.hp.spp.webservice.ups.client;

public abstract class IServiceType  implements java.io.Serializable {
    private java.lang.String configuredQueryId;

    private java.lang.String operation;

    private java.lang.String revisionVersion;

    private java.lang.String transactionId;

    public IServiceType() {
    }

    public IServiceType(
           java.lang.String configuredQueryId,
           java.lang.String operation,
           java.lang.String revisionVersion,
           java.lang.String transactionId) {
           this.configuredQueryId = configuredQueryId;
           this.operation = operation;
           this.revisionVersion = revisionVersion;
           this.transactionId = transactionId;
    }


    /**
     * Gets the configuredQueryId value for this IServiceType.
     * 
     * @return configuredQueryId
     */
    public java.lang.String getConfiguredQueryId() {
        return configuredQueryId;
    }


    /**
     * Sets the configuredQueryId value for this IServiceType.
     * 
     * @param configuredQueryId
     */
    public void setConfiguredQueryId(java.lang.String configuredQueryId) {
        this.configuredQueryId = configuredQueryId;
    }


    /**
     * Gets the operation value for this IServiceType.
     * 
     * @return operation
     */
    public java.lang.String getOperation() {
        return operation;
    }


    /**
     * Sets the operation value for this IServiceType.
     * 
     * @param operation
     */
    public void setOperation(java.lang.String operation) {
        this.operation = operation;
    }


    /**
     * Gets the revisionVersion value for this IServiceType.
     * 
     * @return revisionVersion
     */
    public java.lang.String getRevisionVersion() {
        return revisionVersion;
    }


    /**
     * Sets the revisionVersion value for this IServiceType.
     * 
     * @param revisionVersion
     */
    public void setRevisionVersion(java.lang.String revisionVersion) {
        this.revisionVersion = revisionVersion;
    }


    /**
     * Gets the transactionId value for this IServiceType.
     * 
     * @return transactionId
     */
    public java.lang.String getTransactionId() {
        return transactionId;
    }


    /**
     * Sets the transactionId value for this IServiceType.
     * 
     * @param transactionId
     */
    public void setTransactionId(java.lang.String transactionId) {
        this.transactionId = transactionId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof IServiceType)) return false;
        IServiceType other = (IServiceType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.configuredQueryId==null && other.getConfiguredQueryId()==null) || 
             (this.configuredQueryId!=null &&
              this.configuredQueryId.equals(other.getConfiguredQueryId()))) &&
            ((this.operation==null && other.getOperation()==null) || 
             (this.operation!=null &&
              this.operation.equals(other.getOperation()))) &&
            ((this.revisionVersion==null && other.getRevisionVersion()==null) || 
             (this.revisionVersion!=null &&
              this.revisionVersion.equals(other.getRevisionVersion()))) &&
            ((this.transactionId==null && other.getTransactionId()==null) || 
             (this.transactionId!=null &&
              this.transactionId.equals(other.getTransactionId())));
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
        if (getConfiguredQueryId() != null) {
            _hashCode += getConfiguredQueryId().hashCode();
        }
        if (getOperation() != null) {
            _hashCode += getOperation().hashCode();
        }
        if (getRevisionVersion() != null) {
            _hashCode += getRevisionVersion().hashCode();
        }
        if (getTransactionId() != null) {
            _hashCode += getTransactionId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(IServiceType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "IServiceType"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("configuredQueryId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "configuredQueryId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("operation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "operation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("revisionVersion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "revisionVersion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("transactionId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "transactionId"));
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
