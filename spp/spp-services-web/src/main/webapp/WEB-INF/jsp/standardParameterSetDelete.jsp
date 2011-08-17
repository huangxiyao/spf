<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>

<portlet:actionURL var="actionConfirmUrl">
	<portlet:param name="action" value="saveDeleteStandardParameterSet" />
</portlet:actionURL>

<portlet:renderURL var="actionCancelUrl">
	<portlet:param name="action" value="standardParameterSets" />
</portlet:renderURL>

<script type="text/javascript">

function disableButtons(disable)
{
		document.forms["deleteStandardParameterSet"].deleteButton.disabled=disable;
		document.forms["deleteStandardParameterSet"].cancelButton.disabled=disable;
}

function deleteGroupConfirm()
{
	if (document.forms["deleteStandardParameterSet"].changeOwner.value=="")
	{
		alert("Please enter the change owner");
	}
	else if (document.forms["deleteStandardParameterSet"].comment.value=="")
	{
		alert("Please enter a comment");
	}
	else
	{
		document.forms["deleteStandardParameterSet"].action='<c:out value="${actionConfirmUrl}" escapeXml="false"/>';
		disableButtons(true);
		document.forms["deleteStandardParameterSet"].submit();
	}
}

function updateGroupCancel()
{
	document.forms["deleteStandardParameterSet"].action='<c:out value="${actionCancelUrl}" escapeXml="false"/>';
	disableButtons(true);
	document.forms["deleteStandardParameterSet"].submit();
}

</script>

<h1>Confirm parameter set definition delete</h1>

<p>The following parameter set(s) will be deleted :</p>

<ul>	
	<c:forEach items="${standardParameterSetsList.standardParameterSetNameList}" var="standardParameterSetName">
		<li><c:out value="${standardParameterSetName}"/></li>
	</c:forEach>
</ul>

<p>Please enter the following information, and press "Confirm Delete"
button to delete the parameter set.
<form name="deleteStandardParameterSet" method="POST">
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

