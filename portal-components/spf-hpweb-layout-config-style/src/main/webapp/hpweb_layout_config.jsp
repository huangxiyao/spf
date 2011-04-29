<%-----------------------------------------------------------------------------
	hpweb_layout_config.jsp
	
	This is the primary JSP file for including custom code to configure
	the HPWeb layout through the use of the HPWebModel request bean
	(com.hp.frameworks.wpa.portal.hpweb.model.HPWebModel class).
	
	This style will be layout at the beginning of a HPWeb grid, so it
	will be executed before any of the HPWeb styles.

	See the HPWeb Layout VAP Components Usage Guide for information on 
	HPWeb layout customizations.
	
-----------------------------------------------------------------------------%>
<%@ page import="com.hp.it.spf.xa.misc.portal.RequestContext" %>
<jsp:useBean id="HPWebModel" scope="request" 
		class="com.hp.frameworks.wpa.portal.hpweb.HPWebModel" />

<%--
<jsp:setProperty name="HPWebModel" property="" value="" />
--%>
<!--
Diagnostic Context:
<%= RequestContext.getThreadInstance().getDiagnosticContext().getDataAsString() %>
-->