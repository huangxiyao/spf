<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="actionConfirmUrl">
	<portlet:param name="action" value="saveImportStandardParameterSet" />
</portlet:actionURL>

<portlet:renderURL var="actionCancelUrl">
	<portlet:param name="action" value="standardParameterSets" />
</portlet:renderURL>

<script type="text/javascript">

function disableButtons(disable)
{
		document.forms["updateStandardParameterSet"].confirmButton.disabled=disable;
		document.forms["updateStandardParameterSet"].cancelButton.disabled=disable;
}

function updateStandardParameterSetConfirm()
{
	if (document.forms["updateStandardParameterSet"].changeOwner.value=="")
	{
		alert("Please enter the change owner");
	}
	else if (document.forms["updateStandardParameterSet"].comment.value=="")
	{
		alert("Please enter a comment");
	}
	else
	{
		document.forms["updateStandardParameterSet"].action='<c:out value="${actionConfirmUrl}" escapeXml="false"/>';
		disableButtons(true);
		document.forms["updateStandardParameterSet"].submit();
	}
}

function updateStandardParameterSetCancel()
{
	document.forms["updateStandardParameterSet"].action='<c:out value="${actionCancelUrl}" escapeXml="false"/>';
	disableButtons(true);
	document.forms["updateStandardParameterSet"].submit();
}

</script>

<h1>Confirm parameter set definition Update(s)</h1>

<p>The following standardParameterSet definition(s) will be updated or created</p>

<table border="1" cellspacing="0">

<c:forEach items="${xmlUpload.updatedStandardParameterSetMap}" var="status">
	
	<tr>
		<td width="250">
			<c:out value="${status.standardParameterSet.name}"/>
		</td>
		<c:choose>
			<c:when test="${status.existingFlag == 1}">
				<td width="150" bgcolor="#FF66CC">Updated
			</c:when>
			<c:otherwise>
				<td width="150" bgcolor="#FF66CC">New parameter set defintion
			</c:otherwise>
		</c:choose>
		</td>
	</tr>
	
</c:forEach>

</table>

<br>

<p>Please enter the following information, and press "Confirm change"
button to edit/create the above parameter set definition.
<form name="updateStandardParameterSet" method="POST">
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

<input type="button" name="confirmButton" value="Confirm Change" onClick="updateStandardParameterSetConfirm()" class="secButton"> 
<input type="button" name="cancelButton" value="Cancel" onClick="updateStandardParameterSetCancel()" class="secButton">
</form>
