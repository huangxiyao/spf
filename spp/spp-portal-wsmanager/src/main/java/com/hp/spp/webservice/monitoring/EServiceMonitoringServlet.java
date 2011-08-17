package com.hp.spp.webservice.monitoring;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.rpc.ServiceException;

import org.apache.log4j.Logger;

import com.hp.spp.config.Config;
import com.hp.spp.config.ConfigException;
import com.hp.spp.webservice.eservice.client.EServiceManagerWS;
import com.hp.spp.webservice.eservice.client.EServiceManagerWSService;
import com.hp.spp.webservice.eservice.client.EServiceManagerWSServiceLocator;
import com.hp.spp.webservice.eservice.client.EServiceResponse;
import com.hp.spp.webservice.eservice.client.EserviceRequest;
import com.hp.spp.webservice.eservice.manager.SPPEServiceWSManager;


/**
 * Class to check that EService WS are up.
 * Needs the creation of the EService 'SPP eService Monitoring - DO NOT DELETE' 
 * for site 'sppqa'
 * 
 * @author Mathieu Vidal
 *
 */
public class EServiceMonitoringServlet extends HttpServlet {

	private static Logger mLog = Logger.getLogger(EServiceMonitoringServlet.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (mLog.isDebugEnabled()) {
			mLog.debug("EServiceMonitoringServlet called");
		}

		String responseMsg = "ESERVICE WEB SERVICE KO";

		try {
			if (eServiceResponseOK() == true) {
				responseMsg = "ESERVICE WEB SERVICE OK";
			}
		} catch (Exception e) {
			mLog.error("Error calling the EService", e);
		}

		if (mLog.isDebugEnabled()) {
			mLog.debug("EServiceMonitoringServlet response message: "+responseMsg);
		}

		response.getWriter().print(responseMsg);
	}

	private boolean eServiceResponseOK() throws RemoteException, ServiceException, ConfigException, MalformedURLException {

		EServiceManagerWSService locator = new EServiceManagerWSServiceLocator();
		String eserviceURL = Config.getValue(SPPEServiceWSManager.mEserviceURL);

		EServiceManagerWS binding = locator.getEServiceManager(new URL(eserviceURL));

		EserviceRequest request = new EserviceRequest();
		request.setSiteName("sppqa");
		request.setEServiceName("SPP eService Monitoring - DO NOT DELETE");

		HashMap userContext = (new UserGroupMonitoringServlet()).getUserProfile();
		request.setUserContext(userContext);

		EServiceResponse eserviceResponse = binding.getEserviceInformation(request);

		return checkValues(eserviceResponse);
	}

	private boolean checkValues(EServiceResponse eserviceResponse) {

		String method = eserviceResponse.getMethod();
		if (method != null)
			return true;
		return false;
	}

}
