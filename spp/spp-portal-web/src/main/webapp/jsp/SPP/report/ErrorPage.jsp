<!-- ErrorPage.jsp -->
<%/*--
	@(#)ErrorPage.jsp 

	Shared Portal Platform.

	Revision History:
	------------------------------------------------------------------------------
	Ver        Modified By               Date                Notes
	------------------------------------------------------------------------------
	v1        ShivaShanker B            20-Sep-2006          Created

	Note :
		
--*/%>
<%@ page import="java.util.Collection"
    contentType="text/html; charset=UTF-8"
%>
			
			
<html>
	<head><title>Shared Portal Platform - Error Page </title><head>
	<body>
	<%              
        	String message = (String)request.getAttribute("Message");
        	out.println("<p> <font style='color:#FF0000;'> M E S S A G E : " + message + "</font></p>");        	
	%>
	</body>
</html>





