/**
 * UGSRuntimeServiceXfireImplPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package com.hp.it.spf.user.group.stub;

public interface UGSRuntimeServiceXfireImplPortType extends java.rmi.Remote {
    public com.hp.it.spf.user.group.stub.SSAHealth instanceHealthCheck() throws java.rmi.RemoteException;
    public com.hp.it.spf.user.group.stub.GroupResponse getUsersForGroup(com.hp.it.spf.user.group.stub.GroupRequest groupRequest) throws java.rmi.RemoteException, com.hp.it.spf.user.group.stub.SiteDoesNotExistException, com.hp.it.spf.user.group.stub.NoRulesOrGroupsForSiteException, com.hp.it.spf.user.group.stub.UGSSystemException, com.hp.it.spf.user.group.stub.InvalidGroupRequestException;
    public com.hp.it.spf.user.group.stub.GroupResponse getGroups(com.hp.it.spf.user.group.stub.GroupRequest groupRequest) throws java.rmi.RemoteException, com.hp.it.spf.user.group.stub.SiteDoesNotExistException, com.hp.it.spf.user.group.stub.NoRulesOrGroupsForSiteException, com.hp.it.spf.user.group.stub.UGSSystemException, com.hp.it.spf.user.group.stub.InvalidGroupRequestException;
    public java.lang.String helloUGSRuntime() throws java.rmi.RemoteException;
}
