<%@page import="com.hp.spp.portal.common.site.SiteManager" %>
<head>
  <title>
    HP - Page Unavailable
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
  <table border="0" cellpadding="0" cellspacing="0" width="740">
    <tr>
      <td width="170" align="center" valign="middle">
        <img src="http://welcome.hp-ww.com/img/hpweb_1-2_topnav_hp_logo.gif" width="64" height="55" alt="HP.com home" border="0">
        <br>
      </td>
      <td width="10">
        <img src="http://welcome.hp-ww.com/img/s.gif" width="10" height="93" alt="">
      </td>
      <td width="560" align="left" valign="top">
        <img src="http://welcome.hp-ww.com/img/s.gif" width="1" height="20" alt="">
        <h1>We're very sorry!</h1>
      </td>
    </tr>
  </table>
  <!-- End Page Title Area -->
  <!-- Begin Left Navigation and Content Area.  To increase width of content area, modify width of table below. -->
  <table border="0" cellpadding="0" cellspacing="0" width="740">
    <tr>
      <td width="170" align="left" valign="top" >
      </td>
      <td valign="top" width="10">
        <a name="jumptocontent">
          <img src="http://welcome.hp-ww.com/img/s.gif" width="10" height="1" alt="Content starts here"></a>
      </td>
      <!-- Start Content Area.  To increase width of content area, modify width of table cell below. -->
      <td width="560" align="left" valign="top">
        <table border="0" cellpadding="0" cellspacing="0" width="560">
          <tr class="theme">
            <td align="left" valign="top" width="540">
              <h2 class="themeheader">
			  <%=SiteManager.getInstance().getSite(request.getParameter("siteName")).getSiteDownMessage()%>
              </h2>
            </td>
            <td align="left" width="10">
              <img src="http://welcome.hp-ww.com/img/s.gif" width="10" height="1" alt="">
            </td>
          </tr>
          <tr class="decoration">
            <td colspan="3">
              <img src="http://welcome.hp-ww.com/img/s.gif" width="1" height="20" alt="">
            </td>
          </tr>
        </table>
        <table width="560" border="0" cellspacing="0" cellpadding="0">
          <tr>
            <td class="bold">
            
            </td>
          </tr>
          <tr class="decoration">
            <td>
              <img src="http://welcome.hp-ww.com/img/s.gif" width="1" height="10" alt="">
            </td>
          </tr>
          <tr class="decoration">
            <td>
              <img src="http://welcome.hp-ww.com/img/s.gif" width="1" height="20" alt="">
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
