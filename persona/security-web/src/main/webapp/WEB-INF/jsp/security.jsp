<%@ include file="include.jsp"%>
<script type="text/javascript">
    function takeAction(activityValue, applicationPortfolioIdentifier) {
        document.getElementById("activity").value = activityValue;
        if (applicationPortfolioIdentifier) {
        	document.getElementById("applicationPortfolioIdentifier").value = applicationPortfolioIdentifier;
        }
        document.getElementById("personaForm").submit();
    }
</script>
<table class="personaTable">
	<tr>
		<td width="200" valign="top" bgcolor="#efefef"><%@ include
			file="leftMenu.jsp"%></td>
		<td valign="top"><%@ include file="errorPane.jsp"%>
		<form method="post" id="personaForm"
			action="<portlet:actionURL><portlet:param name="action" value="securityManagement" /></portlet:actionURL>">
		<table cellspacing="2" cellpadding="3" class="contentTable">
			<tr>
				<th colspan="4" align="center">Applications</th>
			</tr>
			<tr>
				<td colspan="4">Application Identifier: <input
					name="applicationPortfolioIdentifier" id="applicationPortfolioIdentifier"
					value="<c:out value="${applicationPortfolioIdentifier}" />" />&nbsp;
				<input type="submit" class="personaButton" value="query"
					onclick="takeAction('query')" />&nbsp; <input type="hidden"
					name="activity" id="activity" /></td>
			</tr>
			<c:if test="${applicationList != null}">
				<tr>
					<th>Identifier</th>
					<th>Name</th>
					<th>Description</th>
					<th>Action</th>
				</tr>
				<c:forEach items="${applicationList}" var="entry">
					<tr>
						<td><c:out value="${entry.applicationPortfolioIdentifier}" /></td>
						<td><c:out value="${entry.applicationAliasName}" /></td>
						<td><c:out value="${entry.applicationDescription}" /></td>
						<c:if test="${isAdmin}">
							<td><input class="personaButton" type="button"
								value="Edit Simple Permissions"
								onClick="takeAction('simpleedit', '<c:out value="${entry.applicationPortfolioIdentifier}" />')" />
							<input class="personaButton" type="button"
								value="Edit Compound Permissions"
								onClick="takeAction('compoundedit', '<c:out value="${entry.applicationPortfolioIdentifier}" />')" />
							</td>
						</c:if>
						<c:if test="${!isAdmin}">
							<td><input class="personaButton" type="button"
								value="View Simple Permissions"
								onClick="takeAction('simpleedit', '<c:out value="${entry.applicationPortfolioIdentifier}" />')" />
							<input class="personaButton" type="button"
								value="View Compound Permissions"
								onClick="takeAction('compoundedit', '<c:out value="${entry.applicationPortfolioIdentifier}" />')" />
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
