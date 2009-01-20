/**
 * DiagnosticContext.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.hp.it.spf.user.group.stub;

public class DiagnosticContext  implements java.io.Serializable {
    private java.lang.Boolean contextON;

    private java.lang.String location;

    private java.lang.String messageToLog;

    public DiagnosticContext() {
    }

    public DiagnosticContext(
           java.lang.Boolean contextON,
           java.lang.String location,
           java.lang.String messageToLog) {
           this.contextON = contextON;
           this.location = location;
           this.messageToLog = messageToLog;
    }


    /**
     * Gets the contextON value for this DiagnosticContext.
     * 
     * @return contextON
     */
    public java.lang.Boolean getContextON() {
        return contextON;
    }


    /**
     * Sets the contextON value for this DiagnosticContext.
     * 
     * @param contextON
     */
    public void setContextON(java.lang.Boolean contextON) {
        this.contextON = contextON;
    }


    /**
     * Gets the location value for this DiagnosticContext.
     * 
     * @return location
     */
    public java.lang.String getLocation() {
        return location;
    }


    /**
     * Sets the location value for this DiagnosticContext.
     * 
     * @param location
     */
    public void setLocation(java.lang.String location) {
        this.location = location;
    }


    /**
     * Gets the messageToLog value for this DiagnosticContext.
     * 
     * @return messageToLog
     */
    public java.lang.String getMessageToLog() {
        return messageToLog;
    }


    /**
     * Sets the messageToLog value for this DiagnosticContext.
     * 
     * @param messageToLog
     */
    public void setMessageToLog(java.lang.String messageToLog) {
        this.messageToLog = messageToLog;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DiagnosticContext)) return false;
        DiagnosticContext other = (DiagnosticContext) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.contextON==null && other.getContextON()==null) || 
             (this.contextON!=null &&
              this.contextON.equals(other.getContextON()))) &&
            ((this.location==null && other.getLocation()==null) || 
             (this.location!=null &&
              this.location.equals(other.getLocation()))) &&
            ((this.messageToLog==null && other.getMessageToLog()==null) || 
             (this.messageToLog!=null &&
              this.messageToLog.equals(other.getMessageToLog())));
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
        if (getContextON() != null) {
            _hashCode += getContextON().hashCode();
        }
        if (getLocation() != null) {
            _hashCode += getLocation().hashCode();
        }
        if (getMessageToLog() != null) {
            _hashCode += getMessageToLog().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(DiagnosticContext.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://util.ssa.frameworks.hp.com", "DiagnosticContext"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("contextON");
        elemField.setXmlName(new javax.xml.namespace.QName("http://util.ssa.frameworks.hp.com", "contextON"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("location");
        elemField.setXmlName(new javax.xml.namespace.QName("http://util.ssa.frameworks.hp.com", "location"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("messageToLog");
        elemField.setXmlName(new javax.xml.namespace.QName("http://util.ssa.frameworks.hp.com", "messageToLog"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
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
