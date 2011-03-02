<%-----------------------------------------------------------------------------
	cleansheet_account_controls.jsp
	
	STYLE: HPWeb Cleansheet Account Controls
	STYLE TYPE: Account Controls
	USES HEADER FILE: Yes

	Displays the user welcome message and locale controls.
-----------------------------------------------------------------------------%>


<%----------------------------------------------------------------------------- 
	Imports
-----------------------------------------------------------------------------%>

<jsp:directive.page import="com.hp.frameworks.wpa.portal.hpweb.Utils" />
<jsp:directive.page import="com.hp.frameworks.wpa.portal.hpweb.HPWebModel" />


<%----------------------------------------------------------------------------- 
	Tag libraries
-----------------------------------------------------------------------------%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cscore" uri="http://frameworks.hp.com/wpa/cleansheet-core" %>

<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>
<jsp:useBean id="HPWebModel" scope="request" 
		class="com.hp.frameworks.wpa.portal.hpweb.HPWebModel" />

<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<cscore:welcome>
	<jsp:attribute name="username">${HPWebModel.username}</jsp:attribute>
	<jsp:attribute name="signInUrl">${HPWebModel.signInUrl}</jsp:attribute>
	<jsp:attribute name="signOutUrl">${HPWebModel.signOutUrl}</jsp:attribute>
	<jsp:attribute name="registerUrl">${HPWebModel.registerUrl}</jsp:attribute>
	<jsp:attribute name="profileUrl">${HPWebModel.profileUrl}</jsp:attribute>
</cscore:welcome>


