<%@ include file="/WEB-INF/jsp/include.jsp" %>

<style type="text/css">
.sppSectionHeader
{
	border-style: solid;
	border-width: 0px; 
	border-bottom-width: 5px;
	border-color: #FF6600;
}
</style>

<h1>Confirm Group definition Update(s)</h1>



<%

com.hp.spp.portlets.groups.XMLUpload xmlUpload = (com.hp.spp.portlets.groups.XMLUpload) pageContext
.findAttribute("xmlUpload");

//out.print(xmlUpload.getFile());
//out.print(xmlUpload.getUpdatedGroupMap());


%>

<p class="sppSectionHeader">The following group definition(s) will be updated or created</p>

<table border="1" cellspacing="0">
<%
	
java.util.List list = xmlUpload.getUpdatedGroupMap();
for (int i=0; i<list.size(); i++)
{
	com.hp.spp.groups.GroupStatus status = (com.hp.spp.groups.GroupStatus) list.get(i);
	String statusString = "<td width=\"150\" bgcolor=\"#FF66CC\">New Group defintion</td>";
	if (status.getExistingFlag()==1)
	{
		statusString = "<td width=\"150\" bgcolor=\"#66CCFF\">Updated</td>";
	}
	out.print("<tr><td width=\"250\">"+status.getMGroupName()+"</td>"+statusString+"</tr>");
}
%>
</table>

<br>

<portlet:actionURL var="actionConfirmUrl">
	<portlet:param name="action" value="saveImportGroup" />
</portlet:actionURL>

<%String actionConfirmUpdateUrl = String.valueOf(pageContext.findAttribute("actionConfirmUrl"));%>

<portlet:renderURL var="actionCancelUrl">
</portlet:renderURL>

<%String actionCancelUpdateUrl = String.valueOf(pageContext.findAttribute("actionCancelUrl"));%>

<script type="text/javascript">
<!--

/*
 *	Checks if the field is null, empty or filled with blank spaces.
 */
function isFieldBlank(field)
{
	if (field == null)	
	{
		return true;
	}
	
	var length = field.length;
		
	for (var i=0;i<length;i++)
	{
		if (field.charAt(i) != ' ')
		{
			return false;
		}
	}
	return true;
}

function disableButtons(disable)
{
	document.forms["updateGroup"].updateButton.disabled=disable;
	document.forms["updateGroup"].cancelButton.disabled=disable;
}

function updateGroupConfirm()
{
	var owner = document.forms["updateGroup"].changeOwner.value;
	var comment = document.forms["updateGroup"].comment.value;
	if (isFieldBlank(owner))
	{
		alert("Please enter the change owner");
	}
	else if (isFieldBlank(comment))
	{
		alert("Please enter a comment");
	}
	else
	{
		document.forms["updateGroup"].action='<%=actionConfirmUpdateUrl%>';
		disableButtons(true);
		document.forms["updateGroup"].submit();
	}
}

function updateGroupCancel()
{
	document.forms["updateGroup"].action='<%=actionCancelUpdateUrl%>';
	disableButtons(true);
	document.forms["updateGroup"].submit();
}

//-->
</script>

<p>Please enter the following information, and press "Confirm change"
button to edit/create the above group definition.
<form name="updateGroup" method="POST">
<table>
	<tr>
		<td>Change owner :</td>
		<td><input name="changeOwner" type="text" size="50"></td>
	</tr>
	<tr>
		<td>Comment :</td>
		<td><textarea name="comment" rows="4" cols="60"></textarea></td>
	</tr>
</table>

<input type="button" name="updateButton" value="Confirm Change" onClick="updateGroupConfirm()" class="secButton"> <input type="button"
	name="cancelButton" value="Cancel" onClick="updateGroupCancel()" class="secButton"></form>
