<%@ page
	import="com.hp.globalops.hppcbl.passport.PassportService,
	java.util.Map,
	java.util.Iterator,
	com.hp.spp.portal.simulate.bean.SimulateBean,
	com.hp.spp.profile.Constants,
	com.hp.globalops.hppcbl.passport.PassportServiceException,
	javax.servlet.http.Cookie"%>
<%@ taglib uri="vgn-tags" prefix="vgn-portal"%>

<vgn-portal:defineObjects />
<%
	//###########################################
	// Display the dummy search form
	//###########################################

	String errorMessage = null;
	String userId = "";
	if (null != request.getParameter("userId") && !"".equals(request.getParameter("userId")))
		userId = request.getParameter("userId");
%>

<form action="<%=portalContext.getCurrentPortalURI().toString()%>" method="POST">
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr class="theme">
		<th colspan="2" align="left" style="color: #FFFFFF;">Search</th>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td>User&nbsp;Id:&nbsp;</td>
		<td><input type="text" name="userId" value="<%=userId%>" /></td>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td><input type="submit" name="search" value="Search" /></td>
	</tr>
</table>
</form>

<%
	//###########################################
	// Display the dummy search result
	//###########################################

	if (null != request.getParameter("userId") && !"".equals(request.getParameter("userId"))) {
		PassportService ws = new PassportService();
		String profileId = null;
		try {
			profileId = ws.getProfileId(userId).getProfileId();
		} catch (PassportServiceException e) {

		}

		if (null != profileId) {

			Cookie[] cookie = request.getCookies();
			boolean isFind = false;
			String sessionToken = null;

			if (cookie != null) {
				int i = 0;
				while (i < cookie.length && !isFind) {
					isFind = cookie[i].getName().equals("SMSESSION");
					if (isFind)
						sessionToken = cookie[i].getValue();
					i++;
				}
			}
			
			Map userProfile = (Map) session.getAttribute(Constants.PROFILE_MAP) ;

			if (null != sessionToken) {
				SimulateBean bean = new SimulateBean();
				out.println("<!-- setProfileHPPId: "+profileId+" -->");
				bean.setSimulatedProfileHPPId(profileId);
				out.println("<!-- setUserProfile: "+userProfile+" -->");
				bean.setUserProfile(userProfile);
				out.println("<!-- setSessionId: "+sessionToken+" -->");
				bean.setSessionId(sessionToken);
				out.println("<!-- setRedirectURLName: "+"Success"+" -->");
				bean.setRedirectURLName("Success");
				out.println("<!-- setErrorURLName: "+"Error"+" -->");
				bean.setErrorURLName("Error");
%>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr class="theme">
		<th colspan="2" align="left" style="color: #FFFFFF;">Result</th>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td>Link&nbsp;version:&nbsp;</td>
		<%
				String url = null ;
				url = bean.getURLWithParameters() ;
				out.println("<!-- url: "+url+" -->");
				if(url == null)
					url = "" ;
		%>
		<td><a href="<%=url%>">Simulate</a></td>
	</tr>
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
	<tr>
		<td>POST&nbsp;method&nbsp;form&nbsp;version:&nbsp;</td>
		<td>
		<%
				String action = null ;
				action = bean.getActionURL() ;
				out.println("<!-- action: "+action+" -->");
				if(action == null)
					action = "" ;
		%>
		<form action="<%=action%>" method="POST">
		<%
				Map paramMap = null;
				//paramMap = new HashMap();
				paramMap = bean.getParamMap();
				Iterator iterator = paramMap.keySet().iterator();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					String value = (String) paramMap.get(key);
					out.println("<!-- "+key+": "+value+" -->");
					
					if(key == null)
						key = "" ;
					if(value == null)
						value = "" ;
		%> 
			<input type="hidden" name="<%=key%>" value="<%=value%>" /> 
		<%
				}
		%> 
			<input type="submit" name="simulate" value="Simulate" />
		</form>
		</td>
	</tr>
</table>

<%
			} else {
				errorMessage = "SessionToken cannot be resolved!";
			}
		} else {
			errorMessage = "ProfileId cannot be resolved!";
		}
	}

	if (null != errorMessage) {
%>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td colspan="2">&nbsp;</td>
	</tr>
</table>

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td colspan="2" style="color: #FFFFFF;"><%=errorMessage%></td>
	</tr>
</table>

<%
	}
%>
