<%@ tag pageEncoding="UTF-8" body-content="empty" %>

<%@ attribute name="username" type="java.lang.String" %>
<%@ attribute name="signInUrl" type="java.lang.String" %>
<%@ attribute name="signOutUrl" type="java.lang.String" %>
<%@ attribute name="profileUrl" type="java.lang.String" %>
<%@ attribute name="registerUrl" type="java.lang.String" %>
<%@ attribute name="localeSelector" type="java.lang.String" %>
<%@ attribute name="wide" type="java.lang.Boolean" %>

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


<c:if test="${!empty username}">
	<fmt:message var="welcome" key="text.hpweb2003.welcome" bundle="${msgResources}">
		<fmt:param value="${username}" />
	</fmt:message>
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

<a class="screenReading" tabindex="1" href="#jumptocontent">${jumpcontent}</a>

<div id="headerTopBar">	

	<div id="hppWelcomeL">${welcome}</div>	
	
	<c:choose>
		<c:when test="${!empty localeSelector}">
			<div id="localeSelectorContainer">${localeSelector}</div>
		</c:when>
		<c:otherwise>
			<div id="ctryInd">${countryIndicator}</div>		
		</c:otherwise>
	</c:choose>

	<c:if test="${!wide}">
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
	</c:if>
	
</div>