<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="actionConfirmUrl">
	<portlet:param name="action" value="saveImportEService" />
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
		document.forms["updateEService"].confirmButton.disabled=disable;
		document.forms["updateEService"].cancelButton.disabled=disable;
}

function updateEServiceConfirm()
{
	var owner = document.forms["updateEService"].changeOwner.value;
	var comment = document.forms["updateEService"].comment.value;
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
		document.forms["updateEService"].action='<c:out value="${actionConfirmUrl}" escapeXml="false"/>';
		disableButtons(true);
		document.forms["updateEService"].submit();
	}
}

function updateEServiceCancel()
{
	document.forms["updateEService"].action='<c:out value="${actionCancelUrl}" escapeXml="false"/>';
	disableButtons(true);
	document.forms["updateEService"].submit();
}

</script>

<h1>Confirm eService definition Update(s)</h1>

<p>The following eService definition(s) will be updated or created</p>

<table border="1" cellspacing="0">

<c:forEach items="${xmlUpload.updatedEServiceMap}" var="status">
	
	<tr>
		<td width="250">
			<c:out value="${status.EService.name}"/>
		</td>
		<c:choose>
			<c:when test="${status.existingFlag == 1}">
				<td width="150" bgcolor="#FF66CC">Updated
			</c:when>
			<c:otherwise>
				<td width="150" bgcolor="#FF66CC">New eService defintion
			</c:otherwise>
		</c:choose>
		</td>
	</tr>
	
</c:forEach>

</table>

<br>

<p>Please enter the following information, and press "Confirm change"
button to edit/create the above eService definition.
<form name="updateEService" method="POST">
<table>
	<tr>
		<td>Change owner:</td>
		<td><input name="changeOwner" type="text" size="50"></td>
	</tr>
	<tr>
		<td>Comment:</td>
		<td><textarea name="comment" rows="4" cols="60"></textarea></td>
	</tr>
</table>

<input type="button" name="confirmButton" value="Confirm Change" onClick="updateEServiceConfirm()" class="secButton"> 
<input type="button" name="cancelButton" value="Cancel" onClick="updateEServiceCancel()" class="secButton">
</form>
