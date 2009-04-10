/**
 * UGSRuntimeServiceXfireImplPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hp.it.spf.user.group.stub;

public interface UGSRuntimeServiceXfireImplPortType extends java.rmi.Remote {
    public com.hp.it.spf.user.group.stub.InstanceHealthCheckResponse instanceHealthCheck(com.hp.it.spf.user.group.stub.InstanceHealthCheck parameters) throws java.rmi.RemoteException;
    public com.hp.it.spf.user.group.stub.GetUsersForGroupResponse getUsersForGroup(com.hp.it.spf.user.group.stub.GetUsersForGroup parameters) throws java.rmi.RemoteException, com.hp.it.spf.user.group.stub.SiteDoesNotExistException, com.hp.it.spf.user.group.stub.NoRulesOrGroupsForSiteException, com.hp.it.spf.user.group.stub.UGSSystemException, com.hp.it.spf.user.group.stub.InvalidGroupRequestException;
    public com.hp.it.spf.user.group.stub.GetGroupsResponse getGroups(com.hp.it.spf.user.group.stub.GetGroups parameters) throws java.rmi.RemoteException, com.hp.it.spf.user.group.stub.SiteDoesNotExistException, com.hp.it.spf.user.group.stub.NoRulesOrGroupsForSiteException, com.hp.it.spf.user.group.stub.UGSSystemException, com.hp.it.spf.user.group.stub.InvalidGroupRequestException;
    public com.hp.it.spf.user.group.stub.HelloUGSRuntimeResponse helloUGSRuntime(com.hp.it.spf.user.group.stub.HelloUGSRuntime parameters) throws java.rmi.RemoteException;
}
