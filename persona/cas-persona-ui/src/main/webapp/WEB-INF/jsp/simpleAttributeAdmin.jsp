<%@ include file="include.jsp"%>
<script type="text/javascript">
    function takeAction(activityValue, userAttributeIdentifier) {
        document.getElementById("activity").value = activityValue;
        if (userAttributeIdentifier) {
            document.getElementById("userAttributeIdentifier").value = userAttributeIdentifier;
        }
        document.getElementById("personaForm").submit();
    }
</script>
<table class="personaTable">
	<tr>
		<td width="200" valign="top" bgcolor="#efefef" nowrap><%@ include
			file="leftMenu.jsp"%></td>
		<td valign="top"><%@ include file="errorPane.jsp"%>
		<form id="personaForm" method="post"
			action="<portlet:actionURL><portlet:param name="action" value="simpleAttribute_configuration" /></portlet:actionURL>">
		<table cellspacing="2" cellpadding="2" class="contentTable">
			<tr>
				<th colspan="4" align="center">Simple Attributes</th>
			</tr>
			<tr>
				<td colspan="4"><input type="hidden" name="activity"
					id="activity" />Simple Attribute Id / Name: <input
					name="userAttributeIdentifier" id="userAttributeIdentifier"
					value="<c:out value="${userAttributeIdentifier}" />" />&nbsp; <input
					type="radio" name="searchType" value="ByIdentifier"
					<c:if test="${searchType != 'ByName'}">checked</c:if>>By
				Id&nbsp; <input type="radio" name="searchType" value="ByName"
					<c:if test="${searchType == 'ByName'}">checked</c:if>>By
				Name&nbsp; <input type="button" class="personaButton" value="query"
					onclick="takeAction('query')" />&nbsp; <c:if test="${isAdmin}">
					<input type="button" class="personaButton" value="Add"
						onclick="takeAction('add')" />
				</c:if></td>
			</tr>
			<c:if test="${attributeList != null}">
				<tr>
					<th>Id</th>
					<th>Name</th>
					<th>Description</th>
					<c:if test="${isAdmin}">
						<th>Action</th>
					</c:if>
				</tr>
				<c:forEach items="${attributeList}" var="entry">
					<tr>
						<td><c:out value="${entry.userAttributeIdentifier}" /></td>
						<td><c:out value="${entry.userAttributeName}" /></td>
						<td><c:out value="${entry.userAttributeDescription}" /></td>
						<c:if test="${isAdmin}">
							<td><input type="button" class="personaButton" value="edit"
								onClick="takeAction('edit', '<c:out value="${entry.userAttributeIdentifier}" />')" />&nbsp;
							<input type="button" class="personaButton" value="delete"
								onClick="takeAction('delete', '<c:out value="${entry.userAttributeIdentifier}" />')" />
							</td>
						</c:if>
					</tr>
				</c:forEach>
			</c:if>
		</table>
		</form>
		</td>
	</tr>
</table>
