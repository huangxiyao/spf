<%@ include file="include.jsp"%>
<script type="text/javascript">
    function takeAction(activityValue) {
        document.getElementById("activity").value = activityValue;
        document.getElementById("personaForm").submit();
    }
    function goBack() {
        window.location.href= "<%=contentURL%>";
    }
</script>
<table class="personaTable">
	<tr>
		<td width="200" valign="top" bgcolor="#efefef"><%@ include
			file="leftMenu.jsp"%></td>
		<td valign="top"><%@ include file="errorPane.jsp"%>
		<form id="personaForm" method="post"
			action="<portlet:actionURL><portlet:param name="action" value="compoundContentManagement" /></portlet:actionURL>">
		<table cellspacing="2" cellpadding="3" class="contentTable">
			<tr>
				<th colspan="3" align="center">Compound Attributes in User <c:out
					value="${userIdentifierTypeName}" />:<c:out
					value="${userIdentifier}" /></th>
			</tr>
			<tr>
				<td colspan="3"><c:if test="${isAdmin}">Available Compound Attributes:&nbsp; <select
						name="userAttributeIdentifier">
						<c:forEach items="${availableUserAttributeList}" var="entry">
							<option value="<c:out value="${entry.userAttributeIdentifier}" />"><c:out
								value="${entry.userAttributeName}" /> - <c:out
								value="${entry.userAttributeIdentifier}" /></option>
						</c:forEach>
					</select>&nbsp;<input type="hidden" name="activity" id="activity" />
					<input type="hidden" name="userIdentifier"
						value="<c:out value="${userIdentifier}" />" />
					<input type="hidden" name="userIdentifierTypeCode"
						value="<c:out value="${userIdentifierTypeCode}" />" />
					<input class="personaButton" type="button"
						value="Add to current User" onClick="takeAction('add')" />&nbsp;<input
						class="personaButton" type="button"
						title="leave value blank for delete attribute"
						value="Save Compound Attributes" onClick="takeAction('save')" />&nbsp;</c:if><input
					type="button" class="personaButton" value="Back" onClick="goBack()" /></td>
			</tr>
			<tr>
				<th>Compound attribute</th>
				<th>Simple attribute</th>
				<th>Value</th>
			</tr>
			<c:set var="lastInstanceIdentifier" value="" />
			<c:set var="flag" value="false" />
			<c:forEach items="${attributeList}" var="entry">
				<c:if test="${entry.instanceIdentifier != lastInstanceIdentifier}">
					<c:set var="lastInstanceIdentifier"
						value="${entry.instanceIdentifier}" />
					<c:set var="flag" value="${!flag}" />
				</c:if>
				<c:choose>
					<c:when test="${flag}">
						<tr class="even">
					</c:when>
					<c:otherwise>
						<tr class="odd">
					</c:otherwise>
				</c:choose>
				<td><c:out value="${entry.compoundUserAttributeName}" /></td>
				<td><c:out value="${entry.simpleUserAttributeName}" /></td>
				<c:if test="${isAdmin}">
					<td><input
						name="compound_<c:out value="${entry.compoundUserAttributeIdentifier}" />__<c:out value="${entry.simpleUserAttributeIdentifier}" />__<c:out value="${entry.instanceIdentifier}" />"
						value="<c:out value="${entry.value}" />" /></td>
				</c:if>
				<c:if test="${!isAdmin}">
					<td><c:out value="${entry.value}" /></td>
				</c:if>
				</tr>
			</c:forEach>
		</table>
		</form>
		</td>
	</tr>
</table>
