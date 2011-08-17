package com.hp.spp.portal.simulation;

import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.hp.spp.portal.crypto.CryptoToolsException;
import com.hp.spp.portal.crypto.CryptoToolsImpl;
import com.hp.spp.portal.simulate.bean.AbstractSimulateBean;
import com.hp.spp.portal.simulate.bean.ISimulateBean;
import com.hp.spp.portal.simulation.audittrail.AuditTrailManager;
import com.hp.spp.profile.Constants;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * Class which manage the simulation.
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public class StartSimulationManager {
	/**
	 * Logger.
	 */
	private static final Logger mLog = Logger
			.getLogger(StartSimulationManager.class);

	/**
	 * Request for starting the simulation.
	 */
	private HttpServletRequest mRequest = null;

	/**
	 * Site of the simulation.
	 */
	private String mSite = null;

	/**
	 * HPP id of the simulator.
	 */
	private String mHppIdSimulator = null;

	/**
	 * HPP id of the simulated user.
	 */
	private String mHppIdSimulated = null;

	/**
	 * Portal Session Id of the simulator.
	 */
	private String mPortalSessionId = null;

	/**
	 * URL to redirect in case of success.
	 */
	private String mUrlSuccess = null;

	/**
	 * URL to redirect in case of error.
	 */
	private String mUrlError = null;

	/**
	 * Secret Key.
	 */
	private static final String mSECRETKEY = "Pmjq3PcyPb+UyB8CE/2/eT5o6tz3Mj2/";

	/**
	 * Empty constructor.
	 */
	public StartSimulationManager() {
	}

	/**
	 * Constructor. Retrieve the parameters from the request in the portal
	 * context.
	 * 
	 * @param pc
	 *            PortalContext the portal context.
	 */
	public StartSimulationManager(PortalContext pc) {
		// request
		mRequest = pc.getPortalRequest().getRequest();

		// site
		mSite = mRequest.getParameter(ISimulateBean.SPP_SiteName);
		if (mLog.isDebugEnabled()) {
			mLog.debug("mSite : [" + mSite + "]");
		}

		// hppId simulated and portal session id
		String keyEncoded = mRequest
				.getParameter(ISimulateBean.SPP_SimulationKey);
		if (mLog.isDebugEnabled()) {
			mLog.debug("keyEncoded : [" + keyEncoded + "]");
		}
		String key = null;
		try {
			key = (new CryptoToolsImpl(mSECRETKEY)).decrypt(keyEncoded);
		} catch (CryptoToolsException e) {
			mLog.error("Exception during decryption of encoded keyil  : "
					+ e.getMessage(), e);
		}
		if (mLog.isDebugEnabled()) {
			mLog.debug("key : [" + key + "]");
		}

		StringTokenizer stringTokenizer = new StringTokenizer(key,
				AbstractSimulateBean.ENCRYPTIONSEPARATOR);

		mHppIdSimulated = stringTokenizer.nextToken();
		if (mLog.isDebugEnabled()) {
			mLog.debug("mHppIdSimulated : [" + mHppIdSimulated + "]");
		}
		mPortalSessionId = stringTokenizer.nextToken();
		if (mLog.isDebugEnabled()) {
			mLog.debug("mPortalSessionId : [" + mPortalSessionId + "]");
		}

		// hppId simulator
		HttpSession session = mRequest.getSession();
		Map simulatorProfile = (Map) session
				.getAttribute(Constants.PROFILE_MAP);
		mHppIdSimulator = (String) simulatorProfile.get(Constants.MAP_HPPID);

		// hppId simulator
		String urlHomePage = (String) simulatorProfile
				.get(Constants.MAP_HOMEPAGE);

		// url success and error
		mUrlSuccess = urlHomePage + "?page="
				+ mRequest.getParameter(ISimulateBean.SPP_RedirectUrl);
		if (mLog.isDebugEnabled()) {
			mLog.debug("mUrlSuccess : [" + mUrlSuccess + "]");
		}
		mUrlError = urlHomePage + "?page="
				+ mRequest.getParameter(ISimulateBean.SPP_ErrorUrl) + "Simulation";
		if (mLog.isDebugEnabled()) {
			mLog.debug("mUrlError : [" + mUrlError + "]");
		}

	}

	/**
	 * Main class.
	 * 
	 * @return
	 */
	public String simulate() {

		try {
			// update profile
			SessionSimulationManager sessionSimulationManager = new SessionSimulationManager(
					mSite, mHppIdSimulated, mPortalSessionId);
			sessionSimulationManager.startSessionSimulation(mRequest, false);

			// simulation persistence
			SimulationDAO simulationDAO = SimulationDAOSQLManagerImpl
					.getInstance();
			simulationDAO.persistSimulation(mSite, mHppIdSimulator,
					mHppIdSimulated);

			// audit trail
			AuditTrailManager.startSimulation(mHppIdSimulated, mHppIdSimulator);
		} catch (Exception e) {
			String errorMessage = e.getMessage();
			mLog.error(errorMessage);
			return mUrlError + "&ERROR=" + errorMessage;
		}

		return mUrlSuccess;
	}

	public String simulateFromLogin(String site, HttpServletRequest request) {
		// No simulation persisted
		HttpSession session = request.getSession();
		Map profile = (Map) session.getAttribute(Constants.PROFILE_MAP);
		String hppIdSimulator = (String) profile.get(Constants.MAP_HPPID);
		SimulationDAO simulationDAO = SimulationDAOSQLManagerImpl.getInstance();
		String hppidSimulated = simulationDAO.selectSimulated(site,
				(String) profile.get(Constants.MAP_HPPID));
		if (hppidSimulated == null) {
			if (mLog.isDebugEnabled()) {
				mLog.debug("no hppid found to simulate after login");
			}
			return null;
		}

		// update profile
		String portalSessionId = profile.get(Constants.MAP_PORTALSESSIONID)
				.toString();
		SessionSimulationManager sessionSimulationManager = new SessionSimulationManager(
				site, hppidSimulated, portalSessionId);
		try{
			sessionSimulationManager.startSessionSimulation(request, true);
			
		}catch (Exception e) {
			String errorMessage = e.getMessage();
			mLog.error(errorMessage);
			mUrlError =  mUrlError + "&ERROR=" + errorMessage;
			return mUrlError;
		}

		// audit trail
		AuditTrailManager.startSimulationFromLogin(hppidSimulated,
				hppIdSimulator);
		return null;
	}
}
