<%-----------------------------------------------------------------------------
	cleansheet_layout_config.jsp
	
	This is the primary JSP file for including custom code to configure
	the HPWeb layout through the use of the HPCSModel request bean
	(com.hp.it.spf.portal.cleansheet.HPCSModel class).
	
	This style will be layout at the beginning of a HP Clean sheet grid, so it
	will be executed before any of the HP Clean sheet styles.

	See the HP Clean sheet Layout VAP Components Usage Guide for information on 
	HP Clean sheet layout customizations.
	
-----------------------------------------------------------------------------%>

<jsp:useBean id="HPCSModel" scope="request" 
		class="com.hp.it.spf.portal.cleansheet.HPCSModel" />

<%--
<jsp:setProperty name="HPCSModel" property="" value="" />
--%>