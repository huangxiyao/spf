<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<portlet:defineObjects />
<portlet:renderURL var="contentURL" portletMode="view">
    <portlet:param name="action" value="simpleContentManagement" />
</portlet:renderURL>
<portlet:renderURL var="compoundContentURL" portletMode="view">
    <portlet:param name="action" value="compoundContentManagement" />
</portlet:renderURL>
<portlet:renderURL var="compAttributeAdminURL" portletMode="view">
    <portlet:param name="action" value="compoundAttribute_configuration" />
</portlet:renderURL>
<portlet:renderURL var="securityURL" portletMode="view">
    <portlet:param name="action" value="securityManagement" />
</portlet:renderURL>
<portlet:renderURL var="simpAttributeAdminURL" portletMode="view">
    <portlet:param name="action" value="simpleAttribute_configuration" />
</portlet:renderURL>
<%
	String pathCSS = (String)renderRequest.getContextPath()
	        + "/css/view_style.css";
%>

<link type="text/css" rel="stylesheet" href="<%=renderResponse.encodeURL(pathCSS)  %>" />

