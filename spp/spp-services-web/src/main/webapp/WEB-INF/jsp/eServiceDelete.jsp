<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="actionConfirmUrl">
	<portlet:param name="action" value="saveDeleteEService" />
</portlet:actionURL>

<portlet:renderURL var="actionCancelUrl">
</portlet:renderURL>

<script type="text/javascript">

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
		document.forms["deleteEService"].deleteButton.disabled=disable;
		document.forms["deleteEService"].cancelButton.disabled=disable;
}

function deleteGroupConfirm()
{
	var owner = document.forms["deleteEService"].changeOwner.value;
	var comment = document.forms["deleteEService"].comment.value;
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
		document.forms["deleteEService"].action='<c:out value="${actionConfirmUrl}" escapeXml="false"/>';
		disableButtons(true);
		document.forms["deleteEService"].submit();
	}
}

function updateGroupCancel()
{
	document.forms["deleteEService"].action='<c:out value="${actionCancelUrl}" escapeXml="false"/>';
	disableButtons(true);
	document.forms["deleteEService"].submit();
}

</script>

<h1>Confirm eService definition delete</h1>

<p>The following eService(s) will be deleted :</p>

<ul>	
	<c:forEach items="${eServicesList.EServiceNameList}" var="eServiceName">
		<li><c:out value="${eServiceName}"/></li>
	</c:forEach>
</ul>

<p>Please enter the following information, and press "Confirm Delete"
button to delete the eService.
<form name="deleteEService" method="POST">
<table>
	<tr>
		<td>Change owner:</td>
		<td><input type="text" name="changeOwner" size="50"></td>
	</tr>
	<tr>
		<td>Comment:</td>
		<td><textarea name="comment" rows="4" cols="60"></textarea></td>
	</tr>
</table>

<input type="button" name="deleteButton" value="Confirm Delete" onclick="deleteGroupConfirm()" class="secButton"> 
<input type="button" name="cancelButton" value="Cancel" onclick="updateGroupCancel()" class="secButton">

</form>

