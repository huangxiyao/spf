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
		<fmt:message var="spacerUrl" key="link.hpweb2003.secure_spacer" bundle="${urlResources}" />	
		<fmt:message var="imgDir" key="link.hpweb2003.secure_img_dir" bundle="${urlResources}" />
		<fmt:message var="globalImgDir" key="link.hpweb2003.secure_global_img_dir" bundle="${urlResources}" />	
	</c:when>
	<c:otherwise>
		<fmt:message var="spacerUrl" key="link.hpweb2003.spacer" bundle="${urlResources}" />
		<fmt:message var="imgDir" key="link.hpweb2003.img_dir" bundle="${urlResources}" />
		<fmt:message var="globalImgDir" key="link.hpweb2003.global_img_dir" bundle="${urlResources}" />
	</c:otherwise>
</c:choose>


<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>

<c:choose>
	<c:when test="${hpwebfn:isLocaleLTR(urlResources)}">	
		<c:set var="left" value="left" />
		<c:set var="right" value="right" />
		<c:set var="dir" value="" />
		<c:set var="arrowImg" value="${globalImgDir}/hpweb_1-2_arrw_sbmt.gif" />
	</c:when>
	<c:otherwise>
		<c:set var="left" value="right" />
		<c:set var="right" value="left" />
		<c:set var="dir" value=" dir=\"rtl\"" />
		<c:set var="arrowImg" value="${globalImgDir}/hpweb_1-2_arrw_sbmt_reverse.gif" />
	</c:otherwise>
</c:choose>




<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<!-- Begin Search Area -->
<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td align="${left}" valign="top" bgcolor="#E7E7E7">
		<table border="0" cellpadding="0" cellspacing="0" width="740">
		<tr>
			<td width="20" valign="top"><img src="${spacerUrl}" width="20" height="48" class="decoration" alt="" border="0"></td>
			<td width="110" align="${left}" valign="middle" class="color003366bld">&raquo;&nbsp;<a href="${contactUrl}" class="smallbld">${contact}</a></td>
			<td width="200" align="${left}" valign="middle" class="small">
				<c:choose>
					<c:when test="${!empty phoneNumber}">
						${phoneLabel} <span class="bold">${phoneNumber}</span>
					</c:when>
					<c:otherwise>
						<img src="${spacerUrl}" width="1" height="1" alt="" border="0">
					</c:otherwise>
				</c:choose>					
			</td>
			<td width="410" align="${right}"${dir}>
				<c:choose>
					<c:when test="${!empty searchWidget}">
						${searchWidget}
					</c:when>
					<c:otherwise>
						<form action="${searchUrl}" method="GET">
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
						
						<table border="0" cellpadding="0" cellspacing="0" align="${right}">
							<tr class="decoration">
								<td colspan="4"><img src="${spacerUrl}" width="1" height="2" alt="" border="0"></td>
							</tr>							
							<tr>
								<td align="${left}" class="bold"><label for="searchText"${dir}>${searchLabel}</label></td>
								<td valign="top"><img src="${spacerUrl}" width="4" height="1" class="decoration" alt="" border="0"></td>
								<td align="${left}" valign="top"><input type="text" name="qt" size="26" maxlength="100" id="searchText" alt="${searchCriteria}" /><img src="${spacerUrl}" width="4" height="1" alt="" border="0"><input type="image" name="submit" src="${arrowImg}" border="0" alt="${beginSearch}" /></td>
								<td align="${left}"><img src="${spacerUrl}" width="20" height="1" class="decoration" alt="" border="0"></td>
							</tr>
							<c:if test="${!empty searchSectionName}">
								<tr>
									<td></td>
									<td align="${left}" colspan="3" class="small"><div><input type="radio" value="1" name="hpl" checked="true" class="srchradbtn" id="sectionRadio" /><label for="sectionRadio"${dir}><span class="bold">${searchSectionName}</span></label><input type="radio" value="0" name="hpl" class="srchradbtn" id="countryRadio" /><label for="countryRadio"${dir}>${searchAllOf}</label></div></td>
								</tr>
							</c:if>
						</table>
						</form>						
					</c:otherwise>
				</c:choose>
			</td>
		</tr>
		</table>
	</td>
</tr>
</table>
<!-- End Search Area -->