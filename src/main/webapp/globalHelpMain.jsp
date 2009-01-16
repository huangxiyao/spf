<%--
This JSP gets the global help content from the request, where it
was set by the display action.  It then expresses the global help
content, wrapped inside a table which provides a print control. 
--%>

<%@ page import="com.hp.it.spf.xa.misc.portal.Consts" %>

<%@ taglib prefix="vgn-portal" uri="vgn-tags" %>

<vgn-portal:defineObjects/>

<%-- Render the global help content --%>
<%
    Object o = portalContext.getPortalRequest().getRequest().getAttribute(Consts.REQUEST_ATTR_GLOBAL_HELP_DATA);
    if (o!=null) {
        out.println(o.toString());		
    }
%>
