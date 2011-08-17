package com.hp.spp.portal.mockhppwebagent;

import com.hp.spp.db.DB;
import com.hp.spp.db.RowMapper;
import com.hp.spp.profile.Constants;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.URLEncoder;

public class MockHppWebAgentLoginServlet extends HttpServlet {
	static final String FAKE_SMSESSION_PREFIX = "MockHppWebAgentSMSESSION--";

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("USER");
		String hppId =
				DB.queryForObject("select hpp_id from users where user_name = ?",
						new RowMapper<String>() {
							public String mapRow(ResultSet row, int rowNum) throws SQLException {
								return row.getString(1);
							}
						},
						new String[] {username});

		if (hppId != null) {
			redirectToTargetPage(username, hppId, request, response);
		}
		else {
			redirectToLoginPage(request, response);
		}
	}

	private void redirectToLoginPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String targetUrl = request.getParameter("TARGET");
		RequestSiteInfo siteInfo = RequestSiteInfo.parse(targetUrl);
		if (siteInfo == null) {
			throw new ServletException("Unable to determine site from URL: " + targetUrl);
		}
/*
		StringBuilder redirectionTarget = new StringBuilder(request.getContextPath());
		redirectionTarget.append("/site/");
		if (!"console".equals(siteInfo.getProtectedSiteName())) {
			redirectionTarget.append("public");
		}
		redirectionTarget.append(siteInfo.getProtectedSiteName());
		redirectionTarget.append("/template.PRELOGIN/?").append(Constants.SMAUTHREASON).append("=0");
*/
		StringBuilder redirectionTarget = MockHppWebAgentFilter.getPreloginPageUri(request, siteInfo);
		redirectionTarget.append('?').append(Constants.SMAUTHREASON).append("=0");
		redirectionTarget.append("&TARGET=").append(URLEncoder.encode(targetUrl, "UTF-8"));
		response.sendRedirect(redirectionTarget.toString());
	}

	private void redirectToTargetPage(String username, String hppId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Cookie cookie = new Cookie(Constants.SMSESSION, FAKE_SMSESSION_PREFIX + new HppUserInfo(username, hppId).toString());
		cookie.setPath(request.getContextPath());
		response.addCookie(cookie);
		String redirectionTarget = request.getParameter("TARGET");
		response.sendRedirect(redirectionTarget);
	}
}
