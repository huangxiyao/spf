/**
 * UPSWebServices.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package com.hp.spp.webservice.ups.client;

public interface UPSWebServices extends java.rmi.Remote {
    public void initCacheManager(java.lang.String attributeXMLPath, java.lang.String clientInfoXMLPath, java.lang.String adapterSettingsPath) throws java.rmi.RemoteException;
    public com.hp.spp.webservice.ups.client.UPSServiceResponse invokeService(com.hp.spp.webservice.ups.client.UPSServiceRequest serviceRequest) throws java.rmi.RemoteException, com.hp.spp.webservice.ups.client.GenericUPSFault;
}
