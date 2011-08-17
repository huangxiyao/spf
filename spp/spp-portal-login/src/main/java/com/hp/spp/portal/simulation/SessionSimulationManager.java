package com.hp.spp.portal.simulation;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.hp.spp.portal.common.helper.SiteURLHelper;
import com.hp.spp.portal.common.util.CheckSession;
import com.hp.spp.portal.login.business.preprocess.Localizer;
import com.hp.spp.portal.login.business.rule.UPSRulesProcessor;
import com.hp.spp.portal.login.dao.LoginDAOSQLManagerImpl;
import com.hp.spp.profile.Constants;
import com.hp.spp.webservice.ups.manager.SPPUserManager;

/**
 * Class which manage the simulation for the data in session.
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public class SessionSimulationManager {
	/**
	 * Logger.
	 */
	private static final Logger mLog = Logger
			.getLogger(SessionSimulationManager.class);

	/**
	 * Name of the site for the simulation.
	 */
	private String mSiteName = null;

	/**
	 * Hpp Id of the user simulated.
	 */
	private String mHppIdSimulated = null;

	/**
	 * SM Session token of the simulator.
	 */
	private String mPortalSessionId = null;

	/**
	 * Constructor.
	 * 
	 * @param siteName
	 *            Name of the site for the simulation.
	 * @param hppIdSimulated
	 *            Hpp Id of the user simulated.
	 * @param portalSessionId
	 *            Portal Session Id of the simulator.
	 */
	public SessionSimulationManager(String siteName, String hppIdSimulated,
			String portalSessionId) {
		mSiteName = siteName;
		mHppIdSimulated = hppIdSimulated;
		mPortalSessionId = portalSessionId;
	}

	Integer startSessionSimulation(HttpServletRequest request, boolean isSimulateFromLogin) {
		// retrieve simulator map
		if (mLog.isDebugEnabled()) {
			mLog.debug("retrieve simulator map");
		}
		HttpSession session = request.getSession();
		Map simulatorProfile = (Map) session
				.getAttribute(Constants.PROFILE_MAP);

		if (mLog.isDebugEnabled()) {
			mLog.debug("start simulation of user [" + mHppIdSimulated
					+ "] for simulator ["
					+ simulatorProfile.get(Constants.MAP_HPPID));
		}

		// retrieve access code for unavailalibity site
		Object accessCode = session.getAttribute(Constants.ACCESS_CODE);
		
		
//		 retrieve simulated profile from ups
		if (mLog.isDebugEnabled()) {
			mLog.debug("retrieve simulated profile from ups for user ["
					+ mHppIdSimulated + "] and site [" + mSiteName + "]");
		}

		SPPUserManager sppUserManager = new SPPUserManager();

		//(slawek) - I changed the call from getUserProfile to getUserProfileWithoutGroups
		//as UGS is normally called below in computePostUPSProfile.
		Map simulatedProfile = sppUserManager.getUserProfileWithoutGroups(
				mHppIdSimulated, mSiteName, true);

		// Add information related to simulation prior to calculating post UPS profile as the UGS
		// group definitions may rely on these profile attributes.
		//needs to be added here
		//Code to be added here.
		
		
		//Compute rule calculation based on the simulated profile, check error code
		String siteIdentifier = new Localizer().getSiteIdentifier(SiteURLHelper.determineMasterSite(request));
		Integer error = new UPSRulesProcessor().execute(request, simulatedProfile, true);
			
		if(error != null){
			handleRuleErrors(error,siteIdentifier,isSimulateFromLogin,simulatorProfile,simulatedProfile);
		}
		/*	int errorCode = error.intValue();
			
			// FIXME (girish) For future it is a good idea to merge the HPP workflow
			// and the rule based workflow into a single class which takes decision based purely
			// on error code values.
			if (errorCode == Constants.SPP_DISCLAIMER_SIMULATION_ERROR_CODE){
				//If this is a case of persist simulation and if we have a disclaimer error
				// only then remove the flag
				if (isSimulateFromLogin){
					mLog.info("Removing persist simulation for user");
					LoginDAOSQLManagerImpl loginDAOSQL = LoginDAOSQLManagerImpl.getInstance();
					loginDAOSQL.removePersistSimulation(siteIdentifier);
				}
				
				String errorMessage = "Error during simulation of user ["
					+ mHppIdSimulated + "]" + ",  user has not accepted consent in " +
					"  disclaimer Form ";
				throw new IllegalStateException(errorMessage); 
			}
		}*/

		// remove everything in session
		if (mLog.isDebugEnabled()) {
			mLog.debug("remove everything in session");
		}
		Enumeration enumeration = session.getAttributeNames();
		while (enumeration.hasMoreElements()) {
			String attributeName = (String) enumeration.nextElement();
			// Remove everything except the attribute containing the email of
			// the user that has
			// been authenticated in HP LDAP - simulatino does not impact this
			// login
			if (!attributeName.equals(Constants.HP_LDAP_AUTH_EMAIL)) {
				session.removeAttribute(attributeName);
			}
		}

		// check if portal session id corresponds
		if (mLog.isDebugEnabled()) {
			mLog.debug("check if portal session id corresponds");
		}
		String portalSessionId = simulatorProfile.get(
				Constants.MAP_PORTALSESSIONID).toString();
		if (!portalSessionId.equals(mPortalSessionId)) {
			String errorMessage = "Portal Session Id [" + mPortalSessionId
					+ "] passed different from Portal Session Id ["
					+ portalSessionId + "] in session ";
			throw new IllegalStateException(errorMessage);
		}
		
	
		//Adding entry to SPP_USERSESSION table for simulated user. (moved below after checking rules)
		
		
		//Adding entry to SPP_USERSESSION table for simulated user
		if(mLog.isDebugEnabled()){
			mLog.debug("adding row to checkSession for simulated user "+mHppIdSimulated+" "+mPortalSessionId);
		}
		CheckSession.addRowToCheckSession(mHppIdSimulated, mPortalSessionId);
	

		// add simulator to map
		if (mLog.isDebugEnabled()) {
			mLog.debug("add simulator to map");
		}
		simulatedProfile.put(Constants.MAP_SIMULATOR, simulatorProfile);

		// change value of IsSimulating
		simulatedProfile.put(Constants.MAP_IS_SIMULATING, "true");

		// compute profile
		if (mLog.isDebugEnabled()) {
			mLog.debug("compute profile");
		}
		String userId = (String) simulatedProfile.get(Constants.MAP_USERNAME);
		Integer errorCode = sppUserManager.computePostUPSProfile(request,
				userId, mHppIdSimulated, mSiteName, simulatedProfile);
		if (errorCode != null) {
			String errorMessage = "Error during simulation of user ["
					+ mHppIdSimulated + "in step computePostUPSProfile. "
					+ "Turn SPPUserManager log to DEBUG for more informations";
			throw new IllegalStateException(errorMessage);
		}

		// add missing attributes
		simulatedProfile.put(Constants.MAP_HOMEPAGE, simulatorProfile
				.get(Constants.MAP_HOMEPAGE));
		simulatedProfile.put(Constants.MAP_PORTALSESSIONID, simulatorProfile
				.get(Constants.MAP_PORTALSESSIONID));

		// put in session
		if (mLog.isDebugEnabled()) {
			mLog.debug("put in session");
		}
		sppUserManager.storeProfileInSession(request, simulatedProfile);

		if (accessCode != null) {
			session.setAttribute(Constants.ACCESS_CODE, accessCode);
		}

		return null;
	}
	
	private void handleRuleErrors(Integer error, String siteIdentifier, boolean isSimulateFromLogin, Map simulatorProfile, Map simulatedProfile){
			int errorCode = error.intValue();
			String errorMessage = "";
			// FIXME (girish) For future it is a good idea to merge the HPP workflow
			// and the rule based workflow into a single class which takes decision based purely
			// on error code values.
			if (errorCode == Constants.SPP_DISCLAIMER_SIMULATION_ERROR_CODE){
				errorMessage = "Error during simulation of user ["
					+ mHppIdSimulated + "]" + ",  user has not accepted consent in " +
					"  disclaimer Form ";
				
			}else if((errorCode == Constants.ERRORCODE_RULE_SPPINACTIVE) || (errorCode == Constants.ERRORCODE_RULE_UPSINACTIVE)){
				errorMessage = "Error during simulation of user ["
					+ mHppIdSimulated + "]" + ",  user is in inactive state";
			}else if(errorCode == Constants.ERRORCODE_RULE_UPSPENDING){
				errorMessage = "Error during simulation of user ["
					+ mHppIdSimulated + "]" + ",  user is in pending state";
			}
				//If this is a case of persist simulation and if we have a disclaimer error
				// only then remove the flag
			if (isSimulateFromLogin){
				mLog.info("Removing persist simulation for user");
				SimulationDAO simulationDAO = SimulationDAOSQLManagerImpl.getInstance();
				String site = (String) simulatorProfile.get(Constants.MAP_SITE);
				String hppIdSimulator = (String) simulatorProfile.get(Constants.MAP_HPPID);
				String hppIdSimulated = (String) simulatedProfile.get(Constants.MAP_HPPID);
				simulationDAO.removeSimulation(site, hppIdSimulator, hppIdSimulated);
			}
			throw new IllegalStateException(errorMessage);
	}
}
