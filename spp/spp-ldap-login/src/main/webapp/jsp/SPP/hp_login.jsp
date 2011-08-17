<%@ page contentType="text/html;charset=UTF-8" %>
<%
	String errorCode = request.getParameter("errorCode");
%>
<html>
<head>
	<style type="text/css">
		body { font-family: Arial,Verdana,Helvetica; }
		.leftBanner { padding: 5px; background-color: rgb(13, 83, 190); color: white; text-align: right; font-size: 1.3em; vertical-align: top; }
		.help { background-color:rgb(153, 153, 153); vertical-align:middle; }
		.help a span { color:white; font-size:10px; font-family:Arial; text-decoration:none; text-align:right; }
		.formContent { background-color:rgb(227, 227, 227); }
		.formContent td { padding: 5px; font-size: 0.8em; }
		.primButton { padding-left: 6px; background-color: rgb(51, 51, 51); border-left-color: rgb(153, 153, 153); border-top-color: rgb(153, 153, 153); font-weight: bold; color: white; font-family: Arial,Verdana,Helvetica,Sans-serif,serif; }
		.notice td { text-align:justify; font-size: 0.6em; }
		.error { border: 2px dashed red; padding: 10px; font-size: 0.8em; }
	</style>
</head>
<body onload="document.forms['login_form'].elements['email'].focus();">
<form action="url_access_security_check" method="POST" name="login_form">
<table width="100%">
	<tr>
		<td height="10px">&nbsp;</td>
	</tr>
	<tr>
		<td align="center">
			<table width="535" cellpadding="0" cellspacing="5" border="0">
				<% if ("AuthenticationFailed".equals(errorCode)) { %>
				<tr>
					<td class="error" colspan="2">
						<p>
							<b>A log-on error occurred</b>
						</p>
						<p>
							Either your e-mail address or password is incorrect.
							<ul>
								<li>check the accuracy of your typing</li>
								<li>your e-mail address may not be formatted correctly</li>
								<li>your Caps Lock key may be on</li>
							</ul>
							For more information refer to Help or contact your helpdesk for further assistance.<br />
							Please try again.
						</p>
					</td>
				</tr>
				<% } else if ("AuthorizationFailed".equals(errorCode)) { %>
				<tr>
					<td class="error" colspan="2">
						<p>
							<b>Authorization error occurred</b>
						</p>
						<p>
							You are not authorized to access this page.
						</p>
					</td>
				</tr>
				<% } %>
				<tr>
					<td width="25%" class="leftBanner" rowspan="2">
						Administration<br />Log On
					</td>
					<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="help">
							<tr valign="middle">
								<td width="100%">&nbsp;</td>
								<!--
								<td nowrap="nowrap">
									   <a href="javascript:alert('help')"><img src="https://login.portal.hp.com/smlogin/images/qmark.gif" alt="Log On Help" border="0"/></a>
								</td>
								<td>&nbsp;</td>
								-->
								<td nowrap="nowrap">
									<a href="hp_login_help.html" target="_blank"><span>Help</span></a>
								</td>
								<td>&nbsp;</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="formContent">
							<tr>
								<td>
									<p>
										<b>Access to this administration feature requires additional security control.</b>
									</p>
									<p>
										Please fill in the login form below with your "primary" HP e-mail address, which is
										commonly your name (firstname.lastname) followed by "@hp.com".<br />
										The password is the password you use to log your PC onto the HP network.
									</p>
									<p>
										<label for="email">E-mail Address:</label><br />
										<input type="text" name="email" id="email" maxlength="75" style="width:100%" /><br />
										<small>Format: sally.smith@hp.com</small>
									</p>
									<p>
										<label for="password">Password:</label><br />
										<input type="password" name="password" id="password" maxlength="75" style="width:100%"/>
									</p>
									<p align="right">
										<br />
										<input type="submit" value="E-mail Log On &raquo;" class="primButton" />
										<br /><br />
									</p>
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<table width="100%" border="0" cellpadding="0" cellspacing="0" class="notice">
							<tr>
								<td>
									This is a private system operated for Hewlett Packard company business.
									Authorization from HP management is required to use this system.The HP Standards
									of Business Conduct and all HP Information Security policies and standards must
									be strictly followed.Use by unauthorized persons is prohibited and may result in
									civil and /or criminal liablity and prosecution.
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</td>
	</tr>
</table>
</form>
</body>
</html>
