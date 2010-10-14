<%-----------------------------------------------------------------------------
	cleansheet_locale_selector.jsp
	
	This is the primary JSP file for including custom code to configure
	the Cleansheet Locale Selector through the use of the HPWebModel request bean
	(com.hp.frameworks.wpa.portal.hpweb.model.HPWebModel class).
	
-----------------------------------------------------------------------------%>

<jsp:useBean id="HPWebModel" scope="request" 
		class="com.hp.frameworks.wpa.portal.hpweb.HPWebModel" />

<%@ include file="sample_cleansheet_locale_selector.jsp" %>

