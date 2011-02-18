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


		
<jsp:scriptlet>
HPWebModel model = (HPWebModel)request.getAttribute("HPWebModel");

if (model == null ) {
	model = new HPWebModel();
}

model.setTagline("dummy tag line");

model.setUsername("John");
model.setSignInUrl("http://www.hp.com");
model.setSignOutUrl("http://www.hp.com");
model.setRegisterUrl("http://www.hp.com");
model.setProfileUrl("http://www.hp.com");

request.setAttribute("HPWebModel", model);

</jsp:scriptlet>

<%----------------------------------------------------------------------------- 
	Template  
-----------------------------------------------------------------------------%>

<cscore:welcome>
	<jsp:attribute name="username">John Doe</jsp:attribute>
	<jsp:attribute name="signInUrl">http://www.hp.com</jsp:attribute>
	<jsp:attribute name="signOutUrl">http://www.hp.com</jsp:attribute>
	<jsp:attribute name="registerUrl">http://www.hp.com</jsp:attribute>
	<jsp:attribute name="profileUrl">http://www.hp.com</jsp:attribute>
</cscore:welcome>

<%--
<cscore:welcome>
	<jsp:attribute name="username">${HPCSModel.username}</jsp:attribute>
	<jsp:attribute name="signInUrl">${HPCSModel.signInUrl}</jsp:attribute>
	<jsp:attribute name="signOutUrl">${HPCSModel.signOutUrl}</jsp:attribute>
	<jsp:attribute name="registerUrl">${HPCSModel.registerUrl}</jsp:attribute>
	<jsp:attribute name="profileUrl">${HPCSModel.profileUrl}</jsp:attribute>
</cscore:welcome>
--%>

