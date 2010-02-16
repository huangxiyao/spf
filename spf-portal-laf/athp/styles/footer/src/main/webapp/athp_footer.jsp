<%-----------------------------------------------------------------------------
	athp_footer.jsp
	
	STYLE: @hp Footer
	STYLE TYPE: Footer
	USES HEADER FILE: Yes

	Displays the privacy link, terms link, feedback link, support link, 
	and HP copyright statement.

-----------------------------------------------------------------------------%>


<%-----------------------------------------------------------------------------
	Imports
-----------------------------------------------------------------------------%>

<jsp:directive.page import="com.epicentric.common.website.I18nUtils" />


<%----------------------------------------------------------- TAG LIBRARIES--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%----------------------------------------------------------------- SCRIPT --%>

<jsp:useBean id="AtHPModel" scope="request" 
        class="com.hp.frameworks.wpa.portal.athp.AtHPModel" />

<jsp:scriptlet>

// Use layout properties from AtHPModel bean, with defaults from message properties.

pageContext.setAttribute("feedbackUrlDef", I18nUtils.getValue(i18nID, "athp.feedbackUrl", "",
			false, request));
pageContext.setAttribute("supportUrlDef", I18nUtils.getValue(i18nID, "athp.supportUrl", "",
			false, request));
pageContext.setAttribute("targetFrameDef", I18nUtils.getValue(i18nID, "athp.targetFrame", "",
			false, request));
pageContext.setAttribute("confidentialLabelDef", I18nUtils.getValue(i18nID, "athp.confidentialLabel", "",
			false, request));
pageContext.setAttribute("publicLabelDef", I18nUtils.getValue(i18nID, "athp.publicLabel", "",
			false, request));
pageContext.setAttribute("revisionDateDisplayedDef", I18nUtils.getValue(i18nID, "athp.revisionDateDisplayed", "",
			false, request));
pageContext.setAttribute("versionNumberDef", I18nUtils.getValue(i18nID, "athp.versionNumber", "",
			false, request));

</jsp:scriptlet>

<%----------------------------------------------------------------- MARKUP --%>

<script type="text/javascript">

	<c:if test="${!empty AtHPModel.versionNumber || ! empty versionNumberDef}">
		setVersionNumber("<c:out value="${AtHPModel.versionNumber}" default="${versionNumberDef}" />");
	</c:if>
	
	<c:if test="${!empty AtHPModel.feedbackUrl || ! empty feedbackUrlDef}">
		setFooterFeedbackPage("<c:out value="${AtHPModel.feedbackUrl}" default="${feedbackUrlDef}"/>");
	</c:if>	
	
	<c:if test="${!empty AtHPModel.supportUrl || ! empty supportUrlDef}">
		setLocalSupportPage("<c:out value="${AtHPModel.supportUrl}" default="${supportUrlDef}"/>");
	</c:if>
	
	<c:if test="${!empty AtHPModel.targetFrame || ! empty targetFrameDef}">
		setTargetFrame("<c:out value="${AtHPModel.targetFrame}" default="${targetFrameDef}" />");
	</c:if>		
	
	<c:if test="${AtHPModel.publicLabel} || publicLabelDef eq 'true'">
		setPagePublic();	
	</c:if>
	
	<c:if test="${AtHPModel.confidentialLabel || confidentialLabelDef eq 'true'}">
		setPageConfidential();	
	</c:if>	
	
	<c:if test="${AtHPModel.revisionDateDisplayed || revisionDateDisplayedDef eq 'true'}">
		displayPageRevision();	
	</c:if>	
	
	drawFooter();
</script>
