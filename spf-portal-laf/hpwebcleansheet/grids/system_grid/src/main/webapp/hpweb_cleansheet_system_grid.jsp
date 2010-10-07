<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%-----------------------------------------------------------------------------
	hpweb_cleansheet_system_grid.jsp
	
	GRID: HPWeb Cleansheet Grid with no horizontal nor vertical navigation

	For use with the HPWeb theme, this grid displays HPWeb header, footer, 
	and the page content.
-----------------------------------------------------------------------------%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="showVertNav" value="false" scope="request" />
<c:set var="showHorzNav" value="false" scope="request" />
		
<%@ include file="hpweb_cleansheet_grid_inc.jsp" %>

