package com.hp.spp.portal;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.hp.spp.config.Config;
import com.hp.spp.config.ConfigException;
import com.hp.spp.portal.common.helper.SiteURLHelper;
import com.hp.spp.portal.common.sql.PortalCommonDAO;
import com.hp.spp.portal.common.sql.PortalCommonDAOCacheImpl;
import com.hp.spp.profile.Constants;

public class AccessibilityFilter implements Filter {

	/**
	 * Logger.
	 */
	private static final Logger mLog = Logger.getLogger(AccessibilityFilter.class);

	private static final String keyDisasterPage = "SPP.disaster.page";

	/**
	 * Data Access Class.
	 */
	private PortalCommonDAO dao = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig arg0) throws ServletException {
		dao = PortalCommonDAOCacheImpl.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
		dao = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		doFilter((HttpServletRequest) request, (HttpServletResponse) response,
				chain);
	}

	/**
	 * Implementation of the doFilter for Http protocol.
	 * <p>
	 * 
	 * @param request
	 *            incoming request
	 * @param response
	 *            incoming response
	 * @param chain
	 *            fitler chain
	 * @throws IOException
	 *             If an exeption occurs duing the fitler chain processing; this
	 *             class doesn't throw any IOException
	 * @throws ServletException
	 *             If an exception occurs during the filter chain processing;
	 *             this class doesn't throw any ServletException
	 */
	public void doFilter(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// Get the site accessed
		String siteURI = SiteURLHelper.determineMasterSite(request);

		// Get accessibily of site
		boolean siteAccessible = true;
		if (!dao.isSiteAccessible(siteURI)) {
			siteAccessible = false;

			// Get Access Code
			String accessCode = dao.getAccessCode(siteURI);
			if (mLog.isDebugEnabled()) {
				mLog.debug("accessCodeParam : " + Constants.ACCESS_CODE);
				mLog.debug("accessCode : " + accessCode);
			}
			if (accessCode != null) {
				// Test with value in session
				HttpSession session = request.getSession(true);
				Object accessCodeSession = session.getAttribute(Constants.ACCESS_CODE);
				if (mLog.isDebugEnabled())
					mLog.debug("accessCodeSession : " + accessCodeSession);
				if (accessCode.equals(accessCodeSession))
					siteAccessible = true;

				// Get value from request parameter
				String accessCodeRequest = request.getParameter(Constants.ACCESS_CODE);
				if (mLog.isDebugEnabled())
					mLog.debug("accessCodeRequest : " + accessCodeRequest);

				if (accessCode.equals(accessCodeRequest)) {
					// put session attribute
					session.setAttribute(Constants.ACCESS_CODE, accessCode);
					siteAccessible = true;
				}
			}
		}

		// Redirect to disaster page
		if (!siteAccessible) {
			String disasterPage = null;
			try {
				disasterPage = Config.getValue(keyDisasterPage);
			} catch (ConfigException e) {
				String error = "ConfigException for key [" + disasterPage
						+ "]: " + e.getMessage();
				mLog.error(error);
				throw new IllegalStateException(error);
			}

			response.sendRedirect(disasterPage+"?siteName="+siteURI);
			
			return;
		}

		// pass the request on
		chain.doFilter(request, response);
	}
}
