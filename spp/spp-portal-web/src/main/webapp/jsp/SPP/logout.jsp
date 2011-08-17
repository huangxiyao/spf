<%@page import="com.hp.spp.portal.simulation.audittrail.AuditTrailManager"%>

<%

// Abbreviation explaination:
// sn = SiteName
// ps = PerSist
// sd = SimulateD
// sr = SimulatoR
// mi = MenuItem

/**
 * @author Cyril MICOUD
 */

String siteName = request.getParameter("sn") ;

if(siteName != null) {
	String uri = "" ;
	uri = uri.concat(request.getContextPath()) ;
	uri = uri.concat("/site") ;
	uri = uri.concat("/public") ;
	uri = uri.concat(siteName) ;
	if(request.getParameter("mi") != null) {
		uri = uri.concat("/menuitem.") ;
		uri = uri.concat(request.getParameter("mi")) ;
	}
	uri = uri.concat("/") ;
	if(request.getParameter("lang") != null) {
		uri = uri.concat("?lang="+request.getParameter("lang").toLowerCase());
	}
	if(request.getParameter("cc") != null) {
		uri = uri.concat("&cc="+request.getParameter("cc"));
	}
	
	// Simulation Audit Trail...
	if(request.getParameter("ps") != null) {
		if(request.getParameter("ps").equals("true"))
			AuditTrailManager.keepSimulationForNextLogin(request.getParameter("sd"), request.getParameter("sr")) ;
		else
			AuditTrailManager.stopSimulationByLogout(request.getParameter("sd"), request.getParameter("sr")) ;
	}
	
	if(session != null)
		session.invalidate() ;
	session = request.getSession(true) ;
	response.sendRedirect(uri) ;
} else {
	out.println("Please contact and administrator to escalate this error!") ;
}
%>