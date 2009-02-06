/**
 * UGSRuntimeServiceXfireImplLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hp.it.spf.user.group.stub;

public class UGSRuntimeServiceXfireImplLocator extends org.apache.axis.client.Service implements com.hp.it.spf.user.group.stub.UGSRuntimeServiceXfireImpl {

    public UGSRuntimeServiceXfireImplLocator() {
    }


    public UGSRuntimeServiceXfireImplLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public UGSRuntimeServiceXfireImplLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for UGSRuntimeServiceXfireImplHttpPort
    private java.lang.String UGSRuntimeServiceXfireImplHttpPort_address = "http://htx658a.cce.hp.com:6793/xfire-webservice-webapp/services/UGSRuntimeServiceXfireImpl";

    public java.lang.String getUGSRuntimeServiceXfireImplHttpPortAddress() {
        return UGSRuntimeServiceXfireImplHttpPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String UGSRuntimeServiceXfireImplHttpPortWSDDServiceName = "UGSRuntimeServiceXfireImplHttpPort";

    public java.lang.String getUGSRuntimeServiceXfireImplHttpPortWSDDServiceName() {
        return UGSRuntimeServiceXfireImplHttpPortWSDDServiceName;
    }

    public void setUGSRuntimeServiceXfireImplHttpPortWSDDServiceName(java.lang.String name) {
        UGSRuntimeServiceXfireImplHttpPortWSDDServiceName = name;
    }

    public com.hp.it.spf.user.group.stub.UGSRuntimeServiceXfireImplPortType getUGSRuntimeServiceXfireImplHttpPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(UGSRuntimeServiceXfireImplHttpPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getUGSRuntimeServiceXfireImplHttpPort(endpoint);
    }

    public com.hp.it.spf.user.group.stub.UGSRuntimeServiceXfireImplPortType getUGSRuntimeServiceXfireImplHttpPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hp.it.spf.user.group.stub.UGSRuntimeServiceXfireImplHttpBindingStub _stub = new com.hp.it.spf.user.group.stub.UGSRuntimeServiceXfireImplHttpBindingStub(portAddress, this);
            _stub.setPortName(getUGSRuntimeServiceXfireImplHttpPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setUGSRuntimeServiceXfireImplHttpPortEndpointAddress(java.lang.String address) {
        UGSRuntimeServiceXfireImplHttpPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hp.it.spf.user.group.stub.UGSRuntimeServiceXfireImplPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hp.it.spf.user.group.stub.UGSRuntimeServiceXfireImplHttpBindingStub _stub = new com.hp.it.spf.user.group.stub.UGSRuntimeServiceXfireImplHttpBindingStub(new java.net.URL(UGSRuntimeServiceXfireImplHttpPort_address), this);
                _stub.setPortName(getUGSRuntimeServiceXfireImplHttpPortWSDDServiceName());
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
        if ("UGSRuntimeServiceXfireImplHttpPort".equals(inputPortName)) {
            return getUGSRuntimeServiceXfireImplHttpPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "UGSRuntimeServiceXfireImpl");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "UGSRuntimeServiceXfireImplHttpPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("UGSRuntimeServiceXfireImplHttpPort".equals(portName)) {
            setUGSRuntimeServiceXfireImplHttpPortEndpointAddress(address);
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
