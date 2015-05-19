<%@ include file="include.jsp"%>
<script type="text/javascript">
    function takeAction(activityValue) {
        document.getElementById("activity").value = activityValue;
        document.getElementById("personaForm").submit();
    }
    function deleteSimple(deletedUserAttributeIdentifier) {
        document.getElementById("simpleAttributeIdentifier").value = deletedUserAttributeIdentifier;
        takeAction("simpledelete");
    }
</script>
<table class="personaTable">
	<tr>
		<td width="200" valign="top" bgcolor="#efefef"><%@ include
			file="leftMenu.jsp"%></td>
		<td valign="top"><%@ include file="errorPane.jsp"%>
		<form id="personaForm" method="post"
			action="<portlet:actionURL><portlet:param name="action" value="securityManagement" /></portlet:actionURL>">
		<table cellspacing="3" cellpadding="2" class="contentTable">
			<tr>
				<td>Identifier:<c:out value="${applicationPortfolioIdentifier}" /></td>
				<td nowrap><c:if test="${isAdmin}">Available Simple Attributes: <select
						name="grantSimpleUserAttributeIdentifier">
						<c:forEach items="${avaliableSimpleAttributeList}" var="entry">
							<option value="<c:out value="${entry.userAttributeIdentifier}"/>">
							<c:out value="${entry.userAttributeName}" /></option>
						</c:forEach>
					</select>
				</c:if></td>
				<td><input type="hidden" name="activity" id="activity" /><c:if
					test="${isAdmin}">
					<input class="personaButton" type="button" value="grant"
						onClick="takeAction('simplegrant')" />
				</c:if> <input class="personaButton" type="button" value="back"
					onClick="takeAction('query')" /> <input type="hidden"
					name="simpleAttributeIdentifier" id="simpleAttributeIdentifier" /><input type="hidden"
					name="applicationPortfolioIdentifier"
					value="<c:out value="${applicationPortfolioIdentifier}"/>" /></td>
			</tr>
		</table>
		<table cellspacing="3" cellpadding="2" class="contentTable">
			<tr>
				<th colspan="3" align="center">Simple Attribute Permissions</th>
			</tr>
			<tr>
				<th>Id</th>
				<th>Name</th>
				<c:if test="${isAdmin}">
					<th>Action</th>
				</c:if>
			</tr>
			<c:forEach items="${permissionList}" var="entry">
				<tr>
					<td><c:out
						value="${entry.simpleUserAttribute.userAttributeIdentifier}" /></td>
					<td><c:out
						value="${entry.simpleUserAttribute.userAttributeName}" /></td>
					<c:if test="${isAdmin}">
						<td><input class="personaButton" type="button" value="delete"
							onClick="deleteSimple('<c:out value="${entry.simpleUserAttribute.userAttributeIdentifier}"/>')" /></td>
					</c:if>
				</tr>
			</c:forEach>
		</table>
		</form>
		</td>
	</tr>
</table>
