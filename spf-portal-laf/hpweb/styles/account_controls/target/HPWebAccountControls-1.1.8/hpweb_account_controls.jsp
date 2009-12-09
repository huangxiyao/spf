<%-----------------------------------------------------------------------------
	hpweb_account_controls.jsp
	
	STYLE: HPWeb Account Controls
	STYLE TYPE: Account Controls
	USES HEADER FILE: Yes

	Displays the user welcome message and locale controls.
-----------------------------------------------------------------------------%>


<%----------------------------------------------------------------------------- 
	Imports
-----------------------------------------------------------------------------%>

<jsp:directive.page import="com.hp.frameworks.wpa.portal.hpweb.Utils" />


<%----------------------------------------------------------------------------- 
	Tag libraries
-----------------------------------------------------------------------------%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="hpweb" uri="http://frameworks.hp.com/wpa/hpweb-core-enhanced" %>

<%----------------------------------------------------------------------------- 
	Style Arguments
-----------------------------------------------------------------------------%>

<%-- boolean widePage --%>
<c:set var="widePageArg" value="${widePage}" />

<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>

<%-- Only support stretch --%>
<c:set var="stretch" value="true" />

<jsp:scriptlet>

String localeSelectorDef = Utils.getI18nValue(i18nID, "hpweb.localeSelector", 
			portalContext);

</jsp:scriptlet>

<jsp:useBean id="HPWebModel" scope="request" 
		class="com.hp.frameworks.wpa.portal.hpweb.HPWebModel" />
		
<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<c:if test="${stretch}">

	<hpweb:welcome>
		<jsp:attribute name="username">${HPWebModel.username}</jsp:attribute>
		<jsp:attribute name="signInUrl">${HPWebModel.signInUrl}</jsp:attribute>
		<jsp:attribute name="signOutUrl">${HPWebModel.signOutUrl}</jsp:attribute>
		<jsp:attribute name="registerUrl">${HPWebModel.registerUrl}</jsp:attribute>
		<jsp:attribute name="profileUrl">${HPWebModel.profileUrl}</jsp:attribute>
		<jsp:attribute name="localeSelector"><c:out value="${HPWebModel.localeSelector}" default="<%= localeSelectorDef %>" escapeXml="false" /></jsp:attribute>
		<jsp:attribute name="wide">${widePageArg}</jsp:attribute>
	</hpweb:welcome>
	
</c:if>
