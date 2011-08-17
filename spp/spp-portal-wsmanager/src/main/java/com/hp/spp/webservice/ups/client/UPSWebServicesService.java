/**
 * UPSWebServicesService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hp.spp.webservice.ups.client;

public interface UPSWebServicesService extends javax.xml.rpc.Service {
    public java.lang.String getUPSWebServicesAddress();

    public com.hp.spp.webservice.ups.client.UPSWebServices_PortType getUPSWebServices() throws javax.xml.rpc.ServiceException;

    public com.hp.spp.webservice.ups.client.UPSWebServices_PortType getUPSWebServices(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
