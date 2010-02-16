<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-----------------------------------------------------------------------------
	athp_split_nav_grid.jsp
	
	GRID: @hp Grid with horizontal and vertical navigation

	For use with the @hp theme, this grid displays HPWeb header, footer, 
	horizontal and vertical navigation, and the page content.
-----------------------------------------------------------------------------%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="showVertNav" value="true" scope="request" />
<c:set var="showHorzNav" value="true" scope="request" />
		
<%@ include file="athp_grid.jsp" %>