/**
 * UGSSystemException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.hp.it.spf.user.group.stub;

public class UGSSystemException  extends org.apache.axis.AxisFault  implements java.io.Serializable {
    private com.hp.it.spf.user.group.stub.DiagnosticContext diagnosticContext;

    private java.lang.String txID;

    public UGSSystemException() {
    }

    public UGSSystemException(
           com.hp.it.spf.user.group.stub.DiagnosticContext diagnosticContext,
           java.lang.String txID) {
        this.diagnosticContext = diagnosticContext;
        this.txID = txID;
    }


    /**
     * Gets the diagnosticContext value for this UGSSystemException.
     * 
     * @return diagnosticContext
     */
    public com.hp.it.spf.user.group.stub.DiagnosticContext getDiagnosticContext() {
        return diagnosticContext;
    }


    /**
     * Sets the diagnosticContext value for this UGSSystemException.
     * 
     * @param diagnosticContext
     */
    public void setDiagnosticContext(com.hp.it.spf.user.group.stub.DiagnosticContext diagnosticContext) {
        this.diagnosticContext = diagnosticContext;
    }


    /**
     * Gets the txID value for this UGSSystemException.
     * 
     * @return txID
     */
    public java.lang.String getTxID() {
        return txID;
    }


    /**
     * Sets the txID value for this UGSSystemException.
     * 
     * @param txID
     */
    public void setTxID(java.lang.String txID) {
        this.txID = txID;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UGSSystemException)) return false;
        UGSSystemException other = (UGSSystemException) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.diagnosticContext==null && other.getDiagnosticContext()==null) || 
             (this.diagnosticContext!=null &&
              this.diagnosticContext.equals(other.getDiagnosticContext()))) &&
            ((this.txID==null && other.getTxID()==null) || 
             (this.txID!=null &&
              this.txID.equals(other.getTxID())));
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
        if (getDiagnosticContext() != null) {
            _hashCode += getDiagnosticContext().hashCode();
        }
        if (getTxID() != null) {
            _hashCode += getTxID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UGSSystemException.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://exceptions.shared.ugs.hp.com", "UGSSystemException"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("diagnosticContext");
        elemField.setXmlName(new javax.xml.namespace.QName("http://exceptions.shared.ugs.hp.com", "diagnosticContext"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://util.ssa.frameworks.hp.com", "DiagnosticContext"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("txID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://exceptions.shared.ugs.hp.com", "txID"));
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


    /**
     * Writes the exception data to the faultDetails
     */
    public void writeDetails(javax.xml.namespace.QName qname, org.apache.axis.encoding.SerializationContext context) throws java.io.IOException {
        context.serialize(qname, null, this);
    }
}
