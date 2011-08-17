<%@ page import="com.hp.spp.profile.Constants,
 com.hp.spp.portal.login.dao.LoginDAO,
 com.hp.spp.portal.login.dao.LoginDAOCacheImpl" %>
<%@ page import="com.hp.spp.config.Config"%>
<%@ page import="java.util.regex.Pattern"%>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<portlet:defineObjects />

<script language="JavaScript" type="text/javascript">
<!--
	function submitLogin(){
		document.getElementById("msg").innerHTML = document.getElementById("LayerLogin").innerHTML;
		//document.getElementById('LayerLogin').style.display = "block"
		document.forms['login'].submit();
	}
//-->
</script>

<!-- determination of hpappid and target. -->
<%!
	//FIXME (slawek) 2008/02/24
	//
	// This is a fix to prevent the caching of landing page with TARGET parameter value containing spppro.austin.hp.com.
	// This resulted in the user ending up on a site using the load balancer url (spppro...) and then
	// the GPP e-services that validate HTTP_REFERER header were failing preventing the users to access them.
	//
	// To avoid the full web app deployment the change has been done in this JSP file - this way we only need
	// to replace this file.
	// For the next release this code should be moved to the LoginPortlet class!!!

	private static final Pattern PRO_LOAD_BALANCER_HOSTNAME = Pattern.compile("spppro\\.austin\\.hp\\.com");
	private static final String PRO_LICENSE_PLATE_HOSTNAME = "h20375.www2.hp.com";

	public static String replaceProLoadBalancerHostnameWithLicensePlate(String target) {
		if (target != null) {
			return PRO_LOAD_BALANCER_HOSTNAME.matcher(target).replaceAll(PRO_LICENSE_PLATE_HOSTNAME);
		}
		return target;
	}
%>
<%
	// hpappid
	String hpappid = (String) renderRequest.getAttribute(Constants.HPAPPID);
	String target = replaceProLoadBalancerHostnameWithLicensePlate((String) renderRequest.getAttribute(Constants.TARGET));
	String urlConfigFcc = (String) renderRequest.getAttribute("LOGIN_FORM_URI");
	// static resource location
	String resourcePath = Config.getValue("SPP.StaticResourcesBaseUri", "/portal/spp");

	String userName = (String) renderRequest.getAttribute(Constants.USER_ID);
	String password = (String) renderRequest.getAttribute(Constants.PASSWORD);
	String submit = (String) renderRequest.getAttribute(Constants.SUBMIT) + " ";
	String waitMessage = (String) renderRequest.getAttribute(Constants.WAIT_MESSAGE);

	// retrieve initial parameters and attributes
	String displayError = (String) renderRequest.getParameter(Constants.DISPLAY_ERROR);
	String errorCode = (String) renderRequest.getParameter(Constants.ERROR);
	String localeCode = (String) request.getAttribute(Constants.PREFIX_VGN_LOGINPORTLET + Constants.MAP_LANGUAGE);
	String site = (String) renderRequest.getAttribute(Constants.MAP_SITE);

	  // retrieve error message
	LoginDAO loginDAO = LoginDAOCacheImpl.getInstance();
	String error = loginDAO.getCustomErrorMessage(site, errorCode, localeCode);
	if (error == null) {
		error = loginDAO.getMessageFromLabel(errorCode, localeCode, site);
	}
	if (error == null) {
		error = loginDAO.getMessageFromLabel(errorCode, localeCode);
	}
    if(error == null){
       error = loginDAO.getMessageFromLabel(errorCode, Constants.DEFAULT_LANGUAGE.substring(0,2)); 
    }

    // if error message, display
	boolean shouldDisplayErrorMessage = error != null && !Constants.NOMESSAGE.equals(error) &&
			(displayError == null || Boolean.TRUE.equals(Boolean.valueOf(displayError)));
%>

<!-- login form -->
<form action="<%=urlConfigFcc%>" method="post" name="login" >
	<input type="hidden" name="HPAPPID" value="<%=hpappid%>"/>
	<input type="hidden" name="TARGET" value="<%=target%>">
  <table>
    <tr>
      <td><%=userName%></td>
    </tr>
    <tr>
      <td><input type="text" name="USER" id="user" tabindex="1" maxlength="60" style="width:140px"/></td>
    </tr>
    <tr>
      <td><%=password%></td>
    </tr>
    <tr>
      <td><input type="password" name="PASSWORD" id="password" tabindex="2" maxlength="32" style="width:140px"/></td>
    </tr>
  </table>
  <br />
  <table>
    <tr>
      <td><input type="submit" value="<%=submit%>&raquo;" class="primButton" tabindex="3" onclick="submitLogin();"/></td>
    </tr>
  </table>
</form>

<!-- display error message -->
<%
	if (shouldDisplayErrorMessage){
%>
<table>
  <tr>
	<td style="color:#FF0000;">
	  <%=error%>
		</td>
	</tr>
</table>
<%
  }  
%>
<div id="msg">
</div>
<div id='LayerLogin'  style='display: none;'><%=waitMessage%>&nbsp; <img src="<%=resourcePath%>/img/progress.gif"/></div>
