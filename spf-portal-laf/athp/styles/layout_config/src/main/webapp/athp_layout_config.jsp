<%-----------------------------------------------------------------------------
	athp_layout_config.jsp
	
	This is the primary JSP file for including custom code to configure
	the @hp layout through the use of the AtHPModel request bean
	(com.hp.frameworks.wpa.portal.athp.model.AtHPModel class).
	
	This style will be layout at the beginning of a @hp grid, so it
	will be executed before any of the @hp styles.

	See the @hp Layout VAP Components Usage Guide for information on 
	@hp layout customizations.
	
-----------------------------------------------------------------------------%>

<%-- Retrieve request-scoped AtHPModel bean to set layout properties --%>

<jsp:useBean id="AtHPModel" scope="request" 
		class="com.hp.frameworks.wpa.portal.athp.AtHPModel" />

<%--
<jsp:setProperty name="AtHPModel" property="generateBreadcrumbs"
		value="true" />
--%>