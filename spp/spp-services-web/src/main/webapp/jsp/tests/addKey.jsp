<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%
String key = request.getParameter("key");
com.hp.spp.groups.Site site = com.hp.spp.groups.dao.SiteDAOCacheImpl.getInstance().load(Integer.parseInt(key));

response.sendRedirect("testCache.jsp");
%>
