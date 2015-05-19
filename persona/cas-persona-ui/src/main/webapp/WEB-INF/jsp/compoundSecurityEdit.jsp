<%@ include file="include.jsp"%>
<script type="text/javascript">
    function takeAction(activityValue) {
        document.getElementById("activity").value = activityValue;
        document.getElementById("personaForm").submit();
    }
    function del(compoundAttributeIdentifier, simpleAttributeIdentifier) {
    	document.getElementById("compoundAttributeIdentifier").value = compoundAttributeIdentifier;
    	document.getElementById("simpleAttributeIdentifier").value = simpleAttributeIdentifier;
        takeAction("compounddelete");
    }
    function grant() {
    	var compoundAttrValue = document.getElementById("grantCompoundUserAttributeIdentifier").value;
    	if (compoundAttrValue == "") {
    		return;
    	}
        takeAction("compoundgrant");
    }
    function cascadeSimpleAttributes() {
    	var compoundAttrValue = document.getElementById("grantCompoundUserAttributeIdentifier").value;
		takeAction("cascadeSimpleAttributes");
    }
</script>
<table class="personaTable">
	<tr>
		<td width="200" valign="top" bgcolor="#efefef"><%@ include
			file="leftMenu.jsp"%></td>
		<td valign="top"><%@ include file="errorPane.jsp"%>
		<form id="personaForm" method="post"
			action="<portlet:actionURL><portlet:param name="action" value="securityManagement" /></portlet:actionURL>">
		<input type="hidden" name="applicationPortfolioIdentifier"
			value="<c:out value="${applicationPortfolioIdentifier}"/>" /> <input
			type="hidden" name="activity" id="activity" />
		<table cellspacing="3" cellpadding="2" class="contentTable">
			<tr>
				<td>Identifier:<c:out value="${applicationPortfolioIdentifier}" /></td>
				<td nowrap><c:if test="${isAdmin}">Available Compound Attributes:
					<select name="grantCompoundUserAttributeIdentifier" id="grantCompoundUserAttributeIdentifier"
						onChange="cascadeSimpleAttributes()">
						<option value=""></option>
						<c:forEach items="${avaliableCompoundAttributeList}" var="entry">
							<c:if
								test="${entry.userAttributeIdentifier == grantCompoundUserAttributeIdentifier}">
								<option value="<c:out value="${entry.userAttributeIdentifier}"/>"
									selected><c:out value="${entry.userAttributeName}" />
								</option>
							</c:if>
							<c:if
								test="${entry.userAttributeIdentifier != grantCompoundUserAttributeIdentifier}">
								<option value="<c:out value="${entry.userAttributeIdentifier}"/>">
								<c:out value="${entry.userAttributeName}" /></option>
							</c:if>
						</c:forEach>
					</select>
				</c:if></td>
				<td nowrap><c:if test="${isAdmin}">Available Simple Attributes:
					<select name="grantSimpleUserAttributeIdentifier">
						<c:forEach items="${avaliableSimpleAttributeList}" var="entry">
							<option value="<c:out value="${entry.userAttributeIdentifier}"/>">
							<c:out value="${entry.userAttributeName}" /></option>
						</c:forEach>
					</select>
				</c:if></td>
				<td nowrap><c:if test="${isAdmin}">
					<input class="personaButton" type="button" value="grant"
						onClick="grant()" />
				</c:if> <input class="personaButton" type="button" value="back"
					onClick="takeAction('query')" /></td>
			</tr>
		</table>
		<table cellspacing="4" cellpadding="2" class="contentTable">
			<tr>
				<th colspan="4" align="center">Compound Attribute Permissions</th>
			</tr>
			<tr>
				<th>Compound Id</th>
				<th>Compound Name</th>
				<th>Simple Name</th>
				<c:if test="${isAdmin}">
					<th>Action</th>
				</c:if>
			</tr>
			<c:forEach items="${permissionList}" var="entry">
				<tr>
					<td><c:out
						value="${entry.compoundUserAttribute.userAttributeIdentifier}" /></td>
					<td><c:out
						value="${entry.compoundUserAttribute.userAttributeName}" /></td>
					<td><c:out
						value="${entry.simpleUserAttribute.userAttributeName}" /></td>
					<c:if test="${isAdmin}">
						<td><input class="personaButton" type="button" value="delete"
							onClick="del('<c:out value="${entry.compoundUserAttribute.userAttributeIdentifier}"/>', '<c:out value="${entry.simpleUserAttribute.userAttributeIdentifier}"/>')" /></td>
					</c:if>
				</tr>
			</c:forEach>
		</table>
		<input type="hidden" name="compoundAttributeIdentifier" id="compoundAttributeIdentifier" /> <input
			type="hidden" name="simpleAttributeIdentifier" id="simpleAttributeIdentifier" /></form>
		</td>
	</tr>
</table>
