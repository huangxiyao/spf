<%@ page import="com.hp.spp.portal.common.util.HPUrlLocator"%>
<%@page import="com.epicentric.common.website.I18nUtils"%>
<%@ page import="com.hp.spp.portal.common.helper.LocalizationHelper" %>
<%@ page import="com.hp.spp.portal.common.helper.PortalHelper" %>

<%@ taglib uri="vgn-tags" prefix="vgn-portal"%>

<vgn-portal:defineObjects />

<%
	String hpImagesURL = HPUrlLocator.getWelcomeUrl(portalContext, false)+ "/";
	LocalizationHelper localizationHelper = new LocalizationHelper() ;
	String i18nID = localizationHelper.getCommonI18nID(portalContext);

	String backuri = new PortalHelper().getSafeRequestURL(request).replaceAll("printable=x", ""); 
%>

<%
	if( request.getParameter("printable")!=null) {
%>
<table border="0" cellpadding="0" cellspacing="0" width="1040">
	<tr>
		<td width="170" align="center" valign="middle"><a
			href="<%=HPUrlLocator.getLocalizedHPUrl(portalContext, HPUrlLocator.WELCOME_PAGE, true)%>"
			target="_blank"> <img border="0" alt="logo hp - invent"
			src="<%=hpImagesURL%>img/hpweb_1-2_topnav_hp_logo.gif"></a>
		</td>
		<td width="10"><img src="<%=hpImagesURL%>img/s.gif" width="10"
			height="93" alt="" />
		</td>
		<td width="560" bgcolor="#FFFFFF" align="left" valign="top">
        </td>
		<td width="300" align="center" valign="middle" >
			&nbsp;&raquo;&nbsp;
		<font color="#003366"><a href="<%=backuri%>"><vgn-portal:i18nValue stringID="<%=i18nID%>" key="original_page" defaultValue="Return to original Page" /></a></font>
		</td>
	</tr>
</table>
<%
	}else{
%>

<table border="0" cellpadding="0" cellspacing="0" width="170">
	<tr>
		<td width="170" align="center" valign="middle"><a
			href="<%=HPUrlLocator.getLocalizedHPUrl(portalContext, HPUrlLocator.WELCOME_PAGE, true)%>"
			target="_blank"> <img border="0" alt="logo hp - invent"
			src="<%=hpImagesURL%>img/hpweb_1-2_topnav_hp_logo.gif"></a>
		</td>
		<td width="10"><img src="<%=hpImagesURL%>img/s.gif" width="10"
			height="93" alt="" />
		</td>
	</tr>
</table>

<%
		}
%>
