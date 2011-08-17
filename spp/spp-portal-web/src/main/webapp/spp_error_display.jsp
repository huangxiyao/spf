<%@ page import="org.apache.log4j.Logger"%>
<%!
	private static final Logger mLog = Logger.getLogger("com.hp.spp.jsp.spp_error_display");
%>
<%
	Object error = pageContext.findAttribute("javax.servlet.error.exception");
	String uri = (String)  pageContext.findAttribute("javax.servlet.error.request_uri");
	if (error == null) {
		error = pageContext.findAttribute(PageContext.EXCEPTION);
	}
	if (uri == null) {
		uri = request.getRequestURI();
	}

	if (error == null) {
		mLog.error("Unexpected error occured for request '" + uri + "'");
	}
	else {
		if (error instanceof Throwable) {
			mLog.error("Unexpected error occured for request '" + uri + "'", (Throwable) error);
		}
		else {
			mLog.error("Unexpected error occured for request '" + uri + "': " + error);
		}
	}
	response.sendRedirect("/portal/error_display.jsp");
%>

