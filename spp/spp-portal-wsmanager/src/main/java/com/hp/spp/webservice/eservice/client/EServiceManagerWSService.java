/**
 * EServiceManagerWSService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hp.spp.webservice.eservice.client;

public interface EServiceManagerWSService extends javax.xml.rpc.Service {
    public java.lang.String getEServiceManagerAddress();

    public com.hp.spp.webservice.eservice.client.EServiceManagerWS getEServiceManager() throws javax.xml.rpc.ServiceException;

    public com.hp.spp.webservice.eservice.client.EServiceManagerWS getEServiceManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
