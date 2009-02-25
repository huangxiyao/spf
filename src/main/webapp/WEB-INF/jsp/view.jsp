<%-----------------------------------------------------------------------------
	view.jsp

	Renders the view mode for the HtmlViewer portlet.
-----------------------------------------------------------------------------%>

<%------------------------------------------------------------- DIRECTIVES --%>

<%@ include file="include.jsp" %>
<%@ page import="com.hp.it.spf.xa.htmlviewer.portlet.util.Consts" %>
<%@ page import="com.hp.it.spf.xa.i18n.portlet.I18nUtility" %>

<%----------------------------------------------------------------- SCRIPT --%>

<portlet:defineObjects />

<%---------------------------------------------------------------- MARKUP ---%>

<link href="<%= I18nUtility.getLocalizedFileURL(renderRequest, renderResponse, "/css/html_viewer.css", false) %>" rel="stylesheet" type="text/css">

<c:out value="${viewContent}"/>
