/**
 * UserGroupManagerService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.hp.spp.webservice.groups.client;

public interface UserGroupManagerService extends javax.xml.rpc.Service {
    public java.lang.String getUserGroupManagerAddress();

    public com.hp.spp.webservice.groups.client.UserGroupManager_PortType getUserGroupManager() throws javax.xml.rpc.ServiceException;

    public com.hp.spp.webservice.groups.client.UserGroupManager_PortType getUserGroupManager(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
