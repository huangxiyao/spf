<%@ include file="include.jsp"%>
<script type="text/javascript">
    function takeAction(activityValue) {
        document.getElementById("activity").value = activityValue;
        document.getElementById("personaForm").submit();
    }
</script>
<table class="personaTable">
    <tr>
        <td width="200" valign="top" bgcolor="#efefef"><%@ include file="leftMenu.jsp"%></td>
        <td valign="top"><%@ include file="errorPane.jsp"%>
        <form id="personaForm" method="post"
            action="<portlet:actionURL><portlet:param name="action" value="simpleAttribute_configuration" /></portlet:actionURL>">
        <table cellspacing="2" cellpadding="2" class="contentTable">
            <tr>
                <th colspan="2" align="center">Simple Attribute Meta-data</th>
            </tr>
            <tr>
                <td width="200">Id:</td>
                <c:choose>
                    <c:when test="${empty userAttributeIdentifier}">
                        <td><input type="text" name="userAttributeIdentifier" /></td>
                    </c:when>
                    <c:otherwise>
                        <td><c:out value="${userAttributeIdentifier}"/></td>
                    </c:otherwise>
                </c:choose>
            </tr>
            <tr>
                <td>Name:</td>
                <td><input name="userAttributeName" value="<c:out value="${userAttributeName}"/>" /></td>
            </tr>
            <tr>
                <td>Description:</td>
                <td><textarea name="userAttributeDescription"><c:out value="${userAttributeDescription}" /></textarea></td>
            </tr>
            <tr>
                <td>Definition:</td>
                <td><textarea name="userAttributeDefinition" rows="15"><c:out
                    value="${userAttributeDefinition}" /></textarea></td>
            </tr>
            <tr>
                <td colspan="2">
                    <input type="hidden" name="activity" id="activity" />
                    <c:choose>
                        <c:when test="${empty userAttributeIdentifier}">
                            <input type="button" class="personaButton" value="Save Meta-data" onClick="takeAction('insert')" />
                        </c:when>
                        <c:otherwise>
                            <input type="hidden" name="userAttributeIdentifier" value="<c:out value="${userAttributeIdentifier}" />" />
                            <input type="button" class="personaButton" value="Save Meta-data" onClick="takeAction('update')" />
                        </c:otherwise>
                    </c:choose>&nbsp;<input type="button" class="personaButton" value="Back" onClick="takeAction('query')" />
                </td>
            </tr>
        </table>
        </form>
        </td>
    </tr>
</table>
