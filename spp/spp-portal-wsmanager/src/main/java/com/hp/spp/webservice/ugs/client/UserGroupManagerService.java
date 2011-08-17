/**
 * UserGroupManagerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hp.spp.webservice.ugs.client;

public interface UserGroupManagerService extends javax.xml.rpc.Service {
    public java.lang.String getUserGroupManagerAddress();

    public com.hp.spp.webservice.ugs.client.UserGroupManager_PortType getUserGroupManager() throws javax.xml.rpc.ServiceException;

    public com.hp.spp.webservice.ugs.client.UserGroupManager_PortType getUserGroupManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
