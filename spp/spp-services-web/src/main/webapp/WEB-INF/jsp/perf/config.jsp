<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>

<style type="text/css">
	#configTable th {text-align: right;}
</style>
<form action="<portlet:actionURL />" method="POST">
	<table id="configTable">
		<tr>
			<p><b>Execution Times in [ms]:</b></p>
			<td>
				<c:choose>
					<c:when test="${isUsingGlobalExecutionTime}">
						<c:out value="${executionTime}" /> <small><i>(using global execution time)</i></small>
					</c:when>
					<c:otherwise>
                        <tr>
                         <th>Min time  [ms]:  </th>
                          <td> <input type="text" name="minExecutionTime" value="<c:out value="${minExecutionTime}" />" /> </td>
                         </tr>

                         <tr>
                        <th> Max time  [ms]:   </th>
                       <td> <input type="text" name="maxExecutionTime" value="<c:out value="${maxExecutionTime}" />" />  </td>
                         </tr>
                      </c:otherwise>
				</c:choose>
        <tr>
			<th>Number of Items:</th>
			<td><input type="text" name="numberOfItems" value="<c:out value="${numberOfItems}" />" /></td>
		</tr>
		<tr>
			<th>Display Mode:</th>
			<td>
				<select name="displayMode">
					<option value="verbose">verbose</option>
					<option value="brief" <c:if test="${displayMode == 'brief'}">selected="selected"</c:if>>brief</option>
				</select>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<input type="submit" value="Save" />
			</td>
		</tr>
	</table>
</form>
<p>
	<a href="<portlet:renderURL><portlet:param name="preview" value="true" /></portlet:renderURL>">Preview</a>
</p>
<c:if test="${isUsingConfigFile}">
	<p>
		<a href="<portlet:actionURL><portlet:param name="refreshConfig" value="true" /></portlet:actionURL>">Refresh Global Configuration</a>
	</p>
</c:if>