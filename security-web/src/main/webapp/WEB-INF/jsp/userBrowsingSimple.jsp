<%@ include file="include.jsp"%>
<script type="text/javascript">
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
			action="<portlet:actionURL><portlet:param name="action" value="simpleContentManagement" /></portlet:actionURL>">
		<table cellspacing="2" cellpadding="3" class="contentTable">
			<tr>
				<th colspan="2" align="center">Add Simple Attribute to User <c:out
					value="${userIdentifierTypeName}" />:<c:out
					value="${userIdentifier}" /></th>
			</tr>
			<tr>
				<td>Available Simple Attributes:</td>
				<td><select name="userAttributeIdentifier">
					<c:forEach items="${availableUserAttributeList}" var="entry">
						<option value="<c:out value="${entry.userAttributeIdentifier}" />"><c:out
							value="${entry.userAttributeName}" /> - <c:out
							value="${entry.userAttributeIdentifier}" /></option>
					</c:forEach>
				</select></td>
			</tr>
			<tr>
				<td>Simple Attribute Value</td>
				<td><input name="userAttributeValue" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="hidden" name="activity"
					value="add" /><input type="hidden" name="userIdentifier"
					value="<c:out value="${userIdentifier}" />" /><input type="hidden"
					name="userIdentifierTypeCode"
					value="<c:out value="${userIdentifierTypeCode}" />" /><input
					class="personaButton" type="submit" value="Add to current User" /><input
					type="button" class="personaButton" value="Back" onClick="goBack()" /></td>
			</tr>
		</table>
		</form>
		</td>
	</tr>
</table>
