<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%-----------------------------------------------------------------------------
	hpweb_vert_nav_grid.jsp
	
	GRID: HPWeb Grid with vertical navigation

	For use with the HPWeb theme, this grid displays HPWeb header, footer, 
	vertical navigation, and the page content.
-----------------------------------------------------------------------------%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="showVertNav" value="true" scope="request" />
<c:set var="showHorzNav" value="false" scope="request" />
		
<%@ include file="hpweb_grid_inc.jsp" %>

