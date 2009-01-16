<%-- This JSP displays an SPF-standard authorization error page. 

This JSP assumes that the surrounding grid or styles have defined the 
following CSS classes: errorTitle, errorMessage, and errorCode. --%>

<%@ page import="com.epicentric.common.website.SessionUtils"%>

<%@ taglib uri="http://www.hp.com/spf/i18n/portal"
	prefix="spf-i18n-portal"%>
<%@ taglib uri="vgn-tags" prefix="vgn-portal"%>

<vgn-portal:defineObjects />

<%    
    String i18nID = portalContext.getCurrentStyle().getUID();
%>

<%-- Output page data --%>
<div style="padding-top: 10px; margin-left: 10px">
<h3 class="errorTitle"><spf-i18n-portal:i18nValuestringID
	="<%= i18nID %>" key="autherror.title.text" /></h3>
<table border="0" cellspacing="0">
	<tr>
		<td>
		<p class="errorMessage"><sp-i18n-portal:i18nValue
			stringID="<%= i18nID %>" key="autherror.notice.text" /></p>
		</td>
	</tr>
	<tr>
		<td>
		<p class="errorCode"><sp-i18n-portal:i18nValue
			stringID="<%= i18nID %>" key="autherror.errorcode.text" /></p>
		</td>
	</tr>
</table>
</div>
