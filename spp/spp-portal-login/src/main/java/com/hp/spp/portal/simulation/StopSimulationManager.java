package com.hp.spp.portal.simulation;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.hp.spp.portal.common.helper.SiteURLHelper;
import com.hp.spp.portal.common.util.CheckSession;
import com.hp.spp.portal.login.dao.LoginDAO;
import com.hp.spp.portal.login.dao.LoginDAOCacheImpl;
import com.hp.spp.portal.simulation.audittrail.AuditTrailManager;
import com.hp.spp.profile.Constants;
import com.vignette.portal.website.enduser.PortalContext;

/**
 * Manage the stop of the simulation.
 * 
 * @author mvidal@capgemini.fr
 * 
 */
public class StopSimulationManager {

	/**
	 * Logger.
	 */
	private static final Logger mLog = Logger.getLogger(StopSimulationManager.class);

	/**
	 * Portal context of the query.
	 */
	private PortalContext mPortalContext = null;

	public StopSimulationManager(PortalContext pc) {
		mPortalContext = pc;
	}

	public String unsimulate() {
		// retrieve simulator map
		if (mLog.isDebugEnabled())
			mLog.debug("retrieve simulator map");
		HttpServletRequest request = mPortalContext.getPortalRequest().getRequest();
		HttpSession session = request.getSession();
		Map simulatedProfile = (Map) session.getAttribute(Constants.PROFILE_MAP);
		Map simulatorProfile = (Map) simulatedProfile.get(Constants.MAP_SIMULATOR);
		if (mLog.isDebugEnabled())
			mLog.debug("unsimulate [" + simulatedProfile + "] for simulator ["
					+ simulatorProfile + "]");

		// retrieve access code for site unavailability
		Object accessCode = session.getAttribute(Constants.ACCESS_CODE);

		// remove everything from session
		if (mLog.isDebugEnabled())
			mLog.debug("remove everything from session");
		Enumeration enumeration = session.getAttributeNames();
		while (enumeration.hasMoreElements()) {
			String attributeName = (String) enumeration.nextElement();
			// Remove everything except the attribute containing the email of the user that has
			// been authenticated in HP LDAP - simulatino does not impact this login
			if (!attributeName.equals(Constants.HP_LDAP_AUTH_EMAIL)) {
				session.removeAttribute(attributeName);
			}
		}

		// put simulator map as profile map
		if (mLog.isDebugEnabled())
			mLog.debug("put simulator map as profile map");
		session.setAttribute(Constants.PROFILE_MAP, simulatorProfile);
		if (accessCode != null)
		session.setAttribute(Constants.ACCESS_CODE, accessCode);
		
		// persistence
		SimulationDAO simulationDAO = SimulationDAOSQLManagerImpl.getInstance();
		String site = (String) simulatorProfile.get(Constants.MAP_SITE);
		String hppIdSimulator = (String) simulatorProfile.get(Constants.MAP_HPPID);
		String hppIdSimulated = (String) simulatedProfile.get(Constants.MAP_HPPID);
		simulationDAO.removeSimulation(site, hppIdSimulator, hppIdSimulated);

		// audit trail
		AuditTrailManager.stopSimulation(hppIdSimulated, hppIdSimulator);
		// removing the simulated user entry from SPP_USERSESSION table.
		if(mLog.isDebugEnabled()){
			mLog.debug("Removing entry from table for "+hppIdSimulated+" "+session.getId());
		}
		CheckSession.removeSimulatedUserRowForCheckSession(session.getId(),hppIdSimulated);
		// redirection
		LoginDAO loginDAO = LoginDAOCacheImpl.getInstance();
		String pathStopSimulationPage = loginDAO.getPathStopSimulationPage(site);
		String urlHomePage = SiteURLHelper.getURLHomePage(request);
		mLog.info("site : " + site);
		mLog.info("pathStopSimulationPage : " + pathStopSimulationPage);
		mLog.info("urlHomePage : " + urlHomePage);
		String redirection = SiteURLHelper.getURLPage(site, pathStopSimulationPage, urlHomePage);
		return redirection;
	}

}
