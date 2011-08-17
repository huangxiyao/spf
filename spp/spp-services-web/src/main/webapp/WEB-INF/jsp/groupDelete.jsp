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

<h1>Confirm Group definition delete</h1>

<%

com.hp.spp.portlets.groups.GroupList groupList = (com.hp.spp.portlets.groups.GroupList) pageContext
.findAttribute("groupList");

%>

<p class="sppSectionHeader">The following group(s) will be deleted :</p>
<ul>
	<%
	
	for (int i=0; i<groupList.getGroupNameList().size(); i++)
	{
		out.print("<li>"+groupList.getGroupNameList().get(i)+"</li>");
	}
	
	%>
</ul>

<portlet:actionURL var="actionConfirmUrl">
	<portlet:param name="action" value="saveDeleteGroup" />
</portlet:actionURL>

<%String actionConfirmDeleteUrl = String.valueOf(pageContext.findAttribute("actionConfirmUrl"));%>

<portlet:renderURL var="actionCancelUrl">
</portlet:renderURL>

<%String actionCancelDeleteUrl = String.valueOf(pageContext.findAttribute("actionCancelUrl"));%>

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
		document.forms["deleteGroup"].deleteButton.disabled=disable;
		document.forms["deleteGroup"].cancelButton.disabled=disable;
}

function deleteGroupConfirm()
{
	var owner = document.forms["deleteGroup"].changeOwner.value;
	var comment = document.forms["deleteGroup"].comment.value;
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
		document.forms["deleteGroup"].action='<%=actionConfirmDeleteUrl%>';
		disableButtons(true);
		document.forms["deleteGroup"].submit();
	}
}

function deleteGroupCancel()
{
	document.forms["deleteGroup"].action='<%=actionCancelDeleteUrl%>';
	disableButtons(true);
	document.forms["deleteGroup"].submit();
}

//-->
</script>



<p>Please enter the following information, and press "Confirm Delete"
button to delete the group.
<form name="deleteGroup" method="POST">
<table>
	<tr>
		<td>Change owner :</td>
		<td><input type="text" name="changeOwner" size="50"></td>
	</tr>
	<tr>
		<td>Comment :</td>
		<td><textarea name="comment" rows="4" cols="60"></textarea></td>
	</tr>
</table>

<input type="button" name="deleteButton" value="Confirm Delete" onClick="deleteGroupConfirm()" class="secButton"> <input type="button"
	name="cancelButton" value="Cancel" onClick="deleteGroupCancel()" class="secButton"></form>

