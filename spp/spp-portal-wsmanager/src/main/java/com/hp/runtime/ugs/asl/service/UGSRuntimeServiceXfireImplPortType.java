/**
 * UGSRuntimeServiceXfireImplPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hp.runtime.ugs.asl.service;

public interface UGSRuntimeServiceXfireImplPortType extends java.rmi.Remote {
    public com.hp.runtime.ugs.asl.service.SSAHealth instanceHealthCheck() throws java.rmi.RemoteException;
    public com.hp.runtime.ugs.asl.service.GroupResponse getUsersForGroup(com.hp.runtime.ugs.asl.service.GroupRequest groupRequest) throws java.rmi.RemoteException, com.hp.runtime.ugs.asl.service.SiteDoesNotExistException, com.hp.runtime.ugs.asl.service.NoRulesOrGroupsForSiteException, com.hp.runtime.ugs.asl.service.UGSSystemException, com.hp.runtime.ugs.asl.service.InvalidGroupRequestException;
    public com.hp.runtime.ugs.asl.service.GroupResponse getGroups(com.hp.runtime.ugs.asl.service.GroupRequest groupRequest) throws java.rmi.RemoteException, com.hp.runtime.ugs.asl.service.SiteDoesNotExistException, com.hp.runtime.ugs.asl.service.NoRulesOrGroupsForSiteException, com.hp.runtime.ugs.asl.service.UGSSystemException, com.hp.runtime.ugs.asl.service.InvalidGroupRequestException;
    public java.lang.String helloUGSRuntime() throws java.rmi.RemoteException;
}
