<%@ include file="/WEB-INF/jsp/include.jsp"%>

<style type="text/css">
.sppSectionHeader
{
	border-style: solid;
	border-width: 0px; 
	border-bottom-width: 5px;
	border-color: #FF6600;
}
</style>

<SCRIPT LANGUAGE="JavaScript">
<!--

function disableButtons(disable)
{
		document.forms["deleteExport"].deleteButton.disabled=disable;
		document.forms["deleteExport"].exportButton.disabled=disable;
		document.forms["importGroup"].importButton.disabled=disable;
}

function selectAll(bool)
{
        var inputs = document.getElementsByTagName("input");
        for (var cpt = 0 ; cpt < inputs.length ; cpt++)
        {
                if ((inputs[cpt].type == "checkbox") && (inputs[cpt].name == "groupId"))
                        inputs[cpt].checked = bool;
        }
}

function importSSPGroup()
{
	if (document.forms["importGroup"].file.value=="")
	{
		alert("Please enter an XML Content");
	}
	else
	{
		disableButtons(true);
		document.forms["importGroup"].submit();
	}
}


-->
</SCRIPT>

<%//get the name of the site from the passing parameters
			String siteName = null;

			java.util.HashMap UseContextKeys = (java.util.HashMap) request
					.getAttribute(com.hp.spp.common.util.SppConstants.SPP_USERCONTEXTKEYS_MAP_NAME);
			if ((UseContextKeys != null)
					&& (UseContextKeys
							.get(com.hp.spp.common.util.SppConstants.SPP_USERCONTEXTKEYS_KEY_SITE_NAME) != null)) {
				siteName = (String) UseContextKeys
						.get(com.hp.spp.common.util.SppConstants.SPP_USERCONTEXTKEYS_KEY_SITE_NAME);
			}

			%>


<portlet:defineObjects />


<%// get the confirmation message from the session, if it s not null, display it nd remove it from the session
			// else, write nothing

			String confirmationMessage = (String) session
					.getAttribute("confirmationMessage");

			/*
			 java.util.Enumeration enume = session.getAttributeNames();
			 while (enume.hasMoreElements())
			 {
			 String key = (String) enume.nextElement();
			 out.print(key +" : "+session.getAttribute(key) +"<br>");
			 }
			 */
			if (confirmationMessage != null) {
				out.print("<br><h2><font color=\"blue\">" + confirmationMessage
						+ "</font></h2>");
				request.getSession().removeAttribute("confirmationMessage");
			}

			%>

<p class="sppSectionHeader"><b>Import Group definitions :</b></p>
<!--
<p><i>Please select the XML file you would like to upload, and press the
"Import Group Definition" button.</i></p>
-->
<p><i>Please copy the XML content you would like to upload, paste it in
the textarea below, and press the "Import Group Definition" button.</i></p>

<portlet:actionURL var="actionGroupImportUrl">
	<portlet:param name="action" value="importGroup" />
</portlet:actionURL>

<%String actionSPPGroupImportUrl = String.valueOf(pageContext
					.findAttribute("actionGroupImportUrl"));%>
<!-- 
<form method="POST" action="<%=actionSPPGroupImportUrl%>" enctype="multipart/form-data"><input
	type="file" name="file">
-->
<form method="POST" name="importGroup"
	action="<%=actionSPPGroupImportUrl%>"><textarea name="file" cols="70"
	rows="15"></textarea> <br>
<br>
<input type="button" name="importButton" value="Import Group Definition"
	onClick="importSSPGroup()" class="secButton"> <!-- <input type="submit" value="Import Group Definition">-->
</form>

<p></p>

<portlet:actionURL var="actionGroupDeleteUrl">
	<portlet:param name="action" value="deleteGroup" />
</portlet:actionURL>

<%String actionSPPGroupDeleteUrl = String.valueOf(pageContext
					.findAttribute("actionGroupDeleteUrl"));%>

<portlet:actionURL var="actionGroupExportUrl">
	<portlet:param name="action" value="exportGroup" />
</portlet:actionURL>

<%String actionSPPGroupExportUrl = String.valueOf(pageContext
					.findAttribute("actionGroupExportUrl"));%>

<script type="text/javascript">
<!--

function deleteGroup()
{
	if (!checkOneChecked())
	{
		alert("Please select at least one group to delete");
	}
	else
	{
		document.forms["deleteExport"].action='<%=actionSPPGroupDeleteUrl%>';
		disableButtons(true);
		document.forms["deleteExport"].submit();
	}
}

function exportGroup()
{
	if (!checkOneChecked())
	{
		alert("Please select at least one group to export");
	}
	else
	{
		document.forms["deleteExport"].action='<%=actionSPPGroupExportUrl%>';
		disableButtons(true);
		document.forms["deleteExport"].submit();
	}
}

function isArray(obj)
{
	if (obj == null)
	{
		return false;
	}
	else
	{
		return(typeof(obj.length)=="undefined")?false:true;
	}
}


function checkOneChecked()
{
	var totalChecked = 0;
	if (isArray(document.forms["deleteExport"].groupId))
	{
		for ( i=0; i < document.forms["deleteExport"].groupId.length; i++ ) 
		{
	    	if ( document.forms["deleteExport"].groupId[i].checked == true ) {
	    	  	totalChecked++;
			}
		}
	}
	else
	{
		if ( (document.forms["deleteExport"].groupId != null) && (document.forms["deleteExport"].groupId.checked == true) )
		{
			totalChecked = 1;
		}
	}	
	if (totalChecked==0)
	{
		return false;
	}
	else
	{
		return true;
	}	
}

function resetCache()
{
	var argv = resetCache.arguments;
	for (var i=0; i<argv.length; i++) {
		alert("resetCache : " + argv[i]);
	}
	my_window = window.open('http://prmnt20.bbn.hp.com:27005/spp-services-web/jsp/Hello.jsp');	
	my_window.close();	
		alert("resetCache : " + argv[i]);
}

function popup(mylink, windowname)
{
	if (! window.focus)return true;
	var href;
	if (typeof(mylink) == 'string')
   		href=mylink;
	else
		href=mylink.href;
	window.open(href, windowname, 'width=400,height=200,scrollbars=yes');
return false;
}



//-->
</script>

<p class="sppSectionHeader"><b>Export/Delete Group definitions:</b></p>
<p><i>Please select the group(s) you would like to export or delete, and
press the "Export Group definition" or "Delete Group definition" button.</i></p>

<form name="deleteExport" method="POST"><br>
<a onclick="selectAll(true);">Select all</a> / <a
	onclick="selectAll(false);">Unselect all</a> <br>
<table border="1" cellspacing="0">
	<tr>
		<td>&nbsp;</td>
		<td width="250">Name</td>
		<td>Create Date <br>
		<i>(mm/dd/yyyy)</i></td>
		<td>Last update <br>
		<i>(mm/dd/yyyy)</i></td>
	</tr>

	<%java.util.HashMap model = (java.util.HashMap) pageContext
					.findAttribute("model");

			com.hp.spp.common.util.format.Formatter formatter = new com.hp.spp.common.util.format.Formatter();

			java.util.Set groupList = (java.util.Set) model.get("groups");
			java.util.Iterator it = groupList.iterator();
			while (it.hasNext()) {
				com.hp.spp.groups.Group g = (com.hp.spp.groups.Group) it.next();

				out
						.print("<tr><td><input type=\"checkbox\" name=\"groupId\" value=\""
								+ g.getId()
								+ "\"></td><td>"
								+ g.getName()
								+ "</td><td>"
								+ formatter.dateToString(g.getCreationDate())
								+ "</td><td>"
								+ formatter.dateToString(g
										.getModificationDate()) + "</td></tr>");

			}

			%>

</table>
<br>
<input type="button" name="exportButton" value="Export Group definition"
	onClick="exportGroup()" class="secButton"> <input type="button"
	name="deleteButton" value="Delete Group definition"
	onClick="deleteGroup()" class="secButton"></form>

<p class="sppSectionHeader"><b>View Group definition change history :</b>
</p>

<portlet:renderURL var="historyUrl">
	<portlet:param name="action" value="history" />
</portlet:renderURL>

<%String historySPPUrl = String.valueOf(pageContext
					.findAttribute("historyUrl"));%>

<p><i><a href="<%=historySPPUrl %>">Access group definition change
history and associated configuration backups.</a></i> <br>