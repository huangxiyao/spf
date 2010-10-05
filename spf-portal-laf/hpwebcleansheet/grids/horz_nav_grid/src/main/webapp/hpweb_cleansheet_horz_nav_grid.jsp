<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%-----------------------------------------------------------------------------
	hpweb_cleansheet_horz_nav_grid.jsp
	
	GRID: HPWeb Cleansheet Grid with horizontal navigation

	For use with the HPWeb theme, this grid displays HPWeb header, footer, 
	horizontal navigation, and the page content.
-----------------------------------------------------------------------------%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="showVertNav" value="false" scope="request" />
<c:set var="showHorzNav" value="true" scope="request" />
		
<%@ include file="hpweb_cleansheet_grid_inc.jsp" %>

