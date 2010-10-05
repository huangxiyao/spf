
<style text="text/css">
.calloutEnhancedExt {font-color: ${themeColor}; }

#horzNavButtonBar {background: ${themeColor}; }

#horzNavHelpButton{background: ${themeColor}; }

.horzNavButton a.topMenuItem.menuNormal {background-color: ${themeColor}; }
<%-- the following style has to go after the above style, and not before it. --%>
.horzNavButton a.topMenuItem.active {background-color: #FFFFFF; }

#horzNavButtonBarBackground {background: ${themeColor}; }
</style>

<%--  Add extra right margin property for IE6 to fix missing right bounding box
	border.   Do the opposite for a rtl language.  The isRTL variable is 
	defined in the hpweb grid component. 
--%>
							
<c:choose>
<c:when test="${isRTL}">

<!--[if IE 6]>
<style type="text/css">
.lastColumnPortlet #headerAlphaBoundingBox,
.lastColumnPortlet #headerBetaBoundingBox,
.lastColumnPortlet #headerBetaBevelBoundingBox,
.lastColumnPortlet #introBlock {margin-left:10px; }
</style>
<![endif]-->

</c:when>
<c:otherwise>

<!--[if IE 6]>
<style type="text/css">
.lastColumnPortlet #headerAlphaBoundingBox,
.lastColumnPortlet #headerBetaBoundingBox,
.lastColumnPortlet #headerBetaBevelBoundingBox,
.lastColumnPortlet #introBlock {margin-right:10px; }
</style>
<![endif]-->

</c:otherwise>
</c:choose>
				
<%-- Add rtl horz navigation style properties to handle rtl languages --%>

<c:choose>
<c:when test="${isRTL}">

<style text="text/css">
.horzNavButton {float: right; border-left: 1px solid #FFFFFF; }

#horzNavHelpButton {float: left; padding-right: 0; padding-left: 10px; text-align:left; }

.horzNavButton a.dropMenuItem {text-align: right; }
</style>

</c:when>
<c:otherwise>

<style text="text/css">
.horzNavButton {border-right: 1px solid #FFFFFF; }
</style>

</c:otherwise>
</c:choose>

<%-- Styles use when javascript is disabled --%>

<noscript>
<style text="text/css">
.horzNavButton a.topMenuItem {text-decoration: underline; }
</style>
</noscript>

