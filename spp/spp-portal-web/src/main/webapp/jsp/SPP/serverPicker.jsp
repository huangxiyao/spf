<%@ page import="com.hp.spp.common.util.Environment"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.List"%>
<%@page import="java.util.Set" %>
<%@page import="com.hp.spp.portal.common.dao.CommonDAO" %>
<%@page import="com.hp.spp.portal.common.dao.CommonDAOCacheImpl" %>
<%@ page contentType="text/html;charset=UTF-8" session="false" %>
<%-- VAP 7.4 comes with JSTL 1.1 but SPP cannot upgrade to this version due to other deps; therefore
we continue to use JSTL 1.0 but have to update taglib URL, otherwise the runtime EL values are not allowed --%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%!
	private static final String HTTP_HP_URL = "http://welcome.hp-ww.com";
	private static final String HTTPS_HP_URL = "https://secure.hp-ww.com";

	private static final String ITG_URL = "sppitg.austin.hp.com";

	private static final String PRO_URL = "h20375.www2.hp.com";
%>
<%
	String target = request.getParameter("target");
	if (target != null) {
		response.sendRedirect(target + ";jsessionid=" + request.getSession().getId());
		return;
	}

	// If we get here this means it's not a session initialization redirection but the page to select the server
	boolean showAllServers = Boolean.valueOf(request.getParameter("showAll"));
	List allServers = Environment.singletonInstance.getManagedServersList();
	int serverCount = (showAllServers ? allServers.size() : 1);

	CommonDAO commonDao = CommonDAOCacheImpl.getInstance();
	Set supportedSites = commonDao.getSupportedSites();
	
	pageContext.setAttribute("imageServer", "http".equals(request.getScheme()) ? HTTP_HP_URL : HTTPS_HP_URL);
	pageContext.setAttribute("sessionExists", Boolean.valueOf(request.getSession(false) != null));
	pageContext.setAttribute("showAllServers", Boolean.valueOf(showAllServers));
	pageContext.setAttribute("targetUrl", "http://" + ("PROD".equals(Environment.singletonInstance.getType()) ? PRO_URL : ITG_URL));
	pageContext.setAttribute("servers", allServers.subList(0, serverCount));
	pageContext.setAttribute("supportedSites", supportedSites);

%>
<html>
<head>
  <title>
    SPP Server Picker
  </title>
  <meta http-equiv="Content-type" content="text/html; charset=ISO-8859-1">
  <meta http-equiv="Content-Style-Type" content="text/css">
  <meta http-equiv="Pragma" content="no-cache">
  <meta http-equiv="Cache-Control" content="no-cache">
  <meta http-equiv="Expires" content="-1">
</head>
<body class="colorFFFFFFbg" text="#000000" link="#003366" alink="#003366" vlink="#660066" marginheight="0" marginwidth="0" leftmargin="0" topmargin="0">
  <table border="0" cellpadding="0" cellspacing="0" width="740">
    <tr class="decoration">
    </tr>
  </table>
  <table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
      <td align="left" valign="top" bgcolor="#666666">
        <table border="0" cellpadding="0" cellspacing="0" width="720">
          <tr class="decoration">
          </tr>
        </table>
      </td>
    </tr>
  </table>
  <table border="0" cellpadding="0" cellspacing="0" width="100%">
    <tr>
      <td align="left" valign="top" bgcolor="#E7E7E7">
        <table border="0" cellpadding="0" cellspacing="0" width="740">
          <tr>
            <td width="20" valign="top">
              &nbsp;
            </td>
            <td width="150" align="left" valign="middle" class="color003366bld">
              &nbsp;
            </td>
            <td width="570" align="right">
          </tr>
        </table>
      </td>
    </tr>
  </table>
  <!-- Begin Page Title Area -->
  <table border="0" cellpadding="0" cellspacing="0" width="920">
    <tr>
      <td width="170" align="center" valign="middle">
        <img src="<c:out value='${imageServer}' />/img/hpweb_1-2_topnav_hp_logo.gif" width="64" height="55" alt="HP.com home" border="0">
        <br>
      </td>
      <td width="10">
        <img src="<c:out value='${imageServer}' />/img/s.gif" width="10" height="93" alt="">
      </td>
      <td width="740" align="left" valign="top">
        <img src="<c:out value='${imageServer}' />/img/s.gif" width="1" height="20" alt="">
        <h1>SPP Server Picker</h1>
      </td>
    </tr>
  </table>
  <!-- End Page Title Area -->
  <!-- Begin Left Navigation and Content Area.  To increase width of content area, modify width of table below. -->
  <table border="0" cellpadding="0" cellspacing="0" width="920">
    <tr>
      <td width="170" align="left" valign="top" >
		  &nbsp;
	  </td>
      <td valign="top" width="10">
        <a name="jumptocontent">
          <img src="<c:out value='${imageServer}' />/img/s.gif" width="10" height="1" alt="Content starts here"></a>
      </td>
      <!-- Start Content Area.  To increase width of content area, modify width of table cell below. -->
      <td width="740" align="left" valign="top">
        <table border="0" cellpadding="0" cellspacing="0" width="740">
          <tr class="theme">
            <td align="left" valign="top" width="730">
				<c:if test="${sessionExists}">
				<font color="red"><b>WARNING!</b><br />You have an existing session. Please close this window and access this URL using a new browser instance!</font>
				</c:if>
				<c:if test="${!sessionExists}">
					<c:forEach items="${servers}" var="server" varStatus="status">
						<p>
							<c:if test="${showAllServers}">
								<b>Server <c:out value="${server}"/> : </b>
							</c:if>
							<c:forEach items="${supportedSites}" var="supportedSite" varStatus="siteStatus">
							<c:if test="${(supportedSite != 'DUMMY_SITE') && (supportedSite != 'gpp-dev') && (supportedSite != 'console')}">
								<a href="<c:out escapeXml='false' value='http://${server}/portal/jsp/SPP/serverPicker.jsp?target=${targetUrl}/portal/site/public${supportedSite}/' />"><c:out value="${supportedSite}"/></a>&nbsp;&nbsp;
							</c:if>
							<c:if test="${(supportedSite == 'console')}">
								<a href="<c:out escapeXml='false' value='http://${server}/portal/jsp/SPP/serverPicker.jsp?target=${targetUrl}/portal/site/console/' />">Vignette Console</a>&nbsp;&nbsp;
							</c:if>
							</c:forEach>					
						</p>
					</c:forEach>
					<c:choose>
						<c:when test="${showAllServers}">
							<p>
								<a href="/portal/jsp/SPP/serverPicker.jsp">Show Only Primary Server</a>
							</p>
						</c:when>
						<c:otherwise>
							<p>
								<a href="/portal/jsp/SPP/serverPicker.jsp?showAll=true">Show All Servers</a>
							</p>
						</c:otherwise>
					</c:choose>
				</c:if>
            </td>
            <td align="left" width="10">
              <img src="<c:out value='${imageServer}' />/img/s.gif" width="10" height="1" alt="">
            </td>
          </tr>
          <tr class="decoration">
            <td colspan="3">
              <img src="<c:out value='${imageServer}' />/img/s.gif" width="1" height="20" alt="">
            </td>
          </tr>
        </table>
        <table width="740" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td class="bold">

            </td>
          </tr>
          <tr class="decoration">
            <td>
              <img src="<c:out value='${imageServer}' />/img/s.gif" width="1" height="10" alt="">
            </td>
          </tr>
          <tr class="decoration">
            <td>
              <img src="<c:out value='${imageServer}' />/img/s.gif" width="1" height="20" alt="">
            </td>
          </tr>
        </table>
      </td>
      <!-- End Content Area -->
      <!--stopindex-->
    </tr>
  </table>
  <!-- End Left Navigation and Content Area -->
</body>
</html>
