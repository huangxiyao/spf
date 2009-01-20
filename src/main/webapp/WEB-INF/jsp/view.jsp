<%-----------------------------------------------------------------------------
	view.jsp

	Renders the view mode for the HtmlViewer portlet.
-----------------------------------------------------------------------------%>

<%------------------------------------------------------------- DIRECTIVES --%>

<jsp:directive.page
	import="com.hp.it.spf.xa.htmlviewer.portlet.util.Consts" />

<%---------------------------------------------------------- TAG LIBRARIES --%>

<jsp:directive.include file="include.jsp" />

<%----------------------------------------------------------------- SCRIPT --%>

<portlet:defineObjects />
<jsp:scriptlet>
	String pathToCSS = (String) renderRequest.getContextPath() + "/css/html_viewer.css";
</jsp:scriptlet>

<%---------------------------------------------------------------- MARKUP ---%>

<link
	href="<%= renderResponse.encodeURL(pathToCSS) %>"
	rel="stylesheet" type="text/css">
<c:out value="${viewContent}"/>
