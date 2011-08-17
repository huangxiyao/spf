/**
 * UPSWebServicesServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hp.spp.webservice.ups.client;

public class UPSWebServicesServiceLocator extends org.apache.axis.client.Service implements com.hp.spp.webservice.ups.client.UPSWebServicesService {

    public UPSWebServicesServiceLocator() {
    }


    public UPSWebServicesServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public UPSWebServicesServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for UPSWebServices
    private java.lang.String UPSWebServices_address = "http://15.138.173.88:8001/upsweb/services/UPSWebServices";

    public java.lang.String getUPSWebServicesAddress() {
        return UPSWebServices_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String UPSWebServicesWSDDServiceName = "UPSWebServices";

    public java.lang.String getUPSWebServicesWSDDServiceName() {
        return UPSWebServicesWSDDServiceName;
    }

    public void setUPSWebServicesWSDDServiceName(java.lang.String name) {
        UPSWebServicesWSDDServiceName = name;
    }

    public com.hp.spp.webservice.ups.client.UPSWebServices_PortType getUPSWebServices() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(UPSWebServices_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getUPSWebServices(endpoint);
    }

    public com.hp.spp.webservice.ups.client.UPSWebServices_PortType getUPSWebServices(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hp.spp.webservice.ups.client.UPSWebServicesSoapBindingStub _stub = new com.hp.spp.webservice.ups.client.UPSWebServicesSoapBindingStub(portAddress, this);
            _stub.setPortName(getUPSWebServicesWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setUPSWebServicesEndpointAddress(java.lang.String address) {
        UPSWebServices_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hp.spp.webservice.ups.client.UPSWebServices_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hp.spp.webservice.ups.client.UPSWebServicesSoapBindingStub _stub = new com.hp.spp.webservice.ups.client.UPSWebServicesSoapBindingStub(new java.net.URL(UPSWebServices_address), this);
                _stub.setPortName(getUPSWebServicesWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("UPSWebServices".equals(inputPortName)) {
            return getUPSWebServices();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "UPSWebServicesService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://UPSWeb.hp.com/Services", "UPSWebServices"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("UPSWebServices".equals(portName)) {
            setUPSWebServicesEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
