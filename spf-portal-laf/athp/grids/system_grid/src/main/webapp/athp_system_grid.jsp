<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%-----------------------------------------------------------------------------
	athp_system_grid.jsp
	
	GRID: @hp Grid with no navigation

	For use with the @hp theme, this grid displays HPWeb header, footer, 
	and the page content.
-----------------------------------------------------------------------------%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="showVertNav" value="false" scope="request" />
<c:set var="showHorzNav" value="false" scope="request" />
		
<%@ include file="athp_grid.jsp" %>
