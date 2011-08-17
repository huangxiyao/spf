/**
 * EServiceResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hp.spp.webservice.eservice.client;

public class EServiceResponse  implements java.io.Serializable {
    private java.lang.String method;

    private boolean openInNewWindow;

    private java.util.HashMap parameters;

    private boolean securityMode;

    private long simulationMode;

    private java.lang.String url;

    private java.lang.String windowParameters;

    private java.lang.String characterEncoding;

    public EServiceResponse() {
    }

    public EServiceResponse(
           java.lang.String method,
           boolean openInNewWindow,
           java.util.HashMap parameters,
           boolean securityMode,
           long simulationMode,
           java.lang.String url,
           java.lang.String windowParameters,
           java.lang.String characterEncoding) {
           this.method = method;
           this.openInNewWindow = openInNewWindow;
           this.parameters = parameters;
           this.securityMode = securityMode;
           this.simulationMode = simulationMode;
           this.url = url;
           this.windowParameters = windowParameters;
           this.characterEncoding = characterEncoding;
    }


    /**
     * Gets the method value for this EServiceResponse.
     * 
     * @return method
     */
    public java.lang.String getMethod() {
        return method;
    }


    /**
     * Sets the method value for this EServiceResponse.
     * 
     * @param method
     */
    public void setMethod(java.lang.String method) {
        this.method = method;
    }


    /**
     * Gets the openInNewWindow value for this EServiceResponse.
     * 
     * @return openInNewWindow
     */
    public boolean isOpenInNewWindow() {
        return openInNewWindow;
    }


    /**
     * Sets the openInNewWindow value for this EServiceResponse.
     * 
     * @param openInNewWindow
     */
    public void setOpenInNewWindow(boolean openInNewWindow) {
        this.openInNewWindow = openInNewWindow;
    }


    /**
     * Gets the parameters value for this EServiceResponse.
     * 
     * @return parameters
     */
    public java.util.HashMap getParameters() {
        return parameters;
    }


    /**
     * Sets the parameters value for this EServiceResponse.
     * 
     * @param parameters
     */
    public void setParameters(java.util.HashMap parameters) {
        this.parameters = parameters;
    }


    /**
     * Gets the securityMode value for this EServiceResponse.
     * 
     * @return securityMode
     */
    public boolean isSecurityMode() {
        return securityMode;
    }


    /**
     * Sets the securityMode value for this EServiceResponse.
     * 
     * @param securityMode
     */
    public void setSecurityMode(boolean securityMode) {
        this.securityMode = securityMode;
    }


    /**
     * Gets the simulationMode value for this EServiceResponse.
     * 
     * @return simulationMode
     */
    public long getSimulationMode() {
        return simulationMode;
    }


    /**
     * Sets the simulationMode value for this EServiceResponse.
     * 
     * @param simulationMode
     */
    public void setSimulationMode(long simulationMode) {
        this.simulationMode = simulationMode;
    }


    /**
     * Gets the url value for this EServiceResponse.
     * 
     * @return url
     */
    public java.lang.String getUrl() {
        return url;
    }


    /**
     * Sets the url value for this EServiceResponse.
     * 
     * @param url
     */
    public void setUrl(java.lang.String url) {
        this.url = url;
    }


    /**
     * Gets the windowParameters value for this EServiceResponse.
     * 
     * @return windowParameters
     */
    public java.lang.String getWindowParameters() {
        return windowParameters;
    }


    /**
     * Sets the windowParameters value for this EServiceResponse.
     * 
     * @param windowParameters
     */
    public void setWindowParameters(java.lang.String windowParameters) {
        this.windowParameters = windowParameters;
    }


    /**
     * Gets the characterEncoding value for this EServiceResponse.
     * 
     * @return characterEncoding
     */
    public java.lang.String getCharacterEncoding() {
        return characterEncoding;
    }


    /**
     * Sets the characterEncoding value for this EServiceResponse.
     * 
     * @param characterEncoding
     */
    public void setCharacterEncoding(java.lang.String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EServiceResponse)) return false;
        EServiceResponse other = (EServiceResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.method==null && other.getMethod()==null) || 
             (this.method!=null &&
              this.method.equals(other.getMethod()))) &&
            this.openInNewWindow == other.isOpenInNewWindow() &&
            ((this.parameters==null && other.getParameters()==null) || 
             (this.parameters!=null &&
              this.parameters.equals(other.getParameters()))) &&
            this.securityMode == other.isSecurityMode() &&
            this.simulationMode == other.getSimulationMode() &&
            ((this.url==null && other.getUrl()==null) || 
             (this.url!=null &&
              this.url.equals(other.getUrl()))) &&
            ((this.windowParameters==null && other.getWindowParameters()==null) || 
             (this.windowParameters!=null &&
              this.windowParameters.equals(other.getWindowParameters()))) &&
            ((this.characterEncoding==null && other.getCharacterEncoding()==null) || 
             (this.characterEncoding!=null &&
              this.characterEncoding.equals(other.getCharacterEncoding())));
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
        if (getMethod() != null) {
            _hashCode += getMethod().hashCode();
        }
        _hashCode += (isOpenInNewWindow() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        if (getParameters() != null) {
            _hashCode += getParameters().hashCode();
        }
        _hashCode += (isSecurityMode() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        _hashCode += new Long(getSimulationMode()).hashCode();
        if (getUrl() != null) {
            _hashCode += getUrl().hashCode();
        }
        if (getWindowParameters() != null) {
            _hashCode += getWindowParameters().hashCode();
        }
        if (getCharacterEncoding() != null) {
            _hashCode += getCharacterEncoding().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EServiceResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:EServiceManager", "EServiceResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("method");
        elemField.setXmlName(new javax.xml.namespace.QName("", "method"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("openInNewWindow");
        elemField.setXmlName(new javax.xml.namespace.QName("", "openInNewWindow"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("parameters");
        elemField.setXmlName(new javax.xml.namespace.QName("", "parameters"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("securityMode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "securityMode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("simulationMode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "simulationMode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("url");
        elemField.setXmlName(new javax.xml.namespace.QName("", "url"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("windowParameters");
        elemField.setXmlName(new javax.xml.namespace.QName("", "windowParameters"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("characterEncoding");
        elemField.setXmlName(new javax.xml.namespace.QName("", "characterEncoding"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
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
