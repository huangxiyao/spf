<%-----------------------------------------------------------------------------
	view.jsp

	Renders the view mode for the HtmlViewer portlet.
-----------------------------------------------------------------------------%>

<%------------------------------------------------------------- DIRECTIVES --%>

<%@ include file="include.jsp" %>
<%@ page import="com.hp.it.spf.htmlviewer.portlet.util.Consts" %>
<%@ page import="com.hp.it.spf.xa.i18n.portlet.I18nUtility" %>

<%----------------------------------------------------------------- SCRIPT --%>

<portlet:defineObjects />

<%---------------------------------------------------------------- MARKUP ---%>

<c:out value="${viewContent}" escapeXml="false"/>
