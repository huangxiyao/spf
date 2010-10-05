<%@ tag pageEncoding="UTF-8" body-content="scriptless" %>

<%@ attribute name="width" type="java.lang.Integer" %>
<%@ attribute name="selectedIndex" type="java.lang.Integer" %>
<%@ attribute name="stretchTabs" type="java.lang.Boolean" %>
<%@ attribute name="hairline" type="java.lang.Boolean" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="hpwebfn" uri="http://frameworks.hp.com/wpa/hpweb-fn" %>


<%----------------------------------------------------------------------------- 
	Messages  
-----------------------------------------------------------------------------%>
<fmt:setBundle var="urlResources" basename="com.hp.frameworks.wpa.hpweb.Urls" />

<c:choose>
	<c:when test="${pageContext.request.scheme eq 'https'}">
		<fmt:message var="spacerUrl" key="link.hpweb2003.secure_spacer" bundle="${urlResources}" />	
		<fmt:message var="globalImgDir" key="link.hpweb2003.secure_global_img_dir" bundle="${urlResources}" />	
	</c:when>
	<c:otherwise>
		<fmt:message var="spacerUrl" key="link.hpweb2003.spacer" bundle="${urlResources}" />
		<fmt:message var="globalImgDir" key="link.hpweb2003.global_img_dir" bundle="${urlResources}" />
	</c:otherwise>
</c:choose>


<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>

<jsp:useBean id="tabList" class="java.util.ArrayList" />
<c:set var="com.hp.frameworks.wpa.hpweb.TABS" value="${tabList}" scope="request" />
<jsp:doBody />

<c:if test="${empty selectedIndex}">
	<c:set var="selectedIndex" value="0" />
</c:if>

<c:if test="${empty stretchTabs}">
	<c:set var="stretchTabs" value="true" />
</c:if>

<c:choose>
	<c:when test="${empty width || width == 0}">
		<c:set var="bodyWidth" value="100%" />
		<c:set var="width" value="100%" />
		<c:if test="${stretchTabs}">
			<c:set var="tabWidth" value="${hpwebfn:round(100 div fn:length(tabList))}%" />
		</c:if>
	</c:when>
	<c:otherwise>
		<c:set var="bodyWidth" value="${width - 10}" />
		<c:if test="${stretchTabs}">
			<c:set var="tabWidth" value="${hpwebfn:round((width - 10 - (fn:length(tabList) * 5) - ((fn:length(tabList) - 1) * 2)) div fn:length(tabList))}" />
		</c:if>
	</c:otherwise>
</c:choose>

<c:if test="${empty hairline}">
	<c:set var="hairline" value="true" />
</c:if>

<c:choose>
	<c:when test="${hpwebfn:isLocaleLTR(urlResources)}">	
		<c:set var="tabCornerUrl" value="${globalImgDir}/hpweb_1-2_tab_right.gif" />	
	</c:when>
	<c:otherwise>
		<c:set var="tabCornerUrl" value="${globalImgDir}/hpweb_1-2_tab_left.gif" />
	</c:otherwise>
</c:choose>


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<table border="0" cellpadding="0" cellspacing="0" width="${width}">
<tr>
	<c:forEach var="item" items="${tabList}" varStatus="loopStatus">

		<c:choose>
			<c:when test="${loopStatus.index == selectedIndex}">
				<c:set var="cellClass" value="theme" />
				<c:set var="headerClass" value="themeheader" />
			</c:when>
			<c:otherwise>
				<c:set var="cellClass" value="color666666bg" />
				<c:set var="headerClass" value="colorFFFFFFbld" />		
			</c:otherwise>
		</c:choose>
			
		<c:if test="${!stretchTabs}">
			<td width="5" class="${cellClass}"><img src="${spacerUrl}" width="5" height="1" alt="" class="decoration"></td>
		</c:if>
		
		<td class="${cellClass}" align="center" <c:if test="${!empty tabWidth}"> width="${tabWidth}"</c:if>><h2 class="${headerClass}" title="${item.alt}">
			<c:choose>
			<c:when test="${loopStatus.index == selectedIndex}">		
				${item.title}
			</c:when>
			<c:otherwise>
				&raquo;&nbsp;<a href="${item.url}" class="colorFFFFFFbld">${item.title}</a>
			</c:otherwise>
			</c:choose>
		</h2></td>				
		
		<c:if test="${!stretchTabs}">
			<td width="5" class="${cellClass}"><img src="${spacerUrl}" width="5" height="1" alt="" class="decoration"></td>
		</c:if>
						
		<td valign="top" width="5" class="${cellClass}"><img src="${tabCornerUrl}" width="5" height="23" alt="" border="0"></td>
		
		<c:if test="${!loopStatus.last}">
			<td width="2"><img src="${spacerUrl}" width="2" height="1" alt="" class="decoration"></td>
		</c:if>
	</c:forEach>
	
	<td <c:if test="${!stretchTabs}"> width="100%"</c:if>><img src="${spacerUrl}" width="10" height="1" alt="" class="decoration"></td>
</tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="${width}">
	<tr class="decoration">
		<td class="theme" width="${bodyWidth}"><img src="${spacerUrl}" height="2" alt=""></td>
		<td class="theme" width="8"><img src="${spacerUrl}" width="8" height="2" alt=""></td>
		<td class="theme" width="2"><img src="${spacerUrl}" width="2" height="2" alt=""></td>
	</tr>
	<tr class="decoration">
		<td class="colorFFFFFFbg" colspan="2"><img src="${spacerUrl}" width="1" height="10" alt=""></td>
		<c:if test="${hairline}"><td width="2" class="theme"><img src="${spacerUrl}" width="2" height="1" alt=""></td></c:if>
	</tr>
	<tr>
		<td <c:if test="${!hairline}"> colspan="3"</c:if>>
			<c:forEach var="item" items="${tabList}" varStatus="loopStatus">
				<c:if test="${loopStatus.index == selectedIndex}">
					${item.body}
				</c:if>
			</c:forEach>						
		</td>
		<c:if test="${hairline}">
			<td width="8" class="colorFFFFFFbg"><img src="${spacerUrl}" width="8" height="1" alt=""></td>
			<td width="2" class="theme"><img src="${spacerUrl}" width="2" height="1" alt=""></td>
		</c:if>							
	</tr>
	
	<c:if test="${hairline}">
		<tr class="decoration">
			<td class="colorFFFFFFbg" colspan="2"><img src="${spacerUrl}" width="1" height="10" alt=""></td>
			<td width="2" class="theme"><img src="${spacerUrl}" width="2" height="1" alt=""></td>
		</tr>
		<tr class="decoration">
			<td class="theme" colspan="3"><img src="${spacerUrl}" width="1" height="2" alt=""></td>
		</tr>	
		<tr class="decoration">
			<td class="colorFFFFFFbg" colspan="3" ><img src="${spacerUrl}" width="1" height="10" alt="" border="0"></td>
		</tr>
	</c:if>
</table>


<c:remove var="tabs" />