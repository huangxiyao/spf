/**
 * EserviceRequest.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.hp.spp.webservice.eservice.client;

public class EserviceRequest  implements java.io.Serializable {
    private java.lang.String EServiceName;

    private java.util.HashMap httpRequestParameters;

    private java.lang.String siteName;

    private java.lang.String urlProdFromRequest;

    private java.lang.String urlTestFromRequest;

    private java.util.HashMap userContext;

    public EserviceRequest() {
    }

    public EserviceRequest(
           java.lang.String EServiceName,
           java.util.HashMap httpRequestParameters,
           java.lang.String siteName,
           java.lang.String urlProdFromRequest,
           java.lang.String urlTestFromRequest,
           java.util.HashMap userContext) {
           this.EServiceName = EServiceName;
           this.httpRequestParameters = httpRequestParameters;
           this.siteName = siteName;
           this.urlProdFromRequest = urlProdFromRequest;
           this.urlTestFromRequest = urlTestFromRequest;
           this.userContext = userContext;
    }


    /**
     * Gets the EServiceName value for this EserviceRequest.
     * 
     * @return EServiceName
     */
    public java.lang.String getEServiceName() {
        return EServiceName;
    }


    /**
     * Sets the EServiceName value for this EserviceRequest.
     * 
     * @param EServiceName
     */
    public void setEServiceName(java.lang.String EServiceName) {
        this.EServiceName = EServiceName;
    }


    /**
     * Gets the httpRequestParameters value for this EserviceRequest.
     * 
     * @return httpRequestParameters
     */
    public java.util.HashMap getHttpRequestParameters() {
        return httpRequestParameters;
    }


    /**
     * Sets the httpRequestParameters value for this EserviceRequest.
     * 
     * @param httpRequestParameters
     */
    public void setHttpRequestParameters(java.util.HashMap httpRequestParameters) {
        this.httpRequestParameters = httpRequestParameters;
    }


    /**
     * Gets the siteName value for this EserviceRequest.
     * 
     * @return siteName
     */
    public java.lang.String getSiteName() {
        return siteName;
    }


    /**
     * Sets the siteName value for this EserviceRequest.
     * 
     * @param siteName
     */
    public void setSiteName(java.lang.String siteName) {
        this.siteName = siteName;
    }


    /**
     * Gets the urlProdFromRequest value for this EserviceRequest.
     * 
     * @return urlProdFromRequest
     */
    public java.lang.String getUrlProdFromRequest() {
        return urlProdFromRequest;
    }


    /**
     * Sets the urlProdFromRequest value for this EserviceRequest.
     * 
     * @param urlProdFromRequest
     */
    public void setUrlProdFromRequest(java.lang.String urlProdFromRequest) {
        this.urlProdFromRequest = urlProdFromRequest;
    }


    /**
     * Gets the urlTestFromRequest value for this EserviceRequest.
     * 
     * @return urlTestFromRequest
     */
    public java.lang.String getUrlTestFromRequest() {
        return urlTestFromRequest;
    }


    /**
     * Sets the urlTestFromRequest value for this EserviceRequest.
     * 
     * @param urlTestFromRequest
     */
    public void setUrlTestFromRequest(java.lang.String urlTestFromRequest) {
        this.urlTestFromRequest = urlTestFromRequest;
    }


    /**
     * Gets the userContext value for this EserviceRequest.
     * 
     * @return userContext
     */
    public java.util.HashMap getUserContext() {
        return userContext;
    }


    /**
     * Sets the userContext value for this EserviceRequest.
     * 
     * @param userContext
     */
    public void setUserContext(java.util.HashMap userContext) {
        this.userContext = userContext;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof EserviceRequest)) return false;
        EserviceRequest other = (EserviceRequest) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.EServiceName==null && other.getEServiceName()==null) || 
             (this.EServiceName!=null &&
              this.EServiceName.equals(other.getEServiceName()))) &&
            ((this.httpRequestParameters==null && other.getHttpRequestParameters()==null) || 
             (this.httpRequestParameters!=null &&
              this.httpRequestParameters.equals(other.getHttpRequestParameters()))) &&
            ((this.siteName==null && other.getSiteName()==null) || 
             (this.siteName!=null &&
              this.siteName.equals(other.getSiteName()))) &&
            ((this.urlProdFromRequest==null && other.getUrlProdFromRequest()==null) || 
             (this.urlProdFromRequest!=null &&
              this.urlProdFromRequest.equals(other.getUrlProdFromRequest()))) &&
            ((this.urlTestFromRequest==null && other.getUrlTestFromRequest()==null) || 
             (this.urlTestFromRequest!=null &&
              this.urlTestFromRequest.equals(other.getUrlTestFromRequest()))) &&
            ((this.userContext==null && other.getUserContext()==null) || 
             (this.userContext!=null &&
              this.userContext.equals(other.getUserContext())));
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
        if (getEServiceName() != null) {
            _hashCode += getEServiceName().hashCode();
        }
        if (getHttpRequestParameters() != null) {
            _hashCode += getHttpRequestParameters().hashCode();
        }
        if (getSiteName() != null) {
            _hashCode += getSiteName().hashCode();
        }
        if (getUrlProdFromRequest() != null) {
            _hashCode += getUrlProdFromRequest().hashCode();
        }
        if (getUrlTestFromRequest() != null) {
            _hashCode += getUrlTestFromRequest().hashCode();
        }
        if (getUserContext() != null) {
            _hashCode += getUserContext().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(EserviceRequest.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:EServiceManager", "EserviceRequest"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("EServiceName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "EServiceName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("httpRequestParameters");
        elemField.setXmlName(new javax.xml.namespace.QName("", "httpRequestParameters"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("siteName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "siteName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("urlProdFromRequest");
        elemField.setXmlName(new javax.xml.namespace.QName("", "urlProdFromRequest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("urlTestFromRequest");
        elemField.setXmlName(new javax.xml.namespace.QName("", "urlTestFromRequest"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://schemas.xmlsoap.org/soap/encoding/", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userContext");
        elemField.setXmlName(new javax.xml.namespace.QName("", "userContext"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://xml.apache.org/xml-soap", "Map"));
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
