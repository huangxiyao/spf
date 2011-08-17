/**
 * EServiceManagerWSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.hp.spp.webservice.eservice.client;

public class EServiceManagerWSServiceLocator extends org.apache.axis.client.Service implements com.hp.spp.webservice.eservice.client.EServiceManagerWSService {

    public EServiceManagerWSServiceLocator() {
    }


    public EServiceManagerWSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public EServiceManagerWSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for EServiceManager
    private java.lang.String EServiceManager_address = "http://localhost:27005/spp-services-web/services/EServiceManager";

    public java.lang.String getEServiceManagerAddress() {
        return EServiceManager_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String EServiceManagerWSDDServiceName = "EServiceManager";

    public java.lang.String getEServiceManagerWSDDServiceName() {
        return EServiceManagerWSDDServiceName;
    }

    public void setEServiceManagerWSDDServiceName(java.lang.String name) {
        EServiceManagerWSDDServiceName = name;
    }

    public com.hp.spp.webservice.eservice.client.EServiceManagerWS getEServiceManager() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(EServiceManager_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getEServiceManager(endpoint);
    }

    public com.hp.spp.webservice.eservice.client.EServiceManagerWS getEServiceManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hp.spp.webservice.eservice.client.EServiceManagerSoapBindingStub _stub = new com.hp.spp.webservice.eservice.client.EServiceManagerSoapBindingStub(portAddress, this);
            _stub.setPortName(getEServiceManagerWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setEServiceManagerEndpointAddress(java.lang.String address) {
        EServiceManager_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hp.spp.webservice.eservice.client.EServiceManagerWS.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hp.spp.webservice.eservice.client.EServiceManagerSoapBindingStub _stub = new com.hp.spp.webservice.eservice.client.EServiceManagerSoapBindingStub(new java.net.URL(EServiceManager_address), this);
                _stub.setPortName(getEServiceManagerWSDDServiceName());
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
        if ("EServiceManager".equals(inputPortName)) {
            return getEServiceManager();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://localhost:27005/spp-services-web/services/EServiceManager", "EServiceManagerWSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://localhost:27005/spp-services-web/services/EServiceManager", "EServiceManager"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("EServiceManager".equals(portName)) {
            setEServiceManagerEndpointAddress(address);
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
