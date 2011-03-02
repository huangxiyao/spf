<%@ tag pageEncoding="UTF-8" body-content="empty" %>

<%@ attribute name="username" type="java.lang.String" %>
<%@ attribute name="signInUrl" type="java.lang.String" %>
<%@ attribute name="signOutUrl" type="java.lang.String" %>
<%@ attribute name="profileUrl" type="java.lang.String" %>
<%@ attribute name="registerUrl" type="java.lang.String" %>
<%@ attribute name="localeSelector" type="java.lang.String" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="csfn" uri="http://frameworks.hp.com/wpa/cleansheet-fn" %>


<%----------------------------------------------------------------------------- 
	Messages  
-----------------------------------------------------------------------------%>
<fmt:setBundle var="msgResources" basename="com.hp.it.spf.portal.cleansheet.Messages" />
<fmt:setBundle var="urlResources" basename="com.hp.it.spf.portal.cleansheet.Urls" />

<fmt:message var="jumpcontent" key="text.cleansheet.jumpcontent" bundle="${msgResources}" />
<fmt:message var="javascript" key="text.cleansheet.javascript" bundle="${msgResources}" />
<fmt:message var="javascriptUrl" key="link.cleansheet.javascript" bundle="${urlResources}" />

<fmt:message var="signInAlt" key="text.cleansheet.signinAlt" bundle="${msgResources}" />
<fmt:message var="signIn" key="text.cleansheet.signin" bundle="${msgResources}" />
<fmt:message var="signOut" key="text.cleansheet.signout" bundle="${msgResources}" />
<fmt:message var="register" key="text.cleansheet.register" bundle="${msgResources}" />
<fmt:message var="profile" key="text.cleansheet.profile" bundle="${msgResources}" />

<fmt:message var="countryIndicator" key="text.cleansheet.countryIndicator" bundle="${msgResources}" />

<fmt:message var="home" key="text.cleansheet.home" bundle="${msgResources}" />
<fmt:message var="prodServ" key="text.cleansheet.prodserv" bundle="${msgResources}" />
<fmt:message var="support" key="text.cleansheet.support" bundle="${msgResources}" />
<fmt:message var="solutions" key="text.cleansheet.solutions" bundle="${msgResources}" />
<fmt:message var="buy" key="text.cleansheet.buy" bundle="${msgResources}" />

<fmt:message var="homeUrl" key="link.cleansheet.home" bundle="${urlResources}" />
<fmt:message var="prodServUrl" key="link.cleansheet.prodserv" bundle="${urlResources}" />
<fmt:message var="supportUrl" key="link.cleansheet.support" bundle="${urlResources}" />
<fmt:message var="solutionsUrl" key="link.cleansheet.solutions" bundle="${urlResources}" />
<fmt:message var="buyUrl" key="link.cleansheet.buy" bundle="${urlResources}" />

<c:if test="${!empty username}">
	<fmt:message var="welcome" key="text.cleansheet.welcome" bundle="${msgResources}">
		<fmt:param value="${username}" />
	</fmt:message>
</c:if>

<c:choose>
	<c:when test="${pageContext.request.scheme eq 'https'}">	
		<fmt:message var="imgDir" key="link.cleansheet.secure_img_dir" bundle="${urlResources}" />	
		<fmt:message var="globalImgDir" key="link.cleansheet.secure_global_img_dir" bundle="${urlResources}" />
	</c:when>
	<c:otherwise>
		<fmt:message var="imgDir" key="link.cleansheet.img_dir" bundle="${urlResources}" />
		<fmt:message var="globalImgDir" key="link.cleansheet.global_img_dir" bundle="${urlResources}" />
	</c:otherwise>
</c:choose>


<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>

<c:choose>
	<c:when test="${csfn:isLocaleLTR(urlResources)}">	
		<c:set var="hppServiceImg" value="${globalImgDir}hppserviceid.gif" />	
	</c:when>
	<c:otherwise>
		<c:set var="hppServiceImg" value="${globalImgDir}hppserviceid_reverse.gif" />
	</c:otherwise>
</c:choose>


<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<noscript><a href="${javascriptUrl}">${javascript}</a></noscript>

<a class="screenReading" href="#jumptocontent">${jumpcontent}</a>

<div id="topBar">	

	<c:choose>
		<c:when test="${!empty localeSelector}">
			${localeSelector}
		</c:when>
		<c:otherwise>
			<div id="ctryInd">${countryIndicator}</div>		
		</c:otherwise>
	</c:choose>
	
	<div id="hppSignIn">
		<c:if test="${!empty signInUrl}">
			<c:choose>
				<c:when test="${!empty username}">
					&raquo;&nbsp;<a href="${signOutUrl}" class="small">${signOut}</a>&nbsp;<span class="color666666">|</span>&nbsp;&raquo;&nbsp;<a href="${profileUrl}" class="small">${profile}</a>
				</c:when>
				<c:otherwise>
					<img src="${hppServiceImg}" alt="${signInAlt}" style="display:inline;" /><a href="${signInUrl}" class="small udrlinebold">${signIn}</a><c:if test="${!empty registerUrl}">&nbsp;<span class="color666666">|</span>&nbsp;&raquo;&nbsp;<a href="${registerUrl}" class="small">${register}</a></c:if>
				</c:otherwise>
			</c:choose>
		</c:if>
	</div>
	
	<div id="hppWelcome">${welcome}</div>		
</div>

<div id="topNav">
	<a href="${homeUrl}"><img src="${imgDir}top/hpweb_1-2_topnav_home.gif" width="100" height="24" border="0" alt="${home}"></a><a href="${prodServUrl}" target="_top"><img src="${imgDir}top/hpweb_1-2_topnav_prdsrv.gif" width="166" height="24" border="0" alt="${prodServ}"></a><a href="${supportUrl}" target="_top"><img src="${imgDir}top/hpweb_1-2_topnav_supprt.gif" width="163" height="24" border="0" alt="${support}"></a><a href="${solutionsUrl }" target="_top"><img src="${imgDir}top/hpweb_1-2_topnav_slutns.gif" width="143" height="24" border="0" alt="${solutions}"></a><a href="${buyUrl}" target="_top"><img src="${imgDir}top/hpweb_1-2_topnav_buy.gif" width="143" height="24" border="0" alt="${buy}"></a></div>

