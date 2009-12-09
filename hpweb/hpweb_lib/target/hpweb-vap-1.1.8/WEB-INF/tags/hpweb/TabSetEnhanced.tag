<%@ tag pageEncoding="UTF-8" body-content="scriptless" %>

<%@ attribute name="width" type="java.lang.Integer" %>
<%@ attribute name="selectedIndex" type="java.lang.Integer" %>
<%@ attribute name="stretchTabs" type="java.lang.Boolean" %>
<%@ attribute name="hairline" type="java.lang.Boolean" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>

<jsp:useBean id="tabList" class="java.util.ArrayList" />
<c:set var="com.hp.frameworks.wpa.hpweb.TABS" value="${tabList}" scope="request" />
<jsp:doBody />

<c:if test="${empty selectedIndex}">
	<c:set var="selectedIndex" value="0" />
</c:if>

<c:if test="${empty hairline}">
	<c:set var="hairline" value="true" />
</c:if>


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<div <c:if test="${width > 0}">style="width: ${width}px;"</c:if>>
	<div id="tabbedNavEnhanced"<c:if test="${hairline}"> style="margin-bottom: 0px"</c:if>>
		<c:forEach var="item" items="${tabList}" varStatus="loopStatus">
	
			<c:choose>
				<c:when test="${loopStatus.index == selectedIndex}">
					<div class="tab tabSelected" title="${item.alt}">${item.title}<span class="screenReading"> - Tab - Selected</span></div>
				</c:when>
				<c:otherwise>
					<div class="tab" title="${item.alt}">&raquo;<a href="${item.url}">${item.title}<span class="screenReading"> - Tab </span></a></div>		
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</div>
	<div<c:if test="${hairline}"> class="paddedBlock themeRightBorder"</c:if>>
		<c:forEach var="item" items="${tabList}" varStatus="loopStatus">
			<c:if test="${loopStatus.index == selectedIndex}">
				${item.body}
			</c:if>
		</c:forEach>	
	</div>		
</div>


<%----------------------------------------------------------------------------- 
	Clean-up  
-----------------------------------------------------------------------------%>

<c:remove var="tabs" />
