<%@ tag pageEncoding="UTF-8" body-content="empty" %>

<%@ attribute name="username" type="java.lang.String" %>
<%@ attribute name="signInUrl" type="java.lang.String" %>
<%@ attribute name="signOutUrl" type="java.lang.String" %>
<%@ attribute name="profileUrl" type="java.lang.String" %>
<%@ attribute name="registerUrl" type="java.lang.String" %>
<%@ attribute name="localeSelector" type="java.lang.String" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hpwebfn" uri="http://frameworks.hp.com/wpa/hpweb-fn" %>


<%----------------------------------------------------------------------------- 
	Messages  
-----------------------------------------------------------------------------%>
<fmt:setBundle var="msgResources" basename="com.hp.frameworks.wpa.hpweb.Messages" />
<fmt:setBundle var="urlResources" basename="com.hp.frameworks.wpa.hpweb.Urls" />

<fmt:message var="jumpcontent" key="text.hpweb2003.jumpcontent" bundle="${msgResources}" />
<fmt:message var="javascript" key="text.hpweb2003.javascript" bundle="${msgResources}" />
<fmt:message var="javascriptUrl" key="link.hpweb2003.javascript" bundle="${urlResources}" />

<fmt:message var="signInAlt" key="text.hpweb2003.signinAlt" bundle="${msgResources}" />
<fmt:message var="signIn" key="text.hpweb2003.signin" bundle="${msgResources}" />
<fmt:message var="signOut" key="text.hpweb2003.signout" bundle="${msgResources}" />
<fmt:message var="register" key="text.hpweb2003.register" bundle="${msgResources}" />
<fmt:message var="profile" key="text.hpweb2003.profile" bundle="${msgResources}" />

<fmt:message var="countryIndicator" key="text.hpweb2003.countryIndicator" bundle="${msgResources}" />

<fmt:message var="home" key="text.hpweb2003.home" bundle="${msgResources}" />
<fmt:message var="prodServ" key="text.hpweb2003.prodserv" bundle="${msgResources}" />
<fmt:message var="support" key="text.hpweb2003.support" bundle="${msgResources}" />
<fmt:message var="solutions" key="text.hpweb2003.solutions" bundle="${msgResources}" />
<fmt:message var="buy" key="text.hpweb2003.buy" bundle="${msgResources}" />

<fmt:message var="homeUrl" key="link.hpweb2003.home" bundle="${urlResources}" />
<fmt:message var="prodServUrl" key="link.hpweb2003.prodserv" bundle="${urlResources}" />
<fmt:message var="supportUrl" key="link.hpweb2003.support" bundle="${urlResources}" />
<fmt:message var="solutionsUrl" key="link.hpweb2003.solutions" bundle="${urlResources}" />
<fmt:message var="buyUrl" key="link.hpweb2003.buy" bundle="${urlResources}" />

<c:if test="${!empty username}">
	<fmt:message var="welcome" key="text.hpweb2003.welcome" bundle="${msgResources}">
		<fmt:param value="${username}" />
	</fmt:message>
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
		<c:set var="hppServiceImg" value="${globalImgDir}hppserviceid.gif" />	
	</c:when>
	<c:otherwise>
		<c:set var="left" value="right" />
		<c:set var="right" value="left" />
		<c:set var="dir" value=" dir=\"rtl\"" />
		<c:set var="hppServiceImg" value="${globalImgDir}hppserviceid_reverse.gif" />
	</c:otherwise>
</c:choose>


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<!-- Begin Top Navigation Area -->
<table border="0" cellpadding="0" cellspacing="0" width="740">
<tr class="decoration">
	<td width="10"><a href="#jumptocontent"><img src="${spacerUrl}" width="10" height="1" alt="${jumpcontent}" border="0"></a><noscript><a href="${javascriptUrl}">${javascript}</a></noscript></td>
	<td align="left" width="260" class="smallbold"${dir}>${welcome}</td>
	<td><img src="${spacerUrl}" width="10" height="24" alt="" border="0"></td>
	<td align="${left}" width="260" class="color003366">
		<c:if test="${!empty signInUrl}">
			<c:choose>
				<c:when test="${!empty username}">
					&raquo;&nbsp;<a href="${signOutUrl}" class="small"${dir}>${signOut}</a>&nbsp;<span class="color666666">|</span>&nbsp;&raquo;&nbsp;<a href="${profileUrl}" class="small"${dir}>${profile}</a>
				</c:when>
				<c:otherwise>
					<img src="${hppServiceImg}" alt="${signInAlt}" style="display:inline;" /><a href="${signInUrl}" class="small udrlinebold"${dir}>${signIn}</a><c:if test="${!empty registerUrl}">&nbsp;<span class="color666666">|</span>&nbsp;&raquo;&nbsp;<a href="${registerUrl}" class="small"${dir}>${register}</a></c:if>
				</c:otherwise>
			</c:choose>
		</c:if>
	</td>
	<c:choose>
		<c:when test="${!empty localeSelector}">
			<td align="${right}"${dir}>${localeSelector}</td>
		</c:when>
		<c:otherwise>
				<td align="${right}" width="180" class="countryInd">${countryIndicator}</td>
				<td><img src="${spacerUrl}" width="20" height="1" alt="" border="0"></td>		
		</c:otherwise>
	</c:choose>
	
</tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
<tr>
	<td align="${left}" valign="top" bgcolor="#666666">
		<table border="0" cellpadding="0" cellspacing="0" width="720">
		<tr class="decoration">
			<td><a href="${homeUrl}"><img src="${imgDir}top/hpweb_1-2_topnav_home.gif" width="100" height="24" border="0" alt="${home}"></a></td>	
			<td class="colorE7E7E7bg"><img src="${spacerUrl}" width="1" height="1" alt=""></td>
			<td><a href="${prodServUrl}" target="_top"><img src="${imgDir}top/hpweb_1-2_topnav_prdsrv.gif" width="166" height="24" border="0" alt="${prodServ}"></a></td>
			<td class="colorE7E7E7bg"><img src="${spacerUrl}" width="1" height="1" alt=""></td>
			<td><a href="${supportUrl}" target="_top"><img src="${imgDir}top/hpweb_1-2_topnav_supprt.gif" width="163" height="24" border="0" alt="${support}"></a></td>
			<td class="colorE7E7E7bg"><img src="${spacerUrl}" width="1" height="1" alt=""></td>
			<td><a href="${solutionsUrl }" target="_top"><img src="${imgDir}top/hpweb_1-2_topnav_slutns.gif" width="143" height="24" border="0" alt="${solutions}"></a></td>
			<td class="colorE7E7E7bg"><img src="${spacerUrl}" width="1" height="1" alt=""></td>
			<td><a href="${buyUrl}" target="_top"><img src="${imgDir}top/hpweb_1-2_topnav_buy.gif" width="143" height="24" border="0" alt="${buy}"></a></td>
			<td class="colorE7E7E7bg"><img src="${spacerUrl}" width="1" height="1" alt=""></td>			
		</tr>
		</table>
	</td>
</tr>
</table>
<!-- End Top Navigation Area -->