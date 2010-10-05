<%@ tag pageEncoding="UTF-8" body-content="empty"%>

<%@ attribute name="sectionTitle" type="java.lang.String" %>
<%@ attribute name="contactText" type="java.lang.String" %>
<%@ attribute name="contactUrl" type="java.lang.String" %>

<%@ attribute name="liveChatText" type="java.lang.String" %>
<%@ attribute name="liveChatUrl" type="java.lang.String" %>
<%@ attribute name="buyingOptText" type="java.lang.String" %>
<%@ attribute name="buyingOptUrl" type="java.lang.String" %>

<%@ attribute name="orderStatusText" type="java.lang.String" %>
<%@ attribute name="orderStatusUrl" type="java.lang.String" %>
<%@ attribute name="custSvcText" type="java.lang.String" %>
<%@ attribute name="custSvcUrl" type="java.lang.String" %>

<%@ attribute name="username" type="java.lang.String" %>
<%@ attribute name="signInUrl" type="java.lang.String" %>
<%@ attribute name="signOutUrl" type="java.lang.String" %>
<%@ attribute name="profileUrl" type="java.lang.String" %>
<%@ attribute name="registerUrl" type="java.lang.String" %>
<%@ attribute name="myAccountUrl" type="java.lang.String" %>
<%@ attribute name="cartText" type="java.lang.String" %>
<%@ attribute name="cartUrl" type="java.lang.String" %>
<%@ attribute name="cartItemText" type="java.lang.String" %>
<%@ attribute name="cartItemCount" type="java.lang.Integer" %>
<%@ attribute name="wide" type="java.lang.Boolean" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="hpwebfn" uri="http://frameworks.hp.com/wpa/hpweb-fn" %>


<%----------------------------------------------------------------------------- 
	Messages  
-----------------------------------------------------------------------------%>
<fmt:setBundle var="msgResources" basename="com.hp.frameworks.wpa.hpweb.Messages" />
<fmt:setBundle var="urlResources" basename="com.hp.frameworks.wpa.hpweb.Urls" />

<fmt:message var="home" key="text.hpweb2003.home" bundle="${msgResources}" />
<fmt:message var="homeUrl" key="link.hpweb2003.home" bundle="${urlResources}" />

<fmt:message var="signIn" key="text.hpweb2003.signin" bundle="${msgResources}" />
<fmt:message var="signOut" key="text.hpweb2003.signout" bundle="${msgResources}" />
<fmt:message var="register" key="text.hpweb2003.register" bundle="${msgResources}" />
<fmt:message var="profile" key="text.hpweb2003.profile" bundle="${msgResources}" />
<fmt:message var="myAccount" key="text.hpweb.myaccount" bundle="${msgResources}" />

<c:if test="${empty contactText}">
	<fmt:message var="contactText" key="text.hpweb2003.contact" bundle="${msgResources}" />
</c:if>

<c:if test="${empty contactUrl}">
	<fmt:message var="contactUrl" key="link.hpweb2003.contact" bundle="${urlResources}" />
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



<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<div id="stretchLogoWithPageTitleBlack">
	<div id="logo"><a href="${homeUrl}" title="${home}"><img src="${globalImgDir}hpweb_1-2_topnav_hp_logo.gif" width="64" height="55" alt="" border="0"><span class="screenReading">${home}</span></a></div>
	<div id="sectionTitle" <c:if test="${empty sectionTitle}">class="noSecTitle"</c:if>><h1>${sectionTitle}</h1><span id="stretchContactHP" class="colorFFFFFF">&raquo; <a href="${contactUrl}">${contactText}</a></span></div>

	<c:if test="${wide}">
		<div id="optLinks">
			
			<c:if test="${!empty liveChatUrl || !empty buyingOptUrl}">
				<div class="floatLeft <c:if test="${borderNeeded}">border</c:if>">
					<ul class="linksUnit colorFFFFFF">
						<c:if test="${!empty liveChatUrl}">
							<li>&raquo; <a href="${liveChatUrl}">${liveChatText}</a></li>
						</c:if>
						<c:if test="${!empty buyingOptUrl}">
							<li>&raquo; <a href="${buyingOptUrl}">${buyingOptText}</a></li>
						</c:if>
					</ul>
				</div>

				<c:set var="borderNeeded" value="true" />
			</c:if>

			<c:if test="${!empty orderStatusUrl || !empty custSvcUrl}">
				<div class="floatLeft <c:if test="${borderNeeded}">border</c:if>">
					<ul class="linksUnit colorFFFFFF">
						<c:if test="${!empty orderStatusUrl}">
							<li>&raquo; <a href="${orderStatusUrl}">${orderStatusText}</a></li>
						</c:if>
						<c:if test="${!empty custSvcUrl}">
							<li>&raquo; <a href="${custSvcUrl}">${custSvcText}</a></li>
						</c:if>
					</ul>
				</div>

				<c:set var="borderNeeded" value="true" />
			</c:if>

			<c:if test="${!empty signInUrl}">
				<div class="floatLeft <c:if test="${borderNeeded}">border</c:if>">
					<ul class="linksUnit colorFFFFFF">
						<c:choose>
							<c:when test="${empty username}">
								<c:choose>
									<c:when test="${!empty myAccountUrl}">
										<li>&raquo; <a href="${myAccountUrl}">${myAccount}</a></li>
										<li>&raquo; <a href="${signInUrl}">${signIn}</a></li>
									</c:when>
									<c:otherwise>
										<li>&raquo; <a href="${signInUrl}">${signIn}</a></li>
										<li>&raquo; <a href="${registerUrl}">${register}</a></li>
									</c:otherwise>
								</c:choose>
							</c:when>
							<c:otherwise>
								<c:choose>
									<c:when test="${!empty myAccountUrl}">
										<li>&raquo; <a href="${myAccountUrl}">${myAccount}</a></li>
										<li>&raquo; <a href="${signOutUrl}">${signOut}</a></li>
									</c:when>
									<c:otherwise>
										<li>&raquo; <a href="${signOutUrl}">${signOut}</a></li>
										<li>&raquo; <a href="${profileUrl}">${profile}</a></li>
									</c:otherwise>
								</c:choose>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>
				
				<c:set var="borderNeeded" value="true" />
			</c:if>
			
			<c:if test="${!empty cartUrl}">
				<div class="floatLeft <c:if test="${borderNeeded}">border</c:if>">
					<img width="14" height="13" border="0" alt="${cartText}" src="${globalImgDir}shopping_cart_icon_black.gif">
					&nbsp;<a href=${cartUrl} class="udrline">${cartText}</a>
					<br>
					<c:if test="${!empty cartItemText}">
						${hpwebfn:format(cartItemText, cartItemCount)}
					</c:if>									
				</div>
			</c:if>
		</div>
	</c:if>
</div>
