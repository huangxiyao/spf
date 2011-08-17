<%@page import="com.epicentric.common.website.MenuItemUtils"%>
<%@page import="com.epicentric.common.website.MenuItemNode"%>
<%@page import="com.epicentric.template.Style"%>
<%@page import="com.epicentric.common.website.I18nUtils"%>
<%@ page import="com.hp.spp.portal.common.util.HPUrlLocator"%>
<%@ page import="com.hp.spp.portal.common.helper.LocalizationHelper" %>
<%@ page import="com.hp.spp.portal.common.helper.PortalHelper" %>

<%@ taglib uri="vgn-tags" prefix="vgn-portal" %>

<vgn-portal:defineObjects/>
<% 
	String hpImagesURL = HPUrlLocator.getWelcomeUrl(portalContext,false) + "/";
	String message = "";
	String description = "";
	String i18nID = "";

	LocalizationHelper localizationHelper = new LocalizationHelper() ;
	String i18pageID = localizationHelper.getCommonI18nID(portalContext);

	String styleFriendID = portalContext.getCurrentSecondaryPage().getFriendlyID();
	if (!styleFriendID.equals("PAGE") && !styleFriendID.equals("JSP_INCLUDE_PAGE") && !styleFriendID.equals("spp_pagedisplay") && !styleFriendID.equals("MAXIMIZE")){
		i18nID = portalContext.getCurrentSecondaryPage().getUID();
		message = I18nUtils.getValue(i18nID, "title", "", request);
		description = I18nUtils.getValue(i18nID, "description", "", request);
	}else{
		i18nID = MenuItemUtils.getSelectedMenuItemNode(portalContext).getID() ;

		message = I18nUtils.getValue(i18nID, "title_display", "", session, request);
		if (message.equals("")) {
			message = I18nUtils.getValue(i18nID, "title", "", session, request);
		}
		// This is a workaround to the fact that we sometimes don't want a translation. In this case
		// business is putting one or more space characters in the translation. During site export/import
		// this spaces are removed by Vignette. Therefore before we import the translation the spaces
		// are replaced by &nbsp; and such modified translations are imported. But Vignette will HTML-escape
		// the text which will result in value like "&amp;nbsp;" and which will be shown on the screen
		// not as invisible space but as "&nbsp;".
		// This workaround is to make sure that the values that come as "&amp;nbsp;" are not visible on the screen.
		if (message != null && message.indexOf("&amp;nbsp;") >= 0) {
			message = message.replaceAll("&amp;nbsp;", "&nbsp;");
		}

		description = I18nUtils.getValue(i18nID, "description_display", "", session, request);
		if (description.equals("")) {
			description = I18nUtils.getValue(i18nID, "description", "", session, request);
		}
		// For "why" see the explanation in the comment above.
		if (description != null && description.indexOf("&amp;nbsp;") >= 0) {
			description = description.replaceAll("&amp;nbsp;", "&nbsp;");
		}
	}

	String backuri = new PortalHelper().getSafeRequestURL(request).replaceAll("printable=x", "");

%>

<%
	if( request.getParameter("printable")!=null) {
%>
				
<table border="0" cellpadding="0" cellspacing="0" width="1040">
        <tr>
            <td width="170" align="center" valign="middle">
            <a href="<%=HPUrlLocator.getLocalizedHPUrl(portalContext, HPUrlLocator.WELCOME_PAGE, true)%>" target="_blank">
            <img border="0" alt="logo hp - invent" src="<%=hpImagesURL%>img/hpweb_1-2_topnav_hp_logo.gif"></a>
        </td>
                <td width="10"><img src="<%=hpImagesURL%>img/s.gif" width="10" height="93" alt="" /></td>
                <td width="560" align="left" valign="top">
                <table width="560" border="0" cellpadding="0" cellspacing="0">
                	<tr height="20">
                		<td>&nbsp;</td>
                	</tr>
                	<tr height="53">
                		<td>
                		<h1><%=message%>&nbsp;</h1>
			            <h2><%=description%>&nbsp;</h2>
	               		</td>
                	</tr>
                	<tr height="20">
                		<td>&nbsp;</td>
                	</tr>
                </table>
                </td>
				<td width="300" align="left" valign="middle">
					&nbsp;&raquo;&nbsp;
					<font color="#003366"><a href="<%=backuri%>"><vgn-portal:i18nValue stringID="<%=i18pageID%>" key="original_page" defaultValue="Return to original Page" /></a></font>
				</td>
		</tr>
</table>
<%
	}else{
%>

<table border="0" cellpadding="0" cellspacing="0" width="740">
        <tr>
            <td width="170" align="center" valign="middle">
            <a href="<%=HPUrlLocator.getLocalizedHPUrl(portalContext, HPUrlLocator.WELCOME_PAGE, true)%>" target="_blank">
            <img border="0" alt="logo hp - invent" src="<%=hpImagesURL%>img/hpweb_1-2_topnav_hp_logo.gif"></a>
			</td>
                <td width="10"><img src="<%=hpImagesURL%>img/s.gif" width="10" height="93" alt="" /></td>
                <td width="560" align="left" valign="top">
                <table width="560" border="0" cellpadding="0" cellspacing="0">
                	<tr height="20">
                		<td>&nbsp;</td>
                	</tr>
                	<tr height="53">
                		<td>
                		<h1><%=message%>&nbsp;</h1>
			            <h2><%=description%>&nbsp;</h2>
	               		</td>
                	</tr>
                	<tr height="20">
                		<td>&nbsp;</td>
                	</tr>
                </table>
                </td>
		</tr>
</table>
<%
		}
%>