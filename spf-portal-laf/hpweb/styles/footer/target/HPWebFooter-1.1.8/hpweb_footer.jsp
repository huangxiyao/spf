<%-----------------------------------------------------------------------------
	hpweb_footer.jsp
	
	STYLE: HPWeb Footer
	STYLE TYPE: Footer
	USES HEADER FILE: Yes

	Displays the privacy link, terms link, and optionally, the sales and
	service terms link, feedback link, and site map link.
	For German locale only, the imprint link is displayed.
	The Omniture metrics collection script will be inserted.

-----------------------------------------------------------------------------%>

<%-----------------------------------------------------------------------------
	Imports
-----------------------------------------------------------------------------%>

<%@ page import="com.hp.frameworks.wpa.portal.hpweb.Utils" %>

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
	Variables
-----------------------------------------------------------------------------%>

<%

String privacyUrlDef = Utils.getI18nValue(i18nID, "hpweb.privacyUrl",
			portalContext);
String imprintUrlDef = Utils.getI18nValue(i18nID, "hpweb.imprintUrl",
			portalContext);
String termsUrlDef = Utils.getI18nValue(i18nID, "hpweb.termsUrl",
			portalContext);
String saleTermsUrlDef = Utils.getI18nValue(i18nID, "hpweb.saleTermsUrl",
			portalContext);
String feedbackUrlDef = Utils.getI18nValue(i18nID, "hpweb.feedbackUrl",
			portalContext);
String feedbackTextDef = Utils.getI18nValue(i18nID, "hpweb.feedbackText",
			portalContext);
String metricsUrlDef = Utils.getI18nValue(i18nID, "hpweb.metricsUrl",
			portalContext);
String siteMapUrlDef = Utils.getI18nValue(i18nID, "hpweb.siteMapUrl",
			portalContext);

%>

<jsp:useBean id="HPWebModel" scope="request" 
		class="com.hp.frameworks.wpa.portal.hpweb.HPWebModel" />
		

<%-----------------------------------------------------------------------------
	Template
-----------------------------------------------------------------------------%>

<hpweb:footer>
	<jsp:attribute name="privacyUrl"><c:out value="${HPWebModel.privacyUrl}" default="<%= privacyUrlDef %>" escapeXml="false" /></jsp:attribute> 
	<jsp:attribute name="imprintUrl"><c:out value="${HPWebModel.imprintUrl}" default="<%= imprintUrlDef %>" escapeXml="false" /></jsp:attribute>
	<jsp:attribute name="termsUrl"><c:out value="${HPWebModel.termsUrl}" default="<%= termsUrlDef %>" escapeXml="false" /></jsp:attribute>		
	<jsp:attribute name="saleTermsUrl"><c:out value="${HPWebModel.saleTermsUrl}" default="<%= saleTermsUrlDef %>" escapeXml="false" /></jsp:attribute>
	<jsp:attribute name="feedbackUrl"><c:out value="${HPWebModel.feedbackUrl}" default="<%= feedbackUrlDef %>" escapeXml="false" /></jsp:attribute>
	<jsp:attribute name="feedbackText"><c:out value="${HPWebModel.feedbackText}" default="<%= feedbackTextDef %>" escapeXml="false" /></jsp:attribute>			
	<jsp:attribute name="metricsUrl"><c:out value="${HPWebModel.metricsUrl}" default="<%= metricsUrlDef %>" escapeXml="false" /></jsp:attribute>
	<jsp:attribute name="siteMapUrl"><c:out value="${HPWebModel.siteMapUrl}" default="<%= siteMapUrlDef %>" escapeXml="false" /></jsp:attribute>
	<jsp:attribute name="wide">${widePageArg}</jsp:attribute>
</hpweb:footer>
