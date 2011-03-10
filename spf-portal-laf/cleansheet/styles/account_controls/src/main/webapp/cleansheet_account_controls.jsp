<%-----------------------------------------------------------------------------
	cleansheet_account_controls.jsp
	
	STYLE: HP Cleansheet Account Controls
	STYLE TYPE: Account Controls
	USES HEADER FILE: Yes

	Displays the user welcome message and locale controls.
-----------------------------------------------------------------------------%>


<%----------------------------------------------------------------------------- 
	Imports
-----------------------------------------------------------------------------%>

<jsp:directive.page import="com.hp.it.spf.portal.cleansheet.Utils" />
<jsp:directive.page import="com.hp.it.spf.portal.cleansheet.HPCSModel" />


<%----------------------------------------------------------------------------- 
	Tag libraries
-----------------------------------------------------------------------------%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cscore" uri="http://frameworks.hp.com/spf/cleansheet-core" %>

<%----------------------------------------------------------------------------- 
	Local variables  
-----------------------------------------------------------------------------%>
<jsp:useBean id="HPCSModel" scope="request" 
		class="com.hp.it.spf.portal.cleansheet.HPCSModel" />

<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<cscore:welcome>
	<jsp:attribute name="username">${HPCSModel.username}</jsp:attribute>
	<jsp:attribute name="signInUrl">${HPCSModel.signInUrl}</jsp:attribute>
	<jsp:attribute name="signOutUrl">${HPCSModel.signOutUrl}</jsp:attribute>
	<jsp:attribute name="registerUrl">${HPCSModel.registerUrl}</jsp:attribute>
	<jsp:attribute name="profileUrl">${HPCSModel.profileUrl}</jsp:attribute>
</cscore:welcome>


