<%@ tag pageEncoding="UTF-8" body-content="empty"%>

<%@ attribute name="contactUrl" type="java.lang.String" %>
<%@ attribute name="phoneLabel" type="java.lang.String" %>
<%@ attribute name="phoneNumber" type="java.lang.String" %>
<%@ attribute name="searchWidget" type="java.lang.String" %>
<%@ attribute name="searchUrl" type="java.lang.String" %>
<%@ attribute name="searchSectionName" type="java.lang.String" %>
<%@ attribute name="searchQueryPrefix" type="java.lang.String" %>
<%@ attribute name="searchReturnText" type="java.lang.String" %>
<%@ attribute name="searchContactUrl" type="java.lang.String" %>
<%@ attribute name="searchReturnUrl" type="java.lang.String" %>
<%@ attribute name="searchOmnitureTag" type="java.lang.String" %>
<%@ attribute name="searchAudience" type="java.lang.String" %>
<%@ attribute name="searchRadioPadding" type="java.lang.String" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hpwebfn" uri="http://frameworks.hp.com/wpa/hpweb-fn" %>


<%----------------------------------------------------------------------------- 
	Messages  
-----------------------------------------------------------------------------%>
<fmt:setBundle var="msgResources" basename="com.hp.frameworks.wpa.hpweb.Messages" />
<fmt:setBundle var="urlResources" basename="com.hp.frameworks.wpa.hpweb.Urls" />

<fmt:message var="contact" key="text.hpweb2003.contact" bundle="${msgResources}" />
<fmt:message var="searchCountryCode" key="text.hpweb2003.searchCountryCode" bundle="${msgResources}" />
<fmt:message var="searchLanguageCode" key="text.hpweb2003.searchLanguageCode" bundle="${msgResources}" />
<fmt:message var="searchLabel" key="text.hpweb2003.searchLabel" bundle="${msgResources}" />
<fmt:message var="searchCriteria" key="text.hpweb2003.searchCriteria" bundle="${msgResources}" />
<fmt:message var="searchStatus" key="text.hpweb2003.searchStatus" bundle="${msgResources}" />
<fmt:message var="searchAllOf" key="text.hpweb2003.searchAllOf" bundle="${msgResources}" />

<c:if test="${empty contactUrl}">
	<fmt:message var="contactUrl" key="link.hpweb2003.contact" bundle="${urlResources}" />
</c:if>

<c:if test="${empty searchUrl}">
	<fmt:message var="searchUrl" key="link.hpweb2003.search" bundle="${urlResources}" />
</c:if>

<c:choose>
	<c:when test="${pageContext.request.scheme eq 'https'}">	
		<fmt:message var="globalImgDir" key="link.hpweb2003.secure_global_img_dir" bundle="${urlResources}" />	
	</c:when>
	<c:otherwise>
		<fmt:message var="globalImgDir" key="link.hpweb2003.global_img_dir" bundle="${urlResources}" />
	</c:otherwise>
</c:choose>


<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>

<c:choose>
	<c:when test="${hpwebfn:isLocaleLTR(urlResources)}">	
		<c:set var="arrowImg" value="${globalImgDir}/hpweb_1-2_arrw_sbmt.gif" />
	</c:when>
	<c:otherwise>
		<c:set var="arrowImg" value="${globalImgDir}/hpweb_1-2_arrw_sbmt_reverse.gif" />
		<c:set var="dir" value=" dir=\"rtl\"" />
	</c:otherwise>
</c:choose>


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<div id="contactSearchArea" class="colorE7E7E7bg">
	<div id="contactHP">
		<span class="color003366bld">&raquo;&nbsp;</span><a href="${contactUrl}" class="smallbld">${contact}</a>
	</div>
	
	<c:if test="${!empty phoneNumber}">
		<div id="phoneOrders">
			${phoneLabel} <span class="bold">${phoneNumber}</span>			
		</div>
	</c:if>
	
	<div id="searchArea"<c:if test="${empty searchSectionName}"> class="NoRadioButtons"</c:if>>
		<c:choose>
			<c:when test="${!empty searchWidget}">
				${searchWidget}
			</c:when>
			<c:otherwise>
				<form id="searchForm" action="${searchUrl}" method="GET" class="zeroMargin">
				<input type="hidden" name="charset" value="UTF-8" />
				<input type="hidden" name="cc" value="${searchCountryCode}" />
				<input type="hidden" name="lang" value="${searchLanguageCode}" />						
				<input type="hidden" name="qp" value="${searchQueryPrefix}" />
				<input type="hidden" name="hps" value="${searchSectionName}" />
				<input type="hidden" name="hpn" value="${searchReturnText}" />
				
				<c:if test="${!empty searchContactUrl}">
					<input type="hidden" name="hpa" value="${searchContactUrl}" />
				</c:if>
				
				<c:if test="${!empty searchReturnUrl}">
					<input type="hidden" name="hpr" value="${searchReturnUrl}" />
				</c:if>
				
				<c:if test="${!empty searchOmnitureTag}">
					<input type="hidden" name="hpo" value="${searchOmnitureTag}" />
				</c:if>
				
				<c:if test="${!empty searchAudience}">
					<input type="hidden" name="h_audience" value="${searchAudience}" />
				</c:if>
				
				<div id="searchBox" class="bold">
					<label for="textbox1">${searchLabel}</label>&nbsp;<input type="text" name="qt" size="26" maxlength="100" id="textbox1" alt="${searchCriteria}" />&nbsp;<input type="image" name="submit" src="${arrowImg}" alt="${beginSearch}" />
				</div>
								
				<c:if test="${!empty searchSectionName}">
					<div id="searchScope" class="small">
						<input type="radio" value="1" name="hpl" checked="checked" class="srchradbtn" id="sectionRadio" /><label for="sectionRadio"><span class="bold">${searchSectionName}</span></label><input type="radio" value="0" name="hpl" class="srchradbtn" id="countryRadio" /><label for="countryRadio"${dir}>${searchAllOf}</label>${searchRadioPadding}
					</div>
				</c:if>
				
				</form>						
			</c:otherwise>
		</c:choose>	
	</div>	
</div>


