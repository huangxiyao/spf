<%@ page language="java" pageEncoding="UTF-8" %>
<%@ include file="taglibs.jsp" %>	

<portlet:defineObjects/>

<portlet:actionURL windowState="maximized" portletMode="view" copyCurrentRenderParameters="true" var="submit" escapeXml="false" />

<form:form method="post" action="${submit}">
<p>User: ${user}</p>
<div>
	<table width="1000px" border="1" style="font-size:12px">
		<tr height="30px">
			<td width="10%">&nbsp;</td>
			<td width="30%" style="background-color:#DDD">SPF_ Session Attributes</td>
			<td width="30%" style="background-color:#DDD">SPF_RETAIN_ Session Attributes</td>
			<td width="30%" style="background-color:#DDD">Other Session Attributes</td>
		</tr>
		<tr height="30px">
			<td style="background-color:#DDD">Portlet Scope</td>
			<td>
				<table>
					<c:forEach var="item" items="${spfPortletScopeAttributes}">
						<tr><td>${item}</td></tr>
					</c:forEach>
				</table>
			</td>
			<td>
				<table>
					<c:forEach var="item" items="${retainPortletScopeAttributes}">
						<tr><td>${item}</td></tr>
					</c:forEach>
				</table>
			</td>
			<td>
				<table>
					<c:forEach var="item" items="${otherPortletScopeAttributes}">
						<tr><td>${item}</td></tr>
					</c:forEach>
				</table>
			</td>
		</tr>	
		<tr height="30px">
			<td style="background-color:#DDD">Application Scope</td>
			<td>
				<table>
					<c:forEach var="item" items="${spfApplicationScopeAttributes}">
						<tr><td>${item}</td></tr>
					</c:forEach>
				</table>
			</td>
			<td>
				<table>
					<c:forEach var="item" items="${retainApplicationScopeAttributes}">
						<tr><td>${item}</td></tr>
					</c:forEach>
				</table>
			</td>
			<td>
				<table>
					<c:forEach var="item" items="${otherApplicationScopeAttributes}">
						<tr><td>${item}</td></tr>
					</c:forEach>
				</table>
			</td>
		</tr>	
	</table>
</div>
<div style="clear:both"><br/>
	<input type="submit" name="action" value="Add Session Attributes" />&nbsp; &nbsp; &nbsp;
	<input type="submit" name="action" value="Render" />
	 
</div>
</form:form>

