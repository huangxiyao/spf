<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%-----------------------------------------------------------------------------
	cleansheet_vert_nav_grid.jsp
	
	GRID: Cleansheet Grid with vertical navigation

	For use with the HPWeb theme, this grid displays HPWeb header, footer, 
	vertical navigation, and the page content.
-----------------------------------------------------------------------------%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="showVertNav" value="true" scope="request" />
<c:set var="showHorzNav" value="false" scope="request" />
		
<%@ include file="cleansheet_grid_inc.jsp" %>

