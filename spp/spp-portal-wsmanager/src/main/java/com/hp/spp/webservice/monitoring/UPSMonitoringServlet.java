package com.hp.spp.webservice.monitoring;

import com.hp.globalops.hppcbl.passport.PassportService;
import com.hp.globalops.hppcbl.passport.PassportServiceException;
import com.hp.globalops.hppcbl.passport.beans.Fault;
import com.hp.spp.portal.common.site.SiteManager;
import com.hp.spp.portal.common.util.Site;
import com.hp.spp.portal.crypto.CryptoTools;
import com.hp.spp.portal.crypto.CryptoToolsException;
import com.hp.spp.portal.crypto.CryptoToolsImpl;
import com.hp.spp.profile.Constants;
import com.hp.spp.webservice.ups.manager.SPPUserManager;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Class to check that access to UPS is OK. Needs the creation of a specific
 * user for each site.
 * 
 * @author Mathieu Vidal
 * 
 */
public class UPSMonitoringServlet extends HttpServlet {

	private static Logger mLog = Logger.getLogger(UPSMonitoringServlet.class);

	//private static final String mUPSMonitoringLoginId = "SPP.ups.monitoring.servlet.login.";

	//private static final String mUPSMonitoringPwdId = "SPP.ups.monitoring.servlet.pwd.";
	
	private static final String keyForDecryption = "dUzLUvvpgFH4E4Z5Ue+RyHVMy1L76YBR";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String responseMsg = null;
		String siteName = request.getParameter("site");
		if (siteName != null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("UPSMonitoringServlet called for site '" + siteName + "'");
			}

			responseMsg = "UPS KO";

			int responseCode = serviceResponseOK(siteName);
			switch (responseCode) {
			case 0:
				responseMsg = "UPS KO";
				break;
			case 1:
				responseMsg = "UPS OK";
				break;
			case 2:
				responseMsg = "Incorrect site name";
				break;
			case 3:
				responseMsg = "No login/pwd defined for the site";
				break;
			case 4:
				responseMsg = "HPP KO";
				break;
			default:
				responseMsg = "UPS KO";
				break;
			}

			if (mLog.isDebugEnabled()) {
				mLog.debug("UPSMonitoringServlet response message: "
						+ responseMsg);
			}
		} else {
			responseMsg = "Missing parameter 'site' in the request";
		}

		response.getWriter().print(responseMsg);
	}
	
	private String decrypt(String encryptedPassword){
		CryptoTools cryptoTools = new CryptoToolsImpl(keyForDecryption);
		String clearPassword = null;
		try {
			clearPassword = cryptoTools.decrypt(encryptedPassword);
		} catch (CryptoToolsException e) {
			String error = "Error occured when decrypting the encrypted String using cryptoTools.decrypt";	
			mLog.error(error);
		}
		return clearPassword;
	}

	/*
	 * Following code for respective cases: 0: UPS KO 1: UPS OK 2: site not
	 * existing 3: no login/pwd existing for site 4: HPP KO
	 */
	private int serviceResponseOK(String siteName) {
		// check site name
		if (!Site.concernSite(siteName)) {
			mLog.error("site passed in request parameter is not defined");
			return 2;
		}

		// retrieve login/pwd of user for the site
		String login = null;
		String pwd = null;
        login = SiteManager.getInstance().getSite(siteName).getUPSMonitoringServletLogin();
        if(login == null){
            mLog.error("Login ID for UPSMonitoringServlet not available for site: " + siteName);
            return 3;
        }
        pwd = decrypt(SiteManager.getInstance().getSite(siteName).getUPSMonitoringServletPwd());
        if(pwd == null){
            mLog.error("Login Password for UPSMonitoringServlet not available for site: " + siteName);
            return 3;
        }


		// call HPP for hppid and smsession
		PassportService ws = new PassportService();
		String hppId = null;
		try {
			hppId = (ws.getProfileId(login)).getProfileId();
		} catch (PassportServiceException e) {
			String error = "PassportServiceException for Login [" + login + "]";
			mLog.error(error);
			List list = e.getFaults();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Fault fault = (Fault) iter.next();
				mLog.error(fault.getDescription());
			}
			return 4;
		}

		String sessionToken = null;
		try {
			sessionToken = (ws.login(login, pwd)).getSessionToken();
		} catch (PassportServiceException e) {
			String error = "PassportServiceException for Login [" + login
					+ "]: " + e.getMessage();
			mLog.error(error);
			List list = e.getFaults();
			for (Iterator iter = list.iterator(); iter.hasNext();) {
				Fault fault = (Fault) iter.next();
				mLog.error(fault.getDescription());
			}
			return 4;
		}

		// UPS call
		Map userProfile = new HashMap();
		Integer error = (new SPPUserManager())
				.constructQueryAndRetrieveProfileFromUPS(login, hppId,
						siteName, sessionToken, userProfile);
		
		// check that at least the language is present in user profile
		if (error != null || userProfile == null || userProfile.size() == 0
				|| userProfile.get(Constants.MAP_LANGUAGE) == null)
			return 0;

		return 1;
	}

}
