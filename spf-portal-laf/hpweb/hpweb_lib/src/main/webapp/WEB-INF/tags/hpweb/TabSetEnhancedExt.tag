<%@ tag pageEncoding="UTF-8" body-content="scriptless" %>

<%@ attribute name="width" type="java.lang.Integer" %>
<%@ attribute name="selectedIndex" type="java.lang.Integer" %>
<%@ attribute name="stretchTabs" type="java.lang.Boolean" %>
<%@ attribute name="hairline" type="java.lang.Boolean" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


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


<%-- Use the width, in px, if it is supplied --%>

<c:if test="${width > 0}">
	<c:set var="width" value="${width}px" />
</c:if>

<div <c:if test="${! empty width}">style="width: ${width};"</c:if>>
	<div id="tabbedNavEnhanced"<c:if test="${hairline}"> style="margin-bottom: 0px"</c:if>>
		<c:forEach var="item" items="${tabList}" varStatus="loopStatus">
	
			<c:choose>
				<c:when test="${loopStatus.index == selectedIndex}">

					<%-- Calc width for each tab string if width 
						 isn't specified. --%>

					<%-- TSG wants selected tab to also display &raquo character, exception to hp.com standards for tabs.  --%>
					<div class="tab tabSelected" title="${item.alt}">&raquo;<a href="${item.url}">${item.title}<span class="screenReading"> - Tab - Selected</span></a></div>
				</c:when>
				<c:otherwise>
					<div class="tab" title="${item.alt}">&raquo;<a href="${item.url}" >${item.title}<span class="screenReading"> - Tab </span></a></div>		
				</c:otherwise>
			</c:choose>
		</c:forEach>
	</div>
<%-- Do not need to display content
	<div<c:if test="${hairline}"> class="paddedBlock themeRightBorder"</c:if>>
		<c:forEach var="item" items="${tabList}" varStatus="loopStatus">
			<c:if test="${loopStatus.index == selectedIndex}">
				${item.body}
			</c:if>
		</c:forEach>	
	</div>		
--%>
</div>


<%----------------------------------------------------------------------------- 
	Clean-up  
-----------------------------------------------------------------------------%>

<c:remove var="tabs" />
