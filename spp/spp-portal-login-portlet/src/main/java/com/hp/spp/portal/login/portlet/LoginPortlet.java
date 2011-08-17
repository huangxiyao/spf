package com.hp.spp.portal.login.portlet;

import java.io.IOException;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import com.hp.spp.portal.login.dao.LoginDAO;
import com.hp.spp.portal.login.dao.LoginDAOCacheImpl;
import com.hp.spp.portal.common.sql.PortalCommonDAOCacheImpl;
import com.hp.spp.profile.Constants;
import com.hp.spp.config.Config;

public class LoginPortlet extends GenericPortlet {

	private static final Logger mLog = Logger.getLogger(LoginPortlet.class);

	/**
	 * @see javax.portlet.Portlet#init(javax.portlet.PortletConfig)
	 */
	public void init(PortletConfig config) throws PortletException {
		super.init(config);
	}

	/**
	 * Serve up the <code>view</code> mode.
	 * 
	 * @see javax.portlet.GenericPortlet#doView(javax.portlet.RenderRequest,
	 *      javax.portlet.RenderResponse)
	 */
	public void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {
		try {
			if (mLog.isDebugEnabled()) {
				mLog.debug("##### doView ######");
			}
			response.setContentType(request.getResponseContentType());

			LoginDAO loginDAO = LoginDAOCacheImpl.getInstance();

			String site = setSiteRequestAttribute(request);
			retrievePortletId(loginDAO, site);
			setHppIdRequestAttribute(loginDAO, site, request);
			setLabelsRequestAttributes(loginDAO, request, site);
			setTargetRequestAttribute(site, request);
			setLoginFccUriRequestAttribute(request);

			// Display view jsp
			String jspView = getPortletConfig().getInitParameter("jspView");
			if (mLog.isDebugEnabled()) {
				mLog.debug("jspView : " + jspView);
			}
			PortletRequestDispatcher dispatcher = getPortletContext().getRequestDispatcher(jspView);

			dispatcher.include(request, response);

			if (mLog.isDebugEnabled()) {
				mLog.debug("##### end of doView #####");
			}
		} catch (Exception e) {
			mLog.error("error : " + e.getMessage(), e);
		}
	}

	private void setLoginFccUriRequestAttribute(RenderRequest request) {
		String loginFccUri = Config.getValue("SPP.LoginFccUri", "/siteminderagent/forms/spp_login.fcc");
		//force URL to be in HTTPS unless the protcol is explicitely specified in the config variable
		if (loginFccUri.indexOf("://") == -1 && !request.isSecure()) {
			StringBuffer newUrlConfigFcc = new StringBuffer("https://").append(request.getServerName());
			int httpsPort = Config.getIntValue("SPP.port.https", 443);
			if (httpsPort != 443) {
				newUrlConfigFcc.append(':').append(httpsPort);
			}
			newUrlConfigFcc.append(loginFccUri);
			loginFccUri = newUrlConfigFcc.toString();
		}
		request.setAttribute("LOGIN_FORM_URI", loginFccUri);
	}

	private void setTargetRequestAttribute(String site, RenderRequest request) {
		String target = (String) request.getParameter(Constants.TARGET);
		if (target == null){
			String scheme = PortalCommonDAOCacheImpl.getInstance().getSiteProtocol(site);
			String serverName = request.getServerName();
			int port = Config.getIntValue("SPP.port." + scheme, -1);
			if ("https".equals(scheme) && port == 443) {
				port = -1;
			}
			else if ("http".equals(scheme) && port == 80) {
				port = -1;
			}
			String pathInfo = "/portal/site/";
			if ("console".equals(site)) {
				pathInfo = "/portal/";
			}
			target = scheme + "://"+ serverName + (port == -1 ? "" : ":" + port) + pathInfo + site + "/";
		}

		// add from login to target
		if (target.indexOf(Constants.FROMLOGIN + "=true") == -1){
			String separator = "?";
			if (target.indexOf('?') > -1) {
				separator = "&";
			}
			target = target + separator + Constants.FROMLOGIN + "=true";
		}
		request.setAttribute(Constants.TARGET, target);
	}

	private void retrievePortletId(LoginDAO loginDAO, String site) {
		//FIXME (slawek) - I'm not clear why this is needed. Leaving it for now to not cumulated
		// issues that might be related to the refactoring.
		String portletId = loginDAO.getPortletId(site);
		if (mLog.isDebugEnabled()) {
			mLog.debug("portletId : " + portletId);
		}
	}

	private void setHppIdRequestAttribute(LoginDAO loginDAO, String site, RenderRequest request) {
		String hpappid = loginDAO.getHpappid(site);
		request.setAttribute(Constants.HPAPPID, hpappid);
		if (mLog.isDebugEnabled()) {
			mLog.debug("hpappid : " + hpappid);
		}
	}

	private void setLabelsRequestAttributes(LoginDAO loginDAO, RenderRequest request, String siteName) {
		String localeCode = (String) request
				.getAttribute(Constants.PREFIX_VGN_LOGINPORTLET + Constants.MAP_LANGUAGE);
		if (mLog.isDebugEnabled()) {
			mLog.debug("localeCode : " + localeCode);
		}

		// localized labels

		request.setAttribute(Constants.USER_ID, getLabelValue(loginDAO, Constants.USER_ID, localeCode, siteName));

		request.setAttribute(Constants.PASSWORD, getLabelValue(loginDAO, Constants.PASSWORD, localeCode, siteName));

		request.setAttribute(Constants.SUBTITLE, getLabelValue(loginDAO, Constants.SUBTITLE, localeCode, siteName));

		request.setAttribute(Constants.SUBMIT, getLabelValue(loginDAO, Constants.SUBMIT, localeCode, siteName));

		request.setAttribute(Constants.WAIT_MESSAGE, getLabelValue(loginDAO, Constants.WAIT_MESSAGE, localeCode, siteName));
	}

	private String setSiteRequestAttribute(RenderRequest request) {
		// retrieve minimal data
		String site = (String) request.getAttribute(Constants.PREFIX_VGN_LOGINPORTLET
				+ Constants.MAP_SITE);
		// info to retrieve from site name without public
		if (site.startsWith("public")) {
			site = site.replaceFirst("public", "");
		}

		request.setAttribute(Constants.MAP_SITE, site);
		if (mLog.isDebugEnabled()) {
			mLog.debug("site : " + site);
		}
		return site;
	}

    private String getLabelValue(LoginDAO loginDAO, String label, String localeCode, String siteName){
        String labelValue;

        labelValue = loginDAO.getMessageFromLabel(label, localeCode, siteName);

        if (labelValue == null) {
			labelValue = loginDAO.getMessageFromLabel(label, localeCode);
		}
        //Default case for english
        if(labelValue == null){
           labelValue = loginDAO.getMessageFromLabel(label, Constants.DEFAULT_LANGUAGE.substring(0,2));
        }

        if (mLog.isDebugEnabled()) {
			mLog.debug(label+ " : " + labelValue);
        }
        return labelValue;
    }

}
