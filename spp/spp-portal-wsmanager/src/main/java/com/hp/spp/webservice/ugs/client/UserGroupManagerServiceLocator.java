/**
 * UserGroupManagerServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hp.spp.webservice.ugs.client;

public class UserGroupManagerServiceLocator extends org.apache.axis.client.Service implements com.hp.spp.webservice.ugs.client.UserGroupManagerService {

    public UserGroupManagerServiceLocator() {
    }


    public UserGroupManagerServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public UserGroupManagerServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for UserGroupManager
    private java.lang.String UserGroupManager_address = "http://vdccintitg.houston.hp.com/spp-services-web/services/UserGroupManager";

    public java.lang.String getUserGroupManagerAddress() {
        return UserGroupManager_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String UserGroupManagerWSDDServiceName = "UserGroupManager";

    public java.lang.String getUserGroupManagerWSDDServiceName() {
        return UserGroupManagerWSDDServiceName;
    }

    public void setUserGroupManagerWSDDServiceName(java.lang.String name) {
        UserGroupManagerWSDDServiceName = name;
    }

    public com.hp.spp.webservice.ugs.client.UserGroupManager_PortType getUserGroupManager() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(UserGroupManager_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getUserGroupManager(endpoint);
    }

    public com.hp.spp.webservice.ugs.client.UserGroupManager_PortType getUserGroupManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hp.spp.webservice.ugs.client.UserGroupManagerSoapBindingStub _stub = new com.hp.spp.webservice.ugs.client.UserGroupManagerSoapBindingStub(portAddress, this);
            _stub.setPortName(getUserGroupManagerWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setUserGroupManagerEndpointAddress(java.lang.String address) {
        UserGroupManager_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hp.spp.webservice.ugs.client.UserGroupManager_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hp.spp.webservice.ugs.client.UserGroupManagerSoapBindingStub _stub = new com.hp.spp.webservice.ugs.client.UserGroupManagerSoapBindingStub(new java.net.URL(UserGroupManager_address), this);
                _stub.setPortName(getUserGroupManagerWSDDServiceName());
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
        if ("UserGroupManager".equals(inputPortName)) {
            return getUserGroupManager();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://vdccintitg.houston.hp.com/spp-services-web/services/UserGroupManager", "UserGroupManagerService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://vdccintitg.houston.hp.com/spp-services-web/services/UserGroupManager", "UserGroupManager"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("UserGroupManager".equals(portName)) {
            setUserGroupManagerEndpointAddress(address);
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
