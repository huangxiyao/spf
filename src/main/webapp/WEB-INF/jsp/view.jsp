<%-----------------------------------------------------------------------------
	view.jsp

	Renders the view mode for the HtmlViewer portlet.
-----------------------------------------------------------------------------%>

<%------------------------------------------------------------- DIRECTIVES --%>

<jsp:directive.page
	import="com.hp.it.spf.htmlviewer.portlet.util.Consts" />
<jsp:directive.page import="com.hp.it.spf.htmlviewer.portlet.util.Utils" />

<%---------------------------------------------------------- TAG LIBRARIES --%>

<jsp:directive.include file="include.jsp" />

<%----------------------------------------------------------------- SCRIPT --%>

<portlet:defineObjects />
<jsp:scriptlet>
	String pathToImages = (String)renderRequest.getContextPath();
	String fileContent = (String) request.getAttribute(Consts.VIEW_CONTENT);
	String linkClass = (String) request.getAttribute(Consts.LINK_CLASS);
	String launchButtonless = (String) request.getAttribute(Consts.LAUNCH_BUTTONLESS);
	if (linkClass != null && fileContent != null) {
		String classString = "class=\""
				+ (String) request.getAttribute(Consts.LINK_CLASS) + "\"";
		fileContent = Utils.addCssStyle(fileContent, classString);
	}
	if ("true".equalsIgnoreCase(launchButtonless)) {
		String onclickString = "onclick=\"callButtonlessWindow_"
					+ renderResponse.getNamespace() + "(this);return false;\"";
		fileContent = Utils.addButtonLess(fileContent, onclickString);
	}
</jsp:scriptlet>

<%---------------------------------------------------------------- MARKUP ---%>

<link
	href="<%= renderResponse.encodeURL(pathToImages) %>/css/html_viewer.css"
	rel="stylesheet" type="text/css">
<script type="text/javascript" language="javascript" charset="utf-8">
	function callButtonlessWindow_<portlet:namespace/>(ev){
		window.open (ev,'LinkoutWindow','top=0,left=0,location=no,toolbar=no,menubar=no,status=no,resizable=yes,scrollbars=yes');
	}
</script>

<%= fileContent %>
