<%-- This JSP displays an SPF-standard system error based on error title, code 
and message attributes expected to have been set in the session by the 
forwarding action.  Defaults are provided if they were not set. --%>

<%@ page import="com.epicentric.template.Style"%>
<%@ page import="com.vignette.portal.util.WebUtils"%>
<%@ page import="com.hp.it.spf.xa.i18n.portal.I18nUtility"%>
<%@ page import="com.hp.it.spf.xa.exception.portal.ExceptionUtil"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.text.MessageFormat"%>

<%@ taglib uri="http://www.hp.com/spf/i18n/portal"
	prefix="spf-i18n-portal"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="vgn-tags" prefix="vgn-portal"%>

<vgn-portal:defineObjects />

<%-- Prepare general variables --%>
<%
    Style _thisStyleObject = portalContext.getCurrentStyle();
    String i18nID = _thisStyleObject.getUID();
	HashMap map = (HashMap)portalContext.getPortalRequest().getSession().getAttribute(ExceptionUtil.SESSION_ATTR_SYSTEM_ERROR_DATA);
%>

<%-- Prepare error message string --%>
<%
	// Get error message from session
	String errorMessage = null;
    if (map != null)
		errorMessage = (String)map.get(ExceptionUtil.ERROR_MESSAGE_ATTR);
	
    boolean defaultErrorMessage = false;
    if (errorMessage == null || errorMessage.length() == 0) {
        defaultErrorMessage = true;
		
		// Use default system error message.
		// Get display message from resource bundle.

		errorMessage = I18nUtility.getI18nValue(i18nID, 
			"default_error_message",
			"The page you were looking for is not available at this time.<br/>Please try again later.",
			portalContext);
	}
%>

<%-- Prepare error title string --%>
<%
	// Get error title from session
	String errorTitle = null;
    if (map != null)
		errorTitle = (String)map.get(ExceptionUtil.PAGE_TITLE_ATTR);
	
    boolean defaultErrorTitle = false;
    if (errorTitle == null || errorTitle.length() == 0) {
        defaultErrorTitle = true;
		
		// Use default system error message.
		// Get display message from resource bundle.

		errorTitle = I18nUtility.getI18nValue(i18nID, 
			"default_error_title",
			"Could not open page",
			portalContext);
	}
%>

<%-- Prepare error code string --%>
<%
	// Get error code from session.
	String errorCode = null;
	if (map != null)
		errorCode = (String)map.get(ExceptionUtil.ERROR_CODE_ATTR);

	// Use a default error code of "portappl-internal_error" 
	// if there is no error code and no customized error message 
	// to handle uncaught exceptions thrown by the portal application.
	
    if (errorCode == null || errorCode.length() == 0) {
    	if (defaultErrorMessage == true)
			errorCode = "portappl-internal_error";
    }
    
	// If there is no error code, then there's nothing to display.

	String errorCodeMessage = "";
    if (errorCode != null && errorCode.length() != 0) {
		errorCodeMessage = I18nUtility.getI18nValue(i18nID,
			"error_code_message_format",
			"(Error {0})",
			portalContext);
		errorCodeMessage =
			MessageFormat.format(errorCodeMessage, new String[] {errorCode});
	}
%>

<%-- Output page data --%>
<div style="padding-top: 10px; margin-left: 10px">
<h3 class="errorTitle"><%= errorTitle %></h3>

<p class="errorDescription"><%= errorMessage %></p>

<p class="errorCode"><%= errorCodeMessage %></p>
</div>
