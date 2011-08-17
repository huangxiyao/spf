<%@ page import="java.util.*,
                     com.epicentric.common.website.*,
                     com.epicentric.authentication.*,
                     com.epicentric.common.website.RealmUtils,
					 com.hp.globalops.hppcbl.passport.beans.Fault"
%>

<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@ taglib uri="vgn-tags" prefix="vgn-portal" %>

<vgn-portal:defineObjects/>
<%
String i18nID = portalContext.getCurrentSecondaryPage().getUID() ;
String headerTitle = "" ;
String headerDescription = "" ;

if(request.getAttribute("forward") != null 
		&& request.getAttribute("faults") != null 
		|| request.getAttribute("forward") == null 
		&& request.getAttribute("faults") == null) { 

	headerTitle = I18nUtils.getValue(i18nID, "headerTitle", "", request) ;
	headerDescription = I18nUtils.getValue(i18nID, "headerDescription", "", request) ;
} else {
	
	headerTitle = I18nUtils.getValue(i18nID, "headerTitleSuccess", "", request) ;
	headerDescription = I18nUtils.getValue(i18nID, "headerDescriptionSuccess", "", request) ;
}

String headerTitleBgColor = I18nUtils.getValue(i18nID, "headerTitleBgColor", "", request) ;
String headerDescriptionBgColor = I18nUtils.getValue(i18nID, "headerDescriptionBgColor", "", request) ;

String empty = "" ;
HashMap faultsMap = new HashMap() ;

//To avoid cross site scripting attack the email_message has been encoded.
String emailMessage = StringEscapeUtils.escapeHtml(request.getParameter("email_message"));

%>

<style type="text/css">
	.contactHPtheme {
		background: <%=headerTitleBgColor%>;
	}
	.contactHPthemeheader {
		color: #FFFFFF; 
		font-weight:bold;
	}
	.sppPersonalBlue {
		background-color: #0066FF;
		color: #FFFFFF;
		font-weight: bold;
		font-size: small;
		text-align: left;
		text-decoration: none;
		padding-left:7px;
		padding-right:7px;
		padding-top:1px;
		padding-bottom:1px;
	}
	.sppPersonalRed {
		background-color: #CC0000;
		color: #FFFFFF;
		font-weight: bold;
		font-size: small;
		text-align: left;
		text-decoration: none;
		padding-left:7px;
		padding-right:7px;
		padding-top:1px;
		padding-bottom:1px;
	}
	.sppPersonalGrey {
		background-color: #E7E7E7;
		text-align: left;
		text-decoration: none;
		padding-left:7px;
		padding-right:7px;
		padding-top:3px;
		padding-bottom:3px;
	}
	.sppPersonalWhite {
		background-color: #FFFFFF;
		text-align: left;
		text-decoration: none;
		padding-left:7px;
		padding-right:7px;
		padding-top:3px;
		padding-bottom:3px;
	}
</style>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr class="contactHPtheme">
<% if(!"".equals(headerTitle)) {%>
		<td style="padding-left:10px;padding-right:10px;padding-top:2px;padding-bottom:2px;"><span class="contactHPthemeheader"><%=headerTitle%></span></td>
<%}else{%>
		<td style="padding-left:10px;padding-right:10px;padding-top:2px;padding-bottom:2px;"><span class="contactHPthemeheader"><%=portalContext.getCurrentSecondaryPage().getTitle()%></span></td>
<%}%>
	</tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<%if (!headerDescription.equals("")) {%>
	<tr class="<%if ("".equals(headerDescriptionBgColor)) {%>colorDCDCDCbg<%}else{ %><%=headerDescriptionBgColor%><%}%>">
		<td style="padding-left:8px;padding-top:8px;text-align:left;vertical-align:bottom;"><%=headerDescription.replaceAll("\r\n", "<br>").replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")%></td>
	</tr>
	<tr class="<%if ("".equals(headerDescriptionBgColor)) {%>colorDCDCDCbg<%}else{ %><%=headerDescriptionBgColor%><%}%>">
		<td style="height:4px;"><spacer type="block" width="1" height="1" /></td>
	</tr>
	<tr>
		<td bgcolor="#FFFFFF" style="height:3px;"><spacer type="block" width="1" height="1" /></td>
	</tr>
<%}else{%>
	<tr>
		<td bgcolor="#FFFFFF" style="height:6px;"><spacer type="block" width="1" height="1" /></td>
	</tr>
<%}%>
</table>	

<% if(request.getAttribute("forward") != null 
		&& request.getAttribute("faults") != null 
		|| request.getAttribute("forward") == null 
		&& request.getAttribute("faults") == null) { %>
<form action="<%=portalContext.createTemplateProcessURI()%>" method="post" name="contact_hp_form" style="display: inline; margin: 0px">

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td style="text-align:left;vertical-align:top" width="100%">
		
<% if(request.getAttribute("faults") != null) { %>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td class="spSesamePersonalWhite" style="color:#FF0000;font-weight: bold;">
			
			<label for="error_message">
				<vgn-portal:i18nValue stringID="<%=i18nID%>" key="error_message" defaultValue="Please correct the errors below highlighted with red text." />
				<%
				ArrayList faults = (ArrayList)request.getAttribute("faults") ;
				
				for(int i = 0; i < faults.size(); i++)
				{
					Fault fault = (Fault)faults.get(i) ;

					out.println("<!-- "+fault.getFieldName()+" : "+fault.getDescription()+" -->") ;
					
					// HPP fields
					if(fault.getFieldName().equals("email") && !faultsMap.containsKey("email_address"))
					{
						faultsMap.put("email_address", fault.getDescription()) ;
					}
					else if(fault.getFieldName().equals("topic") && !faultsMap.containsKey("email_topic"))
					{
						faultsMap.put("email_topic", fault.getDescription()) ;
					}
					else if(fault.getFieldName().equals("emailMessage") && !faultsMap.containsKey("email_message"))
					{
						faultsMap.put("email_message", fault.getDescription()) ;
					}
					out.println("<!-- "+faultsMap.size()+" -->") ;
				}
				%>
			</label>
		</td>
	</tr>
</table>
<%}else{%>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td bgcolor="#FFFFFF" style="height:10px;"><spacer type="block" width="1" height="1" /></td>
	</tr>
</table>
<% } %>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td class="sppPersonalWhite" width="33%">
			<label for="email_topic">
				<vgn-portal:i18nValue stringID="<%=i18nID%>" key="email_topic" defaultValue="Topic" />
			</label>
		</td>
		<td class="sppPersonalWhite" width="66%">
			<select class="textBox18" name="email_topic" >
				<option value="<%=""+Integer.MAX_VALUE %>"></option>
				<% 
					ArrayList subjectMap = (ArrayList) request.getAttribute("subjectMap") ;
					if(subjectMap != null) 
					{
						Iterator iter = subjectMap.iterator() ;
						while(iter.hasNext()) 
						{ 
							ArrayList row = (ArrayList)iter.next() ;
							String key = row.get(0).toString() ;
							String value = row.get(1).toString() ;
							String selected = (request.getParameter("email_topic") != null && request.getParameter("email_topic").equals(key))?" selected":"" ;
				%>
				<option value="<%=key%>"<%=selected%>><%=value%></option>
				<% 
						}
					} 
				%>
			</select>
		</td>
	</tr>
	<% if(faultsMap.containsKey("email_topic")) { %>
	<tr>
		<td>&nbsp;</td>
		<td class="sppPersonalWhite" style="color:#FF0000;font-weight: bold;">
			<%=(String)faultsMap.get("email_topic")%>
		</td>
	</tr>
	<% } %>
	<tr>
		<td class="sppPersonalWhite" width="33%">
			<label for="email_address">
				<vgn-portal:i18nValue stringID="<%=i18nID%>" key="email_address" defaultValue="Your email" />
			</label>
		</td>
		<td class="sppPersonalWhite" width="66%"><input type="text" size="53" class="textBox18" name="email_address" value="<%=(request.getParameter("email_address") != null)?request.getParameter("email_address"):empty%>" /></td>
	</tr>
	<% if(faultsMap.containsKey("email_address")) { %>
	<tr>
		<td>&nbsp;</td>
		<td class="sppPersonalWhite" style="color:#FF0000;font-weight: bold;">
			<%=(String)faultsMap.get("email_address")%>
		</td>
	</tr>
	<% } %>
	<tr>
		<td class="sppPersonalWhite" width="33%">
			<label for="email_message">
				<vgn-portal:i18nValue stringID="<%=i18nID%>" key="email_message" defaultValue="Your email Message" />
			</label>
		</td>
		<td class="sppPersonalWhite" width="66%">
		<textarea rows="7" cols="42" class="textBox18" name="email_message"><%=emailMessage != null?emailMessage:empty%></textarea>
		</td>
	</tr>
	<% if(faultsMap.containsKey("email_message")) { %>
	<tr>
		<td>&nbsp;</td>
		<td class="sppPersonalWhite" style="color:#FF0000;font-weight: bold;">
			<%=(String)faultsMap.get("email_message")%>
		</td>
	</tr>
	<% } %>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td class="sppPersonalWhite" style="color:#FF0000;font-weight: bold;">
			&nbsp; <!-- White space -->
		</td>
	</tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td class="sppPersonalWhite" style="text-align:right;">
			<vgn-portal:i18nElement><input type="submit" name="submit" value="<vgn-portal:i18nValue stringID="<%=i18nID%>" key="submitLabel" defaultValue="Send" /> &raquo;" class="primButton" /></vgn-portal:i18nElement>
		</td>
	</tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td class="sppPersonalWhite" style="color:#FF0000;font-weight: bold;">
			&nbsp; <!-- White space -->
		</td>
	</tr>
</table>

		</td>
	</tr>
</table>
</form>
<% } %>
