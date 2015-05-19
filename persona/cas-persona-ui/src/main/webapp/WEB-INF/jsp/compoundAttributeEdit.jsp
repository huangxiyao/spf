<%@ include file="include.jsp"%>
<script type="text/javascript">
	function takeAction(activityValue) {
	    document.getElementById("activity").value = activityValue;
	    document.getElementById("personaForm").submit();
	}
    function deleteSimple(deletedUserAttributeIdentifier) {
        document.getElementById("deletedUserAttributeIdentifier").value = deletedUserAttributeIdentifier;
        takeAction("deleteSimple");
    }
</script>
<table class="personaTable">
	<tr>
		<td width="200" valign="top" bgcolor="#efefef"><%@ include
			file="leftMenu.jsp"%></td>
		<td valign="top"><%@ include file="errorPane.jsp"%>
		<form id="personaForm" method="post"
			action="<portlet:actionURL><portlet:param name="action" value="compoundAttribute_configuration" /></portlet:actionURL>">
		<table cellspacing="2" cellpadding="2" class="contentTable">
			<tr>
				<th colspan="2" align="center">Compound Attribute Meta-data</th>
			</tr>
            <tr>
                <td width="200">Id:</td>
                <c:choose>
                    <c:when test="${empty userAttributeIdentifier and isAdmin}">
                        <td><input type="text" name="userAttributeIdentifier" /></td>
                    </c:when>
                    <c:otherwise>
                        <td><c:out value="${userAttributeIdentifier}"/></td>
                    </c:otherwise>
                </c:choose>
            </tr>
			<tr>
				<td>Name:</td>
				<c:if test="${isAdmin}">
					<td><input name="userAttributeName"
						value="<c:out value="${userAttributeName}"/>" /></td>
				</c:if>
				<c:if test="${!isAdmin}">
					<td><c:out value="${userAttributeName}" /></td>
				</c:if>
			</tr>
			<tr>
				<td>Description:</td>
				<c:if test="${isAdmin}">
					<td><textarea name="userAttributeDescription"><c:out
						value="${userAttributeDescription}" /></textarea></td>
				</c:if>
				<c:if test="${!isAdmin}">
					<td><c:out value="${userAttributeDescription}" /></td>
				</c:if>
			</tr>
			<tr>
				<td>Definition:</td>
				<c:if test="${isAdmin}">
					<td><textarea name="userAttributeDefinition" rows="15"><c:out
						value="${userAttributeDefinition}" /></textarea></td>
				</c:if>
				<c:if test="${!isAdmin}">
					<td><c:out value="${userAttributeDefinition}" /></td>
				</c:if>
			</tr>
			<tr>
				<td colspan="2"><input type="hidden" name="activity"
					id="activity" /> <c:if test="${isAdmin}">
					<c:if test="${userAttributeIdentifier != null && userAttributeIdentifier != ''}">
						<input type="hidden" name="userAttributeIdentifier"
							value="<c:out value="${userAttributeIdentifier}" />" />
						<input type="button" class="personaButton" value="Save Meta-data"
							onClick="takeAction('update')" />
					</c:if>
					<c:if test="${userAttributeIdentifier == null || userAttributeIdentifier == ''}">
						<input type="button" class="personaButton" value="Save Meta-data"
							onClick="takeAction('insert')" />
					</c:if>
				</c:if>&nbsp;<input type="button" class="personaButton" value="Back"
					onClick="takeAction('query')" /></td>
			</tr>
		</table>
		<c:if test="${userAttributeIdentifier != null && userAttributeIdentifier != ''}">
			<table cellspacing="2" cellpadding="3" class="contentTable">
				<tr>
					<th colspan="4" align="center">Simple Attribute Members</th>
				</tr>
				<c:if test="${isAdmin}">
					<tr>
						<td colspan="4">Available Simple Attributes:&nbsp; <select
							name="addedUserAttributeIdentifier" id="addedUserAttributeIdentifier">
							<c:forEach items="${availableSimpleAttributeList}" var="entry">
								<option value="<c:out value="${entry.userAttributeIdentifier}" />"><c:out
									value="${entry.userAttributeName}" /> - <c:out
									value="${entry.userAttributeIdentifier}" /></option>
							</c:forEach>
						</select> &nbsp;<input class="personaButton" type="button"
							value="Add to current Compound Attribute"
							onClick="takeAction('addSimple')" /><input type="hidden"
							name="deletedUserAttributeIdentifier" id="deletedUserAttributeIdentifier"/></td>
					</tr>
				</c:if>
				<tr>
					<th>Simple Attribute Id</th>
					<th>Simple Attribute Name</th>
					<th>Simple Attribute Description</th>
					<c:if test="${isAdmin}">
						<th>Action</th>
					</c:if>
				</tr>
				<c:forEach items="${simpleAttributeList}" var="entry">
					<tr>
						<td><c:out value="${entry.userAttributeIdentifier}" /></td>
						<td><c:out value="${entry.userAttributeName}" /></td>
						<td><c:out value="${entry.userAttributeDescription}" /></td>
						<c:if test="${isAdmin}">
							<td><input class="personaButton" type="button"
								value="delete"
								onClick="deleteSimple('<c:out value="${entry.userAttributeIdentifier}" />')" /></td>
						</c:if>
					</tr>
				</c:forEach>
			</table>
		</c:if></form>
		</td>
	</tr>
</table>
