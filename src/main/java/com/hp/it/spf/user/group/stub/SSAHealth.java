/**
 * SSAHealth.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hp.it.spf.user.group.stub;

public class SSAHealth  implements java.io.Serializable {
    private com.hp.it.spf.user.group.stub.AnyType2AnyTypeMap allStatus;

    private java.lang.Boolean health;

    private com.hp.it.spf.user.group.stub.Instance instance;

    public SSAHealth() {
    }

    public SSAHealth(
           com.hp.it.spf.user.group.stub.AnyType2AnyTypeMap allStatus,
           java.lang.Boolean health,
           com.hp.it.spf.user.group.stub.Instance instance) {
           this.allStatus = allStatus;
           this.health = health;
           this.instance = instance;
    }


    /**
     * Gets the allStatus value for this SSAHealth.
     * 
     * @return allStatus
     */
    public com.hp.it.spf.user.group.stub.AnyType2AnyTypeMap getAllStatus() {
        return allStatus;
    }


    /**
     * Sets the allStatus value for this SSAHealth.
     * 
     * @param allStatus
     */
    public void setAllStatus(com.hp.it.spf.user.group.stub.AnyType2AnyTypeMap allStatus) {
        this.allStatus = allStatus;
    }


    /**
     * Gets the health value for this SSAHealth.
     * 
     * @return health
     */
    public java.lang.Boolean getHealth() {
        return health;
    }


    /**
     * Sets the health value for this SSAHealth.
     * 
     * @param health
     */
    public void setHealth(java.lang.Boolean health) {
        this.health = health;
    }


    /**
     * Gets the instance value for this SSAHealth.
     * 
     * @return instance
     */
    public com.hp.it.spf.user.group.stub.Instance getInstance() {
        return instance;
    }


    /**
     * Sets the instance value for this SSAHealth.
     * 
     * @param instance
     */
    public void setInstance(com.hp.it.spf.user.group.stub.Instance instance) {
        this.instance = instance;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SSAHealth)) return false;
        SSAHealth other = (SSAHealth) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.allStatus==null && other.getAllStatus()==null) || 
             (this.allStatus!=null &&
              this.allStatus.equals(other.getAllStatus()))) &&
            ((this.health==null && other.getHealth()==null) || 
             (this.health!=null &&
              this.health.equals(other.getHealth()))) &&
            ((this.instance==null && other.getInstance()==null) || 
             (this.instance!=null &&
              this.instance.equals(other.getInstance())));
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
        if (getAllStatus() != null) {
            _hashCode += getAllStatus().hashCode();
        }
        if (getHealth() != null) {
            _hashCode += getHealth().hashCode();
        }
        if (getInstance() != null) {
            _hashCode += getInstance().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SSAHealth.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://util.ssa.frameworks.hp.com", "SSAHealth"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("allStatus");
        elemField.setXmlName(new javax.xml.namespace.QName("http://util.ssa.frameworks.hp.com", "allStatus"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "anyType2anyTypeMap"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("health");
        elemField.setXmlName(new javax.xml.namespace.QName("http://util.ssa.frameworks.hp.com", "health"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("instance");
        elemField.setXmlName(new javax.xml.namespace.QName("http://util.ssa.frameworks.hp.com", "instance"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://util.ssa.frameworks.hp.com", "Instance"));
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
