<%@ tag pageEncoding="UTF-8" body-content="empty" %>

<%@ attribute name="username" type="java.lang.String" %>
<%@ attribute name="signInUrl" type="java.lang.String" %>
<%@ attribute name="signOutUrl" type="java.lang.String" %>
<%@ attribute name="profileUrl" type="java.lang.String" %>
<%@ attribute name="registerUrl" type="java.lang.String" %>
<%@ attribute name="myAccountUrl" type="java.lang.String" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="csfn" uri="http://frameworks.hp.com/wpa/cleansheet-fn" %>


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
<fmt:message var="myAccount" key="text.hpweb.myaccount" bundle="${msgResources}" />


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

<a class="screenReading" tabindex="1" href="#jumptocontent">${jumpcontent}</a>

<!-- style top_buyhp is defined in header_footer_v2.css by.hp.com. need to be customized locally  -->
<div class="top_buyhp" style="text-transform: none; height:18px;">
	<div class="top_wrapper">
	    <div class="top_left" style="font-size:11pt;padding-left:15px">${welcome}</div>	
			
		<c:if test="${!empty signInUrl}">
			<c:choose>
				<c:when test="${empty username}">
					<c:choose>
						<c:when test="${!empty myAccountUrl}">
							<div class="top_right"> »
								<a class="ribbon_link link_metrics" name="${signIn}" title="${signIn}" 
									href="${signInUrl}" tabindex="1" style="font-size:10pt;padding-right:32px">${signIn}</a>
							</div>
							<div class="top_right"> » 
								<a class="ribbon_link link_metrics" name="${myAccount}" title="${myAccount}" 
										href="${myAccountUrl}" tabindex="1" style="font-size:10pt;padding-right:25px">${myAccount}</a>
							</div>							
						</c:when>
						<c:otherwise>
							<div class="top_right"> »
								<a class="ribbon_link link_metrics" name="${register}" title="${register}" 
										href="${registerUrl}" tabindex="1" style="font-size:10pt;padding-right:32px">${register}</a>
							</div>							
							<div class="top_right"> »
								<a class="ribbon_link link_metrics" name="${signIn}" title="${signIn}" 
									href="${signInUrl}" tabindex="1" style="font-size:10pt;padding-right:25px">${signIn}</a>
							</div>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<c:choose>
						<c:when test="${!empty myAccountUrl}"> 
							<div class="top_right"> »
								<a class="ribbon_link link_metrics" name="${signOut}" title="${signOut}" 
									href="${signOutUrl}" tabindex="1" style="font-size:10pt;padding-right:32px">${signOut}</a>
							</div>
							<div class="top_right"> »
								<a class="ribbon_link link_metrics" name="${myAccount}" title="${myAccount}" 
										href="${myAccountUrl}" tabindex="1" style="font-size:10pt;padding-right:25px">${myAccount}</a>
							</div>							
						</c:when>
						<c:otherwise>
							<div class="top_right"> »
								<a class="ribbon_link link_metrics" name="${profile}" title="${profile}" 
										href="${profileUrl}" tabindex="1" style="font-size:10pt;padding-right:32px">${profile}</a>
							</div>							
							<div class="top_right"> »
								<a class="ribbon_link link_metrics" name="${signOut}" title="${signOut}" 
									href="${signOutUrl}" tabindex="1" style="font-size:10pt;padding-right:25px">${signOut}</a>
							</div>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</c:if>
	</div>
</div>