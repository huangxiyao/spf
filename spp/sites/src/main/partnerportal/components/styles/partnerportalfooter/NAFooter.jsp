<%@ taglib uri="vgn-tags" prefix="vgn-portal" %>
<%@ page import="com.hp.spp.portal.common.util.HPUrlLocator" %>
<%@ page import="com.hp.spp.portal.common.helper.LocalizationHelper" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.hp.spp.common.util.DiagnosticContext" %>
<%@ page import="com.hp.spp.portal.diagnosticcontext.DiagnosticContextFilter" %>

<vgn-portal:defineObjects/>

<% 
	String defaultURL = HPUrlLocator.getWelcomeUrl(portalContext, false) + "/";
	String i18nID = new LocalizationHelper().getCommonI18nID(portalContext);
	GregorianCalendar c = new GregorianCalendar(); 
%>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr class="decoration">
    <td class="color666666bg"><img src="<%=defaultURL%>img/s.gif" width="1" height="4" alt="" border="0"></td>
</tr>
<tr>
    <td align="left" valign="top">
        <table border="0" cellpadding="0" cellspacing="0" width="740">
		<tr>
			<td align="left" class="small"  colspan="4">
				You may see content that is not applicable to your authorization, accreditation or program participation. Please use the "Contact HP" link in the top left corner of any page if you need more information or assistance.
			</td>
		</tr>
        <tr class="decoration">
            <td colspan="4"><img src="<%=defaultURL%>img/s.gif" width="1" height="4" alt="" border="0"></td>
        </tr>
         <tr>

            <td width="33%" align="center">
            	<a href="<%=HPUrlLocator.getLocalizedHPUrl(portalContext, HPUrlLocator.PRIVACY_PAGE, true)%>" 
            		class="udrlinesmall" target="_blank">
            		<vgn-portal:i18nValue stringID="<%=i18nID%>" key="privacy" defaultValue="Default privacy" />
            	</a>
            </td>
            <td width="33%" align="center">
            	<a href="<%=HPUrlLocator.getLocalizedHPUrl(portalContext, HPUrlLocator.LEGACY_PAGE, true)%>" 
            	class="udrlinesmall" target="_blank">
            		<vgn-portal:i18nValue stringID="<%=i18nID%>" key="legacy" defaultValue="Default legacy" />
            	</a>
            </td>
            <td width="33%" align="center"></td>
        </tr>
        <tr class="decoration">
            <td colspan="4"><img src="<%=defaultURL%>img/s.gif" width="1" height="4" alt=""></td>
        </tr>
        <tr>
            <td align="center" colspan="4" class="small">
			© <%=c.get(Calendar.YEAR)%> Hewlett-Packard Development Company, L.P.
			</td>
        </tr>		
        </table>
    </td>
</tr>
</table>
<% DiagnosticContext diagnosticContext = (DiagnosticContext) request.getAttribute(DiagnosticContextFilter.DIAGNOSTIC_CONTEXT_REQUEST_KEY);

   if (diagnosticContext != null) {
	   if (!diagnosticContext.isEmpty()) {
			out.println("<!-- Diagnostic error message -->");
			out.println("<!--" + diagnosticContext.getErrorMessage() + "-->");
   	   }
   }
%>
