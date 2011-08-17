<%@ taglib uri="vgn-tags" prefix="vgn-portal" %>
<%@ page import="com.hp.spp.portal.common.util.HPUrlLocator" %>
<%@ page import="com.hp.spp.portal.common.helper.LocalizationHelper" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="com.hp.spp.common.util.DiagnosticContext" %>
<%@ page import="com.hp.spp.portal.diagnosticcontext.DiagnosticContextFilter" %>
<%@ page import="com.hp.spp.portal.common.helper.ProfileHelper" %>
<%@ page import="com.hp.spp.profile.Constants" %>

<vgn-portal:defineObjects/>

<%
	String defaultURL = HPUrlLocator.getWelcomeUrl(portalContext, false) + "/";
	String i18nID = new LocalizationHelper().getCommonI18nID(portalContext);
	GregorianCalendar c = new GregorianCalendar();
	ProfileHelper profileHelper = new ProfileHelper();
%>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr class="decoration">
    <td class="color666666bg"><img src="<%=defaultURL%>img/s.gif" width="1" height="4" alt="" border="0"></td>
</tr>
<tr>
    <td align="left" valign="top">
        <table border="0" cellpadding="0" cellspacing="0" width="740">
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
			<%--Added the below <td> part to display the china IPC license number in SPP footer if the lang code is 'zh_cn'. 
				For this we added the value for "ipc_license" key in APJ localization for only china country
				This license number will display on the footer only when the lang code = zh_cn. 
				For all other languages, it will not display anything as we haven't mentioned value for the 
				other languages except china--%>			
            <td width="33%" align="center">
				<%
				// Retrieve the language name of the end-user
				String langCode = profileHelper.getProfileValue(portalContext, Constants.MAP_LANGUAGE);
				if(langCode == null || "".equals(langCode))
					langCode = "en" ;
				else
					langCode = langCode.toLowerCase() ;
				
				if ( langCode.equals("zh_cn")  ) { %>
					<a href="<%="http://www.miibeian.gov.cn"%>" class="udrlinesmall" target="_blank">
						<vgn-portal:i18nValue stringID="<%=i18nID%>" key="ipc_license" defaultValue="" />
					</a>
				<%}%>
			</td>
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
