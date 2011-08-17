<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>

<%

com.hp.spp.common.util.xml.XMLContent xmlContent = (com.hp.spp.common.util.xml.XMLContent) pageContext
.findAttribute("xmlContent");

String encodedXml = new com.hp.spp.common.util.format.Formatter().encodeXmlForHtml(xmlContent.getXmlContent());

%>
	<c:choose>
		<c:when test="${backView == 'standardParameterSets'}">
			<H3>Standard Parameters Definition(s)</H3>
		</c:when>
		<c:when test="${backView == 'eservices'}">
			<H3>Eservice Definition(s)</H3>
		</c:when>
		<c:when test="${backView == 'groups'}">
			<H3>Group Definition(s)</H3>
		</c:when>
	</c:choose>

<textarea cols="70" rows="50" readonly><%=encodedXml%></textarea>
<br>

<portlet:renderURL var="actionBackUrl">
	<c:choose>
		<c:when test="${backView == 'standardParameterSets'}">
			<portlet:param name="action" value="standardParameterSets"/>
		</c:when>
		<c:when test="${backView == 'eservices'}">
			<portlet:param name="action" value="eservices"/>
		</c:when>
		<c:when test="${backView == 'groups'}">
			<portlet:param name="action" value="groups"/>
		</c:when>
	</c:choose>
</portlet:renderURL>

<p style="text-align:center;"><a href='<c:out value="${actionBackUrl}" escapeXml="false"/>'>Back</a></p>
