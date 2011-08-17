<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@page import="com.hp.spp.config.Config" %>
<%@page import="com.hp.spp.config.Config" %>
<%@page import="org.apache.log4j.Logger" %>

<%@ page language="java"%>

<portlet:defineObjects />



<%
    String defaultPath = renderRequest.getContextPath() + "/AgeingReportPage/report";
	//Encode the URL string
	String url= defaultPath+"?lastLoginDate="+request.getParameter("lastLoginDate");
	String encodedURL = renderResponse.encodeURL(url);
%>
<a href="<%=encodedURL%>"> Download user ageing report link </a>

