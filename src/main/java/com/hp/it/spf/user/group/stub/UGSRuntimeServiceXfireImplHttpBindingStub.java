/**
 * UGSRuntimeServiceXfireImplHttpBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hp.it.spf.user.group.stub;

public class UGSRuntimeServiceXfireImplHttpBindingStub extends org.apache.axis.client.Stub implements com.hp.it.spf.user.group.stub.UGSRuntimeServiceXfireImplPortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[4];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("instanceHealthCheck");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "instanceHealthCheck"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">instanceHealthCheck"), com.hp.it.spf.user.group.stub.InstanceHealthCheck.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">instanceHealthCheckResponse"));
        oper.setReturnClass(com.hp.it.spf.user.group.stub.InstanceHealthCheckResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "instanceHealthCheckResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getUsersForGroup");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "getUsersForGroup"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">getUsersForGroup"), com.hp.it.spf.user.group.stub.GetUsersForGroup.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">getUsersForGroupResponse"));
        oper.setReturnClass(com.hp.it.spf.user.group.stub.GetUsersForGroupResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "getUsersForGroupResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "SiteDoesNotExistException"),
                      "com.hp.it.spf.user.group.stub.SiteDoesNotExistException",
                      new javax.xml.namespace.QName("http://exceptions.shared.ugs.hp.com", "SiteDoesNotExistException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "NoRulesOrGroupsForSiteException"),
                      "com.hp.it.spf.user.group.stub.NoRulesOrGroupsForSiteException",
                      new javax.xml.namespace.QName("http://exceptions.shared.ugs.hp.com", "NoRulesOrGroupsForSiteException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "InvalidGroupRequestException"),
                      "com.hp.it.spf.user.group.stub.InvalidGroupRequestException",
                      new javax.xml.namespace.QName("http://exceptions.shared.ugs.hp.com", "InvalidGroupRequestException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "UGSSystemException"),
                      "com.hp.it.spf.user.group.stub.UGSSystemException",
                      new javax.xml.namespace.QName("http://exceptions.shared.ugs.hp.com", "UGSSystemException"), 
                      true
                     ));
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("getGroups");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "getGroups"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">getGroups"), com.hp.it.spf.user.group.stub.GetGroups.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">getGroupsResponse"));
        oper.setReturnClass(com.hp.it.spf.user.group.stub.GetGroupsResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "getGroupsResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "SiteDoesNotExistException"),
                      "com.hp.it.spf.user.group.stub.SiteDoesNotExistException",
                      new javax.xml.namespace.QName("http://exceptions.shared.ugs.hp.com", "SiteDoesNotExistException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "NoRulesOrGroupsForSiteException"),
                      "com.hp.it.spf.user.group.stub.NoRulesOrGroupsForSiteException",
                      new javax.xml.namespace.QName("http://exceptions.shared.ugs.hp.com", "NoRulesOrGroupsForSiteException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "InvalidGroupRequestException"),
                      "com.hp.it.spf.user.group.stub.InvalidGroupRequestException",
                      new javax.xml.namespace.QName("http://exceptions.shared.ugs.hp.com", "InvalidGroupRequestException"), 
                      true
                     ));
        oper.addFault(new org.apache.axis.description.FaultDesc(
                      new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "UGSSystemException"),
                      "com.hp.it.spf.user.group.stub.UGSSystemException",
                      new javax.xml.namespace.QName("http://exceptions.shared.ugs.hp.com", "UGSSystemException"), 
                      true
                     ));
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("helloUGSRuntime");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "helloUGSRuntime"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">helloUGSRuntime"), com.hp.it.spf.user.group.stub.HelloUGSRuntime.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">helloUGSRuntimeResponse"));
        oper.setReturnClass(com.hp.it.spf.user.group.stub.HelloUGSRuntimeResponse.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "helloUGSRuntimeResponse"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

    }

    public UGSRuntimeServiceXfireImplHttpBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public UGSRuntimeServiceXfireImplHttpBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public UGSRuntimeServiceXfireImplHttpBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://exceptions.shared.ugs.hp.com", "InvalidGroupRequestException");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.InvalidGroupRequestException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://exceptions.shared.ugs.hp.com", "NoRulesOrGroupsForSiteException");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.NoRulesOrGroupsForSiteException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://exceptions.shared.ugs.hp.com", "SiteDoesNotExistException");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.SiteDoesNotExistException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://exceptions.shared.ugs.hp.com", "UGSSystemException");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.UGSSystemException.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">anyType2anyTypeMap>entry");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.AnyType2AnyTypeMapEntry.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">getGroups");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.GetGroups.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">getGroupsResponse");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.GetGroupsResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">getUsersForGroup");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.GetUsersForGroup.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">getUsersForGroupResponse");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.GetUsersForGroupResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">helloUGSRuntime");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.HelloUGSRuntime.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">helloUGSRuntimeResponse");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.HelloUGSRuntimeResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">instanceHealthCheck");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.InstanceHealthCheck.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", ">instanceHealthCheckResponse");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.InstanceHealthCheckResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "anyType2anyTypeMap");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.AnyType2AnyTypeMap.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://service.asl.ugs.runtime.hp.com", "ArrayOfString");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.ArrayOfString.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://shared.ugs.hp.com", "ArrayOfUserContext");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.ArrayOfUserContext.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://shared.ugs.hp.com", "GroupRequest");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.GroupRequest.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://shared.ugs.hp.com", "GroupResponse");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.GroupResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://shared.ugs.hp.com", "UserContext");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.UserContext.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://util.ssa.frameworks.hp.com", "DiagnosticContext");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.DiagnosticContext.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://util.ssa.frameworks.hp.com", "Instance");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.Instance.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://util.ssa.frameworks.hp.com", "SSAHealth");
            cachedSerQNames.add(qName);
            cls = com.hp.it.spf.user.group.stub.SSAHealth.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public com.hp.it.spf.user.group.stub.InstanceHealthCheckResponse instanceHealthCheck(com.hp.it.spf.user.group.stub.InstanceHealthCheck parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "instanceHealthCheck"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.hp.it.spf.user.group.stub.InstanceHealthCheckResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.hp.it.spf.user.group.stub.InstanceHealthCheckResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.hp.it.spf.user.group.stub.InstanceHealthCheckResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public com.hp.it.spf.user.group.stub.GetUsersForGroupResponse getUsersForGroup(com.hp.it.spf.user.group.stub.GetUsersForGroup parameters) throws java.rmi.RemoteException, com.hp.it.spf.user.group.stub.SiteDoesNotExistException, com.hp.it.spf.user.group.stub.NoRulesOrGroupsForSiteException, com.hp.it.spf.user.group.stub.UGSSystemException, com.hp.it.spf.user.group.stub.InvalidGroupRequestException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getUsersForGroup"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.hp.it.spf.user.group.stub.GetUsersForGroupResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.hp.it.spf.user.group.stub.GetUsersForGroupResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.hp.it.spf.user.group.stub.GetUsersForGroupResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.hp.it.spf.user.group.stub.SiteDoesNotExistException) {
              throw (com.hp.it.spf.user.group.stub.SiteDoesNotExistException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.hp.it.spf.user.group.stub.NoRulesOrGroupsForSiteException) {
              throw (com.hp.it.spf.user.group.stub.NoRulesOrGroupsForSiteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.hp.it.spf.user.group.stub.UGSSystemException) {
              throw (com.hp.it.spf.user.group.stub.UGSSystemException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.hp.it.spf.user.group.stub.InvalidGroupRequestException) {
              throw (com.hp.it.spf.user.group.stub.InvalidGroupRequestException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public com.hp.it.spf.user.group.stub.GetGroupsResponse getGroups(com.hp.it.spf.user.group.stub.GetGroups parameters) throws java.rmi.RemoteException, com.hp.it.spf.user.group.stub.SiteDoesNotExistException, com.hp.it.spf.user.group.stub.NoRulesOrGroupsForSiteException, com.hp.it.spf.user.group.stub.UGSSystemException, com.hp.it.spf.user.group.stub.InvalidGroupRequestException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "getGroups"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.hp.it.spf.user.group.stub.GetGroupsResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.hp.it.spf.user.group.stub.GetGroupsResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.hp.it.spf.user.group.stub.GetGroupsResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
    if (axisFaultException.detail != null) {
        if (axisFaultException.detail instanceof java.rmi.RemoteException) {
              throw (java.rmi.RemoteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.hp.it.spf.user.group.stub.SiteDoesNotExistException) {
              throw (com.hp.it.spf.user.group.stub.SiteDoesNotExistException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.hp.it.spf.user.group.stub.NoRulesOrGroupsForSiteException) {
              throw (com.hp.it.spf.user.group.stub.NoRulesOrGroupsForSiteException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.hp.it.spf.user.group.stub.UGSSystemException) {
              throw (com.hp.it.spf.user.group.stub.UGSSystemException) axisFaultException.detail;
         }
        if (axisFaultException.detail instanceof com.hp.it.spf.user.group.stub.InvalidGroupRequestException) {
              throw (com.hp.it.spf.user.group.stub.InvalidGroupRequestException) axisFaultException.detail;
         }
   }
  throw axisFaultException;
}
    }

    public com.hp.it.spf.user.group.stub.HelloUGSRuntimeResponse helloUGSRuntime(com.hp.it.spf.user.group.stub.HelloUGSRuntime parameters) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "helloUGSRuntime"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {parameters});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (com.hp.it.spf.user.group.stub.HelloUGSRuntimeResponse) _resp;
            } catch (java.lang.Exception _exception) {
                return (com.hp.it.spf.user.group.stub.HelloUGSRuntimeResponse) org.apache.axis.utils.JavaUtils.convert(_resp, com.hp.it.spf.user.group.stub.HelloUGSRuntimeResponse.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
