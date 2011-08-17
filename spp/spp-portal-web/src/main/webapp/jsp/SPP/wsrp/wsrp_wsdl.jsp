<%@ page import="com.hp.spp.wsrp.shield.WsrpShield"%><%@ page import="com.hp.spp.wsrp.shield.RemoteServerDisabledException"%><%@ page language="java" contentType="text/xml ; charset=UTF-8" %><?xml version="1.0" encoding="UTF-8"?>
<%
	String targetWsrpWsdlUrl = request.getParameter("targetWsdlUrl");
	if (targetWsrpWsdlUrl == null) {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ERROR: 'targetWsdlUrl' parameter not specified!");
		return;
	}

/*	
	String scheme = request.getScheme();
	String server = request.getServerName();
	int port = request.getServerPort();
	String context = request.getContextPath();
	String hostContext = scheme + "://" + server;
	
	if (port != 80)
	{
		hostContext += ":" + port;
	}

	hostContext += context;
*/
	int posEndHostContext = targetWsrpWsdlUrl.indexOf("/wsdl/wsrp_wsdl.jsp");
	if (posEndHostContext == -1) {
		response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ERROR: 'targetWsdlUrl' value should end with '/wsdl/wsrp_wsdl.jsp'");
		return;
	}
	if (!WsrpShield.getInstance().isServerEnabled(targetWsrpWsdlUrl)) {
		String requestUrl = request.getRequestURL().append('?').append(request.getQueryString()).toString();
		throw new RemoteServerDisabledException("WSRP server is disabled (" + requestUrl + "). You can enable it using wsrpAdmin.jsp page.");
	}
	String hostContext = targetWsrpWsdlUrl.substring(0, posEndHostContext);
%>

<!--
Copyright 2003-2005 The Apache Software Foundation.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
-->

<wsdl:definitions targetNamespace="urn:oasis:names:tc:wsrp:v1:wsdl"
                  xmlns:bind="urn:oasis:names:tc:wsrp:v1:bind"
                  xmlns="http://schemas.xmlsoap.org/wsdl/"
                  xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
                  xmlns:soap="http://schemas.xmlsoap.org/wsdl/soap/">

    <import namespace="urn:oasis:names:tc:wsrp:v1:bind"
            location="wsrp_v1_bindings.wsdl"/>

    <wsdl:service name="WSRPService">
        <wsdl:port binding="bind:WSRP_v1_Markup_Binding_SOAP" name="WSRPBaseService">
            <soap:address location="<%= hostContext %>/WSRP4JProducer/WSRPBaseService"/>
        </wsdl:port>
        <wsdl:port binding="bind:WSRP_v1_ServiceDescription_Binding_SOAP" name="WSRPServiceDescriptionService">
            <soap:address location="<%= hostContext %>/WSRP4JProducer/WSRPServiceDescriptionService"/>
        </wsdl:port>
        <wsdl:port binding="bind:WSRP_v1_Registration_Binding_SOAP" name="WSRPRegistrationService">
            <soap:address location="<%= hostContext %>/WSRP4JProducer/WSRPRegistrationService"/>
        </wsdl:port>
        <wsdl:port binding="bind:WSRP_v1_PortletManagement_Binding_SOAP" name="WSRPPortletManagementService">
            <soap:address location="<%= hostContext %>/WSRP4JProducer/WSRPPortletManagementService"/>
        </wsdl:port>
    </wsdl:service>
    
</wsdl:definitions>
