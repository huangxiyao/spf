<%-----------------------------------------------------------------------------
	hpweb_chromeless.jsp
	
	STYLE: HPWeb portlet chromeless
	STYLE TYPE: Portlet Chrome
	USES HEADER FILE: Yes

	Displays the frame around the portlet window.
	Controls and portlet modes are not supported.

-----------------------------------------------------------------------------%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="style" value="callout" scope="request" />
		
<%@ include file="hpweb_chrome_inc.jsp" %>
