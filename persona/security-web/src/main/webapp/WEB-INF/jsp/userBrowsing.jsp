<%@ include file="include.jsp"%>
<script type="text/javascript">
	function takeAction(activityValue, userIdentifier) {
	    document.getElementById("activity").value = activityValue;
	    if (userIdentifier) {
	        document.getElementById("userIdentifier").value = userIdentifier;
	    }
	    document.getElementById("personaForm").submit();
	}
    function deleteSimple(deletedInstanceIdentifier) {
        document.getElementById("deletedInstanceIdentifier").value = deletedInstanceIdentifier;
        takeAction('delete');
    }
</script>
<table class="personaTable">
	<tr>
		<td width="200" valign="top" bgcolor="#efefef"><%@ include
			file="leftMenu.jsp"%></td>
		<td valign="top"><%@ include file="errorPane.jsp"%>
		<form id="personaForm" method="post"
			action="<portlet:actionURL><portlet:param name="action" value="simpleContentManagement" /></portlet:actionURL>">
		<table cellspacing="2" cellpadding="2" class="contentTable">
			<tr>
				<th colspan="3" align="center">User Attributes</th>
			</tr>
			<tr>
				<td colspan="3">User id: <input name="userIdentifier" id="userIdentifier"
					value="<c:out value="${userIdentifier}" />" />&nbsp;in&nbsp; <select
					name="userIdentifierTypeCode">
					<c:forEach items="${userIdentifierTypes}" var="entry">
						<option value="<c:out value="${entry.userIdentifierTypeCode}" />"
							<c:if test="${entry.userIdentifierTypeCode == userIdentifierTypeCode}">
						selected="true"
						</c:if>><c:out
							value="${entry.userIdentifierName}" /></option>
					</c:forEach>
				</select>&nbsp;type&nbsp; <input type="hidden" name="activity" id="activity" />
				<input type="button" class="personaButton" value="query"
					onclick="takeAction('query')" />&nbsp;<c:if test="${isAdmin}">
					<input class="personaButton" type="button"
						value="Modify Compound Attribute Values"
						onClick="takeAction('jumpEditCompound')" />
					<input type="hidden" name="deletedInstanceIdentifier"
						id="deletedInstanceIdentifier" />
				</c:if><c:if test="${!isAdmin}">
					<input class="personaButton" type="button"
						value="View Compound Attribute Values"
						onClick="takeAction('jumpEditCompound')" />
				</c:if></td>
			</tr>
		</table>
		<c:if test="${userIdentifier != null && userIdentifier != ''}">
			<table cellspacing="2" cellpadding="3" class="contentTable">
				<tr>
					<th colspan="4" align="center">Simple Attributes</th>
				</tr>
				<c:if test="${isAdmin}">
					<tr>
						<td colspan="4"><input class="personaButton" type="button"
							value="Add Simple Attribute to Current User"
							onClick="takeAction('jumpAddSimple')" /><input
							class="personaButton" type="button"
							value="Save Simple Attributes" onClick="takeAction('save')" /></td>
					</tr>
				</c:if>
				<tr>
					<th>Simple Attribute Name</th>
					<th>Simple Attribute Id</th>
					<th>Value</th>
					<c:if test="${isAdmin}">
						<th>Action</th>
					</c:if>
				</tr>
				<c:forEach items="${attributeList}" var="entry">
					<tr>
						<td><c:out value="${entry.simpleUserAttributeName}" /></td>
						<td><c:out value="${entry.simpleUserAttributeIdentifier}" /></td>
						<c:if test="${isAdmin}">
							<td><input
								name="simple_<c:out value="${entry.simpleUserAttributeIdentifier}" />__<c:out value="${entry.instanceIdentifier}" />"
								value="<c:out value="${entry.value}" />" /></td>
							<td><input class="personaButton" type="button"
								value="delete"
								onClick="deleteSimple('<c:out value="${entry.instanceIdentifier}" />')" /></td>
						</c:if>
						<c:if test="${!isAdmin}">
							<td><c:out value="${entry.value}" /></td>
						</c:if>
					</tr>
				</c:forEach>
			</table>
		</c:if></form>
		</td>
	</tr>
</table>
