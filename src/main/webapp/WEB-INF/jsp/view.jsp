<%-----------------------------------------------------------------------------
	view.jsp

	Renders the view mode for the HtmlViewer portlet.
-----------------------------------------------------------------------------%>

<%------------------------------------------------------------- DIRECTIVES --%>

<jsp:directive.page
	import="com.hp.it.spf.xa.htmlviewer.portlet.util.Consts" />
<jsp:directive.page import="com.hp.it.spf.xa.htmlviewer.portlet.util.Utils" />

<%---------------------------------------------------------- TAG LIBRARIES --%>

<jsp:directive.include file="include.jsp" />

<%----------------------------------------------------------------- SCRIPT --%>

<portlet:defineObjects />
<jsp:scriptlet>
	String fileContent = (String) request.getAttribute(Consts.VIEW_CONTENT);
</jsp:scriptlet>

<%---------------------------------------------------------------- MARKUP ---%>

<%= fileContent %>
