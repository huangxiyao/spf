<%--
This JSP gets the global help content from the request, where it
was set by the display action.  It then expresses the global help
content, wrapped inside a table which provides a print control. 
--%>

<%@ page import="com.epicentric.template.Style,
	com.hp.it.spf.xa.misc.portal.Consts" %>

<%@ taglib uri="http://www.hp.com/spf/i18n/portal" prefix="spf-i18n-portal"%>

<%@ taglib prefix="vgn-portal" uri="vgn-tags" %>

<vgn-portal:defineObjects/>

<%
    String i18nID = portalContext.getCurrentStyle().getUID();
    // The global help css file under templates directory
    Style _thisStyleObject = portalContext.getCurrentStyle();
    String cssPath = request.getContextPath() + "/" + 
        _thisStyleObject.getUrlSafeRelativePath() + 
        "globalHelp.css";
%>

<%-- Import the css for this page --%>

<style type="text/css">
    @import url("<%= cssPath %>");
</style>

<div id="print">
<script type="text/javascript" language="javascript" charset="utf-8">
    // <![CDATA[
    document.writeln('<a class="helpLink" href="#" onclick="globalHelpPrint();return false;"><spf-i18n-portal:i18nValue stringID="<%= i18nID %>" key="globalhelp.print.text" defaultValue="Print"/></a>');
    // ]]>
</script>
<noscript>
<spf-i18n-portal:i18nValue stringID="<%= i18nID %>" key="globalhelp.print.noscript.text" defaultValue="To print this document use Ctrl+p on your keypad."/>
</noscript>
</div>

<%
    //Get the content from the request and display on the page
    Object o = portalContext.getPortalRequest().getRequest().getAttribute(Consts.REQUEST_ATTR_GLOBAL_HELP_DATA);
    if (o!=null) {
        out.println(o.toString());		
    }
%>
