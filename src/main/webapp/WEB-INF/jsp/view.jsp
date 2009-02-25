<%-----------------------------------------------------------------------------
	view.jsp

	Renders the view mode for the HtmlViewer portlet.
-----------------------------------------------------------------------------%>

<%------------------------------------------------------------- DIRECTIVES --%>

<%@ include file="include.jsp" %>
<%@ page import="com.hp.it.spf.xa.htmlviewer.portlet.util.Consts" %>

<%----------------------------------------------------------------- SCRIPT --%>

<portlet:defineObjects />

<%---------------------------------------------------------------- MARKUP ---%>

<link href="<%= renderResponse.encodeURL("/css/html_viewer.css") %>" rel="stylesheet" type="text/css">

<c:out value="${viewContent}"/>
